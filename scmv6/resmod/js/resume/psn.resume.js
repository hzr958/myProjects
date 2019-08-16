var PsnResume = PsnResume ? PsnResume : {};
var serviceTypeNfsc = "nfsc";

PsnResume.loadPsnResumeModules = function(){
	//人员基本信息
	if($("#baseInfoModuleStatus").val() == 0){
		PsnResume.psnBaseinfo();
	}else{
		$("#psn_base_info_div").hide();
	}
	//教育经历
	if($("#eduModuleStatus").val() == 0){
		PsnResume.psnEducation();
	}
	//工作经历
	if($("#workModuleStatus").val() == 0){
		PsnResume.psnWorkinfo();
	}
	//项目
	if($("#prjModuleStatus").val() == 0){
		PsnResume.showResumePrj();
	}
	//代表性成果模块
	if($("#pubModuleStatus").val() == 0){
		PsnResume.showCVRepresentPub(5);
	}else{
		$("#represent_pub_title_div").hide();
	}
	//其他代表性成果
	if($("#otherPubModuleStatus").val() == 0){
		PsnResume.showCVRepresentPub(6);
	}else{
		$("#other_pub_title_div").hide();
	}
	if($("#otherPubModuleStatus").val() != 0 && $("#pubModuleStatus").val() != 0){
		$("#pub_title_main_div").hide();
	}
}



//显示代表性成果
PsnResume.showCVRepresentPub = function(moduleId){
	$.ajax({
		url: "/pub/resume/ajaxpubinfo",
		type: "post",
		data: {
			"des3CVId": $("#des3ResumeId").val(),
			"serviceType": serviceTypeNfsc,
			"moduleId": moduleId
		},
		dataType:"html",
		success: function(data){
			if(moduleId == 5){
				$("#cv_representpub").html(data);
			}else{
				$("#cv_otherpub").html(data);
			}
		},
		error: function(){}
	});
}
   
//显示简历列表
PsnResume.showResumeList = function(obj) {
	Resume.doHitMore(obj,2000);
	$.ajax({
		url: "/psnweb/resume/ajaxlist",
		type: "post",
		dataType:"html",
		success: function(data){
			Resume.ajaxTimeOut(data , function(){
				if (data.indexOf("toDetail") > -1) {
					PsnResume.createResume();
				} else {
					var option={'targettxt':data};
					resumetipbox(option);
				}
			});
		}
	});
};

//显示简历-项目信息
PsnResume.showResumePrj = function() {
	$.ajax({
		url: "/psnweb/resume/ajaxprjinfo",
		type: "post",
		dataType:"html",
		data: {
			"des3ResumeId": $("#des3ResumeId").val(),
			"serviceType": serviceTypeNfsc
		},
		success: function(data){
			Resume.ajaxTimeOut(data , function(){
				$('#resume_prj').html("");
				$('#resume_prj').html(data);
			});
		}
	});
};

//显示简历-项目弹出框
PsnResume.showCVPrjBox = function(obj) {
	showDialog("CVPrjBox");
//	PsnResume.getPsnPrj();
	PsnResume.getResumePrj();
	/*$.ajax({
		url: "/psnweb/resume/ajaxupdateprjinfo",
		type: "post",
		dataType: "html",
		data: {
			"des3ResumeId": $("#des3ResumeId").val(),
			"serviceType": serviceTypeNfsc
			},
		success: function(data){
			Resume.ajaxTimeOut(data , function(){
				$("#CVPrjBox").html(data);
				addFormElementsEvents($("#CVPrjBox")[0]);
				showDialog("CVPrjBox");
				PsnResume.getPsnPrj();
				PsnResume.getResumePrj();
			});
		}
	});*/
	//已导入项目列表
//	$.ajax({
//		url: "/psnweb/resume/ajaxresumeprjlist",
//		type: "post",
//		dataType: "html",
//		data: {
//			"des3ResumeId": $("#des3ResumeId").val(),
//			"serviceType": serviceTypeNfsc
//			},
//		success: function(data){
//			Resume.ajaxTimeOut(data , function(){
//				$("#CVPrjBox").html(data);
//				addFormElementsEvents($("#CVPrjBox")[0]);
//				showDialog("CVPrjBox");
//				PsnResume.getPsnPrj();
//			});
//		}
//	});
};

//显示简历-项目弹出框-项目列表
PsnResume.getPsnPrj = function() {
	window.Mainlist({
		name: 'psnPrjList',
		listurl: '/psnweb/resume/ajaxprjlist',
		listdata: {"serviceType": serviceTypeNfsc},
		listcallback: function(xhr){
//			PsnResume.getResumePrj();
			PsnResume.resetPrjCheckStatus();
		},
		method: 'scroll'
	});
};

