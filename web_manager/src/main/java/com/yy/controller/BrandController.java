package com.yy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yy.core.pojo.entity.PageResult;
import com.yy.core.pojo.entity.Result;
import com.yy.core.pojo.good.Brand;
import com.yy.core.service.BrandService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/brand")
public class BrandController {

	@Reference
	private BrandService brandService;

	@ResponseBody
	@RequestMapping(value = "/findAll",method = RequestMethod.GET)
	public List<Brand> showBrandList(){
		return brandService.showBrandList();
	}

	@ResponseBody
	@RequestMapping(value = "/findByPage",method = RequestMethod.GET)
	public PageResult findPage(int page, int rows){
		return brandService.findPage(page,rows);
	}

	@ResponseBody
	@RequestMapping(value="/save",method = RequestMethod.POST)
	public Result addBrand(@RequestBody Brand brand){
		try {
			brandService.addBrand(brand);
			return new Result(true,"添加成功");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"添加失败");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/findById",method = RequestMethod.GET)
	public Brand findOne(Long id){
		return brandService.getBrandById(id);
	}

	@ResponseBody
	@RequestMapping("/update")
	public Result updateBrand(@RequestBody Brand brand){
		try {
			brandService.updateBrand(brand);
			return new Result(true,"更新成功");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"更新失败");
		}
	}

	@ResponseBody
	@RequestMapping("/search")
	public PageResult searchBrand(@RequestBody Brand brand, int page, int rows){
		return brandService.findPage(brand,page,rows);
	}

	@ResponseBody
	@RequestMapping("/delete")
	public Result deleteBrand(Long [] ids){
		try {
			brandService.deleteBrand(ids);
			return new Result(true,"删除成功");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"删除失败");
		}
	}

	@ResponseBody
	@RequestMapping("/selectOptionList")
	public List<Map> selectOptionList(){
		return brandService.selectOptionList();
	}

}
