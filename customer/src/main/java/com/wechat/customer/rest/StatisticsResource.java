package com.wechat.customer.rest;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.wechat.customer.dao.CustomerGoodsTypeDao;
import com.wechat.customer.po.CustomerGoodsEntity;
import com.wechat.customer.po.CustomerGoodsTypeEntity;
import com.wechat.customer.po.CustomerOrderEntity;
import com.wechat.customer.po.CustomerUserEntity;
import com.wechat.customer.service.CustomerGoodsService;
import com.wechat.customer.service.CustomerOrderService;
import com.wechat.customer.util.JsonHelper;
import com.wechat.customer.util.ResponseBuildHelper;

import io.swagger.annotations.ApiParam;

/**
 * <b>类名：</b>StatisticsResource.java<br>
 * <p><b>标题：</b>统计</p>
 * <p><b>描述：</b>统计API</p>
 * @author <font color='blue'>edison_dsj@163.com</font>
 * @date
 * 桃之夭夭,灼灼其华
 */

@Path("statis")
@Component
public class StatisticsResource {
	@Autowired
	CustomerGoodsService goodsService;
	@Autowired
	CustomerOrderService orderService;
	@Autowired
	CustomerGoodsTypeDao goodsTypeDao;
	
	/**
	 * 统计本商铺 消费者购买情况
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("buy")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findUserBuy(@Context HttpServletRequest request,
								@Context HttpServletResponse response)throws Exception{
		HttpSession session = request.getSession();
		CustomerUserEntity admin = (CustomerUserEntity) session.getAttribute("user");
		String username = admin.getUserName();
		Iterable<CustomerGoodsTypeEntity> typeList = goodsTypeDao.findAll();
		List<Object> listCount = new ArrayList<Object>();
		List<Object> listType = new ArrayList<Object>();
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -30);
		date = calendar.getTime();
		for(CustomerGoodsTypeEntity type:typeList){
			List<CustomerGoodsEntity> list = goodsService.findGoodsByCreateByAndType(username, type.getType());
			long count = 0;
			for(CustomerGoodsEntity entity:list){
				Long sum = orderService.findCountsByCreateByAndCode(username, entity.getGoodsCode(),date);
				count += sum;
			}
			listCount.add(count);
			listType.add(type.getType());
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("count", listCount);
		map.put("type", listType);
		return ResponseBuildHelper.buildResponse(Response.Status.OK,map);

	}
	
	
	@GET
	@Path("getType")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findGoodsType(@Context HttpServletRequest request,
								@Context HttpServletResponse response)throws Exception{
		Iterable<CustomerGoodsTypeEntity> typeList = goodsTypeDao.findAll();
		return ResponseBuildHelper.buildResponse(Response.Status.OK,typeList);

	}
	
	@POST
	@Path("income")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response storeIncome(@Context HttpServletRequest request,
								@Context HttpServletResponse response,
								@ApiParam(value = "起始时间", required = false) String time)throws Exception{
		HttpSession session = request.getSession();
		CustomerUserEntity admin = (CustomerUserEntity) session.getAttribute("user");
		String username = admin.getUserName();
		List<CustomerOrderEntity> list = orderService.findByCreateBy(username);
		BigDecimal sum = new BigDecimal("0");
		
		//时间获取//转换
		JsonNode timeJson = JsonHelper.parseJson(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//起始时间
		String timeBegin = timeJson.get("begin").textValue();
		Date begin = sdf.parse(timeBegin);
		String timeEnd = timeJson.get("end").textValue();
		Date end = sdf.parse(timeEnd);
		for(CustomerOrderEntity entity:list){
			BigDecimal price = entity.getPrice();
			long d = entity.getCreateTime().getTime();
			if(d>=begin.getTime()&&d<=end.getTime()){
				sum = sum.add(price);
			}
		}
		return ResponseBuildHelper.buildResponse(Response.Status.OK,sum);
	}
}
