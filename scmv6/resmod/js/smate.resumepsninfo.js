var smate = smate ? smate : {};
smate.resumepsninfo = smate.resumepsninfo ? smate.resumepsninfo : {};

/**
 * 初始年份
 */
smate.resumepsninfo.currentYear = function(){
    return $("#tmp_currentYear").val();
};

smate.resumepsninfo.startYear = function(){
    return $("#tmp_startYear").val();
};

//产生数字下拉，例如年度等
smate.resumepsninfo.yearOptions = function(start, end, select_id){
    if (start > end) {
        var tmp = start;
        start = end;
        end = tmp;
    }
    var select = $("#" + select_id);
    for (; end >= start; end--) {
        var option = $("<option value='" + end + "' select=''>" + end + "</option>");
        select.append(option);
    }
};

smate.resumepsninfo.refreshInformation = null;

/**
 * 编辑基本信息 start
 */
smate.resumepsninfo.base = smate.resumepsninfo.base ? smate.resumepsninfo.base : {};

/**
 * 编辑头像html内容
 */
smate.resumepsninfo.base.avatar = function(){
    var _base_list_text = $("#base_list_text");
    _base_list_text.hide();
    
    var _edit_box = $("#base_list_avatar_box");
    if (_edit_box.attr("id") == "base_list_avatar_box") {//显示即可
        _edit_box.show();
    } else {//创建
        _edit_box = $('<div id="base_list_avatar_box" style="height:160px;"></div>');
        var _init = function(_data){
            var _val = {
                "avatar_img": $("#detail_avatars").attr("src")
            };//获取旧图片路径
            var _text_format = $.scmformat(_data["text_format"], _val);//格式化数据
            _edit_box.html(_text_format);
            _base_list_text.before(_edit_box);//插入格式化后的内容
            $("#avatar_img_cancel").click(smate.resumepsninfo.base.avatarClose);//取消编辑头像
            if (_val.avatar_img.indexOf("head_nan_photo.jpg") == -1 && _val.avatar_img.indexOf("head_nv_photo.jpg") == -1) {
                $("#avatar_img_delete").click(smate.resumepsninfo.base.avatar.del);//删除头像
            } else {
                $("#avatar_img_delete").hide();
            }
            var _des3PsnId = $("#detail_avatars").attr("des3PsnId");
            $("#base_list_avatar_box_upload").imgcutupload({
                "snsctx": snsctx,
                "resmod": resmod,
                "imgType": 1,
                "locale": locale,
                "dataDes3Id": _des3PsnId,
                "selectFileFn":1
            });
        };
        smate.resumepsninfo.ajaxLoadHtml("editAvatar.html", null, null, _init);
    }
};

/**
 * 编辑头像关闭
 */
smate.resumepsninfo.base.avatarClose = function(){
    $("#base_list_text").show();
    $("#base_list_avatar_box").remove();
};

/**
 * 删除头像
 */
smate.resumepsninfo.base.avatar.del = function(){
    jConfirm(smate.resumepsninfo.locale["confirm_delete_avatar"], smate.resumepsninfo.locale["confirm_tip"], function(sure){
        if (sure == true) {
            $.ajax({
                url: snsctx + '/profile/ajaxRemoveAvatars',
                type: 'post',
                dataType: 'json',
                success: function(_data){
                    if (_data.result == 'error') {
                        $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
                    } else {
						$("#detail_avatars,#avatar_img_id").attr("src", _data.defaultAvatars + "?temp=" + (new Date().getTime().toString(36)));
                        smate.resumepsninfo.base.avatarClose();
                    }
                },
                error: function(){
                    $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
                }
            });
        }
    });
};

//头像处理回调函数
function imgUploadCutCallBack(patch){
    $("#detail_avatars,#avatar_img_id").attr("src", patch + "?temp=" + (new Date().getTime().toString(36)));
    var post_data = {
        "avatars": patch
    };
    $.proceeding.show();
    $.ajax({
        url: snsctx + '/profile/ajaxSaveAvatar',
        type: 'post',
        dataType: 'json',
        data: post_data,
        async: false,
        success: function(_data){
            $.proceeding.hide();
            if (_data.result == 'error') {
                $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
            } else {
                smate.resumepsninfo.base.avatarClose();
            }
        },
        error: function(){
            $.proceeding.hide();
            $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
        }
    });
};
/**
 * 编辑头携信息 start
 */
smate.resumepsninfo.titolo = smate.resumepsninfo.titolo ? smate.resumepsninfo.titolo : {};

/**
 * 编辑头携信息html内容
 */
smate.resumepsninfo.titolo.editHtml=function(){
	var _base_list_text = $("#base_list_text");
    _base_list_text.hide();
    var _edit_box = $("#titolo_list_edit_box");
    if (_edit_box.attr("id") == "titolo_list_edit_box") {//显示即可
        _edit_box.show();
        
        smate.resumepsninfo.base.editData(function(data){
            //填充数据
            smate.resumepsninfo.titolo.editFill(data);
        });
        
    } else {//创建
    	 _edit_box = $('<div id="titolo_list_edit_box"></div>');
    	 var _init = function(){
             
    		 smate.resumepsninfo.base.region = $("#base_regionId,#base_provinceId,#base_cityId").region({
                 "locale": locale,
                 "ctx": snsctx,
                 "lazy": true
             });
             
           smate.resumepsninfo.base.birth = new DateInputUtil.DateInput("base_birth", smate.resumepsninfo.startYear(), smate.resumepsninfo.currentYear(), locale);
             

    	        smate.resumepsninfo.base.editData(function(data){
    	            //填充数据
    	            smate.resumepsninfo.titolo.editFill(data);
    	        });
             
        $("#base_close_a").click(smate.resumepsninfo.titolo.editClose);//关闭
             
         smate.resumepsninfo.titolo.editValidate();//初始化校验
             $("#base_save_a").click(function(){
                 $("#base_form").submit();
             });//保存基本信息
         };
       
        smate.resumepsninfo.ajaxLoadHtml("editTitlo.html", _edit_box, _base_list_text, _init);
    }
};

/**
 * 编辑职称信息 start
 */
smate.resumepsninfo.position = smate.resumepsninfo.position ? smate.resumepsninfo.position : {};

/**
 * 编辑职称信息html内容
 */
smate.resumepsninfo.position.editHtml=function(){
	var _base_list_text = $("#base_list_text");
    _base_list_text.hide();
    var _edit_box = $("#position_list_edit_box");
    if (_edit_box.attr("id") == "position_list_edit_box") {//显示即可
        _edit_box.show();
        
        smate.resumepsninfo.base.editData(function(data){
            //填充数据
            smate.resumepsninfo.position.editFill(data);
        });
        
    } else {//创建
    	 _edit_box = $('<div id="position_list_edit_box"></div>');
    	 var _init = function(){
             
    		 smate.resumepsninfo.base.region = $("#base_regionId,#base_provinceId,#base_cityId").region({
                 "locale": locale,
                 "ctx": snsctx,
                 "lazy": true
             });
    		 $("#base_position").complete({
                 "ctx": snsctx,
                 "key": "position",
                 "bind": {
                     "code": "base_posId"
                 }
             });//职称自动提示下拉框
           smate.resumepsninfo.base.birth = new DateInputUtil.DateInput("base_birth", smate.resumepsninfo.startYear(), smate.resumepsninfo.currentYear(), locale);
             
		     cnf.position.setNew(cnf.position.get());
           _edit_box.find('.selectClass').authoritydiv({
               "locale": locale,
               "authorityValue":cnf.position.get() 
           }, {}, function(params, value){
		     		cnf.position.setNew(value);
           		});

    	     smate.resumepsninfo.base.editData(function(data){
    	            //填充数据
    	            smate.resumepsninfo.position.editFill(data);
    	        });
             
        $("#base_close_a").click(smate.resumepsninfo.position.editClose);//关闭
             
         
		$("#base_save_a").click(function(){
		
			smate.resumepsninfo.position.editSave();
			smate.resumepsninfo.position.editClose();
		});
             
         };
       
        smate.resumepsninfo.ajaxLoadHtml("editPosition.html", _edit_box, _base_list_text, _init);
    }
};


/**
 * 编辑基本信息html内容
 */
smate.resumepsninfo.base.editHtml = function(){
    var _base_list_text = $("#base_list_text");
    _base_list_text.hide();
    
    var _edit_box = $("#base_list_edit_box");
    if (_edit_box.attr("id") == "base_list_edit_box") {//显示即可
        _edit_box.show();
        
        smate.resumepsninfo.base.editData(function(data){
            //填充数据
            smate.resumepsninfo.base.editFill(data);
        });
        
    } else {//创建
        _edit_box = $('<div id="base_list_edit_box"></div>');
        var _init = function(){
            smate.resumepsninfo.base.region = $("#base_regionId,#base_provinceId,#base_cityId").region({
                "locale": locale,
                "ctx": snsctx,
                "lazy": true
            });
            
            $("#base_degreeName").complete({
                "ctx": snsctx,
                "key": "degree"
            });//学历自动提示下拉框
            $("#base_position").complete({
                "ctx": snsctx,
                "key": "position",
                "bind": {
                    "code": "base_posId"
                }
            });//职称自动提示下拉框
            smate.resumepsninfo.base.birth = new DateInputUtil.DateInput("base_birth", smate.resumepsninfo.startYear(), smate.resumepsninfo.currentYear(), locale);
            
            smate.resumepsninfo.base.editData(function(data){
                //填充数据
                smate.resumepsninfo.base.editFill(data);
            });
            
            $("#base_close_a").click(smate.resumepsninfo.base.editClose);//关闭
            if (locale == "zh_CN") {
                $("#base_name").blur(function(){
                    if ($.trim(this.value) != "") {
                        smate.resumepsninfo.base.ajaxpinyin(this, this.value);
                    }
                });
            }
            
            smate.resumepsninfo.base.editValidate();//初始化校验
            $("#base_save_a").click(function(){
                $("#base_form").submit();
            });//保存基本信息
        };
        smate.resumepsninfo.ajaxLoadHtml("editBase_" + locale + ".html", _edit_box, _base_list_text, _init);
    }
};

/**
 * 编辑基本信息html内容：姓名
 */
smate.resumepsninfo.base.editHtmlName = function(){
    var _base_list_text = $("#base_list_text");
    _base_list_text.hide();
    
    var _edit_box = $("#base_list_edit_box_name");
    if (_edit_box.attr("id") == "base_list_edit_box_name") {//显示即可
        _edit_box.show();
        smate.resumepsninfo.base.editData(function(data){
            //填充数据
            smate.resumepsninfo.base.editFillName(data);
        });
    } else {//创建
        _edit_box = $('<div id="base_list_edit_box_name"></div>');
        var _init = function(){
            
         $("#base_close_a_name").click(smate.resumepsninfo.base.editCloseName);//关闭
            if (locale == "zh_CN") {
                $("#base_name").blur(function(){
                    if ($.trim(this.value) != "") {
                        smate.resumepsninfo.base.ajaxpinyin(this, this.value);
                    }
                });
            } 
			
			smate.resumepsninfo.base.editData(function(data){
                //填充数据
                smate.resumepsninfo.base.editFillName(data);
            });
            
         smate.resumepsninfo.base.editValidateName();//初始化校验
            $("#base_save_a_name").click(function(){
                $("#baseName_form").submit();
            });//保存基本信息
        };
        smate.resumepsninfo.ajaxLoadHtml("editBaseName_" + locale + ".html", _edit_box, _base_list_text, _init);
    }
};

/**
 * 编辑基本信息html内容：更多其它内容
 */
smate.resumepsninfo.base.editHtmlMore = function(){
    var _base_list_text = $("#base_list_text");
    _base_list_text.hide();
    
    var _edit_box = $("#base_list_edit_box");
    if (_edit_box.attr("id") == "base_list_edit_box_more") {//显示即可
        _edit_box.show();
        
        smate.resumepsninfo.base.editData(function(data){
            //填充数据
            smate.resumepsninfo.base.editFillMore(data);
        });
        
    } else {//创建
        _edit_box = $('<div id="base_list_edit_box_more"></div>');
        var _init = function(){
            
         $("#base_degreeName").complete({
                "ctx": snsctx,
                "key": "degree"
            });
			
            smate.resumepsninfo.base.birth = new DateInputUtil.DateInput("base_birth", smate.resumepsninfo.startYear(), smate.resumepsninfo.currentYear(), locale);
            
            smate.resumepsninfo.base.editData(function(data){
                //填充数据
                smate.resumepsninfo.base.editFillMore(data);
            });
            
            $("#base_close_a_more").click(smate.resumepsninfo.base.editCloseMore);//关闭
            
            smate.resumepsninfo.base.editValidateMore();//初始化校验
            $("#base_save_a_more").click(function(){
                $("#baseMore_form").submit();
            });//保存基本信息
        };
        smate.resumepsninfo.ajaxLoadHtml("editBaseMore_" + locale + ".html", _edit_box, _base_list_text, _init);
    }
};

/**
 * 编辑基本信息html内容：国家地区
 */
smate.resumepsninfo.base.editHtmlLocation = function(){
    var _base_list_text = $("#base_list_text");
    _base_list_text.hide();
    
    var _edit_box = $("#base_list_edit_box_location");
    if (_edit_box.attr("id") == "base_list_edit_box_location") {//显示即可
        _edit_box.show();
        
        smate.resumepsninfo.base.editData(function(data){
            //填充数据
            smate.resumepsninfo.base.editFillLocation(data);
        });
        
    } else {//创建
        _edit_box = $('<div id="base_list_edit_box_location"></div>');
        var _init = function(){
            smate.resumepsninfo.base.region = $("#base_regionId,#base_provinceId,#base_cityId").region({
                "locale": locale,
                "ctx": snsctx,
                "lazy": true
            });
            
            smate.resumepsninfo.base.birth = new DateInputUtil.DateInput("base_birth", smate.resumepsninfo.startYear(), smate.resumepsninfo.currentYear(), locale);
            
            smate.resumepsninfo.base.editData(function(data){
                //填充数据
                smate.resumepsninfo.base.editFillLocation(data);
            });
            
         $("#base_close_a_location").click(smate.resumepsninfo.base.editCloseLocation);//关闭
            
		   $("#base_save_a_location").click(function(){
		   	smate.resumepsninfo.base.editSaveLocation();
		   });//保存基本信息
        };
        smate.resumepsninfo.ajaxLoadHtml("editBaseLocation_" + locale + ".html", _edit_box, _base_list_text, _init);
    }
};

//判断是否是特殊字符
smate.resumepsninfo.base.isSpecial = function(s){
    var str = '",.;[]{}+=|\*&^%$#@!~()-/?<>';
    var flag = false;
    if ($.trim(s).length > 0) {
        for (var i = 0; i < str.length; i++) {
            if (s.indexOf(str.charAt(i)) >= 0) {
                flag = true;
                break;
            }
        }
        if (flag) 
            return true;
    }
    var patrn = /^[^<]*(<(.|\s)+>)[^>]*$|^#$/;
    if (!patrn.exec(s)) 
        return false;
    return true;
};

/**
 * 编辑头携信息校验
 */

smate.resumepsninfo.titolo.editValidate =function(){
	$("#base_form").validate({
        rules: {
            "titolo": {
            	 required: true,
                maxlength: 50
            }
        },
        messages: {
            
            "titolo": {
            	 required: smate.resumepsninfo.locale["base_titolo_required"],
                maxlength: $.format(smate.resumepsninfo.locale["base_titolo_max"])
            }
        },
        errorPlacement: function(error, element){
            $(element).parent().next().prepend(error);
        },
        submitHandler: function(){
            smate.resumepsninfo.titolo.editSave();
            return false;
        }
    });
    
   
    
	
};


