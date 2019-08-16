<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:if test="${psnEduList!=null && fn:length(psnEduList)>0}">
  <li>
    <h3>教育经历</h3>
    <dl>
      <c:forEach items="${psnEduList}" var="psnEdu">
        <dd>
          <!-- <img src="${resmod }/images/mobile/u_logo01.jpg"> SCM-12966 -->
          <c:if test="${!empty psnEdu.insImgPath }">
            <img src="${psnEdu.insImgPath }" onerror="this.src='${resmod }/images/mobile/u_logo01.jpg'">
          </c:if>
          <c:if test="${empty psnEdu.insImgPath }">
            <img src="${resmod }/images/mobile/u_logo01.jpg">
          </c:if>
          <h4 style="margin-left: 58px;">
            ${psnEdu.insName}
            <c:if test="${psnEdu.study!=null&&psnEdu.study!=''}">, ${psnEdu.study}</c:if>
            <c:if test="${psnEdu.degreeName!=null&&psnEdu.degreeName!=''}">, ${psnEdu.degreeName}</c:if>
          </h4>
          <c:choose>
            <c:when
              test="${psnEdu.fromYear !=null && psnEdu.toYear!=null && psnEdu.fromMonth!=null && psnEdu.toMonth!=null}">
              <p style="margin-left: 61px;">${psnEdu.fromYear}/${psnEdu.fromMonth}-
                ${psnEdu.toYear}/${psnEdu.toMonth}</p>
            </c:when>
            <c:when
              test="${psnEdu.fromYear!=null && psnEdu.toYear!=null && psnEdu.fromMonth ==null && psnEdu.toMonth==null}">
              <p style="margin-left: 61px;">${psnEdu.fromYear}-${psnEdu.toYear}</p>
            </c:when>
            <c:when test="${ psnEdu.toYear==null && psnEdu.toMonth==null}">
              <p style="margin-left: 61px;">
                <c:if test="${psnEdu.fromYear!=null}">${psnEdu.fromYear}</c:if>
                <c:if test="${psnEdu.fromMonth!=null}">/${psnEdu.fromMonth}</c:if>
                - 至今
              </p>
            </c:when>
            <c:otherwise>
              <p style="margin-left: 61px;">
                ${psnEdu.fromYear}
                <c:if test="${psnEdu.fromMonth!=null}">/${psnEdu.fromMonth}</c:if>
                - ${psnEdu.toYear}
                <c:if test="${psnEdu.toMonth!=null}">/${psnEdu.toMonth}</c:if>
              </p>
            </c:otherwise>
          </c:choose>
        </dd>
      </c:forEach>
    </dl>
  </li>
</c:if>
