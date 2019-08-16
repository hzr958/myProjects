<#if pubMemberChicagoName?exists>${pubMemberChicagoName}.&nbsp;</#if><#rt>
<#if title?exists>"${title}."&nbsp;</#if><#rt>
<#if journalName?exists || volume?exists || issue?exists || publishYear?exists || pageNumber?exists>
<#if journalName?exists><i>${journalName}</i></#if><#if volume?exists> ${volume?string}</#if><#if (journalName?exists || volume?exists) && (issue?exists || publishYear?exists)>,&nbsp;</#if><#rt>
<#if issue?exists>no. ${issue?string}</#if><#rt>
<#if publishYear?exists>(${publishYear?c})</#if><#if (journalName?exists || volume?exists ||  issue?exists || publishYear?exists) && pageNumber?exists>:&nbsp;<#else>.</#if><#rt>
<#if pageNumber?exists>${pageNumber}.</#if><#rt></#if>