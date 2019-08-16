/**
 * @author zym
 * 输入自动下拉框提示插件
 */
(function($){
    ////自动单词与常用自动下拉框集成封装 start
    //供自动单词使用
	// $.smate.autoword.config 供自动单词完成插件(smate.autoword.js)使用
	$.smate = $.smate ? $.smate : {};
    $.smate.auto = $.smate.auto ? $.smate.auto : {};
	
    $.smate.auto.keyword = function(input){
        return input.complete({
			"width": 200,
            "key": "key_word"
        });
    };
    $.smate.auto.friendnames = function(input){
        return input.complete({
			"width": 200,
            "key": "friend_names"
            	
        });
    };
	
	// 研究领域关键词
	/*
$.smate.auto.disc_key_word = function(input){
        return input.complete({
			"width": 200,
            "key": "disc_key_word"
        });
    };
*/
    /**
     * 专家系统，关键词提示
     */
    $.smate.auto.expertKeyword = function(input){
        return input.complete({
			"width": 200,
			"ctx":"/egtexpertweb",
            "key": "expert_keyword"
        });
    };
    
    $.smate.auto.keywordHot = function(input){
        return input.complete({
			"width": 200,
            "key": "key_word_hot"
        });
    };
	
    $.smate.auto.insName = function(input){
        return input.complete({
            "key": "ins_name",
            "width": 200
        });
    };
	
	//rol资助机构
	$.smate.auto.findAgencys = function(input){
        return input.complete({
            "key": "find_Agencys",
            "width": 200,
			"ctx": "/scmwebrol",
			"bind":{"callback":typeof $.smate.auto.agency_callback !="undefined"?$.smate.auto.agency_callback:function(data){}}
        });
    };
	
  //rol单位人员提示
    $.smate.auto.rolPsnName = function(input){
        return input.complete({
            "key": "ajaxGetRolPsns",
            "width": 200,
            "parse": $.rolInsPsns_parse,
			"extraParams":typeof extraParams != "undefined"?extraParams:{},
			"bind":{"callback":typeof $.smate.auto.rolpsn_callback !="undefined"?$.smate.auto.rolpsn_callback:function(data){}}
        });
    };
	//联系人名字提示
    $.smate.auto.friendName = function(input){
        return input.complete({
            "key": "ajaxGetFriends",
            "width": 200,
            "parse": $.friend_name_parse,
            "formatItem": $.friend_name_formatItem,
			"extraParams":typeof extraParams != "undefined"?extraParams:{},
			"bind":{"callback":typeof $.smate.auto.friend_callback !="undefined"?$.smate.auto.friend_callback:function(data){}}
        });
    };
    //群组成员名字提示
    $.smate.auto.groupMemberName = function(input){
    	return input.complete({
    		"key":"ajaxGetGroupMember",
    		"width":200,
    		"parse":$.groupMember_name_parse,
    		"formatItem":$.groupMember_name_formateItem,
    		"extraParams":typeof extraParams != "undefined"?extraParams:{}
    	});
    };
    
    //报表单位提示
    $.smate.auto.rolRptIns = function(input){
    	return input.complete({
    		"key":"rolRptIns",
    		"width":200
    	});
    };
    $.smate.auto.rolPosition = function(input){
    	return input.complete({
    		"key":"rolPosition",
    		"width":200
    	});
    };
	//课程
    $.smate.auto.taught = function(input){
        return input.complete({
			"width": 200,
            "key": "taught"
        });
    };
	
	//研究领域
    $.smate.auto.discInput = function(input){
        return input.complete({
            "key": "disc_input",
            "width": 200,
    		"extraParams":typeof extraParams != "undefined"?extraParams:{}
        });
    };
    
    //基金关键词
    $.smate.auto.rolFundKeywords = function(input){
        return input.complete({
            "key": "fund_keywords",
            "width": 200,
            "ctx": "/scmwebrol",
				"extraParams":typeof extraParams != "undefined"?extraParams:{}
        });
    };
    //基金关键词
    $.smate.auto.bpoFundKeywords = function(input){
        return input.complete({
            "key": "fund_keywords",
            "width": 200,
            "ctx": "/scmwebbpo",
				"extraParams":typeof extraParams != "undefined"?extraParams:{}
        });
    };
	
    ////自动单词与常用自动下拉框集成封装 end
    
    $.fn.complete = function(options){
        var defaults = {
            "key": null,
            "bind": null,
            "ctx": "/scmwebsns",
            "width": null
        };
        
        if (typeof options != "undefined") {
            $.extend(defaults, options);
            if (typeof ctx != "undefined") {
                defaults["ctx"] = ctx;
            }
        }
        
        ////常用自动下拉框封装 start
        var urlMap = {
        	"friend_names": "/psnweb/friend/ajaxautofriendnames",//联系人名字
            "key_word": defaults["ctx"] + "/ac/personalKeywords.action",//关键词
            "expert_keyword": defaults["ctx"] + "/const/ajaxKeyword",//专家系统关键词提示
            //"disc_key_word": defaults["ctx"] + "/psnView/ajaxsmate.autoCompleteKw",//研究领域关键词
            "key_word_hot": defaults["ctx"] + "/ac/keywordsHotAc.action",//热词库
            "ins_name": defaults["ctx"] + "/ac/institution.action",//单位名称
            "fund_category": defaults["ctx"] + "/ac/fundcategory.action", // 基金类别
            "enKeyword": defaults["ctx"] + "/ac/discKeywordsEnAc.action", // 英文关键词
            "queryKeyword": defaults["ctx"] + "/ac/disckeywordAc.action", // 关键词查询
            "keywordCmd": defaults["ctx"] + "/keywordscmd/ajaxsmate.autoKwCmd", // 领域推荐
            "egrantkeywordCmd": defaults["ctx"] + "/egrant/keywordscmd/ajaxsmate.autoKwCmd", //egrant领域推荐
            "position": defaults["ctx"] + "/ac/position.action", // 职称
            "ajaxGetFriends": defaults["ctx"] + "/publication/ajaxGetFriendsBysmate.auto",//我的联系人
            "queryJournal": defaults["ctx"] + "/ac/journalInfAc.action", // 期刊检索
            "ajaxGetGroupMember":defaults["ctx"] + "/ac/ajaxGetGroupMembers",//群组成员
            "ajaxGetRolPsns": defaults["ctx"] + "/publication/ajaxGetInsPersonsmate.auto",//rol单位人员
            "rolRptIns":defaults["ctx"] + "/ac/stainstitution.action",
            "rolPosition":defaults["ctx"] + "/ac/position.action",
            "taught":defaults["ctx"] + "/ac/taught.action",
            "disc_input":defaults["ctx"] + "/ac/discInput.action",
            "fund_keywords":defaults["ctx"] + "/ac/fundkeywords.action",//基金关键词
            "insUnit":defaults["ctx"] + "/ac/insUnit.action",//单位院系名称
            "find_Agencys":defaults["ctx"] + "/ac/getAgencys.action",//资助机构
            "degree": [{
                "name": "Ph.D",
                "code": ""
            }, {
                "name": "博士",
                "code": ""
            }, {
                "name": "Master",
                "code": ""
            }, {
                "name": "硕士",
                "code": ""
            }, {
                "name": "Bachelor",
                "code": ""
            }, {
                "name": "学士",
                "code": ""
            }, {
                "name": "Polytechnic",
                "code": ""
            }, {
                "name": "专科",
                "code": ""
            }, {
                "name": "Secondary technical school",
                "code": ""
            }, {
                "name": "中专",
                "code": ""
            }, {
                "name": "High School or below",
                "code": ""
            }, {
                "name": "高中及以下",
                "code": ""
            }]//学位下拉框
        };
        //默认宽度，这里补充各种下拉框默认宽度
        var widthMap = {            //"ins_name":100
        };
        ////常用自动下拉框封装 end
        
        //根据自动完成类型，动态设置默认值
        var _setting_default = function(_this){//
            var keyVal = defaults["key"];
            if (typeof urlMap[keyVal] != "undefined") {
                defaults["key"] = urlMap[keyVal];
            }
            
            if (defaults["width"] == null) {
                defaults["width"] = typeof widthMap[keyVal] != "undefined" ? widthMap[keyVal] : _this.width();
            }
        };
        
        var objAuto = {
            "length": this.length
        };
        
        this.each(function(index){
            var _this = $(this);
            _setting_default(_this);//根据自动完成类型，动态设置默认值
            //返回结果和操作方法
            var result = {
                "obj": _this,//对象：自动完成输入框
                "result": function(){//返回结果
                    return {
                        "name": _this.val(),
                        "code": _this.attr("code")
                    };
                },
                "extend": function(efun){//集成扩展其它功能，在选中选项后，执行传入的外部回调函数
                    var reset = function(efun){
                        defaults["result"] = function(event, data, formatted){
                            efun(data);
                        };
                    }(efun);
                }
            };
            objAuto[_this.attr("id")] = result;
            var lastResult = null;
            _this.autocomplete(defaults["key"], {
                width: defaults["width"],
                highlight: function(value, term){
                    return value;
                },
                max: 10,
                dataType: 'JSON',
                scroll: false,
                scrollHeight: 300,
                cacheLength: 0,
                extraParams: defaults["extraParams"] != null ? defaults["extraParams"] : {
                    'excludeParam': function(){
                        if (lastResult != _this.val()) {
                            _this.attr("code", "");
                            if (defaults["bind"] != null) {
                                $.each(defaults["bind"], function(index){
                                    $("#" + this).val("");
                                });
                            }
                        }
                        return '';
                    }
                },
                formatItem: function(data, i, n, value, term){
                    if (defaults["formatItem"] != null) {
                        return defaults["formatItem"](data, i, n, value, term);
                    } else {
                        return data.name;
                    }
                    
                },
                parse: function(data){//转化数据
                    if(typeof data ==="undefined" || data==null){
						   	return;
						   }
                    if (defaults["parse"] != null) {
                        return defaults["parse"](data);
                    } else {
                        var parsed = [];
                        for (var i = 0; i < data.length; i++) {
                            var item = data[i];
                            parsed.push({
                                "data": item,
                                "value": item.code.toString(),
                                "result": item.name
                            });
                        }
                        return parsed;
                    }
                }
            }).result(function(event, data, formatted){//返回result
                if (defaults["result"] != null) {//集成外部插件，用于处理数据
                    defaults["result"](event, data, formatted);
                } else {
                    if (data && data.code) {
                        lastResult = data.name;
                        _this.attr("code", data.code);
                    }
                }
                
                if (defaults["bind"] != null) {//绑定文本框、hidden标签或回调函数
                    $.each(defaults["bind"], function(index){
                        if ($.isFunction(this)) {
                            this(data);
                        } else {
                            $("#" + this).val(data[index]);
                        }
                    });
                }
                
            });
            
            _this.keydown(function(event){
                if (event.keyCode == 13) {//防止事件冒泡
                    event.preventDefault();
                    return false;
                }
            });
        });
        
        return objAuto.length == 1 ? objAuto[this.attr("id")] : objAuto;
    }
})(jQuery);

