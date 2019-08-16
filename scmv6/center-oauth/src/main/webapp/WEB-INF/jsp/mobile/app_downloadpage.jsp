<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<title>科研之友客户端</title>
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>

<script type="text/javascript">
$(function(){
  var ua = navigator.userAgent.toLowerCase();
  if(ua.match(/MicroMessenger|QQ/i)){
    //排除掉QQ浏览器
    if(ua.match(/MQQBrowser/i)){
      $("#showTip_browser").show();
      return;
    }
    $("#showTip").show();
  }
})
function downloadApp(){
    window.location.href = "http://itunes.apple.com/us/app/id1369636776";
}
</script>
</head>
<body>
    <div class="new-download-container">
        <!-- <div  class="new-download-container_header">
            <span class="new-download-container_header-item">
                <i class="material-icons">close</i>
            </span>
            <span class="new-download-container_header-center">科研之友客户端</span>
            <span class="new-download-container_header-item">
                <i class="material-icons">more_horiz</i>
            </span>
        </div>  -->     
        <div class="new-download-container_body">

            <div class="new-download-container_body-explain" id="showTip" style="display:none">
                <div  class="new-download-container_body-explain_title">已安装科研之友客户端</div>
                <div  class="new-download-container_body-explain_item">点击右上角的<i class="material-icons">more_horiz</i></div>
                <div  class="new-download-container_body-explain_item">选在Safari浏览器中打开</div>
            </div>
            
            <div class="new-download-container_body-explain" id="showTip_browser" style="display:none">
                <div  class="new-download-container_body-explain_title">已安装科研之友客户端</div>
                <div  class="new-download-container_body-explain_item">请使用Safari浏览器打开本页面</div>
            </div>

            <div class="new-download-container_avator"></div>

            <div class="new-download-container_footer">
                <div class="new-download-container_footer-explain">未安装科研之友客户端</div>
                <div class="new-download-container_footer-download" onclick="downloadApp()">前往 App Store 下载</div>
            </div>
        
        </div>
        

    </div>
</body>
</html>
