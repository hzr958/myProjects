var wechat = wechat || {};
wechat.pub = wechat.pub || {};

wechat.pub.ajaxrefreshpubs = function(myscroll) {
    var data = {
        "articleType" : $("#articleType").val(),
        "fromPage" : $("#fromPage").val(),
        "pubType" : $("#pubType").val(),
        "orderType" : $("#orderType").val(),
        "pubLocale" : $("#pubLocale").val(),
        "flag" : 1
    }
    wechat.pub.ajaxrequest(data, wechat.pub.refreshpubs);
}

wechat.pub.refreshpubs = function(data) {
    $("#listdiv").html("");
    $("#listdiv").html(data);
    $("#count").val(0);
}

wechat.pub.ajaxuploadpubs = function() {
    if ($("#load_preloader").length > 0) {
        return;
    }
    wechat.pub.showLoader();
    var nextId = null;
    if (nextId == null || nextId == "") {
        var nextIds = document.getElementsByName("nextIdEx");
        if (nextIds.length > 0) {
            nextId = nextIds.length;
        } else {
            nextId = 0;
        }
    }
    $("#nextId").val(nextId);
    var data = {
        "articleType" : $("#articleType").val(),
        "pubLocale" : $("#pubLocale").val(),
        "pubType" : $("#pubType").val(),
        "fromPage" : $("#fromPage").val(),
        "orderType" : $("#orderType").val(),
        "flag" : 1,
        "nextId" : nextId
    }
    wechat.pub.ajaxrequest(data, wechat.pub.uploadpubs);
    $("#des3NextId").val("");

}
wechat.pub.showLoader = function() {
    var loaderhtml = '<div class="preloader active" id="load_preloader" style="height:80px; padding: 20px 0px;">'
            + ' <div class="preloader-ind-cir__box" style="width: 24px; height: 24px; margin:0 auto;">'
            + '<div class="preloader-ind-cir__fill">'
            + '<div class="preloader-ind-cir__arc-box left-half">'
            + '<div class="preloader-ind-cir__arc">'
            + '</div>'
            + '</div>'
            + '<div class="preloader-ind-cir__gap">'
            + '<div class="preloader-ind-cir__arc">'
            + '</div>'
            + '</div>'
            + '<div class="preloader-ind-cir__arc-box right-half">'
            + '<div class="preloader-ind-cir__arc">'
            + '</div>'
            + '</div>'
            + '</div>' + '</div>' + '</div>';
    $("#addload").before(loaderhtml);
    $("#addload").remove();

}

wechat.pub.uploadpubs = function(data) {
    var hasPrivatePub = $("#psnHasPrivatePub").val();
    var isOthers = $("#other").val();
    var noMoreRecordTips = "没有更多记录";
    if (isOthers == "true" && hasPrivatePub == "true") {
        noMoreRecordTips = "由于权限设置, 可能部分数据未显示";
    }
    if ($("#listdiv").find(".noRecord").length == 0) {
        if ($("#listdiv").find(".paper").length > 0
                && data.indexOf("response_no-result") > -1) {
            $("#load_preloader").before(data);
            $(".response_no-result").text(noMoreRecordTips);
        } else {
            $("#load_preloader").before(data);
            $(".dev_nextIdEx:last")
                    .after(
                            '<div id="addload" style="width: 100%; height: 120px;"></div>');
        }
    }
    // 如果只有一页内容，则要显示提示语
    var totalCount = $("#pubTotalCount").val();
    var pageSize = $("#prePageSize").val();
    if (totalCount != "" && pageSize != ""
            && parseInt(totalCount) <= parseInt(pageSize)) {
        if (parseInt(totalCount) != 0) {
            $("#load_preloader").before(
                    "<div class='response_no-result'>" + noMoreRecordTips
                            + "</div>");
        } else {
            $(".response_no-result").text(noMoreRecordTips);
        }
        $("#addload").remove();
    }
    if (parseInt($("#count").val()) == 0 && data.indexOf("noRecord") != -1) {
        var count = parseInt($("#count").val()) + 1;
        $("#count").val(count);
    }
    $("#load_preloader").remove();
}

