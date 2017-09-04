package com.wechat.customer.po;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "customer_goods")
public class CustomerGoodsEntity {
	private long id;
	private String goodsName;//商品名称
	private String goodsCode;//商品編碼
	private String content;//商品描述
	private String url;//图片链接
	private String type;//类型
	private BigDecimal price;//价格
	private long sold;//已售
	private long counts;//库存
	private Date createTime;//创建时间
	private String createBy;//创建人
	private String storeName;//店铺名称
	
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id", nullable = false)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@Basic
	@Column(name = "goods_name")
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	@Basic
	@Column(name = "goods_code")
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	
	@Basic
	@Column(name = "sold")
	public long getSold() {
		return sold;
	}
	public void setSold(long sold) {
		this.sold = sold;
	}
	@Basic
	@Column(name = "goods_type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Basic
	@Column(name = "goods_price")
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	@Basic
	@Column(name = "goods_counts")
	public long getCounts() {
		return counts;
	}
	public void setCounts(long counts) {
		this.counts = counts;
	}
	
	@Basic
	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Basic
	@Column(name = "create_By")
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
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
	@Column(name = "content")
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Basic
	@Column(name = "url")
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
