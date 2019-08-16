<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=10;IE=9; IE=8; IE=EDGE" />
<title>人员检索</title>
<link href="${resmod }/css/home.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/common.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/public.css" rel="stylesheet" type="text/css" />
<link href="${resscmsns }/css_v5/css2016/public2016.css" rel="stylesheet" type="text/css" />
<link href="${resscmsns }/css_v5/css2016/header2016.css" rel="stylesheet" type="text/css" />
<link href="${resscmsns }/css_v5/css2016/footer2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/header_sns.css" rel="stylesheet" type="text/css" />
<script src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="${resmod}/js/msgbox/msgbox.tip_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/msgbox/msgbox.common.js"></script>
<script type="text/javascript" src="${resmod }/js/search/PsnSearch.js"></script>
<script type="text/javascript" src="${resmod }/js/smate.maint.js"></script>
<script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
<script type="text/javascript">
var searchType="<s:text name='page.main.search' />";
var searchType2="<s:text name='page.main.search2' />";
var searchType3="<s:text name='page.main.search3' />";
var snsctx="${snsctx}";
var msgBoxIsClose = parseInt(MsgBoxUtil.getCookie("msgBoxIsClose") == null? 0 : MsgBoxUtil.getCookie("msgBoxIsClose")); //消息弹出框是否点击了关闭.
</script>
<script type="text/javascript">
    $(document).ready(function(){ 	
    	//绑定检索人员标签响应事件.
    	ScmMaint.searchSomeOneBind(); 
    	ScmMaint.searchWater(searchType);
    	var href=window.location.href;
    	$("#to_login").attr("href","${snsctx}/index?service="+href);
    	//加载消息提示相关信息.
    	ScmMaint.bindMainLabels();
    	if("${login}"=='true'){
    		MsgBoxUtil.showMsgTip();
    	}
 
    	//初始化页面标签的显示状态和控制信息.
    	$("#divselect1").live({//动态消息响应事件.
			"mouseenter" : function() {
					$("#home_search_items").animate({opacity:1, height:'show'},{queue: false},300);
			},
			"mouseout" : function(e) {
				$("#home_search_items").bind({//动态消息提示框响应事件.
					"mouseenter" : function() {
						$("#home_search_items").animate({opacity:1, height:'show'},{queue: false},300);
					},
					mouseleave : function() {//鼠标离开事件.
						setTimeout(function(){
							$("#home_search_items").stop(true,true).hide();
						},1000);
					}
				});
			},
			"blur":function(){
				 $("#home_search_items").stop(true,true).hide();
			}
		});
    	var searchString =$("#searchString").val();
    	if(searchString!="" && searchString!=searchType && searchString!=searchType2 && searchString!=searchType3){
    		/* $("#search_some_one").css("color","#000000"); */
    		$("#search_some_one").val(searchString);
    		var data = {"searchString":searchString};
    		PsnSearch.ajaxlist(data);
    	}
    })	
</script>
<script type="text/javascript">
var page = {};
page.submit = function(p){
	var pageSize =$("#pageSize").val();
	if(!p || !/\d+/g.test(p))
  	  	p = 1;
	var searchString = $.trim($("#searchString").val());
	var data = {"searchString":searchString ,
			    "page.pageSize":pageSize  ,
			    "page.pageNo":p     
			     };
	
	PsnSearch.ajaxlist(data); 
};

page.topage = function(){
	var  toPage = $.trim($("#toPage").val()) ;
	var pageSize =$("#pageSize").val();
	if(!/^\d+$/g.test(toPage))
		toPage = 1;
	
	toPage   =Number(toPage) ;
	var totalPages = Number( $("#totalPages").val()  );
	
	if(toPage > totalPages){
		toPage = totalPages ;
	}else if(toPage<1){
		toPage = 1 ;
	}
	var searchString = $.trim($("#searchString").val());
	var data = {"searchString":searchString ,
		        "page.pageSize":pageSize  ,
		        "page.pageNo":toPage     
		     };
	 PsnSearch.ajaxlist(data); 
};

