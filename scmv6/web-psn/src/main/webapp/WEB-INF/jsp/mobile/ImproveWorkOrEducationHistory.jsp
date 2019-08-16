<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<title>科研之友</title>
<link href="${resmod}/mobile/css/style.css" rel="stylesheet" type="text/css">
<link href="${resmod }/css/jquery.validate.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="${resmod}/js/jquery.validate.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.complete.js"></script>
<script type="text/javascript" src="${resmod}/js/mobile/scm.register.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.autoword.js"></script>
<script type="text/javascript" src="${resmod}/js/link.status.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		
		$("#check-1").attr("checked","checked")
		$("input:radio[name='exprienceRadio']").click(function(){
			var selected = $("input:radio[name='exprienceRadio']:checked").val();
			if(selected=="work"){
				$("#scmworkoredu").html("工作");
				$("#check-2").removeAttr("checked");
				$("#check-1").attr("checked", "checked");
				$("#educationForm").hide();
				$("#workHistoryForm").show();
			}else{
				$("#scmworkoredu").html("教育");
				$("#check-1").removeAttr("checked");
				$("#check-2").attr("checked", "checked");
				$("#educationForm").show();
				$("#workHistoryForm").hide();
			}
			
		});
		
		$("#workHistoryForm").validate({
			errorPlacement: function(error, element) {//修改错误的显示位置.
				element.parent().find("p.workMsg").html("");
				error.appendTo(element.parent().find("p.workMsg"));
                $(error).css("line-height","5px");				
				$("#saveButton").enabled();
		    }, 
			rules: { 
				insName:{ 
					required: true, 
					maxlength:100
				}
			},
			messages: {
				insName: {
				    required: "工作单位不能为空",
				    maxlength: "不能大于100个字符"
				},
			},
			onfocusout: false,
			onkeyup: false,
			onclick: false,
			submitHandler : function(form) {
				formsubmit(form);
			}
		});
		//添加，自动以验证方法
		jQuery.validator.addMethod("isTrueFromYear", function(value, element ,param) {         
		    var flag =compareEduTime();     
		    return this.optional(element) || flag;         
		}, "请选择正确的起止时间");    
		
		$("#educationForm").validate({
			errorPlacement: function(error, element) {//修改错误的显示位置.
				element.parent().find("p.eduMsg").html("");
				error.appendTo(element.parent().find("p.eduMsg"));
                $(error).css("line-height","5px");      				
				$("#saveButton").enabled();
		    }, 
			rules: { 
				insName:{ 
					required: true, 
					maxlength:100
				},  
				isTrueFromYear:{
					required: true ,
					isTrueFromYear: true
				}
			},
			messages: {
				insName: {
				    required: "学校不能为空",
				    maxlength: "不能大于100个字符"
				},
				isTrueFromYear: {
					required: "起止时间不能为空" 
				}
                
			},
			onfocusout: false,
            onkeyup: false,
            onclick: false,
			submitHandler : function(form) {
				formsubmit(form);
			}
		});
		
		setYear();
		setMonth();
		
		$("#saveButton").click(function(){
			var selected = $("input:radio[name='exprienceRadio']:checked").val();
			if(selected=="work"){
				$("#workHistoryForm").submit();
			}else{
				if(compareEduTime()){
    				$("#educationForm").submit();						
				}
			}
		});
		//页面重新加载时清空并启用按钮
		(function(){
			$("input[type='text']").val("");
			$("#saveButton").enabled();
		})();
		
		validateEduTimeSelect();
	});  //end  ready
	
	
	function setYear(){
		var start = 1960;
		var nowDate = new Date();
		var end = nowDate.getFullYear();
		var nowYear = end;
		var years = "";
		for(var i=start; i<=end; i++){
    		years += "<option value='"+i+"'>"+i+"年</option>"; 
		}
		$("#work_from_year").html(years);
		$("#edu_from_year").html(years);
		// 修复 SCM-8878
		years = "";
	    end = nowDate.getFullYear() + 7;
	    for(var i=start; i<=end; i++){
			years += "<option value='"+i+"'>"+i+"年</option>";
		}
		$("#edu_to_year").html(years);
		$("#work_from_year").val(nowYear);
		$("#edu_from_year").val(nowYear-1);
		$("#edu_to_year").val(nowYear);
	}
	
	function setMonth(){
		var months="";
		for(var i=1; i<13; i++){
			months += "<option value='"+i+"'>"+i+"月</option>";
		}
		$(".month").html(months);
	}
	
	function compareEduTime(){
		var startYear = parseInt($('#edu_from_year option:selected') .val());
		var startMonth = parseInt($('#edu_from_month option:selected') .val());
		var toYear = parseInt($('#edu_to_year option:selected') .val());
		var toMonth = parseInt($('#edu_to_month option:selected') .val());
		var result = true;
		if(startYear > toYear){
			result = false;
		}else if(startYear == toYear && startMonth > toMonth ){
			result = false;
		}
		if(!result){
			$("#edu_time_tips").text("请选择正确的起止时间").removeClass("eduMsg ").addClass("error");
		}else{
			$("#edu_time_tips").text("起止时间").removeClass("error ").addClass("eduMsg");
		}
		return result;		
	};
	var formsubmit=function(form){
		$("#saveButton").disabled();
		if($("input:radio[name='exprienceRadio']:checked").val()=="work"){
			$("#workHistoryForm").attr("action",'/psnweb/mobile/saveworkhis');
		}else{
			$("#educationForm").attr("action",'/psnweb/mobile/saveeduhis');
		}
		 var url = window.location.href;
		 var reqUrl = null;
		 if(url.indexOf("reqUrl=")>0){			
			 reqUrl = url.substring(url.indexOf("reqUrl=")+7);
		 }
         if(reqUrl){
        	$("input[name='reqUrl']").val(reqUrl);
         }
		form.submit();
		
		setTimeout(function(){
			//$("input[type='text']").val("");
			$("#saveButton").enabled();
		},2000);
		
	}
	
	//校验工作单位输入框，不为空时恢复提示信息
	function validateWorkInsName(){
	    var insName = $.trim($("#insName").val());
	    if(insName != null && insName != ""){
	        $("#work_insName_tips").text("请输入当前工作单位");
	    }
	}
	
	//校验就读学校输入框，不为空时恢复提示信息
	function validateEduInsName(){
	    var insName = $.trim($("#eduInsName").val());
        if(insName != null && insName != ""){
            $("#edu_insName_tips").text("请输入当前就读学校");
        }
	}
	
	/* //校验就读专业输入框，不为空时恢复提示信息
    function validateStudyMajor(){
        var study = $.trim($("#study").val());
        if(study != null && study != ""){
            $("#edu_study_tips").text("请输入当前专业");
        }
    } */
	
  /* //校验学历输入框，不为空时恢复提示信息
    function validateDegreeName(){
        var degreeName = $.trim($("#degreeName").val());
        if(degreeName != null && degreeName != ""){
            $("#edu_degreeName_tips").text("请输入当前学历");
        }
    } */
	
	//改变选中时，如果有错误信息，需要恢复成提示信息
	function validateEduTimeSelect(){
	    $(".edu_select").change(function(){
		    if(compareEduTime()){
		        $("#edu_time_tips").text("起止时间");
		    }
	    });
	}
	
