package com.wechat.customer.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wechat.customer.dao.CustomerGoodsDao;
import com.wechat.customer.po.CustomerGoodsEntity;
import com.wechat.customer.service.CustomerGoodsService;

@Service
public class CustomerGoodsServiceImpl implements CustomerGoodsService {
	@Autowired
	CustomerGoodsDao goodsDao;
	
	@Override
	public CustomerGoodsEntity findGoodsByCode(String goodscode) {
		return goodsDao.findGoodsByGoodsCode(goodscode);
	}

	@Override
	public void saveGoods(CustomerGoodsEntity goodsEntity) {
		goodsDao.save(goodsEntity);
	}
	
	@Override
	public Iterable<CustomerGoodsEntity> findGoodsAll() {
		return goodsDao.findAll();
	}
	
	@Override
	public List<CustomerGoodsEntity> findGoodsByCreateBy(String username) {
		return goodsDao.findGoodsByCreateBy(username);
	}

	@Override
	public void deleteGoods(CustomerGoodsEntity entity) {
		goodsDao.delete(entity);
	}

	@Override
	public List<CustomerGoodsEntity> findGoodsByCreateByAndType(String username,String type){
		return goodsDao.findGoodsByCreateByAndType(username,type);
	}
}
