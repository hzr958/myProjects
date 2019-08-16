<#if (msgType<=0) >    
	<#if (isSender=='yes')> 
		System message: You have accepted the friend request of (<#if (extOtherInfo.reciverEnNameLst?exists && extOtherInfo.reciverEnNameLst!="null")>${extOtherInfo.reciverEnNameLst}<#else>${extOtherInfo.reciverZhNameLst}</#if>)
	</#if>  
	<#if (isSender=='no')> 
		System message: You invited (<#if (extOtherInfo.psnFirstName!="null" && extOtherInfo.psnLastName!="null")>${extOtherInfo.psnFirstName} ${extOtherInfo.psnLastName}<#else>${extOtherInfo.psnChineseName}</#if>) as a friend successfully
	</#if>  
</#if> 
<#if (msgType==1)>  
	<#if (isSender=='yes')> 
		System message: You have refused (<#if (extOtherInfo.reciverEnNameLst?exists && extOtherInfo.reciverEnNameLst!="null")>${extOtherInfo.reciverEnNameLst}<#else>${extOtherInfo.reciverZhNameLst}</#if>) friends invitation
	</#if>  
	<#if (isSender=='no')> 
		System message: (<#if (extOtherInfo.psnFirstName!="null" && extOtherInfo.psnLastName!="null")>${extOtherInfo.psnFirstName} ${extOtherInfo.psnLastName}<#else>${extOtherInfo.psnChineseName}</#if>) have refused to add you as a friend
	</#if>   
</#if> 
<#if (msgType==2)>    
	<#if (isSender=='yes')> 
		System message: You have accepted the request to join the ${extOtherInfo.groupName} group
	</#if>  
	<#if (isSender=='no')> 
		System message: You invited (<#if (extOtherInfo.psnFirstName!="null" && extOtherInfo.psnLastName!="null")>${extOtherInfo.psnFirstName} ${extOtherInfo.psnLastName}<#else>${extOtherInfo.psnChineseName}</#if>) to join the (${extOtherInfo.groupName}) group successfully
	</#if>  
</#if> 
<#if (msgType==3)>  
	<#if (isSender=='yes')> 
		System message: You have refused to join the (${extOtherInfo.groupName}) group
	</#if>  
	<#if (isSender=='no')> 
		System message: (<#if (extOtherInfo.psnFirstName!="null" && extOtherInfo.psnLastName!="null")>${extOtherInfo.psnFirstName} ${extOtherInfo.psnLastName}<#else>${extOtherInfo.psnChineseName}</#if>) refused to join the (${extOtherInfo.groupName}) group
	</#if> 
</#if> 
<#if (msgType==4)>  
	<#if (isSender=='yes')> 
		System message: You have accepted the request of (<#if (extOtherInfo.reciverEnNameLst?exists && extOtherInfo.reciverEnNameLst!="null")>${extOtherInfo.reciverEnNameLst}<#else>${extOtherInfo.reciverZhNameLst}</#if>) to join the (${extOtherInfo.groupName}) group
	</#if>  
	<#if (isSender=='no')> 
		System message: Group administrator (<#if (extOtherInfo.psnFirstName!="null" && extOtherInfo.psnLastName!="null")>${extOtherInfo.psnFirstName} ${extOtherInfo.psnLastName}<#else>${extOtherInfo.psnChineseName}</#if>) have accepted the request of you to join the (${extOtherInfo.groupName}) group
	</#if> 
</#if> 
<#if (msgType==5)>  
	<#if (isSender=='yes')> 
		System message: You have refused (<#if (extOtherInfo.reciverEnNameLst?exists && extOtherInfo.reciverEnNameLst!="null")>${extOtherInfo.reciverEnNameLst}<#else>${extOtherInfo.reciverZhNameLst}</#if>) to join the (${extOtherInfo.groupName}) group
	</#if>  
	<#if (isSender=='no')> 
		System message: Group administrator (<#if (extOtherInfo.psnFirstName?exists && extOtherInfo.psnFirstName!="null")>${extOtherInfo.psnFirstName} ${extOtherInfo.psnLastName}<#else>${extOtherInfo.psnChineseName}</#if>) have refused you to join the (${extOtherInfo.groupName}) group
	</#if> 
</#if> 
<#if (msgType==6)>  
		System message: You have been removed from "${content.insZhName}" on research online by "<#if (firstName?exists && firstName!="")>${firstName}${lastName}<#else>${psnName}</#if>"
</#if> 
<#if (msgType==7)>  
	<#if (isSender=='yes')> 
		System message: You have deleted the (${extOtherInfo.groupName}) group
	</#if>  
	<#if (isSender=='no')> 
		System message: Group administrator（<#if (extOtherInfo.psnFirstName?exists && extOtherInfo.psnFirstName!="null")>${extOtherInfo.psnFirstName} ${extOtherInfo.psnLastName}<#else>${extOtherInfo.psnChineseName}</#if>） have deleted the (${extOtherInfo.groupName}) group
	</#if> 
</#if> 