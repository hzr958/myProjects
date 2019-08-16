// 防止事件冒泡
function stopBubble(e) {
	// If an event object is provided, then this is a non-IE browser
	if ( e && e.stopPropagation )
	// and therefore it supports the W3C stopPropagation() method
	e.stopPropagation();
	else
	// Otherwise, we need to use the Internet Explorer
	// way of cancelling event bubbling
	window.event.cancelBubble = true;
} 


//显示更多操作菜单
function show_more_operation(btn,div_id,div_bg){
	var div_obj = $("#"+div_id);
	var bg_obj = null;
	if(div_bg){
		bg_obj  = $("#"+div_bg);
	}
	if(div_obj.is(":hidden")){
		div_obj.show();
		//定位
		div_obj.css("left",$(btn).offset().left);
		div_obj.css("top",$(btn).offset().top+23);
		div_obj.css("zIndex","1000");
		if(bg_obj){
			bg_obj.css("left",$(div_obj).offset().left-1);
			bg_obj.css("top",$(div_obj).offset().top);
			div_obj.css("zIndex","1000");
			bg_obj.css("width",$(div_obj).width() +"px");
			bg_obj.css("height",$(div_obj).height() +"px");
			bg_obj.show();
		}
	}else{
		div_obj.hide();
		if(bg_obj!=null)
			bg_obj.hide();
		return;
	}
	//标示鼠标是否移入DIV
	var flag = true;
	div_obj.bind("mouseover",function(){
		flag = false;
	});
	//鼠标移出，将DIV隐藏
	div_obj.bind("mouseleave",function(){
		flag = true;
		setTimeout(function(){
			if(flag){
				div_obj.hide();
				if(bg_obj){
					bg_obj.hide();
				}
			}
		},2000);
	});
	//点击链接，将DIV隐藏
	$('#more_operation>a').click(function(){
		div_obj.hide();
		if(bg_obj){
			bg_obj.hide();
		}
	});
	//2秒还未进入，关闭
	setTimeout(function(){
		if(flag){
			div_obj.hide();
			if(bg_obj){
				bg_obj.hide();
			}
		}
	},2000);
}

//事件处理函数，用户DIV层失去焦点时，隐藏out_div_hidden层
//$(document).bind("click",hidden_out_div);
function hidden_out_div(evt) {
	 var element = evt.srcElement||evt.target; 
	 var flag = true;
	 while (element) {
	  if ($(element).hasClass("out_div_hidden")){
	   		flag = false;
			break;
	  }
	  	element = element.parentNode; 
	 }
	 if (flag) {
	 	$(".out_div_hidden").hide();
	 }
}

//事件处理函数，用户DIV层失去焦点时，隐藏outeditfd_div_hidden层,同时删除页面上的left_nav_infolink_high样式的样式
//$(document).bind("click",hidden_outeditfd_div);
function hidden_outeditfd_div(evt) {
	 var element = evt.srcElement||evt.target; 
	 var flag = true;
	 while (element) {
	  if ($(element).hasClass("outeditfd_div_hidden")){
	   		flag = false;
			break;
	  }
	  	element = element.parentNode; 
	 }
	 if (flag) {
	 	$(".outeditfd_div_hidden").hide();
		$(".left_nav_infolink_high").removeClass("left_nav_infolink_high");
	 }
}

//打开指定定位到按钮下方的层，该层需指定class:hidden_out_div
function openFloatDiv(btn,div_id){
	var obj = $('#'+div_id);
	if($(obj).is(":hidden")){
		//失去焦点，需要隐藏带hidden_out_div class的层
	//	$(document).bind("click",hidden_out_div);
		//显示层，需要延迟显示
		setTimeout(function(){
		$(obj).show();
		//定位
		$(obj).css("left",$(btn).offset().left);
		$(obj).css("top",$(btn).offset().top+24);
		$(obj).css("zIndex",1000);
		$(obj).css("height",$(obj).find(".float_div_content").height()+70)
		},100);
	}else{
		$(obj).hide();
	}
}

//左菜单展开，关闭
function switch_left_nav(obj){
	var div = $(obj).parent();
	if($(obj).hasClass("left_nav_open"))
	{
		div.find(">div:not(div:first-child)").hide();
		$(obj).removeClass("left_nav_open");
	}else{
		div.find(">div").show();
		$(obj).addClass("left_nav_open");
	}
}
 //左边菜单展开，关闭替换图标
