/**
 * 个人文件相关js
 */
var VFileMain = VFileMain ? VFileMain : {};

// （我的文件、文件推荐）菜单 事件
VFileMain.menuClick = function(model){
	var arrLi = $("*[scm_file_id='menu__list']").find("li");
	arrLi.removeClass("item_selected");
	$("body").removeClass("no-scrollbar");
	if(model=="myFile"){
		arrLi.eq(0).addClass("item_selected");
		BaseUtils.changeUrl(model);
		VFileMain.getMyFileList();
	}else if(model=="fileRecommend"){
		arrLi.eq(1).addClass("item_selected");
		BaseUtils.changeUrl(model);
		
	}else{
		arrLi.eq(0).addClass("item_selected");
		BaseUtils.changeUrl('myFile');
		VFileMain.getMyFileList();
	}
}
VFileMain.doShare = function($this){
	BaseUtils.doHitMore($this,1000);
	SmateShare.getPsnFileListBatchSareParam();
	
};
VFileMain.shareBoxDoShare = function($this){
	$(".drawer-batch__box[list-drawer='myfile_list']").find(".drawer-batch__header .button_main").click();
	BaseUtils.doHitMore($this,1000);
	SmateShare.getPsnFileListShareBoxBatchSareParam();
	
};


VFileMain.myfileMainlist ={} ;
VFileMain.getMyFileList = function(){
	VFileMain.myfileMainlist = window.Mainlist({
		name : "myfile_list",
		listurl : "/psnweb/myfile/ajaxmyfilelist",
		listdata : {},
		method : "pagination",
		listcallback : function(xhr) {
			addFormElementsEvents();
		},
		statsurl: "/psnweb/myfile/ajaxmyfilelistcallback"
		,
		drawermethods:locale == "zh_CN"? {"删除文件":function(a){
        if(a.length>0){
          VFileMain.batchDeletePsnfile(a.join(","));
        }
      },"下载文件":function(a){
				if(a.length>0){
					VFileMain.batchDownloadPsnfile(a);
				}
			},"分享文件":function(a){
				if(a.length>0){
					VFileMain.shareBoxDoShare();
					// 为了显示批量处理窗口
					$(".drawer-batch__box").find(".drawer-batch__mask").addClass("dev_show_drawer-batch");
				}
			}
		}:{"Delete":function(a){
      if(a.length>0){
        VFileMain.batchDeletePsnfile(a.join(","));
      }
    },"Download":function(a){
			if(a.length>0){
				VFileMain.batchDownloadPsnfile(a);
			}
		},"Share":function(a){
			if(a.length>0){
				VFileMain.shareBoxDoShare();
				// 为了显示批量处理窗口
				$(".drawer-batch__box").find(".drawer-batch__mask").addClass("dev_show_drawer-batch");
			}
		}}
	});
}
;// "ZBASywMvS1YYeISSvqSw%2BA%3D%3D,ZBASywMvS1ZNGXS3G7uDXw%3D%3D,ZBASywMvS1b08WpiWsFdWg%3D%3D"
VFileMain.batchDownloadPsnfile = function(fileIds){
// document.location.href = ctxpath + "/zip/downLoad?ids=" + fileIds+ "&nodeId=0&type=0";
    $.post('/fileweb/batchdownload/ajaxgeturl', {"des3Ids": fileIds.join(","), "type": 0}, function(result){
        BaseUtils.ajaxTimeOut(result, function(){
            if(!!result && result.status == "success"){
                window.location.href = result.data.url;
            }
        });
    });
}

// 批量删除个人文件
VFileMain.batchDeletePsnfile = function(fileIds) {
  $.ajax( {
    url :"/psnweb/myfile/ajaxbatchdeletepsnfile" ,
    type : "post",
    dataType : "json",
    data : {
        "des3FileIds":fileIds
    },
    success : function(data) {
      SmateShare.ajaxTimeOut(data,function(){
        if (data.status == "success") {
          scmpublictoast(fileMain.delSuccessPre + data.count
              + (data.count > 1 ? fileMain.delSuccessFiles : fileMain.delSuccessFile), 2000);
          // 刷新个人文件列表
          VFileMain.myfileMainlist.reloadCurrentPage();
          VFileMain.myfileMainlist.drawerRemoveSelected();
        } else {
          scmpublictoast(fileMain.delFail, 2000);
        }
      });
    }
  });
}

