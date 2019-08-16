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
<link href="${resmod}/css/public.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/public.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css" />
<link href="/resmod/css/wechat.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod}/js/common/smate.common.js"></script>
<script type="text/javascript">
    function linkOthers(){
        window.location.href="/dynweb/mobile/dynshow";
    }
    function login(){
        var service = window.location.href;
        link = "/oauth/index?service="+encodeURIComponent(service);
        window.location.href=link;
    }
</script>
</head>
<body style="background-color: white !important;">
  <div id="header" class="fund__page-header">
    <span class="fund__page-icon"><i class="material-icons"
      onclick="SmateCommon.goBack('/dynweb/mobile/dynshow');">keyboard_arrow_left</i></span> <span class="fund__page-title"
      style="margin-right: 40px !important;">项目详情</span>
  </div>
  <div class="content" style="padding-top: 15px;">
    <div class="no_effort" style="margin-top: 30px;">
      <div class="response_no-result" style="margin-top: 0px !important;">该项目由于个人隐私设置, 无法查看</div>
    </div>
  </div>
</body>
</html>