/**
 * 群组动态js
 */
var GrpDiscussList = GrpDiscussList ? GrpDiscussList : {};
/**
 * 收藏成果---zzx------------
 */
GrpDiscussList.dynCollectPub = function(des3PubId, resType, ev) {
    BaseUtils.doHitMore($(ev.currentTarget), 1000);
    if (resType == 'pub') {
        GrpDiscussList.CollectSnSPub(des3PubId, function() {
            scmpublictoast(dynCommon.CollectionSuccess, 2000);
        });
    } else if (resType == 'pdwhpub') {
        GrpDiscussList.CollectPdwhPub(des3PubId, function() {
            scmpublictoast(dynCommon.CollectionSuccess, 2000);
        });
    }
}
/**
 * 取消收藏成果
 */
GrpDiscussList.dynDelCollectPub = function(des3PubId, resType, ev) {
    BaseUtils.doHitMore($(ev.currentTarget), 1000);
    var pubDb = "";
    var collectOperate = 1; // 0==收藏， 1==取消收藏
    if (resType == 'pub') {
        pubDb = "SNS";
    } else if (resType == 'pdwhpub') {
        pubDb = "PDWH";
    }
    $.ajax({
        url : '/pubweb/ajaxdealCollectedPub',
        type : 'post',
        data : {
            "des3PubId" : des3PubId,
            "pubDb" : pubDb
        },
        dataType : "json",
        success : function(data) {
            GrpDiscussList.ajaxTimeOut(data, function() {
                if (data.result == "success") {
                    alert("")
                }

            });
        }

    });
}
GrpDiscussList.dealCollectedPub = function(pubId, pubDb, obj) {
    if (obj) {
        BaseUtils.doHitMore(obj, 500);
    }
    if (pubDb == 'pub') {
        pubDb = "SNS";
    } else if (pubDb == 'pdwhpub') {
        pubDb = "PDWH";
    }
    var collected = $(obj).attr("collected"); // 0==收藏 ， 1==取消收藏
    $.ajax({
        url : "/pub/opt/ajaxCollect",
        type : 'post',
        data : {
            "des3PubId" : pubId,
            "pubDb" : pubDb,
            "collectOperate" : collected
        },
        dateType : 'json',
        success : function(data) {
            BaseUtils.ajaxTimeOut(data, function() {
                if (data && data.result == "success") {
                    $(obj).attr("collected", collected == "1" ? "0" : "1");
                    $(obj).find("a").html(
                            collected == "1" ? discussCommon.collection
                                    : discussCommon.cancelCollection)
                    if (typeof collectedPubBack == "function") {
                        collectedPubBack(obj, collected, pubId, pubDb);
                    } else {
                        scmpublictoast(discussCommon.i18n_optSuccess, 1000);
                    }
                } else if (data && data.result == "exist") {
                    scmpublictoast(discussCommon.i18n_pubIsSaved, 1000);
                } else if (data && data.result == "isDel") {
                    scmpublictoast(discussCommon.i18n_pubIsDeleted, 1000);
                } else if (data && data.result == "not_exists") {
                    scmpublictoast(discussCommon.i18n_pubIsDeleted, 1000);
                } else {
                    scmpublictoast(discussCommon.i18n_pubImportError, 1000);
                }
            });
        },
        error : function(data) {
            scmpublictoast(pubi18n.i18n_pubImportError, 1000);
        }
    });
}
/**
 * 收藏个人库成果--zzx--------
 */
