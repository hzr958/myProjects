<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%-- <%@ include file="/common/taglibs.jsp"%> --%>
<!-- 基金标注 -->
<div class="handin_import-content_container-center" style="align-items: flex-start;">
  <div class="handin_import-content_container-left" style="margin-top: 6px;">
    <span><spring:message code="pub.enter.fundInfo" /></span>
  </div>
  <div class="handin_import-content_container-right">
    <div class="handin_import-content_container-right_input">
      <input type="text" class="dev-detailinput_container full_width json_fundInfo" maxlength="100" name="fundInfo"
        value="${pubVo.fundInfo }" />
    </div>
    <div class="json_fundInfo_msg" style="display: none"></div>
  </div>
</div>