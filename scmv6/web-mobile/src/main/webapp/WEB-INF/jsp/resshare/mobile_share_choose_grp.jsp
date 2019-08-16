<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css">
<link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/plugin/scm.pop.mobile.css">
<form action="/psn/share/edit" method="post" id = "mobile_share_form" onsubmit="return backToSharePageFromGrp('save', 'btnReturn');">
    <input type="hidden" value = "${vo.des3ResId }" id = "mobile_share_des3ResId" name = "des3ResId">
    <input type="hidden" value = "${vo.resType }" id = "mobile_share_resType" name = "resType">
    <input type="hidden" value = "${vo.shareTo }" id = "mobile_share_shareTo" name = "shareTo">
    <input type="hidden" value = "${vo.des3FriendIds }" id = "mobile_share_des3FriendIds" name = "des3FriendIds">
    <input type="hidden" value = "${vo.currentDes3GrpId }" id = "mobile_share_currentDes3GrpId" name = "currentDes3GrpId">
    <input type="hidden" value = "${vo.des3GrpId }" id = "mobile_share_des3GrpId" name = "des3GrpId">
    <input type="hidden" value = "${vo.shareText }" id = "mobile_share_shareText" name = "shareText">
    <input type="hidden" value = "${vo.fromPage }" id = "mobile_share_fromPage" name = "fromPage">
    <input type="hidden" value = "${vo.hideModule }" id = "hideModule" name = "hideModule">
    <input type="hidden" value = "${vo.des3DynId }" id = "mobile_share_des3DynId" name = "des3DynId">
</form>
<input type="hidden" value = "${vo.des3GrpId }" id = "old_mobile_share_des3GrpId" name = "old_des3GrpId">
<div class="paper__func-header">
     <span class="paper__func-header_function-left"  onclick="backToSharePageFromGrp('cancel', 'btnReturn')">取消</span>
     <span>选择群组</span>
     <span class="paper__func-header_function-right" onclick="MobileSmateShare.backShare()">确定</span>
</div>
  <%@ include file="/common/mobile/common_mobile_search_input.jsp" %> 
<div class="effort_list" style="width: 100%;z-index: 99;">
  <div class="paper_content-container main-list" style="min-height: 100%;">
    <div class="main-list__list item_no-padding" list-main="mobile_share_psn_or_grp_list"></div>
  </div>
</div>

<script type="text/javascript">
   $(function(){
     //处理返回上一页
      if (window.history && window.history.pushState) {
        setTimeout(function(){
          window.addEventListener("popstate", grpPageBackEvent, false);
        }, 1000);
      }
      window.history.pushState('forward', null, '#'); //在IE中必须得有这两行
      window.history.forward(1);
     //加载好友列表,并将已经勾选的选中
     MobileSmateShare.showChooseList($.trim($("#m_grp_file_share_search_key").val()),"grp");
     //初始化检索输入框
     var searchInputOptions = {
         "searchFunc": "MobileSmateShare.showChooseList(this.value,'grp')", //点击检索图标执行函数
         "placeHolder": "检索群组",
         "searchInputVal" : "${vo.searchKey }", //检索的字符串
         "needFilter": false, //是否需要显示过滤条件图标
     };
     commonMobileSearch.initSearchInput(searchInputOptions);
     
   })
</script>










