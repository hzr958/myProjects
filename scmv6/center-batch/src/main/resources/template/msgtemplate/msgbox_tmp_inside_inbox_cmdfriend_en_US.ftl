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
				<div id="smsObjContent">
					<div style="margin-bottom:10px;">${content}</div>
					<#if (commenderList?exists) && (commenderList?size>0)>
						<#list commenderList as commender>
							<div style="margin-top:10px; float:left;">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
					         	<tbody>
					         		<tr>
					            		<td width="30" height="30" rowspan="3" align="left" style="padding-right:10px;">
					            			<a href="${webDomain}/scmwebsns/resume/psnView?des3PsnId=${commender.commenderDes3Id}&menuId=1100" target="_blank">
					            				<img src="${commender.commenderAvatars}" width="30" height="30" border="0">
					            			</a>
					            		</td>
					            		<td width="80" align="left">
					            			<p style="width:80px; text-overflow:ellipsis; white-space:nowrap; overflow:hidden; font-size:12px; line-height:16px;">
					            				<a href="${webDomain}/scmwebsns/resume/psnView?des3PsnId=${commender.commenderDes3Id}&menuId=1100" target="_blank" style="font-size:12px; color:#005eac; text-decoration:none;" title="${commender.commenderName}">${commender.commenderName}</a>
					            			</p>
					            		</td>
					          		</tr>
					          		<tr>
					            		<td height="2" align="left"></td>
					          		</tr>
					          		<tr>
					                  <td height="20" align="left" id="lab${commender.commenderId}">
			                  			<#if (commender.isFriend?? && commender.isFriend="0")>
						            			<a class="addFriendClass thickbox" title="Invite ${commender.commenderName?html} be your friend"  des3id="${commender.commenderDes3Id}" style="font-size:12px;"><span class="addfriends"></span>Add Friend</a>
						            		</#if>
					                  </td>
					               </tr>
					        		</tbody>
					        	</table>
			          	</div>
		          	</#list>
		          	<div style="clear:both"></div>
	          	</#if>
				</div>
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
