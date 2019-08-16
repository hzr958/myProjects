<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=EDGE, chrome=1">
<meta http-equiv="content-style-type" content="text/css">
<title>群组管理</title>
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<link href="/resmod/css/scmjscollection.css" rel="stylesheet" type="text/css">
<link href="${resmod }/smate-pc/new-confirmbox/confirm.css" rel="stylesheet" type="text/css">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script src="${resmod}/smate-pc/js/scmpc_dialogs.js" type="text/javascript"></script>
<script src="${resmod}/smate-pc/js/scmpc_chipbox.js" type="text/javascript"></script>
<script src="${resmod}/smate-pc/js/scmpc_form.js" type="text/javascript"></script>
  <script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js"></script>
  <script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
<script src="${resmod}/js/loadStateIco.js" type="text/javascript"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod }/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod }/js_v8/grp/manage/grp.manage.js"></script>
<script type="text/javascript">
var model="${model}";
var locale = '${locale}';
$(document).ready(function(){
    addFormElementsEvents();
    GrpManage.getMyGrpList();

})



</script>
</head>
<body>
  <header>
    <div class="header__2nd-nav" style="top: 0px">
      <div class="header__2nd-nav_box" style="justify-content: flex-end; position: relative;">
        <div class="searchbox__container main-list__searchbox" list-search="mygrp_list">
          <div class="searchbox__main" style="margin-left: 400px;">
            <input placeholder="查找群组">
            <div class="searchbox__icon material-icons"></div>
          </div>
        </div>
        <nav class="nav_horiz nav_horiz-container" style="position: absolute; top: 0px; right: 175px;">
          <ul class="nav__list" scm_file_id="menu__list">
            <li class="nav__item item_selected">群组列表</li>
          </ul>
          <div class="nav__underline"></div>
        </nav>
        <div class="header__2nd-nav_action-list" style="flex-grow: 0;">
          <button class="button_main button_primary-reverse" onClick="GrpManage.exportGrp();">
            批量导入群组
          </button>
        </div>
      </div>
    </div>
  </header>
  <div class="module-home__box dev_myfile_item">
    <!--我的群组列表模块  -->
    <%@ include file="/WEB-INF/grp_management/grp_item.jsp"%>
  </div>


   <!--  检索基准库成果弹框-->

  <div class="dialogs__box dialogs__childbox_limited-biggest"
       style="width: 600px;" id="select_pdwh_pub_import" dialog-id="search_pdwhpub_import_dialog"
       flyin-direction="bottom">
    <div class="dialogs__childbox_fixed" id="id_pdwhpub_header">
      <div class="dialogs__header">
        <div class="dialogs__header_title">
          基准库的论文
        </div>
        <div class="dialogs__header_searchbox" style="margin-right: 64px;">
          <div class="searchbox__container main-list__searchbox" list-search="mypublist">
            <div class="searchbox__main">
              <input placeholder="检索论文" id="search_pdwhpub_key">
              <div class="searchbox__icon material-icons" onclick="GrpManage.showPdwhPubList()"></div>
            </div>
          </div>
        </div>
        <i class="list-results_close" onclick="GrpManage.hidePubUI();"></i>
      </div>
    </div>
    <div class="dialogs__childbox_adapted" style="height: 560px;">
      <div class="main-list__list" id="pdwhpublistId" list-main="pdwhpublist">
        <!-- 我的成果列表 -->
      </div>
    </div>
    <div class="dialogs__childbox_fixed">
      <div class="dialogs__footer">
        <button class="button_main button_primary-reverse" disabled onclick="GrpManage.savePdwhpubToGrps(this);">
          确定
        </button>
      </div>
    </div>
  </div>

</body>
</html>
