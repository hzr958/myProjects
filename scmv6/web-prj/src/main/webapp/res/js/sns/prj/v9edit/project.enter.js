ProjectEnter = ProjectEnter || {} ;
ProjectMember = function() {
};
ProjectAttachment = function() {
};

ProjectFulltext = function() {
};
/**
 * 设置隐私权限
 * 
 * @param obj
 */
ProjectEnter.setAuthority = function (obj) {
    var authority = $(obj).attr("authority");
    if(authority == "7" || authority ==""){
        $("#_prj_authority").val("4");
        $(obj).attr("authority","4");
        $(obj).attr("title",ProjectEnter.onlyMe);
        $(obj).removeClass("selected-func_close-open");
        $(obj).addClass("selected-func_close");
    }else{
        $("#_prj_authority").val("7");
        $(obj).attr("authority","7");
        $(obj).attr("title",ProjectEnter.public);
        $(obj).addClass("selected-func_close-open");
        $(obj).removeClass("selected-func_close");
    }
}

/**
 * 设置项目类型
 * 
 * @param obj
 */
ProjectEnter.setPrjType = function (obj) {
    $("#_project_prj_type").find(".selected-oneself").removeClass("selected-oneself_confirm")
    $(obj).addClass("selected-oneself_confirm");
    var type = $(obj).attr("type");
    $("#_project_prj_type_h").val(type);
}


/**
 * 设置项目状态
 * 
 * @param obj
 */
ProjectEnter.setPrjState = function (obj) {
    $("#_project_prj_state").find(".selected-oneself").removeClass("selected-oneself_confirm")
    $(obj).addClass("selected-oneself_confirm");
    var type = $(obj).attr("state");
    $("#_project_prj_stat_h").val(type);
};


ProjectEnter.getInsNameJson = function() {
    return {
        "limit" : 10
    };
}
ProjectEnter.InsNameCallBack = function(obj) {
  var code = $("#_project_ins_name").attr("code");
  $("#_project_ins_id").val(code);
}


// 删除关键词
ProjectEnter.deleteKey = function(event) {
    var $obj = $(".new-importantkey_container .new-importantkey_container-item").last();
    var item = $obj.find(".new-importantkey_container-item_colse");
    ProjectEnter.deleteKeyElement(item);
};
// 删除关键词
ProjectEnter.deleteKeyElement = function(obj) {
    var item = $(obj).closest(".new-importantkey_container-item");
    var words = $(item).text();
    var length = $("#prj_keyword_list").find(".json_keyword").length;
    $(item).remove();
    var length = $("#prj_keyword_list").find(".json_keyword").length;
    if (length < 5) {
        $("#addKeyInput").show();
    }
     length = $("#prj_keyword_list_en").find(".json_keyword").length;
    if (length < 5) {
        $("#addKeyInputEn").show();
    }
};
// 添加关键字
ProjectEnter.addkey = function(event) {
    var addKeyList = $.trim($("#addKeyInput").val()).split(/[;；]/);
    var $jsonKey = $("#prj_keyword_list").find(".json_keyword");
    var keyLength = $jsonKey.length;
    for (var i = 0; i < addKeyList.length; i++) {
        var addKey = addKeyList[i];
        addKey = BaseUtils.replaceHtml(addKey);
        if (addKey && addKey != "" && keyLength < 5) {// 最多5个关键词
            var repeat = false;
            $jsonKey.each(function() {// 有相同的就不添加
                if ($.trim($(this).text()) == addKey) {
                    repeat = true;
                }
            })
            if (repeat) {
                $("#addKeyInput").val("");
                return;
            }
            ProjectEnter.addKeyElement(addKey);// 添加节点
            ++keyLength;
            if (keyLength == 5) {
                $("#addKeyInput").val("");
                $("#addKeyInput").hide();
            }
        }
    }
};

// 添加关键字
ProjectEnter.addEnkey = function(event) {
    var addKeyList = $.trim($("#addKeyInputEn").val()).split(/[;；]/);
    var $jsonKey = $("#prj_keyword_list_en").find(".json_keyword");
    var keyLength = $jsonKey.length;
    for (var i = 0; i < addKeyList.length; i++) {
        var addKey = addKeyList[i];
        addKey = BaseUtils.replaceHtml(addKey);
        if (addKey && addKey != "" && keyLength < 5) {// 最多5个关键词
            var repeat = false;
            $jsonKey.each(function() {// 有相同的就不添加
                if ($.trim($(this).text()) == addKey) {
                    repeat = true;
                }
            })
            if (repeat) {
                $("#addKeyInputEn").val("");
                return;
            }
            ProjectEnter.addEnKeyElement(addKey);// 添加节点
            ++keyLength;
            if (keyLength == 5) {
                $("#addKeyInputEn").val("");
                $("#addKeyInputEn").hide();
            }
        }
    }
};
/**
 * 显示关键词
 */
ProjectEnter.showKeyItem = function () {
    var keywords = $("#_project_zh_keywords").val();
    if(keywords == ""){
        return ;
    }
    var split = keywords.split(";");
    if(split.length>0){
        var  keyLength = 0
        for(var i = 0 ; i<split.length && $.trim(split[i]) != "" ; i++){
            ProjectEnter.addKeyElement(split[i]);
            ++keyLength;
        }
        if (keyLength == 5) {
            $("#addKeyInput").val("");
            $("#addKeyInput").hide();
        }
    }
}
ProjectEnter.showEnKeyItem = function () {
    var keywords = $("#_project_en_keywords").val();
    if(keywords == ""){
        return ;
    }
    var split = keywords.split(";");
    if(split.length>0){
        var  keyLength = 0
        for(var i = 0 ; i<split.length && $.trim(split[i]) != "" ; i++){
            ProjectEnter.addEnKeyElement(split[i]);
            ++keyLength;
        }

        if (keyLength == 5) {
            $("#addKeyInputEn").val("");
            $("#addKeyInputEn").hide();
        }
    }
}
ProjectEnter.addKeyElement = function(addKey) {
    var element = '<div class="new-importantkey_container-item">'
        + '<div class="new-importantkey_container-item_detaile json_keyword" title="'
        + addKey
        + '">'
        + addKey
        + '</div>'
        + '<div class="new-importantkey_container-item_colse">'
        + '<i class="material-icons"  onclick="ProjectEnter.deleteKeyElement(this)">close</i>'
        + '</div>' + '</div>';
    $("#addKeyInput").before(element);
    $("#addKeyInput").val("");
};
ProjectEnter.addEnKeyElement = function(addKey) {
    var element = '<div class="new-importantkey_container-item">'
        + '<div class="new-importantkey_container-item_detaile json_keyword" title="'
        + addKey
        + '">'
        + addKey
        + '</div>'
        + '<div class="new-importantkey_container-item_colse">'
        + '<i class="material-icons"  onclick="ProjectEnter.deleteKeyElement(this)">close</i>'
        + '</div>' + '</div>';
    $("#addKeyInputEn").before(element);
    $("#addKeyInputEn").val("");
};
/**
 * 处理关键词
 */
