/**
 * js常用方法--zzx
 */
var BaseUtils= BaseUtils?BaseUtils:{};

/**
 * 移动端定义滑动事件--zzx-----
 * $obj=需要绑定事件的$对象
 * options={
 * "startEvent":开始事件,
 * "muEvent":向上移动过程事件,
 * "mdEvent":向下移动过程事件,
 * "mlEvent":向左移动过程事件,
 * "mrEvent":向右移动过程事件,
 * "uEvent":向上滑动事件, 
 * "dEvent":向下滑动事件,
 * "lEvent":向左滑动事件,
 * "rEvent":向右滑动事件,
 * "endEvent":结束事件,
 * "mu":0,//默认触发向上滑动的最小距离
 * "md":0,//默认触发向下滑动的最小距离
 * "ml":0,//默认触发向左滑动的最小距离
 * "mr":0 //默认触发向右滑动的最小距离
 * }
 * 每个函数都可以调用的参数对象
 * BaseUtils.mobileSlide={
 *    "$this":当前事件对象
 *    "startmousex":初始x坐标
 *    "startmousey":初始y坐标
 *    "movex":移动x坐标
 *    "movey":移动y坐标
 * }
 */
BaseUtils.mobileSlideEvent = function($obj,options){
	if($obj){
		var collectEvent = {
				"mu":0,//默认触发向上滑动的最小距离
				"md":0,//默认触发向下滑动的最小距离
				"ml":0,//默认触发向左滑动的最小距离
				"mr":0 //默认触发向右滑动的最小距离
		};
		if (typeof options != "undefined") {
			collectEvent=$.extend({},collectEvent, options);
	    }
		
		var Mouse = {
			    x: 0,
			    y: 0
		};
		BaseUtils.mobileSlide={};
		$obj.unbind().bind("touchstart",function(e){
			BaseUtils.mobileSlide.$this = $(e.currentTarget);
			Mouse.x = e.originalEvent.touches[0].pageX;
			Mouse.y = e.originalEvent.touches[0].pageY;
			BaseUtils.mobileSlide.startmousex=Mouse.x;
			BaseUtils.mobileSlide.startmousey=Mouse.y;
			if(typeof collectEvent.startEvent =="function"){
				collectEvent.startEvent(BaseUtils.mobileSlide);
			}
		}).bind("touchmove",function(e){
			var moveX = e.originalEvent.touches[0].pageX-Mouse.x;
			var moveY = e.originalEvent.touches[0].pageY-Mouse.y;
			BaseUtils.mobileSlide.movex=moveX;
			BaseUtils.mobileSlide.movey=moveY;
			//向上
			if(moveY<0&&Math.abs(moveY)>Math.abs(moveX)){
				if(typeof collectEvent.muEvent =="function"){ 
					collectEvent.muEvent(BaseUtils.mobileSlide);
				}
			}
			//向下
			if(moveY>0&&Math.abs(moveY)>Math.abs(moveX)){
				if(typeof collectEvent.mdEvent =="function"){
					collectEvent.mdEvent(BaseUtils.mobileSlide);
				}
			}
			//向左
			if(moveX<0&&Math.abs(moveX)>Math.abs(moveY)){
				if(typeof collectEvent.mlEvent =="function"){
					collectEvent.mlEvent(BaseUtils.mobileSlide);
				}
			}
			//向右
			if(moveX>0&&Math.abs(moveX)>Math.abs(moveY)){
				if(typeof collectEvent.mrEvent =="function"){
					collectEvent.mrEvent(BaseUtils.mobileSlide);
				}
			}
		}).bind("touchend",function(e){
			var moveX = e.originalEvent.changedTouches[0].pageX-Mouse.x;
			var moveY = e.originalEvent.changedTouches[0].pageY-Mouse.y;
			//向上
			if(moveY<collectEvent.mu&&Math.abs(moveY)>Math.abs(moveX)){
				if(typeof collectEvent.uEvent =="function"){
					collectEvent.uEvent(BaseUtils.mobileSlide);
				}
			}
			//向下
			if(moveY>collectEvent.md&&Math.abs(moveY)>Math.abs(moveX)){
				if(typeof collectEvent.dEvent =="function"){
					collectEvent.dEvent(BaseUtils.mobileSlide);
				}
			}
			//向左
			if(moveX<collectEvent.ml&&Math.abs(moveX)>Math.abs(moveY)){
				if(typeof collectEvent.lEvent =="function"){
					collectEvent.lEvent(BaseUtils.mobileSlide);
				}
			}
			//向右
			if(moveX>collectEvent.mr&&Math.abs(moveX)>Math.abs(moveY)){
				if(typeof collectEvent.rEvent =="function"){
					collectEvent.rEvent(BaseUtils.mobileSlide);
				}
			}
			if(typeof collectEvent.endEvent =="function"){
				collectEvent.endEvent(BaseUtils.mobileSlide);
			}
		});
	}
}

