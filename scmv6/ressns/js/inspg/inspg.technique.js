/*
 *hzr 
 */

(function($) {

    $.fn.technique = function(options) {
        var defaults = {
            "url" : "/inspg/const/ajaxTechnique",
            "inputIds" : "skillsFieldIds",
            "id" : "technique_list_div",// 技术领域弹出框默认ID
            "repeat" : false,// 技术领域默认不允许重复
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
            "_cur_disc_code" : null,// 当前选择的技术领域代码
            "_ajax" : function(techCode) {// ajax加载学科代码
                var _result = result;
                $.ajax({
                    url : defaults["url"],
                    type : 'post',
                    dataType : 'json',
                    data : (typeof techCode == "undefined" ? {} : {
                        "techCode" : techCode
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
                $("#technique_code_0").html(
                        $.technique.layerHtml(data["technique_code_0"],
                                defaults["locale"]));
                $(".dis_c_tech").each(
                        function() {
                            $(this).html(
                                    $.technique.subLayerHtml(data[$(this).attr(
                                            "id")], defaults["locale"]))
                        });
            }

        };
        $("body").append($.technique["createHtml"](defaults["id"]));// 技术领域弹出框内容布局
        $("body").append(
                $.technique["creatThinkBoxHtml"](input, defaults["id"]));// 弹出框属性
        $("#" + input).thickbox();// 初始化弹出框 defaults["id"]
        result["_ajax"]();// 添加各级技术领域

        // 点击显示或隐藏下级学科
        $(".tech_click").live("click", function(e) {
            $(this).parent().addClass("on");
            $(this).attr('class', 'tech_clicked');
            $(this).children('i').attr('class', 'Shear-headbottom');
            $(this).nextAll("div").toggle();
            return false;// 防止事件冒泡
        });

        $(".tech_clicked").live("click", function(e) {
            $(this).parent().removeClass("on");
            $(this).attr('class', 'tech_click');
            $(this).children('i').attr('class', 'Shear-head');
            $(this).nextAll("div").toggle();
            return false;// 防止事件冒泡
        });

        // 点击“+”，在右边添加选中选项
        $(".sel_tech").live(
                "click",
                function(e) {
                    var inputArr = $("#selectedItems_tech").children("a");
                    var selectedNum = inputArr.length;

                    // 在<a>中disabled无法生效，添加标记属性，被选中的项目无法被再次添加；
                    var selected = $(this).attr("al_selected");

                    if (typeof (selected) != 'undefined') {
                        return;
                    }

                    if (selectedNum >= 3) {
                        $.smate.scmtips.show('warn', "你只能选择最多3个技术领域");
                        return;
                    }

                    // var li = $(this).parent();
                    var id = $(this).attr("id");
                    var name = $(this).attr("name");
                    var techCode = $(this).attr("techCode");
                    $(this).children("i").removeClass("add_text");// addClass("correct_text");左侧选中项由“+”变为“勾”
                    $(this).attr("al_selected", "true");
                    $("#selectedItems_tech").append(
                            $.technique.selectedLayerHtml(id, name, techCode));
                    return false;
                });

        // 点击“-”，从右边删除选中选项
        $(".del_text").live("click", function(e) {
            var id = $(this).parent().attr("id");
            var name = $(this).parent().attr("name");
            var leftId = id.substring(4);
            $('#' + leftId).children("i").addClass("add_text");// 左侧选中项由“勾”变为“+”
                                                                // discipline_code_I01
            $('#' + leftId).removeClass("al_selected");
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
                        data : (typeof techCode == "undefined" ? {} : {
                            "techCode" : techCode
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
                            var inputTechCodes = $("#" + bindId).attr(
                                    "techCodes");
                            // 先清空，防止重复
                            $("#selectedItems_tech").html('');
                            if (inputNames == "undefined" || inputNames == null
                                    || inputNames == ""
                                    || inputTechCodes == "undefined"
                                    || inputTechCodes == null
                                    || inputTechCodes == "") {
                                return;
                            }

                            // 右侧显示,左侧展开
                            var names = $.trim(inputNames).split(";");
                            var techCodes = $.trim(inputTechCodes).split(";");
                            // code与名称长度不一样，返回
                            if (names.length != techCodes.length) {
                                return;
                            }

                            for (var i = 0; i < names.length; i++) {
                                var techCode = $.trim(techCodes[i]);
                                var name = $.trim(names[i]);
                                var id = "technique_code_" + techCode;
                                var code1 = techCode.substring(0, 1);
                                // 判断.add_text,即+号是否存在
                                var clickOrNot = $("#" + id).children(
                                        '.add_text').val();
                                // 判断是否是刚进入页面。+号存在，直接模拟点击事件；+号不存在，即已经点开，则拼接已经填写的内容；
                                if (typeof (clickOrNot) == 'undefined') {
                                    $("#selectedItems_tech").append(
                                            $.technique.selectedLayerHtml(id,
                                                    name, techCode));
                                    $("#" + id).attr("al_selected", "true");
                                } else {
                                    $("#" + id).find(".add_text").click();
                                }
                                $("#technique_code_" + code1).prevAll(
                                        "a.tech_click").click();
                            }
                        }
                    });

                });

        // 查询
        $("#tech_search_btn")
                .click(
                        function() {
                            var searchVal = $("#tech_search_value").val();
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
                                            "techCode" : searchVal1
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
                                                        "无符合条件的技术领域");
                                                return;
                                            }

                                            result["html"](data);
                                            $(".tech_click").click();
                                            // 右侧显示,左侧展开
                                            var selectedArr = $(
                                                    "#selectedItems_tech")
                                                    .children("a");

                                            if (typeof (selectedArr) == 'undefined'
                                                    || selectedArr.length == 0) {
                                                return;
                                            }

                                            var names = new Array();
                                            var techCodes = new Array();

                                            selectedArr.each(function() {
                                                names
                                                        .push($(this).attr(
                                                                'name'));
                                                techCodes.push($(this).attr(
                                                        'techcode'))

                                            });

                                            // 先清空，防止重复
                                            $("#selectedItems_tech").html('');
                                            for (var i = 0; i < names.length; i++) {
                                                var techCode = $
                                                        .trim(techCodes[i]);
                                                var name = $.trim(names[i]);
                                                var id = "technique_code_"
                                                        + techCode;
                                                var code1 = techCode.substring(
                                                        0, 1);
                                                // 判断.add_text,即+号是否存在
                                                var clickOrNot = $("#" + id)
                                                        .children('.add_text')
                                                        .val();
                                                // 判断是否是刚进入页面。+号存在，直接模拟点击事件；+号不存在，即已经点开，则拼接已经填写的内容；
                                                if (typeof (clickOrNot) == 'undefined') {
                                                    $("#selectedItems_tech")
                                                            .append(
                                                                    $.technique
                                                                            .selectedLayerHtml(
                                                                                    id,
                                                                                    name,
                                                                                    techCode))
                                                    $("#" + id).attr(
                                                            "al_selected",
                                                            "true");
                                                } else {
                                                    $("#" + id).find(
                                                            ".add_text")
                                                            .click();
                                                }
                                                $("#technique_code_" + code1)
                                                        .prevAll("a.tech_click")
                                                        .click();
                                            }
                                            $(".tech_click").click();
                                        }
                                    });

                        });

        // 将选择中技术领域填入界面
        $("#" + defaults["id"] + "_save")
                .click(
                        function() {
                            var inputArr = $("#selectedItems_tech").children(
                                    "a");
                            var selectedItems = $('#' + bindId).find('option');

                            var selectedNum = inputArr.length;
                            if (typeof selectedNum == "undefined"
                                    || selectedNum == 0) {
                                $.smate.scmtips.show('warn', "请选择对应技术领域");
                                return;
                            }
                            var inputNames = null;
                            var inputTechCodes = null;
                            inputArr
                                    .each(function() {
                                        var techCode = $(this).attr("techCode");
                                        var name = $(this).attr("name");
                                        inputTechCodes = (inputTechCodes == null ? techCode
                                                : inputTechCodes + "; "
                                                        + techCode);
                                        inputNames = (inputNames == null ? name
                                                : inputNames + "; " + name);
                                    });

                            selectedItems.val("");// 置空后再填充值
                            selectedItems.text(inputNames);
                            selectedItems.val(inputTechCodes); // TODO
                                                                // 在InsgpSetting.jsp中为disCodes填充值
                            $('#' + defaults['inputIds']).attr('value',
                                    inputTechCodes);
                            $('#' + defaults['inputIds']).attr('techNames',
                                    inputNames);
                            $('#' + bindId).attr("techCodes", inputTechCodes);

                            // SCM-7560 选择技术领域后，自动去掉为空提示语
                            if ($('#' + defaults['inputIds']).attr('value') != null
                                    && $('#' + defaults['inputIds']).attr(
                                            'techNames') != null) {
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

    $.technique = {
        "createHtml" : function(id) {
            var arr = [];
            arr.push('<div id="' + id + '" style="display:none;">');
            arr.push('<td class="pop_content">');
            arr.push('<div class="sel_lst" style="height:374px;">');

            arr
                    .push('<div class="sel_lstl" style="height: 374px;"><div class="lst"> ');
            arr
                    .push('<div class="Retrieval" style="padding-left:15px;"><input id="tech_search_value" type="text" style="color:#000000" class="Retrieval-input" value=""/><input id="tech_search_btn" name="" type="button" class="btn-search"/></div>');
            arr.push('<div class="clear"></div>');
            arr.push('<div class="sel_lw mtop10" id="technique_code_0">');
            arr.push('</div></div></div>');

            arr
                    .push('<div class="sel_lstr"><div class="sel_lstrw"><h4 class="mtop5"> 已选择：</h4><span>( 你最多可以选择3项)</span><div id= "selectedItems_tech" class="mtop10">');
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
        "layerHtml" : function(discArr, locale) {// 技术领域html
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
                                    var name = this[locale + "_name"];
                                    var tech_code = this["tech_code"];
                                    var textId = "technique_code_" + tech_code;
                                    arr
                                            .push('<li><a class="tech_click" herf="#"><i class="Shear-head"></i><span>'
                                                    + name
                                                    + '</span></a><div class="dis_c_tech" id="'
                                                    + textId
                                                    + '" style="display:none"><div></li>');
                                });
                arr.push('</ul>');
            }
            return arr.join(""); // 将数组转换成字符串
        },
        "subLayerHtml" : function(discArr, locale) {// 下级技术领域
            var arr = [];
            $.each(discArr, function() {
                var name = this[locale + "_name"];
                var tech_code = this["tech_code"];
                var textId = "technique_code_" + tech_code;

                arr.push('<a href="javascript:void(0)" class="sel_tech" id="'
                        + textId + '" name="' + name + '" techCode="'
                        + tech_code + '"><span>' + name
                        + '</span><i class="add_text"></i></a>');
            });
            return arr.join("");
        },
        "selectedLayerHtml" : function(id, name, techCode) {// 选中的技术领域
            var arr = [];
            arr.push('<a href="#" class="sel_led" id="sel_' + id + '" name="'
                    + name + '" techCode="' + techCode + '"><span>' + name
                    + '</span><i class="del_text"></i></a>');
            return arr.join("");
        },
        "creatThinkBoxHtml" : function(input, id) {
            return '<input id="'
                    + input
                    + '" class="thickbox" style="display:none;" alt="#TB_inline?height=414&amp;width=680&amp;inlineId='
                    + id + '" type="button" title="选择技术领域"/>';
        }
    };
})(jQuery);
