<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div id="sm_header">
  <%--系统logo --%>
  <div class="sm_logo" style="margin-top: 5px;">${pageTopLeftContent}</div>
  <div class="sm_top_menu">
    <a id="loginUrlId" href="/scmwebsns/index?service=${curDomain}">登录</a>
    <!-- <a href="/inspg/inspgmain">登录</a>  -->
    <span>|</span>
    <%--帮助中心 --%>
    <c:if test="${locale=='zh_CN'}">
      <a href="${res}/html/helpCenter/index.html" target="_blank"> <s:text name="home.index.helpCenter" />
      </a>
    </c:if>
    <c:if test="${locale=='en_US'}">
      <a href="${res}/html/helpCenter/index_en_US.html" target="_blank">&nbsp;Learning Center&nbsp;</a>
    </c:if>
    <span>|</span>
    <%--在线客服 --%>
    <a href="javascript:void(0)"
      onclick="javascript:window.open('http://crm2.qq.com/page/portalpage/wpa.php?uin=800018382&amp;cref=${pageContext.request.requestURL}&amp;ref=&amp;pt=scholarmate kefu&amp;f=1&amp;ty=1&amp;ap=&amp;as=&amp;aty=&amp;a=', '_blank', 'height=544, width=644,toolbar=no,scrollbars=no,menubar=no,status=no');">
      <s:text name="home.index.online" />
    </a>
    <!-- <script charset="utf-8" type="text/javascript"
			src="http://wpa.b.qq.com/cgi/wpa.php?key=XzgwMDAxODM4Ml8yNDEzNTFfODAwMDE4MzgyXw"></script>
		<span> -->
  </div>
</div>