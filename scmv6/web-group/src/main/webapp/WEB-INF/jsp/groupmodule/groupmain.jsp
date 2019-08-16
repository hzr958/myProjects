<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/dialog.css" />
<!--Let browser know website is optimized for mobile-->
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/ajaxparamsutil.js"></script>
<script type="text/javascript" src="${resmod}/js/cursorPositionPlugins.js"></script>
<script type="text/javascript" src="${resmod}/js/ajaxparamsutil_${locale}.js"></script>
<script type="text/javascript" src="${ressns}/js/group/group.js"></script>
<script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
<script type="text/javascript" src="${ressns}/js/group/group.publication.js"></script>
<script type="text/javascript" src="${ressns}/js/group/group.publication_${locale}.js"></script>
<script type="text/javascript" src="${resmod }/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="${resmod }/js/plugin/jquery.browser.tips.js"></script>
<script type="text/javascript" src="${ressns}/js/group/tages-main.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/js2016/action.js"></script>
<script type="text/javascript" src="${resmod }/js/myAutoComplete.js"></script>
<script type="text/javascript" src="${resdyn}/dyn/group/group.dynamic.pc.js"></script>
<script type="text/javascript" src="${resdyn}/dyn/group/group.dynamic.list.pc.js"></script>
<script type="text/javascript" src="${resdyn}/dyn/group/group.dynamicCommon_${locale }.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/grp/grp.dynamic.list.opendetail.pc.js"></script>
<script type="text/javascript" src="${resdyn}/dyn/group/group.select.pub.pc.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<script type="text/javascript" src="${resscmsns }/js_v5/plugin/jquery.sharePullMode.js"></script>
<script type="text/javascript" src="${resmod}/js/sharebutton.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/no.result.prompt.language.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/plugin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/plugin/dialog.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/plugin/jquery.textarea.autoresize.js"></script>
<script type="text/javascript" src="${resmod }/js/smate.toast.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/plugin/jquery.proceeding.js"></script>
<link rel="stylesheet" type="text/css" href="${resmod}/css/smate.scmtips.css" />
<link type="text/css" rel="stylesheet" href="${resscmsns}/css_v5/plugin/jquery.thickbox.css" />
<style type="text/css">
.mark_icon01, .mark_icon02 {
  width: 16px;
  height: 16px;
  background: url("${resmod}/interconnection/images/icon.png") no-repeat;
  display: inline-block;
  vertical-align: middle;
}

.mark_icon01 {
  background-position: -52px 0;
}

.mark_icon02 {
  background-position: -52px -26px;
}

#pending:hover {
  border: 1px solid #ddd;
  border-radius: 3px;
  color: #333;
}
</style>
<script type="text/javascript" src="${resscmsns}/js_v5/plugin/jquery.scmtips.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/plugin/jquery.thickbox.resources.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/plugin/jquery.thickbox.min.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/plugin/jquery.fileupload.js"></script>
<script type="text/javascript" src="${ressns}/js/fulltext/jquery.fulltext.request.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.scmtips.js"></script>
<script type="text/javascript">
	var searchKey='${searchKey}';
	$(document).ready(
			function() {
				if ($("#oldToGroupPending").val() == "oldtogrouppending") {
					var url = window.location.href;
					history.pushState("", "title", url.substr(0, url.lastIndexOf("&")));
					$("#targetToPending").val("targetToPending");
					$("#membermenu").find("a").click();

				} else{
					// 下面有判断模块 跳转
					//Group.selectModule('${des3GroupId}', '${targetModule}');
					//群组文件和群组成果邮件跳进来到当前文件和当前成果
					if ('${searchKey}' != '') {
						 if ('${targetModule}' == 'file') {
							$("#fileSearch").val('${searchKey}');

				          } 
					}
				}
				
			/* 	//群组文件和群组成果邮件跳进来到当前文件和当前成果
				if ('${searchKey}' != '') {
					 if ('${toModule}' == 'file') {
						$("#fileSearch").val('${searchKey}');
					} 
					 if ('${toModule}' == 'pub') {
							$("#input_default01").val('${searchKey}');
							Group.searchGroupPub();

						} 

					} */

				

				if($("#toModule").val() == "member"){ 
					Group.selectModule('${des3GroupId }', 'member');
				}else if( $("#toModule").val() == "file"){
					
					Group.selectModule('${des3GroupId }', 'file');
				}else if($("#toModule").val() == "pub"||$("#_backType").val() == 3){ 
					
					Group.selectModule('${des3GroupId }', 'pub');
				}else{
					Group.selectModule('${des3GroupId }', 'brief');
				}
			});
	
	
