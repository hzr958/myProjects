<input type="hidden" name="dynId" value="${GROUP_DYN_ID?if_exists }" resId="${RES_ID?if_exists}"  dynType="${DYN_TYPE?if_exists}"/>
      <div class="dynamic_header">
       <div onclick='groupDynamic.openPsnDetail("${DES3_PSN_ID ?if_exists}",event)' style='cursor: pointer;'>
         <img src="${AUTHOR_AVATAR ?if_exists}" class="avatar" onerror="this.src='/ressns/images/phone120X120.gif'">
       </div>
       <div class="author_information">
         <div class="action" >
          <div class="name" onclick='groupDynamic.openPsnDetail("${DES3_PSN_ID ?if_exists}",event)' style='cursor: pointer;'>${ZH_AUTHOR_NAME ?if_exists}</div>
        </div>
        <div class="institution">
         ${PSN_WORK_INFO?if_exists }         
       </div>
     </div>
     <div class="time"></div>          	      
   </div>
   <div class="dynamic_content_container">
    <div class="text_content">${GROUP_DYN_CONTENT?if_exists }</div>
    	<#if (RES_ID?exists)>
    	<#if (RES_ID?exists&&GROUP_DYN_CONTENT?exists)>
    		<div class="dynamic_content_divider"></div>
    	</#if>
        	<div class="attachment">
      		<div class="pub_whole">
        		<div <#if (FULL_TEXT_FIELD?exists)> onclick='groupDynamic.fullTextDownload("${FULL_TEXT_FIELD?if_exists}")' style='cursor: pointer;' </#if> >
        			<#if (FULL_TEXT_FIELD?exists)>
        				<#if (FULL_TEXT_IMAGE?exists)>
        					<img src="${FULL_TEXT_IMAGE?if_exists}"  class="pub_avatar">
        				<#else>
        					<img src="/resscmwebsns/images_v5/images2016/file_img1.jpg"  class="pub_avatar">
        				</#if>
        			<#else>
        				<img src="/resscmwebsns/images_v5/images2016/file_img.jpg"  class="pub_avatar">
        			</#if>
        		</div>
        		<div class="pub_information">
         		<div class="title">
         			<span onclick='groupDynamic.openPubDetail("${RES_ID?if_exists}",event)' style='cursor: pointer;'>${ZH_RES_NAME?if_exists}</span>
         		</div>
         		<div class="author">${AUTHOR_NAMES?if_exists}</div>
         		<div class="source">${ZH_RES_DESC?if_exists}</div>
       		</div>	
     	</div>
     	</div>
    	</#if>
</div>