<input type="hidden" name="dynId" value="${GROUP_DYN_ID?if_exists }" des3ResId="${DES3_RES_ID?if_exists}"  dynType="${DYN_TYPE?if_exists}"/>
<div class="dynamic_header">
       <div onclick='groupDynamic.openPsnDetail("${DES3_PSN_ID ?if_exists}",event)' style='cursor: pointer;'>
         <img src="${AUTHOR_AVATAR?if_exists }" class="avatar" onerror="this.src='/ressns/images/phone120X120.gif'">
       </div>
       <div class="author_information">
         <div class="action" >
          <div class="name" onclick='groupDynamic.openPsnDetail("${DES3_PSN_ID ?if_exists}",event)' style='cursor: pointer;'>
           	${ZH_AUTHOR_NAME?if_exists }
           </div>
          <div class="type">上传了</div>
        </div>
        <div class="institution">
          ${PSN_WORK_INFO?if_exists }               
       </div>
     </div>
     <div class="time"></div>          	      
   </div>
   <div class="dynamic_content_container">
    <div class="text_content">${GROUP_DYN_CONTENT?if_exists}</div>
   <#if (RES_ID?exists&&GROUP_DYN_CONTENT?exists)>
    	<div class="dynamic_content_divider"></div>
    </#if>
        <div class="attachment">
      		<div class="pub_whole">
        		<div onclick="groupDynamic.openFile('${RES_ID?if_exists}','1','1');" style='cursor: pointer;'>
        		<img src="${FILE_IMAGE?if_exists}"  class="pub_avatar">
        		</div>
        		<div class="pub_information">
         		<div class="title" ><span onclick="groupDynamic.openFile('${RES_ID?if_exists}','1','1');" style='cursor: pointer;'>${RES_NAME?if_exists}</span></div>
       		</div>	
     	</div>
   </div>
   </div>
