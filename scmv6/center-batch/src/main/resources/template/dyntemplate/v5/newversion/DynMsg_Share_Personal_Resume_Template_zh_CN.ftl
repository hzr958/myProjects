<#if (msgTitle?exists && msgTitle=='title') >
	分享了个人简历
</#if>
<#if (msgContent?exists && msgContent=='content') >分享了简历“${resume.resumeName}”，请点击链接&nbsp;
<#if (resume.resumeUrl?exists && resume.resumeUrl!='null' && resume.resumeUrl!='')>
	<#assign urlHasValue="true">
	<a href='${resume.resumeUrl}' target='_blank'>URL</a>
</#if>
<#if (resume.resumePdf?exists && resume.resumePdf!='null' && resume.resumePdf!='')>
	<#assign pdfHasValue="true">
	<#if urlHasValue?exists>、</#if>
	<a href='${resume.resumePdf}' target='_blank'>PDF</a>
</#if>
<#if (resume.resumeWord?exists && resume.resumeWord!='null' && resume.resumeWord!='')>
	<#if pdfHasValue?exists || urlHasValue?exists>、</#if>
	<a href='${resume.resumeWord}' target='_blank'>WORD</a>
</#if>&nbsp;查看。
</#if>