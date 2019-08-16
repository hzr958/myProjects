<#if (msgTitle?exists)  >  
${psnName} Request for collect publication
</#if>
<#if (msgContent?exists)> 
${msgContent} 
<p>Click&nbsp;<a onClick=frmPubSubmit('${pubUrl}') href='javascript:void(0)' Class='Blue'>here</a>&nbsp;to enter the collect publication page.</p>
</#if>

  
  

  