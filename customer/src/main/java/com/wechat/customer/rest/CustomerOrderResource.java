package com.wechat.customer.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wechat.customer.po.CustomerGoodsEntity;
import com.wechat.customer.po.CustomerOrderEntity;
import com.wechat.customer.po.CustomerUserEntity;
import com.wechat.customer.service.CustomerGoodsService;
import com.wechat.customer.service.CustomerOrderService;
import com.wechat.customer.util.CustomerUtils;
import com.wechat.customer.util.ResponseBuildHelper;

import io.swagger.annotations.ApiParam;

/**
 * <b>类名：</b>CustomerOrderResource.java<br>
 * <p><b>标题：</b>订单</p>
 * <p><b>描述：</b>订单API</p>
 * @author <font color='blue'>edison_dsj@163.com</font>
 * @date
 * 桃之夭夭,灼灼其华
 */

@Path("order")
@Component
public class CustomerOrderResource {
	@Autowired
	CustomerOrderService orderService;
	@Autowired
	CustomerGoodsService goodsService;
	
	@POST
	@Path("add/{goodsCode}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response userRegister(@Context HttpServletRequest request, 
								@Context HttpServletResponse response,
								@ApiParam(value = "商品编码", required = true) @PathParam("goodsCode") String goodsCode)throws Exception {
		if(StringUtils.isEmpty(goodsCode)){
			return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"下单失败");
		}
		HttpSession session = request.getSession();
		CustomerUserEntity admin = (CustomerUserEntity) session.getAttribute("user");
		Date date = new Date();
		CustomerGoodsEntity entity = goodsService.findGoodsByCode(goodsCode);
		CustomerOrderEntity order = new CustomerOrderEntity();
		order.setBusiness(entity.getCreateBy());
		order.setConsumer(admin.getUserName());
		order.setCreateTime(date);
		order.setGoodsCode(goodsCode);
		order.setGoodsName(entity.getGoodsName());
		order.setPrice(entity.getPrice());
		order.setStatus(CustomerUtils.USER_ORDER_BEGIN);
		order.setQuotationNumber("KH"+date.getTime());
		orderService.saveOrder(order);
		return ResponseBuildHelper.buildResponse(Response.Status.OK,"添加成功");
	}
	
	
	@GET
	@Path("myOrder")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response orderMySelf(@Context HttpServletRequest request, 
								@Context HttpServletResponse response)throws Exception {
		HttpSession session = request.getSession();
		CustomerUserEntity admin = (CustomerUserEntity) session.getAttribute("user");
		Map<String,Object> map = new HashMap<String,Object>();
		List<CustomerOrderEntity> list = orderService.findByConsumer(admin.getUserName());
		map.put("data", list);
		return ResponseBuildHelper.buildResponse(Response.Status.OK,map);
	}
	
	@GET
	@Path("OrderToBegin/{status}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response orderBegin(@Context HttpServletRequest request, 
								@Context HttpServletResponse response,
								@ApiParam(value = "状态", required = true) @PathParam("status") String status)throws Exception {
		HttpSession session = request.getSession();
		CustomerUserEntity admin = (CustomerUserEntity) session.getAttribute("user");
		if(admin.getPower()!=CustomerUtils.POWER_CUSTOMER){
			return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"查询失败，您还不是商户");
		}
		List<CustomerOrderEntity> list = orderService.findByBusinessAndStatus(admin.getUserName(),Integer.valueOf(status));
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("data", list);
		return ResponseBuildHelper.buildResponse(Response.Status.OK,map);
	}
	
	@GET
	@Path("delete/{quotationNumber}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteOrder(@Context HttpServletRequest request, 
								@Context HttpServletResponse response,
								@ApiParam(value = "保单号", required = true) @PathParam("quotationNumber") String quotationNumber)throws Exception {
		HttpSession session = request.getSession();
		CustomerUserEntity admin = (CustomerUserEntity) session.getAttribute("user");
		CustomerOrderEntity entity = orderService.findByQuotationNumber(quotationNumber);
		if(entity.getConsumer().equals(admin.getUserName())){
			orderService.del(entity);
			return ResponseBuildHelper.buildResponse(Response.Status.OK,"删除成功");
		}
		return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"删除失败");
	}
	
	@GET
	@Path("send/{quotationNumber}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response sendOrder(@Context HttpServletRequest request, 
								@Context HttpServletResponse response,
								@ApiParam(value = "保单号", required = true) @PathParam("quotationNumber") String quotationNumber)throws Exception {
		HttpSession session = request.getSession();
		CustomerUserEntity admin = (CustomerUserEntity) session.getAttribute("user");
		CustomerOrderEntity entity = orderService.findByQuotationNumber(quotationNumber);
		if(entity.getBusiness().equals(admin.getUserName())){
			entity.setStatus(CustomerUtils.USER_ORDER_END);
			CustomerGoodsEntity goodsEntity = goodsService.findGoodsByCode(entity.getGoodsCode());
			long counts = goodsEntity.getCounts();
			long sold = goodsEntity.getSold();
			counts = counts - 1;
			sold = sold + 1;
			if(counts<0){
				return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"库存不足!");
			}
			goodsEntity.setCounts(counts);
			goodsEntity.setSold(sold);
			goodsService.saveGoods(goodsEntity);
			orderService.saveOrder(entity);
			return ResponseBuildHelper.buildResponse(Response.Status.OK,"发货成功");
		}
		return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"发货失败");
	}
}
