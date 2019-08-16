<#if (resDetails?exists)>
<p style="font-size:16px; font-weight:bold; margin-bottom:10px; color:#888888;">
<#if (resType=4)>
已选择${resCount}个项目分享
<#elseif (resType==11)>
已选择${resCount}个基金分享
<#elseif (resType==26)>
  已选择${resCount}个新闻分享
<#else>
已选择${resCount}篇论文分享
</#if>
</p>
<div style="max-height:55px; overflow-y:hidden;">
<#list resDetails as resItem>
<#if (resType!=11)>
<p><#if resItem.authorNames!=''>${resItem.authorNames},&nbsp;</#if><a href="${resItem.resLink}" class="Blue" target="_blank">${resItem.title}</a><#if resItem.resOther!=''>,&nbsp;${resItem.resOther}</#if></p>
</#if>
</#list>
</div>
<#if (resCount > 1)>
<p>......</p>
</#if>
</#if>
