package com.e3mall.order.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.e3mall.common.utils.CookieUtils;
import com.e3mall.common.utils.E3Result;
import com.e3mall.pojo.TbUser;
import com.e3mall.sso.service.TokenService;

public class LoginInterceptor implements HandlerInterceptor{

	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	
	@Value("${SSO_URL}")
	private String SSO_URL;
	
	@Resource
	private TokenService tokenService;
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//获取token
		String token = CookieUtils.getCookieValue(request, TOKEN_KEY);
		if(StringUtils.isBlank(token)){
			//用户没有登录，跳转到sso系统的登录界面
			response.sendRedirect(SSO_URL + "/page/login.html?redirect="+request.getRequestURL());
			//拦截
			return false;
		}
		//用户登录是否过期
		E3Result result = tokenService.getUserByToken(token);
		if(result.getStatus()!=200){
			//用户登录时间过期，跳转到sso系统的登录界面
			response.sendRedirect(SSO_URL + "/page/login.html?redirect="+request.getRequestURL());
			//拦截
			return false;
		}
		//已登录
		TbUser user = (TbUser) result.getData();
		request.setAttribute("user", user);
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}
}
