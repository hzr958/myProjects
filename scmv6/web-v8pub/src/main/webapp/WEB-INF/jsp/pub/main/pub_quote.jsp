<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<link type="text/css" rel="stylesheet" href="${resmod}/css/public.css" />
<link type="text/css" rel="stylesheet" href="${resmod }/css/pop.css" />
<script type="text/javascript" src="${resmod}/js/smate.share.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<script type="text/javascript" src="${resmod}/js/weixin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="${resmod}/js/sharebutton.js"></script>
<script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
<title>引用信息</title>
<script>
	function selectText(el) {
		if (window.getSelection) {
			var s = window.getSelection()
			s.selectAllChildren(el)
		} else {
			var s = document.body.createTextRange();
			s.moveToElementText(el);
			s.select();
		}
	}

	function selName(obj) {
		var content = $(obj).parent().find(
				".new-quote_container-body_item-content");
		selectText(content.get(0));
	}
</script>
</head>
<body>
  <div class="new-quote_container-body_title">
    <spring:message code="common.cite.copy" />
  </div>
  <c:forEach items="${pubQuoteList}" var="item">
    <div class="new-quote_container-body_item">
      <span class="new-quote_container-body_item-title" onclick="selName(this)">${item.quoteName}</span> <span
        class="new-quote_container-body_item-content" onclick="selectText(this)">${item.quoteContent}</span>
    </div>
  </c:forEach>
</body>