var PubEdit = PubEdit ? PubEdit : {}

PubEdit.save = function(obj) {
    // 防重复点击
    BaseUtils.doHitMore(obj, 10000);
    if ($(".json_title").valid()) {
        if (!PubEdit.valid()) {
            $("#save_success_msg").val(Enter.saveIncomplete);
        } else {
            $("#save_success_msg").val(Enter.saveOk);
        }
        var pubJson = pub.getJson();
        $.ajax({
            url: "/pub/enter/savepub",
            type: "post",
            dataType: "json",
            data: {
                "jsonData": pubJson
            },
            timeout: 10000,
            success: function(data) {
                if (data.status == "isDel") {
                    scmpublictoast(Enter.pubIsDeleted, 1000);
                } else {
                    $(".json_des3PubId").val(data.des3PubId);
                    PubEdit.showDailog();
                }
            },
            error: function() {
                $("#save_msg").text(Enter.error);
                PubEdit.showDailog("error");
            }
        });
    } else {
        scmpublictoast($(".json_title").attr("title_msg") + Enter.notEmpty, 1500);
    }
}
;
PubEdit.valid = function() {
    var pubType = $(".json_pubType").val();
    var area = $(".json_patent_area").val();
    if (pubType != '5') {
        var result = false;
        $(".json_member_name").each(function() {
            if ($(this).val() != "") {
                if ($(this).valid()) {
                    result = true;
                }
            }
        });
        if (result == false) {
            return false;
        }
        return $("#enterPubForm").valid();
    } else {
        if (!$(".json_title").valid()) {
            return false;
        }
        if (!$(".json_patent_area").valid()) {
            return false;
        }
        if (area == "OTHER") {
            if (!$(".json_patent_type").valid()) {
                return false;
            }
        }
        if (!$(".json_fulltext_fileName").valid()) {
            return false;
        }
        if (!$(".json_patent_status").valid()) {
            return false;
        }
        if (!$(".json_patent_applicationNo").valid()) {
            return false;
        }
        if (!$(".json_patent_applicationDate").valid()) {
            return false;
        }
        var status = $(".json_patent_status").val();
        if (status == '1') {
            if (!$(".json_patent_publicationOpenNo").valid()) {
                return false;
            }
            if (!$(".json_patent_startDate").valid()) {
                return false;
            }
            if (!$(".json_patent_endDate").valid()) {
                return false;
            }
        }
        var result = false;
        $(".json_member_name").each(function() {
            if ($(this).val() != "") {
                if ($(this).valid()) {
                    result = true;
                }
            }
        });
        if (result == false) {
            return false;
        }
        var result_member = false;
        $(".json_member_email").each(function() {
            if ($(this).val() != "") {
                if ($(this).valid()) {
                    result_member = true;
                }
            }
        });
        if (result_member == false) {
            return false;
        }

        return true;
    }
}
;
PubEdit.change = function() {
    $(".json_pubType").val($("#changTypeName").val());
    // 返回原来的类型 在填充页面需要的字段
    var des3pdwhPubId = $("#returnOldType").val();
    if ($(".json_pubType").val() == $(".json_oldPubType").val()) {
        // 切换原来的类型
        if (des3pdwhPubId != "") {
            window.location.href = "/pub/autoFillPub?des3pdwhPubId=" + des3pdwhPubId + "isChangeType=1";
            return;
        } else {
            window.location.href = "/pub/enter?des3PubId=" + $(".json_des3PubId").val();
            return;
        }
    } else {
        var pubJson = pub.getJson(1);
    }
    var temp_form = document.createElement("form");
    temp_form.action = window.location.href;
    // 如需打开新窗口，form的target属性要设置为'_blank'
    temp_form.target = "_self";
    temp_form.method = "post";
    temp_form.style.display = "none";
    // 添加参数
    var opt = document.createElement("input");
    var opt2 = document.createElement("input");
    // 是否是切换类型 解决切换类型之后 作者没有保存
    var opt3 = document.createElement("input");
    opt.name = "pubJson";
    opt.value = pubJson;
    if (des3pdwhPubId != "") {
        opt2.name = "des3pdwhPubId";
        opt2.value = des3pdwhPubId;
    }
    opt3.name = "isChangeType";
    opt3.value = 1;
    temp_form.appendChild(opt);
    temp_form.appendChild(opt2);
    temp_form.appendChild(opt3);
    document.body.appendChild(temp_form);
    // 提交数据
    temp_form.submit();
}
// 成果编辑页面切换成果类型
PubEdit.changePubType = function(obj) {
    var $this = $(obj);
    var typeId = $this.attr("typeId");
    // TODO 收集数据，构建json，提交
}
;

