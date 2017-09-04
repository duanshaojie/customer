package com.wechat.customer.util;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wechat.customer.dao.CustomerGoodsTypeDao;
import com.wechat.customer.dao.CustomerUserDao;
import com.wechat.customer.po.CustomerGoodsTypeEntity;
import com.wechat.customer.po.CustomerUserEntity;
/**
 * 服务器启动自动创建admin管理员账号
 * @author dev
 *
 */
@Component
public class CreateAdminUtils{
	@Autowired
	CustomerUserDao userDao;
	@Autowired
	CustomerGoodsTypeDao goodsTypeDao;
	
	@PostConstruct
	public void CreateAdmin(){
		CustomerUserEntity adminUser = userDao.findByUserName(CustomerUtils.ADMIN);
		if(adminUser==null){
			CustomerUserEntity entity = new CustomerUserEntity();
			entity.setUserName(CustomerUtils.ADMIN);
			String password = MD5Util.MD5Encode(CustomerUtils.ADMIN, "");
			entity.setPassword(password);
			entity.setPower(CustomerUtils.POWER_ADMIN);
			String scope = CustomerUtils.SCOPE_D+CustomerUtils.SCOPE_R+CustomerUtils.SCOPE_W;
			entity.setScope(scope);
			entity.setStatus(CustomerUtils.USER_STATUS_TRUE);
			userDao.save(entity);
		}
		
		Iterable<CustomerGoodsTypeEntity> list = goodsTypeDao.findAll();
		int i = 0;
		for(CustomerGoodsTypeEntity type:list){
			i +=1;
		}
		if(i==0){
			CustomerGoodsTypeEntity drink = new CustomerGoodsTypeEntity();
			drink.setType("饮品类");
			CustomerGoodsTypeEntity candy = new CustomerGoodsTypeEntity();
			candy.setType("糖果类");
			CustomerGoodsTypeEntity snacks = new CustomerGoodsTypeEntity();
			snacks.setType("零食类");
			goodsTypeDao.save(drink);
			goodsTypeDao.save(candy);
			goodsTypeDao.save(snacks);
		}
	}
}
