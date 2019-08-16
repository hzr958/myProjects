<#if (msgType<=0) >    
	<#if (isSender=='yes')> 
		系统消息：您已接受（<#if (extOtherInfo.reciverZhNameLst?exists && extOtherInfo.reciverZhNameLst!="null")>${extOtherInfo.reciverZhNameLst}<#else>${extOtherInfo.reciverEnNameLst}</#if>）的好友邀请
	</#if>  
	<#if (isSender=='no')> 
		系统消息：邀请好友（<#if (extOtherInfo.psnChineseName?exists && extOtherInfo.psnChineseName!="null")>${extOtherInfo.psnChineseName}<#else>${extOtherInfo.psnFirstName} ${extOtherInfo.psnLastName}</#if>）成功 
	</#if>  
</#if> 
<#if (msgType==1)>  
	<#if (isSender=='yes')> 
		系统消息：您已拒绝（<#if (extOtherInfo.reciverZhNameLst?exists && extOtherInfo.reciverZhNameLst!="null")>${extOtherInfo.reciverZhNameLst}<#else>${extOtherInfo.reciverEnNameLst}</#if>）的好友邀请
	</#if>  
	<#if (isSender=='no')> 
		系统消息：（<#if (extOtherInfo.psnChineseName?exists && extOtherInfo.psnChineseName!="null")>${extOtherInfo.psnChineseName}<#else>${extOtherInfo.psnFirstName} ${extOtherInfo.psnLastName}</#if>）拒绝加您为好友 
	</#if>   
</#if> 
<#if (msgType==2)>    
	<#if (isSender=='yes')> 
		系统消息：您已接受加入群组（${extOtherInfo.groupName}）的邀请
	</#if>  
	<#if (isSender=='no')> 
		系统消息：邀请好友（<#if (extOtherInfo.psnChineseName?exists && extOtherInfo.psnChineseName!="null")>${extOtherInfo.psnChineseName}<#else>${extOtherInfo.psnFirstName} ${extOtherInfo.psnLastName}</#if>）加入群组（${extOtherInfo.groupName}）成功
	</#if>  
</#if> 
<#if (msgType==3)>  
	<#if (isSender=='yes')> 
		系统消息：您已拒绝加入群组（${extOtherInfo.groupName}）
	</#if>  
	<#if (isSender=='no')> 
		系统消息：（<#if (extOtherInfo.psnChineseName?exists && extOtherInfo.psnChineseName!="null")>${extOtherInfo.psnChineseName}<#else>${extOtherInfo.psnFirstName} ${extOtherInfo.psnLastName}</#if>）好友拒绝加入群组（${extOtherInfo.groupName}）
	</#if> 
</#if> 
<#if (msgType==4)>  
	<#if (isSender=='yes')> 
		系统消息：您已接受（<#if (extOtherInfo.reciverZhNameLst?exists && extOtherInfo.reciverZhNameLst!="null")>${extOtherInfo.reciverZhNameLst}<#else>${extOtherInfo.reciverEnNameLst}</#if>）加入群组（${extOtherInfo.groupName}）的请求
	</#if>  
	<#if (isSender=='no')> 
		系统消息：管理员（<#if (extOtherInfo.psnChineseName?exists && extOtherInfo.psnChineseName!="null")>${extOtherInfo.psnChineseName}<#else>${extOtherInfo.psnFirstName} ${extOtherInfo.psnLastName}</#if>）已接受您加入群组（${extOtherInfo.groupName}）的请求
	</#if> 
</#if> 
<#if (msgType==5)>  
	<#if (isSender=='yes')> 
		系统消息：您已拒绝（<#if (extOtherInfo.reciverZhNameLst?exists && extOtherInfo.reciverZhNameLst!="null")>${extOtherInfo.reciverZhNameLst}<#else>${extOtherInfo.reciverEnNameLst}</#if>）加入群组（${extOtherInfo.groupName}）的请求
	</#if>  
	<#if (isSender=='no')> 
		系统消息：管理员（<#if (extOtherInfo.psnChineseName?exists && extOtherInfo.psnChineseName!="null")>${extOtherInfo.psnChineseName}<#else>${extOtherInfo.psnFirstName} ${extOtherInfo.psnLastName}</#if>）已拒绝您加入群组（${extOtherInfo.groupName}）的请求
	</#if> 
</#if> 
<#if (msgType==6)>  
		系统消息：您已经被"<#if (psnName?exists && psnName!="")>${psnName}<#else>${firstName}${lastName}</#if>"从"${content.insZhName}科研在线"中移除
</#if> 
<#if (msgType==7)>  
	<#if (isSender=='yes')> 
		系统消息：您已删除群组 （${extOtherInfo.groupName}）
	</#if>  
	<#if (isSender=='no')> 
		系统消息：管理员（<#if (extOtherInfo.psnChineseName?exists && extOtherInfo.psnChineseName!="null")>${extOtherInfo.psnChineseName}<#else>${extOtherInfo.psnFirstName} ${extOtherInfo.psnLastName}</#if>）已删除群组 （${extOtherInfo.groupName}）
	</#if> 
</#if> 