PubEdit.deletetarget = function(obj) {
    if ($(obj).hasClass("selected-func_nodelete")) {
        // 灰色的就不删除
        return;
    }
    obj.closest(".handin_import-content_container-right_author-body").remove();
    PubEdit.publicdelete();
    if (!$(".json_member")) {
        // 保证3个记录的
        start = 0;
    } else if ($(".json_member").length < 3) {
        addmenber();
    }
}
;
PubEdit.upchange = function(obj) {
    var $nowNode = $(obj).closest(".json_member");
    var $prevNode = $nowNode.prev(".json_member");
    var $nowNode1 = $nowNode.clone(true);
    var $prevNode1 = $prevNode.clone(true);

    $prevNode.replaceWith($nowNode1);
    $nowNode.replaceWith($prevNode1);
    PubEdit.publicdelete();
    PubEdit.changeAddMenber();
    PubEdit.addMenberInputOn();
    window.addFormElementsEvents();
    // 绑定下拉选择
}
;
PubEdit.downchange = function(obj) {
    var $nowNode = $(obj).closest(".json_member");
    var $nextNode = $nowNode.next(".json_member");
    var $nowNode1 = $nowNode.clone(true);
    var $nextNode1 = $nextNode.clone(true);

    $nextNode.replaceWith($nowNode1);
    $nowNode.replaceWith($nextNode1);
    PubEdit.publicdelete();
    PubEdit.changeAddMenber();
    PubEdit.addMenberInputOn();
    window.addFormElementsEvents();
    // 绑定下拉选择
}
;
PubEdit.changeAddMenber = function() {
    $(".selected-func_delete").removeClass("selected-func_nodelete");
    var obj = $(".json_member:eq(-2)");
    var last = $(".json_member:last");
    if (PubEdit.menberInputIsEmpty(obj) && $(".json_member").length > 3 && PubEdit.menberInputIsEmpty(last)) {
        // 倒数第二个为空删除掉最后一个
        PubEdit.deletetarget($(".json_member:last").find(".selected-func_delete"));
    }
    if (!PubEdit.menberInputIsEmpty(last)) {// 最后一行不为空加一个
    // addmenber();
    }
    $(".selected-func_delete:last").addClass("selected-func_nodelete");
}
;
PubEdit.menberInputIsEmpty = function(obj) {
    var isEmpty = true;
    $(obj).find("input[type='text']").each(function() {
        // 判读是不是都为空
        if ($(this).val() != "") {
            isEmpty = false;
        }
    });
    return isEmpty;
}
PubEdit.publicdelete = function() {
    if (document.getElementsByClassName("selected-func_up-none").length > 0) {
        document.getElementsByClassName("selected-func_up-none")[0].classList.remove("selected-func_up-none");
    }
    if (document.getElementsByClassName("selected-func_down-none").length > 0) {
        document.getElementsByClassName("selected-func_down-none")[0].classList.remove("selected-func_down-none");
    }
    if (document.getElementsByClassName("selected-func_down") && document.getElementsByClassName("selected-func_up")) {
        var length = document.getElementsByClassName("selected-func_down").length;
        if (document.getElementsByClassName("selected-func_up")[0]) {
            document.getElementsByClassName("selected-func_up")[0].classList.add("selected-func_up-none");
        }
        if (document.getElementsByClassName("selected-func_down")[length - 1]) {
            document.getElementsByClassName("selected-func_down")[length - 1].classList.add("selected-func_down-none");
        }
    }
}
;
// 点击上传
PubEdit.fileuploadBoxOpenInputClick = function(ev) {
    var $this = $(ev.currentTarget);
    $this.find('input.fileupload__input').click();
    ev.stopPropagation();
    // 阻止事件的向上传播
    return;
}
;
// 初始化上传控件
PubEdit.bindASyncUpload = function() {
    var $drawerBatchBox = $(".upfile");
    $drawerBatchBox.each(function() {
        PubEdit.initialization(this);
    });
}
;
// 初始化上传控件
PubEdit.initialization = function(obj) {
    var multiple = "false";
    if ($(obj).attr("fileType") == "file") {
        multiple = "true";
    }
    var data = {
        "fileurl": "/fileweb/fileupload",
        "filedata": {
            fileDealType: "generalfile"
        },
        "method": "nostatus",
        "nonsupportType": ["js", "jsp", "php"],
        "filecallback": PubEdit.upfileBack,
        // 回调函数
        "filecallbackparam": {
            "obj": obj,
            "fileType": $(obj).attr("fileType"),
        },
        "multiple": multiple,
        "maxtip": "0"
    };
    fileUploadModule.initialization(obj, data);
}
;
// 回调函数
PubEdit.upfileBack = function(xhr, params) {
    const data = eval('(' + xhr.responseText + ')');
    if (data.successFiles >= 1) {
        var fileIdArray = data.successDes3FileIds.split(",");
        // 支持多文件上传
        for (var key = 0; key < fileIdArray.length; key++) {
            var des3fid = fileIdArray[key];
            /* var fileName = data.fileName.split(",")[0]; */
            var fileName = data.extendFileInfo[key].fileName;
            var downloadUrl = data.extendFileInfo[key].downloadUrl;
            var obj = params.obj;
            var fileType = params.fileType;
            var pubPermission = $(".json_permission").val();
            var item = "";
            if (fileType == "fulltext") {
                item = '<div class="handin_import-content_container-right_upload-item" >' + '<span class="handin_import-content_container-right_upload-item_detaile">' + '<a title="' + fileName + '" href="" onclick=PubEdit.link("' + downloadUrl + '",event);>' + fileName + '</a>' + '</span>' + '<i class="material-icons handin_import-content_upload-item_icon" onclick="PubEdit.deleteFileItem(this,\'' + fileType + '\')">close</i>' + '<input type="hidden" class="json_fulltext_des3fileId" name="fileId" value="' + des3fid + '">' + '<input type="hidden" class="json_fulltext_fileName" name="fileName" value="' + fileName + '">';
                if (pubPermission == 7) {
                    item += '<i id="jsFullTextPic" class="selected-func_close selected-func_close-open" onclick="PubEdit.changFulltextPermission(this)" title="' + Enter.public + '"></i>' + '<input type="hidden" class="json_fulltext_permission" id="permission" name="permission" value="0">';
                } else {
                    item += '<i id="jsFullTextPic" class="selected-func_close selected-func_close-none" onclick="PubEdit.changFulltextPermission(this)" title="' + Enter.privatePubfulltextTips + '"></i>' + '<input type="hidden" class="json_fulltext_permission" id="permission" name="permission" value="2">';
                }
                item += '<div class="handin_import-content_container-upload_again" onclick="PubEdit.resetUpFulltext()">' + Enter.reupload + '</div>' + '</div>';
                $(obj).hide();
                $(obj).parent().find(".dev_upfiled_list").empty();
                // SCM-22034 增加通过pdf解析填充成果功能 20181228
                // 成果为手工录入进来的且未被改动时，才触发pdf导入的操作
                var title = $(".json_title").val();
                if (title == "") {
                    PubEdit.importPdwhPubByPDF(data.extendFileInfo[0]);
                }
            } else {
                item = '<div class="handin_import-content_container-right_upload-item json_accessory">' + '<span class="handin_import-content_container-right_upload-item_detaile">' + '<a title="' + fileName + '" href="" onclick=PubEdit.link("' + downloadUrl + '",event);>' + fileName + '</a>' + '</span>' + '<i class="material-icons handin_import-content_upload-item_icon" onclick="PubEdit.deleteFileItem(this,\'' + fileType + '\')">close</i>' + '<input type="hidden" class="json_accessory_des3fileId" value="' + des3fid + '">' + '<input type="hidden" class="json_accessory_fileName" value="' + fileName + '">' + '<i class="selected-func_close selected-func_close-open" onclick="PubEdit.changAccessorysPermission(this)" title="' + Enter.public + '"></i>' + '<input type="hidden" class="json_accessory_permission" value="0">' + '</div>';
            }
            $(obj).parent().find(".dev_upfiled_list").append(item);
        }
    }
    PubEdit.resetFileUploadBox(obj);
}
;
/**
 * 通过解析pdf文件进行填充成果 只有当传入文件类型为pdf时才做填充的逻辑
 */
PubEdit.importPdwhPubByPDF = function(fileInfo) {
    var pubType = $("#pubType").val();
    if (pubType == "3" || pubType == "4" || pubType == "5") {
        // 此三种类型才进行pdf导入操作
        var fileType = fileInfo.fileType;
        var des3FileId = fileInfo.des3FileId;
        if (fileType != "" && fileType == 'pdf') {
            $.ajax({
                url: "/pub/enter/importpubbypdf",
                type: 'post',
                dataType: 'json',
                data: {
                    "des3FileId": des3FileId
                },
                success: function(data) {
                    if (data.result != "" && data.result == "SUCCESS") {
                        // 设置类型为PDF导入
                        $(".json_recordFrom").eq(0).val(8);
                        $("#des3FileId").val(des3FileId);

                        $("#titleDes3PdwhId").val(data.msg);
                        // 直接确认填充成果信息
                        PubEdit.confirmAutoFillPub();
                    }
                },
                error: function(data) {// 出错不需要提示
                }
            });
        }
    }
}
;

