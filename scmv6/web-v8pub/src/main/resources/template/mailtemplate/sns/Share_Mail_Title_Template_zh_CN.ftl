<#if (mailContext?exists)>
	${psnName}分享了${total}<#rt/>
	<#switch type>
	<#case '2'>条你可能感兴趣的文献<#break>
	<#case '3'>个你可能感兴趣的文件<#break>
	<#case '4'>个你可能感兴趣的项目<#break>
	<#case '1'>条你可能感兴趣的成果<#break>
	<#default>个你可能感兴趣的资源
	</#switch>
	${zhShareTitle}
<#else>
	${psnName}分享了${total}<#rt/>
	<#switch type>
	<#case '2'>条你可能感兴趣的文献<#break>
	<#case '3'>个你可能感兴趣的文件<#break>
	<#case '4'>个你可能感兴趣的项目<#break>
	<#case '1'>条你可能感兴趣的成果<#break>
	<#default>个你可能感兴趣的资源
	</#switch>
	<#if (minZhShareTitle?length gt 100) >
	${minZhShareTitle?substring(0,100)} ...
	<#else> ${minZhShareTitle}
	</#if>
	
</#if>