/**
 * @author zzx
 * 自动填充
 * //需要引入cursorPositionPlugins.js
 * //需要引入ajaxparamsutil.js
 * ---------------------------------------------------------------------------
 * 吃货的修炼目标是：有鳄鱼的咬合力，有蟒蛇的吞咽力，有老虎的撕啃力，有鲸鲨的喝灌力，有鸭子的消化力，有蚂蚁的减肥力。
 * -------------------------------------------------
 */
var autocompleteword =autocompleteword||{};
autocompleteword.getWordList = function(obj,options){
	var defaults = {
			url:"/psnweb/friend/ajaxautofriendnames",
			data:{
				q:"" //检索的字段
			},
			myfunction_show:autocompleteword.listShow, //自定义成功回调展示方法
			myfunction_enter:autocompleteword.listEnter, //自定义回车确认方法
			mySelected:".dialogs_invitePsn_chips_input",  //触发的元素
			myClass_checked:"checked",
			myLiClass_list:"ac_even",
			myUlClass_parent:"ac_results"
		};
	if (typeof options != "undefined") {
		defaults=$.extend({},defaults, options);
    }
	obj.on("keyup", defaults['mySelected'], function(event){//查询数据
		var code = event.keyCode;
		if(code!=40&&code!=38&&code!=13){
		autocompleteword.obj = $(this);
		//触发的元素的位置
		autocompleteword.mySelectedTop = autocompleteword.obj[0].getBoundingClientRect().top;
		autocompleteword.mySelectedLeft = autocompleteword.obj[0].getBoundingClientRect().left;
		autocompleteword.arr = obj.find(defaults['mySelected']).getCursorPositionSubStr();//获取检索条件
		if(autocompleteword.arr != null && typeof autocompleteword.arr != "undefined"&&autocompleteword.arr[0]!=""){//如果获取到光标前的数据则赋给q并执行查询
			defaults.data.q =$.trim(autocompleteword.arr[0]);
			ajaxparamsutil.doAjax(
					defaults['url'],
					defaults['data'],
					defaults['myfunction_show']
					);
		}else{//否则清空之前的列表
			$("."+defaults['myUlClass_parent']).remove();
		}
		}
	});
	obj.on("keydown", defaults['mySelected'], function(event){//展示预选列表选择元素（按上下时选择列表中的数据，回车确认）
		var code = event.keyCode;
		var lis = $("."+defaults['myUlClass_parent']).find("."+defaults['myLiClass_list']);
		if(lis!=null&& typeof lis != 'undefined'&&lis.length>0||code==13){
			autocompleteword.liIndex=-1;
			$.each(lis,function(i,n){
				if($(n).hasClass(defaults['myClass_checked'])){
					autocompleteword.liIndex=i;
				}
			});
			lis.removeClass(defaults['myClass_checked']);
			if(code==13){//回车事件处理
				autocompleteword.stopDefault(event);
				defaults['myfunction_enter'](autocompleteword.obj.getCursorPositionSubStr());
				return;
			}else if(code==40){//↓
				autocompleteword.stopDefault(event);
				autocompleteword.liIndex=autocompleteword.liIndex+1==lis.length?0:autocompleteword.liIndex+1;//列表
			}else if(code==38){//↑
				autocompleteword.stopDefault(event);
				autocompleteword.liIndex=autocompleteword.liIndex-1<-1?lis.length-1:autocompleteword.liIndex-1;
			}else{
				return;
			}
			if(autocompleteword.liIndex!=-1){
				
			lis.eq(autocompleteword.liIndex).addClass(defaults['myClass_checked']);
			}
		}
	});
	obj.on("blur", defaults['mySelected'], function(e){//失去焦点清空预选列表
		//点击其他地方关闭
	    if (e && e.stopPropagation) {//非IE  
	        e.stopPropagation();  
	    }  
	    else {//IE  
	        window.event.cancelBubble = true;  
	    } 
	    $(document).click(function(){$("."+defaults['myUlClass_parent']).remove();});
	});
	obj.on("focus", defaults['mySelected'], function(){//获得焦点
	});
	
}
//取消默认事件
autocompleteword.stopDefault = function(e){
	//如果提供了事件对象，则这是一个非IE浏览器 
	if ( e && e.preventDefault ) 
	//阻止默认浏览器动作(W3C) 
	e.preventDefault(); 
	else
	//IE中阻止函数器默认动作的方式 
	window.event.returnValue = false; 
}
//获取选中的列表数据（autocompleteword.liIndex 来源于选中的li的索引）
autocompleteword.getValue = function(){
	var myValue = "";
	$("li[class='"+defaults['myLiClass_list']+"']").each(function(i,obj){
		if(autocompleteword.liIndex==i){
			myValue = $(obj).text();
			return;
		}
	});
	return $.trim(myValue);
}
//----------------------------------以下纯属例子------详细调用可以参考 --bjsns--inviteGroupMember.jsp-----------------------------------------------------------------------------------------------
/**
 * 例子方法，自定义展示预选列表，可以模仿构造
 * ---------------------------------
 * 必带的属性：ac_results ac_even checked
 * ---------------------------------
 */
autocompleteword.listShow = function(data){
	$(".ac_results").remove();
	if(data!=null&&typeof data != "undefined"&&data.length>0){
	var showList= "<div class='ac_results' style='position: absolute; width: 200px; top: "+(autocompleteword.mySelectedTop-50)+"px; left: "+(autocompleteword.mySelectedLeft-560)+"px;z-index:z-index: 200001;'>" +
			"<ul>";
	$.each(data,function(i,obj){
		if(i==0){
			showList+="<li class='ac_even checked'>"+obj.name+"</li>";
		}else{
			showList+="<li class='ac_even'>"+obj.name+"</li>";
		}
	});
	showList+="</ul></div>";
	$("#invitePsn_psnList_check").after(showList);
	}
}
/**
 * 例子方法，自定义回车事件，可以模仿构造
 * ---------------------------------
 * 必带的属性：
 * ---------------------------------
 */
autocompleteword.listEnter = function(arr){
	if(arr!=null&&arr[0]!=""){
		if(autocompleteword.getValue() !=""){
			arr[0]=autocompleteword.getValue(); 
		}
		var div_psn = "" +
		"<div class='chips_normal'>" +
			"<div class='content' >"+$.trim(arr[0])+"</div>" +
			"<div class='delete'>" +
				"<i class='material-icons' onclick='Group.removeDoc(this,event)' myTime=''>&#xe5cd;</i>" +
			"</div>" +
		"</div>";
		$("#myEdit").before(div_psn);
		var div_Edit = "<div id='myEdit' type='text' class='dialogs_invitePsn_chips_input' contenteditable='true'>"+arr[1]+"</div>";
		$("#myEdit").remove();
		setTimeout(function(){
			$("#invitePsn_psnList_check").append(div_Edit);
			$("#myEdit").focus();
		},1);
		$(".ac_results").remove();
	}
	
}