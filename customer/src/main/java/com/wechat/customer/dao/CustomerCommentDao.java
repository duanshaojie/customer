package com.wechat.customer.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.wechat.customer.po.CustomerCommentEntity;

public interface CustomerCommentDao extends CrudRepository<CustomerCommentEntity, Long> {

	CustomerCommentEntity findCommentByQuotationNumber(String quotationNumber);

	List<CustomerCommentEntity> findByGoodsId(String goodsId);

}