function select_search_menu(url){
	var userInfo=$("#search_some_one").val();
	if(userInfo==searchType || userInfo==searchType2 || userInfo==searchType3){
		$("#search_some_one").val("");
	}
	$("#search_some_one").val($("#searchString").val());  
	$("#search_some_one_form").attr("action",url);
	$("#search_some_one_form").submit();
}
</script>
</head>
<body>
  <div class="top-t">
    <div class="header-t">
      <div class="header_wrap-t">
        <a href="${snsctx }/main" class="logo fl"></a>
        <div class="search fl">
          <div id="divselect1" class="fl">
            <span class="tp_box"><i class="ry_icon" id="search_type_img"></i></span>
            <ul id="home_search_items" style="display: none">
              <li onclick="ScmMaint.select_search_paper()"><a href="javascript:void(0);" selectid="1"><i
                  class="lw_icon"></i>论文</a></li>
              <li onclick="ScmMaint.select_search_patent()"><a href="javascript:void(0)" selectid="2"><i
                  class="zl_icon"></i>专利</a></li>
              <li onclick="ScmMaint.select_search_psn()"><a href="javascript:void(0)" selectid="3"><i
                  class="ry_icon"></i>人员</a></li>
            </ul>
          </div>
          <form id="search_some_one_form" action="/psnweb/search" method="get">
            <input id="search_some_one" name="searchString" title="<s:text name='page.main.search' />" type="text" /> <input
              id="des3PsnIdSearch" name="des3PsnId" type="hidden" value="${des3PsnId }" />
          </form>
          <input id="searchString" type="hidden" value="${searchString }" /> <a href="javascript:void(0)" class="s_btn"
            onclick="ScmMaint.searchSomeOne()"></a>
        </div>
        <s:if test="login=='true'">
          <div class="sm_top_menu" style="margin-top: 10px; float: right; color: #666;">
            <span id="psnBox" style="position: relative; color: black;"> <%--用户名 --%>
              <div
                style="display: inline-block; text-align: right; text-overflow: ellipsis; overflow: hidden; white-space: nowrap; position: relative; top: 5px;">
                <%-- <s:text name="page.main.welcome"></s:text> --%>
                <a class="sm_top_name Blue" id="link-setting"> <%--修改了名称的显示方式，限制只显示20个字符_MJG_ --%> <img alt=""
                  src="${avatars }" width="40px"></img></a>
              </div> <%--账户设置 --%>
              <div class="quit-nav2" id="quit-nav" style="display: none">
                <ul>
                  <li><a href="/psnweb/homepage/show" title="<s:text name='page.main.editPage' />"><i
                      class="py-icon icon24"></i> <s:text name='page.main.editPage' /></a></li>
                  <li><a href="/psnweb/psnsetting/main" title="<s:text name='page.main.selfSetting' />"><i
                      class="usersetting_icon"></i> <s:text name='page.main.selfSetting' /></a></li>
                  <c:if test="${userRolData.rolMultiRole && userRolData.rolInsId != 0}">
                    <li><a target="_self"
                      href="http://${userRolData.rolDomain }/scmwebrol/select-user-role?switch=yes"><i
                        class="change_icon"></i> <s:text name="skin.main.switchrole" /></a></li>
                  </c:if>
                  <li><a href="#" onclick="ScmMaint.logoutSys()" title="<s:text name='skins.main.jsp.logout' />">
                      <i class="py-icon icon25"></i> <s:text name="skins.main.jsp.logout" />
                  </a></li>
                </ul>
              </div> <i class="top_useimg"></i>
            </span> <span>|</span>
            <%--消息中心 --%>
            <b id="msg_tip_zone" style="position: relative;"> <a id="msg_tip_box" href="${snsctx}/msgbox/smsMain">
                <s:text name="shins.main.jsp.label.msgCenter" /> <span id="main_msg_total_label" class="red"></span>
            </a> <span class="Orange" id="msg_tip_totalTop" style="display: none;">${unReadTotal }</span>
              <div id="msgTip_div" style="position: absolute; top: 18px; top: 8px\0; right: -145px; opacity: 1;"></div>
            </b> <span>|</span>
            <%--在线客服 --%>
            <a href="javascript:void(0)"
              onclick="javascript:window.open('http://crm2.qq.com/page/portalpage/wpa.php?uin=800018382&amp;cref=${pageContext.request.requestURL}&amp;ref=&amp;pt=scholarmate kefu&amp;f=1&amp;ty=1&amp;ap=&amp;as=&amp;aty=&amp;a=', '_blank', 'height=544, width=644,toolbar=no,scrollbars=no,menubar=no,status=no');"><s:text
                name='page.main.online' /></a> <span>|</span>
            <%--切换语言 --%>
            <a id="page_language" onclick="ScmMaint.changeLanguage(${locale=='en_US'}?'zh_CN':'en_US')"> <c:if
                test="${locale=='zh_CN'}">English</c:if> <c:if test="${locale=='en_US'}">中文版</c:if>
            </a>
            <div class="clear_h20"></div>
          </div>
        </s:if>
        <s:else>
          <div class="login fr">
            <a href="#" class="ky_top"></a>
            <p>
              <a id="to_login" href="${snsctx }/index">请登录</a> <a href="${snsctx }/register/register">免费注册</a>
            </p>
          </div>
        </s:else>
        <!--<div class="logining">
          <div class="mana_r fl">
            <a href="" class="mana_t">李家诚<i class="top_slt"></i></a>
            <div class="mana_p">
              <a href=""><i class="mana_icon pson_icon"></i>个人信息</a>
              <a href=""><i class="mana_icon use_icon"></i>账号管理</a>
              <a href=""><i class="mana_icon out_icon"></i>退出系统</a>
            </div>
          </div>
          <div class="logining_icon"><a href="#"><i class="material-icons chat">&#xe0b7;</i><span>9</span></a></div>
          <div class="logining_icon"><a href="#"><i class="material-icons help_outline">&#xe8fd;</i></div>
      </div>-->
      </div>
    </div>
    <div class="result_class">
      <div class="result_class_wrap">
        <ul>
          <li id="search_paper" onclick="select_search_menu('/pub/search/pdwhpaper');"><a
            href="javascript:void(0);">论文</a></li>
          <li id="search_patent" onclick="select_search_menu('/pub/search/pdwhpatent');"><a
            href="javascript:void(0);">专利</a></li>
          <li id="search_person" class="cur" onclick="select_search_menu('/psnweb/search');"><a
            href="javascript:void(0);">人员</a></li>
          <li id="search_ins" onclick="select_search_menu('/prjweb/outside/agency/searchins');"><a
            href="javascript:void(0);">机构</a></li>
          <!-- <li><a href="#">基金</a></li>
        <li><a href="#">期刊</a></li> -->
        </ul>
      </div>
    </div>
  </div>
  <div class="clear_h20"></div>
  <div id="content">
    <div id="psnList"></div>
  </div>
  <div class="clear_h20"></div>
  <div id="psnList"></div>
</body>
</html>
