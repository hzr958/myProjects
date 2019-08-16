<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type = "text/javascript">
    //判断如果是Chrome才设置
    $(document).ready(function(){
      if (Sys.chrome) {
        $("#add_psn_email_input").attr("readonly","readonly");
      }
    })
</script>
<div class="set-email__main-title" style="border: none;">
  <span class="set-email__main-heading" style="font-size: 20px; font-weight: normal;"><s:text
      name="psnset.email.account" /> </span> <span class="set-email__main-title__tip">&nbsp;<s:text
      name="psnset.set.loginAccount.receiveEmail" /></span>
</div>
<div class="set-container__right-content">
  <div class="confirm-eamil__box" style="border: none;">
    <div class="confirm-email__body" style="border-top: 1px solid #ddd;">
      <div class="confirm-email__body-item" style="border-bottom: 1px solid #eee;">
        <c:forEach items="${personEmailInfoList }" var="email">
          <div class="confirm-email__body-list" mailId="${ email.id}">
            <div class="confirm-email__body-list__address" title="${email.email }">
              <div class="confirm-email__body-list__address-content">${email.email }</div>
              <c:if test="${email.loginMail != 1  &&  email.firstMail == 1}">
                <div class="confirm-email__body-list__address-sub">
                  <s:text name="psnset.account" />
                </div>
              </c:if>
              <c:if test="${email.loginMail == 1  &&  email.firstMail != 1}">
                <div class="confirm-email__body-list__address-sub">
                  <s:text name="psnset.login" />
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
                <div class="confirm-email__body-sty_box" style="text-align: left; width: 200px;">
                  <div class="confirm-email__body-set" style="width: 170px;" resend="${email.resend}"
                    delaySendDate="${email.delaySendDate }" sendDate="${email.sendDate }">
                    <span class="send_validate_${email.id }" onclick="PsnsettingPwd.sendValidateEmail(this);" style="cursor: pointer;"> <c:if
                        test="${email.resend ==true }">
                        <s:text name="psnset.resendvadilate.email" />
                      </c:if> <c:if test="${email.resend ==false }">
                        <s:text name="psnset.sendvadilate.email" />
                      </c:if>
                    </span>
                  </div>
                  <c:if test="${email.resend ==true }">
                    <div class="confirm-email__body-set" style="width: 70px;">
                      <span class="show_email_validate_box_${email.id }" style="cursor: pointer;" onclick="PsnsettingPwd.showEmailValidateCodeBox(this);"><s:text
                          name="psnset.email.confirm" /></span>
                    </div>
                  </c:if>
                </div>
              </c:if>
              <c:if test="${email.isVerify == 1 }">
                <div class="confirm-email__body-sty_box" style="text-align: left; width: 200px;">
                  <div class="confirm-email__body-set"></div>
                </div>
              </c:if>
              <c:if test="${email.loginMail == 1 &&  email.firstMail == 1  }">
                <div class="confirm-email__body-sty_box" style="width: 60%;">
                  <div class="confirm-email__body-opera">
                    <span style="color: #999 !important" onclick="" title='<s:text name="psnset.loginAccount"/>'><s:text
                        name="psnset.loginAccount" /></span>
                  </div>
                  <div class="confirm-email__body-icon" onclick="PsnsettingPwd.delPsnEmailConfirm(this);"></div>
                </div>
              </c:if>
              <c:if test="${email.loginMail != 1 || email.firstMail != 1}">
                <div class="confirm-email__body-sty_box" style="width: 60%;">
                  <div class="confirm-email__body-opera">
                    <span style="cursor: pointer;" onclick="PsnsettingPwd.checkEmailHasUsed(this);"
                      title='<s:text name="psnset.set.loginAccount"/>'><s:text name="psnset.set.loginAccount" /></span>
                  </div>
                  <div class="confirm-email__body-icon" onclick="PsnsettingPwd.delPsnEmailConfirm(this);"></div>
                </div>
              </c:if>
            </div>
          </div>
        </c:forEach>
      </div>
    </div>
    <div class="confirm-email__footer" style="border: none;">
      <div class="confirm-email__footer-box" style="margin: 0px 8px 0px 60px;">
        <input type="text" maxlength="50" class="confirm-email__footer-input" id="add_psn_email_input"
          placeholder="<s:text name="psnset.add.email.account"/>" onfocus="this.removeAttribute('readonly')">
      </div>
      <div class="confirm-email__footer-confirm" onclick="PsnsettingPwd.addPsnEmail(this);">
        <s:text name="psnset.sure" />
      </div>
    </div>
  </div>
</div>
<div class="background-screen_cover" id="psn-set-emall-validate">
  <div class="input-verificationcode_container">
    <div class="input-verificationcode_container-header">
      <span class="input-verificationcode_container-header_infor"><s:text name="psnset.complete.verify.code" /></span>
      <i class="material-icons input-verificationcode_container-header_icon"
        onclick="PsnsettingPwd.hidderEmailValidateCodeBox(this);">clear</i>
    </div>
    <div class="input-verificationcode_container-body">
      <span class="input-verificationcode_container-body_tip"><s:text name="psnset.verification.code" /> </span> <input
        class="input-verificationcode_container-body_input" id="psn-set-emall-validate-input" type="text"
        style="padding-left: 4px;" title='<s:text name="psnset.enter.verify.code"/>' maxlength="6"
        placeholder='<s:text name="psnset.enter.verify.code"/>'>
    </div>
    <div class="input-verificationcode_container-footer">
      <div class="input-verificationcode_container-footer_cancle"
        onclick="PsnsettingPwd.hidderEmailValidateCodeBox(this);">
        <s:text name="psnset.email.cancel" />
      </div>
      <div class="input-verificationcode_container-footer_confirm" onclick="PsnsettingPwd.sureEmailValidateCode(this);">
        <s:text name="psnset.sure" />
      </div>
    </div>
  </div>
</div>
<div class="background-screen_cover" id="psn-set-password-validate">
  <div class="input-verificationcode_container">
    <div class="input-verificationcode_container-header">
      <span class="input-verificationcode_container-header_infor"><s:text name="psnset.input.your.password" /></span> <i
        class="material-icons input-verificationcode_container-header_icon"
        onclick="PsnsettingPwd.hideValidatePassword(this);">clear</i>
    </div>
    <div class="input-verificationcode_container-body">
      <span class="input-verificationcode_container-body_tip"><s:text name="psnset.password" /> </span> <input
        class="input-verificationcode_container-body_input" id="psn-password-validate-input" type="password"
        style="padding-left: 4px;" title='<s:text name="psnset.input.your.password2"/>'
        placeholder='<s:text name="psnset.input.your.password2"/>'>
    </div>
    <div class="input-verificationcode_container-footer">
      <div class="input-verificationcode_container-footer_cancle" onclick="PsnsettingPwd.hideValidatePassword(this);">
        <s:text name="psnset.email.cancel" />
      </div>
      <div class="input-verificationcode_container-footer_confirm" onclick="PsnsettingPwd.validatePassword();">
        <s:text name="psnset.sure" />
      </div>
    </div>
  </div>
</div>
