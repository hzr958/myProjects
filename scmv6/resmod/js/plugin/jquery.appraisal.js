/**
 * @author zym
 * 联系人评价插件
 */
(function($){

    $.fn.appraisal = function(options){
        var defaults = {
            "key": null,
            "ctxpath": null,
            "appStatus": null
        };
        
        if (typeof options != "undefined") {
            $.extend(defaults, options);
            if (typeof ctxpath != "undefined") {
                defaults["ctxpath"] = ctxpath;
            }
        }
        
        ////常用评价封装 start
        var urlMap = {
            //工作评价
            "work": defaults["ctxpath"] + "/friendFappraisal/ajaxLoadMain",
			//工作评价详情
            "work_detail": defaults["ctxpath"] + "/friendFappraisal/ajaxLoadDetail"
        };
        
        ////常用评价封装 end
        
        //根据评价类型，动态设置默认值
        var _setting_default = function(_this){//
            var keyVal = defaults["key"];
            if (typeof urlMap[keyVal] != "undefined") {
                defaults["key"] = urlMap[keyVal];
            }
            
            if (typeof urlMap[keyVal + "_detail"] != "undefined") {
                defaults["key_detail"] = urlMap[keyVal + "_detail"];
            }
        };
        
        this.each(function(index){
            var _this = $(this);
            
            _setting_default(_this);//根据评价类型，动态设置默认值
            var _workId = _this.attr("workId");
            var _des3PsnId = _this.attr("des3PsnId");
            var _psnName = _this.attr("psnName");
            if (_des3PsnId == null || _des3PsnId.length == 0) {
                return true;
            }
			
            var post_data = {
                "workId": _workId,
                "des3PsnId": _des3PsnId,
                "psnName": _psnName
            };
            if (defaults["appStatus"] != null) {
                post_data["appStatus"] = defaults["appStatus"];
            }
            $.ajax({//工作评价
                url: defaults["key"],
                type: 'post',
                dataType: 'html',
                data: post_data,
                success: function(data){
                    if (data && $.trim(data) != "") {
                        var _html = $(data);
						var _is_op_count = 0;
                        _html.find(".tmp_box_op").click(function(){//显示详情按钮
                            var _a = $(this);
                            var totalCount = _a.attr("totalCount");
							if(typeof locale == "undefined"){
								alert("缺少变量locale");
								return;
							}
                            var _more_evaluate = _html.find(".more_evaluate_"+locale);
                            var first = _more_evaluate.find("dd").size();
                            post_data["first"] = first;
                            post_data["totalCount"] = totalCount;
							if(_is_op_count++==0){
								_work_detail(_more_evaluate, post_data);//加载详情
							}else{//折叠详情
								_more_evaluate.toggle();
							}
							_html.find(".tmp_box_op").find(".open,.fold").toggle();
                        });
                        
                        _html.find(".tmp_box_more a").click(function(){//更多按钮
                            var _a = $(this);
                            var totalCount = _a.attr("totalCount");
                            var _more_evaluate = _html.find(".more_evaluate_"+locale);
                            var first = _more_evaluate.find("dd").size();
                            post_data["first"] = first;
                            post_data["totalCount"] = totalCount;
                            _work_detail(_more_evaluate, post_data);//加载详情
                        });
                        
                        _this.after(_html);
                    }
                }
            });
            
            //加载评价详情
            var _work_detail = function(_more_evaluate, post_data){
            
                //open loading
                $.proceeding.show();
                $.ajax({
                    url: defaults["key_detail"],
                    type: 'post',
                    dataType: 'html',
                    data: post_data,
                    success: function(data){
                        if (data && $.trim(data) != "") {
                            var _tmp_bos_more = _more_evaluate.find(".tmp_box_more");
                            _tmp_bos_more.before(data);
                            _more_evaluate.show();
                            if (_more_evaluate.find("dd").size() < parseInt(post_data["totalCount"])) {//更多详情按钮
                                _tmp_bos_more.show();
                            } else {
                                _tmp_bos_more.hide();
                            }
                            //close loading
                            $.proceeding.hide();
                        }
                    }
                });
            };
        });
    }
})(jQuery);
