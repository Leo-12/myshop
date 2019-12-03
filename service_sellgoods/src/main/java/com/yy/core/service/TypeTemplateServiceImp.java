package com.yy.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yy.core.dao.specification.SpecificationOptionDao;
import com.yy.core.dao.template.TypeTemplateDao;
import com.yy.core.pojo.entity.PageResult;
import com.yy.core.pojo.specification.SpecificationOption;
import com.yy.core.pojo.specification.SpecificationOptionQuery;
import com.yy.core.pojo.template.TypeTemplate;
import com.yy.core.pojo.template.TypeTemplateQuery;
import com.yy.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;

@Service
public class TypeTemplateServiceImp implements TypeTemplateService {

	@Autowired
	private TypeTemplateDao typeTemplateDao;

	@Autowired
	private SpecificationOptionDao specificationOptionDao;

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public PageResult findPage(TypeTemplate typeTemplate, int page, int rows) {
		//redis中缓存模板的所有数据
		List<TypeTemplate> typeTemplateList = typeTemplateDao.selectByExample(null);
		//模板id作为键，品牌集合作为value
		for (TypeTemplate template : typeTemplateList){
			String brandIdsJsonStr = template.getBrandIds();
			//将json转成集合存进去
			List<Map> brandList = JSON.parseArray(brandIdsJsonStr, Map.class);
			redisTemplate.boundHashOps(Constants.BRAND_LIST_REDIS).put(template.getId(),brandList);
			List<Map> specList = findSpecList(template.getId());
			redisTemplate.boundHashOps(Constants.SPEC_LIST_REDIS).put(template.getId(),specList);
		}

		//模板分页
		PageHelper.startPage(page,rows);
		TypeTemplateQuery query = new TypeTemplateQuery();
		TypeTemplateQuery.Criteria criteria = query.createCriteria();
		if (typeTemplate != null){
			if (typeTemplate.getName() != null && typeTemplate.getName().length() > 0){
				criteria.andNameLike("%" + typeTemplate.getName() + "%");
			}
		}
		Page<TypeTemplate> page1 = (Page<TypeTemplate>)typeTemplateDao.selectByExample(query);
		return new PageResult(page1.getTotal(),page1.getResult());
	}

	@Override
	public void addTypeTemplate(TypeTemplate typeTemplate) {
		typeTemplateDao.insert(typeTemplate);
	}

	@Override
	public TypeTemplate findTypeTemplate(Long id) {
		return typeTemplateDao.selectByPrimaryKey(id);
	}

	@Override
	public void updateTypeTemplate(TypeTemplate typeTemplate) {
		typeTemplateDao.updateByPrimaryKeySelective(typeTemplate);
	}

	@Override
	public void deleteTypeTemplate(Long[] ids) {
		for (Long id : ids){
			typeTemplateDao.deleteByPrimaryKey(id);
		}
	}

	@Override
	public List<Map> selectOptionList() {
		return typeTemplateDao.selectOptionList();
	}

	@Override
	public List<Map> findSpecList(Long id) {
		TypeTemplate typeTemplate = typeTemplateDao.selectByPrimaryKey(id);
		String specIds = typeTemplate.getSpecIds();
		List<Map> list = JSON.parseArray(specIds,Map.class);
		if (list != null){
			for(Map map : list){
				SpecificationOptionQuery query = new SpecificationOptionQuery();
				SpecificationOptionQuery.Criteria criteria = query.createCriteria();
				criteria.andSpecIdEqualTo(new Long((Integer)map.get("id")));
				List<SpecificationOption> options = specificationOptionDao.selectByExample(query);
				map.put("options",options);
			}
		}
		return list;
	}
}
