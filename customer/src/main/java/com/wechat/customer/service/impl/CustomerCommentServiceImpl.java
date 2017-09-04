package com.wechat.customer.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wechat.customer.dao.CustomerCommentDao;
import com.wechat.customer.po.CustomerCommentEntity;
import com.wechat.customer.service.CustomerCommentService;

@Service
public class CustomerCommentServiceImpl implements CustomerCommentService {
	@Autowired
	CustomerCommentDao commentDao;
	
	@Override
	public CustomerCommentEntity findByQuotationNumber(String quotationNumber) {
		// TODO Auto-generated method stub
		return commentDao.findCommentByQuotationNumber(quotationNumber);
	}

	@Override
	public void save(CustomerCommentEntity com) {
		// TODO Auto-generated method stub
		commentDao.save(com);
	}

	@Override
	public List<CustomerCommentEntity> findByGoodsId(String goodsId) {
		// TODO Auto-generated method stub
		List<CustomerCommentEntity> list = commentDao.findByGoodsId(goodsId);
		return list;
	}

}