function replaceImg(obj){
    var img = $(obj).find("img");
	var src = img.attr("src");
	if(src.indexOf('open')>0){
		img.attr("src",src.replace("open","close"));
	}else{
		img.attr("src",src.replace("close","open"));
	}	
}
//打开左侧菜单，根据设置的.info样式
function open_left_nav(){
	$("#left_nav").find(".info")
		.each(function(){
			$(this).parent().find(">div:first-child").trigger("click");
	});
}

//左侧菜单鼠标事件
function bind_left_nav_mouse()
{
	//鼠标在上面高亮显示
	$("#left_nav div.infolink").mouseover(function(){
		$(this).addClass("infolink_hover");
	});
	//鼠标移走移除高亮
	$("#left_nav div.infolink").mouseout(function(){
		$(this).removeClass("infolink_hover");
	});
}

//左侧菜单编辑文件夹
function bind_left_nav_folder()
{
	var left_nav = $("#left_nav");
	left_nav.find('div.edit_folder').click(function(){
		
		//失去焦点，需要隐藏带hidden_out_div class的层
		$(document).bind("click",hidden_outeditfd_div);
		var top = $(this).offset().top;
		var left = $(this).offset().left;
		var fdid = $(this).parent().find(".folder_id").val();
		var fdname = $(this).parent().find(".folder_name").val();
		var parent = $(this).parent();
		setTimeout(function(){
		    //添加焦点事件
			parent.addClass("left_nav_infolink_high");
			var edit_div = $('#float_edit');
			edit_div.css("display","block");
			edit_div.css("top",top);
			edit_div.css("left",left);
			edit_div.find(".folder_id").val(fdid);
			edit_div.find(".folder_name").val(fdname);
		},100);
	});
	
	//需要编辑文件夹的，高亮显示，同时显示编辑图片
	left_nav.find(".edit_folder_div").mouseover(function(){
		$(this).addClass("infolink_hover");
		$(this).find(".edit_folder").show();
	});
	//需要编辑文件夹的，移除高亮显示，同时不显示编辑图片
	left_nav.find(".edit_folder_div").mouseout(function(){
		$(this).find(".edit_folder").hide();
		$(this).removeClass("infolink_hover");
	});
}

//显示操作正确信息
function show_yes_tips(tip_id,msg,time)
{
	if(!time || time < 3000){
		time = 3000;
	}
	scmSuccess(msg,null,time);
}

//显示操作错误信息
function show_wrong_tips(tip_id,msg,time)
{
	if(!time || time <3000){
		time = 3000;
	}
	scmError(msg, null,3000);
}


//杰青近五年代表性论著操作错误信息
function show_unlimit_wrong_tips(tip_id,msg)
{
	scmError(msg, null,3000);
}

//在弹出框中显示提示信息
function show_msg_tips_newdiv(type,msg,id){
	if(!type || !msg)
		return;
	var time=3000;
	if('success'==type || 'yes'==type)
		scmSuccess(msg,null,time);
	if('warn'==type || 'warning'==type)
		scmWarn(msg,null,time);
	if('error'==type || 'wrong'==type)
		scmError(msg,null,time);
}

function show_msg_tips(type,msg,width){
	if(!type || !msg)
		return;
	var time=1000;
	if('success'==type || 'yes'==type)
		scmSuccess(msg, width,time);
	if('warn'==type || 'warning'==type)
		scmWarn(msg, width,time);
	if('error'==type || 'wrong'==type)
		scmError(msg, width,time);
}

