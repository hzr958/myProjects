<#if (fileList?exists && fileList?size>0)>
	<div id="attachFileBlock" style="display:none;">
		<#list fileList as file>			
		   <div class="add_word mdown5" id="attachFileDiv" style="width: 448px;" ismine="1">
				<i class="annex_icon Fleft"></i>
				<div class="attachName inp_bg3 Fleft" style="width: 410px; color: #999;text-overflow:ellipsis; white-space:nowrap; overflow:hidden;">
					<a href="${file.downloadUrl}" class="Blue" title="${file.fileName}" target="_blank">${file.fileName}</a>
				</div> 	
				<input type="hidden" class="input_attachFileId" value="${file.attachFileId}">	
				<input type="hidden" class="input_des3attachFileId" value="${file.des3AttachFileId}">	
				<input type="hidden" class="input_fileId" value="${file.fileId}">	
				<a onclick="msgboxSend.deleteAttachFile(this)" class="box-delete mleft5" href="javascript:void(0);" style="display: none;"></a>    
			</div>
		</#list>
	</div>
</#if>
