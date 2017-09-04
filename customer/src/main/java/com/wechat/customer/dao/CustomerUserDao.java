package com.wechat.customer.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.wechat.customer.po.CustomerUserEntity;

public interface CustomerUserDao extends CrudRepository<CustomerUserEntity, Long> {
	CustomerUserEntity findByUserName(String username);
	
	List<CustomerUserEntity> findUserByStatus(int status);

	CustomerUserEntity findByUserNameAndPassword(String username, String password);
}
