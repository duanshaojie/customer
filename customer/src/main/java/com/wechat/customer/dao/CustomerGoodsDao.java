package com.wechat.customer.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.wechat.customer.po.CustomerGoodsEntity;

public interface CustomerGoodsDao extends CrudRepository<CustomerGoodsEntity, Long> {
	CustomerGoodsEntity findGoodsByGoodsCode(String goodsCode);

	List<CustomerGoodsEntity> findGoodsByCreateBy(String createBy);

	List<CustomerGoodsEntity> findGoodsByCreateByAndType(String username, String type);
}