// 重新加载我的文件列表,去除所有的查询条件
VFileMain.reloadMyFileList = function(){
  BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function() {
    VFileMain.myfileMainlist.resetAllFilterOptions();
  }, 1);
};
VFileMain.getShareRecordsList = function(){
    if(document.getElementsByClassName("drawer-batch__tip-container").length > 0){
        document.getElementsByClassName("drawer-batch__tip-container")[0].style.display = "none";
    }
	VFileMain.shareRecordsMainlist = window.Mainlist({
		name : "sharerecords_list",
		listurl : "/psnweb/myfile/ajaxfilesharerecordslist",
		listdata : {},
		method : "scroll",
		listcallback : function(xhr) {
		},
	});
}
VFileMain.cancelFileShare = function(des3ShareBaseId,obj){
	BaseUtils.doHitMore(obj,1000);
	$.ajax( {
		url :"/psnweb/myfile/ajaxcancelfileshare" ,
		type : "post",
		dataType : "json",
		data : {
			des3ShareBaseId:des3ShareBaseId
		},
		success : function(data) {
			SmateShare.ajaxTimeOut(data,function(){
				if(data.result == "success"){
				  var $parentNode = $(obj).closest(".main-list__item").parent();
					$(obj).closest(".main-list__item").remove();
					if($parentNode.find(".main-list__item").length==0){
					  $parentNode.append("<div class='response_no-result'>未找到符合条件的记录</div>");
          }
					scmpublictoast(fileMain.cancelShareSuccess,1000);
				}else{
					scmpublictoast(fileMain.cancelShareFail,1000);
				}
				
			});
		}
	});
	
}
VFileMain.showShareRecordsUI = function($this){
  BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function() {
    BaseUtils.doHitMore($this,1000);
    VFileMain.getShareRecordsList();
    showDialog('file_share_records');
  }, 1);
}
VFileMain.hideShareRecordsUI = function($this){
	BaseUtils.doHitMore($this,1000);
	hideDialog('file_share_records');
	// 初始化文件列表批量框
	VFileMain.myfileMainlist.initializeDrawer();
	VFileMain.myfileMainlist.reloadCurrentPage();
}


// 显示添加文件框
VFileMain.addPsnFile = function() {
  BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function() {
    VFileMain.SelectFileUpload();
    document.getElementsByClassName("form__sxn_row-list")[0].innerHTML = "";
  }, 1);
};



/**
 * 群组上传文件弹出框
 */
VFileMain.SelectFileUpload = function() {
	addFormElementsEvents();
	showDialog("psn_file_upload_file");
	VFileMain.initSelectFileUpload();
};
VFileMain.initSelectFileUpload = function() {
	// 上传文件重置
	var fileDoxDom = $("#fileupload").find(".fileupload__box").get(0);
	fileUploadModule.resetFileUploadBox(fileDoxDom);
	var dataJson = {
		"fileDealType" : "generalfile"
	};
	// var filetype =
  // ".txt,.pdf,.doc,.docx,.ppt,.pptx,.xls,.xlsx,.rar,.zip,.jpg,.png,.gif,.jpeg,.htm,.html,.xhtml".split(",");
	var data = {
		"fileurl" : "/fileweb/fileupload",
		"filedata" : dataJson,
		"filecallback" : VFileMain.uploadFileCallBack,
		"method":"dropadd",
		"multiple":"true",
		"maxlength":"10",
		"maxtip":"1"
	}
	fileUploadModule.initialization(document.getElementById("fileupload"), data);
};
/**
 * 上传文件回调函数
 */
VFileMain.uploadFileCallBack = function(xhr) {
	const data = eval('('+xhr.responseText+')');
	BaseUtils.ajaxTimeOut(data,function(){
		if (data.successFiles >=1 ) {
			var fileIdArray = data.successDes3FileIds.split(",");
			// 多文件上传
			for(var key=0 ;key<fileIdArray.length;key++){
			var returnData ={
				 'fileDesc':$("#psn_file_upload_file_content").val(),
 				 'des3ArchiveFileId' :fileIdArray[key]
			}
			$.ajax( {
				url :"/psnweb/myfile/ajasavemyuploadfile" ,
				type : "post",
				dataType : "json",
				data : returnData,
				async : false,//多文件同步方式
				success : function(data) {
					SmateShare.ajaxTimeOut(data,function(){
						/*if(data.result === "success"){
							VFileMain.cancleUploadFile();
							VFileMain.menuClick('myFile');
							scmpublictoast(fileMain.uploadSuccess,1000);
						}else{
							scmpublictoast(fileMain.uploadFail,1000);
						}*/
					});
				}
			});
		}
			VFileMain.cancleUploadFile();
      VFileMain.menuClick('myFile');

		} else {
			scmpublictoast(data.failFileNames, 2000);
		}
	})
	
};

// 本地上传文件弹出框 取消
;
VFileMain.cancleUploadFile = function() {
	hideDialog("psn_file_upload_file");
	$("#psn_file_upload_file_content").val("");
}

/**
 * 点击按钮上传文件
 */