wechat.pub.ajaxrequest = function(data, callback) {
    var isLogin = $("#hasLogin").val();
    var url = "";
    if ($("#other").val() == 'true') {
        url = "/pub/querylist/ajaxpsn";
        data['psnId'] = $("#psnId").val();
        if (isLogin == "1") {
            url = "/pub/querylist/psn";
        }
    } else {
        url = "/pub/querylist/ajaxpsn";
    }
    $.ajax({
        url : url,
        type : "post",
        dataType : "html",
        data : data,
        async : false,
        success : function(data) {
            setTimeout(function() {
                callback(data);
            }, 300)

        },
        error : function(data) {
            $("#load_preloader").hide();
            alert("请求数据出错!");
        }
    });
}

/**
 * 加载评论信息
 */
wechat.pub.ajaxLoadComments = function(pagNo, maxresult, type) {
    var des3PubId = $("#des3PubId").val();

    $
            .ajax({
                url : "/pubweb/wechat/ajaxpubreplylist",
                type : "post",
                dataType : "html",
                data : {
                    "des3PubId" : des3PubId,
                    "pageNo" : pagNo,
                    "maxresult" : maxresult
                },
                success : function(data) {
                    if (type == 1) {
                        $("#replyDiv").append(data);
                        var replySizeArray = document
                                .getElementsByName("dynReplySize");
                        // var comment = $("#pubCommentCount").text();
                        var comment = $("#comentCountSpan").html();
                        var commentNum = comment.replace(/\D+/g, "");
                        if (parseInt(commentNum) < 1) {
                            $(".wdful_comments").hide();
                        }
                        if (replySizeArray.length < parseInt(commentNum)) {
                            $("#showMore").show();
                        } else {
                            $("#showMore").hide();
                        }
                    }
                    if (type == 2) {
                        $("#replyDiv").prepend(data);
                        $("html,body").animate({
                            scrollTop : $("#animatePubId").offset().top
                        }, 100);
                        /*
                         * var replySizeArray
                         * =document.getElementsByName("dynReplySize"); var
                         * comment = $("#comentCountSpan").html(); var
                         * commentNum=comment.replace(/\D+/g, "");
                         * if(parseInt(commentNum)<1){
                         * $(".wdful_comments").hide(); }
                         * if(replySizeArray.length<parseInt(commentNum)){
                         * $("#showMore").show(); }else{ $("#showMore").hide(); }
                         */
                    }

                },
                error : function() {
                    alert("加载出错");
                    return false;
                }
            });
}
/*
 * wechat.pub.ajaxLoadComments=function(pagNo,maxresult,type){ var des3PubId =
 * $("#des3PubId").val();
 * 
 * $.ajax( { url : "/pubweb/wechat/ajaxpubreplylist", type : "post",
 * dataType:"html", data : { "des3PubId":des3PubId, "pageNo":pagNo,
 * "maxresult":maxresult }, success : function(data) { if(type==1){
 * $("#replyDiv").append(data); } if(type==2){ $("#replyDiv").append(data);
 *  }
 *  }, error : function() { alert("加载出错！"); return false; } }); }
 */

/**
 * 添加评论信息,没地方调用
 */
// wechat.pub.ajaxdoComment=function(des3PubId,artileType,resNode,replyContent){
//	
// $.ajax( {
// url : "/pubweb/wechat/addpubreply",
// type : "post",
// dataType:"json",
// data : {
// "des3PubId":des3PubId,
// "artileType":artileType,
// "replyContent":replyContent,
// "resNode":resNode
// },
// success : function(data) {
// if(data.result=="success"){
// wechat.pub.ajaxLoadComments(1,1,2);
// var pcc = $("#pubCommentCount").text();
// $("#pubCommentCount").text(parseInt(pcc)+1);
// }
// $("#pubReplyBox").slideUp();
// },
// error : function() {
// alert("评论出错！");
// return false;
// }
// });
// // 产生动态 评论：操作类型为 1
// var data = {
// "resType" : 1,
// "des3ResId" :des3PubId,
// "des3PubId":des3PubId,
// "dynType":"B2TEMP",
// "replyContent":replyContent,
// "operatorType":1
// }
//		
// wechat.pub.ajaxrealtime(data);
// }
/**
 * 成果分享
 */
