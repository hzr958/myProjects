<#if (resDetails?exists)>
<p style="font-size:16px; font-weight:bold; margin-bottom:10px; color:#888888;">
<#if (resType=4)>
You have selected ${resCount} projects
<#else>
You have selected ${resCount} publications
</#if>
</p>
<div style="max-height:50px; overflow-y:hidden;">
<#list resDetails as resItem>
<p><#if resItem.authorNames!=''>${resItem.authorNames},&nbsp;</#if><a href="${resItem.resLink}" class="Blue" target="_blank">${resItem.title}</a><#if resItem.resOther!=''>,&nbsp;${resItem.resOther}</#if></p>
</#list>
</div>
<#if (resCount > 1)>
<p>......</p>
</#if>
</#if>