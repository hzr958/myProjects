common = common ? common : {};
common.comment = common.comment ? common.comment : {};
/*
 * 常量 定义时与CommentEnum一致
 */
common.comment.news = 1001; // 新闻评论
common.comment.photo = 2001; // 图片评论
common.comment.publication = 3001; // 成果评论
common.comment.project = 4001; // 项目评论
common.comment.dyn = 5001; // 动态评论(包括链接)
common.comment.dynFile = 5002; // 动态文件赞
// 国际化
common.comment.optSuccess = locale == "zh_CN" ? "操作成功"
        : "Operated successfully";
common.comment.commentEnterWarn = locale == "zh_CN" ? "请输入评论内容"
        : "Please enter the comment";
common.comment.lengthWarn = locale == "zh_CN" ? "对不起，您输入的内容太长"
        : "Sorry,the text you enter more than the prescribed number of words";
common.comment.optFailed = locale == "zh_CN" ? "操作失败" : "Operated failed";
/**
 * Inspg-评论通用JS path:根据系统定义,Action代码完成时可用
 * 
 * 
 * 新增返回data格式:
 * {result:{psn:{img:img,name:name,id:id}\n,content:content,createDate:createDate\n},
 * massage:success/fail/error}
 */

/*
 * 提交评论 传入参数: 1,type:上边定义的常量类型 2,psnid:评论用户的id 3,parentid:评论归属id 4,content:评论内容
 * 5,callback:回调函数,获取返回数据,构造页面新增评论显示
 */
common.comment.submit = function(type, parentId, content, callback) {
    if (content == "" || content == null) {
        $.smate.scmtips.show("warn", common.comment.commentEnterWarn);
        return false;
    }
    var commentContentLen = 0;
    commentContentLen = common.textLengthCount(content);
    if (commentContentLen > 250) {
        $.smate.scmtips.show("warn", common.comment.lengthWarn);
        return false;
    }
    var post_data = {
        type : type,
        parentId : parentId,
        content : content
    }
    $.ajax({
        url : '/inspg/inspgcomment/ajaxsavecomment',
        type : 'post',
        dataType : "json",
        data : post_data,
        success : function(data) {
            if (data.ajaxSessionTimeOut) {
                jConfirm("系统超时或未登录，你要登录吗？", "提示", function(result) {
                    if (result) {
                        document.location.href = "/inspg/login?service="
                                + window.location.href;
                    }
                });
                return;
            }
            if (data.result == "error") {
                $.smate.scmtips.show('error', data.msg, 350, 10000);
            } else {
                if (typeof (callback) == "undefined") {
                    common.comment.load(type, callback);
                } else {
                    callback(data, type);
                }
            }
        },
        error : function() {
            $.smate.scmtips.show(data.msg);
        }
    });
};

/*
 * 删除评论 参数: id:评论id type:评论类型
 */
common.comment.remove = function(type, id, callback) {
    var post_data = {
        type : type,
        id : id
    }
    $.ajax({
        url : '/inspg/inspgcomment/ajaxremove',
        type : 'post',
        dataType : "json",
        data : post_data,
        success : function(data) {
            if (data.result == "error") {
                $.smate.scmtips.show('error', data.msg, 350, 10000);
            } else {
                common.comment.load(type, callback);
            }
        },
        error : function() {
            return false;
        }
    });
};

/*
 * 加载评论 参数: des3ObjectId:评论归属id type:评论类型 callback:回调函数
 */
common.comment.load = function(type, callback) {
    if (typeof (type) == "undefined") {
        return;
    }
    $("#comment_content").text("");
    $('#comment_content').divWatermark({
        tipCon : '我也想说一句......',
        blurClass : 'watermark'
    });
    var post_data = {
        type : type,
        des3ObjectIds : common.like.collectresdata(type)
    }
    $.ajax({
        url : '/inspg/inspgcomment/ajaxloadcomment',
        type : 'post',
        dataType : "json",
        data : post_data,
        success : function(data) {
            if (data.result == "error") {
                $.smate.scmtips.show('error', data.msg, 350, 10000);
            } else {
                if (typeof (callback) == "undefined") {
                    common.comment.filldata(data, type);
                } else {
                    callback(data, type);
                }
            }
        },
        error : function() {
            return false;
        }
    });
};

