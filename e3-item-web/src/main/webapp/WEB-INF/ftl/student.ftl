<html>
	<head>
		<title>my is student test</title>
	</head>
	<body>
		<!-- 取pojo的属性 -->
		学生编号：${student.id}<br/>
		学生name：${student.name}<br/>
		学生age：${student.age}<br/>
		
		<!-- 取list的值 -->
		<table border="1">
			<tr>
				<th>序号</th>
				<th>id</th>
				<th>name</th>
				<th>age</th>
			</tr>
			<#list studentList as stu>
				<!-- if else 用法 -->
				<#if stu_index%2==0>
					<tr bgcolor="red">
				<#else>
					<tr bgcolor="green">
				</#if>
					<td>${stu_index}</td>
					<td>${stu.id}</td>
					<td>${stu.name}</td>
					<td>${stu.age}</td>
				</tr>
			</#list>
		</table>
		
		<!-- date类型的使用:?date ?time ?datetime ?string() -->
		date类型的使用:${date?string('yyyy/MM/dd HH:mm:ss')}<br/>
		
		<!-- null值处理 -->
		null值：${val!"这是默认值!!"}&nbsp;&nbsp;&nbsp;&nbsp;
		<#if val??>
			val中有值
		<#else>
			val为空！！！
		</#if>
		<br />
		<!-- include -->
		include使用：<#include "hello.ftl">
	</body>
</html>