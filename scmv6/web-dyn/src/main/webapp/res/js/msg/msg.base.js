/**
 * 消息中心js--zzx
 */
var MsgBase = MsgBase ? MsgBase : {};

// 显示文件来源选择框
MsgBase.showMsgFileSelectDialog = function() {
    var chatPsnId = $("*[scm_id='chatPsnInfo']").attr("scm_chatPsnNo");
    if (chatPsnId == null || chatPsnId == "") {
        chatPsnId = $("#msg_friends").find(".chip__box").attr("code");
        if (chatPsnId == null || chatPsnId == "") {
            scmpublictoast(msgBase.pleaseSelectSender, 2000);
            return;
        }
    }
    showDialog("select_msg_file_dialog");
    $("body").css("overflow", "hidden");
}
// 隐藏文件来源选择框
MsgBase.hideMsgFileSelectDialog = function() {
    hideDialog("select_msg_file_dialog");
}
// 显示文件库文件选择框
MsgBase.showMyFileSelectDialog = function() {
    hideDialog("select_msg_file_dialog");
    MsgBase.msgfileBoxInterval = setInterval(function() {// 确定按钮的监听
        MsgBase.is_filesend();
    }, 1000);
    showDialog("select_my_file_import_dialog");
    MsgBase.showMyFileList();
}
// 隐藏文件库文件选择框
MsgBase.hideMyFileSelectDialog = function() {
    $("#grp_file_search_my_file_key").val("");
    clearInterval(MsgBase.msgfileBoxInterval);// 取消确定按钮的监听
    hideDialog("select_my_file_import_dialog");
}
// 显示本地文件选择框
MsgBase.showLocalFileSelectDialog = function() {
    hideDialog("select_msg_file_dialog");
    addFormElementsEvents();
    showDialog("msg_local_file_upload");
    $("body").css("overflow", "hidden");
    MsgBase.initLocalFileSelectUpload();
}
// 隐藏本地文件选择框
MsgBase.hideLocalFileSelectDialog = function(e) {

    hideDialog("msg_local_file_upload");
    if (document.getElementsByClassName("form__sxn_row-list").length > 0) {
        document.getElementsByClassName("form__sxn_row-list")[0].innerHTML = "";
    }
    /* document.body.removeChild(document.getElementById("msg_local_file_upload")); */
}
MsgBase.initLocalFileSelectUpload = function() {
    // 上传文件重置
    var fileDoxDom = $("#msg_local_fileupload_box").find(".fileupload__box")
            .get(0);
    fileUploadModule.resetFileUploadBox(fileDoxDom);
    var dataJson = {
        "fileDealType" : "generalfile"
    };
    // var filetype =
    // ".txt,.pdf,.doc,.docx,.ppt,.pptx,.xls,.xlsx,.rar,.zip,.jpg,.png,.gif,.jpeg,.htm,.html,.xhtml".split(",");
    var data = {
        "fileurl" : "/fileweb/fileupload",
        "filedata" : dataJson,
        "filecallback" : MsgBase.uploadLocalFileCallBack,
        "method" : "dropadd",
        "multiple" : "true",
        "maxlength" : "10",
        "maxtip" : "1"
    }
    fileUploadModule.initialization(document
            .getElementById("msg_local_fileupload_box"), data);
};
MsgBase.uploadLocalFileCallBack = function(xhr) {
    const data = eval('(' + xhr.responseText + ')');
    BaseUtils
            .ajaxTimeOut(
                    data,
                    function() {
                        if (data.successFiles >= 1) {
                            var fileIdArray = data.successDes3FileIds
                                    .split(",");
                            for (var key = 0; key < fileIdArray.length; key++) {
                                var returnData = {
                                    'fileDesc' : $(
                                            "#msg_local_file_upload_content")
                                            .val(),
                                    'des3ArchiveFileId' : fileIdArray[key]
                                }
                                $
                                        .ajax({
                                            url : "/psnweb/myfile/ajasavemyuploadfile",
                                            type : "post",
                                            dataType : "json",
                                            data : returnData,
                                            async : false,// 多文件上传同步方式
                                            success : function(data) {
                                                BaseUtils
                                                        .ajaxTimeOut(
                                                                data,
                                                                function() {
                                                                    if (data.result === "success") {
                                                                        MsgBase
                                                                                .doSendChatLocalFileForShare(data.psnFileId);
                                                                    } else {
                                                                        scmpublictoast(
                                                                                msgBase.uploadFail,
                                                                                1000);
                                                                    }
                                                                });
                                            }
                                        });
                            }
                        } else {
                            scmpublictoast(data.failFileNames, 2000);
                        }
                    })
};
MsgBase.uploadLocalFile = function() {
    // 点击上传文件
    var fileSelect = $("#msg_local_fileupload_box").find(".file_selected");
    if (fileSelect == undefined || fileSelect.length == 0) {
        scmpublictoast(msgBase.uploadFile, 2000);
        return;
    }
    var fileDoxDom = $("#msg_local_fileupload_box").find(".fileupload__box")
            .get(0);
    var dataFile = {
        "fileDesc" : $("#msg_local_file_upload_content").val()
    }
    // 超时判断
    $.ajax({
        url : "/dynweb/ajaxtimeout",
        dataType : "json",
        type : "post",
        data : {},
        success : function(data) {
            BaseUtils.ajaxTimeOut(data, function() {
                fileUploadModule.uploadFile(fileDoxDom, dataFile);
            });

        }
    });
};
// 文件发送按钮的disabled设置
MsgBase.is_filesend = function() {
    var length = $("#select_my_file_import").find(
            "input:checkbox[name='pub-type']:checked").length;
    if (length > 0) {
        $("#select_my_file_import").find(".button_primary-reverse").removeAttr(
                "disabled");
    } else {
        $("#select_my_file_import").find(".button_primary-reverse").attr(
                "disabled", true);
    }
}
// 站内信查看成果
MsgBase.showPubDetails = function(obj) {
    var newwindow = window.open("about:blank");
    $.ajax({
        url : "/pub/details/ajaxview",
        dataType : "json",
        type : "post",
        data : {
            "des3PubId" : $(obj).attr("des3PubId")
        },
        success : function(data) {
            var des3relationid = $(obj).attr("des3relationid");
            newwindow.location.href = data.shortUrl + "?des3relationid="
                    + des3relationid;
        }
    });
}
// 站内信 我的文件列表
MsgBase.showMyFileList = function() {
    $("#grpFileMyFileListId").doLoadStateIco({
        style : "height: 38px; width:38px; margin:auto ; margin-top:10px;",
        status : 1
    });
    MsgBase.myfileMainlist = window
            .Mainlist({
                name : "myfilelist",
                listurl : "/psnweb/myfile/ajaxmyfilelist",
                listdata : {
                    "source" : "isPFBox"
                },
                method : "scroll",
                listcallback : function(xhr) {
                    $("#select_my_file_import")
                            .find(".main-list__item  input[type='checkbox']")
                            .change(
                                    function() {
                                        if (this.checked == true) {
                                            var length = $(
                                                    "#select_my_file_import")
                                                    .find(
                                                            "input:checkbox[name='pub-type']:checked").length;
                                            if (length > 10) {
                                                scmpublictoast("最多可以分享10个文件",
                                                        2000);
                                                this.checked = false;
                                            }
                                        }
                                    });
                },
            });
}
// 显示成果UI
MsgBase.showMsgPubUI = function() {
    var chatPsnId = $("*[scm_id='chatPsnInfo']").attr("scm_chatPsnNo");
    if (chatPsnId == null || chatPsnId == "") {
        chatPsnId = $("#msg_friends").find(".chip__box").attr("code");
        if (chatPsnId == null || chatPsnId == "") {
            scmpublictoast(msgBase.pleaseSelectSender, 2000);
            return;
        }
    }
    MsgBase.msgpubBoxInterval = setInterval(function() {// 确定按钮的监听
        MsgBase.is_pubsend();
    }, 1000);
    showDialog("select_my_pub_import_dialog");
    MsgBase.showMyPubList();
}