ProjectEnter.dealKeyItem = function () {
    var keywords = "";
    $("#prj_keyword_list").find(".new-importantkey_container-item").each(function () {
        keywords +=$(this).find(".json_keyword").html()+";";
    });
    if(keywords != ""){
        keywords = keywords.substring(0,keywords.length-1);
    }
    $("#_project_zh_keywords").val(keywords);
}

/**
 * 处理资助机构，去除json字符串 {}
 */
ProjectEnter.dealAgency = function () {
    var keys =["_project_scheme_agency_name","_project_scheme_name","_project_scheme_agency_name_en","_project_scheme_name_en"];
    for(var i in keys){
        var val = $("#"+keys[i]).val().replace(/[{}\[\]\,]/g,"");
        $("#"+keys[i]).val(val);
    }

}
/**
 * 处理关键词
 */
ProjectEnter.dealEnKeyItem = function () {
    var keywords = "";
    $("#prj_keyword_list_en").find(".new-importantkey_container-item").each(function () {
        keywords +=$(this).find(".json_keyword").html()+";";
    });
    if(keywords != ""){
        keywords = keywords.substring(0,keywords.length-1);
    }
    $("#_project_en_keywords").val(keywords);
}

// 项目成员
/**
 * 设置项目状态
 * 
 * @param obj
 */
ProjectMember.setNotifyAuthor = function (obj) {
    var val = $(obj).find("input").val();
    if(val == "1"){
        $(obj).removeClass("selected-author_confirm");
        $(obj).find("input").val("0");
    }else{
        $(obj).addClass("selected-author_confirm");
        $(obj).find("input").val("1");
    }
};

/**
 * 删除成员 替换模板字符，同时设置序号，包括重新排[xx]中的顺序
 * 
 * @param obj
 */
ProjectMember.delPrjMember = function (obj) {
    // 删除当前div
    if(obj != undefined){
        $(obj).closest(".prj_member_dev").remove();
    }
    $("#projectMemberList").find(".prj_member_dev").each(function(i){
        ProjectMember.modifyPrjMemberField(this,i);
    });
    ProjectMember.dealMoveArrow();
    ProjectMember.checkPrjMember();
}

/**
 * 修改对象的字段, 某节点的所有字段
 * 
 * @param obj
 *          document对象
 * @param 序号
 *          0：表示修改第一条数据
 */
ProjectMember.modifyPrjMemberField = function (obj ,i) {
    // $(obj).find(".prj_member_seq_dev").html(i+1);
    $(obj).find("*").each(function(){
        // 序号
        var num = $.fn.setNumberLen(i+1 ,2);

        strName = $(this).attr("name");
        if(strName) {
            strName = strName.replace(":","@");
            var atIndex = strName.indexOf("@");
            if(atIndex >= 0) {
                strName = strName.replace(/\[\d*\]\/\@/g,"["+ num +"]\/@");
                // 重新定位
                atIndex = strName.indexOf("@");
                var strAttr = strName.substring(atIndex+1,strName.length);
                $(this).attr("name",strName);
            } else {
                strAttr = strName;
            }
        }
        strId =  $(this).attr("id");
        if(strId) {
            strAttr = strId.replace(":","@");
            strAttr = strAttr.replace(/\[\d*\]\/\@/g,"["+ num +"]\/@");
            strAttr = strAttr.replace(/_\d+_/g,"_"+ num +"_");
            strAttr = strAttr.replace(/[\@\/\[\]]/g,"_");
            strAttr = strAttr.replace(/[_]+/g,"_");
            $(this).attr("id",strAttr);
        }
    });
}
/**
 * 设置拥有者
 */
ProjectMember.setOwner = function (obj) {
    var confirm = $(obj).hasClass("selected-oneself_confirm");
    if(confirm){
        $(obj).removeClass("selected-oneself_confirm");
        $(obj).addClass("selected-oneself");
        $(obj).closest(".prj_member_seq_dev").find("input").val("0");
    }else{
        if($(obj).closest(".prj_member_dev").find("input[name$='name']").val() == ""){
            scmpublictoast(ProjectEnter.memberFirstInput, 1500);
            return ;
        }
        // 先清空
        $("#projectMemberList").find(".prj_member_seq_dev").each(function () {
            $(this).find("i").removeClass("selected-oneself_confirm");
            $(this).find("input").val("0");
            if(!$(this).find("i").hasClass("selected-oneself")){
                $(this).find("i").addClass("selected-oneself");
            }
        })
        $(obj).addClass("selected-oneself_confirm");
        $(obj).removeClass("selected-oneself");
        $(obj).closest(".prj_member_seq_dev").find("input").val("1");
    }

}
/**
 * 新增成员
 * 
 * @param obj
 */
ProjectMember.addPrjMember = function () {
    var prjMemberNode = $(".prj_member_template").clone(true);
    prjMemberNode.css("display","");
    prjMemberNode.removeClass("prj_member_template");
    var length = $("#projectMemberList").find(".prj_member_dev").length;
    ProjectMember.modifyPrjMemberField(prjMemberNode[0],length)
    $("#projectMemberList").append(prjMemberNode);
    ProjectMember.dealMoveArrow();
    // 监听成员
    ProjectMember.listenerPrjMember();
}
/**
 * 检查项目成果是否个数； 小于3 自动补充
 */