/**
 * 移除事件-zzx-----
 */
BaseUtils.stopAJAX =function(arr){
	if(arr){
		arr.forEach(function(o,i) {
			 o.abort();
			 arr.splice(i, 1);
		});
	}
}
//检查是否超时
BaseUtils.checkTimeout = function(myfunction){
	$.ajax({
		url: "/dynweb/ajaxtimeout",
		dataType:"json",
		type:"post",
		data: {
		},
		success: function(data){
			BaseUtils.ajaxTimeOut(data,function(){
				if(typeof myfunction == "function"){
					myfunction();
				}
			});
		},
		error: function(data){
		    if(typeof myfunction == "function"){
                myfunction();
            }
		}
	});
}

//超时检查
BaseUtils.mobileCheckTimeoutByUrl = function(url, myfunction){
    $.ajax({
        url: url,
        dataType:"json",
        type:"post",
        data: {},
        success: function(data){
            BaseUtils.ajaxTimeOutMobile(data,function(){
                if(typeof myfunction == "function"){
                    myfunction();
                }
            });
        },
        error: function(data){
            if(typeof myfunction == "function"){
                myfunction();
            }
        }
    });
}
//判断当前系统是否超时，不要全部都去dyn项目，不然dyn项目压力应该会有点大
//reqUrl， 当前系统中的判断超时的url, url都是/xxxweb/ajaxtimeout格式
//refreshAfterLogin:弹出登录框后登录是否刷新当前页面， 0：不刷新；1：刷新
//myfunction: 具体要执行的操作
BaseUtils.checkCurrentSysTimeout = function(reqUrl, myfunction, refreshAfterLogin){
    if(refreshAfterLogin == 0){
        $("#login_box_refresh_currentPage").val("false");
    }
    $.ajax({
        url: reqUrl,
        dataType:"json",
        type:"post",
        data: {
        },
        success: function(data){
            BaseUtils.ajaxTimeOut(data,function(){
                $("#login_box_refresh_currentPage").val("true");
                if(typeof myfunction == "function"){
                    myfunction();
                }
            });
        },
        error: function(data){
            $("#login_box_refresh_currentPage").val("true");
            if(typeof myfunction == "function"){
                myfunction();
            }
        }
    });
}
//文件下载
BaseUtils.fileDown = function(fileUrl,obj,e){
	if(obj){
		BaseUtils.doHitMore(obj,1000);
	}
	if(e){
		BaseUtils.stopNextEvent(e);
	}
	$.ajax({
		url: "/dynweb/ajaxtimeout",
		dataType:"html",
		type:"post",
		data: {
		},
		success: function(data){
			BaseUtils.ajaxTimeOut(data,function(){
				if(fileUrl!=null&&fileUrl!=""){
					window.location.href = fileUrl;
				}
			});
		}
	});
}
/**
 * ----冒泡事件处理-----
 */
BaseUtils.stopNextEvent=function(evt){
	if(evt.currentTarget){
		if(evt.stopPropagation){
			evt.stopPropagation();
		}else{
			evt.cancelBubble=true;
		}
	}
};
/**
 * ---防止重复点击--
 */
BaseUtils.doHitMore = function(obj,time){
	if(time==null){
		time=1000;
	}
	$(obj).css('pointer-events','none');
	//$(obj).attr("disabled",true);
	var click = $(obj).attr("onclick");
	if(click!=null&&click!=""){
		$(obj).attr("onclick","");
	}
	setTimeout(function(){
		$(obj).css('pointer-events','auto');
		//$(obj).removeAttr("disabled");
		if(click!=null&&click!=""){
			$(obj).attr("onclick",click);
		}
	},time);
};
//设置为不可点击；
BaseUtils.unDisable = function(obj){
	$(obj).css('pointer-events','none');
	var click = $(obj).attr("onclick");
	if(click!=null&&click!=""){
		$(obj).attr("onclick","");
	}
	return click;
};
//设置为可以点击
BaseUtils.disable = function(obj,click){
	$(obj).css('pointer-events','auto');
	if(click!=null&&click!=""){
		$(obj).attr("onclick",click);
	}
};
/**
 * ---防止重复点击并修改点击方法--
 */
