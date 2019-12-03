package com.yy.core.service;

import com.yy.core.pojo.entity.PageResult;
import com.yy.core.pojo.good.Brand;

import java.util.List;
import java.util.Map;

public interface BrandService {
	List<Brand> showBrandList();

	PageResult findPage(int pageNum, int pageSize);

	void addBrand(Brand brand);

	Brand getBrandById(Long id);

	void updateBrand(Brand brand);

	PageResult findPage(Brand brand, int pageNum, int pageSize);

	void deleteBrand(Long [] ids);

	List<Map> selectOptionList();
}