ProjectMember.checkPrjMember = function(){
    var len = $("#projectMemberList").find(".prj_member_dev").length ;
    if(len <= 2){
        for(var i = 0 ; i<(3-len) ; i++){
            ProjectMember.addPrjMember();
        }
    }else{
        var name = $("#projectMemberList").find(".prj_member_dev").eq(len-1).find("input[name$='member_psn_name']").val();
        var ins_name = $("#projectMemberList").find(".prj_member_dev").eq(len-1).find("input[name$='ins_name1']").val();
        var email = $("#projectMemberList").find(".prj_member_dev").eq(len-1).find("input[name$='email']").val();
        if($.trim(name) !="" || $.trim(ins_name) !="" || ($.trim(email) !="" ) ){
            ProjectMember.addPrjMember();
        }else if($.trim(name) =="" && $.trim(ins_name) =="" && ($.trim(email) =="" ) ){
            // 倒数第二行为空时， 删除最后一行数据
            if(len>3){
                var sec_name = $("#projectMemberList").find(".prj_member_dev").eq(len-2).find("input[name$='member_psn_name']").val();
                var sec_ins_name2 = $("#projectMemberList").find(".prj_member_dev").eq(len-2).find("input[name$='ins_name1']").val();
                var sec_email = $("#projectMemberList").find(".prj_member_dev").eq(len-2).find("input[name$='email']").val();
                if($.trim(sec_name) =="" && $.trim(sec_ins_name2) =="" && ($.trim(sec_email) =="" )){
                    ProjectMember.delPrjMember($("#projectMemberList").find(".prj_member_dev:last").find("input[name$='member_psn_name']")[0]);
                }
            }
        }

    }
}
/**
 * 最后添加监听
 */
ProjectMember.listenerPrjMember = function () {
    // 绑定监听事件
    window.addFormElementsEvents();
    $("#projectMemberList").find(".prj_member_dev:last").each(function () {
        $(this).find("input").bind("input",ProjectMember.checkPrjMember);
        $(this).find("input[name$='member_psn_name']").bind("input",this,ProjectMember.authorNameChange);
    });
}

/**
 * 初始化监听成员
 */
ProjectMember.initListenerPrjMember = function () {
    $("#projectMemberList").find(".prj_member_dev").each(function () {
        $(this).find("input").bind("keyup onchange",ProjectMember.checkPrjMember);
        $(this).find("input[name$='member_psn_name']").bind("input",this ,ProjectMember.authorNameChange);
    });
};

 ProjectMember.callbackName = function(obj,otherStr){
    if(otherStr && otherStr!=""){
        var other =  JSON.parse(otherStr);
        var $parent = $(obj).closest(".prj_member_dev");
        $parent.find("input[name$='ins_name1']").val(other.insName);
        // $parent.find(".json_member_insNames").attr("code",other.insId);
        $parent.find("input[name$='email']").val(other.email);
        $parent.find(".json_member_des3PsnId").val(other.des3PsnId);
    }
};


 ProjectMember.getExitPsnId = function() {
    var ids = "";
    $("#projectMemberList").find(".prj_member_dev").each(function() {
        var des3PsnId = $(this).find(".json_member_des3PsnId").val();
        if (des3PsnId != "") {
            ids += "," + des3PsnId;
        }
    });
    if (ids != "") {
        ids = ids.substring(1);
    };
    return {"des3PsnId" : ids};
};
 ProjectMember.authorNameChange = function(obj){
    $(obj.currentTarget).closest(".prj_member_dev").find(".json_member_des3PsnId").val("");
};
// selected-func_up-none
/**
 * 处理箭头
 */
ProjectMember.dealMoveArrow = function () {
    var length = $("#projectMemberList").find(".arrow_dev").length ;
    if(length == 0){
        return ;
    }else  if(length == 1){
        $("#projectMemberList").find(".prj_member_dev .arrow_dev").each(function(i){
            $(this).find(".selected-func_down").addClass("selected-func_up-none");
            $(this).find(".selected-func_up").addClass("selected-func_up-none");
        });
    }else{
        $("#projectMemberList").find(".prj_member_dev .arrow_dev").each(function(i){
            $(this).find(".selected-func_delete").removeClass("selected-func_nodelete");
            if(i == 0){
                $(this).find(".selected-func_up").addClass("selected-func_up-none");
                $(this).find(".selected-func_down").removeClass("selected-func_up-none");
            }else if(i == length-1){
                $(this).find(".selected-func_down").addClass("selected-func_down-none");
                $(this).find(".selected-func_up").removeClass("selected-func_up-none");
                $(this).find(".selected-func_delete").addClass("selected-func_nodelete");
            }else{
                $(this).find(".selected-func_down").removeClass("selected-func_down-none");
                $(this).find(".selected-func_up").removeClass("selected-func_up-none");
            }
        });
    }
}
/**
 * 向下移动成员
 * 
 * @param obj
 */
ProjectMember.downMember =function (obj) {
   var seq = $(obj).closest(".prj_member_dev").find("input[name$='seq_no']").attr("name");
    seq = seq.replace(/\D+/g,"");
    seq = seq.replace(/^0?/g,"");
    seq = parseInt(seq);
    // 当前要移动的节点
    var moveNode = $(obj).closest(".prj_member_dev");
    var cloneNode = moveNode.clone(true);
    moveNode.remove();
    $("#projectMemberList").find(".prj_member_dev").each(function(i){
        if(i == seq-1){
            $(this).after(cloneNode);
        }
    });
    // 公用方法
    ProjectMember.delPrjMember();
}

/**
 * 向下移动成员
 * 
 * @param obj
 */
ProjectMember.upMember =function (obj) {
    var seq = $(obj).closest(".prj_member_dev").find("input[name$='seq_no']").attr("name");
    seq = seq.replace(/\D+/g,"");
    seq = seq.replace(/^0?/g,"");
    seq = parseInt(seq);
    // 当前要移动的节点
    var moveNode = $(obj).closest(".prj_member_dev");
    var cloneNode = moveNode.clone(true);
    moveNode.remove();
    $("#projectMemberList").find(".prj_member_dev").each(function(i){
        if(i == seq-2){
            $(this).before(cloneNode);
        }
    });
    // 公用方法
    ProjectMember.delPrjMember();
}




// 项目附件 start
// 点击上传
ProjectAttachment.fileuploadBoxOpenInputClick = function(ev) {
    var $this = $(ev.currentTarget);
    $this.find('input.fileupload__input').click();
    ev.stopPropagation();// 阻止事件的向上传播
    return;
};

