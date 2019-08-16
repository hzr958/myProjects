<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ressie }/css/unit.css" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Insert title here</title>
<script type="text/javascript">
var flag=true;
$(document).ready(function(){
    if($("#message").html().length>0){
        var warnmsg = $("#message").text().replace(/<\/?.+?>/g, "").replace(/[\r\n]/g,"");
//      warnmsg = warnmsg.replace("QQ","<br/>");
        $.scmtips.show("error",warnmsg); 
    }
	
    $("#sourceFile").change(function(){ 
        if($(this).val().length>0){
            $(".filedata").val($(this).val().substring($(this).val() .replace(/\\/g, '/').lastIndexOf('/') + 1));
        }
        if(($(this)[0].files[0].size)>31457280){
        	  flag=false;
        	  $.scmtips.show('warn','文件大小最大为30兆');
        }
    }); 
    
})
function checkradio(){ 
    var flag = false;
    var item = $("input[name=fileType][type=radio]:checked").val(); 
    if(item!=undefined && item.length>0){ 
        flag =true;
    } 
    return flag;
} 
   function nextStep(){
	   if(!checkradio()){
	        $.scmtips.show('warn','请选择文件类型');
	        return;
	    }
	   if($("#sourceFile").val().length<=0){
	        $.scmtips.show('warn','请选择需要导入的文件');
	        return;
	    }
	  if(!flag){
		  $.scmtips.show('warn','文件大小最大为30兆');
	  }
	   $("#mainForm").submit();
   }
   function openFile(){
	    window.open("/prjweb/project/getfile");
	}
</script>
</head>
<body>
  <form id="mainForm" method="post" action="${ctx}/prjweb/project/importfilelist" enctype="multipart/form-data">
    <span id="message" style="display: none"><s:actionmessage /></span>
    <div class="conter">
      <div id="con_five_1" style="display: block">
        <div class="channel-box_cont">
          <ul class="matter_conter_flow">
            <li class="hover"><div>
                <span class="channel-bg01">1</span>
                <p>选择文件</p>
              </div></li>
            <li><p class="channel-bt02"></p>
              <div>
                <span class="channel-bg01">2</span>
                <p>数据预览</p>
              </div></li>
            <li><p class="channel-bt02"></p>
              <div>
                <span class="channel-bg01">3</span>
                <p>导入成功</p>
              </div></li>
          </ul>
        </div>
        <div class="matter_file_conter">
          <p class="matter_file_top">您可以将其他科研项目管理系统（如：基金委ISIS系统）导出的项目数据以文件形式导入到本单位项目库中。</p>
          <ul class="martter_file_data">
            <li><span class="martter_file_data_source">数据来源：</span>
              <div class="js-demo-1">
                <label><input type="radio" name="fileType" value="SCMIRIS" /> 基金委ISIS系统资助项目（*.mdb）—
                  从基金委ISIS系统导出本单位的项目文件，再到此处导入。 </label><br /> <label><input type="radio" name="fileType"
                  value="SCMEXCEL" /> 科研之友Excel (*.xls) <a href="#" onclick="openFile();" class="download">下载</a>
                  空白模板，填写项目详细信息后，上传填写完毕的Excel 文件。 </label>
              </div></li>
            <li class="martter_file_data_browse"><span class="martter_file_data_source">文本文件：</span>
              <div class="martter-demo-2">
                <input type="text" class="martter_file_demo_paper filedata" readonly="readonly">
                <div class="sie_demo_browse">
                  <a href="#" class="martter-demo-browse">浏览</a> <input type="file" class="sie_matter_file"
                    id="sourceFile" name="sourceFile">
                </div>
              </div> <!--                     <div class="martter-demo-2"> --> <!--                         <p><input type="file" id="sourceFile" name="sourceFile" class="martter_file_demo_paper" value="浏览"></p> -->
              <!--                         <a href="#"  class="martter-demo-browse">浏览</a> --> <!--                     </div> -->
            </li>
            <li><a href="#" class="martter-demo-step" onclick="nextStep();">下一步</a></li>
          </ul>
        </div>
      </div>
      <div id="con_five_2" style="display: none"></div>
      <div id="con_five_3" style="display: none"></div>
      <div id="con_five_4" style="display: none"></div>
    </div>
  </form>
</body>
</html>