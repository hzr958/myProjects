/**
 * @author zym
 * 学科领域插件
 */
(function($){
	
	var smate = smate ? smate : {};
	smate.discipline = smate.discipline ? smate.discipline : {};
	
    $.fn.smate.discipline = function(options){
        var defaults = {
            "url": "/const/ajaxDiscipline",
            "id": "discipline_list_div",//学科领域弹出框默认ID
            "repeat": false,//学科领域默认不允许重复
            "locale": null,//国际化
            "ctx": null,//上下文路径
            "resmod": null,//资源路径
            "simple": false,//默认要求至少选择第三级别
            "bind": null//保存或者取消的绑定方法
        };
        if ($("#" + defaults["id"]).attr("id") == defaults["id"]) {
            alert("警告：学科领域插件重复定义");
            return;
        }
        var _input = defaults["id"] + "_input";//thinkbox属性标签
        var _this = this;
        if (typeof options != "undefined") {
            $.extend(defaults, options);
        }
        
        //加载上下文路径
        if (defaults["ctx"] == null) {
            alert("学科领域插件缺少上下文路径");
        }
        
        if (defaults["resmod"] == null) {
            alert("学科领域插件缺少资源路径");
        }
        
        defaults["url"] = defaults["ctx"] + defaults["url"];
        
        //加载系统当前语言
        if (defaults["locale"] == null && typeof locale != "undefined") {
            defaults["locale"] = locale;
        }
        
        //返回结果和操作方法
        var result = {
            "obj": null,//对象：最后一个触发学科代码的下拉框
            "_cur_disc_code": null,//当前选择的学科代码
            "_ajax": function(discCode){//ajax加载学科代码
                var _result = result;
                $.ajax({
                    url: defaults["url"],
                    type: 'post',
                    dataType: 'json',
                    data: (typeof discCode == "undefined" ? {} : {
                        "discCode": discCode
                    }),
                    success: function(data){
                    	var timeOut = data['ajaxSessionTimeOut'];
            			if(timeOut != null && typeof timeOut != 'undefined' && timeOut == 'yes'){
            				if(jConfirm){
            					// 超时
                				jConfirm($.discipline["_helper"](locale, "label.loginTimeout"),
                						$.discipline["_helper"](locale, "label.tip"), function(r) {
                					if(r) {
                						var url = document.location.href;
                						url = url.replace('#', '');
                						document.location.href = url;
                					}
                				});
            				}else{
            					alert($.discipline["_helper"](locale, "label.loginTimeout"));
                	            window.location.reload();
            				}
            	        }
                        _result["_html"](data);
                    }
                });
                
                return this;
            },
            "_html": function(data){//json数据生成学科代码列表
                $("#discipline_code_1,#discipline_code_2,#discipline_code_3,#discipline_code_4").each(function(){
                    $(this).html($.discipline._layer_html(data[$(this).attr("id")], defaults["locale"]));
                });
                this["_select_css"]();
            },
            "_select_css": function(_parent){//当前选择的学科代码高亮显示
                if (typeof _parent != "undefined") {
                    _parent.find(".discipline_body_li_select").removeClass("discipline_body_li_select");
                }
                var _cur_disc_code = this["_cur_disc_code"];
                if (_cur_disc_code != null) {
                    $(".discipline_body ol li").each(function(){
                        var liId = $(this).attr("id");
                        if (/^.+disc_code_(.{1,7})$/gi.test(liId)) {
                            var tmp_disc_code = RegExp.$1;
                            if (_cur_disc_code.indexOf(tmp_disc_code) == 0) {
                                $(this).addClass("discipline_body_li_select");
                            }
                        }
                    });
                }
                this["_disc_check"]();
            },
            "_disc_check": function(){//检查选择的学科代码是否合法
                var _cur_disc_code = this["_cur_disc_code"];
                var _val = this.val();
                var _tip_div = $(".discipline_tips_text div").removeClass("discipline_warn").removeClass("discipline_yes").removeClass("discipline_no");
                if (_cur_disc_code == null || _cur_disc_code == "") {//未选择
                    _tip_div.addClass("discipline_warn");
                    _tip_div.html($.discipline["_helper"](defaults["locale"], "label.disciplineChoosenTips"));
                } else if (/^.{1,3}$/gi.test(_cur_disc_code) && !defaults["simple"]) {//过于简单，并且未取消选择级别限制
                    _tip_div.addClass("discipline_no");
                    var _part1 = $.discipline["_helper"](defaults["locale"], "label.disciplineChoosen");
                    var _part2 = defaults["locale"] == "zh_CN" ? '“name”' : '"name"';
                    _part2 = _part2.replace("name", _val == null ? "" : _val["name"]);
                    var _part3 = $.discipline["_helper"](defaults["locale"], "label.disciplineChoosenTooSimple");
                    _tip_div.html(_part1 + '<span class="discipline_name">' + _part2 + '</span>' + _part3);
                } else if (/^.{5,7}$/gi.test(_cur_disc_code) && !defaults["repeat"] && this._is_dup()) {//第3级重复检查
                    _tip_div.addClass("discipline_no");
                    var _part1 = $.discipline["_helper"](defaults["locale"], "label.currentChoose");
                    var _part2 = defaults["locale"] == "zh_CN" ? '“name”' : '"name"';
                    _part2 = _part2.replace("name", _val == null ? "" : _val["name"]);
					if(locale == "zh_CN"){
						_part2 = _part2.length>18?_part2.substring(0,18)+"..."+_part2.substr(-1,1):_part2;
					}else{
						_part2 = _part2.length>28?_part2.substring(0,28)+"..."+_part2.substr(-1,1):_part2;
					}
                    var _part3 = $.discipline["_helper"](defaults["locale"], "label.multiple");
                    _tip_div.html(_part1 + '<span class="discipline_name">' + _part2 + '</span>' + _part3);
                    
                } else {//正常选择
                    _tip_div.addClass("discipline_yes");
                    var _part1 = $.discipline["_helper"](defaults["locale"], "label.disciplineChoosen");
                    var _part2 = defaults["locale"] == "zh_CN" ? '“name”' : '"name"';
                    _part2 = _part2.replace("name", _val == null ? "" : _val["name"]);
					_part2 = _part2.length>60?_part2.substring(0,60)+"..."+_part2.substr(-1,1):_part2;
                    _tip_div.html(_part1 + '<span class="discipline_name">' + _part2 + '</span>');
                }
                
            },
            "_is_dup": function(){//判断学科代码是否重复
                var _cur_disc_code = this["_cur_disc_code"];
                var isDup = false;
                _this.each(function(index){//判断学科代码下拉框的取值情况
                    var select = $(this);
                    if (result["obj"] != null && select.attr("seq") != result["obj"].attr("seq") && select.attr("disc_code") == _cur_disc_code) {
                        isDup = true;
                        return false;
                    }
                });
                return isDup;
            },
            "vals": function(){//获取vals，返回选择学科代码各级别的值
                var valArr = [];
                var discSelectArr = $("#discipline_code_1,#discipline_code_2,#discipline_code_3,#discipline_code_4").find(".discipline_body_li_select");
                discSelectArr.each(function(){
                    var liId = $(this).attr("id");
                    var val = {};
                    if (/^id_(.+)_disc_code_(.{1,7})$/gi.test(liId)) {
                        val["id"] = RegExp.$1;
                        val["disc_code"] = RegExp.$2;
                        val["name"] = $(this).find("a").html().replace(/^(.{1,7}-)(.*)$/gi, "$2");//仅保留学科代码名称
                        val["_this"] = $(this);
                        valArr.push(val);
                    }
                });
                return valArr;
            },
            "val": function(){//获取val，返回最后选择的学科代码
                var _vals = this.vals();
                var _last_val = null;
                $.each(_vals, function(){
                    if (_last_val == null || this["disc_code"].length > _last_val["disc_code"].length) {
                        _last_val = this;
                    }
                });
                return _last_val;
            },
            "_set": function(){//通过disc_code自动选择学科
                if (this["obj"] != null) {
                    var _exist_disc_code = this["obj"].attr("disc_code");
                    if (_exist_disc_code == null || _exist_disc_code == "") {
                        this["_cur_disc_code"] = null;
                        this["_ajax"]();
                    } else {
                        this["_cur_disc_code"] = _exist_disc_code;
                        this["_ajax"](_exist_disc_code.length < 7 ? _exist_disc_code : _exist_disc_code.substring(0, 5));
                    }
                }
            },
            "_remove": function(discCode){//删除当前级别和子级别的学科代码
                var isRemove = false;
                var _vals = this.vals();
                if (discCode == null && discCode.length == 0) 
                    return isRemove;
                
                $.each(_vals, function(){
                    if ((this["disc_code"].length > discCode.length && this["disc_code"].indexOf(discCode) == 0) || this["disc_code"] == discCode) {
                        isRemove = true;
                        this["_this"].removeClass("discipline_body_li_select");
                    }
                });
                if (isRemove) {
                    if (discCode.length == 1) {
                        this["_cur_disc_code"] = null;
                    } else {
                        this["_cur_disc_code"] = discCode.substring(0, discCode.length - 2);
                    }
                    
                    this["_disc_check"]();
                }
                return isRemove;
                
            }
        };
        
        
        $(".discipline_body ol li").delegate("click", function(e){//选择学科代码
            var liId = $(this).attr("id");
            if (/^.+disc_code_(.{1,7})$/gi.test(liId)) {
                var discCode = RegExp.$1;
                if (result["_remove"](discCode)) {
                    return;
                }
                result["_cur_disc_code"] = discCode;
                if (discCode.length < 7) {
                    result["_ajax"](discCode);
                } else {
                    result["_select_css"]($("#discipline_code_4"));
                }
                
            }
            return false;//防止事件冒泡
        });
        
        _this.each(function(index){//外部下拉框点击功能
            $(this).click(function(){
                result["obj"] = $(this);
                result["obj"].blur();//下拉框点击后，马上失去焦点
                result["obj"].hide();
                $("#" + _input).click();
                result["_set"]();
                result["obj"].show();
            }).attr("seq", index);//序列，用于排除与自己比较
        });
        
        $("body").append($.discipline["_create_html"](defaults["locale"], defaults["id"]));//学科领域弹出框内容布局
        $("body").append($.discipline["_create_think_box_html"](defaults["locale"], _input, defaults["id"]));//弹出框属性
        $("#" + _input).thickbox({
            resmod: defaults["resmod"]
        });//初始化弹出框
        result["_ajax"]();//初始化1级学科
        $("#" + defaults["id"] + "_save").click(function(){//保存学科代码
            var _tip = $(".discipline_tips_text div");
            if (_tip.hasClass("discipline_no")) 
                return;
            var _select = result["obj"];
            if (_select != null) {
                var _val = result["val"]();
                var _option = _select.find("option");
                _select.attr("disc_code", _val == null ? "" : _val["disc_code"]);
                _option.text(_val == null ? "" : _val["name"]);
                _option.val(_val == null ? "" : _val["id"]);
                var _is_close = true;
                if (defaults["bind"] != null) {
                    _is_close = defaults["bind"](result, "save", function(){
                        $.Thickbox.closeWin();
                    }, _select.attr("id"));
                }
                if (typeof _is_close == "undefined" || _is_close) {//默认关闭窗口
                    $.Thickbox.closeWin();
                }
            }
        });
        
        $("#" + defaults["id"] + "_cancel").click(function(){//关闭学科代码窗口
            $.Thickbox.closeWin();
            if (defaults["bind"] != null) {
                defaults["bind"](result, "cancel");
            }
        });
        return result;
    };
    
    $.smate.discipline = {
        "_create_html": function(locale, id){//学科代码弹出框布局
            var arr = [];
            arr.push('<div id="' + id + '" style="display:none;">');
            arr.push('<div><div style="height:368;overflow:hidden;"><div class="discipline_list_div">');
            arr.push('<div class="discipline_box">');
            arr.push('<div class="discipline_header">');
            arr.push('<div class="header_blank"><span class="layer_a">' + $.discipline["_helper"](locale, "label.disciplineFirst") + '</span></div>');
            arr.push('<div class="header_blank"><span class="layer_b">' + $.discipline["_helper"](locale, "label.disciplineSecond") + '</span></div>');
            arr.push('<div class="header_blank"><span class="layer_c">' + $.discipline["_helper"](locale, "label.disciplineThird") + '</span></div>');
            arr.push('<div class="header_blank end_blank"><span class="layer_d">' + $.discipline["_helper"](locale, "label.disciplineFourth") + '</span></div>');
            arr.push('</div>');
            arr.push('<div class="discipline_body">');
            arr.push('<div class="disc_blank"><div id="discipline_code_1" class="disc_a layer_a"></div></div>');
            arr.push('<div class="disc_blank"><div id="discipline_code_2" class="disc_b layer_b"></div></div>');
            arr.push('<div class="disc_blank"><div id="discipline_code_3" class="disc_c layer_c"></div></div>');
            arr.push('<div class="disc_blank end_blank"><div id="discipline_code_4" class="disc_d layer_d"></div></div>');
            arr.push('</div>');
            arr.push('<div class="discipline_footer">');
            arr.push('<div class="tips_blank">');
            arr.push('<div class="discipline_tips_text"><div>');
            arr.push('</div></div></div></div>');
            arr.push('</div>');
            arr.push('</div>');
            arr.push('<div class="pop_buttom">');
            arr.push('<input class="discipline_choosen" type="hidden" value="" name=""/>');
            arr.push('<input type="button" name="submit" id="' + id + '_save" value="' + $.discipline["_helper"](locale, "label.submit") + '" class="uiButton uiButtonConfirm text14"/>　');
            arr.push('<input type="button" name="cancel" id="' + id + '_cancel" value="' + $.discipline["_helper"](locale, "label.cancel") + '" class="uiButton text14 mright10"/>');
            arr.push('</div></div></div></div>');
            
            
            return arr.join("");
        },
        "_layer_html": function(discArr, locale){//学科代码html
        	var arr = [];
            if (typeof discArr == "undefined") {
            	// 修改在"IE10"下出现线条缺失的情况
            	arr.push('<div></div>');
            } else {
                arr.push("<ol>");
                $.each(discArr, function(){
                    var id = this["id"];
                    var name = this[locale + "_name"];
                    var disc_code = this["disc_code"];
                    var textId = "id_" + id + "_disc_code_" + disc_code;
                    arr.push('<li id="' + textId + '"><a href="javascript:void(0)" hidefocus>' + this["disc_code"] + "-" + name + '</a></li>');
                });
                arr.push("</ol>");
            }
            return arr.join(""); //将数组转换成字符串
        },
        "_create_think_box_html": function(locale, _input, id){//thinkbox属性标签
            return '<input id="' + _input + '" class="thickbox" style="display:none;" alt="#TB_inline?height=369&amp;width=725&amp;inlineId=' + id + '" type="button" title="' + $.discipline["_helper"](locale, "label.choseDiscipline") + '"/>';
        },
        "_helper": function(locale, key){//国际化辅助
            return this._locale[locale + "." + key];
        },
        "_locale": {//国际化
            "zh_CN.label.addDiscipline": "添加学科代码",
            "zh_CN.label.cancel": "取消",
            "zh_CN.label.cannotSubmit": "过于简单，不能提交！请继续选择下一级学科",
            "zh_CN.label.choseDiscipline": "选择学科代码",
            "zh_CN.label.currentChoose": "当前值",
            "zh_CN.label.defaultSelectValue": "选择学科",
            "zh_CN.label.disciplineChoosen": "你已经选中了",
            "zh_CN.label.disciplineChoosenTips": "请从上面的选择框中由左到右选择你的学科",
            "zh_CN.label.disciplineChoosenTooSimple": "，但是过于简单，请继续选择下一级学科",
            "zh_CN.label.disciplineFirst": "一级学科",
            "zh_CN.label.disciplineFourth": "四级学科",
            "zh_CN.label.disciplineSecond": "二级学科",
            "zh_CN.label.disciplineThird": "三级学科",
            "zh_CN.label.en.keywords": "英文关键字",
            "zh_CN.label.multiple": "你已经选择, 不能选取重复值, 请改选其他学科",
            "zh_CN.label.reset": "删除",
            "zh_CN.label.resetComfirm": "确定删除?",
            "zh_CN.label.resetComfirm.title": "提示",
            "zh_CN.label.sorry": "对不起，你选中的学科",
            "zh_CN.label.submit": "确认",
            "zh_CN.label.zh.keywords": "中文关键字",
            "zh_CN.label.cannontAddMore": "最多只能添加到5个",
            "zh_CN.label.code": "学科代码",
            "zh_CN.label.select": "选择学科",
            "zh_CN.label.tip": "提示",
            "zh_CN.label.loginTimeout": "系统超时或未登录，你要登录吗？",
            "en_US.label.addDiscipline": "Add",
            "en_US.label.cancel": "Cancel",
            "en_US.label.cannotSubmit": "is too simple . Submit forbidden",
            "en_US.label.choseDiscipline": "Discipline Area",
            "en_US.label.currentChoose": "Multiple choosen found ",
            "en_US.label.defaultSelectValue": "Select a discipline",
            "en_US.label.disciplineChoosen": "You have selected",
            "en_US.label.disciplineChoosenTips": "Please select the discipline area from the list above",
            "en_US.label.disciplineChoosenTooSimple": ", it is too simple",
            "en_US.label.disciplineFirst": "Level 1",
            "en_US.label.disciplineFourth": "Level 4",
            "en_US.label.disciplineSecond": "Level 2",
            "en_US.label.disciplineThird": "Level 3",
            "en_US.label.en.keywords": "English Keyword",
            "en_US.label.multiple": "You can not select multiple value ",
            "en_US.label.reset": "Delete",
            "en_US.label.resetComfirm": "Are you sure about that? ",
            "en_US.label.resetComfirm.title": "Tips",
            "en_US.label.sorry": "Sorry ",
            "en_US.label.submit": "Confirm",
            "en_US.label.zh.keywords": "Chinese Keyword ",
            "en_US.label.cannontAddMore": "You can have five disciplines at most",
            "en_US.label.code": "Discipline code",
            "en_US.label.select": "Select a discipline",
            "en_US.label.tip": "Info",
            "en_US.label.loginTimeout": "You are not logged in or session time out, please log in again"
        }
    };
    
})(jQuery);
