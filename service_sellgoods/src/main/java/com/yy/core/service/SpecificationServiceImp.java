package com.yy.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yy.core.dao.specification.SpecificationDao;
import com.yy.core.dao.specification.SpecificationOptionDao;
import com.yy.core.pojo.entity.PageResult;
import com.yy.core.pojo.entity.SpeciEntity;
import com.yy.core.pojo.specification.Specification;
import com.yy.core.pojo.specification.SpecificationOption;
import com.yy.core.pojo.specification.SpecificationOptionQuery;
import com.yy.core.pojo.specification.SpecificationQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SpecificationServiceImp implements SpecificationService {

	@Autowired
	private SpecificationDao specificationDao;

	@Autowired
	private SpecificationOptionDao specificationOptionDao;

	@Override
	public PageResult searchSpeci(Specification specification, int page, int rows) {
		PageHelper.startPage(page,rows);
		SpecificationQuery query = new SpecificationQuery();
		SpecificationQuery.Criteria criteria = query.createCriteria();
		if (specification.getSpecName() != null && specification.getSpecName().length() > 0){
			criteria.andSpecNameLike("%" + specification.getSpecName() + "%");
		}
		Page<Specification> page1 = (Page<Specification>)specificationDao.selectByExample(query);
		return new PageResult(page1.getTotal(),page1.getResult());
	}

	@Override
	public void saveSpeci(SpeciEntity speciEntity) {
		specificationDao.insert(speciEntity.getSpecification());
		if (speciEntity.getSpecificationOptionList() != null){
			for (SpecificationOption option : speciEntity.getSpecificationOptionList()){
				option.setSpecId(speciEntity.getSpecification().getId());
				specificationOptionDao.insert(option);
			}
		}
	}

	@Override
	public SpeciEntity findSpeci(Long id) {
		Specification specification = specificationDao.selectByPrimaryKey(id);
		SpeciEntity speciEntity = new SpeciEntity();
		speciEntity.setSpecification(specification);
		SpecificationOptionQuery query = new SpecificationOptionQuery();
		SpecificationOptionQuery.Criteria criteria = query.createCriteria();
		criteria.andSpecIdEqualTo(id);
		List<SpecificationOption> specificationOptions = specificationOptionDao.selectByExample(query);
		speciEntity.setSpecificationOptionList(specificationOptions);
		return speciEntity;
	}

	@Override
	public void updateSpeci(SpeciEntity speciEntity) {
		specificationDao.updateByPrimaryKeySelective(speciEntity.getSpecification());
		SpecificationOptionQuery query = new SpecificationOptionQuery();
		SpecificationOptionQuery.Criteria criteria = query.createCriteria();
		criteria.andSpecIdEqualTo(speciEntity.getSpecification().getId());
		specificationOptionDao.deleteByExample(query);

		if (speciEntity.getSpecificationOptionList() != null){
			for (SpecificationOption option : speciEntity.getSpecificationOptionList()){
				option.setSpecId(speciEntity.getSpecification().getId());
				specificationOptionDao.insert(option);
			}
		}
	}

	@Override
	public void deleteSpeci(Long[] ids) {
		for (Long id : ids){
			specificationDao.deleteByPrimaryKey(id);
			SpecificationOptionQuery query = new SpecificationOptionQuery();
			SpecificationOptionQuery.Criteria criteria = query.createCriteria();
			criteria.andSpecIdEqualTo(id);
			specificationOptionDao.deleteByExample(query);
		}
	}

	@Override
	public List<Map> selectOptionList() {
		return specificationDao.selectOptionList();
	}

}
