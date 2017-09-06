package com.wechat.customer.rest;

import java.math.BigDecimal;
import java.util.ArrayList;
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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.wechat.customer.po.CustomerGoodsEntity;
import com.wechat.customer.po.CustomerUserEntity;
import com.wechat.customer.service.CustomerGoodsService;
import com.wechat.customer.util.CustomerUtils;
import com.wechat.customer.util.JsonHelper;
import com.wechat.customer.util.MD5Util;
import com.wechat.customer.util.ResponseBuildHelper;
import com.wechat.customer.vo.GoodsVO;

import io.swagger.annotations.ApiParam;

/**
 * <b>类名：</b>CustomerGoodsResource.java<br>
 * <p><b>标题：</b>商品</p>
 * <p><b>描述：</b>商品API</p>
 * @author <font color='blue'>edison_dsj@163.com</font>
 * @date
 * 桃之夭夭,灼灼其华
 */

@Path("goods")
@Component
public class CustomerGoodsResource {
	@Autowired
	CustomerGoodsService goodsService;
	
	@POST
	@Path("addGoods")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response userRegister(@Context HttpServletRequest request, 
			@Context HttpServletResponse response,
			@ApiParam(value = "商品信息", required = false) String goods)throws Exception {
		if(StringUtils.isEmpty(goods)){
			return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"商品信息不能为空");
		}
		HttpSession session = request.getSession();
		CustomerUserEntity admin = (CustomerUserEntity) session.getAttribute("user");
		if(admin==null||admin.getPower()==CustomerUtils.POWER_PERSON){
			return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"无权限操作");
		}
		Date date = new Date();
		JsonNode goodsMessage = JsonHelper.parseJson(goods);
		String goodsname = goodsMessage.get("goodsname").textValue();
		String goodsprice = goodsMessage.get("goodsprice").textValue();
		String goodscount = goodsMessage.get("goodscount").textValue();
		String goodstype = goodsMessage.get("goodstype").textValue();
		String goodscode = goodsMessage.get("goodscode").textValue();
		String url = goodsMessage.get("url").textValue();
		String content = goodsMessage.get("content").textValue();
		CustomerGoodsEntity entity = goodsService.findGoodsByCode(goodscode);
		if(entity==null){
			CustomerGoodsEntity goodsEntity = new CustomerGoodsEntity();
			goodsEntity.setCounts(Long.valueOf(goodscount));
			goodsEntity.setCreateBy(admin.getUserName());
			goodsEntity.setCreateTime(date);
			goodsEntity.setGoodsName(goodsname);
			goodsEntity.setGoodsCode(goodscode);
			goodsEntity.setPrice(new BigDecimal(goodsprice));
			goodsEntity.setType(goodstype);
			goodsEntity.setStoreName(admin.getStoreName());
			goodsEntity.setUrl(url);
			goodsEntity.setContent(content);
			goodsService.saveGoods(goodsEntity);
			return ResponseBuildHelper.buildResponse(Response.Status.OK,"添加成功");

		}
		return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"商品已存在");
	}
	
	/**
	 * 当个产品详情
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("find/{goodsCode}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response goodsFindAll(@Context HttpServletRequest request, 
								@Context HttpServletResponse response,
								@ApiParam(value = "商品编码", required = true) @PathParam("goodsCode") String goodsCode)throws Exception {
		CustomerGoodsEntity entity = goodsService.findGoodsByCode(goodsCode);
		return ResponseBuildHelper.buildResponse(Response.Status.OK,entity);
	}
	
	/**
	 * 商品浏览
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("findAll")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response goodsFindAll(@Context HttpServletRequest request, 
			@Context HttpServletResponse response)throws Exception {
		Iterable<CustomerGoodsEntity> list = goodsService.findGoodsAll();
		Map<String,Object> map = new HashMap<String,Object>();
		List<GoodsVO> listNew = new ArrayList<GoodsVO>();
		for(CustomerGoodsEntity entity:list){
			GoodsVO vo = new GoodsVO();
			BeanUtils.copyProperties(vo, entity);
			listNew.add(vo);
		}
		map.put("data", listNew);
		return ResponseBuildHelper.buildResponse(Response.Status.OK,map);
	}
	
	/**
	 * 商家自己的产品列表
	 * @param request
	 * @param response
	 * @param goods
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("findOur")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response goodsFindOur(@Context HttpServletRequest request, 
			@Context HttpServletResponse response)throws Exception {
		HttpSession session = request.getSession();
		CustomerUserEntity admin = (CustomerUserEntity) session.getAttribute("user");
		List<CustomerGoodsEntity> list = goodsService.findGoodsByCreateBy(admin.getUserName());
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("data", list);
		return ResponseBuildHelper.buildResponse(Response.Status.OK,map);
	}
	
	/**
	 * 删除产品
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("delete/{goodsCode}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response goodsDelete(@Context HttpServletRequest request, 
								@Context HttpServletResponse response,
								@ApiParam(value = "商品编码", required = true) @PathParam("goodsCode") String goodsCode)throws Exception {
		HttpSession session = request.getSession();
		CustomerUserEntity admin = (CustomerUserEntity) session.getAttribute("user");
		if(admin==null){
			return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"请登录");
		}
		CustomerGoodsEntity entity = goodsService.findGoodsByCode(goodsCode);
		if(!admin.getUserName().equals(entity.getCreateBy())){
			return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"您无权删除此商品");
		}
		goodsService.deleteGoods(entity);
		return ResponseBuildHelper.buildResponse(Response.Status.OK,"删除成功");
	}
	
	@POST
	@Path("update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response goodsUpdate(@Context HttpServletRequest request, 
								@Context HttpServletResponse response,
								@ApiParam(value = "商品信息", required = false) String goods)throws Exception {
		HttpSession session = request.getSession();
		CustomerUserEntity admin = (CustomerUserEntity) session.getAttribute("user");
		if(admin==null){
			return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"请登录");
		}
		JsonNode goodsEntity = JsonHelper.parseJson(goods);
		String goodscode = goodsEntity.get("goodscode").textValue();
		CustomerGoodsEntity entity = goodsService.findGoodsByCode(goodscode);
		if(!admin.getUserName().equals(entity.getCreateBy())){
			return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"您无权更新此商品");
		}
		String goodsname = goodsEntity.get("goodsname").textValue();
		String goodsprice = goodsEntity.get("goodsprice").textValue();
		String goodscount = goodsEntity.get("goodscount").textValue();
		String goodstype = goodsEntity.get("goodstype").textValue();
		String url = goodsEntity.get("url").textValue();
		String content = goodsEntity.get("content").textValue();

		entity.setCounts(Long.valueOf(goodscount));
		entity.setCreateBy(admin.getUserName());
		entity.setGoodsName(goodsname);
		entity.setGoodsCode(goodscode);
		entity.setPrice(new BigDecimal(goodsprice));
		entity.setType(goodstype);
		entity.setUrl(url);
		entity.setContent(content);
		goodsService.saveGoods(entity);
		return ResponseBuildHelper.buildResponse(Response.Status.OK,"更新成功");
	}
	
	
}
