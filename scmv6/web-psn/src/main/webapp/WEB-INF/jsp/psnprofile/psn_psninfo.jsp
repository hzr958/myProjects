<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	$(document).ready(function(){
		initArea();
		Resume.personInfo.base.checkNameInvalid();
	});
</script>
<div id="psn_info_editDiv" style="overflow-y: auto;">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header" style="line-height: 56px;">
      <div class="dialogs__header_title">
        <s:text name='homepage.profile.note.titolo' />
      </div>
    </div>
  </div>
  <div class="dialogs__childbox_adapted">
    <div class="dialogs__content global__padding_24">
      <form>
        <div class="form__sxn_row">
          <div class="input__box">
            <label class="input__title">姓</label>
            <div class="input__area">
              <input type="text" id="zhLastName" name="zhLastName" value="${zhLastName }" maxlength="20"
                onblur="Resume.parsepinyin(this, 'lastName', 'lastName_div', 'lastName_error')" />
            </div>
            <div class="input__helper"></div>
          </div>
          <div class="input__box">
            <label class="input__title">名</label>
            <div class="input__area">
              <input type="text" id="zhFirstName" name="zhFirstName" value="${zhFirstName }" maxlength="40"
                onblur="Resume.parsepinyin(this, 'firstName', 'firstName_div', 'firstName_error')" />
            </div>
            <div class="input__helper"></div>
          </div>
        </div>
        <div class="form__sxn_row">
          <div class="input__box" id="firstName_div">
            <label class="input__title">First name*</label>
            <div class="input__area">
              <input type="text" id="firstName" name="firstName" value="${firstName }" maxlength="40" />
            </div>
            <div class="input__helper" invalid-message="" id="firstName_error"></div>
          </div>
          <div class="input__box">
            <label class="input__title">Middle name</label>
            <div class="input__area">
              <input type="text" id="otherName" name="otherName" value="${otherName }" maxlength="61" />
            </div>
            <div class="input__helper"></div>
          </div>
          <div class="input__box" id="lastName_div">
            <label class="input__title">Last name*</label>
            <div class="input__area">
              <input type="text" id="lastName" name="lastName" value="${lastName }" maxlength="20" />
            </div>
            <div class="input__helper" invalid-message="" id="lastName_error"></div>
          </div>
        </div>
        <div class="form__sxn_row">
          <div class="input__box ${!empty currentWorkStr ? 'input_not-null' : ''}" id="currentWorkSelectDiv">
            <label class="input__title"><s:text name='homepage.profile.note.position.info' /></label>
            <div class="sel__box" style="border-bottom: 1px solid #ccc;" selector-id="work_history_select"
              sel-value="${currentWorkId }" id="current_work_box">
              <div class="sel__value sel__change-work__container">
                <span class="sel__value_selected sel__value_placeholder" id="current_work_box_text_span">${currentWorkStr }</span>
                <i class="material-icons sel__dropdown-icon">arrow_drop_down</i>
              </div>
            </div>
            <div class="input__helper" invalid-message=""></div>
            <div class="sel__value-add__newwork">
              <a onclick="Resume.editFirstWorkHistory();"><s:text name="homepage.profile.btn.addworkIns" /></a>
            </div>
          </div>
          <%-- <div class="input__box input_not-null">
            <label class="input__title"><s:text name="homepage.profile.title.work"></s:text></label>
            <div class="sel__container">
              <div class="sel__value sel__value-btn__content" > <span class="sel__value-online__edit" onclick="Resume.editFirstWorkHistory();"><s:text name="homepage.online.editor"></s:text></span></div>
            </div>
            <div class="input__helper"></div>
          </div> --%>
        </div>
        <input type="hidden" id="workId" name="workId" value="${currentWorkId }" /> <input type="hidden"
          id="encodeWorkId" value="<iris:des3 code='${currentWorkId }'/>" />
        <div class="form__sxn_row">
          <div class="input__box">
            <label class="input__title"><s:text name='homepage.profile.note.title' /></label>
            <div class="input__area">
              <input type="text" name="psnTitolo" id="psnTitolo" value="${titolo}" maxlength="200" />
            </div>
            <div class="input__helper"></div>
          </div>
        </div>
        <div class="form__sxn_row">
          <div class="input__box no-flex input_not-null">
            <label class="input__title"><s:text name='homepage.profile.note.region' /></label>
            <div class="sel__box" selector-id="1st_area">
              <div class="sel__value" style="display: flex; align-items: center;">
                <span class="sel__value_selected sel__value_placeholder"><s:text
                    name='homepage.profile.note.first.region' /></span> <i class="material-icons sel__dropdown-icon">arrow_drop_down</i>
              </div>
            </div>
            <div class="input__helper" invalid-message=""></div>
          </div>
          <div class="input__box no-flex input_not-null">
            <label class="input__title"></label>
            <div class="sel__box" style="visibility: hidden" selector-id="2nd_area">
              <div class="sel__value" style="display: flex; align-items: center;">
                <span class="sel__value_selected sel__value_placeholder"><s:text
                    name='homepage.profile.note.second.region' /></span> <i class="material-icons sel__dropdown-icon">arrow_drop_down</i>
              </div>
            </div>
            <div class="input__helper" invalid-message=""></div>
          </div>
          <div class="input__box no-flex input_not-null">
            <label class="input__title"></label>
            <div class="sel__box" style="visibility: hidden" selector-id="3nd_area">
              <div class="sel__value" style="display: flex; align-items: center;">
                <span class="sel__value_selected sel__value_placeholder"><s:text
                    name='homepage.profile.note.third.region' /></span> <i class="material-icons sel__dropdown-icon">arrow_drop_down</i>
              </div>
            </div>
            <div class="input__helper" invalid-message=""></div>
          </div>
        </div>
        <input type="hidden" name="psnRegionId" id="psnRegionId" value="${psnInfo.regionId }" />
        <div class="form__sxn_row">
          <div class="input__box">
            <label class="input__title" id="brief_label" name="brief_label"><s:text
                name='homepage.profile.title.about' /></label>
            <div class="input__area input__container" style="max-height: 108px; overflow-y: hidden; overflow-x: hidden;">
              <textarea id="psnBriefDescArea"
                name="psnBriefDescArea" style="overflow-y:auto;">${psnBriefDesc }</textarea>
              <div class="textarea-autoresize-div"></div>
            </div>
            <div class="input__helper"></div>
          </div>
        </div>
      </form>
    </div>
  </div>
  <div class="dialogs__childbox_fixed" style="display: flex;">
    <div class="dialogs__footer" style="line-height: 56px;">
      <button class="button_main button_primary-reverse" onclick="savePsnInfo(this);">
        <s:text name='homepage.profile.btn.save' />
      </button>
      <button class="button_main button_primary-cancle" onclick="hidePsnInfoBox(this);">
        <s:text name='homepage.profile.btn.cancel' />
      </button>
    </div>
  </div>
</div>
<%@ include file="psn_workhistory_add_primary.jsp"%>
