package com.wechat.customer.po;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "customer_user")
public class CustomerUserEntity {
	private long userId;//用户id
	private String userName;//用户名
	private String password;//密码
	private int power;//权限   30管理员 10个人用户  20商家
	private String scope;//权限范围 r/d/w
	private int status;//状态

	private String name;//真实姓名
	private String mobile;//电话
	private String address;//家庭地址
	
	//商铺信息
	private String storeName;//商家名称
	private String storeCode;//营业执照
	private String type;//经营类型
	
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "user_id", nullable = false)
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	@Basic
    @Column(name = "user_name")
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Basic
    @Column(name = "password")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Basic
    @Column(name = "power")
	public int getPower() {
		return power;
	}
	public void setPower(int power) {
		this.power = power;
	}
	
	@Basic
    @Column(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Basic
    @Column(name = "mobile")
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	@Basic
    @Column(name = "address")
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Basic
    @Column(name = "scope")
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	
	@Basic
    @Column(name = "status")
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	@Basic
	@Column(name = "store_name")
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	
	@Basic
	@Column(name = "store_code")
	public String getStoreCode() {
		return storeCode;
	}
	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}
	
	@Basic
	@Column(name = "store_type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
	
	
}
