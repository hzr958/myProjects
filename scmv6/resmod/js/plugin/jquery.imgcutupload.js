(function($) {
	$.fn.extend({
		imgcutupload: function(options) {
			var defaults = {
					ctxpath:"/scmwebsns",
					respath:"/resscmwebsns",
					imgType:null, //1头像， 3群组图片， 5简历头像.
					locale:"zh_CN",
					imagePath: "",//图片路径.
					imageheight : 22,//最后裁剪长、宽，保存路径.
			   	    imagewidth : 60,//最后裁剪长、宽，保存路径.
			   	    filedwidth : 390,
			   	    dataDes3Id:null,//数据ID，比如PSN_ID，群组ID等.
			   	    allowType : '*.jpg,*.jpeg,*.gif,*.png,*.bmp',
			    	titleLabelCon:null,//图片上传备注内容，如需修改则提供此项内容_MJG_2013-04-12_SCM-2176.
					extendFun :null,
					editWindows:1,//1=需要弹出剪裁窗口,用于群组简介图片上传
					selectFileFn:0,//1选择图片后立刻上传 , 0不立刻上传
					ispsnweb:0 //更换头像psnweb
			};
			options = $.extend({},defaults, {
				target : this
			}, options);
			return this.each(function() {
				new $.ImgCutUpload(this, options);
			});
		}
	}),
	$.ImgCutUpload = function(obj, options) {
		
		if(options.imagePath == ''){
			options.imagePath = options.respath+"/images_v5/browse_icon_"+options.locale+".gif";
		}
		if(options.locale == 'en_US'){
			$.ImgCutUpload.message = $.ImgCutUpload.en_message;
		}else{
			$.ImgCutUpload.message = $.ImgCutUpload.zh_message;
		}
		$.ImgCutUpload.options = options;
		var postdata ={};
		//最终裁剪大小
		if(typeof options.cutwidth != "undefined" && typeof options.cutheight != "undefined"){
			postdata["imgw"] = options.cutwidth;
			postdata["imgh"] = options.cutheight;
		}
		//选择框比例大小
		if(typeof options.aspectRatio != "undefined"){
			postdata["aspectRatio"] = options.aspectRatio;
		}
		//裁剪宽X轴该变量
		if(typeof options.dragWidth != "undefined"){
			postdata["dragWidth"] = options.dragWidth;
		}
		
		//裁剪宽Y轴该变量
		if(typeof options.dragHeight != "undefined"){
			postdata["dragHeight"] = options.dragHeight;
		}
		if(typeof options.ispsnweb == "undefined" ){
			$.ajax( {
				url : (options.selectFileFn==1)?(options.ctxpath + '/imageUpload/ajaxLoadUpload2'):(options.ctxpath + '/imageUpload/ajaxLoadUpload'),
					type : 'post',
				data : postdata,
				dataType:'html',
				success : function(data) {
					$(obj).append(data);
					//增加判断图片上传是否登录有效_MJG_SCM-3536.
					if(data.ajaxSessionTimeOut!='yes'&&$("#upload_img_cutfile").size() > 0){
						initFileUpload();
					}
				}
			});
		}else{
			if($("#upload_img_cutfile").size() > 0){
				initFileUpload();
			}
		}
		function disabledUploadBtn(){
			$("#img_cut_upload_btn").addClass("uiButtonDisabled");
			$("#img_cut_upload_btn").attr("disabled","disabled");
			
		}
		
		function enabledUploadBtn(){
			$("#img_cut_upload_btn").removeClass("uiButtonDisabled");
			$("#img_cut_upload_btn").removeAttr("disabled","");
			
		}
		function disabledUploadSelect(){
			$("#upload_img_cutfile").attr("disabled","disabled");
		}
		function enabledUploadSelect(){
			$("#upload_img_cutfile").removeAttr("disabled","");
		}
		
		function initFileUpload(){
			//修改图片上传备注内容_MJG_SCM-2176.
			if(options.titleLabelCon!=null && typeof options.titleLabelCon != "undefined"){
				$(".label_con").html(options.titleLabelCon);
			}
			if(typeof options.ispsnweb == "undefined" ){
				$("#upload_img_cutfile").filestyle({ 
		   	     	image: options.imagePath,
		   	     	imageheight : options.imageheight,
		   	     	imagewidth : options.imagewidth,
		   	     	width : options.filedwidth
		   	 	}).change(function(){ showFileName();});
			}else{
				$("#upload_img_cutfile").change(function(){ 
					showFileName();
					});
			}
		   
		   $("#img_cut_upload_cr").bind("click",function(){
			   //根据复选框是否选中来设定标签的style属性，以兼容不同浏览器的显示效果_MJG_2012-10-16_SCM-951
			   if($(this).is(":checked") ){//&& $(".upload_img_cutfile").val().length>0){
				   enabledUploadSelect();
				  
				   if($(".upload_img_cutfile").val().length>0){
					   enabledUploadBtn();
				   }
			   }else{
				   disabledUploadSelect();
				   disabledUploadBtn();
			   }
		   });
		   
		   //点击上传按钮时，触发的回调函数，用于双层thickbox的情况
		   $("#show_cut_image").click(function(){
		   		if(options.extendFun!=null){
					options.extendFun();
				}
		   });
		   
		   $("#show_cut_image").thickbox({
			   ctxpath : options.ctxpath,
			   respath : options.respath
		   });
		   $("#img_cut_upload_btn").unbind();
			$("#img_cut_upload_btn").bind("click",ajaxFileUpload);
		}
		
		function showFileName() {
			var filedata = $('#upload_img_cutfile').val();
			if (filedata == ''){
				return;
			}else {
				if(options.selectFileFn!=0){
					$("#img_cut_upload_btn").click();
				}
				var fileName = filedata.substring(filedata .replace(/\\/g, '/').lastIndexOf('/') + 1);
				enabledUploadBtn();
				$("#img_cut_upload_crli").css("display","block");//.show();
				$("#img_cut_upload_cr").attr("checked","checked");
				$('.file.inp_text.upload_img_cutfile').attr('value', fileName);
			}
		}
		
		function ajaxFileUpload() {
			
			$("#upload_img_loading").ajaxStart(function() {
				$(this).show();
			}).ajaxComplete(function() {
				$(this).hide();
			});// 文件上传完成将图片隐藏起来//开始上传文件时显示一个图片
			
			disabledUploadBtn();
			   
			$.ajaxFileUpload( {
				url : options.ctxpath + "/archiveFiles/ajaxImageCutUpload",// 请求地址
				secureuri : false,// 一般设置为false
				fileElementId : 'upload_img_cutfile',// 文件的id属性
				data : {
				    allowType : options.allowType,
					des3Id : $.isFunction(options.dataDes3Id)?options.dataDes3Id():options.dataDes3Id,
					imgType : options.imgType
				},
				dataType : 'json',// 返回值类型
				success : ajaxFileUploadSuccess,
				error : ajaxFileUploadError
			});
		}
		
		// 服务器成功响应处理函数
		function ajaxFileUploadSuccess(data, status){
			if (data.result == 'error') {
				$.scmtips.show('warn',data.msg);
				$(".upload_img_cutfile,#upload_img_cutfile").change(function() {showFileName();});
				$('.upload_img_cutfile').attr('value', '');
			} else {
				$("#upload_img_path").val(data.path);
				 if('0'==options.editWindows){
					 imgUploadCutCallBack(data.path);
					//点击上传按钮时，触发的回调函数，用于双层thickbox的情况
				   		if($.ImgCutUpload.options.extendFun!=null){
							 $.Thickbox.closeWin();
						}else{
							parent.$.Thickbox.closeWin();
						}
						$.scmtips.show('success',$.ImgCutUpload.message.save_success);
				 }else{
					 $("#show_cut_image").trigger("click");
				 }
				$(".upload_img_cutfile,#upload_img_cutfile").change(function() {showFileName();});
				$('.file').attr('value', '');
			}
			disabledUploadBtn();
			$("#img_cut_upload_crli").hide();
			if(options.selectFileFn==1){
				$("#img_cut_upload_crli").show();
			}
			
		}
		
		// 服务器响应失败处理函数
		function ajaxFileUploadError(data, status){
			$(".upload_img_cutfile,#upload_img_cutfile").change(function() {showFileName();});
			$('.upload_img_cutfile').attr('value', '');
			$("#img_cut_upload_crli").hide();
			$.scmtips.show('error',"error code:"+status);
		}
	},
	$.ImgCutUpload.options = {},
	$.ImgCutUpload.zh_message = {
		save_success : "编辑成功",
		save_error : "编辑失败，请稍候再试"
	},
	$.ImgCutUpload.en_message = {
			save_success : "Upload picture successfully",
			save_error : "Upload picture failed"
	},
	$.ImgCutUpload.message = {
			save_success : "",
			save_error : ""
	},
	$.ImgCutUpload.saveCutResult = function(post_data){
		
		post_data.dataType = $.ImgCutUpload.options.imgType;
		post_data.dataDes3Id  = $.isFunction($.ImgCutUpload.options.dataDes3Id)?$.ImgCutUpload.options.dataDes3Id():$.ImgCutUpload.options.dataDes3Id;
		$.ajax( {
			url : $.ImgCutUpload.options.ctxpath+'/imageUpload/ajaxCutImage',
			type : 'post',
			dataType:'json',
			data : post_data,
			success : function(data) {
				if(data.result=="success"){
					imgUploadCutCallBack(data.path);
					//点击上传按钮时，触发的回调函数，用于双层thickbox的情况
			   		if($.ImgCutUpload.options.extendFun!=null){
						 $.Thickbox.closeWin();
					}else{
						parent.$.Thickbox.closeWin();
					}
					
					$.scmtips.show('success',$.ImgCutUpload.message.save_success);
				}else{
					//点击上传按钮时，触发的回调函数，用于双层thickbox的情况
			   		if($.ImgCutUpload.options.extendFun!=null){
						 $.Thickbox.closeWin();
					}else{
						parent.$.Thickbox.closeWin();
					}
					$.scmtips.show('warn',$.ImgCutUpload.message.save_error);
				}
			},error:function(data){
				//点击上传按钮时，触发的回调函数，用于双层thickbox的情况
			   	if($.ImgCutUpload.options.extendFun!=null){
					$.Thickbox.closeWin();
				}else{
					parent.$.Thickbox.closeWin();
				}
				$.scmtips.show('error',$.ImgCutUpload.message.save_error);
			}
		});
	};
	
})(jQuery);