/**
 * 编辑基本信息校验
 */
smate.resumepsninfo.base.editValidate = function(){
    $("#base_form").validate({
        rules: {
            "name": (locale == "zh_CN" ? {
                minlength: 2,
                required: true,
                maxlength: 61
            } : {
                maxlength: 61
            }),
            "lastName": {
                required: true,
                //corrent_name: true,
                maxlength: 20
            },
            "firstName": {
                required: true,
                //corrent_name: true,
                maxlength: 40,
                first_last_name: true
            },
            "otherName": {
                maxlength: 61
            },
            "degreeName": {
                maxlength: 50
            }
        },
        messages: {
            "name": (locale == "zh_CN" ? {
                required: smate.resumepsninfo.locale["base_name_required"],
                minlength: $.format(smate.resumepsninfo.locale["base_name_min"]),
                corrent_name: smate.resumepsninfo.locale["base_name_ok"],
                maxlength: $.format(smate.resumepsninfo.locale["base_name_max"])
            } : {
                maxlength: $.format(smate.resumepsninfo.locale["base_name_max"])
            }),
            "lastName": {
                required: smate.resumepsninfo.locale["base_last_name_required"],
                corrent_name: smate.resumepsninfo.locale["base_last_name_ok"],
                maxlength: $.format(smate.resumepsninfo.locale["base_last_name_max"])
            },
            "firstName": {
                required: smate.resumepsninfo.locale["base_first_name_required"],
                corrent_name: smate.resumepsninfo.locale["base_first_name_ok"],
                maxlength: $.format(smate.resumepsninfo.locale["base_first_name_max"]),
                first_last_name: smate.resumepsninfo.locale["base_last_first_max"]
            },
            "otherName": {
                maxlength: $.format(smate.resumepsninfo.locale["base_other_name_max"])
            },
            
            "degreeName": {
                maxlength: $.format(smate.resumepsninfo.locale["base_degree_max"])
            }
        },
        errorPlacement: function(error, element){
            $(element).parent().next().prepend(error);
        },
        submitHandler: function(){
            smate.resumepsninfo.base.editSave();
            return false;
        }
    });
    
    //first_name+last_name不超过50个
    jQuery.validator.addMethod("first_last_name", function(value, element){
    
        var firstName = $("#base_firstName").val();
        var lastName = $("#base_lastName").val();
        var fullName = firstName + " " + lastName;
        
        if (fullName.length > 62) 
            return false;
        
        return true;
    }, "");
    
    jQuery.validator.addMethod("corrent_name", function(value, element){
    
        if (smate.resumepsninfo.base.isSpecial(value)) {
            return false;
        }
        return true;
    }, "");
    
    //autocompleteInitDegree("degreeName",320);

};

/**
 * 编辑基本信息校验:name
 */
smate.resumepsninfo.base.editValidateName = function(){
    $("#baseName_form").validate({
        rules: {
            "name": (locale == "zh_CN" ? {
                minlength: 2,
                required: true,
                maxlength: 61
            } : {
                maxlength: 61
            }),
            "lastName": {
                required: true,
                //corrent_name: true,
                maxlength: 20
            },
            "firstName": {
                required: true,
                //corrent_name: true,
                maxlength: 40,
                first_last_name: true
            }
        },
        messages: {
            "name": (locale == "zh_CN" ? {
                required: smate.resumepsninfo.locale["base_name_required"],
                minlength: $.format(smate.resumepsninfo.locale["base_name_min"]),
                corrent_name: smate.resumepsninfo.locale["base_name_ok"],
                maxlength: $.format(smate.resumepsninfo.locale["base_name_max"])
            } : {
                maxlength: $.format(smate.resumepsninfo.locale["base_name_max"])
            }),
            "lastName": {
                required: smate.resumepsninfo.locale["base_last_name_required"],
                corrent_name: smate.resumepsninfo.locale["base_last_name_ok"],
                maxlength: $.format(smate.resumepsninfo.locale["base_last_name_max"])
            },
            "firstName": {
                required: smate.resumepsninfo.locale["base_first_name_required"],
                corrent_name: smate.resumepsninfo.locale["base_first_name_ok"],
                maxlength: $.format(smate.resumepsninfo.locale["base_first_name_max"]),
                first_last_name: smate.resumepsninfo.locale["base_last_first_max"]
            }
        },
        errorPlacement: function(error, element){
            $(element).parent().next().prepend(error);
        },
        submitHandler: function(){
            smate.resumepsninfo.base.editSaveName();
            return false;
        }
    });
    
    //first_name+last_name不超过50个
    jQuery.validator.addMethod("first_last_name", function(value, element){
    
        var firstName = $("#base_firstName").val();
        var lastName = $("#base_lastName").val();
        var fullName = firstName + " " + lastName;
        
        if (fullName.length > 62) 
            return false;
        
        return true;
    }, "");
    
    jQuery.validator.addMethod("corrent_name", function(value, element){
    
        if (smate.resumepsninfo.base.isSpecial(value)) {
            return false;
        }
        return true;
    }, "");
    
    //autocompleteInitDegree("degreeName",320);

};


/**
 * 编辑基本信息校验:more
 */
smate.resumepsninfo.base.editValidateMore = function(){
    $("#baseMore_form").validate({
        rules: {
            "otherName": {
                maxlength: 61
            },
            "degreeName": {
                maxlength: 50
            }
        },
      messages: {
            "otherName": {
                maxlength: $.format(smate.resumepsninfo.locale["base_other_name_max"])
            },
            
            "degreeName": {
                maxlength: $.format(smate.resumepsninfo.locale["base_degree_max"])
            }
         },
        errorPlacement: function(error, element){
            $(element).parent().next().prepend(error);
        },
        submitHandler: function(){
            smate.resumepsninfo.base.editSaveMore();
            return false;
        }
    });
    
};

/**
 * 编辑基本信息查询内容
 */
smate.resumepsninfo.base.editData = function(dataSetting){
    $.ajax({
        url: snsctx + "/profile/ajaxBase",
        type: 'post',
        dataType: 'json',
        success: function(_data){
            if (typeof dataSetting != "undefined") {
                dataSetting(_data);
            }
        }
    });
};


/**
 * 编辑职称信息填充
 */

/*smate.resumepsninfo.position.editFill = function(_data){

    if (_data != null) {
    	  $("#base_position").val(_data["position"]);
    }
};*/


/**
 * 编辑头携信息填充
 */

smate.resumepsninfo.titolo.editFill = function(_data){

    if (_data != null) {
    	_data["titolo"]==null?$("#base_titolo").val(_data["insName"]): $("#base_titolo").val(_data["titolo"]);
    }
};

/**
 * 编辑基本信息填充内容
 */
smate.resumepsninfo.base.editFill = function(_data){

    if (_data != null) {
        $("#base_name").val(_data["name"]);
        $("#base_firstName").val(_data["firstName"]);
        $("#base_lastName").val(_data["lastName"]);
        $("#base_otherName").val(_data["otherName"]);
/*        $("#base_titolo").val(_data["titolo"]);*/
        $("#base_degreeName").val(_data["degreeName"]);
        //$("#regionId").val(_data["regionId"]);
        
        var _regionShow = _data["regionShow"];
        var _regionArr = _regionShow != null ? _regionShow.split(",") : [];
        //重新选择下拉框
        $("#base_regionId,#base_provinceId,#base_cityId").each(function(index){
            var _this = $(this);
            _this.attr("code", "");
            if (typeof _regionArr[index] != "undefined") {
                _this.attr("code", _regionArr[index]);
            }
        });
        smate.resumepsninfo.base.region.select();
        smate.resumepsninfo.base.birth.initDateSelector(_data["birthday"]);
        
/*        $("#base_position").val(_data["position"]);*/
        $("#base_posId").val(_data["posId"]);
        if (_data["sex"] == 1) {
            $("#base_male").attr("checked", "checked");
        } else {
            $("#base_female").attr("checked", "checked");
        }
    }
};

smate.resumepsninfo.base.editFillName = function(_data){

    if (_data != null) {
        $("#base_name").val(_data["name"]);
        $("#base_firstName").val(_data["firstName"]);
        $("#base_lastName").val(_data["lastName"]);
        $("#base_otherName").val(_data["otherName"]);
    }
};


smate.resumepsninfo.base.editFillMore = function(_data){

    if (_data != null) {
        $("#base_otherName").val(_data["otherName"]); 
        smate.resumepsninfo.base.birth.initDateSelector(_data["birthday"]);
        $("#base_degreeName").val(_data["degreeName"]);
        if (_data["sex"] == 1) {
            $("#base_male").attr("checked", "checked");
        } else {
            $("#base_female").attr("checked", "checked");
        }
    }
};


smate.resumepsninfo.base.editFillLocation = function(_data){

    if (_data != null) {
        
        var _regionShow = _data["regionShow"];
        var _regionArr = _regionShow != null ? _regionShow.split(",") : [];
        //重新选择下拉框
        $("#base_regionId,#base_provinceId,#base_cityId").each(function(index){
            var _this = $(this);
            _this.attr("code", "");
            if (typeof _regionArr[index] != "undefined") {
                _this.attr("code", _regionArr[index]);
            }
        });
        smate.resumepsninfo.base.region.select();
    }
};


/**
 * 编辑头携信息保存
 */
smate.resumepsninfo.position.editSave = function(){
    var position = $.trim($("#base_position").val());
    var post_data = {      
        'position': position,
			'anyUser':cnf.position.getNew()		  
     };
    $.proceeding.show();
    $.ajax({
        url: snsctx + '/profile/ajaxSavePosition',
        type: 'post',
        dataType: 'json',
        data: post_data,
        success: function(data){
            if (data.result == 'error') {
                $.smate.scmtips.show('error', data["msg"]);
            } else {
            
                window.location.href = snsctx + "/profile/load";            
                smate.resumepsninfo.titolo.editClose();
					 cnf.position.set(cnf.brief.getNew());
            	}
            $.proceeding.hide();
        },
        error: function(){
            $.proceeding.hide();
            $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
        }
    });
};


/**
 * 编辑头携信息保存
 */
smate.resumepsninfo.titolo.editSave = function(){
    var titolo = $.trim($("#base_titolo").val());
    var post_data = {      
        'titolo': titolo,
    };
    $.proceeding.show();
    $.ajax({
        url: snsctx + '/profile/ajaxSaveTitolo',
        type: 'post',
        dataType: 'json',
        data: post_data,
        success: function(data){
            if (data.result == 'error') {
                $.smate.scmtips.show('error', data["msg"]);
            } else {
            
                window.location.href = snsctx + "/profile/load";            
                smate.resumepsninfo.titolo.editClose();
            }
            $.proceeding.hide();
        },
        error: function(){
            $.proceeding.hide();
            $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
        }
    });
};

/**
 * 编辑基本信息保存
 */
smate.resumepsninfo.base.editSave = function(){

    var name = $.trim($("#base_name").val());
    var firstName = $.trim($("#base_firstName").val());
    var lastName = $.trim($("#base_lastName").val());
    var otherName = $.trim($("#base_otherName").val());
    var sex = $.trim($(":radio[name='sex']:checked").val());

    var degreeName = $.trim($("#base_degreeName").val());
    var birthday = smate.resumepsninfo.base.birth.getDate();
    var regionId = smate.resumepsninfo.base.region.val();
    var regionNames = smate.resumepsninfo.base.region.texts();//国家地区名称
    var position = $("#base_position").val();
    var posId = $("#base_posId").val();
    var post_data = {
        'name': name,
        'firstName': firstName,
        'lastName': lastName,
        'otherName': otherName,
        'sex': sex,
        'degreeName': degreeName,
        'posId': posId,
        'position': position,
        'birthday': birthday,
        'regionId': regionId,
    };
    
    $.proceeding.show();
    $.ajax({
        url: snsctx + '/profile/ajaxSaveBase',
        type: 'post',
        dataType: 'json',
        data: post_data,
        success: function(data){
            if (data.result == 'error') {
                $.smate.scmtips.show('error', data["msg"]);
            } else {
            
                window.location.href = snsctx + "/profile/load";
                /*
                 var _name = $.htmlformat(name);
                 var _firstName = $.htmlformat(firstName);
                 var _lastName = $.htmlformat(lastName);
                 var _full_name = "";
                 if (locale == "zh_CN") {//姓名
                 _full_name = _name == "" ? (_firstName +" "+ _lastName) : _name;
                 } else {
                 _full_name = (_firstName == "" && _lastName == "") ? _name : (_firstName +" "+ _lastName);
                 }
                 
                 var _titolo = $.htmlformat(titolo);//头衔
                 var _position = $.htmlformat(position);//无头衔，取职称
                 var _full_title = _titolo != "" ? _titolo : _position;
                 $("#base_list_text_name").html(_full_name);
                 $("#base_list_text_titolo").html(_full_title == "" ? "" : ("(" + _full_title + ")"));
                 $("#base_list_text_region").html(locale == "zh_CN" ? regionNames.join(" ") : regionNames.reverse().join(" "));
                 */
                smate.resumepsninfo.base.editClose();
            }
            $.proceeding.hide();
        },
        error: function(){
            $.proceeding.hide();
            $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
        }
    });
};

/**
 * 编辑基本信息保存
 */
smate.resumepsninfo.base.editSaveName = function(){

    var name = $.trim($("#base_name").val());
    var firstName = $.trim($("#base_firstName").val());
    var lastName = $.trim($("#base_lastName").val());
  
    var post_data = {
        'name': name,
        'firstName': firstName,
        'lastName': lastName
    };
    
    $.proceeding.show();
    $.ajax({
        url: snsctx + '/profile/ajaxSaveBaseName',
        type: 'post',
        dataType: 'json',
        data: post_data,
        success: function(data){
            if (data.result == 'error') {
                $.smate.scmtips.show('error', data["msg"]);
            } else {
            
                smate.resumepsninfo.base.editCloseName();
                window.location.href = snsctx + "/profile/load";
            }
            $.proceeding.hide();
        },
        error: function(){
            $.proceeding.hide();
            $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
        }
    });
};

/**
 * 编辑基本信息保存
 */
smate.resumepsninfo.base.editSaveMore = function(){

    var otherName = $.trim($("#base_otherName").val());
    var sex = $.trim($(":radio[name='sex']:checked").val());

    var degreeName = $.trim($("#base_degreeName").val());
    var birthday = smate.resumepsninfo.base.birth.getDate();
    var post_data = {
        'otherName': otherName,
        'sex': sex,
        'degreeName': degreeName,
        'birthday': birthday
    };
    
    $.proceeding.show();
    $.ajax({
        url: snsctx + '/profile/ajaxSaveBaseMore',
        type: 'post',
        dataType: 'json',
        data: post_data,
        success: function(data){
            if (data.result == 'error') {
                $.smate.scmtips.show('error', data["msg"]);
            } else {
            
                smate.resumepsninfo.base.editCloseMore();
            }
            $.proceeding.hide();
        },
        error: function(){
            $.proceeding.hide();
            $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
        }
    });
};

/**
 * 编辑基本信息保存
 */
