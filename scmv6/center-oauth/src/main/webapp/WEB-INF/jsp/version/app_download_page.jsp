<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<meta name="keywords" content='<s:text name="page.seo.index.Keywords"/>' />
<meta name="description" content='<s:text name="page.seo.index.description"/>' />
<meta name="robots" content='<s:text name="page.seo.index.robots"/>' />
<title><s:text name="page.seo.index.title" /></title>
<link href="${resmod }/css_v5/css2016/header2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css_v5/css2016/public2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css_v5/css2016/footer2016.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${resmod }/smate-pc/css/scmpcframe.css">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod }/mobile/js/jquery-qrcode.js"></script>
<script type="text/javascript">
window.onload = function(){
  document.getElementsByClassName("new-pcdownload_app")[0].style.height = window.innerHeight + "px";
  var downloadUrl = "${domainScm}/oauth/app/download";
  //生成支付二维码
  var options = {
      render: "canvas",
      fill: '#000',//二维码颜色
      background: '#ffffff',//背景颜色
      size: 235,//大小
      left: 2,
      top: 2,
      text: downloadUrl//二维码内容
  };
  $('#smate_app_download_qrcode').empty().qrcode(options);
}
</script>
</head>
<jsp:include page="/common/header.jsp"></jsp:include>


<div class="new-pcdownload_app">
    <div class="new-pcdownload_appcontainer">
        <div class="new-pcdownload_appcontainer-icon">
            <div class="new-pcdownload_appcontainer-icon_content1">打造智慧科研平台,</div>
            <div class="new-pcdownload_appcontainer-icon_content2">帮助科研人员更成功,文案请替换</div>
        </div>
        <div class="new-pcdownload_appcontainer-func">
              <div class="new-pcdownload_appcontainer-func_title"></div>
              <div class="new-pcdownload_appcontainer-func_code" id="smate_app_download_qrcode"></div>
              <div class="new-pcdownload_appcontainer-func_btn">扫一扫,下载科研之友客户端</div>
        </div>
    </div>
</div>


<jsp:include page="/common/footer.jsp"></jsp:include>
</html>










