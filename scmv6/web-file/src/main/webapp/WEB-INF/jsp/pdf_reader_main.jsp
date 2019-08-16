<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "https://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/resscmwebsns/js_v5/jquery.js"></script>
<script type="text/javascript" src="/resmod/js/plugin/jquery.fileupload.js"></script>
<title>pdf读取</title>
<script type="text/javascript">
function ajaxFileUpload(){

	$.ajaxFileUpload({
			url:"/fileweb/fileupload",
			secureuri:false,
			fileElementId:'filedata',
			data:{
			},
			dataType: 'json',
			success: function (data) {
				alert(JSON.stringify(data));
				$("#content").html(JSON.stringify(data));
			},
			error:function(data){
				
			}
	});
}
</script>
</head>
<body>
  这是pdf读取内容界面
  <br />
  <span class="cu12 discuss_tit Fleft">选择文件</span>
  <input type="file" id="filedata" name="filedata" multiple="multiple" />
  <br />
  <a id="btnUpload2" onclick="ajaxFileUpload();" class="uiButton uiButtonConfirm text14" href="javascript:void(0)">预览内容</a>
  <div id="content"></div>
</body>
</html>