// 重置上传框
PubEdit.resetFileUploadBox = function(obj) {
    $(obj).find(".fileupload__core").removeClass("finish_shown").addClass("initial_shown");
    $(obj).find(".fileupload__box").removeClass("upload_finished");
    $(obj).find(".preloader").removeClass("green-style active").children().remove();
    $(obj).find(".fileupload__progress_text").text("");
    $(obj).find(".fileupload__input").val("");
    PubEdit.initialization(obj);
}
;
// 重新上传全文
PubEdit.resetUpFulltext = function() {
    $("#fulltextFileInput").click();
}
// 删除文件
PubEdit.deleteFileItem = function(obj, fileType) {
    var item = $(obj).closest(".handin_import-content_container-right").find(".upfile");
    if ($(obj).parent().parent().find(".json_fulltext_fileName")) {
        $(obj).parent().parent().find(".json_fulltext_fileName").val("");
    }
    $(obj).closest(".handin_import-content_container-right_upload-item").remove();
    if (fileType == "fulltext") {
        $(item).show();
    }
}
;
// 设置全文下载权限
PubEdit.changFulltextPermission = function(obj) {
    var pubPermission = $(".json_permission").val();
    if (pubPermission != '' && pubPermission == '4') {
        $(obj).removeClass("selected-func_close-open");
        $(".json_fulltext_permission").val("2");
        $(obj).addClass("item_disabled");
    } else {
        if ($(obj).hasClass("selected-func_close-open")) {
            BaseUtils.checkCurrentSysTimeout("/pub/ajaxtimeout", function() {
                smate.showTips._showNewTips(Enter.private_content, Enter.tips, "changeFulltextToPrivate()", "", Enter.confirm_btn, Enter.cancel_btn);
            }, 1);
        } else {
            $(obj).addClass("selected-func_close-open");
            $(".json_fulltext_permission").val("0");
            $(obj).attr("title", Enter.public);
        }
    }
}

function changeFulltextToPrivate() {
    $("#fullTextPic").removeClass("selected-func_close-open");
    $("#jsFullTextPic").removeClass("selected-func_close-open");
    $(".json_fulltext_permission").val("2");
    $("#fullTextPic").attr("title", Enter.onlyMe);
    $("#jsFullTextPic").attr("title", Enter.onlyMe);
    var pluginclose = document.getElementsByClassName("new-searchplugin_container-close")[0];
    if($(".new-searchplugin_container")){
        $(".new-searchplugin_container").remove();
     }
}
// 设置附件下载权限
PubEdit.changAccessorysPermission = function(obj) {
    if ($(obj).hasClass("selected-func_close-open")) {
        $(obj).removeClass("selected-func_close-open");
        $(obj).parent().find(".json_accessory_permission").val("2");
        $(obj).attr("title", Enter.onlyMe);
    } else {
        $(obj).addClass("selected-func_close-open");
        $(obj).parent().find(".json_accessory_permission").val("0");
        $(obj).attr("title", Enter.public);
    }
}
// 设置成果权限
PubEdit.changPubPermission = function(obj) {
    if ($(obj).hasClass("selected-func_close-open")) {
        BaseUtils.checkCurrentSysTimeout("/pub/ajaxtimeout", function() {
            smate.showTips._showNewTips(Enter.private_content, Enter.tips, "changePubToPrivate()", "", Enter.confirm_btn, Enter.cancel_btn);
        }, 1);
    } else {
        $(obj).addClass("selected-func_close-open");
        $(".json_permission").val("7");
        $(obj).attr("title", Enter.public);
        $("#fullTextPic").removeClass("selected-func_close-none");
        $("#jsFullTextPic").removeClass("selected-func_close-none");
        $("#fullTextPic").attr("title", Enter.onlyMe);
        $("#jsFullTextPic").attr("title", Enter.onlyMe);
    }
}

