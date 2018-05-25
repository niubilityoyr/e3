package com.e3mall.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.e3mall.common.utils.E3Result;
import com.e3mall.mapper.TbUserMapper;
import com.e3mall.pojo.TbUser;
import com.e3mall.pojo.TbUserExample;
import com.e3mall.pojo.TbUserExample.Criteria;
import com.e3mall.sso.service.RegisterService;

@Service
public class RegisterServiceImpl implements RegisterService{

	@Autowired
	private TbUserMapper userMapper;
	
	//校验数据
	public E3Result checkData(String param, Integer type) {
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		if(type==1){
			criteria.andUsernameEqualTo(param);
		}else if(type==2){
			criteria.andPhoneEqualTo(param);
		}else if(type==3){
			criteria.andEmailEqualTo(param);
		}
		List<TbUser> list = userMapper.selectByExample(example);
		if(list!=null && list.size()>0){
			return E3Result.ok(false);
		}
		return E3Result.ok(true);
	}

	//注册
	public E3Result register(TbUser user) {
		//数据校验
		if(StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())
				|| StringUtils.isBlank(user.getPhone())){
			return E3Result.build(400, "传递的数据出现异常！！！");
		}
		E3Result result1 = checkData(user.getUsername(), 1);
		if(!(boolean)result1.getData()){
			return E3Result.build(400, "用户名已存在！！！");
		}
		E3Result result2 = checkData(user.getPhone(), 2);
		if(!(boolean)result2.getData()){
			return E3Result.build(400, "手机号已存在！！！");
		}
		
		//补全信息
		user.setCreated(new Date());
		user.setUpdated(new Date());
		//密码加密
		String pwd = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(pwd);
		//添加数据库中
		userMapper.insert(user);
		
		return E3Result.ok();
	}
	
}
