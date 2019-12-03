package com.yy.controller;

import com.yy.core.pojo.entity.Result;
import com.yy.util.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadController {

	@Value("${FILE_SERVER_URL}")
	private String FILE_SERVER_URL;

	@RequestMapping("/uploadFile")
	public Result uploadFile(MultipartFile file){

		//取文件的扩展名
		String originalFilename = file.getOriginalFilename();
		String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
		try {
			//创建一个fastDFS的客户端
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:fastDFS/fdfs_client.conf");
			//执行上传处理
			String path = fastDFSClient.uploadFile(file.getBytes(), extName);
			//拼接返回的url和ip地址，拼装成完整的url
			String url = FILE_SERVER_URL + path;
			return new Result(true,url);
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false,"上传失败");
		}
	}
}