function rol_show_msg_tips(type,msg,rowCount){
	if(!type || !msg)
		return;
	var time=3000;
	if('success'==type || 'yes'==type)
		type='yes';
	if('warn'==type || 'warning'==type)
		type='warn';
	if('error'==type || 'wrong'==type)
		type='wrong';
	var classs = 'tips_'+type+'_msg';
	var tip = $("#tip_msg_box");
	if(rowCount && rowCount>1){
		var aHeigth = 30;
		tip.css("height",aHeigth+(rowCount*10));
		
		var bHeigth = 26;
		tip.find(".tips").css("height",bHeigth+(rowCount*10));
	}
	tip.find("#tips_msg").html(msg);
	tip.find("#tips_msg").removeClass();	
	tip.find("#tips_msg").addClass(classs);
	tip.show();
	tip.click(function(){$(this).hide();});
	tip.find(".tips_close").click(function(){$(tip).hide();});
	setTimeout(function(){tip.hide();},time);
}
//手动关闭显示的操作消息
function close_msg_tips(){
	$("#tip_msg_box").hide();
}
//替换HMTL特殊字符,注意替换顺序
function covertHmtl(str)
{
	str = str.replace(/\&/gi,"&amp;");
	str = str.replace(/\>/gi,"&gt;");
	str = str.replace(/\</gi,"&lt;");
	str = str.replace(/\n/gi,"<br/>");
	str = str.replace(/\s/gi,"&nbsp;");
	return str;
}
//将textarea转换成span，过滤掉特殊字符
function refreshTextArea()
{
	var objs = $(".rep_textarea");
	for(var i = 0; i < objs.size(); i++)
	{
		var tag=objs[i];
		var p = tag.parentNode;
		if(!p) p = document;
		if(/\r(\n)?/g.test(tag.value)==true)
		{
			newTag = getSpan(tag.value.replace(/\r(\n)?/g,"<br>"));
		}
		else
		{
			newTag = getSpan(tag.value);
		}
		p.replaceChild(newTag,tag);
	}
}
function getSpan(text)
{
	var node = document.createElement("span");
	node.innerHTML=text+"&nbsp;";
	return node;
}
//帮助信息
function bindHelps()
{
	$(".box_shrink").bind("click",function(){
		

		var box_shrink_left = $(this).find(".box_shrink_left");
		if(box_shrink_left.is(":visible")){
			box_shrink_left.hide();
			$(this).find(".box_shrink_left1").show();
			$(this).find(".box_shrink_left_down").hide();
			$(this).find(".box_shrink_left_up").show();
		}else{
			box_shrink_left.show();
			$(this).find(".box_shrink_left1").hide();
			$(this).find(".box_shrink_left_down").show();
			$(this).find(".box_shrink_left_up").hide();
		}
	});
	$(".box_shrink").each(function(){
		var isShow = $(this).find(".box_shrink_left1").is(":visible")? true : false;
		if(isShow){
			$(this).find(".box_shrink_left_down").hide();
			$(this).find(".box_shrink_left_up").show();
		}else{
			$(this).find(".box_shrink_left_down").show();
			$(this).find(".box_shrink_left_up").hide();
		}
	});
	//链接不进行事件冒泡
	$(".box_shrink").find(".box_shrink_left1 a").bind("click",function(event){
		stopBubble(event);
	});
	
}

//帮助信息
function bindHelps2()
{
	$(".box_shrink").bind("click",function(){
		var box_shrink_left = $(this).find(".box_shrink_left");
		if(box_shrink_left.is(":visible")){
			box_shrink_left.hide();
			$(this).find(".box_shrink_left1").show();
			$(this).find(".box_shrink_left_down").hide();
			$(this).find(".box_shrink_left_up").show();
			$("#help_margin").height(111);
			var radioVal=$("input[type=radio][name=authorRelation][checked]").val();
			if(radioVal==0){
				//添加成果作者和部门关联 div
				var searchTrDiv=$("#float_margin").find("#search_tr_margin");
				if(searchTrDiv.length <= 0){
					searchTrDiv=$("<div id='search_tr_margin'>&nbsp;</div>");
					searchTrDiv.height(125);
					$("#float_margin").find("#table_margin").append(searchTrDiv);
				} else {
					searchTrDiv.height(searchTrDiv.height() + 29);
				}
			}
		}else{
			box_shrink_left.show();
			$(this).find(".box_shrink_left1").hide();
			$(this).find(".box_shrink_left_down").show();
			$(this).find(".box_shrink_left_up").hide();
			//修改 help div的高度.
			$("#help_margin").height(49);
			var radioVal=$("input[type=radio][name=authorRelation][checked]").val();
			if(radioVal==0){
				//添加成果作者和部门关联 div
				var searchTrDiv=$("#float_margin").find("#search_tr_margin");
				if(searchTrDiv.length <= 0){
					searchTrDiv=$("<div id='search_tr_margin'>&nbsp;</div>");
					searchTrDiv.height(125);
					$("#float_margin").find("#table_margin").append(searchTrDiv);
				} else {
					searchTrDiv.height(searchTrDiv.height() - 29);
				}
			}
		}
	});
	$(".box_shrink").each(function(){
		var isShow = $(this).find(".box_shrink_left1").is(":visible")? true : false;
		if(isShow){
			$(this).find(".box_shrink_left_down").hide();
			$(this).find(".box_shrink_left_up").show();
		}else{
			$(this).find(".box_shrink_left_down").show();
			$(this).find(".box_shrink_left_up").hide();
		}
	});
	//链接不进行事件冒泡
	$(".box_shrink").find(".box_shrink_left1 a").bind("click",function(event){
		stopBubble(event);
	});
	
}

