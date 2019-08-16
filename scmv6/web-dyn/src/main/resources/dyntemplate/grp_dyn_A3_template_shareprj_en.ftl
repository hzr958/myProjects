<input type="hidden" name="dynId" value="${GROUP_DYN_ID?if_exists }" dynOwnerDes3Id="${DYN_OWNER_DES3_ID?if_exists}" resType="${RES_TYPE?if_exists}"  des3ResId="${DES3_RES_ID?if_exists}" resId="${RES_ID?if_exists}"  dynType="${DYN_TYPE?if_exists}" />
<div class="dynamic-content">
	<div class="dynamic-content__post">
		<div class="dynamic-post__avatar" onclick='DiscussOpenDetail.openPsnDetail("${DES3_PSN_ID ?if_exists}",event)'>
			<a><img src="${AUTHOR_AVATAR ?if_exists}" onerror="this.src='/ressns/images/phone120X120.gif'"></a>
		</div>
		<div class="dynamic-post__main">
			<div class="dynamic-post__author">
				<a class="dynamic-post__author_name"   title="${EN_AUTHOR_NAME ?if_exists}" onclick='DiscussOpenDetail.openPsnDetail("${DES3_PSN_ID ?if_exists}",event)'>
					${EN_AUTHOR_NAME ?if_exists}
				</a>
				<span class="dynamic-post__author_action">shared</span>
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
								<div class="pub-idx__full-text_img" style="cursor: default;">
									<#if (FULL_TEXT_FIELD?exists)>
									    <a onclick="DiscussOpenDetail.fileDownloadEvent('${DES3_RES_ID?if_exists}', '${RES_TYPE?if_exists}', '${GROUP_DYN_ID?if_exists }', '${DYN_TYPE?if_exists}')">
    										<#if (FULL_TEXT_IMAGE?exists)>
    											<img src="${FULL_TEXT_IMAGE?if_exists}" onerror="src='/resscmwebsns/images_v5/images2016/file_img1.jpg'">
    										<#else>
    											<img src="/resscmwebsns/images_v5/images2016/file_img1.jpg">
    										</#if>
    									</a>
									<#else>
										<img src="/resscmwebsns/images_v5/images2016/file_img.jpg">
									</#if>
								</div>
							</div>
							<div class="pub-idx__main_box">
								<div class="pub-idx__main">
									<div class="pub-idx__main_title">
										<#if (PDWH_URL ?exists) && PDWH_URL==2>
											<a onclick='DiscussOpenDetail.openPdwhDetail("${DES3_RES_ID?if_exists}",event)'>
										<#else>
											<#if (PUB_INDEX_URL?exists)>
												<a onclick='DiscussOpenDetail.shortUrlOpenPubDetail("${PUB_INDEX_URL?if_exists}",event)'>
											<#else>
												<a onclick='DiscussOpenDetail.openPubDetail("${DES3_RES_ID?if_exists}",event)'>
											</#if>
										</#if>
										${EN_RES_NAME?if_exists}</a>
									</div>
									<div class="pub-idx__main_author">${AUTHOR_NAMES?if_exists}</div>
									<div class="pub-idx__main_src">${EN_RES_DESC?if_exists}</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</#if>
		</div>
	</div>
</div>
