		 
		<input type="hidden" name="dynId" value="${DYN_ID?c }" dynOwnerDes3Id="${DYN_OWNER_DES3_ID?if_exists}" des3ResId="${DES3_RES_ID?if_exists}"  des3DynId="${DES3_DYN_ID?if_exists}" parentDynId="${PARENT_DYN_ID?c}" />  
		<div class="dynamic-creator__box" style="position: relative; height: auto;">
          <div class="dyn-publish_content">
               <span class="dyn-publish_content-author" >
                  <a class="dynamic-creator__name" style="margin-right:4px;" title="${PERSON_NAME_ZH?if_exists }" onclick="dynamic.openPsnDetail('${DES3_PRODUCER_PSN_ID}' ,event)">${PERSON_NAME_ZH?if_exists }</a>${OPERATOR_TYPE_ZH?if_exists }
          		</span>
          		<div class="dyn-publish_content-detail" transurl="true">${USER_ADD_CONTENT?if_exists }</div>
          </div>
        </div>
         ${ORIGINAL_TEMPLATE_ZH}