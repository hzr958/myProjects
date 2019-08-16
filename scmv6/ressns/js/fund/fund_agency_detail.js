var FundDetail = FundDetail ? FundDetail : {}
// 显示基金列表
FundDetail.showFundList = function() {
    var fundAgencyId = $("#fundAgencyId").val();
    var fundAgencyName = $("#fundAgencyName").val();
    var logoUrl = $("#logoUrl").val();
    var disIdStr = "[";
    var insType = "";
    $(".setting-list .cur").find(".dev_condition").each(function(i) {
        disIdStr += "," + $(this).attr("value");
    });
    disIdStr += "]";
    $(".setting-list .cur").find(".dev_insType").each(function(i) {
        insType += "." + $(this).attr("value");
    })
    disIdStr = disIdStr.replace(",", "");
    insType = insType.replace(".", "");
    window.Mainlist({
        name : "fund_list",
        listurl : "/prjweb/outside/ajaxfundlist",
        listdata : {
            "fundAgencyId" : fundAgencyId,
            "fundAgencyName" : fundAgencyName,
            "logoUrl" : logoUrl,
            "disIdStr" : disIdStr,
            "insType" : insType
        },
        method : "scroll",
        listcallback : function(xhr) {
           //加载基金图片
          SmateCommon.loadFundLogos($("#aidInsDeatils_des3FundIds").val(),$("#aidInsDetails_hasLogin"));
        }
    });
};

// 左侧显示查询条件
FundDetail.showLeftCondition = function() {
    var fundAgencyId = $("#fundAgencyId").val();
    $.ajax({
        url : "/prjweb/outside/ajaxfundcondition",
        type : "post",
        data : {
            "fundAgencyId" : fundAgencyId
        },
        dataType : "html",
        success : function(data) {
            $("#leftCondition").html(data);
            FundDetail.LeftConditionCount();// 统计数
        },
        error : function() {

        }
    });
};

// 左侧统计基金数
FundDetail.LeftConditionCount = function() {
    var fundAgencyId = $("#fundAgencyId").val();
    var length = $(".dev_condition").length;
    var disIdStr = "[";
    $(".dev_condition").each(function(i, dom) {
        if (i == length - 1) {
            disIdStr += $(this).attr("value");
        } else {
            disIdStr += $(this).attr("value") + ",";
        }
    });
    disIdStr = disIdStr + "]";
    $
            .ajax({
                url : "/prjweb/outside/ajaxfundcount",
                type : "post",
                data : {
                    "fundAgencyId" : fundAgencyId,
                    "disIdStr" : disIdStr
                },
                dataType : "json",
                success : function(data) {
                    if (data) {
                        var objdata = eval(data);
                        $(".dev_condition")
                                .each(
                                        function(i) {
                                            var _dowCount = $(this);
                                            $
                                                    .each(
                                                            objdata,
                                                            function(n, value) {
                                                                var disId = value['disId'];
                                                                var count = value['count'];

                                                                if (_dowCount
                                                                        .attr("value") == disId) {
                                                                    var htmlStr = "("
                                                                            + count
                                                                            + ")";
                                                                    _dowCount
                                                                            .next()
                                                                            .find(
                                                                                    "i")
                                                                            .html(
                                                                                    htmlStr);
                                                                }
                                                                if (disId
                                                                        .indexOf("insType") != -1) {
                                                                    $(
                                                                            ".dev_insType")
                                                                            .each(
                                                                                    function(
                                                                                            i) {
                                                                                        if (disId
                                                                                                .indexOf($(
                                                                                                        this)
                                                                                                        .attr(
                                                                                                                "value")) != -1) {
                                                                                            var htmlStr = "("
                                                                                                    + count
                                                                                                    + ")";
                                                                                            $(
                                                                                                    this)
                                                                                                    .next()
                                                                                                    .find(
                                                                                                            "i")
                                                                                                    .html(
                                                                                                            htmlStr);
                                                                                        }
                                                                                    });
                                                                }

                                                            });
                                        });
                        var numlist = document
                                .getElementsByClassName("funding-agencies_container-left_area-num");
                        for (var i = 0; i < numlist.length; i++) {
                            if (numlist[i].innerHTML == "(0)") {
                                var numList = numlist[i]
                                        .closest(".funding-agencies_container-left_area-item");
                                $(numList)
                                        .addClass(
                                                "funding-agencies_container-left_area-none");
                            }
                        }
                    }
                },
                error : function() {

                }
            });
};

// 左侧点击事件
FundDetail.LeftOnclick = function(obj) {
    var haveCur = $(obj).hasClass("cur");
    var span = $(obj).find("span");
    if ($(span).attr("value") == 0) { // 点击其他时
        if ($(span).hasClass("dev_condition")) {
            $(".dev_condition").each(function(i) {
                $(this).parent("li").removeClass("cur");
            });
        } else {
            $(".dev_insType").each(function(i) {
                $(this).parent("li").removeClass("cur");
            });
        }
    } else {
        $(".dev_condition[value='0']").parent("li").removeClass("cur");
        $(".dev_insType[value='0']").parent("li").removeClass("cur");
    }

    if ($(obj).hasClass("cur")) {
        $(obj).removeClass("cur");
    } else if (!haveCur) {
        $(obj).addClass("cur");
    }
}