(function($){
		$.smate = $.smate ? $.smate : {};
    $.fn.extend({
           autocomplete: function(urlOrData, options){
            var isUrl = typeof urlOrData == "string";
            options = $.extend({}, $.smate.autocompleter.defaults, {
                url: isUrl ? urlOrData : null,
                data: isUrl ? null : urlOrData,
                delay: isUrl ? $.smate.autocompleter.defaults.delay : 10,
                max: options && !options.scroll ? 10 : 150
            }, options);
            
            // if highlight is set to false, replace it with a do-nothing function
            options.highlight = options.highlight ||
            function(value){
                return value;
            };
            
            // if the formatMatch option is not specified, then use formatItem for backwards compatibility
            options.formatMatch = options.formatMatch || options.formatItem;
            
            return this.each(function(){
                new $.smate.autocompleter(this, options);
            });
        },
        result: function(handler){
            return this.bind("result", handler);
        },
        search: function(handler){
            return this.trigger("search", [handler]);
        },
        flushCache: function(){
            return this.trigger("flushCache");
        },
        setOptions: function(options){
            return this.trigger("setOptions", [options]);
        },
        unAutocomplete: function(){
            return this.trigger("unAutocomplete");
        }
    });
    
    $.smate.autocompleter = function(input, options){
    
        var KEY = {
            UP: 38,
            DOWN: 40,
            DEL: 46,
            TAB: 9,
            RETURN: 13,
            ESC: 27,
            COMMA: 188,
            PAGEUP: 33,
            PAGEDOWN: 34,
            BACKSPACE: 8
        };
        
        // Create $ object for input element
        var $input = $(input).attr("smate.autocomplete", "off").addClass(options.inputClass);
        
        var timeout;
        var previousValue = "";
        var cache = $.smate.autocompleter.Cache(options);
        var hasFocus = 0;
        var lastKeyPressCode;
        var config = {
            mouseDownOnSelect: false
        };
        var select = $.smate.autocompleter.Select(options, input, selectCurrent, config);
        
        var blockSubmit;
        
        // prevent form submit in opera when selecting with return key
        $.browser.opera &&
        $(input.form).bind("submit.smate.autocomplete", function(){
            if (blockSubmit) {
                blockSubmit = false;
                return false;
            }
        });
        
        // only opera doesn't trigger keydown multiple times while pressed, others don't work with keypress at all
        $input.bind(($.browser.opera ? "keypress" : "keyup") + ".smate.autocomplete", function(event){
            // track last key pressed
            lastKeyPressCode = event.keyCode;
            switch (event.keyCode) {
            
                case KEY.UP:
                    event.preventDefault();
                    if (select.visible()) {
                        select.prev();
                    } else {
                        onChange(0, true);
                    }
                    break;
                    
                case KEY.DOWN:
                    event.preventDefault();
                    if (select.visible()) {
                        select.next();
                    } else {
                        onChange(0, true);
                    }
                    break;
                    
                case KEY.PAGEUP:
                    event.preventDefault();
                    if (select.visible()) {
                        select.pageUp();
                    } else {
                        onChange(0, true);
                    }
                    break;
                    
                case KEY.PAGEDOWN:
                    event.preventDefault();
                    if (select.visible()) {
                        select.pageDown();
                    } else {
                        onChange(0, true);
                    }
                    break;
                    
                // matches also semicolon
                case options.multiple && $.trim(options.multipleSeparator) == "," && KEY.COMMA:
                case KEY.TAB:
                case KEY.RETURN:
                    if (selectCurrent(event.keyCode)) {
                        // stop default to prevent a form submit, Opera needs special handling
                        event.preventDefault();
                        blockSubmit = true;
                        return false;
                    }
                    break;
                    
                case KEY.ESC:
                    select.hide();
                    break;
                    
                default:
                    clearTimeout(timeout);
                    timeout = setTimeout(onChange, options.delay);
                    break;
            }
        }).focus(function(){
            // track whether the field has focus, we shouldn't process any
            // results if the field no longer has focus
            hasFocus++;
        }).blur(function(){
            hasFocus = 0;
            if (!config.mouseDownOnSelect) {
                hideResults();
            }
        }).click(function(){
        	//参数判断是否是机构注册页面,如果是在注册页面，就点击不会弹出部门提示
        	if(typeof(options.onClickMark)=="undefined" || options.onClickMark!='true' ){
            // show select when clicking in a focused field  edit zhang tian
            if (hasFocus++ > 1 && !select.visible() && !$input.attr("isselect")) {
                onChange(0, true);
            } else if ($input.attr("isselect")) {
                options.minChars = 0;
                clearTimeout(timeout);
                timeout = setTimeout(onChange, options.delay);
            }
        	}
            
        }).bind("search", function(){
            // TODO why not just specifying both arguments?
            var fn = (arguments.length > 1) ? arguments[1] : null;
            function findValueCallback(q, data){
                var result;
                if (data && data.length) {
                    for (var i = 0; i < data.length; i++) {
                        if (data[i].result.toLowerCase() == q.toLowerCase()) {
                            result = data[i];
                            break;
                        }
                    }
                }
                if (typeof fn == "function") 
                    fn(result);
                else 
                    $input.trigger("result", result && [result.data, result.value]);
            }
            $.each(trimWords($input.val()), function(i, value){
                request(value, findValueCallback, findValueCallback);
            });
        }).bind("flushCache", function(){
            cache.flush();
        }).bind("setOptions", function(){
            $.extend(options, arguments[1]);
            // if we've updated the data, repopulate
            if ("data" in arguments[1]) 
                cache.populate();
        }).bind("unsmate.autocomplete", function(){
            select.unbind();
            $input.unbind();
            $(input.form).unbind(".smate.autocomplete");
        });
        
        
        function selectCurrent(keyCode){
            var selected = select.selected();
            if (!selected) 
                return false;
            
            var v = selected.result;
            previousValue = v;
            
            if (options.multiple) {
                var words = trimWords($input.val());
                if (words.length > 1) {
                    v = words.slice(0, words.length - 1).join(options.multipleSeparator) + options.multipleSeparator + v;
                }
                v += options.multipleSeparator;
            }
            
            $input.val(v);
            hideResultsNow();
            var label = selected.data.name;
            $input.trigger("result", [selected.data, selected.value]);
            //这里扑捉异常，label中有括号时，会报脚本错误。
            try {
                if ($(label).attr("class") == "triggerEvent" && keyCode == KEY.RETURN) {
                    $(label).trigger("click");
                }
            } 
            catch (e) {
            }
            return true;
        }
        
        function onChange(crap, skipPrevCheck){
            if (lastKeyPressCode == KEY.DEL) {
                select.hide();
                return;
            }
            if ($input.attr("isselect"))//判断输入框是否允许select下拉框方式,需要在input元素上面增加一个isselect属性.
            {
                var currentValue = $input.val();
                if (!skipPrevCheck && currentValue == previousValue && !$input.attr("isselect")) 
                    return;
                previousValue = currentValue;
                currentValue = lastWord(currentValue);
                //输入为空格时，不会触发弹出部门
                if(trimWords(currentValue).length>0){
                if ((currentValue.length >= options.minChars) || (options.minChars == 0)) {
                    $input.addClass(options.loadingClass);
                    if (!options.matchCase) 
                        currentValue = currentValue.toLowerCase();
                    request(currentValue, receiveData, hideResultsNow);
                } else {
                    stopLoading();
                    select.hide();
                }
                }
            } else {
            
                setTimeout(function(){
                    var currentValue = $input.val();
                    if (!skipPrevCheck && currentValue == previousValue) 
                        return;
                    
                    previousValue = currentValue;
                    
                    currentValue = lastWord(currentValue);
                    if (currentValue.length >= options.minChars) {
                        $input.addClass(options.loadingClass);
                        if (!options.matchCase) 
                            currentValue = currentValue.toLowerCase();
                        request(currentValue, receiveData, hideResultsNow);
                    } else {
                        stopLoading();
                        select.hide();
                    }
                }, 50);
            }
        };
        
        function trimWords(value){
            if (!value) {
                return [""];
            }
            var words = value.split(options.multipleSeparator);
            var result = [];
            $.each(words, function(i, value){
                if ($.trim(value)) 
                    result[i] = $.trim(value);
            });
            return result;
        }
        
        function lastWord(value){
            if (!options.multiple) 
                return value;
            var words = trimWords(value);
            return words[words.length - 1];
        }
        
        // fills in the input box w/the first match (assumed to be the best match)
        // q: the term entered
        // sValue: the first matching result
        function autoFill(q, sValue){
            // autofill in the complete box w/the first match as long as the user hasn't entered in more data
            // if the last user key pressed was backspace, don't autofill
            if (options.autoFill && (lastWord($input.val()).toLowerCase() == q.toLowerCase()) && lastKeyPressCode != KEY.BACKSPACE) {
                // fill in the value (keep the case the user has typed)
                $input.val($input.val() + sValue.substring(lastWord(previousValue).length));
                // select the portion of the value not typed by the user (so the next character will erase)
                $.smate.autocompleter.Selection(input, previousValue.length, previousValue.length + sValue.length);
            }
        };
        
        function hideResults(){
            clearTimeout(timeout);
            timeout = setTimeout(hideResultsNow, 200);
        };
        
        function hideResultsNow(){
            var wasVisible = select.visible();
            select.hide();
            clearTimeout(timeout);
            stopLoading();
            if (options.mustMatch) {
                // call search and run callback
                $input.search(function(result){
                    // if no value found, clear the input box
                    if (!result) {
                        if (options.multiple) {
                            var words = trimWords($input.val()).slice(0, -1);
                            $input.val(words.join(options.multipleSeparator) + (words.length ? options.multipleSeparator : ""));
                        } else 
                            $input.val("");
                    }
                });
            }
            if (wasVisible) 
                // position cursor at end of input field
                $.smate.autocompleter.Selection(input, input.value.length, input.value.length);
        };
        
        function receiveData(q, data){
            if (data && data.length && hasFocus) {
                stopLoading();
                select.display(data, q);
                autoFill(q, data[0].value);
                select.show();
            } else {
                hideResultsNow();
            }
        };
        
        function request(term, success, failure){
            if (!options.matchCase) 
                term = term.toLowerCase();
            var data = cache.load(term);
        		if(options.url=='/scmwebsns/ac/countryRegion.action'){
        			//如果是获取国家或地区的自动提示直接过去js的内容 tsz_2014.11.28_SCM-6076
            	data=[];
            	if(term==""){
            		 if(local="zh_CN"){
            		     for(var i=0;i<10;i++){
                         	var temp=constRegionZh[i];
                         	data.push({'data':{'name':temp},'value':temp,'result':temp});
                         		}
            		 }else{
            			for(var i=0;i<10;i++){
                     	var temp=constRegionEn[i];
                     	data.push({'data':{'name':temp},'value':temp,'result':temp});
                     		}
            		     }
            	}else{
	            	var length=constRegionEn.length;
	            	for(var i=0;i<length;i++){
	            		var temp=constRegionEn[i];
	            		if(temp.toLowerCase().indexOf(term.toLowerCase())>-1){
	            			data.push({'data':{'name':temp},'value':temp,'result':temp});
	            			}
	            		}
	            	if(data.length==0){
	            		var length=constRegionZh.length;
	                	for(var i=0;i<length;i++){
	                		var temp=constRegionZh[i];
	                		if(temp.toLowerCase().indexOf(term.toLowerCase())>-1){
	                				data.push({'data':{'name':temp},'value':temp,'result':temp});
	                			}
	                		}
	            		}
            		}
            	//data=[{'data':{'name':'iris'},'value':'tsz','result':'tanshaozhi'},{'data':{'name':'iris'},'value':'tsz','result':'tanshaozhi'}];
    				success(term,data);
        			
        		}else{
            // recieve the cached data
            if (data && data.length) {
                success(term, data);
                // if an AJAX url has been supplied, try loading the data now
            } else if ((typeof options.url == "string") && (options.url.length > 0)) {
            
                var extraParams = {
                    timestamp: +new Date()
                };
                $.each(options.extraParams, function(key, param){
                    extraParams[key] = typeof param == "function" ? param() : param;
                });
                
                $.ajax({
                    // try to leverage ajaxQueue plugin to abort previous requests
                    mode: "abort",
                    // limit abortion to this input
                    port: "smate.autocomplete" + input.name,
                    dataType: options.dataType,
                    url: options.url,
                    data: $.extend({
                        q: lastWord(term),
                        limit: options.max
                    }, extraParams),
                    success: function(data){
                        var parsed = options.parse && options.parse(data) || parse(data);
                        cache.add(term, parsed);
                        success(term, parsed);
                    }
                });
            } else {
                // if we have a failure, we need to empty the list -- this prevents the the [TAB] key from selecting the last successful match
                select.emptyList();
                failure(term);
            }
        	}
        };
        
        function parse(data){
			   if(typeof data ==="undefined" || data==null){
					return;
			    }
            var parsed = [];
            var rows = data.split("\n");
            for (var i = 0; i < rows.length; i++) {
                var row = $.trim(rows[i]);
                if (row) {
                    row = row.split("|");
                    parsed[parsed.length] = {
                        data: row,
                        value: row[0],
                        result: options.formatResult && options.formatResult(row, row[0]) || row[0]
                    };
                }
            }
            return parsed;
        };
        
        function stopLoading(){
            $input.removeClass(options.loadingClass);
        };
        
            };
    
    $.smate.autocompleter.defaults = {
        inputClass: "ac_input",
        resultsClass: "ac_results",
        loadingClass: "ac_loading",
        gbResultClass: "ac_results_bg",
        minChars: 1,
        delay: 400,
        matchCase: false,
        matchSubset: true,
        matchContains: false,
        cacheLength: 10,
        max: 100,
        mustMatch: false,
        extraParams: {},
        selectFirst: true,
        formatItem: function(row){
            return row[0];
        },
        showCallback: function(){
        	//成果检索页面专用
        	var objdiv = $("#showmeins_div");
        	if(objdiv && objdiv.is(":visible")){
        		objdiv.css("display","none");
        	};
			//rol资助机构名称专用
			var agencyDiv = $("#agency_div");
        	if(agencyDiv && agencyDiv.is(":visible")){
        		agencyDiv.css("display","none");
        	}
			if(typeof rol_find_agency_flag != "undefined"){
				rol_find_agency_flag=false;
			}
        },
        hideCallback: function(){
			if(typeof rol_find_agency_flag != "undefined"){
				rol_find_agency_flag=true;
			}
        },
        formatMatch: null,
        autoFill: false,
        width: 0,
        multiple: false,
        multipleSeparator: ", ",
        
        highlight: function(value, term){
            return value.replace(new RegExp("(?![^&;]+;)(?!<[^<>]*)(" + term.replace(/([\^\$\(\)\[\]\{\}\*\.\+\?\|\\])/gi, "\\$1") + ")(?![^<>]*>)(?![^&;]+;)", "gi"), "<strong>$1</strong>");
        },
        scroll: true,
        scrollHeight: 180,
        //是否会动态移动滚动条，例如调用scroll(0,1000000)
        dynScrollTop: false
    };
    
    $.smate.autocompleter.Cache = function(options){
    
        var data = {};
        var length = 0;
        
        function matchSubset(s, sub){
            if (!options.matchCase) 
                s = s.toLowerCase();
            var i = s.indexOf(sub);
            if (i == -1) 
                return false;
            return i == 0 || options.matchContains;
        };
        
        function add(q, value){
            if (length > options.cacheLength) {
                flush();
            }
            if (!data[q]) {
                length++;
            }
            data[q] = value;
        }
        
        function populate(){
            if (!options.data) 
                return false;
            // track the matches
            var stMatchSets = {}, nullData = 0;
            
            // no url was specified, we need to adjust the cache length to make sure it fits the local data store
            if (!options.url) 
                options.cacheLength = 1;
            
            // track all options for minChars = 0
            stMatchSets[""] = [];
            
            // loop through the array and create a lookup structure
            for (var i = 0, ol = options.data.length; i < ol; i++) {
                var rawValue = options.data[i];
                // if rawValue is a string, make an array otherwise just reference the array
                rawValue = (typeof rawValue == "string") ? [rawValue] : rawValue;
                
                var value = options.formatMatch(rawValue, i + 1, options.data.length);
                if (value === false) 
                    continue;
                
                var firstChar = value.charAt(0).toLowerCase();
                // if no lookup array for this character exists, look it up now
                if (!stMatchSets[firstChar]) 
                    stMatchSets[firstChar] = [];
                
                // if the match is a string
                var row = {
                    value: value,
                    data: rawValue,
                    result: options.formatResult && options.formatResult(rawValue) || value
                };
                
                // push the current match into the set list
                stMatchSets[firstChar].push(row);
                
                // keep track of minChars zero items
                if (nullData++ < options.max) {
                    stMatchSets[""].push(row);
                }
            };
            
            // add the data items to the cache
            $.each(stMatchSets, function(i, value){
                // increase the cache size
                options.cacheLength++;
                // add to the cache
                add(i, value);
            });
        }
        
        // populate any existing data
        setTimeout(populate, 25);
        
        function flush(){
            data = {};
            length = 0;
        }
        
        return {
            flush: flush,
            add: add,
            populate: populate,
            load: function(q){
                if (!options.cacheLength || !length) 
                    return null;
                /* 
                 * if dealing w/local data and matchContains than we must make sure
                 * to loop through all the data collections looking for matches
                 */
                if (!options.url && options.matchContains) {
                    // track all matches
                    var csub = [];
                    // loop through all the data grids for matches
                    for (var k in data) {
                        // don't search through the stMatchSets[""] (minChars: 0) cache
                        // this prevents duplicates
                        if (k.length > 0) {
                            var c = data[k];
                            $.each(c, function(i, x){
                                // if we've got a match, add it to the array
                                if (matchSubset(x.value, q)) {
                                    csub.push(x);
                                }
                            });
                        }
                    }
                    return csub;
                } else                // if the exact item exists, use it
                if (data[q]) {
                    return data[q];
                } else if (options.matchSubset) {
                    for (var i = q.length - 1; i >= options.minChars; i--) {
                        var c = data[q.substr(0, i)];
                        if (c) {
                            var csub = [];
                            $.each(c, function(i, x){
                                if (matchSubset(x.value, q)) {
                                    csub[csub.length] = x;
                                }
                            });
                            return csub;
                        }
                    }
                }
                return null;
            }
        };
    };
    
    $.smate.autocompleter.Select = function(options, input, select, config){
        var CLASSES = {
            ACTIVE: "ac_over"
        };
        
        var listItems, active = -1, data, term = "", needsInit = true, element, list, bgelement;
        
        // Create results
        function init(){
            if (!needsInit) 
                return;
            element = $("<div/>").hide().addClass(options.resultsClass).css("position", "absolute").appendTo(document.body);
            
            //背景IFRAME
            bgelement = $("<div><iframe frameborder='0' hspace='0' src='' style='width:100%;height:100%;'/></div>").hide().addClass(options.gbResultClass).css("position", "absolute").appendTo(document.body);
            
            list = $("<ul/>").appendTo(element).mouseover(function(event){
                if (target(event).nodeName && target(event).nodeName.toUpperCase() == 'LI') {
                    active = $("li", list).removeClass(CLASSES.ACTIVE).index(target(event));
                    $(target(event)).addClass(CLASSES.ACTIVE);
                }
            }).click(function(event){
                $(target(event)).addClass(CLASSES.ACTIVE);
                select();
                // TODO provide option to avoid setting focus again after selection? useful for cleanup-on-focus
                try {
                    input.focus();
                } 
                catch (e) {
                }
                return false;
            }).mousedown(function(){
                config.mouseDownOnSelect = true;
                if(typeof $.smate.autoword!="undefined"){//SCM-4345
                    $.smate.autoword.config = config; 
                     }// 供自动单词完成插件(jquery.smate.autoword.js)使用
            }).mouseup(function(){
                config.mouseDownOnSelect = false;
            });
            
            if (options.width > 0) 
                element.css("width", options.width);
            
            needsInit = false;
        }
        
        function target(event){
            var element = event.target;
            while (element && element.tagName != "LI") 
                element = element.parentNode;
            // more fun with IE, sometimes event.target is empty, just ignore it then
            if (!element) 
                return [];
            return element;
        }
        
        function moveSelect(step){
            listItems.slice(active, active + 1).removeClass(CLASSES.ACTIVE);
            movePosition(step);
            var activeItem = listItems.slice(active, active + 1).addClass(CLASSES.ACTIVE);
            if (options.scroll) {
                var offset = 0;
                listItems.slice(0, active).each(function(){
                    offset += this.offsetHeight;
                });
                if ((offset + activeItem[0].offsetHeight - list.scrollTop()) > list[0].clientHeight) {
                    list.scrollTop(offset + activeItem[0].offsetHeight - list.innerHeight());
                } else if (offset < list.scrollTop()) {
                    list.scrollTop(offset);
                }
            }
        };
        
        function movePosition(step){
            active += step;
            if (active < 0) {
                active = listItems.size() - 1;
            } else if (active >= listItems.size()) {
                active = 0;
            }
        }
        
        function limitNumberOfItems(available){
            return options.max && options.max < available ? options.max : available;
        }
        
        function fillList(){
            list.empty();
            var max = limitNumberOfItems(data.length);
            for (var i = 0; i < max; i++) {
                if (!data[i]) 
                    continue;
                var formatted = options.formatItem(data[i].data, i + 1, max, data[i].value, term);
                if (formatted === false) 
                    continue;
                var li = $("<li/>").html(options.highlight(formatted, term)).addClass(i % 2 == 0 ? "ac_even" : "ac_odd").appendTo(list)[0];
                $.data(li, "ac_data", data[i]);
            }
            listItems = list.find("li");
            if (options.selectFirst) {
                listItems.slice(0, 1).addClass(CLASSES.ACTIVE);
                active = 0;
            }
            // apply bgiframe if available
            if ($.fn.bgiframe) 
                list.bgiframe();
        }
        
        return {
            display: function(d, q){
                init();
                data = d;
                term = q;
                fillList();
            },
            next: function(){
                moveSelect(1);
            },
            prev: function(){
                moveSelect(-1);
            },
            pageUp: function(){
                if (active != 0 && active - 8 < 0) {
                    moveSelect(-active);
                } else {
                    moveSelect(-8);
                }
            },
            pageDown: function(){
                if (active != listItems.size() - 1 && active + 8 > listItems.size()) {
                    moveSelect(listItems.size() - 1 - active);
                } else {
                    moveSelect(8);
                }
            },
            hide: function(){
                element && element.hide();
                listItems && listItems.removeClass(CLASSES.ACTIVE);
                bgelement && bgelement.hide();
                active = -1;
                options.hideCallback();
            },
            visible: function(){
                return element && element.is(":visible");
            },
            current: function(){
                return this.visible() && (listItems.filter("." + CLASSES.ACTIVE)[0] || options.selectFirst && listItems[0]);
            },
            show: function(){
            
                options.showCallback();
                var offset = $(input).offset();
                var scrollTop = 0;
				
                     //如果绑定后会动态移动滚动条
                if (options.dynScrollTop && $.browser.msie) {
                    if (document.documentElement && document.documentElement.scrollTop) {
                        scrollTop = document.documentElement.scrollTop;
                    } else {
                        scrollTop = document.body.scrollTop;
                           }
                     }
					 
				     //自动调整弹出框位置
					 var wWidth = $(window).width();
					 var wHeight = $(window).height()
					 var wScrollTop = $(window).scrollTop();
				    var elementWidth = typeof options.width == "string" || options.width > 0 ? options.width : $(input).width();
					 if(wWidth-offset.left<elementWidth){
					 	offset.left=wWidth-elementWidth-10;
					 }
					 var elementHeight = element.height();
					 if(wHeight-offset.top+wScrollTop <elementHeight){
					 	offset.top= wHeight+wScrollTop-elementHeight-25;
					 }
				
                var offsetTop = offset.top + input.offsetHeight;
                var offsetLeft = offset.left;
                element.css({
                    width: elementWidth,
                    top: offsetTop,
                    left: offsetLeft
                }).show();
                bgelement.css({
                    width: element.width(),
                    height: element.height(),
                    top: offsetTop,
                    left: offsetLeft
                }).show();
                if (options.scroll) {
                    list.scrollTop(0);
                    list.css({
                        maxHeight: options.scrollHeight,
                        overflow: 'smate.auto'
                    });
                    
                    if ($.browser.msie && typeof document.body.style.maxHeight === "undefined") {
                        var listHeight = 0;
                        listItems.each(function(){
                            listHeight += this.offsetHeight;
                        });
                        var scrollbarsVisible = listHeight > options.scrollHeight;
                        list.css('height', scrollbarsVisible ? options.scrollHeight : listHeight);
                        if (!scrollbarsVisible) {
                            // IE doesn't recalculate width when scrollbar disappears
                            listItems.width(list.width() - parseInt(listItems.css("padding-left")) - parseInt(listItems.css("padding-right")));
                        }
                    }
                    
                }
            },
            selected: function(){
                var selected = listItems && listItems.filter("." + CLASSES.ACTIVE).removeClass(CLASSES.ACTIVE);
                return selected && selected.length && $.data(selected[0], "ac_data");
            },
            emptyList: function(){
                list && list.empty();
            },
            unbind: function(){
                bgelement && bgelement.remove();
                element && element.remove();
            }
        };
    };
    
    $.smate.autocompleter.Selection = function(field, start, end){
        if (field.createTextRange) {
            var selRange = field.createTextRange();
            selRange.collapse(true);
            selRange.moveStart("character", start);
            selRange.moveEnd("character", end);
            selRange.select();
        } else if (field.setSelectionRange) { 
			try {
	            field.setSelectionRange(start, end);
	        } 
	        catch (e) {
	        }
        } else {
            if (field.selectionStart) {
                field.selectionStart = start;
                field.selectionEnd = end;
            }
        }
        try {
            field.focus();
        } 
        catch (e) {
        }
    };
    
})(jQuery);
