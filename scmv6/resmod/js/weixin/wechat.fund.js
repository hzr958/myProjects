var wechat = wechat||{};
wechat.fund = wechat.fund || {};

wechat.fund.ajaxrefreshfunds= function(){
   var data= {"flag":1}
   wechat.fund.ajaxrequest(data,wechat.fund.refreshfunds);
}

wechat.fund.refreshfunds=function(data){
	$("#listdiv").html("");
	$("#listdiv").html(data);
}
wechat.fund.ajaxrequest=function(data,callback){
	$.ajax({
		url:'/prjweb/wechat/findfunds',
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
   