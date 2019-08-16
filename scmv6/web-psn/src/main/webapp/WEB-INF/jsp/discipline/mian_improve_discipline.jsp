<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<title>科研之友</title>
<link href="${resmod}/mobile/css/style.css" rel="stylesheet" type="text/css">
<link href="${resmod}/css_v5/css2016/public2016.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="${resscmwebsns }/resscmwebsns/js_v5/json2.js"></script>
<script type="text/javascript">

$(document).ready(function(){
	
	
	$(".fld li a").click( function(){
		
		var hasClass = $(this).attr("class");
		if(hasClass ==  undefined){
			$(".fld li a").removeAttr("class");
			$(this).attr("class" ,"cur");
			
			getSubArea( $(this).attr("id"));
		}else{
			 //$(this).removeAttr("class");
		}
	});
	// 选中第一个
	$(".fld li  #100").attr("class","cur");
	
	getSubArea(100);
	
	//保存
	$(".save_btn").click(
		function () {
			 var keysArr = [];
			 var keyStr = "";
			 var arr =  [] ;
			$(".hidden_data").each(function(i){
				keyStr = keyStr.concat($(this).val());
			});
			if(keyStr.length >0){
				keyStr = keyStr.substring( 0 ,keyStr.length-1);
			}
			arr = keyStr.split(";");
			for(var i = 0 ; i <arr.length ; i++){
				   keysArr.push({
			            'keys': arr[i]
			        });
			}
			var str_key = JSON.stringify(keysArr);
		    if(str_key == "[]") {//关键词为空
			        str_key = '[{"keys":""}]';//区分学科领域与关键词
			  }
		    var post_data = {
		        'strDisc': str_key,
				'anyUser':null
		    };
		    
			 $.ajax({
				url:"/psnweb/discipline/saveRegResearchDiscipline",
				type:"post",
				dataType:"json",
				data:post_data,
				success:function(data){
					if( data.result=='success'){
						
						
					}
				}
			});	
		}); 
	
	
		  
});   //end 





//获取子领域
function getSubArea( mainId){
	
	removeSubArea();
	var mainAreaId = mainId ;
	$.ajax({
		url:"/psnweb/improveDiscipline/ajaxSubAreas",
		type:"post",
		dataType:"json",
		data:"mainAreaId="+mainAreaId,
		success:function(data){
			if( data.result=='success'){
				var  nodeName = $("#"+mainAreaId).html();
				var lastIndex = nodeName.lastIndexOf("(");
				 if(lastIndex >0){
					 nodeName = nodeName.substring(0,lastIndex);
				 }
				$(".con_3").append('<h2>'+nodeName+'</h2>');
				$(".con_3").append(	'<ul class="fld_ej"></ul>');
				  jQuery.each(data.data, function(i,item){  
		              $(".con_3  ul").append('<li><a href="#"  id='+item.id+'>'+item.name+'</a></li>');
		              var matchSubArea = $("#select_"+mainAreaId).val().match(item.name);
		              if( matchSubArea != null){
		            	 
		            	  $("#"+item.id).attr("class" ,"hover");
		              }
		               
		            });  
				  addSubAreaClick();
			}
		}
	});	
}
//移除子域
function  removeSubArea(){
	var childs=$(".con_3").children().remove();    
	
}
//增加子标题的点击事件
function  addSubAreaClick(){
	$(".fld_ej  a").each(function(i){
		   $(this).click(changeState);
		 });
	
	};

//子领域，点击事件
function changeState(){
	var state = false ;
	state = $(this).hasClass('hover');
	if(state){//已经点击了  ,移除这个属性
		$(this).removeAttr("class");
		 var mainId = Math.floor($(this).attr("id")/100)*100;
		 var selectCount = $(".fld_ej  .hover").length ;
		 var nodeName = $("#"+mainId).text();
	
		 var lastIndex = nodeName.lastIndexOf("(");
		 if(lastIndex >0){
			 nodeName = nodeName.substring(0,lastIndex);
		 }
		 if(selectCount == 0){
			 $("#"+mainId).text(nodeName);
		 }else{
			 var selectName =  nodeName+"("+selectCount+")";
			 $("#"+mainId).text(selectName);
		 }
		 
		 //去除隐藏域的值
		 var  lastVal =  $("#select_"+mainId).val();
		 var  nowVal =   lastVal.replace($(this).text()+";" ,"") ;
		 $("#select_"+mainId).val(nowVal);
	
	}else{
		$(this).attr("class","hover");
		 var mainId = Math.floor($(this).attr("id")/100)*100;
		 var selectCount = $(".fld_ej  .hover").length ;
		 var nodeName = $("#"+mainId).text();
	
		 var lastIndex = nodeName.lastIndexOf("(");
		 if(lastIndex >0){
			 nodeName = nodeName.substring(0,lastIndex);
		 }
		 var selectName =  nodeName+"("+selectCount+")";
		 $("#"+mainId).text(selectName);
		 //添加隐藏域的值
		 var  lastVal =  $("#select_"+mainId).val();
		 var  nowVal =  lastVal+ $(this).text() +";";
		 $("#select_"+mainId).val(nowVal);
	}
} 

</script>
</head>
<body>
  <div class="con_2">
    <h2>
      完善学科领域：<span>请选择关注的学科领域</span>
    </h2>
    <ul class="fld">
      <s:iterator value="mainAreaList" id="mainArea" status="itStat">
        <li><a href="#" id="${id }">${name }</a> <input type="hidden" class="hidden_data" id="select_${id}">
        </li>
      </s:iterator>
    </ul>
    <div class="clear"></div>
  </div>
  <div class="con_3"></div>
  <div class="save">
    <input type="button" class="save_btn" value="保存"><a href="Retrieve-password.html"><input type="button"
      class="skip_btn" value="跳过"></a>
  </div>
</body>
</html>
