package com.e3mall.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.e3mall.common.jedis.JedisClient;
import com.e3mall.common.utils.E3Result;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.mapper.TbUserMapper;
import com.e3mall.pojo.TbUser;
import com.e3mall.pojo.TbUserExample;
import com.e3mall.pojo.TbUserExample.Criteria;
import com.e3mall.sso.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService{

	@Autowired
	private TbUserMapper mapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${SESSION_DATE}")
	private int SESSION_DATE;
	
	//登录方法
	public E3Result login(String username, String password) {
		//根据用户名查询用户
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = mapper.selectByExample(example);
		if(list==null || list.size()<=0){
			//用户不存在
			return E3Result.build(400, "用户名或密码错误！！！");
		}
		//判断密码是否正确
		TbUser user = list.get(0);
		String pwd = DigestUtils.md5DigestAsHex(password.getBytes());
		if(!user.getPassword().equals(pwd)){
			//密码错误
			return E3Result.build(400, "用户名或密码错误！！！");
		}
		//生成一个token
		String token = UUID.randomUUID().toString();
		//将用户密码置空
		user.setPassword(null);
		//把用户信息保存到redis中
		jedisClient.set("SESSION:"+token, JsonUtils.objectToJson(user));
		//设置过期时间
		jedisClient.expire("SESSION:"+token, SESSION_DATE);
		//返回结果，带token
		return E3Result.ok(token);
	}
	
}
