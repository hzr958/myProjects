<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function editEdu_requestData() {
	return {"insName":$('#editEduInsName').val()};
}
$(function(){
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
})
</script>
<c:if test="${!empty eduHistory }">
  <!-- <div class="dialogs__box" style="width: 480px;" dialog-id="editPsnEduBox"  cover-event="hide">  -->
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="dialogs__header_title">
        <s:text name='homepage.profile.title.edu' />
      </div>
    </div>
  </div>
  <div class="dialogs__childbox_adapted">
    <div class="dialogs__content global__padding_24">
      <form name="editEduForm" method="post" action="">
        <input type="hidden" name="eduId" id="eduId" value="${eduHistory.eduId }" /> <input type="hidden"
          name="des3EduId" id="des3EduId" value="<iris:des3 code='${eduHistory.eduId }'/>" />
        <div class="form__sxn_row">
          <div class="input__box" id="editEduInsNameDiv">
            <label class="input__title" name="edu_ins_label"><s:text name='homepage.profile.note.institution' /></label>
            <div class="input__area">
              <input type="text" name="insName" id="editEduInsName" value="${eduHistory.insName }" maxlength="200"
                class="js_autocompletebox" request-url="/psnweb/ac/ajaxautoinstitution" contenteditable="true" />
            </div>
            <div class="input__helper" id="editEduInsNameHelper"></div>
          </div>
        </div>
        <input type="hidden" name="insId" id="editEduInsId" value="${eduHistory.insId }" />
        <div class="form__sxn_row">
          <div class="input__box">
            <label class="input__title" name="edu_dep_label"><s:text name='homepage.profile.note.major' /></label>
            <div class="input__area">
              <input type="text" name="study" id="editEduStudy" value="${eduHistory.study }" maxlength="200"
                class="js_autocompletebox" request-url="/psnweb/ac/ajaxautoinsunit"
                request-data="editEdu_requestData();" contenteditable="true" />
            </div>
            <div class="input__helper"></div>
          </div>
          <div class="input__box">
            <label class="input__title" name="edu_titolo_label"><s:text name='homepage.profile.note.degree' /></label>
            <div class="input__area">
              <input type="text" name="degree" id="editEduDegree" value="${eduHistory.degreeName }" maxlength="50"
                class="js_autocompletebox" request-url="/psnweb/ac/ajaxautodegree" contenteditable="true" />
            </div>
            <div class="input__helper"></div>
          </div>
        </div>
        <div class="form__sxn_row">
          <div class="input__box" id="editEduYearDiv">
            <label class="input__title" name="edu_fYear_label"><s:text name='homepage.profile.note.start.year' /></label>
            <div class="input__area">
              <input type="text" name="fromYear" id="editEduFromTime"
                value="${eduHistory.fromYear}<c:if test="${!empty eduHistory.fromMonth && !empty eduHistory.fromYear}">/</c:if>${eduHistory.fromMonth}"
                date-year="${eduHistory.fromYear}" date-month="${eduHistory.fromMonth}" date-format="yyyy-mm" readonly
                datepicker />
            </div>
            <div class="input__helper" id="editEduYearHelp"></div>
          </div>
          <div class="input__box dev_end_year  <c:if test='${eduHistory.isActive == 1}'> input_disabled </c:if>"
            id="editEduToYearDiv">
            <label class="input__title" name="edu_tYear_label"><s:text name='homepage.profile.note.end.year' /></label>
            <div class="input__area">
              <input type="text" name="toYear" id="editEduToTime"
                value="${eduHistory.toYear}<c:if test="${!empty eduHistory.toYear && !empty eduHistory.toMonth}">/</c:if>${eduHistory.toMonth}"
                date-year="${eduHistory.toYear }" date-month="${eduHistory.toMonth }" date-format="yyyy-mm" readonly
                datepicker />
            </div>
            <div class="input__helper" id="editEduToYearHelp"></div>
          </div>
          <div class="input__box no-flex">
            <div class="input__area">
              <div class="input-radio__sxn">
                <div class="input-custom-style">
                  <input type="checkbox" id="editEduUpToNow" onchange="clickUpToNow(this);"
                    <c:if test="${eduHistory.isActive == 1}"> checked="checked" </c:if>> <i
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
              <input type="text" name="description" id="editEduDescription" value="${eduHistory.description }"
                maxlength="200" />
            </div>
            <div class="input__helper" helper-text="<s:text name='homepage.profile.note.courses.studied'/>"></div>
          </div>
        </div>
      </form>
    </div>
  </div>
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__footer">
      <button class="button_main button_primary-reverse" onclick="saveEduHistory('edit',this);">
        <s:text name='homepage.profile.btn.save' />
      </button>
      <button class="button_main" onclick="delEduHistory('<iris:des3 code="${eduHistory.eduId }"></iris:des3>',this);">
        <s:text name='homepage.profile.btn.delete' />
      </button>
      <button class="button_main" onclick="hideEditEduBox(this);">
        <s:text name='homepage.profile.btn.cancel' />
      </button>
    </div>
  </div>
  <!-- </div> -->
</c:if>