function changePubToPrivate() {
    $("#pubPermissionDiv").removeClass("selected-func_close-open");
    $(".json_permission").val("4");
    $("#pubPermissionDiv").attr("title", Enter.onlyMe);
    $("#fullTextPic").removeClass("selected-func_close-open");
    $("#fullTextPic").addClass("selected-func_close-none");
    $(".json_fulltext_permission").val("2");
    $("#fullTextPic").attr("title", Enter.privatePubfulltextTips);
    $("#jsFullTextPic").removeClass("selected-func_close-open");
    $("#jsFullTextPic").addClass("selected-func_close-none");
    $("#jsFullTextPic").attr("title", Enter.privatePubfulltextTips);
    var pluginclose = document.getElementsByClassName("new-searchplugin_container-close")[0];
    if($(".new-searchplugin_container")){
        //$(".background-cover").remove();
        $(".new-searchplugin_container").remove();
     }
}
// 地区
PubEdit.initArea = function() {
    if ($("#countryId").length <= 0 || $("#countryId").val() == "" || $("#countryId").val() == "0") {
        return;
    }
    $.ajax({
        url: "/psnweb/psninfo/ajaxRegion",
        type: 'post',
        dataType: 'json',
        data: {
            "superRegionId": $("#countryId").val()
        },
        success: function(data) {
            if (data.thirdRegionName != null) {
                var $selectBox = document.querySelector('.sel__box[selector-id="1st_area"]');
                $selectValue = $selectBox.querySelector("input");
                $selectValue.value = data.thirdRegionName;
                $selectBox.setAttribute("sel-value", data.secondCode);
                var $selectsecondBox = document.querySelector('.sel__box[selector-id="2nd_area"]');
                $selectValue = $selectsecondBox.querySelector("input");
                $selectValue.value = data.secondRegionName;
                $selectsecondBox.style.visibility = "visible";
                $selectsecondBox.setAttribute("sel-value", data.firstCode);
                var $selectthirdBox = document.querySelector('.sel__box[selector-id="3nd_area"]');
                $selectValue = $selectthirdBox.querySelector("input");
                $selectValue.value = data.firstRegionName;
                $selectthirdBox.style.visibility = "visible";
            } else if (data.secondRegionName != null) {
                var $selectBox = document.querySelector('.sel__box[selector-id="1st_area"]');
                $selectValue = $selectBox.querySelector("input");
                $selectValue.value = data.secondRegionName;
                $selectBox.setAttribute("sel-value", data.firstCode);
                var $selectsecondBox = document.querySelector('.sel__box[selector-id="2nd_area"]');
                $selectValue = $selectsecondBox.querySelector("input");
                $selectValue.value = data.firstRegionName;
                $selectsecondBox.setAttribute("sel-value", $("#countryId").val());
                $selectsecondBox.style.visibility = "visible";
                PubEdit.getNextLevelRegion("3nd_area");
            } else {
                if ($("#countryId").val() != "") {
                    var $selectBox = document.querySelector('.sel__box[selector-id="1st_area"]');
                    $selectValue = $selectBox.querySelector("input");
                    $selectBox.setAttribute("sel-value", data.firstRegionCode);
                    $selectValue.value = data.firstRegionName;
                    PubEdit.getNextLevelRegion("2nd_area");
                }
            }
        },
        error: function() {
        }
    });
}
;
// 判断是否有下一级地区
PubEdit.getNextLevelRegion = function(selectId) {
    $.ajax({
        url: "/psnweb/psninfo/ajaxGetNextLevelRegion",
        type: "post",
        dataType: "json",
        data: {
            "superRegionId": $(".json_countryId").val()
        },
        success: function(data) {
            if (data.nextLevel == 'true') {
                var $selectsecondBox = document.querySelector('.sel__box[selector-id="' + selectId + '"]');
                $selectsecondBox.style.visibility = "visible";
                $selectValue.classList.remove("sel__value_placeholder");
            }
        }

    });
}
PubEdit.getAreaJSON = function() {
    var $valueBox = document.querySelector('.sel__box[selector-id="1st_area"]');
    return {
        "superRegionId": $valueBox.getAttribute("sel-value")
    };
}
;
PubEdit.getSubAreaJSON = function() {
    var $valueBox = document.querySelector('.sel__box[selector-id="2nd_area"]');
    return {
        "superRegionId": $valueBox.getAttribute("sel-value")
    };
}
;
PubEdit.getRegionId = function() {
    var $valueBox = document.querySelector('.sel-dropdown__box[selector-data="3nd_area"]');
    $("#countryId").val($valueBox.querySelector('*[selected="selected"]').getAttribute("sel-itemvalue"));
}
;
// 显示二级地区
PubEdit.getArea = function() {
    var $valueBox = document.querySelector('.sel-dropdown__box[selector-data="1st_area"]');
    $("#countryId").val($valueBox.querySelector('*[selected="selected"]').getAttribute("sel-itemvalue"));
    var $selectBox = document.querySelector('.sel__box[selector-id="2nd_area"]');
    var $selectBox3 = document.querySelector('.sel__box[selector-id="3nd_area"]');

    if ($selectBox.style.visibility === "visible" && $(".sel__box[selector-id='1st_area']").attr("sel-nextlevel") != "true") {
        $selectBox.style.visibility = "hidden";
        $selectBox3.style.visibility = "hidden";
    }
    if ($selectBox.style.visibility === "hidden" && $(".sel__box[selector-id='1st_area']").attr("sel-nextlevel") == "true") {
        $selectBox.style.visibility = "visible";
    } else {
        $selectValue = $selectBox.querySelector("input");
        $selectValue.value = "";
        $selectValue.setAttribute("placeholder", Enter.secondRegion);
        $selectBox.setAttribute("sel-value", "");
        document.querySelector('.sel__box[selector-id="3nd_area"]').style.visibility = "hidden";

        $selectValue3 = $selectBox3.querySelector("input");
        $selectValue3.value = "";
        $selectValue3.setAttribute("placeholder", Enter.thirdRegion);
        $selectBox3.setAttribute("sel-value", "");
        document.querySelector('.sel__box[selector-id="3nd_area"]').style.visibility = "hidden";
    }
}
;
// 显示三级地区
PubEdit.getSubArea = function() {
    var $valueBox = document.querySelector('.sel-dropdown__box[selector-data="2nd_area"]');
    $("#countryId").val($valueBox.querySelector('*[selected="selected"]').getAttribute("sel-itemvalue"));
    var $selectBox = document.querySelector('.sel__box[selector-id="3nd_area"]');
    if ($selectBox.style.visibility === "visible" && $(".sel__box[selector-id='2nd_area']").attr("sel-nextlevel") != "true") {
        $selectBox.style.visibility = "hidden";
    }
    if ($selectBox.style.visibility === "hidden" && $(".sel__box[selector-id='2nd_area']").attr("sel-nextlevel") == "true") {
        $selectBox.style.visibility = "visible";
    } else {
        $selectValue = $selectBox.querySelector("input");
        $selectValue.value = "";
        $selectValue.setAttribute("placeholder", Enter.thirdRegion);
        $selectBox.setAttribute("sel-value", "");
    }
}
;
// 删除关键词
PubEdit.deleteKey = function(event) {
    var $obj = $(".new-importantkey_container .new-importantkey_container-item").last();
    var item = $obj.find(".new-importantkey_container-item_colse");
    PubEdit.deleteKeyElement(item);
}
;
// 删除关键词
PubEdit.deleteKeyElement = function(obj) {
    var item = $(obj).closest(".new-importantkey_container-item");
    var words = $(item).text();
    var length = $(".json_keyword").length;
    $(item).remove();
    if (length == 5) {
        $("#addKeyInput").show();
    }
}
;
// 添加关键字
PubEdit.addkey = function(event) {
    var addKeyList = $.trim($("#addKeyInput").val()).split(/[;；]/);
    var $jsonKey = $(".json_keyword");
    var keyLength = $jsonKey.length;
    for (var i = 0; i < addKeyList.length; i++) {
        var addKey = addKeyList[i];
        if (addKey && addKey != "" && keyLength < 5) {
            // 最多5个关键词
            var repeat = false;
            $jsonKey.each(function() {
                // 有相同的就不添加
                if ($.trim($(this).text()) == addKey) {
                    repeat = true;
                }
            })
            if (repeat) {
                $("#addKeyInput").val("");
                return;
            }
            if (addKey.toUpperCase() == "<IFRAME>") {
                $("#addKeyInput").val("");
                return;
            }
            PubEdit.addKeyElement(addKey);
            // 添加节点
            ++keyLength;
            if (keyLength == 5) {
                $("#addKeyInput").val("");
                $("#addKeyInput").hide();
            }
        }
    }
}
;
PubEdit.addKeyElement = function(addKey) {
    var element = '<div class="new-importantkey_container-item">' + '<div class="new-importantkey_container-item_detaile json_keyword" title="' + addKey + '">' + addKey + '</div>' + '<div class="new-importantkey_container-item_colse">' + '<i class="material-icons"  onclick="PubEdit.deleteKeyElement(this)">close</i>' + '</div>' + '</div>';
    $("#addKeyInput").before(element);
    $("#addKeyInput").val("");
}
;
// 添加科技领域
PubEdit.showScienceAreaBox = function(areaSelect) {
    var areaIds = "";
    areaIds = $(".json_scienceArea_scienceAreaId:eq(0)").val() + "," + $(".json_scienceArea_scienceAreaId:eq(1)").val();
    $.ajax({
        url: "/psnweb/sciencearea/ajaxaddpubarea",
        type: "post",
        dataType: "html",
        data: {
            "scienceAreaIds": areaIds
        },
        success: function(data) {
            $("#login_box_refresh_currentPage").val("false");
            // 登录不跳转
            BaseUtils.ajaxTimeOut(data, function() {
                $("#scienceAreaBox").html(data);
                showDialog("scienceAreaBox");
                addFormElementsEvents(document.getElementById("research-area-list"));
                $("#areaSelect").val(areaSelect);
            });
        },
        error: function() {}
    });
}
;
PubEdit.addScienceArea = function(id) {
    var key = $("#" + id + "_category_title").html();
    var sum = $("#choosed_area_list").find(".main-list__item").length;
    if (sum < 2) {
        var html = '<div class="main-list__item" style="padding: 0px 16px!important;" areaid = "' + id + '" >' + '<div class="main-list__item_content">' + key + '</div>' + '<div class="main-list__item_actions"  onclick="javascript:PubEdit.delScienceArea(\'' + id + '\');"><i class="material-icons">close</i></div></div>';
        $("#choosed_area_list").append(html);
        $("#unchecked_area_" + id).attr("onclick", "");
        $("#unchecked_area_" + id).attr("id", "checked_area_" + id);
        $("#" + id + "_status").html("check");
        $("#" + id + "_status").css("color", "forestgreen");
    } else {
        // 出提示语
        scmpublictoast(Enter.scienceAreaLimit, 1500);
    }
}
;
PubEdit.delScienceArea = function(id) {
    $("#choosed_area_list").find(".main-list__item[areaid='" + id + "']").remove();
    $("#checked_area_" + id).attr("onclick", "PubEdit.addScienceArea('" + id + "')");
    $("#checked_area_" + id).attr("id", "unchecked_area_" + id);
    $("#" + id + "_status").html("add");
    $("#" + id + "_status").css("color", "");
}
;
// 保存科技领域
PubEdit.saveScienceArea = function() {

    setTimeout(function() {
        $("#scienceAreaId1").val("");
        $(".json_scienceArea_scienceAreaId:first").val("");
        $("#scienceAreaId2").val("");
        $(".json_scienceArea_scienceAreaId:eq(1)").val("");
        var keyName = "";
        $("#choosed_area_list").find(".main-list__item").each(function(index) {
            var id = $(this).attr("areaid");
            keyName += $("#" + id + "_category_title").html() + ", ";
            if (index == 0) {
                $(".json_scienceArea_scienceAreaId:first").val(id);
            } else {
                $(".json_scienceArea_scienceAreaId:eq(1)").val(id);
            }
        });
        $("#scienceAreaId").val(keyName.replace(/,\s$/, ""));
    }, 100);
    hideDialog("scienceAreaBox");
}
;
// 隐藏
PubEdit.hideScienceAreaBox = function(obj) {
    hideDialog("scienceAreaBox");
}
;
PubEdit.showDailog = function(type) {
    var imgeUrl = $("#successImg").val();
    var msg = $("#save_msg").val();

    if (type == "changeType") {
        $("#show_img").attr("src", $("#successImg").val());
        $("#show_msg").html($("#change_msg").val());
        $("#returnBottom").html(Enter.changeType_confirm).attr("onclick", "PubEdit.change();");
        $("#continueBottom").html(Enter.changeType_cancel).attr("onclick", "PubEdit.closeTip();");
    } else if (type == "error") {
        $("#show_img").attr("src", $("#errorImg").val());
        $("#show_msg").html($("#save_error_msg").val());
        $("#returnBottom").html(Enter.backlist).attr("onclick", "PubEdit.viewPubs(this);");
        $("#continueBottom").html(Enter.continue).attr("onclick", "PubEdit.closeTip();");
    } else {
        $("#show_img").attr("src", $("#successImg").val());
        $("#show_msg").html($("#save_success_msg").val());
        $("#returnBottom").html(Enter.backlist).attr("onclick", "PubEdit.viewPubs(this);");
        $("#continueBottom").html(Enter.continue).attr("onclick", "PubEdit.continueEdit();");
    }
    $("#resultMsg").show();
    PubEdit.positioncenter({
        targetele: 'new-success_save',
        closeele: 'new-success_save-header_tip'
    });
}
PubEdit.positioncenter = function(options) {
    var defaults = {
        targetele: "",
        closeele: ""
    };
    var opts = Object.assign(defaults, options);
    if ((opts.targetele == "") || (opts.closeele == "")) {
        alert("为传入必须的操作元素");
    } else {
        var postarget = document.getElementsByClassName(opts.targetele)[0];
        var opstclose = document.getElementsByClassName(opts.closeele);
        postarget.closest(".background-cover").style.display = "block";
        setTimeout(function() {
            postarget.style.left = (window.innerWidth - postarget.offsetWidth) / 2 + "px";
            postarget.style.bottom = (window.innerHeight - postarget.offsetHeight) / 2 + "px";
        }, 300);
        window.onresize = function() {
            postarget.style.left = (window.innerWidth - postarget.offsetWidth) / 2 + "px";
            postarget.style.bottom = (window.innerHeight - postarget.offsetHeight) / 2 + "px";
        }
        for (var i = 0; i < opstclose.length; i++) {
            opstclose[i].onclick = function() {
                this.closest("." + opts.targetele).style.bottom = -600 + "px";
                setTimeout(function() {
                    postarget.closest(".background-cover").style.display = "none";
                }, 300);
            }
        }

    }
}
;
// 一些下拉框和单选框赋值在这里加载
PubEdit.selectAndRadioOnclickInit = function() {
    $(".dev_pub_type").click(function() {
        // 成果类型的选择
        var type = $(this).find("span").attr("value");
        if ($(".json_pubType").val() == type) {
            return;
        }
        PubEdit.showDailog("changeType");
        $("#changTypeName").val(type);
    });
    $(".dev_standard_type").click(function() {
        // 标准类型的选择
        var type = $(this).find("span").attr("value");
        var showName = $(this).find("span").html();
        if ($(".json_standard_type").val() == type) {
            return;
        }
        PubEdit.changeStandardType(type);
        $(".json_standard_type").val(type);
        $("#showStandardTypeName").val(showName);
    });
    $(".dev_publish_status").click(function() {
        // 期刊论文的状态
        var status = $(this).find("span").attr("value");
        $("#publishStatus").val(status);
    });
    $('.handin_import-container').on('click', '.selected-author,.selected-author_confirm', function() {
        // 动态事件绑定 多选框
        var $this = $(this);
        var $input;
        if ($this.siblings(".json_situation_sitStatus").length > 0) {
            // 收录情况
            $input = $this.siblings(".json_situation_sitStatus");
        }
        if ($this.siblings(".json_member_communicable").length > 0) {
            // 通信作者
            if ($this.closest(".json_member").find(".json_member_name").val() == "") {
                // 没有填名字不选中
                scmpublictoast(Enter.notMenberName, 1000);
                return;
            }
            $input = $this.siblings(".json_member_communicable");
        }
        // //////
        if ($this.hasClass("selected-author")) {
            $this.removeClass("selected-author").addClass("selected-author_confirm");
            $input.val("true");
        } else {
            $this.removeClass("selected-author_confirm").addClass("selected-author");
            $input.val("false");
        }
    });
    $('.handin_import-container').on('click', '.selected-oneself_confirm,.selected-oneself', function() {
        // 动态绑定单选框
        var $this = $(this);

        if ($this.hasClass("selected-oneself")) {
            // 选中
            if ($this.hasClass("dev_menber_i")) {
                // 第一作者
                if ($this.closest(".json_member").find(".json_member_name").val() == "") {
                    // 没有填名字不选中
                    scmpublictoast(Enter.notMenberName, 1000);
                    return;
                }
                $(".dev_menber_i").removeClass("selected-oneself_confirm").addClass("selected-oneself");
                $(".json_member_owner").val("false");
                $this.siblings(".json_member_owner").val("true");
            }
            if ($this.hasClass("dev_patent_type")) {
                // 专利的类型
                $(".dev_patent_type").removeClass("selected-oneself_confirm").addClass("selected-oneself");
                var val = $this.attr("value");
                $(".json_patent_type").val(val);
            }
            if ($this.hasClass("dev_patent_area")) {
                // 专利地区
                $(".dev_patent_area").removeClass("selected-oneself_confirm").addClass("selected-oneself");
                var val = $this.attr("value");
                $(".json_patent_area").val(val);
                PubEdit.changePatentArea();
            }
            if ($this.hasClass("dev_patent_status")) {
                // 专利状态
                $(".dev_patent_status").removeClass("selected-oneself_confirm").addClass("selected-oneself");
                var val = $this.attr("value");
                $(".json_patent_status").val(val);
                PubEdit.changePartentStatus();
            }
            if ($this.hasClass("dev_patent_transitionStatus")) {
                // 专利转化状态
                $(".dev_patent_transitionStatus").removeClass("selected-oneself_confirm").addClass("selected-oneself");
                var val = $this.attr("value");
                $(".json_patent_transitionStatus").val(val);
                PubEdit.changeTransitionStatus();
            }
            if ($this.hasClass("dev_book_type")) {
                // 书籍类型
                $(".dev_book_type").removeClass("selected-oneself_confirm").addClass("selected-oneself");
                var val = $this.attr("value");
                $(".json_book_type").val(val);
            }
            if ($this.hasClass("dev_conferencePaper_paperType")) {
                // 会议类型
                $(".dev_conferencePaper_paperType").removeClass("selected-oneself_confirm").addClass("selected-oneself");
                var val = $this.attr("value");
                $(".json_conferencePaper_paperType").val(val);
            }
            if ($this.hasClass("dev_thesis_degree")) {
                // 学位
                $(".dev_thesis_degree").removeClass("selected-oneself_confirm").addClass("selected-oneself");
                var val = $this.attr("value");
                $(".json_thesis_degree").val(val);
            }
            if ($this.hasClass("dev_sc_acquisitionType")) {
                // 权利获得方式-软件著作权
                $(".dev_sc_acquisitionType").removeClass("selected-oneself_confirm").addClass("selected-oneself");
                var val = $this.attr("value");
                $(".json_softwarecopyright_acquisitionType").val(val);
            }
            if ($this.hasClass("dev_sc_scopeType")) {
                // 权利范围-软件著作权
                $(".dev_sc_scopeType").removeClass("selected-oneself_confirm").addClass("selected-oneself");
                var val = $this.attr("value");
                $(".json_softwarecopyright_scopeType").val(val);
            }
            $this.removeClass("selected-oneself").addClass("selected-oneself_confirm");
        } else {
            if ($this.hasClass("dev_menber_i")) {
                // 是否本人
                $this.removeClass("selected-oneself_confirm").addClass("selected-oneself");
                $this.siblings(".json_member_owner").val("false");
            }

            if ($this.hasClass("dev_patent_transitionStatus")) {
                // 专利转化状态
                $this.removeClass("selected-oneself_confirm").addClass("selected-oneself");
                $(".json_patent_transitionStatus").val("NULL");
                $(".dev_patent_price").show();
            }

        }
    });

}
;
// 改变标准类型时需要执行的逻辑
PubEdit.changeStandardType = function(standardType) {
    // 选中国际标准类型，显示公布机构
    if (standardType == "INTERNATIONAL" ) {
        $("#standard_publishAgency").show();
        $("#standard_technicalCommittees").hide();
        $(".json_standard_technicalCommittees").val("");
    } else {
        // 选中非国际标准类型，显示归口单位
        $("#standard_publishAgency").hide();
        $(".json_standard_publishAgency").val("");
        $("#standard_technicalCommittees").show();
    }
}
// 设置标准显示的名字
PubEdit.setStandardShowName = function(standardType) {
    if (standardType == "") {
        standardType = "INTERNATIONAL";
    }
    switch (standardType) {
    case "INTERNATIONAL":
        $("#showStandardTypeName").val(Enter.standard_international);
        break;
    case "NATION_FORCE":
        $("#showStandardTypeName").val(Enter.standard_nation_force);
        break;
    case "NATION_RECOMMENDED":
        $("#showStandardTypeName").val(Enter.standard_nation_recommended);
        break;
    case "INDUSTRIAL_FORCE":
        $("#showStandardTypeName").val(Enter.standard_industrial_force);
        break;
    case "INDUSTRIAL_RECOMMENDED":
        $("#showStandardTypeName").val(Enter.standard_industrial_recommended);
        break;
    case "LOCAL":
        $("#showStandardTypeName").val(Enter.standard_local);
        break;
    case "ENTERPRISE":
        $("#showStandardTypeName").val(Enter.standard_enterprise);
    case "OTHER":
        $("#showStandardTypeName").val(Enter.standard_other);
        break;
    }
}
// 改变专利申请状态时候
PubEdit.changePartentStatus = function() {
    var val = $(".json_patent_status").val();
    if (val == "1") {
        // 授权
        $(".dev_patentNo_msg").text(Enter.patentNo);

        $(".dev_publishDate_msg").text(Enter.partenPublicDate);
        $(".dev_patent_patentee").text(Enter.patentee);
        $(".json_patent_applier").removeClass("json_patent_applier").addClass("json_patent_patentee");
        $(".dev_partent_start_end_date").show();
        $(".dev_patent_transition").show();
    } else {
        $(".dev_patentNo_msg").text(Enter.applicationNo);
        $(".dev_publishDate_msg").text(Enter.partenApplicationDate);
        $(".dev_patent_patentee").text(Enter.applier);
        $(".json_patent_patentee").removeClass("json_patent_patentee").addClass("json_patent_applier");
        $(".dev_partent_start_end_date").hide();
        $(".dev_patent_transition").hide();
    }
}
// 改变专利转化状态
PubEdit.changeTransitionStatus = function() {
    var val = $(".json_patent_transitionStatus").val();
    if (val == "NONE") {
        $(".json_patent_price").val("");
        $(".dev_patent_price").hide();
    } else {
        $(".dev_patent_price").show();
    }
}
;
PubEdit.viewPubs = function(obj) {
    if (obj != undefined && $(obj).attr("isEdit") == "true") {
        // 编辑后，返回的页面
        window.location.href = "/pub/backurl?isEdit=true";
    } else {
        window.location.href = "/pub/backurl";
    }
}
;
PubEdit.getAutoCompleteJson = function(type) {
    return {
        "type": type
    };
}
PubEdit.continueEdit = function() {
    var url = window.location.href;
    if (url.match(/\?/g)) {
        url = url.match(/[?&]des3PubId=/g) ? url : url + "&des3PubId=" + $(".json_des3PubId").val();
    } else {
        url = url + "?des3PubId=" + $(".json_des3PubId").val();
    }
    window.location.href = url;
}
;
PubEdit.closeTip = function() {
    setTimeout(function() {
        $("#resultMsg").hide();
    }, 300);
}
;

