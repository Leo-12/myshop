package com.yy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yy.core.pojo.entity.Result;
import com.yy.core.pojo.user.User;
import com.yy.core.service.UserService;
import com.yy.util.PhoneFormatCheckUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yy
 * @date 2019/11/28 15:30
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Reference
	private UserService userService;

	@RequestMapping("/add")
	public Result add(@RequestBody User user,String smscode){
		Boolean isCheck = userService.checkSmsCheck(user.getPhone(), smscode);
		if (!isCheck){
			return new Result(false,"验证码输入错误");
		}
		try {
			userService.add(user);
			return new Result(true,"注册成功");
		} catch (Exception e){
			e.printStackTrace();
			return new Result(false,"注册失败");
		}
	}

	//发送短信验证码
	@RequestMapping("/sendCode")
	public Result sendCode(String phone){
		try {
			if (phone == null || "".equals(phone)){
				return new Result(false,"手机号不能为空");
			}
			if (!PhoneFormatCheckUtils.isPhoneLegal(phone)){
				return new Result(false,"手机号格式不正确");
			}
			userService.sendCode(phone);
			return new Result(true,"发送成功");
		} catch (Exception e){
			e.printStackTrace();
			return new Result(false,"发送失败");
		}
	}
}
