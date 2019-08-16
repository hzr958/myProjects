<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Title</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>人员列表</title>
<link rel="stylesheet" href="${ressie }/css/unit.css" />
<link rel="stylesheet" href="${ressie }/css/administrator.css" />
<link type="text/css" rel="stylesheet" href="${ressie}/css/plugin/jquery.thickbox.css" />
<script type="text/javascript" src="${ressie}/js/plugin/jquery.thickbox.resources.js"></script>
<script type="text/javascript" src="${ressie}/js/plugin/jquery.thickbox.min.js"></script>
<script type="text/javascript" src="${respsn}/person/ins.psn.import.js"></script>
<script type="text/javascript" src="${respsn}/person/ins.person.import.js"></script>
<script type="text/javascript" src="${respsn}/person/jquery.fileupload.js"></script>
<script type="text/javascript" src="${respsn}/person/jquery.filestyle.js"></script>
<script type="text/javascript" src="${respsn}/person/psn.fileupload.handler.js"></script>
<script type="text/javascript">
var tip1='<s:text name="user.import.warn.msg1" />';
var tip2='<s:text name="user.import.warn.msg2" />';
var tip3='<s:text name="user.import.warn.msg3" />';
var tip4='<s:text name="user.import.warn.msg4" />';
var tip5='<s:text name="user.import.warn.msg5" />';
var tip6='<s:text name="user.import.warn.msg6" />';
var tip7='<s:text name="user.import.warn.msg7" />';
var fileTypeNotMatch = "<s:text name='referencelist.fileFormateError'/>";
var msg = "${message}";
var dealing="${dealing}";
var uploadMaxSize = "<s:text name='file.upload.maxSize' />";

$(document).ready(function(){
	$("#progress_bar").thickbox({
        "ctxpath" : "/psnweb",
        "respath" : "${ressie}",
        "type" : "progressbar"
    });
	
	$("input[type='file']").change(function(){                      
	    if($(this).val().length>0){
	        $(".filedata").val($(this).val().substring($(this).val() .replace(/\\/g, '/').lastIndexOf('/') + 1));
	    } 
	}); 
	
	//如果还在跑任务处理数据 直接显示进度页面 
    if(dealing=='true'){
        $("#progress_bar").click();
    }else{
        manage.profileImport.showMsg();
    }
	
});

function needMail(){
    if($("input[id='isNeedMail']").is(':checked')){
           $("input[name='needMail']").val("true");
       }else{
           $("input[name='needMail']").val("false");
       }
}
//进度完成调用函数
function finish(){
    //人员导入进度完成后进入结果显示页面 tsz
    setTimeout(function(){
        location.href="/psnweb/person/importresult";
    },1000);
    
}

//获取处理进度方法
function getProgress(back){
    $.ajax( {
        url : '/psnweb/person/ajaximportprogress',
        type : 'post',
        dataType:'json',
        data : {"versionId":$("#versionId").val()},
        success : function(data) {
            //{"all":"100","already":"50","rate":"50%"}
            //系统监听出现异常 提示
            //跳转首页 并提示数据已经的处理 请稍后的进入此页面查看
            back(data);
        },
        error:function(data){
            back(data);
        }
    });
}


 </script>
</head>
<body>
  <div class="conter">
    <div id="con_five_1" style="display: block">
      <form name="mainForm" id="mainForm" method="post" action="${ctx}/profile/importPsn" enctype="multipart/form-data">
        <input type="hidden" id="needMail" name="needMail" value="" />
        <div class="matter_file_conter step_file_conter">
          <ul class="step_section">
            <li class="step_section_top">
              <ul class="step_file_left">
                <li class="channel-box_cont_1"><span class="channel-bg01">1</span></li>
              </ul>
              <div class="step_section_option">
                <p class="step_section_option_descend ftbold">
                  <i></i>下载人员导入Excel空白模板 <a href="#" onclick="manage.profileImport.downFile();"
                    class="martter-demo-browse ml10">下载模板</a>
                </p>
              </div>
            </li>
            <li class="step_section_conter"><img src="${ressie }/images/channel_bt.png" alt=""></li>
            <li class="step_section_top">
              <ul class="step_file_left">
                <li class="channel-box_cont_1"><span class="channel-bg01">2</span></li>
              </ul>
              <div class="step_section_option">
                <p class="step_section_option_descend ftbold">离线填写人员信息</p>
                <p class="f666 mt10">提示：把下载的Exce模板，离线填写人员信息，然后上传。</p>
              </div>
            </li>
            <li class="step_section_conter"><img src="${ressie }/images/channel_bt.png" alt=""></li>
            <li class="step_section_top">
              <ul class="step_file_left">
                <li class="channel-box_cont_1"><span class="channel-bg01">3</span></li>
              </ul>
              <div class="step_section_option">
                <p class="step_section_option_descend ftbold">上传填写完毕的人员信息</p>
                <p class="menu mt10">
                  <span class="ftbold mr20">上传文件：</span> <input type="text"
                    class="martter_file_demo_paper step_file_demo_paper filedata" readonly="readonly">
                <div class="martter-demo-browse ml10 step_file_select_conter" style="top: -31px; z-index: 100;">
                  浏览 <input type="file" id="filedata" name="filedata" class="step_file_select_input">
                </div>
                <!--  <a href="#" class="martter-demo-browse ml10">浏览</a> -->
                </p>
              </div>
            </li>
            <li class="step_section_conter pt20"><a href="#" onclick="manage.profileImport.uploadFile();"
              id="export_link" class="martter-demo-step finish_catalog_step">导入</a> <a href="/psnweb/person/maint"
              class="martter-demo-browse">返回列表</a></li>
          </ul>
        </div>
      </form>
    </div>
  </div>
  <iframe name="down_file_frame" id="down_file_frame" src="" style="display: none"></iframe>
  <input type="hidden" id="progress_bar" />
  <div id="importConfirmBox" style="visibility: hidden;">
    <%-- <div class="dialog_content" align="center">
           <p style="font-size:15px" align="left"><s:text name="user.import.warn.confirm"/></p>
            <p align="left"><input type="checkbox" id="isNeedMail" onclick="needMail();"/>&nbsp;<label for="isNeedMail"><s:text name="user.import.isNeedMail"/></label></p>
            
        </div> --%>
    <div class="sie_personnnel_checkbox h100 sie_imp_chk">
      <p class="sie_ball_manner">
        <s:text name="user.import.warn.confirm" />
      </p>
      <p align="left">
        <input type="checkbox" id="isNeedMail" onclick="needMail();" />&nbsp;<label for="isNeedMail"><s:text
            name="user.import.isNeedMail" /></label>
      </p>
    </div>
    <%-- 
        <div class="pop_buttom">
            <input id="rightCKB" type="checkbox" value="" style="display: none;" checked="checked"/>
            <a onclick="parent.$.Thickbox.closeWin();prcWin.openWin();" class="uiButton uiButtonConfirm text14"><s:text name="common.label.confirm"/></a>&nbsp;&nbsp;
            <a onclick="parent.$.Thickbox.closeWin();" class="uiButton text14 mright10"><s:text name="common.btn.cancel"/></a>
        </div> --%>
  </div>
</body>
</html>
