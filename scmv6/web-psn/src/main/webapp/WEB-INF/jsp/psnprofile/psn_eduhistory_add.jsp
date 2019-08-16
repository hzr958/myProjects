<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function addEdu_requestData() {
	return {"insName" : $('#eduInsName').val()};
}
</script>
<div class="dialogs__box oldDiv setnormalZindex" dialog-id="addPsnEduBox" cover-event="hide" style="width: 480px; height: 400px;"
  id="addPsnEduBox">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="dialogs__header_title">
        <s:text name='homepage.profile.title.edu' />
      </div>
    </div>
  </div>
  <div class="dialogs__childbox_adapted">
    <div class="dialogs__content global__padding_24">
      <form autocomplete="off">
        <div class="form__sxn_row">
          <div class="input__box" id="eduInsNameDiv">
            <label class="input__title" name="edu_ins_label"><s:text name='homepage.profile.note.institution' /></label>
            <div class="input__area">
              <input type="text" name="insName" id="eduInsName" value="" maxlength="200" class="js_autocompletebox"
                request-url="/psnweb/ac/ajaxautoinstitution" contenteditable="true" />
            </div>
            <div class="input__helper" id="eduInsNameHelper"></div>
          </div>
        </div>
        <div class="form__sxn_row">
          <div class="input__box">
            <label class="input__title" name="edu_dep_label"><s:text name='homepage.profile.note.major' /></label>
            <div class="input__area">
              <input type="text" name="department" id="eduDepartment" value="" maxlength="200"
                class="js_autocompletebox" request-url="/psnweb/ac/ajaxautoinsunit" request-data="addEdu_requestData();"
                contenteditable="true" />
            </div>
            <div class="input__helper"></div>
          </div>
          <div class="input__box">
            <label class="input__title" name="edu_titolo_label"><s:text name='homepage.profile.note.degree' /></label>
            <div class="input__area">
              <input type="text" name="position" id="eduPosition" value="" maxlength="50" class="js_autocompletebox"
                request-url="/psnweb/ac/ajaxautodegree" contenteditable="true" />
            </div>
            <div class="input__helper"></div>
          </div>
        </div>
        <div class="form__sxn_row">
          <div class="input__box" id="addEduYearDiv">
            <label class="input__title" name="edu_fYear_label"><s:text name='homepage.profile.note.start.year' /></label>
            <div class="input__area">
              <input type="text" name="fromYear" id="eduFromTime" value="" readonly datepicker date-format="yyyy-mm" />
            </div>
            <div class="input__helper" id="addEduYearHelp"></div>
          </div>
          <div class="input__box dev_end_year" id="addEduToYearDiv">
            <label class="input__title" name="edu_tYear_label"><s:text name='homepage.profile.note.end.year' /></label>
            <div class="input__area">
              <input type="text" name="toYear" id="eduToTime" value="" readonly datepicker date-format="yyyy-mm" />
            </div>
            <div class="input__helper" id="addEduToYearHelp"></div>
          </div>
          <div class="input__box no-flex">
            <div class="input__area">
              <div class="input-radio__sxn">
                <div class="input-custom-style">
                  <input type="checkbox" id="addEduUpToNow" onchange="clickUpToNow(this);"> <i
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
            <label class="input__title" name="edu_desc_label"><s:text
                name='homepage.profile.note.to.description' /></label>
            <div class="input__area">
              <input type="text" name="description" id="eduDescription" value="" maxlength="200" />
            </div>
            <div class="input__helper" helper-text="<s:text name='homepage.profile.note.courses.studied'/>"></div>
          </div>
        </div>
      </form>
    </div>
  </div>
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__footer">
      <button class="button_main button_primary-reverse" onclick="saveEduHistory('add',this);">
        <s:text name='homepage.profile.btn.save' />
      </button>
      <button class="button_main button_primary-cancle" onclick="hideAddEduBox(this);">
        <s:text name='homepage.profile.btn.cancel' />
      </button>
    </div>
  </div>
</div>