smate.resumepsninfo.base.editSaveLocation = function(){

    var regionId = smate.resumepsninfo.base.region.val();
    var regionNames = smate.resumepsninfo.base.region.texts();//国家地区名称
 
    var post_data = {
        'regionId': regionId
    };
    
    $.proceeding.show();
    $.ajax({
        url: snsctx + '/profile/ajaxSaveBaseLocation',
        type: 'post',
        dataType: 'json',
        data: post_data,
        success: function(data){
            if (data.result == 'error') {
                $.smate.scmtips.show('error', data["msg"]);
            } else {
                smate.resumepsninfo.base.editCloseLocation();
                window.location.href = snsctx + "/profile/load";
            }
            $.proceeding.hide();
        },
        error: function(){
            $.proceeding.hide();
            $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
        }
    });
};

smate.resumepsninfo.base.ajaxpinyin = function(elementId, value){
    if (/^[\u4e00-\u9fa5]+$/i.test(value) && $("#base_lastName").val() == "" && $("#base_firstName").val() == "") {
        $.ajax({
            url: snsctx + '/profile/ajaxPinyin',
            type: 'post',
            dataType: 'json',
            data: {
                'name': value
            },
            success: function(data){
                $("#base_lastName").val(data.lastName);
                $("#base_firstName").val(data.firstName);
            }
        });
    }
};



/**
 * 编辑职称信息关闭
 */
smate.resumepsninfo.position.editClose = function(){
    $("#base_list_text").show();
    $("#position_list_edit_box").remove();
};
/**
 * 编辑头携信息关闭
 */
smate.resumepsninfo.titolo.editClose = function(){
    $("#base_list_text").show();
    $("#titolo_list_edit_box").remove();
};

/**
 * 编辑基本信息关闭
 */
smate.resumepsninfo.base.editClose = function(){
    $("#base_list_text").show();
    $("#base_list_edit_box").remove();
};

smate.resumepsninfo.base.editCloseName = function(){
    $("#base_list_text").show();
    $("#base_list_edit_box_name").remove();
};

smate.resumepsninfo.base.editCloseMore = function(){
    $("#base_list_text").show();
    $("#base_list_edit_box_more").remove();
};

smate.resumepsninfo.base.editCloseLocation = function(){
    $("#base_list_text").show();
    $("#base_list_edit_box_location").remove();
};
/**
 * 编辑基本信息 end
 */
/**
 * 编辑首要简历 start
 */
smate.resumepsninfo.cv = smate.resumepsninfo.cv ? smate.resumepsninfo.cv : {};

/**
 * 检查是否有首要简历
 */
smate.resumepsninfo.cv.check = function(){
    var _des3PsnId = $("#create_a_public_cv").attr("des3PsnId");
    $.ajax({
        url: snsctx + "/personalResume/ajaxPublicCV",
        type: 'post',
        data: {
            "des3PsnId": _des3PsnId
        },
        dataType: 'json',
        success: function(_data){
            if (_data.length > 0) {//已经创建其它语言主页
                if (_data.length == 1) {
                    $("#edit_a_public_cv").click(function(){//编辑简历
                        $.proceeding.show();
                        window.location.href = snsctx + "/personalResume/edit?menuId=1203&resumeId=" + _data[0]["resumeId"];
                    }).show().attr("title", _data[0]["resumeName"]);
                    $("#del_a_public_cv").show().attr("title", _data[0]["resumeName"]);
                } else {
                    $.each(_data, function(){
                        if (this["language"] == locale.replace("_", "-")) {
                            $("#edit_a_public_cv").attr("href", snsctx + "/personalResume/edit?menuId=1203&resumeId=" + this["resumeId"]).show();
                            $("#del_a_public_cv").show();
                        }
                    });
                }
            } else {//未创建其它语言的主页
                $("#create_a_public_cv").thickbox({
                    resmod: resmod
                }).show();
            }
        }
    });
};

/*
 smate.resumepsninfo.cv.createTip = function(){
 //创建简历
 $(":radio[name='language']").click(function(){
 var _this = $(this);
 var _language = $(this).val().replace("_", "-");
 smate.resumepsninfo.cv.add(_language);
 $("#resume_cv_language").hide();
 });
 
 $("#create_a_public_cv,#resume_cv_language").mouseenter(function(){
 
 if (typeof (resumeAddTimeout) != "undefined") {
 clearTimeout(resumeAddTimeout);
 }
 if($(this).attr("id")!="resume_cv_language"){
 var _offset = $(this).offset();
 $("#resume_cv_language").css({"left":_offset["left"]+"px","top":(_offset["top"]+25)+"px"}).fadeIn();
 }
 }).mouseleave(function(){
 
 resumeAddTimeout = setTimeout(function() {
 $("#resume_cv_language").fadeOut();
 }, 400);
 });
 };
 */
/**
 * 加载简历html内容
 */
/*
 smate.resumepsninfo.cv.viewHtml = function(){
 var _cv_content = $("#cv_content");
 var _des3PsnId = _cv_content.attr("des3PsnId");
 
 $("#resume_list_a_create").thickbox({
 "resmod": resmod
 });//初始化弹出框;
 var _init = function(){
 
 };
 
 smate.resumepsninfo.ajaxLoadCv("/personalResume/ajaxLoadCV", _more, _init, {
 "page.ignoreMin": true,
 "page.pageNo": smate.resumepsninfo.pub.pageNo,
 "page.pageSize": 3
 });
 };
 */
/**
 * 添加首要简历
 */
smate.resumepsninfo.cv.add = function(_language, callback){
    var _cvName = "zh-CN" == _language ? "其他语言的主页" : "Other languages";
    var _isPublicCV = "zh-CN" == _language ? 2 : 3;
    var post_data = {
        "styles": "blue_resume",
        "purposeConf": "08",
        "resumeName": _cvName,
        "bodyConf": "01,02,03,04,05,06,07,08,10,11,12",
        "sidebarConf": "03,04,06,09,13,14",
        "language": _language,
        "isPublicCV": _isPublicCV
    };
    $.proceeding.show();
    $.ajax({
        url: snsctx + '/personalResume/add',
        type: 'post',
        dataType: 'json',
        data: post_data,
        success: function(_data){
            if (typeof callback != "undefined") {//
                callback();
            }
            if (_data.result == 'error') {
                $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
            } else {
                window.location.href = snsctx + "/personalResume/edit?menuId=1203&resumeId=" + _data.resumeId;
            }
            
            $.proceeding.hide();
        },
        error: function(){
            $.proceeding.hide();
            $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
        }
    });
};

/**
 * 取消首要简历
 */
smate.resumepsninfo.cv.cancel = function(){

    jConfirm(smate.resumepsninfo.locale["cv_del_tips"], smate.resumepsninfo.locale["cv_confirm_title"], function(r){
        if (r) {
				$.proceeding.show();
            $.ajax({
                url: snsctx + '/personalResume/ajaxCancelCV',
                type: 'post',
                dataType: 'json',
                data: {},
                success: function(_data){
                    $.proceeding.hide();
                    if (_data.result == 'error') {
                        $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
                        return;
                    }
                    window.location.reload();
                },
                error: function(){
                    $.proceeding.hide();
                    $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
                }
            });
        }
    });
};

/**
 * 设置首要简历
 */
smate.resumepsninfo.cv.set = function(_resumeId, _language){
    var _isPublicCV = "zh-CN" == _language ? 2 : 3;
    var post_data = {
        "resumeId": _resumeId,
        "isPublicCV": _isPublicCV
    };
    $.proceeding.show();
    $.ajax({
        url: snsctx + '/personalResume/ajaxSetPublicCV',
        type: 'post',
        dataType: 'json',
        data: post_data,
        success: function(_data){
            if (_data.result == 'error') {
                $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
            } else {
                $.smate.scmtips.show('success', smate.resumepsninfo.locale["save_success"]);
                window.setTimeout(function(){
                    window.location.href = snsctx + "/profile/load";
                }, 2000);
            }
            $.Thickbox.closeWin();
            $.proceeding.hide();
        },
        error: function(){
            $.proceeding.hide();
            $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
        }
    });
};

/**
 * 编辑首要简历 end
 */
/**
 * 编辑联系方式 start
 */
smate.resumepsninfo.contact = smate.resumepsninfo.contact ? smate.resumepsninfo.contact : {};

/**
 * 编辑联系方式html内容
 */
smate.resumepsninfo.contact.editHtml = function(){
    var _contact_list_text = $("#contact_list_text");
    _contact_list_text.hide();
    
    var _edit_box = $("#contact_list_edit_box");
    if (_edit_box.attr("id") == "contact_list_edit_box") {//显示即可
        _edit_box.show();
    } else {//创建
        _edit_box = $('<div id="contact_list_edit_box"></div>');
        var _init = function(){
            $("#email").html($("#contact_list_text_hidden_email").val());
            $("#tel").val($("#contact_list_text_hidden_tel").val());//加载值
            $("#mobile").val($("#contact_list_text_hidden_mobile").val());
            $("#qqNo").val($("#contact_list_text_hidden_qq").val());
            $("#skype").val($("#contact_list_text_hidden_skype").val());
            
            $("#contact_close_a").click(smate.resumepsninfo.contact.editClose);//关闭
            smate.resumepsninfo.contact.editValidate();//初始化校验
				$("#contact_save_a").click(function(){
					$("#contact_form").submit();
				});//保存联系方式
            //邮件设置
            $("#contact_list_email").thickbox({
                "snsctx": snsctx,
                "resmod": resmod,
                "type": "loadEmail"
            });
         var _boxes = _edit_box.find('.selectClass');
         $.each(["Email", "Tel", "Mobile", "Qq", "Skype"], function(index){
				    var _type = this;
					 cnf.contact["setNew"+_type](cnf.contact["get"+_type]());
                _boxes.eq(index).authoritydiv({
                    "locale": locale,
                    "authorityValue": cnf.contact["get"+_type]() 
                },{},function(params,value){
					 cnf.contact["setNew"+_type](value);
				});
            });
       		};
        smate.resumepsninfo.ajaxLoadHtml("editContact.html", _edit_box, _contact_list_text, _init);
    }
};

/**
 * 编辑联系方式校验
 */
smate.resumepsninfo.contact.editValidate = function(){
    $("#contact_form").validate({
        rules: {
            "tel": {
                maxlength: 25
            },
            "mobile": {
                maxlength: 20
            },
            "qqNo": {
                digits: true,
                minlength: 5,
                maxlength: 20
            },
            "skype": {
                maxlength: 50
            }
        },
        messages: {
            "tel": {
                maxlength: $.format(smate.resumepsninfo.locale["contact_tel_max"], 25)
            },
            "mobile": {
                maxlength: $.format(smate.resumepsninfo.locale["contact_mobile_max"], 20)
            },
            "qqNo": {
                digits: smate.resumepsninfo.locale["contact_qq_format"],
                minlength: $.format(smate.resumepsninfo.locale["contact_qq_min"], 5),
                maxlength: $.format(smate.resumepsninfo.locale["contact_qq_max"], 20)
            },
            "skype": {
                maxlength: $.format(smate.resumepsninfo.locale["contact_skype_max"], 50)
            }
        },
        submitHandler: function(){
            smate.resumepsninfo.contact.editSave();
            return false;
        }
    });
};

/**
 * 编辑联系方式保存
 */
smate.resumepsninfo.contact.editSave = function(){
	$("#contact_list_text_hidden_email").val($("#email").text());

    var tel = $.trim($("#tel").val());
    var mobile = $.trim($("#mobile").val());
    var qqNo = $.trim($("#qqNo").val());
    var skype = $.trim($("#skype").val());
    var post_data = {
        'tel': tel,
        'qqNo': qqNo,
        'mobile': mobile,
        'skype': skype,
		  'anyUserEmail':cnf.contact.getNewEmail(),
		  'anyUserTel':cnf.contact.getNewTel(),
		  'anyUserMobile':cnf.contact.getNewMobile(),
		  'anyUserQq':cnf.contact.getNewQq(),
		  'anyUserSkype':cnf.contact.getNewSkype(),
    };
    $.proceeding.show();
    $.ajax({
        url: snsctx + '/profile/ajaxSaveContact',
        type: 'post',
        dataType: 'json',
        data: post_data,
        success: function(data){
            if (data.result == 'error') {
                $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
            } else {
                $("#contact_list_text_hidden_tel").val(tel);
                $("#contact_list_text_hidden_mobile").val(mobile);
                $("#contact_list_text_hidden_qq").val(qqNo);
                $("#contact_list_text_hidden_skype").val(skype);
                
                var _label_empty = smate.resumepsninfo.locale["contact_label_emtpy"];
                var _tel = $.htmlformat(tel);
                var _mobile = $.htmlformat(mobile);
                var _qqNo = $.htmlformat(qqNo);
                var _skype = $.htmlformat(skype);
                $("#contact_list_text_tel").html(_tel == "" ? _label_empty : _tel);
                $("#contact_list_text_mobile").html(_mobile == "" ? _label_empty : _mobile);
                $("#contact_list_text_qq").html(_qqNo == "" ? _label_empty : _qqNo);
                $("#contact_list_text_skype").html(_skype == "" ? _label_empty : _skype);
                if (smate.resumepsninfo.refreshInformation != null) {//刷新信息完整度
                    smate.resumepsninfo.refreshInformation();
                }
               
			   	 cnf.contact.setEmail(cnf.contact.getNewEmail()); 
			   	 cnf.contact.setTel(cnf.contact.getNewTel()); 
			   	 cnf.contact.setMobile(cnf.contact.getNewMobile()); 
			   	 cnf.contact.setQq(cnf.contact.getNewQq()); 
			   	 cnf.contact.setSkype(cnf.contact.getNewSkype()); 
                smate.resumepsninfo.contact.editClose();
            }
            $.proceeding.hide();
        },
        error: function(){
            $.proceeding.hide();
            $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
        }
    });
};

/**
 * 编辑联系方式关闭
 */
smate.resumepsninfo.contact.editClose = function(){
	$("#contact_list_text_hidden_email").val($("#email").text());
    $("#contact_list_text").show();
    $("#contact_list_edit_box").remove();
};

/**
 * 编辑联系方式 end
 */
smate.resumepsninfo.edu = smate.resumepsninfo.edu ? smate.resumepsninfo.edu : {};
/**
 * 编辑教育经历 start
 */
/**
 * 编辑教育经历html内容
 */
smate.resumepsninfo.edu.editHtml = function(_data){
    var _edu_list_text = $("#edu_list_text");
    _edu_list_text.hide();
    
    var _edit_box = $("#edu_list_edit_box");
    if (_edit_box.attr("id") == "edu_list_edit_box") {//显示即可
        _edit_box.show();
        smate.resumepsninfo.edu.editClear();
        if (typeof _data != "undefined") {
            smate.resumepsninfo.edu.editFill(_data);
        }else{
        	//隐藏删除按钮
        	$('#edu_del_a').hide();
        }
    } else {//创建
        _edit_box = $('<div id="edu_list_edit_box"></div>');
        //data = {"text_format":_text_format}
        var _init = function(){
            $("#edu_insName").complete({
                "ctx": snsctx,
                "key": "ins_name",
                "bind": {
                    "code": "edu_insId"
                }
            });//单位自动提示下拉框
            $("#edu_degreeName").complete({
                "ctx": snsctx,
                "key": "degree"
            });//学历自动提示下拉框
            $("#edu_close_a").click(smate.resumepsninfo.edu.editClose);//关闭
            $("#edu_save_a").click(function(){
                $("#edu_form").submit();
            });//保存教育经历
            //初始化年份下拉框
            smate.resumepsninfo.yearOptions(parseInt(smate.resumepsninfo.startYear()), parseInt(smate.resumepsninfo.currentYear()), "edu_fromYear");
            smate.resumepsninfo.yearOptions(parseInt(smate.resumepsninfo.startYear()), parseInt(smate.resumepsninfo.currentYear()) + 7, "edu_toYear");
            
            smate.resumepsninfo.edu.editValidate();//初始化校验
            if (typeof _data != "undefined") {
                smate.resumepsninfo.edu.editFill(_data);
            }else{
            	//隐藏删除按钮
            	$('#edu_del_a').hide();
            }
			var _eduId = (typeof _data == "undefined" || typeof _data["eduId"] == "undefined") ? null : _data["eduId"];
			 cnf.edu.setNew(cnf.edu.get(_eduId));
			_edit_box.find('.selectClass').authoritydiv({
				"locale": locale,
				"authorityValue": cnf.edu.get(_eduId)
			},{},function(params,value){
				 cnf.edu.setNew(value);
			});

			var _top = $("#education_box").offset().top;
		   $(document).scrollTop(_top);
        };
        smate.resumepsninfo.ajaxLoadHtml("editEdu.html", _edit_box, _edu_list_text, _init);
    }
};
/**
 * 编辑教育经历查询内容
 */
