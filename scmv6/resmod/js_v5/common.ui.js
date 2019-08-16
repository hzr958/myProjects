var common = common ? common : {};
common.demo = function() {
};

/** 复选框全选功能. */
common.selectAllCheckbox = function(obj, checkboxName) {
	var choose = obj.checked;
	$(":checkbox[name='" + checkboxName + "']").each(function() {
		$(this).attr("checked", choose);
	});
};

/** 复选框全选功能.(不包括禁用的) */
common.selAllNotDisable = function(obj, checkboxName) {
	var choose = obj.checked;
	$(":checkbox[name='" + checkboxName + "'][disabled!='disabled']").each(function() {
		$(this).attr("checked", choose);
	});
};

/** 复选框全选 触发复选框全选 **/
common.checkAll = function(obj,checkboxAllName) {
	var flag = true;
	var checkboxName = $(obj).attr("name");
	if (obj.checked) {
		var cks = $(":checkbox[name='" + checkboxName + "']");
		for ( var i = 0; i < cks.length; i++) {
			// 没有全部选择
			if (!cks[i].checked) {
				flag = false;
				break;
			}
		}
	} else {
		flag = false;
	}
	$(":checkbox[name='"+checkboxAllName+"']").attr("checked", flag);
}

/** 复选框全选 触发复选框全选（不包括禁用的） **/
common.checkAllNotDisable = function(obj,checkboxAllName) {
	var flag = true;
	var checkboxName = $(obj).attr("name");
	if (obj.checked) {
		var cks = $(":checkbox[name='" + checkboxName + "'][disabled!='disabled']");
		for ( var i = 0; i < cks.length; i++) {
			// 没有全部选择
			if (!cks[i].checked) {
				flag = false;
				break;
			}
		}
	} else {
		flag = false;
	}
	$(":checkbox[name='"+checkboxAllName+"']").attr("checked", flag);
}

/** top菜单更多操作 * */
common.showMoreOperation = function(btn, div_id,e) {
	var div_obj = $("#" + div_id);
	// 显示隐藏
	if (div_obj.is(":hidden")) {
		div_obj.show();
		div_obj.css("zIndex", "1000");
	} else {
		div_obj.hide();
		return;
	}
	// 标示鼠标是否移入DIV
	var flag = true;
	div_obj.unbind();
	div_obj.bind("mouseenter", function() {
		flag = false;
	});
	// 鼠标移出，将DIV隐藏
	//div_obj.bind("mouseleave", function() {
		//flag = true;
		//setTimeout(function() {
			//if (flag) {
				//div_obj.hide();
			//}
		//}, 1000);
	//});
	// 点击链接，将DIV隐藏
	//$('#' + div_id + '>a').click(function() {
		//div_obj.hide();
	//});
	// 2秒还未进入，关闭
	//setTimeout(function() {
		//if (flag) {
			//div_obj.hide();
		//}
	//}, 1000);
	//点击其他地方关闭
    if (e && e.stopPropagation) {//非IE  
        e.stopPropagation();  
    }  
    else {//IE  
        window.event.cancelBubble = true;  
    } 
    $(document).click(function(){div_obj.hide();});
	
};

// 限制textarea最多输入
common.setTextareaMaxLength = function(maxLength) {
	$("textarea").keyup(function() {
		var area = $(this).val();
		if (area.length > maxLength) {
			$(this).val(area.substring(0, maxLength));
		}
	});
	$("textarea").blur(function() {
		var area = $(this).val();
		if (area.length > maxLength) {
			$(this).val(area.substring(0, maxLength));
		}
	});
};
// 验证邮件格式是否合法
common.isEmail = function(email) {
	return /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i
			.test(email);
};

common.isEmail2 = function(email) {
	return /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/gi
			.test(email);
};

/**
 * 转义特殊字符,如：&gt &lt;转换为><
 * 
 * @param str
 * @return
 */
common.unescapeHTML = function(str) {
	$tmp = $("<div></div>").html(str);
	nodes = $tmp.contents();
	nl = nodes.length;
	if (nl > 0) {
		var a = [];
		for ( var i = 0, il = nl; i < il; i++) {
			a.push(nodes[i].nodeValue);
		}
		return a.join('');
	}
	return '';
};

