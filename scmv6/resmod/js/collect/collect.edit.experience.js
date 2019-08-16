var isShow=false;
var collectExp=collectExp?collectExp:{};

collectExp.hideEdit=function(id){
	$(".edit_box").each(function(){
		$(this).css("display", "none");
	});
	isShow=false;
	if(document.body.clientHeight<=450)
	parent.$.Thickbox.resetHeight(document.body.clientHeight+20);
};	
collectExp.showEdit=function(id){
	$(".edit_box").each(function(){
		$(this).css("display", "none");
	});
	$("#"+id).css("display", "");
	if(!isShow){ 
		if(document.body.clientHeight<=450)
		parent.$.Thickbox.resetHeight(document.body.clientHeight);
	}
	isShow=true;
};
collectExp.closeDiv=function() {
		parent.$.Thickbox.closeWin();
		collectExp.resParent();
	};
collectExp.hideDiv=function(id) {
	$("#" + id).hide();
};	
collectExp.resParent=function() {
	var groupId = parent.$("#groupId").val();
	var folderId = parent.$("#folderId").val();
	var publicationArticleType = parent.$("#publicationArticleType").val();
	if (groupId != '') {
		if (publicationArticleType == 1)
			parent.window.location.href = ctxpath+"/publication/group/collect?isInitRadio=me&groupId="
					+ groupId + "&folderId=" + folderId;
		if (publicationArticleType == 4)
			parent.window.location.href = ctxpath+"/project/group/collect?isInitRadio=me&groupId="
					+ groupId + "&folderId=" + folderId;
	} else {
		if (publicationArticleType == 1)
			parent.window.location.href = "/pubweb/publication/collect";
		if (publicationArticleType == 4)
			parent.window.location.href = ctxpath+"/project/collect";
	}
};
collectExp.saveWork=function(id){
	var da = new Date();
	var thisYear = da.getFullYear();
	var thisMonth = da.getMonth()+1;
	var workId =id;
	var insId = $.trim($("#insName_work_insId"+id).val());
	var insName = $.trim($("#insName_work"+id).val());
	var profTitle = $.trim($("#profTitle_work"+id).val());
	var department = $.trim($("#department_work"+id).val());
	var position = $.trim($("#position_work"+id).val());
	var fromYear = $.trim($("#fromYear_work"+id).val());
	var fromMonth = $.trim($("#fromMonth_work"+id).val());
	var toYear = $.trim($("#toYear_work"+id).val());
	var toMonth = $.trim($("#toMonth_work"+id).val());
	var authority = $.trim($("#work_authority_"+id).val());
	var desc = $.trim($("#work_description"+id).val());
	var isActive ="0";		
	var isPrimary ="0";	
	if($("#workIsPrimary"+id).attr('checked')){
		isPrimary = "1";
	}	 
	if(insName.length==0){
		$.scmtips.show('warn',referencesearch_experience_work_input);
		return;
	}
	if($("#isActive_work"+id).attr('checked')){
		isActive = 1;
		if(fromYear.length==0 || fromMonth.length==0){
			$.scmtips.show('warn',referencesearch_experience_frist_date);
			 return;
		}else if(fromYear>thisYear || (fromYear == thisYear && fromMonth > thisMonth)){
			$.scmtips.show('warn',referencesearch_experience_frist_not_last_date);
				return;
		}
	}else{
		if(fromYear.length==0 || fromMonth.length==0 || toYear.length==0 || toMonth.length==0){
			$.scmtips.show('warn',referencesearch_experience_all_date);
				return;
		} 
		if(parseInt(fromYear) > parseInt(toYear) || ((parseInt(fromYear) == parseInt(toYear)) && (parseInt(fromMonth) > parseInt(toMonth)))){
			$.scmtips.show('warn',referencesearch_experience_frist_not_last_date);
				return;
		}
	}
	$.ajax({
	url:ctxpath+'/publication/saveWork',
	type:'post',
	data:{'workId':workId,'insId':insId,'insName':insName,'profTitle':profTitle,'department':department,'position':position,'fromYear':fromYear,'fromMonth':fromMonth,'toYear':toYear,'toMonth':toMonth,'isActive':isActive,'isPrimary':isPrimary,"authority":authority,'desc':desc},
	dataType:'json',  
	timeout: 10000,
	success: function(result){
		   if(result){
			   $.scmtips.show('success',referencesearch_base_ajax_yes);
		      setTimeout(function(){window.location.reload();},1000);
		   }
		   else{
			   $.scmtips.show('error',referencesearch_base_ajax_error); 
			  setTimeout(function(){window.location.reload();},1000); 
		   } 
		},
	error:function(){
		$.scmtips.show('error',referencesearch_base_ajax_error); 
			setTimeout(function(){window.location.reload();},1000);
		}
    });
};
collectExp.delWork=function(id){
	var workId = id;
	if(workId!=''){
		jConfirm(referencesearch_msg_del_work, referencesearch_msg_alert, function(r) {
			if(r){
				$.ajax( {
					url : ctxpath+'/profile/ajaxDelWorkHistory',
					type : 'post',
					dataType:'json',
					data : {'workId':workId},
					success : function(data) {
						if(data.result){
							$.scmtips.show('success',referencesearch_base_ajax_yes);
						 setTimeout(function(){window.location.reload();},1000); 
						}
					},
					error:function(){
						$.scmtips.show('error',referencesearch_base_ajax_error); 
						 setTimeout(function(){window.location.reload();},1000); 
					}
				});
			}
		});
				
	}
};

