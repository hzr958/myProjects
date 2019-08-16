<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s:iterator value="page.result" var="groupPub" status="st">
  <div class="list_container" onclick="groupSelectPub.sureSelect('${groupPub.pubId}' ,'${groupPub.des3pubId}' ) ">
    <input type="hidden" id="full_image_${groupPub.pubId}" value="${groupPub.fileTypeIcoUrl}">
    <div class="pub_information">
      <div class="title" id="title_${groupPub.pubId}">${groupPub.zhTitle}</div>
      <div class="author" id="author_${groupPub.pubId}">${groupPub.authorNames}</div>
      <div class="source" id="brief_${groupPub.pubId}">${groupPub.briefDesc}</div>
    </div>
  </div>
</s:iterator>
