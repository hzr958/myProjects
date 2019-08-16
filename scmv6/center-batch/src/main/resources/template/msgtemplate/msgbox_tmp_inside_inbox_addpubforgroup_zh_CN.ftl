<input type="hidden" value="${des3Id}" id="des3RecvId"/>
<div class="inbox_title">
	<div class="last_letter">
		<#if (preDes3Id?exists)>
			<a href="javascript:smsInboxMsg('${preDes3Id}')" id="prevId" style="display: inline;" previd="${preDes3Id}">&nbsp;&nbsp;上一封</a>
		<#else>
			<a href="#" id="prevId" disabled="disabled" style="color:rgb(204, 204, 204);">&nbsp;&nbsp;上一封</a>
		</#if>
	</div>
	<div class="theme" id="theme_title" title="${viewTitle}">${viewTitle}</div>
	<div class="next_letter">
		<#if (nextDes3Id?exists)>
			<a href="javascript:smsInboxMsg('${nextDes3Id}')" id="nextId" style="display: inline;" nextid="${nextDes3Id}">下一封&nbsp;&nbsp;</a>
		<#else>
			<a href="#" id="nextId" disabled="disabled" style="color:rgb(204, 204, 204);">下一封&nbsp;&nbsp;</a>
		</#if>
	</div>
</div>
<div class="mail_content">
	<div class="sender_avatar">
		<#if (senderDes3Id?exists)>
			<p><a href="/scmwebsns/resume/view?des3PsnId=${senderDes3Id}"><img src="${senderAvatars}" width="50" height="50" /></a></p>
			<p><a href="/scmwebsns/resume/view?des3PsnId=${senderDes3Id}" class="Blue" id="senderName_link">${senderName}<input type="hidden" value="${senderId }" id="senderId_hidden"/></a></p>
		<#else><!-- 人员合并，发送人已删除 -->
			<p><a onclick="emptySenderTip()"><img src="${senderAvatars}" width="50" height="50" /></a></p>
		</#if>
		<p class="en10" id="receive-time">${receiverDate}</p>
	</div>
	<div class="mail_words">
		<ul>
			<li>
				<div id="smsObjContent">${content}</div>
				<p></p>
				<br />
				<span>点击<a class="Blue" href="/scmwebsns/group/pub?groupPsn.groupNodeId=1&groupPsn.des3GroupId=${des3GroupId!''}&sharePub=true" scmflag="hidden">这里</a>为群组添加成果</span>
			</li>
			<li>
				<div class="Fleft">
					<#if (senderDes3Id?exists)>
						<a onclick="smsAjaxReply('${senderId}','${senderName}')" class="uiButton text14 mright10" title="回复">回复</a>
						<input type="hidden" class="thickbox" id="hidden-reply" alt="/scmwebsns/msgbox/ajaxSendMessageBox?TB_iframe=true&rank_menu=true&height=405&width=740" title="回复"/>
			   		<a onclick="smsAjaxForward()" title="转发" class="uiButton text14 mright10">转发</a>
			   		<input type="hidden" class="thickbox" id="hidden-forward" alt="/scmwebsns/msgbox/ajaxSendMessageBox?TB_iframe=true&rank_menu=true&height=405&width=740" title="转发"/>
	   			</#if>
		   		<a onclick="MsgBoxUtil.backList('/scmwebsns/msgbox/smsMain',0)" title="返回收件箱" class="uiButton text14 mright10">返回收件箱</a>
		  		</div>
				<div class="Fright" style="margin-right: 20px; display: inline;">
					<a onclick="smsInMarkUnread('${id?c}')" class="Blue">标记未读</a> | 
					<a onclick="smsDeleteInboxDetail('${id?c}')" class="Blue" id="detailLinkDelete">删除</a>
				</div>
			</li>
		</ul>
	</div>
</div>

<form id="frmPublicationSearch" action="/scmwebsns/forwardUrl" target="_blank" method="post">
	<input type="hidden" name="forwardUrl" id="forwardUrl"/>
	<input type="hidden" name="ownerUrl" id="ownerUrl"/>
</form>
