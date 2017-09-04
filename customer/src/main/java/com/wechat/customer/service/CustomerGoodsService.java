package com.wechat.customer.service;

import java.util.List;

import com.wechat.customer.po.CustomerGoodsEntity;

public interface CustomerGoodsService {

	CustomerGoodsEntity findGoodsByCode(String goodscode);

	void saveGoods(CustomerGoodsEntity goodsEntity);

	Iterable<CustomerGoodsEntity> findGoodsAll();

	List<CustomerGoodsEntity> findGoodsByCreateBy(String username);

	void deleteGoods(CustomerGoodsEntity entity);

	List<CustomerGoodsEntity> findGoodsByCreateByAndType(String username, String type);

}
