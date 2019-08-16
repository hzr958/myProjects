common = common ? common : {};
common.like = common.like ? common.like : {};
/*
 * 常量 定义时与likeEnum一致
 */
common.like.news = 1001; // 新闻赞
common.like.photo = 2001; // 图片赞
common.like.publication = 3001; // 成果赞
common.like.project = 4001; // 项目赞
common.like.dyn = 5001; // 动态赞

common.like.optSuccess = locale == "zh_CN" ? "操作成功" : "Operated successfully";
common.like.optFailed = locale == "zh_CN" ? "操作失败" : "Operated failed";
common.like.personalList = locale == "zh_CN" ? "赞人员列表"
        : "Praise personnel list";
common.like.noAward = locale == "zh_CN" ? "对不起，暂时没有人赞过" : "Sorry,Nobody like";
/**
 * Inspg-赞通用JS path:根据系统定义,Action代码完成时可用
 * 
 * 
 * 加载返回data格式: {result:[{psnId:psnId,psnImg:psnImg,\npsnNameZh:psnNameZh,
 * psnNameEn:psnNameEn,id:id},{},{}......],message:success/fail/error}
 */

/**
 * 赞人员列表.
 */
common.like.showdetail = function(type, obj) {
    var likeId = $(obj).attr("likeid");
    if (likeId == 0 || type == 0) {
        $.smate.scmtips.show("warn", common.like.noAward);
    } else {
        $("#hidden-awardLink").attr(
                "alt",
                "/inspg/inspglike/ajaxlikedetail?des3ObjectId=" + likeId
                        + "&type=" + type
                        + "&TB_iframe=true&height=384&width=672");
        $("#hidden-awardLink").attr("title", common.like.personalList);
        $("#hidden-awardLink").click();
    }
};

/**
 * 动态赞人员列表.
 */
common.like.dynshowdetail = function(type, des3LikeId, likeId) {
    if (des3LikeId == 0 || type == 0) {
        $.smate.scmtips.show("warn", common.like.noAward);
    } else {
        $("#box_like_list_" + likeId).attr(
                "alt",
                "/inspg/inspglike/ajaxlikedetail?des3ObjectId=" + des3LikeId
                        + "&type=" + type
                        + "&TB_iframe=true&height=384&width=672");
        $("#box_like_list_" + likeId).attr("title", common.like.personalList);
        $("#box_like_list_" + likeId).click();
    }
};

/*
 * 提交赞 参数: 1,type:上边定义的常量类型 2,psnId:赞用户的id 3,parentId:赞归属id
 */
common.like.submit = function(type, obj, callback) {
    var post_data = {
        type : type,
        des3ObjectId : obj
    }
    $.ajax({
        url : '/inspg/inspglike/ajaxsavelike',
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
                    common.like.filldata(data, type);
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
 * 取消赞 参数: id:赞id type:赞类型
 */
common.like.remove = function(type, obj, callback) {
    var post_data = {
        type : type,
        des3ObjectId : obj
    }
    $.ajax({
        url : '/inspg/inspglike/ajaxremovelike',
        type : 'post',
        dataType : "json",
        data : post_data,
        success : function(data) {
            if (data.result == "error") {
                $.smate.scmtips.show('error', data.msg, 350, 10000);
            } else {
                if (typeof (callback) == "undefined") {
                    common.like.filldata(data, type);
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
/*
 * 加载赞 参数: des3ObjectId:赞归属id type:赞类型 callback:回调函数
 */
common.like.load = function(type, callback) {
    if (typeof (type) == "undefined") {
        return;
    }
    var post_data = {
        type : type,
        des3ObjectIds : common.like.collectresdata(type)
    }
    $.ajax({
        url : '/inspg/inspglike/ajaxloadlike',
        type : 'post',
        dataType : "json",
        data : post_data,
        success : function(data) {
            if (data.result == "error") {
                $.smate.scmtips.show('error', data.msg, 350, 10000);
            } else {
                if (typeof (callback) == "undefined") {
                    common.like.filldata(data, type);
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

common.like.filldata = function(result, type) {
    // var objArr = document.getElementsByName("like_obj");
    if (result.existslist == "1") {
        $(".zan_list").show();
        $(".awardLink_" + type)
                .each(
                        function() {
                            var objId = $(this).attr("resId");
                            var des3ObjId = $(this).attr("des3ResId");
                            // 当前登录者已经攒了此条新闻
                            if (result["isZan_" + des3ObjId] == 1) {
                                $(".like_" + type + "_" + objId).hide();
                                $(".unlike_" + type + "_" + objId).show();
                                $(".unlike_count_" + type + "_" + objId).text(
                                        result["listNum_" + des3ObjId]);
                                $(".award_num_" + type + "_" + objId).text(
                                        result["listNum_" + des3ObjId]);
                            } else {
                                $(".like_" + type + "_" + objId).show();
                                $(".unlike_" + type + "_" + objId).hide();
                                $(".like_count_" + type + "_" + objId).text(
                                        result["listNum_" + des3ObjId]);
                                $(".award_num_" + type + "_" + objId).text(
                                        result["listNum_" + des3ObjId]);
                            }
                            var likePsnContent = common.like
                                    .generatelikepsnhtml(result["likeList_"
                                            + des3ObjId]);
                            $(".award_psn_list_" + type + "_" + objId).html(
                                    '<span>' + likePsnContent + '</span>');
                        });
    } else {
        $(".zan_list").hide();
        $(".awardLink_" + type).each(function() {
            var objId = $(this).attr("resId");
            $(".like_" + type + "_" + objId).show();
            $(".unlike_" + type + "_" + objId).hide();
        });
    }
}

common.like.generatelikepsnhtml = function(likeList) {
    var psnLikeHtml = "";
    for (var i = 0; i < likeList.length; i++) {
        psnLikeHtml += "<a href=\"/scmwebsns/resume/view?des3PsnId="
                + likeList[i].des3PsnId + "\" target=\"_blank\"><img src=\""
                + likeList[i].psnImg + "\" width=\"32\" height=\"32\" /></a>";
    }
    return psnLikeHtml;
}

common.like.collectresdata = function(type) {
    var objIdArr = "";
    $(".awardLink_" + type).each(function() {
        objIdArr += $(this).attr("des3ResId") + ",";
    });
    return objIdArr;
}