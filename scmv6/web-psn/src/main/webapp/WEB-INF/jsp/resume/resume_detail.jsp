<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>论文推荐</title>
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta charset="UTF-8">
<meta http-equiv="content-style-type" content="text/css">
<link href="${resmod}/smate-pc/css/scm-newpagestyle.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod}/js/homepage/homepage_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/homepage/psn.homepage.js"></script>
<script type="text/javascript" src="${resmod}/js/resume/psn.cv_${locale }.js"></script>
<script type="text/javascript" src="${resmod}/js/resume/psn.resume.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_form.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_dialogs.js"></script>
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
$(function(){
	PsnResume.loadPsnResumeModules();
});
</script>
</head>
<body>
  <input type="hidden" name="des3ResumeId" id="des3ResumeId" value="${des3ResumeId }" />
  <input type="hidden" name="des3CnfId" id="des3CnfId" value="${des3CnfId }" />
  <input type="hidden" name="resumeName" id="resumeName" value="${resumeName }" />
  <input type="hidden" name="baseInfoModuleStatus" id="baseInfoModuleStatus" value="${baseInfoModuleStatus }" />
  <input type="hidden" name="workModuleStatus" id="workModuleStatus" value="${workModuleStatus }" />
  <input type="hidden" name="eduModuleStatus" id="eduModuleStatus" value="${eduModuleStatus }" />
  <input type="hidden" name="prjModuleStatus" id="prjModuleStatus" value="${prjModuleStatus }" />
  <input type="hidden" name="pubModuleStatus" id="pubModuleStatus" value="${pubModuleStatus }" />
  <input type="hidden" name="otherPubModuleStatus" id="otherPubModuleStatus" value="${otherPubModuleStatus }" />
  <div class="new__resume-header_title">
    <div class="new__resume-tool" style="width: 1180px; margin: 0 auto;">
      <div class="new__resume-cancle" onclick="PsnResume.toBack(this);">
        <s:text name="resume.back" />
      </div>
      <div class="new__resume-export" onclick="PsnResume.exportCV('${des3ResumeId }','nfsc',this);">
        <s:text name="resume.export" />
      </div>
    </div>
  </div>
  <div class="new__resume-container" style="margin-top: 56px;">
    <!-- 简历标题 begin -->
    <%@ include file="resume_name.jsp"%>
    <!-- 简历标题  end -->
    <div class="new__resume-psn__item new__resume-psn__head-item" id="psn_base_info_div">
      <div class="new__resume-psn__infor" style="max-width: 92.5%;" id="baseinfo"></div>
      <!-- 个人信息 -->
      <div class="dialogs__box oldDiv" style="width: 480px;" dialog-id="editPsnInfoBox" cover-event="hide"
        id="editPsnInfoBox"></div>
      <div class="new__resume-edit__icon-edit_container" onclick="PsnResume.getpsninfo()">
        <div class="new__resume-edit__icon-edit" id="psnInfoEditIcon"></div>
        <div class="new__resume-edit__icon-edit_title">
          <s:text name="resume.edit" />
        </div>
      </div>
    </div>
    <div id="resume_education">
      <!-- 教育经历 -->
    </div>
    <div id="resume_workinfo">
      <!-- 工作经历 -->
    </div>
    <!-- 简历-项目模块 begin -->
    <div id="resume_prj" class="new__resume-list__bottom"></div>
    <!-- 简历-项目 end -->
    <!-- 成果模块 begin -->
    <%@ include file="psn_resume_pub.jsp"%>
    <!-- 成果模块 end -->
    <div style="height: 32px;"></div>
  </div>
</body>
</html>