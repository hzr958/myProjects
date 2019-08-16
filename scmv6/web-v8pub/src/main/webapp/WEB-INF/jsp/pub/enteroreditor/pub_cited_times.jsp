<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%-- <%@ include file="/common/taglibs.jsp"%> --%>
<!-- 引用数 -->
<div class="handin_import-content_container-center" style="align-items: flex-start;">
  <div class="handin_import-content_container-left" style="margin-top: 6px;">
    <span style="color: red;"></span><span><spring:message code="pub.enter.citations" /></span>
  </div>
  <div class="handin_import-content_container-right">
    <div class="handin_import-content_container-right_input error_import-tip_border">
      <input type="text" class="dev-detailinput_container full_width json_citations"
        <c:if test="${pubVo.pubType == 3 || pubVo.pubType == 4}">
                placeholder='<spring:message code="pub.enter.citations.msg"/>' title='<spring:message code="pub.enter.citations.msg"/>'
            </c:if>
        maxlength="5" name="citeTimes"
        <c:if test="${pubVo.citations > 0}">
                value="${pubVo.citations}"
            </c:if>>
    </div>
    <div class="json_citations_msg" style="display: none"></div>
  </div>
</div>
