<#if (msgTitle?exists)  >  
 From ${psnName}'s Friend Recommendation

<#else>
  
  
   ${psnName} recommend you and <#list psnFrds as frd>
	<span id='spn${frd.psnId}'>
		<a href="${frd.psnUrl}" class="Blue" target="_blank">${frd.frdName}</a>
	     <#if frd_has_next>
	       、
	     </#if>
    </span>
 </#list> to be friends<br/>
  
 
 </#if>

  <#if (msgContent?exists)  >  
 Validation message:${msgContent}
  
  </#if>