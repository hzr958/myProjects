<#if pubMemberChicagoName?exists>${pubMemberChicagoName}.&nbsp;</#if><#rt>
<#if title?exists>"${title}."&nbsp;</#if><#rt>
<#if patentNo?exists> ${patentNo?string}</#if><#rt>
<#if publishYear?exists || publishMonth?exists || publishDay?exists>,filed&nbsp;</#if><#rt>
<#if publishMonth?exists>${publishMonth?c}<#if publishYear?exists || publishDay?exists>&nbsp;<#else>.</#if></#if><#rt>
<#if publishDay?exists>${publishDay?c}<#if publishYear?exists>,&nbsp;<#else>.</#if></#if><#rt>
<#if publishYear?exists>${publishYear?c}.</#if>