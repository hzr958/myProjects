var outside = outside ? outside : {};
outside.homepage = outside.homepage ? outside.homepage : {};

/**
 * 根据权限选项，加载主页各个模块
 */
outside.homepage.loadHomepageModule = function(){
	var anyMod = $("#cnfAnyMode").val();
	var allPrivate = true;
	$(".psn_outside_conf").each(function(){
		var _this = $(this);
		var _val = _this.val();
		if((anyMod & _val) == _val){
		  allPrivate = false;
			var moduleName = _this.attr("name");
			switch (moduleName) {
			case "briefConf":
				outside.homepage.ajaxFindPsnBriefDesc();
				break;
			case "keywordsConf":
				outside.homepage.showPsnScienceArea();
				outside.homepage.ajaxShowPsnKeyWords(false);
				break;
			case "workHistoryConf":
				outside.homepage.showPsnWorkHistory();
				break;
			case "eduHistoryConf":
				outside.homepage.showPsnEduHistory();
				break;
			case "representPubConf":
				outside.homepage.showPsnRepresentPub();
				break;
			case "representPrjConf":
				outside.homepage.showPsnRepresentPrj();
				break;
			case "contactInfoConf":
				outside.homepage.showFindPsnContactInfo();
				break;
			}
		}
	});
  if(allPrivate){
    $(".dev_private_msg").show();
  }
	if(anyMod != 0){
		outside.homepage.cooperator();
	}
}

function toHomePage(){
	if($("#isMySelf").val() == "true"){
		window.location.href = "/psnweb/homepage/show";
	}else{
		window.location.href = "/psnweb/outside/homepage?des3PsnId=" + $("#outsideDes3PsnId").val();
	}
}

//显示人员联系信息
outside.homepage.showFindPsnContactInfo= function(){
	$.ajax({
		url: "/psnweb/outside/ajaxcontactshow",
		type: "post",
		data: {
			"des3PsnId": $("#outsideDes3PsnId").val()
		},
		dataType: "html",
		success: function(data){
				$("#contactInfoModule").html(data);
				$("#contactInfoModule").show();			
		},
		error: function(){
			
		}
	});
	$("#contactInfoModule").show();
}

//显示简介信息
outside.homepage.ajaxFindPsnBriefDesc = function(){
	$.ajax({
		url: "/psnweb/outside/ajaxbrief",
		type: "post",
		data: {
			"des3PsnId": $("#outsideDes3PsnId").val()
		},
		dataType: "html",
		success: function(data){
//			Resume.ajaxTimeOut(data ,function(){
				$("#briefDescModule").html(data);
				$("#briefDescModule").show();
//				hideOperationBtn();
//			});
			
		},
		error: function(){
			
		}
	});
}

//科技领域------------------
outside.homepage.showPsnScienceArea = function(){
	$.ajax({
		url: "/psnweb/outside/ajaxsciencearea",
		type: "post",
		dataType: "html",
		data: {
			"des3PsnId": $("#outsideDes3PsnId").val()
		},
		success: function(data){
//			Resume.ajaxTimeOut(data , function(){
				$("#scienceAreaDiv").remove();
				$(data).insertAfter($("#scienceAreaModuleDiv"));
//				hideOperationBtn();
//			});
			
		},
		error: function(){}
	});
}

//人员关键词模块显示
outside.homepage.ajaxShowPsnKeyWords = function(editKeywords){
	$.ajax({
		url: "/psnweb/outside/ajaxkeywords",
		type: "post",
		dataType: "html",
		data: {
			"des3PsnId": $("#outsideDes3PsnId").val(),
			"editKeywords":editKeywords
		},
		success: function(data){
			
//			Resume.ajaxTimeOut(data, function(){
				if(editKeywords==false){
					$(data).insertAfter($("#keywordsModuleDiv"));
					$("#keywordsModule").show();
//					hideOperationBtn();	
				}else{
					$("#psnKeyWordsBox").remove();
					$(data).insertAfter($("#keywordsModule"));
					showDialog("psnKeyWordsBox");
				}
//			})
			
			
		},
		error: function(){
			
		}
	});
}