// 初始化上传控件
ProjectAttachment.initialization = function(obj) {
    var data = {
        "fileurl" : "/fileweb/fileupload",
        "filedata" : {
            fileDealType : "generalfile"
        },
        "method" : "nostatus",
        "nonsupportType" : ["js","jsp","php"],
        "filecallback" : ProjectAttachment.upfileBack,// 回调函数
        "filecallbackparam" : {
            "obj" : obj,
            "fileType" : $(obj).attr("fileType")
        },
        "multiple" : "true",
        "maxtip" : "0"
    };
    fileUploadModule.initialization(obj, data);
};
/**
 * 上传全文回调
 */
ProjectAttachment.upfileBack = function (xhr,params){
    const data = eval('(' + xhr.responseText + ')');
    if (data.successFiles >= 1) {
      var fileIdArray = data.successDes3FileIds.split(",");
      // 支持多文件上传
      for (var key = 0; key < fileIdArray.length; key++) {
        var fileId = fileIdArray[key];
        var fileName = data.extendFileInfo[key].fileName;
        var downloadUrl = data.extendFileInfo[key].downloadUrl;
        var createTime = data.createTime;
        var fileType = data.extendFileInfo[key].fileType;

        var prjAttachmentNode = $(".prj_attachment_template").clone(true);
        prjAttachmentNode.css("display","");
        prjAttachmentNode.removeClass("prj_attachment_template");
        prjAttachmentNode.addClass("prj_attachment_dev");
        var length = $("#prj_attachment_list").find(".prj_attachment_dev").length;
        ProjectAttachment.modifyPrjAttachmentField(prjAttachmentNode[0],length)
        $("#prj_attachment_list").append(prjAttachmentNode);
        // fileUploadModule.resetFileUploadBox($("#projectAttachmentList")[0]);
        var fileuploadNode = $(".dev_fileupload__box").clone(true);
        fileuploadNode.removeClass("dev_fileupload__box");
        fileuploadNode.css("display","");
        $("#projectAttachmentList").find(".upfile").html("");
        $("#projectAttachmentList").find(".upfile").append(fileuploadNode);
        ProjectAttachment.initialization($("#projectAttachmentList")[0]);
        // 给节点赋值
        var flag ="";
        length = length+1;
        if(length<10){
            flag= "0"
        }
        $("#_prj_attachments_prj_attachment_"+flag+length+"_file_id").val(fileId);
        $("#_prj_attachments_prj_attachment_"+flag+length+"_seq_no").val(length);
        $("#_prj_attachments_prj_attachment_"+flag+length+"_file_ext").val(fileType);
        $("#_prj_attachments_prj_attachment_"+flag+length+"_file_name").val(fileName);
        $("#_prj_attachments_prj_attachment_"+flag+length+"_file_name").
          closest(".prj_attachment_dev").find(".dev_prj_name_show a").html(fileName) ;
        $("#_prj_attachments_prj_attachment_"+flag+length+"_file_name").
        closest(".prj_attachment_dev").find(".dev_prj_name_show a").attr("title",fileName);
        // BaseUtils.fileDown('/scmwebsns/archiveFiles/downLoadNoVer.action?fdesId=JvUzHyT7%252BGLg2%252Bv4oFtHpA%253D%253D',this,event);
        $("#_prj_attachments_prj_attachment_"+flag+length+"_file_name").
        closest(".prj_attachment_dev").find(".dev_prj_name_show").attr("onclick",
            "ProjectEnter.link('"+downloadUrl+"',this,event);");
    }
    }else{
        scmpublictoast(data.failFileNames, 2000);
    }

}




/**
 * 2(不允许)---》0 运行
 * 
 * @param obj
 */
ProjectAttachment.setPermission = function (obj) {
    return ;
    var val = $(obj).find("input").val();
    if(val == "2"){
        $(obj).removeClass("selected-func_close");
        $(obj).addClass("selected-func_close-open");
        $(obj).find("input").val("0");
        $(obj).attr("title",ProjectEnter.public);
    }else{
        $(obj).addClass("selected-func_close");
        $(obj).removeClass("selected-func_close-open");
        $(obj).find("input").val("2");
        $(obj).attr("title",ProjectEnter.onlyMe);
    }
};

/**
 * 删除附件 替换模板字符，同时设置序号，包括重新排[xx]中的顺序
 * 
 * @param obj
 */
ProjectAttachment.delPrjAttachment = function (obj) {
    // 删除当前div
    if(obj != undefined){
        $(obj).closest(".prj_attachment_dev").remove();
    }
    $("#prj_attachment_list").find(".prj_attachment_dev").each(function(i){
        ProjectAttachment.modifyPrjAttachmentField(this,i);
    });

}

/**
 * 修改对象的字段, 某节点的所有字段
 * 
 * @param obj
 *          document对象
 * @param 序号
 *          0：表示修改第一条数据
 */
ProjectAttachment.modifyPrjAttachmentField = function (obj ,i) {
    $(obj).find(".prj_attachment_seq_dev").html(i+1);
    $(obj).find("*").each(function(){
        // 序号
        var num = $.fn.setNumberLen(i+1 ,2);

        strName = $(this).attr("name");
        if(strName) {
            strName = strName.replace(":","@");
            var atIndex = strName.indexOf("@");
            if(atIndex >= 0) {
                strName = strName.replace(/\[\d*\]\/\@/g,"["+ num +"]\/@");
                // 重新定位
                atIndex = strName.indexOf("@");
                var strAttr = strName.substring(atIndex+1,strName.length);
                $(this).attr("name",strName);
            } else {
                strAttr = strName;
            }
        }
        strId =  $(this).attr("id");
        if(strId) {
            strAttr = strId.replace(":","@");
            strAttr = strAttr.replace(/\[\d*\]\/\@/g,"["+ num +"]\/@");
            strAttr = strAttr.replace(/_\d+_/g,"_"+ num +"_");
            strAttr = strAttr.replace(/[\@\/\[\]]/g,"_");
            strAttr = strAttr.replace(/[_]+/g,"_");
            $(this).attr("id",strAttr);
        }
    });
}


ProjectAttachment.addNewLine = function () {
    var prjAttachmentNode = $(".prj_attachment_template").clone(true);
    prjAttachmentNode.css("display","");
    prjAttachmentNode.removeClass("prj_attachment_template");
    var length = $("#prj_attachment_list").find(".prj_attachment_dev").length;
    ProjectAttachment.modifyPrjAttachmentField(prjAttachmentNode[0],length)
    $("#prj_attachment_list").append(prjAttachmentNode);
}
// selected-func_up-none






