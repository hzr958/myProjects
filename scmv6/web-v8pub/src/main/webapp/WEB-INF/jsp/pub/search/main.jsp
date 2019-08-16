<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=10;IE=9; IE=8; IE=EDGE" />
<meta name="format-detection" content="telephone=no">
<title>人员检索</title>
<link href="${resmod }/css/home.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/common.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/public.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/header_sns.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/smate-pc/new-mypaper/common.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/smate-pc/new-mypaper/public2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
  <script src="${resmod }/js/jquery.js"></script>
  <script type="text/javascript" src="${resmod}/js/plugin/jquery-migrate-1.2.1.min.js"></script>
  <script type="text/javascript" src="${resmod}/js/msgbox/msgbox.tip_${locale}.js"></script>
  <script type="text/javascript" src="${resmod}/js/msgbox/msgbox.common.js"></script>
  <script type="text/javascript" src="${resmod }/js/search/PsnSearch.js"></script>
  <script type="text/javascript" src="${resmod }/js/search/search.message_${locale }.js"></script>
  <script type="text/javascript" src="${resmod }/js/smate.maint.js"></script>
  <script type="text/javascript" src="${ressns}/js/dyn/main/main.base_${locale }.js"></script>
  <script type="text/javascript" src="${ressns}/js/dyn/main/main.base.js"></script>
  <script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
  <script type="text/javascript">
var snsctx="${snsctx}";
var msgBoxIsClose = parseInt(MsgBoxUtil.getCookie("msgBoxIsClose") == null? 0 : MsgBoxUtil.getCookie("msgBoxIsClose")); //消息弹出框是否点击了关闭.
</script>
  <script type="text/javascript">
    $(document).ready(function(){ 	
    	var searchString =$("#searchString").val();
    	if(searchString!=""){
    		var data = {"searchString":searchString};
    		PsnSearch.ajaxlist(data);
    	}
    	//SCM-13624
    	$("#search_some_one_form").attr("action","/pub/search/psnsearch");	
        if(document.getElementsByClassName("header__nav")){
            document.getElementById("num1").style.display="flex";
            document.getElementsByClassName("header-main__box")[0].removeChild(document.getElementById("num2"));
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
	$("#search_some_one").val($("#searchString").val());  
	$("#search_some_one_form").attr("action",url);
	$("#search_some_one_form").submit();
}
</script>
</head>
<body>
  <div class="result_class">
    <div class="result_class_wrap">
      <ul>
        <li id="search_paper" onclick="select_search_menu('/pub/search/pdwhpaper');"><a href="javascript:void(0);"><spring:message
              code='pub.menu.search.paper' /></a></li>
        <li id="search_patent" onclick="select_search_menu('/pub/search/pdwhpatent');"><a
          href="javascript:void(0);"><spring:message code='pub.menu.search.patent' /></a></li>
        <li id="search_person" class="cur" onclick="select_search_menu('/pub/search/psnsearch');"><a
          href="javascript:void(0);"><spring:message code='pub.menu.search.person' /></a></li>
        <li id="search_ins" onclick="select_search_menu('/prjweb/outside/agency/searchins');"><a href="javascript:void(0);"><spring:message
              code="ins.serach.title" /></a></li>
        <!-- <li><a href="#">基金3</a></li>
        <li><a href="#">期刊</a></li> -->
      </ul>
    </div>
  </div>
  <div id="content">
    <input id="searchString" type="hidden" value="${searchString }" />
    <div id="psnList"></div>
  </div>
  <div class="clear_h20"></div>
  <div id="psnList"></div>
</body>
</html>