BaseUtils.doHitMoreAndchangF = function(obj,time,strF){
	if(time==null){
		time=1000;
	}
	var click = $(obj).attr("onclick");
	if(click!=null&&click!=""){
		$(obj).attr("onclick","");
	}
	setTimeout(function(){
		if(click!=null&&click!=""&&!strF){
			$(obj).attr("onclick",click);
		}
		if(strF){
			$(obj).attr("onclick",strF);
		}
	},time);
};
/**
 * ---scm-超时处理--
 */
//新的超时处理，在当前页面弹出登录框
BaseUtils.ajaxTimeOut = function(data,myfunction){
	 var toConfirm=false;
		
		if('{"ajaxSessionTimeOut":"yes"}'==data){
			toConfirm = true;
		}
		if(!toConfirm&&data!=null){
			toConfirm=data.ajaxSessionTimeOut;
		}
		if(toConfirm){
		    try {
		      //移动端有些地方需要判断超时,但移动端是直接跳登录,所以在此加上异常处理
		      ScmLoginBox.showLoginToast();
		    } catch (err) {
		      return;
		    }
		}else{
			if(typeof myfunction == "function"){
				myfunction();
			}
		}
}
//新的超时处理，在当前页面弹出登录框
BaseUtils.ajaxMobileTimeOut = function(data,myfunction){
   var toConfirm=false;
    
    if('{"ajaxSessionTimeOut":"yes"}'==data){
      toConfirm = true;
    }
    if(!toConfirm&&data!=null){
      toConfirm=data.ajaxSessionTimeOut;
    }
    if(toConfirm){
      window.location.href="/oauth/mobile/index?sys=wechat&service=" + encodeURIComponent(window.location.href);
    }else{
      if(typeof myfunction == "function"){
        myfunction();
      }
    }
}

//旧的超时处理逻辑
BaseUtils.ajaxTimeOutOld = function(data,myfunction){
    var toConfirm=false;
    if('{"ajaxSessionTimeOut":"yes"}'==data){
    	toConfirm = true;
    }
    if(!toConfirm&&data!=null){
    	toConfirm=data.ajaxSessionTimeOut;
    }
    if(toConfirm){
        jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r) {
            if (r) {
                var url = window.location.href;
                if(url.indexOf("/prjweb/outside/showfund")>=0){
                    //站外访问基金详情跳转到登录页面
                    url="/oauth/index?service="+encodeURIComponent(url);
                }
                if(url.indexOf("/pub/search/pdwhpatent")>=0 || url.indexOf("/pub/search/pdwhpaper")>=0
                        || url.indexOf("/pubweb/search/psnsearch")){
                    //站外检索成果，专利,人员跳转到登录页面
                    url="/oauth/index?service="+encodeURIComponent(url);
                }
                document.location.href=url;
                return;
            }
        });
    }else{
    	if(typeof myfunction == "function"){
    		myfunction();
    	}
    }
}

//移动端的超时
BaseUtils.ajaxTimeOutMobile = function(data,myfunction){
    var toConfirm=false;
    if('{"ajaxSessionTimeOut":"yes"}'==data){
    	toConfirm = true;
    }
    if(!toConfirm&&data!=null){
    	toConfirm=data.ajaxSessionTimeOut;
    }
    if(toConfirm){
        var url = window.location.href;
        BaseUtils.buildLoginUrl(url);
        return;
    }else{
    	if(typeof myfunction == "function"){
    		myfunction();
    	}
    }
}

