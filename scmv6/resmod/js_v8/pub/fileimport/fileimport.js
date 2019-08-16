var FileImport = FileImport || {};

//选择导入的文件类型
FileImport.select = function(){
	$("i.selected-oneself").click(function(){
		$("i.selected-oneself_confirm").removeClass("selected-oneself_confirm").addClass("selected-oneself");
		$(this).addClass("selected-oneself_confirm");
		$("#dbType").val($(this).attr("value"));
	});
};
//选择文件获取文件名称
FileImport.selectFile = function(obj){
	var $obj = $(obj);
	if($obj.val().length>0) 
		$('.sourceFile').val($obj.val().substring($obj.val().replace(/\\/g, '/').lastIndexOf('/') + 1));
};
FileImport.uploadPubSourceFile = function (){
    var formData = new FormData();
    formData.append("filedata", document.getElementById("sourceFile").files[0]);
    formData.append("fileDealType","pubsrcfile");
    $.ajax({
        url: "/fileweb/fileupload",
        type: "POST",
        data: formData,
        /**
         *必须false才会自动加上正确的Content-Type
         */
        contentType: false,
        async: false,
        dataType: "json",
        /**
         * 必须false才会避开jQuery对 formdata 的默认处理
         * XMLHttpRequest会对 formdata 进行正确的处理
         */
        processData: false,
        success: function (data) {
            BaseUtils.ajaxTimeOut(data,function(){
                if (data.successFiles >=1 ) {
                    var fileIdArray = data.successDes3FileIds.split(",");
                    var des3ArchiveFileId   = fileIdArray[0];
                    $("#des3PubFileId").val(des3ArchiveFileId);
                } else {
                    scmpublictoast(data.failFileNames, 2000);
                }
            })
        },
        error: function () {

        }
    });
}
//导入按钮
FileImport.importFile = function(obj){
	var click = BaseUtils.unDisable(obj);
	BaseUtils.checkTimeout(function(){
		if($("#sourceFile").val().length<=0){
			scmpublictoast(FileImpotMsg.selectFile,2000);
			BaseUtils.disable(obj,click);
			return;
		}
		if($("#dbType").val()==""){
			scmpublictoast(FileImpotMsg.selectimportType,2000);
			BaseUtils.disable(obj,click);
			return;
		}
		var file = document.getElementById("sourceFile").files[0];
        if (file.size >= 1024 * 1024 * 30) {
            scmpublictoast(FileImpotMsg.overSize,2000);
            return ;
        }
        $("#pubFileId").val("");
        FileImport.uploadPubSourceFile();
        if($("#des3PubFileId").val() != ""){
            console.log($("#des3PubFileId").val());
            $("#importForm").submit();
        }
/*        var form = new FormData(document.getElementById("importForm"));
        $.ajax({
            url:"/pub/file/import",
            type:"post",
            data:form,
            processData:false,
            contentType:false,
            success:function(data){
            	var result = /id="new-success_save"/g.exec(data);
            	if(result && result != ""){
            		$("#resultMsg").html(data);
            		show();
            	}else{//有成果
            		$("html").html(data);
            		initImpList();
            	}
            },
            error:function(e){

            }
        });  */      

    });
}
//下载excel模板
FileImport.openFile = function(){
	window.open("/scmwebsns/one/openfile?type=1&flag=9" ,"_self");
}

FileImport.changeRepeatSelect = function(obj){
	var value = $(obj).val();
	if(value==0){
		$(obj).parent().siblings(".repeatSelectMsg").text(FileImpotMsg.repeatMsg0);
	}
	if(value==1){
		$(obj).parent().siblings(".repeatSelectMsg").text(FileImpotMsg.repeatMsg1);
	}
	if(value==2){
		$(obj).parent().siblings(".repeatSelectMsg").text(FileImpotMsg.repeatMsg2);
	}
}
