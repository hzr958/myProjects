<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<div class="m-bottom">
  <c:if test="${pubOperateVO.hasLogin}">
    <div class="m-bottom_wrap" id="pubReplyBox" style="display: flex; align-items: center;">
     
      <div class="input_box" style=" width: 85%; margin: 0px;">
        <textarea autofocus placeholder="添加评论" id="pubReplyContent" maxlength="250" rows="1"
          style="white-space: pre-wrap; text-indent: 0px; border: none;"></textarea>
      </div>
       <a href="javascript:;" id="scm_send_comment_btn" class="not_point" onclick="doPubComment()" style="width: 15%;">发布</a>
    </div>
  </c:if>
  <c:if test="${!empty pubOperateVO.hasLogin && !pubOperateVO.hasLogin }">
    <jsp:include page="/WEB-INF/jsp/mobile/bottom/mobile_outside_footer.jsp"></jsp:include>
  </c:if>
</div>