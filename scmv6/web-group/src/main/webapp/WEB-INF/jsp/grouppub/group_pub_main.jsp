<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s:if test='(groupPsn.openType =="O") || (groupInvitePsn.groupRole>0)'>
  <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
  <html xmlns="https://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="format-detection" content="telephone=no">
<link rel="stylesheet" type="text/css" media="screen" href="${resscmsns}/css_v5/plugin/jquery.alerts.css" />
<link rel="stylesheet" type="text/css" href="${resscmsns}/css_v5/pop.css" />
<link rel="stylesheet" type="text/css" href="${resscmsns}/css_v5/group/group.css" />
<script type="text/javascript" src="${resscmsns }/js_v5/plugin/jquery.sharePullMode.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/plugin/jquery.fulltext.request.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/json2.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/table.float.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/plugin/jquery.alerts_${locale}.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/plugin/jquery.alerts.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/plugin/jquery.scmtips.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/plugin/jquery.thickbox.resources.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/plugin/jquery.thickbox.min.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.award.js"></script>
<script type="text/javascript" src="${ressns}/js/group/group.publication.leftmenu.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/plugin/jquery.watermark.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/plugin/jquery.module.loaddiv.js"></script>
<script type="text/javascript" src="${ressns}/js/group/group.publication_${locale}.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/group/group.dynamic.js"></script>
<script type="text/javascript" src="${ressns}/js/group/group.menu.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/group/group.folder.js"></script>
<script type="text/javascript" src="${ressns}/js/group/group.publication.js"></script>
<script type="text/javascript" src="${ressns}/js/group/scholar.view.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/scm.page.js"></script>
<script type="text/javascript" src="${resmod}/js/dynamic/dynamic.share.count.js"></script>
<script type="text/javascript" src="${resmod}/js/dynamic/dynamic.share.count_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/common.order.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.statistics.js"></script>
<script type="text/javascript" src="${resmod}/js/loadding_div.js"></script>
<script type="text/javascript" src="${ressns}/js/group/maint_${locale}.js"></script>
<script type="text/javascript" src="${ressns}/js/group/MemberPub/member.publication.leftmenu.js"></script>
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/dialog.css" />
<!-- SCM-6550 微信-->
<script type="text/javascript">
	function getQrImg(url){
		if(navigator.userAgent.indexOf("MSIE")>0){
			$("#share-qr-img").qrcode({render: "table",width: 175,height:175,text:url });
		}else{
			$("#share-qr-img").qrcode({render: "canvas",width: 175,height:175,text:url });
		}	}