common.comment.filldata = function(result, type) {
    $(".comment_num").text(result.commentnum);
    $("#parentId").val(result.parentId);
    var psnImg = result.psnImg;
    if (result.psnId == 0) {
        var psnImg = "https://www.scholarmate.com/avatars/head_nan_photo.jpg";
        $(".d_phone").html(
                '<img src="' + psnImg + '"  width="32" height="32"  />')
    } else {
        $(".d_phone").html(
                "<a href='/scmwebsns/resume/view?des3PsnId=" + result.des3PsnId
                        + "' target='_blank'><img src='" + result.psnImg
                        + "'  width='32' height='32'  /></a>")
    }
    if (result.existslist == "1") {
        var likePsnContent = common.comment.generatecommentpsnhtml(
                result.commentList, result.psnId);
        $("#pubview_discuss_list").html(likePsnContent);
    } else if (result.existslist == "0") {
        $(".comment_num").text(0);
        $("#pubview_discuss_list").html("");
    }
};

common.comment.generatecommentpsnhtml = function(commentList, psnId) {
    var psnCommentHtml = "<ul>";
    for (var i = 0; i < commentList.length; i++) {
        psnCommentHtml += "<li><div class=\"d_phone\"><a href=\"/scmwebsns/resume/view?des3PsnId="
                + commentList[i].des3PsnId
                + "\" target=\"_blank\"><img src=\""
                + commentList[i].psnImg
                + "\" width=\"32\" height=\"32\" /></a></div>"
                + "<div class=\"dis_contant\" style=\"width:585px; table-layout:fixed; word-wrap:break-word; overflow:hidden;\">"
                + "<p><a href=\"/scmwebsns/resume/view?des3PsnId="
                + commentList[i].des3PsnId
                + "\" target=\"_blank\" class=\"Blue\">"
                + commentList[i].psnName
                + "</a>："
                + commentList[i].content
                + "</p>"
                + "<p class=\"en10\">"
                + common.comment.formatdate(commentList[i].createDate)
                + "</p></div>";
        if (commentList[i].psnId == psnId) {
            psnCommentHtml += "<a href=\"javascript:void(0);\" class='Blue' onclick=\"common.comment.remove(common.comment.news,"
                    + commentList[i].id + ");\">删除评论</a>"
        }
        psnCommentHtml += "<div class=\"delete-friend2\"></div></li>";
    }
    psnCommentHtml = psnCommentHtml + "</ul>"
    return psnCommentHtml;
}

/**
 * 毫秒数转换日期格式
 */
common.comment.formatdate = function(createDate) {
    var createTime = new Date(createDate);
    var createTimeStr = createTime.getFullYear()
            + "/"
            + (createTime.getMonth() < 10 ? "0" + (createTime.getMonth() + 1)
                    : createTime.getMonth())
            + "/"
            + (createTime.getDate() < 10 ? "0" + createTime.getDate()
                    : createTime.getDate())
            + " "
            + (createTime.getHours() < 10 ? "0" + createTime.getHours()
                    : createTime.getHours())
            + ":"
            + (createTime.getMinutes() < 10 ? "0" + createTime.getMinutes()
                    : createTime.getMinutes())
            + ":"
            + (createTime.getSeconds() < 10 ? "0" + createTime.getSeconds()
                    : createTime.getSeconds());
    return createTimeStr;
}
/**
 * 动态 加载评论 type：评论类型 des3ObjectIds:parentId des3版 parentId所属id resId：parentId
 * dynId：动态Id
 */
