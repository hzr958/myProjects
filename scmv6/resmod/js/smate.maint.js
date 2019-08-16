/**
 * 菜单皮肤页面JS.
 * @author MJG
 */
var ScmMaint =
{
		"searchSomeOneBind":function(){//检索人员标签bind事件.
			$("#search_some_one").bind("focus",function(){
				/*this.placeholder="";*/
				if($.trim(this.value) == $.trim(this.title)){
					this.value = "";
				}
				/*$(this).css("color","#000000");*/
			}).bind("blur",function(){
				/*this.placeholder=this.title;*/
				if($.trim(this.value) == ""){
					/*this.value = this.title;*/
					/*$(this).css("color","#999999");*/
				}
			});/*.css("color","#999999");*/
			
		
			 $("#search_some_one").on("keydown", function (e) {
			        if (e.keyCode == 13) {
			        	if($.trim($(this).val())==""){
			        		return   false;
						}
			        }
			    });
		},"select_search_psn":function(){
			$("#search_some_one_form").attr("action","/pubweb/search/psnsearch");
			ScmMaint.searchWater(searchType);
			$("#search_some_one").attr("title",searchType);
			$("#home_search_items").stop(true,true).hide();
			$("#search_type_img").removeClass();
			$("#search_type_img").addClass("ry_icon");
			$("#search_type_img").attr("style","background-position: 0 -105px");
			
		},"select_search_patent":function(){
			$("#search_some_one_form").attr("action"," /pub/search/pdwhpatent");
			ScmMaint.searchWater(searchType3);
			$("#search_some_one").attr("title",searchType3);
			$("#home_search_items").stop(true,true).hide();
			$("#search_type_img").attr("style","");
			$("#search_type_img").removeClass();
			$("#search_type_img").addClass("zl_icon");	
		},"select_search_paper":function(){
			$("#search_some_one_form").attr("action","/pub/search/pdwhpaper");
			ScmMaint.searchWater(searchType2);
			$("#search_some_one").attr("title",searchType2);
			$("#home_search_items").stop(true,true).hide();
			$("#search_type_img").attr("style","");
			$("#search_type_img").removeClass();
			$("#search_type_img").addClass("lw_icon");		
		},"select_search_ins":function(){
			$("#search_some_one_form").attr("action","/prjweb/outside/agency/searchins");
			ScmMaint.searchWater(searchType4);
			$("#search_some_one").attr("title",searchType4);
			$("#home_search_items").stop(true,true).hide();
			$("#search_type_img").attr("style","");
			$("#search_type_img").removeClass();
			$("#search_type_img").addClass("mesm_icon");
			/*$("#search_type_img").addClass("lw_icon");	*/	
		},"searchSomeOne":function(){//检索人员.
			var userInfo = $.trim($("#search_some_one").val());
			var title = $("#search_some_one").attr("title");
			if(userInfo==searchType || userInfo==searchType2 || userInfo==searchType3 || userInfo==searchType4){
				return;
			}
			if(userInfo == "" || userInfo == title){
				return;
			}
			$("#search_some_one_form").submit();
		},
		"searchSomeSubmit":function(){//检索人员验证.
			var userInfo = $.trim($("#search_some_one").val());
			var title = $("#search_some_one").attr("title");
			if(userInfo == "" || userInfo == title){
				return false;
			}
			return true;
		},
		"refreshCode":function(){//刷新验证码.
			//清空输入框
			$("#checkcode").attr("value","");
			$("#validateCode").attr("src",ctx+"/validatecode/jcaptcha.jpg?now=" + new Date());
		},
		"logoutSys":function(sys){//退出登录.
			MsgBoxUtil.delCookie("msgBoxIsClose");
			Cookie.delCookie("CasAuth");
			Cookie.delCookie("pwd");
			var rolDomain=$("#rolDomain").val();
			//rolDomain = rolDomain.replace(/^/s*/, "").replace(//s*$/, "");
			
			/*document.location.href = rolDomain+"/oauth/loginout";*/
			if(rolDomain==domainOauth){
				location.href = domainOauth+"/oauth/logout?sys=SNS";
			}
			else{  
				location.href = "http://"+rolDomain+"/scmwebrol/loginout";
			}
//			document.location.href=domCas+"logout?locale="+locale+"&url="+logoutindex;

		},
		"changeLanguage":function(language){//已登录系统的语言切换响应事件(直接跳转到“个人中心”页面并同时切换语言).
			document.location.href=snsctx+"/main?locale="+language;
		},
		"setMenuTab":function(name,cursel,n){//子菜单样式控制事件.
			for (var i = 1; i <= n; i++) {
				var menu = document.getElementById(name + i);
				var con = document.getElementById("con_" + name + "_" + i);
				if(con && menu){
					menu.className = (i == cursel) ? "hover" : "";
					con.style.display = (i == cursel) ? "block" : "none";
				}
			}
		},
		"setIndexMenuTab":function(name,cursel,n){//子菜单样式控制事件.
			for (var i = 1; i <= n; i++) {
				var menu = document.getElementById(name + i);
				var con = document.getElementById("con_" + name + "_" + i);
				if(con && menu){
					menu.className = (i == cursel) ? "sm_hover" : "";
					con.style.display = (i == cursel) ? "block" : "none";
				}
			}
		},
		"mmenuURL":function () //菜单样式控制JS事件.
		{
			var thisURL = document.URL;
			tmpUPage = thisURL.split( "/" );  
			thisUPage_s = tmpUPage[ tmpUPage.length-2 ]; 
			thisUPage_s= thisUPage_s.toLowerCase();// 
	 
			if(thisUPage_s=="domain")
			{
				$("#mm2").className="menuhover";
				$("#mb2").className = "";
			}
			else if(thisUPage_s=="hosting")
			{
				$("#mm3").className="menuhover";
				$("#mb3").className = "";
			}	
			else if(thisUPage_s=="mail")
			{
				$("#mm4").className="menuhover";
				$("#mb4").className = "";
			}
			else if(thisUPage_s=="solutions"||thisUPage_s=="site"){
				$("#mm5").className="menuhover";
				$("#mb5").className = "";
			}
			else if(thisUPage_s=="promotion"){
				$("#mm6").className="menuhover";
				$("#mb6").className = "";
			}
			else if(thisUPage_s=="trade"||thisUPage_s=="phonetic"||thisUPage_s=="switchboard"||thisUPage_s=="note"){
				$("#mm7").className="menuhover";
				$("#mb7").className = "";
			}
			else if(thisUPage_s=="benefit"){
				$("#mm8").className="menuhover";
				$("#mb8").className = "";
			}
			else if(thisUPage_s=="userlogon"||thisUPage_s=="domain_service"||thisUPage_s=="hosting_service"||thisUPage_s=="mail_service"||thisUPage_s=="Payed"||thisUPage_s=="unPayed"||thisUPage_s=="Invoice"||thisUPage_s=="Finance"||thisUPage_s=="RegInfoModify"){
				$("#mm9").className="menuhover";
				$("#mb9").className = "";
			}
			else if (thisUPage_s=="fundapp") {
				if (tmpUPage[tmpUPage.length-1].toLowerCase()=="main") {
					$('#mainmenu_body').find('.nav-three').hide();
				}
			}
			else
			{
				$("#mm1").className="";
				$("#mb1").className = "";
			}
	},
	"cleanSavedRegCon":function(){//清空浏览器保存的注册用户名称和密码字段内容_MaoJianGuo_2012-11-15_SCM-181
		if(language=='zh_CN'){
			$("#name").val('');
		}else{
			$("#first_name").val('');
			$("#last_name").val('');
		}
		$("#email").val('');
		$("#newpassword").val('');
		$("#renewpassword").val('');
	},
	"changeSkinStyle":function(){//变换皮肤页面样式(隐藏main.jsp皮肤页面的菜单栏标签，搜索人员标签，修改head标签的标签ID)
		$("#mainmenu_body").hide();
		$("#header").attr("id","home-header");
		$(".top-search-btn").hide();
	},
	"searchWater":function(searchtype){//绑定检索人员输入框的水印效果.
		
		$('#search_some_one').val(searchtype);
		$(this).css("color","#999");

	
	$("#search_some_one").bind("focus",function(){
		if(this.value == this.title){
			this.value = "";
		}
		$(this).css("color","#000000");
	}).bind("blur",function(){
		if($.trim(this.value) == ""){
			this.value = this.title;
		}
		//当检索框失去焦点时，如果显示内容为“检索人员”，则对显示内容的样式加水印显示效果_MJG_2012-12-10_ROL-10.
		if($.trim(this.value)==this.title){
			$(this).css("color","#999");
		}
	});
	},
	"bindMainLabels":function(){//绑定main.jsp页面的标签响应事件_MJG_SCM-20140605.
		//绑定切换站点标签事件.
		$("#switch_site_label").bind({
			"click":function(){
				//如果标签显示，则将其隐藏；反之则显示.
				if ($("#switching_box").is(":visible")) {
					$("#switching_box").stop(true,true).hide();
				}
				else {
					$("#switching_box").animate({
						opacity: 1,
						height: 'show'
					}, 300);
				}
			}
			,"mouseleave":function(){//鼠标离开事件.
				$("#switching_box").bind({
					mouseenter : function() {//鼠标悬浮事件.
						$("#switching_box").animate({opacity:1, height:'show'},300);
					}
					,mouseleave : function() {//鼠标离开事件.
						setTimeout(function(){
							$("#switching_box").stop(true,true).hide();
						},1100);
					}
				});
			}
			,"blur" : function() {//失去焦点事件.
				$("#switching_box").bind({
					"click" : function() {//失去焦点事件.
						$("#switching_box").animate({opacity:1, height:'show'},300);
					}
				});
				setTimeout(function(){
					$("#switching_box").stop(true,true).hide();
				},1100);
			}
		});
		$("#psnBox").bind({//个人设置弹出框响应事件.
			mouseenter : function() {//鼠标悬浮事件.
				$("#quit-nav").animate({opacity:1, height:'show'},300);
			}
			,mouseleave : function() {//鼠标离开事件.
				$("#quit-nav").stop(true,true).hide();
			}
		});
		$("#msg_tip_box").bind({//动态消息响应事件.
			"mouseenter" : function() {
				if(parseInt($("#msg_tip_totalTop").text()) > 0) {
					$("#work-reminds").animate({opacity:1, height:'show'},{queue: false},300);
				}
			},
			"mouseleave" : function() {
				$("#work-reminds").stop(true,true).hide();
			}
			
		});
		if (parseInt($("#msg_total_count").val()) <= 0) {
			$("#work-reminds").stop(true, true).hide();
			$("#msgTip_div").stop(true, true).hide();
		}
	},
	"remindScmInfo":function(){//原切换站点响应事件(切换站点标签为select标签).
		//rol-1869 去掉从科研之友机构版切换到科研之友时的提示信息
		/*var switch_sys_remind='是否切换到科研之友';
		var switch_sys_title='提示';
		if (locale == 'en_US' || locale == 'en') {
			switch_sys_remind='Are you sure to switch to ScholarMate?';
			switch_sys_title='Reminder';
		}
 	   jConfirm(switch_sys_remind, switch_sys_title, function(result){
 		   if(result){
 			   location.href=scm_url;
 			}
 		 });*/
 	  location.href=scm_url;
	},
	"initMainLabelStatus":function(){
		//设定检索人员标签的显示内容.
		
		$("#show_select").click(function(e){
			var _obj = $("#home_search_items");
			// 显示隐藏
			if (_obj.is(":hidden")) {
				_obj.show();
			} else {
				_obj.hide();
				return;
			}
			//点击其他地方关闭
		    if (e && e.stopPropagation) {//非IE  
		        e.stopPropagation();  
		    }  
		    else {//IE  
		        window.event.cancelBubble = true;  
		    } 
		    $(document).click(function(){_obj.hide();});
		});
		
		
		var activeMenuId=$(".headerMenu").find(".headerNav").find("ul").find(".header_nav_on").attr("id");
		//如果选中菜单显示标签属性丢失，则默认显示“我的科研”下的菜单.
		if(activeMenuId==undefined){
			$("#mm1").attr("class","header_nav_on");
		}
		var activeMenuId=$(".nav-t").find(".nav_wrap-t").find("ul").find(".cur").attr("id");
		//如果选中菜单显示标签属性丢失，则默认显示“我的科研”下的菜单.
		if(activeMenuId==undefined){
			$("#mm1").attr("class","cur");
		}
		//科研之友机构版右上角切换到科研之友图标的点击响应事件_MJG_SCM-5293.
		$(".sm_toplogo").bind("click",function(){
			ScmMaint.remindScmInfo();
		});

		ScmMaint.searchWater(searchTypeTip);	

		//显示提醒信息.
		$("#work-reminds").animate({opacity:1, height:'show'},300);
		$("#msgTip_div").animate({opacity:1, height:'show'},300);
		//ie6提示信息
		/*$("body").browserTips({
			locale:locale
		});*/
		/*
		 * //如果浏览器版本是IE7.0则设置div标签的宽度，以兼容IE7的切换站点标签显示位置异常的问题.
		if($.browser.msie){
			if($.browser.version=='7.0'){
				var ie7_width=$(".top-right").width();
				$(".sm_topnav").css({"width":ie7_width});
			}
		}
		 */
	},
	"HTMLEnCode":function(str) {//字符转码.
		var s = "";
		if (str.length == 0)
			return "";
		s = str.replace(/&/g, "&amp;");
		s = s.replace(/</g, "&lt;");
		s = s.replace(/>/g, "&gt;");
		s = s.replace(/ /g, "&nbsp;");
		s = s.replace(/\'/g, "&#39;");
		s = s.replace(/\"/g, "&quot;");
		s = s.replace(/\r\n/g, "<br>");
		return s;
	},
	"HTMLDeCode":function(str) {//字符解码.
		var s = "";
		if (str.length == 0)
			return "";
		s = str.replace(/&amp;/g, "&");
		s = s.replace(/&lt;/g, "<");
		s = s.replace(/&gt;/g, ">");
		s = s.replace(/&nbsp;/g, " ");
		s = s.replace(/'/g, "\'");
		s = s.replace(/&quot;/g, "\"");
		s = s.replace(/<br>/g, "\n");
		s = s.replace("<br/>", "\n");

		return s;
	},
	"bindlocateMenu":function(){//绑定菜单定位事件.
		$(".headerMenu").bind("click",function(){
			var topMenu=$(this).find(".headerNav > ul")
		});
	},
	"URLencode":function (sStr) //对URL传参的字符串进行编码处理.
	{
	    return escape(sStr).replace(/\+/g, '%2B').replace(/\"/g,'%22').replace(/\'/g, '%27').replace(/\//g,'%2F');
	}
};
$(function(){
	//二级菜单鼠标悬浮事件   有5个一级菜单   
	//我的
	$("#mm1").bind({
		mouseenter : function() {//鼠标悬浮事件.
			 $("#nav_erji_1").css("display" ,"block")
		}
		,mouseleave : function() {//鼠标离开事件.
			 $("#nav_erji_1").css("display" ,"none")
		}
	});
	// 联系人
	$("#mm2").bind({
		mouseenter : function() {//鼠标悬浮事件.
			 $("#nav_erji_2").css("display" ,"block")
		}
		,mouseleave : function() {//鼠标离开事件.
			 $("#nav_erji_2").css("display" ,"none")
		}
	});
	// 群组
	$("#mm"+3).bind({
		mouseenter : function() {//鼠标悬浮事件.
			 $("#nav_erji_3").css("display" ,"block")
		}
		,mouseleave : function() {//鼠标离开事件.
			 $("#nav_erji_3").css("display" ,"none")
		}
	});
	// 机构
	$("#mm"+4).bind({
		mouseenter : function() {//鼠标悬浮事件.
			 $("#nav_erji_4").css("display" ,"block")
		}
		,mouseleave : function() {//鼠标离开事件.
			 $("#nav_erji_4").css("display" ,"none")
		}
	});
	
	//绑定三级菜单的鼠标悬浮事件.
	$(".mainmenu_rbg > ul > li").hover(function(){
		var thirdNav = $(this).find('.nav_third');
		if (!thirdNav.hasClass('hide')) {
			$(this).find('.nav_third').animate({opacity:1, height:'show'},300);	
		}
	},function(){										
		$('.nav_third').stop(true,true).hide();					
	});
});