//工作经历--------------------
outside.homepage.showPsnWorkHistory = function(){
	$.ajax({
		url: "/psnweb/outside/ajaxwork",
		type: "post",
		dataType: "html",
		data: {
			"des3PsnId": $("#outsideDes3PsnId").val()
		},
		success: function(data){
//			Resume.ajaxTimeOut(data ,function(){
				$("#workHistoryItems").remove();
				$(data).insertAfter($("#workHistoryModuleDiv"));
//				hideOperationBtn();
//			});
			
		},
		error: function(){}
	});
}

//教育经历--------------------
outside.homepage.showPsnEduHistory = function(){
	$.ajax({
		url: "/psnweb/outside/ajaxedu",
		type: "post",
		dataType: "html",
		data: {
			"des3PsnId": $("#outsideDes3PsnId").val()
		},
		success: function(data){
//			Resume.ajaxTimeOut(data , function(){
				$("#eduHistoryItems").remove();
				$(data).insertAfter($("#eduHistoryModuleDiv"));
//				hideOperationBtn();
//			});
			
		},
		error: function(){}
	});
}

//代表性成果-----------------
outside.homepage.showPsnRepresentPub = function(){
	$.ajax({
		url: "/pub/outside/ajaxrepresentpub",
		type: "post",
		dataType: "html",
		data: {
			"des3PsnId": $("#outsideDes3PsnId").val()
		},
		success: function(data){
//			Resume.ajaxTimeOut(data ,function(){
				$(data).insertAfter($("#representPubModuleDiv"));
//				hideOperationBtn();
//			});
			
		},
		error: function(){
			
		}
	});
}

//代表性项目-------------------
outside.homepage.showPsnRepresentPrj = function(){
	$.ajax({
		url: "/psnweb/outside/ajaxrepresentprj",
		type: "post",
		dataType: "html",
		data: {
			"des3PsnId": $("#outsideDes3PsnId").val()
		},
		success: function(data){
//			Resume.ajaxTimeOut(data ,function(){
				$(data).insertAfter($("#representPrjModuleDiv"));
//				hideOperationBtn();
//			});
			
		},
		error: function(){
			
		}
	});
}

//查看项目详情
function viewPsnPrjDetails(des3Id){
	var params="des3PrjId="+encodeURIComponent(des3Id);
	var url = "/prjweb/project/detailsshow?"+params;
	//newwindow = window.open("about:blank");
	//newwindow.location.href = url;
	window.open(url,"_blank");
	//newwindow.focus();
}

//查看成果详情
function viewPubDetails(des3Id, Id){
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
				/* newwiondow.focus();*/
			}
			if(data.result==1 || data.ajaxSessionTimeOut=='yes'){
				newwindow.location.href="/pubweb/outside/details?"+params+"&currentDomain="+"/pubweb"+"&pubFlag=1";
				/*newwiondow.focus();*/
			}else if(data.result==0){
			 	newwindow.location.href="/pubweb/publication/wait?"+params;
			}
		},
		error:function(){
			
		}
	});
}

//显示人员科技领域认同
outside.homepage.showScienceAreaIdentifyPsn = function(scienceAreaId){
	jConfirm(homepage.timeOut, homepage.tips, function(r) {
		if (r) {
			document.location.href="/psnweb/homepage/show?des3PsnId="+$("#outsideDes3PsnId").val();
			return 0;
		}
	});
}


//成果合作者
outside.homepage.cooperator = function() {
	$("#psn_cooper_loading").show();
	$.ajax({
		url : '/psnweb/outside/ajaxpubcooperator',
		type : 'post',
		dataType : 'html',
		data : {"des3CurrentId":$("#outsideDes3PsnId").val(), "showList": 1},
		success : function(data) {
			$('#psn_cooperator_list').html(data);
			if($("#psn_cooperator_list").find(".main-list__item").length==0){
				$("#psn_cooperator_list").html("<div class='response_no-result'>"+homepage.noRecord+"</div>");
			}
		}
	});
};

//合作者-查看全部
outside.homepage.psnCooperatorAll = function() {
	jConfirm(homepage.timeOut, homepage.tips, function(r) {
		if (r) {
			document.location.href="/psnweb/homepage/show?des3PsnId="+$("#outsideDes3PsnId").val();
			return 0;
		}
	});
};
