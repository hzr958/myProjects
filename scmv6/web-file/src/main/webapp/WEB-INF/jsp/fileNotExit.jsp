<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jstl/xml_rt" prefix="x"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EDGE, chrome=1" />
<meta http-equiv="content-style-type" content="text/css" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link href="${resmod}/mobile/css/common.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css" />
<link href="${resmod}/css/public.css" rel="stylesheet" type="text/css" />
<link href="/resmod/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<title><s:text name="skin.main.title_scm" /></title>
<script type="text/javascript" src="/resscmwebsns/js_v5/jquery.js"></script>
<link href="${resmod }/css_v5/css2016/newfirstpage.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css_v5/css2016/footer2016.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
    $(function(){
        document.getElementsByClassName("psn-check__tip")[0].style.minHeight = window.innerHeight - 208 + "px";
    })
	var checkPage = function(){
  		if(/ios|symbianos|windows mobile|windows ce|ucweb|rv:1.2.3.4|midp|android|webos|iphone|ipad|ipod|blackberry|iemobile|opera mini/i.test(navigator.userAgent.toLowerCase())) {
  			$("#dev_mid").show();
  		} else {
  			$("#dev_pc").show();
  		}
  	}
  	window.onload=checkPage;
  	function backUrl(){
  	  if (window.history.length > 1) {
  	    window.history.go(-1);
  	    return;
  	  }
  	  document.location.href=document.referrer;
  	}
</script>
<style>
  .scholarmate-code img{
     width: 85px!important;
     height: 85px!important;
  }
</style>
</head>
<body >
  <!-- pc端 -->
  <div id="dev_pc" style="display: none;">
    <div class="psn-check__tip" style="display: flex;">
      <div class="psn-check__icon"></div>
      <span><s:text name="fileDownLoad.label.fileNotExit" /></span> <a style="cursor: pointer"> <span
        class="psn-checktitle__onload" onclick="javascript:backUrl();"><s:text
            name="fileDownLoad.label.backPrePage" /></span>
      </a>
    </div>
    <div><jsp:include page="/skins_v6/footer_infor.jsp" /></div>
  </div>
  <!-- 移动端 -->
  <div id="dev_mid" style="display: none;">
    <div class="m-top">
      <a href="javascript:;" onclick="window.history.go(-1);" class="fl"><i class="material-icons navigate_before"></i></a>返回
    </div>
    <!-- <div class=" noRecord" style="margin-top:-40px"> -->
    <div class="noRecord">
      <div class="content">
        <div class="no_effort">
          <h2 class="tc">
            <s:text name="fileDownLoad.label.fileNotExit" />
          </h2>
          <div class="no_effort_tip pl27"></div>
        </div>
      </div>
    </div>
  </div>
  
  
</body>
</html>