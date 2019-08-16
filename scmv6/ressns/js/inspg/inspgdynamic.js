smate = smate ? smate : {};
smate.inspgdynamic = smate.inspgdynamic ? smate.inspgdynamic : {};

/**
 * 加载动态
 */
smate.inspgdynamic.ajaxLoadDynamic = function() {

    $("#loadDynamic_div").show();
    $("#moreDynamic_div").hide();
    $("#noMoreDynamic_div").hide();
    var params = {
        "des3InspgId" : $("#des3InspgId").val(),
        "lastId" : $("#lastId").val(),
        "loadType" : $("#loadType").val()
    };
    $
            .ajax({
                url : '/inspg/inspgdynamic/ajaxloaddynamic',
                type : 'post',
                dataType : 'json',
                data : params,
                success : function(data) {
                    if (data.result = "success") {
                        $("#loadDynamic_div").hide();
                        if (typeof (data.html) == "undefined"
                                || data.html == '') {
                            $("#noMoreDynamic_div").show();
                        } else {
                            // 填充数据 没有动态由后台返回页面
                            $("#dataList").html(
                                    $("#dataList").html() + "" + data.html);
                            // 弹出窗口,新鲜事图片
                            $(".gallery_link").each(
                                    function() {
                                        var hrefStr = $(this).attr("href");
                                        $(this).attr("class",
                                                "gallery_link_over");
                                        if (hrefStr != '#') {
                                            $(this).attr(
                                                    "href",
                                                    hrefStr + "?temp="
                                                            + Math.random());
                                            $(this).thickbox();
                                        }
                                    });
                            $(".box_like_list").thickbox({
                                type : "showlikeDetail",
                                isShowImgLink : "false"
                            });
                            // 分享下拉模式
                            $(".share_pull").sharePullMode({
                                showSharePage : function(_this) {
                                    DynMessageUtil.showSharePage(_this);
                                },
                                showShareSites : function() {
                                    showShareSites();
                                },
                                isShowSmate : 0
                            });
                            // 加载 lastId
                            $("#lastId").val(data.lastId);
                            if (data.returnDynSum < 10) {
                                $("#noMoreDynamic_div").show();
                            } else {
                                $("#moreDynamic_div").show();
                            }
                        }
                        // 评论删除
                        replyOperatShowOrHide();
                    }
                },
                error : function(data, status, e) {
                }
            });
};

/**
 * 点击背景事件
 */
smate.inspgdynamic.documentClickBox = function() {
    $("#showMenuList").hide();
    $("#selDymic").hide();
    // 点击别处，隐藏回复框
    $(".reply_content_div").each(function() {
        $(this).hide();
        $(this).parent().find(".reply_enter_div").show();
    });
}

/**
 * 检查动态中的资源是否存在
 */
smate.inspgdynamic.checkSourceExists = function(type, des3ObjectId, callback) {
    var params = {
        resType : type,
        des3ResId : des3ObjectId
    };
    $.ajax({
        url : '/inspg/inspgdynamic/ajaxchecksourceexists',
        type : 'post',
        dataType : 'json',
        data : params,
        success : function(data) {
            if (data.result == "success") {
                callback();
            } else if (data.result == "warn") {
                $.smate.scmtips.show('warn', data.msg);
                return;
            } else if (data.ajaxSessionTimeOut == "yes") {
                jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r) {
                    if (r) {
                        // 区别机构主页(站外)和其他项目_临时办法
                        if (window.location.href.indexOf("/inspg") >= 0) {
                            document.location.href = "/inspg/login?service="
                                    + window.location.href;
                        }
                    }
                });
            } else {
                $.smate.scmtips.show('error', data.msg);
            }
        },
        error : function(data, status, e) {
            $.smate.scmtips.show('error', "检查资源出错");
        }
    });
}
