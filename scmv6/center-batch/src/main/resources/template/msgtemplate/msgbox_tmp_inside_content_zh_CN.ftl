<div>${content}</div>
<#if msgType==1>
	<#if relation?exists>
		<br /><label class="b">关系：${relation}</label>
	</#if>
	<#if psnWork?exists>
		<br /><label class="b">工作经历：${psnWork}</label>
	</#if>
</#if>
<#if (commenderList?exists) && (commenderList?size>0)>
	<#list commenderList as commender>
		<div style="margin-top:10px; float:left;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
         	<tbody>
         		<tr>
            		<td width="30" height="30" rowspan="3" align="left">
            			<a href="${webDomain}/scmwebsns/resume/psnView?des3PsnId=${commender.commenderDes3Id}&menuId=1100" target="_blank">
            				<img src="${commender.commenderAvatars}" width="30" height="30" border="0">
            			</a>
            		</td>
            		<td height="10" align="left">
            			<p style="width:80px; text-overflow:ellipsis; white-space:nowrap; overflow:hidden;">
            				<a href="${webDomain}/scmwebsns/resume/psnView?des3PsnId=${commender.commenderDes3Id}&menuId=1100" target="_blank" style="font-size:12px; color:#005eac; text-decoration:none;" title="${commender.commenderName}">${commender.commenderName}</a>
            			</p>
            		</td>
          		</tr>
          		<tr>
            		<td height="2" align="left"></td>
          		</tr>
          		<tr>
                  <td height="10" align="left"></td>
               </tr>
        		</tbody>
        	</table>
  		</div>
  	</#list>
  	<div style="clear:both"></div>
</#if>