</script>
<title>群组成果</title>
<script type="text/javascript">
	var ctxpath = snsctx;
	var action_label_update_tagname = '<s:text name="action.label.update.tagname" />';
	var person_label_resetComfirm_title = '<s:text name="person.label.resetComfirm.title " />';
	var action_label_del_tagname = '<s:text name="action.label.del.tagname" />';
	var action_label_please_tagname = '<s:text name="action.label.please.tagname" />';
	var msgTitle = "<s:text name='publicationEdit.label.save.title'/>";
	var deletePubFolderMsg = "<s:text name='maint.search.msg.confirm.delete.pubfolder'/>";
	var required_publication = "<s:text name='maint.action.addToFolder.msg.required_publication'/>";
	var operationSuccess = "<s:text name='maint.action.success'/>";
	var required_reference = "<s:text name='maint.action.addToFolder.msg.required_reference'/>";
	var fail_network = "<s:text name='maint.action.fail.network'/>";
	var unClassicValue = "<iris:des3 code='-1'/>";
	var articleName = "<s:text name='common.articleType.output'/>";
	var atName = "${articleName}";
	var maxLength = "<s:text name='maint_maint.label.maxLength'/>";//
	var prompt = "<s:text name='friend.cited.alert'/>";//
	var msg_ofAward_title = '<s:text name="award.public.praiseMessageList"/>';
	var pub_required = "<s:text name='maint.action.addToFolder.msg.required_publication'/>";
	var searchInputTip = "<s:text name='application.file.tip.inputTip.groupPubs'/>";
	var delGroupCfm = "<s:text name='group.tip.delGroupCfm' />";
	var leaveGroupCfm = "<s:text name='group.tip.leaveGroupCfm' />";
	var reminder = "<s:text name='group.tip.reminder' />";
	var i18n_timeout = "<s:text name='sns.tip.timeout'/>";
	var i18n_tipTitle = "<s:text name='dialog.manageTag.tip.cfmDelete.title'/>";
	//分页标示群组成果
	var publist="false";
	//菜单定位
	var leftMenuLocateId = "${empty leftMenuId?'menu-mine':leftMenuId}";
	var articleTypeName = groupDetailPubs.noUpdate;
	//文件夹列表的JSON格式
	var groupFolderListJson = $
	{
		groupFolderListJson
	};
	
	function toPubCollect(){
		var des3GroupId=$("#des3GroupId").val();
		var folderId=$("#groupFolderId").val();
		var groupNodeId=$("#groupNodeId").val();
		Group.publication.clearBackPage();
		forwardUrl1("/pubweb/publication/enter?backType=1&menuId=31&groupId="+encodeURIComponent(des3GroupId) + "&folderId="+folderId+"&nodeId="+groupNodeId);
		
	}

	$(document).ready(Group.publication.ready);

	$(document).ready(function() {
		//如果响应邀请成员上传成果的邮件链接，而该群组不允许普通群组成员上传时，弹出此提示.
		var isAllowUpload='${isAllowUpload}';
		if(isAllowUpload!=null&&isAllowUpload!=''&&isAllowUpload=='true'){
			var warnInfo = "<s:text name='group.create.step2.pubViewType2' />";
			$.scmtips.show('warn',warnInfo);
		}
		//排序
		ScholarCommonOrder.init("/groupweb/grouppub/show", "${resmod}");
		$("input.thickbox").thickbox();
		$("#relatedRecommend2").loaddiv({
				ctxpath:snsctx,
				respath:resscmsns,
				type:"relatedRecommend",
				parameters :{"classStyle":"short","groupCategory":'${groupPsn.groupCategory}'}
		});
		// 分享下拉模式
		$(".share_pull").sharePullMode({
			showSharePage:function(_this){
				Group.publication.sharePublication(_this,1);
			}
		});
		
		$(".checkbox_status").click(function(){
			 $(this).css("display","none");
			 $(this).parent().find("input[type='checkbox']").css("display","block");
		});
		
		$("#page_list").find("tr").each(function(){
			//阅读统计
			smate.statistics.addReadRecord($(this).find(".pub_ownerId_class").val(),$(this).find(".pub_pubId_class").val(),1,"normalRead");
		});

		 //全文下载
		$(".notPrintLinkSpan").fullTextRequest();
		 
		//加载成员成果左边栏
		Member.publication.loadLeftMenu($("#des3GroupId").val(), $("#groupNodeId").val());
	});

	//全文下载记录
	function downLoadcallBack(resId, resOwnerPsn, resType, resNode){
		smate.statistics.addDCRecord(resOwnerPsn, resId, resType, 'downloadCount', resNode);
	}
	
	//左侧栏显示
	
	function switchContents(menuId, url,searchName, searchId) {
	$("#searchId").val(searchId);
	$('#leftMenuId').val(menuId);
	$("#searchName").val(searchName);
	$("#searchKey").val("");
	$("#pageNo").val(1);
	
	var folderUrl;
	if($("#navAction").val()=='groupPubs'){
		folderUrl = "/groupweb/grouppub/show";
	}else if($("#navAction").val()=='groupRefs'){
		folderUrl = snsctx+"/group/ref";
	}else if($("#navAction").val()=='groupPrjs'){
		folderUrl = snsctx+"/group/prj";
	}else if($("#navAction").val()=='groupFiles'){
		folderUrl = snsctx+"/group/file";
	}else if($("#navAction").val()=='groupWorks'){
		folderUrl = snsctx+"/group/work";
	}else if($("#navAction").val()=='groupCourses'){
		folderUrl = snsctx+"/group/course";
	}
	
	$("#mainForm").attr("action", folderUrl);
	$("#mainForm").submit();
};

