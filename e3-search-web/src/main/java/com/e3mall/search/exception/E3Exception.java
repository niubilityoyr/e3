package com.e3mall.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 全局异常处理器
 * @author Dell
 *
 */
public class E3Exception implements HandlerExceptionResolver{

	private static final Logger logger = LoggerFactory.getLogger(E3Exception.class);
	
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		//打印控制台
		ex.printStackTrace();
		//写日志
		logger.debug("系统出现异常");
		logger.info("系统出现异常");
		logger.error("系统出现异常！！！", ex);
		//发短信，邮箱
		//发邮件：jmail，发短信：阿里云
		//返回视图
		ModelAndView andView = new ModelAndView();
		andView.setViewName("error/exception");
		return andView;
	}

}
