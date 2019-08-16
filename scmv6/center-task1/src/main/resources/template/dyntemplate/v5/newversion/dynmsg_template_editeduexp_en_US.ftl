<li onmouseover="dynMsgUtil.showDynOperation(this)"  onmouseout="dynMsgUtil.hideDynOperation(this)">
    <div class="ui_avatar">
       <a href="/scmwebsns/resume/psnView?des3PsnId=${des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank"><img src="${psnAvatar}" width="50" height="50" border="0" /></a>
    </div>
    <div class="publish-nr">
      <div class="moving-contat">
        <a href="/scmwebsns/resume/psnView?des3PsnId=${des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" class="Blue">${psnEnName}</a> updated education experience
      <#if eduName!=''||eduSpecialty!=''||eduDegree!=''>
      <p style="margin-top:10px;">${eduName}<#if eduName!='' && eduSpecialty!=''>, ${eduSpecialty}<#else>${eduSpecialty}</#if><#if (eduSpecialty!='' || eduName!='') && eduDegree!=''>, ${eduDegree}<#else>${eduDegree}</#if></p>
      </#if>
      </div>
      
      <div class="appraisa-choose">
        <p class="t_detail">
             <#assign dynDateVar>
             <#if dynDate=='dateTime'>${normalEnDynDate}<#else>${dynDate} ago</#if>
             </#assign>
        <span class="f888 date01"> ${dynDateVar}</span>
      </div>     
    </div>
    
    <div class="drop_down"><a style="cursor:pointer;display:none;" onmouseover="dynMsgUtil.extendDynOperation(this)" onmouseout="dynMsgUtil.shrinkDynOperation(this)" class="link_delete"></a>
        <div class="div_item_delete drop_down_en_US" style="display:none" onmouseout="$(this).hide()" onmouseover="$(this).show();$(this).parent().find('.link_delete').show();">
        <div class="drop_jt_en_US"></div>
           <ul>
              <#if (isMine==1)>
                <li><a style="cursor:pointer;" onclick="dynMsgUtil.ajaxDeleteDyn('${dynId?c}')"><i class="img_nochoose"></i>Delete</a></li>
                <li><a style="cursor:pointer; border-width:0;" onclick="dynMsgUtil.ajaxShieldDynByType('05','${dynId?c}')"><i class="img_nochoose"></i>Shield such update</a></li>
              <#else>
                <li><a style="cursor:pointer;" onclick="dynMsgUtil.ajaxDeleteDyn('${dynId?c}')"><i class="img_nochoose"></i>Delete</a></li>
                <li><a style="cursor:pointer;" onclick="dynMsgUtil.ajaxShieldDynByPsn('${dynId?c}')"><i class="img_nochoose"></i>Shield this person</a></li>
                <li><a style="cursor:pointer; border-width:0;" onclick="dynMsgUtil.ajaxShieldDynByType('05','${dynId?c}')"><i class="img_nochoose"></i>Shield such update</a></li>
             </#if>
           </ul>
         </div>
    </div>
</li>