//帮助信息
function bindHelps3()
{
	$(".box_shrink").bind("click",function(){
		var box_shrink_left = $(this).find(".box_shrink_left");
		if(box_shrink_left.is(":visible")){
			box_shrink_left.hide();
			$(this).find(".box_shrink_left1").show();
			$(this).find(".box_shrink_left_down").hide();
			$(this).find(".box_shrink_left_up").show();
			$("#help_margin").height(106);
		}else{
			box_shrink_left.show();
			$(this).find(".box_shrink_left1").hide();
			$(this).find(".box_shrink_left_down").show();
			$(this).find(".box_shrink_left_up").hide();
			$("#help_margin").height(78);
		}
	});
	$(".box_shrink").each(function(){
		var isShow = $(this).find(".box_shrink_left1").is(":visible")? true : false;
		if(isShow){
			$(this).find(".box_shrink_left_down").hide();
			$(this).find(".box_shrink_left_up").show();
		}else{
			$(this).find(".box_shrink_left_down").show();
			$(this).find(".box_shrink_left_up").hide();
		}
	});
	//链接不进行事件冒泡
	$(".box_shrink").find(".box_shrink_left1 a").bind("click",function(event){
		stopBubble(event);
	});
	
}

//显示隐藏检索条件
function view_search(){
	var search_block = $("#search_block");
	if(search_block.is(":visible")){
		$("#isSearchShow").val(0);
		$("#view_search_block_link").show();
		$("#hide_search_block_link").hide();
		search_block.hide();
	}else{
		$("#view_search_block_link").hide();
		$("#hide_search_block_link").show();
		$("#isSearchShow").val(1);
		search_block.show();
	}
}

//显示隐藏检索条件
function view_search2(){
	var search_block = $("#search_block");
	if(!search_block.is(":hidden")){
		$("#isSearchShow").val(0);
		$("#view_search_block_link").show();
		$("#hide_search_block_link").hide();
		search_block.hide();
		//移除search div
		$("#search_margin").remove();
		//移除成果作者和部门关联 div
		$("#search_tr_margin").remove();
		var radioVal=$("input[type=radio][name=authorRelation][checked]").val();
		if(radioVal==0){
			//添加成果作者和部门关联 div
			var searchTrDiv=$("#float_margin").find("#search_tr_margin");
			if(searchTrDiv.length <= 0){
				searchTrDiv=$("<div id='search_tr_margin'>&nbsp;</div>");
				var box_shrink_left = $('#pub_task_notice').find(".box_shrink_left");
				if(box_shrink_left.is(":visible")){
					searchTrDiv.height(102);
				}else{
					searchTrDiv.height(132);
				}
				$("#float_margin").find("#table_margin").append(searchTrDiv);
			} else {
				searchTrDiv.height(searchTrDiv.height() - 40);
			}
		}
		$(".rol_search_box").css("background-image","url()");
		$(".rol_search_box").css("min-height",10);
	}else{
		$(".rol_search_box").css("background-image","url("+respath+"/images_v5/icon_text.gif)");
		$(".rol_search_box").css("min-height",60);
		
		$("#view_search_block_link").hide();
		$("#hide_search_block_link").show();
		$("#isSearchShow").val(1);
		search_block.show();
		//添加search div
		var searchDiv=$("#float_margin").find("#search_margin");
		if(searchDiv.length==0){
			searchDiv=$("<div id='search_margin'>&nbsp;</div>");
			searchDiv.height(128);
			$("#float_margin").append(searchDiv);
		}
		var radioVal=$("input[type=radio][name=authorRelation][checked]").val();
		if(radioVal==1){
			//添加成果作者和部门关联 div
			var searchTrDiv=$("#float_margin").find("#search_tr_margin");
			if(searchTrDiv.length==0){
				searchTrDiv=$("<div id='search_tr_margin'>&nbsp;</div>");
				searchTrDiv.height(90);
				$("#float_margin").append(searchTrDiv);
			}
		} else if (radioVal == 0){
			//添加成果作者和部门关联 div
			var searchTrDiv=$("#float_margin").find("#search_tr_margin");
			if(searchTrDiv.length <= 0){
				searchTrDiv=$("<div id='search_tr_margin'>&nbsp;</div>");
				searchTrDiv.height(171);
				$("#float_margin").find("#table_margin").append(searchTrDiv);
			} else {
				searchTrDiv.height(searchTrDiv.height() + 40);
			}
		}
	}
}

