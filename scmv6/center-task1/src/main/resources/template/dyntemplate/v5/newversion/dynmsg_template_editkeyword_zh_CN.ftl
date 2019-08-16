<li onmouseover="dynMsgUtil.showDynOperation(this)"  onmouseout="dynMsgUtil.hideDynOperation(this)">
    <div class="ui_avatar">
       <a href="/scmwebsns/resume/psnView?des3PsnId=${des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank"><img src="${psnAvatar}" width="50" height="50" border="0" /></a>
    </div>
    <div class="publish-nr">
      <div class="moving-contat"><a href="/scmwebsns/resume/psnView?des3PsnId=${des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" class="Blue">${psnName}</a><label style="color:#999">更新了研究领域：</label>
	     <#if (keyDetails?exists) && (keyDetails?size>0)> 
	     	<p style="margin-top:10px;"> 
		     <#list keyDetails as key>
		     ${key.keyword}<#if key_has_next>；</#if>
		     </#list>
	      </p>
	    </#if>
     </div>
      
       <div class="appraisa-choose">
        <p class="t_detail">
          <#assign dynDateVar>
          <#if dynDate=='dateTime'>${normalDynDate}<#else>${dynDate?replace("s","秒")?replace("m","分钟")?replace("h","小时")?replace("d","天")}以前</#if>
          </#assign>
          <span class="f888 date01"> ${dynDateVar}</span>&nbsp;&nbsp;&nbsp;&nbsp;<!--<a href="#" class="Blue"><i class="icon-praise"></i>赞(${awardNum})</a> - <a href="#" class="Blue">评论(${replyNum})</a> - <a href="#" class="Blue">分享(${shareNum})</a>-->
        </p>
       </div>
    </div>
    
    <div class="drop_down"><a style="cursor:pointer;display:none;" onmouseover="dynMsgUtil.extendDynOperation(this)" onmouseout="dynMsgUtil.shrinkDynOperation(this)" class="link_delete"></a>
        <div class="div_item_delete drop_down_zh_CN" style="display:none" onmouseout="$(this).hide()" onmouseover="$(this).show();$(this).parent().find('.link_delete').show();">
        <div class="drop_jt_zh_CN"></div>
           <ul>
            <#if (isMine==1)>
              <li><a style="cursor:pointer;" onclick="dynMsgUtil.ajaxDeleteDyn('${dynId?c}')"><i class="img_nochoose"></i>删除此动态</a></li>
              <li><a style="cursor:pointer; border-width:0;" onclick="dynMsgUtil.ajaxShieldDynByType('05','${dynId?c}')"><i class="img_nochoose"></i>屏蔽此类动态</a></li>
            <#else>
               <li><a style="cursor:pointer;" onclick="dynMsgUtil.ajaxDeleteDyn('${dynId?c}')"><i class="img_nochoose"></i>删除此动态</a></li>
               <li><a style="cursor:pointer;" onclick="dynMsgUtil.ajaxShieldDynByPsn('${dynId?c}')"><i class="img_nochoose"></i>屏蔽此人动态</a></li>
               <li><a style="cursor:pointer; border-width:0;" onclick="dynMsgUtil.ajaxShieldDynByType('05','${dynId?c}')"><i class="img_nochoose"></i>屏蔽此类动态</a></li>
             </#if>
           </ul>
         </div>
    </div>
</li>