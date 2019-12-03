package com.yy.core.pojo.entity;

import com.yy.core.pojo.specification.Specification;
import com.yy.core.pojo.specification.SpecificationOption;

import java.io.Serializable;
import java.util.List;

public class SpeciEntity implements Serializable {

	private Specification specification;

	private List<SpecificationOption> specificationOptionList;

	public SpeciEntity(Specification specification, List<SpecificationOption> specificationOptionList) {
		this.specification = specification;
		this.specificationOptionList = specificationOptionList;
	}

	public SpeciEntity() {
	}

	public Specification getSpecification() {
		return specification;
	}

	public void setSpecification(Specification specification) {
		this.specification = specification;
	}

	public List<SpecificationOption> getSpecificationOptionList() {
		return specificationOptionList;
	}

	public void setSpecificationOptionList(List<SpecificationOption> specificationOptionList) {
		this.specificationOptionList = specificationOptionList;
	}
}