PubEdit.link = function(url, e) {
    e = window.event || e;
    location.href = url;
    if (window.event)
        window.event.returnValue = false;
    else
        e.preventDefault();
    // for firefox

}
;

PubEdit.validObj = function(obj) {
    $(obj).closest(".error_import-tip_border").removeClass("error_import-tip_border-warn");
    $(obj).valid();
}
;

PubEdit.showAddNewJournal = function() {
    $("#addNewJournal").css("display", "");
    if (document.getElementsByClassName("new-add_periodical")) {
        var target = document.getElementsByClassName("new-add_periodical")[0];
        target.style.left = (window.innerWidth - target.offsetWidth) / 2 + "px";
        target.style.bottom = (window.innerHeight - target.offsetHeight) / 2 + "px";
        window.onresize = function() {
            target.style.left = (window.innerWidth - target.offsetWidth) / 2 + "px";
            target.style.bottom = (window.innerHeight - target.offsetHeight) / 2 + "px";
        }
    }
}

PubEdit.cancleAddNewJournal = function() {
    $("#addNewJournal").css("display", "none");
}

PubEdit.confirmAddNewJournal = function() {
    var jname = $.trim($("#add_journal_name").val());
    var jissn = $.trim($("#add_journal_issn").val());

    if (jname == "") {
        scmpublictoast(Enter.journalName, 1500);
        // $.scmtips.show("warn",i18n_pub["journal.add.validate.name.empty"]);
        $("#add_journal_name").focus();
        return false;
    }
    context_jname = jname;
    context_jissn = jissn;
    if (jname.length > 250) {
        scmpublictoast(Enter.journalNameTips, 1500);
        // $.scmtips.show("warn",i18n_pub["journal.add.validate.name.maxlength"]);
        $("#add_journal_name").focus();
        return false;
    }
    if (jissn != "") {
        var pattern = /^(\S{4}-\S{4})$/;
        if (!pattern.test(jissn)) {
            // $.scmtips.show("warn",i18n_pub["journal.add.validate.issn"]);
            scmpublictoast(Enter.ISSNFormat, 1500);
            $("#add_journal_issn").focus();
            return false;
        }
    }
    PubEdit.doAddJournal(jname, jissn);
    PubEdit.cancleAddNewJournal();
}

