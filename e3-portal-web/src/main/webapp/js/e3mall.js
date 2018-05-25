var E3MALL = {
	checkLogin : function(){
		var _ticket = $.cookie("TOKEN_KEY");
		if(!_ticket){
			return ;
		}
		$.ajax({
			url : "http://localhost:8088/user/token/" + _ticket+".do",
			dataType : "jsonp",
			type : "GET",
			success : function(data){
				if(data.status == 200){
					var username = data.data.username;
					var html = username + "，欢迎来到宜立方购物网！<a href='javascript:;' class=\"link-logout\">[退出]</a>";
					$("#loginbar").html(html);
					//给退出绑定事件
					$("#loginbar").find(".link-logout").click(function(){
						$.ajax({
							url : "http://localhost:8088/user/out.do",
							dataType : "jsonp",
							type : "GET",
							success : function(data){
								if(data.status==200){
									location.href="http://localhost:8083";
								}
							}
						});
					})
				}
			}
		});
	}
}
$(function(){
	// 查看是否已经登录，如果已经登录查询登录信息
	E3MALL.checkLogin();
});