 <#if (msgTitle?exists)>  
   	<div id="share_Title_CN"> 
		<a href="javascript:void(0);" onclick="showShareList('${sid}','${rid}',this)">${zhPsnName}分享了
		<#if total=="1"><#switch type><#case '2'>文献<#break><#case '3'>文件<#break><#case '4'>项目<#break><#case '1'>文献<#break><#default>资源</#switch>${zhShareTitle}
		<#else>${zhShareTitle}${total}<#switch type><#case '2'>条文献<#break><#case '3'>个文件<#break><#case '4'>个项目<#break><#case '1'>条文献<#break><#default>个资源</#switch>
		</#if>
		<input type="hidden" id="shareType" name="shareType" value="${type}"/>
		<input type="hidden" id="receiveId" value="${rid}"/> 
		</a>
	</div>	
  	<div id="share_Title_EN">
	  	<a href="#" onclick="showShareList('${sid}','${rid}',this)">${enPsnName} has shared&nbsp;
	  	<#if total == "1">${enShareTitle} to you.
	  	<#else>${enShareTitle} and ${(total?number)-1} more&nbsp;<#switch type><#case '2'>references<#break><#case '3'>files<#break><#case '4'>projects<#break><#case '1'>references<#break><#default>resources</#switch> to you.
		</#if>
		<input type="hidden" id="shareType" name="shareType" value="${type}"/>
		<input type="hidden" id="receiveId" value="${rid}"/>
		</a>
	</div>
	</#if>