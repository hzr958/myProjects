<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>合作者推荐</title>
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/jquery.autoword.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/jquery.scmtips.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/jquery.complete.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/jquery.thickbox.css" />
<script type="text/javascript" src="${resmod}/js/plugin/jquery.autoword.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.scmtips.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.complete.overloaded.js"></script>
<script type="text/javascript" src="${resmod}/js/jquery.complete.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.thickbox.resources.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.thickbox.min.js"></script>
<script type="text/javascript" src="${resmod}/js/cooperatorCmd.js"></script>
<script type="text/javascript">
var urlType = '${urlType}';
var menuId = '${menuId}';
var ctxpath = "${snsctx}";
var locale = "${locale}"; 
var ressns = "${ressns}";
var snsctx = "${snsctx}";
var domainrol = "${domainrol}";

var autoword;
var networkExc = '<s:text name="cooperator.tip.network.Exc" />';
var noLogin = '<s:text name="cooperator.tip.noLogin" />';
var reminder = '<s:text name="cooperator.tip.reminder" />';
var keywordReq = '<s:text name="cooperator.tip.keyword.noBlank" />';
var sendFriendSuccess = '<s:text name="cooperator.tip.sendFriendSuccess" />';
var sendFriendError = '<s:text name="cooperator.tip.sendFriendError" />';
var sendFriendLabel = '<s:text name="cooperator.tip.sendFriendLabel" />';
$(document).ready(function(){
	<c:if test="${empty title && empty abs }">
		//document.location.href="${ctx}${urlType}/cooperatorCmd/main?menuId=${menuId }";
		document.location.href="/scmmanagement/cooperatorCmd/main?mmId="+${mmId}+"&selectedId="+${selectedId};
	</c:if>
	autoword = $("#keys_div").autoword({
		"words_max":10,
		"select":$.auto["key_word"],
		"onlyText":true,
	 	"watermark_flag":true,
      "watermark_tip":'<s:text name="cooperator.label.watermark.keyword.tip"/>'
	});
	<s:iterator value="keywordsList">
	   autoword.put('${dicId}','${fn:replace(keyword, "\'", "\\\'")}');
	</s:iterator>
	$('#loadFlag_hidden').val(0);
	CooperatorCmd.loadCooperator(1, '${page.pageSize}');
});
</script>
</head>
<body>
  <form id="refForm" action="${ctx}/reference/relatedReference/detail?menuId=4" method="post">
    <input type="hidden" id="ref_title" name="title" /> <input type="hidden" id="ref_abs" name="pubAbstract" /> <input
      type="hidden" id="ref_keywordStr" name="keywordStr" />
  </form>
  <form action="${ctx}/paper/journal/extractKeyWord?menuId=4" method="post" id="jnlForm">
    <input type="hidden" id="jnl_title" name="title" /> <input type="hidden" id="jnl_digest" name="digest" /> <input
      type="hidden" id="jnl_keywordStr" name="keywordStr" />
  </form>
  <form id="mainForm" method="post">
    <input type="hidden" id="title_hidden" name="title" value="" /> <input type="hidden" id="abs_hidden" name="abs"
      value="" /> <input type="hidden" id="selectedId_hidden" name="selectedId" value="${selectedId}" /> <input
      type="hidden" id="mmId_hidden" name="mmId" value="${mmId}" />
  </form>
  <div id="content">
    <div class="peer">
      <div class="keyword_search mdown15">
        <div class="peer_title">
          <span> <i class="icon21 py-icon"></i> <c:choose>
              <c:when test="${urlType eq '/fund' }">
                <s:text name="cooperator.label.min.title1" />
              </c:when>
              <c:otherwise>
                <s:text name="cooperator.label.min.title3" />
              </c:otherwise>
            </c:choose>
          </span>
          <div class="show_box">
            <a onclick="CooperatorCmd.hideSearch(this)" id="search_hidelink" class="Blue" style="cursor: pointer;"><s:text
                name="cooperator.label.hideCondition" /><i class="shear_head-up mleft3"></i></a> <a
              onclick="CooperatorCmd.showSearch(this)" id="search_showlink" class="Blue"
              style="cursor: pointer; display: none;"><s:text name="cooperator.label.showCondition" /><i
              class="shear_head-down mleft3"></i></a>
          </div>
        </div>
        <div class="peer_table" id="peer_table">
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <c:if test="${!empty title }">
              <tr>
                <td width="8%" align="right" valign="top"><strong><s:text name="cooperator.label.title" /></strong></td>
                <td align="left">
                  <div id="title_td" style="width: 840px;">
                    <s:property value="title" />
                  </div>
                </td>
              </tr>
            </c:if>
            <c:if test="${!empty abs }">
              <tr>
                <td width="8%" align="right" valign="top"><strong><s:text name="cooperator.label.abs" /></strong></td>
                <td align="left">
                  <div class="width_contant" style="border: 1px solid #cccccc;">
                    <pre id="abs_td"
                      style="white-space: pre-wrap; /* css-3 */ white-space: -moz-pre-wrap; /* Mozilla, since 1999 */ white-space: -pre-wrap; /* Opera 4-6 */ white-space: -o-pre-wrap; /* Opera 7 */ word-wrap: break-word;">
                      <s:property value="abs" />
                    </pre>
                  </div>
                </td>
              </tr>
            </c:if>
            <tr>
              <td>&nbsp;</td>
              <td height="25" align="left"><a class="uiButton text14" onclick="CooperatorCmd.editTitleAndAbs();"
                style="padding: 4px 15px; course: pointer;" title="<s:text name='cooperator.btn.edit' />"><s:text
                    name="cooperator.btn.edit" /></a></td>
            </tr>
          </table>
          <div class="t_line">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tbody>
                <tr>
                  <td width="8%" align="right" valign="top"><strong><s:text
                        name="cooperator.label.keyword" /></strong></td>
                  <td align="left">
                    <div style="width: 840px;" id="keys_div" class="auto_word_outer_div"></div>
                    <p class="f666 mtop5">
                      <s:text name="cooperator.label.keyword.tip" />
                    </p>
                  </td>
                </tr>
                <tr>
                  <td align="right">&nbsp;</td>
                  <td height="35" align="left"><a onclick="CooperatorCmd.reCmdCooperatorEx();"
                    class="uiButton uiButtonConfirm text14" style="padding: 4px 15px; cursor: pointer;"
                    title="<s:text name='cooperator.btn.recommend.again' />"><s:text
                        name="cooperator.btn.recommend.again" /></a></td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
    <%-- <div class="Menubox">
        <ul>
          <li id="two1" onclick="CooperatorCmd.reCmdJournalEx();"><s:text name="sns.recommend.jnl"/></li>
          <li id="two2" class="hover"><s:text name="sns.recommend.cpt"/></li>
          <li id="two3" onclick="CooperatorCmd.reCmdRefEx();"><s:text name="sns.recommend.reltref"/></li>
        </ul>
   </div> --%>
    <div class="clear_h20"></div>
    <div>
      <h2 style="font-size: 14px; margin-bottom: 10px;">
        <i class="icon21 py-icon"></i>
        <c:choose>
          <c:when test="${urlType eq '/fund' }">
            <s:text name="cooperator.label.table.min.title1" />
          </c:when>
          <c:otherwise>
            <s:text name="cooperator.label.min.title3" />
          </c:otherwise>
        </c:choose>
      </h2>
      <div id="con_one_tip">
        <img src="${res}/images_v5/loading_img.gif" style="vertical-align: middle;" />&nbsp;
        <s:text name="cooperator.label.loading.tip" />
      </div>
      <div id="con_one_page"></div>
    </div>
  </div>
  <input type="hidden" id="keywordsJson_hidden" value="<c:out value='${keywordJson}'/>" />
  <input type="hidden" id="discId_hidden" value="${discId}" />
  <input type="hidden" id="loadFlag_hidden" value="${loadFlag}" />
</body>
</html>