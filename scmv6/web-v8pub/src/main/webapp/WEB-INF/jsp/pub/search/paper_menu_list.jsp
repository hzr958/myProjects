<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:choose>
    <c:when test="${pubListVO.searchMenu}"><!-- 需要显示左侧分类栏 -->
        <input type="hidden" id="listWithMenu" value="true"/><!-- 用于标识当前是否是和左侧分类菜单栏一起显示 -->
        <div id="left_menu">
          <%@ include file="/WEB-INF/jsp/pub/search/paper_leftmenu.jsp"%><!-- 引入左侧菜单分类栏页面 -->
        </div>
        <div id="content_list">
          <%@ include file="/WEB-INF/jsp/pub/search/paper_list.jsp"%><!-- 引入成果列表 -->
        </div>
    </c:when>
    <c:otherwise><!-- 只显示成果列表 -->
        <%@ include file="/WEB-INF/jsp/pub/search/paper_list.jsp"%><!-- 引入成果列表 -->
    </c:otherwise>
</c:choose>