function view_search3(){
	var search_block = $("#search_block");
	if(search_block.is(":visible")){
		$("#isSearchShow").val(0);
		$("#view_search_block_link").show();
		$("#hide_search_block_link").hide();
		search_block.hide();
		//移出search div
		$("#search_margin").remove();
	}else{
		$("#view_search_block_link").hide();
		$("#hide_search_block_link").show();
		$("#isSearchShow").val(1);
		search_block.show();
		//添加search div
		var searchDiv=$("#float_margin").find("#search_margin");
		if(searchDiv.length==0){
			searchDiv=$("<div id='search_margin'>&nbsp;</div>");
			searchDiv.height(105);
			$("#float_margin").append(searchDiv);
		}
	}
}


//验证邮件格式是否合法
function isEmail(email) {
	return /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i.test(email);
}

//产生数字下拉，例如年度等
function genNumDescOption(start,end,select_id){
	if(start > end){
		var tmp = start;
		start = end;
		end = tmp;
	}
	var select = $("#"+select_id);
	for(;end >= start;end--){
		var option = $("<option value='"+end+"' select=''>"+end+"</option>");
		select.append(option);
	}
}

//判断是否是不是中文
function isChinStr(s){
	var regu = "[\w\W]*[\u4e00-\u9fa5][\w\W]*";   
	var re = new RegExp(regu);	
	if (s=="")
		return false;
	if (re.test(s)) {
		return true;
	}else{
		return false;
	}
}

//判断是否是数字
 function isNumStr(s) { 
	var patrn=/^[0-9]{1,20}$/; 
	if (!patrn.exec(s)) return false ;
	return true ;
} 
 
 //字母、数字、下划线   
 function isCharsOrNum(s) {   
	 var patrn=/^(\w)$/;   
	 if (!patrn.exec(s)) 
		 return false;  
	 	return true ; 
 }  
 
//字母、数字
 function isChrOrNum(s){	 
	 var patrn=/^[A-Za-z0-9]+$/; 
		if (!patrn.exec(s)) return false ;
		return true ;
 }
 
//是否是中文或者是英文和数字 
 function isParamStr(s){
	 var flag = false;
	 if(isChinStr(s))
		 flag =true;
	 if(isChrOrNum(s))
		 flag =true;
	 return flag;
 }
 
//限制textarea最多输入
 function setTextareaMaxLength(maxLength){
 	 $("textarea").keyup(function(){ 
 		 var area=$(this).val(); 
 		 if(area.length>maxLength){ 
 			 $(this).val(area.substring(0,maxLength)); 
 		 } 
 	 }); 
 	 $("textarea").blur(function(){ 
 		 var area=$(this).val(); 
 		 if(area.length>maxLength){ 
 			 $(this).val(area.substring(0,maxLength)); 
 		 } 
 	 });
 }
 //判断是否是特殊字符
 function isSpecial(s){
	 var str = '",.;[]{}+=|\*&^%$#@!~()-/?<>';
	 var flag = false;
	 if($.trim(s).length>0){
		 for(var i=0;i<str.length;i++){
			 if(s.indexOf(str.charAt(i))>=0){
				 flag=true; 
				 break;
			 }
		 }
		 if(flag)
		 return true;	 
	 }
	 var patrn = /^[^<]*(<(.|\s)+>)[^>]*$|^#$/;
		if (!patrn.exec(s)) return false ;
		return true ;
 }
 