wechat.pub.ajaxdopubshare = function(des3PubId) {
    $.ajax({
        url : '/pubweb/wechat/ajaxpubshare',
        type : 'post',
        dataType : 'json',
        data : {
            "des3PubId" : des3PubId
        },
        success : function(data) {
            if (data.result == "success") {
                var pcc = $("#pubShareCount").text();
                $("#pubShareCount").text(parseInt(pcc) + 1);
                alert("分享成功!");
            } else {
                alert("分享失败!");
            }
        },
        error : function() {
            alert("分享失败,请稍候再试!");
        }
    });
    // 产生动态 分享：操作类型为 3
    var data = {
        "resType" : 1,
        "des3ResId" : des3PubId,
        "des3PubId" : des3PubId,
        "dynType" : "B2TEMP",
        "replyContent" : "",
        "operatorType" : 3
    }

    wechat.pub.ajaxrealtime(data);
}
/**
 * 发布动态信息
 */
wechat.pub.ajaxrealtime = function(dataJson, callBack) {
    $.ajax({
        url : '/dynweb/dynamic/ajaxrealtime',
        type : 'post',
        dataType : 'json',
        data : dataJson,
        success : function(data) {
            if (data.result == "success") {
            } else {
                alert("发布动态信息失败,请稍候再试!");
            }
        },
        error : function() {
            alert("发布动态信息失败,请稍候再试!");
        }
    });
}

/**
 * 成果赞
 */
wechat.pub.ajaxdoawardpub = function(des3PubId) {
    // 点赞操作
    $.ajax({
        url : "/dynweb/dynamic/ajaxawarddyn",
        type : "post",
        data : {
            "resType" : 1,
            "des3ResId" : des3PubId,
            "des3PubId" : des3PubId,
            "dynType" : "B2TEMP",
            "replyContent" : "",
            "operatorType" : 2
        },
        dataType : "json",
        success : function(data) {
            if (data.action == 1) {
                $("#pubAwardCount").prev(".material-icons").css({
                    "background" : "#2196f3",
                    "color" : "#ffffff"
                });
                // 不能取消赞
                $("#hasAwarded").val("1");
                var pcc = $("#pubAwardCount").text();
                $("#pubAwardCount").text(parseInt(pcc) + 1);
            }
        },
        error : function(e) {
        }
    });
}
/**
 * 成果评论,没地方调用
 */
// wechat.pub.submitComment= function(des3ResId,commnetContent){
// var datas={
// "artileType":1,
// "resNode":1,
// "des3ResId":des3ResId,
// "resType":1,
// "dynType":"B2TEMP",
// "replyContent":commnetContent,
// "operatorType":1
// }
// $.ajax({
// url : '/dynweb/dynamic/ajaxreplydyn',
// type : 'post',
// data:datas,
// dataType : "json",
// success : function(data) {
// $(".wdful_comments").show();
// wechat.pub.ajaxLoadComments(1,1,2);
// var commentStr = $("#comentCountSpan").html();
// var commentCount = commentStr.replace(/\D+/g, "");;
// commentCount = $.trim(commentCount) != "" ? parseInt(commentCount) : 0;
// $("#comentCountSpan").html("评论(" + (commentCount + 1) + ")");
// $("#pubReplyBox").slideUp();
// //隐藏"查看更早10条评论"
// var replySizeArray =document.getElementsByName("dynReplySize");
// if (replySizeArray.length >10) {
// $("#showMore").show();
// }
// },
// error: function (){
// }
// });
//	
// }
// 成果详情评论,一条一条添加的
wechat.pub.submitMobileComment = function(des3ResId, commnetContent,
        articleType) {
    articleType = articleType > 0 ? articleType : 1;// 默认为1
    $.ajax({
        url : '/pubweb/comment/ajaxaddpubreply',
        type : 'post',
        data : {
            "des3PubId" : des3ResId,
            "replyContent" : commnetContent,
            "articleType" : articleType
        },
        dataType : "json",
        success : function(data) {
            $(".wdful_comments").show();
            wechat.pub.ajaxLoadComments(1, 1, 2);
            var commentStr = $("#comentCountSpan").html();
            var commentCount = commentStr.replace(/\D+/g, "");
            ;
            commentCount = $.trim(commentCount) != "" ? parseInt(commentCount)
                    : 0;
            $("#comentCountSpan").html("评论(" + (commentCount + 1) + ")");
            $("#pubReplyBox").slideUp();
            // 隐藏"查看更早10条评论"
            var replySizeArray = document.getElementsByName("dynReplySize");
            if (replySizeArray.length > 10) {
                $("#showMore").show();
            }
        }

    });

}
// 成果快速分享
wechat.pub.quickShareDyn = function(resType, des3ResId) {
    if (des3ResId == "dev_publist_share") {
        des3ResId = $('.dev_publist_share').val();
    }
    var dataJson = {
        "dynType" : "B2TEMP",
        "resType" : Number(resType),
        "des3ResId" : des3ResId,
        "operatorType" : 3,
        "databaseType" : 2
    }
    $.ajax({
        url : '/dynweb/dynamic/ajaxquickshare',
        type : 'post',
        dataType : 'json',
        data : dataJson,
        success : function(data) {
            if (data.result == "success") {
                scmpublictoast("分享成功", 1000);
            } else {
                scmpublictoast("网络繁忙，请稍后重试", 1000);
            }
        },
        error : function() {
            scmpublictoast("网络繁忙，请稍后重试", 1000);
        }
    });
};
// 个人主页 成果分享功能
wechat.pub.quickPsnHomeShareDyn = function(resType, obj) {
    /*
     * if (des3ResId == "dev_publist_share") { des3ResId =
     * $('.dev_publist_share').val(); }
     */
    var des3ResId = $(obj).attr("des3PubId");
    var pubId = $(obj).attr("pubId");
    var dataJson = {
        "dynType" : "B2TEMP",
        "resType" : Number(resType),
        "des3ResId" : des3ResId,
        "operatorType" : 3,
        "databaseType" : 2
    }
    $.ajax({
        url : '/dynweb/dynamic/ajaxquickshare',
        type : 'post',
        dataType : 'json',
        data : dataJson,
        success : function(data) {
            if (data.result == "success") {
                var count = Number($("#shareCount_" + pubId).text().replace(
                        /[\D]/ig, ""));
                count = count + 1;
                $("#shareCount_" + pubId).text("分享" + "(" + count + ")");
            }
        },
        error : function() {
            alert("分享失败,请稍候再试!");
        }
    });
};
/**
 * 更新全文认领和成果认领
 */
