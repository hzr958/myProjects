<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!-- <input type="hidden" name="pageNumber" value="${pageNumber}"/> -->
<input type="hidden" name="awardCount" value="${dynMap['dyn-statis-award']}" />
<input type="hidden" name="commentCount" value="${dynMap['dyn-statis-reply']}" />
<input type="hidden" name="shareCount" value="${dynMap['dyn-statis-share']}" />
<div class="dynamic__box">${dynMap['dynamicContent']}</div>
<div class="dynamic-cmt"></div>