<li onmouseover="dynMsgUtil.showDynOperation(this)"  onmouseout="dynMsgUtil.hideDynOperation(this)">
     <div class="ui_avatar">
       <a href="/scmwebsns/resume/psnView?des3PsnId=${des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank"><img src="${psnAvatar}" width="50" height="50" border="0" /></a>
    </div>
    <div class="publish-nr">
      <div class="moving-contat">
        <#list joinerDetails as joiner><a href="/scmwebsns/resume/psnView?des3PsnId=${joiner.joiner_des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" class="Blue">${joiner.joiner_psnName}</a><#if joiner_has_next>、</#if></#list>
       <#if queryType==4>
        加入了群组
       <#else>
       加入了一个群组
       <div style="margin-top:10px;">
          <div class="imageslist" style="width:30px;height:30px;">
          <#if  (groupPage?exists)>  
             <#if (groupPage!='')>
             <a href="${groupPage}&menuId=1100" target="_blank">
               <img src="${groupAvatar}" width="30" height="30" border="0" style="vertical-align:top"/>
             </a>  
             </#if>
          </#if>
          </div>
          <div class="friendcontant">
            <p class="fr-name" style="width:500px;font-size:12px;">
               <#if  (groupPage?exists)> <#if (groupPage!='')><a href="${groupPage}&menuId=1100" class="Blue" target="_blank">${groupName}</a><#else>${groupName}</#if></#if>
            </p>
            <p class="f8080 fr-name" style="width:500px;font-size:12px;">
               <label id="groupMember_${dynId?c}"></label>名成员&nbsp;&nbsp;&nbsp;&nbsp;<label id="groupPubNum_${dynId?c}"></label>篇论文
            </p>
          </div>
          <div style="clear:both"></div>
	     </div>
	     </#if>
      </div>
            
      <div class="appraisa-choose">
        <p class="t_detail">
          <#assign dynDateVar>
          <#if dynDate=='dateTime'>${normalDynDate}<#else>${dynDate?replace("s","秒")?replace("m","分钟")?replace("h","小时")?replace("d","天")}以前</#if>
          </#assign>
           <span class="f888 date01">${dynDateVar}</span>&nbsp;&nbsp;&nbsp;&nbsp;
           <#if (groupPage?exists)&&(groupPage!='')&&(queryType!=4)><a href="${groupPage}&menuId=1100" class="Blue" target="_blank">查看群组</a>&nbsp;</#if>
           <#if (joinGroupFlag==0)&&(queryType!=4)&&(groupNode?exists)><a onclick="dynMsgUtil.ajaxJoinGroup(this,'${groupId?c}','${groupNode}')" style="cursor:pointer;" class="Blue apply_joingroup" dynId="${dynId?c}" groupId="${groupId?c}" groupNode="${groupNode}">申请加入群组</a></#if>
        </p>
      </div>
      
    </div>
    
    
    <div class="drop_down">
    		<a style="display:none;" class="group_params" isMine="${isMine}" isGroupManager="${isGroupManager}"></a>
    		<a style="cursor:pointer;display:none;" onmouseover="dynMsgUtil.extendDynOperation(this)" onmouseout="dynMsgUtil.shrinkDynOperation(this)" class="link_delete"></a>
        <div class="div_item_delete drop_down_zh_CN" style="display:none" onmouseout="$(this).hide()" onmouseover="$(this).show();$(this).parent().find('.link_delete').show();">
        <div class="drop_jt_zh_CN"></div>
           <ul>
                <li><a style="cursor:pointer; border-width:0;" onclick="dynMsgUtil.ajaxDeleteDyn('${dynId?c}')"><i class="img_nochoose"></i>删除此动态</a></li>
           </ul>
         </div>
    </div>
</li>