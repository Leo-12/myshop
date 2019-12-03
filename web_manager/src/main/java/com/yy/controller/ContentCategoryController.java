package com.yy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yy.core.pojo.ad.Content;
import com.yy.core.pojo.ad.ContentCategory;
import com.yy.core.pojo.entity.PageResult;
import com.yy.core.pojo.entity.Result;
import com.yy.core.service.ContentCategoryService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/contentCategory")
public class ContentCategoryController {

	@Reference
	private ContentCategoryService contentCategoryService;

	@RequestMapping("/findAll")
	public List<ContentCategory> findAll(){
		return contentCategoryService.findAll();
	}

	@RequestMapping("/search")
	public PageResult search(@RequestBody ContentCategory contentCategory, int page, int rows){
		return contentCategoryService.search(contentCategory,page,rows);
	}

	@RequestMapping("/add")
	public Result add(@RequestBody ContentCategory contentCategory){
		try {
			contentCategoryService.add(contentCategory);
			return new Result(true,"添加成功");
		} catch (Exception e){
			e.printStackTrace();
			return new Result(false,"添加失败");
		}
	}

	@RequestMapping("/findOne")
	public ContentCategory findOne(Long id){
		return contentCategoryService.findOne(id);
	}

	@RequestMapping("/update")
	public Result update(@RequestBody ContentCategory contentCategory){
		try {
			contentCategoryService.update(contentCategory);
			return new Result(true,"更新成功");
		} catch (Exception e){
			e.printStackTrace();
			return new Result(false,"更新失败");
		}
	}

	@RequestMapping("/delete")
	public Result delete(Long[] ids){
		try {
			contentCategoryService.delete(ids);
			return new Result(true,"删除成功");
		} catch (Exception e){
			e.printStackTrace();
			return new Result(false,"删除失败");
		}
	}
}
