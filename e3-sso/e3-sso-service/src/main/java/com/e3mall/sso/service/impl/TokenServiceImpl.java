package com.e3mall.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.e3mall.common.jedis.JedisClient;
import com.e3mall.common.utils.E3Result;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.pojo.TbUser;
import com.e3mall.sso.service.TokenService;

@Service
public class TokenServiceImpl implements TokenService{

	@Autowired
	private JedisClient jedisClient;
	
	@Value("${SESSION_DATE}")
	private int SESSION_DATE;
	
	//根据 token 从redis中查数据
	public E3Result getUserByToken(String token) {
		String json = jedisClient.get("SESSION:"+token);
		if(StringUtils.isBlank(json)){
			//用户登录过期了
			return E3Result.build(400, "用户登录过期了！！！");
		}
		//取出用户信息
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		//设置过期时间
		jedisClient.expire("SESSION:"+token, SESSION_DATE);
		return E3Result.ok(user);
	}
	
}
