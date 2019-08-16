<input type="hidden" value="${des3Id}" id="des3RecvId"/>
<input type="hidden" value="<#if des3ResRecId??>${des3ResRecId}</#if>" id="des3ResRecId"/>
<input type="hidden" value="${xmlId}" id="xmlId_hidden"/>
<div class="inbox_title">
	<div class="last_letter">
		<#if (preDes3Id?exists)>
			<a href="javascript:shareInboxMsg('${preDes3Id}')" id="prevId" style="display: inline;" previd="${preDes3Id}">&nbsp;&nbsp;上一封</a>
		<#else>
			<a href="#" id="prevId" disabled="disabled" style="color:rgb(204, 204, 204);">&nbsp;&nbsp;上一封</a>
		</#if>
	</div>
	<div class="theme" id="theme_title" title="${viewTitle}">${viewTitle}</div>
	<div class="next_letter">
		<#if (nextDes3Id?exists)>
			<a href="javascript:shareInboxMsg('${nextDes3Id}')" id="nextId" style="display: inline;" nextid="${nextDes3Id}">下一封&nbsp;&nbsp;</a>
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
			<#if (resDetailList?exists) && (resDetailList?size>0)>
				<#list resDetailList as resDetail>
					<li class="resItem_li">
						<div class="papers_list" id="papers_list${resDetail_index }">
							<div class="pa_input">${resDetail_index + 1 }</div>
							<div class="file_describes">
								<dl>
									<dd>${resDetail.htmlAbstract }<input type="hidden" class="des3ResRecId" value="${resDetail.des3ResRecId!''}"/></dd>
								</dl>
							</div>
							<div class="point_add2" style="text-align:left;">
								<input type="hidden" class="pub_id_hidden" value="${resDetail.resId?c }"/>
			          		<input type="hidden" class="dup_pub_id" value="${resDetail.dupResId }"/>
			          		<input type="hidden" class="nodeId" value="${resDetail.nodeId }"/>
			          		<input type="hidden" class="dbid" value="${resDetail.dbid!'' }"/>
			          		<#if resDetail.isInsert?is_number>
				          		<div style="color:red;">
							      	<#if resDetail.isInsert == 1>
											<input type="radio" value="2" class="radiobutton" name="dup_flag_${resDetail_index }"/>&nbsp;新增<br/>
										</#if>
										<input type="radio" value="1" class="radiobutton" checked="checked" name="dup_flag_${resDetail_index }"/>&nbsp;替换<br/>
										<input type="radio" value="0" class="radiobutton" name="dup_flag_${resDetail_index }"/>&nbsp;跳过
									</div>
								</#if>
							</div>
						</div>
					</li>
				</#list>
				<li class="nr-topline3">
					<a onclick="cfmImportToMyPubLib()" class="uiButton text14 uiButtonConfirm" title="确定">确定</a>
					<a onclick="shareInboxMsg('${des3Id}')" class="uiButton text14 uiButtonConfirm" title="重新选择">重新选择</a>
				</li>
			</#if>
		</ul>
	</div>
</div>

<form id="frmPublicationSearch" action="/scmwebsns/forwardUrl" target="_blank" method="post">
	<input type="hidden" name="forwardUrl" id="forwardUrl"/>
	<input type="hidden" name="ownerUrl" id="ownerUrl"/>
</form>