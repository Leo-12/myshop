package com.yy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yy.core.pojo.ad.Content;
import com.yy.core.pojo.entity.PageResult;
import com.yy.core.pojo.entity.Result;
import com.yy.core.service.ContentService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/content")
public class ContentController {

	@Reference
	private ContentService contentService;

	@RequestMapping("/search")
	public PageResult search(@RequestBody Content content, int page, int rows){
		return contentService.search(content,page,rows);
	}

	@RequestMapping("/add")
	public Result add(@RequestBody Content content){
		try {
			contentService.add(content);
			return new Result(true,"添加成功");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"添加失败");
		}
	}

	@RequestMapping("/findOne")
	public Content findOne(Long id){
		return contentService.findOne(id);
	}

	@RequestMapping("/update")
	public Result update(@RequestBody Content content){
		try {
			contentService.update(content);
			return new Result(true,"更新成功");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"更新失败");
		}
	}

	@RequestMapping("/delete")
	public Result delete(Long[] ids){
		try {
			contentService.delete(ids);
			return new Result(true,"删除成功");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"删除失败");
		}
	}

}
