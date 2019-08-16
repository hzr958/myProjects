<#if pubMemberMLAname?exists>${pubMemberMLAname}.&nbsp;</#if><#rt>
<#if title?exists>"${title}."&nbsp;</#if><#rt>
<#if bookTitle?exists><i>${bookTitle}</i></#if><#rt>
<#if publishYear?exists>(${publishYear?c})</#if><#rt>
<#if (bookTitle?exists || publishYear?exists) && pageNumber?exists></#if><#rt>
<#if (bookTitle?exists || publishYear?exists) && !pageNumber?exists>.&nbsp;</#if><#rt>
<#if pageNumber?exists>:${pageNumber}.</#if><#rt>
<#if editors?exists>${editors}<#if publisher?exists>,&nbsp;<#else>.</#if></#if><#rt>
<#if publisher?exists>${publisher}.</#if>