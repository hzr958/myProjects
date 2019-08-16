<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${resmod }/css_v5/reset.css"/>
<link rel="stylesheet" href="${resmod }/css_v5/project/unit.css"/>
<link rel="stylesheet" href="${resmod }/css_v5/project/achievement_lt.css"/>
<link rel="stylesheet" href="${resmod }/css_v5/project/administrator.css"/>
  <link href="${resmod }/css/scmjscollection.css" rel="stylesheet" type="text/css" />
  <script type="text/javascript" src="${resmod }/js/jquery.js"></script>
  <script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
  <script type="text/javascript" src="${resmod }/js/smate.toast.js"></script>
  <script type="text/javascript" src="${resmod }/js_v8/grp/manage/grp.manage.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>群组管理</title>
<script type="text/javascript">
var flag=true;
var errorMsg = "${errorMsg}" ;
$(document).ready(function(){

    var setele = document.getElementsByClassName("new-success_save")[0];
    if(setele){
        setele.style.left = (window.innerWidth - setele.offsetWidth)/2 + "px";
        setele.style.bottom = (window.innerHeight - setele.offsetHeight)/2 + "px";
    }
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
        	  $.scmtips.show('warn','<s:text name="prj.filelist.overSize"/>');
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
    //BaseUtils.checkCurrentSysTimeout("/prjweb/ajaxtimeout", formCommit);
    formCommit();
}
function formCommit(){

   if($("#sourceFile").val().length<=0){
       scmpublictoast("请选择文件" , 2000) ;
        return;
    }
   if(!flag){
       scmpublictoast("文件大小不能超过30M" , 2000) ;
       return ;
   }
    var  fileName = $("#sourceFile").val().toLowerCase();
    var index =fileName.lastIndexOf(".")
    var fileType = fileName.substr(index+1);
    if(fileType != "xls" && fileType != "xlsx"){
        scmpublictoast("不支持上传此类型文件" , 2000) ;
        return ;
    }
   showProgress();
    setTimeout(function(){
        $("#mainForm").submit()
    },2000);
}
function showProgress(){
    $('.sie_searching_fx').css("display","block");
    $('#imp_pop_bg').css("display","block");
}
function openFile(){
	$("#down_file_frame").attr("src","/prjweb/project/getfile?xxtemp="+ (new Date().getTime().toString(36)));
//     window.open("/prjweb/project/getfile");
}

function checkDownload(){
    BaseUtils.checkCurrentSysTimeout("/prjweb/ajaxtimeout", openFile);
}
function continueImpPrjFile(){
    /**
     * 失败后重置导入类型与文件选择框
     */
    setTimeout(function(){
        $("#resultMsg").hide();
    },300);
};
</script> 
</head>
<body>
<form id="mainForm" method="post" action="/scmmanagement/grpmanage/exportgrplist" enctype="multipart/form-data">
<span id="message" style="display: none"><s:actionmessage/></span>
<div class="pop_bg" style="display: none;" id="imp_pop_bg"></div>
<div class="sie_searching_fx version-tip" style="display: none;">
    <img class="sie_upload2 icon_schedule" src="${resmod}/images/icon_schedule.gif" alt="">
    <p class="mt16 ofw3 hanggao24" >正在读取文件，请稍后</p>
<!--     <input type="submit" class="martter-demo-browse mt28 " value="取消" onclick="cancelJob();return false"> -->
</div>
<div class="conter mt80" style="" >
    <div id="con_five_1" style="display:block">
        <div class="ds_jc">
            <div class="step ds_f">
                <div class="step_one step_one-ing">
                    <span>1</span>
                    <p>选择文件</p>
                </div>
                <div class="step_one step_one-un">
                    <span>2</span>
                    <p>数据预览</p>
                </div>
                <div class="step_one step_one-un">
                    <span>3</span>
                    <p>导入成功</p>
                </div>
                <div class="step_up ds_c">
                    <span class="step_up-ing wd210">
                        <span class="step_up-ing01"></span>
                    </span>
                    <span class="step_one-un wd210"></span>
                </div>
            </div>
        </div>
        <div class="matter_file_conter">
            <p class="matter_file_top">你可以以文件形式导入到群组列表中。</p>
            <ul class="martter_file_data">
                <li><span class="martter_file_data_source">数据来源：</span>
                    <div class="js-demo-1 pt4">
                        <p class="hanggao24"><label><input type="radio" checked name="fileType" value="SCMEXCEL" /> &nbsp;科研之友Excel (*.xls、*.xlsx)
                          <a href="/pub/one/openfile?type=1&flag=11"  class="download">下载&nbsp;</a>
                          空白模板，填写群组详细信息后，上传填写完毕的Excel 文件。</label></p>
                    </div>
                </li>
                <li class="martter_file_data_browse">
                    <span class="martter_file_data_source">文件文件：</span>
                        <div class="martter-demo-2" style="justify-content: flex-start;">
                            <input type="text" class="martter_file_demo_paper filedata" readonly="readonly">
                            <div class="sie_demo_browse">
                                <a href="###"  class="martter-demo-browse">浏览</a>
                                <input type="file" class="sie_matter_file" id="sourceFile" name="sourceFile">
                            </div>
                    </div>
                </li>
                <li style="padding-left: 11px; margin-top: 32px;">
                    <a href="###" class="martter-demo-step" onclick="nextStep();" style="margin-left: 74px;">下一步</a>
                    <a href="/scmmanagement/grpmanage/main" class="martter-demo-browse ml12">返回列表</a>
                </li>
            </ul>
        </div>
    </div>
      <iframe name="down_file_frame" id="down_file_frame" src="" style="display:none"></iframe>
    <div id="con_five_2" style="display:none"></div>
    <div id="con_five_3" style="display:none"></div>
    <div id="con_five_4" style="display:none"></div>

</div>
    <c:if test="${errorMsg eq '-1'}" >
      <div class="background-cover" id="resultMsg" style="display: block;">
        <div class="new-success_save" id="new-success_save" style="">
          <div class="new-success_save-body">
            <div class="new-success_save-body_avator">
              <img src="${resmod}/smate-pc/img/fail.png">
            </div>
            <div class="new-success_save-body_tip">
              <span><s:text name="referencesearch.lable.contentFormat" /></span>
            </div>
            <div class="new-success_save-body_footer">
              <div class="new-success_save-body_footer-continue" onclick="continueImpPrjFile();"><s:text name="dialog.manageTag.btn.close"/> </div>
            </div>
          </div>
        </div>
      </div>
    </c:if>
</form>
</body>
</html>
