<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="publication.pubView.label.pageTitle" /></title>
<meta name="description" content="${pubSEO.description}" charset="utf-8">
<c:if test="${not empty pubSEO.keywords}">
<meta name="keywords" content="${pubSEO.keywords}" charset="utf-8">
</c:if>
<link rel="stylesheet" type="text/css" href="${res}/css_v5/public.css" />
<link rel="stylesheet" type="text/css" href="${res}/css_v5/common.css" />
<link rel="stylesheet" type="text/css" href="${res}/css_v5/achievement.css" />
<link rel="stylesheet" type="text/css" href="${res}/css_v5/home/home.css" />
<link rel="stylesheet" type="text/css" href="${res}/css_v5/plugin/jquery.scmtips.css" />
<link rel="stylesheet" type="text/css" media="screen" href="${res}/css_v5/plugin/jquery.alerts.css" />
<script type="text/javascript" src="${res}/js_v5/jquery.js"></script>
<script type="text/javascript" src="${res}/js_v5/common.ui.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.alerts_${locale}.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.alerts.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.scmtips.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.winscroll.float.js"></script>
<script type="text/javascript" src="${res}/js_v5/home/publication/scholar.view.js"></script>
<script type="text/javascript" src="${res}/js_v5/home/publication/scholar.view_${locale}.js"></script>
<script type="text/javascript" src="${res}/js_v5/home/statistics/statistics.js"></script>
<script type="text/javascript">
function googleSearch(obj){
	 var title_text=getViewTitle();
 	 var url = "https://www.google.com.hk/#newwindow=1&safe=strict&site=&source=hp&q=" + encodeURIComponent(title_text);
 	 $(obj).attr('href', url);
}

function baiduSearch(obj){
	 var title_text=getViewTitle();
 	 var url = "http://www.baidu.com/s?rsv_spt=1&issp=1&rsv_bp=0&ie=utf-8&tn=baiduhome_pg&wd=" + encodeURIComponent(title_text);
 	 $(obj).attr('href', url);
}

function getViewTitle(){
    var title_text ="";
    var title_zh=$.trim($("#bdText_zh_CN").text());
    var title_en=$.trim($("#bdText_en_US").text());
    if("zh_CN"=="${locale}"){
        title_text = title_zh==""?title_en:title_zh;
    }else{
        title_text = title_en==""?title_zh:title_en;
    }
    return title_text;
} 
</script>
</head>
<body>
<body id="mainbody">
  <%@ include file="pubDetailViewCitation.jsp"%>
  <form name="mainform" id="mainform" action="" method="post">
    <input type="hidden" name="des3Id" id="des3Id" value="${pubDetailVO.des3PubId}" />
    <div class="container">
      <div class="cont01">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <c:set var="typeInfo" value="${pubDetailVO.pubTypeInfo}" />
          <%@ include file="pubbasic_public_view.jsp"%>
          <c:if test="${pubDetailVO.pubType=='1'}">
            <%@ include file="pubbasic_01_detailView.jsp"%>
          </c:if>
          <c:if test="${pubDetailVO.pubType=='2'}">
            <%@ include file="pubbasic_02_detailView.jsp"%>
          </c:if>
          <c:if test="${pubDetailVO.pubType=='3'}">
            <%@ include file="pubbasic_03_detailView.jsp"%>
          </c:if>
          <c:if test="${pubDetailVO.pubType=='4'}">
            <%@ include file="pubbasic_04_detailView.jsp"%>
          </c:if>
          <c:if test="${pubDetailVO.pubType=='5'}">
            <%@ include file="pubbasic_05_detailView.jsp"%>
          </c:if>
          <c:if test="${pubDetailVO.pubType=='7'}">
            <%@ include file="pubbasic_07_detailView.jsp"%>
          </c:if>
          <c:if test="${pubDetailVO.pubType=='8'}">
            <%@ include file="pubbasic_08_detailView.jsp"%>
          </c:if>
          <c:if test="${pubDetailVO.pubType=='10'}">
            <%@ include file="pubbasic_10_detailView.jsp"%>
          </c:if>
          <%@ include file="pubbasic_other_view.jsp"%>
        </table>
      </div>
    </div>
    <div class="clear_h20"></div>
  </form>
</body>
</html>