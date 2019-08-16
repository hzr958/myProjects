/*
 *hzr 
 */

(function($) {

    $.fn.discipline = function(options) {
        var defaults = {
            "url" : "/inspg/const/ajaxDiscipline",
            "inputIds" : "disciplineFieldIds",
            "id" : "discipline_list_div",// 学科领域弹出框默认ID
            "repeat" : false,// 学科领域默认不允许重复
            "locale" : "zh_CN",// 国际化
            "simple" : false,// 默认要求至少选择第二级别
            "bind" : null
        // 保存或者取消的绑定方法
        };
        var input = defaults["id"] + "_input";// thinkbox属性标签
        var bindId = defaults["id"] + "_k";
        var _this = this;

        var result = {
            "obj" : null,// 对象：最后一个触发学科代码的下拉框
            "_cur_disc_code" : null,// 当前选择的学科代码
            "_ajax" : function(discCode) {// ajax加载学科代码
                var _result = result;
                $.ajax({
                    url : defaults["url"],
                    type : 'post',
                    dataType : 'json',
                    data : (typeof discCode == "undefined" ? {} : {
                        "discCode" : discCode
                    }),
                    success : function(data) {
                        var timeOut = data['ajaxSessionTimeOut'];
                        if (timeOut != null && typeof timeOut != 'undefined'
                                && timeOut == 'yes') {
                            // 防止没有载入jConfirm插件
                            if (jConfirm) {
                                // 超时
                                jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r) {
                                    if (r) {
                                        var url = document.location.href;
                                        url = url.replace('#', '');
                                        document.location.href = url;
                                    }
                                });
                            } else {
                                alert("系统超时或未登录，你要登录吗？");
                                window.location.reload();
                            }
                        }
                        _result["html"](data);
                    }
                });

                return this;
            },
            "html" : function(data) {// json数据生成学科代码列表
                // $("#discipline_code_0").html($.discipline.layerHtml(data[$(this).attr("id")],
                // defaults["locale"]));
                $("#discipline_code_0").html(
                        $.discipline.layerHtml(data["discipline_code_0"],
                                defaults["locale"]));
                $(".dis_c").each(
                        function() {
                            $(this).html(
                                    $.discipline.subLayerHtml(data[$(this)
                                            .attr("id")], defaults["locale"]))
                        });
            }

        };
        $("body").append($.discipline["createHtml"](defaults["id"]));// 学科领域弹出框内容布局
        $("body").append(
                $.discipline["creatThinkBoxHtml"](input, defaults["id"]));// 弹出框属性
        $("#" + input).thickbox();// 初始化弹出框 defaults["id"]
        result["_ajax"]();// 添加各级学科

        // 点击显示或隐藏下级学科
        $(".disc_click").live("click", function(e) {
            $(this).parent().addClass("on");
            $(this).attr('class', 'disc_clicked');
            $(this).children('i').attr('class', 'Shear-headbottom');
            $(this).nextAll("div").toggle();
            return false;// 防止事件冒泡
        });

        $(".disc_clicked").live("click", function(e) {
            $(this).parent().removeClass("on");
            $(this).attr('class', 'disc_click');
            $(this).children('i').attr('class', 'Shear-head');
            $(this).nextAll("div").toggle();
            return false;// 防止事件冒泡
        });

        // 点击“+”，在右边添加选中选项
        $(".sel").live(
                "click",
                function(e) {
                    // $('sel_lstl').children().find(".sel").live("click",function(e){
                    var inputArr = $("#selectedItems").children("a");
                    var selectedNum = inputArr.length;

                    // 在<a>中disabled无法生效，添加标记属性，被选中的项目无法被再次添加；
                    var selected = $(this).attr("al_selected");

                    if (typeof (selected) != 'undefined') {
                        return;
                    }

                    if (selectedNum >= 3) {
                        $.smate.scmtips.show('warn', "你只能选择最多3个学科");
                        return;
                    }

                    // var li = $(this).parent();
                    var id = $(this).attr("id");
                    var name = $(this).attr("name");
                    var disCode = $(this).attr("disCode");
                    $(this).children("i").removeClass("add_text");// addClass("correct_text");左侧选中项由“+”变为“勾”
                    $(this).attr("al_selected", "true");
                    $("#selectedItems").append(
                            $.discipline.selectedLayerHtml(id, name, disCode));
                    return false;
                });

        /*
         * $(".sel").live("click",function(e){
         * $(this).children("i.add_text").click(); });
         */

        // 点击“-”，从右边删除选中选项
        $(".del_text").live("click", function(e) {
            var id = $(this).parent().attr("id");
            var name = $(this).parent().attr("name");
            var leftId = id.substring(4);
            $('#' + leftId).children("i").addClass("add_text");// 左侧选中项由“勾”变为“+”
                                                                // discipline_code_I01
            $('#' + leftId).removeAttr("al_selected");
            // 在右侧删除
            $('#' + id).remove();
            return false;
        });

        // 如果值填充目标处已经有值，需要在弹出框中重新复原值对应项已经被选中的界面
        $("#" + bindId).click(
                function() {

                    $.ajax({
                        url : defaults["url"],
                        type : 'post',
                        dataType : 'json',
                        data : (typeof discCode == "undefined" ? {} : {
                            "discCode" : discCode
                        }),
                        success : function(data) {
                            var timeOut = data['ajaxSessionTimeOut'];
                            if (timeOut != null
                                    && typeof timeOut != 'undefined'
                                    && timeOut == 'yes') {
                                // 防止没有载入jConfirm插件
                                if (jConfirm) {
                                    // 超时
                                    jConfirm("系统超时或未登录，你要登录吗？", "提示", function(
                                            r) {
                                        if (r) {
                                            var url = document.location.href;
                                            url = url.replace('#', '');
                                            document.location.href = url;
                                        }
                                    });
                                } else {
                                    alert("系统超时或未登录，你要登录吗？");
                                    window.location.reload();
                                }
                            }
                            // 重新布局
                            result["html"](data);
                            $("#" + input).click();
                            $("#" + bindId).blur();

                            var inputNames = $("#" + bindId).text().replace(
                                    /[\r\n]/g, "");// 去掉回车换行;
                            var inputDisCodes = $("#" + bindId)
                                    .attr("disCodes");
                            // 先清空，防止重复
                            $("#selectedItems").html('');
                            if (inputNames == "undefined" || inputNames == null
                                    || inputNames == ""
                                    || inputDisCodes == "undefined"
                                    || inputDisCodes == null
                                    || inputDisCodes == "") {
                                return;
                            }
                            // 右侧显示,左侧展开
                            var names = $.trim(inputNames).split(";");
                            var disCodes = $.trim(inputDisCodes).split(";");
                            // code与名称长度不一样，返回
                            if (names.length != disCodes.length) {
                                return;
                            }

                            for (var i = 0; i < names.length; i++) {
                                var disCode = $.trim(disCodes[i]);
                                var name = $.trim(names[i]);
                                var id = "discipline_code_" + disCode;
                                var code1 = disCode.substring(0, 1);
                                // 判断.add_text,即+号是否存在
                                var clickOrNot = $("#" + id).children(
                                        '.add_text').val();
                                // 判断是否是刚进入页面。+号存在，直接模拟点击事件；+号不存在，即已经点开，则拼接已经填写的内容；
                                if (typeof (clickOrNot) == 'undefined') {
                                    $("#selectedItems").append(
                                            $.discipline.selectedLayerHtml(id,
                                                    name, disCode));
                                    $("#" + id).attr("al_selected", "true");
                                } else {
                                    $("#" + id).find(".add_text").click();
                                }
                                $("#discipline_code_" + code1).prevAll(
                                        "a.disc_click").click();
                            }
                        }
                    });

                });

        // 查询
        $("#disc_search_btn")
                .click(
                        function() {
                            var searchVal = $("#disc_search_value").val();
                            if (typeof (searchVal) == 'undefined'
                                    || searchVal == null) {
                                return;
                            }
                            searchVal1 = $.trim(searchVal);
                            $
                                    .ajax({
                                        url : defaults["url"],
                                        type : 'post',
                                        dataType : 'json',
                                        data : {
                                            "discCode" : searchVal1
                                        },
                                        success : function(data) {
                                            var timeOut = data['ajaxSessionTimeOut'];
                                            if (timeOut != null
                                                    && typeof timeOut != 'undefined'
                                                    && timeOut == 'yes') {
                                                // 防止没有载入jConfirm插件
                                                if (jConfirm) {
                                                    // 超时
                                                    jConfirm(
                                                            "系统超时或未登录，你要登录吗？",
                                                            "提示",
                                                            function(r) {
                                                                if (r) {
                                                                    var url = document.location.href;
                                                                    url = url
                                                                            .replace(
                                                                                    '#',
                                                                                    '');
                                                                    document.location.href = url;
                                                                }
                                                            });
                                                } else {
                                                    alert("系统超时或未登录，你要登录吗？");
                                                    window.location.reload();
                                                }
                                            }

                                            if (data['isNull'] == 'true') {
                                                $.smate.scmtips.show('warn',
                                                        "无符合条件的学科领域");
                                                return;
                                            }

                                            result["html"](data);
                                            $(".disc_click").click();
                                            // 右侧显示,左侧展开
                                            var selectedArr = $(
                                                    "#selectedItems").children(
                                                    "a");

                                            if (typeof (selectedArr) == 'undefined'
                                                    || selectedArr.length == 0) {
                                                return;
                                            }

                                            var names = new Array();
                                            var discodes = new Array();

                                            selectedArr.each(function() {
                                                names
                                                        .push($(this).attr(
                                                                'name'));
                                                discodes.push($(this).attr(
                                                        'discode'))

                                            });

                                            // 先清空，防止重复
                                            $("#selectedItems").html('');
                                            for (var i = 0; i < names.length; i++) {
                                                var disCode = $
                                                        .trim(discodes[i]);
                                                var name = $.trim(names[i]);
                                                var id = "discipline_code_"
                                                        + disCode;
                                                var code1 = disCode.substring(
                                                        0, 1);
                                                // 判断.add_text,即+号是否存在
                                                var clickOrNot = $("#" + id)
                                                        .children('.add_text')
                                                        .val();
                                                // 判断是否是刚进入页面。+号存在，直接模拟点击事件；+号不存在，即已经点开，则拼接已经填写的内容；
                                                if (typeof (clickOrNot) == 'undefined') {
                                                    $("#selectedItems")
                                                            .append(
                                                                    $.discipline
                                                                            .selectedLayerHtml(
                                                                                    id,
                                                                                    name,
                                                                                    disCode))
                                                    $("#" + id).attr(
                                                            "al_selected",
                                                            "true");
                                                } else {
                                                    $("#" + id).find(
                                                            ".add_text")
                                                            .click();
                                                }
                                                $("#discipline_code_" + code1)
                                                        .prevAll("a.disc_click")
                                                        .click();
                                            }
                                            $(".disc_click").click();
                                        }
                                    });

                        });

        // 将选择中学科填入界面
        $("#" + defaults["id"] + "_save")
                .click(
                        function() {
                            var inputArr = $("#selectedItems").children("a");
                            var selectedItems = $('#' + bindId).find('option');

                            var selectedNum = inputArr.length;
                            if (typeof selectedNum == "undefined"
                                    || selectedNum == 0) {
                                $.smate.scmtips.show('warn', "请选择对应学科");
                                return;
                            }
                            var inputNames = null;
                            var inputDisCodes = null;
                            inputArr
                                    .each(function() {
                                        var disCode = $(this).attr("disCode");
                                        var name = $(this).attr("name");
                                        inputDisCodes = (inputDisCodes == null ? disCode
                                                : inputDisCodes + "; "
                                                        + disCode);
                                        inputNames = (inputNames == null ? name
                                                : inputNames + "; " + name);
                                    });

                            selectedItems.val("");// 置空后再填充值
                            selectedItems.text(inputNames);
                            selectedItems.val(inputDisCodes); // TODO
                                                                // 在InsgpSetting.jsp中为disCodes填充值
                            $('#' + defaults['inputIds']).attr('value',
                                    inputDisCodes);
                            $('#' + defaults['inputIds']).attr('discNames',
                                    inputNames);
                            $('#' + bindId).attr("disCodes", inputDisCodes);

                            // SCM-7560 选择学科领域后，自动去掉为空提示语
                            if ($('#' + defaults['inputIds']).attr('value') != null
                                    && $('#' + defaults['inputIds']).attr(
                                            'discNames') != null) {
                                $('#' + defaults['inputIds'])
                                        .parent()
                                        .find("label")
                                        .each(
                                                function() {
                                                    if ($(this).attr("for") == defaults['inputIds']) {
                                                        $(this).prev().remove();
                                                        $(this).remove();
                                                    }
                                                });
                            }

                            var _is_close = true;
                            if (defaults["bind"] != null) { // bind，定义在保存时需要额外操作的函数
                                _is_close = defaults["bind"](result, "save",
                                        function() {
                                            $.Thickbox.closeWin();
                                        }, _this.attr("id"));
                            }
                            if (typeof _is_close == "undefined" || _is_close) {// 默认关闭窗口
                                $.Thickbox.closeWin();
                            }

                        });

        $("#" + defaults["id"] + "_cancel").click(function() {// 关闭学科代码窗口
            $.Thickbox.closeWin();
            if (defaults["bind"] != null) {
                defaults["bind"](result, "cancel");
            }
        });
        return result;
    };

    $.discipline = {
        "createHtml" : function(id) {
            var arr = [];
            arr.push('<div id="' + id + '" style="display:none;">');
            arr.push('<td class="pop_content">');
            arr.push('<div class="sel_lst" style="height:374px;">');

            arr
                    .push('<div class="sel_lstl" style="height: 374px;"><div class="lst"> ');
            arr
                    .push('<div class="Retrieval" style="padding-left:15px;"><input id="disc_search_value" type="text" style="color:#000000" class="Retrieval-input" value=""/><input id="disc_search_btn" name="" type="button" class="btn-search"/></div>');
            arr.push('<div class="clear"></div>');
            arr.push('<div class="sel_lw mtop10" id="discipline_code_0">');
            arr.push('</div></div></div>');

            arr
                    .push('<div class="sel_lstr"><div class="sel_lstrw"><h4 class="mtop5"> 已选择：</h4><span>( 你最多可以选择3项)</span><div id= "selectedItems" class="mtop10">');
            arr.push('</div></div></div>');
            arr.push('</div>');
            arr.push('<div class="clear"></div>');
            arr.push('<div class="pop_buttom">');
            arr
                    .push('<input type="button" name="submit" id="'
                            + id
                            + '_save" value="确定" class="uiButton uiButtonConfirm text14"/>　');
            arr
                    .push('<input type="button" name="cancel" id="'
                            + id
                            + '_cancel" value="取消" class="uiButton text14 mright10"/></td>');
            return arr.join("");
        },
        "layerHtml" : function(discArr, locale) {// 学科代码html
            var arr = [];
            if (typeof discArr == "undefined") {
                // 修改在"IE10"下出现线条缺失的情况
                arr.push('<div></div>');
            } else {
                arr.push('<ul>');
                $
                        .each(
                                discArr,
                                function() {
                                    // var id = this["id"];
                                    var name = this[locale + "_name"];
                                    var disc_code = this["disc_code"];
                                    var textId = "discipline_code_" + disc_code;
                                    arr
                                            .push('<li><a class = "disc_click" href="#"><i class="Shear-head"></i><span>'
                                                    + name
                                                    + '</span></a><div class="dis_c" id="'
                                                    + textId
                                                    + '" style="display:none"><div></li>');
                                });
                arr.push('</ul>');
            }
            return arr.join(""); // 将数组转换成字符串
        },
        "subLayerHtml" : function(discArr, locale) {// 下级学科
            var arr = [];
            $.each(discArr, function() {
                var name = this[locale + "_name"];
                var disc_code = this["disc_code"];
                var textId = "discipline_code_" + disc_code;

                arr.push('<a href="javascript:void(0)" class="sel" id="'
                        + textId + '" name="' + name + '" disCode="'
                        + disc_code + '"><span>' + name
                        + '</span><i class="add_text"></i></a>');
            });
            return arr.join("");
        },
        "selectedLayerHtml" : function(id, name, disCode) {// 选中的学科
            var arr = [];
            arr.push('<a href="#" class="sel_led" id="sel_' + id + '" name="'
                    + name + '" disCode="' + disCode + '"><span>' + name
                    + '</span><i class="del_text"></i></a>');
            return arr.join("");
        },
        "creatThinkBoxHtml" : function(input, id) {

            return '<input id="'
                    + input
                    + '" class="thickbox" style="display:none;" alt="#TB_inline?height=414&amp;width=680&amp;inlineId='
                    + id + '" type="button" title="选择学科领域"/>';
        }
    };
})(jQuery);
