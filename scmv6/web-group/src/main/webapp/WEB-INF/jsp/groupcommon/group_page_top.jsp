<%@ page language="java" pageEncoding="UTF-8"%>
<input type="hidden" value="${groupPsn.groupId}" id="hidden_groupIdABC" />
<div class="mainnr-title mdown5">
  <div class="co-titcontent">
    <div class="co-titcontent">
      <div <c:if test="${fn:length(groupPsn.groupName)>35}">style='width:450px;'</c:if> class="cu14 group-name"
        title="<s:property value="groupPsn.groupName" escapeHtml="true"/>">
        <i class="icon-group"></i>
        <s:property value="groupPsn.groupName" />
      </div>
    </div>
    <span class="cu14" title="<s:property value="groupPsn.groupNo" escapeHtml="true"/>"> &nbsp;&nbsp;&nbsp;<span><s:text
          name="page.label.groupNo" />&nbsp;<s:property value="groupPsn.groupNo" /></span>
    </span>
  </div>
  <div class="backgroup">
    <s:if test="groupInvitePsn==null">
      <s:if test="#request['isReadyJoin']!=true">
					&nbsp;&nbsp;&nbsp;&nbsp;
				<i class="icon46 py-icon" style="margin-right: 2px;"></i>
        <a onclick="Group.dynamic.joinGroup(this)" existGroupCode="${not empty groupPsn.groupCode}"
          title="<s:text name="page.groupDetail.joinGroup2" />"> <s:text name="page.groupDetail.joinGroup2" />
        </a>&nbsp;&nbsp;&nbsp;&nbsp;
			</s:if>
      <s:else>
        <span class="Orange"><s:text name="page.groupDetail.auditing" /></span>&nbsp;&nbsp;&nbsp;&nbsp;
			</s:else>
      <input type="hidden" id="inputGroupCode" class="thickbox" title="<s:text name='page.groupDetail.joinGroup2'/>" />
    </s:if>
    <%--分享群组 --%>
    <i class="icon-sharegroup"></i> <a onclick="shareGroup('${groupPsn.groupId}')"
      title="<s:text name='group.link.shareGroup' />"><s:text name="group.link.shareGroup" /></a> <input type="hidden"
      alt="${snsctx}/dynamic/ajaxShareMaint?TB_iframe=true&height=480&width=720" class="thickbox"
      title="<s:text name='group.shareBox.title' />" id="groupShareBtn" />
    <textarea id="inviteTitle" name="inviteTitle" style="display: none;"><s:text
        name="group.member.invite.send.content.tip.new">
        <s:param>
          <s:property value="groupPsn.navGroupName" />
        </s:param>
      </s:text></textarea>
    <textarea id="inviteContent" name="inviteContent" style="display: none;"><s:text
        name="group.member.invite.content.tip.new">
        <s:param>
          <s:property value="groupPsn.navGroupName" />
        </s:param>
      </s:text></textarea>
    &nbsp;&nbsp;&nbsp;&nbsp;
    <%--从感兴趣的群组、检索群组进入群组详细信息页面时，隐藏“返回群组列表”的标签(如果开放设置该标签，从感兴趣的群组进入群组信息页面后点击其他链接会出现无法返回列表的问题)_MJG_2012-11-08_SCM-1192 --%>
    <%-- <c:if test="${groupListUrl!=null}">
		<i class="icon-backgroup"></i>
		<a href="${ctx}${groupListUrl}" title="<s:text name='group.link.backToList' />"><s:text name="group.link.backToList" /></a>
		</c:if> --%>
    <%--返回群组列表 --%>
    <c:if test='${groupListUrl==null || groupListUrl==""}'>
      <i class="icon-backgroup"></i>
      <a href="${snsctx}/group/main" title="<s:text name='group.link.backToList' />"><s:text
          name="group.link.backToList" /></a>
    </c:if>
    <%--群组主页地址_临时_MaoJianGuo_分享处用到 --%>
    <input type="hidden" id="group_webpage_url" value="" /> <input type="hidden" id="group_webpage_img"
      value="${groupPsn.groupImgUrl }" /> <input type="hidden" name="groupListUrl" value="${groupListUrl}">
  </div>
</div>
<script type="text/javascript">
	//alert("是否为群组成员:${groupInvitePsn!=null}\n群组是否为开放类别:${groupPsn.openType=='O'}\n");
	//alert("是否支持成果:${groupPsn.isPubView==1}\n是否支持文献:${groupPsn.isRefView==1}\n是否支持项目:${groupPsn.isPrjView==1}\n是否支持群组文件:${groupPsn.isShareFile==1}\n是否支持作业:${groupPsn.isWorkView==1}\n是否支持教学课件:${groupPsn.isMaterialView==1}");
