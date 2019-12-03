package com.yy.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yy.core.dao.good.BrandDao;
import com.yy.core.dao.good.GoodsDao;
import com.yy.core.dao.good.GoodsDescDao;
import com.yy.core.dao.item.ItemCatDao;
import com.yy.core.dao.item.ItemDao;
import com.yy.core.dao.seller.SellerDao;
import com.yy.core.pojo.entity.GoodsEntiey;
import com.yy.core.pojo.entity.PageResult;
import com.yy.core.pojo.good.Brand;
import com.yy.core.pojo.good.Goods;
import com.yy.core.pojo.good.GoodsDesc;
import com.yy.core.pojo.good.GoodsQuery;
import com.yy.core.pojo.item.Item;
import com.yy.core.pojo.item.ItemCat;
import com.yy.core.pojo.item.ItemQuery;
import com.yy.core.pojo.seller.Seller;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class GoodsServiceImp implements GoodsService {

	@Autowired
	private GoodsDescDao goodsDescDao;

	@Autowired
	private GoodsDao goodsDao;

	@Autowired
	private ItemDao itemDao;

	@Autowired
	private BrandDao brandDao;

	@Autowired
	private ItemCatDao itemCatDao;

	@Autowired
	private SellerDao sellerDao;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private ActiveMQTopic topicPageAndSolrDestination;
	@Autowired
	private ActiveMQQueue queueSolrDeleteDestination;

	@Override
	public void add(GoodsEntiey goodsEntiey) {
		goodsEntiey.getGoods().setAuditStatus("0"); //设置未申请状态
		goodsDao.insert(goodsEntiey.getGoods());
		goodsEntiey.getGoodsDesc().setGoodsId(goodsEntiey.getGoods().getId()); //设置ID
		goodsDescDao.insert(goodsEntiey.getGoodsDesc()); //插入商品扩展数据
		saveItemList(goodsEntiey);
	}

	@Override
	public PageResult findPage(Goods goods, int page, int rows) {
		PageHelper.startPage(page,rows);
		GoodsQuery query = new GoodsQuery();
		GoodsQuery.Criteria criteria = query.createCriteria();
		criteria.andIsDeleteIsNull();
		if (goods != null){
			if (goods.getAuditStatus() != null && goods.getAuditStatus().length() > 0){
				criteria.andAuditStatusEqualTo(goods.getAuditStatus());
			}
			if (goods.getGoodsName() != null && goods.getGoodsName().length() > 0){
				criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");
			}
			if (goods.getSellerId() != null && goods.getSellerId().length() > 0){
				criteria.andSellerIdEqualTo(goods.getSellerId());
			}
		}
		Page<Goods> page1 = (Page<Goods>)goodsDao.selectByExample(query);
		return new PageResult(page1.getTotal(),page1.getResult());
	}

	@Override
	public GoodsEntiey findOne(Long id) {
		GoodsEntiey goodsEntiey = new GoodsEntiey();
		Goods goods = goodsDao.selectByPrimaryKey(id);
		goodsEntiey.setGoods(goods);
		GoodsDesc goodsDesc = goodsDescDao.selectByPrimaryKey(id);
		goodsEntiey.setGoodsDesc(goodsDesc);

		ItemQuery query = new ItemQuery();
		ItemQuery.Criteria criteria = query.createCriteria();
		criteria.andGoodsIdEqualTo(id);
		List<Item> itemList = itemDao.selectByExample(query);
		goodsEntiey.setItemList(itemList);
		return goodsEntiey;
	}

	@Override
	public void update(GoodsEntiey goodsEntiey) {
		goodsEntiey.getGoods().setAuditStatus("0");
		goodsDao.updateByPrimaryKey(goodsEntiey.getGoods());
		goodsDescDao.updateByPrimaryKey(goodsEntiey.getGoodsDesc());

		ItemQuery query = new ItemQuery();
		ItemQuery.Criteria criteria = query.createCriteria();
		criteria.andGoodsIdEqualTo(goodsEntiey.getGoods().getId());
		itemDao.deleteByExample(query);
		saveItemList(goodsEntiey);
	}

	@Override
	public void delete(Long id) {
		Goods goods = new Goods();
		goods.setId(id);
		goods.setIsDelete("1");
		goodsDao.updateByPrimaryKeySelective(goods);

		//将商品的id作为消息发送给消息服务器
		jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(String.valueOf(id));
				return textMessage;
			}
		});
	}

	@Override
	public void updateStatus(Long id, String status) {
		//修改商品的状态码
		Goods goods = new Goods();
		goods.setId(id);
		goods.setAuditStatus(status);
		goodsDao.updateByPrimaryKeySelective(goods);
		//修改库存对象的状态码
		Item item = new Item();
		item.setStatus(status);
		ItemQuery query = new ItemQuery();
		ItemQuery.Criteria criteria = query.createCriteria();
		criteria.andGoodsIdEqualTo(id);
		itemDao.updateByExampleSelective(item,query);

		//将商品id作为消息发送给消息服务器
		jmsTemplate.send(topicPageAndSolrDestination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(String.valueOf(id));
				return textMessage;
			}
		});
	}

	@Override
	public void updateIsMarketable(Long[] ids, String isMarketable) {
		for (Long id : ids){
			Goods goods = goodsDao.selectByPrimaryKey(id);
			if ("1".equals(goods.getAuditStatus())){
				goods.setIsMarketable(isMarketable);
				goodsDao.updateByPrimaryKeySelective(goods);
			}else{
				throw new RuntimeException("只有审核通过的商品才能上下架");
			}
		}
	}

	private void saveItemList(GoodsEntiey goodsEntiey){
		if ("1".equals(goodsEntiey.getGoods().getIsEnableSpec())){
			if (goodsEntiey.getItemList() != null){
				for (Item item : goodsEntiey.getItemList()) {
					String title = goodsEntiey.getGoods().getGoodsName();//标题
					Map<String, Object> specMap = JSON.parseObject(item.getSpec());
					for (String key : specMap.keySet()) {
						title += " " + specMap.get(key);
					}
					item.setTitle(title);
					setItemValus(goodsEntiey, item);
					itemDao.insertSelective(item);
				}
			}
		}else{
			Item item = new Item();
			item.setTitle(goodsEntiey.getGoods().getGoodsName());
			item.setPrice(goodsEntiey.getGoods().getPrice());
			item.setStatus("1"); //状态
			item.setIsDefault("1"); //是否默认
			item.setNum(9999); //库存
			item.setSpec("{}");
			setItemValus(goodsEntiey,item);
			itemDao.insertSelective(item);
		}
	}

	private void setItemValus(GoodsEntiey goodsEntiey, Item item) {
		item.setGoodsId(goodsEntiey.getGoods().getId()); //商品spu编号
		item.setSellerId(goodsEntiey.getGoods().getSellerId()); //商家编号
		item.setCategoryid(goodsEntiey.getGoods().getCategory3Id()); //商品分类编号
		//item.setCreateTime(new Date());
		item.setUpdateTime(new Date());
		Brand brand = brandDao.selectByPrimaryKey(goodsEntiey.getGoods().getBrandId()); //品牌名称
		item.setBrand(brand.getName());
		ItemCat itemCat = itemCatDao.selectByPrimaryKey(goodsEntiey.getGoods().getCategory3Id()); //分类名称
		item.setCategory(itemCat.getName());
		Seller seller = sellerDao.selectByPrimaryKey(goodsEntiey.getGoods().getSellerId()); //商家名称
		item.setSeller(seller.getNickName());
		//图片地址(取spu的第一个图片)
		List<Map> imageList = JSON.parseArray(goodsEntiey.getGoodsDesc().getItemImages(),Map.class);
		if (imageList.size() > 0){
			item.setImage((String)imageList.get(0).get("url"));
		}
	}
}