</script>
<style type="text/css">
.groupMemberMenuTitle {
  background: url(${resmod}/images/left_nav.png) 0px 2px no-repeat;
  display: table-cell;
  vertical-align: middle;
  padding-left: 20px;
}
</style>
</head>
<body>
  <form action="/groupweb/grouppub/show" method="post" id="mainForm">
    <input type="hidden" id="ownerUrl" name="ownerUrl">
    <%-- 查看群组详情必需变量 --%>
    <%-- <input type="hidden" id="groupId" value="${groupId}" name="groupId"/>  --%>
    <input type="hidden" id="groupPsn.des3GroupId" value="${des3GroupId}" name="groupPsn.des3GroupId" /> <input
      type="hidden" id="groupPsn.groupNodeId" value="${groupNodeId}" name="groupPsn.groupNodeId" /> <input
      type="hidden" id="des3GroupId" value="<iris:des3 code='${groupId }'/>" name="des3GroupId" /> <input type="hidden"
      id="groupNodeId" value="${groupNodeId}" name="groupNodeId" /> <input type="hidden" id="des3GroupNodeId"
      value="<iris:des3 code="${groupNodeId}"/>" name="des3GroupNodeId" /> <input type="hidden" id="groupName"
      value="${groupPsn.groupName}" name="groupName" /> <input type="hidden" id="groupDescriptionSub"
      value="${groupDescriptionSub}" name="groupDescriptionSub" /> <input type="hidden" name="leftMenuId"
      id="leftMenuId" value="${leftMenuId}" /> <input type="hidden" name="searchName" id="searchName"
      value="${searchName}" /> <input type="hidden" name="searchId" id="searchId"
      value="${empty searchId?'-1':searchId}" /> <input type="hidden" name="pubIds" id="pubIds" value="" /> <input
      type="hidden" id="forwardUrl" name="forwardUrl" value="" /> <input type="hidden" name="exportType"
      id="exportType" value="" /> <input type="hidden" name="articleType" id="articleType" value="1" /> <input
      type="hidden" name="navAction" id="navAction" value="groupPubs" /> <input type="hidden" id="groupFolderId"
      name="groupFolderId" value="${searchId}" /> <input type="hidden" id="groupFolderIds" name="groupFolderIds"
      value="${searchIds}" /> <input type="hidden" id="des3PsnId" name="des3PsnId" value="${des3PsnId}" />
    <%-- 用于弹出成果评价 --%>
    <input type="hidden" id="doComment_btn" class="thickbox" alt="" title="<s:text name="group.res.pubs.appraisal"/>" />
    <input type="hidden" id="ooPsnId" value="${psnId}" /><input type="hidden" id="ooDes3PsnId"
      value="<iris:des3 code='${psnId}'/>" /> <input type="hidden" id="groupRole" value="${groupInvitePsn.groupRole}" />
    <input type="hidden" alt="" class="thickbox" id="hidden-shareLink" /> <input type="hidden" alt="" class="thickbox"
      id="hidden-awardLink" />
    <div id="content">
      <!-- 头部菜单 -->
      <%@ include file="../groupcommon/group_page_top.jsp"%>
      <div id="Tab3">
        <div class="Contentbox3">
          <div class="hover">
            <s:if test='groupPsn.isPubView != "1"'>
              <div id="content">
                <div class="confirm_words">
                  <div class="norecord_tips">
                    <p class="cu-font14">
                      <s:text name="group.tip.noRecord2" />
                    </p>
                  </div>
                </div>
              </div>
            </s:if>
            <s:else>
              <div class="left-wrap">
                <!-- 左边菜单栏 -->
                <%@ include file="../grouppub/group_pub_left.jsp"%>
                <!-- 成果合作者 
							<div class="Collaborator-list border-down" id="pubCooperation"></div> -->
                <!-- 相关推荐 -->
                <div class="recommend" id="relatedRecommend2"></div>
                <!-- 成员成果 -->
                <div id="groupMemberPub" class="sidebar-nav" style=""></div>
              </div>
              <div class="right-wrap" id="right-wrap">
                <s:if test="page.totalCount > 0">
                  <!-- 表头 -->
                  <%@ include file="../grouppub/group_pub_table_top.jsp"%>
                  <div class="main-column">
                    <table width="100%" border="0" cellspacing="0" cellpadding="1" class="t_css" id="page_list">
                      <s:iterator value="page.result" id="result" status="itStat">
                        <tr class="line_1" id="tr${pubId}">
                          <input type="hidden" class="pub_nodeId_class" value="${nodeId}" />
                          <input type="hidden" class="pub_groupId_class" value="${des3GroupId}" />
                          <input type="hidden" class="pub_pubId_class" value="${pubId}" />
                          <input type="hidden" class="pub_des3PubId_class" value="${des3Id}" />
                          <input type="hidden" class="pub_ownerId_class" value="<iris:des3 code='${ownerPsnId}'/>" />
                          <td width="5%" align="center"><input name="ckpub" class="group_checkbox_click"
                            type="checkbox" id="ckpub${pubId}" onclick="Group.publication.checkPub(this);"
                            selectPubId="<iris:des3 code='${pubId}'/>" value="<iris:des3 code='${groupPubsId}'/>" /> <input
                            type="hidden" class="inputpubId" value="<iris:des3 code='${pubId}'/>" /> <input
                            type="hidden" class="inputpubId_nodes" value="${pubId}" /> <input type="hidden"
                            class="inputnodeId" value="${nodeId}" /> <input type="hidden" class="pub_groupId_class"
                            value="${des3GroupId}" /> <input type="hidden" class="pubType" value="${typeId }" /></td>
                          <td align="left">
                            <%-- ${htmlAbstract} --%>
                            <p style="word-wrap: break-word; word-break: break-all;">${authorNames }</p> <span
                            id="title_${pubId }" class="pubGridTitle" style="word-break: break-word; cursor: pointer;">
                              <span class="notPrintLinkSpan_title" style="cursor: pointer"
                              onclick="javascript:ScholarView.viewPubDetail('<iris:des3 code='${pubId}'/>',this,${pubId},'')">
                                <font color="#005eac"> <s:if test="#locale=='zh_CN'">
                                    <s:if test="zhTitle==null || zhTitle==''">${enTitle }</s:if>
                                    <s:else>${zhTitle }</s:else>
                                  </s:if> <s:else>
                                    <s:if test="enTitle==null || enTitle==''">${zhTitle }</s:if>
                                    <s:else>${enTitle }</s:else>
                                  </s:else>
                              </font>
                            </span>
                          </span><br /> <span class="maintBriefDesc">${briefDesc}</span><span style="white-space: nowrap;"></span>
                            <c:if test="${fulltextExt !=null}">
                              <span id="span_fulltext_${pubId}" class="notPrintLinkSpan" des3resrecid=""
                                des3ressendid="" groupid="${groupId}" resid="${pubId}" resnode="${fullTextNodeId}"
                                restype="1" style="cursor: pointer; white-space: nowrap;"> <img
                                style="padding-left: 1px;" src="${resmod}${fileTypeIcoUrl}">
                              </span>
                            </c:if> <s:if test="#ooPsnId==psnId">
                              <span><img style="padding-left: 2px; padding-top: 3px;"
                                src="${res}/images/ico_me_${locale}.gif" /> </span>
                            </s:if>
                            <p style="margin-top: 5px;">
                              <a style="cursor: pointer; display: none; vertical-align: middle"
                                onclick="smate.award.ajaxAward('1','${nodeId}','${pubId}')" id="award_1_${pubId}"
                                class="f888 mright10" title="<s:text name="main.btn.zhan.tip"/>"><s:text
                                  name="main.btn.zhan" /><span id="awardcount1_1_${pubId}" count="0"></span></a> <a
                                style="cursor: pointer; display: none; vertical-align: middle"
                                onclick="smate.award.ajaxCancelAward('1','${nodeId}','${pubId}')"
                                id="cancelaward_1_${pubId}" class="f888 mright10"
                                title="<s:text name="main.btn.zhan.cancel.tip"/>"><s:text name="main.btn.zhan" /><span
                                id="awardcount2_1_${pubId}" count=""></span></a> <input type="hidden" class="awardLink_p_1"
                                resId="${pubId}" des3ResId="<iris:des3 code="${pubId}"/>" nodeId="${nodeId}" awardId=""
                                resType="1" /> <span class="mright10 fe6e6">|</span> <b class="public_pulldown"> <a
                                id="share_${pubId}" class="f888 mright10 share_sites_show" resId="${pubId}"
                                title="<s:text name="share.friend.label.inf1"/>"><s:text
                                    name="share.friend.label.inf1" /><span id="shareCountLabel_1_${pubId}" count="0"
                                  class="center"></span><i class="publication-up"></i></a> <a id="shareCountLink_1_${pubId}"
                                class="share_pull shareCountLink_1" style="display: none" resId="${pubId}" resType="1"
                                des3ResId="<iris:des3 code="${pubId}"/>" nodeId="${nodeId}" shareId=""></a>
                              </b> <span class="mright10 fe6e6">|</span>
                              <c:if test="${groupPubsIsShowComment==1 }">
                                <a id="comment_${pubId}" class="cls_comment f888" pubId="${pubId}"
                                  title="<s:text name="sns.common.comment.label"/>"><s:text
                                    name="maint.pubview.label.comment" /><span id="comment_count_${pubId}"
                                  class="center"> <c:if test="${pubReviews > 0 &&  pubReviews !=null}">(${pubReviews})</c:if></span></a>
                                <a style="display: none;" id="comment_count_span_${pubId}" class="Blue cls_comment"
                                  pubId="${pubId}" /></a>
                              </c:if>
                              <c:if test="${groupPubsIsShowComment==0 }">
                                <span id="comment_${pubId}" class="f888" pubId="${pubId}"
                                  title="<s:text name="sns.common.comment.label"/>"><s:text
                                    name="maint.pubview.label.comment" /><span id="comment_count_${pubId}"
                                  class="center"> <c:if test="${pubReviews > 0 &&  pubReviews !=null}">(${pubReviews})</c:if></span></span>
                                <a style="display: none;" id="comment_count_span_${pubId}" class="Blue cls_comment"
                                  pubId="${pubId}" /></a>
                              </c:if>
                            </p>
                          </td>
                          <td width="15%" align="center"><c:if test="${sourceDbId ne 8}">${citedTimes}</c:if></td>
                        </tr>
                      </s:iterator>
                    </table>
                    <!-- 分页 -->
                    <%@ include file="/common/pub-page-tages.jsp"%>
                  </div>
                </s:if>
                <s:else>
                  <!-- 无结果记录_MJG_SCM-3357 -->
                  <%@ include file="../grouppub/group_pub_no_result.jsp"%>
                </s:else>
              </div>
            </s:else>
          </div>
        </div>
      </div>
    </div>
  </form>
</body>
  </html>
</s:if>
<s:elseif test="groupInvitePsn.groupRole<0">
  <div id="content">
    <div class="confirm_words">
      <div class="norecord_tips">
        <p class="cu-font14">
          <s:text name="group.tip.noRecord1" />
        </p>
      </div>
    </div>
  </div>
</s:elseif>
<s:else>
  <div id="content">
    <div class="confirm_words">
      <div class="norecord_tips">
        <p class="cu-font14">
          <s:text name="group.tip.noRecord3" />
        </p>
      </div>
    </div>
  </div>
</s:else>