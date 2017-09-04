package com.wechat.customer.service;

import java.util.Date;
import java.util.List;

import com.wechat.customer.po.CustomerOrderEntity;

public interface CustomerOrderService {

	CustomerOrderEntity saveOrder(CustomerOrderEntity entity);

	List<CustomerOrderEntity> findByConsumer(String userName);

	List<CustomerOrderEntity> findByBusinessAndStatus(String userName, int status);

	CustomerOrderEntity findByQuotationNumber(String quotationNumber);

	void del(CustomerOrderEntity entity);

	Long findCountsByCreateByAndCode(String createBy, String goodsCode, Date date);

	List<CustomerOrderEntity> findCountsByCreateBy(String username);

	List<CustomerOrderEntity> findByCreateBy(String username);
	
}
