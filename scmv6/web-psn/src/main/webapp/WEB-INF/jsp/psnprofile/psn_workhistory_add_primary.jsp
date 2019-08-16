<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	addFormElementsEvents();
    //绑定tab切换事件
      $("input").keyup(function(event){
         if (event.keyCode == 9) {
           var startTime = $(this).attr("name");
           if (startTime == "startTime" || startTime == "endTime" || startTime == "fromYear" || startTime == "toYear") {
             $(this).click();
           } else {
             $(".datepicker__box").css("display","none");
           }
         }
      });
});
function addWork_requestData() {
	return {"insName" : $('#newInsNamePrimary').val()};
}
</script>
<div id="add_primary_wokhis" style="display: none;">
  <!-- <div class="dialogs__box oldDiv" style="width: 480px;" id="addPsnWorkBox" dialog-id="addPsnWorkBox"  cover-event="hide"> -->
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header" style="line-height: 56px;">
      <div class="dialogs__header_title">
        <s:text name='homepage.profile.title.work' />
      </div>
    </div>
  </div>
  <div class="dialogs__childbox_adapted">
    <div class="dialogs__content global__padding_24">
      <form autocomplete="off">
        <div class="form__sxn_row">
          <div class="input__box" id="newInsNameDivPrimary">
            <label class="input__title" name="work_ins_label"><s:text name='homepage.profile.note.institution' /></label>
            <div class="input__area">
              <input type="text" name="insName" id="newInsNamePrimary" value="" maxlength="200"
                class="js_autocompletebox" request-url="/psnweb/ac/ajaxautoinstitution" contenteditable="true" />
            </div>
            <div class="input__helper" id="newInsNameHelperPrimary"></div>
          </div>
        </div>
        <div class="form__sxn_row">
          <div class="input__box">
            <label class="input__title" name="work_dep_label"><s:text name='homepage.profile.note.department' /></label>
            <div class="input__area">
              <input type="text" name="department" id="newDepartmentPrimary" value="" maxlength="601"
                class="js_autocompletebox" request-url="/psnweb/ac/ajaxautoinsunit"
                request-data="addWork_requestData();" contenteditable="true" />
            </div>
            <div class="input__helper"></div>
          </div>
          <div class="input__box">
            <label class="input__title" name="work_pos_label"><s:text name='homepage.profile.note.position' /></label>
            <div class="input__area">
              <input type="text" name="position" id="newPositionPrimary" value="" maxlength="100"
                class="js_autocompletebox" request-url="/psnweb/ac/ajaxautoposition" contenteditable="true" />
            </div>
            <div class="input__helper"></div>
          </div>
        </div>
        <div class="form__sxn_row">
          <div class="input__box" id="addWorkFromYearDivPrimary">
            <label class="input__title" name="work_fYear_label"><s:text name='homepage.profile.note.start.year' /></label>
            <div class="input__area">
              <input type="text" name="startTime" id="newStartTimePrimary" value="" readonly datepicker
                date-format="yyyy-mm" />
            </div>
            <div class="input__helper" id="addWorkFromYearHelpPrimary"></div>
          </div>
          <div class="input__box dev_end_year" id="addWorkToYearDivPrimary">
            <label class="input__title" name="work_tYear_label"><s:text name='homepage.profile.note.end.year' /></label>
            <div class="input__area">
              <input type="text" name="endTime" id="newEndTimePrimary" value="" datepicker date-format="yyyy-mm"
                readonly />
            </div>
            <div class="input__helper" id="addWorkToYearHelpPrimary"></div>
          </div>
          <div class="input__box no-flex">
            <div class="input__area">
              <div class="input-radio__sxn">
                <div class="input-custom-style">
                  <input type="checkbox" id="addWorkUpToNowPrimary" onchange="clickUpToNow(this);"> <i
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
              <input type="text" maxlength="200" name="description" id="newDescriptionPrimary" value="" />
            </div>
            <div class="input__helper" helper-text="<s:text name='homepage.profile.note.courses.taught'/>"></div>
          </div>
        </div>
      </form>
    </div>
  </div>
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__footer" style="line-height: 56px;">
      <button class="button_main button_primary-reverse add_work_savebtn"
        onclick="Resume.saveFirstWorkHistory('add', this);">
        <s:text name='homepage.profile.btn.save' />
      </button>
      <button class="button_main add_work_cancelbtn" onclick="Resume.cancelFirstWorkHistory();">
        <s:text name='homepage.profile.btn.cancel' />
      </button>
    </div>
  </div>
  <!-- </div> -->
</div>
