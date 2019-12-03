package com.yy.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yy.core.dao.good.BrandDao;
import com.yy.core.pojo.entity.PageResult;
import com.yy.core.pojo.good.Brand;
import com.yy.core.pojo.good.BrandQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImp implements BrandService {

	@Autowired
	private BrandDao brandDao;

	@Override
	public List<Brand> showBrandList() {
		return brandDao.selectByExample(null);
	}

	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum,pageSize);
		Page<Brand> page = (Page<Brand>)brandDao.selectByExample(null);
		return new PageResult(page.getTotal(),page.getResult());
	}

	@Override
	public void addBrand(Brand brand) {
		brandDao.insert(brand);
	}

	@Override
	public Brand getBrandById(Long id) {
		return brandDao.selectByPrimaryKey(id);
	}

	@Override
	public void updateBrand(Brand brand) {
		brandDao.updateByPrimaryKey(brand);
	}

	@Override
	public PageResult findPage(Brand brand, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum,pageSize);
		BrandQuery query = new BrandQuery();
		BrandQuery.Criteria criteria = query.createCriteria();
		if (brand != null){
			if (brand.getName() != null && brand.getName().length() > 0){
				criteria.andNameLike("%" + brand.getName() + "%");
			}
			if (brand.getFirstChar() != null && brand.getFirstChar().length() > 0){
				criteria.andFirstCharEqualTo(brand.getFirstChar());
			}
		}
		Page<Brand> page = (Page<Brand>)brandDao.selectByExample(query);
		return new PageResult(page.getTotal(),page.getResult());
	}

	@Override
	public void deleteBrand(Long[] ids) {
		for (Long id : ids){
			brandDao.deleteByPrimaryKey(id);
		}
	}

	@Override
	public List<Map> selectOptionList() {
		return brandDao.selectOptionList();
	}
}
