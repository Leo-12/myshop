package com.yy.core.service;

import com.yy.core.pojo.entity.PageResult;
import com.yy.core.pojo.entity.SpeciEntity;
import com.yy.core.pojo.specification.Specification;

import java.util.List;
import java.util.Map;

public interface SpecificationService {

	PageResult searchSpeci(Specification specification, int page, int rows);

	void saveSpeci(SpeciEntity speciEntity);

	SpeciEntity findSpeci(Long id);

	void updateSpeci(SpeciEntity speciEntity);

	void deleteSpeci(Long[] ids);

	List<Map> selectOptionList();
}
