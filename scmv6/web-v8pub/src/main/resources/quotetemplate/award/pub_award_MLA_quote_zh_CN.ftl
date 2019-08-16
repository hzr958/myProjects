<#if pubMemberMLAname?exists>${pubMemberMLAname}.&nbsp;</#if><#rt>
<#if title?exists>"${title}."&nbsp;</#if><#rt>
<#if awardCategory?exists><i>${awardCategory}</i>.&nbsp;</#if><#rt>
<#if awardGrade?exists>${awardGrade}.&nbsp;</#if><#rt>
<#if issueInsName?exists>${issueInsName}<#if !publishYear?exists>.</#if></#if><#rt>
<#if publishYear?exists>(${publishYear?c}).</#if>