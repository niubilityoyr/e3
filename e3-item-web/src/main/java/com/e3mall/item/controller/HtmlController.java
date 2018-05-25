package com.e3mall.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Controller
public class HtmlController {
	
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@RequestMapping("getHtml")
	@ResponseBody
	public String getHtml() throws Exception{
		//获取Configuration对象
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		//加载一个模板，创建一个模板对象。
		Template template = configuration.getTemplate("hello.ftl");
		//创建数据集
		Map data = new HashMap<>();
		data.put("hello", "123456");
		//创建一个Writer对象，一般创建一FileWriter对象，指定生成的文件名。
		Writer out = new FileWriter(new File("E:/freemarker/hello2.html"));
		//调用模板对象的process方法输出文件。
		template.process(data, out);
		//关闭流
		out.close();
		return "ok";
	}
	
}