//显示简历-项目弹出框-已导入项目列表
PsnResume.getResumePrj = function() {
	$.ajax({
		url: "/psnweb/resume/ajaxresumeprjlist",
		type: "post",
		dataType: "html",
		data: {
			"des3ResumeId": $("#des3ResumeId").val(),
			"serviceType": serviceTypeNfsc
			},
		success: function(data){
			Resume.ajaxTimeOut(data , function(){
				$('#addedPrjList').html("");
				$('#addedPrjList').html(data);
				PsnResume.getPsnPrj();
//				PsnResume.resetPrjCheckStatus();
			});
		}
	});
};

PsnResume.resetPrjCheckStatus = function() {
	var prjIds = $("#resumePrjIds").val();
	if (prjIds != undefined) {
		$(".prjCheckStatus").each(function(){
			var _this = $(this);
			var prjId = _this.attr("checkPrjId");
			if(prjIds.indexOf(prjId) == -1){
				_this.html("add");
				_this.css("color", "");
				var title = $("#title_"+ prjId).html();
				var authorName = $("#authorNames_"+prjId).html();
				$("#unadded_"+prjId).attr("onclick", "PsnResume.addToRepresentPrj('"+prjId + "')");
				_this.removeClass("prjCheckStatus");
			}else{
				_this.html("check");
				_this.css("color", "forestgreen");
				var title = $("#title_"+ prjId).html();
				var authorName = $("#authorNames_"+prjId).html();
				$("#unadded_"+prjId).attr("onclick", "");
				_this.removeClass("prjCheckStatus");
			}
		});
	}
};

PsnResume.addToRepresentPrj = function(id) {
//	var prjSum = parseInt($("#representPrjSum").val());
	var title = $("#title_"+id).html();
	var author = $("#authorNames_"+id).html();
//	if(prjSum < 5){
		$("#checkDiv_"+id).children("i").html("check");
		$("#checkDiv_"+id).children("i").css("color","forestgreen");
//		$("#checkDiv_"+id).removeAttr("onclick");
		$("#unadded_"+id).removeAttr("onclick");
		var addHtml = $("#addedPrjItem").prop("outerHTML");
		addHtml = addHtml.replace("replacetitle", title).replace("replaceAuthorName", author).replace("addedPrjItem", "addedPrj_" + id).replace("onclickmethod", 'onclick="PsnResume.delRepresentPrj(\''+id+'\');"').replace("$prjId$", id);
		$("#addedPrjList").append(addHtml);
		$("#addedPrj_" + id).show();
		var prjIds = $("#resumePrjIds").val();
		$("#resumePrjIds").val(prjIds + id + ",");
		//$("#representPrjSum").val(prjSum + 1);
//	}
};

PsnResume.delRepresentPrj = function(id, title, author) {
	var title = $("#selected_prj_title_"+id).html();
	var author = $("#selected_prj_author_"+id).html();
	var selectDiv = "#checkDiv_"+id;
	var selectedItem = "#unadded_"+id;
	$(selectDiv).children("i").html("add");
	$(selectDiv).children("i").css("color","");
	$(selectedItem).attr("onclick", "PsnResume.addToRepresentPrj('"+id + "')");
	$("#addedPrj_" + id).remove();
	var prjIds = $("#resumePrjIds").val();
	if (prjIds != undefined) {
		$("#resumePrjIds").val(prjIds.replace(id + ",", ""));
	}
	//var prjSum = parseInt($("#representPrjSum").val());
	//$("#representPrjSum").val(prjSum - 1);
};


//关闭简历-项目弹出框
PsnResume.hideCVPrjBox = function(obj) {
	if(obj != undefined){//防止重复点击
		Resume.doHitMore(obj ,3000);
	}
	//$("#resumePrjIds").val($("#resumePrjIdsOld").val());
	hideDialog("CVPrjBox");
};

//保存简历-项目模块信息
PsnResume.saveCVPrj = function(obj) {
	if(obj != undefined){//防止重复点击
		Resume.doHitMore(obj ,3000);
	}
	$.ajax({
		url: "/psnweb/resume/ajaxupdateprjinfo",
		type: "post",
		dataType: "html",
		data:{
			"des3ResumeId": $("#des3ResumeId").val(),
			"serviceType": serviceTypeNfsc,
			'addedPrjIds':$("#resumePrjIds").val()
		},
		success: function(data){
			Resume.ajaxTimeOut(data , function(){
				hideDialog("CVPrjBox");
				PsnResume.showResumePrj();
//				$('#dev_prjInfo').remove();
			});
		}
	});
};

//显示个人基本信息
PsnResume.psnBaseinfo = function(resumeId){
	var des3ResumeId = $("#des3ResumeId").val();
	$("#baseinfo").html("");
	$.ajax({
		url :  '/psnweb/resume/ajaxbaseinfo',
		type : 'POST',
		dataType : 'json',
		data : {'serviceType' : 'nfsc',
			    'des3ResumeId' : des3ResumeId},
		success : function(data){ 
			if(data.result == "success"){
				if(data.isShowMoule == "1"){
					$("#psnInfoEditIcon").remove()
				}else{
					$("#baseinfo").html(data.baseinfo);
				}
			}				
		},
		error : function(data){
		}
	});
};

