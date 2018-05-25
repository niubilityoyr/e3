package com.e3mall.sso.controller;

import javax.jws.WebParam.Mode;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3mall.common.utils.CookieUtils;
import com.e3mall.common.utils.E3Result;
import com.e3mall.sso.service.LoginService;

/**
 * 登录的controller
 * @author Dell
 *
 */
@Controller
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	
	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	
	//进入登录界面
	@RequestMapping("/page/login")
	public String showLogin(String redirect, Model model){
		model.addAttribute("redirect", redirect);
		return "login";
	}
	
	//登录
	@RequestMapping(value="/user/login", method=RequestMethod.POST)
	@ResponseBody
	public E3Result login(String username, String password, 
			HttpServletRequest request, HttpServletResponse response){
		//登录
		E3Result result = loginService.login(username, password);
		if(result.getStatus()==200){
			//登录成功后，把 token 保存到cookie中
			CookieUtils.setCookie(request, response, TOKEN_KEY, result.getData()+"");
		}
		return result;
	}
	
	//退出登录
	@RequestMapping(value="/user/out",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Object out(HttpServletRequest request, HttpServletResponse response, 
			String callback){
		E3Result result = E3Result.ok();
		CookieUtils.deleteCookie(request, response, TOKEN_KEY);
		if(!StringUtils.isBlank(callback)){
			//判断是否为 jsonp 调用
			MappingJacksonValue jacksonValue = new MappingJacksonValue(result);
			jacksonValue.setJsonpFunction(callback);
			return jacksonValue;
		}
		return result;
	}
}
