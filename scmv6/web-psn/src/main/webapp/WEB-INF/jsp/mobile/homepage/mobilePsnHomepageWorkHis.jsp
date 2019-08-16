<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:if test="${psnWorkList!=null && fn:length(psnWorkList)>0}">
  <li>
    <h3>工作经历</h3>
    <dl>
      <c:forEach items="${psnWorkList}" var="psnwork">
        <dd>
          <!-- <img src="${resmod }/images/mobile/u_logo01.jpg"> -->
          <c:if test="${!empty psnwork.insImgPath }">
            <img src="${psnwork.insImgPath }" onerror="this.src='${resmod }/images/mobile/u_logo01.jpg'">
          </c:if>
          <c:if test="${empty psnwork.insImgPath }">
            <img src="${resmod }/images/mobile/u_logo01.jpg">
          </c:if>
          <h4 style="margin-left: 58px;">
            ${psnwork.insName}
            <c:if test="${psnwork.department!=null&&psnwork.department!=''}">, ${psnwork.department}</c:if>
            <c:if test="${psnwork.position!=null&&psnwork.position!=''}">, ${psnwork.position}</c:if>
          </h4>
          <c:choose>
            <c:when
              test="${psnwork.fromYear !=null && psnwork.toYear!=null && psnwork.fromMonth!=null && psnwork.toMonth!=null}">
              <p style="margin-left: 61px;">${psnwork.fromYear}/${psnwork.fromMonth}-
                ${psnwork.toYear}/${psnwork.toMonth}</p>
            </c:when>
            <c:when
              test="${psnwork.fromYear!=null && psnwork.toYear!=null && psnwork.fromMonth ==null && psnwork.toMonth==null}">
              <p style="margin-left: 61px;">${psnwork.fromYear}-${psnwork.toYear}</p>
            </c:when>
            <c:when test="${ psnwork.toYear==null && psnwork.toMonth==null}">
              <p style="margin-left: 61px;">
                <c:if test="${psnwork.fromYear!=null}">${psnwork.fromYear}</c:if>
                <c:if test="${psnwork.fromMonth!=null}">/${psnwork.fromMonth}</c:if>
                - 至今
              </p>
            </c:when>
            <c:otherwise>
              <p style="margin-left: 61px;">
                ${psnwork.fromYear}
                <c:if test="${psnwork.fromMonth!=null}">/${psnwork.fromMonth}</c:if>
                - ${psnwork.toYear}
                <c:if test="${psnwork.toMonth!=null}">/${psnwork.toMonth}</c:if>
              </p>
            </c:otherwise>
          </c:choose>
        </dd>
      </c:forEach>
    </dl>
  </li>
</c:if>
