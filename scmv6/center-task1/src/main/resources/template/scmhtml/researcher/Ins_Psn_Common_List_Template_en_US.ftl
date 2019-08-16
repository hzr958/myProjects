<td width="10%" align="center"><a target="_blank" href="${psnUrl!'#'}"><img  src="${avatars}" width="50" height="50" /></a></td>
<td align="left">
<p class="f666">
    <a target="_blank" href="${psnUrl!'#'}" class="Blue mright10">${firstName!''}  ${lastName!''}</a>(SID:${sid!''})

</p>
<p class="f666">${positionEn!''}<#if (positionEn?exists) && (unitEname?exists) && (unitEname!="")>; </#if>${unitEname!''}</p>
<p class="f888">
<#if (email?exists)><i class="icon_infor e-mail"></i>${email!''}&nbsp;&nbsp;&nbsp;&nbsp;</#if><#if (tel?exists && tel!='')><i class="phone icon_infor"></i>${tel!''}</#if></p>
</td>