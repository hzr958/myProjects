<#if pubMemberMLAname?exists>${pubMemberMLAname}.&nbsp;</#if><#rt>
<#if title?exists>"${title}."&nbsp;</#if><#rt>
<#if publishYear?exists || programeName?exists || department?exists || issue_org?exists || countryName?exists>
<#if publishYear?exists>(${publishYear?c}).&nbsp;</#if><#rt>
<#if programeName?exists>${programeName}.&nbsp;</#if><#rt>
<#if department?exists>${department}.&nbsp;</#if><#rt>
<#if issue_org?exists>${issue_org}.&nbsp;</#if><#rt>
<#if countryName?exists>${countryName}.</#if><#rt>
<#else>.</#if>