//显示教育经历
PsnResume.psnEducation = function(resumeId){
	var des3ResumeId = $("#des3ResumeId").val();
	$("#resume_education").html("");
	$.ajax({
		url :  '/psnweb/resume/ajaxeduinfo',
		type : 'POST',
		dataType : 'html',
		data : {'serviceType':'nfsc',
			    'des3ResumeId' : des3ResumeId},
		success : function(data){ 
			$("#resume_education").html(data);
		},
		error : function(data){
		}
	});
}

//显示工作经历
PsnResume.psnWorkinfo = function(){
	var des3ResumeId = $("#des3ResumeId").val();
	$.ajax({
		url :  '/psnweb/resume/ajaxworkinfo',
		type : 'POST',
		dataType : 'html',
		data : {'serviceType':'nfsc',
		    'des3ResumeId' : des3ResumeId},
		success : function(data){ 
			$("#resume_workinfo").html(data);
		},
		error : function(data){
		}
	});
};


//代表性成果-----------------begin
//弹出成果选择框
PsnResume.showPsnCVPubBox = function(moduleId){
	$.ajax({
		url: "/pub/psncv/ajaxselectpub",
		type: "post",
		dataType: "html",
		data: {
			"des3CVId": $("#des3ResumeId").val(),
			"moduleId": moduleId
		},
		success: function(data){
//			Resume.ajaxTimeOut(data ,function(){
				$("#cvPubBox").html(data);
				addFormElementsEvents($("#cvPubBox")[0]);
				showDialog("cvPubBox");
				PsnResume.getPsnOpenPub(moduleId);
//			});
			
		},
		error: function(){
			
		}
	});
	
}

//获取人员公开成果
PsnResume.getPsnOpenPub = function(moduleId){
	window.Mainlist({
		name: 'psnOpenPubList',
		listurl: '/pub/ajaxgetopenlist',
		listdata: {
			"des3CnfId": $("#des3CnfId").val()
		},
		listcallback: function(xhr){
			PsnResume.resetPubCheckStatus(moduleId);
		},
		method: 'scroll'
	});
}

//隐藏弹框
PsnResume.hideRepresentPubBox = function(obj, moduleId){
	if(obj != undefined){//防止重复点击
//		Resume.doHitMore(obj ,3000);
	}
	if(moduleId == 5){
		$("#addToRepresentPubIds").val($("#addToRepresentPubIdsOld").val());
	}else{
		$("#otherSelectedPubIds").val($("#otherSelectedPubIdsOld").val());
	}
	hideDialog("cvPubBox");
	$("#pubPageNo").val(1);
}


//保存代表性成果
PsnResume.saveCVRepresentPub = function(obj, moduleId){
	var selectPubJson = [];
	var pubIds = "";
	var moduleSeq = 5;
	var moduleTitle = "代表性成果";
	$("#addedPubList").find(".main-list__item").each(function(index){
		var des3PubId = $(this).attr("des3pubid");
		if (des3PubId != "") {
			var pubJson = {"des3PubId":des3PubId, "seqNo": index+1};
			selectPubJson.push(pubJson);
		}
	});

	if(moduleId == 6){
		moduleSeq = 6;
		moduleTitle = "其他成果";
	}

	$.ajax({
		url: "/pub/resume/ajaxsaverepresentpub",
		type: "post",
		data: {
			"des3ResumeId": $("#des3ResumeId").val(),
			"serviceType": serviceTypeNfsc,
			"moduleId": moduleId,
			"moduleSeq" : moduleSeq,
			"moduleTitle": moduleTitle,
			"moduleInfo": JSON.stringify(selectPubJson)
		},
		dataType:"json",
		success: function(data){
			PsnResume.hideRepresentPubBox(obj, moduleId);
			PsnResume.showCVRepresentPub(moduleId);
		},
		error: function(){}
	});
}

PsnResume.viewPubDetails = function(des3Id, Id){
	var params="des3Id="+encodeURIComponent(des3Id);
	var pram ={pubId:Id,des3Id:des3Id}; 
	newwindow = window.open("about:blank");
	$.ajax({
		url:"/pubweb/publication/ajaxview",
		type:"post",
		data:pram,
		dataType:"json",
		success:function(data){
			if(data.result==2){
				 newwindow.location.href=data.shortUrl;
				 newwiondow.focus();
			}
			if(data.result==1 || data.ajaxSessionTimeOut=='yes'){
				newwindow.location.href="/pubweb/outside/details?"+params+"&currentDomain="+"/pubweb"+"&pubFlag=1"; 
				newwindow.focus();
			}else if(data.result==0){
			 	newwindow.location.href="/pubweb/publication/wait?"+params;
			}
		},
		error:function(){
			
		}
	});
}

//重置成果列表中成果选中状态
PsnResume.resetPubCheckStatus = function(moduleId){
	var $selectPub = $("#addedPubList").find(".main-list__item");
	$selectPub.each(function(){
		var des3PubId = $(this).attr("des3pubid");	
		$("#psnOpenPubList").find(".main-list__item[des3PubId='"+des3PubId+"']")
		.find(".checkPubDiv").attr("onclick", "")
		.find("i").html("check").css("color", "forestgreen").removeClass("pubCheckStatus");
	});
	
}

