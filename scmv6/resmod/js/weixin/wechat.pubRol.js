var wechat = wechat||{};
wechat.pub = wechat.pub||{};

wechat.pub.ajaxrefreshpubs= function(){
   var data= {"selectTags":$("#selectTags").val(),"selectType":$("#selectType").val(),"timeOrder":$("#timeOrder").val(),"flag":1}
   wechat.pub.ajaxrequest(data,wechat.pub.refreshpubs);
}

wechat.pub.refreshpubs=function(data){
	 is_add_nav_bar(data)
	$("#listdiv").html("");
	$("#listdiv").html(data);
	$("#counts").val(0);
}


function  is_add_nav_bar(data){
	var havaPub = data.match("dataCount");
	if(havaPub!=null){
		var nav_bar_count = $(".nav_bar").size();
		if(nav_bar_count ==0){
			 $("body:first").append('<div class="nav_bar" onclick="checkbox()"> <input id="invite_co-authors" type="checkbox" checked="checked"><span>邀请我的合作者成为联系人</span></div>');
		}
	}
}

wechat.pub.ajaxuploadpubs=function(){
	// var nextId=$("#des3NextId").val();
	var nextId=null;
	 if(nextId==null||nextId==""){
		 var nextIds =document.getElementsByName("nextIdEx");
		 if(nextIds.length>0){
			 nextId=nextIds.length;
		 }else{
			 return;
		 }
	 }
	 var data= {"flag":1,"nextId":nextId}
	 wechat.pub.ajaxrequest(data,wechat.pub.uploadpubs);
	 $("#des3NextId").val("");
}

wechat.pub.uploadpubs=function(data){
	if(data.indexOf("未查到推荐论文记录")==-1){
		$("#listdiv").append(data);
	}
	if(parseInt($("#counts").val())==0&&data.indexOf("未查到推荐论文记录")!=-1){
		$("#listdiv").append(data);
		var count=parseInt($("#counts").val())+1;
		$("#counts").val(count);
	}
	
}

wechat.pub.ajaxrequest=function(data,callback){
	$.ajax({
		url:'/pub/confirmpublist',
		type : "post",
		dataType : "html",
		data :data,
		success:function(data){
			callback(data);
		},
		error:function(data){
			alert("请求数据出错!");
		}
	});
}
