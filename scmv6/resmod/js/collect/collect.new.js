var collect=collect?collect:{};

$(document).ready(function(){
	$("a.thickbox,input.thickbox").thickbox({
		ctxpath:ctxpath,
		respath:respath	
	});
	
	$(".List tr:even").addClass("alt");
/*	$(".List tr").mouseover(function () {
		$(this).addClass("over");
	}).mouseout(function () {
		$(this).removeClass("over");
	}).click(function (){
	   if ($(this).hasClass("selecteds")){
		    $(this).removeClass("selecteds");
		    $(this).find("input[type='checkbox']").removeAttr("checked");
		    collect.checkedDBall();
	   }else{
		    $(this).addClass("selecteds");
		    $(this).find("input[type='checkbox']").attr("checked",true);
		    collect.checkedDBall();
	   }
	});*/
});
	
collect.select_All_ShDB_Code = function(status,type){
	$("#"+type+" .data_tr").find("input[type='checkbox']").attr('checked',status);
};
	
	
collect.checkedDBall=function(){
	var flag = true;
	$(".data_tr").find("input[type='checkbox']").each(function(){ 
		  if(!$(this).attr('checked')){
			  flag=false;
			  return;
		  }
	});
	$(".dbCB_All").attr("checked",flag);
};
	
collect.removeMe = function(){
		$("#title").val('');
		$("#insName").val('');
};

collect.removeFriend=function (){
		$("#friend_psn_id").val('');
		$("#friend_select").val('');
		$("#title_friend").val('');
		$("#cname_friend").val('');	
		$("#lname_friend").val('');	
		$("#fname_friend").val('');
		$("#oname_friend").val('');	
		$("#insName_friend").val('');
	};

collect.removeElse = function (){
		$("#title_else").val('');
		$("#oname_else").val('');
		$("#insName_else").val('');
};

collect.setTab = function(name,cursel){
	for(var i=1;i<=3;i++){
		var menu=document.getElementById(name+i);
		menu.className=i==cursel?"hover":"";
	}
};

collect.setRolTab = function(name,cursel){
	for(var i=2;i<=3;i++){
		var menu=document.getElementById(name+i);
		//增加非空判断，与页面标签的控制保持一致_MaoJianGuo_2012-12-10_ROL-60.
		if(menu!=null){
			menu.className=i==cursel?"hover":"";
		}
	}
};

collect.rolRadioChoose = function (typeId){
	if(typeId==1){
		isInsPsn=true;
		collect.setRolTab('two',2);
		$("#tr_searchby_ins").css("display","");
	}
	if(typeId==2){
		isInsPsn=false;
		collect.setRolTab('two',3);
		$("#tr_searchby_ins").css("display","none");
	}
	$("#cname_friend").val("");
	$("#lname_friend").val("");
	$("#fname_friend").val("");
	$("#friendPsnId").val('');
	$("#friendName").val('');
	$("#title_friend").val("");
	$("#insName_friend").val(locale_insName);
	
	$.autoword["div_plugin_shoose_friends"].clear();
	collect.removeSelectAll();
};

collect.selectInsByDate = function(insInputId,yearInputId,flag){
	if(publicationArticleType=='1'){	
		var fyear =  $.trim($("#"+insInputId).attr("fyear"));
		var tyear =  $.trim($("#"+insInputId).attr("tyear"));	   
		var now= new Date();
		var currYear = now.getFullYear();
		if(fyear.length>0){	
			tyear = tyear==""?currYear:tyear;
			if(flag){
				if(tyear>currYear){
					$("#"+yearInputId).val((currYear-fyear)<=3?(fyear+"-"+tyear):(currYear-3)+"-"+tyear);
				}else{
					$("#"+yearInputId).val((tyear-fyear)<=3?(fyear+"-"+tyear):(tyear-3)+"-"+tyear);
				}
			}else{
			  $("#"+yearInputId).val(fyear+"-"+tyear);
			}	
		}else{
			$("#"+yearInputId).val(((now.getFullYear())-3)+"-"+now.getFullYear());
		}
		$("#defyear").val($("#"+yearInputId).val());
	}
};
	
collect.radioIsChoose=function(value){
		if(value=="me"){
			context_search_type="me";
			$("#search_me").attr("checked",true);
			collect.removeMe();
			$("#table_else").css("display", "none");
			$("#table_me").css("display", "");
			$("#meuiList li").each(function(idx){
				if(idx==0){
					$("#insName").val($(this).attr("alt")); 
					 $("#insName").attr("fyear",$(this).attr("fyear")); 
					 $("#insName").attr("tyear",$(this).attr("tyear"));
					 return;
				}
			});
			collect.selectInsByDate("insName","publicyear",true);
		}
		if(value=="else"){
			context_search_type="else";
			$("#search_else").attr("checked",true);
			collect.removeElse();
			$("#table_me").css("display", "none");
			$("#table_else").css("display", "");
			$("#else_uiList li").each(function(idx){
				if(idx==0){
					$("#insName_else").val($(this).attr("alt")); 
					$("#insName_else").attr("fyear",$(this).attr("fyear")); 
					$("#insName_else").attr("tyear",$(this).attr("tyear"));
					 return;
				}
			});
			collect.selectInsByDate("insName_else","publicyear_else",true);
		}
		collect.removeSelectAll();
};
	
collect.removeDataChecked=function (){
   $(".data_tr").each(function(){ 
	   $(this).find("input[type='checkbox']").attr('checked',false);
	   $(this).removeClass("selected");
   });
};