GrpDiscussList.CollectSnSPub = function(des3PubId, myFunction) {
    /*
     * var params=[]; params.push({"des3PubId":des3PubId}); var
     * postData={"jsonParams":JSON.stringify(params),"articleType":"2"};
     */
    $.ajax({
        url : '/pubweb/ajaxsavePaper',
        type : 'post',
        data : {
            "des3PubId" : des3PubId,
            "pubDb" : "SNS"
        },
        dataType : "json",
        success : function(data) {
            GrpDiscussList.ajaxTimeOut(data, function() {
                if (data.result == "success") {
                    if (typeof myFunction == "function") {
                        myFunction();
                    } else {
                        scmpublictoast(discussCommon.CollectionSuccess, 1000);
                    }
                } else if (data.result == "exist") {
                    scmpublictoast(discussCommon.pubIsSaved, 1000);
                } else if (data.result == "isDel") {
                    scmpublictoast(discussCommon.pubIsDeleted, 1000);
                } else {
                    scmpublictoast(discussCommon.CollectionFail, 1000);
                }
            });
        }

    });
}

/**
 * 收藏基准库成果--zzx--------
 */
GrpDiscussList.CollectPdwhPub = function(des3PubId, myFunction) {
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
            "pubDb" : "PDWH"
        },
        success : function(data) {
            GrpDiscussList.ajaxTimeOut(data, function() {
                if (data.result == "success") {
                    if (typeof myFunction == "function") {
                        myFunction();
                    } else {
                        scmpublictoast(discussCommon.CollectionSuccess, 1000);
                    }
                } else if (data.result == "exist") {
                    scmpublictoast(discussCommon.pubIsSaved, 1000);
                } else if (data.result == "isDel") {
                    scmpublictoast(discussCommon.pubIsDeleted, 1000);
                } else {
                    scmpublictoast("收藏失败", 1000);
                }
            });
        }
    });
}

