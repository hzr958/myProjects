        <input type="hidden" name="dynId" value="${DYN_ID?c }" dynOwnerDes3Id="${DYN_OWNER_DES3_ID?if_exists}" resOwnerDes3Id="${RES_OWNER_DES3ID?if_exists}" des3ResId="${DES3_RES_ID?if_exists}"  des3DynId="${DES3_DYN_ID?if_exists}" resType="${RES_TYPE}" resId="${RES_ID?c}" dynType="${DYN_TYPE}" databaseType="${PDWH_URL ?if_exists}" parentDynId="${PARENT_DYN_ID?c}" dbId ='${DB_ID?if_exists}'/>
        <div class="dynamic-content">
          <div class="dynamic-content__post" >
            <div class="dynamic-post__avatar" onclick="dynamic.openPsnDetail('${DES3_PRODUCER_PSN_ID}' ,event)"> <img src="${PERSON_AVATARS?if_exists }" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'"> </div>
            <div class="dynamic-post__main" onclick="dynamic.openPsnDetail('${DES3_PRODUCER_PSN_ID}' ,event)">
              <div class="dynamic-post__author" > 
              	<a class="dynamic-post__author_name" >${ PERSON_NAME_ZH?if_exists }</a>
                <span class="dynamic-post__author_action">${OPERATOR_TYPE_ZH?if_exists }</span>
               </div>
              <div class="dynamic-post__author_inst">${PERSON_INSINFO_ZH?if_exists }</div>
            </div>
            <div class="dynamic-post__time" id="time_${DYN_ID?c}">${PUBLISH_TIME?if_exists }</div>
          </div>
          <div class="dynamic-content__main mobile">
            <div class="dynamic-main__box">
              <div class="dynamic-main__att"  >
                <div class="pub-idx_medium">
                  <div class="pub-idx__base-info">
                    <div class="pub-idx__full-text_box">
                      <div class="pub-idx__full-text_img dev_pub_fulltext">
                      <a onclick="dynamic.fullTextEvent('${DES3_DYN_ID}','${DYN_TYPE}','${DES3_RES_ID}','${RES_TYPE}',event)">
						<img src="${LINK_IMAGE?if_exists}"> 
                     </a>
                      </div>
                    </div>
                    <div class="pub-idx__main_box" onclick="dynamic.openDetail('${DES3_DYN_ID}','${DYN_TYPE}','${DES3_RES_ID}','${RES_TYPE}',event)">
                      <div class="pub-idx__main">
                        <div class="pub-idx__main_title" style="font-size: 14px;">
						   <a>${LINK_TITLE_ZH?if_exists }</a>
						 </div> 
                        <div class="pub-idx__main_author">${PUB_AUTHORS?if_exists }</div>
                        <div class="pub-idx__main_src"><span class="pub-idx__main_src_desc">${PUB_DESCR_ZH?if_exists }</span><#if (PUB_PUBLISHYEAR?exists)> <span class="fccc">| ${PUB_PUBLISHYEAR?c}</span></#if></div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="dynamic-social__list">
         <div class="dynamic-social__item dynamic-social__item_mobile dev_awardcount_${DYN_ID?c}" style="padding:0px!important; font-size: 12px;"  onclick="dynamic.awardDyn(${RES_TYPE},'${DES3_RES_ID}','${DYN_TYPE}','${DES3_DYN_ID}','${DES3_PARENT_DYN_ID}',event)"><a>赞</a></div>
        <div class="dynamic-social__item dynamic-social__item_mobile dev_commentcount_${DYN_ID?c }" style="padding:0px!important; font-size: 12px;" onclick="dynamic.replyDyn(${RES_TYPE},'${DES3_RES_ID}', '${DYN_TYPE}', '${DES3_DYN_ID}',event)"><a>评论</a></div>
          <div class="dynamic-social__item dynamic-social__item_mobile dev_sharecount_${DYN_ID?c }"  style="padding:0px!important; font-size: 12px;"  resType="${RES_TYPE}"  resId="${RES_ID?c}"  dynType="${DYN_TYPE}" dynId="${DYN_ID?c}" databaseType="${PDWH_URL ?if_exists}" dbId ='${DB_ID?if_exists}' parentDynId="${PARENT_DYN_ID?c}" 
            onclick="dynamic.shareDynMain('${DES3_DYN_ID}','${DYN_TYPE}','${DES3_RES_ID}','${RES_TYPE}',event);"><a>分享</a></div>  
            <div class="dynamic-social__item dynamic-social__item_mobile dev_save" style="padding:0px!important; font-size: 12px;" onClick="dynamic.dynCollectPub('${DES3_DYN_ID}','${DYN_TYPE}','${DES3_RES_ID}','${RES_TYPE}',event);"><a>收藏</a></div>      
        </div>