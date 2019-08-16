        <input type="hidden" name="dynId" value="${DYN_ID?c }" dynOwnerDes3Id="${DYN_OWNER_DES3_ID?if_exists}" resOwnerDes3Id="${RES_OWNER_DES3ID?if_exists}" des3ResId="${DES3_RES_ID?if_exists}"  des3DynId="${DES3_DYN_ID?if_exists}" resType="${RES_TYPE}" resId="${RES_ID?c}" dynType="${DYN_TYPE}" databaseType="${PDWH_URL ?if_exists}" dbId ='${DB_ID?if_exists}' parentDynId="${PARENT_DYN_ID?c}"/>
        <div class="dynamic-content">
          <div class="dynamic-content__post" >
            <div class="dynamic-post__avatar" onclick="dynamic.openPsnDetail('${DES3_PRODUCER_PSN_ID}' ,event)"> <img src="${PERSON_AVATARS?if_exists }" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'"> </div>
            <div class="dynamic-post__main" onclick="dynamic.openPsnDetail('${DES3_PRODUCER_PSN_ID}' ,event)">
              <div class="dynamic-post__author" > 
              	<a class="dynamic-post__author_name" >${ PERSON_NAME_ZH?if_exists }</a>
                <span class="dynamic-post__author_action">${OPERATOR_TYPE_EN?if_exists }</span>
               </div>
              <div class="dynamic-post__author_inst">${PERSON_INSINFO_ZH?if_exists }</div>
            </div>
            <div class="dynamic-post__time" id="time_${DYN_ID?c}">${PUBLISH_TIME?if_exists }</div>
          </div>
          <div class="dynamic-content__main">
            <div class="dynamic-main__box">
              <div class="dynamic-main__att"  >
                <div class="pub-idx_medium">
                  <div class="pub-idx__base-info">
                    <div class="pub-idx__full-text_box">
                      <div class="pub-idx__full-text_img dev_pub_fulltext">
                      <a onclick="dynamic.fullTextEvent('${DES3_DYN_ID}','${DYN_TYPE}','${DES3_RES_ID}','${RES_TYPE}',event)">
						<img src="${LINK_IMAGE?if_exists}" onerror="src='/resmod/images_v5/images2016/file_img1.jpg'"> 
                     </a>
                      </div>
                    </div>
                    <div class="pub-idx__main_box" onclick="dynamic.openDetail('${DES3_DYN_ID}','${DYN_TYPE}','${DES3_RES_ID}','${RES_TYPE}',event)">
                      <div class="pub-idx__main">
						 <div class="pub-idx__main_title" style="font-size: 14px;">
						   <a>${LINK_TITLE_EN?if_exists }</a>
						 </div>
                        <div class="pub-idx__main_author">${PUB_AUTHORS?if_exists }</div>
                        <div class="pub-idx__main_src"><p style="font-style:normal;">${PUB_DESCR_ZH?if_exists }</p></div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="dynamic-social__list">
            <div class="dynamic-social__item dynamic-social__item-left dynamic-social__item_mobile dev_awardcount_${DYN_ID?c}" style="padding:0px!important; font-size: 12px; display: flex; justify-content: center;"  onclick="dynamic.awardDyn(${RES_TYPE},'${DES3_RES_ID}','${DYN_TYPE}','${DES3_DYN_ID}','${DES3_PARENT_DYN_ID}',event)">
               <i class="paper_footer-tool paper_footer-fabulous"></i>
               <a class="dynamic-social__item-content_detaile">Like</a>
            </div>
            <#if (RES_TYPE!=11&&RES_TYPE!=25&&RES_TYPE!=26)> 
            	<div class="dynamic-social__item dynamic-social__item-left dynamic-social__item_mobile dev_commentcount_${DYN_ID?c }" style="padding:0px!important; font-size: 12px; display: flex; justify-content: center;" onclick="dynamic.replyDyn(${RES_TYPE},'${DES3_RES_ID}', '${DYN_TYPE}', '${DES3_DYN_ID}',event)">
            	   <i class="paper_footer-tool paper_footer-comment2"></i>
            	   <a class="dynamic-social__item-content_detaile">Comment</a>
            	</div>
        	</#if>
        	<#if (RES_TYPE==26)> 
            <div class="dynamic-social__item dynamic-social__item-left dynamic-social__item_mobile menuList_list-container_none  dev_viewcount_${DYN_ID?c }" style="align-items:center;" onclick="">
                <i class="new-Standard_Share-checkinfor"></i>
                <a>View</a>
            </div>
            </#if>
          	<div class="dynamic-social__item dynamic-social__item-left dynamic-social__item_mobile dev_sharecount_${DYN_ID?c }" style="padding:0px!important; font-size: 12px; display: flex; justify-content: center;"   resType="${RES_TYPE}"  resId="${RES_ID?c}"  dynType="${DYN_TYPE}" dynId="${DYN_ID?c}" databaseType="${PDWH_URL ?if_exists}" dbId ='${DB_ID?if_exists}' parentDynId="${PARENT_DYN_ID?c}" 
            onclick="dynamic.shareDynMain('${DES3_DYN_ID}','${DYN_TYPE}','${DES3_RES_ID}','${RES_TYPE}',event);">
               <i class="paper_footer-tool paper_footer-share"></i>
               <a class="dynamic-social__item-content_detaile share_word_a">Share</a>
            </div>      
            <#if (RES_TYPE!=4 && RES_TYPE!=26)>  
            <div class="dynamic-social__item dynamic-social__item-left dynamic-social__item_mobile dev_save" style="padding:0px!important; font-size: 12px; display: flex; justify-content: center;" onClick="dynamic.dynCollectPub('${DES3_DYN_ID}','${DYN_TYPE}','${DES3_RES_ID}','${RES_TYPE}',event);">
               <i class="paper_footer-tool paper_footer-comment"></i>
               <a class="dynamic-social__item-content_detaile" title="Save">Save</a>
            </div> 
            </#if>     
        </div>