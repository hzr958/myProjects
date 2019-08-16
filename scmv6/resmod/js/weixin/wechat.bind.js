var wechat =wechat||{};
wechat.bind = wechat.bind||{};

wechat.bind.refreshcode=function(){
	document.getElementById("img_checkcode").src = "/open/validatecode.gif?date="+new Date();
};

wechat.bind.savebind=function(form) {
	if($("#validateCode").length!=0){
		var code = $("#validateCode").val().trim();
		if(code==""){
			$("#hint").text("请输入验证码");
			return false;
		}
	}
	$("#login").disabled();
	$("#bindForm").attr("action",'/open/wechat/bind');
	form.submit();
};