smate.resumepsninfo.edu.editData = function(des3Id, dataSetting){
    $.ajax({
        url: snsctx + "/profile/ajaxEditEduHistory",
        type: 'post',
        data: {
            "des3Id": des3Id
        },
        dataType: 'json',
        success: function(_data){
            if (typeof dataSetting != "undefined") {
                dataSetting(_data);
            }
        }
    });
};
/**
 * 编辑教育经历填充内容
 */
smate.resumepsninfo.edu.editFill = function(_data){

    if (_data != null) {
        $("#edu_insName").val(_data["insName"]);
        $("#edu_insId").val(_data["insId"]);
        $("#edu_study").val(_data["study"]);
        $("#edu_degreeName").val(_data["degreeName"]);
        $("#edu_fromYear").val(_data["fromYear"]);
        $("#edu_fromMonth").val(_data["fromMonth"]);
        $("#edu_toYear").val(_data["toYear"]);
        $("#edu_toMonth").val(_data["toMonth"]);
        $("#edu_des3Id").val(_data["des3Id"]);
		$("#edu_descName").val(_data["description"]);
		$('#edu_del_a').click(function(){smate.resumepsninfo.edu.del(_data["des3Id"]);});
        if (_data["isPrimary"] == 1) {
            $("#edu_isPrimary").attr("checked", true);
        }
    }
};

/**
 * 清空教育经历
 */
smate.resumepsninfo.edu.editClear = function(){
    $("#edu_insName").val("");
    $("#edu_insId").val("");
    $("#edu_study").val("");
    $("#edu_degreeName").val("");
    $("#edu_fromYear").val("");
    $("#edu_fromMonth").val("");
    $("#edu_toYear").val("");
    $("#edu_toMonth").val("");
    $("#edu_des3Id").val("");
	$("#edu_descName").val("");
    $("#edu_isPrimary").attr("checked", false);
};
/**
 * 编辑教育经历校验
 */
smate.resumepsninfo.edu.editValidate = function(){
    $("#edu_form").validate({
        rules: {
            "insName": {
                required: true,
                maxlength: 100
            },
            "from_to": {
                edu_from_to_empty: true,
                edu_from_to_equal: true
            },
            "degreeName": {
                maxlength: 50
            }
        },
        messages: {
            "insName": {
                required: smate.resumepsninfo.locale["edu_ins_name_required"],
                maxlength: $.format(smate.resumepsninfo.locale["edu_ins_name_max"])
            },
            "degreeName": {
                maxlength: $.format(smate.resumepsninfo.locale["edu_degree_max"])
            }
        },
        submitHandler: function(){
            smate.resumepsninfo.edu.editSave();
            return false;
        }
    });
    //开始时间、结束时间必须选择
    jQuery.validator.addMethod("edu_from_to_empty", function(value, element){
        var fromYear = $("#edu_fromYear").val();
        if (fromYear == "") 
            return false;
        
        var toYear = $("#edu_toYear").val();
        if (toYear == "") 
            return false;
        
        return true;
    }, smate.resumepsninfo.locale["date_required"]);
    //开始时间，结束时间不能相等
    jQuery.validator.addMethod("edu_from_to_equal", function(value, element){
        return smate.resumepsninfo.edu.editDateRule();
    }, smate.resumepsninfo.locale["data_start_end_rule"]);
    
    $("#edu_toYear").bind("change", function(){
        if (smate.resumepsninfo.edu.editDateRule()) {
            $(".error[for='from_to']").hide();
        }
    });
    $("#edu_fromYear").bind("change", function(){
        if (smate.resumepsninfo.edu.editDateRule()) {
            $(".error[for='from_to']").hide();
        }
    });
    
    $("#edu_toMonth").bind("change", function(){
        var flag = true;
        var fromYear = $("#edu_fromYear").val();
        if (fromYear == "") 
            flag = false;
        var toYear = $("#edu_toYear").val();
        if (toYear == "") 
            flag = false;
        
        if (flag) {
            $(".error[for='from_to']").hide();
        }
    });
    
};
/**
 * 教育经历时间规则
 */
smate.resumepsninfo.edu.editDateRule = function(){
    var fromYear = parseInt($("#edu_fromYear").val());
    var fromMonth = isNaN($("#edu_fromMonth").val()) ? -1 : parseInt($("#edu_fromMonth").val());
    var toYear = parseInt($("#edu_toYear").val());
    var toMonth = isNaN($("#edu_toMonth").val()) ? -1 : parseInt($("#edu_toMonth").val());
    if (fromYear > toYear) 
        return false;
    if (fromMonth > -1 && toMonth > -1 && fromYear == toYear && fromMonth > toMonth) 
        return false;
    return true;
};
/**
 * 删除教育经历
 */
smate.resumepsninfo.edu.del = function(des3Id){
    jConfirm(smate.resumepsninfo.locale["edu_del_tips"], smate.resumepsninfo.locale["tips"], function(r){
        if (r) {
            $.ajax({
                url: snsctx + '/profile/ajaxDeleteEduHistory',
                type: 'post',
                dataType: 'json',
                data: {
                    'des3Id': des3Id
                },
                success: function(data){
//                    smate.resumepsninfo.edu.refresh();
//                    if (smate.resumepsninfo.refreshInformation != null) {//刷新信息完整度
//                        smate.resumepsninfo.refreshInformation();
//                    }
                	 window.location.href=snsctx+"/profile/load";
                }
            });
        }
    });
};
/**
 * 保存教育经历
 */
smate.resumepsninfo.edu.editSave = function(){
    var insName = $.trim($("#edu_insName").val());
    var insId = $.trim($("#edu_insId").val());
    var study = $.trim($("#edu_study").val());
    var degreeName = $.trim($("#edu_degreeName").val());
    var fromYear = $.trim($("#edu_fromYear").val());
    var fromMonth = $.trim($("#edu_fromMonth").val());
    var toYear = $.trim($("#edu_toYear").val());
    var toMonth = $.trim($("#edu_toMonth").val());
    var des3Id = $.trim($("#edu_des3Id").val());
	var description = $.trim($("#edu_descName").val());
    var isPrimary = 0;
    if ($("#edu_isPrimary").is(":checked")) {
        isPrimary = 1;
    }
    var _edit_box = $("#edu_list_edit_box");
    var post_data = {
        'insName': insName,
        'insId': insId,
        'study': study,
        'degreeName': degreeName,
        'fromYear': fromYear,
        'fromMonth': fromMonth,
        'toYear': toYear,
        'toMonth': toMonth,
        'des3Id': des3Id,
        'isPrimary': isPrimary,
		'description':description,
		'anyUser':cnf.edu.getNew()
    };
    $.proceeding.show();
    $.ajax({
        url: snsctx + '/profile/ajaxSaveEduHistory',
        type: 'post',
        dataType: 'json',
        data: post_data,
        success: function(data){
            if (data.result == 'error') {
                $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
            } else {
//                smate.resumepsninfo.edu.refresh();
//                if (smate.resumepsninfo.refreshInformation != null) {//刷新信息完整度
//                    smate.resumepsninfo.refreshInformation();
//                }
            	window.location.href=snsctx+"/profile/load";
				cnf.edu.set(data.eduId,cnf.edu.getNew());
            }
            $.proceeding.hide();
        },
        error: function(){
            $.proceeding.hide();
            $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
        }
    });
    return false;
};


/**
 * 刷新教育经历内容
 */
smate.resumepsninfo.edu.refresh = function(){

    $.ajax({
        url: snsctx + '/profile/ajaxEduHistoryList',
        type: 'post',
        dataType: 'json',
        success: function(data){
        
            var _edit_box = $("#edu_list_edit_box");
            var _eduId = _edit_box.attr("eduId");
            var _compare_result = {
                "type": "modify",
                "eduId": _eduId
            };
        
            var htmlName = (locale == "zh_CN") ? "viewEdu_zh_CN.html" : "viewEdu.html";
            //data = {"text_format":_text_format}
                //教育经历编辑后，填充数据到html中
                smate.resumepsninfo.ajaxLoadHtml(htmlName, null, null, function(_data){
                    var _htmlArr = [];
                    $.each(data, function(){
                        var _start_date = (this["fromYear"]!=null?this["fromYear"]:"") + (this["fromMonth"] != null ? ("/" + this["fromMonth"]) : "");
                        var _end_date = (this["toYear"]!=null?this["toYear"]:"") + (this["toMonth"] != null ? ("/" + this["toMonth"]) : "");
                        var edu_date = _start_date + (_end_date.length > 0 ? "–" : "") + _end_date;
                        
                        var _val = {
                            "edu_ins_name": (this["insName"] != null ? (",&nbsp;" + this["insName"]) : ""),
                            "edu_degree": (this["degreeName"] != null ? (",&nbsp;" + this["degreeName"]) : ""),
                            "edu_study": (this["study"] != null ? (",&nbsp;" + this["study"]) : ""),
                            "edu_des3Id": this["des3Id"],
                            "edu_date": edu_date,
							"edu_descName":(this["description"] != null ? (",&nbsp;" + this["description"]) : "")
                        };
                        var _text_format = $.scmformat(_data["text_format"], _val);//格式化数据
                        _htmlArr.push(_text_format);
                    });
                    
                    $("#edu_list_text").html("<ul>" + _htmlArr.join("") + "</ul>");
                    smate.resumepsninfo.edu.editClose();
                });
        },
        error: function(){
            $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
        }
    });
};
/**
 * 编辑教育经历关闭
 */
smate.resumepsninfo.edu.editClose = function(){
    $("#edu_list_text").show();
    $("#edu_list_edit_box").remove();
};

/**
 * 编辑教育经历 end
 */
/**
 * 编辑工作经历 start
 */
smate.resumepsninfo.work = smate.resumepsninfo.work ? smate.resumepsninfo.work : {};
/**
 * 编辑工作经历html内容
 */
smate.resumepsninfo.work.editHtml = function(_data){
    var _work_list_text = $("#work_list_text");
    _work_list_text.hide();
    
    var _edit_box = $("#work_list_edit_box");
    if (_edit_box.attr("id") == "work_list_edit_box") {//显示即可
        _edit_box.show();
        smate.resumepsninfo.work.editClear();
        if (typeof _data != "undefined") {
            smate.resumepsninfo.work.editFill(_data);
        }else{
        	//隐藏删除按钮
        	$('#work_del_a').hide();
        	$("#work_isActive").unbind();
        	 $("#work_isActive").bind("click", function(){
                 if (this.checked) {
                     $("#work_toYear").attr("disabled", "disabled");
                     $("#work_toMonth").attr("disabled", "disabled");
                     	//头衔
                     //$("#update_titolo").attr("checked",true);
                     //$("#titolo_desc").removeAttr("disabled");
 	                 // $(".update_titolo").show();
 	                  //var titolol = $("#work_insName").val();
 	                  //$("#titolo_desc").val(titolol);
                 } else {
                     $("#work_toYear").removeAttr("disabled");
                     $("#work_toMonth").removeAttr("disabled");
                     //$("#update_titolo").attr("checked",false);
                     //$(".update_titolo").hide();
                 }
             });
        }    
    } else {//创建
        _edit_box = $('<div id="work_list_edit_box"></div>');
        //data = {"text_format":_text_format}
        var _init = function(){
            $("#work_insName").complete({
                "ctx": snsctx,
                "key": "ins_name",
                "bind": {
                    "code": "work_insId"
                }
            });//单位自动提示下拉框
            
			 $("#work_department").complete({
                "ctx": snsctx,
                "key": "insUnit",
				    "formatItem": $.ins_unit_formatItem,
					 "width": 200,
					 "extraParams":{insName:function(){return $.trim($("#work_insName").val());}}
             });//单位院系自动提示下拉框
			
            $("#work_position").complete({
                "ctx": snsctx,
                "key": "position",
                "bind": {
                    "code": "work_posId"
                }
            });//职称自动提示下拉框
         $("#work_close_a").click(smate.resumepsninfo.work.editClose);//关闭
         $("#work_save_a").click(function(){
                $("#work_form").submit();
            });//保存工作经历
            
			 if(typeof _data != "undefined"){
			 	$(".update_titolo").hide();
			 }
	              
            //选择至今
            $("#work_isActive").bind("click", function(){
                if (this.checked) {
                    $("#work_toYear").attr("disabled", "disabled");
                    $("#work_toMonth").attr("disabled", "disabled");
                    	//头衔
                   // $("#update_titolo").attr("checked",true);
                   // $("#titolo_desc").removeAttr("disabled");
 	                //  var titolol = $("#work_insName").val();
 	                 // $("#titolo_desc").val(titolol);
                    //if(typeof _data == "undefined"){
	                 //   	$(".update_titolo").show();
	                 	//	}
                } else {
                    $("#work_toYear").removeAttr("disabled");
                    $("#work_toMonth").removeAttr("disabled");
                   // $("#update_titolo").attr("checked",false);
                  //  $(".update_titolo").hide();
                }
            });
            $("#update_titolo").bind("click", function(){
                if (this.checked) {
                    $("#titolo_desc").removeAttr("disabled");
                 } else {
                        $("#titolo_desc").attr("disabled", "disabled");
                    	}
                	});
            //初始化年份下拉框
            smate.resumepsninfo.yearOptions(parseInt(smate.resumepsninfo.startYear()), parseInt(smate.resumepsninfo.currentYear()), "work_fromYear");
            smate.resumepsninfo.yearOptions(parseInt(smate.resumepsninfo.startYear()), parseInt(smate.resumepsninfo.currentYear()), "work_toYear");
            smate.resumepsninfo.work.editValidate();//初始化校验
            if (typeof _data != "undefined") {
                smate.resumepsninfo.work.editFill(_data);
            }else{
            	//隐藏删除按钮
            	$('#work_del_a').hide();
            }
			
			var _workId = (typeof _data == "undefined" || typeof _data["workId"] == "undefined") ? null : _data["workId"];
			cnf.work.setNew(cnf.work.get(_workId));
			_edit_box.find('.selectClass').authoritydiv({
				"locale": locale,
				"authorityValue": cnf.work.get(_workId)
			},{},function(params,value){
				 cnf.work.setNew(value);
			});
			
			var _top = $("#work_box").offset().top;
		   $(document).scrollTop(_top);
        };
        smate.resumepsninfo.ajaxLoadHtml("editWork.html", _edit_box, _work_list_text, _init);
    }
};

