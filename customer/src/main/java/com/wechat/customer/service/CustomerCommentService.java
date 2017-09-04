package com.wechat.customer.service;

import java.util.List;

import com.wechat.customer.po.CustomerCommentEntity;

public interface CustomerCommentService {

	CustomerCommentEntity findByQuotationNumber(String quotationNumber);

	void save(CustomerCommentEntity com);

	List<CustomerCommentEntity> findByGoodsId(String goodsId);

}
