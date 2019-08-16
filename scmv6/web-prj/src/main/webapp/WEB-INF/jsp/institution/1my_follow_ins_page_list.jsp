<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>


   <c:forEach items="${listInfo}" var="info" >
    <div class="create-InstitutionalGroups_body-item">
      <div class="create-InstitutionalGroups_item-avator">
        <img src="${info.logoUrl}">
      </div>
      <div class="create-InstitutionalGroups_item-body">
        <div>
          <div class="create-InstitutionalGroups_item-title">
              ${info.insName}
          </div>
          <div class="create-InstitutionalGroups_item-count">
            <div class="create-InstitutionalGroups_countbox">
              <span class="create-InstitutionalGroups_countbox-title">${info.stView}</span>
              <span class="create-InstitutionalGroups_countbox-num">阅读</span>
            </div>
            <div class="create-InstitutionalGroups_countbox">
              <span class="create-InstitutionalGroups_countbox-title">${info.stShare}</span>
              <span class="create-InstitutionalGroups_countbox-num">分享</span>
            </div>
            <div class="create-InstitutionalGroups_countbox">
              <span class="create-InstitutionalGroups_countbox-title">${info.stFollow}</span>
              <span class="create-InstitutionalGroups_countbox-num">查看</span>
            </div>
          </div>
        </div>
        <div>
          <div class="create-InstitutionalGroups_item-Admin">进入管理</div>
          <%--<div class="create-InstitutionalGroups_item-Exten">推广</div>--%>
        </div>
      </div>
    </div>
   </c:forEach>


