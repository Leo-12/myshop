package com.yy.test;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

/**
 * @author yy
 * @date 2019/11/26 9:17
 */
public class FreeMarkerTest {
	public static void main(String[] args) throws Exception {
		//1、创建模板引擎初始化对象
		Configuration configuration = new Configuration();
		//2、加载模板所在目录位置
		configuration.setDirectoryForTemplateLoading(new File("D:\\idea-workspace\\youlexuan\\freemarker_demo\\src\\main\\resources\\ftl"));
		//3、加载模板对象
		Template template = configuration.getTemplate("test.ftl");
		//4、模拟假数据，并将这些数据放入到模板中
		Map<String, Object> rootMap = new HashMap<>();
		rootMap.put("name","ko");
		rootMap.put("password","1");
		//模拟添加集合数据
		List<String> personList = new ArrayList<>();
		personList.add("纳兹");
		personList.add("露西");
		personList.add("格雷");
		personList.add("艾露莎");
		personList.add("温蒂");
		rootMap.put("personList",personList);

		Map<String,String> personMap = new HashMap<>();
		personMap.put("动漫","Fairly Tile");
		personMap.put("man","纳兹");
		personMap.put("women","露西");
		rootMap.put("personMap",personMap);

		rootMap.put("today",new Date());
		rootMap.put("point",10000000);
		//5、创建IO流，流中指定文件输出位置和文件名
		Writer writer = new FileWriter(new File("D:\\idea-workspace\\youlexuan\\freemarker_demo\\src\\main\\resources\\test.html"));
		//6、生成
		template.process(rootMap,writer);
		//7、关流
		writer.close();
	}
}
