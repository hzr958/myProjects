<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<ul>
  <li><a onclick="CooperatorCmd.switchDisLoadCooperator(0)" style="cursor: pointer;" class="leftnav-hover"
    id="expert_disclist0"> <span class="Shear-head"></span>
      <div class="Fleft text-overflow2">
        <strong><s:text name="cooperator.label.disc.all" /></strong>
      </div>
  </a></li>
  <c:forEach items="${expertDiscList}" var="superDisc">
    <c:choose>
      <c:when test="${locale eq 'zh_CN' }">
        <c:set var="superDiscName" value="${superDisc.discName }" />
      </c:when>
      <c:otherwise>
        <c:set var="superDiscName" value="${superDisc.discEnName }" />
      </c:otherwise>
    </c:choose>
    <li><a onclick="CooperatorCmd.switchDisLoadCooperator(${superDisc.discId})" style="cursor: pointer;"
      id="expert_disclist${superDisc.discId}"> <span class="Shear-head"></span>
        <div class="two_nav_name" style="width: 120px;" title="${superDiscName}">
          <i class="icon_starpic"></i><strong>${superDiscName}</strong>
        </div>
    </a> <c:if test="${superDisc.suberDiscList!= null || fn:length(superDisc.suberDiscList) != 0}">
        <dl style="display: none;">
          <c:forEach items="${superDisc.suberDiscList}" var="suberDisc">
            <c:choose>
              <c:when test="${locale eq 'zh_CN' }">
                <c:set var="suberDiscName" value="${suberDisc.discName }" />
              </c:when>
              <c:otherwise>
                <c:set var="suberDiscName" value="${suberDisc.discEnName }" />
              </c:otherwise>
            </c:choose>
            <dd title="${suberDiscName }">
              <a onclick="CooperatorCmd.switchDisLoadCooperator(${suberDisc.discId})" style="cursor: pointer;"
                id="expert_disclist${suberDisc.discId}"><div class="two_nav_name" style="width: 110px">${suberDiscName}</div>
                <span class="Fright">(${suberDisc.expertNum})</span></a>
            </dd>
          </c:forEach>
        </dl>
      </c:if></li>
  </c:forEach>
</ul>