// 关闭成果UI
MsgBase.hideMsgPubUI = function() {
    $("#search_my_pub_key").val("");
    clearInterval(MsgBase.msgpubBoxInterval);// 取消确定按钮的监听
    hideDialog("select_my_pub_import_dialog");
}
// 文件发送按钮的disabled设置
MsgBase.is_pubsend = function() {
    // 最多可以选择10个
    var length = $("#select_my_pub_import").find(
            "input:checkbox[name='pub-type']:checked").length;
    if (length > 0) {
        $("#select_my_pub_import").find(".button_primary-reverse").removeAttr(
                "disabled");
    } else {
        $("#select_my_pub_import").find(".button_primary-reverse").attr(
                "disabled", true);
    }
}
// 站内信 我的成果列表
MsgBase.showMyPubList = function() {
    $("#grpPubMyPubListId").doLoadStateIco({
        style : "height: 38px; width:38px; margin:auto ; margin-top:10px;",
        status : 1
    });
    var dataJson = {};
    MsgBase.myfileMainlist = window
            .Mainlist({
                name : "mypublist",
                listurl : "/groupweb/grppub/ajaxmsgpublist",
                listdata : dataJson,
                method : "scroll",
                listcallback : function(xhr) {
                    $("#myPubListId")
                            .find(".main-list__item  input[type='checkbox']")
                            .change(
                                    function() {
                                        if (this.checked == true) {
                                            var length = $(
                                                    "#select_my_pub_import")
                                                    .find(
                                                            "input:checkbox[name='pub-type']:checked").length;
                                            if (length > 10) {
                                                scmpublictoast("最多可以分享10个成果",
                                                        2000);
                                                this.checked = false;
                                            }
                                        }
                                    });
                },
            });
}
// 站内信 点击新建会话按钮
MsgBase.createNewChat = function() {
    $("*[list-main='msg_chat_psn_list']").find(".item_selected").removeClass(
            "item_selected");
    $("#msg_friends").show();
    $("*[list-main='msg_chat_content_list']").unbind("scroll",
            MsgBase.ChatContentScroll);
    /* $("#msg_addfriend").focus(); */
    $("*[scm_id='chatPsnInfo']").attr("scm_chatpsnno", "").html("");
    $("*[list-main='msg_chat_content_list']").html("");
    $("#msg_friends").find(".chip__box").remove();
}
// （消息中心、站内信、全文请求）菜单 事件
MsgBase.menuClick = function(model) {
    var arrLi = $("*[scm_msg_id='menu__list']").find("li");
    arrLi.removeClass("item_selected");
    $("body").removeClass("no-scrollbar");
    if (model == "chatMsg") {
        $("#chatPsnSearchName").val("");
        $("body").addClass("no-scrollbar");
        arrLi.eq(1).addClass("item_selected");
        $("*[scm_msg_menu='centerMsg']").hide();
        $("*[scm_msg_menu='reqFullTextMsg']").hide();
        $("*[scm_msg_menu='chatMsg']").show();
        $("*[list-main='msg_chat_psn_list']").show();
        $("#msg_friend_all_list").hide();
        MsgBase.getChatPsnList();// 请求会话列表
        // 自动填词绑定，compose:确定选中回调
        window.ChipBox({
            name : "msg_select_friends",
            maxItem : 1,
            isFreedom : true,
            stats : {
                display : false
            },
            callbacks : {
                compose : function() {
                    var des3ChatPsnId = $("#msg_friends").find(".chip__box")
                            .attr("code");
                    MsgBase.addNewChat(des3ChatPsnId);
                }
            }
        });
        // addFormElementsEvents($("#msg_friends")[0]);
        $("#msg_friends").hide();
    } else if (model == "reqFullTextMsg") {
        arrLi.eq(2).addClass("item_selected");
        $("*[scm_msg_menu='centerMsg']").hide();
        $("*[scm_msg_menu='chatMsg']").hide();
        $("*[scm_msg_menu='reqFullTextMsg']").show();
        MsgBase.showFulltextRequestMsgList();
    } else {
        model = "centerMsg";
        arrLi.eq(0).addClass("item_selected");
        $("*[scm_msg_menu='chatMsg']").hide();
        $("*[scm_msg_menu='reqFullTextMsg']").hide();
        $("*[scm_msg_menu='centerMsg']").show();
        MsgBase.showMsgList();
    }
    MsgBase.changeUrl(model);
}
// 如果会话存在则点击选中，否则新建会话
MsgBase.addNewChat = function(des3ChatPsnId) {
    if (des3ChatPsnId == null || des3ChatPsnId == "") {
        return;
    }
    var item = $("*[list-main='msg_chat_psn_list']").find(
            ".main-list__item[code='" + des3ChatPsnId + "']");
    if (item.length > 0) {
        item.click();
    } else {
        $.ajax({
            url : '/dynweb/showmsg/ajaxcreatemsgchat',
            type : 'post',
            data : {
                'des3ReceiverId' : des3ChatPsnId
            },
            dataType : 'json',
            success : function(data) {
                BaseUtils.ajaxTimeOut(data, function() {
                    MsgBase.getChatPsnList();
                });
            }
        });
        /*
         * MsgBase.chatContentList(des3ChatPsnId,"");
         * if($("#new_chat_item").length<=0){
         * $("*[list-main='msg_chat_psn_list']").prepend(MsgBase.newChatBoxInfo); }
         */
    }
    $("*[scm_id='chatPsnInfo']").attr("scm_chatPsnNo", des3ChatPsnId);
}
/**
 * //站内信 消息中心列表 status:消息状态：0=未读、1=已读、
 */
MsgBase.showMsgList = function(status) {
    if (status != 0) {
        status = 1;
    }
    window.Mainlist({
        name : "msglist",
        listurl : "/dynweb/showmsg/ajaxgetmsglist",
        listdata : {
            "status" : status,
            "page.pageSize" : 10
        },
        method : "scroll",
        listcallback : function(xhr) {

        }
    });
};
MsgBase.showPubDetail = function(obj) {
    var des3PubId = $(obj).attr("des3PubId");
    var des3relationid = $(obj).attr("des3relationid");
    var url = "/pub/details?des3PubId=" + des3PubId + "&des3relationid="
            + des3relationid;
    window.open(url);
};
// 站内信 删除消息中心消息
MsgBase.delMsg = function(id, e, obj) {
    MsgBase.stopNextEvent(e);
    $.ajax({
        url : "/dynweb/showmsg/ajaxdelmsg",
        dataType : "json",
        type : "post",
        data : {
            msgRelationIds : id
        },
        success : function(data) {
            MsgBase.ajaxTimeOut(data, function() {
                smate.msgcount.header();
                $(obj).closest(".main-list__item ").remove();
                scmpublictoast(msgBase.delSuccess, 2000);
                // MsgBase.showMsgList();
            });

        }
    });
}
// 标记已读
MsgBase.setChatReadMsg = function(des3SenderId) {
    $.ajax({
        url : "/dynweb/showmsg/ajaxsetread",
        dataType : "json",
        type : "post",
        data : {
            des3SenderId : des3SenderId,
            readMsgType : 2
        },
        success : function(data) {
            MsgBase.ajaxTimeOut(data, function() {
                smate.msgcount.header();
            });
        }
    });
}
// 标记已读
MsgBase.setReadMsg = function(type) {
    if (type == 1
            && $("*[list-main='msglist']").find(".response_no-result").length == 1) {
        scmpublictoast(msgBase.noMsg, 1000);
        return;
    }
    $.ajax({
        url : "/dynweb/showmsg/ajaxsetread",
        dataType : "json",
        type : "post",
        data : {
            readMsgType : 3
        },
        success : function(data) {
            MsgBase.ajaxTimeOut(data, function() {
                if (type == 1) {
                    MsgBase.showMsgList(1);
                    scmpublictoast(msgBase.markSuccess, 1000);
                }
                smate.msgcount.header();
            });

        }
    });
}
// 标记已读
MsgBase.setOneReadMsg = function(msgRelationId, type, e, obj) {
    if (e) {
        MsgBase.stopNextEvent(e);
    }
    $.ajax({
        url : "/dynweb/showmsg/ajaxsetread",
        dataType : "json",
        type : "post",
        data : {
            msgRelationId : msgRelationId,
            readMsgType : 1
        },
        success : function(data) {
            MsgBase.ajaxTimeOut(data, function() {
                if (typeof obj != "undefind") {
                    $(obj).closest(".main-list__item").removeClass(
                            "item_selected");
                }
                if (type == 1) {
                    $(obj).remove();
                    scmpublictoast("已标记为已读", 1000);
                }
                if (type == 0) {
                    $(obj).find(".dev_one_read").remove();
                }
                smate.msgcount.header();
            });
        }
    });
};
// 新站内信会话
MsgBase.newChatBoxInfo = '\
<li class="main-list__item" id="new_chat_item"> \
  <div class="main-list__item_content"> \
    <div class="inbox-messenger__item"> \
      <div class="inbox-messenger__avatar"><img src="/resmod/smate-pc/img/logo_psndefault.png"></div> \
      <div class="inbox-messenger__info"> \
        <div class="inbox-messenger__psn-info"> \
          <div class="inbox-messenger__psn-name" >新消息</div> \
          <div class="inbox-messenger__operations"> \
            <div class="inbox-messenger__time"> \
            </div> \
            <div class="inbox-messenger__settings material-icons">delete</div> \
          </div> \
        </div> \
        <div class="inbox-messenger__newest-record"></div> \
      </div> \
    </div> \
  </div> \
</li>';
//
MsgBase.refGetChatPsnList = function(type) {
    MsgBase.msgchatpsnlist = window.Mainlist({
        name : "msg_chat_psn_list",
        listurl : "/dynweb/showmsg/ajaxgetchatpsnlist",
        listdata : {},
        method : "scroll",
        listcallback : function(xhr) {
            // $("#chatPsnSearchName").val("");
            if (type == 1) {
                $("*[list-main='msg_chat_psn_list']").find(".main-list__item")
                        .eq(0).click();
            } else {
                $("*[list-main='msg_chat_psn_list']").find(".main-list__item")
                        .eq(0).addClass("item_selected");
            }
        },
    });
}
// 站内信 获取消息人列表
MsgBase.getChatPsnList = function() {
    MsgBase.msgchatpsnlist = window.Mainlist({
        name : "msg_chat_psn_list",
        listurl : "/dynweb/showmsg/ajaxgetchatpsnlist",
        listdata : {},
        method : "scroll",
        listcallback : function(xhr) {
            var list = $("*[list-main='msg_chat_psn_list']");
            if ((des3ChatPsnId == null || des3ChatPsnId == "")) {// 不是从其他页面跳转过来就默认点击第一个人
                if (list.find(".main-list__item").length < 11) {// 只在第一次加载列表时点击
                    list.find(".main-list__item").eq(0).click();
                }
            } else {// 是从其他页面跳转过来：
                $("*[scm_id='chatPsnInfo']").attr("scm_chatPsnNo",
                        des3ChatPsnId);
                des3ChatPsnId = "";
                setTimeout(function() {
                    list.find(".main-list__item").eq(0).click();
                }, 100);
            }
        },
    });
}
// 站内信 消息人列表 事件
MsgBase.getChatPsnListEvent = function(des3ChatPsnId, obj) {
    $("*[list-main='msg_chat_content_list']").unbind("scroll",
            MsgBase.ChatContentScroll);
    $(obj).find(".has-unread-messages ").removeClass("has-unread-messages");
    if (des3ChatPsnId == "") {
        des3ChatPsnId = $(obj).attr("code");
    }
    $(obj).closest(".main-list__list").find(".main-list__item").removeClass(
            "item_selected");
    $(obj).addClass("item_selected");
    $("#msg_friends").hide();
    $("*[scm_id='chatPsnInfo']").html(
            $(obj).find(".inbox-messenger__psn-name").html());
    $("*[scm_id='chatPsnInfo']").attr("scm_chatPsnNo", des3ChatPsnId);
    MsgBase.getChatContentList(des3ChatPsnId);
}
MsgBase.ajaxEventArr = [];

