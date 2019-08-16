<li onmouseover="dynMsgUtil.showDynOperation(this)"  onmouseout="dynMsgUtil.hideDynOperation(this)">
    <div class="ui_avatar">
      <a href="/scmwebsns/resume/psnView?des3PsnId=${des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank"><img src="${psnAvatar}" width="50" height="50" border="0" /></a>
    </div>
    <div class="publish-nr">
      <div class="moving-contat">
        <a href="/scmwebsns/resume/psnView?des3PsnId=${des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" class="Blue">${psnEnName}</a> added a friend.
        <div style="margin-top:10px;">
          <div class="imageslist" style="width:30px;height:30px;">
          	<#if (isExistFlag=='true')>
             <a href="/scmwebsns/resume/psnView?des3PsnId=${friend_des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank">
               <img src="${friend_psnAvatar}" width="30" height="30" border="0" style="vertical-align:top"/>
            </a>  
            <#else>
            	<a onclick="dynMsgUtil.emptySenderTip()">
        			<img src="${defaultAvatars}" width="30" height="30" border="0" style="vertical-align:top"/>
        			</a>
        		</#if>
          </div>
          <div class="friendcontant">
            <p class="fr-name" style="width:500px;font-size:12px;">
            <#if (isExistFlag=='true')>
              <a href="/scmwebsns/resume/psnView?des3PsnId=${friend_des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" class="Blue">${friend_psnEnName}</a>
          	<#else>
        			${friend_psnEnName}
        		</#if>
            </p>
            <p class="f8080 fr-name" style="width:500px;font-size:12px;"> <#if (friend_psnTitle?exists) && friend_psnTitle!="null">${friend_psnTitle}</#if></p>
          </div>
          <div style="clear:both"></div>
        </div>
        
      </div>
      
      <div class="appraisa-choose">
        <p class="t_detail">
           <#assign dynDateVar>
             <#if dynDate=='dateTime'>${normalEnDynDate}<#else>${dynDate} ago</#if>
           </#assign>
        <span class="f888 date01"> ${dynDateVar}</span>&nbsp;&nbsp;&nbsp;&nbsp;
         <#if (isMine==0 && isExistFlag=='true' && friend_des3PsnId!=currentDes3PsnId)>
         <a style="cursor:pointer;" class="Blue addFriendClass" id="addFriend_${friend_psnId}" psnhm="${friend_psnId?c}" des3Id="${friend_des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" >Add ${friend_psnEnName} as friend</a>
         </#if>
        </p>
      </div>
      
    </div>
    
    <div class="drop_down"><a style="cursor:pointer;display:none;" onmouseover="dynMsgUtil.extendDynOperation(this)" onmouseout="dynMsgUtil.shrinkDynOperation(this)" class="link_delete"></a>
        <div class="div_item_delete drop_down_en_US" style="display:none" onmouseout="$(this).hide()" onmouseover="$(this).show();$(this).parent().find('.link_delete').show();">
        <div class="drop_jt_en_US"></div>
           <ul>
             <#if (isMine==1)>
               <li><a style="cursor:pointer;" onclick="dynMsgUtil.ajaxDeleteDyn('${dynId?c}')"><i class="img_nochoose"></i>Delete</a></li>
               <li><a style="cursor:pointer; border-width:0;" onclick="dynMsgUtil.ajaxShieldDynByType('01','${dynId?c}')"><i class="img_nochoose"></i>Shield such update</a></li>
             <#else>
               <li><a style="cursor:pointer;" onclick="dynMsgUtil.ajaxDeleteDyn('${dynId?c}')"><i class="img_nochoose"></i>Delete</a></li>
               <li><a style="cursor:pointer;" onclick="dynMsgUtil.ajaxShieldDynByPsn('${dynId?c}')"><i class="img_nochoose"></i>Shield this person</a></li>
               <li><a style="cursor:pointer; border-width:0;" onclick="dynMsgUtil.ajaxShieldDynByType('01','${dynId?c}')"><i class="img_nochoose"></i>Shield such update</a></li>
             </#if>
           </ul>
         </div>
    </div>
 </li>