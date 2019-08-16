<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta charset="utf-8">
<title>科研之友</title>
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod}/js_v8/common/jquery/jquery-3.4.1.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/android/app_fun.js"></script>
</head>
<body>
     <div class="provision_container-title">
            <i class="material-icons" onclick="window.history.back()">keyboard_arrow_left</i>
            <span>设置</span>
            <i></i>
        </div>
    <div class="Setcontent_container—body" id="app_settings">
        <div class="Setcontent_container—body_header">
          <span class="Setcontent_container—body_header-title">账号信息</span>
          <span class="Setcontent_container—body_header-text" id="psn_account"></span>
        </div>
        <div class="Setcontent_container—body_content">
          <div  class="Setcontent_container—content_item" onclick="aboutUs();">
               <span class="Setcontent_container—content_item-left">关于我们</span>
               <span class="Setcontent_container—content_item-right"><i class="material-icons">keyboard_arrow_right</i></span>
          </div>
          <div class="Setcontent_container—content_item" style="display:none;">
               <span class="Setcontent_container—content_item-left">分享</span>
               <span class="Setcontent_container—content_item-right"><i class="material-icons">keyboard_arrow_right</i></span>
          </div>
          <div class="Setcontent_container—content_item" id="app_version" style="display:none;">
               <span class="Setcontent_container—content_item-left" style="border: none;">当前版本</span>
               <span class="Setcontent_container—content_item-right" style="border: none;" id="app_version_text"></span>
          </div>
        </div>
        
        <div class="Setcontent_container—body_outbtn" onclick="logout();">退出</div>
    </div>
</body>
<script type="text/javascript">
  var userAgent = navigator.userAgent;
  
  window.onload=function(){
    ajaxFindCurrentAccount();
    showAppVersion();
    document.getElementsByClassName("Setcontent_container—body")[0].style.height = window.innerHeight - 48 + "px"; 
  }
  
  function ajaxFindCurrentAccount(){
    $.post("/psn/ajaxaccount", {}, function(data, status, xhr){
        if(status == "success" && data.status == "success"){
          $("#psn_account").text(data.account);
        }
    }, "json");
  }
  
  function showAppVersion(){
    var version = appFun.getVersionName();
    if(version != "" && version != null && version != "null"){
      $("#app_version_text").text(version);
      $("#app_version").show();
    }
  }
  
  
  
  function aboutUs(){
    window.location.href ="/resmod/html/mobile/aboutUs.html";
  }
  
  function logout(){
    window.location.href ="/oauth/mobile/logout";
  }
</script>
</html>