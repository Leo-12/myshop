package com.yy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yy.core.pojo.entity.PageResult;
import com.yy.core.pojo.entity.Result;
import com.yy.core.pojo.template.TypeTemplate;
import com.yy.core.service.TypeTemplateService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {

	@Reference
	private TypeTemplateService typeTemplateService;

	@RequestMapping("/search")
	public PageResult findPage(@RequestBody TypeTemplate typeTemplate, int page, int rows){
		return typeTemplateService.findPage(typeTemplate,page,rows);
	}

	@RequestMapping("/add")
	public Result addTypeTemplate(@RequestBody TypeTemplate typeTemplate){
		try {
			typeTemplateService.addTypeTemplate(typeTemplate);
			return new Result(true,"添加成功");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"添加失败");
		}
	}

	@RequestMapping("/findOne")
	public TypeTemplate findTypeTemplate(Long id){
		return typeTemplateService.findTypeTemplate(id);
	}

	@RequestMapping("/update")
	public Result updateTypeTemplate(@RequestBody TypeTemplate typeTemplate){
		try {
			typeTemplateService.updateTypeTemplate(typeTemplate);
			return new Result(true,"添加成功");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"添加失败");
		}
	}

	@RequestMapping("/delete")
	public Result deleteTypeTemplate(Long[] ids){
		try {
			typeTemplateService.deleteTypeTemplate(ids);
			return new Result(true,"添加成功");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"添加失败");
		}
	}

	@RequestMapping("/selectOptionList")
	public List<Map> selectOptionList(){
		return typeTemplateService.selectOptionList();
	}

}
