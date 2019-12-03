package com.yy.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.yy.core.pojo.seller.Seller;
import com.yy.core.service.SellerService;
import com.yy.core.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailServiceImp implements UserDetailsService {

	private SellerService sellerService;

	public SellerService getSellerService() {
		return sellerService;
	}

	public void setSellerService(SellerService sellerService) {
		this.sellerService = sellerService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		System.out.println("经过了userDetailsServiceImp");

		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));

		Seller seller = sellerService.findSeller(username);
		if (seller != null){
			if (seller.getStatus().equals("1")){
				return new User(username,seller.getPassword(),grantedAuthorities);
			}else{
				return null;
			}
		}else {
			return null;
		}


	}
}
