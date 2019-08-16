	<input type="hidden" name="dynId" value="${GROUP_DYN_ID?if_exists }" des3ResId="${DES3_RES_ID?if_exists}" resType="${RES_TYPE?if_exists}" resId="${RES_ID?if_exists}" dynType="${DYN_TYPE?if_exists}"/>
      <div class="dynamic-content">
          <div class="dynamic-content__post">
            <div class="dynamic-post__avatar" onclick='DiscussOpenDetail.openPsnDetail("${DES3_PSN_ID ?if_exists}",event)'> 
			<a><img src="${AUTHOR_AVATAR ?if_exists}" onerror="this.src='/ressns/images/phone120X120.gif'"></a>
			</div>
            <div class="dynamic-post__main">
              <div class="dynamic-post__author" > 
              	<a class="dynamic-post__author_name"  title="${ZH_AUTHOR_NAME ?if_exists}" onclick='DiscussOpenDetail.openPsnDetail("${DES3_PSN_ID ?if_exists}",event)'>
					${ZH_AUTHOR_NAME ?if_exists}
				</a> 
				<span class="dynamic-post__author_action">分享了</span>
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
              	<input type="hidden" id="zhTitle_${GROUP_NOTENCODE_DYN_ID }" value="${ZH_RES_NAME }"/>
      			<input type="hidden" id="enTitle_${GROUP_NOTENCODE_DYN_ID }" value="${EN_RES_NAME }"/>
      			<input type="hidden" id="zhShowDesc_${GROUP_NOTENCODE_DYN_ID }" value="${ZH_RES_DESC }"/>
      			<input type="hidden" id="enShowDesc_${GROUP_NOTENCODE_DYN_ID }" value="${EN_RES_DESC }"/>
              <div class="dynamic-main__att">
                <div class="pub-idx_medium">
                  <div class="pub-idx__base-info">
                    <div class="pub-idx__full-text_box">
                      <div class="pub-idx__full-text_img">
                      <a href="/dynweb/news/details?des3NewsId=${RES_ID?if_exists}"  target='_blank'>
	                     <#if (NEWS_LOGO_URL?exists)>
	        				<img src="${NEWS_LOGO_URL?if_exists}"  onerror="this.src='/resmod/smate-pc/img/logo_newsdefault.png'">	
	        			<#else>
	        				<img src="/resmod/smate-pc/img/logo_newsdefault.png"/>
	        			</#if>
	        		  </a>
                      </div>
                    </div>
                    <input type="hidden" class="need_init_agency fundDyn" value="${RES_ID?if_exists}"/>
                    <div class="pub-idx__main_box">
                      <div class="pub-idx__main">
                        <div class="pub-idx__main_title">
	        				<a href="/dynweb/news/details?des3NewsId=${RES_ID?if_exists}"  target='_blank'>${ZH_RES_NAME?if_exists}</a>
	        			</div>
                        <div class="pub-idx__main_author"></div>
                        <div class="pub-idx__main_src">${ZH_RES_DESC?if_exists}</div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              </#if>
            </div>
          </div>
 </div>
