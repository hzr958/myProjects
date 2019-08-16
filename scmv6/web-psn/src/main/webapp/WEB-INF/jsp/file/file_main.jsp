<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=EDGE, chrome=1">
<meta http-equiv="content-style-type" content="text/css">
<title>科研之友</title>
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<link href="/resmod/css/scmjscollection.css" rel="stylesheet" type="text/css">
<link href="${resmod }/smate-pc/new-confirmbox/confirm.css" rel="stylesheet" type="text/css">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script type="text/javascript" src="${resmod}/js/file/file_main.js"></script>
<script type="text/javascript" src="${resmod}/js/file/file_main_${locale }.js"></script>
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="${resmod }/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<script type="text/javascript" src="${resmod }/js/smate.share.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script src="${resmod}/smate-pc/js/scmpc_dialogs.js" type="text/javascript"></script>
<script src="${resmod}/smate-pc/js/scmpc_chipbox.js" type="text/javascript"></script>
<script src="${resmod}/smate-pc/js/scmpc_form.js" type="text/javascript"></script>
<script src="${resmod}/js/loadStateIco.js" type="text/javascript"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod }/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod }/smate-pc/new-confirmbox/confirm.js"></script>
<script type="text/javascript">
var model="${model}";
var locale = '${locale}';
$(document).ready(function(){
	VFileMain.menuClick(model);
	addFormElementsEvents();
	var tiplist = document.getElementsByClassName("input-custom-style");
 	for(var i = 0; i < tiplist.length; i++){
 		tiplist[i].onclick = function(){
 			if(	document.getElementsByClassName("drawer-batch__tip-container").length!=0){
 				document.getElementsByClassName("drawer-batch__tip-container")[0].style.display="block";
 			}
 		}
 	}
	
	if(document.getElementsByClassName("drawer-batch__box").length!=0){
        document.getElementsByClassName("drawer-batch__box")[0].onmousedown=function(){
        	$(".drawer-batch__tip-container").remove();
		}
	}

    var total = document.getElementsByClassName("header__box")[0].offsetWidth;
    var parentleft = document.getElementsByClassName("header__nav")[0].offsetLeft;
    var subleft  = document.getElementsByClassName("header-nav__item-bottom")[0].offsetWidth;
})
//初始化 分享 插件
function initSharePlugin(obj){
		if(SmateShare.timeOut && SmateShare.timeOut == true)
			return;
	    $("#share_to_scm_box").find(".nav__underline").hide();
		$(obj).dynSharePullMode({
			'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)',
			'styleVersion' : 10
		});
		$("#share_site_div_id").find(".inside").click();
		var $li = $("#share_to_scm_box").find("li");
		$li.eq(0).hide();
		$li.eq(2).hide();
		$li.eq(1).click();
		$("#share_to_scm_box").find(".nav__underline").show();
};


</script>
</head>
<body>
  <header>
    <div class="header__2nd-nav">
      <div class="header__2nd-nav_box" style="justify-content: flex-end; position: relative;">
        <nav class="nav_horiz nav_horiz-container" style="position: absolute; top: 0px; right: 175px;">
          <ul class="nav__list" scm_file_id="menu__list">
            <li class="nav__item item_selected" onclick="VFileMain.reloadMyFileList();"><s:text
                name='apps.files.myFiles' /></li>
          </ul>
          <div class="nav__underline"></div>
        </nav>
        <div class="header__2nd-nav_action-list" style="flex-grow: 0;">
          <%-- <a onClick="VFileMain.showShareRecordsUI(this)" style="margin-right: 0px;"><s:text
              name='apps.files.showShareRecords' /></a> --%>
          <button class="button_main button_primary-reverse" onClick="VFileMain.addPsnFile(this)">
            <s:text name='apps.files.addFile' />
          </button>
        </div>
      </div>
    </div>
  </header>
  <div class="module-home__box dev_myfile_item">
    <!--我的文件模块  -->
    <%@ include file="/WEB-INF/jsp/file/myfile_item.jsp"%>
  </div>
  <div class="module-home__box dev_filerecommend_item" style="display: none;">
    <!-- 文件推荐模块 -->
    <%@ include file="/WEB-INF/jsp/file/filerecommend_item.jsp"%>
  </div>
  <!-- 文件分享记录模块 -->
  <%@ include file="/WEB-INF/jsp/file/sharerecords_item.jsp"%>
  <jsp:include page="/common/smate.share.jsp" />
  <jsp:include page="/WEB-INF/jsp/file/file_eidt.jsp" />
  <div class="dialogs__box  dialogs__childbox_limited-big" dialog-id="psn_file_upload_file" flyin-direction="bottom" style="width: 480px"
    id="psn_file_upload_file_id">
    <div class="dialogs__childbox_fixed">
      <div class="dialogs__header">
        <div class="dialogs__header_title">
          <s:text name='apps.files.addFile.uploadTitle' />
        </div>
      </div>
    </div>
    <div class="dialogs__childbox_adapted" style="overflow-y: inherit;">
      <div class="dialogs__content global__padding_24" style="padding-bottom: 0px !important" id="fileupload">
        <form enctype="multipart/form-data" method="post">
          <div style="height: 160px; margin-bottom: 12px;">
            <div class="fileupload__box" maxlength="10"
        maxclass="psn_file_upload"></div>
          </div>
          
          <div class="form__sxn_row-list" style=" min-height: 48px; width: 100%; margin: 8px 0px">
          
          </div>
          
          <div class="form__sxn_row">
            <div class="input__box">
              <label class="input__title" style="margin-bottom: 6px;"><s:text name='apps.files.addFile.desc' /></label>
              <div class="input__area">
                <textarea id="psn_file_upload_file_content" class="dev_input-edit-area" maxlength="200"></textarea>
                <div class="textarea-autoresize-div"></div>
              </div>
              <div class="input__helper"></div>
            </div>
          </div>
          
        </form>
      </div>
    </div>
    <div class="dialogs__childbox_fixed">
      <div class="dialogs__footer">
        <button class="button_main button_primary-reverse" id="uploadfilebuttonId"
          onclick="VFileMain.uploadFileButton(this);">
          <s:text name='apps.files.addFile.upload' />
        </button>
        <button class="dev_input-footer_delete  button_main " onclick="VFileMain.cancleUploadFile();">
          <s:text name='apps.files.addFile.cancel' />
        </button>
      </div>
    </div>
  </div>
  <div class="drawer-batch__tip-container">
    <s:text name='apps.files.prompt' />
  </div>
</body>
</html>
