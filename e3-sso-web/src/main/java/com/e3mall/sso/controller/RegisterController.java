package com.e3mall.sso.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3mall.common.utils.E3Result;
import com.e3mall.pojo.TbUser;
import com.e3mall.sso.service.RegisterService;

/**
 * 注册的controller
 * @author Dell
 *
 */

@Controller
public class RegisterController {
	
	@Resource
	private RegisterService service;
	
	//进入注册界面
	@RequestMapping("/page/register")
	public String showRegister(){
		return "register";
	}
	
	//校验数据
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public E3Result checkData(@PathVariable String param, @PathVariable Integer type) {
		E3Result result = service.checkData(param, type);
		return result;
	}
	
	//注册
	@RequestMapping(value="/user/register", method=RequestMethod.POST)
	@ResponseBody
	public E3Result register(TbUser user){
		E3Result result = service.register(user);
		return result;
	}
}