//改变url
BaseUtils.changeUrl = function(targetModule) {
	var json = {};
	var oldUrl = window.location.href;
	var index = oldUrl.lastIndexOf("model");
	var newUrl = window.location.href;
	if (targetModule != undefined && targetModule != "") {
		if(oldUrl.lastIndexOf("?")<0){
			if (index < 0) {
				newUrl = oldUrl + "?model=" + targetModule;
			} else {
				newUrl = oldUrl.substring(0, index) + "model=" + targetModule;
			}
		}else{
			if (index < 0) {
				newUrl = oldUrl + "&model=" + targetModule;
			} else {
				newUrl = oldUrl.substring(0, index) + "model=" + targetModule;
			}
		}
		
	}
	window.history.replaceState(json, "", newUrl);
}
//当前url中的某个参数
BaseUtils.removeParamFromUrl = function(param) {
	var json = {};
	var oldUrl = window.location.href;
	param = param+"=";
	var index = oldUrl.lastIndexOf(param);
	var newUrl = "";
	if (index > 0) {
	var  split = oldUrl.split("?|$");
		for (var i = 0 ; i< split.length ; i++){
			if(split[i].IndexOf(param) ){
				continue ;
			}
			newUrl+=split[i];
		}
		window.history.replaceState(json, "", newUrl);
	}
	
}
//div 编辑框 焦点置后
BaseUtils.set_focus = function(el){
    el.focus();
    if($.support.msie){
        var range = document.selection.createRange();
        this.last = range;
        range.moveToElementText(el);
        range.select();
        document.selection.empty(); //取消选中
    }
    else{
        var range = document.createRange();
        range.selectNodeContents(el);
        range.collapse(false);
        var sel = window.getSelection();
        sel.removeAllRanges();
        sel.addRange(range);
    }
}
BaseUtils.str2Regexp = function(str, attributes){
	str = str
		.replace(/\\/g, '\\\\')
		.replace(/\$/g, '\\$')
		.replace(/\^/g, '\\^')
		.replace(/\(/g, '\\(')
		.replace(/\)/g, '\\)')
		.replace(/\[/g, '\\[')
		.replace(/\]/g, '\\]')
		.replace(/\{/g, '\\{')
		.replace(/\}/g, '\\}')
		.replace(/\*/g, '\\*')
		.replace(/\+/g, '\\+')
		.replace(/\?/g, "\\?")
		.replace(/\./g, '\\.')
		.replace(/\|/g, '\\|');

	return new RegExp(str, attributes);
}
BaseUtils.expand_more = function(ev){
  BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function() {
    ev.stopPropagation();
    ev.preventDefault();
    var $this=$(ev.currentTarget);
     if($('#skin_psn_setting').is(':hidden')){
           $('#skin_psn_setting').show();
      }else{
          $('#skin_psn_setting').hide();
      }
  }, 1);
}
/**
 * (未登录系统的)转换页面语言.
 */
BaseUtils.change_local_language=function(locale){
	strUrl = location.href;//当前url地址.
	//有一些请求是表单请求，特殊处理
	if(strUrl.indexOf("pubweb/publication/switchtype.action")!=-1){
		//我的-成果 编辑页面
		var formObj = $("#mainform");
		var tmpInput=$("<input type='hidden' name='locale' />");
		 	tmpInput.attr("value", locale);
		 	formObj.append(tmpInput);
		var selectObj = document.getElementById("selpub_type");
		ScholarEnter.toSwitchPubType(selectObj);
		return false;
	}
	//请求的url地址.
	var hrefString=strUrl.substring(0,strUrl.indexOf("?"));
	var params = "";
	var flag=false;//验证url请求参数中是否包含页面语言参数locale(true-包含；false-不包含).
	if(strUrl.indexOf("?")>=0){
		//url请求参数.
		var paraString = strUrl.substring(strUrl.indexOf("?") + 1, strUrl.length);
		if(paraString!=''){
			var paraArr=paraString.split("&");
			for ( var i = 0; j = paraArr[i]; i++) {
				//参数名.
				var name=j.substring(0, j.indexOf("="));
				//参数值.
				var value=j.substring(j.indexOf("=") + 1, j.length);
				if(name=='locale'){//页面语言的参数设置为locale的值.
					flag=true;
					value=locale;
				}else{//其他参数进行编码处理,替换掉特殊字符.
					//value=SCMIndex.URLencode(value);
				}
				params=params+name+"="+value+"&";
			} 
		}
	}
	//如果请求参数中不包含语言参数，则增加该参数.
	if(!flag){
		params=params+"locale="+locale;
	}
	//重定位页面请求地址
	window.location.href = hrefString+"?"+params;
	
};
/**
 * 数组去重
 * 列子：
 *  var arr = [112,112,34,'你好',112,112,34,'你好','str','str1'];
 *	alert(BaseUtils.unique3(arr));
 * 数组拼接
 * 列子.数组拼接：String s=arr .join(";");
 */
