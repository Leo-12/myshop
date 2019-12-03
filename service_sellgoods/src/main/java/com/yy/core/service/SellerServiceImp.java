package com.yy.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yy.core.dao.seller.SellerDao;
import com.yy.core.pojo.entity.PageResult;
import com.yy.core.pojo.seller.Seller;
import com.yy.core.pojo.seller.SellerQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class SellerServiceImp implements SellerService {

	@Autowired
	private SellerDao sellerDao;

	@Override
	public void addSeller(Seller seller) {
		seller.setCreateTime(new Date());
		sellerDao.insertSelective(seller);
	}

	@Override
	public PageResult searchSeller(Seller seller, int page, int rows) {
		PageHelper.startPage(page,rows);
		SellerQuery query = new SellerQuery();
		SellerQuery.Criteria criteria = query.createCriteria();
		criteria.andStatusEqualTo(seller.getStatus());
		if (seller != null){
			if (seller.getName() != null && seller.getName().length() > 0){
				criteria.andNameLike("%" + seller.getName() + "%");
			}
			if (seller.getNickName() != null && seller.getNickName().length() > 0){
				criteria.andNickNameLike("%" + seller.getNickName() + "%");
			}
		}
		Page<Seller> page1 = (Page<Seller>)sellerDao.selectByExample(query);
		return new PageResult(page1.getTotal(),page1.getResult());
	}

	@Override
	public Seller findSeller(String sellerId) {
		return sellerDao.selectByPrimaryKey(sellerId);
	}

	@Override
	public void updateStatus(String sellerId, String status) {
		Seller seller = sellerDao.selectByPrimaryKey(sellerId);
		seller.setStatus(status);
		sellerDao.updateByPrimaryKeySelective(seller);
	}
}