//字符串真实的长度
 function getStrRealLength(str){
 	return str.replace(/[^\x00-\xff]/gi, "--").replace(/[wW]/gi, "--").length;
 }
 //字符串缩略
 function subStrBreviary(str,maxLength){
 	var realLength = getStrRealLength(str);
 	if(realLength <= maxLength){
 		return str;
 	}
 	//at least one half
 	var tmpStr = str.substring(0,maxLength/2);
 	var tmpLength = getStrRealLength(tmpStr);
 	if(maxLength - tmpLength < 1){
 		return tmpStr + "..";
 	}
 	//cut
 	for(var i = maxLength/2;i < realLength;i++){
 		if(maxLength - tmpLength < 1){
 			return tmpStr + "..";
 		}
 		tmpStr = tmpStr + str.charAt(i);
 		var tmpLength = getStrRealLength(tmpStr);
 	}
 }
 
subByeStr = function(s, n) {
	var r = /[^\x00-\xff]/g; // 其中x00-\xff代表非汉字编码
	var byteLen = s.replace(r, "**").length;	// 获取字符串的字节长度，其中replace(r, "**")表示讲一个中文字体换成两个英文符号
	if(byteLen <= n) return s; // 如果字节长度小于或者等于要截取的字节长度，直接返回原字符串     
	
	var m = Math.floor(n/2); // 因为要截取的字符串一定大于等于字符串的长度的一半，所以从字符串长度的一半开始
	for(var i=m; i<s.length; i++) {
        strLen = s.substr(0, i).replace(r, "**").length;	
		if (strLen == n) {       
			return s.substr(0, i); 
		} else if (strLen > n) {
			return s.substr(0, i-1); 
		}
	}
	
	return s;
}
 
// 格式化显示字符串
formateStr = function(name, regex, rowLen) {
	var array = name;
	if(regex) {
		array = name.split(regex);
	}
	
	var index = 0;
	var item = array[0];
	for(var i = index, len = array.length; i < len; i++) {
		
		for (var j = i + 1; j < len; j++) {
			var temp2 = array[j];
			var itemLen = item.replace(/[^\x00-\xff]/g, "**").length;
			var temp2Len = temp2.replace(/[^\x00-\xff]/g, "**").length;
			if (itemLen + temp2Len + 1 > rowLen) {
				if (itemLen > rowLen) {
					var temp1 = subByeStr(array[i], rowLen);
					item = array[i].substring(temp1.length, array[i].length);
					array[i] = temp1 + "<br />" + item;
					i = i - 1;
				} else {
					item = array[j];
					array[j - 1] = array[j - 1] + '<br />';
					i = j - 1;
				}
				break;
			} else {
				item = item + ' ' + array[j];
			}
		}
	}
	
	return array.join(' ');
};
 
 /**
  * 转义特殊字符,如：&gt &lt;转换为><
 * @param str
 * @return
 */
function unescapeHTML(str){
	 $tmp = $("<div></div>").html(str);
	 nodes = $tmp.contents();
	 nl = nodes.length;
	 if (nl > 0) {
	 var a = [];
	 for (var i=0, il=nl; i<il; i++) {
		 a.push(nodes[i].nodeValue);
	 }
	 return a.join('');
	 }
	 return '';
}
/**
 * 格式化数字.
 * @param num
 * @param exponent
 * @return
 * format("12345.67","#,###.00") 12,345.67
 * format("12345.67","￥#,###元0角0分") ￥12,345元6角7分
 */
