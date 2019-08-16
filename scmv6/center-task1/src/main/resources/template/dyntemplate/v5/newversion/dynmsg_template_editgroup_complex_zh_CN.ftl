<li>
    <div class="ui_avatar">
      <a href="/scmwebsns/resume/psnView?des3PsnId=${des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank"><img src="${psnAvatar}" width="50" height="50" border="0" /></a>
    </div>
    <div class="publish-nr">
      <div class="moving-contat">
        <a href="/scmwebsns/resume/psnView?des3PsnId=${des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" class="Blue">${psnName}</a>修改了群组<#if queryType!=4>“<#if (groupPage!='')><a href="${groupPage}&menuId=1100" class="Blue">${groupName}</a><#else>${groupName}</#if>”</#if>的<#list editTypeDetails as editItem><#switch editItem.editType><#case 0>简介<#break><#case 1>基本设置<#break><#case 2>主页<#break><#case 3>公告<#break><#case 4>课程目标<#break><#default></#switch><#if editItem_has_next>、</#if></#list>
      </div>
                
      <div class="appraisa-choose">
        <p class="t_detail">
          <#assign dynDateVar>
          <#if dynDate=='dateTime'>${normalDynDate}<#else>${dynDate?replace("s","秒")?replace("m","分钟")?replace("h","小时")?replace("d","天")}以前</#if>
          </#assign>
        <span class="f888 date01"> ${dynDateVar}</span>
        </p>
      </div>
    </div>
</li>