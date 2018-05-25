package com.e3mall.item.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.e3mall.item.pojo.Student;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemarkerTest {
	
	@Test
	public void test1() throws Exception{
		//第一步：创建一个Configuration对象，直接new一个对象。构造方法的参数就是freemarker对于的版本号。
		Configuration configuration = new Configuration(Configuration.getVersion());
		//第二步：设置模板文件所在的路径。
		configuration.setDirectoryForTemplateLoading(new File("D:/myMaven/e3-item-web/src/main/webapp/ftl"));
		//第三步：设置模板文件使用的字符集。一般就是utf-8.
		configuration.setDefaultEncoding("utf-8");
		//第四步：加载一个模板，创建一个模板对象。
		Template template = configuration.getTemplate("student.ftl");
		//第五步：创建一个模板使用的数据集，可以是pojo也可以是map。一般是Map。
		Map map = new HashMap<>();
		map.put("hello", "hello freemarker");
		map.put("student", new Student(1, "小明", 18));
		List studentList = new ArrayList<>();
		studentList.add(new Student(1, "小明1", 19));
		studentList.add(new Student(2, "小明2", 20));
		studentList.add(new Student(3, "小明3", 52));
		studentList.add(new Student(4, "小明4", 11));
		studentList.add(new Student(5, "小明5", 33));
		map.put("studentList", studentList);
		//时间类型
		map.put("date", new Date());
		//null值
		map.put("val", "val");
		//第六步：创建一个Writer对象，一般创建一FileWriter对象，指定生成的文件名。
		Writer writer = new FileWriter(new File("E:/freemarker/student.html"));
		//第七步：调用模板对象的process方法输出文件。
		template.process(map, writer);
		//第八步：关闭流。
		writer.close();
	}
	
}
