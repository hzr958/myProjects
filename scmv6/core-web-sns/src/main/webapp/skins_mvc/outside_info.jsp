<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="multiple-button-container">
  <div class="button__box button__model_rect" onclick="to_login();">
    <a>
      <div
        class="button__main button__style_flat button__size_normal button__color-plain color-display_blue ripple-effect">
        <span><spring:message code='page.index.login' /></span>
      </div>
    </a>
  </div>
  <div class="button__box button__model_rect" onclick="to_register()">
    <a>
      <div
        class="button__main button__style_flat button__size_normal button__color-plain color-display_blue ripple-effect">
        <span><spring:message code='register.person' /></span>
      </div>
    </a>
  </div>
</div>