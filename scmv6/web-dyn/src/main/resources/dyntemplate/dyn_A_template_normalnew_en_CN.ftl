        <input type="hidden" name="dynId" value="${DYN_ID?c }" dynOwnerDes3Id="${DYN_OWNER_DES3_ID?if_exists}" resOwnerDes3Id="${RES_OWNER_DES3ID?if_exists}" des3ResId="${DES3_RES_ID?if_exists}"  des3DynId="${DES3_DYN_ID?if_exists}" resType="${RES_TYPE}" resId="${RES_ID?c}" dynType="${DYN_TYPE}" databaseType="${PDWH_URL ?if_exists}" parentDynId="${PARENT_DYN_ID?c}" dbId ="${DB_ID?if_exists}"/>
        <div class="dynamic-content">
          <div class="dynamic-content__post" >
            <div class="dynamic-post__avatar"> <img  onclick="dynamic.openPsnDetail('${DES3_PRODUCER_PSN_ID}' ,event)" src="${PERSON_AVATARS?if_exists }" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'"> </div>
            <div class="dynamic-post__main" >
              <div class="dynamic-post__author" > <a class="dynamic-post__author_name"  title="${ PERSON_NAME_EN?if_exists }" onclick="dynamic.openPsnDetail('${DES3_PRODUCER_PSN_ID}' ,event)" >${ PERSON_NAME_EN?if_exists }</a> </div>
              <div class="dynamic-post__author_inst">${PERSON_INSINFO_EN?if_exists }</div>
            </div>
            <div class="dynamic-post__time" id="time_${DYN_ID?c}">${PUBLISH_TIME?if_exists }</div>
          </div>
          <div class="dynamic-content__main">
            <div class="dynamic-main__box">
              <div class="dyn_content">${USER_ADD_CONTENT?if_exists }</div>
              <#if (RES_TYPE!=0)>
              <div class="dynamic-divider"></div>
              <div class="dynamic-main__att"  >
                <div class="pub-idx_medium">
                  <div class="pub-idx__base-info">
                    <div class="pub-idx__full-text_box ">
                      <div class="pub-idx__full-text_img pub-idx__full-text_newbox dev_pub_fulltext" des3pubid="${FULLTEXT_DES3PUBID?if_exists }" des3fileid="${FULLTEXT_DES3FILEID?if_exists }">
                      <a  class="pub-idx__full-text_newbox-avator" onclick="dynamic.fullTextEvent('${DES3_DYN_ID}','${DYN_TYPE}','${DES3_RES_ID}','${RES_TYPE}',event)">
                     <img src="${LINK_IMAGE?if_exists}" onerror="src='/resmod/images_v5/images2016/file_img1.jpg'">
                     </a>
                      </div>
                    </div>
                    <div class="pub-idx__main_box">
                      <div class="pub-idx__main">
                        <div class="pub-idx__main_title">
                        <a onclick="dynamic.openDetail('${DES3_DYN_ID}','${DYN_TYPE}','${DES3_RES_ID}','${RES_TYPE}',event)">${LINK_TITLE_EN?if_exists }</a>
                        </div>
                        <div class="pub-idx__main_author">${PUB_AUTHORS?if_exists }</div>
                        <div class="pub-idx__main_src"><p style="font-style:normal;">${PUB_DESCR_EN?if_exists }</p><#if (PUB_PUBLISHYEAR?exists)> </#if></div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              </#if> 
            </div>
          </div>
        </div>
        <div class="dynamic-social__list">
          <div class="dynamic-social__item  dev_awardcount_${DYN_ID?c}"   onclick="dynamic.awardDyn(${RES_TYPE},'${DES3_RES_ID}','${DYN_TYPE}','${DES3_DYN_ID}','${DES3_PARENT_DYN_ID}',event)"><a>Like</a></div>
          <div class="dynamic-social__item  dev_commentcount_${DYN_ID?c }"  onclick="dynamic.replyDyn(${RES_TYPE},'${DES3_RES_ID}', '${DYN_TYPE}', '${DES3_DYN_ID}',event)"><a>Comment</a></div>
          <div class="dynamic-social__item  dev_sharecount_${DYN_ID?c }"    resType="${RES_TYPE}"  resId="${RES_ID?c}"  dynType="${DYN_TYPE}" databaseType="${PDWH_URL ?if_exists}" 
          des3ResId="${DES3_RES_ID?if_exists}" des3PubId="${DES3_PUB_ID?if_exists}" dynId="${DYN_ID?c}"  parentDynId="${PARENT_DYN_ID?c}"   
          onclick="dynamic.shareDynMain('${DES3_DYN_ID}','${DYN_TYPE}','${DES3_RES_ID}','${RES_TYPE}',event);"
          ><a>Share</a></div>
          <#if (RES_TYPE!=0&&RES_TYPE!=4)>
          <#if (RES_TYPE!=11&&RES_TYPE!=25&&RES_TYPE!=26)>
          <div class="dynamic-social__item dev_cite"><a onclick="dynamic.dynCitePub('${DES3_DYN_ID}','${DYN_TYPE}','${DES3_RES_ID}','${RES_TYPE}',event);"  title="Cite">Cite</a></div>
          </#if>
          <#if (RES_TYPE!=26)>    
          <div class="dynamic-social__item dev_save" onclick="dynamic.dynCollectPub('${DES3_DYN_ID}','${DYN_TYPE}','${DES3_RES_ID}','${RES_TYPE}',event);"><a title="Save">Save</a></div>
          </#if>
          <a class="thickbox dyn_thickbox"></a>
          </#if>
        </div>