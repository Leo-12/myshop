package com.yy.core.service;

import com.yy.core.pojo.entity.PageResult;
import com.yy.core.pojo.template.TypeTemplate;

import java.util.List;
import java.util.Map;

public interface TypeTemplateService {
	PageResult findPage(TypeTemplate typeTemplate, int page, int rows);

	void addTypeTemplate(TypeTemplate typeTemplate);

	TypeTemplate findTypeTemplate(Long id);

	void updateTypeTemplate(TypeTemplate typeTemplate);

	void deleteTypeTemplate(Long[] ids);

	List<Map> selectOptionList();

	List<Map> findSpecList(Long id);
}
