<td width="10%" align="center">
<a target="_blank" href="${psnUrl!'#'}">
    <img src="${avatars!'#'}" width="50" height="50" />
    </a>
</td>
<td align="left">
    <p class="f666">
        <a  target="_blank"  href="${psnUrl!'#'}" class="Blue mright10">${firstName!''}  ${lastName!''}</a>(H-index: ${hIndex!'0'})
        <span class="mleft10 Orange"><#if (isLeave?exists && status !=0)><#if isLeave=='0'><#else>Left</#if></#if></span>
    </p>
    <#if (positionEn?exists || affiliatedIns?exists)>
	    <p>
	        ${affiliatedIns!''}<#if (affiliatedIns?exists) && (positionEn?exists) && (positionEn!="")>; </#if>  ${positionEn!''}
	    </p>
    </#if>
    <#if (tel?exists || email?exists)>
    	<p>
        <#if (tel?exists && tel!='')>
        <i class="phone icon_infor"></i>${tel!''}&nbsp;&nbsp;&nbsp;&nbsp;
        </#if>
        <#if (email?exists)>
        <i class="icon_infor e-mail"></i>${email!''}
        </#if>
    	</p>
    </#if>
    <#if (associationRole?exists)>
    	<p class="associationRole_temp">
    	<#if (associationRole?exists && associationRole!='')>
    		${associationRole!''}
    	</#if>
    	</p>
    </#if>
    <#if (!associationRole?? || associationRole=='')>
    	<p class="associationRole_temp">
    		Researcher
    	</p>
    </#if>
</td>
