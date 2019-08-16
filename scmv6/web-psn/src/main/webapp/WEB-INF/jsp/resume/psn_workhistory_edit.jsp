<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function editWork_requestData() {
	return {"insName":$('#insName').val()};
}
</script>
<c:if test="${!empty cvWorkInfo }">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="dialogs__header_title">
        <s:text name='homepage.profile.title.work' />
      </div>
    </div>
  </div>
  <div class="dialogs__childbox_adapted">
    <div class="dialogs__content global__padding_24">
      <form name="editWorkForm" method="post" action="/psnweb/cvWorkInfo/ajaxsave">
        <input type="hidden" name="des3ResumeId" id="des3ResumeId" value="<iris:des3 code='${des3ResumeId}'/>" /> <input
          type="hidden" name="seqWork" id="seqWork" value="${cvWorkInfo.seqWork }" />
        <div class="form__sxn_row">
          <div class="input__box" id="insNameDiv">
            <label class="input__title" name="work_ins_label"><s:text name='homepage.profile.note.institution' /></label>
            <div class="input__area">
              <input type="text" name="insName" id="insName" value="${cvWorkInfo.insName }" maxlength="200"
                class="js_autocompletebox" request-url="/psnweb/ac/ajaxautoinstitution" contenteditable="true" />
            </div>
            <div class="input__helper" id="insNameHelper"></div>
          </div>
        </div>
        <input type="hidden" name="insId" id="insId" value="${cvWorkInfo.insId }" />
        <div class="form__sxn_row">
          <div class="input__box">
            <label class="input__title" name="work_dep_label"><s:text name='homepage.profile.note.department' /></label>
            <div class="input__area">
              <input type="text" name="department" id="department" value="${cvWorkInfo.department }" maxlength="601"
                class="js_autocompletebox" request-url="/psnweb/ac/ajaxautoinsunit"
                request-data="editWork_requestData();" contenteditable="true" />
            </div>
            <div class="input__helper"></div>
          </div>
          <div class="input__box">
            <label class="input__title" name="work_pos_label"><s:text name='homepage.profile.note.position' /></label>
            <div class="input__area">
              <input type="text" name="position" id="position" value="${cvWorkInfo.degreeName }" maxlength="100"
                class="js_autocompletebox" request-url="/psnweb/ac/ajaxautoposition" contenteditable="true" />
            </div>
            <div class="input__helper"></div>
          </div>
        </div>
        <div class="form__sxn_row">
          <div class="input__box" id="workFromYearDiv">
            <label class="input__title" name="work_fYear_label"><s:text name='homepage.profile.note.start.year' /></label>
            <div class="input__area">
              <input type="text" name="fromYear" id="startTime"
                value="${cvWorkInfo.fromYear}<c:if test="${!empty cvWorkInfo.fromMonth && !empty cvWorkInfo.fromYear}">/</c:if>${cvWorkInfo.fromMonth}"
                date-year="${cvWorkInfo.fromYear}" date-month="${cvWorkInfo.fromMonth}" date-format="yyyy-mm" datepicker
                readonly />
            </div>
            <div class="input__helper" id="workFromYearHelp"></div>
          </div>
          <div class="input__box dev_end_year  <c:if test='${ cvWorkInfo.isActive ==1 }'>  input_disabled </c:if> "
            id="workToYearDiv">
            <label class="input__title" name="work_tYear_label"><s:text name='homepage.profile.note.end.year' /></label>
            <div class="input__area">
              <input type="text" name="toYear" id="endTime"
                value="${cvWorkInfo.toYear}<c:if test="${!empty cvWorkInfo.toYear && !empty cvWorkInfo.toMonth}">/</c:if>${cvWorkInfo.toMonth}"
                date-year="${cvWorkInfo.toYear}" date-month="${cvWorkInfo.toMonth}" date-format="yyyy-mm" datepicker
                readonly />
            </div>
            <div class="input__helper" id="workToYearHelp"></div>
          </div>
          <div class="input__box no-flex">
            <div class="input__area">
              <div class="input-radio__sxn">
                <div class="input-custom-style">
                  <input type="checkbox" id="editWorkUpToNow" onchange="clickUpToNow(this);"
                    <c:if test="${ cvWorkInfo.isActive ==1 }">  checked="checked" </c:if>> <i
                    class="material-icons custom-style"></i>
                </div>
                <div class="input-radio__label">
                  <s:text name='homepage.profile.note.to.present' />
                </div>
              </div>
            </div>
            <div class="input__helper"></div>
          </div>
        </div>
        <div class="form__sxn_row">
          <div class="input__box">
            <label class="input__title" name="work_desc_label"><s:text
                name='homepage.profile.note.to.description' /></label>
            <div class="input__area">
              <input type="text" name="description" id="description" value="${cvWorkInfo.description }" maxlength="200" />
            </div>
            <div class="input__helper" helper-text="<s:text name='homepage.profile.note.courses.taught'/>"></div>
          </div>
        </div>
      </form>
    </div>
  </div>
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__footer">
      <button class="button_main button_primary-reverse" onclick="PsnResume.saveWorkHistory('edit',this);">
        <s:text name='homepage.profile.btn.save' />
      </button>
      <button class="button_main button_primary-cancle" onclick="PsnResume.deleteWork(this);">
        <s:text name='homepage.profile.btn.delete' />
      </button>
      <button class="button_main button_primary-cancle" onclick="hideEditWorkBox(this);">
        <s:text name='homepage.profile.btn.cancel' />
      </button>
    </div>
  </div>
</c:if>