/**
 * 编辑工作经历查询内容
 */
smate.resumepsninfo.work.editData = function(des3Id, dataSetting){
    $.ajax({
        url: snsctx + "/profile/ajaxEditWorkHistory",
        type: 'post',
        data: {
            "des3Id": des3Id
        },
        dataType: 'json',
        success: function(_data){
            if (typeof dataSetting != "undefined") {
                dataSetting(_data);
            }
        }
    });
};
/**
 * 编辑工作经历填充内容
 */
smate.resumepsninfo.work.editFill = function(_data){

    if (_data != null) {
        $("#work_insName").val(_data["insName"]);
        $("#work_insId").val(_data["insId"]);
        $("#work_department").val(_data["department"]);
        $("#work_position").val(_data["position"]);
        $("#work_fromYear").val(_data["fromYear"]);
        $("#work_fromMonth").val(_data["fromMonth"]);
        if (_data["isActive"] == 1) {
            $("#work_isActive").attr("checked", true);
            $("#work_toYear").attr("disabled", "disabled");
            $("#work_toMonth").attr("disabled", "disabled");
        } else {
            $("#work_toYear").val(_data["toYear"]);
            $("#work_toMonth").val(_data["toMonth"]);
        }
        $("#work_des3Id").val(_data["des3Id"]);
        $('#work_del_a').click(function(){smate.resumepsninfo.work.del(_data["des3Id"]);});
        if (_data["isPrimary"] == 1) {
            $("#work_isPrimary").attr("checked", true);
        }
		$("#work_descName").val(_data["description"]);
    }
};

/**
 * 清空工作经历
 */
smate.resumepsninfo.work.editClear = function(){
    $("#work_insName").val("");
    $("#work_insId").val("");
    $("#work_department").val("");
    $("#work_position").val("");
    $("#work_fromYear").val("");
    $("#work_fromMonth").val("");
    $("#work_toYear").val("");
    $("#work_toMonth").val("");
    $("#work_des3Id").val("");
    $("#work_toYear").removeAttr("disabled");
    $("#work_toMonth").removeAttr("disabled");
    $("#work_isPrimary").attr("checked", false);
    $("#work_isActive").attr("checked", false);
	$("#work_descName").val("");
};
/**
 * 编辑工作经历校验
 */
smate.resumepsninfo.work.editValidate = function(){
    $("#work_form").validate({
        rules: {
            "insName": {
                required: true,
                maxlength: 100
            },
            "department": {
                maxlength: 601
            },
            "position": {
                maxlength: 100
            },
            "from_to": {
                work_from_to_empty: true,
                work_from_to_equal: true
            },
            "titolo_desc":{
            work_titolo_required: true,
            maxlength: 50
            }
        },
        messages: {
            "insName": {
                required: smate.resumepsninfo.locale["work_ins_name_required"],
                maxlength: $.format(smate.resumepsninfo.locale["work_ins_name_max"])
            },
            "department": {
                maxlength: $.format(smate.resumepsninfo.locale["work_degree_max"])
            },
            "position": {
                maxlength: $.format(smate.resumepsninfo.locale["work_position_max"])
            },
            "titolo_desc":{
            	maxlength: $.format(smate.resumepsninfo.locale["base_titolo_max"])
           	}
        },
        submitHandler: function(){
            smate.resumepsninfo.work.editSave();
            return false;
        }
    });
    //开始时间、结束时间必须选择
    jQuery.validator.addMethod("work_from_to_empty", function(value, element){
        var fromYear = $("#work_fromYear").val();
        if (fromYear == "") 
            return false;
        
        if ($("#work_isActive").is(":checked")) 
            return true;
        var toYear = $("#work_toYear").val();
        if (toYear == "") 
            return false;
        
        return true;
    }, smate.resumepsninfo.locale["date_required"]);
    //开始时间，结束时间不能相等
    jQuery.validator.addMethod("work_from_to_equal", function(value, element){
        return smate.resumepsninfo.work.editDateRule();
    }, smate.resumepsninfo.locale["data_start_end_rule"]);
    //头衔不为空
    jQuery.validator.addMethod("work_titolo_required", function(value, element){
    	if ($("#work_isActive").is(":checked")&&$("#update_titolo").is(":checked")&&$("#update_titolo").is(":visible")&&$.trim($("#titolo_desc").val()).length==0){
            return false;
    	} else{
    		return true;
    	}
    	
    }, smate.resumepsninfo.locale["base_titolo_required"]);
    
    $("#work_toYear").bind("change", function(){
        if (smate.resumepsninfo.work.editDateRule()) {
            $(".error[for='from_to']").hide();
        }
    });
    $("#work_fromYear").bind("change", function(){
        if (smate.resumepsninfo.work.editDateRule()) {
            $(".error[for='from_to']").hide();
        }
    });
    
    $("#work_isActive").bind("click", function(){
        if (smate.resumepsninfo.work.editDateRule()) {
            $(".error[for='from_to']").html("no error").hide();//奇怪问题，html("no error")不写的话，hide无效
        }
    });
    
    $("#work_toMonth").bind("change", function(){
        var flag = true;
        var fromYear = $("#work_fromYear").val();
        if (fromYear == "") 
            flag = false;
        var toYear = $("#work_toYear").val();
        if (toYear == "") 
            flag = false;
        
        if (flag) {
            $(".error[for='from_to']").hide();
        }
    });
};
/**
 * 工作经历时间规则
 */
smate.resumepsninfo.work.editDateRule = function(){
    if ($("#work_isActive").is(":checked")) 
        return true;
    var fromYear = parseInt($("#work_fromYear").val());
    var fromMonth = isNaN($("#work_fromMonth").val()) ? -1 : parseInt($("#work_fromMonth").val());
    var toYear = parseInt($("#work_toYear").val());
    var toMonth = isNaN($("#work_toMonth").val()) ? -1 : parseInt($("#work_toMonth").val());
    if (fromYear > toYear) 
        return false;
    if (fromMonth > -1 && toMonth > -1 && fromYear == toYear && fromMonth > toMonth) 
        return false;
    return true;
};
/**
 * 删除工作经历
 */
smate.resumepsninfo.work.del = function(des3Id){
    jConfirm(smate.resumepsninfo.locale["work_del_tips"], smate.resumepsninfo.locale["tips"], function(r){
        if (r) {
            $.ajax({
                url: snsctx + '/profile/ajaxDeleteWorkHistory',
                type: 'post',
                dataType: 'json',
                data: {
                    'des3Id': des3Id
                },
                success: function(data){
						  if($("#base_list_work_a").attr("des3Id")==des3Id){
					 			window.location.href=snsctx+"/profile/load";
						  }else{
//							  smate.resumepsninfo.work.refresh();
//							  if (smate.resumepsninfo.refreshInformation != null) {//刷新信息完整度
//							  	smate.resumepsninfo.refreshInformation();
//							  }
							window.location.href=snsctx+"/profile/load";
						  }
                }
            });
        }
    });
};
/**
 * 保存工作经历
 */
smate.resumepsninfo.work.editSave = function(){
    var insName = $.trim($("#work_insName").val());
    var insId = $.trim($("#work_insId").val());
    var posId = $.trim($("#work_posId").val());
    var position = $.trim($("#work_position").val());
    var fromYear = $.trim($("#work_fromYear").val());
    var fromMonth = $.trim($("#work_fromMonth").val());
    var department = $.trim($("#work_department").val());
	 var description = $.trim($("#work_descName").val());
    var isActive = 0;
    var isPrimary = 0;
    var toYear = "";
    var toMonth = "";
    var updateTitle = 0;
    var title = $.trim($("#titolo_desc").val());//头衔
    if ($("#work_isPrimary").is(":checked")) {
        isPrimary = 1;
    }
    if ($("#work_isActive").is(":checked")) {
        isActive = 1;
    } else {
        toYear = $.trim($("#work_toYear").val());
        toMonth = $.trim($("#work_toMonth").val());
    }
    if ($("#update_titolo").is(":checked")&&title!='') {
    	updateTitle = 1;
    } 
    var toYear = $.trim($("#work_toYear").val());
    var toMonth = $.trim($("#work_toMonth").val());
    var des3Id = $.trim($("#work_des3Id").val());
    var _edit_box = $("#work_list_edit_box");
    var post_data = {
        'insName': insName,
        'insId': insId,
        'department': department,
        'posId': posId,
        'position': position,
        'fromYear': fromYear,
        'fromMonth': fromMonth,
        'toYear': toYear,
        'toMonth': toMonth,
        'isActive': isActive,
        'des3Id': des3Id,
        'isPrimary': isPrimary,
		'description': description,
		'updateTitle':updateTitle,
		'title':title,
		'anyUser':cnf.work.getNew()
    };
    
    $.proceeding.show();
    $.ajax({
        url: snsctx + '/profile/ajaxSaveWorkHistory',
        type: 'post',
        dataType: 'json',
        data: post_data,
        success: function(data){
            if (data.result == 'error') {
                $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
            } else {
					 	window.location.href=snsctx+"/profile/load";
            /* smate.resumepsninfo.work.refresh();
                if (smate.resumepsninfo.refreshInformation != null) {//刷新信息完整度
                    smate.resumepsninfo.refreshInformation();
                }*/
           // if(updateTitle==1){
                		//同步页面头衔的显示  tsz
               // 	 $("#base_titlo").text(title);
               // }

				cnf.work.set(data.workId,cnf.work.getNew());
            }
            $.proceeding.hide();
        },
        error: function(){
            $.proceeding.hide();
            $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
        }
    });
    return false;
};

/**
 * 加载工作评价
 */
smate.resumepsninfo.work.appraisal = function(){
    $(".work_exlist").appraisal({
        "key": "work",
        "snsctx": snsctx
    });
};

/**
 * 刷新工作经历内容
 */
smate.resumepsninfo.work.refresh = function(){

    $.ajax({
        url: snsctx + '/profile/ajaxWorkHistoryList',
        type: 'post',
        dataType: 'json',
        success: function(data){
        
            var _edit_box = $("#work_list_edit_box");
            var _workId = _edit_box.attr("workId");
            var _compare_result = {
                "type": "modify",
                "workId": _workId
            };
            var htmlName = (locale == "zh_CN") ? "viewWork_zh_CN.html" : "viewWork.html";
            //data = {"text_format":_text_format}
                //工作经历编辑后，填充数据到html中
                smate.resumepsninfo.ajaxLoadHtml(htmlName, null, null, function(_data){
                    var _htmlArr = [];
                    $.each(data, function(){
                        var _start_date = (this["fromYear"]!=null?this["fromYear"]:"") + (this["fromMonth"] != null ? ("/" + this["fromMonth"]) : "");
                        var _end_date = (this["toYear"]!=null?this["toYear"]:"") + (this["toMonth"] != null ? ("/" + this["toMonth"]) : "");
                        if (this["isActive"] == 1) {
                            _end_date = smate.resumepsninfo.locale["now_title"];
                        }
                        var work_date = _start_date + (_end_date.length > 0 ? "–" : "") + _end_date;
                        
                        var _val = {
                            "work_ins_name": (this["insName"] != null ? (",&nbsp;" + this["insName"]) : ""),
                            "work_department": (this["department"] != null ? (",&nbsp;" + this["department"]) : ""),
                            "work_position": (this["position"] != null ? (",&nbsp;" + this["position"]) : ""),
                            "work_des3Id": this["des3Id"],
                            "work_date": work_date,
							"work_descName":(this["description"] != null ? (",&nbsp;" + this["description"]) : "")
                        };
                        var _keep = smate.resumepsninfo.work.keepData(this["workId"]);
                        $.extend(_val, _keep);//添加工作评价参数
                        var _text_format = $.scmformat(_data["text_format"], _val);//格式化数据
                        _htmlArr.push(_text_format);
                    });
                    
                    $("#work_list_text").html("<ul>" + _htmlArr.join("") + "</ul>");
                    $("#work_list_text").find("ul li:last").css({
                        "border-bottom": "0px"
                    });
                    smate.resumepsninfo.work.appraisal();//加载工作评价
                    smate.resumepsninfo.work.editClose();
                });
        },
        error: function(){
            $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
        }
    });
};

/**
 *
 * 收集工作评价保留的旧参数
 */
smate.resumepsninfo.work.keepData = function(workId){
    var _keep = {
        "workId": "",
        "des3PsnId": "",
        "psnName": ""
    };//初始化，避免加载工作评价异常
    $(".work_exlist").each(function(){
        var _this = $(this);
        var _workId = _this.attr("workId");
        var _des3PsnId = _this.attr("des3PsnId");
        var _psnName = _this.attr("psnName");
        if (_workId != null && _workId.length > 0 && _workId == workId) {
            _keep = {
                "workId": _workId,
                "des3PsnId": _des3PsnId,
                "psnName": _psnName
            };
            return false;
        }
    });
    return _keep;
};
/**
 * 编辑工作经历关闭
 */
smate.resumepsninfo.work.editClose = function(){
    $("#work_list_text").show();
    $("#work_list_edit_box").remove();
};

/**
 * 编辑工作经历 end
 */
/**
 * 编辑研究领域 start
 */
smate.resumepsninfo.key = smate.resumepsninfo.key ? smate.resumepsninfo.key : {};
/**
 * 编辑研究领域html内容
 */
smate.resumepsninfo.key.editHtml = function(){
	smate.resumepsninfo.loadKeywordForEdit(smate.resumepsninfo.key.editInit);
}
/**
 * 研究领域编辑初始化控件或按钮
 */
smate.resumepsninfo.key.editInit = function(){
//	var max = 0;
	var maxLen = 50;
//	var length = $(".keyShow").length + $(".keyHiden").length;
//	var length =  $(".keyHiden").length;
	
//	if(length < maxLen){
//		max = maxLen - length;
//	}
	
	$("#disc_keywords_div").autoword({
        "words_max": maxLen,
        "select": $.auto["key_word"],
        "watermark_flag":true,
        "watermark_tip":smate.resumepsninfo.locale["watermark_tips"],
			"delClick":function(val,label,result){
				var curLength = result.inputCount();
				if( curLength< maxLen){
				   result._input_show();
				}
				var hot_key_list = $(".hot_key_list").find("a");
				for (var i = 0; i < hot_key_list.length; i++){
					if($(hot_key_list[i]).text()==val){
						$(hot_key_list[i]).parent().show();
						break;
					}
				}
			}
    });//自动提示下拉框
    
    $("#key_close_a").click(smate.resumepsninfo.loadKeyword);//关闭
    
	$("#key_save_a").click(function(){
		smate.resumepsninfo.key.editSave();
	});//保存学科代码   

	cnf.expertise.setNew(cnf.expertise.get());
	$("#discipline_box").find('.selectClass').empty().unbind("click").authoritydiv({
		"locale": locale,
		"authorityValue": cnf.expertise.get()
	}, {}, function(params, value){
		cnf.expertise.setNew(value);
	});
	
    smate.resumepsninfo.key.editData();
	
};

/**
 * 研究领域编辑填充数据
 */
smate.resumepsninfo.key.editData = function(){
    //清理数据
    smate.resumepsninfo.key.editClear();
    var _key_count = 0;
    var _index_key = {};
	 var _zhKey = $.autoword["disc_keywords_div"];
	 _zhKey.getDefaults()["words_max"] = 50;
	 // 添加关键词到编辑框
    $(".keyHiden").each(function(index){
			var _trim = $.trim($(this).text());
         if (_trim.length > 0) {
                _zhKey.putAndCount("", _trim,$(this).attr("count"));//添加关键词
                
            }
    });
	smate.resumepsninfo.loadRecommendKeyword();
};