// ********************************************* 项目全文****start

/**
 * 设置全文权限 0（所有人）---》2 (仅本人)
 * 
 * @param obj
 */
ProjectFulltext.changFulltextPermission = function (obj) {
    return  ;
    var permission = $("#fulltext_permission").val();
    if(permission == "0"){
        $("#fulltext_permission").val("2");
        $(obj).removeClass("selected-func_close-open");
        $(obj).attr("title",ProjectEnter.onlyMe)
    }else{
        $("#fulltext_permission").val("0");
        $(obj).addClass("selected-func_close-open");
        $(obj).attr("title",ProjectEnter.public);
    }
}

/**
 * 删除全文
 */
ProjectFulltext.deleteFulltext = function (){
    $("#upload_fulltext_view").css("display","");
    $("#upload_fulltext_content").css("display","none");
    // 重新初始化
    ProjectFulltext.initialization($("#upload_fulltext_view")[0]);
    ProjectFulltext.clearFulltextInfo();

}


// 初始化上传控件
ProjectFulltext.initialization = function(obj) {
    var data = {
        "fileurl" : "/fileweb/fileupload",
        "filedata" : {
            fileDealType : "generalfile"
        },
        "method" : "nostatus",
        "nonsupportType" : ["js","jsp","php"],
        "filecallback" : ProjectFulltext.upfileBack,// 回调函数
        "filecallbackparam" : {
            "obj" : obj,
            "fileType" : $(obj).attr("fileType")
        }
        
    };
    fileUploadModule.initialization(obj, data);
};
/**
 * 上传全文回调
 */
ProjectFulltext.upfileBack = function (xhr,params){
    const data = eval('(' + xhr.responseText + ')');
    if(data.result == "success"){
        var fileId = data.fileId;
        var fileName = data.fileName;
        var downloadUrl = data.extendFileInfo[0].downloadUrl;
        var createTime = data.createTime;
        var fileType = data.extendFileInfo[0].fileType;

        $("#_prj_fulltext_file_id").val(fileId);
        $("#_prj_fulltext_upload_date").val(createTime);
        $("#_prj_fulltext_file_ext").val(fileType);
        $("#_prj_fulltext_file_name").val(fileName);
        $("#fulltext_permission").val("2");

        $("#fulltext_link").html(fileName);
        $("#fulltext_link").attr("title",fileName);
        $("#fulltext_link").attr("onclick","ProjectEnter.link('"+downloadUrl+"',this,event)");

        $("#upload_fulltext_content").css("display","");
        // 重新初始化
        var fileuploadNode = $(".dev_pubfulltext_upload__box").clone(true);
        fileuploadNode.removeClass("dev_pubfulltext_upload__box");
        fileuploadNode.css("display","");
        $("#upload_fulltext_view").find(".upfile").html("");
        $("#upload_fulltext_view").find(".upfile").append(fileuploadNode);
        $("#upload_fulltext_view").css("display","none");
        ProjectAttachment.initialization($("#upload_fulltext_view")[0]);

        // 默认为隐私
        $("#jsFullTextPic").click();

    }

}


// 点击上传
ProjectFulltext.fileuploadBoxOpenInputClick = function(ev) {
    var $this = $(ev.currentTarget);
    $this.find('input.fileupload__input').click();
    ev.stopPropagation();// 阻止事件的向上传播
    return;
};
// 重新上传全文
ProjectFulltext.resetUpFulltext = function(obj) {
    // 重新初始化
    if($(obj).attr("can") == "true"){
        ProjectFulltext.initialization($("#upload_fulltext_view")[0]);
        $("#upload_fulltext_view").find(".fileupload__input").click();
        $(obj).attr("can","false");
        setTimeout( function () {
            $(obj).attr("can","true");
        },5000);
    }

}
ProjectFulltext.clearFulltextInfo = function () {
    $("#_prj_fulltext_node_id").val("");
    $("#_prj_fulltext_file_id").val("");
    $("#_prj_fulltext_upload_date").val("");
    $("#_prj_fulltext_file_ext").val("");
    $("#_prj_fulltext_ins_id").val("");
    $("#_prj_fulltext_file_name").val("");
    $("#fulltext_permission").val("");
}


// ********************************************* 项目全文****end


/**
 * 项目日期初始化
 */
ProjectEnter.initProjectDate = function () {

    var start_year =  $("#_project_start_year").val();
    var start_month =  $("#_project_start_month").val();
    var start_day =  $("#_project_start_day").val();

    var startDate = "";
    startDate = $.trim(start_year) != "" ? startDate+ start_year : startDate;
    startDate = $.trim(start_month) != "" ? startDate+"-"+start_month : startDate;
    startDate = $.trim(start_day) != "" ? startDate+"-"+ start_day : startDate;

     $("#projectStartDate").val(startDate) ;
    var end_year =  $("#_project_end_year").val();
    var end_month =  $("#_project_end_month").val();
    var end_day =  $("#_project_end_day").val();
    var endDate = "";
    endDate = $.trim(end_year) != "" ? endDate+ end_year : endDate;
    endDate = $.trim(end_month) != "" ? endDate+"-"+end_month : endDate;
    endDate = $.trim(end_day) != "" ? endDate+"-"+ end_day : endDate;

    $("#projectEndDate").val(endDate) ;
}


/**
 * 处理日期
 */
