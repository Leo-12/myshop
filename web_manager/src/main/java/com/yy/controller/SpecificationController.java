package com.yy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yy.core.pojo.entity.PageResult;
import com.yy.core.pojo.entity.Result;
import com.yy.core.pojo.entity.SpeciEntity;
import com.yy.core.pojo.specification.Specification;
import com.yy.core.service.SpecificationService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/specification")
public class SpecificationController {

	@Reference
	private SpecificationService specificationService;

	@RequestMapping("/search")
	public PageResult searchSpeci(@RequestBody Specification specification, int page, int rows){
		return specificationService.searchSpeci(specification,page,rows);
	}

	@RequestMapping("/add")
	public Result addSpeci(@RequestBody SpeciEntity speciEntity){
		try {
			specificationService.saveSpeci(speciEntity);
			return new Result(true,"添加成功");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"添加失败");
		}
	}

	@RequestMapping("/findOne")
	public SpeciEntity findSpeci(Long id){
		SpeciEntity speci = specificationService.findSpeci(id);
		return speci;
	}

	@RequestMapping("/update")
	public Result updateSpeci(@RequestBody SpeciEntity speciEntity){
		try {
			specificationService.updateSpeci(speciEntity);
			return new Result(true,"更新成功");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"更新失败");
		}
	}

	@RequestMapping("/delete")
	public Result deleteSpeci(Long[] ids){
		try {
			specificationService.deleteSpeci(ids);
			return new Result(true,"删除成功");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"删除失败");
		}
	}

	@RequestMapping("/selectOptionList")
	public List<Map> selectOptionList(){
		return specificationService.selectOptionList();
	}
}
