<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "https://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="format-detection" content="telephone=no">
<title>文件加载</title>
<link rel="stylesheet" type="text/css" media="screen" href="${resscmsns}/css_v5/plugin/jquery.alerts.css" />
<link rel="stylesheet" type="text/css" href="${resscmsns}/css_v5/pop.css" />
<link rel="stylesheet" type="text/css" href="${resscmsns}/css_v5/group/group.css" />
<script src="${resmod}/js/jquery.js" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function(){
	var count = parseInt($("#count").val());
	var pubId = $("#pubId").val();
	var des3Id = $("#des3Id").val();
	var nodeId = $("#nodeId").val();
	var ownerId =$("#ownerId").val();
	var groupId = $("#groupId").val();
	var params = "des3Id="+des3Id+"&groupId="+groupId+"&ownerId="+ownerId+"&snsNodeId="+nodeId;
	var pram={pubId:pubId};
	/* for(var i=0;i<2000;i++){

	showAwiat(des3Id,null,pubId,params,count,pram)
	//var val = window.setInterval(showAwiat(des3Id,null,pubId,params,count,pram),3000);
	} */
	var vals = window.setInterval(function (){showAwiat(des3Id,null,pubId,params,count,pram,vals)},5000);
})

function showAwiat(des3Id,obj,Id,params,count,pram,vals){
	var c=parseInt($("#count").val());
	var counts = c+1;
	$("#count").val(counts);
	$.ajax({
		
		url:"/pubweb/publication/view",
		type:"post",
		data:pram,
		dataType:"json",
		cache:"false",
		success:function(data){
			if(data.result==1){
				window.clearInterval(vals);
				window.self.location= "/scmwebsns/publication/view?"+params;
				//ScholarView.forceOpen("/scmwebsns/publication/view?"+params);
			}
			if(counts==20){
				$("#loadDynamic").hide();
				window.clearInterval(vals);
				$("#reminder").show();
			}
			
			return;
		},
		error:function(){
			return;
		}
		
	})
	
}

</script>
</head>
<body>
  <div class="morenews2" id="loadDynamic" style="width: 660px; margin-left: 150px">
    <img src="${resmod}/images/loading_img.gif" style="vertical-align: middle;" />&nbsp;&nbsp;数据加载中......
  </div>
  <div class="reminder" id="reminder" style="display: none;">
    <a href="#" onclick="document.location.reload()">点击此处重新加载</a>
  </div>
  <input type="hidden" name="des3Id" value="${des3Id}" id="des3Id">
  <input type="hidden" name="nodeId" value="${nodeId}" id="nodeId">
  <input type="hidden" name="groupId" value="${groupId}" id="groupId">
  <input type="hidden" name="ownerId" value="${ownerId}" id="ownerId">
  <input type="hidden" name="pubId" value="${gpubId}" id="pubId">
  <input type="hidden" name="count" value="0" id="count">
</body>
</html>