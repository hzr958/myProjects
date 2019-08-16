<li>
    <div class="ui_avatar">
      <a href="/scmwebsns/resume/psnView?des3PsnId=${des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank"><img src="${psnAvatar}" width="50" height="50" border="0" /></a>
    </div>
    <div class="publish-nr">
      <div class="moving-contat">
        <#assign splitChar></#assign>
        <a href="/scmwebsns/resume/psnView?des3PsnId=${des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" class="Blue">${psnEnName}</a> updated 
          <#if queryType==4>
             the group 
          <#else>
             <#if (groupPage!='')><a href="${groupPage}&menuId=1100" class="Blue">${groupName}</a><#else>${groupName}</#if>'s
          </#if>
          <#list editTypeDetails as editItem><#if (!editItem_has_next) && (editItem_index!=0)> and <#else>${splitChar}</#if><#switch editItem.editType><#case 0>about<#break><#case 1>settings<#break><#case 2>profile<#break><#case 3>announcement<#break><#case 4>course objectives<#break><#default></#switch><#if editItem_has_next><#assign splitChar>, </#assign></#if></#list>
      </div>
                
      <div class="appraisa-choose">
        <p class="t_detail">
        <#assign dynDateVar>
             <#if dynDate=='dateTime'>${normalEnDynDate}<#else>${dynDate} ago</#if>
        </#assign>
        <span class="f888 date01"> ${dynDateVar}</span>
        </p>
      </div>
    </div>
</li>