GrpDiscussList.showDynsTimeStatus = false;
GrpDiscussList.showDynsTime = function(type) {
    var dynbtn = "<div class='dynamic-post__operation-box dev_delBtn'>"
            + "<div class='dynamic-operation__icon dev_dyn_menu' >"
            + " <span class=' material-icons' onclick='GrpDiscussList.openOptMenu(this,event)'>more_horiz</span>"
            + "</div>"
            + "<div class='dynamic-operation__actions-box dev_del_dyn'>"
            + "<ul class='menu__list style_dense-item '>"
            + "<li class='menu__item'><a>"
            + "<div class='menu-item__content '>"
            + "<div class='menu-item__content_icon'></div>"
            + "<div class='menu-item__content_main' onclick='GrpDiscussList.delgrpdyn(this,event)'>"
            + discussCommon.delDynamic + "</div>" + "</div>" + "</a></li>"
            + "</ul>" + "</div>" + "</div>";
    $(".discuss_box").each(function(i, obg) {
        $(obg).find(".show_time").html($(obg).attr("dynTime"));
        $(obg).find(".dev_delBtn").remove();
        $(obg).find(".show_time").after(dynbtn);
    });
    // 不是站外特殊操作，主要是站外要去除“删除该条动态的”的功能
    if (type != "outside") {
        $(".show_time").on(
                {
                    "mouseenter" : function() {
                        var $obj = $(this).closest(".discuss_box");
                        if ($obj.find(".dev_del_dyn").is(":hidden")
                                && !GrpDiscussList.showDynsTimeStatus) {
                            GrpDiscussList.showDynsTimeStatus = true;
                            $obj.find(".show_time").hide();
                            $obj.find(".dev_delBtn").show();
                            $obj.find(".dev_dyn_menu").show();
                            $obj.find(".dev_del_dyn").hide();
                            setTimeout(function() {
                                GrpDiscussList.showDynsTimeStatus = false;
                            }, 200);
                        }
                    }
                });
        $(".dev_dyn_menu").on(
                {
                    "mouseleave" : function(evt) {
                        var $obj = $(this).closest(".discuss_box");
                        if ($obj.find(".dev_del_dyn").is(":hidden")
                                && !GrpDiscussList.showDynsTimeStatus) {
                            GrpDiscussList.showDynsTimeStatus = true;
                            $obj.find(".show_time").show();
                            $obj.find(".dev_delBtn").hide();
                            setTimeout(function() {
                                GrpDiscussList.showDynsTimeStatus = false;
                            }, 200);
                        }
                    }
                });
        $(document).click(function() {
            $(".dev_delBtn").hide();
            $(".show_time").show();
        });
        /*
         * $(".dev_del_dyn").on({ "mouseleave":function(e){
         * if(!GrpDiscussList.showDynsTimeStatus){
         * GrpDiscussList.showDynsTimeStatus=true; var $obj =
         * $(this).closest(".discuss_box"); $obj.find(".dev_delBtn").hide();
         * $obj.find(".show_time").show();
         * setTimeout(function(){GrpDiscussList.showDynsTimeStatus=false;},200); } }
         * });
         */
    }
};
GrpDiscussList.showDynsOperation = function() {
    $(".discuss_box")
            .each(
                    function(i, obg) {
                        if ($(obg).find("input[name='dynId']").attr(
                                "databaseType") == 2) {
                            var href = $(obg).find("#dynamicCite").find("a")
                                    .attr("href");
                            href = href.replace(
                                    "/pubweb/publication/ajaxpubquote",
                                    "/pubweb/publication/ajaxpdwhpubquote");
                            $(obg).find("#dynamicCite").find("a").attr("href",
                                    href);

                            var onclick = $(obg).find("#dynamicCollection")
                                    .attr("onclick");
                            onclick = onclick.replace(
                                    "PubImportRef.importMyRef",
                                    "PubImportRef.importPdwhMyRef");
                            $(obg).find("#dynamicCollection").attr("onclick",
                                    onclick);

                            $(obg).find("#dymGrpShare").attr("databaseType",
                                    "2");

                            var dbId = $(obg).find("input[name='dynId']").attr(
                                    "dbId");
                            $(obg).find("#dymGrpShare").attr("dbId", dbId);

                        }
                    });
}
GrpDiscussList.openOptMenu = function(obj, e) {
    if (e.stopPropagation) {
        e.stopPropagation();
    } else {
        e.cancelBubble = true;
    }
    GrpDiscussList.showDynsTimeStatus = true;
    $(".dev_del_dyn").hide();
    $(".show_time").show();
    $(obj).closest(".dev_delBtn").find(".dev_del_dyn").show();
    $(obj).closest(".dev_delBtn").find(".dev_dyn_menu").hide();

    setTimeout(function() {
        GrpDiscussList.showDynsTimeStatus = false;
    }, 300);
}
GrpDiscussList.delgrpdyn = function(obj, e) {
    BaseUtils.checkCurrentSysTimeout("/groupweb/ajaxtimeout", function() {
        if (e.stopPropagation) {
            e.stopPropagation();
        } else {
            e.cancelBubble = true;
        }
        var $input = $(obj).closest(".main-list__item").find("input");
        if ($(obj).closest(".main-list__item").find(".discuss_box").attr(
                "isCanDel") == '0') {
            scmpublictoast("没有权限删除该动态", 1000);
            return;
        }
        $.ajax({
            url : "/dynweb/dynamic/grpdyn/ajaxdelgrpdyn",
            dataType : "json",
            type : "post",
            data : {
                des3DynId : $input.val(),
                des3GroupId : $("#grp_params").attr("smate_des3_grp_id"),
            },
            success : function(data) {

            }
        });
        $(obj).closest(".main-list__item").hide();
        scmpublictoast(discussCommon.delSuccess, 1000);
    }, 1);

}

