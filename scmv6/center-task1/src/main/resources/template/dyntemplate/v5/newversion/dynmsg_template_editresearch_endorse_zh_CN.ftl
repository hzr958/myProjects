<#if (endorseList?exists)>
     <div class="total-praise identityLeft" >
     	 <#list endorseList as infoList>
     		 <a href="/scmwebsns/resume/psnView?des3PsnId=${infoList.endorsePsnId!''}" target="_blank" target="_blank" title="${infoList.psn_name!''}" style="text-decoration: none;  margin-right:5px; vertical-align:middle;" >
     	 		<img endorsePsnId="${infoList.endorsePsnId!''}" src="${infoList.head_url!''}" width="19" height="19" />
     	 	</a>
     	 </#list>
		<#if (endorseCount?exists)>
			<span style="color:#000">
			<#if (isMine==0)>
			<#if (endorseCount?number lt 6)>
			 <a class="Blue b" title="认同您的好友" href="/scmwebsns/resume/psnView?des3PsnId=${des3PsnId!''}&src=discipline_box" target="_blank" target="_blank" style="text-decoration: none;">${endorseCount}</a>人已认同
			 <#else>等<a class="Blue b" title="认同您的好友" href="/scmwebsns/resume/psnView?des3PsnId=${des3PsnId!''}&src=discipline_box" target="_blank" target="_blank" style="text-decoration: none;">${endorseCount}</a>人已认同
			</#if>
			<#else>
			<#if (endorseCount?number lt 6)>
			 ${endorseCount}人已认同
			 <#else>等${endorseCount}人已认同
			</#if>
			</#if>
			</span>
		</#if>
    </div>	
</#if>
   