BaseUtils.unique3 = function(arr){
	 var res = [];
	 var json = {};
	 for(var i = 0; i < arr.length; i++){
	  if(!json[arr[i]]){
	   res.push(arr[i]);
	   json[arr[i]] = 1;
	  }
	 }
	 return res;
}


//跳转个人主页
BaseUtils.goToPsnHomepage = function(desPsnId, shortUrl){
	if(!BaseUtils.checkIsNull(shortUrl)){
		/*if(shortUrl.indexOf("http") > -1){
			window.open(shortUrl);
		}else{
			window.open("/P/"+shortUrl);
		}*/
		window.open(shortUrl);
	}else{
		window.open("/psnweb/outside/homepage?des3PsnId="+desPsnId);
	}
}

//检查是否为空
BaseUtils.checkIsNull = function(param){
	var check = false;
	if(param == null || param == "" || typeof(param) == "undefined"){
		check = true;
	}else{
		param = BaseUtils.trimLeftAndRightSpace(param);
		if(param == ""){
			check = true;
		}
	}
	return check;
}

//去掉左右两端空格
BaseUtils.trimLeftAndRightSpace = function(str){ //删除左右两端的空格
    return str.replace(/^\s+|\s+$/gm, "");
}
//保存当前url并跳转。用于后退
BaseUtils.forwardUrlRefer = function(isLogin,forwardUrl){
	if(forwardUrl==""){
		return;
	}
	if(isLogin){//需要登录的
		BaseUtils.checkTimeout(function(){
			BaseUtils.sendForwardUrlRefer(forwardUrl);			
		});
	}else{
		BaseUtils.sendForwardUrlRefer(forwardUrl);			
	}
};
//保存当前链接并跳转
BaseUtils.sendForwardUrlRefer = function(forwardUrl){
	var data = {"forwardUrl":forwardUrl};
	$.ajax({
		url : '/pub/ajaxforwardUrlRefer',
		type : 'post',
		dataType:'json',
		data : data,
		success : function(data) {
			window.location.href=data.forwardUrl;
		},
	});	
};

//构建登录url
BaseUtils.buildLoginUrl = function(url){
    var targetUrl = "/oauth/index?service=";
    if(!BaseUtils.checkIsNull(url)){
        $.ajax({
            url : '/oauth/url/ajaxencode',
            type : 'post',
            dataType:'json',
            data : {"service": encodeURIComponent(url)},
            success : function(data) {
                if(data.result == "success" && !BaseUtils.checkIsNull(data.des3Url)){
                    document.location.href=targetUrl + data.des3Url;
                }else{
                    document.location.href=targetUrl + encodeURIComponent(url);
                }
            },
            error: function(data){
                document.location.href=targetUrl + encodeURIComponent(url);
            }
        });
    }else{
        document.location.href=targetUrl;
    }
};


BaseUtils.nonsupportType = [".js",".jsp",".php",".aspx",".asp",".asa",".sh",".cmd",".bat",".exe",".dll",".com"];//不支持的文件类型，2018-11-06 ajb
/**
 * true=允许， false =不允许
 * @param fileName
 * @returns {*}
 */
BaseUtils.checkFileType= function($fileName ){
	if($fileName==undefined|| $fileName==""){
		return false ;
	}

	for (var i = 0; i < BaseUtils.nonsupportType.length; i++) {
		if ($fileName.substr($fileName.lastIndexOf(".")).toLowerCase() === BaseUtils.nonsupportType[i].toLowerCase()) {
            scmpublictoast(BaseUtils.nonsupportTypeMsg,1000);
			return false ;
		}
	}
	return true ;
}

//返回上一页
BaseUtils.goBackReferrer = function() {
  var referrer = document.referrer;
  var currentURI = window.location.href;
  if (referrer && referrer != "" && referrer.indexOf("/oauth/index") == -1 && referrer != currentURI) {
    document.location.href = document.referrer;
  } else {
    document.location.href = "/dynweb/main";
  }
};



