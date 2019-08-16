
$(function(){
	 $("#enterPubForm").validate({  //初始化form表单验证
		 errorPlacement : function(error, element) {//修改错误的显示位置.error是错误信息的节点。element是要校验的input节点
			 $(element).closest(".error_import-tip_border").addClass("error_import-tip_border-warn");
		 },
		 onfocusout: function(element) {
			 if($(element).valid()){
				 $(element).closest(".error_import-tip_border").removeClass("error_import-tip_border-warn");
			 }
	     },  
	     onfocusin: function(element) {
	    	 $(element).closest(".error_import-tip_border").removeClass("error_import-tip_border-warn");
	     },
	     onkeyup: false,//键盘事件不校验
	     focusCleanup: true,//聚焦时候清除错误信息
		 ignore: ".ignore",//设置忽略某些元素不验证这里表示都要验证包括隐藏域，验证时忽略“ignore”类的所有元素。
		 rules: {//表单name验证
			 showTotalPages: {
				 check_startAndEndPage: "showTotalPages",
			 },
		 }, 
		 messages: validate_errorMsg//定义错误消息提示
	 });
	 /*利用class名验证*/
	$.validator.addClassRules({
		json_title: {
			required:true,
			check_titleSpecialChar: true,
			maxlength: 2000
		},
		json_citations: {
			check_digits: true//整数
		},
		/*json_srcFulltextUrl:{
			url: true
		},	*/
		json_fulltext_fileName:{
			required:true,
		},
		json_member_email: {
			check_email:true,                  
		},
		json_member_name:{
			required:true,
		},
		json_journal_name: {
			required:true,
		},
		json_journal_volumeNo: {
			//check_digits:true,
			required:true,
		},
		json_journal_issue: {
			//check_digits:true,
			required:true,
		},
		json_patent_type: {
			required:true,
		},
		json_patent_area: {
			required:true,
		},	
		json_patent_status: {
			required:true,
		},
		json_patent_applicationNo: {
			required:true,
		},
		json_patent_publicationOpenNo: {
			required:true,			
		},
		json_patent_applicationDate: {
			required:true,						
		},
		json_patent_startDate: {
			required:true,
			check_startDateAndEndDate: ["json_patent_startDate","json_patent_endDate"]
		},
		json_patent_endDate: {
			required:true,
			check_startDateAndEndDate: ["json_patent_startDate","json_patent_endDate"]
		},
		json_conferencePaper_paperType: {
			required:true,
		},
		json_conferencePaper_name: {
			required:true,
		},
		json_countryId: {
			required:true,
		},
		json_publishDate: {
			check_publishDate: true,
		},
		json_conferencePaper_startDate: {
			required:true,
			check_startDateAndEndDate: ["json_conferencePaper_startDate","json_conferencePaper_endDate"]
		},
		json_conferencePaper_endDate: {
			required:true,
			check_startDateAndEndDate: ["json_conferencePaper_startDate","json_conferencePaper_endDate"]
		},
		json_thesis_degree: {
			required:true,
		},
		json_thesis_defenseDate: {
			required:true,
			check_publishDate: true,
		},
		json_thesis_issuingAuthority: {
			required:true,
		},
		json_thesis_department: {
			required:true,
		},
		json_bookChapter_name: {
			required:true,
		},
		json_bookChapter_editors: {
			required:true,
		},
		json_bookChapter_publisher: {
			required:true,
		},
		json_book_editors: {
			required:true,
		},
		json_book_totalWords:{
			check_digits: true//整数	
		},
		json_book_publisher: {
			required:true,
		},		
		json_award_issuingAuthority: {
			required:true,
		},
		json_award_grade: {
			required:true,
		},
		json_award_category: {
			required:true,
		},
		json_award_awardDate:{
			required:true,
			check_publishDate: true,
		},
		json_patent_price: {
			check_price: true,
		},
		json_standard_publishAgency: {
		  required:true,
		  maxlength: 100
		},
		json_standard_standardNo: {
		  required:true,
		  check_titleSpecialChar: true,
      maxlength: 100
		},
		json_softwarecopyright_registerNo: {
      required:true,
      check_titleSpecialChar: true,
      maxlength: 100
    },
		json_standard_technicalCommittees: {
		  required:true,
		  maxlength: 50
		},
		dev_patent_type_input: {
			required:true,			
		},
	});

	
 });
//====================下面是自定义验证方法==============================================
 //检查标题去掉特殊字符后是否为空
 jQuery.validator.addMethod("check_titleSpecialChar", function(value, element, param) {  	 
    	value = value.replace(/&amp;/g, "&");
    	value = value.replace(/&quot;/g, "\"");
    	value = value.replace(/&apos;/g, "'");
    	value = value.replace(/&gt;/g, ">");
    	value = value.replace(/&lt;/g, "<");
    	value = value.replace(/&nbsp;/g, " ");
    	value = value.replace(/\r\n/g, "<br/>");
        value = BaseUtils.replaceHtml(value);
        value = $.trim(value);
		//2018-10-30 java后台会做过滤
    	//var value = $.trim(value.replace(/<[^>]*>/gi,""));
    	if(value=="" || value=="&nbsp;"){
    	    $(element).val("");
    		return false;
    	}
    	return true;
 }); 
 
 //检查起始页码的格式并给开始和结束页码赋值
