<#switch resType>
	<#case 0>
	  ${sayContent} &nbsp;&nbsp;<#if (linkUrl?exists)><a href="${linkUrl}" class="Blue" target="_blank">${linkUrl}</a></#if>
	<#break>
	
	<#case 1> 
        <p><#if resEnauthor!=''>${resEnauthor}, </#if><a href="${resLink}<#if (groupId?exists)>&des3GroupId=${groupId}</#if>" id="shareTheme_${resType}_${resId?c}" class="Blue" target="_blank">${resEnTitle}</a><#if resEnother!=''>, ${resEnother}</#if></p>
	<#break>
	
	<#case 2> 
	    <p><#if resEnauthor!=''>${resEnauthor}, </#if><a href="${resLink}<#if (groupId?exists)>&des3GroupId=${groupId}</#if>" id="shareTheme_${resType}_${resId?c}" class="Blue" target="_blank">${resEnTitle}</a><#if resEnother!=''>, ${resEnother}</#if></p>
	<#break>
	
    <#case 3>
	    <#if (fileExt)!='imgIc'>
        <span class="icon_${fileExt} shared_pic" style="margin-left:-2px;">&nbsp;</span><a onclick="dynMsgUtil.ajaxDownloadRes(this, '${resType}','${resNode}','${resId?c}')" class="Blue">${resEnTitle}</a>
        <#else>
        <p><a href="${imgIcPath}" onclick="dynMsgUtil.ajaxIsShowPicInTB(this,'${resType}','${resNode}','${resId?c}')" class="gallery_link thickbox" rel="gallery-plants${randomTemp}" title="${resEnTitle}"><img src="${imgIc}"/></a></p>
        <p><a onclick="dynMsgUtil.ajaxDownloadRes(this, '${resType}','${resNode}','${resId?c}')" class="Blue">${resEnTitle}</a></p>
        </#if>
	<#break>
	
	<#case 4>
	    <p><#if resEnauthor!=''>${resEnauthor}, </#if><a href="${resLink}<#if (groupId?exists)>&des3GroupId=${groupId}</#if>" id="shareTheme_${resType}_${resId?c}" class="Blue" target="_blank">${resEnTitle}</a><#if resEnother!=''>, ${resEnother}</#if></p>
	<#break>
	
	<#case 5> 
	    <#if (fileExt)!='imgIc'>
        <span class="icon_${fileExt}">&nbsp;</span><a onclick="dynMsgUtil.ajaxDownloadRes(this, '${resType}','${resNode}','${resId?c}')" class="Blue">${resEnTitle}</a>
        <#else>
        <p><a href="${imgIcPath}" onclick="dynMsgUtil.ajaxIsShowPicInTB(this,'${resType}','${resNode}','${resId?c}')" class="gallery_link thickbox" rel="gallery-plants${randomTemp}" title="${resEnTitle}"><img src="${imgIc}"/></a></p>
        <p><a onclick="dynMsgUtil.ajaxDownloadRes(this, '${resType}','${resNode}','${resId?c}')" class="Blue">${resEnTitle}</a></p>
        </#if>
	<#break>
	
	<#case 6>
	<#break>
	
	<#case 7> 
	<a href="/scmwebsns/resume/psnView?des3PsnId=${des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" class="Blue">${psnEnName}</a> commented to <a href="/scmwebsns/resume/psnView?des3PsnId=${replyObjPsn_des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" class="Blue">${replyObjPsn_psnEnName}</a>: ${sayContent}
	<#break>
	
	<#case 8>
	    <div class="dyn_img"><a href="${groupPage}&menuId=1100" target="_blank"><img src="${groupAvatar}" width="32" height="32" title="${groupName}"></a></div>
        <div class="dyn_introduction">
         <h3><a href="${groupPage}&menuId=1100" target="_blank" class="b">${groupName}</a></h3>
         <p>${groupDesc}</p>
        </div>
		<div style="clear:both"></div>
	<#break>
	
	<#case 9> 
	    <div class="dyn_img"><a href="${resumeLink}" target="_blank"><img src="/resscmwebsns/images_v5/icon2/icon05.gif" width="32" height="32" title="${resumeName}"></a></div>
        <div class="dyn_introduction">
         <a href="${resumeLink}" class="Blue" target="_blank">${resumeName}</a><br/>
         <#if (resumePdf?exists)>
		 <a href="${resumePdf}" class="Blue" target="_blank">${resumeName}.pdf</a><br/>
		 </#if>
		 <#if (resumeDoc?exists)>
		 <a href="${resumeDoc}" class="Blue" target="_blank">${resumeName}.doc</a>
		 </#if>
        </div>
		<div style="clear:both"></div>
	<#break>
	
	<#case 10>
	    <div class="dyn_img"><a href="${linkUrl}" target="_blank"><img src="${icon}" width="32" height="32" title="${enTitle}"></a></div>
        <div class="dyn_introduction">
         <h3><a href="${linkUrl}" class="b" target="_blank">${Title}</a></h3>
         <p>${enDesc}</p>
        </div>
		<div style="clear:both"></div>
	<#break>
	
	<#case 11> 
		<div class="dyn_img"><a href="${linkUrl}" target="_blank"><img src="${icon}" width="32" height="32" title="${enTitle}"></a></div>
        <div class="dyn_introduction">
         <h3><a href="${linkUrl}" class="b" target="_blank">${enTitle}</a></h3>
         <p>${enDesc}</p>
        </div>
		<div style="clear:both"></div>
	<#break>
	
	<#case 12>
		<div class="dyn_img"><a href="${linkUrl}" target="_blank"><img src="${icon}" width="32" height="32" title="${enTitle}"></a></div>
        <div class="dyn_introduction">
         <h3><a href="${linkUrl}" class="b" target="_blank">${enTitle}</a></h3>
         <p>${enDesc}</p>
        </div>
		<div style="clear:both"></div> 
	<#break>
	
	<#case 13> 
		<div class="dyn_img"><a href="${linkUrl}" target="_blank"><img src="${icon}" width="32" height="32" title="${enTitle}"></a></div>
        <div class="dyn_introduction">
         <h3><a href="${linkUrl}" class="b" target="_blank">${enTitle}</a></h3>
         <p>${enDesc}</p>
        </div>
		<div style="clear:both"></div>
	<#break>
	
	<#case 14> 
		<div class="dyn_img"><a href="${linkUrl}" target="_blank"><img src="${icon}" width="32" height="32" title="${enTitle}"></a></div>
        <div class="dyn_introduction">
         <h3><a href="${linkUrl}" class="b" target="_blank">${enTitle}</a></h3>
         <p>${enDesc}</p>
        </div>
		<div style="clear:both"></div>
	<#break>
	
	<#case 15> 
		<div class="dyn_img"><a href="${linkUrl}" target="_blank"><img src="${icon}" width="32" height="32" title="${enTitle}"></a></div>
        <div class="dyn_introduction">
         <h3><a href="${linkUrl}" class="b" target="_blank">${enTitle}</a></h3>
         <p>${enDesc}</p>
        </div>
		<div style="clear:both"></div>
	<#break>
	
	<#case 16> 
		<div class="dyn_img"><a href="${linkUrl}" target="_blank"><img src="${icon}" width="32" height="32" title="${enTitle}"></a></div>
        <div class="dyn_introduction">
         <h3><a href="${linkUrl}" class="b" target="_blank">${enTitle}</a></h3>
         <p>${enDesc}</p>
        </div>
		<div style="clear:both"></div>
	<#break>
	
	<#case 17>
		<div class="dyn_img"><a href="${linkUrl}" target="_blank"><img src="${icon}" width="32" height="32" title="${enTitle}"></a></div>
        <div class="dyn_introduction">
         <h3><a href="${linkUrl}" class="b" target="_blank">${enTitle}</a></h3>
         <p>${enDesc}</p>
        </div>
		<div style="clear:both"></div> 
	<#break>
	<#case 18>
		<div class="dyn_img"><a href="${linkUrl}" target="_blank"><img src="${icon}" width="32" height="32" title="${enTitle}"></a></div>
        <div class="dyn_introduction">
         <h3><a href="${linkUrl}" class="b" target="_blank">${enTitle}</a></h3>
         <p>${enDesc}</p>
        </div>
		<div style="clear:both"></div> 
	<#break>
	
	<#case 19>
	   <a href="/scmwebsns/resume/psnView?des3PsnId=${des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" class="Blue">${psnEnName}</a><label style="color:#999"> updated Work Experience</label>
      <#if insName!=''||unitName!=''||position!=''>
      <p style="margin-top:10px;">${insName}<#if insName!='' && unitName!=''>，${unitName}<#else>${unitName}</#if><#if (insName!='' || unitName!='') && position!=''>，${position}<#else>${position}</#if></p>
      </#if>
	<#break>
	
	<#case 20>
	   <a href="/scmwebsns/resume/psnView?des3PsnId=${des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" class="Blue">${psnEnName}</a><label style="color:#999"> updated Education Experience</label>
      <#if eduName!=''||eduSpecialty!=''||eduDegree!=''>
      <p style="margin-top:10px;">${eduName}<#if eduName!='' && eduSpecialty!=''>，${eduSpecialty}<#else>${eduSpecialty}</#if><#if (eduSpecialty!='' || eduName!='') && eduDegree!=''>，${eduDegree}<#else>${eduDegree}</#if></p>
      </#if>
	<#break>
	
	<#case 21>
	  <a href="/scmwebsns/resume/psnView?des3PsnId=${des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" class="Blue">${psnEnName}</a><label style="color:#999"> updated Research Area</label>
     <#if (discDetails?exists) && (discDetails?size>0)> 
     <p style="margin-top:10px;"> 
     <#list discDetails as disc>
     ${disc.discEnName}<#if disc_has_next>；</#if>
     </#list>
     </p>
     </#if>
	<#break>
	
	<#case 24> 
	    <p style="color:#666666">
	    Journal:<a href="${resLink}" id="shareTheme_${resType}_${resId?c}" class="Blue" target="_blank">${resEnTitle}</a>
        <#if pissn!=''>
        (${pissn})
        </#if>
        </p>
	<#break>

</#switch>
