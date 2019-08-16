<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function editWork_requestData() {
	return {"insName":$('#insName').val()};
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
<c:if test="${!empty workHistory }">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="dialogs__header_title">
        <s:text name='homepage.profile.title.work' />
      </div>
    </div>
  </div>
  <div class="dialogs__childbox_adapted">
    <div class="dialogs__content global__padding_24">
      <form name="editWorkForm" method="post" action="/psnweb/workhistory/ajaxsave">
        <input type="hidden" name="workId" id="workId" value="${workHistory.workId }" /> <input type="hidden"
          name="des3WorkId" id="des3WorkId" value="<iris:des3 code='${workHistory.workId }'/>" />
        <div class="form__sxn_row">
          <div class="input__box" id="insNameDiv">
            <label class="input__title" name="work_ins_label"><s:text name='homepage.profile.note.institution' /></label>
            <div class="input__area">
              <input type="text" name="insName" id="insName" value="${workHistory.insName }" maxlength="200"
                class="js_autocompletebox" request-url="/psnweb/ac/ajaxautoinstitution" contenteditable="true" />
            </div>
            <div class="input__helper" id="insNameHelper"></div>
          </div>
        </div>
        <input type="hidden" name="insId" id="insId" value="${workHistory.insId }" />
        <div class="form__sxn_row">
          <div class="input__box">
            <label class="input__title" name="work_dep_label"><s:text name='homepage.profile.note.department' /></label>
            <div class="input__area">
              <input type="text" name="department" id="department" value="${workHistory.department }" maxlength="601"
                class="js_autocompletebox" request-url="/psnweb/ac/ajaxautoinsunit"
                request-data="editWork_requestData();" contenteditable="true" />
            </div>
            <div class="input__helper"></div>
          </div>
          <div class="input__box">
            <label class="input__title" name="work_pos_label"><s:text name='homepage.profile.note.position' /></label>
            <div class="input__area">
              <input type="text" name="position" id="position" value="${workHistory.position }" maxlength="100"
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
                value="${workHistory.fromYear}<c:if test="${!empty workHistory.fromMonth && !empty workHistory.fromYear}">/</c:if>${workHistory.fromMonth}"
                date-year="${workHistory.fromYear}" date-month="${workHistory.fromMonth}" date-format="yyyy-mm"
                datepicker readonly />
            </div>
            <div class="input__helper" id="workFromYearHelp"></div>
          </div>
          <div class="input__box dev_end_year  <c:if test='${ workHistory.isActive ==1 }'>  input_disabled </c:if> "
            id="workToYearDiv">
            <label class="input__title" name="work_tYear_label"><s:text name='homepage.profile.note.end.year' /></label>
            <div class="input__area">
              <input type="text" name="toYear" id="endTime"
                value="${workHistory.toYear}<c:if test="${!empty workHistory.toYear && !empty workHistory.toMonth}">/</c:if>${workHistory.toMonth}"
                date-year="${workHistory.toYear}" date-month="${workHistory.toMonth}" date-format="yyyy-mm" datepicker
                readonly />
            </div>
            <div class="input__helper" id="workToYearHelp"></div>
          </div>
          <div class="input__box no-flex">
            <div class="input__area">
              <div class="input-radio__sxn">
                <div class="input-custom-style">
                  <input type="checkbox" id="editWorkUpToNow" onchange="clickUpToNow(this);"
                    <c:if test="${ workHistory.isActive ==1 }">  checked="checked" </c:if>> <i
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
              <input type="text" name="description" id="description" value="${workHistory.description }" maxlength="200" />
            </div>
            <div class="input__helper" helper-text="<s:text name='homepage.profile.note.courses.taught'/>"></div>
          </div>
        </div>
      </form>
    </div>
  </div>
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__footer">
      <button class="button_main button_primary-reverse" onclick="saveWorkHistory('edit',this);">
        <s:text name='homepage.profile.btn.save' />
      </button>
      <button class="button_main button_primary-cancle"
        onclick="delWorkHistory('<iris:des3 code="${workHistory.workId }"></iris:des3>' , this);">
        <s:text name='homepage.profile.btn.delete' />
      </button>
      <button class="button_main button_primary-cancle" onclick="hideEditWorkBox(this);">
        <s:text name='homepage.profile.btn.cancel' />
      </button>
    </div>
  </div>
</c:if>
