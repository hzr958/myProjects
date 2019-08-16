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
		    ScmLoginBox.showLoginToast();
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
        url="/oauth/index?service="+encodeURIComponent(url);
        document.location.href=url;
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
    return str.replace(/(^\s*)|(\s*$)/g, "");
}
