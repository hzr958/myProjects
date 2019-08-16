<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%><html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<head>
    <title></title>
    <meta charset="utf-8">
    <meta name="viewport" content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
    <link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/mobile.css">
    <link href="${resmod}/mobile/css/plugin/scm.pop.mobile.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${resmod}/js/jquery.js"></script>
    <script type="text/javascript" src="${resmod}/js/common/smate.common.js"></script>
    <script type="text/javascript" src="${resmod}/mobile/js/plugin/scm.pop.mobile.js"></script>
    <script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
    <script type="text/javascript">
      window.onload = function(){
        document.getElementsByClassName("new-edit_psntitle-body")[0].style.height =  window.innerHeight - 96 + "px";
        var containerele = document.getElementsByClassName("new-edit_psntitle-body")[0];
        containerele.style.height =  document.body.clientHeight -  96 + "px";
        window.onresize = function(){
            containerele.style.height =  document.body.clientHeight -  96 + "px";
            document.getElementsByClassName(" new-eadit_psn-information")[0].style.height = document.body.clientHeight / 2 + "px";
        }
    }
      function savePsnIntro(){
        $.ajax({
          url: "/psn/mobile/save/psnIntro",
          type: "post",
          dataType: "json",
          data: {
            "psnBriefDesc":$.trim($("#psnBriefDesc").val())
          },
          success: function(data){
              if(data.status == "success"){
                document.location.href="/psnweb/mobile/homepage";
              }else{
                  scmpublictoast("操作失败, 请稍后再试",1000);
              }
          }
      });
      }
    </script>
</head>
<body>
        <div class="new-edit_keword-header">
            <i class="material-icons" onclick="javascript:SmateCommon.goBack('/psnweb/mobile/homepage');">keyboard_arrow_left</i>
            <span class="new-edit_keword-header_title">个人简介</span> 
            <i class="material-icons"></i>
        </div>
        <div class="new-edit_psntitle-body" style="margin-top: 50px;  padding-top:20px;">
            <divc class=" new-eadit_psn-information">
               <textarea class="textarea_box new-eadit_psn-information_input" id="psnBriefDesc" style="overflow-y:auto;resize: none;" placeholder="请输入个人简介">${psnIntroContent }</textarea>
            </div>
        </div>
        
        <div class="new-edit_page-normal_footer" onclick="savePsnIntro();">保存</div>
</body>
</html>