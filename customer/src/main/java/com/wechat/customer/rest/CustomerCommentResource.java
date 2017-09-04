package com.wechat.customer.rest;

import java.math.BigDecimal;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.wechat.customer.po.CustomerCommentEntity;
import com.wechat.customer.po.CustomerGoodsEntity;
import com.wechat.customer.po.CustomerOrderEntity;
import com.wechat.customer.po.CustomerUserEntity;
import com.wechat.customer.service.CustomerCommentService;
import com.wechat.customer.service.CustomerGoodsService;
import com.wechat.customer.service.CustomerOrderService;
import com.wechat.customer.util.CustomerUtils;
import com.wechat.customer.util.JsonHelper;
import com.wechat.customer.util.ResponseBuildHelper;

import io.swagger.annotations.ApiParam;

@Path("comment")
@Component
public class CustomerCommentResource {
	@Autowired
	CustomerCommentService commentService;
	@Autowired
	CustomerOrderService orderService;
	@Autowired
	CustomerGoodsService goodsService;;
	
	@POST
	@Path("add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addComment(@Context HttpServletRequest request, 
			@Context HttpServletResponse response,
			@ApiParam(value = "评论信息", required = false) String comment)throws Exception {
		if(StringUtils.isEmpty(comment)){
			return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"评论信息不能为空");
		}
		HttpSession session = request.getSession();
		CustomerUserEntity admin = (CustomerUserEntity) session.getAttribute("user");
		if(admin==null){
			return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"用户未登录");
		}
		Date date = new Date();
		JsonNode commentJson = JsonHelper.parseJson(comment);
		String quotationNumber = commentJson.get("quotationNumber").textValue();
		CustomerOrderEntity order = orderService.findByQuotationNumber(quotationNumber);
		CustomerGoodsEntity goodsEntity = goodsService.findGoodsByCode(order.getGoodsCode());
		CustomerCommentEntity entity = commentService.findByQuotationNumber(quotationNumber);
		if(entity!=null){
			return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"您已经进行过评论!");
		}
		CustomerCommentEntity com = new CustomerCommentEntity();
		com.setComment(commentJson.get("comment").textValue());
		com.setCreateBy(admin.getUserName());
		com.setCreateTime(date);
		com.setGoodsId(String.valueOf(goodsEntity.getId()));
		com.setQuotationNumber(quotationNumber);
		commentService.save(com);
		return ResponseBuildHelper.buildResponse(Response.Status.OK,"添加成功"); 
	}
	
	@GET
	@Path("find/{goodsId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findComment(@Context HttpServletRequest request, 
			@Context HttpServletResponse response,
			@ApiParam(value = "商品id", required = true) @PathParam("goodsId") String goodsId)throws Exception {
		if(StringUtils.isEmpty(goodsId)){
			return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"商品Id不能为空");
		}
		HttpSession session = request.getSession();
		CustomerUserEntity admin = (CustomerUserEntity) session.getAttribute("user");
		if(admin==null){
			return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"用户未登录");
		}
		Map<String,Object> map = new HashMap<String,Object>();
		List<CustomerCommentEntity> list = commentService.findByGoodsId(goodsId);
		map.put("data", list);
		return ResponseBuildHelper.buildResponse(Response.Status.OK,list); 
	}
	
	
	
	
}