</script>
</head>
<body class="white_bg">
  <div class="per_inf"></div>
  <div class="con_1">
    <input type="hidden" id="errorMsg" value="${errorMsg }" /> <input type="hidden" id="workInsNameError"
      value="${workInsNameError }" /> <input type="hidden" id="positionError" value="${positionError }" /> <input
      type="hidden" id="departmentError" value="${departmentError}" /> <input type="hidden" id="insNameError"
      value="${insNameError }" /> <input type="hidden" id="studyError" value="${studyError}" />
    <div class="radio_type">
      <ul>
        <li><input type="radio" checked="checked" class="radio" name="exprienceRadio" id="check-1" value="work"
          hidden><label for="check-1">我在工作</label></li>
        <li><input type="radio" class="radio" name="exprienceRadio" id="check-2" value="edu" hidden><label
          for="check-2" title="">我是学生</label></li>
      </ul>
      <div class="clear"></div>
    </div>
    <h2>
           完善<p id="scmworkoredu" style="display: inline;">工作</p>经历：
    </h2>
    <form id="workHistoryForm" action="/psnweb/mobile/saveworkhis" method="post">
      <input type="hidden" id="isActive" name="isActive" value="1" /> <input type="hidden" id="isPrimary"
        name="isPrimary" value="1" /> <input type="hidden" id="anyUser" name="anyUser" value="7" /> <input
        type="hidden" id="wxOpenIdWork" name="wxOpenId" value="${wxOpenId }" /> <input type="hidden" id="wxUrlWrok"
        name="wxUrl" value="${wxUrl }" /> <input type="hidden" id="wxUrlWrok32" name="wxUrl32" value="${domainMobile }" />
      <input type="hidden" id="sysType" name="sysType" value="${sysType}" /> <input type="hidden" id="reqUrl"
        name="reqUrl" value="" />
      <ul class="it_box">
        <li><input type="text" class="inp_text" style="color: #333333; padding-left: 0.3rem;" placeholder="单位"
          maxlength="100" id="insName" name="insName" value="${insName}" onkeyup="validateWorkInsName();" />
          <p class="workMsg" id="work_insName_tips" style="line-height: 0px !important;">${!empty workInsNameError ? workInsNameError : "请输入当前工作单位"}</p></li>
        <li><input type="text" placeholder="部门" style="color: #333333; padding-left: 0.3rem;" maxlength="601"
          name="department" id="department" value="${department}" />
          <p class="workMsg" style="line-height: 0px !important;">${!empty departmentError ? departmentError : "请输入当前工作单位所在部门"}</p></li>
        <li><select class="slt fl" style="color: #333333" name="fromYear" id="work_from_year"><option
              value="2015">2015年</option></select> <select class="slt fr month" style="color: #333333" name="fromMonth"
          id="work_from_month"></select>
          <div class="clear"></div>
          <p style="line-height: 0px !important;">当前单位入职时间</p></li>
        <li><input type="text" style="color: #333333; padding-left: 0.3rem;" placeholder="职称" name="position"
          id="position" maxlength="100" />
          <p class="workMsg" style="line-height: 0px !important;">${!empty positionError ? positionError : "请输入当前职称"}</p></li>
      </ul>
    </form>
    <form id="educationForm" action="/psnweb/mobile/saveeduhis" method="post" style="display: none;">
      <input type="hidden" id="isPrimaryEdu" name="isPrimary" value="1" /> <input type="hidden" id="anyUserEdu"
        name="anyUser" value="7" /> <input type="hidden" id="wxOpenIdEdu" name="wxOpenId" value="${wxOpenId }" /> <input
        type="hidden" id="wxUrlEdu" name="wxUrl" value="${wxUrl }" /> <input type="hidden" id="sysType" name="sysType"
        value="${sysType}" /> <input type="hidden" id="reqUrl" name="reqUrl" value="" />
      <ul class="it_box">
        <li><input type="text" style="color: #333333; padding-left: 0.3rem;" placeholder="学校" maxlength="100"
          id="eduInsName" name="insName" onkeyup="validateEduInsName();" />
          <p class="eduMsg" id="edu_insName_tips" style="line-height: 0px !important;">${!empty insNameError ? insNameError : "请输入当前就读学校"}</p></li>
        <li><input type="text" style="color: #333333; padding-left: 0.3rem;" placeholder="专业" maxlength="200"
          id="study" name="study" />
          <p class="eduMsg" id="edu_study_tips" style="line-height: 0px !important;">${!empty studyError ? studyError : "请输入当前专业"}</p></li>
        <li>
          <div style="display: flex; justify-content: space-between; width: 100%; margin-bottom: -5px;">
            <select class="slt fl deu edu_select" style="color: #333333" name="fromYear" id="edu_from_year"></select> <select
              class="slt fl month edu edu_select" style="color: #333333" name="fromMonth" id="edu_from_month"></select>
          </div>
        </li>
        <p class="fl" style="line-height: 0px !important;">&nbsp;到&nbsp;</p>
        <li>
          <div style="display: flex; justify-content: space-between; width: 100%; margin: 8px 0px;">
            <select class="slt fl edu edu_select" style="color: #333333" name="toYear" id="edu_to_year"></select> <select
              class="slt fl month edu edu_select" style="color: #333333" name="toMonth" id="edu_to_month"></select>
          </div> <input type="hidden" id="isTrueFromYear" style="color: #333333" name="isTrueFromYear" value="isTrueFromYear" />
          <label id="edu_time_tips" class="eduMsg" style="line-height: 0px !important;padding-bottom:10px;">起止时间</label> <!-- <p class="eduMsg" id="edu_time_tips">起止时间</p> -->
        </li>
        <li><input type="text" style="color: #333333; padding-left: 0.3rem;  margin-top: 25px;" placeholder="学历" name="degreeName"
          id="degreeName" maxlength="50" "/>
          <p class="eduMsg" id="edu_degreeName_tips" style="line-height: 0px !important;">${!empty degreeError ? degreeError : "请输入当前学历"}</p></li>
      </ul>
    </form>
  </div>
  <div class="save">
    <input type="button" id="saveButton" class="save_btn" value="保存" style="width: 100%">
  </div>
  <!-- if里面代码不要换行，不然影响样式 -->
  <%-- <c:if test="${sysType=='CXC_LOGIN'}">
    <div class="save"><input type="button" id="saveButton" class="save_btn" value="保存"><a href="${domainMobile}/psnweb/login/tocxc?need_login=1"><input type="button" class="skip_btn" value="跳过..."/></a></div>
</c:if>
<c:if test="${!empty wxOpenId and empty sysType}">
    <div class="save"><input type="button" id="saveButton" class="save_btn" value="保存"><a href="/open/wechat/bind?wxOpenId=${wxOpenId }&url=${wxUrl}&from=reg"><input type="button" class="skip_btn" value="跳过..."/></a></div>
</c:if>
<c:if test="${empty wxOpenId and empty sysType }">
    <div class="save"><input type="button" id="saveButton" class="save_btn" value="保存"><a href="/psnweb/mobile/improvearea"><input type="button" class="skip_btn" value="跳过..."/></a></div>
</c:if> --%>
</body>
</html>