ProjectEnter.dealProjectDate = function () {
    /*
     * if( $("#projectStartDate").attr("date-year") != ""){ $("#_project_start_year").val(
     * $("#projectStartDate").attr("date-year"));
     * $("#_project_start_month").val($("#projectStartDate").attr("date-month"));
     * $("#_project_start_day").val($("#projectStartDate").attr("date-date")); }
     * 
     * if($("#projectEndDate").attr("date-year") != ""){ $("#_project_end_year").val(
     * $("#projectEndDate").attr("date-year"));
     * $("#_project_end_month").val($("#projectEndDate").attr("date-month"));
     * $("#_project_end_day").val($("#projectEndDate").attr("date-date")); }
     */
    var  startDate = $("#projectStartDate").val();
    if(startDate == ""){
        $("#_project_start_year").val("");
        $("#_project_start_month").val("");
        $("#_project_start_day").val("");
    }else{
        $("#_project_start_year").val(ProjectEnter.getYear(startDate));
        $("#_project_start_month").val(ProjectEnter.getMonth(startDate));
        $("#_project_start_day").val(ProjectEnter.getDay(startDate));
    }
    var endDate = $("#projectEndDate").val();
    if(endDate == ""){
        $("#_project_end_year").val( "");
        $("#_project_end_month").val("");
        $("#_project_end_day").val("");
    }else{
        $("#_project_end_year").val( ProjectEnter.getYear(endDate));
        $("#_project_end_month").val(ProjectEnter.getMonth(endDate));
        $("#_project_end_day").val(ProjectEnter.getDay(endDate));
    }

}
ProjectEnter.getYear = function (date) {
    var split = date.split("/");
    if(split.length >0){
        return split[0];
    }
}
ProjectEnter.getMonth = function (date) {
    var split = date.split("/");
    if(split.length >1){
        return split[1];
    }
};
ProjectEnter.getDay = function (date) {
    var split = date.split("/");
    if(split.length >2){
        return split[2];
    }
}


/**
 * 资助机构 中文
 */
/**
 * 查找机构 var param = { "q":"", "limit":10, "excludeParam":"", "agencyId":"", "lang":"zh_CN", }
 */
ProjectEnter.SchemeAgencyParamZH = function () {
    var  param = {
        "limit":10,
        "agencyId":$("#_project_scheme_agency_id").val(),
        "lang":"zh_CN",
    }
    return param ;
}
// 机构回调
ProjectEnter.SchemeAgencyCallZH = function (data) {
    var code = $("#_project_scheme_agency_name").attr("code");
    if(code != ""){
        $("#_project_scheme_agency_id").val(code);
    }
}
// 类别回调
ProjectEnter.SchemeCallZH = function (data) {
    var code = $("#_project_scheme_name").attr("code");
    if(code != ""){
        $("#_project_scheme_id").val(code);
    }
}

/**
 * 资助机构 英文
 */
/**
 * 查找机构 var param = { "q":"", "limit":10, "excludeParam":"", "agencyId":"", "lang":"en_US", }
 */
ProjectEnter.SchemeAgencyParamEN = function () {
    var  param = {
        "limit":10,
        "agencyId":$("#_project_scheme_agency_en_id").val(),
        "lang":"en_US",
    }
    return param ;
}
// 机构回调
ProjectEnter.SchemeAgencyCallEN = function (data) {
    var code = $("#_project_scheme_agency_name_en").attr("code");
    if(code != ""){
        $("#_project_scheme_agency_en_id").val(code);
    }
}
// 类别回调
ProjectEnter.SchemeCallEN = function (data) {
    var code = $("#_project_scheme_name_en").attr("code");
    if(code != ""){
        $("#_project_scheme_en_id").val(code);
    }
}


// ******************************************

// 添加科技领域
ProjectEnter.showDisciplineBox = function(areaSelect) {
    var discipline = $("#_project_discipline_id").val();

    $.ajax({
        url : "/prjweb/prj/ajaxadddisciplines",
        type : "post",
        dataType : "html",
        data : {
            "discipline" : discipline
        },
        success : function(data) {
            $("#login_box_refresh_currentPage").val("false");// 登录不跳转
            BaseUtils.ajaxTimeOut(data , function(){
                $("#disciplineBox").html(data);
                showDialog("disciplineBox");
                addFormElementsEvents(document
                    .getElementById("research-area-list"));
                $("#areaSelect").val(areaSelect);
               var areaid =  $("#choosed_area_list").find(".main-list__item").attr("areaid");
               if(areaid){
                   $("#checked_area_"+areaid).closest(".nav-cascade__section").find(".list_toggle-off").click();
               }
            });
        },
        error : function() {
        }
    });
};

// 添加领域
ProjectEnter.showAreaBox = function(areaSelect) {
  var areaIds = $("#_project_discipline_id").val();

  $.ajax({
      url : "/psnweb/sciencearea/ajaxaddprjarea",
      type : "post",
      dataType : "html",
      data : {
        "scienceAreaIds": areaIds
      },
      success : function(data) {
          $("#login_box_refresh_currentPage").val("false");// 登录不跳转
          BaseUtils.ajaxTimeOut(data , function(){
              $("#disciplineBox").html(data);
              showDialog("disciplineBox");
              addFormElementsEvents(document
                  .getElementById("research-area-list"));
              $("#areaSelect").val(areaSelect);
             var areaid =  $("#choosed_area_list").find(".main-list__item").attr("areaid");
             if(areaid){
                 $("#checked_area_"+areaid).closest(".nav-cascade__section").find(".list_toggle-off").click();
             }
          });
      },
      error : function() {
      }
  });
};

ProjectEnter.addDiscipline = function(id) {
    var key = $("#" + id + "_category_title").html();
    var sum = $("#choosed_area_list").find(".main-list__item").length;
    if (sum < 1) {
        var html = '<div class="main-list__item" style="padding: 0px 16px!important;" areaid = "'
            + id
            + '" >'
            + '<div class="main-list__item_content">'
            + key
            + '</div>'
            + '<div class="main-list__item_actions"  onclick="javascript:ProjectEnter.delDiscipline(\''
            + id + '\');"><i class="material-icons">close</i></div></div>';
        $("#choosed_area_list").append(html);
        $("#unchecked_area_" + id).attr("onclick", "");
        $("#unchecked_area_" + id).attr("id", "checked_area_" + id);
        $("#" + id + "_status").html("check");
        $("#" + id + "_status").css("color", "forestgreen");
    } else {
        // 出提示语
        scmpublictoast(ProjectEnter.selectOneDis, 1500);
    }
};
ProjectEnter.delDiscipline = function(id) {
    $("#choosed_area_list").find(".main-list__item[areaid='" + id + "']")
        .remove();
    $("#checked_area_" + id).attr("onclick",
        "ProjectEnter.addDiscipline('" + id + "')");
    $("#checked_area_" + id).attr("id", "unchecked_area_" + id);
    $("#" + id + "_status").html("add");
    $("#" + id + "_status").css("color", "");
};
// 保存科技领域
ProjectEnter.saveDiscipline = function() {

    setTimeout(function() {
        var length = $("#choosed_area_list").find(".main-list__item").length;
        if(length == 0){
            $("#_project_discipline_id").val("");
            $("#_project_discipline_id_1").val("");
        }
        $("#choosed_area_list").find(".main-list__item").each(function(index) {
            var id = $(this).attr("areaid");
            var keyName = $(this).find(".main-list__item_content").html();
            $("#_project_discipline_id").val(id);
            $("#_project_discipline_id_1").val(keyName);
        });

    }, 100);
    hideDialog("disciplineBox");
};
// 隐藏
ProjectEnter.hideDiscipline = function(obj) {
    hideDialog("disciplineBox");
};

