<#if (mailContext?exists)>
	${psnName}分享了${total}
	<#switch type>
	<#case '2'>条您可能感兴趣的文献<#break>
	<#case '3'>个您可能感兴趣的文件<#break>
	<#case '4'>个您可能感兴趣的项目<#break>
	<#case '1'>条您可能感兴趣的文献<#break>
	<#default>个您可能感兴趣的资源
	</#switch><br /><br />
	${zhShareTitle}
<#else>
	${psnName}分享了${total}
	<#switch type>
	<#case '2'>条您可能感兴趣的文献<#break>
	<#case '3'>个您可能感兴趣的文件<#break>
	<#case '4'>个您可能感兴趣的项目<#break>
	<#case '1'>条您可能感兴趣的文献<#break>
	<#default>个您可能感兴趣的资源
	</#switch><br /><br />
	<#if (minZhShareTitle?length gt 100) >
	${minZhShareTitle?substring(0,100)} ...
	<#else> ${minZhShareTitle}
	</#if>
	
</#if>