//代表性成果-------------------end


//=============================================================
/**
 * 打开简历详情
 */
PsnResume.openResumDetail = function(des3ResumeId){
	window.location.href = "/psnweb/resume/resumedetail?des3ResumeId="+des3ResumeId;
};

/**
 * 删除教育经历
 */
PsnResume.deleteEdu = function(obj){
	if(obj != undefined){//防止重复点击
		Resume.doHitMore(obj ,3000);
	}
	var des3ResumeId = $("#des3ResumeId").val();
	var seqEdu = $("#seqEdu").val()
	$.ajax({
		url :  '/psnweb/resume/ajaxdeleteedu',
		type : 'POST',
		dataType : 'json',
		data : {'serviceType':'nfsc',
		    'des3ResumeId' : des3ResumeId,
		    'seqEdu' : seqEdu
		    },
		success : function(data){ 
			if(data.result == "success"){
				//关掉弹窗
				hideEditEduBox();
				PsnResume.psnEducation();
			}else{
				scmpublictoast(homepage.systemBusy,1000);
			}
		},
		error : function(data){
		}
	});
};

/**
 * 删除工作经历
 */
PsnResume.deleteWork = function(obj){
	if(obj != undefined){//防止重复点击
		Resume.doHitMore(obj ,3000);
	}
	var des3ResumeId = $("#des3ResumeId").val();
	var seqWork = $("#seqWork").val()
	$.ajax({
		url :  '/psnweb/resume/ajaxdeletework',
		type : 'POST',
		dataType : 'json',
		data : {'serviceType':'nfsc',
		    'des3ResumeId' : des3ResumeId,
		    'seqWork' : seqWork
		    },
		success : function(data){ 
			if(data.result == "success"){
				//关掉弹窗
				hideEditWorkBox();
				PsnResume.psnWorkinfo();
			}else{
				scmpublictoast(homepage.systemBusy,1000);
			}
		},
		error : function(data){
		}
	});
};

/**
 * 创建简历
 */
PsnResume.createResume = function(){
	$.ajax({
		url :  '/psnweb/resume/ajaxadd',
		type : 'POST',
		dataType : 'json',
		data : {'serviceType':'nfsc',
		    'resumeName' : '我的简历'},
		success : function(data){ 
			Resume.ajaxTimeOut(data , function(){
				if (data.result == "overtake") {
					scmpublictoast("每个人最多10份简历",1000);
				} else {
					PsnResume.openResumDetail(data.des3ResumeId);
				}
			});
		},
		error : function(data){
		}
	});
};
function compareEduTime(startYear, startMonth, toYear, toMonth){
	if(startYear > toYear){
		return false;
	}else if(startYear == toYear && startMonth > toMonth ){
		return false;
	}else{
		return true;
	}
	
};
/**
 * 添加保存教育经历
 */
