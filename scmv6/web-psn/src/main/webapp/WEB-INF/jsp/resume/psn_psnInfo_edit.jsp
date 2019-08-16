<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function edit_requestData() {
	return {"insName":$('#insName').val()};
}
</script>
<c:if test="${!empty cvPsnInfo }">
  <!-- <div class="dialogs__box" style="width: 480px;" dialog-id="editPsnEduBox"  cover-event="hide">  -->
  <form name="editEduForm" method="post" action="">
    <div class="dialogs__childbox_fixed">
      <div class="dialogs__header" style="line-height: 56px;">
        <div class="dialogs__header_title">
          <s:text name="resume.psninfo" />
        </div>
      </div>
    </div>
    <div class="dialogs__childbox_adapted">
      <div class="dialogs__content global__padding_24">
        <form name="editEduForm" method="post" action="">
          <input type="hidden" name="des3ResumeId" id="des3ResumeId" value="<iris:des3 code='${des3ResumeId}'/>" />
          <div class="form__sxn_row">
            <div class="input__box" id="editPsnNameDiv">
              <label class="input__title" id="edit_namne_label"><s:text name="resume.name" /></label>
              <div class="input__area">
                <input type="text" name="psnName" id="psnName" value="${cvPsnInfo.name}" maxlength="50" />
              </div>
              <div class="input__helper" id="editPsnNameHelper"></div>
            </div>
          </div>
          <div class="form__sxn_row">
            <div class="input__box" id="editEduInsNameDiv">
              <label class="input__title" id="edit_ins_label"><s:text name="resume.ins" /></label>
              <div class="input__area">
                <input type="text" name="insName" id="insName" value="${cvPsnInfo.insInfo }" maxlength="200"
                  class="js_autocompletebox" request-url="/psnweb/ac/ajaxautoinstitution" contenteditable="true" />
              </div>
              <div class="input__helper" id="editEduInsNameHelper"></div>
            </div>
          </div>
          <div class="form__sxn_row">
            <div class="input__box">
              <label class="input__title" id="edit_dep_label"><s:text name="resume.department" /></label>
              <div class="input__area">
                <input type="text" name="department" id="department" value="${cvPsnInfo.department }" maxlength="200"
                  class="js_autocompletebox" request-url="/psnweb/ac/ajaxautoinsunit" request-data="edit_requestData();"
                  contenteditable="true" />
              </div>
              <div class="input__helper"></div>
            </div>
            <div class="input__box">
              <label class="input__title" id="edit_titolo_label"><s:text name="resume.degree" /></label>
              <div class="input__area">
                <input type="text" name="degree" id="degree" value="${cvPsnInfo.degree }" maxlength="50"
                  class="js_autocompletebox" request-url="/psnweb/ac/ajaxautodegree" contenteditable="true" />
              </div>
              <div class="input__helper"></div>
            </div>
          </div>
        </form>
      </div>
    </div>
    <div class="dialogs__childbox_fixed">
      <div class="dialogs__footer">
        <button class="button_main button_primary-reverse" onclick="PsnResume.savePsnInfo('edit',this);">
          <s:text name='homepage.profile.btn.save' />
        </button>
        <button class="button_main" onclick="hideDialog('editPsnInfoBox');">
          <s:text name='homepage.profile.btn.cancel' />
        </button>
      </div>
    </div>
    <!-- </div> -->
  </form>
</c:if>