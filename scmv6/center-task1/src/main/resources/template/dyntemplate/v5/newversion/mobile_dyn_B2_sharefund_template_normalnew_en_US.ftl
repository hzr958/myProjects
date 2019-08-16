<input type="hidden" name="dynId" value="${DYN_ID?c }" resType="${RES_TYPE}" resId="${RES_ID?c}" dynType="${DYN_TYPE}" databaseType="${PDWH_URL ?if_exists}" parentDynId="${PARENT_DYN_ID?c}" dbId ='${DB_ID?if_exists}'/>
        <div class="dynamic-content">
          <div class="dynamic-content__post">
            <div class="dynamic-post__avatar" onclick="dynamic.openPsnDetail('${DES3_PRODUCER_PSN_ID}' ,event)"> <img src="${PERSON_AVATARS?if_exists }" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'"> </div>
            <div class="dynamic-post__main" onclick="dynamic.openPsnDetail('${DES3_PRODUCER_PSN_ID}' ,event)">
              <div class="dynamic-post__author" > 
              	<a class="dynamic-post__author_name" >${ PERSON_NAME_ZH?if_exists }</a>
                <span class="dynamic-post__author_action">${OPERATOR_TYPE_ZH?if_exists }</span>
               </div>
              <div class="dynamic-post__author_inst">${PERSON_INSINFO_ZH?if_exists }</div>
            </div>
            <div class="dynamic-post__time" time_${DYN_ID?c}"   id="time_${DYN_ID?c}" style="pointer-events: auto;display: flex;justify-content: flex-end;align-items: center;">
            ${PUBLISH_TIME?if_exists }
            <#if PUBLISH_TIME??>
                <i class="material-icons" style="font-size: 20px;margin-left: 4px;color: #333;">more_horiz</i>
            </#if>
            </div>
          </div>
          <div class="dynamic-content__main">
            <div class="dynamic-main__box">
              <#if (RES_TYPE ==  11)> 
              <div class="dynamic-main__att"  >
                <div class="pub-idx_medium">
                  <div class="pub-idx__base-info">
                    <div class="pub-idx__full-text_box">
                      <div class="pub-idx__full-text_img"><a href="/prjweb/wechat/findfundsxml?des3FundId=${ENCODE_FUND_ID?if_exists}"><img src="<#if (FUND_LOGO_URL?exists)>${FUND_LOGO_URL?if_exists}<#else>/ressns/images/default/default_fund_logo.jpg</#if>" onerror="this.src='/ressns/images/default/default_fund_logo.jpg'"></a></div>
                    </div>
                    <div class="pub-idx__main_box" onclick="dynamic.openNotNextFundDetail(${ENCODE_FUND_ID?if_exists}, ev);">
                      <div class="pub-idx__main">
						<div class="pub-idx__main_title">${FUND_TITLE_EN?if_exists }</div>
                        <div class="pub-idx__main_author"></div>
                        <div class="pub-idx__main_src"><p style="font-style:normal;">${FUND_DESC_EN?if_exists }</p></div>
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
        
             	<div class="dynamic-social__item dynamic-social__item-normal-size dev_awardcount_${DYN_ID?c}"   onclick="dynamic.awardDyn( ${RES_TYPE},${RES_ID?c}, '${DYN_TYPE}', ${DYN_ID?c},${PARENT_DYN_ID?c},event)"><a>Like</a></div>
                <!--<div class="dynamic-social__item  dev_commentcount_${DYN_ID?c }"  onclick="dynamic.replyDyn(${RES_TYPE},'${RES_ID}', '${DYN_TYPE}', ${DYN_ID?c},event)"><a>Comment</a></div>-->
          		<div class="dynamic-social__item dynamic-social__item-normal-size dev_sharecount_${DYN_ID?c }"    resType="${RES_TYPE}"  resId="${RES_ID?c}"  dynType="${DYN_TYPE}" dynId="${DYN_ID?c}" parentDynId="${PARENT_DYN_ID?c}"   onclick="SmateShare.getParam(this);initSharePlugin(this);"><a>Share</a></div>        
                <div class="dynamic-social__item dynamic-social__item-normal-size collectCancel_${FUND_ID?c }" style="display:none;" id="collectCancel_${DYN_ID?c }"  parentDynId="${PARENT_DYN_ID?c}"   onclick="javascript:FundRecommend.dynCollectCoperation($(this), '${ENCODE_FUND_ID?if_exists}', 1, '${FUND_ID?c }');"><a>Cancel</a></div>
          		<div class="dynamic-social__item dynamic-social__item-normal-size collect_${FUND_ID?c }" id="collect_${DYN_ID?c }"  parentDynId="${PARENT_DYN_ID?c}"   onclick="javascript:FundRecommend.dynCollectCoperation($(this), '${ENCODE_FUND_ID?if_exists}', 0, '${FUND_ID?c }');"><a>Save</a></div>
        </div>