PubEdit.doAddJournal = function(jname, jissn) {

    $.ajax({
        url: '/pub/addjournal',
        type: 'POST',
        data: {
            "jissn": jissn,
            "jname": jname
        },
        dataType: 'json',
        success: function(response) {
            if (response) {
                if (response.result == 'success') {
                    $(".json_journal_jid").val(response.data.id);
                    $(".json_journal_issn").val(jissn);
                    $(".json_journal_name").val(jname);
                    // scmpublictoast("添加新期刊成功", 1500);
                } else if (response.result == 'exist') {
                    $(".json_journal_issn").val(jissn);
                    $(".json_journal_name").val(jname);
                    // scmpublictoast("系统已存在该期刊，添加失败", 1500);
                } else {// scmpublictoast("添加新期刊失败，请检查网络", 1500);
                }
            }
        },
        error: function() {
            scmpublictoast("添加新期刊失败，请检查网络", 1500);
        }
    });
}
;

PubEdit.selectJournal = function(id, name, issn) {
    id = "";
    // 由于查重列表现在修改为匹配上基础期刊显示base_journal表+当前人未匹配，所以这里不再有jid.
    // $("#_pub_journal_jid").val(id);
    // $("#_pub_journal_jissn").val(issn);
    $(".json_journal_name").val(name);

}
;
// 表单校验
PubEdit.formValid = function() {
    // PubEdit.valid();
    $("#enterPubForm").valid();
    $(".json_member_name:first").valid();
    if ($(".json_patent_publicationOpenNo").length > 0) {
        $(".json_patent_publicationOpenNo").valid();
    }
    if ($(".json_patent_applicationNo").length > 0) {
        $(".json_patent_applicationNo").valid();
    }
}
;
// 成果编辑自动补全成果其他信息
PubEdit.autoFillPubInfo = function(des3pdwhPubId) {

    $("#titleDes3PdwhId").val(des3pdwhPubId);
    // 给出一个弹出框是否补全其他信息
    if ($("#titleDes3PdwhId").val() != "" && $("#titleDes3PdwhId").val() != null) {
        PubEdit.pdwhIsExist(des3pdwhPubId);
        // SCM-23657
    }
}
;

