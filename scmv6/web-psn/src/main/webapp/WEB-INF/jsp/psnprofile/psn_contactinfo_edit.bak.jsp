<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="dialogs__box" style="width: 320px;" dialog-id="contactInfoBox" cover-event="hide" id="contactInfoBox">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="dialogs__header_title">
        <s:text name='homepage.profile.title.contact' />
      </div>
    </div>
  </div>
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__content global__padding_24">
      <form>
        <div class="form__sxn_row">
          <div class="input__box icon-label" id="tel_div">
            <label class="input__title material-icons">phone</label>
            <div class="input__area">
              <input id="tel" name="tel" value="" maxLength="50" />
            </div>
            <div class="input__helper" id="tel_error"></div>
          </div>
        </div>
        <div class="form__sxn_row">
          <div class="input__box icon-label" id="email_div">
            <label class="input__title material-icons">email</label>
            <div class="input__area">
              <input id="email" name="email" value="" maxLength="50" />
            </div>
            <div class="input__helper" invalid-message="" id="email_error"></div>
          </div>
        </div>
      </form>
    </div>
  </div>
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__footer">
      <button class="button_main button_primary-reverse" onclick="javascript:saveContactInfo(this);">
        <s:text name='homepage.profile.btn.save' />
      </button>
      <button class="button_main" onclick="javascript:hideContactInfoBox(this);">
        <s:text name='homepage.profile.btn.cancel' />
      </button>
    </div>
  </div>
</div>