collectExp.delEdu=function(id){
	var eduId = id;
	if(eduId!=''){
		jConfirm(referencesearch_msg_del_edu, referencesearch_msg_alert, function(r) {
			if(r){
				$.ajax( {
					url : ctxpath+'/profile/ajaxDelEduHistory',
					type : 'post',
					dataType:'json',
					data : {'eduId':eduId},
					success : function(data) {
						if(data.result){
							$.scmtips.show('success',referencesearch_base_ajax_yes);
						 setTimeout(function(){window.location.reload();},1000); 
						}
					},
					error:function(){
						$.scmtips.show('error',referencesearch_base_ajax_error); 
						 setTimeout(function(){window.location.reload();},1000); 
					}
				});
			}
		});
	}
};

collectExp.saveEdu = function(id){
	var eduId = id;
	var insId = $.trim($("#insName_edu_insId"+id).val());
	var insName = $.trim($("#insName_edu"+id).val());
	var study = $.trim($("#study_edu"+id).val());
	var degree = $.trim($("#degree_edu"+id).val());
	var fromYear = $.trim($("#fromYear_edu"+id).val());
	var fromMonth = $.trim($("#fromMonth_edu"+id).val());
	var toYear = $.trim($("#toYear_edu"+id).val());
	var toMonth = $.trim($("#toMonth_edu"+id).val());
	var authority = $.trim($("#edu_authority_"+id).val());
	var desc = $.trim($("#edu_description"+id).val());
	if(insName.length==0){
		$.scmtips.show('warn',referencesearch_experience_edu_ins_input);
		return;
	}
	var isPrimary ="0";	
	if($("#eduIsPrimary"+id).attr('checked')){
		isPrimary = "1";
	}	
	if(fromYear.length==0 || fromMonth.length==0 || toYear.length==0 || toMonth.length==0){
		$.scmtips.show('warn',referencesearch_experience_all_date);
			return;
	} 
	if(fromYear > toYear || (fromYear == toYear && fromMonth > toMonth)){
		$.scmtips.show('warn',referencesearch_experience_frist_not_last_date);
			return;
	}
	$.ajax({
	url:ctxpath+'/publication/saveEdu',
	type:'post',
	data:{'eduId':eduId,'insId':insId,'insName':insName,'study':study,'degree':degree,'fromYear':fromYear,'fromMonth':fromMonth,'toYear':toYear,'toMonth':toMonth,'isPrimary':isPrimary,"authority":authority,'desc':desc},
	dataType:'json',  
	timeout: 10000,
	success: function(result){
		   if(result){
			   $.scmtips.show('success',referencesearch_base_ajax_yes);
			   setTimeout(function(){window.location.reload();},1000);
		   }else{
			   $.scmtips.show('error',referencesearch_base_ajax_error);
			   setTimeout(function(){window.location.reload();},1000);
			}
	},
    error: function(){
    	$.scmtips.show('error',referencesearch_base_ajax_error);
		setTimeout(function(){window.location.reload();},1000);
	  }
    });
};
	



