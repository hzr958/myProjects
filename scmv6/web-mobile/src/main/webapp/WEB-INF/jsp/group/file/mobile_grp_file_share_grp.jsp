<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <meta name="viewport" content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css">
    <link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/plugin/scm.pop.mobile.css">
    
    <link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">

    <script type="text/javascript" src="${resmod }/js/jquery.js"></script>
    <script type="text/javascript" src="${resmod}/mobile/js/plugin/scm.pop.mobile.js"></script>
    <script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
    <script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
    <script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js"></script>
    <script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
    <script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_form.js"></script>
    <script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
    <script type="text/javascript" src="${resmod}/js/weixin/iscroll.js?version=1"></script>
    <script type="text/javascript" src="${resmod}/js/group/mobile/mobile_grp_file.js"></script>
    <script type="text/javascript">
       $(function(){
          //加载好友列表,并将已经勾选的选中
         MobileGrpFile.showChooseList($.trim($("#m_grp_file_share_search_key").val()),"grp");
         //初始化检索输入框
         var searchInputOptions = {
             "searchFunc": "MobileGrpFile.showChooseList(this.value,'grp')", //点击检索图标执行函数
             "placeHolder": "检索群组",
             "searchInputVal" : "${groupFile.searchKey }", //检索的字符串
             "needFilter": false, //是否需要显示过滤条件图标
         };
         commonMobileSearch.initSearchInput(searchInputOptions); 
       })
       
    </script>
</head>
<body>
<form action="/psn/mobile/resshare" method="post" id = "m_grp_file_choose_page">
    <input type="hidden" value = "${groupFile.des3ResId }" id = "m_grp_file_des3ResId" name = "des3ResId">
    <input type="hidden" value = "${groupFile.resType }" id = "m_grp_file_resType" name = "resType">
    <input type="hidden" value = "${groupFile.sharePlatform }" id = "m_grp_file_sharePlatform" name = "sharePlatform">
    <input type="hidden" value = "${groupFile.des3RecieverIds }" id = "m_grp_file_des3RecieverIds" name = "des3RecieverIds">
    <input type="hidden" value = "${groupFile.des3GrpIds }" id = "m_grp_file_des3GrpIds" name = "des3GrpIds">
    <input type="hidden" value = "${groupFile.des3GrpId }" id = "m_grp_file_des3GrpId" name = "des3GrpId">
    <input type="hidden" value = "${groupFile.leaveMsg }" id = "m_grp_file_leaveMsg" name = "leaveMsg">
</form>
<div class="paper__func-header">
     <span class="paper__func-header_function-left"  onclick="javascript:window.history.back()">取消</span>
     <span>选择群组</span>
     <span class="paper__func-header_function-right" onclick="MobileGrpFile.backShare()">确定</span>
</div>
<!-- <div class="paper__func-tool" style="top: 48px;">
    <div class="paper__func-tool-findbox" style = "width: 96%;">
        <i class="paper__func-search"></i>
        <input type="text" placeholder="检索群组" id = "m_grp_file_share_search_key" oninput="MobileGrpFile.showChooseList(this.value,'grp')">
    </div>
</div> -->
  <%@ include file="/common/mobile/common_mobile_search_input.jsp" %> 
<div class="effort_list" style="width: 100%;z-index: 99;">
  <div class="paper_content-container main-list" style="min-height: 100%;">
    <div class="main-list__list item_no-padding" list-main="m_grp_file_share_grp"></div>
  </div>
</div>
</body>
</html>