wechat.pub.msgtips = function(other) {
    if (other != "true") {
        $.ajax({
            url : '/data/pub/ajaxmsgtips',
            type : 'post',
            dataType : 'json',
            data : {},
            success : function(data) {
                BaseUtils.ajaxTimeOut(data, function() {
                    if (Number(data.pubConfirmCount) > 0) {
                        $("#confirmPubNum").html(data.pubConfirmCount);
                        $("#confirmPubNum").closest(".dev_confirmpub_tip")
                                .show();
                        $("#confirmPubNum").closest(".dev_confirmpub_tip")
                                .attr("onclick", "wechat.pub.confimPubList()");
                    }
                    if (Number(data.pubFulltextCount) > 0) {
                        $("#confirmfullTextNum").html(data.pubFulltextCount);
                        $("#confirmfullTextNum").closest(
                                ".dev_confirmfulltext_tip").show();
                        $("#confirmfullTextNum").closest(
                                ".dev_confirmfulltext_tip").attr("onclick",
                                "wechat.pub.confimpubftrcmd()");
                    }
                });
            }
        });
    }
};

// 移动端-我的成果-待确认成果列表展示
wechat.pub.confimPubList = function() {
    window.location.href = "/pub/confirmpublist";
};

// 移动端-我的成果-待确认的全文
wechat.pub.confimpubftrcmd = function() {
    window.location.href = "/pubweb/wechat/pubfulltextlist?toBack=psnpub";
};

