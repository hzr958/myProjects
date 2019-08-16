<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="confirm-email__body-header">
  <div class="confirm-email__body-header__title">
    <s:text name="psnset.email.address" />
  </div>
  <div class="confirm-email__body-header__func">
    <s:text name="psnset.operate" />
  </div>
</div>
<div class="confirm-email__body-item">
  <c:forEach items="${personEmailRegister }" var="email">
    <div class="confirm-email__body-list" mailId="${ email.id}">
      <div class="confirm-email__body-list__address" title="${email.email }">
        <div class="confirm-email__body-list__address-content">${email.email }</div>
        <c:if test="${email.loginMail == 1  &&  email.firstMail == 1}">
          <div class="confirm-email__body-list__address-sub">
            <s:text name="psnset.first.email" />
          </div>
        </c:if>
        <c:if test="${email.loginMail == 1  &&  email.firstMail != 1}">
          <div class="confirm-email__body-list__address-sub">
            <s:text name="psnset.login" />
          </div>
        </c:if>
        <c:if test="${email.loginMail != 1  &&  email.firstMail == 1}">
          <div class="confirm-email__body-list__address-sub">
            <s:text name="psnset.account" />
          </div>
        </c:if>
      </div>
      <div class="confirm-email__body-list__confirm">
        <c:if test="${email.isVerify == 1 }">
          <s:text name="psnset.has.sure" />
        </c:if>
        <c:if test="${email.isVerify != 1 }">
          <s:text name="psnset.no.sure" />
        </c:if>
      </div>
      <div class="confirm-email__body-list__funcbox">
        <c:if test="${email.isVerify != 1 }">
          <div class="confirm-email__body-sty_box" style="text-align: left;">
            <div class="confirm-email__body-set">
              <span onclick="PsnsettingPwd.sendConfirmEmail(this);" style="cursor: pointer;"><s:text
                  name="psnset.sendconfirm.email" /></span>
            </div>
          </div>
        </c:if>
        <c:if test="${email.isVerify == 1 }">
          <div class="confirm-email__body-sty_box" style="text-align: left;">
            <div class="confirm-email__body-set"></div>
          </div>
        </c:if>
        <c:if test="${email.loginMail == 1  &&  email.firstMail == 1}">
          <div class="confirm-email__body-sty_box" style="text-align: left;">
            <div class="confirm-email__body-opera"></div>
          </div>
        </c:if>
        <c:if test="${email.loginMail == 1  &&  email.firstMail != 1}">
          <div class="confirm-email__body-sty_box">
            <div class="confirm-email__body-opera">
              <span style="cursor: pointer;" onclick="PsnsettingPwd.setFirstEmailConfirm(this);"
                title='<s:text name="psnset.set.firstemail"/>'><s:text name="psnset.set.firstemail" /></span>
            </div>
            <div class="confirm-email__body-icon" onclick="PsnsettingPwd.delPsnEmailConfirm(this);"></div>
          </div>
        </c:if>
        <c:if test="${email.loginMail != 1  &&  email.firstMail == 1}">
          <div class="confirm-email__body-sty_box">
            <div class="confirm-email__body-opera">
              <span style="cursor: pointer;" onclick="PsnsettingPwd.setFirstEmailConfirm(this);"
                title='<s:text name="psnset.set.firstemail"/>'><s:text name="psnset.set.firstemail" /></span>
            </div>
            <div class="confirm-email__body-icon" onclick="PsnsettingPwd.delPsnEmailConfirm(this);"></div>
          </div>
        </c:if>
        <c:if test="${email.loginMail != 1  &&  email.firstMail != 1}">
          <div class="confirm-email__body-sty_box">
            <div class="confirm-email__body-opera">
              <span style="cursor: pointer;" onclick="PsnsettingPwd.setFirstEmailConfirm(this);"
                title='<s:text name="psnset.set.firstemail"/>'><s:text name="psnset.set.firstemail" /></span>
            </div>
            <div class="confirm-email__body-icon" onclick="PsnsettingPwd.delPsnEmailConfirm(this);"></div>
          </div>
        </c:if>
      </div>
    </div>
  </c:forEach>
</div>
<div class="confirm-email__footer">
  <div class="confirm-email__footer-newaddress">
    <s:text name="psnset.add.emailaddress" />
    <s:text name="psnset.colon" />
  </div>
  <div class="confirm-email__footer-box" style="border-color: rgb(221, 221, 221);">
    <input type="text" class="confirm-email__footer-input" maxlength="50" id="add_psn_email_input"
      placeholder="<s:text name="psnset.add.emailaddress"/>">
  </div>
  <div class="confirm-email__footer-confirm" onclick="PsnsettingPwd.addPsnEmail(this);">
    <s:text name="psnset.sure" />
  </div>
</div>
</div>
