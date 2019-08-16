
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="main-list__item">
  <div class="main-list__item_content">
    <div class="notification__box">
      <a>
        <div class="notification__content">
          <div class="notification__text">
            <span>${msil.senderZhName}</span> <span class="text_link-style">请求加你为联系人</span>
          </div>
          <div class="notification__actions">
            <div class="notification__time">${msil.createDate }</div>
            <div class="notification__settings">
              <div class="notification__settings_item material-icons">fiber_manual_record</div>
              <div class="notification__settings_item material-icons">delete</div>
            </div>
          </div>
        </div>
      </a>
    </div>
  </div>
</div>