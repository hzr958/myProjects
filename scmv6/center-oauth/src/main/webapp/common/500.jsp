<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="res" value="/resscmwebsns" />
<c:set var="resmod" value="/resmod" />
<c:set var="locale"><%=org.springframework.context.i18n.LocaleContextHolder.getLocale().toString() %></c:set>
<%@ page import="org.slf4j.Logger,org.slf4j.LoggerFactory"%>
<%
	Throwable ex = null;
	if (exception != null)
		ex = exception;
	if (request.getAttribute("javax.servlet.error.exception") != null)
		ex = (Exception) request.getAttribute("javax.servlet.error.exception");

	//记录日志
	if (ex != null) {
		Logger logger = LoggerFactory.getLogger("500.jsp");
		logger.error(ex.getMessage(), ex);
	}
	//是否是ajax请求
	String uri = (String)request.getAttribute("action_request_uri");
	if(uri!= null && uri.toLowerCase().indexOf("ajax") > -1){
		request.setAttribute("is_ajax_req", true);
	}else{
		request.setAttribute("is_ajax_req", false);
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
<title>Scholar 500-error</title>
<link href="${res}/css/public.css" rel="stylesheet" type="text/css" />
<link href="${res}/css/all.css" rel="stylesheet" type="text/css" />
<link href="${res}/css/public.css" rel="stylesheet" type="text/css" />
<link href="${res}/css/header.css" rel="stylesheet" type="text/css" />
<link href="${res}/css/footer.css" rel="stylesheet" type="text/css" />
<link href="${res}/css/page.css" rel="stylesheet" type="text/css" />
<link href="${res}/css_v5/error.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${resmod }/js_v8/common/jquery/jquery-3.4.1.js"></script>
<script type="text/javascript">
window.onload = function(){
  var sUserAgent = navigator.userAgent.toLowerCase();
  //有新的移动端系统可以接着往这添加
  var reg=/ios|symbianos|windows mobile|windows ce|ucweb|rv:1.2.3.4|midp|android|webos|iphone|ipad|ipod|blackberry|iemobile|opera mini/i;
  //系统是移动端
  if (reg.test(sUserAgent)){
    $("#mobile_error_tips").show();
    $(".pc_error_tips").hide();
  }else{
    $("#mobile_error_tips").hide();
    $(".pc_error_tips").show();
  }
}

</script>
</head>
<body>
  <fmt:bundle basename="resource.jsp500Resource">
    <c:if test="${is_ajax_req }">
      <div id="content" class="pc_error_tips" style="display:none;">
        <div style="width: 976px">
          <div class="wrongtext_box">
            <span class="font_24b">系统正忙， 请稍后再试</span><br /> <br /> 系统正忙， 请稍后再试。<br /> <a href="${ctx }">返回首页</a>
          </div>
        </div>
      </div>
    </c:if>
    <c:if test="${!is_ajax_req }">
      <div class="eror_main pc_error_tips" style="display:none;">
        <div style="margin-left: 90px;">
          <div class="error_pic2">
            <img src="${res }/images_v5/error/role_fgx2.gif" width="8" height="220" />
          </div>
          <div class="error_contant">
            <p style="font-size: 24px; font-family: '微软雅黑', '黑体', Arial; color: #ff6600; font-weight: bold;">系统正忙，
              请稍后再试。</p>
            <p>系统正忙， 请稍后再试。</p>
            <div class="error_btn">
              <a href="/oauth/index"><span class="back_icon">返回首页</span></a> <a
                onclick="javascript:window.history.back();"><span class="back_last">返回上一页</span></a>
            </div>
          </div>
          <div class="clear" style="height: 1px; overflow: hidden;"></div>
        </div>
      </div>
    </c:if>
    
    
    <!-- 移动端500错误   begin -->
    <div style="position:absolute; padding-top:50%; left:0; text-align:center; width:100%;font-size: 40px;display:none;" id="mobile_error_tips">
      <div><img src="/resmod/images_v5/error/data_error.png"></div>
      <div>系统正忙，请稍后再试</div>
      <div style="text-align: center; margin-top: 50px; color: #ca2a22;">
          <span onclick="document.location.reload();">重新加载</span>
      </div>
    </div>
    <!-- 移动端500错误   end -->
  </fmt:bundle>
</body>
</html>
