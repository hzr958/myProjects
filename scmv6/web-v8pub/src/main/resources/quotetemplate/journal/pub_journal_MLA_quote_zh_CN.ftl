<#if pubMemberMLAname?exists>${pubMemberMLAname}.&nbsp;</#if><#rt>
<#if title?exists>"${title}."&nbsp;</#if><#rt>
<#if journalName?exists || volume?exists || issue?exists || publishYear?exists || startPage?exists || endPage?exists>
<#if journalName?exists><i>${journalName}</i>&nbsp;</#if><#rt>
<#if volume?exists>${volume?string}<#if issue?exists>.</#if></#if><#if issue?exists>&nbsp;${issue?string}&nbsp;</#if><#rt>
<#if publishYear?exists>(${publishYear?c})</#if><#if (journalName?exists || volume?exists ||  issue?exists || publishYear?exists) && pageNumber?exists>:&nbsp;<#else>.</#if><#rt>
<#if pageNumber?exists>${pageNumber}.</#if><#rt></#if>