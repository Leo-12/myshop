package com.yy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yy.core.service.SearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author yy
 * @date 2019/11/21 15:48
 */
@RestController
@RequestMapping("/itemsearch")
public class SearchController {

	@Reference
	private SearchService searchService;

	@RequestMapping("/search")
	public Map<String, Object> search(@RequestBody Map paramMap){
		return searchService.search(paramMap);
	}
}
