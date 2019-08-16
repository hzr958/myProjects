<#if (msgTitle?exists)  >  
${psnName} request to join your group:"${groupName}"
</#if>

<#if (msgContent?exists) > 
<a href="${psnUrl}"  >${psnName}</a> request to join your group:"${groupName}",click <a href="${approveUrl}" target="_self">here</a> to proceed.
</#if>