jQuery.validator.addMethod("check_startAndEndPage", function(value, element, param) {
	if($.trim(value)==""){
		$("#totalPages").val(""); 
		$("#pageNumber").val("");
		return true;
	}
	// SCM-21745 去除对页码页数的输入控制，因为页数页码中可能也还有其他字符
	var reg = RegExp(/-/);
	if($.trim(value).match(reg)){
	  $("#totalPages").val(""); 
	  $("#pageNumber").val($(element).val());
	} else{
	  $("#totalPages").val($(element).val()); 
    $("#pageNumber").val("");
	}
//    var ismatch = /^\d+-\d+$/.test($.trim(value));
//    if(ismatch){
//    	var page = $.trim(value).split("-");
//    	if(page[0].length>8 || page[1].length>8){
//    		$(element).val("");
//    		scmpublictoast("页码超出8位数长度!", 1500);
//    		return true;
//    	}
//    	if(page[0]-page[1]>0){
//        	$(element).val(page[1]+"-"+page[0]);
//    	}
//    	if(param == "showTotalPages"){
//    		$("#totalPages").val(""); 
//    		$("#pageNumber").val($(element).val());
//    	}
//    }else if(/^\d+$/.test($.trim(value))){
//    	if(value.length>8){
//    		$(element).val("");
//    		scmpublictoast("页码超出8位数长度!", 1500);
//    		return true;
//    	}
//    	if(param == "showTotalPages"){
//    		$("#totalPages").val($.trim(value)); 
//    		$("#pageNumber").val("");
//    	}
//    }else{
//    	if(param != "journal"){
//        	$(element).val("");
//    	}
//    }
 	return true;
});  

//获取出版日期的值
jQuery.validator.addMethod("check_publishDate", function(value, element, param) {  
	var check_publishDate = validate_errorMsg.check_publishDate;
	var result = true;
    var publishDate = $(element).val();
    if(/^\d{4}(\/\d{2}){0,2}$/.test(publishDate)){
		var nowDate = new Date();
		var publishDate = new Date(publishDate);
    	if(publishDate>nowDate){
    		result = false;
    		check_publishDate = validate_errorMsg.check_publishDate_max;
    	}
    }else{
    	result = false;
    }
	$.validator.messages.check_publishDate = check_publishDate;
 	return  result;
});  
//校验开始日期和结束日期
jQuery.validator.addMethod("check_startDateAndEndDate", function(value, element, param) {
	var startDateStr = $("."+param[0]).val();
	var endDateStr = $("."+param[1]).val();
	var regex = /^\d{4}(-\d{2}){0,2}$/;
	if(regex.test(endDateStr) && regex.test(startDateStr)){
		var startDate = new Date(startDateStr);
		var endDate = new Date(endDateStr);
		if(startDate>endDate){
		  $("."+param[1]).val("");
			$("."+param[1]).closest(".error_import-tip_border").addClass("error_import-tip_border-warn");
			if(endDateStr == value){
				return false;
			}
		}else{
			$("."+param[0]).closest(".error_import-tip_border").removeClass("error_import-tip_border-warn");
			$("."+param[1]).closest(".error_import-tip_border").removeClass("error_import-tip_border-warn");
		}
	}else if(!regex.test(startDateStr)){
	  $("."+param[0]).val("");
		$("."+param[0]).closest(".error_import-tip_border").addClass("error_import-tip_border-warn");
		if(startDateStr == value){
			return false;
		}
	}else if(!regex.test(endDateStr) ){
	  $("."+param[1]).val("");
		$("."+param[1]).closest(".error_import-tip_border").addClass("error_import-tip_border-warn");
		if(endDateStr == value){
			return false;
		}
	}
	return true;
});  
//校验数字
jQuery.validator.addMethod("check_digits", function(value, element, param) {
	var result = true;
	if(!(/^\d+$/.test($.trim(value)) || $.trim(value) == "") || /^0$/.test($.trim(value))){
		$(element).val("");
	}		
	return result;
});
//邮箱校验
jQuery.validator.addMethod("check_email", function(value, element, param) {
	return /^\w+([.-]*\w+)*@\w+([.-][\w]+)+$/.test($.trim(value));
});
//交易金额
jQuery.validator.addMethod("check_price", function(value, element, param) {
	var result = true;
	if(!(/^\d{0,5}(\.\d{0,4})?$/.test($.trim(value)) || $.trim(value) == "") || /^0$/.test($.trim(value))){
		$(element).val("");
	}		
	return result;
});