PubEdit.pdwhIsExist = function(des3PubId) {
    $.ajax({
        url: "/pub/opt/ajaxpdwhisexists",
        type: 'post',
        dataType: "json",
        async: false,
        data: {
            "des3PubId": des3PubId
        },
        timeout: 10000,
        success: function(data) {
            if (data.result == 'success') {
                PubEdit.showConfirm();
            } else {
                scmpublictoast(Enter.pubIsNotExist, 1000);
            }
        },
        error: function(data) {
            scmpublictoast("系统出错", 1000);
        }
    });
}

// 成果新增-查重逻辑
PubEdit.checkRepeat = function(obj) {
    // 防重复点击
    if (obj == undefined) {
        $("#dup_box_flag").val("0");
    } else {
        $("#dup_box_flag").val("1");
    }
    if (!$(".json_title").valid()) {
        scmpublictoast($(".json_title").attr("title_msg") + Enter.notEmpty, 1500);
        return;
    }
    var pubType = $.trim($("#pubType").val());
    if (pubType == 12 && !$(".json_standard_standardNo").valid()) {
        scmpublictoast($(".json_standard_standardNo").attr("title_msg") + Enter.notEmpty, 1500);
        return;
    }
    if (pubType == 13 && !$(".json_softwarecopyright_registerNo").valid()) {
        scmpublictoast($(".json_softwarecopyright_registerNo").attr("title_msg") + Enter.notEmpty, 1500);
        return;
    }
    BaseUtils.doHitMore(obj, 10000);
    var des3GrpId = $(".json_des3GrpId").eq(0).val();
    var des3PubId = $(".json_des3PubId").eq(0).val();
    var des3PsnId = $("#userDes3PsnId").val();
    var title = $.trim($(".json_title").eq(0).val());

    var publishDate = "";
    if (pubType == 4 || pubType == 3 || pubType == 2 || pubType == 7 || pubType == 10) {
        // 由于存在多种成果类型，每种成果类型在进行查重时在构建HashTPP（由title和pubYear和pubType构造）时，解决无法根据成果发表时间来去重的问题
        publishDate = $.trim($("#publishDate").val());
    } else if (pubType == 5) {
      var patentStatus = $.trim($(".json_patent_status").eq(0).val());
      if(patentStatus == "1"){
        // 授权状态取开始日期
        publishDate = $.trim($("#startDate").val());
      } else{
        // 申请状态取申请日期
        publishDate = $.trim($("#applicationDate").val());
      }
    } else if (pubType == 1) {
        publishDate = $.trim($("#awardDate").val());
    } else if (pubType == 8) {
        publishDate = $.trim($("#defenseDate").val());
    }
    var doi = $.trim($(".json_doi").eq(0).val());
    var sourceId = $.trim($(".json_sourceId").eq(0).val());
    var srcDbId = $.trim($(".json_srcDbId").eq(0).val());
    var applicationNo = $.trim($(".json_patent_applicationNo").eq(0).val());
    var publicationOpenNo = $.trim($(".json_patent_publicationOpenNo").eq(0).val());
    var standardNo = $.trim($(".json_standard_standardNo").eq(0).val());
    var registerNo = $.trim($(".json_softwarecopyright_registerNo").eq(0).val());
    $.ajax({
        url: "/pub/enter/ajaxcheckrepeat",
        type: "post",
        dataType: "html",
        data: {
            "des3GrpId": des3GrpId,
            "des3PubId": des3PubId,
            "des3PsnId": des3PsnId,
            "title": title,
            "pubType": pubType,
            "publishDate": publishDate,
            "doi": doi,
            "sourceId": sourceId,
            "srcDbId": srcDbId,
            "applicationNo": applicationNo,
            "publicationOpenNo": publicationOpenNo,
            "standardNo": standardNo,
            "registerNo": registerNo
        },
        success: function(data) {
            BaseUtils.ajaxTimeOut(data, function() {
                if (data != null && data != "") {
                    $("#dev_duppub_box").html(data);
                    $("#dev_duppub_box").show();
                    // 弹框
                    var box = document.getElementsByClassName("repeat_achive-container")[0];
                    PubEdit.positionfix({
                        screentarget: box
                    });
                } else {
                    if (obj != undefined && obj != null) {
                        // 标识是点击保存按钮
                        PubEdit.save(obj);
                    }
                }
            });
        }
    });
}
;
// hht 弹框
PubEdit.showConfirm = function() {
    $("#autoFillPub").css("display", "");
    if (document.getElementsByClassName("auto_Fill_Pub")) {
        var target = document.getElementsByClassName("auto_Fill_Pub")[0];
        target.style.left = (window.innerWidth - target.offsetWidth) / 2 + "px";
        target.style.bottom = (window.innerHeight - target.offsetHeight) / 2 + "px";
        window.onresize = function() {
            target.style.left = (window.innerWidth - target.offsetWidth) / 2 + "px";
            target.style.bottom = (window.innerHeight - target.offsetHeight) / 2 + "px";
        }
    }
}
// 取消自动补全
PubEdit.cancleAutoFillPub = function() {
    $("#autoFillPub").css("display", "none");
}
// 确认自动补全
PubEdit.confirmAutoFillPub = function() {

    var des3pdwhPubId = $("#titleDes3PdwhId").val();
    $("#des3pdwhPubId").val(des3pdwhPubId);
    var pubjson = pub.getJson();
    var membersJsonStr = JSON.stringify(pub.members)
    $("#membersJsonStr").val(membersJsonStr);
    var autoFillPubForm = document.getElementById("autoFillPubForm");
    if (autoFillPubForm) {
        autoFillPubForm.submit();
    }

}

