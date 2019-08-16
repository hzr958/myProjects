<#if pubMemberAPAname?exists>${pubMemberAPAname}.&nbsp;</#if><#rt>
<#if publishYear?exists>(${publishYear?c}).&nbsp;</#if><#rt>
<#if title?exists>${title}.&nbsp;</#if><#rt>
<#if journalName?exists><i>${journalName}</i><#if issue?exists || volume?exists || startPage?exists || endPage?exists>,&nbsp;<#else>.</#if></#if><#rt>
<#if volume?exists>${volume?string}</#if><#if issue?exists>(${issue?string})</#if><#if (issue?exists || volume?exists) && pageNumber?exists>,&nbsp;<#else>.</#if><#rt>
<#if pageNumber?exists>${pageNumber}.</#if><#rt>