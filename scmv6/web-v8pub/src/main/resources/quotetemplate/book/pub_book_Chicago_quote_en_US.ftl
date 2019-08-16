<#if pubMemberChicagoName?exists>${pubMemberChicagoName}.&nbsp;</#if><#rt>
<#if title?exists>"${title}."&nbsp;</#if><#rt>
<#if publisher?exists><i>${publisher}</i><#if publishYear?exists>,&nbsp;(${publishYear?c})</#if>.<#else><#if publishYear?exists>(${publishYear?c}).</#if></#if>