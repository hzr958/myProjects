<#if (mailContext?exists)>
	${psnName} shared 
	${total}
	<#switch type>
		<#case '2'> references that you may like <#break>
		<#case '3'> files that you may like <#break>
		<#case '4'> projects that you may like <#break>
		<#case '1'> references that you may like <#break>
		<#default> resources that you may like
	</#switch>.<br /><br />
	${enShareTitle}
<#else>
	${psnName} shared 
	${total}
	<#switch type>
		<#case '2'> references that you may like <#break>
		<#case '3'> files that you may like <#break>
		<#case '4'> projects that you may like <#break>
		<#case '1'> references that you may like <#break>
		<#default> resources that you may like
	</#switch>.<br /><br />
	<#if (minEnShareTitle?length gt 100) >
	${minEnShareTitle?substring(0,100)} ...
	<#else> ${minEnShareTitle}
	</#if>
</#if>