collect.removeSelectAll=function(){
	$(".dbCB_All").attr("checked",false);
	collect.removeDataChecked();
};	
	
collect.ajaxSetFriend = function(psnId){
	$.ajax({
		url:ctxpath+'/publication/ajaxFriendsByPsnId',
		type:'post',
		data:{'psnid':psnId},
		dataType:'json',  
		timeout: 10000,
		success: function(data){
			if(!data){
				return;
			}
			$("#cname_friend").val(data.psnName);
			$("#lname_friend").val(data.psnLastName);
			$("#fname_friend").val(data.psnFirstName);
			$("#oname_friend").val(data.psnOtherName);
			$("#friendPsnId").val(psnId);
			$("#friendName").val(data.psnName);
		}
	 }); 
};

collect.ajaxSetFriendWordEdus = function(psnId){
	$("#uiList li").each(function(){ 
		$(this).remove(); 
    });
	$.ajax({
		url:ctxpath+'/publication/ajaxFriendWorkEdus',
		type:'post',
		data:{'psnid':psnId},
		dataType:'json',  
		timeout: 10000,
		success: function(result){
			if(!result || !result.data){
				return;
			}
			$.each(result.data,function(n,obj) {    
				if(obj.primary==1){
					$("#insName_friend").val(obj.name);
				}
	         $("#uiList").append("<li style='padding:0px;margin:0px;' alt='"+obj.name+"'>&nbsp;&nbsp;"+obj.name+"</li>");
			}); 
			if(result.data.length<1){
				$("#insName_friend").val("");
				$("#showfrdins_div").css("display","none");
			}
		}
	 }); 
};
	
collect.ajaxSetInsPsn = function (psnId){
		$.ajax({
			url:ctxpath+'/publication/ajaxGetInsPersonByPnsId',
			type:'post',
			data:{'psn_id':psnId},
			dataType:'json',  
			timeout: 10000,
			success: function(data){
				if(!data){
					return;
				}
				$("#ins_prox_person_name").val(data.psnName);
				$("#cname_friend").attr("value",data.psnName);
				$("#lname_friend").attr("value",data.psnLastName);
				$("#fname_friend").attr("value",data.psnFirstName);
				$("#friendPsnId").attr("value",psnId);
				$("#friendName").attr("value",data.psnName);
				$.autoword["div_plugin_shoose_friends"].clear();
				$.autoword["div_plugin_shoose_friends"].put(psnId,data.psnName);
			}
		 }); 
	};
	
	/**
	 * 根据登录人Id，初始化该人上次最后选中的教育经历
	 * @param psnId
	 * @return
	 */
collect.ajaxGetLastSelectedAffiliation=function(){ 
	$.ajax({
		url:ctxpath+'/profile/getPersonDefaultAffiliation',
		type:'post',
		data:{},
		dataType:'json',  
		timeout: 10000,
		success: function(data){ 
			if(data.result !=""){ 
				$("#insName").attr("value",data.result);
			}
		}
	 }); 
};
	
	/**
	 * 检索万方医学数据库
	 * @param criteria
	 * @return
	 */
collect.searchWanFangYX=function (pageNO){
		//setTimeout(function(){scroll(0,0);},100);
		console.log("wanfangshujuku");
		wanFangYX_Status="1";
		setWorkingStatus();
		var logAffiliaton="\r\ndb_code:WanFangYX,start find webService";
		writeLog("4","",logCriteriaStr+logAffiliaton);
		$.ajax({
			url:ctxpath+'/publication/seachWfyx',
			type:'post',
			data:{'title':criteria.topic,'name':criteria.cname,'firstName':criteria.firstName,'lastName':criteria.lastName,'otherName':criteria.otherName,'insName':affiliaton_insName,'year':criteria.pubYear,'pageNo':pageNO},  
			timeout: 300000,
			success: function(data){
				isWanFangNext = false;
				wanFangYX_Status="0";
				setWorkingStatus();
				var resInt = 0;
				var varCmdId = 0;	
				if(!isNaN(data)){
					varCmdId=1;
					resInt = data;
					data="";
				}else if(data.indexOf("wfyxResultSart[")>0){
					var a = data.indexOf("wfyxResultSart[")+15;
					var b = data.indexOf("]wfyxResultEnd");
					resInt = data.substring(a,b);
				}
				OnUpdate_ie(-1,varCmdId,resInt,data);
			},
			error:function(){
				writeLog("4","",logCriteriaStr+logAffiliaton+",error");
			}
		 });
	};

	/**
	 * 导入选定的万方数据
	 * @param wfyxXmlIds
	 */
	collect.getWanFangYxXml = function (wfyxXmlIds){
		wanFangYX_Status="5";
		//setWorkingStatus();
		writeLog("5","","db_code:WanFangYX,wfyxXmlIds:"+wfyxXmlIds);
		$.ajax({
			url:ctxpath+'/publication/getWfyxXml',
			type:'post',
			data:{'wfyxXmlIds':wfyxXmlIds},
			timeout: 300000,
			success: function(data){
				wanFangYX_Status="0";
				//setWorkingStatus();
				if(isIEBrowser){
					importData_ie(data,"WanFangYX");
				}else{
					importData_ff(data,"WanFangYX");
				}
				writeLog("5","","db_code:WanFangYX,imoprt xml success xmlLength:"+data.length);
			},
			error: function(){
				writeLog("5","","db_code:WanFangYX,imoprt xml error,wfyxXmlIds:"+wfyxXmlIds);
			}
		 });
	};
	
	