<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
</head>

<body>
<table width="700" border="0" align="center" cellpadding="0" cellspacing="0" id="mailId">
  <tr>
    <td height="50" align="center" style="color:#666; font-size:11px;">如邮件不能正常显示，请打开下面链接查看<br />
      <#if (viewMailUrl?exists)> <a href="${viewMailUrl}" target="_blank">${viewMailUrl}</a><#else><a href="#">http://www.xxxxxx.com</a></#if></td>
  </tr>
</table>
<table width="700" border="0" align="center" cellpadding="0" cellspacing="0" style="border:#4F81BD 1px solid;font-family: Arial, Helvetica, sans-serif;background:#fff;">
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="30" bgcolor="#4F81BD" style=" font-size:16px;font-weight:bold;text-indent:5px;color:#fff;">科研之友：科研社区网络，成就创新梦想</td>
      </tr>
    </table>
      <table width="670" border="0" align="center" cellpadding="0" cellspacing="0" style="font-size:12px;">
        <tr>
          <td colspan="3">&nbsp;</td>
        </tr>
        <tr>
          <td height="25" colspan="3">尊敬的<#if (userName?exists)> ${userName}<#else> AA</#if>：</td>
        </tr>
        <tr>
          <td height="25" colspan="3">您好！</td>
        </tr>
        <tr>
          <td height="25" colspan="3" style="line-height:20px;"><strong><#if (sourceUser?exists)><#list sourceUser as user>${user.sourceUserName}</#list><#else>XX</#if></strong>近期更新了<#if (sourceUser?exists)><#list sourceUser as user>【${user.sourceOutput}】</#list><#else>【n】</#if>篇论文成果，
          点击按钮加他为好友，查看并共享成果信息。</td>
        </tr>
        <tr>
          <td colspan="3">&nbsp;</td>
        </tr>
        <tr>
        	<#if (sourceUser?exists)><#list sourceUser as user>
          <td height="25" colspan="3">
          <a href="${mailBaseUrl?replace("target1",'yes')?replace("target2",user.sourceUserUrl)?replace("target3",user.souUserParms) }" target="_blank" 
          style="text-decoration:none;font-weight:bold;">${user.sourceUserName}</a>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${user.util}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <table width="80" border="0" cellpadding="0" cellspacing="1" bgcolor="#285510">
        		<tr>
            	<td height="22" align="center" bgcolor="#69a551"  
            	style=" border-top:1px solid #8fbb7b;">
            	<a href="${mailBaseUrl?replace("target1",'yes')?replace("target2",user.userWebUrl)?replace("target3",user.userWebParms) }"
            	style="font-size:12px; color:#FFF; font-family:Arial, Helvetica, sans-serif; text-align:center; font-weight:bold; text-decoration:none;">
            	加为好友</a></td>
        		</tr>
    		</table>
          </td>
       		</#list>
       		<#else>
        	 <td height="25" colspan="3">
        	 <a href="#">XX</a>
        	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;中山大学&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        	 <table width="80" border="0" cellpadding="0" cellspacing="1" bgcolor="#285510">
        		<tr>
            	<td height="22" align="center" bgcolor="#69a551"  
            	style=" border-top:1px solid #8fbb7b;">
            	<a href="#"
            	style="font-size:12px; color:#FFF; font-family:Arial, Helvetica, sans-serif; text-align:center; font-weight:bold; text-decoration:none;">
            	加为好友</a></td>
        		</tr>
    		</table>
        	 </td>
        	</#if>
        </tr>
        <tr>
          <td colspan="3" style="border-bottom:#ccc 1px dashed;">&nbsp;</td>
        </tr>
        <tr>
          <td colspan="3">&nbsp;</td>
        </tr>
        <tr>
          <td height="25">您可能认识的人：</td>
          <td height="25">&nbsp;</td>
          <td width="256" style="text-align:right;"><#if (moreUserUrl?exists)><a target="_blank" style="text-decoration:none;" href="${mailBaseUrl?replace("target2",moreUserUrl)?replace("target3",moreUserParms) }"><#else><a href="#"></#if>>> 查看全部</a></td>
        </tr>
        <tr>
        	<#if (sourceUserList?exists)>
          	<#if (sourceUserList?size > 0) >
          		<#list sourceUserList as userList>
		 						 	<td width="224" height="70" style="text-align:center;line-height:20px;">
		 						 	<a href="${mailBaseUrl?replace("target1",'yes')?replace("target2",userList.sourceUserUrl)?replace("target3",userList.souUserParms) }" 
		 						 	target="_blank" style="text-decoration:none;font-weight:bold;">${userList.sourceUserName}</a> <br />
		 						 	华南理工大学<br />
		 						 	<table width="80" border="0" cellpadding="0" cellspacing="1" bgcolor="#285510">
        								<tr>
            							<td height="22" align="center" bgcolor="#69a551"  
            							style=" border-top:1px solid #8fbb7b;">
            							<a href="${mailBaseUrl?replace("target1",'yes')?replace("target2",userList.userWebUrl)?replace("target3",userList.userWebParms) }"
            							style="font-size:12px; color:#FFF; font-family:Arial, Helvetica, sans-serif; text-align:center; font-weight:bold; text-decoration:none;">
            							加为好友</a></td>
        								</tr>
    								</table>
		 						 	</td>
		 							<#if (userList_index+1 >= 3)>
		 	 						<#break>
		 						</#if>
		 					 </#list>
		 				</#if>
				 <#else>
          <td width="224" height="70" style="text-align:center;line-height:20px;">
          	<a href="#" style="text-decoration:none;font-weight:bold;">
          	DDD</a> 
          	<br />
          	华南理工大学
          	<br /> 
          	<table width="80" border="0" cellpadding="0" cellspacing="1" bgcolor="#285510">
        		<tr>
            	<td height="22" align="center" bgcolor="#69a551"  
            	style=" border-top:1px solid #8fbb7b;">
            	<a href="#"
            	style="font-size:12px; color:#FFF; font-family:Arial, Helvetica, sans-serif; text-align:center; font-weight:bold; text-decoration:none;">
            	加为好友</a></td>
        		</tr>
    		</table>
          	</td>
          <td width="190" style="text-align:center;line-height:20px;">
               <a href="#" style="text-decoration:none;font-weight:bold;">
         		DEE</a> <br />
                                深圳大学 <br /> 
          <table width="80" border="0" cellpadding="0" cellspacing="1" bgcolor="#285510">
        		<tr>
            	<td height="22" align="center" bgcolor="#69a551"  
            	style=" border-top:1px solid #8fbb7b;">
            	<a href="#"
            	style="font-size:12px; color:#FFF; font-family:Arial, Helvetica, sans-serif; text-align:center; font-weight:bold; text-decoration:none;">
            	加为好友</a></td>
        		</tr>
    		</table>
          </td>
          
          <td height="70" style="text-align:center;line-height:20px;">
          <a href="#" style="text-decoration:none;font-weight:bold;">
          DBB</a> <br />
                   北京大学 <br /> 
          <table width="80" border="0" cellpadding="0" cellspacing="1" bgcolor="#285510">
        		<tr>
            	<td height="22" align="center" bgcolor="#69a551"  
            	style=" border-top:1px solid #8fbb7b;">
            	<a href="#"
            	style="font-size:12px; color:#FFF; font-family:Arial, Helvetica, sans-serif; text-align:center; font-weight:bold; text-decoration:none;">
            	加为好友</a></td>
        		</tr>
    		</table>
          </td>
        </#if>
        </tr>
        <tr>
          <td height="25" colspan="3">&nbsp;</td>
        </tr>
        <tr>
          <td height="25" colspan="3">添加更多的好友，可以扩展您的科研人脉，获取更多信息和机会。</td>
        </tr>
        <tr>
          <td height="80" colspan="3">&nbsp;</td>
        </tr>
        <tr>
          <td height="25" colspan="3">
		  科研之友客户服务小组<br />
		  Email: support@scholarmate.com <br />
		 		</td>
        </tr>
        <tr>
          <td colspan="3">&nbsp;</td>
        </tr>
      </table></td>
  </tr>
</table>
<#include "/base_foot_zh_CN.ftl" encoding= "UTF-8">
</body>
</html>
