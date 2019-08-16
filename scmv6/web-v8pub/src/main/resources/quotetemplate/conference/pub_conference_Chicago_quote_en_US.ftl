<#if pubMemberChicagoName?exists>${pubMemberChicagoName}.&nbsp;</#if><#rt>
<#if title?exists>"${title}."&nbsp;</#if><#rt>
<#if confName?exists>In <i>${confName}</i><#if startPage?exists || endPage?exists ||publishYear?exists>,&nbsp;<#else>.</#if></#if><#rt>
<#if pageNumber?exists>pp.&nbsp;${pageNumber}.&nbsp;</#if><#rt>
<#if publishYear?exists>${publishYear?c}.</#if>