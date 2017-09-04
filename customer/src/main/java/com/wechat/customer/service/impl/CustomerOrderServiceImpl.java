package com.wechat.customer.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wechat.customer.dao.CustomerOrderDao;
import com.wechat.customer.po.CustomerOrderEntity;
import com.wechat.customer.service.CustomerOrderService;

@Service
public class CustomerOrderServiceImpl implements CustomerOrderService {
	@Autowired
	CustomerOrderDao orderDao;
	
	@Override
	public CustomerOrderEntity saveOrder(CustomerOrderEntity entity){
		return orderDao.save(entity);
	}

	@Override
	public List<CustomerOrderEntity> findByConsumer(String userName) {
		// TODO Auto-generated method stub
		return orderDao.findOrderByConsumer(userName);
	}

	@Override
	public List<CustomerOrderEntity> findByBusinessAndStatus(String userName, int status) {
		// TODO Auto-generated method stub
		return orderDao.findOrderByBusinessAndStatus(userName, status);
	}

	@Override
	public CustomerOrderEntity findByQuotationNumber(String quotationNumber) {
		// TODO Auto-generated method stub
		return orderDao.findOrderByQuotationNumber(quotationNumber);
	}

	@Override
	public void del(CustomerOrderEntity entity) {
		// TODO Auto-generated method stub
		orderDao.delete(entity);
	}
	
	@Override
	public Long findCountsByCreateByAndCode(String createBy,String goodsCode,Date date){
		return orderDao.findCountsByCreateByAndGoodsCode(createBy,goodsCode,date);
	}
	
	@Override
	public List<CustomerOrderEntity> findByCreateBy(String username){
		return orderDao.findOrderByBusiness(username);
	}

	@Override
	public List<CustomerOrderEntity> findCountsByCreateBy(String username) {
		// TODO Auto-generated method stub
		return null;
	}
}
