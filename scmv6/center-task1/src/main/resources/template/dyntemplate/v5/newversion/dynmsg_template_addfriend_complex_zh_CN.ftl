<li onmouseover="dynMsgUtil.showDynOperation(this)"  onmouseout="dynMsgUtil.hideDynOperation(this)">
    <div class="ui_avatar">
    <a href="/scmwebsns/resume/psnView?des3PsnId=${des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank"><img src="${psnAvatar}" width="50" height="50" border="0" /></a>
    </div>
    <div class="publish-nr">
      <div class="moving-contat">
       <a href="/scmwebsns/resume/psnView?des3PsnId=${des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" class="Blue">${psnName}</a>
       		与
       <#list friendDetails as friend>
       	<#if (friend.friend_psnId!=-1)>
	      	<a href="/scmwebsns/resume/psnView?des3PsnId=${friend.friend_des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" class="Blue">${friend.friend_psnName}</a>
	      <#else>
	      	${friend.friend_psnName}
	      </#if>
	      <#if friend_has_next>、</#if>
       </#list>成为了好友
       <#list friendDetails as friend>
          <#if friend_index==0>
           <#assign addFriendName>${friend.friend_psnName}</#assign>
           <#assign addFriendDes3Id>${friend.friend_des3PsnId}</#assign>
           <#assign addFriendId>${friend.friend_psnId?c}</#assign>
           <#assign addFriendPsnIns><#if friend.friend_psnIns?exists>${friend.friend_psnIns}</#if></#assign>
           <#assign addFriendPsnTitle><#if friend.friend_psnTitle?exists>${friend.friend_psnTitle}</#if></#assign>
	        <div style="margin-top:10px;">
	          <div class="imageslist" style="width:30px;height:30px;">
	          		<#if (friend.friend_psnId!=-1)>
		         	   <a href="/scmwebsns/resume/psnView?des3PsnId=${friend.friend_des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank">
		      	         <img src="${friend.friend_psnAvatar}" width="30" height="30" border="0" style="vertical-align:top"/>
	   		         </a>  
	   		      <#else>
	   		      	<a onclick="dynMsgUtil.emptySenderTip()">
	   		      	<img src="${defaultAvatars}" width="30" height="30" border="0" style="vertical-align:top"/>
	   		      	</a> 
	          		</#if>
	          </div>
	          <div class="friendcontant">
	            <p class="fr-name" style="width:500px;font-size:12px;">
	            	<#if (friend.friend_psnId!=-1)>
	               	<a href="/scmwebsns/resume/psnView?des3PsnId=${friend.friend_des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" class="Blue">${friend.friend_psnName}</a>
	               <#else>
	               	${friend.friend_psnName}
	               </#if>
	            </p>
	            <p class="f8080 fr-name" style="width:500px;font-size:12px;">${addFriendPsnTitle!''}</p>
	          </div>
	          <div style="clear:both"></div>
	        </div>
	        <#break>
          </#if>
       </#list>
      </div>
      <div class="appraisa-choose">
        <p class="t_detail">
          <#assign dynDateVar>
          <#if dynDate=='dateTime'>${normalDynDate}<#else>${dynDate?replace("s","秒")?replace("m","分钟")?replace("h","小时")?replace("d","天")}以前</#if>
          </#assign>
           <span class="f888 date01"> ${dynDateVar}</span>&nbsp;&nbsp;&nbsp;&nbsp;
           <#if (addFriendDes3Id!=currentDes3PsnId && isMine==0 && addFriendId!='-1')>
	           <a style="cursor:pointer;display:none;" class="Blue addFriendClass" psnhm="${addFriendId}" id="addFriend_${addFriendId}" des3Id="${addFriendDes3Id}" title="请求加${addFriendName}为好友">加${addFriendName}为好友</a>
	        </#if>
        </p>
      </div>
    </div>
       
    <div class="drop_down"><a style="cursor:pointer;display:none;" onmouseover="dynMsgUtil.extendDynOperation(this)" onmouseout="dynMsgUtil.shrinkDynOperation(this)" class="link_delete"></a>
        <div class="div_item_delete drop_down_zh_CN" style="display:none" onmouseout="$(this).hide()" onmouseover="$(this).show();$(this).parent().find('.link_delete').show();">
        <div class="drop_jt_zh_CN"></div>
           <ul>
             <#if (isMine==1)>
               <li><a style="cursor:pointer;" onclick="dynMsgUtil.ajaxDeleteDyn('${dynId?c}')"><i class="img_nochoose"></i>删除此动态</a></li>
               <li><a style="cursor:pointer; border-width:0;" onclick="dynMsgUtil.ajaxShieldDynByType('01','${dynId?c}')"><i class="img_nochoose"></i>屏蔽此类动态</a></li>
             <#else>
               <li><a style="cursor:pointer;" onclick="dynMsgUtil.ajaxDeleteDyn('${dynId?c}')"><i class="img_nochoose"></i>删除此动态</a></li>
               <li><a style="cursor:pointer;" onclick="dynMsgUtil.ajaxShieldDynByPsn('${dynId?c}')"><i class="img_nochoose"></i>屏蔽此人动态</a></li>
               <li><a style="cursor:pointer; border-width:0;" onclick="dynMsgUtil.ajaxShieldDynByType('01','${dynId?c}')"><i class="img_nochoose"></i>屏蔽此类动态</a></li>
             </#if>
           </ul>
         </div>
    </div>
 </li>