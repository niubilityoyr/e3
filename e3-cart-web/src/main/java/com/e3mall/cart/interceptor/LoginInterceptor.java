package com.e3mall.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.e3mall.common.utils.CookieUtils;
import com.e3mall.common.utils.E3Result;
import com.e3mall.pojo.TbUser;
import com.e3mall.sso.service.TokenService;

/**
 * 判断用户是否登录的拦截器。
 * 登录了，把用户信息存入request中，因为 handler 中的与request是同一个对象
 * @author Dell
 *
 */
public class LoginInterceptor implements HandlerInterceptor{

	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	
	@Autowired
	private TokenService tokenService;
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//执行 Handler 前处理
		//判断用户是否登录
		//获取 cookie 中的令牌
		String token = CookieUtils.getCookieValue(request, TOKEN_KEY);
		if(StringUtils.isBlank(token)){
			//令牌为空
			return true;
		}
		//获取用户信息
		E3Result result = tokenService.getUserByToken(token);
		if(result.getStatus()!=200){
			//用户信息过期
			return true;
		}
		TbUser user = (TbUser) result.getData();
		//存入 request 中，供 handler 判断使用
		request.setAttribute("user", user);
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//执行 Handler 之后返回ModelAndView之前
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 返回ModelAndView之后，执行。异常处理。
	}

}
