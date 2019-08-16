<#if pubMemberChicagoName?exists>${pubMemberChicagoName}.&nbsp;</#if><#rt>
<#if title?exists>"${title}."&nbsp;</#if><#rt>
<#if programeName?exists>${programeName}.&nbsp;</#if><#rt>
<#if department?exists>${department}.&nbsp;</#if><#rt>
<#if issue_org?exists>${issue_org}.&nbsp;</#if><#rt>
<#if countryName?exists>${countryName}<#if publishYear?exists>&nbsp;<#else>.</#if></#if><#rt>
<#if publishYear?exists>(${publishYear?c}).</#if>