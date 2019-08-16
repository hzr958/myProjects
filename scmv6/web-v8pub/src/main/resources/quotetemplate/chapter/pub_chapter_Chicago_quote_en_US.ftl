<#if pubMemberChicagoName?exists>${pubMemberChicagoName}.&nbsp;</#if><#rt>
<#if title?exists>"${title}."&nbsp;</#if><#rt>
<#if bookTitle?exists><i>${bookTitle}</i>.&nbsp;</#if><#rt>
<#if editors?exists>${editors}.&nbsp;</#if><#rt>
<#if publisher?exists>${publisher}</#if><#rt>
<#if (bookTitle?exists || publishYear?exists) && (pageNumber?exists)>(${publishYear?c})</#if><#rt>
<#if (bookTitle?exists || publishYear?exists) && (!pageNumber?exists)>.&nbsp;</#if><#rt>
<#if pageNumber?exists>:${pageNumber}.</#if><#rt>