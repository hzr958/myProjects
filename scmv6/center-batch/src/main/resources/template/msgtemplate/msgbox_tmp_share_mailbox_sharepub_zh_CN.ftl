<input type="hidden" value="${des3MailId}" id="des3MailId"/>
<div class="inbox_title">
	<div class="last_letter">
		<#if (preDes3MailId?exists)>
			<a href="javascript:shareOutboxMsg('${preDes3MailId}')" id="prevId" style="display: inline;" previd="${preDes3MailId}">&nbsp;&nbsp;上一封</a>
		<#else>
			<a href="#" id="prevId" disabled="disabled" style="color:rgb(204, 204, 204);">&nbsp;&nbsp;上一封</a>
		</#if>
	</div>
	<div class="theme" id="theme_title" title="${viewTitle}">${viewTitle}</div>
	<div class="next_letter">
		<#if (nextDes3MailId?exists)>
			<a href="javascript:shareOutboxMsg('${nextDes3MailId}')" id="nextId" style="display: inline;" nextid="${nextDes3MailId}">下一封&nbsp;&nbsp;</a>
		<#else>
			<a href="#" id="nextId" disabled="disabled" style="color:rgb(204, 204, 204);">下一封&nbsp;&nbsp;</a>
		</#if>
	</div>
</div>
<div class="mail_content">
	<div class="sender_avatar">
		<ul class="sender_avatar">
			<li>
				<#if (receiverList?exists) && (receiverList?size>0)>
					<#list receiverList as receiver>
						<#if (receiver_index >= 3)>
							<#break>
						</#if>
						<p><a href="/scmwebsns/resume/view?des3PsnId=${receiver.receiverDes3Id}"><img src="${receiver.receiverAvatars}" width="50" height="50" /></a></p>
						<p><a href="/scmwebsns/resume/view?des3PsnId=${receiver.receiverDes3Id}" class="Blue">${receiver.receiverName}</a></p>
					</#list>
				<#else>
					<p><img src="${defaultAvatars}" width="50" height="50" /></p>
				</#if>
			</li>
			<li>
				<p class="en10" id="receive-time">${sendDate}</p>
				<#if (receiverNum?exists) && (receiverNum>3)>
					<p><a href="#TB_inline?height=400&amp;width=620&amp;inlineId=sender_list" title="接收人员列表" class="Blue thickbox">查看更多收件人</a></p>
				</#if>
			</li>
		<ul>
	</div>
	<div class="mail_words">
		<ul>
			<li>${content}</li>
			<#if (resDetailList?exists) && (resDetailList?size>0)>
				<#list resDetailList as resDetail>
					<li>
						<div class="papers_list">
							<div class="pa_input">${resDetail_index + 1 }</div>
							<div class="file_describes">
								<dl>
									<dd>${resDetail.htmlAbstract }</dd>
								</dl>
							</div>
							<#if (isCancel == 1)><div class="point_add"><i class="added_file"></i>此分享已被取消。</div></#if>
						</div>
					</li>
				</#list>
			</#if>
			<li class="nr-topline3">
				<#if (isCancel == 0)>
					<a title="取消共享" id="cancelShareLink" class="uiButton text14 mright10" onclick="cancelShare('${des3ResSendId}')">取消共享</a>
				</#if>
				<a onclick="MsgBoxUtil.backList('/scmwebsns/msgbox/shareMain',1)" title="返回列表" class="uiButton text14 mright10">返回列表</a>
			</li>
		</ul>
	</div>
</div>

<form id="frmPublicationSearch" action="/scmwebsns/forwardUrl" target="_blank" method="post">
	<input type="hidden" name="forwardUrl" id="forwardUrl"/>
	<input type="hidden" name="ownerUrl" id="ownerUrl"/>
</form>

<#if (receiverList?exists) && (receiverList?size>3)>
	<div id="sender_list" style="position:relative;display: none;">
		<div class="dialog_content">
			<ul class="fri_list">
				<#list receiverList as receiver>
					<li>
						<div class="pat-manpic"><a href="/scmwebsns/resume/view?des3PsnId=${receiver.receiverDes3Id}"><img src="${receiver.receiverAvatars}" width="50" height="50" /></a></div>
						<div class="pat-contant">
							<p><a href="/scmwebsns/resume/view?des3PsnId=${receiver.receiverDes3Id}" class="Blue">${receiver.receiverName}</a></p>
						</div>
					</li>
				</#list>
			</ul>
		</div>
		<div style="height:20px;">&nbsp;</div>
		<div class="pop_buttom" style="bottom:9px;width:620px;position:absolute;"><a  onclick="$.Thickbox.closeWin()" class="uiButton text14 mright10" title="关闭">关闭</a></div>
	</div>
</#if>