PubEdit.titleChange = function() {
    $(".json_title").blur(function() {
        PubEdit.checkRepeat();
    });
    $(".json_standard_standardNo").blur(function() {
      PubEdit.checkRepeat();
    });
    $(".json_softwarecopyright_registerNo").blur(function() {
      PubEdit.checkRepeat();
    });
}

// gzl_查看重复成果的弹框js
PubEdit.positionfix = function(options) {
    var defaults = {
        screentarget: "",
    };
    var opts = Object.assign(defaults, options);
    if (opts.screentarget) {
        var parentbox = document.getElementsByClassName("bckground-cover")[0];
        parentbox.style.display = "block";
        var parentboxwidth = parentbox.offsetWidth;
        var parentboxheight = parentbox.offsetHeight;
        var sunboxwidth = opts.screentarget.offsetWidth;
        var sunboxheight = opts.screentarget.offsetHeight;
        opts.screentarget.style.bottom = (parentboxheight - sunboxheight) / 2 + "px";
        opts.screentarget.style.left = (parentbox.offsetWidth - opts.screentarget.offsetWidth) / 2 + "px";
        window.onresize = function() {
            var parentboxwidth = parentbox.offsetWidth;
            var parentboxheight = parentbox.offsetHeight;
            var sunboxwidth = opts.screentarget.offsetWidth;
            var sunboxheight = opts.screentarget.offsetHeight;
            opts.screentarget.style.bottom = (parentboxheight - sunboxheight) / 2 + "px";
            opts.screentarget.style.left = (parentbox.offsetWidth - opts.screentarget.offsetWidth) / 2 + "px";
        }
    }
}
;

// 查重弹出框-取消保存/新增成果
PubEdit.cancelDupPubBox = function() {
    window.location.href = "/pub/backurl";
}
;

// 查重弹出框-继续保存/新增成果
PubEdit.saveDupPubBox = function(obj) {
    $('.dev_duppub_box_bg').hide();
    if ($("#dup_box_flag").val() == "1") {
        // 标识是点击保存按钮
        PubEdit.save(obj);
    }
    $("#dev_duppub_box").html("");
}
;

// 查重弹出框-关闭
PubEdit.closeDupBox = function() {
    $('.dev_duppub_box_bg').hide();
    $("#dev_duppub_box").html("");
}

PubEdit.showPubDetail = function(obj, pubIndexUrl) {
    BaseUtils.doHitMore(obj, 10000);
    window.open(pubIndexUrl);
}
/**
 * 最后一行输入自动加一行，倒数第二行全部为空删除掉,输入框控制
 */
PubEdit.addMenberInputOn = function() {
    PubEdit.checkLastMemberInput();
    $(".json_member").find("input[type='text']").on("input", function(e) {
        var memberNode = $(this).closest(".json_member");
        if ($(memberNode)[0].isEqualNode($(".json_member:last")[0])) {
            addmenber();
        }
        if ($(memberNode)[0].isEqualNode($(".json_member:eq(-2)")[0])) {
            var isEmpty = true;
            $(memberNode).find("input[type='text']").each(function() {
                // 判读最后行是不是都为空
                if ($(this).val() != "") {
                    isEmpty = false;
                }
            });
            if (isEmpty && $(".json_member").length > 3) {
                $(".selected-func_delete:last").removeClass("selected-func_nodelete");
                PubEdit.deletetarget($(".json_member:last").find(".selected-func_delete"));
                $(".selected-func_delete:last").addClass("selected-func_nodelete");
            }
        }
    });
}
;
// 最后一行是否有数据
PubEdit.checkLastMemberInput = function() {
    var isEmpty = true;
    var memberNode = $(".json_member:last");
    $(memberNode).find("input[type='text']").each(function() {
        // 判读最后行是不是都为空
        if ($(this).val() != "") {
            isEmpty = false;
        }
    });
    if (!isEmpty) {
        addmenber();
    }
}
PubEdit.addMenberInputBlur = function(obj) {
    $(obj).find("input[type='text']").blur(function(e) {
        if ($(obj).find(".json_member_name").val() == "") {
            // 去掉是否本人勾选
            $(obj).find(".dev_menber_i").removeClass("selected-oneself_confirm").addClass("selected-oneself");
            $(obj).find(".json_member_owner").val("false");
            // 去掉通讯作者勾选
            $(obj).find(".dev_communicale").removeClass("selected-author_confirm").addClass("selected-author");
            $(obj).find(".json_member_communicable").val("false");
        }
    });
}
;

PubEdit.addMenberInputOnChange = function(obj) {
    $(obj).find(".json_member_name").change(function() {
        $(obj).find(".json_member_des3PsnId").val("");
    });
}
;
// 修改期刊名称
PubEdit.changeJournalName = function(obj) {
    $(".json_journal_jid").val("");
    $(".json_journal_issn").val("");
}
;
// 专利国家选择
PubEdit.changePatentArea = function() {
    var area = $(".json_patent_area").val();
    $(".json_patent_type").val("");
    $(".dev_patent_type").removeClass("selected-oneself_confirm").addClass("selected-oneself");
    // 全掉选中
    $(".dev_patent_type_input").val("");
    $(".dev_patent_type[value='51']").removeClass("selected-oneself").addClass("selected-oneself_confirm");
    switch (area) {
    case "CHINA":
        $(".dev_patent_type_i").show();
        $(".dev_type_51").show();
        $(".dev_type_52").show();
        $(".dev_type_53").show();
        $(".dev_type_54").hide();
        $(".dev_type_other").hide();
        break;
    case "USA":
        $(".dev_patent_type_i").show();
        $(".dev_type_51").show();
        $(".dev_type_52").hide();
        $(".dev_type_53").show();
        $(".dev_type_54").show();
        $(".dev_type_other").hide();
        break;
    case "EUROPE":
        $(".dev_patent_type_i").show();
        $(".dev_type_51").show();
        $(".dev_type_52").hide();
        $(".dev_type_53").hide();
        $(".dev_type_54").hide();
        $(".dev_type_other").hide();
        break;
    case "WIPO":
        $(".dev_patent_type_i").show();
        $(".dev_type_51").show();
        $(".dev_type_52").show();
        $(".dev_type_53").hide();
        $(".dev_type_54").hide();
        $(".dev_type_other").hide();
        break;
    case "JAPAN":
        $(".dev_patent_type_i").show();
        $(".dev_type_51").show();
        $(".dev_type_52").show();
        $(".dev_type_53").show();
        $(".dev_type_54").hide();
        $(".dev_type_other").hide();
        break;
    case "OTHER":
        $(".dev_patent_type_i").hide();
        $(".dev_type_other").show();
        break;
    default:
        break;
    }
}
;
