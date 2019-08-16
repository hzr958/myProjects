<#assign total=total?number/> 
<#if (mailContext?exists)>
<#if (psnName?exists)>${psnName}</#if>分享了<#if total==1><#switch type><#case '2'>文献<#break><#case '3'>文件<#break><#case '4'>项目<#break><#case '1'>文献<#break><#default>资源</#switch>${zhShareTitle}<#else>${zhShareTitle}<#if (total>=2)>等</#if>${total}<#switch type><#case '2'>条文献<#break><#case '3'>个文件<#break><#case '4'>个项目<#break><#case '1'>条文献<#break><#default>个资源</#switch></#if>
<#else>
<#if (psnName?exists)>${psnName}</#if>分享了<#if total==1><#switch type><#case '2'>文献<#break><#case '3'>文件<#break><#case '4'>项目<#break><#case '1'>文献<#break><#default>资源</#switch><#if (minZhShareTitle?length gt 100) >
	${minZhShareTitle?substring(0,100)} ...
	<#else> ${minZhShareTitle}
	</#if><#else><#if (minZhShareTitle?length gt 100) >
	${minZhShareTitle?substring(0,100)} ...
	<#else> ${minZhShareTitle}
	</#if><#if (total>=2)>等</#if>${total}<#switch type><#case '2'>条文献<#break><#case '3'>个文件<#break><#case '4'>个项目<#break><#case '1'>条文献<#break><#default>个资源</#switch></#if>
</#if>