common.isSpecial = function(s) {
	var str = '",.;[]{}+=|\*&^%$#@!~()-/?<>';
	var flag = false;
	if ($.trim(s).length > 0) {
		for ( var i = 0; i < str.length; i++) {
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
common.ajaxSessionTimeoutHandler = function(data) {
	var ajaxTimeOutFlag = data['ajaxSessionTimeOut'];
	if (ajaxTimeOutFlag != null && typeof ajaxTimeOutFlag != "undefined"
			&& ajaxTimeOutFlag == 'yes') {
		if(locale!= null && typeof locale != "undefined"
			&& locale == 'zh_CN'){
			alert("您还未登录或登录已超时，请登录！");
		}else{
			alert("You are not logged in or session time out, please log in again!");
		}
		top.location.href = "/scmwebsns/";
		return true;

	}
	return false;
};

// 判断是否是不是中文
common.isChinaStr = function(str) {
	var regu = "[\w\W]*[\u4e00-\u9fa5][\w\W]*";
	var re = new RegExp(regu);
	if (str == "")
		return false;
	if (re.test(str)) {
		return true;
	} else {
		return false;
	}
};
// 以下是调用
// if (Sys.ie) alert('IE: ' + Sys.ie);
// if (Sys.firefox) alert('Firefox: ' + Sys.firefox);
// if (Sys.chrome) alert('Chrome: ' + Sys.chrome);
// if (Sys.opera) alert('Opera: ' + Sys.opera);
// if (Sys.safari) alert('Safari: ' + Sys.safari);
// if(Sys.ie == '8.0')
var Sys = {};
common.conifgBrowser = function() {
	var ua = navigator.userAgent.toLowerCase();
	var pm = navigator.platform.toLowerCase();
	var s;
	(s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1]: (s = ua
			.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] : (s = ua
			.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] : (s = ua
			.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] : (s = ua
			.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : (s = ua
			.match(/rv:([\d.]+)/)) ? Sys.ie = s[1]  : 0;
	(s=pm.match(/win\d*/gi))?Sys.win = true: (s = pm
			.match(/macintel/gi)) ? Sys.mac = true : (s = pm
			.match(/linux/gi))? Sys.linux = true : '';
};
common.conifgBrowser();

common.inputWordCount = function(obj, length, flag) {
	var content = $.trim($(obj).val());
	var inputLength = 0;
	if(typeof(flag)!='undefined' && flag == "1"){
		inputLength = common.textLengthCount(content);
	}
	else{
		inputLength = content.length;
	}
	$("#" + $(obj).attr("id") + "_countLabel").text(inputLength);
	if (inputLength > length) {
		$("#" + $(obj).attr("id") + "_countLabel").css({"color":"red","font-weight":"bold"});
	} else {
		$("#" + $(obj).attr("id") + "_countLabel").css({"color":"#999","font-weight":"normal"});
	}
};

common.divWordCount = function(divId, length, flag) {
	var obj=$("#"+divId);
	var content = $.trim($(obj).text());
	var inputLength = 0;
	if(typeof(flag)!='undefined' && flag == "1"){
		inputLength = common.textLengthCount(content);
	}
	else{
		inputLength = content.length;
	}
	$("#" + divId + "_countLabel").text(inputLength);
	if (inputLength > length) {
		$("#" + divId + "_countLabel").css({"color":"red","font-weight":"bold"});
	}else{
		$("#" + divId + "_countLabel").css({"color":"#999","font-weight":"normal"});
	}
};

/**
 * 评论字数计算.
 */
common.textLengthCount = function(text){
	if($.trim(text).length==0){
		return 0;
	}else{
		var inputLength = 0;
		text = $.trim(text);
		for(var i=0;i<text.length;i++){
			var cchar = text.charAt(i);
			if(common.isChinaStr(cchar)){
				inputLength += 1;
			}else{
				inputLength += 0.5;
			}
		}
		inputLength = Math.round(inputLength);
		return inputLength;
	}
}
// 转码
common.htmlEncode = function(str) {
	var s = "";
	if (str.length == 0)
		return "";
	s = str.replace(/&/g, "&amp;");
	s = s.replace(/</g, "&lt;");
	s = s.replace(/>/g, "&gt;");
	s = s.replace(/\'/g, "&#39;");
	s = s.replace(/\"/g, "&quot;");
	return s;
};

// 解码
common.htmlDecode = function(str) {
	var s = "";
	if (str.length == 0)
		return "";
	s = str.replace(/&amp;/g, "&");
	s = s.replace(/&lt;/g, "<");
	s = s.replace(/&gt;/g, ">");
	s = s.replace(/&#39;/g, "\'");
	s = s.replace(/&quot;/g, "\"");
	return s;
};

/**
 * 必须引用，jquery.thickbox.css,jquery.thickbox.min.js.
 * 背景加上灰色，用$.Thickbox.closeWin(),移除.
 * @return
 */
common.bgCover = function(){
	if(document.getElementById("TB_overlay") == null){
		$("body").append("<div id='TB_overlay'><div id='TB_window'></div></div>");
		//$("#TB_overlay").click(tb_remove);
	}
	var userAgent = navigator.userAgent.toLowerCase();
	if (userAgent.indexOf('mac') != -1 && userAgent.indexOf('firefox')!=-1){
		$("#TB_overlay").addClass("TB_overlayMacFFBGHack");//use png overlay so hide flash
	}else{
		$("#TB_overlay").addClass("TB_overlayBG");//use background and opacity
	}
};

common.help=function(hiddenId,showId){
	$("#"+hiddenId).hide();
	$("#"+showId).show();
};

// 将编码为160的字符转为编码为32的字符
common.divSpaceEncode = function(str){
	var s = "";
	var cchar = "";
	var size = str.length;
	if (size == 0)
		return "";
	for(var i=0; i<size; i++){
		cchar = str.charAt(i);
		if(parseInt(str.charCodeAt(i))==160){
			cchar = String.fromCharCode(32);
		}
		s += cchar;
	}
	return s;
}
common.placeCaretAtEnd = function(el) {
    try {
	el.focus();
    if (typeof window.getSelection != "undefined" && typeof document.createRange != "undefined") {
        var range = document.createRange();
        console.log(range);
        range.selectNodeContents(el);
        range.collapse(false);
        var sel = window.getSelection();
        sel.removeAllRanges();
        sel.addRange(range);
    } else if (typeof document.body.createTextRange != "undefined") {
        var textRange = document.body.createTextRange();
        var ele = document.getElementById($(el).attr("id"));
        textRange.moveToElementText(ele);
        textRange.collapse(false);
        textRange.select();
    }
	} catch (e) {
		
	}
}

/**
 * 将光标移至结尾
 */
common.moveCursorToEnd = function(obj){
	if(obj == null)
		return;
	if(obj instanceof jQuery){
		obj = obj[0];// jquery对象转换为dom对象
	}
	if(obj.lastChild == null)
		return;
	obj.focus();
    if(window.getSelection){
    	var selObj = window.getSelection();            
        var rangeObj = document.createRange();
        rangeObj.setStart(obj.lastChild, obj.lastChild.length);
        selObj.removeAllRanges();
        selObj.addRange(rangeObj);
    }
}