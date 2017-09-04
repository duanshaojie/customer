package com.wechat.customer.service;

import java.util.List;

import com.wechat.customer.po.CustomerUserEntity;

public interface CustomerUserService {

	CustomerUserEntity findUserByUsername(String username);

	CustomerUserEntity saveUser(CustomerUserEntity entity);

	CustomerUserEntity findLoginUser(String username, String password);

	Iterable<CustomerUserEntity> findAll();

	void del(CustomerUserEntity entity);

}