function formatNumberPex(v, formatString){
	  v = v + "";
	  var parterns = formatString.split(".");
	  var numbers = v.split(".");
	    
	  var lparterns = parterns[0].split("");
	  var lparternsbak = parterns[0].split("");
	  var lnumbers = numbers[0].split("");
	    
	  var lkeep = "";
	  var rkeep = "";
	    
	  //得到左侧要替换的部分
	  var lplaces = [];
	  for(var i=0;i<lparterns.length;i++){
		  var parternchar = lparterns[i];
		  if (parternchar == "#" || parternchar == "0"){
			  lplaces.push(i);
		  }
	  }

	  //替换左侧，左侧有数字才要替换，以避免v = .99型的数字而产生错误
	  if (lnumbers[0] && lnumbers[0].length>0){
		  var numberIndex = lnumbers.length - 1;
		  var replaced = 0;  
		  for(var i=lplaces.length - 1;i>=0;i--){
			  replaced ++; //被替换的字符数量
			  var place = lplaces[i];
			  lparterns[place] = lnumbers[numberIndex];
			    
			  if (numberIndex == 0) {
				  break;
			  }
			  numberIndex--;
		  }
		    
		  //处理以#为第一个格式（#前可能有非0的其他串也在此范围）的格式串，对于以#开头的格式串，将不会截取数字串，要补齐
		  var lstartIdx = lplaces[0];
		    
		  if (lparternsbak[lstartIdx]=="#"){
			  if (lnumbers.length > replaced){
				  var idx = lnumbers.length - replaced;
				  for(var i=0;i<idx;i++){
					  lkeep += lnumbers[i];
				  }
				    
				  lparterns[lstartIdx] = lkeep + lparterns[lstartIdx];
			  }
		  }
	  }
	    
	  //替换右侧
	  if (parterns[1] && parterns[1].length > 0){
		  var rparterns = parterns[1].split("");
		  var rparternsbak = parterns[1].split("");
		    
		  if (numbers[1] && numbers[1].length>0){
			  var rnumbers = numbers[1].split("");
		
			  //得到右侧将要替换的部分
			  var rplaces = [];
			  for(var i=0;i<rparterns.length;i++){
				  var parternchar = rparterns[i];
				  if (parternchar == "#" || parternchar == "0"){
					  rplaces.push(i);
				  }
			  }
			    
			  var replaced = 0;  
			  for(var i=0;i<rplaces.length;i++){
				  replaced ++; //被替换的字符数量
				  var place = rplaces[i];
				  rparterns[place] = rnumbers[i];
			    
				  if (i==rnumbers.length - 1){
					  break;
				  }
			  }
			    
			  //处理以#结束的（#后有非0的串也在此范围）
			  var rlastIdx = rplaces[rplaces.length-1];
			  if (rparternsbak[rlastIdx]=="#"){
				  for(var i=replaced-1;i<rnumbers.length;i++){
					  rkeep += rnumbers[i];
				  }
			    
				  rparterns[rlastIdx] += rkeep;
			  }
		  }
	  }
	    
	  for(var i=0;i<lparterns.length;i++){
		  if (lparterns[i]=="#"){
			  lparterns[i] = "";
		  }
	  }
	    
	  var result = lparterns.join("");
	  if (parterns[1]){
		  for(var i=0;i<rparterns.length;i++){
			  if (rparterns[i] == "#"){
				  rparterns[i] = "";
			  }
		  }
	  	result += "." + rparterns.join("");
	  }
	    
	  //第一位不能为,号
	  if (result.substring(0,1)==","){
		  result = result.substring(1);
	  }
	    
	  //最后一位也不能为,号
	  if (result.substring(result.length-1)==","){
		  result = result.substring(0,result.length);
	  }
	  return result;
}

//显示操作信息（type：成功[yes或success],警告[warn或warning],失败[error或wrong]）
function show_msg_tips_batch_imp(type,msg){
	if(!type || !msg)
		return;
	if('success'==type || 'yes'==type)
		type='yes';
	if('warn'==type || 'warning'==type)
		type='warn';
	if('error'==type || 'wrong'==type)
		type='wrong';
	var classs = 'tips_'+type+'_msg';
	var tip = $("#tip_msg_box");
	tip.find("#tips_msg").html(msg);
	tip.find("#tips_msg").removeClass();	
	tip.find("#tips_msg").addClass(classs);
	tip.show();
	//tip.click(function(){$(this).hide();});
	tip.find(".tips_close").click(function(){$(tip).hide();});
}

var Sys = {};
function conifgBrowser(){
    var ua = navigator.userAgent.toLowerCase(); 
    var s; 
    (s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] : 
    (s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] : 
    (s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] : 
    (s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] : 
    (s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0; 
    //以下是调用 
    //if (Sys.ie) alert('IE: ' + Sys.ie); 
    //if (Sys.firefox) alert('Firefox: ' + Sys.firefox); 
    //if (Sys.chrome) alert('Chrome: ' + Sys.chrome); 
    //if (Sys.opera) alert('Opera: ' + Sys.opera); 
    //if (Sys.safari) alert('Safari: ' + Sys.safari); 
    //if(Sys.ie == '8.0')
};
conifgBrowser();