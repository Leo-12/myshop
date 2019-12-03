package com.yy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yy.core.pojo.template.TypeTemplate;
import com.yy.core.service.TypeTemplateService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {

	@Reference
	private TypeTemplateService typeTemplateService;

	@RequestMapping("/findOne")
	public TypeTemplate findTypeTemplate(Long id){
		return typeTemplateService.findTypeTemplate(id);
	}

	@RequestMapping("/findBySpecList")
	public List<Map> findSpecList(Long id){
		return typeTemplateService.findSpecList(id);
	}

}
