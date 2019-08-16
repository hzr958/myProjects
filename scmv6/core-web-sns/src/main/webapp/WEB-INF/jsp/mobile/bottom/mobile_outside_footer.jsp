<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript">
function loginToScmOutside(){
	service = $("#loginTargetUrl").val();
	if($("#loginTargetUrl").val() == ""){
	    BaseUtils.buildLoginUrl(window.location.href);
	    return null;
	}
	document.location.href = "/oauth/mobile/index?service="+service;
}
</script>
<input id="loginTargetUrl" name="loginTargetUrl" type="hidden" value="${loginTargetUrl }" />
<!-- <div style="height: 56px;"></div> -->
<div class="psnmain-page_footer" onclick="loginToScmOutside();">
  <div class="psnmain-page_footer-left">
    <img src="${resmod }/mobile/images/scm_icon_share.png"> <span class="psnmain-page_footer-left_detaile">科研之友</span>
  </div>
  <div class="psnmain-page_footer-right">
    <i class="material-icons">person</i> <span class="psnmain-page_footer-right_detaile">我</span>
  </div>
</div>