<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="new__resume-list__tip-box" id="pub_title_main_div">
  <div style="display: flex; justify-content: center; width: 94%; align-items: center;">
    <div class="new__resume-list__item-line"></div>
    <div class="new__resume-list__item-title new__resume-list__item-title_top">
      <s:text name="resume.pub.exp" />
    </div>
    <div class="new__resume-list__item-line"></div>
  </div>
  <div class="new__resume-list__content-tip"></div>
</div>
<div class="new__resume-list__item" id="represent_pub_title_div">
  <div class="new__resume-list__item-title">
    <s:text name="resume.pub.selected" />
  </div>
  <div class="new__resume-edit__icon-edit_container" onclick="PsnResume.showPsnCVPubBox('5');">
    <div class="new__resume-edit__icon-edit"></div>
    <div class="new__resume-edit__icon-edit_title">
      <s:text name="resume.edit" />
    </div>
  </div>
</div>
<div class="new__resume-list__bottom" id="cv_representpub"></div>
<div class="new__resume-list__item" id="other_pub_title_div">
  <div class="new__resume-list__item-title">
    <s:text name="resume.pub.other" />
  </div>
  <div class="new__resume-edit__icon-edit_container" onclick="PsnResume.showPsnCVPubBox('6');">
    <div class="new__resume-edit__icon-edit"></div>
    <div class="new__resume-edit__icon-edit_title">
      <s:text name="resume.edit" />
    </div>
  </div>
</div>
<div class="new__resume-list__bottom" id="cv_otherpub"></div>
<div class="dialogs__box cvPubDiv" dialog-id="cvPubBox" cover-event="hide" style="width: 720px; height: 600px;"
  id="cvPubBox"></div>