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
<link href="${resmod}/css_v5/css2016/newfirstpage.css" rel="stylesheet" type="text/css" />
<title><s:text name="skin.main.title_scm" /></title>
<script type="text/javascript" src="/resscmwebsns/js_v5/jquery.js"></script>
<script type="text/javascript">
  	var checkPage = function(){
  		if(/ios|symbianos|windows mobile|windows ce|ucweb|rv:1.2.3.4|midp|android|webos|iphone|ipad|ipod|blackberry|iemobile|opera mini/i.test(navigator.userAgent.toLowerCase())) {
  			$("#dev_mid").show();
  		} else {
  			$("#dev_pc").show();
  		}
  	}
  	window.onload=checkPage;
  	</script>
<%@ include file="/common/meta.jsp"%>
</head>
<body>
  <!-- pc端 -->
  <div id="dev_pc" style="display: none;">
    <div id="main" class="psn-check__tip" style="padding-left: 15px; padding-right: 15px;">
      <div class="psn-check__icon">
        <span><s:text name="fileDownLoad.label.fileWithFriend"></s:text></span> <a style="cursor: pointer"> <span
          class="psn-checktitle__onload" onclick="javascript:history.back(-1);"><s:text
              name="fileDownLoad.label.backPrePage"></s:text></span>
        </a>
      </div>
    </div>
  </div>
  <!-- 移动端 -->
  <div id="dev_mid" style="display: none;">
    <div class="m-top">
      <a href="javascript:;" onclick="window.history.go(-1);" class="fl"><i class="material-icons navigate_before"></i></a>返回
    </div>
    <div class=" noRecord" style="margin-top: -40px">
      <div class="content">
        <div class="no_effort">
          <h2 class="tc">对不起，您没有下载此文档的权限！</h2>
          <div class="no_effort_tip pl27"></div>
        </div>
      </div>
    </div>
  </div>
</body>
</html>