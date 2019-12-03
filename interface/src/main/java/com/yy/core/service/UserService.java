package com.yy.core.service;

import com.yy.core.pojo.user.User;

/**
 * @author yy
 * @date 2019/11/28 15:34
 */
public interface UserService {

	void add(User user);

	Boolean checkSmsCheck(String phone,String smsCode);

	void sendCode(String phone);
}
