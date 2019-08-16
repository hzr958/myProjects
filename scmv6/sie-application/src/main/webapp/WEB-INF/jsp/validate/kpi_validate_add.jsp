<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="UTF-8">
  <title>科研验证</title>
  <link rel="stylesheet" href="${ressie }/css/administrator.css" />
  <link rel="stylesheet" href="${ressie }/css/reset.css" />
  <link rel="stylesheet" href="${ressie}/css/plugin/toast.css" />
  <script type="text/javascript" src="${ressie}/js/jquery-1.9.1.min.js"></script>
  <script type="text/javascript" src="${ressie}/js/jquery.js"></script>
  <script type="text/javascript" src="${ressie}/js/jquery.fileupload.js"></script>
  <script type="text/javascript" src="${ressie}/js/plugin/smate.toast.js"></script>
  <script type="text/javascript" src="${ressie}/js/plugin/scm-pc_filedragbox.js"></script>
  <script type="text/javascript" src="${resapp }/validate/validate.form.js"></script>
  <script type="text/javascript">
$(document).ready(function(){
    //登录框登录不刷新
    $("#login_box_refresh_currentPage").val("false");
    initFileUpload();
});

const initFileUpload=function (){
	
	    var data ={
	            "fileurl": "/application/validate/ajaxsubmitfile",
	            "filedata":{ fileDealType: "generalfile" },
	            "method":"click",
	            "type":"NotList",
	            "checktimeouturl":'/application/validate/ajaxcheckupload'
	        };
	    sieFileUploadModule.initialization($(".demo_file")[0], data);
};


</script>
</head>
<body>
  <form name="mainForm" id="mainForm" action="" method="post" enctype="multipart/form-data">
    <header>
    <div class="header__2nd-nav">
      <div class="header__2nd-nav_box" style="justify-content: flex-end;">
        <nav class="nav_horiz nav_horiz-container" style="margin-left: 944px; top: 0px;">
        <ul class="nav__list" scm_file_id="menu__list">
          <li class="nav__item item_selected" onclick="Validate.backList();">科研验证</li>
        </ul>
        <div class="nav__underline" style="width: 75px; left: 9px;"></div>
        </nav>
        <div class="header__2nd-nav_action-list">
          <a href="###" style="margin-right: 0px;"></a>
        </div>
      </div>
    </div>
    </header>
    <div class="message" style="margin-top: 65px;">
      <div class="message_conter">
        <div class="message_conter_left">
          <div class="seek2">
            科研验证<span class="ml8">在线文档验证</span>
          </div>
          <div class="SIE_psninfor-item" style="margin-top: 32px; margin-left: -26px;">
            <div class="w132 SIE_psninfor-item_left">
              <b style="margin-left: 29px;">*</b> <span>待验证文档：</span>
            </div>
            <div class="SIE_psninfor-item_right js-demo-1">
              <div class="SIE_psninfor-item_right-content"></div>
              <div class="demo_file" style="cursor:pointer;">
                <div class="fileupload__box"></div>
              </div>
            </div>
          </div>
          <div id="validateTip" style="display: none;justify-content: flex-start;align-items: center;">
            <i class="SIE-handin_hottip" style="margin: 0px;margin-left: 115px;"></i><label style="margin-left: 8px;font-size: 14px;color: red;"></label>
          </div>
          <div class="refer" style="margin-left: -6px;">
            <a href="###" onclick="Validate.doSubmit();" class="refer_1">提交</a> <a href="###"
              onclick="Validate.backList();" class="refer_2" style="margin-right: 540px;">返回</a>
          </div>
        </div>
        <div class="message_conter_right">
          <div class="seek2">
            支持的资助机构列表<span class="ml8"></span>
          </div>
          <div class="seek_1">
            <p><a href="https://isisn.nsfc.gov.cn" target="_blank" style="color: #2882d8 !important;">1、国家自然科学基金委会</a></p>
            <p><a href="http://ywgl.jxstc.gov.cn" target="_blank" style="color: #2882d8 !important;">2、江西省科技厅</a></p>
          </div>
          <div class="seek2">
            使用步骤<span class="ml8"></span>
          </div>
          <div class="seek_1">
            <p style="color: #999;">1、请进入上述资助机构系统</p>
            <p style="color: #999;">2、生成需要验证的PDF文档（已提交的申报项目、进展报告或结题报告）</p>
            <p style="color: #999;">3、将生成的PDF文档在此处上传，即刻进行科研验证，提前排查诚信风险</p>
          </div>
          
          <div class="seek2">
            注意事项<span class="ml8"></span>
          </div>
          <div class="seek_1">
            <p style="color: #999;">1、目前仅支持上述资助机构系统</p>
            <p style="color: #999;">2、PDF文件大小不得超过30M</p>
            <p style="color: #999;">3、如有疑问，可联系<a style="color: #2882d8 !important;" onclick="javascript:window.open('http://crm2.qq.com/page/portalpage/wpa.php?uin=800018382&amp;cref=${pageContext.request.requestURL}&amp;ref=&amp;pt=scholarmate kefu&amp;f=1&amp;ty=1&amp;ap=&amp;as=&amp;aty=&amp;a=', '_blank', 'height=544, width=644,toolbar=no,scrollbars=no,menubar=no,status=no');">在线客服</a></p>
          </div>
        </div>
      </div>
    </div>
  </form>
</body>
</html>