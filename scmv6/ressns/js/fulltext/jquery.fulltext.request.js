/**
 * 全文请求插件.
 * 
 * @author cxr
 */
(function($) {
    $.fullTextRequest = $.fullTextRequest ? $.fullTextRequest : {};
    $.fn.fullTextRequest = function(options) {
        var defaults = {
            "mailTitle" : "请求${0}“${1}”的全文",
            "mailEnTitle" : "Request the full-text of ${0} \"${1}\""
        };

        if (typeof options != "undefined") {
            $.extend(defaults, options);
        }

        var tips = {
            "operationFail_zh_CN" : "操作失败",
            "operationFail_en_US" : "Operated failed",
            "operationSuccess_zh_CN" : "请求发送成功",
            "operationSuccess_en_US" : "Send request successfully!",
            "operationSendFail_zh_CN" : "请求发送失败",
            "operationSendFail_en_US" : "Send request failed!",
            "prompt_zh_CN" : "提示",
            "prompt_en_US" : "Reminder",
            "noExistsPrompt_zh_CN" : "全文尚未上传或公开，是否要向${0}请求全文？",
            "noExistsPrompt_en_US" : "The full-text is not uploaded or open accessed. Would you like to ask ${0} to share full-text to you? ",
            "noPermissionPrompt_zh_CN" : "全文尚未上传或公开，是否要向${0}请求全文？",
            "noPermissionPrompt_en_US" : "The full-text is not uploaded or open accessed. Would you like to ask ${0} to share full-text to you? ",
            "resTypeName_1_zh_CN" : "成果",
            "resTypeName_1_en_US" : "publication",
            "resTypeName_2_zh_CN" : "文献",
            "resTypeName_2_en_US" : "reference",
            "resTypeName_4_zh_CN" : "项目",
            "resTypeName_4_en_US" : "project",
            "mineTip_zh_CN" : "自己无需给自己发请求",
            "mineTip_en_US" : "It is your own!",
            "sessionTimeout_zh_CN" : "系统超时或未登录，你要登录吗？",
            "sessionTimeout_en_US" : "You are not logged in or session time out, please log in again.",
            "1_isDel_zh_CN" : "此成果已删除",
            "1_isDel_en_US" : "The publication has been deleted.",
            "2_isDel_zh_CN" : "此文献已删除",
            "2_isDel_en_US" : "The reference has been deleted.",
            "4_isDel_zh_CN" : "此项目已删除",
            "4_isDel_en_US" : "The project has been deleted."
        };

        this
                .each(function() {

                    $(this)
                            .unbind("click")
                            .bind(
                                    'click',
                                    function() {
                                        var _this = $(this);

                                        var resNode = Number(_this
                                                .attr("resNode"));
                                        var resType = Number(_this
                                                .attr("resType"));
                                        var params = {
                                            "resNode" : resNode,
                                            "resType" : resType
                                        };
                                        var resId = _this.attr('resId')
                                        if (resId != undefined) {
                                            params.resId = Number(resId);
                                        }

                                        var des3Id = _this.attr("des3Id");
                                        if (des3Id != undefined) {
                                            params.des3Id = encodeURIComponent(des3Id);
                                        }
                                        var groupId = _this.attr("groupId");
                                        if (typeof groupId != "undefined"
                                                && groupId != "null"
                                                && groupId != "") {
                                            params.groupId = groupId;
                                        }
                                        $
                                                .ajax({
                                                    type : "post",
                                                    url : "/pubweb/fullText/ajaxgetinfo",
                                                    data : params,
                                                    dataType : "json",
                                                    async : false,
                                                    success : function(data) {
                                                        if (data.ajaxSessionTimeOut) {// 系统超时
                                                            jConfirm(
                                                                    tips["sessionTimeout_"
                                                                            + locale],
                                                                    tips["prompt_"
                                                                            + locale],
                                                                    function(r) {
                                                                        if (r) {
                                                                            if (typeof loginCommend != "undefined") {
                                                                                loginCommend();
                                                                            } else {
                                                                                // 区别机构主页(站外)和其他项目_临时办法
                                                                                if (window.location.href
                                                                                        .indexOf("/inspg") >= 0) {
                                                                                    document.location.href = "/inspg/login?forwardUrl="
                                                                                            + window.location.href;
                                                                                } else {
                                                                                    document.location
                                                                                            .reload();
                                                                                }
                                                                            }
                                                                        }
                                                                    });
                                                        } else {
                                                            if (data.resIsDel == 1) {
                                                                if (typeof resDelCallback != "undefined") {
                                                                    resDelCallback(_this);
                                                                } else {
                                                                    $.smate.scmtips
                                                                            .show(
                                                                                    "warn",
                                                                                    tips[data.resType
                                                                                            + "_isDel_"
                                                                                            + locale]);
                                                                }
                                                            } else {
                                                                var mailTitle = defaults["mailTitle"]
                                                                        .replace(
                                                                                "${0}",
                                                                                tips["resTypeName_"
                                                                                        + data.resType
                                                                                        + "_zh_CN"])
                                                                        .replace(
                                                                                "${1}",
                                                                                data.resTitle);
                                                                var mailEnTitle = defaults["mailEnTitle"]
                                                                        .replace(
                                                                                "${0}",
                                                                                tips["resTypeName_"
                                                                                        + data.resType
                                                                                        + "_en_US"])
                                                                        .replace(
                                                                                "${1}",
                                                                                data.resEnTitle);
                                                                if (data.result == "none") {
                                                                    if (data.isMine != 1) {
                                                                        sendRequest(
                                                                                data.des3PsnId,
                                                                                data.resType,
                                                                                data.resId,
                                                                                data.resNode,
                                                                                "",
                                                                                0,
                                                                                "",
                                                                                mailTitle,
                                                                                mailEnTitle);
                                                                    }
                                                                } else {
                                                                    if (data.permission == "no") {
                                                                        var des3FullTextId = (data.fullTextDes3FileId == "") ? ""
                                                                                : data.fullTextDes3FileId;
                                                                        var fullTextNode = (data.fullTextNode == "") ? 0
                                                                                : data.fullTextNode;
                                                                        var fullTextName = data.fullTextName;
                                                                        sendRequest(
                                                                                data.des3PsnId,
                                                                                data.resType,
                                                                                data.resId,
                                                                                data.resNode,
                                                                                des3FullTextId,
                                                                                fullTextNode,
                                                                                fullTextName,
                                                                                mailTitle,
                                                                                mailEnTitle);
                                                                    } else {
                                                                        // 下载的回调函数
                                                                        if (typeof downLoadcallBack != "undefined") {
                                                                            downLoadcallBack(
                                                                                    data.resId,
                                                                                    data.psnId,
                                                                                    data.resType,
                                                                                    data.resNode);// 回调函数
                                                                        }
                                                                        // 动态的全文下载
                                                                        if (typeof dynDownLoadcallBack != "undefined") {
                                                                            dynDownLoadcallBack(
                                                                                    data.resId,
                                                                                    data.resNode,
                                                                                    data.resType);// 回调函数
                                                                        }
                                                                        // 分享的全文下载
                                                                        if (typeof shareDownLoadcallBack != "undefined") {
                                                                            shareDownLoadcallBack(
                                                                                    data.resId,
                                                                                    data.resNode,
                                                                                    data.resType);// 回调函数
                                                                        }
                                                                        document.location.href = data.fullTextUrl;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    },
                                                    error : function(e) {
                                                        $.smate.scmtips
                                                                .show(
                                                                        "error",
                                                                        tips["operationFail_"
                                                                                + locale]);
                                                    }
                                                });
                                        $.Event.stopEvent();
                                    });
                });

        // 发送全文请求
        var sendRequest = function(des3ReceiverId, resType, resId, resNode,
                des3FullTextId, fullTextNode, fullTextName, mailTitle,
                mailEnTitle) {
            $
                    .ajax({
                        type : "post",
                        url : "/scmwebsns/msgbox/ajaxSendFTRequest",
                        data : {
                            "des3ReceiverId" : des3ReceiverId,
                            "resType" : resType,
                            "resId" : resId,
                            "resNode" : resNode,
                            "des3FullTextId" : des3FullTextId,
                            "fullTextNode" : fullTextNode,
                            "fullTextName" : fullTextName,
                            "mailTitle" : encodeURIComponent(mailTitle),
                            "mailEnTitle" : encodeURIComponent(mailEnTitle)
                        },
                        dataType : "json",
                        success : function(data) {
                            if (data.result == "success") {
                                $.smate.scmtips.show("success",
                                        tips["operationSuccess_" + locale]);
                            } else {
                                if (data.ajaxSessionTimeOut) {// 系统超时
                                    jConfirm(
                                            tips["sessionTimeout_" + locale],
                                            tips["prompt_" + locale],
                                            function(r) {
                                                if (r) {
                                                    if (typeof loginCommend != "undefined") {
                                                        loginCommend();
                                                    } else {
                                                        // 区别机构主页(站外)和其他项目_临时办法
                                                        if (window.location.href
                                                                .indexOf("/inspg") >= 0) {
                                                            document.location.href = "/inspg/login?forwardUrl="
                                                                    + window.location.href;
                                                        } else {
                                                            document.location
                                                                    .reload();
                                                        }
                                                    }
                                                }
                                            });
                                } else {
                                    $.smate.scmtips
                                            .show("error",
                                                    tips["operationSendFail_"
                                                            + locale]);
                                }
                            }
                        },
                        error : function(e) {
                            $.smate.scmtips.show("error", tips["operationFail_"
                                    + locale]);
                        }
                    });
        }

    };
    $.Event.getEvent = function getEvent() {
        if (window.event) {
            return window.event;
        } else {
            var aFunction = function(dArguments, dLevel) {
                if (!dArguments)
                    return null;
                if (!dLevel)
                    return null;
                for (var i = 0; i < dArguments.length; i++) {
                    if (dArguments[i] && dArguments[i].altKey !== undefined) {
                        return dArguments[i];
                    }
                }
                if (dArguments && dArguments.callee && dArguments.callee.caller
                        && dArguments.callee.caller.arguments) {
                    return aFunction(dArguments.callee.caller.arguments, 5 - 1);
                }
            };
            return aFunction(arguments, 5);
        }
    };
    $.Event.stopEvent = function getEvent() {
        var e = $.Event.getEvent();
        if (e && e.stopPropagation) {// 非IE
            e.stopPropagation();
        } else {// IE
            window.event.cancelBubble = true;
        }
    };
})(jQuery);