// 成果赞操作
wechat.pub.potaward = function(resType, resNode, des3ResId, obj) {
    BaseUtils.doHitMore(obj, 1000);
    var awardCount = 0;
    var post_data = {
        "resType" : resType,
        "resNode" : resNode,
        "des3ResId" : des3ResId
    };
    var isAward = $(obj).attr("isAward");
    if (isAward == '1') {
        post_data.status = '0';// 取消赞
    } else {
        post_data.status = '1';// 点赞
    }
    $.ajax({
        url : '/pubweb/pubopt/ajaxaward',
        type : 'post',
        dataType : 'json',
        data : post_data,
        success : function(data) {
            BaseUtils.ajaxTimeOut(data, function() {
                if (data.result == "success") {
                    awardCount = data.awardTimes;
                    if (isAward == 1) {// 取消赞
                        $(obj).attr("isAward", 0);// paper_footer-fabulous_like
                        $(obj).find('.paper_footer-tool:first').removeClass(
                                "paper_footer-award_unlike");
                        $(obj).find('.paper_footer-tool:first').addClass(
                                "paper_footer-fabulous");
                        if (awardCount == 0) {
                            $(obj).find('a').text("赞");
                        } else {
                            $(obj).find('a').text("赞(" + awardCount + ")");
                        }
                    } else {// 赞
                        $(obj).attr("isAward", 1);
                        $(obj).find('.paper_footer-tool:first').removeClass(
                                "paper_footer-fabulous");
                        $(obj).find('.paper_footer-tool:first').addClass(
                                "paper_footer-award_unlike");
                        $(obj).find('a').text("取消赞(" + awardCount + ")");
                    }
                }
            });
        },
    });
};

// 成果列表-评论 (打开成果详情)
wechat.pub.opendetail = function(des3PubId) {
    location.href = "/pubweb/wechat/findpubxmlnonext?des3PubId=" + des3PubId;
};

// 弹出分享成果框
wechat.pub.sharePubBox = function(des3PubId) {
    $('.dev_publist_share').val(des3PubId);
    $('#dynamicShare').show();
};
// 个人主页弹出分享成果框
wechat.pub.sharePsnHomePagePubBox = function(des3PubId, pubId) {
    $("#shareScreen").attr("pubId", pubId);
    $("#shareScreen").attr("des3PubId", des3PubId);
    $('#dynamicShare').show();
};
/**
 * 个人主页收藏成果 publicationArticleType
 */
wechat.pub.importPdwh = function(des3PubId, obj) {
    /*
     * var params=[]; params.push({"des3PubId":des3PubId}); var
     * postData={"jsonParams":JSON.stringify(params),"publicationArticleType":"2"};
     */
    $.ajax({
        url : "/pubweb/ajaxsavePaper",
        type : "post",
        dataType : "json",
        data : {
            "des3PubId" : des3PubId,
            "pubDb" : "SNS"
        },
        success : function(data) {
            BaseUtils.ajaxTimeOutMobile(data, function() {
                if (data && data.result) {
                    if (data.result == "success") {
                        $(obj).removeAttr("onclick");
                        $(obj).attr(
                                "onclick",
                                "wechat.pub.delCollectedPub('" + des3PubId
                                        + "',this)");
                        $(obj).find(".importPdwh").text("取消收藏");
                        $(obj).find("a").addClass(
                                "paper_footer-tool__box-click");
                        $(obj).find("i").removeClass("paper_footer-comment")
                                .addClass("paper_footer-comment__flag");
                        /* scmpublictoast("收藏成功",1000); */
                    } else if (data.result == "exist") {
                        scmpublictoast("已收藏该成果", 1000);
                    } else if (data.result == "isDel") {
                        scmpublictoast("成果不存在", 1000);
                    } else {
                        scmpublictoast("收藏失败", 1000);
                    }
                } else {
                    scmpublictoast("收藏失败", 1000);
                }
            });
        },
        error : function() {
        }
    });

};
// 我的论文-取消收藏

wechat.pub.delCollectedPub = function(des3PubId, obj) {
    $.ajax({
        url : "/pubweb/ajaxdelCollectedPub",
        type : 'post',
        data : {
            "des3PubId" : des3PubId,
            "pubDb" : "SNS"
        },
        success : function(data) {
            if (data && data.result == "success") {
                $(obj).removeAttr("onclick");
                $(obj).attr("onclick",
                        "wechat.pub.importPdwh('" + des3PubId + "',this)");
                $(obj).find("a").removeClass("paper_footer-tool__box-click");
                $(obj).find("i").removeClass("paper_footer-comment__flag")
                        .addClass("paper_footer-comment");
                $(obj).find(".importPdwh").text("收藏");
            } else {
                scmpublictoast("取消收藏失败!", 1000);
            }
        }
    });
};

// 登录超时 跳转url
wechat.pub.PsnHometimeOut = function() {
    var url = "/psnweb/mobile/otherhome?des3ViewPsnId=" + $("#des3PsnId").val();
    document.location.href = "/oauth/mobile/index?sys=wechat&service="
            + encodeURIComponent(url);
}