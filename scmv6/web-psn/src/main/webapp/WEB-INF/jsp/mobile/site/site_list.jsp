<!doctype html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<title>科研之友</title>
<link href="${resmod}/mobile/css/style.css" rel="stylesheet" type="text/css">
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js?version=2"></script>
<script type="text/javascript" src="/resmod/mobile/js/wechat.custom.js"></script>
<script>

$(document).ready(function(){
	
	
});
</script>
</head>
<body class="bg_write">
  <div class="top">
    <div class="top_mn">
      <a href="javascript:void();" onclick="window.history.back();" class="rtn_icon"></a>
      <h1 class="${fromPage}">切换到其他站点</h1>
    </div>
  </div>
  <div class="b_set"></div>
  <div style="width: 100%; min-width: 360px; height: auto;">
    <div class="project__item-container">
      <a href="https://isisn.nsfc.gov.cn/egrantweb/index-mobile">
        <div class="project__item-list">
          <div class="project__item-list__title">国家基金委</div>
          <div class="project__item-list__tip">
            <i class="material-icons">chevron_right</i>
          </div>
        </div>
      </a> <a href="https://www.innocity.com/onlineweb/mindex">
        <div class="project__item-list">
          <div class="project__item-list__title">创新城</div>
          <div class="project__item-list__tip">
            <i class="material-icons">chevron_right</i>
          </div>
        </div>
      </a> <a href="http://pro.gdstc.gov.cn/egrantweb/indexmobile">
        <div class="project__item-list">
          <div class="project__item-list__title">广东省科学技术厅</div>
          <div class="project__item-list__tip">
            <i class="material-icons">chevron_right</i>
          </div>
        </div>
      </a> <a href="http://ywgl.jxstc.gov.cn/egrantweb/indexmobile">
        <div class="project__item-list">
          <div class="project__item-list__title">江西省科技厅</div>
          <div class="project__item-list__tip">
            <i class="material-icons">chevron_right</i>
          </div>
        </div>
      </a> <a href="http://ywgl.snstd.gov.cn/egrantweb/indexmobile">
        <div class="project__item-list">
          <div class="project__item-list__title">陕西省科技厅</div>
          <div class="project__item-list__tip">
            <i class="material-icons">chevron_right</i>
          </div>
        </div>
      </a> <a href="http://61.187.87.55/egrantweb/indexmobile">
        <div class="project__item-list">
          <div class="project__item-list__title">湖南省科技厅</div>
          <div class="project__item-list__tip">
            <i class="material-icons">chevron_right</i>
          </div>
        </div>
      </a>
    </div>
  </div>
  <jsp:include page="../bottom/mobile_bottom.jsp"></jsp:include>
</body>
</html>