common.comment.dynload = function(type, des3ObjectIds, parentId, resId, dynId,
        callback) {
    var post_data = {
        type : type,
        des3ObjectIds : des3ObjectIds
    }
    $
            .ajax({
                url : '/inspg/inspgcomment/ajaxloadcomment',
                type : 'post',
                dataType : "json",
                data : post_data,
                success : function(data) {
                    if (data.result == "error") {
                        $.smate.scmtips.show('error', data.msg, 350, 10000);
                    } else {
                        if (typeof (callback) == "undefined") {
                            if (data.existslist == "1") {
                                var commentHTML = common.comment
                                        .createdyncommenthtml(data.commentList,
                                                data.psnId, des3ObjectIds,
                                                parentId, type, resId, dynId);
                                $(".reply_dl_" + parentId).html(commentHTML);
                                $(".reply_num_label_" + parentId).text(
                                        data.commentnum);
                                $(".reply_num_label_" + parentId).parent()
                                        .show()
                            } else if (data.existslist == "0") {
                                $(".reply_dl_" + parentId).html("");
                                $(".reply_num_label_" + parentId).text(0);
                                $(".reply_num_label_" + parentId).parent()
                                        .hide()
                            }
                            replyOperatShowOrHide();
                            var replyBtnObj = $(
                                    "#textarea_" + dynId + "_" + parentId)
                                    .find(".replyBtn");
                            replyBtnObj.removeAttr("disabled");
                            replyBtnObj.attr("style", "cursor: pointer;");
                        } else {
                            callback(data);
                        }
                    }
                },
                error : function() {
                    $.smate.scmtips.show('error', common.comment.optFailed,
                            350, 10000);
                }
            });
}

/**
 * 动态 删除评论 参数: id:评论id type:评论类型
 */
common.comment.dynremove = function(type, id, des3ObjectId, parentId, resId,
        dynId, callback) {
    var post_data = {
        type : type,
        id : id
    }
    $.ajax({
        url : '/inspg/inspgcomment/ajaxremove',
        type : 'post',
        dataType : "json",
        data : post_data,
        success : function(data) {
            if (data.result == "error") {
                $.smate.scmtips.show('error', data.msg, 350, 10000);
            } else {
                common.comment.dynload(type, des3ObjectId, parentId, resId,
                        dynId, callback);
            }
        },
        error : function() {
            return false;
        }
    });
};

/**
 * 动态 评论加载HTML构建
 */
common.comment.createdyncommenthtml = function(commentList, psnId,
        des3ObjectId, parentId, resType, resId, dynId) {
    var psnCommentHtml = "";
    var showType = $(".reply_dl_" + parentId).attr("showReply");
    for (var i = 0; i < commentList.length; i++) {
        if (showType == 1 || (i == 0 || i == (commentList.length - 1))) {
            psnCommentHtml += "<dd><div class=\"t_namecard\"><a href=\"/scmwebsns/resume/view?menuId=1100&des3PsnId="
                    + commentList[i].des3PsnId
                    + "\" target=\"_blank\"><img src=\""
                    + commentList[i].psnImg
                    + "\" width=\"32\" height=\"32\"  border=\"0\"/></a></div>"
                    + "<div class=\"t_message2\" >"
                    + "<p><a href=\"/scmwebsns/resume/view?des3PsnId="
                    + commentList[i].des3PsnId
                    + "\" target=\"_blank\" class=\"Blue\">"
                    + commentList[i].psnName
                    + "</a>："
                    + commentList[i].content
                    + "</p>"
                    + "<p class=\"en10\">"
                    + common.comment.formatdate(commentList[i].createDate)
                    + "<span class=\"reply_operation\" style=\"display:none;\">"
            if (commentList[i].psnId == psnId) {
                psnCommentHtml += "&nbsp;<a onclick=\"common.comment.dynremove("
                        + resType
                        + ","
                        + commentList[i].id
                        + ",'"
                        + des3ObjectId
                        + "',"
                        + parentId
                        + ","
                        + resId
                        + ","
                        + dynId
                        + ");\" style=\"cursor:pointer\" class=\"Blue\" title=\"删除\"><i class=\"delete-icon\"></i></a>"
            }
            psnCommentHtml += "</span></p></div></dd>";
            if ((showType == 0) && (i == 0) && (commentList.length > 2)) {
                psnCommentHtml += "<dd style='cursor:pointer' id='' onclick=\"$('.reply_dl_"
                        + parentId
                        + "').attr('showReply',1);common.comment.dynload("
                        + resType
                        + ",'"
                        + des3ObjectId
                        + "',"
                        + parentId
                        + ","
                        + parentId + "," + dynId + ");\" >";
                psnCommentHtml += "<a class='Blue'>还有<label id=''>"
                        + (commentList.length - 2) + "</label>条评论</a></dd>";
            }
        }
    }
    return psnCommentHtml;

}