PsnResume.saveEduHistory = function(type ,obj){
	var des3ResumeId = $("#des3ResumeId").val();
	if(obj != undefined){//防止重复点击
		Resume.doHitMore(obj ,3000);
	}
	var result = true;
	if("add" == type && $("#eduInsName").val().trim() == ""){
		$("#eduInsNameDiv").addClass("input_invalid");
		$("#eduInsNameHelper").attr("invalid-message", homepage.inputInsName);
		result = false;
	}
	if("edit" == type && $("#editEduInsName").val().trim() == ""){
		$("#editEduInsNameDiv").addClass("input_invalid");
		$("#editEduInsNameHelper").attr("invalid-message", homepage.inputInsName);
		result = false;
	}
	var startYear = parseInt($("#eduFromTime").attr("date-year"));
	var startMonth = parseInt($("#eduFromTime").attr("date-month"));
	var toYear = parseInt($("#eduToTime").attr("date-year"));
	var toMonth = parseInt($("#eduToTime").attr("date-month"));
	var editStartYear = parseInt($("#editEduFromTime").attr("date-year"));
	var editStartMonth = parseInt($("#editEduFromTime").attr("date-month"));
	var editToYear = parseInt($("#editEduToTime").attr("date-year"));
	var editToMonth = parseInt($("#editEduToTime").attr("date-month"));
	var currentDate = new Date();
	
	var addIsActive = 0  ;
	var editIsActive = 0; 
	var  addEduUpToNow = $("#addEduUpToNow").prop("checked");
	var  editEduUpToNow = $("#editEduUpToNow").prop("checked");
	if(addEduUpToNow){
		addIsActive = 1 ;
	}
	if(editEduUpToNow){
		editIsActive = 1;
	}
	
	if("add" == type){
		if(isNaN(toYear)&&addIsActive==0){
			$("#addEduToYearDiv").addClass("input_invalid");
			$("#addEduToYearHelp").attr("invalid-message", homepage.trueEndTime);
			result = false;
		}
		if(isNaN(startYear)){
			$("#addEduYearDiv").addClass("input_invalid");
			$("#addEduYearHelp").attr("invalid-message", homepage.trueStartTime);
			result = false;
		}else{
			if(!((!isNaN(toYear) && compareEduTime(startYear, startMonth, toYear, toMonth)) || addIsActive == 1)){
				$("#addEduToYearDiv").addClass("input_invalid");
				$("#addEduToYearHelp").attr("invalid-message", homepage.trueEndTime);
				result = false;
			}else{
                $("#addEduToYearDiv").removeClass("input_invalid");
                $("#addEduToYearHelp").attr("invalid-message", "");
                $("#addEduYearDiv").removeClass("input_invalid");
                $("#addEduYearHelp").attr("invalid-message", "");
            }
            if(addIsActive == 1 && !compareEduTime(startYear, startMonth, currentDate.getFullYear(), currentDate.getMonth())){
                $("#addEduYearDiv").addClass("input_invalid");
                $("#addEduYearHelp").attr("invalid-message", homepage.trueStartTime);
                result = false;
            }
		}
	}
	if("edit" == type){
		if(isNaN(editToYear)&&editIsActive==0){
			$("#editEduToYearDiv").addClass("input_invalid");
			$("#editEduToYearHelp").attr("invalid-message", homepage.trueEndTime);
			result = false;
		}
		if(isNaN(editStartYear)){
			$("#editEduYearDiv").addClass("input_invalid");
			$("#editEduYearHelp").attr("invalid-message", homepage.trueStartTime);
			result = false;
			
		}else{
			if(!((!isNaN(editToYear) && compareEduTime(editStartYear, editStartMonth, editToYear, editToMonth) ) || editIsActive == 1)){
				$("#editEduToYearDiv").addClass("input_invalid");
				$("#editEduToYearHelp").attr("invalid-message", homepage.trueEndTime);
				result = false;
			}else{
                $("#editEduToYearDiv").removeClass("input_invalid");
                $("#editEduToYearHelp").attr("invalid-message", "");
                $("#editEduYearDiv").removeClass("input_invalid");
                $("#editEduYearHelp").attr("invalid-message", "");
            }
            if(editIsActive == 1 && !compareEduTime(editStartYear, editStartMonth, currentDate.getFullYear(), currentDate.getMonth())){
                $("#editEduYearDiv").addClass("input_invalid");
                $("#editEduYearHelp").attr("invalid-message", homepage.trueStartTime);
                result = false;
            }
		}
	}
	if(!result){
		return false;
	}
	var addData,editData;
	if("edit" == type){
		editData = {
				'des3ResumeId' : des3ResumeId,
				'serviceType':'nfsc',
				'cvEduInfo.seqEdu': $("#seqEdu").val(),
				'cvEduInfo.insId': $("#editEduInsId").val(),
				'cvEduInfo.insName': $("#editEduInsName").val().trim(),
				'cvEduInfo.department': $("#department").val().trim(),
				'cvEduInfo.degreeName': $("#editEduDegree").val().trim(),
				'cvEduInfo.fromYear': $("#editEduFromTime").attr("date-year"),
				'cvEduInfo.fromMonth':$("#editEduFromTime").attr("date-month") == "null" ? "" : $("#editEduFromTime").attr("date-month") ,
				'cvEduInfo.toYear': $("#editEduToTime").attr("date-year") == "null" ? "" : $("#editEduToTime").attr("date-year") ,
				'cvEduInfo.toMonth':$("#editEduToTime").attr("date-month") == "null" ? "" : $("#editEduToTime").attr("date-month") ,
				'cvEduInfo.description': $("#editEduDescription").val(),
				'cvEduInfo.isActive':editIsActive
		};
	}else{
		addData = {
				'des3ResumeId' : des3ResumeId,
				'serviceType':'nfsc',
				'cvEduInfo.insId': $("#eduInsName").attr("code"),
				'cvEduInfo.insName': $("#eduInsName").val().trim(),
				'cvEduInfo.department': $("#eduDepartment").val().trim(),
				'cvEduInfo.degreeName': $("#eduPosition").val().trim(),
				'cvEduInfo.fromYear': $("#eduFromTime").attr("date-year"),
				'cvEduInfo.fromMonth':$("#eduFromTime").attr("date-month") == "null" ? "" : $("#eduFromTime").attr("date-month") ,
				'cvEduInfo.toYear': $("#eduToTime").attr("date-year") == "null" ? "" : $("#eduToTime").attr("date-year") ,
				'cvEduInfo.toMonth':$("#eduToTime").attr("date-month") == "null" ? "" : $("#eduToTime").attr("date-month") ,
				'cvEduInfo.description': $("#eduDescription").val()  ,
				'cvEduInfo.isActive':addIsActive
		};
	}
	var insId = "";
	var code = $('#editEduInsName').attr("code");
	if (code != null && code != "" && code != undefined) {
		insId = code;
	} else {
		insId = $("#editEduInsId").val();
	}
	
	$.ajax({
		url: "/psnweb/resume/ajaxupdateedu",
		type: "post",
		dataType: "json",
		data: type == "edit" ? editData : addData,
		success: function(data){
			Resume.ajaxTimeOut(data , function(){
				if(data.result == "success"){
					//关掉弹窗
					if("edit" == type){
						hideEditEduBox();
					}else if("add" == type){
						hideAddEduBox();
					}
					PsnResume.psnEducation();
				}else{
					scmpublictoast(homepage.systemBusy,1000);
				}
			});
			
		}, 
		error: function(data){
			scmpublictoast(homepage.systemBusy,1000);
			hideEditEduBox();
			hideAddEduBox();
		}
	});
};


