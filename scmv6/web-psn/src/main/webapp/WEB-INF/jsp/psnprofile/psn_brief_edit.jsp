<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="dialogs__childbox_fixed">
  <div class="dialogs__header">
    <div class="dialogs__header_title">
      <s:text name='homepage.profile.title.about' />
    </div>
  </div>
</div>
<div class="dialogs__childbox_adapted">
  <div class="dialogs__content global__padding_24">
    <form>
      <div class="form__sxn_row">
        <div class="input__box">
          <label class="input__title" id="brief_label" name="brief_label"<%-- style="${!empty psnBriefDesc ? 'display:none;' : ''}" --%> ><s:text
              name='homepage.profile.title.about' /></label>
          <div class="input__area">
            <textarea id="psnBriefDescArea" name="psnBriefDescArea">${psnBriefDesc }</textarea>
            <div class="textarea-autoresize-div"></div>
          </div>
          <div class="input__helper"></div>
        </div>
      </div>
    </form>
  </div>
</div>
<div class="dialogs__childbox_fixed">
  <div class="dialogs__footer">
    <button class="button_main button_primary-reverse" onclick="javascript:savePsnBrief(this);">
      <s:text name='homepage.profile.btn.save' />
    </button>
    <button class="button_main" onclick="javascript:hideBriefDescBox(this);">
      <s:text name='homepage.profile.btn.cancel' />
    </button>
  </div>
</div>
