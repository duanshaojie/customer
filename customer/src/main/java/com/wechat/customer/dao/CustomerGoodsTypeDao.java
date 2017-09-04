package com.wechat.customer.dao;

import org.springframework.data.repository.CrudRepository;

import com.wechat.customer.po.CustomerGoodsTypeEntity;

public interface CustomerGoodsTypeDao extends CrudRepository<CustomerGoodsTypeEntity, Long> {

}