PsnResume.editEduHistory = function(seqEdu){
	var des3ResumeId = $("#des3ResumeId").val();
	$.ajax({
		url: "/psnweb/resume/ajaxeditedu",
		type: "post",
		dateType: "html",
		data: {
			'serviceType':'nfsc',
			"des3ResumeId" : des3ResumeId,
			"seqEdu" : seqEdu
		},
		success: function(data){
			Resume.ajaxTimeOut(data , function(){
				$("#editPsnEduBox").html(data);
				addFormElementsEvents($("#editPsnEduBox")[0]);
				showDialog("editPsnEduBox");
				//绑定校验
				$("#editEduInsName").bind('blur',checkEduNameNull);
				$("#editEduFromTime").bind('blur',checkEduDateNull);
				$("#editEduToTime").bind('blur',checkEduEndDateNull);
			});
			
		},
		error: function(){
			hideDialog("editPsnEduBox");
		}
	});
};


PsnResume.editWorkHistory = function(seqWork){
	var des3ResumeId = $("#des3ResumeId").val();
	$.ajax({
		url: "/psnweb/resume/ajaxeditwork",
		type: "post",
		dateType: "html",
		data: {
			'serviceType':'nfsc',
			"des3ResumeId" : des3ResumeId,
			"seqWork" : seqWork
		},
		success: function(data){
			Resume.ajaxTimeOut(data , function(){
				$("#editPsnWorkBox").html(data);
				addFormElementsEvents($("#editPsnWorkBox")[0]);
				showDialog("editPsnWorkBox");
				//绑定校验
				$("#insName").bind('blur',checkWorkNameNull);
				$("#startTime").bind('blur',checkWorkDateNull);
				$("#endTime").bind('blur',checkWorkEndDateNull);
			});
			
		},
		error: function(){
			hideDialog("editPsnEduBox");
		}
	});
};


