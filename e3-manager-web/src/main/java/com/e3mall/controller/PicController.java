package com.e3mall.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.e3mall.common.utils.FastDFSClient;
import com.e3mall.common.utils.JsonUtils;

/**
 * 图片上传
 * @author Dell
 *
 */
@Controller
public class PicController {

	@Value("${PIC_SERVER_URL}")
	private String PIC_SERVER_URL;
	
	@RequestMapping("/pic/upload")
	@ResponseBody
	public String upload(MultipartFile uploadFile){
		//要返回的信息
		Map map = new HashMap<>();
		try {
			FastDFSClient client = new FastDFSClient("classpath:conf/client.conf");
			//文件名
			String filename = uploadFile.getOriginalFilename();
			//扩展名
			String extName = filename.substring(filename.lastIndexOf(".")+1);
			//上传
			String url = client.uploadFile(uploadFile.getBytes(), extName);
			map.put("error", 0);
			map.put("url", PIC_SERVER_URL + url);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("error", 1);
			map.put("message", "上传图片失败！！！");
		}
		String json = JsonUtils.objectToJson(map);
		return json;
	}
}
