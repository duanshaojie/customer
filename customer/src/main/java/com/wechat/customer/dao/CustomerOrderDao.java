package com.wechat.customer.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.wechat.customer.po.CustomerOrderEntity;

public interface CustomerOrderDao extends CrudRepository<CustomerOrderEntity, Long> {
	List<CustomerOrderEntity> findOrderByConsumer(String username);
	
	List<CustomerOrderEntity> findOrderByBusinessAndStatus(String username,int status);

	CustomerOrderEntity findOrderByQuotationNumber(String quotationNumber);
	
	@Query("select count(*) from CustomerOrderEntity c where c.business = ?1 and c.goodsCode = ?2 and c.createTime>?3")
	Long findCountsByCreateByAndGoodsCode(String createBy, String goodsCode, Date date);

	List<CustomerOrderEntity> findOrderByBusiness(String username);

}