VFileMain.uploadFileButton = function(obj){
  // 防重复点击
  BaseUtils.doHitMore(obj,6000);
	// 点击上传文件
    if(document.getElementsByClassName("drawer-batch__tip-container").length > 0){
        document.getElementsByClassName("drawer-batch__tip-container")[0].style.display = "none";
    }
	var fileSelect = $("#fileupload").find(".file_selected");
	if(fileSelect == undefined  || fileSelect.length==0){
		scmpublictoast(fileMain.uploadFile, 2000);
		 return ;
	}
	var fileDoxDom = $("#fileupload").find(".fileupload__box").get(0);
	var dataFile={
			"fileDesc":$("#psn_file_upload_file_content").val()
	}
	// 超时判断
	$.ajax({
		url: "/dynweb/ajaxtimeout",
		dataType:"json",
		type:"post",
		data: {
		},
		success: function(data){
			BaseUtils.ajaxTimeOut(data,function(){
				fileUploadModule.uploadFile(fileDoxDom,dataFile);
			});
			
		}
	});
	
	
	
};
VFileMain.delMyFileConfirm = function(des3FileId,obj){
  BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function() {
    var screencallbackData={
        'des3FileId':des3FileId,
        'obj':obj
    }
    var option={
        'screentxt':fileMain.delFileConfirm ,
        'screencallback':VFileMain.delMyFile,
        'screencallbackData':screencallbackData
    };
    popconfirmbox(option)  ;
  }, 1);
};
VFileMain.delMyFile = function(callbackData){
	var   des3FileId = callbackData.des3FileId ;
	var   obj = callbackData.obj ;
	BaseUtils.doHitMore(obj,1000);
	$.ajax({
		url: "/psnweb/myfile/ajaxdelmyfile",
		dataType:"json",
		type:"post",
		data: {
			des3FileId:des3FileId
		},
		success: function(data){
			BaseUtils.ajaxTimeOut(data,function(){
				if(data.result == "success"){
					VFileMain.myfileMainlist.reloadCurrentPage();
					// 移除批量中选中的数据
					VFileMain.myfileMainlist.drawerRemoveSelected(des3FileId);
					scmpublictoast(fileMain.delScuess, 1000);
				}else{
					scmpublictoast(fileMain.delFail, 1000);
				}
				
			});
			
		}
	});
};
/**
 * 文件编辑弹窗代码，此部分编辑需要注意弹窗显示的一些问题SCM-13366。 注意填充内容，高度计算，弹窗显示，光标定位这样的顺序不能变。
 */
VFileMain.showFileEdit = function(des3fileId,obj){
  BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function() {
    const fileEidtObj = $("*[dialog-id='file_eidt_dialog']");
    var textarea = fileEidtObj.find('textarea')[0];
    const $div = fileEidtObj.find(".textarea-autoresize-div")[0]; // 自动填词辅助框，用来计算高度
    fileEidtObj.attr("des3_file_id",des3fileId);
    var desc = $(obj).closest(".main-list__item").find(".file-idx__main_intro").text().trim();
    // 先填充内容 SCM-13366
    textarea.value = desc;
    $div.innerHTML = textarea.value + "<br/>";
    // 然后计算高度 SCM-13366
    textarea.closest(".input__area").style.height = window.getComputedStyle($div).getPropertyValue("height");
    // 之后显示弹窗 SCM-13366
    showDialog("file_eidt_dialog");
    // 最后定位光标焦点 SCM-13366
    $(textarea).focus();
  }, 1);
};
VFileMain.hideFileEdit = function(){
	hideDialog("file_eidt_dialog");
};
VFileMain.saveMyFileDesc = function(obj){
	BaseUtils.doHitMore(obj,1000);
	const fileEidtObj = $("*[dialog-id='file_eidt_dialog']");
	$.ajax({
		url: "/psnweb/myfile/ajaxsavefiledesc",
		dataType:"json",
		type:"post",
		data: {
			des3FileId:fileEidtObj.attr("des3_file_id"),
			fileDesc:$.trim(fileEidtObj.find("textarea").val())
		},
		success: function(data){
			BaseUtils.ajaxTimeOut(data,function(){
				VFileMain.myfileMainlist.reloadCurrentPage();
				VFileMain.hideFileEdit();
				scmpublictoast(fileMain.saveScuess, 1000);
			});
			
		}
	});
};
/**
 * 打开文件
 */
VFileMain.openFile = function(fileId,obj) {
	BaseUtils.doHitMore(obj,1000);
	window.location.href = snsctx + "/file/download?des3Id=" + fileId
			+ "&nodeId=1&type=1&flag=5";
};
/**
 * test
 */
VFileMain.test = function() {
    alert("code"+code);
};
