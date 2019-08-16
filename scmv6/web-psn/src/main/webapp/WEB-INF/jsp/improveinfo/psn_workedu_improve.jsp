<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	addFormElementsEvents();
 	 //为必填项添加校验
     $("#eduInsName").bind('blur',Improve.checkEduName);
     $("#eduFromTime").bind('blur',Improve.checkEduDate);
     $("#eduToTime").bind('blur',Improve.checkEduEndDate);
 	 $("#newInsName").bind('blur',Improve.checkWorkName);
 	 $("#newStartTime").bind('blur',Improve.checkWorkDate);
 	$("#newEndTime").bind('blur',Improve.checkWorkEndDate);
 	initWorkEduMessage();
 	
 	//绑定tab切换事件
 	$("input").keyup(function(event){
 	   if (event.keyCode == 9) {
 	     var startTime = $(this).attr("name");
 	     if (startTime == "startTime" || startTime == "endTime") {
 	       $(this).click();
 	     } else {
 	       $(".datepicker__box").css("display","none");
 	     }
 	   }
 	})
 
});
/**
 * 进行人员存在的信息的初始化，如机构名称
 */
function initWorkEduMessage(){
    $.ajax({
    	url:'/psnweb/workhistory/ajaximproveget',
        type:'post',
        dataType:'json',
        timeout: 10000,
        success: function(data){
            if(data.result == "noPerson" || data.result == "noWork"){
            	// 人员不存在或者工作经历没有的情况下
            	$('#newInsName').val("");
            	return;
            }
            $('#newInsName').val(data.insName);
            $('#newInsName').attr("code",data.code);
            
            if(data.department != null){
            	initMessageStyle('#newDepartment',data.department);
            }
            if(data.position != null){
                initMessageStyle('#newPosition',data.position);
            }
          /*   if(data.fromMonth != null){
            	initDateStyle('#newStartTime',data.fromYear,data.fromMonth);
            }
            if(data.toMonth != null){
                initDateStyle('#newEndTime',data.toYear,data.toMonth);
            } */
        },
        error : function(data){
            $('#newInsName').val("");
        }
    });
};

function initMessageStyle(idName,msg){
	var $obj = $(idName);
	$obj.parent().parent().addClass("input_not-null");
	$obj.val(msg);
}

/* function initDateStyle(idName,year,month){
    var $obj = $(idName);
    $obj.parent().parent().addClass("input_not-null");
    $obj.attr("date-year",year);
    $obj.attr("date-month",month);
} */

