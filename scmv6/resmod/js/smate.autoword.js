/**
 * @author zym
 * 自动单词完成插件
 */
(function($){
	// $.autoword.config 由输入自动下拉框提示插件(jquery.complete.js)的"mousedown"事件创建，供"blur"事件使用
	$.smate = $.smate ? $.smate : {};
	$.smate.autoword = $.smate.autoword ? $.smate.autoword : {};
	$.fn.autoword = function(options){
        var defaults = {
            "split_char": [",", "，", ";", "；", ":", "："],//单词默认完成符号
            "split_charCallback":null,//单词默认完成符号回调函数
            "words_max": 5,//设置允许单词最大数量
            "focus": true,//单词文本框默认自动获取焦点
            "repeat": false,//单词默认不允许重复
            "textrepeat":true,//单词文本不允许重复
            "blur": true,//失去焦点自动完成
            "enter": true,//回车自动完成
            "delClick":true,//点击x号时可执行的function
            "select": null,//输入自动下拉框提示插件
            "selectCallback":null,//下拉插件回调函数
            "filter": ["<", ">", "&"],//过滤特殊字符
        	"model":"multiple", //multiple:允许输入多个，single:如果存在一个则不再允许输入
        	"email": false, // 是否对输入框失去焦点时对内容是否为邮件的判断
        	"show_max":500, //显示框中文字最大长度    scm-6676每个关键词显示最多40字符
        	"onlyText" : true, //重复值判断 值判断text
        	"watermark_flag":false, //是否需要水印，默认false
        	"watermark_tip":"", //水印提示
        	"onlyPsnId":true //重复值判断 对于类似同一个人的英文名与中文名都对应同一个psnid的场景的判断    注:一般用于人名判断
        };
        if (typeof options != "undefined") {
            $.extend(defaults, options);
        }
        
        var objAuto = {
            "length": this.length
        };
        
        this.each(function(index){
            var _this = $(this);
           var mark;   	//水印相关延时标记
           var blurKeyMark;  //失去焦点、回车 延时标记
//           var clickflag = 0;
            //返回结果和操作方法
            var result = {
                "obj": _this,//对象：自动单词完成框
                "put": function(val, text){//添加word
                    if (!defaults["repeat"]) {//重复不添加
                        var isToAdd = true;
                        $.each(this.words(), function(){
									var _tmp_val = this["val"];
									var _tmp_text = this["text"];
                            if ($.trim(_tmp_text+_tmp_val) == $.trim(text+val)) {//重复
                                isToAdd = false;
                                return false;
                            		} 
                            if(defaults["onlyText"]&&defaults["textrepeat"]){
                            	if($.trim(_tmp_text).toLowerCase() == $.trim(text).toLowerCase()){
                            		isToAdd = false;
                                    return false;
                            	}
                            }
                            if(defaults["onlyPsnId"]){
                            	if(_tmp_val!=''&&($.trim(_tmp_val)== $.trim(val))){
                            		isToAdd = false;
                                    return false;
                            	}
                            }
                        });
                        if (!isToAdd) 
                            return this;
                    }
                    _this.find(".auto_word_input").parent().before(_create_word_html(val, text));//word添加到DIV
                    var label = _this.find(".auto_word_input").parent().prev().find(".auto_word");
                    var width = label.width()>defaults["show_max"]?defaults["show_max"]:(($.browser.msie||(/Trident\/7\./).test(navigator.userAgent))?label.width()+2:label.width());
                    label.width(width);
                    	if(defaults["watermark_flag"]==true){
                    		if(result.inputCount()<defaults["words_max"]){
                    			_this.find(".auto_word_watermark").text(defaults["watermark_tip"]);
                    			_this.find(".auto_word_watermark").show();
                        		}else{
                        			_this.find(".auto_word_watermark").hide();
                        		}
                    		}
                    return this;
                },"putAndCount": function(val, text,count){//添加word和统计数 zk
                    if (!defaults["repeat"]) {//重复不添加
                        var isToAdd = true;
                        $.each(this.words(), function(){
									var _tmp_val = this["val"];
									var _tmp_text = this["text"];
                            if ($.trim(_tmp_text+_tmp_val) == $.trim(text+val)) {//重复
                                isToAdd = false;
                                return false;
                            		} 
                            if(defaults["onlyText"]){
                            	if($.trim(_tmp_text).toLowerCase() == $.trim(text).toLowerCase()){
                            		isToAdd = false;
                                    return false;
                            	}
                            }
                            if(defaults["onlyPsnId"]){
                            	if(_tmp_val!=''&&($.trim(_tmp_val)== $.trim(val))){
                            		isToAdd = false;
                                    return false;
                            	}
                            }
                        });
                        if (!isToAdd) 
                            return this;
                    }
                    if(count>0){
                    	_this.find(".auto_word_input").parent().before(	_create_word_AndCount_html(val, text,count));//word添加到DIV
                    	  }else{
                    		  _this.find(".auto_word_input").parent().before(_create_word_html(val, text));//word添加到DIV
                    	  }
                    	  
                    var label = _this.find(".auto_word_input").parent().prev().find(".auto_word");
                    
                    var width = label.width()>defaults["show_max"]?defaults["show_max"]:(($.browser.msie||(/Trident\/7\./).test(navigator.userAgent))?label.width()+2:label.width());
                    label.width(width);
                    	if(defaults["watermark_flag"]==true){
                    		if(result.inputCount()<defaults["words_max"]){
                    			_this.find(".auto_word_watermark").text(defaults["watermark_tip"]);
                    			_this.find(".auto_word_watermark").show();
                        		}else{
                        			_this.find(".auto_word_watermark").hide();
                        		}
                    		}
                    return this;
                },
                "putAll": function(words){//添加多个word
                    var _result = this;
                    $.each(words, function(){
                        _result.put(this.val, this.text);
                    });
                    return this;
                },
                "remove": function(text, val){//删除单个word
                    _this.find(".auto_word_div").each(function(){
                        var word = $(this);
                        var textLabel = word.find("label");
                        var tmp_val = $.trim(word.attr("val"));
                        var tmp_text = $.trim(textLabel.html());
                        if (($.trim(text) + $.trim(val)) == (tmp_text + tmp_val)) {//匹配text+val相同
                            word.remove();
                        }
                    });
                    this._input_show();//显示输入框
                    return this;
                },
                "clear": function(isFocus){//清除所有word
                    _this.find(".auto_word_div").remove();
                    if (typeof isFocus != "undefined" && isFocus) {
                        this._input_show();//显示输入框
                    }
                    return this;
                },
                "replace": function(words){//用新的words，替换旧words
                    this.clear();
                    var add = this.add;
                    $.each(words, function(){
                        add(this.val, this.word);
                    });
                    this._input_show();//显示输入框
                    return this;
                },
                "words": function(){//获取words，返回[{'val':val1,'text':text1},{'val':val2,'text':text2}]
                    var wordArr = [];
                    _this.find(".auto_word_div").each(function(){
                        var word = $(this);
                        var textLabel = word.find("label");
                        var val = $.trim(word.attr("val"));
                        var text = $.trim(textLabel.html().replace(/&amp;/gi, "&"));
                        wordArr.push({
                            'val': val,
                            'text': text
                        });
                    });
                    return wordArr;
                },
                "vals": function(){//获取vals，返回[val1,val2]
                    var valArr = [];
                    $.each(this.words(), function(){
                        valArr.push(this.val);
                    });
                    return valArr;
                },
                "texts": function(){//获取texts，返回[text1,text2]
                    var textArr = [];
                    $.each(this.words(), function(){
                        textArr.push(this.text);
                    });
                    return textArr;
                },
				"_input_hide":function(){//限制不能输入，即不显示输入框
					 var _input = _this.find(".auto_word_input");
					 _input.hide();
				},
                "_input_show": function(isEmpty){//未超单词数量显示输入框，并获得焦点
                	
                	if(defaults["watermark_flag"]){	
                		_this.find(".auto_word_watermark").hide();
              			}
                    var _input = _this.find(".auto_word_input");
                  
                    if (typeof isEmpty == "undefined" || isEmpty) {
                        _input.val("");
                        result._input_width();
                    }
                    if (this.texts().length < defaults["words_max"]) {
                    
                        _input.parent().show();
                        if (defaults["focus"]) {
                            setTimeout(function(){
                                _input.focus();
                            }, 80);
                        }
                   
                    } else {//超出单词数量隐藏输入框
                        _input.parent().hide();
                    }
                },
             "_watermark":function(){//未超单词数量显示水印 
                		 if(result.inputCount() < defaults["words_max"]){
                			 _this.find(".auto_word_watermark").show();
                		 	}
                		 	
            	 },
             "_input_split_char":function(_inputStr){//将输入、复制的关键拆分 by zk 2013/08/13
                  var _splitArray="";
            	   var reE = /[,;，；:：]/g;
            		var repText = "";
            		repText =_inputStr.replace(reE,',');
            		if(repText.length>0)
            			_splitArray = repText.split(",");
            	   if(_splitArray.length>0){
	            		for (var i=0; i<_splitArray.length;i++){
	          			  if(result.inputCount()>=defaults["words_max"]){
	          				  break;
	          			  		}
	          				result.put("", _splitArray[i])._input_show();
	          				}
            	   		}
            	   else{
            		   result.put("", _inputStr)._input_show();
            	   		}
            	 },
               
                "_input_width": function(){
                    var _input = _this.find(".auto_word_input");
                    var text = _input.val();
                    text = text.replace(/[^\u0000-\u00ff]/g, "aa");
                    var newWidth = 8 + text.length * 8;
                    var newWidth = newWidth > _this.width() ? _this.width() : newWidth;
                    /*
var maxchar = 50;//最多50字母，34中文
                    var iCount = text.length;
                    if (iCount <= maxchar) {//自动适应中英文长度		 
                        _input.attr("maxlength", maxchar);
                    } else {
                        _input.attr("maxlength", (maxchar - (iCount - maxchar)));
                    }
*/
                    _input.css({
                        "width": newWidth + "px"
                    });
                },
				"clear_input":function(){//清空input内容
					var _input = _this.find(".auto_word_input");
					_input.val("");
				},
                "_input_filter": function(_text){//过滤特殊字符
                    if (_text != null && defaults["filter"] != null) {
                        $.each(defaults["filter"], function(index){
                            _text = _text.replace(new RegExp(this, "gm"), "");
                        });
                        return _text;
                    }
                },
                "_input_validate":function(_validate_url,_id,_text){//验证输入正确性
                	if(_id==""){//验证_text
                		_text=$.trim(_text);
                		//判断是否有重复的
                		var repeatFlag=0;
                		 $.each(this.words(), function(){
            	    		 if(this.text==_text){
            	    			repeatFlag=1;
            	    		 }
                         });
                		 if(repeatFlag==0){
                		     //判断是否是邮件
                			 var re = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;   
                			 if (re.test(_text)){//是邮件直接输入
                				 result.put("", _text)._input_show();
                			 }else{//判断正确性
                				 $.ajax({
                      	    		type:"post",
                      	    		url:_validate_url,
                      	    		data:{"autoInpuName":_text},
                      	    		dataType:"json",
                      	    		success:function(data){
                      	    			var autoId=data.autoId;
                      	    			if(autoId!="null"){
                      	    				result.put(autoId, _text)._input_show();
                      	    			}else{
                      	    				result._input_show();
                      	    					}
                      	    		},error:function(){
                      	    			result._input_show();
                      	    		}
                      	    	});
                			 }
                		
                		 }else{
                			 result._input_show();
                		 }
                	}
                },
                "inputCount":function(){//返回当前框中有几个输入
                	return _this.find("div.auto_word_div").length;
                },
                "getDefaults":function(){//增加一个返回插件的设置
                	return defaults;
                }
            };
            //保存操作对象
            objAuto[_this.attr("id")] = result;
            $.smate.autoword[_this.attr("id")] = result;
            //添加单词输入框
            _this.append(_create_word_input());//input添加到DIV
            
            //键盘事件
            var _input = _this.find(".auto_word_input");
            _input.attr("id", _this.attr("id") + "_input");
            if(defaults["watermark_flag"]==true){
        		if(result.inputCount()<defaults["words_max"]){
        			_this.find(".auto_word_watermark").text(defaults["watermark_tip"]);
        			_this.find(".auto_word_watermark").show();
            		}else{
            			_this.find(".auto_word_watermark").hide();
            		}
        		}
            
            //单词输入完成结束符或回车
            _input.keyup(function(event){
                result._input_width();
                var _input = $(this);
                
                if("single"==defaults["model"]){
                	if(result.vals().length>0){
                    	_input.val("");
                	   }
                   }    
                 
                var text = result._input_filter(_input.val());
                if (text.length > 0) {
					var lastchr = text.charAt(text.length - 1);
			    	var _validate_url=defaults["validateUrl"];
					
					if(event.keyCode==186 || event.keyCode==59 || event.keyCode==188){//按键；=(ie:186,ff:59)，=(ie,ff=188)式完成输入
						if (text.length > 1) {
							if($.inArray(lastchr,defaults["split_char"])!=-1){
								//result.put("", text.substring(0, text.length - 1))._input_show();
								if (_validate_url != null && typeof(_validate_url)!="undefined") {
									
								    result._input_validate(_validate_url,"",text.substring(0, text.length - 1));
								}else{
									result._input_split_char(text.substring(0, text.length - 1));
									//result.put("", text.substring(0, text.length - 1))._input_show();
								}
								if($.isFunction(defaults["split_charCallback"])){
									defaults["split_charCallback"](text.substring(0, text.length - 1));
								}
							}else{
								if (defaults["split_char"] != null && defaults["split_char"].length > 0) {
									//result.put("", text)._input_show();
									if (_validate_url != null && typeof(_validate_url)!="undefined") {
										
										result._input_validate(_validate_url,"",text);
									}else{
										result._input_split_char(text);
									}
								}
							}
						} else {//输入无效内容，清空
								result._input_show();
                        }
					}else{
					  $.each(defaults["split_char"], function(){
                        if (lastchr == this) {
                            if (text.length > 1) {
                               // result.put("", text.substring(0, text.length - 1))._input_show();
                            	if (_validate_url != null && typeof(_validate_url)!="undefined") {
									
                            		result._input_validate(_validate_url,"",text.substring(0, text.length - 1));
                            	}else{
											result._input_split_char(text.substring(0, text.length - 1));
                            	//	result.put("",text.substring(0, text.length - 1) )._input_show();
                            	}
                            } else {//输入无效内容，清空
                            
                            	if (defaults["split_char"] != null && defaults["split_char"].length > 0) {
									result._input_show();
								}
                            }
                            return false;
                        }
                    });
					}
					if($.isFunction(defaults["enter"])&& event.keyCode == 13){
                    	defaults["enter"](this);
                    }else if (defaults["enter"] && event.keyCode == 13) {//回车自动完成
                        //检查是否需要验证输入内容的正确性.
                    	if (_validate_url != null && typeof(_validate_url)!="undefined") {
                    		result._input_validate(_validate_url,"",text);
                    	}else{
                    		blurKeyMark=	setTimeout(function(){
                    	   result._input_split_char(text);
                    	//	result.put("", text)._input_show();
                    		},160)
                    	}
                        
                    }
                }else{
                 	result._input_show();
            	}
            }).keydown(function(event){//删除和移动
                result._input_width();
                var _input = $(this);
                var _input_parent = _input.parent();
                var text = result._input_filter(_input.val());
                if (text == "") {
                    if (event.keyCode == "8") {
                        _input_parent.prev().find(".auto_word_div_del").click();//改为触发事件删除--lrl
                    } else if (event.keyCode == "37") {
                        _input_parent.prev().before(_input_parent);
                        result._input_show();
                    } else if (event.keyCode == "39") {
                        _input_parent.next().after(_input_parent);
                        result._input_show();
                    }
                }
                if (event.keyCode == 13) {//防止事件冒泡
                    event.preventDefault();
                    if("single"==defaults["model"]){
                        	_input.val("");
                    }
                    return false;
                }
               
            }).blur(function(event){//失去焦点
            	     var _this = this;
            
                if (!$.isFunction(defaults["blur"]) && defaults["blur"]) {
                	blurKeyMark=setTimeout(function(){
                    var _input = $(_this);
                    
                    if("single"==defaults["model"]){
                    	if(result.vals().length>0){
                        	_input.val("");
                        	}
                    	}  
                    var text = result._input_filter(_input.val());
                    var isOut = true;
                    /*$("#en_auto_word_outer_div_1").each(function(){
                        if ($(this).is(":visible")) {//关联内容显示，不自动完成单词
                            isOut = false;
                            return false;
                        }
                    });*/
                    
                    if (isOut && text.length > 0 ) {//关联内容隐藏，自动完成单词
                    	var re = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/; // 输入不是邮件失去焦点清空内容
                    	if(defaults["email"] && !re.test(text)) {
                    		 _input.val("");
                    	} else {
                    		// 点击输入自动下拉框提示的关键词不做处理
                    		if(typeof $.smate.autoword.config == "undefined" || ($.smate.autoword.config != "undefined" && !$.smate.autoword.config.mouseDownOnSelect)){
                    			//直接复制关键词中有分隔符(,;，；:：)，会保存超过指定个数 SCM-3394 by zk 2013/08/13
                    			result._input_split_char(text);
                    		}
//                    		return;
                    		}
                    } else {//输入无效内容，清空
                        _input.val("");
                    		}
                		},160)
                }else{
						if($.isFunction(defaults["blur"])){
							defaults["blur"](this);
							 result._input_width();
						}
				}
                
            if(defaults["watermark_flag"]==true){
       			 mark = setTimeout(result._watermark,75);
                }
               
            }).focus(function(event){
            	if(defaults["watermark_flag"]){	
            		clearTimeout(mark);
            		_this.find(".auto_word_watermark").hide();
          		}
        });
            
             if (defaults["select"] != null && $.isFunction(defaults["select"])) {//自动完成下拉框
                var select = defaults["select"](_input);
                select["extend"](function(data){
					if(defaults["enter"]){
						//如果需要验证输入内容的正确性，则不清除上一输入内容(兼容选择联系人和匹配关键词业务需求的功能实现)_MJG_2013-03-04_SCM-1932).
						//if(!$.isFunction(defaults["blur"]) && defaults["blur"])
						/*var _validate_url=defaults["validateUrl"];
						if(_validate_url!=null && typeof(_validate_url)!="undefined"){
							
						}else{
							
						}*/
//						_input.parent().prev().find(".auto_word_div_del").click();//修改为事件触发删除--lrl
						clearTimeout(blurKeyMark);
					}
//					   _input.val(data["name"]);
					  
                    result.put(data["code"], data["name"])._input_show();
					if($.isFunction(defaults["selectCallback"])){
						defaults["selectCallback"](data);
					}
                });
            }
             if (!$.isFunction(defaults["delClick"]) && defaults["delClick"]) {
	            _this.find(".auto_word_div_del").delegate("click", function(e){//鼠标事件，删除word   delegate不能响应，把delegate改成live
	                $(this).parent().remove();
	                if(typeof $.smate.autoword.friendId_clear !="undefined"){
	                	$.smate.autoword.friendId_clear();
	                }
	                result._input_show();//显示输入框
	                return false;//防止事件冒泡
	            });
             }else if($.isFunction(defaults["delClick"])){
            	 _this.find(".auto_word_div_del").delegate("click", function(e){//鼠标事件，删除word
 	                $(this).parent().remove();
 	                result._input_show();//显示输入框
 	                defaults["delClick"]($(this).parent().attr("val"),$(this).parent().find("label").text(),result);
 	                return false;//防止事件冒泡
 	            });
             }
            _this.click(function(){
            	result._input_show(false);//显示输入框
            });
            
        });
        return objAuto.length == 1 ? objAuto[this.attr("id")] : objAuto;
    };
    
    //块html前面带统计数zk[认同数]（如关键词）
    var _create_word_AndCount_html = function(val, text,count){//创建一个块
        var arr = [];
        arr.push('<div class="auto_word_div" style="padding-left: 1px;" val="' + val + '" >');
        arr.push('<a href="javascript:void(0)" class="numberGray_list">'+count+'</a>');
        arr.push('<label class="auto_word" style="padding-left: 5px;" title="' + text + '">' + text + '</label>');
        arr.push('<a href="javascript:void(0)" class="auto_word_div_del"></a>');
        arr.push('</div>');
        return arr.join("");
    };
    
    //块html（如关键词）
    var _create_word_html = function(val, text){//创建一个块
        var arr = [];
        arr.push('<div class="auto_word_div" val="' + val + '">');
        arr.push('<label class="auto_word" title="' + text + '">' + text + '</label>');
        arr.push('<a href="javascript:void(0)" class="auto_word_div_del"></a>');
        arr.push('</div>');
        return arr.join("");
    };
    
    //单词输入框html  //auto_word_watermark 水印提示
    var _create_word_input = function(){//scm-6676 每个关键词允许输入80字符
    		  return '<div class="auto_word_input_div" style=""><input name="input" type="text" class="auto_word_input" size="1" maxlength="80" /></div><label class="auto_word_watermark" />';
    };
    
})(jQuery);
