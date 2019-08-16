<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="menuParent" style="padding: 0px;">
  <div class="ListTitlePanel">
    <div class="ListTitle" style="width: 230px; border-bottom: 1px solid #ddd; padding-left: 0px;  margin-left: 35px; border-top: none;  padding-top: 0px;">
      <span><spring:message code='menu.patent.category' /></span>
      <div class="leftbgbt">
        <i class="material-icons" id="iconQh"> &#xE5CE;</i>
      </div>
    </div>
  </div>
  <div class="menuList" id="stype" style="border-top: none;">
    <c:forEach items="${pubListVO.pubTypeMap }" var="pubType">
      <div>
      <c:if test="${pubType.value == 0}">
        <a class="menuList_list-container menuList_list-container_none " style="pointer-events: none;color: rgba(0, 0, 0, 0.24); justify-content: space-between; align-items: center;" href="javascript:;"
          onclick="page.searchByType('${pubType.key}','${pubType.value}')"> <span class="category-left__content">
            <c:if test="${pubType.key==51}">
              <spring:message code='menu.patent.category.invention' />
            </c:if> <c:if test="${pubType.key==52}">
              <spring:message code='menu.patent.category.utilModel' />
            </c:if> <c:if test="${pubType.key==53}">
              <spring:message code='menu.patent.category.design' />
            </c:if> <c:if test="${pubType.key==7}">
              <spring:message code='menu.patent.category.others' />
            </c:if>
        </span> <em class="fr">(${pubType.value})</em> <i name="type" class="material-icons close" style="display: none;visibility: unset;">close</i>
        </a>
      </c:if>
      <c:if test="${pubType.value != 0}">
        <a class="menuList_list-container" href="javascript:;"
          onclick="page.searchByType('${pubType.key}','${pubType.value}')" style="justify-content: space-between; align-items: center;"> <span class="category-left__content">
            <c:if test="${pubType.key==51}">
              <spring:message code='menu.patent.category.invention' />
            </c:if> <c:if test="${pubType.key==52}">
              <spring:message code='menu.patent.category.utilModel' />
            </c:if> <c:if test="${pubType.key==53}">
              <spring:message code='menu.patent.category.design' />
            </c:if> <c:if test="${pubType.key==7}">
              <spring:message code='menu.patent.category.others' />
            </c:if>
        </span> <em class="fr">(${pubType.value})</em> <i name="type" class="material-icons close"  style="display: none;visibility: unset;">close</i>
        </a>
      </c:if>
      </div>
    </c:forEach>
  </div>
</div>