</script>
<div class="Menubox3">
  <%--群组菜单显示标签(群组类型10-教学课程群组；11-科研项目群组；12-其他类型群组；13-中心/实验室科研（原创新）群组 --%>
  <s:if test="groupPsn.groupCategory == 10">
    <%@ include file="../groupcommon/group_page_top_course.jsp"%>
  </s:if>
  <s:elseif test="groupPsn.groupCategory == 11">
    <%@ include file="../groupcommon/group_page_top_project.jsp"%>
  </s:elseif>
  <s:elseif test="groupPsn.groupCategory == 13">
    <%@ include file="../groupcommon/group_page_top_innovation.jsp"%>
  </s:elseif>
  <s:else>
    <%@ include file="../groupcommon/group_page_top_otherType.jsp"%>
  </s:else>
</div>
<%--更多菜单 --%>
<div id="more_menu" class="tc_box" style="display: none;">
  <ul>
    <%--群组设置 --%>
    <s:if test="groupInvitePsn.groupRole == 1">
      <li id="groupEdit" onclick="Group.menu.jump('${des3GroupId}','${groupNodeId}','edit');"><a> <s:text
            name="group.tab.groupSettings" />
      </a></li>
    </s:if>
    <s:if test="groupInvitePsn.groupRole==1 || groupInvitePsn.groupRole==2">
      <li><a style="dispaly: none;" id="selfEdit" class="Blue" title="<s:text name='group.link.settings' />"> <s:text
            name="group.link.settings" />
      </a></li>
    </s:if>
    <%--待批准成员 --%>
    <s:if test="groupInvitePsn.groupRole==1 || groupInvitePsn.groupRole==2">
      <li id="groupMemberNotYet" onclick="Group.menu.jump('${des3GroupId}','${groupNodeId}','memberNotYet');"><a><s:text
            name="group.tab.more.pendingCfm" /></a></li>
    </s:if>
    <%--复制群组 只有课程群组有复制功能 zk add --%>
    <s:if test="groupPsn.groupCategory==10">
      <li><a style="dispaly: none;" id="copyCourse" class="Blue" title="<s:text name='group.link.copyGroup' />"><s:text
            name='group.link.copyGroup' /></a></li>
    </s:if>
    <%--删除群组 --%>
    <s:if test="groupInvitePsn.groupRole==1">
      <li onclick="Group.menu.del('${des3GroupId}','${groupNodeId}')"><a><s:text name="group.tab.delGroup" /></a></li>
    </s:if>
    <s:if test="groupInvitePsn.groupRole==3">
      <%--个人设置 --%>
      <li><a style="dispaly: none;" id="selfEdit" class="Blue" title="<s:text name='group.link.settings' />"> <s:text
            name="group.link.settings" />
      </a></li>
      <%--退出群组 --%>
      <li onclick="Group.menu.leave('${des3GroupId}','${groupNodeId}')"><a><s:text name="group.tab.leaveGroup" /></a></li>
    </s:if>
  </ul>
</div>
<script type="text/javascript">
var main_path = "https://"+document.domain + "/group/";
var delGroupCfm = "<s:text name='group.tip.delGroupCfm' />";
var shareConfig={};
var shareContentConfig={};
var irisShareConfig = {};
var ctxpath ="${snsctx}";

$(document).ready(function(){
	$("#groupShareBtn").thickbox({
		respath : '${resscmsns}'
	});
	//显示更多菜单.
	$('#settings').click(function(event) {
		//触发本事件的a标签.
		var _this = $(this);
		var _pos = _this.offset();
		var top = _pos.top;    //获取a的居上位置
		var left = _pos.left;    //获取a的居左位置
		var a_height = _this.height();   //获取a的高度
		var a_width = _this.width();       //获取a的宽度
		//设置更多菜单标签的位置.
		var result_left=left;
		if(screen.width<='1024'&&screen.height<='768'){
			<%--当屏幕分辨率小于1024*768时，群组更多按钮下拉菜单与设置按钮右对齐；反之则保持左对齐_MJG_SCM-1680.--%>
			if('${locale}'=='zh_CN'){
				result_left=left-a_width-parseInt(_this.css("margin-right"));
			}else{
				result_left=left-parseInt(_this.css("padding-left"))-parseInt(_this.css("padding-right"));
			}
		}
		$("#more_menu").css({'top':top+a_height,'left':result_left});
		common.showMoreOperation(this, "more_menu",event);
	});
	
	/**
	* 调用本群组菜单的页面需设置 navAction 隐藏变量.变量值与本页面对应li选项的ID值相同.
	*/
	var nav = $("#navAction").val();
	//设定群组导航菜单被选定样式
	$("#"+nav).attr("class","hover");

	//设置群组网页地址.
	setGroupPageUrl();
	<s:if test="groupInvitePsn!=null">
	//设置(个人设置).
	$("#selfEdit")
			.thickbox(
					{
						ctxpath : "${snsctx}",
						respath : "${resscmsns}",
						parameters : {
							"groupPsn.des3GroupId" : '${des3GroupId}',
							"groupPsn.groupNodeId" : '${groupNodeId}'
						},
						type : "groupSelfEdit"
					});
		<s:if test="groupInvitePsn.isActivity == 0">
		setTimeout(function(){
			$("#selfEdit").trigger("click");
		},2000);
		</s:if>
		
		//群组复制（教学群组）  zk add SCM-3390
		 var locale = "${locale}";
		 var gccType;
		 if (locale=="zh_CN")
			 gccType = "groupCopyCourse_cn";
		 else
			 gccType = "groupCopyCourse_en";
		$("#copyCourse").thickbox({
			ctxpath : "${snsctx}",
			respath : "${resscmsns}",
			parameters : {
				"des3GroupId" : '${des3GroupId}',
				"groupNodeId" : '${groupNodeId}'
			},
 			type : gccType
		});
	</s:if>
});
function shareGroup(groupId){
	var groupName = $("#groupName").val();
	//如下注释部分为原分享群组的参数_MaoJianGuo_2013-02-21_SCM-1718.
    /* shareConfig = {
		"resType" : 9,
		"resIds" : groupId,
		"shareType" : "3,5",
		"shareTypeFirst" : "3"
	};

	shareContentConfig = {
		"friendSendContent":"<s:text name='shareBox.form.content.con1' /> \"" + groupName + "\" <s:text name='shareBox.form.content.con2' /><p style='padding-left:2em'><a href='"+$("#group_webpage_url").val()+"' class='Blue' target='_blank'>"+$("#group_webpage_url").val()+"</a>",
		"friendSendTitle":"<s:text name='shareBox.form.title.con' /> \"" + groupName + "\""
	}; */
	//默认留言显示内容.
	//var default_content="<s:text name='group.member.invite.friend.content.tip' />";
	//分享群组标签的请求参数：shareConfig，shareGroupInfo，shareContentConfig 。_MaoJianGuo_2013-02-27-SCM-1718.
	shareConfig = {
		"resIds" : groupId,
		"shareType" : "1,3,5",
		"shareTypeFirst" : "5",
		"actionName":"group",
		"msgType":48,
		"resNode":Number("${groupNodeId}"),
		"recommendUrl": $("#group_webpage_url").val(),
		"supportEmail": 1,
		"jsonParam":{
           "resId":Number(groupId),
           "resNode":Number("${groupNodeId}"),
           "groupId":Number(groupId),
           "groupNode":Number("${groupNodeId}"),
           "resType":"8"
		}
	};
	
	//添加分享记录的时候，需要用到
	var resDetails = new Array();
	resDetails.push({ 'resId' : Number(groupId), 'resNode' : Number("${groupNodeId}")});
	shareConfig['jsonParam']['resDetails'] = resDetails;
	
	shareGroupInfo={
		"des3GroupId" : '${des3GroupId}',
		"groupNodeId" : '${groupNodeId}',
		"groupDescription":'${groupPsn.groupDescription}',
		"groupName":groupName,
		"sumMembers":'${groupPsn.sumMembers}',
		"sumPubs":'${groupPsn.sumPubs}',
		"sumRefs":'${groupPsn.sumRefs}',
		"sumPrjs":'${groupPsn.sumPrjs}',
		"sumFiles":'${groupPsn.sumFiles}'
	};
	shareContentConfig = {
		"friendSendContent":"<s:text name='shareBox.form.content.con1' /> \"" + groupName + "\" <s:text name='shareBox.form.content.con2' /><p style='padding-left:2em'><a href='"+$("#group_webpage_url").val()+"' class='Blue' target='_blank'>"+$("#group_webpage_url").val()+"</a>",
		"friendSendTitle":"<s:text name='shareBox.form.title.con' /> \"" + groupName + "\"",
		"InviteSendContent":$("#inviteContent").val(),
		"InviteSendTitle":$("#inviteTitle").val()
	};

	irisShareConfig = {
			'url' : $('#group_webpage_url').val(),
			'irisText' : '<s:text name="shareBox.form.group" />' + groupName,
			'pic' : $('#group_webpage_img').val(),
			'picSearch' : false,
			'category' : 9
	};

	$("#groupShareBtn").click();
}
/**
 * 设置群组网页地址隐藏参数.
 */
function setGroupPageUrl(){
	  var pre = "https://"+document.domain + "${snsctx}/group/dyn?des3GroupId=${des3GroupId}&groupNodeId=${groupNodeId}";
	  $("#group_webpage_url").val(pre);
	 
} 
var i18n_timeout = "<s:text name='sns.tip.timeout'/>";
var i18n_tipTitle = "<s:text name='dialog.manageTag.tip.cfmDelete.title'/>";
</script>
<script type="text/javascript" src="${ressns}/js/group/group.timeout.handler.js"></script>