/**
 * 编辑研究领域保存
 */
smate.resumepsninfo.key.editSave = function(){
	var length = $.autoword["disc_keywords_div"].inputCount();
	var maxLen = 50;
	if(length > maxLen){
		$.smate.scmtips.show('warn', i18n_research_limited);
		return false;
	}
    var keysArr = [];
    var keyStr = "";
    var keys = $.autoword["disc_keywords_div"].words();
    $.each(keys, function(){
        keysArr.push({
            'keys': this["text"]
        });
        keyStr = keyStr.concat(this["text"]).concat("; ");
    });

    var str_key = JSON.stringify(keysArr);
    if (str_key == "[]") {//关键词为空
        str_key = '[{"keys":""}]';//区分学科领域与关键词
    }
    var post_data = {
        'strDisc': str_key,
		  'anyUser':cnf.expertise.getNew()
    };
    $.proceeding.show();
    //保存关键词
    $.ajax({
        url: snsctx + '/profile/ajaxSavePersonal',
        type: 'post',
        dataType: 'json',
        data: post_data,
        success: function(data){
            if (data.result == 'error') {
                $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
            } else {
					if (smate.resumepsninfo.refreshInformation != null) {//刷新信息完整度
                  smate.resumepsninfo.refreshInformation();
                        //重新加载基本信息处。 tsz   不是ajax加载 也不应该刷新页面，后台准备数据 直接显示。
//                SCM-6509  修改为不再显示
                  /*                  if(data.baseKey!=null && data.baseKey != ""){
                	  		//初始化基本信息处显示的关键字 html
                	  		
                	  	$("#key_list_text_show").html(data.baseKey+'<a  style="margin-left: 10px" href="/scmwebsns/profile/load?src=discipline&menuId=1200"><i class="icon_edit"></i></a>');
                  }else{
                	  $("#key_list_text_show").text("");
                        }*/
                        
                    }
                cnf.expertise.set(cnf.expertise.getNew());
                smate.resumepsninfo.loadKeyword();
            	}
            $.proceeding.hide();
        },
        error: function(){
            $.proceeding.hide();
            $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
        }
    });
};

/**
 * 清空研究领域
 */
smate.resumepsninfo.key.editClear = function(){
    //清空研究领域
    $.each(["disc_keywords_div"], function(){
        $.autoword[this].clear();
    });
};


/**
 * 编辑研究领域 end
 */
/**
 * 编辑学科领域 start
 */
smate.resumepsninfo.disc = smate.resumepsninfo.disc ? smate.resumepsninfo.disc : {};
/**
 * 编辑学科领域html内容
 */
smate.resumepsninfo.disc.editHtml = function(){
    var _disc_list_text = $("#disc_list_text");
    _disc_list_text.hide();
    
    var _edit_box = $("#disc_list_edit_box");
    if (_edit_box.attr("id") == "disc_list_edit_box") {//显示即可
        _edit_box.show();
        smate.resumepsninfo.disc.editData();
        //比较特殊的处理方式，学科领域不能重复初始化插件，改为这里，重新加载权限插件
        var oldValue = Resume.permissions.get("expertise");
        _edit_box.attr("authority", oldValue);
        _edit_box.find('.selectClass').empty().unbind('click').authoritydiv({
            "locale": locale,
            "authorityValue": oldValue
        }, {}, function(params, value){
            _edit_box.attr("authority", value);
        });
    } else {//创建
        _edit_box = $('<div id="disc_list_edit_box"></div>');
        var _init = function(){
            smate.resumepsninfo.disc.editInit();
            smate.resumepsninfo.disc.editData();
				_edit_box.find('.selectClass').authoritydiv({
					"locale": locale,
					"authorityValue": cnf.expertise.get()
				});
        };
        smate.resumepsninfo.ajaxLoadHtml("editDisc.html", _edit_box, _disc_list_text, _init);
    }
}
/**
 * 学科代码编辑初始化控件或按钮
 */
smate.resumepsninfo.disc.editInit = function(){
	$(".disc_input_class").disccomplete({
        "ctx": snsctx,
        "resmod": resmod,
		"supportExt":true,//是否支持推荐插件
        "allowUserInput":false,//是否允许用户输入，不采用nsfc学科
		"discLevel" : 3,
        "extBind":function(result, type, _close){
				if(type=="save"){
					var _input = $(result["obj"]).prev(".disc_input");
		            if (_input != null ) {
						var index = _input.attr("id").substr(-1,1);
		                var _val = result["val_ext"]();//ext结果集
						if (_val != null) {
							for (var d in $("#disc_list_text").data('mydata')) {
								if (index != d && $("#disc_list_text").data('mydata')[d] == _val["name"]) {//除去用户输入与nsfc学科重复
									$("#discipline_input_" + d + "").val("");
									$("#disc_list_text").data('mydata')[d] = "";
								}
							}
						}
		                _input.attr("code", _val == null ? "" : _val["disc_code"]);
		                _input.val(_val == null ? "" : _val["name"]);
						_input.attr("disc_id", _val == null ? "" : _val["id"]);
        				$("#disc_list_text").data('mydata')[index] = _val == null ? "" : _val["name"];
					}
	        	}
				},
        "discBind":function(result, type, _close){
				if(type=="save"){
					var _input = $(result["obj"]).prev(".disc_input");
		            if (_input != null) {
						var index = _input.attr("id").substr(-1,1);
		                var _val = result["val"]();//disc结果集
						if (_val != null) {
							for (var d in $("#disc_list_text").data('mydata')) {
								if (index != d && $("#disc_list_text").data('mydata')[d] == _val["name"]) {
									$("#discipline_input_" + d + "").val("");
									$("#disc_list_text").data('mydata')[d] = "";
								}
							}
						}
		                _input.attr("code", _val == null ? "" : _val["disc_code"]);
		                _input.val(_val == null ? "" : _val["name"]);
						_input.attr("disc_id", _val == null ? "" : _val["id"]);
        				$("#disc_list_text").data('mydata')[index] = _val == null ? "" : _val["name"];
					}
	        	}
				}
	})
	
    $("#disc_close_a").click(smate.resumepsninfo.disc.editClose);//关闭
    $("#disc_save_a").click(function(){
        Resume.permissions.set("expertise", $("#disc_list_edit_box").attr("authority"));
        Resume.permissions.save(function(){
            smate.resumepsninfo.disc.editSave();
        });
    });//保存学科代码
    //$("#disc_more_a").click(smate.resumepsninfo.disc.editMore);//扩充学科代码和关键词编辑区域
};

/**
 * 清空关键词学科代码
 */
smate.resumepsninfo.disc.editClear = function(){
    $(".disc_input").each(function(){
        var _input = $(this);
        _input.attr("code", "");
        _input.attr("disc_id", "");
        _input.val("");
		_input.next("span").attr("disc_code","");
    });
};
/**
 * 学科代码编辑填充数据
 */
smate.resumepsninfo.disc.editData = function(){
    //清理数据
    smate.resumepsninfo.disc.editClear();
    var _disc_count = 0;
    var _index_disc = {};
	$('#disc_list_text').data('mydata', {});//用于去除用户手动输入但与nsfc学科相同的学科，去除规则是把用户手动输入的去除
    $(".disc_select").each(function(index){//从页面中，加载数据到学科代码编辑界面
        var _this = $(this);
        var _disc_code = _this.attr("disc_code");//学科代码
        var _disc_id = _this.attr("disc_id");//主键
        var _text = $.trim($(this).html());//学科代码名称
        $("#disc_list_text").data('mydata')[index] = _text;
        //if (_disc_code == null || _disc_code.length == 0) {
           // return true;
        //}
        _index_disc[index] = _disc_count;
        var _input = $("#discipline_input_" + _disc_count);
        var _span = $("#discipline_span_" + _disc_count);
		_input.val(_text);//加载到下拉框
        _input.attr("code", _disc_code);
        _input.attr("disc_id", _disc_id);
        _span.attr("disc_code", _disc_code);
        _disc_count++;
    });
    
    //少于3个，默认显示3个
    /*
     $("#disc_list_edit_box tr").each(function(index){
     if (_disc_count < 4 && $.inArray(index, [3, 4]) != -1) {//第4和5个学科代码隐藏
     $(this).hide();
     } else if (_disc_count == 4 && $.inArray(index, [4]) != -1) {//第5个学科代码隐藏
     $(this).hide();
     } else if (_disc_count == 5 && index == 5) {//隐藏添加学科代码按钮
     $(this).hide();
     }
     });
     if (_disc_count != 5 && $("#disc_list_edit_box tr:hidden").length > 0) {//未满5个学科代码，按钮显示
     $("#disc_list_edit_box tr:nth-child(6)").show();
     }
     */
};
/**
 * 扩充学科代码和关键词编辑区域
 */
/*
 smate.resumepsninfo.disc.editMore = function(){
 $("#disc_list_edit_box tr:hidden").each(function(index){
 if (index < 1) {
 $(this).show();
 } else {
 return false;
 }
 });
 if ($("#disc_list_edit_box tr:hidden").length == 0) {//满5个学科代码，按钮隐藏
 $("#disc_list_edit_box tr:nth-child(6)").hide();
 }
 };
 */
/**
 * 关闭学科代码编辑框
 */
smate.resumepsninfo.disc.editClose = function(){
    $("#disc_list_text").show();
    $("#disc_list_edit_box").hide();
};

/**
 * 编辑学科代码保存
 */
smate.resumepsninfo.disc.editSave = function(){

    var discKeysArr = [];
    $(".disc_input").each(function(index){//获取学科代码
        var _this = $(this);
        var _disc_id = _this.attr("disc_id");
        var _disc_text = _this.val();
        var _disc_code = _this.attr("code");
        if (_disc_id.length > 0 && _disc_text.length > 0) {
            discKeysArr.push({
                'disc_id': _disc_id,
                'disc_code': _disc_code,
                'disc_text': _disc_text
            });
        }
    });
    
    var str_disc = JSON.stringify(discKeysArr);
    if (str_disc == "[]") {//没有学科
        str_disc = '[{"disc_id":""}]';//后台区分学科领域与关键词
    }
    var newValue = Resume.permissions.get("expertise");
    var post_data = {
        'strDisc': str_disc,
        'permission':newValue
    };
    
    $.proceeding.show();
    //保存学科代码和关键词
    $.ajax({
        url: snsctx + '/profile/ajaxSavePersonal',
        type: 'post',
        dataType: 'json',
        data: post_data,
        success: function(data){
            if (data.result == 'error') {
                $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
            } else {
                var _htmlArr = [];
                //data = {"text_format":_text_format}
                //学科代码编辑后，填充数据到html中
                _htmlArr.push("<ul>");
                smate.resumepsninfo.ajaxLoadHtml("viewDisc.html", null, null, function(_data){
                	
                    $.each(discKeysArr, function(){
                    	
                        var _disc_id = this["disc_id"];
                        var _disc_code = this["disc_code"];
                        var _disc_text = this["disc_text"];
                        var _head_url ="";
                        //处理学科认同信息
                        $.ajax({
                        		url:snsctx + '/psnView/ajaxEndorseInfo',
                        		type:'post',
                        		dataType:'html',
                        		async:false,
                        	   data:{"discId":_disc_id,"reqType":0},
                        	   success:function(data){
                        		  		_head_url =$.trim(data);
                        	   			},
                        	   error:function(){}
                        		});
                        var _val = {
                            "disc_id": _disc_id,
                            "disc_code": _disc_code,
                            "disc_text": _disc_text,
                            "head_url":_head_url
                        		};
                        var _text_format = $.scmformat(_data["text_format"], _val);//格式化数据
                        _htmlArr.push($.trim(_text_format));
                    });
                    $("#disc_list_text").html(_htmlArr.join(""));
                    if (smate.resumepsninfo.refreshInformation != null) {//刷新信息完整度
                        smate.resumepsninfo.refreshInformation();
                    }
                }); 
              setTimeout(function(){_htmlArr.push("</ul>");
		              //绑定thickbox事件 
		      		$(document.getElementsByName("endorseFriend")).thickbox();
              	},100);
                smate.resumepsninfo.disc.editClose();
            }
            $.proceeding.hide();
        },
        error: function(){
            $.proceeding.hide();
            $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
        }
    });
};

/**
 * 编辑学科领域 end
 */
/**
 * 编辑个人简介 start
 */
smate.resumepsninfo.brief = smate.resumepsninfo.brief ? smate.resumepsninfo.brief : {};
/**
 * 编辑个人简介html内容
 */
smate.resumepsninfo.brief.editHtml = function(){
    var _brief_list_text = $("#brief_list_text");
    _brief_list_text.hide();
    var _edit_box = $("#brief_list_edit_box");
    if (_edit_box.attr("id") == "brief_list_edit_box") {//显示即可
        $("#brief").val($.trim(resumeUtil.outVal($("#brief_list_text > .mywrap").children().html())));//加载值
        _edit_box.show();
    } else {//创建
        _edit_box = $('<div id="brief_list_edit_box"></div>');
        var _init = function(){
            $("#brief").val($.trim(resumeUtil.outVal($("#brief_list_text .mywrap").children().html())));//加载值
            $("#brief_close_a").click(smate.resumepsninfo.brief.editClose);//关闭
            smate.resumepsninfo.brief.editValidate();//初始化校验
            $("#brief_save_a").click(function(){
                $("#brief_form").submit();
                });//保存个人简介
                
		   cnf.brief.setNew(cnf.brief.get());
			_edit_box.find('.selectClass').authoritydiv({
				"locale": locale,
				"authorityValue": cnf.brief.get()
			},{},function(params,value){
				 cnf.brief.setNew(value);
			});
        };
        smate.resumepsninfo.ajaxLoadHtml("editBrief.html", _edit_box, _brief_list_text, _init);
    }
};

/**
 * 编辑个人简介关闭
 */
smate.resumepsninfo.brief.editClose = function(){
    $("#brief_list_text").show();
    $("#brief_list_edit_box").remove();
};
/**
 * 编辑个人简介校验
 */
smate.resumepsninfo.brief.editValidate = function(){
    $("#brief_form").validate({
        rules: {
            "brief": {
                maxlength: 2000
            }
        },
        messages: {
            "brief": {
                maxlength: $.format(smate.resumepsninfo.locale["brief"])
            }
        },
        submitHandler: function(){
            smate.resumepsninfo.brief.editSave();
            return false;
        }
    });
};
/**
 * 编辑个人简介保存
 */
smate.resumepsninfo.brief.editSave = function(){

    var brief = $.trim($("#brief").val());
    var post_data = {
        'brief': resumeUtil.outHtml(brief).replace(/  /gi, "&nbsp; "),'anyUser':cnf.brief.getNew()
    };
    $.proceeding.show();
    $.ajax({
        url: snsctx + '/profile/ajaxSaveBrief',
        type: 'post',
        dataType: 'json',
        data: post_data,
        success: function(data){
            if (data.result == 'error') {
                $.smate.scmtips.show('error', data.msg);
            } else {
                $("#brief_list_text").html("<span class='mywrap'> <span class='myfnt f12 bg '>"+post_data.brief+"</span><span class='myedit'></span></span>");
                if (smate.resumepsninfo.refreshInformation != null) {//刷新信息完整度
                    smate.resumepsninfo.refreshInformation();
                	 }
					 cnf.brief.set(cnf.brief.getNew());
                smate.resumepsninfo.brief.editClose();
            }
            $.proceeding.hide();
        },
        error: function(){
            $.proceeding.hide();
            $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
        }
    });
};