//改变url
BaseUtils.changeLocationHref = function(paramName, paramVal) {
  var json = {};
  var oldUrl = window.location.href;
  var index = oldUrl.lastIndexOf(paramName);
  var newUrl = window.location.href;
  if (paramName != undefined && paramName != "") {
    var firstParamIndex = oldUrl.lastIndexOf("?");
    if(firstParamIndex < 0){
      newUrl = oldUrl + "?" + paramName + "=" + paramVal;
    }else{
      if (index < 0) {
        newUrl = oldUrl + "&" + paramName + "=" + paramVal;
      } else if(index > 0 && index >= firstParamIndex){
        newUrl = oldUrl.substring(0, index) + paramName + "=" + paramVal;
      }
    }
  }
  window.history.replaceState(json, "", newUrl);
};


/**
 * 检查邮箱
 * @param value
 * @returns {boolean}
 */
BaseUtils.checkEmail = function (value) {
    var checkEmail = /^[a-z0-9]+[a-z0-9_\-.]*@([a-z0-9][0-9a-z\-]*\.)+[a-z]{2,10}$/i;
    return checkEmail.test(value);
}


/**
 * 时间格式转换
 */
BaseUtils.dateFormat = function(fmt,date){
  var o = {
    "M+" : date.getMonth()+1,                 //月份
    "d+" : date.getDate(),                    //日
    "h+" : date.getHours(),                   //小时
    "m+" : date.getMinutes(),                 //分
    "s+" : date.getSeconds(),                 //秒
    "q+" : Math.floor((date.getMonth()+3)/3), //季度
    "S"  : date.getMilliseconds()             //毫秒
  };
  if(/(y+)/.test(fmt)){
    fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));
  }
  for(var k in o){
    if(new RegExp("("+ k +")").test(fmt)){
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    }
  }
  return fmt;
}

/**
 * 替换html标签
 * @param s
 */
BaseUtils.replaceHtml = function (s) {
    var re = /<[/]?(a|abbr|acronym|address|applet|area|article|aside|audio|base|basefont|bdi|bdo|big|blockquote|body|button|canvas|caption|center|cite|code|col|colgroup|command|datalist|dd|del|details|dir|div|dfn|dialog|dl|dt|em|embed|fieldset|figcaption|figure|font|footer|form|frame|frameset|h1|head|header|hr|html|i|iframe|img|input|ins|isindex|kbd|keygen|label|legend|li|link|map|mark|menu|menuitem|meta|meter|nav|noframes|noscript|object|ol|optgroup|option|output|p|param|pre|progress|q|rp|rt|ruby|s|samp|script|section|select|small|source|span|strike|strong|style|sub|summary|sup|table|tbody|td|textarea|tfoot|th|thead|time|title|tr|track|tt|u|ul|var|video|wbr|xmp)([^>]*)>/g;
    return s.replace(re ,"");
}


/**
 *  把html转义成HTML实体字符
 * @param str
 * @returns {string}
 * @constructor
 */
BaseUtils.htmlEncode = function(str) {
  var s = "";
  if (str.length === 0) {
    return "";
  }
  s = str.replace(/&/g, "&amp;");
  s = s.replace(/</g, "&lt;");
  s = s.replace(/>/g, "&gt;");
  s = s.replace(/ /g, "&nbsp;");
  s = s.replace(/\'/g, "&#39;");//IE下不支持实体名称
  s = s.replace(/\"/g, "&quot;");
  return s;
}

/**
 *  转义字符还原成html字符
 * @param str
 * @returns {string}
 * @constructor
 */
BaseUtils.htmlRestore = function(str) {
  var s = "";
  if (str.length === 0) {
    return "";
  }
  s = str.replace(/&amp;/g, "&");
  s = s.replace(/&lt;/g, "<");
  s = s.replace(/&gt;/g, ">");
  s = s.replace(/&nbsp;/g, " ");
  s = s.replace(/&#39;/g, "\'");
  s = s.replace(/&quot;/g, "\"");
  return s;
}




 BaseUtils.htmlEscape = function(text) {
    return text.replace(/[<>"&]/g, function (match, pos, originalText) {
        switch (match) {
            case "<":
                return "&lt;";
            case ">":
                return "&gt;";
            case "&":
                return "&amp;";
            case "\"":
                return "&quot;";
        }
    });
}
 
 BaseUtils.parseMoney = function(amount){
   return amount.replace(/(\d{1,3})(?=(\d{3})+(?:$|\.))/g, '$1,');
 }

/**
 * 检查域名
 * 输入英文和数字组合
 * @param value
 * @returns {boolean}
 */
BaseUtils.checkInsDomain = function (value) {
    var checkDomain = /^[a-zA-Z0-9]+$/g;
    return checkDomain.test(value);
}
