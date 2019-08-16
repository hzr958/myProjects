<div class="dynamic_container">
	<input type="hidden" name="dynId" value="${GROUP_DYN_ID }" des3ResId="${DES3_RES_ID?if_exists}" resType="${RES_TYPE}" resId="${RES_ID}" dynType="${DYN_TYPE}"/>
      <div class="dynamic_header">
       <div onclick='groupDynamic.openPsnDetail("${DES3_PSN_ID ?if_exists}",event)' style='cursor: pointer;'>
         <img src="${AUTHOR_AVATAR }" class="avatar" onerror="this.src='/ressns/images/phone120X120.gif'">
       </div>
       <div class="author_information">
         <div class="action" >
          <div class="name">${AUTHOR_NAME }</div>
        </div>
        <div class="institution">
         ${AUTHOR_INS_NAME }, Department of Information System, Professor              
       </div>
     </div>
     <div class="time"></div>          	      
   </div>
   <div class="dynamic_content_container">
    <div class="text_content">${GROUP_DYN_CONTENT }</div>
    
    
   </div>
 </div>
 <div class="dynamic_operations_container">
  <div class="single_action">赞</div>
  <div class="single_action">评论(8)</div>
  <div class="single_action" style="border: none;" onclick="openshare1(this)">分享</div>
</div>
<div class="dynamic_comment_sample_container">
</div>
</div>