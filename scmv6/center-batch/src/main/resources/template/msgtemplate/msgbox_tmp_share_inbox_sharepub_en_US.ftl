<input type="hidden" value="${des3Id}" id="des3RecvId"/>
<input type="hidden" value="${shareType}" id="shareType"/>
<input type="hidden" value="<#if des3ResRecId??>${des3ResRecId}</#if>" id="des3ResRecId"/>
<div class="inbox_title">
	<div class="last_letter">
		<#if (preDes3Id?exists)>
			<a href="javascript:shareInboxMsg('${preDes3Id}')" id="prevId" style="display: inline;" previd="${preDes3Id}">&nbsp;&nbsp;Previous</a>
		<#else>
			<a href="#" id="prevId" disabled="disabled" style="color:rgb(204, 204, 204);">&nbsp;&nbsp;Previous</a>
		</#if>
	</div>
	<div class="theme" id="theme_title" title="${viewTitle}">${viewTitle}</div>
	<div class="next_letter">
		<#if (nextDes3Id?exists)>
			<a href="javascript:shareInboxMsg('${nextDes3Id}')" id="nextId" style="display: inline;" nextid="${nextDes3Id}">Next&nbsp;&nbsp;</a>
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
			<!-- 共享未取消 -->
			<#if isCancel != 1>
				<li>${content}</li>
				<#if (resDetailList?exists) && (resDetailList?size>0)>
					<#list resDetailList as resDetail>
						<li>
							<div class="<#if resDetail.status != 0>prompt_bj<#else>papers_list</#if>" id="papers_list${resDetail_index }">
								<div class="pa_input">
									<#if isCancel != 1 && !isInValid && resDetail.pubStatus != 1 && resDetail.status == 0>
										<input type="checkbox" name="resItemCk" value="${resDetail.des3ResId }" id="resItemCk${resDetail_index }" index="${resDetail_index }"/>
									<#else>
										<input type="checkbox" disabled />
									</#if>
								</div>
								<div class="file_describes">
									<dl>
										<dd>${resDetail.htmlAbstract }<input type="hidden" class="des3ResRecId" value="${resDetail.des3ResRecId!''}"/></dd>
									</dl>
								</div>
								<#if resDetail.status == 1>
									<div class="point_add"><i class="added_file"></i>Already added to "My publication / reference".</div>
								<#elseif resDetail.status == 2>
									<div class="point_add"><i class="added_file"></i>Already deleted by yourself.</div>
								<#elseif isInValid>
									<div class="point_add"><i class="added_file"></i>overdue sharing</div>
								<#elseif resDetail.pubStatus = 1>
									<div class="point_add"><i class="added_file"></i>Already deleted<#if senderName??> by ${senderName}</#if>.</div>
								<#else>
									<div class="delete-friend" id="delBtn${resDetail_index }"><a  onclick="deleteShareResOne('${resDetail.des3ResId }', '${resDetail_index }')" title="Delete"></a></div>
								</#if>
							</div>
						</li>
					</#list>
					<li>
						<input type="checkbox" onclick="common.selectAllCheckbox(this,'resItemCk')"/>&nbsp;Select All
						<span style="margin-left:20px;">
							<a  onclick="deleteShareResBatch()" class="Blue">Delete</a>
						</span>
					</li>
					<li class="nr-topline3">
				   	<a class="uiButton text14 uiButtonConfirm" title="Add To My Publication" onclick="importToMyLib(1)">Add To My Publication</a> 
				   	<a class="uiButton text14 uiButtonConfirm" title="Add To My Reference" onclick="importToMyLib(2)">Add To My Reference</a>
						<a onclick="MsgBoxUtil.backList('/scmwebsns/msgbox/shareMain',0)" title="Go Back" class="uiButton text14 mright10">Go Back</a>
					</li>
				<#else>
					<li class="nr-topline3">
						<a onclick="MsgBoxUtil.backList('/scmwebsns/msgbox/shareMain',0)" title="Go Back" class="uiButton text14 mright10">Go Back</a>
					</li>
				</#if>
			<#else>
				<li>
					<div class="norecord mdown10" style="width:520px;">
						<div class="wrong_tips">Sorry, this share has been canceled.</div>
					</div>
				</li>
				<li class="nr-topline3">
					<a onclick="MsgBoxUtil.backList('/scmwebsns/msgbox/shareMain',0)" title="Go Back" class="uiButton text14 mright10">Go Back</a>
				</li>
			</#if>
		</ul>
	</div>
</div>

<form id="frmPublicationSearch" action="/scmwebsns/forwardUrl" target="_blank" method="post">
	<input type="hidden" name="forwardUrl" id="forwardUrl"/>
	<input type="hidden" name="ownerUrl" id="ownerUrl"/>
</form>