/**
 * 编辑个人简介 end
 */
/**
 * 编辑所教课程 start
 */
smate.resumepsninfo.taught = smate.resumepsninfo.taught ? smate.resumepsninfo.taught : {};
/**_create_word_html
 * 编辑所教课程html内容
 */
smate.resumepsninfo.taught.editHtml = function(){
    var _taught_list_text = $("#taught_list_text");
    //_taught_list_text.hide();
    $("#taught_list_text_block").hide();
    var _edit_box = $("#taught_list_edit_box");
    if (_edit_box.attr("id") == "taught_list_edit_box") {//显示即可
        $("#taught").val($("#taught_list_text_hidden").val());//加载值
        _edit_box.show();
		smate.resumepsninfo.taught.editData();
    } else {//创建
        _edit_box = $('<div id="taught_list_edit_box"></div>');
        var _init = function(){
			$("#taught").autoword({
                "words_max": 50,
            	"filter": ["<", ">", "&", "_", "%"],//过滤特殊字符
                "select": $.auto["taught"]
            });
			$("#taught_close_a").click(smate.resumepsninfo.taught.editClose);//关闭
			smate.resumepsninfo.taught.editValidate();//初始化校验
			$("#taught_save_a").click(function(){
				$("#taught_form").submit();
			});//保存所教课程

	 		cnf.taught.setNew(cnf.taught.get());
			_edit_box.find('.selectClass').authoritydiv({
				"locale": locale,
				"authorityValue": cnf.taught.get()
			},{},function(params,value){
				 cnf.taught.setNew(value);
			});

			smate.resumepsninfo.taught.editData();
        };
        smate.resumepsninfo.ajaxLoadHtml("editTaught.html", _edit_box, _taught_list_text.parent().parent().parent(), _init);
    }
};

/**
 * 清空课程
 */
smate.resumepsninfo.taught.editClear = function(){
    //清空课程
    $.each(["taught"], function(){
        $.autoword[this].clear();
    });
};

/**
 * 课程编辑填充数据
 */
smate.resumepsninfo.taught.editData = function(){
    //清理数据
    smate.resumepsninfo.taught.editClear();
    var _index_key = {};
    var _keysArr = $.valformat($("#taught_list_text").html()).replace(/&amp;/gi, "&").split(";");
    var _zhKey = $.autoword["taught"];
    $.each(_keysArr, function(){
        var _trim = $.trim(this);
        if (_trim.length > 0) {
            _zhKey.put("", _trim);//添加课程
        }
    });
};

/**
 * 编辑所教课程关闭
 */
smate.resumepsninfo.taught.editClose = function(){
    $("#taught_list_text_block").show();
    $("#taught_list_edit_box").remove();
};
/**
 * 编辑所教课程校验
 */
smate.resumepsninfo.taught.editValidate = function(){
    $("#taught_form").validate({
        rules: {
            "taught": {
                maxlength: 2500
            }
        },
        messages: {
            "taught": {
                maxlength: $.format(smate.resumepsninfo.locale["taught"], 2500)
            }
        },
        submitHandler: function(){
            smate.resumepsninfo.taught.editSave();
            return false;
        }
    });
};
/**
 * 编辑所教课程保存
 */
smate.resumepsninfo.taught.editSave = function(){
    var str = new String();
    var taughts = $.autoword["taught"].words();	
    $.each(taughts, function(index){
		var formatStr = this["text"];//部分英文课程含有特殊字符&
        str = str.concat(formatStr);
		if(index != taughts.length-1){
			str = str.concat("; ")
		}
    });
    var post_data = {
        'content': str,'anyUser':cnf.taught.getNew()
    };
    $.proceeding.show();
    $.ajax({
        url: snsctx + '/profile/ajaxSaveTaught',
        type: 'post',
        dataType: 'json',
        data: post_data,
        success: function(data){
            if (data.result == 'error') {
                $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
            } else {
                $("#taught_list_text").html($.htmlformat(str));
                if (smate.resumepsninfo.refreshInformation != null) {//刷新信息完整度
                    smate.resumepsninfo.refreshInformation();
                	 }
					 cnf.taught.set(cnf.taught.getNew());
                smate.resumepsninfo.taught.editClose();
            }
            $.proceeding.hide();
        },
        error: function(){
            $.proceeding.hide();
            $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
        }
    });
};

/**
 * 编辑所教课程 end
 */
/**
 * 加载项目简表 start
 */
smate.resumepsninfo.prj = smate.resumepsninfo.prj ? smate.resumepsninfo.prj : {};

//当前页面
smate.resumepsninfo.prj.pageNo = 1;

/**
 * 加载项目简表html内容
 */
smate.resumepsninfo.prj.viewHtml = function(isFirstLoad){
    var _prj_list_text = $("#prj_list_text");
    var _more = _prj_list_text.find(".tmp_more");
    var _init = function(isMore, _nextPageNo){
        smate.resumepsninfo.prj.pageNo = _nextPageNo;
        if (!isMore) {
            _more.hide();
        }
    };
    //page.ignoreMin=true忽略最小分页限制
    smate.resumepsninfo.ajaxLoadJsp("/project/ajaxMaint", _more, _init, {
        "page.ignoreMin": true,
        "page.pageNo": smate.resumepsninfo.prj.pageNo,
        "page.pageSize": 10
    },isFirstLoad);
};

/**
 * 跳转到我的项目页面
 */
smate.resumepsninfo.prj.maint = function(){
    window.location.href = snsctx + "/project/maint";
};
/**
 * 加载项目简表 end
 */
/**
 * 加载成果简表 start
 */
smate.resumepsninfo.pub = smate.resumepsninfo.pub ? smate.resumepsninfo.pub : {};

//当前页面
smate.resumepsninfo.pub.pageNo = 1;

/**
 * 加载成果简表html内容
 */
smate.resumepsninfo.pub.viewHtml = function(isFirstLoad){
    var _pub_list_text = $("#pub_list_text");
    var _more = _pub_list_text.find(".tmp_more");
    var _init = function(isMore, _nextPageNo){
        smate.resumepsninfo.pub.pageNo = _nextPageNo;
        if (!isMore) {
            _more.hide();
        }
    };
    //page.ignoreMin=true忽略最小分页限制
    smate.resumepsninfo.ajaxLoadJsp("/publication/ajaxMaint", _more, _init, {
        "page.ignoreMin": true,
        "page.pageNo": smate.resumepsninfo.pub.pageNo,
        "page.pageSize": 10
    },isFirstLoad);
};

/**
 * 跳转到我的成果页面
 */
smate.resumepsninfo.pub.maint = function(){
    window.location.href = snsctx + "/publication/maint";
};
/**
 * 加载成果简表 end
 */
/**
 * 编辑个人主页url start
 */
smate.resumepsninfo.profilePage = smate.resumepsninfo.profilePage ? smate.resumepsninfo.profilePage : {};
/**
 * 编辑个人主页urlhtml内容
 */
smate.resumepsninfo.profilePage.editHtml = function(){
    var _profilePage_list_text = $("#profilePage_list_text").hide();
    
    if ($("#profilePage_list_edit_box").attr("id") != "profilePage_list_edit_box") {//显示即可
        var _path = $.trim($("#profilePage_path").html());
        var _name = $.trim($("#profilePage_name").html());
        var _html = $.loadHtml("/home/resume/html_template/editProfilePage.html", {
            "profilePage_path": _path
        });
        var _text_format = $.scmformat(_html, $.extend({
            "profilePage_name": _name
        }, smate.resumepsninfo.locale));//格式化数据
        _profilePage_list_text.before($('<div id="profilePage_list_edit_box" style="padding: 0px 10px;"></div>').append(_text_format));
        $("#profilePage_close_a").click(smate.resumepsninfo.profilePage.editClose);//关闭
        smate.resumepsninfo.profilePage.editValidate();//初始化校验
        $("#profilePage_save_a").click(function(){
            $("#profilePage_form").submit();
        });//保存个人主页url
        $("#profilePage").bind("keyup", function(){
            var value = this.value;
            value = value.replace(/\W/gi, '');
            this.value = value;
            $(".error[for=profilePage_form]").hide();
        });
    }
};

/**
 * 编辑个人主页url关闭
 */
smate.resumepsninfo.profilePage.editClose = function(){
    $("#profilePage_list_text").show();
    $("#profilePage_list_edit_box").remove();
};
/**
 * 编辑个人主页url校验
 */
smate.resumepsninfo.profilePage.editValidate = function(){
    $("#profilePage_form").validate({
        rules: {
            "profilePage": {
                required: true,
                char_number: true
            }
        },
        messages: {
            "profilePage": {
                required: $.format(smate.resumepsninfo.locale["page_required"])
            }
        },
        errorPlacement: function(error, element){
            $(element).parent().next().append(error);
        },
        submitHandler: function(){
            smate.resumepsninfo.profilePage.editSave();
            return false;
        }
    });
    //只能输入字母及数字
    jQuery.validator.addMethod("char_number", function(value, element){
        var urlValue = $("#profilePage").val();
        var reg = /^([a-zA-Z0-9]+)$/;
        return this.optional(element) || reg.test(urlValue);
    }, smate.resumepsninfo.locale["letters_number"]);
};

/**
 * 编辑个人主页url保存
 */
smate.resumepsninfo.profilePage.editSave = function(){

    var profilePage = $.trim($("#profilePage").val()).toLowerCase();
    if ($("#profilePage_name").html() == profilePage) {
        $.smate.scmtips.show('warn', smate.resumepsninfo.locale["no_change"]);
        return;
    }
    var post_data = {
        'url': profilePage
    };
    $.proceeding.show();
    $.ajax({
        url: snsctx + '/profile/ajaxProfilePage',
        type: 'post',
        dataType: 'json',
        data: post_data,
        success: function(data){
            if (data.result == 'error') {
                $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
            } else if (data.result == 'exist') {
                $.smate.scmtips.show('error', smate.resumepsninfo.locale["page_exist"]);
            } else {
                $("#profilePage_name").html(profilePage);
                //$("#profilePage_link").attr("href", $("#profilePage_path").html() + profilePage);
                smate.resumepsninfo.profilePage.editClose();
            }
            $.proceeding.hide();
        },
        error: function(){
            $.proceeding.hide();
            $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
        }
    });
};

/**
 * 编辑个人主页url end
 */
smate.resumepsninfo.ajaxLoadHtml = function(name, obj, parent, init){//ajax加载Html
    $.ajax({
        url: resmod + "/js_v5/home/resume/html_template/" + name,
        type: 'post',
        dataType: 'html',
        success: function(_text){
            var _filterText = smate.resumepsninfo.htmlFilter(_text);
            var _text_format = $.scmformat(_filterText, smate.resumepsninfo.locale);//格式化数据
            if (obj != null) {
                obj.html(_text_format);
            }
            if (parent != null) {
                parent.before(obj);//插入格式化后的内容
            }
            
            if (typeof init != "undefined") {
                init({
                    "text_format": _text_format
                });
            }
        }
    });
};

//成果、项目
smate.resumepsninfo.ajaxLoadJsp = function(name, parent, init, post_data,isFirstLoad){//ajax加载jsp
    if(typeof isFirstLoad == "undefined" || !isFirstLoad){
		$.proceeding.show();
	 }
	 
    $.ajax({
        url: snsctx + name,
        type: 'post',
        data: post_data,
        dataType: 'html',
        success: function(_text){
        	if (_text.indexOf('"ajaxSessionTimeOut":"yes"')>0) {
        			$.proceeding.hide();
			   	jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r){
			   		if (r) {
			   			document.location.reload();
			   		}
			   	});
        	}else{
			   if (typeof isFirstLoad == "undefined" || !isFirstLoad) {
					$.proceeding.hide();
				}
            var _filterText = smate.resumepsninfo.htmlFilter(_text);
            var _html = $(_filterText);
            var _totalCount = _html.attr("totalCount");
            var _nextPageNo = _html.attr("nextPageNo");
            parent.before(_html);//插入格式化后的内容
            var _first = _html.parent().find(".tmp_item_count").size();
            if (typeof init != "undefined") {
                init(!isNaN(_totalCount) && !isNaN(_first) && parseInt(_totalCount) > parseInt(_first), _nextPageNo);
                }
				
				//全文下载
			   $(".notPrintLinkSpan").fullTextRequest();

			   if("/project/ajaxMaint"==name){
				   resShareUtil=new ResShareUtil();
		        	resShareUtil.ajaxBatchLoadShareCount(4,0);
					//赞
		        	Award.iniAward(4);
					//评论
					$(_text).find(".cls_comment_prj").each(function(){
						$("#"+$(this).attr("id")).thickbox({
							snsctx : snsctx,
							resmod : resmod,
							parameters:{"prjId":$(this).attr("prjId")},
							type : "projectComment"
						});
					});
					
					// 分享下拉模式
					$(".share_pull_prj").sharePullMode({
						showSharePage:function(_this){
							Resume.psnView.shareProject(_this);
						}
					});
					  //初始化项目点击事件
					   if(typeof minePagePrjsAction != "undefined" &&typeof minePagePrjsAction =="function"){
						   minePagePrjsAction();
					   }
			   }else if("/publication/ajaxMaint"==name){
				 //分享初始化
					resShareUtil=new ResShareUtil();
					resShareUtil.ajaxBatchLoadShareCount(1,0);
					//赞
					Award.iniAward(1);
					//评论
					$(_text).find(".cls_comment_pub").each(function(){
						$("#"+$(this).attr("id")).thickbox({
							snsctx : snsctx,
							resmod : resmod,
							parameters:{"pubId":$(this).attr("pubId"),"actionType":"1"},
							type : "publicationComment"
						});
					});
					
					// 分享下拉模式
					$(".share_pull_pub").sharePullMode({
						showSharePage:function(_this){
							Resume.psnView.sharePublication(_this);
						}
					});
					 //初始化成果点击事件
					   if(typeof minePagePubsAction != "undefined" &&typeof minePagePubsAction =="function"){
						   minePagePubsAction();
					   }
			   }
			  
        	}
			   
        },error:function(){
			    if (typeof isFirstLoad == "undefined" || !isFirstLoad) {
					$.proceeding.hide();
				}
		  }
    });
};

//简历
smate.resumepsninfo.ajaxLoadCv = function(name, parent, init, post_data){//ajax加载jsp
    $.ajax({
        url: snsctx + name,
        type: 'post',
        data: post_data,
        dataType: 'html',
        success: function(_text){
            var _filterText = smate.resumepsninfo.htmlFilter(_text);
            var _html = $(_filterText);
            var _totalCount = _html.attr("totalCount");
            var _nextPageNo = _html.attr("nextPageNo");
            parent.before(_html);//插入格式化后的内容
            if (typeof init != "undefshare_pullined") {
                init();
            }
        }
    });
};

smate.resumepsninfo.htmlFilter = function(_text){//过滤不必要的内容，仅保留body之间的内容
    var _bodyStart = _text.indexOf("<body>");
    var _bodyEnd = _text.indexOf("</body>");
    if (_bodyStart > 0 && _bodyEnd > 0) {
        return _text.substring(_bodyStart + 6, _bodyEnd);
    }
    return _text;
};

//删除个人资料首条横线
smate.resumepsninfo.removeTopLine = function(){
    $(".info_n01").each(function(index){
        var _this = $(this);
        if (index == 0) {
            _this.removeClass("left-topline2");
        } else if (!_this.hasClass("left-topline2")) {
            _this.addClass("left-topline2");
        }
    });
};

