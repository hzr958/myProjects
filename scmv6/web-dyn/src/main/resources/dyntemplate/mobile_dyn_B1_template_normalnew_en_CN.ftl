		 
		<input type="hidden" name="dynId" value="${DYN_ID?c }" dynOwnerDes3Id="${DYN_OWNER_DES3_ID?if_exists}" des3ResId="${DES3_RES_ID?if_exists}"  des3DynId="${DES3_DYN_ID?if_exists}" parentDynId="${PARENT_DYN_ID?c}" />  
		<div class="dynamic-creator__box" style="height: auto;">
          <div class="dynamic-creator__info" style="white-space: normal; padding: 8px 0px;"><a class="dynamic-creator__name" onclick="dynamic.openPsnDetail('${DES3_PRODUCER_PSN_ID}' ,event)">${PERSON_NAME_EN?if_exists }</a>${OPERATOR_TYPE_EN?if_exists }
	      	<div style="float:right" id="b1time_${DYN_ID?c}">${PUBLISH_TIME?if_exists }</div>
	      	<div class="dyn_content" transurl="true">${USER_ADD_CONTENT?if_exists }</div>
          </div>
        </div>
        <#if MOBILE_ORIGINAL_TEMPLATE_EN??>
	        ${MOBILE_ORIGINAL_TEMPLATE_EN}
	   	<#else>
		    ${ORIGINAL_TEMPLATE}
		</#if>
      