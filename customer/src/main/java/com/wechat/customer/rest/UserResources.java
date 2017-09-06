package com.wechat.customer.rest;

/**
 * <b>类名：</b>UserResources.java<br>
 * <p><b>标题：</b>用户</p>
 * <p><b>描述：</b>用户API</p>
 * @author <font color='blue'>edison_dsj@163.com</font>
 * @date
 * 桃之夭夭,灼灼其华
 */

import java.util.HashMap;

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
import com.wechat.customer.po.CustomerUserEntity;
import com.wechat.customer.service.CustomerUserService;
import com.wechat.customer.util.CustomerUtils;
import com.wechat.customer.util.JsonHelper;
import com.wechat.customer.util.MD5Util;
import com.wechat.customer.util.ResponseBuildHelper;

import io.swagger.annotations.ApiParam;
@Path("user")
@Component
public class UserResources {
	@Autowired
	CustomerUserService userService;
	
	/**
	 * 注册
	 * @param request
	 * @param response
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@POST
	@Path("register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response userRegister(@Context HttpServletRequest request, 
			@Context HttpServletResponse response,
			@ApiParam(value = "账户密码", required = false) String user)throws Exception {
		if(StringUtils.isEmpty(user)){
			return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"注册信息不能为空");
		}
		JsonNode userMessage = JsonHelper.parseJson(user);
		String username = userMessage.get("username").textValue();
		String password = userMessage.get("password").textValue();
		CustomerUserEntity entity = new CustomerUserEntity();
		if(!"".equals(username)){
			CustomerUserEntity userEntity = userService.findUserByUsername(username);
			if(userEntity==null){
				password = MD5Util.MD5Encode(password, "");
				entity.setUserName(username);
				entity.setPassword(password);
				entity.setPower(CustomerUtils.POWER_PERSON);
				entity.setStatus(CustomerUtils.USER_STATUS_TRUE);
				userService.saveUser(entity);
				return ResponseBuildHelper.buildResponse(Response.Status.OK,"注册成功");
			}
			return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"该用户名已存在");
		}
		return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"用户名不能为空");
	}
	
	/**
	 * 登录
	 * @param request
	 * @param response
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@POST
	@Path("login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response userLogin(@Context HttpServletRequest request, 
			@Context HttpServletResponse response,
			@ApiParam(value = "账户密码", required = false) String user)throws Exception {
		if(StringUtils.isEmpty(user)){
			return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"登录信息为空");
		}
		HttpSession session = request.getSession();
		CustomerUserEntity admin = (CustomerUserEntity) session.getAttribute("user");
		if(admin!=null){
			session.invalidate();
		}
		JsonNode userMessage = JsonHelper.parseJson(user);
		String username = userMessage.get("username").textValue();
		String password = userMessage.get("password").textValue();
		password = MD5Util.MD5Encode(password, "");
		CustomerUserEntity entity = userService.findLoginUser(username, password);
		if(entity!=null){
			if(entity.getStatus()==CustomerUtils.USER_STATUS_FALSE){
				return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"账户已被冻结");
			}
			session.setAttribute("user", entity);
			return ResponseBuildHelper.buildResponse(Response.Status.OK,entity);
		}
		return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"用户名或密码错误");
	}
	
	/**
	 * 管理员查询用户
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("find")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response userfind(@Context HttpServletRequest request, 
			@Context HttpServletResponse response)throws Exception {
		HttpSession session = request.getSession();
		CustomerUserEntity admin = (CustomerUserEntity) session.getAttribute("user");
		int power = admin.getPower();
		if(CustomerUtils.POWER_ADMIN!=power){
			return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"您无权限操作此页面");
		}
		Iterable<CustomerUserEntity> list = userService.findAll();
		HashMap<String,Iterable<CustomerUserEntity>> map = new HashMap<String,Iterable<CustomerUserEntity>>();
		map.put("data", list);
		return ResponseBuildHelper.buildResponse(Response.Status.OK,map);
	}
	
	/**
	 * 删除用户
	 * @param request
	 * @param response
	 * @param username
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("del/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response userdel(@Context HttpServletRequest request, 
			@Context HttpServletResponse response,
			 @ApiParam(value = "管理员用户名", required = true) @PathParam("username") String username)throws Exception {
		HttpSession session = request.getSession();
		CustomerUserEntity admin = (CustomerUserEntity) session.getAttribute("user");
		if(admin.getPower()!=CustomerUtils.POWER_ADMIN){
			return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"您无此操作权限");
		}
		CustomerUserEntity entity = userService.findUserByUsername(username);
		userService.del(entity);
		return ResponseBuildHelper.buildResponse(Response.Status.OK,"删除成功");
	}
	
	/**
	 * 退出，清session
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("loginOut")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response userdel(@Context HttpServletRequest request, 
			@Context HttpServletResponse response)throws Exception {
		HttpSession session = request.getSession();
		session.invalidate();
		return ResponseBuildHelper.buildResponse(Response.Status.OK,"退出成功");

	}
	/**
	 * query登录信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("query")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response userquery(@Context HttpServletRequest request, 
			@Context HttpServletResponse response)throws Exception {
		HttpSession session = request.getSession();
		CustomerUserEntity admin = (CustomerUserEntity) session.getAttribute("user");
		if(admin!=null){
			return ResponseBuildHelper.buildResponse(Response.Status.OK,admin);
		}
		return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"查询失败");
	}
	/**
	 * 激活或冻结
	 * @param request
	 * @param response
	 * @param username
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("frozen/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response userFrozen(@Context HttpServletRequest request, 
			@Context HttpServletResponse response,
			@ApiParam(value = "用户名", required = true) @PathParam("username") String username)throws Exception {
		HttpSession session = request.getSession();
		CustomerUserEntity admin = (CustomerUserEntity) session.getAttribute("user");
		if(admin.getPower()!=CustomerUtils.POWER_ADMIN){
			return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"您无此操作权限");
		}
		CustomerUserEntity entity = userService.findUserByUsername(username);
		if(entity.getStatus()==CustomerUtils.USER_STATUS_FALSE){
			entity.setStatus(CustomerUtils.USER_STATUS_TRUE);
		}else{
			entity.setStatus(CustomerUtils.USER_STATUS_FALSE);
		}
		userService.saveUser(entity);
		return ResponseBuildHelper.buildResponse(Response.Status.OK,"操作成功");
	}
	
	/**
	 * 同意申请
	 * @param request
	 * @param response
	 * @param username
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("approval/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response userApproval(@Context HttpServletRequest request, 
			@Context HttpServletResponse response,
			@ApiParam(value = "用户名", required = true) @PathParam("username") String username)throws Exception {
		HttpSession session = request.getSession();
		CustomerUserEntity admin = (CustomerUserEntity) session.getAttribute("user");
		if(admin.getPower()!=CustomerUtils.POWER_ADMIN){
			return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"您无此操作权限");
		}
		CustomerUserEntity entity = userService.findUserByUsername(username);
		if(entity.getStatus()==CustomerUtils.USER_STATUS_BEGING){
			entity.setPower(CustomerUtils.POWER_CUSTOMER);
			entity.setStatus(CustomerUtils.USER_STATUS_TRUE);
			userService.saveUser(entity);
			return ResponseBuildHelper.buildResponse(Response.Status.OK,"操作成功");
		}else{
			return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"操作失败");
		}
		
	}
	
	@POST
	@Path("become")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response userStore(@Context HttpServletRequest request, 
			@Context HttpServletResponse response,
			@ApiParam(value = "商铺信息信息", required = false) String user)throws Exception {
		if(StringUtils.isEmpty(user)){
			return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"注册信息不能为空");
		}
		HttpSession session = request.getSession();
		CustomerUserEntity admin = (CustomerUserEntity) session.getAttribute("user");
		if(admin==null){
			return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"用户不存在");
		}
		if(admin.getStatus()==CustomerUtils.USER_STATUS_BEGING){
			return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"已有店铺正在审核中");
		}
		JsonNode userJson = JsonHelper.parseJson(user);
		String storeCode = userJson.get("storeCode").textValue();
		String storeName = userJson.get("storeName").textValue();
		String type = userJson.get("type").textValue();

		admin.setStatus(CustomerUtils.USER_STATUS_BEGING);
		admin.setStoreCode(storeCode);
		admin.setStoreName(storeName);
		admin.setType(type);
		userService.saveUser(admin);
		return ResponseBuildHelper.buildResponse(Response.Status.OK,"申请成功");
	}
	
	@POST
	@Path("update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response userUpdate(@Context HttpServletRequest request, 
			@Context HttpServletResponse response,
			@ApiParam(value = "用户信息", required = false) String user)throws Exception {
		if(StringUtils.isEmpty(user)){
			return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"更新信息不能为空");
		}
		HttpSession session = request.getSession();
		CustomerUserEntity admin = (CustomerUserEntity) session.getAttribute("user");
		if(admin==null){
			return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"用户未登录");
		}
		JsonNode json = JsonHelper.parseJson(user);
		String name = json.get("name").textValue();
		String mobile = json.get("mobile").textValue();
		String address = json.get("address").textValue();
		CustomerUserEntity entity = userService.findUserByUsername(admin.getUserName());
		entity.setName(name);
		entity.setMobile(mobile);
		entity.setAddress(address);
		userService.saveUser(entity);
		return ResponseBuildHelper.buildResponse(Response.Status.OK,"修改成功");
	}
	
	@POST
	@Path("updatePsd")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response userUpdatePassword(@Context HttpServletRequest request, 
			@Context HttpServletResponse response,
			@ApiParam(value = "用户密码", required = false) String user)throws Exception {
		if(StringUtils.isEmpty(user)){
			return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"更新信息不能为空");
		}
		HttpSession session = request.getSession();
		CustomerUserEntity admin = (CustomerUserEntity) session.getAttribute("user");
		if(admin==null){
			return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"用户未登录");
		}
		JsonNode json = JsonHelper.parseJson(user);
		String old = json.get("old_password").textValue();
		String newPsd = json.get("new_password").textValue();
		CustomerUserEntity entity = userService.findUserByUsername(admin.getUserName());
		old = MD5Util.MD5Encode(old, "");
		if(!old.equals(entity.getPassword())){
			return ResponseBuildHelper.buildErrorResponse(Response.Status.BAD_REQUEST,"用户原密码输入不正确");
		}
		newPsd = MD5Util.MD5Encode(newPsd, "");
		entity.setPassword(newPsd);
		userService.saveUser(entity);
		return ResponseBuildHelper.buildResponse(Response.Status.OK,"修改成功");
	}
	
}
