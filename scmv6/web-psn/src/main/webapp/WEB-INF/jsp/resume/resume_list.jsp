<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s:if test="resumeList.size() > 0">
  <s:iterator value="resumeList" var="resume" status="st">
    <div class="resume-container__body-list" des3CvId="<iris:des3 code='${resume.resumeId}'/>">
      <div class="resume-container__body-list__num cvSeq">${st.count}</div>
      <div class="resume-container__body-list__nam">
        <a class="resume-container__body-list__nam-hover"
          onclick="PsnResume.openResumDetail('<iris:des3 code='${resume.resumeId}'/>')">${resume.resumeName}</a>
      </div>
      <div class="resume-container__body-list__tim">
        <fmt:formatDate value="${resume.updateDate}" pattern="yyyy-MM-dd" />
      </div>
      <div onclick="PsnResume.deletePsnResume('<iris:des3 code='${resume.resumeId}'/>');">
        <div class="confirm-email__body-icon"></div>
      </div>
    </div>
  </s:iterator>
</s:if>
