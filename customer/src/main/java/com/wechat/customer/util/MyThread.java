package com.wechat.customer.util;

import javax.servlet.ServletContextEvent;

import org.jboss.resteasy.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.wechat.customer.dao.CustomerUserDao;
import com.wechat.customer.po.CustomerUserEntity;

public class MyThread extends Thread {
	@Autowired
	CustomerUserDao userDao;
	private Logger logger = Logger.getLogger(MyThread.class);
	
    public void run() {
    	//web listener  监听启动
    	logger.info("MyThread begin");
    	if(userDao!=null){
    		CustomerUserEntity adminUser = userDao.findByUserName("admin");
    		if(adminUser==null){
    	    	logger.info("admin is null,create begin");
    			CustomerUserEntity entity = new CustomerUserEntity();
    			entity.setUserName(CustomerUtils.ADMIN);
    			String password = MD5Util.MD5Encode(CustomerUtils.ADMIN, "");
    			entity.setPassword(password);
    			entity.setPower(CustomerUtils.POWER_ADMIN);
    			userDao.save(entity);
    		}
    	}
    }
}