PsnResume.saveWorkHistory = function(type ,obj){
	var des3ResumeId = $("#des3ResumeId").val();
	if(obj != undefined){//防止重复点击
		Resume.doHitMore(obj ,3000);
	}
	var result = true;
	if("add" == type && $("#newInsName").val().trim() == ""){
		$("#newInsNameDiv").addClass("input_invalid");
		$("#newInsNameHelper").attr("invalid-message", homepage.inputInsName);
		result = false;
	}
	if("edit" == type && $("#insName").val().trim() == ""){
		$("#insNameDiv").addClass("input_invalid");
		$("#insNameHelper").attr("invalid-message", homepage.inputInsName);
		result = false;
	}
	var startYear = parseInt($("#newStartTime").attr("date-year"));
	var startMonth = parseInt($("#newStartTime").attr("date-month"));
	var toYear = parseInt($("#newEndTime").attr("date-year"));
	var toMonth = parseInt($("#newEndTime").attr("date-month"));
	var editStartYear = parseInt($("#startTime").attr("date-year"));
	var editStartMonth = parseInt($("#startTime").attr("date-month"));
	var editToYear = parseInt($("#endTime").attr("date-year"));
	var editToMonth = parseInt($("#endTime").attr("date-month"));
	var currentDate = new Date();
	
	var addIsActive=0 ; //至今
	var editIsActive=0 ; //至今
	var addWorkupToNow = $("#addWorkUpToNow").prop("checked")
	var editWorkupToNow = $("#editWorkUpToNow").prop("checked")
	if(addWorkupToNow){
		addIsActive = 1 ;
	}
	if(editWorkupToNow){
		editIsActive = 1 ;
	}
	if("add" == type){
		if(isNaN(toYear)&&addIsActive==0){
			$("#addWorkToYearDiv").addClass("input_invalid");
			$("#addWorkToYearHelp").attr("invalid-message", homepage.trueEndTime);
			result = false;
		}
		if(isNaN(startYear)){
			$("#addWorkFromYearDiv").addClass("input_invalid");
			$("#addWorkFromYearHelp").attr("invalid-message", homepage.trueStartTime);
			result = false;
		}else{
			if(!((!isNaN(toYear) && compareEduTime(startYear, startMonth, toYear, toMonth)) || addIsActive == 1)){
				$("#addWorkToYearDiv").addClass("input_invalid");
				$("#addWorkToYearHelp").attr("invalid-message", homepage.trueEndTime);
				result = false;
			}else{
			    $("#addWorkToYearDiv").removeClass("input_invalid");
                $("#addWorkToYearHelp").attr("invalid-message", "");
                $("#addWorkFromYearDiv").removeClass("input_invalid");
                $("#addWorkFromYearHelp").attr("invalid-message", "");
			}
			if(addIsActive == 1 && !compareEduTime(startYear, startMonth, currentDate.getFullYear(), currentDate.getMonth())){
			    $("#addWorkFromYearDiv").addClass("input_invalid");
	            $("#addWorkFromYearHelp").attr("invalid-message", homepage.trueStartTime);
	            result = false;
			}
		}
	}
	if("edit" == type){
		if(isNaN(editToYear)&&editIsActive==0){
			$("#workToYearDiv").addClass("input_invalid");
			$("#workToYearHelp").attr("invalid-message", homepage.trueEndTime);
			result = false;
		}
		if(isNaN(editStartYear)){
			$("#workFromYearDiv").addClass("input_invalid");
			$("#workFromYearHelp").attr("invalid-message", homepage.trueStartTime);
			result = false;
		}else{
			if(!((!isNaN(editToYear) && compareEduTime(editStartYear, editStartMonth, editToYear, editToMonth)) || editIsActive == 1)){
				$("#workToYearDiv").addClass("input_invalid");
				$("#workToYearHelp").attr("invalid-message", homepage.trueEndTime);
				result = false;
			}else{
                $("#workToYearDiv").removeClass("input_invalid");
                $("#workToYearHelp").attr("invalid-message", "");
                $("#workFromYearDiv").removeClass("input_invalid");
                $("#workFromYearHelp").attr("invalid-message", "");
            }
			if(editIsActive == 1 && !compareEduTime(editStartYear, editStartMonth, currentDate.getFullYear(), currentDate.getMonth())){
                $("#workFromYearDiv").addClass("input_invalid");
                $("#workFromYearHelp").attr("invalid-message", homepage.trueStartTime);
                result = false;
            }
		}
	}
	if(!result){
		return false;
	}
	var addData,editData;
	if(type == "edit"){
		editData = {
				'serviceType':'nfsc',
				'des3ResumeId': des3ResumeId,
				'cvWorkInfo.seqWork': $("#seqWork").val(),
				'cvWorkInfo.insId': $("#newInsName").attr("code"),
				'cvWorkInfo.insName': $("#insName").val().trim(),
				'cvWorkInfo.department': $("#department").val().trim(),
				'cvWorkInfo.degreeName': $("#position").val().trim(),
				'cvWorkInfo.fromYear': $("#startTime").attr("date-year"),
				'cvWorkInfo.fromMonth':$("#startTime").attr("date-month") == "null" ? "" : $("#startTime").attr("date-month") ,
				'cvWorkInfo.toYear': $("#endTime").attr("date-year") == "null" ? "" : $("#endTime").attr("date-year") ,
				'cvWorkInfo.toMonth':$("#endTime").attr("date-month") == "null" ? "" : $("#endTime").attr("date-month") ,
				'cvWorkInfo.description': $("#description").val(),
				'cvWorkInfo.isActive': editIsActive
		};
	}else{
		addData = {
				'serviceType':'nfsc',
				'des3ResumeId': des3ResumeId,
				'cvWorkInfo.insId': $("#newInsName").attr("code"),
				'cvWorkInfo.insName': $("#newInsName").val().trim(),
				'cvWorkInfo.department': $("#newDepartment").val().trim(),
				'cvWorkInfo.degreeName': $("#newPosition").val().trim(),
				'cvWorkInfo.fromYear': $("#newStartTime").attr("date-year"),
				'cvWorkInfo.fromMonth':$("#newStartTime").attr("date-month") == "null" ? "" : $("#newStartTime").attr("date-month") ,
				'cvWorkInfo.toYear': $("#newEndTime").attr("date-year") == "null" ? "" : $("#newEndTime").attr("date-year"),
				'cvWorkInfo.toMonth':$("#newEndTime").attr("date-month") == "null" ? "" : $("#newEndTime").attr("date-month"),
				'cvWorkInfo.description': $("#newDescription").val(),
				'cvWorkInfo.isActive': addIsActive
		};
	}
	var insId ="";
	var code = $('#insName').attr("code");
	if (code == null || code == undefined) {
		insId="";
	}
	
	$.ajax({
		url: "/psnweb/resume/ajaxupdateWork",
		type: "post",
		dataType: "json",
		data: type == "edit" ? editData : addData,
		success: function(data){
			Resume.ajaxTimeOut(data , function(){
				if(data.result == "success"){
					if("edit" == type){
						hideEditWorkBox();
					}else if("add" == type){
						hideAddWorkBox();
					}
					PsnResume.psnWorkinfo();
				}else{
					scmpublictoast(homepage.systemBusy,1000);
				}
			});
			
		}, 
		error: function(){
			scmpublictoast(homepage.systemBusy,1000);
			hideEditWorkBox();
			hideAddWorkBox();
		}
	});
};
//保存简历名称
PsnResume.saveCVName = function(){
	var resumeName = $.trim($("#editResumeName").val());
	var des3ResumeId = $("#des3ResumeId").val();
	if(resumeName == ""){
		return;
	}
	$.ajax({
		url: "/psnweb/resume/ajaxeditcvname",
		type: "post",
		dataType: "json",
		data: {'serviceType':'nfsc',
			'des3ResumeId': des3ResumeId,
			'resumeName':resumeName},
		success: function(data){
			Resume.ajaxTimeOut(data , function(){
				if(data.result == "success"){
					$("#resumeNameShow").html(data.resumeName);
					hideDialog('editCvNameBox');
				}else{
					scmpublictoast(homepage.systemBusy,1000);
				}
			});
			
		}, 
		error: function(){
			scmpublictoast(homepage.systemBusy,1000);
		}
	});
};
//打开编辑简历弹出框
PsnResume.editCVName = function(){
	showDialog("editCvNameBox");
	$("#editResumeName").bind('blur',checkCVNameNull);
};
function checkCVNameNull(){
	//add
	if($("#editResumeName").val().trim() == ""){
		$("#eduCVNameDiv").addClass("input_invalid");
		$("#eduCVNameHelper").attr("invalid-message", homepage.noteCVname);
	}else{
		$("#eduCVNameDiv").removeClass("input_invalid");
		$("#eduCVNameHelper").attr("invalid-message","");
	}
}
//导出简历
PsnResume.exportCV = function(des3CVId, type, obj) {//type 为简历类型
    BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function() {
		BaseUtils.doHitMore($(obj),3000);
		window.location.href = "/psnweb/resume/ajaxwordexport?des3ResumeId="+encodeURIComponent(des3CVId)+"&serviceType="+type;
	}, 1);
};


	//保存修改个人信息
