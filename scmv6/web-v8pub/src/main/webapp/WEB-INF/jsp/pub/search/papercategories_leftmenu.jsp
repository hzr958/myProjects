<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="menuParent" style="padding: 0px;">
  <div class="ListTitlePanel">
    <div class="ListTitle" style="width: 230px; border-bottom: 1px solid #ddd; padding-left: 0px;  margin-left: 35px; border-top: none; padding-top: 0px;">
      <span><spring:message code='menu.paper.category' /></span>
      <div class="leftbgbt">
        <!-- <i class="material-icons keyboard_arrow_up" id="iconQh">&#xE5CE;</i> -->
        <i class="material-icons" id="iconQh">&#xE5CE;</i>
      </div>
    </div>
  </div>
  <div class="menuList" id="stype" style="border-top: none;">
    <c:forEach items="${pubListVO.pubTypeMap }" var="pubType">
      <div>
      <c:if test="${pubType.value == 0}">
        <a class="menuList-item__click menuList_list-container_none" style="pointer-events: none;color: rgba(0, 0, 0, 0.24); justify-content: space-between; align-items: center;" href="javascript:;"
          onclick="page.searchByType('${pubType.key}' ,'${pubType.value}')" > <span class="menuList-papper__style">
            <c:if test="${pubType.key==3}">
              <spring:message code='menu.paper.category.conferPaper' />
            </c:if> <c:if test="${pubType.key==4}">
              <spring:message code='menu.paper.category.journalArticle' />
            </c:if> <c:if test="${pubType.key==8}">
              <spring:message code='menu.paper.category.thesis' />
            </c:if> <c:if test="${pubType.key==7}">
              <spring:message code='menu.paper.category.others' />
            </c:if>
        </span> <em class="fr">(${pubType.value})</em> <i name="type" class="material-icons close" style="display: none;visibility: unset;">close</i>
        </a>
       </c:if>
       <c:if test="${pubType.value != 0}">
        <a class="menuList-item__click" href="javascript:;"
          onclick="page.searchByType('${pubType.key}' ,'${pubType.value}')" style="justify-content: space-between; align-items: center;"> <span class="menuList-papper__style">
            <c:if test="${pubType.key==3}">
              <spring:message code='menu.paper.category.conferPaper' />
            </c:if> <c:if test="${pubType.key==4}">
              <spring:message code='menu.paper.category.journalArticle' />
            </c:if> <c:if test="${pubType.key==8}">
              <spring:message code='menu.paper.category.thesis' />
            </c:if> <c:if test="${pubType.key==7}">
              <spring:message code='menu.paper.category.others' />
            </c:if>
        </span> <em class="fr">(${pubType.value})</em> <i name="type" style="display: none;visibility: unset;" class="material-icons close">close</i>
        </a>
       </c:if>
      </div>
    </c:forEach>
  </div>
</div>