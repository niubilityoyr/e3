package com.e3mall.sso.spring;

import org.junit.Test;
import org.springframework.util.DigestUtils;

public class Md5Test {
	@Test
	public void test1(){
		System.out.println(DigestUtils.md5DigestAsHex("123456".getBytes()));
	}
}
