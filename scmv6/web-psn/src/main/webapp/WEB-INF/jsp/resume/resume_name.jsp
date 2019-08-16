
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="new__resume-title">
  <div class="new__resume-title_h1" id="resumeNameShow">${resumeName}</div>
  <div class="new__resume-edit__icon-edit_container" onclick="PsnResume.editCVName();">
    <div class="new__resume-edit__icon-edit"></div>
    <div class="new__resume-edit__icon-edit_title">
      <s:text name="resume.edit" />
    </div>
  </div>
  <div class="dialogs__box oldDiv" style="width: 480px;" dialog-id="editCvNameBox" cover-event="hide" id="editCvNameBox">
    <div class="dialogs__childbox_fixed">
      <div class="dialogs__header">
        <div class="dialogs__header_title">
          <s:text name='homepage.profile.cvtitle' />
        </div>
      </div>
    </div>
    <div class="dialogs__childbox_adapted">
      <div class="dialogs__content global__padding_24">
        <div class="form__sxn_row">
          <div class="input__box" id="eduCVNameDiv">
            <label class="input__title" id="edit_name_label"><s:text name="resume.edit.CVname" /></label>
            <div class="input__area">
              <input type="text" name="editResumeName" id="editResumeName" value="${resumeName}" maxlength="50" />
            </div>
            <div class="input__helper" id="eduCVNameHelper"></div>
          </div>
        </div>
      </div>
    </div>
    <div class="dialogs__childbox_fixed">
      <div class="dialogs__footer">
        <button class="button_main button_primary-reverse" onclick="PsnResume.saveCVName()">
          <s:text name='homepage.profile.btn.save' />
        </button>
        <button class="button_main" onclick="hideDialog('editCvNameBox');">
          <s:text name='homepage.profile.btn.cancel' />
        </button>
      </div>
    </div>
  </div>
</div>