smate.resumepsninfo.isChangeDefault = function(){
	return false;
};

//预览
smate.resumepsninfo.preview = function(e){
	$("#person_role_preview").show();
};

//默认未发生改变，弹出窗口调用后改变函数
smate.resumepsninfo.isChange = function(){
	return false;
};

$(function(){
    $("#avatar_list_a,#avatar_list_a2").click(function(){
        smate.resumepsninfo.base.avatar();//编辑头像html内容
    });
    
    //$("#base_list_a").click(function(){
     //   smate.resumepsninfo.base.editHtml();//编辑基本信息html内容
    //});
    
   $("#base_list_a_name").click(function(){
        smate.resumepsninfo.base.editHtmlName();//编辑基本信息html内容:姓名
    });

   $("#base_list_a_location").click(function(){
        smate.resumepsninfo.base.editHtmlLocation();//编辑基本信息html内容:国家地区
    });
	
   $("#base_list_a_more").click(function(){
        smate.resumepsninfo.base.editHtmlMore();//编辑基本信息html内容:更多
    });

    //$("#base_list_titolo_a").click(function(){
     //   smate.resumepsninfo.titolo.editHtml();//编辑头携信息html内容
    //});
    
   // $("#base_list_position_a").click(function(){
    //    smate.resumepsninfo.position.editHtml();//编辑头携信息html内容
    //});
    
    
    $("#page_list_a").click(function(){
        smate.resumepsninfo.profilePage.editHtml();//编辑主页url html内容
    });

    $("#contact_list_a").click(function(){
        smate.resumepsninfo.contact.editHtml();//编辑联系方式html内容
    });
    
    $("#edu_list_a").click(function(){
        smate.resumepsninfo.edu.editHtml();//编辑教育经历html内容
    });
    
    
    $(".tmp_Edu_Edit").delegate("click", function(e){//点击教育经历编辑链接
        var des3Id = $(this).attr("des3Id");
        smate.resumepsninfo.edu.editData(des3Id, function(data){
            //填充数据
            smate.resumepsninfo.edu.editHtml(data);
        });
        return false;//防止事件冒泡
    });
    
    $("#base_list_edu_a").click(function(e){//点击工作经历编辑链接
    	 var des3Id = $(this).attr("des3Id");
         smate.resumepsninfo.edu.editData(des3Id, function(data){
             //填充数据
             smate.resumepsninfo.edu.editHtml(data);
         });
         return false;//防止事件冒泡
    });
    
    $(".tmp_Edu_Del").delegate("click", function(e){//点击教育经历删除链接
        var des3Id = $(this).attr("des3Id");
        smate.resumepsninfo.edu.del(des3Id);
        return false;//防止事件冒泡
    });
    
    $("#work_list_a,#work_base_a").click(function(){
        smate.resumepsninfo.work.editHtml();//编辑工作经历html内容
    });
    
    $(".tmp_Work_Edit").delegate("click", function(e){//点击工作经历编辑链接
        var des3Id = $(this).attr("des3Id");
        smate.resumepsninfo.work.editData(des3Id, function(data){
            //填充数据
            smate.resumepsninfo.work.editHtml(data);
        });
        return false;//防止事件冒泡
    });
    
	$("#base_list_work_a").click(function(e){//点击工作经历编辑链接
        var des3Id = $(this).attr("des3Id");
        smate.resumepsninfo.work.editData(des3Id, function(data){
            //填充数据
            smate.resumepsninfo.work.editHtml(data);
           });
        return false;//防止事件冒泡
    });
	
    $(".tmp_Work_Del").delegate("click", function(e){//点击工作经历删除链接
        var des3Id = $(this).attr("des3Id");
        smate.resumepsninfo.work.del(des3Id);
        return false;//防止事件冒泡
    });
    
    $("#key_list_a").click(function(){
        smate.resumepsninfo.key.editHtml();//编辑关键词html内容
    });
    
    $("#disc_list_a").click(function(){
        smate.resumepsninfo.disc.editHtml();//编辑学科领域html内容
    });
    
    $("#brief_list_a").click(function(){
        smate.resumepsninfo.brief.editHtml();//编辑个人简介html内容
    });
    
    $("#taught_list_a").click(function(){
        smate.resumepsninfo.taught.editHtml();//编辑所教课程html内容
    });
    
    smate.resumepsninfo.work.appraisal();//加载工作评价
    //项目start
    smate.resumepsninfo.prj.viewHtml(true);//加载项目简表
    $("#prj_list_more").click(function(){
        smate.resumepsninfo.prj.viewHtml();//加载更多项目
    });
    
    //项目end
    
    //成果start
    smate.resumepsninfo.pub.viewHtml(true);//加载成果简表
    $("#pub_list_more").click(function(){
        smate.resumepsninfo.pub.viewHtml();//加载更多成果
    });
    //成果end

	$("#hidden-share,#hidden-shareLink,#hidden-awardLink,#resumeShareBtn,#workComment,#hidden_attach_request,#detail_import_title").thickbox({
		snsctx : snsctx,
		resmod : resmod
	});
    //成果 项目
    $("#prj_list_a,#pub_list_a").thickbox({
        "resmod": resmod,
		"closeEvent": function(tbRemove){
            if (smate.resumepsninfo.isChange()) { //内容发生变化，提示用户是否放弃修改
               
                jConfirm(smate.resumepsninfo.locale["confirm_not_modify"], smate.resumepsninfo.locale["confirm_tip"], function(sure){
                    if (sure == true) {
                        tbRemove();
                    }
                });
            }else{//未发生变化，不提示
				 tbRemove();
			}
		}
    });
	
	$(".del_picture").delegate("click",function(){
		var _this = $(this);
		var anyMod = cnfJSON.cnfMoudle.anyMod;
		var anyModNew = anyMod & parseInt(_this.attr("mod"));
		//修改模块查看权限
	   $.ajax({
	        url: snsctx+'/profile/ajaxSaveMod',
	        type: 'post',
	        dataType: 'json',
	        data: {anyMod:anyModNew},
	        success: function(data){
				window.location.href=snsctx+"/profile/load";
	        },
	      error: function(){
				window.location.href=snsctx+"/profile/load";
	        }
		});
	});
    
    //简历start
    
    /*
     $("#cv_a_create_hascv_zh_CN,#cv_a_create_hascv_en_US").thickbox({
     resmod: resmod
     });
     */
    //初始化弹出框
    //添加首要中文简历
    /*
     $("#cv_a_create_zh_CN").click(function(){
     smate.resumepsninfo.cv.add("zh-CN");
     });
     
     //添加首要英文简历
     $("#cv_a_create_en_US").click(function(){
     smate.resumepsninfo.cv.add("en-US");
     });
     */
    //添加其它语言的简历
    /*
     $("#cv_a_other").click(function(){
     var _cv_create_type = $(this).attr("cv_create_type");
     $("#" + _cv_create_type).click();
     });
     */
    //编辑首要简历
    /*
     $("#cv_a_edit_zh_CN,#cv_a_edit_en_US").click(function(){
     var _resumeId = $(this).attr("resumeId");
     window.location.href = snsctx + "/personalResume/edit?menuId=1203&resumeId=" + _resumeId;
     });
     */
    //取消首要简历
    /*
     $("#cv_a_cancel_zh_CN,#cv_a_cancel_en_US").click(function(){
     var _resumeId = $(this).attr("resumeId");
     smate.resumepsninfo.cv.cancel(_resumeId);
     });
     */
    $("#del_a_public_cv").click(smate.resumepsninfo.cv.cancel);
    
    //个人简介格式化
    var _tmp_text = $("#brief_list_text_hidden").val();
    if (typeof _tmp_text != "undefined") {
        _tmp_text = _tmp_text.replace(/&/gi, "&amp;").replace(/</gi, "&lt;").replace(/>/gi, "&gt;").replace(/\n/gi, "<br>").replace(/"/gi, "&quot;");
        $("#brief_list_text").html(_tmp_text);
    }
    
    //所教课堂格式化
    $("#taught_list_text").html($.htmlformat($("#taught_list_text_hidden").val()));
    
    smate.resumepsninfo.cv.check();//检查是否有首要简历
    //简历end
    
    //上下移动插件
    $(".move_div").movediv({
        resmod: resmod,
        "target": "info_n01",
       // params: resuemConfigsParams,
        "callback": smate.resumepsninfo.moveCallback
    });
    
    $("#module_list_a,#module_list_a2").thickbox({
        snsctx: snsctx,
        resmod: resmod,
        type: "loadAdjusting"
    });
});

smate.resumepsninfo.bindMoveEvent = function(obj){
	obj.movediv({
        resmod: resmod,
        "target": "info_n01",
        //params: resuemConfigsParams,
        "callback": smate.resumepsninfo.moveCallback
    });
};

smate.resumepsninfo.moveCallback = function(params, moveFun, obj){
            var _way = moveFun() == 'up' ? 'next' : 'prev';
            var target_div = obj.closest(".info_n01");
            var target_obj = target_div[_way]().find(".move_div");//移动标签的位置.
            var objSeq = obj.attr("seqNum")
            var targetSeq = target_obj.attr("seqNum")
            obj.attr("seqNum", targetSeq);
            target_obj.attr("seqNum", objSeq);
            $.each(params, function(){
                $.each(this, function(){
                    if (this["seqNum"] == objSeq) {
                        this["seqNum"] = targetSeq;
                    } else if (this["seqNum"] == targetSeq) {
                        this["seqNum"] = objSeq;
                    }
                });
            });
			

		//修改模块顺序
		$.ajax({
			url: snsctx + '/profile/ajaxSaveMod',
			type: 'post',
			dataType: 'json',
			data: {"seqNo1":objSeq,"seqNo2":targetSeq},
			success: function(data){
				if (data.result == "success") {
					smate.resumepsninfo.removeTopLine();
				} else if (data.result == "error") {
					$.smate.scmtips.show('error', data.msg);
					window.setTimeout(function(){
						window.location.reload();
					}, 2000);
				}
			},
			error: function(){
				window.location.href = snsctx + "/profile/load";
			}
		});
          
  }

smate.resumepsninfo.loadKeywordForEdit = function(callback){
	smate.resumepsninfo.ajaxLoadKeyword(callback,"/psnView/ajaxKeywordForEdit");
};

smate.resumepsninfo.loadKeyword = function(callback){
	smate.resumepsninfo.ajaxLoadKeyword(callback,"/psnView/ajaxKeyword");
};

smate.resumepsninfo.ajaxLoadKeyword = function (callback,url){
	// 加载研究领域认同关键词
	$.ajax({
		url: snsctx + url,
		type: 'get',
		dataType: 'html',
		cache:false,
		data: {},
		success: function(data){
			if (data) {
				if (data == '{"ajaxSessionTimeOut":"yes"}') {
					window.location.reload();
				} else {
					$("#discipline_box").empty();
					$("#discipline_box").html(data);
				}
				if($.isFunction(callback)){
					callback();
				}
				// 绑定事件
				smate.resumepsninfo.bindMoveEvent($("#keywordMoveDiv"));
				$("#keywordMoveDiv").attr("seqNum",$("#discipline_box").attr("seqNum"));
				$("#keywordDelDiv").attr("mod",$("#discipline_box").attr("mod"));
				$(".endorsers-action2").thickbox();
			} else {
				alert('error');
			}
		},
		error: function(){
			// 超时
			alert(i18n_timeout);
			var url = document.location.href;
			url = url.replace('#', '');
			document.location.href = url;
		}
	});
};

smate.resumepsninfo.delIdentifKeyword = function(idStr,keyword){
	$.ajax({
		url: snsctx + "/psnView/ajaxDelIdentifKeyword",
		type: 'post',
		dataType: 'json',
		data: {"keywordId":idStr,"keyword":keyword},
		success: function(data){
			if (data) {
				if (data == '{"ajaxSessionTimeOut":"yes"}') {
					window.location.reload();
				} else {
					if(data.result == 'success'){
						$("#keyword_"+idStr).remove();
					}
				}
			} else {
				alert('error');
			}
		},
		error: function(){
			// 超时
			alert(i18n_timeout);
			var url = document.location.href;
			url = url.replace('#', '');
			document.location.href = url;
		}
	});
};
/**
 * 加载推荐关键词
 */
smate.resumepsninfo.loadRecommendKeyword = function(refreshTimes){
	if(!refreshTimes){
		refreshTimes = 0;
	}
	$.ajax({
		url: snsctx + "/psnView/ajaxRecommendKw",
		type: 'post',
		dataType: 'html',
		data: {"refresh":refreshTimes},
		success: function(data){
			if (data == '{"ajaxSessionTimeOut":"yes"}') {
					window.location.reload();
				} else {
					$("#recommend_keyword_div").empty();
					$("#recommend_keyword_div").html(data);
					setTimeout(function(){smate.resumepsninfo.loadRecommendKeywordHide(refreshTimes+1);},100);
				}
		},
		error: function(){
			// 超时
			alert(i18n_timeout);
			var url = document.location.href;
			url = url.replace('#', '');
			document.location.href = url;
		}
	});
};
//加载下一组＂十个关键词＂
smate.resumepsninfo.loadRecommendKeywordHide = function(refreshTimes){
	if(!refreshTimes){
		return;
	}
	$.ajax({
		url: snsctx + "/psnView/ajaxRecommendKw2",
		type: 'post',
		dataType: 'json',
		data: {"refresh":refreshTimes},
		success: function(data){
			if (data == '{"ajaxSessionTimeOut":"yes"}') {
					window.location.reload();
				} else if(data.result =="success" &&data.recommendList!=null) {
					
					var strList = data.recommendList;
					
					for(var size=0;size<strList.length;size++){
						var arr = "";
						arr +='<div class="hot_01 hot_01_node" refreshTimes="'+(refreshTimes+1)+'" style="display:none;" onclick="smate.resumepsninfo.setRecommendKeyword(this)">';
						arr +='<i class="addfriends"/>';
						arr +='<a href="javascript:;">'+strList[size]+'</a>';
						arr +='</div>';
						$(".hot_key_list").append(arr);
					}
				}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
			// 超时
			alert(i18n_timeout);
			var url = document.location.href;
			url = url.replace('#', '');
			document.location.href = url;
		}
	});
};

smate.resumepsninfo.setRecommendKeyword = function(obj){
	var kw = $(obj).text();
	var length = $.autoword["disc_keywords_div"].inputCount();
	if(length < 50){
		$.autoword["disc_keywords_div"].put(kw,kw);
		//删除自身
		obj.parentNode.removeChild(obj);
		//补充满十个
		$(".hot_01_node:first").slideDown();
		$(".hot_01_node:first").attr("class","hot_01");
		
		//隐藏的关键词不够一个时，向服务器再请求十个
		if($(".hot_01_node").size()<=1){
			smate.resumepsninfo.loadRecommendKeywordHide(parseInt($(".hot_01_node").attr("refreshTimes")));
		}
	}
};

smate.resumepsninfo.viewMoreAndLess = function(obj){
	
	if($(obj).attr("action")==0){
		$(".notExistsClass").slideDown("fast");
		$(obj).attr("action",1);
		obj.innerHTML=smate.resumepsninfo.locale["disc_rmc_hide"]+'<i class="shear_head-up">';
		
	}else{
		$(".notExistsClass").slideUp("fast");
		$(obj).attr("action",0);
		obj.innerHTML=smate.resumepsninfo.locale["disc_rmc_viewMore"]+'<i class="shear_head-down">';
	}
};