// 超时处理
GrpDiscussList.ajaxTimeOut = function(data, myfunction) {
    var toConfirm = false;
    toConfirm = data.ajaxSessionTimeOut;
    if ('{"ajaxSessionTimeOut":"yes"}' == data) {
        toConfirm = true;
    }
    if (toConfirm) {
        jConfirm(discussCommon.sessionTimeout, discussCommon.tips, function(r) {
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
// 群组动态赞
GrpDiscussList.award = function(dynId, obj) {
    var restype = $(obj).closest(".main-list__item")
            .find("input[name='dynId']").attr("restype");
    var des3resid = $(obj).closest(".main-list__item").find(
            "input[name='dynId']").attr("des3resid");
    if (restype == "pdwhpub") {
        dynamic.pdwhIsExist(des3resid, function() {
            GrpDiscussList.awardCommonPart(dynId, obj);
        });
    } else {
        GrpDiscussList.awardCommonPart(dynId, obj);
    }
};

/**
 * 群组动态点赞公共部分
 */
GrpDiscussList.awardCommonPart = function(dynId, obj) {
    var des3DynId = $(obj).closest(".main-list__item").find(
            "input[name='dynId']").val();
    $.ajax({
        url : '/dynweb/dynamic/groupdyn/ajaxaward',
        type : 'post',
        dataType : 'json',
        data : {
            "des3DynId" : des3DynId
        },
        success : function(data) {
            GrpDiscussList.ajaxTimeOut(data, function() {
                var awardCount = data.awardCount;
                var post_data = {
                    "resType" : 1,
                    "resNode" : 1
                };
                var awardOperation = 0;
                if (true == data.hasAward) {
                    $(obj).find("a").text(
                            discussCommon.unlike + "(" + awardCount + ")");
                    post_data.status = '1';
                    awardOperation = 1;
                } else {
                    post_data.status = '0';// 取消赞
                    awardOperation = 0; // 取消赞
                    if (0 == awardCount) {
                        $(obj).find("a").text(discussCommon.like);
                    } else {
                        $(obj).find("a").text(
                                discussCommon.like + "(" + awardCount + ")");
                    }
                }
                // 同步成果 基金 赞 start
                var des3ResId = $(obj).closest(".discuss_box").find(
                        "input[name='dynId']").attr("des3resid");
                post_data.des3ResId = des3ResId;
                var dyntype = $(obj).closest(".discuss_box").find(
                        "input[name='dynId']").attr("dyntype");
                var resType = $(obj).closest(".discuss_box").find(
                        "input[name='dynId']").attr("resType");
                post_data.operate = awardOperation;
                if (des3ResId !== undefined && des3ResId !== "") {
                    if ("GRP_ADDPUB" == dyntype || "GRP_PUBLISHDYN" == dyntype
                            || "GRP_SHAREPUB" == dyntype) {// 成果赞
                        post_data.des3PubId = des3ResId;
                        GrpDiscussList.syncPubAward(obj, post_data);
                    }
                    if ("GRP_SHAREFUND" == dyntype) {// 基金赞
                        GrpDiscussList.syncFundAward(obj, des3ResId,
                                awardOperation);
                    }
                    if ("GRP_SHAREPRJ" == dyntype) {// 同步项目赞
                        GrpDiscussList.synchronizePrjAward(post_data);
                    }
                    if ("GRP_SHAREAGENCY" == dyntype) {// 资助机构赞
                        GrpDiscussList.syncAgencyAward(obj, des3ResId,
                                awardOperation);
                    }
                    if ("GRP_SHARENEWS" == dyntype) {// 新闻赞
                        post_data.des3NewsId = des3ResId;
                        GrpDiscussList.syncNewsAward(obj, post_data);
                    }
                    if ("GRP_SHAREPDWHPUB" == dyntype) {// 基准库成果
                        post_data.des3PdwhPubId = des3ResId;
                        GrpDiscussList.syncPdwhPubAward(obj, post_data);
                    }
                }
            });

        },
        error : function() {
        }
    });
}

// 同步资助机构的赞
GrpDiscussList.syncAgencyAward = function(obj, des3ResId, awardOperation) {
    if ($(obj).closest(".discuss_box").find(".dyn_content").html() == "") {
        $.ajax({
            url : "/prjweb/agency/ajaxaward",
            type : "post",
            data : {
                "Des3FundAgencyId" : des3ResId,
                "optType" : awardOperation
            },
            dataType : "json",
            success : function(data) {
            },
            error : function() {
            }
        });
    }
}

// 同步基金的赞
GrpDiscussList.syncFundAward = function(obj, des3ResId, awardOperation) {
    if ($(obj).closest(".discuss_box").find(".dyn_content").html() == "") {
        $.ajax({
            url : "/prjweb/fund/ajaxaward",
            type : "post",
            data : {
                "encryptedFundId" : des3ResId,
                "awardOperation" : awardOperation
            },
            dataType : "json",
            success : function(data) {
            },
            error : function() {
            }
        });
    }
}

// 同步成果赞
GrpDiscussList.syncPubAward = function(obj, post_data) {
    if ($(obj).closest(".discuss_box").find(".dyn_content").html() == "") {
        $.ajax({
            url : '/pub/opt/ajaxlike',
            type : 'post',
            dataType : 'json',
            data : post_data,
            success : function(data) {
            },
            error : function() {
            }
        });
    }
}

// 同步基准库成果赞
GrpDiscussList.syncPdwhPubAward = function(obj, post_data) {
    if ($(obj).closest(".discuss_box").find(".dyn_content").html() == "") {
        $.ajax({
            url : '/pub/opt/ajaxpdwhlike',
            type : 'post',
            dataType : 'json',
            data : post_data,
            success : function(data) {
            },
            error : function() {
            }
        });
    }
}

// 同步新闻赞
GrpDiscussList.syncNewsAward = function(obj, post_data) {
    if ($(obj).closest(".discuss_box").find(".dyn_content").html() == "") {
        $.ajax({
            url : '/dynweb/news/ajaxupdatenewsaward',
            type : 'post',
            dataType : 'json',
            data : post_data,
            success : function(data) {
            },
            error : function() {
            }
        });
    }
}

/**
 * des3ResId status 1=点赞 ； 0=取消赞
 */
GrpDiscussList.synchronizePrjAward = function(post_data) {
    var awardUrl = "";
    if (post_data.status == "1") {
        awardUrl = "/prjweb/project/ajaxprjaddaward";
    } else {
        awardUrl = "/prjweb/project/ajaxprjcancelaward";
    }
    $.ajax({
        url : awardUrl,
        type : "post",
        data : {
            "resType" : 4,
            "resNode" : 1,
            "des3Id" : post_data.des3ResId,
        },
        dataType : "json",
        success : function(data) {
            if (data.result == "success") {

            }
        },
        error : function(date) {

        }
    });
}

// 群组首页动态分享
GrpDiscussList.shareGrpDynamic = function(obj) {
    var restype = $(obj).attr("restype");
    var $box_obj = $(obj).closest(".dynamic__box");
    var des3ResId = $box_obj.find("input[name=dynId]").attr("des3ResId");
    var resid = $box_obj.find("input[name=dynId]").attr("resid");

    if (des3ResId == undefined) {
        des3ResId = resid;
    }
    if (restype == "pub") {
        GrpDiscussList.checkPub(obj, des3ResId);
    } else if (restype == "fund" || restype == "agency") {
        (restype == "fund") ? restype = 11 : restype = 25;
        GrpDiscussList.checkFundAgency(des3ResId, restype, obj)
    } else if (restype == "pdwhpub") {
        dynamic.pdwhIsExist(des3ResId, function() {
            SmateShare.getGroupParam(obj);
            initSharePlugin(obj);
        });
    } else if (restype == "grpfile") {
        GrpDiscussList.checkGrpFile(obj, des3ResId);
    } else if (restype == "news") {
        GrpDiscussList.checkNewsExist(obj, des3ResId);
    } else if (restype == "prj") {
        if (!GrpDiscussList.checkProjectExist(des3ResId)) {
            SmateShare.getGroupParam(obj);
            initSharePlugin(obj);
        }
    } else {
        SmateShare.getGroupParam(obj);
        initSharePlugin(obj);
    }
}

GrpDiscussList.checkProjectExist = function(des3ResId) {
    var a = false;
    $.ajax({
        url : '/prjweb/ajaxCheckPjr',
        type : 'post',
        async : false,
        data : {
            "des3PrjId" : des3ResId,
        },
        success : function(data) {
            if (data.status == "true") {
                scmpublictoast(dynCommon.projectIsDel, 2000);
                a = true;
            }
        }
    });
    return a;
}
/**
 * 检查新闻是否被删除
 */
GrpDiscussList.checkNewsExist = function(obj, des3ResId) {
    $.ajax({
        url : '/dynweb/news/ajaxCheckNews',
        type : 'post',
        async : false,
        data : {
            "des3NewsId" : des3ResId,
        },
        success : function(data) {
            if (data.status == 'isDel') {
                scmpublictoast(dynCommon.newsIsDeleted, 1000);
            } else {
                SmateShare.getGroupParam(obj);
                initSharePlugin(obj);
            }
        }
    });
}

/**
 * 检查数据库中基金资助机构是否被删除
 */
GrpDiscussList.checkFundAgency = function(des3ResId, resType, obj) {
    $.ajax({
        url : '/prjweb/ajaxCheckPub',
        type : 'post',
        async : false,
        data : {
            "Des3FundAgencyId" : des3ResId,
            "resType" : resType
        },
        success : function(data) {
            if (data.status == "fundIsDel") {
                scmpublictoast(dynCommon.fundIsDel, 2000);
            } else if (data.status == "fundAgencyIsDel") {
                scmpublictoast(dynCommon.fundAgencyIsDel, 2000);
            } else {
                SmateShare.getGroupParam(obj);
                initSharePlugin(obj);
            }
        }
    });
}
/**
 * 检查个人库成果是否被删除 以及 是否是隐私成果
 */
GrpDiscussList.checkPub = function(obj, des3ResId) {
    $.ajax({
        url : '/pub/ajaxCheckPub',
        type : 'post',
        data : {
            "des3PubId" : des3ResId
        },
        dataType : "json",
        success : function(data) {
            if (data.status == "isDel") {
                scmpublictoast(discussCommon.i18n_pubIsDeleted, 2000);
            } else if (data.status == "isSelfPrivate") {
                smate.showTips._showNewTips(discussCommon.pubShareContent,
                        discussCommon.tips, "closeGrpDynDiv()", "",
                        dynCommon.chooseCloseBtn, "");
                $("#alert_box_cancel_btn").hide();
            } else if (data.status == "isNotSelfPrivate") {
                smate.showTips._showNewTips(
                        discussCommon.pubNotOwenerShareContent,
                        discussCommon.tips, "closeGrpDynDiv()", "",
                        dynCommon.chooseCloseBtn, "");
                $("#alert_box_cancel_btn").hide();
            } else {
                SmateShare.getGroupParam(obj);
                initSharePlugin(obj);
            }
        }
    });
}

/**
 * 检查群组文件是否被删除
 */
GrpDiscussList.checkGrpFile = function(obj, des3ResId) {
    $.ajax({
        url : '/groupweb/grpfile/ajaxCheckGrpFile',
        type : 'post',
        data : {
            "des3GrpFileId" : des3ResId
        },
        dataType : "json",
        success : function(data) {
            if (data.status == "isDel") {
                scmpublictoast(discussCommon.grpFileIsDeleted, 2000);
            } else {
                SmateShare.getGroupParam(obj);
                initSharePlugin(obj);
            }
        }
    });
}

function closeGrpDynDiv() {
    var pluginclose = document
            .getElementsByClassName("new-searchplugin_container-close")[0];
    if ($(".new-searchplugin_container")) {
        $(".background-cover").remove();
        $(".new-searchplugin_container").remove();
    }
}