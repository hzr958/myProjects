<input type="hidden" value="${des3Id}" id="des3RecvId"/>
<div class="inbox_title">
	<div class="last_letter">
		<#if (preDes3Id?exists)>
			<a href="javascript:smsInboxMsg('${preDes3Id}')" id="prevId" style="display: inline;" previd="${preDes3Id}">&nbsp;&nbsp;Previous</a>
		<#else>
			<a href="#" id="prevId" disabled="disabled" style="color:rgb(204, 204, 204);">&nbsp;&nbsp;Previous</a>
		</#if>
	</div>
	<div class="theme" id="theme_title" title="${viewTitle}">${viewTitle}</div>
	<div class="next_letter">
		<#if (nextDes3Id?exists)>
			<a href="javascript:smsInboxMsg('${nextDes3Id}')" id="nextId" style="display: inline;" nextid="${nextDes3Id}">Next&nbsp;&nbsp;</a>
		<#else>
			<a href="#" id="nextId" disabled="disabled" style="color:rgb(204, 204, 204);">Next&nbsp;&nbsp;</a>
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
				<div id="smsObjContent">${content}
					<#if msgType==1>
						<#if relation?exists>
							<p class="b">Relation: ${relation}</p>
						</#if>
						<#if psnWork?exists>
							<p class="b">Work: ${psnWork}</p>
						</#if>
					</#if>
				</div>
				<#if msgType==1>
					<p></p>
					<br />
					<span>Please click <a href="/scmwebsns/friendFappManage/loadMineFappMain?des3WorkId=&appStatus=" class="Blue">here</a> to proceed.</span>
				</#if>
				<#if (fileList?exists && fileList?size>0)>
					<div id="attachFileBlock">
						<#list fileList as file>			
						   <div class="add_word mdown5" id="attachFileDiv" style="width: 448px;" ismine="1">
								<i class="annex_icon Fleft"></i>
								<div class="attachName inp_bg3 Fleft" style="width: 410px; color: #999;text-overflow:ellipsis; white-space:nowrap; overflow:hidden;">
									<a href="javascript:void(0);" onclick="openFile('${file.des3AttachFileId }','1','0');"  class="Blue" title="${file.fileName}" >${file.fileName}</a>
								</div> 	
								<input type="hidden" class="input_attachFileId" value="${file.attachFileId}">	
								<input type="hidden" class="input_des3attachFileId" value="${file.des3AttachFileId}">	
								<input type="hidden" class="input_fileId" value="<#if (file.fileId??)>${file.fileId!''}</#if>">	
								<a onclick="msgboxSend.deleteAttachFile(this)" class="box-delete mleft5" href="javascript:void(0);" style="display: none;"></a>    
							</div>
						</#list>
					</div>
				</#if>
			</li>
			<li>
				<div class="Fleft">
					<#if (senderDes3Id?exists)>
						<a onclick="smsAjaxReply('${senderId}','${senderName}')" class="uiButton text14 mright10" title="Reply">Reply</a>
						<input type="hidden" class="thickbox" id="hidden-reply" alt="/scmwebsns/msgbox/ajaxSendMessageBox?TB_iframe=true&rank_menu=true&height=405&width=740" title="Reply"/>
			   		<a onclick="smsAjaxForward()" title="Forward" class="uiButton text14 mright10">Forward</a>
			   		<input type="hidden" class="thickbox" id="hidden-forward" alt="/scmwebsns/msgbox/ajaxSendMessageBox?TB_iframe=true&rank_menu=true&height=405&width=740" title="Forward"/>
		   		</#if>
		   		<a onclick="MsgBoxUtil.backList('/scmwebsns/msgbox/smsMain',0)" title="Back to inbox" class="uiButton text14 mright10">Back to inbox</a>
		  		</div>
				<div class="Fright" style="margin-right: 20px; display: inline;">
					<a onclick="smsInMarkUnread('${id?c}')" class="Blue">Mark As Unread</a> | 
					<a onclick="smsDeleteInboxDetail('${id?c}')" class="Blue" id="detailLinkDelete">Delete</a>
				</div>
			</li>
		</ul>
	</div>
</div>

<form id="frmPublicationSearch" action="/scmwebsns/forwardUrl" target="_blank" method="post">
	<input type="hidden" name="forwardUrl" id="forwardUrl"/>
	<input type="hidden" name="ownerUrl" id="ownerUrl"/>
</form>
