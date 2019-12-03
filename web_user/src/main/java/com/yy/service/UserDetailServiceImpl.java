package com.yy.service;

import com.yy.core.service.UserDetailService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yy
 * @date 2019/12/2 13:59
 */
public class UserDetailServiceImpl implements UserDetailService {
	@Override
	public UserDetails loadUserByUsername(String username) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		return new User(username,"",authorities);
	}
}
