<td width="10%" align="center">
<a target="_blank" href="${psnUrl!'#'}">
    <img src="${avatars!'#'}" width="50" height="50" />
    </a>
</td>
<td align="left">
    <p class="f666">
        <a  target="_blank"  href="${psnUrl!'#'}" class="Blue mright10">${zhName!''}</a>(SID: ${sid!''}&nbsp;&nbsp; H-index: ${hIndex!'0'})
        <span class="mleft10 Orange"><#if (isLeave?exists && status !=0)><#if isLeave=='0'><#else>离职</#if></#if></span>
    </p>
    <#if (positionZh?exists || unitZhame?exists)>
	    <p>
	        ${positionZh!''}<#if (positionZh?exists) && (unitZhame?exists) && (unitZhame!="")>; </#if>${unitZhame!''}
	    </p>
    </#if>
    
    	<p>
	    <#if (tel?exists && tel!='')>
	    <i class="phone icon_infor"></i>${tel!''}&nbsp;&nbsp;&nbsp;&nbsp;	    
	    </#if>
	    <#if (email?exists)>
	    <i class="icon_infor e-mail"></i>${email!''}
	    </#if>
	    </p>
    
</td>
