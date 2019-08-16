<input type="hidden" name="dynId" value="${GROUP_DYN_ID?if_exists }" dynOwnerDes3Id="${DYN_OWNER_DES3_ID?if_exists}" des3ResId="${DES3_RES_ID?if_exists}" resId="${RES_ID?if_exists}" resGrpId="${RES_GRP_ID?c}" dynType="${DYN_TYPE?if_exists}" />

<div class="dynamic-content">
	<div class="dynamic-content__post">
		<div class="dynamic-post__avatar" onclick='DiscussOpenDetail.openPsnDetail("${DES3_PSN_ID ?if_exists}",event)'>
			<a><img src="${AUTHOR_AVATAR ?if_exists}" onerror="this.src='/ressns/images/phone120X120.gif'"></a>
		</div>
		<div class="dynamic-post__main">
			<div class="dynamic-post__author">
				<a class="dynamic-post__author_name" title="${ZH_AUTHOR_NAME ?if_exists}" onclick='DiscussOpenDetail.openPsnDetail("${DES3_PSN_ID ?if_exists}",event)'>
					${ZH_AUTHOR_NAME ?if_exists}
				</a>
				<span class="dynamic-post__author_action">上传了</span>
			</div>
			<div class="dynamic-post__author_inst">${PSN_WORK_INFO?if_exists } </div>
		</div>
		<div class="dynamic-post__time show_time"></div>
	</div>
	<div class="dynamic-content__main">
		<div class="dynamic-main__box">
			<div class="dyn_content">
				${GROUP_DYN_CONTENT?if_exists }
			</div>
			<#if (RES_ID?exists)>
				<#if (GROUP_DYN_CONTENT?exists)>
					<div class="dynamic-divider"></div>
				</#if>
				<div class="dynamic-main__att">
					<div class="pub-idx_medium">
						<div class="pub-idx__base-info">
							<div class="pub-idx__full-text_box">
								<div class="pub-idx__full-text_img" onclick="GrpFile.openDynFile('${RES_ID?if_exists}');">

									<#if FILE_TYPE=="txt">
										<img src="/resmod/smate-pc/img/fileicon_txt.png">
									<#elseif FILE_TYPE=="ppt" || FILE_TYPE=="pptx">
										<img src="/resmod/smate-pc/img/fileicon_ppt.png">
									<#elseif FILE_TYPE=="doc" || FILE_TYPE=="docx">
										<img src="/resmod/smate-pc/img/fileicon_doc.png">
									<#elseif FILE_TYPE=="rar" || FILE_TYPE=="zip">
										<img src="/resmod/smate-pc/img/fileicon_zip.png">
									<#elseif FILE_TYPE=="xls" || FILE_TYPE=="xlsx">
										<img src="/resmod/smate-pc/img/fileicon_xls.png">
									<#elseif FILE_TYPE=="pdf">
										<img src="/resmod/smate-pc/img/fileicon_pdf.png">
									<#else>
										<img src="${FILE_IMAGE?if_exists}" onerror="this.src='/resmod/smate-pc/img/fileicon_default.png'">
									</#if>

								</div>
							</div>
							<div class="pub-idx__main_box">
								<div class="pub-idx__main">
									<div class="pub-idx__main_title">
										<a onclick="GrpFile.openDynFile('${RES_ID?if_exists}');">${RES_NAME?if_exists}</a>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</#if>
		</div>
	</div>
</div>
