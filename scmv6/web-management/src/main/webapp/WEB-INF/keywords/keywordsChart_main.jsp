<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>合作者推荐</title>
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/jquery.scmtips.css" />
<script type="text/javascript" src="${resmod}/js/plugin/jquery.scmtips.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.watermark.js"></script>
<script type="text/javascript" src="${resmod}/js/cooperatorCmd.js"></script>
<script type="text/javascript">
var titleReq = '<s:text name="cooperator.tip.title.noBlank" />';
var absReq = '<s:text name="cooperator.tip.abs.noBlank" />';
var formReq = '<s:text name="cooperator.tip.titleAndAbs.noBlank" />';
var title_watermark = '<s:text name="cooperator.label.watermark.title.tip3" />';
var abs_watermark = '<s:text name="cooperator.label.watermark.abs.tip3" />';
<c:if test="${urlType eq '/fund'}">
title_watermark = '<s:text name="cooperator.label.watermark.title.tip1" />';
abs_watermark = '<s:text name="cooperator.label.watermark.abs.tip1" />';
</c:if>
$(document).ready(function() {
	$('#title_inp').watermark({
		tipCon : title_watermark
	});
	
	$('#abs_area').watermark({
		tipCon : abs_watermark
	});
});
</script>
</head>
<body>
  <div id="content">
    <form id="mainForm" action="/scmmanagement/researchkws/detail" method="post">
      <input type="hidden" id="title_hidden" name="title" value="" /> <input type="hidden" id="abs_hidden" name="abs"
        value="" /> <input type="hidden" id="selectedId_hidden" name="selectedId" value="${selectedId}" /> <input
        type="hidden" id="mmId_hidden" name="mmId" value="${mmId}" />
    </form>
    <div id="mian_box" class="peer">
      <div class="keyword_search mdown15">
        <div class="peer_title">
          <span> <i class="icon21 py-icon"></i> <c:choose>
              <c:when test="${urlType eq '/fund' }">
                <s:text name="cooperator.label.min.title1" />
              </c:when>
              <c:otherwise>
                <s:text name="cooperator.label.min.title4" />
              </c:otherwise>
            </c:choose>
          </span>
        </div>
        <div class="peer_table" id="peer_table">
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="8%" align="right" valign="top"><strong><s:text name="cooperator.label.title" /></strong></td>
              <td align="left"><input id="title_inp" type="text" class="inp_text" style="width: 840px;"
                value="<s:property value='title' />" /></td>
            </tr>
            <tr>
              <td align="right" valign="top"><strong><s:text name="cooperator.label.abs" /></strong></td>
              <td align="left"><textarea id="abs_area" class="inp_text" style="width: 840px; height: 240px;"><s:property
                    value='abs' /></textarea></td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td height="35" align="left"><c:choose>
                  <c:when test="${urlType eq '/fund' }">
                    <a class="uiButton uiButtonConfirm text14" onclick="CooperatorCmd.recommendCooperator();"
                      style="padding: 4px 15px; course: pointer;"><s:text name="cooperator.btn.recommend" /></a>
                  </c:when>
                  <c:otherwise>
                    <a class="uiButton uiButtonConfirm text14" onclick="CooperatorCmd.recommendCooperator();"
                      style="padding: 4px 15px; course: pointer;"><s:text name="cooperator.btn.kwanalysis" /></a>
                  </c:otherwise>
                </c:choose></td>
            </tr>
          </table>
        </div>
      </div>
    </div>
  </div>
</body>
</html>