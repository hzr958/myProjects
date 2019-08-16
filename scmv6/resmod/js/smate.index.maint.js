/**
 * 科研之友未登录首页页面的JS事件.
 * @author MJG
 * @since 2012-08-10
 */
var SCMIndex=SCMIndex?SCMIndex:{};

/**
 * 热门研究人员
 */
SCMIndex.popuparResearcher=function(){
	
	$.ajax({
		method : "post",
		url :ctx+"/indexOutside/ajaxPopularResearcher",
		data : {},
		dataType : "html",
		success : function(data) {
			$(".con_box1").html(data);
		},
		error : function(e) {
		}
	});
}

/**
 * 热门研究成果
 */
SCMIndex.popuparPublication=function(){
	
	$.ajax({
		method : "post",
		url :ctx+"/indexOutside/ajaxPopularPub",
		data : {},
		dataType : "html",
		success : function(data) {
			$(".con_box2").html(data);
		},
		error : function(e) {
		}
	});
}

/**
 * 登陆请求. 
 * 如果记住登录 setcookie
 * 如果没选 判断所填帐号与目前kooie所存帐号是否相同
 * 相同 则清除
 */
SCMIndex.login=function(){
	Cookie.setCookieTime("LashLoginUser",$("#username").val(),7);  //记住帐号
};
/**
 * 注册请求.
 */
SCMIndex.register=function(){
	$("#registForm").submit();
};
/**
 * 现在进行登录(将焦点聚到登录的邮箱输入框中).
 */
SCMIndex.loginNow=function(){
	$('#username').focus();
	$.smate.scmtips.show('warn',loginnow);
};
/**
 * 现在就加入科研之友(将焦点聚到注册的名称输入框中).
 */
SCMIndex.registerNow=function(){
	$('#name').focus();
	$.smate.scmtips.show('warn',joinin);
};
/**
 * 忘记密码.
 */
SCMIndex.forgetPassword=function(aObj){
	  var email = $("#username").val(); 
	  var frmUrl = domScm+"/scmwebsns/forgetpwd/forgetPwd?email="+email+"&from=sns";  
	  aObj.href=frmUrl;
	  aObj.click();
};
/**
* 初始化页面的缓存用户名信息_MJG_SCM-3956.
* 默认选中复选框_tsz_2014-01-02_SCM-4229 
* 修正记住登录问题_在index.jsp跳到login.jsp时填写帐号不一致问题_tsz_2014-01-02_SCM-4233

*/
SCMIndex.initPageInfo=function(){
	//首先判断帐号密码是否为空 如果不为空 直接点击登录按钮  实现自动登陆tsz 
	var checkcode=$("#checkcode").attr("id");
	//expert-7 打开主页，弹出脚本错误
	if(typeof password == "undefined"){
		password = $("#password").val();
	}
	if(password!=null && password!=""&&checkcode==null && typeof(checkcode) == "undefined"&&$("#username").val()!=null &&  $("#username").val()!="" && $("#password").val()!=null && $("#password").val()!="" ){
		$("#loginButton").click();
	}else{
		var c_username=Cookie.getCookie("LashLoginUser");
		if(typeof username!='undefined' && username!=null && username!='""' && username!=''){	
			$("#username").val(username);
		}else{
			$("#username").val(c_username);
		}
	}
	
}
/**
 * 站外检索成果.
 */
SCMIndex.searchPub=function(aObj){
	var frmUrl = ctx+"/indexSearch/indexPub";  
	aObj.href=frmUrl;
	aObj.click();
};
/**
 * 站外检索人员.
 */
SCMIndex.searchPerson=function(aObj){
	var frmUrl = ctx+"/indexSearch/indexUser";  
	aObj.href=frmUrl;
	aObj.click();
};
/**
 * 站外检索基金.
 */
SCMIndex.searchFund=function(aObj){
	var frmUrl = ctx+"/indexSearch/indexFund";  
	aObj.href=frmUrl;
	aObj.click();
};
/**
 * 站外检索期刊.
 */
SCMIndex.searchJnl=function(aObj){
	var frmUrl = ctx+"/indexSearch/indexJnl";  
	aObj.href=frmUrl;
	aObj.click();
};
/**
 * 页面跳转到系统首页.
 */