/**
 * @author匹配消息列表中的网址
 */
MsgBase.msgBaseMatchUrl = function(str) {
    var Match = function(str, index, lastIndex) {
        this.str = str; // 匹配到的url
        this.index = index; // 匹配到的字符串在原字符串中的起始位置
        this.lastIndex = lastIndex; // 匹配到的字符串在原字符串中的终止位置
    };
    /**
     * 匹配http://|https://
     */
    var regexp1 = new RegExp('(http:\/\/|https:\/\/)', 'g');
    var matchArray = new Array();
    var matchStr;
    while ((matchStr = regexp1.exec(str)) != null) {
        matchArray.push(new Match(matchStr[0], matchStr.index,
                regexp1.lastIndex));
        /*
         * console.log(match) console.log(regexp1.lastIndex - match.index)
         */
    }
    /**
     * 匹配网址分隔符，空白字符或非单字节字符及特殊标点符号,;`·"|'做分隔
     */
    var regexp2 = /<br>|\s|[^\u0000-\u00FF]|[,;`·"\|']/g;
    for (var i = 0; i < matchArray.length; i++) {
        // 考虑内容结束但没有分隔符的情况
        var endIndex = matchArray[i + 1] ? matchArray[i + 1].index : str.length;
        var substr = str.substring(matchArray[i].lastIndex, endIndex);
        if ((matchStr = regexp2.exec(substr)) != null) { // 匹配到分隔符
            if (regexp2.lastIndex > 1) { // 提取网址
                if (matchStr[0] == '<br>') {
                    matchArray[i].lastIndex += regexp2.lastIndex - 4;
                } else {
                    matchArray[i].lastIndex += regexp2.lastIndex - 1;
                }
                matchArray[i].str = str.substring(matchArray[i].index,
                        matchArray[i].lastIndex);
            } else { // 考虑"http:// "这样的情况
                matchArray.splice(i, 1);
            }
        } else if (endIndex == str.length) { // 考虑内容末尾无空白符的情况
            if (matchArray[i].lastIndex < endIndex) { // 提取网址
                matchArray[i].lastIndex = endIndex;
                matchArray[i].str = str
                        .substring(matchArray[i].index, endIndex);
            } else { // 考虑内容末尾无空白符，但也没有实际网址的情况，如："http://"
                matchArray.splice(i, 1);
            }
        }// 考虑内容并未到结尾，无空白符的情况，如:http://baidu.comhttp://sina.com，这种情况识别为一个网址
        else if (matchArray[i + 1] && (endIndex == matchArray[i + 1].index)) {
            matchArray.splice(i + 1, 1);
            i = i - 1;
        } else { // 其他情况
            matchArray.splice(i, 1);
        }
        // console.log(matchStr);
        regexp2.lastIndex = 0;
    }
    return matchArray;
}

// 发表的动态中含有网址的话展示时显示网页链接
MsgBase.msgBaseTransUrl = function() {
    var msgList = $("*[list-main='msg_chat_content_list']").find(
            '.inbox-chat__record_text');
    if (typeof msgList != "undefined" && typeof msgList != "") {
        msgList
                .each(function() {
                    if ($(this).children().length == 0) {
                        var $div = $(this).text();
                        var c = $div.trim();
                        var matchArray = MsgBase.msgBaseMatchUrl(c);
                        var newstr = "";
                        for (var i = 0; i <= matchArray.length; i++) {
                            var beginIndex = i == 0 ? 0
                                    : matchArray[i - 1].lastIndex;
                            var endIndex = i == matchArray.length ? c.length
                                    : matchArray[i].index;
                            var stri = c.substring(beginIndex, endIndex);
                            var urli = "";
                            if (i < matchArray.length) {
                                urli = " <a href=\""
                                        + matchArray[i].str
                                        + "\" style=\"color: #005eac !important;\" target=\"_blank\">"
                                        + matchArray[i].str + "</a> ";
                            }
                            newstr += stri + urli;
                        }
                        $(this).html(newstr);
                    }
                });
    }
}

// 站内信 获取消息列表-首次调用使用
MsgBase.getChatContentList = function(des3ChatPsnId) {
    $("*[scm_id='load_state_ico']").remove();
    $("*[list-main='msg_chat_content_list']").doLoadStateIco({
        style : "height: 38px; width:38px; margin:auto ; margin-top:10px;",
        status : 1,
        addWay : "html"
    });
    MsgBase.stopAJAX();
    var ajaxgetChatContentList = $
            .ajax({
                url : "/dynweb/showmsg/ajaxgetmsglist",
                dataType : "html",
                type : "post",
                data : {
                    "msgType" : "7",
                    "des3ChatPsnId" : des3ChatPsnId
                },
                success : function(data) {
                    MsgBase
                            .ajaxTimeOut(
                                    data,
                                    function() {
                                        var msg_chat_content = $("*[list-main='msg_chat_content_list']");
                                        msg_chat_content.html(data);
                                        MsgBase.msgBaseTransUrl();
                                        if (msg_chat_content
                                                .find(".dev_inbox-chat-item").length == 0) {
                                            msg_chat_content
                                                    .append('<div class="no_effort"><h2 style="line-height: 86px;">抱歉，暂无任何聊天内容</h2></div>');
                                        }
                                        MsgBase.addChatContentDate(10);
                                        const scm_totalcount = $(
                                                "*[scm_id='chat_content_page']")
                                                .eq(0).attr("scm_totalcount");
                                        const newItemCount = $(
                                                "*[list-main='msg_chat_content_list']")
                                                .find(".dev_inbox-chat-item").length;
                                        if (parseInt(scm_totalcount) <= parseInt(newItemCount)) {
                                            MsgBase.getChatPsnCard();
                                            $(
                                                    "*[list-main='msg_chat_content_list']")
                                                    .unbind(
                                                            "scroll",
                                                            MsgBase.ChatContentScroll);
                                            return;
                                        }
                                        if (parseInt(scm_totalcount) > parseInt(newItemCount)
                                                && msg_chat_content[0].scrollHeight <= msg_chat_content[0].clientHeight) {
                                            MsgBase.ChatContentScroll(1);
                                        }
                                        setTimeout(
                                                function() {
                                                    // 站内信滚动事件绑定
                                                    msg_chat_content
                                                            .unbind(
                                                                    "scroll",
                                                                    MsgBase.ChatContentScroll);
                                                    msg_chat_content
                                                            .bind(
                                                                    "scroll",
                                                                    MsgBase.ChatContentScroll);
                                                    MsgBase.ChatContentScrollRunning = false;
                                                }, 1);
                                        msg_chat_content.scrollTop(2000);
                                        $("*[scm_id='load_state_ico']")
                                                .remove();
                                    });
                }
            });
    MsgBase.ajaxEventArr.push(ajaxgetChatContentList);
}
MsgBase.stopAJAX = function() {
    MsgBase.ajaxEventArr.forEach(function(o, i) {
        o.abort();
        MsgBase.ajaxEventArr.splice(i, 1);
    });
}
// 站内信 获取消息列表-
MsgBase.chatContentList = function(des3ChatPsnId, myfunction) {
    MsgBase.stopAJAX();
    var ajaxgetChatContentList = $
            .ajax({
                url : "/dynweb/showmsg/ajaxgetmsglist",
                dataType : "html",
                type : "post",
                data : {
                    "msgType" : "7",
                    "des3ChatPsnId" : des3ChatPsnId
                },
                success : function(data) {
                    MsgBase
                            .ajaxTimeOut(
                                    data,
                                    function() {
                                        var msg_chat_content = $("*[list-main='msg_chat_content_list']");
                                        msg_chat_content.html(data);
                                        MsgBase.msgBaseTransUrl();
                                        MsgBase.addChatContentDate(10);
                                        const scm_totalcount = $(
                                                "*[scm_id='chat_content_page']")
                                                .eq(0).attr("scm_totalcount");
                                        const newItemCount = $(
                                                "*[list-main='msg_chat_content_list']")
                                                .find(".dev_inbox-chat-item").length;
                                        console.log("首次调用-2成功回调");
                                        if (parseInt(scm_totalcount) <= parseInt(newItemCount)) {
                                            MsgBase.getChatPsnCard();
                                            $(
                                                    "*[list-main='msg_chat_content_list']")
                                                    .unbind(
                                                            "scroll",
                                                            MsgBase.ChatContentScroll);
                                            return;
                                        }
                                        MsgBase.ChatContentScroll(1);
                                        msg_chat_content.unbind("scroll",
                                                MsgBase.ChatContentScroll);
                                        msg_chat_content.bind("scroll",
                                                MsgBase.ChatContentScroll);
                                        MsgBase.ChatContentScrollRunning = false;
                                        if (myfunction != null
                                                && typeof myfunction == "function") {
                                            myfunction();
                                        }
                                        $("*[scm_id='load_state_ico']")
                                                .remove();
                                    });
                }
            });
    MsgBase.ajaxEventArr.push(ajaxgetChatContentList);
}
MsgBase.addChatContentDate = function(count) {
    var msg_chat_content = $("*[list-main='msg_chat_content_list']");
    var date;
    var nextObj;
    msg_chat_content
            .find(".dev_inbox-chat-item:lt(" + count + ")")
            .each(
                    function(i, o) {
                        if ($(o).index() == 1) {
                            $(o).before(
                                    "<div class='inbox-chat__date' chat__date='"
                                            + $(o).attr("dev_create_date")
                                            + "'>"
                                            + $(o).attr("dev_create_date")
                                            + "</div>");
                        }
                        date = $(o).attr("dev_create_date");
                        nextObj = msg_chat_content.find(".dev_inbox-chat-item")
                                .eq(i + 1);
                        // 原来逻辑是600000==10min 改需求为2h == 7200000
                        if (nextObj.length > 0
                                && MsgBase
                                        .isToday(nextObj
                                                .attr("dev_create_date"), date,
                                                7200000)) {
                            if (msg_chat_content
                                    .find(".inbox-chat__date[chat__date='"
                                            + nextObj.attr("dev_create_date")
                                            + "']").length == 0) {
                                $(o)
                                        .after(
                                                "<div class='inbox-chat__date' chat__date='"
                                                        + nextObj
                                                                .attr("dev_create_date")
                                                        + "'>"
                                                        + nextObj
                                                                .attr("dev_create_date")
                                                        + "</div>");
                            }
                        }
                    });
    msg_chat_content.scrollTop(2000);
}
// 21600000=6*60*60*1000 ：6小时间隔
MsgBase.isToday = function(str, str2, time) {
    if (new Date(str.replace(/-/g, "/")) - new Date(str2.replace(/-/g, "/")) > time) {
        return true;
    } else {
        return false;
    }
}
MsgBase.getChatPsnId = function() {
    var chatPsnId = $("*[scm_id='chatPsnInfo']").attr("scm_chatPsnNo");
    if (chatPsnId == null || chatPsnId == "") {
        chatPsnId = $("#msg_friends").find(".chip__box").attr("code");
        if (chatPsnId == null || chatPsnId == "") {
            return "";
        }
    }
    return chatPsnId;
}
MsgBase.getChatPsnCard = function() {
    var chatPsnId = MsgBase.getChatPsnId();
    $
            .ajax({
                url : "/groupweb/grpmember/ajaxgetchatpsncard",
                dataType : "html",
                type : "post",
                data : {
                    des3TargetPsnId : chatPsnId
                },
                success : function(data) {
                    MsgBase
                            .ajaxTimeOut(
                                    data,
                                    function() {
                                        var content_list_obj = $("*[list-main='msg_chat_content_list']");
                                        content_list_obj.find(
                                                ".inbox-chat__namecard")
                                                .remove();
                                        content_list_obj.prepend(data);
                                        // 当存在人员信息时，也要滚动到最后一条信息
                                        content_list_obj.scrollTop(2000);
                                        $("*[scm_id='load_state_ico']")
                                                .remove();
                                    });
                }
            });

}
// 站内信滚动事件
MsgBase.ChatContentScroll = function(first) {
    var content_list_obj = $("*[list-main='msg_chat_content_list']");
    const $listHeight = content_list_obj[0].scrollHeight;
    console.log("scrollTop=" + content_list_obj.scrollTop());
    console.log("ChatContentScrollRunning=" + MsgBase.ChatContentScrollRunning);
    if (first == 1
            || (content_list_obj.scrollTop() <= 200 && !MsgBase.ChatContentScrollRunning)) {
        $("*[scm_id='load_state_ico']").remove();
        content_list_obj.doLoadStateIco({
            style : "height: 38px; width:38px; margin:auto ; margin-top:10px;",
            status : 1,
            addWay : "prepend"
        });
        MsgBase.ChatContentScrollRunning = true;
        var chatPsnId = MsgBase.getChatPsnId();
        var chat_pate = $("*[scm_id='chat_content_page']").eq(0);
        var pageNo = $("*[scm_id='chat_content_page']").eq(0)
                .attr("scm_pageno");
        var pagePageNo = 1;
        if (pageNo != null) {
            pagePageNo = (Number(pageNo) + 1);
        }
        var scm_pagetotalPages = chat_pate.attr("scm_pagetotalPages");
        MsgBase.stopAJAX();
        var ajaxgetChatContentList = $
                .ajax({
                    url : "/dynweb/showmsg/ajaxgetmsglist",
                    dataType : "html",
                    type : "post",
                    data : {
                        "page.pageNo" : pagePageNo,
                        msgType : "7",
                        des3ChatPsnId : chatPsnId
                    },
                    success : function(data) {
                        MsgBase
                                .ajaxTimeOut(
                                        data,
                                        function() {
                                            MsgBase.ChatContentScrollRunning = false;
                                            $("*[scm_id='chat_content_page']")
                                                    .remove();
                                            content_list_obj.prepend(data);
                                            MsgBase.msgBaseTransUrl();
                                            const scm_totalcount = chat_pate
                                                    .attr("scm_totalcount");
                                            const newItemCount = $(
                                                    "*[list-main='msg_chat_content_list']")
                                                    .find(
                                                            ".dev_inbox-chat-item").length;
                                            if (parseInt(scm_totalcount) <= parseInt(newItemCount)) {
                                                MsgBase.getChatPsnCard();
                                                $(
                                                        "*[list-main='msg_chat_content_list']")
                                                        .unbind(
                                                                "scroll",
                                                                MsgBase.ChatContentScroll);
                                            }
                                            MsgBase.addChatContentDate(10);
                                            if (first == 1) {
                                                content_list_obj
                                                        .scrollTop(2000);
                                            } else {
                                                content_list_obj[0].scrollTop = content_list_obj[0].scrollHeight
                                                        - $listHeight - 48;
                                            }
                                            $("*[scm_id='load_state_ico']")
                                                    .remove();
                                        });
                    }
                });
        MsgBase.ajaxEventArr.push(ajaxgetChatContentList);
    }
}
// 站内信 具体发送文件ajax
MsgBase.sendMyFileListTopsn = function(stationFileIdStr, chatPsnId, newChat) {
    $.ajax({
        url : "/dynweb/showmsg/ajaxsendmsg",
        dataType : "json",
        type : "post",
        async : false,
        data : {
            msgType : "7",
            receiverIds : chatPsnId,
            des3FileId : stationFileIdStr,
            smateInsideLetterType : "file",
            belongPerson : "true"
        },
        success : function(result) {
            MsgBase.ajaxTimeOut(result, function() {
                if (result.status == "success") {
                    MsgBase.indexB++;
                } else {
                    scmpublictoast(result.msg, 2000);
                }
            });
        }
    });
}
// 站内信 具体发送成果ajax
MsgBase.sendMyPubListTopsn = function(stationPubIdStr, chatPsnId, newChat) {
    $.ajax({
        url : "/dynweb/showmsg/ajaxsendmsg",
        dataType : "json",
        type : "post",
        async : false,
        data : {
            msgType : "7",
            receiverIds : chatPsnId,
            pubId : stationPubIdStr,
            smateInsideLetterType : "pub",
            belongPerson : "true"
        },
        success : function(result) {
            MsgBase.ajaxTimeOut(result, function() {
                if (result.status == "success") {
                    MsgBase.indexB++;
                } else {
                    scmpublictoast(result.msg, 2000);
                }
            });
        }
    });
}
// 站内信 发送成果
MsgBase.doSendChatPub = function() {
    var newChat = false;
    var chatPsnId = $("*[scm_id='chatPsnInfo']").attr("scm_chatPsnNo");
    if (chatPsnId == null || chatPsnId == "") {
        chatPsnId = $("#msg_friends").find(".chip__box").attr("code");
        if (chatPsnId == null || chatPsnId == "") {
            scmpublictoast(msgBase.pleaseSelectSender, 2000);
            return;
        }
        newChat = true;
    }
    var stationPubIdStr = "";
    MsgBase.indexA = 0;
    MsgBase.indexB = 0;
    // 遍历
    $("#myPubListId").find(".input-custom-style > input:checked ").each(
            function(i, o) {
                stationPubIdStr = $(o).closest(".main-list__item")
                        .attr("pubid");
                if ($.trim(stationPubIdStr) != "") {
                    MsgBase.indexA++;
                    indexB = MsgBase.sendMyPubListTopsn(stationPubIdStr,
                            chatPsnId, newChat);
                }
            });
    if (MsgBase.indexA != 0) {
        var myInterval = setInterval(function() {
            if (MsgBase.indexA == MsgBase.indexB) {
                MsgBase.refGetChatPsnList();
                MsgBase.getChatContentList(chatPsnId);
                MsgBase.hideMsgPubUI();
                window.clearInterval(myInterval)
            }
        }, 500);
    }
}
// 站内信 发送我的文件库选择的文件
MsgBase.doSendChatMyFile = function() {
    var newChat = false;
    var chatPsnId = $("*[scm_id='chatPsnInfo']").attr("scm_chatPsnNo");
    if (chatPsnId == null || chatPsnId == "") {
        chatPsnId = $("#msg_friends").find(".chip__box").attr("code");
        if (chatPsnId == null || chatPsnId == "") {
            scmpublictoast(msgBase.pleaseSelectSender, 2000);
            return;
        }
        newChat = true;
    }
    var stationFileIdStr = "";
    MsgBase.indexA = 0;
    MsgBase.indexB = 0;
    $("#psnFileMyFileListId")
            .find(".main-list__item")
            .each(
                    function(i) {
                        var obj = $(this)
                                .find(
                                        ".main-list__item_checkbox > .input-custom-style > input");
                        if (obj[0].checked == true && obj[0].disabled != true) {
                            stationFileIdStr = $(this).attr("des3FileId");
                            if ($.trim(stationFileIdStr) != "") {
                                MsgBase.indexA++;
                                indexB = MsgBase.sendMyFileListTopsn(
                                        stationFileIdStr, chatPsnId, newChat);
                            }
                        }
                    });
    if (MsgBase.indexA != 0) {// 多选文件发送时统一回调事件
        var myInterval = setInterval(function() {
            if (MsgBase.indexA == MsgBase.indexB) {
                MsgBase.refGetChatPsnList();
                MsgBase.getChatContentList(chatPsnId);
                MsgBase.hideMyFileSelectDialog();
                window.clearInterval(myInterval)
            }
        }, 500);
    }
}

// 站内信，发送本地选择上传的文件
MsgBase.doSendChatLocalFileForShare = function(des3FileId) {
    var newChat = false;
    var chatPsnId = $("*[scm_id='chatPsnInfo']").attr("scm_chatPsnNo");
    if (chatPsnId == null || chatPsnId == "") {
        chatPsnId = $("#msg_friends").find(".chip__box").attr("code");
        if (chatPsnId == null || chatPsnId == "") {
            scmpublictoast(msgBase.pleaseSelectSender, 2000);
            return;
        }
        newChat = true;
    }
    var stationFileIdStr = des3FileId;
    MsgBase.indexB = 0;
    MsgBase.sendMyFileListTopsn(stationFileIdStr, chatPsnId, newChat);

    MsgBase.refGetChatPsnList();
    MsgBase.getChatContentList(chatPsnId);
    MsgBase.hideLocalFileSelectDialog();

}

// 站内信，发送本地选择上传的文件
MsgBase.doSendChatLocalFile = function(des3FileId) {
    var newChat = false;
    var chatPsnId = $("*[scm_id='chatPsnInfo']").attr("scm_chatPsnNo");
    if (chatPsnId == null || chatPsnId == "") {
        chatPsnId = $("#msg_friends").find(".chip__box").attr("code");
        if (chatPsnId == null || chatPsnId == "") {
            scmpublictoast(msgBase.pleaseSelectSender, 2000);
            return;
        }
        newChat = true;
    }
    var stationFileIdStr = des3FileId;
    MsgBase.indexB = 0;
    MsgBase.sendMyFileListTopsn(stationFileIdStr, chatPsnId, newChat);
    var myInterval = setInterval(function() {
        if (MsgBase.indexB == 1) {
            MsgBase.refGetChatPsnList();
            MsgBase.getChatContentList(chatPsnId);
            MsgBase.hideLocalFileSelectDialog();
            window.clearInterval(myInterval)
        }
    }, 500);
}
// 站内信 发送文本消息
MsgBase.doSendChatInfo = function() {
    var newChat = false;
    var chatPsnId = $("*[scm_id='chatPsnInfo']").attr("scm_chatPsnNo");
    if (chatPsnId == null || chatPsnId == "") {
        chatPsnId = $("#msg_friends").find(".chip__box").attr("code");
        if (chatPsnId == null || chatPsnId == "") {
            scmpublictoast(msgBase.pleaseSelectSender, 2000);
            return;
        }
        newChat = true;
    }
    var sendContent = $("#sendContent").val().trim();
    if (sendContent == "") {
        return;
    }
    if (sendContent.length > 500) {
        scmpublictoast("文本消息最多输入500个字符", 1000);
        return;
    }
    $.ajax({
        url : "/dynweb/showmsg/ajaxsendmsg",
        dataType : "json",
        type : "post",
        data : {
            msgType : "7",
            receiverIds : chatPsnId,
            content : $("#sendContent").val(),
            smateInsideLetterType : "text"
        },
        success : function(result) {
            MsgBase.ajaxTimeOut(result, function() {
                if (result.status == "success") {
                    MsgBase.getChatContentList(chatPsnId);
                    $("#sendContent").val("");
                    MsgBase.refGetChatPsnList();
                } else {
                    scmpublictoast(result.msg, 2000);
                }

            });

        }
    });
}

/**
 * dealStatus:1==同意 2==拒绝。 增加状态3==上传全文（hcj添加） obj== this pubIdObj：成果id
 */
MsgBase.optFulltextRequest = function(msgRelationId, dealStatus, obj, pubIdObj) {
    if (dealStatus != 1 && dealStatus != 2 && dealStatus != 3) {
        return;
    }
    var pubId = "";
    if (pubIdObj != undefined) {
        pubId = pubIdObj;
    } else {
        pubId = $(obj).closest(".main-list__item").attr("id");
    }
    // 处理全文请求
    $.ajax({
        url : '/pub/fulltext/ajaxrequpdate',
        type : 'post',
        data : {
            'msgId' : msgRelationId,
            'pubId' : pubId,
            'dealStatus' : dealStatus
        },
        dataType : "json",
        success : function(data) {
            BaseUtils.ajaxTimeOut(data, function() {
                if (data.status == "success") {
                    smate.msgcount.header();
                    scmpublictoast(data.msg, 1000);
                    // 重新刷新页面
                    MsgBase.menuClick('reqFullTextMsg');
                } else {
                    scmpublictoast(data.msg, 2000);
                }
            });
        },
        error : function() {

        }
    });
};
MsgBase.collectionStationFile = function(des3FileId, obj) {
    MsgBase.doHitMore(obj, 2000);
    var dataJson = {
        'des3FileId' : des3FileId
    };
    $.ajax({
        url : '/psnweb/myfile/ajaxcollectionstationfile',
        type : 'post',
        dataType : 'json',
        data : dataJson,
        success : function(data) {
            MsgBase.ajaxTimeOut(data, function() {
                if (data.result == "success") {
                    scmpublictoast(msgBase.saveSuccess, 1000);
                } else if (data.result == "error") {
                    scmpublictoast(msgBase.saveFail, 1000);
                }
            });
        },
        error : function() {
        }
    });
}

/**
 * 打开群组文件
 */
MsgBase.openFile = function(fileId, obj) {
    MsgBase.doHitMore(obj, 2000);
    window.location.href = snsctx + "/file/download?des3Id=" + fileId
            + "&nodeId=0&type=1&flag=5";
};
// 消息中心 下载个人文件 SCM-14409 hcj
MsgBase.downloadFile = function(url, obj) {
    BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function() {
        MsgBase.doHitMore(obj, 2000);
        window.location.href = url;
    }, 1);

}

// ---防止重复点击--zzx--
MsgBase.doHitMore = function(obj, time) {
    $(obj).attr("disabled", true);
    var click = $(obj).attr("onclick");
    if (click != null && click != "") {
        $(obj).attr("onclick", "");
    }
    setTimeout(function() {
        $(obj).removeAttr("disabled");
        if (click != null && click != "") {
            $(obj).attr("onclick", click);
        }
    }, time);
}
// 超时处理
MsgBase.ajaxTimeOut = function(data, myfunction) {
    var toConfirm = false;

    if ('{"ajaxSessionTimeOut":"yes"}' == data) {
        toConfirm = true;
    }
    if (!toConfirm && data != null) {
        toConfirm = data.ajaxSessionTimeOut;
    }
    if (toConfirm) {
        jConfirm(msgBase.timeout, msgBase.tips, function(r) {
            if (r) {
                document.location.href = window.location.href;
                return 0;
            }
        });

    } else {
        if (typeof myfunction == "function") {
            myfunction();
        }
    }
}
// 冒泡事件处理
MsgBase.stopNextEvent = function(evt) {
    if (evt.currentTarget) {
        if (evt.stopPropagation) {
            evt.stopPropagation();
        } else {
            evt.cancelBubble = true;
        }
    }
}
// 站内信 会话删除
MsgBase.delMsgChatRelation = function(msgChatRelationId, evt) {
    if (evt) {
        MsgBase.stopNextEvent(evt);
    }

    if (msgChatRelationId == null || msgChatRelationId == "") {
        return;
    }
    $.ajax({
        url : '/dynweb/showmsg/ajaxdelmsgchatrelation',
        type : 'post',
        dataType : 'json',
        data : {
            msgChatRelationId : msgChatRelationId
        },
        success : function(data) {
            MsgBase.ajaxTimeOut(data, function() {
                MsgBase.refGetChatPsnList(1);
            });
        },
        error : function() {
        }
    });
}
// 站内信 内容删除
MsgBase.delMsgContent = function(msgRelationId, e) {
    var chatPsnId = $("*[scm_id='chatPsnInfo']").attr("scm_chatPsnNo");
    if (chatPsnId == null || chatPsnId == "") {
        chatPsnId = $("#msg_friends").find(".chip__box").attr("code");
        if (chatPsnId == null || chatPsnId == "") {
            return;
        }
    }
    MsgBase.stopNextEvent(e);
    $.ajax({
        url : "/dynweb/showmsg/ajaxdelchatmsg",
        dataType : "json",
        type : "post",
        data : {
            msgRelationId : msgRelationId,
            des3SenderId : chatPsnId
        },
        success : function(data) {
            MsgBase.ajaxTimeOut(data, function() {
                var selectObj = $("*[list-main='msg_chat_psn_list']").find(
                        ".item_selected");
                if (selectObj.length > 0) {
                    selectObj.click();
                } else {
                    MsgBase.getChatContentList(chatPsnId);
                }
            });

        }
    });
}

/**
 * status:消息状态：0=未读、1=已读、2=删除 msgType:消息类型 11全文请求 clickMenu
 * :点击的菜单，比如reqFullTextMsg=全文请求菜单
 */
MsgBase.showFulltextRequestMsgList = function() {
    /*
     * $("#fulltextRequestListId").doLoadStateIco({ style : "height: 38px;
     * width:38px; margin:auto ; margin-top:10px;", status : 1 });
     */
    var dataJson = {
        "status" : 0,
        "msgType" : 11
    };
    MsgBase.myFulltextRequestMsglist = window.Mainlist({
        name : "fulltextRequestList",
        listurl : "/dynweb/showmsg/ajaxgetmsglist",
        listdata : dataJson,
        method : "scroll",
        listcallback : function(xhr) {
            // MsgBase.initFulltextUpload() ;
            smate.msgcount.header();
        },
    });

};
// 站内信成果全文下载
MsgBase.fullTextDownload = function(des3PubId) {
    // 没有成果类型，所以 暂时先这么判断
    $.post('/fileweb/download/ajaxgeturl', {
        "des3Id" : des3PubId,
        "type" : 2
    }, function(result) {
        if (result && result.status == "success") {
            location.href = result.data.url;
        } else {
            $.post('/fileweb/download/ajaxgeturl', {
                "des3Id" : des3PubId,
                "type" : 3
            }, function(result) {
                if (result && result.status == "success") {
                    location.href = result.data.url;
                } else {
                    scmpublictoast(msgBase.downloadDisabledTip, 1000);
                }
            });
        }
    });
}

/**
 * 新建消息--联系人列表事件
 */
MsgBase.showShareToScmSelectFriendBox = function() {
    showDialog("share_to_scm_select_friend_box");
    MsgBase.loadFriendList();
};
MsgBase.hideShareToScmSelectFriendBox = function() {
    // 清空检索框
    var msgFriendMainInputId = $("#msgFriendMainInputId");
    if (msgFriendMainInputId.length > 0) {
        msgFriendMainInputId.val("");
    }
    hideDialog("share_to_scm_select_friend_box");
};
MsgBase.loadFriendList = function(order) {
    $("#id_grp_add_friend_names_list").html("");
    $("#id_grp_add_friend_names_list").doLoadStateIco({
        style : "height:28px; width:28px; margin:auto;margin-top:24px;",
        status : 1
    });

    var orderBy = "";
    if (order != undefined) {
        orderBy = order;
    } else {
        orderBy = $("#share_to_scm_select_friend_box")
                .find(".dev_select_order").val();
    }
    $("#id_grp_add_friend_names_list").html("");
    $("#id_grp_add_friend_names_list").doLoadStateIco({
        style : "height:28px; width:28px; margin:auto;margin-top:24px;",
        status : 1
    });
    var searchKey = $("#share_to_scm_select_friend_box")
            .find(".dev_search_key").val();

    $.ajax({
        url : "/psnweb/friend/ajaxgetmyfriendnames",
        type : 'post',
        dataType : 'html',
        data : {
            "type" : 1,
            "searchKey" : searchKey,
            "orderBy" : orderBy
        },
        success : function(data) {
            MsgBase.ajaxTimeOut(data, function() {
                $("#id_grp_add_friend_names_list").html(data);

                if ($("#id_grp_add_friend_names_list").find(
                        ".friend-selection__item-3").length == 0) {
                    $("#id_grp_add_friend_names_list").addClass(
                            "main-list__list");
                    $("#id_grp_add_friend_names_list").html(
                            "<div class='response_no-result'>"
                                    + msgBase.i18n_noSearchFriend + "</div>");
                } else {
                    $("#id_grp_add_friend_names_list").removeClass(
                            "main-list__list");
                }

                var code = $("#msg_friends").find(".chip__box").attr("code");
                if (code != null && code != "") {
                    $("#id_grp_add_friend_names_list").find(
                            ".psn-idx__main_name[code='" + code + "']")
                            .closest(".psn-idx_small").addClass(" psn_chosen");
                }
                $("#id_grp_add_friend_names_list").find(".psn-idx_small")
                        .click(
                                function() {
                                    $("#id_grp_add_friend_names_list").find(
                                            ".psn_chosen").removeClass(
                                            "psn_chosen");
                                    $(this).addClass("psn_chosen");
                                });
            });
        },
        error : function() {
        }
    });
};

MsgBase.loadFriendListByOrder = function(order) {
    MsgBase.loadFriendList(order);
};

MsgBase.clickFriendName = function() {
    // 清空检索框

    var msgFriendMainInputId = $("#msgFriendMainInputId");
    if (msgFriendMainInputId.length > 0) {
        msgFriendMainInputId.val("");
    }
    var nameObj = $("#id_grp_add_friend_names_list").find(".psn_chosen");
    var name = nameObj.find(".psn-idx__main_name").html();
    var des3PsnId = nameObj.find(".psn-idx__main_name").attr("code");
    var avatar_img = nameObj.find(".psn-idx__avatar_img").html();
    if (des3PsnId == null || des3PsnId == "") {
        MsgBase.hideShareToScmSelectFriendBox();
        return;
    }
    var name = "<div class='chip__box' code='"
            + des3PsnId
            + "'>"
            + "<div class='chip__avatar'>"
            + avatar_img
            + "</div>"
            + "<div class='chip__text'>"
            + name
            + "</div>"
            + "<div class='chip__icon icon_delete' onclick='MsgBase.calFriendName(this)'>"
            + "<i class='material-icons'>close</i>" + "</div>" + "</div>";
    $("#msg_friends").find(".chip__box").remove();
    $("#msg_friends").prepend(name);
    MsgBase.hideShareToScmSelectFriendBox();
    // 清除检索水印
    if ($("#msg_friends").find(".chip__box").length > 0) {
        $("#msg_friends").find(".chip-panel__Prompt").hide();
    }
    if ($("#msg_friends").find(".chip-panel__manual-input").length > 0) {
        $("#msg_friends").find(".chip-panel__manual-input").hide();
    }
}
MsgBase.calFriendName = function(obj) {
    if ($(obj).closest(".chip-panel__box").find(".chip-panel__manual-input").length > 0) {
        $(obj).closest(".chip-panel__box").find(".chip-panel__manual-input")
                .show();
    }
    $(obj).closest(".chip__box").remove();
    updateTips();

}
// 改变url
MsgBase.changeUrl = function(targetModule) {
    var json = {};
    var oldUrl = window.location.href;
    var index = oldUrl.lastIndexOf("model");
    var newUrl = window.location.href;
    if (targetModule != undefined && targetModule != "") {
        if (oldUrl.lastIndexOf("?") < 0) {
            if (index < 0) {
                newUrl = oldUrl + "?model=" + targetModule;
            } else {
                newUrl = oldUrl.substring(0, index) + "model=" + targetModule;
            }
        } else {
            if (index < 0) {
                newUrl = oldUrl + "&model=" + targetModule;
            } else {
                newUrl = oldUrl.substring(0, index) + "model=" + targetModule;
            }
        }

    }
    window.history.replaceState(json, "", newUrl);
}

MsgBase.clickMsgType = function(type, param, e, obj) {
    var param = "";
    MsgBase.stopNextEvent(e);
    var url = "";
    switch (type) {
    case 1:// 联系人请求
        url = "/psnweb/friend/main";
        break;
    case 2:// 成果认领
        url = "/psnweb/homepage/show?jumpto=puball&module=pub";
        break;
    case 3:// 全文认领
        url = "/psnweb/homepage/show?jumpto=pubfulltextall&module=pub";
        break;
    case 4:// 推荐成果
        url = "/psnweb/homepage/show?jumpto=puball&module=pub";
        break;
    case 5:// 推荐人员
        url = "/psnweb/homepage/show?jumpto=pubCooperatorAll&module=pub";
        break;
    case 8:// 请求加入群组
        param = $(obj).find(".notification__text").attr("des3GrpId")
        if (param == null || param == "") {
            scmpublictoast("找不到对应群组信息!", 1000);
            return;
        }
        url = "/groupweb/grpinfo/main?des3GrpId=" + param
                + "&ispending=1&model=member";
        break;
    case 9:// 邀请加入群组
        url = "/groupweb/mygrp/main?menuId=3&model=myGrp";
        break;
    default:
        return;
    }
    window.open(url);
}
MsgBase.hrefUrl = function(url, e) {
    MsgBase.stopNextEvent(e);
    if (url != "") {
        window.open(url);
    }
}
// 站内信--发消息总入口
MsgBase.sendChatMsg = function(type, obj) {
    BaseUtils.doHitMore(obj, 2000);
    if (type == 1) {// 发送--文本
        MsgBase.doSendChatInfo();
    } else if (type == 2) {// 发送--文件库选择的多个文件
        MsgBase.doSendChatMyFile(obj);
    } else if (type == 3) {// 发送--成果
        MsgBase.doSendChatPub(obj);
    }
};

// 更新聊天人的记录
MsgBase.updateChatPsnLastRecode = function(obj) {
    var des3ChatPsnId = $(obj).attr("code");
    $.ajax({
        url : "/dynweb/showmsg/ajaxgetchatpsnLastRecord",
        dataType : "json",
        type : "post",
        data : {
            'des3ChatPsnId' : des3ChatPsnId
        },
        success : function(data) {
            MsgBase.ajaxTimeOut(data,
                    function() {
                        if (data.result == "success") {
                            $(obj).find(".inbox-messenger__newest-record")
                                    .html(data.contentNewest);
                            $(obj).find(".inbox-messenger__time").html(
                                    data.updateDate);
                        }
                    });

        }
    });
}
// 添加联系人 -直接添加
;
MsgBase.addFriend = function(des3TargetPsnId) {
    $.ajax({
        url : '/psnweb/friend/ajaxaddfriend',
        type : 'post',
        data : {
            'des3Id' : des3TargetPsnId
        },
        dataType : 'json',
        timeout : 10000,
        success : function(data) {
            MsgBase.ajaxTimeOut(data, function() {
                if (data.result == "true") {
                    scmpublictoast(msgBase.sentSuccess, 1000);
                } else {
                    scmpublictoast(data.msg, 1000);
                }
            });
        }
    });
}
// 初始化聊天检索框事件
MsgBase.ChatPsnListInputEvent = function() {
    var $input = $("#chatPsnSearchName");
    var $chatPsnList = $("*[list-main='msg_chat_psn_list']");
    var $chatFriendList = $('*[list-main="msg_friend_psn_list"]');
    var $chatAllList = $("*[list-main='msg_all_psn_list']");
    $input.on("focus", MsgBase.ChatPsnListInputSearch);
    $input.on("blur", MsgBase.ChatPsnListInputBlur);
    $input.on("keyup", MsgBase.ChatPsnListInputSearch);
}
// 进入焦点/按键抬起--检索联系人、全站列表-事件
MsgBase.ChatPsnListEvent = function(obj, ev) {
    BaseUtils.stopNextEvent(ev);
    var str = "<div class='chip__box' code='"
            + $(obj).attr("code")
            + "'>"
            + "<div class='chip__text'>"
            + $(obj).text()
            + "</div>"
            + "<div class='chip__icon icon_delete' onclick='MsgBase.calFriendName(this)'>"
            + "<i class='material-icons'>close</i>" + "</div>" + "</div>";
    $("#msg_friends").find(".chip__box").remove();
    $("#msg_addfriend").html("").before(str);
    $("#id_search_psn_list_ac__box").find(".ac__list").html("");
    // MsgBase.addNewChat($(obj).attr("code"));
}
// 进入焦点/按键抬起--检索联系人、全站列表
MsgBase.ChatPsnListInputSearch = function() {
    var $input = $("#chatPsnSearchName");
    var $inputtext = $.trim($input.val());
    var $chatPsnList = $("*[list-main='msg_chat_psn_list']");
    var $chatFriendList = $('*[list-main="msg_friend_psn_list"]');
    var $chatAllList = $("*[list-main='msg_all_psn_list']");
    $chatPsnList.hide();
    $("#msg_friend_all_list").show();
    $("#lab_all").show();
    $("#lab_friend").show();
    $chatFriendList.doLoadStateIco({
        style : "height:28px; width:28px; margin:auto;margin-top:24px;",
        status : 1
    });
    $chatAllList.html("");
    var result = 0;
    // 先请求联系人列表
    $
            .ajax({
                url : '/psnweb/friend/ajaxgetchatfriendlist',
                type : 'post',
                data : {
                    searchType : 0,
                    psnTypeForChat : "friend",
                    "page.pageSize" : "5",
                    searchKey : $inputtext,
                    "page.ignoreMin" : true
                },
                dataType : 'html',
                success : function(data) {
                    BaseUtils
                            .ajaxTimeOut(
                                    data,
                                    function() {
                                        $chatFriendList.html(data);
                                        if ("0" == $chatFriendList.find(
                                                ".js_listinfo").attr(
                                                "smate_count")) {
                                            $("#lab_friend").hide();
                                            result++;
                                        }
                                        // 再请求全站人员列表
                                        if ($.trim($input.val()) == "") {
                                            $("#lab_all").hide();
                                            $chatAllList.html("");
                                            return;
                                        }
                                        $chatAllList
                                                .doLoadStateIco({
                                                    style : "height:28px; width:28px; margin:auto;margin-top:24px;",
                                                    status : 1
                                                });
                                        $
                                                .ajax({
                                                    url : '/psnweb/search/ajaxmsgallpsnlist',
                                                    type : 'post',
                                                    data : {
                                                        searchType : 0,
                                                        searchString : $inputtext,
                                                        "page.pageSize" : "5",
                                                        "page.ignoreMin" : true
                                                    },
                                                    dataType : 'html',
                                                    success : function(data) {
                                                        BaseUtils
                                                                .ajaxTimeOut(
                                                                        data,
                                                                        function() {
                                                                            $chatAllList
                                                                                    .html(data);
                                                                            if ("0" == $chatAllList
                                                                                    .find(
                                                                                            ".js_listinfo")
                                                                                    .attr(
                                                                                            "smate_count")) {
                                                                                $(
                                                                                        "#lab_all")
                                                                                        .hide();
                                                                                result++;
                                                                            }
                                                                            if (result == 2) {
                                                                                $chatAllList
                                                                                        .html("<div class='response_no-result' >"
                                                                                                + msgBase.noRecord
                                                                                                + "</div>");
                                                                            }
                                                                        });
                                                    }
                                                });
                                    });
                }
            });
}
MsgBase.ChatPsnListInputSearch2 = function($inputtext) {
    var $input = $("#chatPsnSearchName");
    if ($inputtext == null || $.trim($inputtext) == "") {
        return;
    }
    var $chatPsnList = $("*[list-main='msg_chat_psn_list']");
    var $chatFriendList = $('*[list-main="msg_friend_psn_list"]');
    var $chatAllList = $("*[list-main='msg_all_psn_list']");
    $chatPsnList.hide();
    $("#msg_friend_all_list").show();
    $("#lab_all").show();
    $("#lab_friend").show();
    $chatFriendList.doLoadStateIco({
        style : "height:28px; width:28px; margin:auto;margin-top:24px;",
        status : 1
    });
    $chatAllList.html("");
    var result = 0;
    // 先请求联系人列表
    $
            .ajax({
                url : '/psnweb/friend/ajaxgetchatfriendlist',
                type : 'post',
                data : {
                    searchType : 0,
                    psnTypeForChat : "friend",
                    "page.pageSize" : "5",
                    searchKey : $inputtext,
                    "page.ignoreMin" : true
                },
                dataType : 'html',
                success : function(data) {
                    BaseUtils
                            .ajaxTimeOut(
                                    data,
                                    function() {
                                        $chatFriendList.html(data);
                                        if ("0" == $chatFriendList.find(
                                                ".js_listinfo").attr(
                                                "smate_count")) {
                                            $("#lab_friend").hide();
                                            result++;
                                        }
                                        $chatAllList
                                                .doLoadStateIco({
                                                    style : "height:28px; width:28px; margin:auto;margin-top:24px;",
                                                    status : 1
                                                });
                                        $
                                                .ajax({
                                                    url : '/psnweb/search/ajaxmsgallpsnlist',
                                                    type : 'post',
                                                    data : {
                                                        searchType : 0,
                                                        searchString : $inputtext,
                                                        "page.pageSize" : "5",
                                                        "page.ignoreMin" : true
                                                    },
                                                    dataType : 'html',
                                                    success : function(data) {
                                                        BaseUtils
                                                                .ajaxTimeOut(
                                                                        data,
                                                                        function() {
                                                                            $chatAllList
                                                                                    .html(data);
                                                                            if ("0" == $chatAllList
                                                                                    .find(
                                                                                            ".js_listinfo")
                                                                                    .attr(
                                                                                            "smate_count")) {
                                                                                $(
                                                                                        "#lab_all")
                                                                                        .hide();
                                                                                result++;
                                                                            }
                                                                            if (result == 2) {
                                                                                $chatAllList
                                                                                        .html("<div class='response_no-result'>"
                                                                                                + msgBase.noRecord
                                                                                                + "</div>");
                                                                            }
                                                                        });
                                                    }
                                                });
                                    });
                }
            });
}
// 失去焦点--会话列表
MsgBase.isChatPsnList = false;
MsgBase.ChatPsnListInputBlur = function() {
    var $input = $("#chatPsnSearchName");
    if ($input.val().length > 0) {
        return;
    }
    setTimeout(function() {
        $("*[list-main='msg_chat_psn_list']").show();
        $("#msg_friend_all_list").hide();
        if (MsgBase.isChatPsnList) {
            MsgBase.isChatPsnList = false;
            return;
        }
        MsgBase.msgchatpsnlist = window.Mainlist({
            name : "msg_chat_psn_list",
            listurl : "/dynweb/showmsg/ajaxgetchatpsnlist",
            listdata : {},
            method : "scroll",
            listcallback : function(xhr) {
            },
        });
    }, 300);
}
// 点击联系人列表、全站列表的新会话产生事件
MsgBase.ChatListClick = function(e, obj) {
    MsgBase.isChatPsnList = true;
    BaseUtils.stopNextEvent(e);
    BaseUtils.doHitMore(obj, 1000);
    $.ajax({
        url : '/dynweb/showmsg/ajaxcreatemsgchat',
        type : 'post',
        data : {
            'des3ReceiverId' : $(obj).attr("code")
        },
        dataType : 'json',
        success : function(data) {
            BaseUtils.ajaxTimeOut(data, function() {
                $("*[list-main='msg_chat_psn_list']").show();
                $("#msg_friend_all_list").hide();
                $("#chatPsnSearchName").val("");
                MsgBase.getChatPsnList();
            });
        }
    });
};

// --------------------------
/**
 * 上传全文弹出框
 */
MsgBase.SelectFulltextUpload = function(obj) {
    addFormElementsEvents();
    showDialog("msg_center_fulltext_upload");
    MsgBase.initSelectFulltextUpload(obj);
    smate.msgcount.header();
};

// 上传全文弹出框 取消
MsgBase.cancleFulltextUpload = function(e) {
    hideDialog("msg_center_fulltext_upload");
};
// 初始化 上传全文弹出框
MsgBase.initSelectFulltextUpload = function(obj) {
    // 上传文件重置
    var fileDoxDom = $("#fileupload").find(".fileupload__box").get(0);
    fileUploadModule.resetFileUploadBox(fileDoxDom);
    /*
     * var dataJson = {
     * allowType:'*.txt;*.jpg;*.jpeg;*.gif;*.pdf;*.doc;*.docx;*.png;*.html;*.xhtml;*.htm;*.rar;*.zip;',
     * pubId: $(obj).closest(".main-list__item").attr("id") };
     */
    var filetype = ".txt,.pdf,.doc,.docx,.ppt,.pptx,.xls,.xlsx,.rar,.zip,.jpg,.png,.gif,.jpeg,.htm,.html,.xhtml"
            .split(",");
    var data = {
        "fileurl" : "/fileweb/fileupload",
        "filedata" : {
            fileDealType : "generalfile"
        },
        "filecallback" : MsgBase.fulltextUploadcallback,
        "filecallbackparam" : {
            pubId : $(obj).closest(".main-list__item").attr("id")
        },
        "method" : "dropadd",
        "filetype" : filetype
    }
    fileUploadModule
            .initialization(document.getElementById("fileupload"), data);
};
// 上传全文，文件上传成功后回调
MsgBase.fulltextUploadcallback = function(xhr, params) {
    if (typeof (xhr) == "undefined" || !params) {
        return false;
    }
    const data = eval('(' + xhr.responseText + ')');
    var pubId = params.pubId;
    var des3fid = data.successDes3FileIds.split(",")[0];
    $.ajax({
        url : '/pub/opt/ajaxupdatefulltext',
        type : 'post',
        data : {
            'pubId' : pubId,
            'des3FileId' : des3fid
        },
        dataType : 'json',
        timeout : 10000,
        success : function(data) {
            MsgBase.ajaxTimeOut(data, function() {
                if (data.result == "true") {
                    var msgRelationId = $("#" + pubId).attr("msgRelationId");
                    MsgBase.optFulltextRequest(msgRelationId, 3, null, pubId);
                    MsgBase.cancleFulltextUpload();
                } else {
                    scmpublictoast('系统出现异常，请稍后再试', 2000);
                }
            });
        }
    });
}

// 上传文件回调 废弃
/*
 * MsgBase.fulltextUploadcallback =function(xhr){ const data =
 * eval('('+xhr.responseText+')'); var pubId = data.pubId ; var fileName =
 * data.fileName; if (fileName == null) { return; } var fileExt =
 * fileName.substring(fileName.lastIndexOf(".")); var fileDate =
 * data.createTime; var fileDesc = data.fileDesc; if (fileDesc == null) {
 * fileDesc = ""; } var file_id = data.fileId; if(file_id == ""){ return; }
 * //上传全文默认权限 var permission = 0;//0:所有人可下载 1:联系人 2:自己 $.ajax({ url:
 * '/pubweb/publication/ajaxupdatefulltext', type:'post',
 * data:{'pubId':pubId,'node_id':1,'actionType':"1",'file_id':file_id,'file_name':fileName,'file_desc':fileDesc,'file_ext':fileExt,'upload_date':fileDate,'permission':permission},
 * dataType:'json', timeout: 10000, success: function(data){
 * MsgBase.ajaxTimeOut(data , function(){ if(data.result == "true"){ var
 * msgRelationId = $("#"+pubId).attr("msgRelationId");
 * MsgBase.optFulltextRequest(msgRelationId, 3, null, pubId);
 * MsgBase.cancleFulltextUpload(); } }); }, error:function(e){ } }); };
 */

/**
 * 点击按钮上传文件
 */
MsgBase.uploadFulltextButton = function() {
    // 点击上传文件
    var fileSelect = $("#fileupload").find(".file_selected");
    if (fileSelect == undefined || fileSelect.length == 0) {
        scmpublictoast(msgBase.uploadTips, 2000);
        return;
    }
    var fileDoxDom = $("#fileupload").find(".fileupload__box").get(0);
    var dataFile = {}
    // 超时判断
    $.ajax({
        url : "/dynweb/ajaxtimeout",
        dataType : "json",
        type : "post",
        data : {},
        success : function(data) {
            BaseUtils.ajaxTimeOut(data, function() {
                fileUploadModule.uploadFile(fileDoxDom, dataFile);
            });

        }
    });

};
MsgBase.enterPsnInfo = function(name, code) {
    var str = "<div class='chip__box' code='"
            + code
            + "'>"
            + "<div class='chip__text'>"
            + name
            + "</div>"
            + "<div class='chip__icon icon_delete' onclick='MsgBase.calFriendName(this)'>"
            + "<i class='material-icons'>close</i>" + "</div>" + "</div>";
    $("#msg_friends").find(".chip__box").remove();
    $("#msg_addfriend").before(str);
}
MsgBase.autoSeachPsnEvent = function() {
    $(document).click(function() {
        $("#id_search_psn_list_ac__box").find(".ac__list").html("");
    });
    $("#msg_addfriend").on("blur", function() {
        // $("#id_search_psn_list_ac__box").find(".ac__list").html("");
    });
    $("#msg_addfriend").on("keydown", function(e) {
        if ($("#msg_addfriend").text().length > 50) {
            $("#msg_addfriend").text($("#msg_addfriend").text().substr(0, 50));
            BaseUtils.set_focus($("#msg_addfriend")[0]);
        }
    });
    $("#msg_addfriend")
            .on(
                    "keyup",
                    function(e) {
                        e.preventDefault();
                        var listUL = $("#id_search_psn_list_ac__box").find(
                                ".ac__list");
                        var searchname = $.trim($("#msg_addfriend").text());
                        if (searchname == ""
                                || (e.keyCode == 59 || e.keyCode == 186 || e.keyCode == 13)
                                && listUL.find(".ac__item").length == 0) {
                            $("#msg_addfriend").text("");
                            listUL.html("");
                            return;
                        }
                        if ((e.keyCode == 59 || e.keyCode == 186 || e.keyCode == 13)
                                && listUL.find(".ac__item").length > 0) {

                            var obj;
                            if (listUL.find(".item_hovered").length > 0) {
                                obj = listUL.find(".item_hovered");
                            } else {
                                obj = listUL.find(".ac__item").eq(0);
                            }
                            MsgBase.enterPsnInfo(obj.text(), obj.attr("code"));
                            // MsgBase.addNewChat(obj.attr("code"));
                            listUL.html("");
                        } else if (e.keyCode == 38
                                && listUL.find(".ac__item").length > 0) {
                            if (listUL.find(".item_hovered").length > 0) {
                                const index = listUL.find(".item_hovered")
                                        .index();
                                listUL.find(".item_hovered").removeClass(
                                        "item_hovered");
                                if (index == 0) {
                                    listUL.find(".ac__item:last").addClass(
                                            "item_hovered");
                                } else {
                                    listUL.find(".ac__item").eq(index - 1)
                                            .addClass("item_hovered");
                                }
                            } else {
                                listUL.find(".ac__item:last").addClass(
                                        "item_hovered");
                            }

                        } else if (e.keyCode == 40
                                && listUL.find(".ac__item").length > 0) {
                            if (listUL.find(".item_hovered").length > 0) {
                                const index = listUL.find(".item_hovered")
                                        .index();
                                listUL.find(".item_hovered").removeClass(
                                        "item_hovered");
                                if (index == listUL.find(".ac__item").length - 1) {
                                    listUL.find(".ac__item:first").addClass(
                                            "item_hovered");
                                } else {
                                    listUL.find(".ac__item").eq(index + 1)
                                            .addClass("item_hovered");
                                }
                            } else {
                                listUL.find(".ac__item:first").addClass(
                                        "item_hovered");
                            }

                        } else {
                            listUL
                                    .doLoadStateIco({
                                        style : "height:28px; width:28px; margin:auto;margin-top:24px;",
                                        status : 1
                                    });
                            const $acBox = document
                                    .getElementsByClassName("ac__box")[0];
                            const $acPos = $("#msg_addfriend")[0]
                                    .getBoundingClientRect(); // 自动填词定位的位置属性
                            const $acBoxHeight = parseInt(window
                                    .getComputedStyle($acBox).getPropertyValue(
                                            "max-height"));
                            if (($acPos.top + $acPos.height + $acBoxHeight + 8) <= window.innerHeight) {
                                $acBox.style.cssText = "display: block; width: "
                                        + $acPos.width
                                        + "px; left: "
                                        + $acPos.left
                                        + "px; top: "
                                        + ($acPos.top + $acPos.height)
                                        + "px; bottom: auto";
                            } else {
                                $acBox.style.cssText = "display: block; width: "
                                        + $acPos.width
                                        + "px; left: "
                                        + $acPos.left
                                        + "px; bottom: "
                                        + (window.innerHeight - $acPos.top)
                                        + "px; top: auto";
                            }

                            $
                                    .ajax({
                                        url : '/psnweb/friend/ajaxgetchatfriendlist',
                                        type : 'post',
                                        dataType : 'html',
                                        data : {
                                            searchType : 1,
                                            psnTypeForChat : "friend",
                                            "page.pageSize" : "5",
                                            searchKey : $.trim($(
                                                    "#msg_addfriend").text()),
                                            "page.ignoreMin" : true
                                        },
                                        success : function(data1) {
                                            BaseUtils
                                                    .ajaxTimeOut(
                                                            data1,
                                                            function() {
                                                                listUL
                                                                        .html(data1);
                                                                listUL
                                                                        .doLoadStateIco({
                                                                            style : "height:28px; width:28px; margin:auto;margin-top:24px;",
                                                                            status : 1,
                                                                            addWay : "append"
                                                                        });
                                                                $
                                                                        .ajax({
                                                                            url : '/psnweb/search/ajaxmsgallpsnlist',
                                                                            type : 'post',
                                                                            dataType : 'html',
                                                                            data : {
                                                                                searchType : 1,
                                                                                searchString : $
                                                                                        .trim($(
                                                                                                "#msg_addfriend")
                                                                                                .html()),
                                                                                "page.pageSize" : "5",
                                                                                "page.ignoreMin" : true
                                                                            },
                                                                            success : function(
                                                                                    data2) {
                                                                                $(
                                                                                        "*[scm_id='load_state_ico']")
                                                                                        .remove();
                                                                                listUL
                                                                                        .find(
                                                                                                ".dev_all_psn")
                                                                                        .remove();
                                                                                listUL
                                                                                        .append(data2);
                                                                            }
                                                                        });
                                                            });
                                        }
                                    });
                        }
                    });
}

// 全文请求消息列表全文下载
MsgBase.downloadPubFullText = function(des3PubId) {
    BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function() {
        $.post('/fileweb/download/ajaxgeturl', {
            "des3Id" : des3PubId,
            "type" : 2
        }, function(result) {
            if (result && result.status == "success") {
                location.href = result.data.url;
            } else {
                scmpublictoast(result.msg, 1000);
            }
        });
    }, 1);

}
