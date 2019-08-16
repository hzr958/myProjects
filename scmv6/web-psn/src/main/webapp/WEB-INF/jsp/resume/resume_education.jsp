
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:if test="${isShowMoule == '0'}">
  <div class="new__resume-list__item">
    <div style="display: flex; justify-content: center; width: 94%; align-items: center;">
      <div class="new__resume-list__item-line"></div>
      <div class="new__resume-list__item-title new__resume-list__item-title_top">
        <s:text name="resume.education.exp" />
      </div>
      <div class="new__resume-list__item-line"></div>
    </div>
    <div class="new__resume-edit__icon-add new__resume-edit__icon-add_item" onclick="addEduHistory();">
      <i class="material-icons" style="font-size: 26px;">add</i> <span><s:text name="resume.add" /></span>
    </div>
  </div>
  <c:forEach var="item" items="${eduMapList}">
    <div class="new__resume-list__content-box">
      <div class="new__resume-list__content-title">
        ${item.eduinfo}
        <c:if test="${!empty item.description}">
          <br />
          <c:out value="${item.description}"></c:out>
        </c:if>
      </div>
      <div class="new__resume-edit__icon-edit_container" onclick="PsnResume.editEduHistory(${item.seqEdu});">
        <div class="new__resume-edit__icon-edit"></div>
        <div class="new__resume-edit__icon-edit_title">
          <s:text name="resume.edit" />
        </div>
      </div>
    </div>
  </c:forEach>
  <%@ include file="psn_eduhistory_add.jsp"%>
  <div class="dialogs__box oldDiv" style="width: 480px;" dialog-id="editPsnEduBox" cover-event="hide" id="editPsnEduBox"></div>
</c:if>