/**
 * 返回
 */
ProjectEnter.back = function () {
    BaseUtils.checkCurrentSysTimeout("/dynweb/ajaxtimeout",ProjectEnter.backPrj , 0);
};
ProjectEnter.backPrj = function () {
    window.location.href = "/pub/prj/backurl";
}
/**
 * 保存
 */
ProjectEnter.save = function (obj) {
    ProjectEnter.dealAgency();
    ProjectEnter.dealKeyItem();
    ProjectEnter.dealEnKeyItem();
    ProjectEnter.dealProjectDate();
    var b = ProjectEnter.checkProjectData();
    if(b){
        // 防重复点击
        BaseUtils.doHitMore(obj,5000);
        $("#save_success_msg").val(ProjectEnter.saveOk);
        BaseUtils.checkCurrentSysTimeout("/dynweb/ajaxtimeout",ProjectEnter.savePjr , 0);
    }else{

    }

}


ProjectEnter.savePjr = function() {
    $.ajax({
        // 几个参数需要注意一下
        type: "POST",// 方法类型
        dataType: "json",// 预期服务器返回的数据类型
        url: "/prjweb/prj/save" ,// url
        data: $('#mainform').serialize(),
        success: function (data) {
            console.log(data);// 打印服务端返回的数据(调试用)
            if (data.result == "success") {
                $("#des3PrjId").val(data.des3PrjId);
                ProjectEnter.showDailog();
            }else if (data.result == "noPermission"){
                scmpublictoast(ProjectEnter.notYour, 2000);
            }else if(data.title == "blank"){
                $("#_project_zh_title").val("");
                $("#_project_en_title").val("");
                ProjectEnter.checkProjectData();
            }else if(data.insName == "blank"){
                $("#_project_ins_name").val("");
                ProjectEnter.checkProjectData();
            }else{
                scmpublictoast(ProjectEnter.error, 2000);
            }
        },
        error : function() {
            alert(ProjectEnter.error);
        }
    });
};

/**
 * 检查内容的完整性
 * 
 * @returns {boolean}
 */
ProjectEnter.valid = function (){
    if($("#_prj_fulltext_file_name").val() == ""){
        return false ;
    }
    if($("#_project_zh_abstract").val() == "" && $("#_project_en_abstract").val() == ""){
        return false ;
    }
    if($("#_project_zh_keywords").val() == "" && $("#_project_en_keywords").val() == ""){
        return false ;
    }
    var agencyZh = false ;
    var agencyEn = false ;
    if($("#_project_scheme_agency_name").val() != "" && $("#_project_scheme_name").val() != ""){
        agencyZh = true ;
    }
    if($("#_project_scheme_agency_name_en").val() != "" && $("#_project_scheme_name_en").val() != ""){
        agencyEn  = true ;
    }
    if(!agencyZh && !agencyEn){
        return false ;
    }
    if($("#_project_discipline_id").val() == ""){
        return false ;
    }
    if($("#_project_ins_name").val() == ""){
        return false ;
    }
    if($("#_project_funding_year").val() == ""){
        return false ;
    }
    if($("#_project_amount").val() == ""){
        return false ;
    }
    if($("#projectStartDate").val() == "" || $("#projectEndDate").val() == ""){
        return false ;
    }
    if($("#_project_remark").val() == ""){
        return false ;
    }
    if($("#_prj_fulltext_fulltext_url").val() == ""){
        return false ;
    }
    var complete = true ;
    $("#projectMemberList").find(".prj_member_dev").each(function(){
        var ins_name = $(this).find("input[name$='ins_name1']").val();
        var email = $(this).find("input[name$='email']").val();
        if(ins_name == "" || email == ""){
            complete =  false ;
        }
    });
    return complete ;
}


ProjectEnter.showDailog = function () {

    $("#show_img").attr("src", $("#successImg").val());
    $("#show_msg").html($("#save_success_msg").val());
    $("#returnBottom").html(ProjectEnter.backlist).attr("onclick", "ProjectEnter.viewPrj(this);");
    $("#continueBottom").html(ProjectEnter.continue).attr("onclick",
        "ProjectEnter.continueEdit();");
    $("#resultMsg").show();
    ProjectEnter.positioncenter({
        targetele : 'new-success_save',
        closeele : 'new-success_save-header_tip'
    });
};

ProjectEnter.viewPrj = function(obj) {
    // window.location.href = "/prj/backurl";
    window.location.href = "/psnweb/homepage/show?module=prj";
};
ProjectEnter.continueEdit = function(obj) {
    var url = window.location.href;
    if (url.search("prj/enter")>-1) {
        url = "/prjweb/prj/edit?des3Id="+$("#des3PrjId").val();
    }
    window.location.href = url;
};
ProjectEnter.positioncenter = function(options) {
    var defaults = {
        targetele : "",
        closeele : ""
    };
    var opts = Object.assign(defaults, options);
    if ((opts.targetele == "") || (opts.closeele == "")) {
        alert("为传入必须的操作元素");
    } else {
        var postarget = document.getElementsByClassName(opts.targetele)[0];
        var opstclose = document.getElementsByClassName(opts.closeele);
        postarget.closest(".background-cover").style.display = "block";
        setTimeout(
            function() {
                postarget.style.left = (window.innerWidth - postarget.offsetWidth)
                    / 2 + "px";
                postarget.style.bottom = (window.innerHeight - postarget.offsetHeight)
                    / 2 + "px";
            }, 300);
        window.onresize = function() {
            postarget.style.left = (window.innerWidth - postarget.offsetWidth)
                / 2 + "px";
            postarget.style.bottom = (window.innerHeight - postarget.offsetHeight)
                / 2 + "px";
        }
        for (var i = 0; i < opstclose.length; i++) {
            opstclose[i].onclick = function() {
                this.closest("." + opts.targetele).style.bottom = -600 + "px";
                setTimeout(
                    function() {
                        postarget.closest(".background-cover").style.display = "none";
                    }, 300);
            }
        }

    }
};
/**
 * 检查数据
 * 
 * @returns {boolean}
 */
