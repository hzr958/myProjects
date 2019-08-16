<#if (msgTitle?exists)  >  
${psnName}请求加入您的群组：“${groupName}”
</#if>

<#if (msgContent?exists) > 
<a href="${psnUrl}"  >${psnName}</a>请求加入群组：“${groupName}”<span id="spnClick">,点击<a href="${approveUrl}" target="_self">此处</a>进行处理</span>。
</#if>