SCMIndex.maint=function(){
	window.location.href=ctx;
};
//对URL传参的字符串进行编码处理.
SCMIndex.URLencode=function (sStr) 
{
    return escape(sStr).replace(/\+/g, '%2B').replace(/\"/g,'%22').replace(/\'/g, '%27').replace(/\//g,'%2F');
};
/**
 * (未登录系统的)转换页面语言.
 */
SCMIndex.change_locol_language=function(locale){
	strUrl = location.href;//当前url地址.
	//请求的url地址.
	var hrefString=strUrl.substring(0,strUrl.indexOf("?"));
	var params = "";
	var flag=false;//验证url请求参数中是否包含页面语言参数locale(true-包含；false-不包含).
	if(strUrl.indexOf("?")>=0){
		//url请求参数.
		var paraString = strUrl.substring(strUrl.indexOf("?") + 1, strUrl.length);
		if(paraString!=''){
			var paraArr=paraString.split("&");
			for ( var i = 0; j = paraArr[i]; i++) {
				//参数名.
				var name=j.substring(0, j.indexOf("="));
				//参数值.
				var value=j.substring(j.indexOf("=") + 1, j.length);
				if(name=='locale'){//页面语言的参数设置为locale的值.
					flag=true;
					value=locale;
				}else{//其他参数进行编码处理,替换掉特殊字符.
					//value=SCMIndex.URLencode(value);
				}
				params=params+name+"="+value+"&";
			} 
		}
	}
	//如果请求参数中不包含语言参数，则增加该参数.
	if(!flag){
		params=params+"locale="+locale;
	}
	//重定位页面请求地址
	window.location.href = hrefString+"?"+params;
	
};
/**
 * 首页的加载JS.
 */
SCMIndex.loadIndexJs=function(){
	SCMIndex.sessionTimeout();
	$("#sm_btn").click(function(){
		var userInfo = $.trim($("#search_some_one").val());
		var title = $("#search_some_one").attr("title");
		if(userInfo == "" || userInfo == title){
			return false;
		}
		$("#search_some_one_form").submit();
	});
	//如果语言版本为英文，则调整登陆输入框下方的显示内容的格式.
	if(language=='en_US'){
		$(".sm_entries2").css("text-align","left");
		$(".sm_entries2").html('&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+smTriesHTML);
	}
	SCMIndex.initPageInfo();
	//ie6提示信息
	$("body").browserTips({
		locale:language
	});
};
/**
 * 超时处理逻辑.
 */
SCMIndex.sessionTimeout=function (){
	if(self.frameElement && self.frameElement.tagName=="IFRAME"){
		var topLocation=top.location.href;
		var reminder = language == "zh_CN" ? "提示" : "Reminder";
		var loginCfmTip = language == "zh_CN" ? "系统超时或未登录，你要登录吗？" 
				: "You are not logged in or session time out, please log in again.";
		parent.$.Thickbox.resetWidth(600);
		parent.$.Thickbox.resetHeight(400);
		jConfirm(loginCfmTip, reminder, function(r) {
			if (r) {
			     if(topLocation.lastIndexOf("specview") != -1){
			    	 topLocation = topLocation.replace("specview","loginSpecView");
			    	 top.location.href = topLocation;
			     }else if(topLocation.lastIndexOf("/personalResume/view") != -1) {
			    	 top.location.href="http://"+window.location.host+ctx+"/index?service=" + encodeURIComponent(topLocation);
			     } else if(topLocation.lastIndexOf("view") != -1) {
				     topLocation = topLocation.replace("view","loginView");
				     top.location.href="http://"+window.location.host+ctx+"/index?service=" + encodeURIComponent(topLocation);
			     } else {
			    	 top.location.href="http://"+window.location.host+ctx+"/index?service=" + encodeURIComponent(topLocation);
			     }
			}else{
				if(parent.$("#TB_window").length>0){
				  parent.$.Thickbox.closeWin();
				}else{
					top.location.href=topLocation;
				}
			}
		});
	}else{
		$("#login_main_div").show();
	}
}