function addWork_requestData() {
	return {"insName" : $('#newInsName').val()};
};
function addEdu_requestData() {
	return {"insName" : $('#eduInsName').val()};
};
function change1(obj) {
	$(obj).addClass("dialogs__header_title-checked");
	$(obj).removeClass("dialogs__header_title-unchecked");
	$('.span2').removeClass("dialogs__header_title-checked");
	$('.span2').addClass("dialogs__header_title-unchecked");
	$('#dev_select_workedu').val("work");
	$('.dialog__item-box__box').show();
	$('.dialog__item-box__box2').hide();
};
function change2(obj) {
	$(obj).addClass("dialogs__header_title-checked");
	$(obj).removeClass("dialogs__header_title-unchecked");
	$('.span1').removeClass("dialogs__header_title-checked");
	$('.span1').addClass("dialogs__header_title-unchecked");
	$('#dev_select_workedu').val("edu");
	$('.dialog__item-box__box2').show();
	$('.dialog__item-box__box').hide();
};
</script>
<input type="hidden" id="dev_select_workedu" value="work" />
<input type="hidden" id="needWorkEdu" value="${needWorkEdu}" />
<input type="hidden" class="needScienceArea" value="${needArea}" />
<input type="hidden" class="needKeyWords" value="${needKeywords}" />
<div class="dialogs__box setnormalZindex" style="width: 480px; z-index: 52;" id="psnWorkEduBox" dialog-id="psnWorkEduBox">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="dialogs__header_title" style="font-size: 18px;">
        <s:text name="psn.improve.info.complete"></s:text>
      </div>
    </div>
  </div>
  <div class="dialogs__childbox_adapted dialog__item-box__adapted">
    <div class="dialogs__content global__padding_12" style="border-bottom: 1px solid #eeeeee;">
      <div style="display: flex; align-items: center; margin-right: 12px;">
        <div style="font-size: 16px; margin-left: 12px;">
          <s:text name="psn.improve.info.status"></s:text>
        </div>
        <div style="display: flex; justify-content: space-around;">
          <div class="dialogs__header_title"
            style="line-height: 17px; font-size: 16px; font-weight: 500; padding: 0 8px 0 16px; flex-grow: 1; display: flex;">
            <span onclick="change1(this);" class="dialogs__header_title-flag dialogs__header_title-checked span1"></span>
            <s:text name="psn.improve.info.work"></s:text>
          </div>
          <div class="dialogs__header_title"
            style="line-height: 17px; font-size: 16px; font-weight: 500; padding: 0 8px 0 16px; flex-grow: 1; display: flex;">
            <span onclick="change2(this);" class="dialogs__header_title-flag dialogs__header_title-unchecked span2"></span>
            <s:text name="psn.improve.info.school"></s:text>
          </div>
        </div>
      </div>
    </div>
    <div class="dialogs__content global__padding_24 dialog__item-box__box">
      <form autocomplete="off">
        <div class="form__sxn_row">
          <div class="input__box" id="newInsNameDiv">
            <label class="input__title" name="work_ins_label"><s:text name='homepage.profile.note.institution' /></label>
            <div class="input__area">
              <input type="text" name="insName" id="newInsName" value="${insName}" maxlength="200"
                class="js_autocompletebox" request-url="/psnweb/ac/ajaxautoinstitution" contenteditable="true"
                code="${insId}">
            </div>
            <div class="input__helper" id="newInsNameHelper" invalid-message=""></div>
          </div>
        </div>
        <div class="form__sxn_row">
          <div class="input__box">
            <label class="input__title" name="work_dep_label"><s:text name='homepage.profile.note.department' /></label>
            <div class="input__area">
              <input type="text" name="department" id="newDepartment" value="" maxlength="601"
                class="js_autocompletebox" request-url="/psnweb/ac/ajaxautoinsunit"
                request-data="addWork_requestData();" contenteditable="true">
            </div>
            <div class="input__helper"></div>
          </div>
          <div class="input__box">
            <label class="input__title" name="work_pos_label"><s:text name='homepage.profile.note.position' /></label>
            <div class="input__area">
              <input type="text" name="position" id="newPosition" value="" maxlength="100" class="js_autocompletebox"
                request-url="/psnweb/ac/ajaxautoposition" contenteditable="true">
            </div>
            <div class="input__helper"></div>
          </div>
        </div>
        <div class="form__sxn_row">
          <div class="input__box" id="addWorkFromYearDiv">
            <label class="input__title" name="work_fYear_label"><s:text name='homepage.profile.note.start.year' /></label>
            <div class="input__area">
              <input type="text" name="startTime" id="newStartTime" value="" readonly="" datepicker=""
                date-format="yyyy-mm" date-year="" date-month="" date-date="">
            </div>
            <div class="input__helper" id="addWorkFromYearHelp" invalid-message=""></div>
          </div>
          <div class="input__box dev_end_year" id="addWorkToYearDiv">
            <label class="input__title" name="work_tYear_label"><s:text name='homepage.profile.note.end.year' /></label>
            <div class="input__area">
              <input type="text" name="endTime" id="newEndTime" value="" datepicker="" date-format="yyyy-mm" readonly=""
                date-year="" date-month="" date-date="">
            </div>
            <div class="input__helper" id="addWorkToYearHelp"></div>
          </div>
          <div class="input__box no-flex input_not-null">
            <div class="input__area">
              <div class="input-radio__sxn">
                <div class="input-custom-style">
                  <input type="checkbox" id="addWorkUpToNow" onchange="Improve.clickUpToNow(this);"> <i
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
      </form>
    </div>
    <div class="dialogs__content global__padding_24 dialog__item-box__box2" style="display: none;">
      <form autocomplete="off">
        <div class="form__sxn_row">
          <div class="input__box" id="eduInsNameDiv">
            <label class="input__title" name="edu_ins_label"><s:text name='homepage.profile.note.institution' /></label>
            <div class="input__area">
              <input type="text" name="insName" id="eduInsName" value="" maxlength="200" class="js_autocompletebox"
                request-url="/psnweb/ac/ajaxautoinstitution" contenteditable="true">
            </div>
            <div class="input__helper" id="eduInsNameHelper" invalid-message=""></div>
          </div>
        </div>
        <div class="form__sxn_row">
          <div class="input__box">
            <label class="input__title" name="edu_dep_label"><s:text name='homepage.profile.note.major' /></label>
            <div class="input__area">
              <input type="text" name="department" id="eduDepartment" value="" maxlength="200"
                class="js_autocompletebox" request-url="/psnweb/ac/ajaxautoinsunit" request-data="addEdu_requestData();"
                contenteditable="true">
            </div>
            <div class="input__helper"></div>
          </div>
          <div class="input__box">
            <label class="input__title" name="edu_titolo_label"><s:text name='homepage.profile.note.degree' /></label>
            <div class="input__area">
              <input type="text" name="position" id="eduPosition" value="" maxlength="50" class="js_autocompletebox"
                request-url="/psnweb/ac/ajaxautodegree" contenteditable="true">
            </div>
            <div class="input__helper"></div>
          </div>
        </div>
        <div class="form__sxn_row">
          <div class="input__box" id="addEduYearDiv">
            <label class="input__title" name="edu_fYear_label"><s:text name='homepage.profile.note.start.year' /></label>
            <div class="input__area">
              <input type="text" name="fromYear" id="eduFromTime" value="" readonly="" datepicker=""
                date-format="yyyy-mm" date-year="" date-month="" date-date="">
            </div>
            <div class="input__helper" id="addEduYearHelp" invalid-message=""></div>
          </div>
          <div class="input__box dev_end_year" id="addEduToYearDiv">
            <label class="input__title" name="edu_tYear_label"><s:text name='homepage.profile.note.end.year' /></label>
            <div class="input__area">
              <input type="text" name="toYear" id="eduToTime" value="" readonly="" datepicker="" date-format="yyyy-mm"
                date-year="" date-month="" date-date="">
            </div>
            <div class="input__helper" id="addEduToYearHelp" invalid-message=""></div>
          </div>
          <div class="input__box no-flex input_not-null">
            <div class="input__area">
              <div class="input-radio__sxn">
                <div class="input-custom-style">
                  <input type="checkbox" id="addEduUpToNow" onchange="Improve.clickUpToNow(this);"> <i
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
      </form>
    </div>
  </div>
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__footer">
      <s:if test="needArea == true || needKeywords == true">
        <button class="button_main button_primary-reverse" onclick="Improve.saveWorkEdu('work',this);">
          <s:text name="psn.improve.info.next"></s:text>
        </button>
      </s:if>
      <s:else>
        <button class="button_main button_primary-reverse" onclick="Improve.saveWorkEdu('work',this);">
          <s:text name="homepage.profile.btn.save"></s:text>
        </button>
      </s:else>
    </div>
  </div>
</div>