ProjectEnter.checkProjectData = function () {
    $("#mainform").find("*").removeClass("error_import-tip_border-warn");
    var titleFlag = true ;
    var insFlag = true ;
    // 检查空数据
    var zhTitle = $("#_project_zh_title").val();
    var enTitle = $("#_project_en_title").val();
    if($.trim(zhTitle) == "" &&  $.trim(enTitle) == ""){
        scmpublictoast(ProjectEnter.titleNotBlank, 1500);
        $("#_project_zh_title").focus();
        window.scrollTo(0,0);
        $("#_project_zh_title").closest("div").addClass("error_import-tip_border-warn");
        titleFlag = false ;
    }

    var insName = $("#_project_ins_name").val();
    if($.trim(insName) == ""){
        if(titleFlag) {
            window.scrollTo(0,500);
            $("#_project_ins_name").focus();
            scmpublictoast(ProjectEnter.institutionNotBlank, 1500);
        }
        $("#_project_ins_name").closest("div").addClass("error_import-tip_border-warn");
        insFlag = false ;
    }

    // 检查日期
    var date = ProjectEnter.listenerPrjDate();
    if(!date){
        return false ;
    }
    // 检查状态
    var state = ProjectEnter.checkPrjState();
    if(!state){
        return false ;
    }
    // 检查金额
    var amount = $("#_project_amount").val();
    if(amount != "") {
      amount=amount.replace(/\s+/g,"");// 去除空格
        var ret = /^[1-9]+[0-9]*?$/g.test(amount);
        if(!ret){
            $("#_project_amount").closest("div").addClass("error_import-tip_border-warn");
            scmpublictoast(ProjectEnter.moneyFormateError, 1500);
            return false ;
        }
    }
    // 检查成员信息
    $("#projectMemberList").find(".prj_member_dev").each(function (i) {
        // 检查邮箱
        var email = $(this).find("input[name$='email']").val();
        if(email != undefined && email != "" && !BaseUtils.checkEmail(email)){
            $(this).find("input[name$='email']").closest("span").addClass("error_import-tip_border-warn");
        }
    });

    var  index = 0;
    $("#projectMemberList").find(".prj_member_dev").each(function(i){
        var name = $(this).find("input[name$='member_psn_name']").val();
        if($.trim(name) == ""){
            $(this).find("input").attr("name","");
        }else{
            ProjectMember.modifyPrjMemberField(this,index);
            index ++ ;
        }
    });
    // 检查成员信息 end
    return titleFlag && insFlag ;
}



/**
 * 监听日期
 */
ProjectEnter.listenerPrjDate = function (obj) {
     // 检查年份
    console.log("listenerPrjDate");
    ProjectEnter.dealProjectDate();
    $("#projectEndDate").closest("div").removeClass("error_import-tip_border-warn");
    $("#projectStartDate").closest("div").removeClass("error_import-tip_border-warn");
     var end_year = $("#_project_end_year").val();
     var end_month = $("#_project_end_month").val();
     var end_day = $("#_project_end_day").val();
     var start_year = $("#_project_start_year").val();
     var start_month = $("#_project_start_month").val();
     var start_day = $("#_project_start_day").val();
     if($.trim(start_year) != "" && $.trim(end_year) != ""){
         var   sYear = new Date(start_year, start_month, start_day);
         var   eYear = new Date(end_year, end_month, end_day);
         if(sYear   > eYear){
             $("#projectEndDate").closest("div").addClass("error_import-tip_border-warn");
             scmpublictoast(ProjectEnter.prjDateError, 1500);
             return false ;
         }
     }
     if($.trim(start_year) == "" && $.trim(end_year) != ""){
         $("#projectStartDate").closest("div").addClass("error_import-tip_border-warn");
         scmpublictoast(ProjectEnter.startDateBlank, 1500);
         return false ;
     }
     return true ;
}

ProjectEnter.checkPrjState = function (obj) {
    return true ;
    var end_year = $("#_project_end_year").val();
    var end_month = $("#_project_end_month").val();
    var end_day = $("#_project_end_day").val();
    var start_year = $("#_project_start_year").val();
    var start_month = $("#_project_start_month").val();
    var start_day = $("#_project_start_day").val();
    // "01"= 进行中 ； "02"= 完成
    var prjIng = "01";
    var prjEnd = "02";
    var prjState = $("#_project_prj_stat_h").val();
    if($.trim(end_year) != ""){
        var   nowY = new Date();
        if(end_month !=""){
            end_month = end_month -1 ;
        }

        var   eYear = new Date(end_year, end_month, end_day,23,59,59);
        if(nowY > eYear && prjState ==prjIng ){   // 项目已经结束
             scmpublictoast(ProjectEnter.prjStateError, 1500);
             return false ;
        }

    }
    return true ;
}

ProjectEnter.listenerAmount = function () {
    $("#_project_amount").bind("input",function () {
        var val = $("#_project_amount").val();
        val = val.replace(/\s+/g,"");// 去除空格
        $("#_project_amount").val(val);
        if(val != ""){
            var ret = /^[1-9]+[0-9]*?$/g.test(val); 
            if(!ret){
                $(this).closest("div").addClass("error_import-tip_border-warn");
            }else{
                $(this).closest("div").removeClass("error_import-tip_border-warn");
            }
        }else{
            $(this).closest("div").removeClass("error_import-tip_border-warn");
        }
    })
}

ProjectEnter.listenerEmail = function () {
    $("input[name$='email']").on("change",function () {
        var val = $(this).val();
        if(val != "" && !BaseUtils.checkEmail(val)){
            $(this).closest("span").addClass("error_import-tip_border-warn");
        }else{
            $(this).closest("span").removeClass("error_import-tip_border-warn");
        }
    })
};

ProjectEnter.link = function(url ,e) {
    e = window.event || e;
    location.href = url;
    if(window.event){window.event.returnValue = false;} else {e.preventDefault();}// for firefox

};
