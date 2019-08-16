<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<link type="text/css" href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet">
  <!-- <div class="background-cover"> -->
<script type="text/javascript">


function show(){
    positioncenter({targetele:'new-success_save',closeele:'new-success_save-header_tip'});
}

/* function closeTip(){
	if($("#result").val()!="-2"){
	    window.open("/pub/file/importenter","_self");
	}
}; */
</script>
<input id="result" type="hidden" value="${totalResult}" />
<div class="new-success_save" id="new-success_save">
  <div class="new-success_save-body">
    <div class="new-success_save-body_avator">
      <c:if test="${totalResult==-1}">
        <img src="${resmod}/smate-pc/img/fail.png">
      </c:if>
      <c:if test="${totalResult!=-1}">
        <img src="${resmod}/smate-pc/img/pass.png">
      </c:if>
    </div>
    <div class="new-success_save-body_tip">
      <c:if test="${totalResult==-1 }">
        <span><spring:message code="referencelist.import.error" /></span>
      </c:if>
      <c:if test="${totalResult!=-1}">
        <span class="new-success_save-body_tip-num">${totalResult}</span><spring:message code="referencelist.label.count_1" />
			        </c:if>
    </div>
    <div class="new-success_save-body_footer">
      <div class="new-success_save-body_footer-complete" onclick="viewHistory();"><spring:message code="referencelist.button.finish" /></div>
      <div class="new-success_save-body_footer-continue" onclick="continueImpFile();"><spring:message code="referencelist.button.continue" /></div>
    </div>
  </div>
</div>
