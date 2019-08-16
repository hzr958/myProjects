<#if (endorseCount?exists)>
     <div class="identity identityLeft">
     	<#if (endorseList?exists)>
     	 <#list endorseList as infoList>
     		 <a href="/scmwebsns/resume/psnView?des3PsnId=${infoList.endorsePsnId!''}" target="_blank" target="_blank" title="${infoList.psn_name!''}" style="text-decoration: none;" >
     	 	<img endorsePsnId="${infoList.endorsePsnId!''}" src="${infoList.head_url!'' }" width="19" height="19" />
     	 	</a>
     	 </#list>
     	 </#if>
			<span style="color:#000"> 
			<#if (isMine==0)>
				<#if (endorseCount?number lt 6)>
				 <a class="Blue b" title="Endorse your friend" href="/scmwebsns/resume/psnView?des3PsnId=${des3PsnId}&src=discipline_box" target="_blank" target="_blank" style="text-decoration: none;">${endorseCount}</a> people have endorsed
				 <#else> And <a class="Blue b" title="Endorse your friend" href="/scmwebsns/resume/psnView?des3PsnId=${des3PsnId}&src=discipline_box" target="_blank" target="_blank" style="text-decoration: none;">${endorseCount - 5 }</a> others have endorsed
				</#if>
				<#else>
				 <#if (endorseCount?number lt 6)>
				 ${endorseCount} people have endorsed
				 <#else> And ${endorseCount - 5 } others have endorsed
				</#if>
			</#if>
			</span>
     	</div>	
</#if>
   