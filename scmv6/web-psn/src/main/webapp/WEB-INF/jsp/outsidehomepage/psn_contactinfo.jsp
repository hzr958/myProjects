<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="module-card__box">
  <div class="module-card__header">
    <div class="module-card-header__title">
      <s:text name="homepage.profile.title.contact" />
    </div>
  </div>
  <c:if test="${not empty tel || not empty email}">
    <div class="global__padding_16">
      <form>
        <c:if test="${not empty tel || isMySelf}">
          <div class="form__sxn_row">
            <div class="input__box readonly icon-label">
              <label class="input__title material-icons">phone</label>
              <div class="input__area">
                <input readonly id="psnTel" disabled="disabled" name="psnTel" value="${tel }">
              </div>
            </div>
          </div>
        </c:if>
        <div class="form__sxn_row">
          <div class="input__box readonly icon-label">
            <label class="input__title material-icons">email</label>
            <div class="input__area">
              <input readonly name="psnEmail" disabled="disabled" id="psnEmail" value="${email }">
            </div>
          </div>
        </div>
      </form>
    </div>
  </c:if>
</div>