</script>
</head>
<body class="grey_bg">
  <input type="hidden" id="toModule" name="toModule" value="<c:out value='${toModule}'/>" />
  <input type="hidden" id="submodule" name="submodule" value="<c:out value='${submodule}'/>" />
  <!--  返回到群组成果专用的隐藏域 -->
  <input type="hidden" id="gp_screenRecords" name="gp_screenRecords" value="<c:out value='${screenRecords}'/>" />
  <input type="hidden" id="gp_screenPubTypes" name="gp_screenPubTypes" value="<c:out value='${screenPubTypes}'/>" />
  <input type="hidden" id="gp_screenYears" name="gp_screenYears" value="<c:out value='${screenYears}'/>" />
  <input type="hidden" id="gp_orderBy" name="gp_orderBy" value="<c:out value='${page.orderBy}'/>" />
  <input type="hidden" id="gp_searchKey" name="gp_searchKey" value="<c:out value='${searchKey}'/>" />
  <input type="hidden" id="gp_pageNo" name="gp_pageNo" value="<c:out value='${page.paramPageNo}'/>" />
  <input type="hidden" id="gp_pageSize" name="gp_pageSize" value="<c:out value='${page.pageSize}'/>" />
  <input type="hidden" id="gp_groupPubEdit" name="gp_groupPubEdit" value="<c:out value='${groupPubEdit}'/>" />
  <input type="hidden" id="_backType" name="backType" value="<c:out value='${backType}'/>" />
  <input type="hidden" id="des3GroupId" name="des3GroupId" value="<iris:des3 code='${form.groupId}'/>" />
  <input type="hidden" id="des3PsnId" name="des3PsnId" value="<iris:des3 code='${form.psnId}'/>" />
  <input type="hidden" id="sumMember" name="sumMember" value="${groupStatistics.sumMembers}" />
  <input type="hidden" id="oldToGroupPending" value="${oldToGroupPending}" />
  <input type="hidden" name="groupImgUrl" value="${groupPsn.groupImgUrl }" />
  <input type="hidden" id="_groupName" value="${groupPsn.groupName }" />
  <input type="hidden" id="current_groupCategory" value="${groupPsn.groupCategory }" />
  <input type="hidden" id="current_add_groupPub_psn" name="current_add_groupPub_psn" />
  <%-- 	<input type="hidden" id="_groupOpenType" name="GroupOpenType"  value=" <s:if test='groupInvitePsn==null&&groupPsn.openType!="P"'>O</s:if>" />
      --%>
  <input type="hidden" id="_groupOpenType" name="GroupOpenType" value="P" />
  <input id="currentPsnGroupRoleStatus" type="hidden" value="${currentPsnGroupRoleStatus}" />
  <input id="targetToPending" type="hidden" value="" />
  <!--  群组动态需要的参数 -->
  <input id="currentPersonZhName" type="hidden" value="${person.name}" />
  <input id="currentPersonEnName" type="hidden" value="${person.ename}" />
  <input id="currentPersonInsname" type="hidden" value="${person.insName}" />
  <input id="currentAvatars" type="hidden" value="${person.avatars}" />
  <div id="groupContent" class="content-1200">
    <div class="group_top">
      <div class="group_pic">
        <img src="${groupPsn.groupImgUrl }">
      </div>
      <div class="group_base" style="border-right: 0px solid #ddd; */">
        <h1>${groupPsn.groupName }</h1>
        <p>
          <span id="group_sumMember"> <c:if test="${groupStatistics.sumMembers ==0}">1</c:if> <c:if
              test="${groupStatistics.sumMembers !=0}">${groupStatistics.sumMembers}</c:if>&nbsp;<s:text
              name="group.tab.member"></s:text></span>
          <c:if test="${ groupPsn.groupCategory   != 10 }">
            <span id="group_sumPub">${groupStatistics.sumPubs } &nbsp;<s:text name="group.tab.publication"></s:text></span>
          </c:if>
          <c:if test="${not empty groupCategory}">
            <span><c:out value="${groupCategory}" /></span>
          </c:if>
        </p>
      </div>
      <div class="group_mn">
        <p style="margin-top: 72px;"></p>
        <p>
          <s:if test="currentPsnGroupRoleStatus == 0 ">
            <!--  陌生人 申请群组 -->
            <a class="waves-effect waves-teal button02 w98" href="javascript:;"
              onclick="Group.applyForGroup('${groupId }' )"> <s:text name="group.btn.join2"></s:text>
            </a>
          </s:if>
          <s:elseif test="currentPsnGroupRoleStatus == 1 ">
            <!--  陌生人  申请中 -->
            <a id="pending" class="waves-effect waves-teal button02 w98" style="cursor: default;" href="javascript:;">
              <s:text name="group.btn.joining"></s:text>
            </a>
          </s:elseif>
          <s:elseif test="currentPsnGroupRoleStatus == 3 ||currentPsnGroupRoleStatus == 4">
            <!-- 管理群组 -->
            <a class="waves-effect waves-teal button02 w98" href="javascript:;"
              onclick="Group.selectModule('${des3GroupId }','editGroup');"> <s:text name="group.tab.groupManager"></s:text>
            </a>
          </s:elseif>
          <s:else>
            <!--  群组成员  不显示 -->
          </s:else>
        </p>
      </div>
      <div class="clear"></div>
      <ul class="group_nav">
        <li class="hover" id="brief"><a href="javascript:;"
          onclick="Group.selectModule('${des3GroupId }','brief');"> <s:text name="group.tab.about"></s:text>
        </a></li>
        <s:if test="(currentPsnGroupRoleStatus == 0 || currentPsnGroupRoleStatus == 1) && groupPsn.openType=='p'">
        </s:if>
        <s:else>
          <li id="membermenu"><a href="javascript:;" onclick="Group.selectModule('${des3GroupId }','member')">
              <s:text name="group.tab.member"></s:text>
          </a></li>
          <c:if test="${groupPsn.groupCategory == '11' && groupPsn.isPubView==1}">
            <li id="pubmenu"><a href="javascript:Group.selectModule('${des3GroupId }','pub');"> <s:text
                  name="group.tab.publication"></s:text></a></li>
          </c:if>
          <s:if test="groupPsn.isShareFile==1">
            <li id="filemenu"><a href="javascript:;" onclick="Group.selectModule('${des3GroupId }','file');"> <s:text
                  name="group.tab.file"></s:text></a></li>
          </s:if>
        </s:else>
      </ul>
    </div>
  </div>
  <div class="background_shielding_layer" id="shelter2"></div>
  <div id="invite_GroupPsn_UI" class="animation_dialogs_fly"
    style="width: 784px; height: 570px; top: 100%; left: calc(50% - 392px);">
    <div class="preloader active" style="height: 28px; width: 28px;"></div>
  </div>
  <div id="cont"
    style="width: 140%; min-height: 100%; position: fixed; top: 0; left: -20%; z-index: 100006; overflow-y: scroll; display: none">
    <div id="content_sub" class="lt_box"
      style="min-height: 16px; width: 180px; margin-top: 185px; left: 1440px; position: absolute; transition: all 0.2s ease-out; opacity: 0; z-index: 100006;"></div>
  </div>
  <div class="clear_h40"></div>
  <div class="btn3"></div>
</body>
</html>
