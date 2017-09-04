package com.wechat.customer.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wechat.customer.dao.CustomerUserDao;
import com.wechat.customer.po.CustomerUserEntity;
import com.wechat.customer.service.CustomerUserService;
import com.wechat.customer.util.CustomerUtils;

@Service
public class CustomerUserServiceImpl implements CustomerUserService {
	
	@Autowired
	CustomerUserDao userDao;
	
	@Override
	public CustomerUserEntity findUserByUsername(String username){
		return userDao.findByUserName(username);
	}
	
	@Override
	public CustomerUserEntity saveUser(CustomerUserEntity entity){
		return userDao.save(entity);
	}
	
	@Override
	public CustomerUserEntity findLoginUser(String username,String password){
		return userDao.findByUserNameAndPassword(username,password);
	}
	
	@Override
	public Iterable<CustomerUserEntity> findAll(){
		return userDao.findAll();
	}
	
	@Override
	public void del(CustomerUserEntity entity){
		userDao.delete(entity);
	}
}
