<li onmouseover="dynMsgUtil.showDynOperation(this)"  onmouseout="dynMsgUtil.hideDynOperation(this)">
    <div class="ui_avatar">
       <a href="/scmwebsns/resume/psnView?des3PsnId=${des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank"><img src="${psnAvatar}" width="50" height="50" border="0" /></a>
    </div>
    <div class="publish-nr">
      <div class="moving-contat">
        <a href="/scmwebsns/resume/psnView?des3PsnId=${des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" class="Blue">${psnEnName}</a>       
        <#if queryType==4>
        joined group
       <#else>
       joined a group
       <div style="margin-top:10px;">
          <div class="imageslist" style="width:30px;height:30px;">
             <#if (groupPage!='')>
             <a href="${groupPage}&menuId=1100" target="_blank">
               <img src="${groupAvatar}" width="30" height="30" border="0" style="vertical-align:top"/>
             </a>  
             </#if>
          </div>
          <div class="friendcontant">
            <p class="fr-name" style="width:500px;font-size:12px;">
               <#if (groupPage!='')><a href="${groupPage}&menuId=1100" class="Blue" target="_blank">${groupName}</a><#else>${groupName}</#if>
            </p>
            <p class="f8080 fr-name" style="width:500px;font-size:12px;">
               <label id="groupMember_${dynId?c}"></label>&nbsp;members&nbsp;&nbsp;&nbsp;&nbsp;<label id="groupPubNum_${dynId?c}"></label>&nbsp;publications
            </p>
          </div>
          <div style="clear:both"></div>
	     </div>
	     </#if>
      </div>
            
      <div class="appraisa-choose">
        <p class="t_detail">
             <#assign dynDateVar>
             <#if dynDate=='dateTime'>${normalEnDynDate}<#else>${dynDate} ago</#if>
             </#assign>
           <span class="f888 date01">${dynDateVar}</span>&nbsp;&nbsp;&nbsp;&nbsp;
           <#if (groupPage!='')&&(queryType!=4)><a href="${groupPage}&menuId=1100" class="Blue" target="_blank">View Group</a>&nbsp;</#if>
           <#if (joinGroupFlag==0)&&(queryType!=4)><a onclick="dynMsgUtil.ajaxJoinGroup(this,'${groupId?c}','${groupNode}')" style="cursor:pointer;" class="Blue apply_joingroup" dynId="${dynId?c}" groupId="${groupId?c}" groupNode="${groupNode}">Request to join</a></#if>
        </p>
      </div>
      
    </div>
    
    <div class="drop_down">
    		<a style="display:none;" class="group_params" isMine="${isMine}" isGroupManager="${isGroupManager}"></a>
    		<a style="cursor:pointer;display:none;" onmouseover="dynMsgUtil.extendDynOperation(this)" onmouseout="dynMsgUtil.shrinkDynOperation(this)" class="link_delete"></a>
        <div class="div_item_delete drop_down_en_US" style="display:none" onmouseout="$(this).hide()" onmouseover="$(this).show();$(this).parent().find('.link_delete').show();">
        <div class="drop_jt_en_US"></div>
           <ul>
                <li><a style="cursor:pointer; border-width:0;" onclick="dynMsgUtil.ajaxDeleteDyn('${dynId?c}')"><i class="img_nochoose"></i>Delete</a></li>
           </ul>
         </div>
    </div>
</li>