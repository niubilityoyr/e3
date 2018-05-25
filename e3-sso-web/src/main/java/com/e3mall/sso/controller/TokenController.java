package com.e3mall.sso.controller;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3mall.common.utils.E3Result;
import com.e3mall.sso.service.TokenService;

/**
 * 根据 token 操作用户信息
 * @author Dell
 *
 */
@Controller
public class TokenController {
	
	@Resource
	private TokenService tokenService;
	
	//通过token获取user对象信息
	@RequestMapping(value="/user/token/{token}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Object getUserByToken(@PathVariable String token, String callback){
		E3Result result = tokenService.getUserByToken(token);
		if(!StringUtils.isBlank(callback)){
			//是否为 jsonp 调用
			MappingJacksonValue jacksonValue = new MappingJacksonValue(result);
			jacksonValue.setJsonpFunction(callback);
			return jacksonValue;
		}
		return result;
	}
	
}