PsnResume.savePsnInfo = function(obj){
	var des3ResumeId = $("#des3ResumeId").val();
	if(obj != undefined){//防止重复点击
		Resume.doHitMore(obj ,3000);
	}
	var name=$.trim($("#psnName").val());
	if(name == ""){
		return;
	}
    var insInfo=$.trim($("#insName").val());
    var department=$.trim($("#department").val());
    var degree=$.trim($("#degree").val());
	$.ajax({
		url: "/psnweb/resume/ajaxeditpsninfo",
		type: "post",
		dataType: "json",
		data: {'serviceType':'nfsc',
			'des3ResumeId': des3ResumeId,
			'cvPsnInfo.name':name,
			'cvPsnInfo.insInfo':insInfo,
			'cvPsnInfo.department':department,
			'cvPsnInfo.degree':degree},
		success: function(data){
			Resume.ajaxTimeOut(data , function(){
				if(data.result == "success"){
					$("#baseinfo").html(data.psnBaseInfo);
					hideDialog('editPsnInfoBox');
				}else{
					scmpublictoast(homepage.systemBusy,1000);
				}
			});
			
		}, 
		error: function(){
			scmpublictoast(homepage.systemBusy,1000);
		}
	});
};

PsnResume.getpsninfo = function(){
	var des3ResumeId = $("#des3ResumeId").val();
	$.ajax({
		url: "/psnweb/resume/ajaxgetpsninfo",
		type: "post",
		dateType: "html",
		data: {
			'serviceType':'nfsc',
			"des3ResumeId" : des3ResumeId,
		},
		success: function(data){
			Resume.ajaxTimeOut(data , function(){
				$("#editPsnInfoBox").html(data);
				addFormElementsEvents($("#editPsnInfoBox")[0]);
				showDialog("editPsnInfoBox");
				$("#psnName").bind('blur',checkPsnNameNull);
			});
			
		},
		error: function(){
			hideDialog("editPsnEduBox");
		}
	});
};
function checkPsnNameNull(){
	if($("#psnName").val().trim() == ""){
		$("#editPsnNameDiv").addClass("input_invalid");
		$("#editPsnNameHelper").attr("invalid-message", "姓名不能为空");
	}else{
		$("#editPsnNameDiv").removeClass("input_invalid");
		$("#editPsnNameHelper").attr("invalid-message","");
	}
}

//删除简历
PsnResume.deletePsnResume = function(des3ResumeId){
	$.ajax({
		url: "/psnweb/resume/ajaxdelete",
		type: "post",
		dateType: "json",
		data: {
			'serviceType':'nfsc',
			"des3ResumeId" : des3ResumeId,
		},
		success: function(data){
			Resume.ajaxTimeOut(data , function(){
				var selecter = "div[des3CvId='"+des3ResumeId+"']"
				$(selecter).remove();
				var seqNo = 1;
				$(".cvSeq").each(function(){
					var _this = $(this);
					_this.html(seqNo);
					seqNo++;
				});
			});
		},
		error: function(data){
			scmpublictoast(homepage.systemBusy,2000);
		}
	});
};

//返回
PsnResume.toBack = function(obj) {
    BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function() {
		BaseUtils.doHitMore($(obj),2000);
		window.location.href = "/psnweb/homepage/show";
	}, 1);
};