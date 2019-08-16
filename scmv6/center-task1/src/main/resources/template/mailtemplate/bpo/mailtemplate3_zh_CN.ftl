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
      <#if (viewMailUrl?exists)><a href="${viewMailUrl}" target="_blank">${viewMailUrl}</a><#else><a href="#">http://www.xxxxxx.com</a></#if></td>
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
          <td height="25" colspan="2">尊敬的<#if (userName?exists)> ${userName}<#else> AA</#if>：</td>
        </tr>
        <tr>
          <td height="25" colspan="2" style="line-height:25px;">H指数是科研人员用来分析论文引用质量的重要指标，已被越来越多的人接受。
          <br />您目前的H指数为<#if (hIndex?exists)><strong>${hIndex}</strong><#else>xx</#if>，请点击查看详情。</td>
        </tr>
        <tr>
          <td colspan="2">&nbsp;</td>
        </tr>
        <tr>
          <td height="25" colspan="2"><#if (mailBaseUrl?exists)><a href="${mailBaseUrl?replace("target2",allOutPutUrl)?replace("target3",allOutPutParams) }"<#else><a href="#" </#if> style="background:#92D050; color:#fff;padding:3px 10px; text-decoration:none; line-height:20px;">查看全部成果</a></td>
        </tr>
        <tr>
          <td colspan="2" style="border-bottom:#ccc 1px dashed;">&nbsp;</td>
        </tr>
        <tr>
          <td colspan="2">&nbsp;</td>
        </tr>
        <tr>
          <td width="262" style="line-height:15px; font-family:'宋体';">如何提高H指数：
            <p> 1. 检索并认领您的论文成果；</p>
			<p>2. 公开已发表过的论文和科研简历；</p>
          	<p>3. 吸引更多关注者查看/下载您的论文。</p>
		  </td>
          <td width="408" style="line-height:15px;"><p>&nbsp;</p>
          <p><#if (mailBaseUrl?exists)><a target="_blank" href="${mailBaseUrl?replace("target2",confPubWebUrl)?replace("target3",confPubParams)}"<#else><a href="#"</#if> style="background:#92D050; color:#fff;padding:2px 10px; text-decoration:none; line-height:20px;">认领成果</a></p>
          <p><#if (mailBaseUrl?exists)><a target="_blank" href="${mailBaseUrl?replace("target2",resProfileUrl)?replace("target3",profileParams)}"<#else><a href="#"</#if> style="background:#92D050; color:#fff;padding:2px 10px; text-decoration:none; line-height:20px;">公开科研简历</a></p>
		  		<p><#if (mailBaseUrl?exists)><a target="_blank" href="${mailBaseUrl?replace("target2",allOutPutUrl)?replace("target3",sharePaperParams)}"<#else><a href="#"</#if> style="background:#92D050; color:#fff;padding:2px 10px; text-decoration:none; line-height:20px;">共享论文给好友</a></p>
		  </td>
        </tr>
        
        <tr>
          <td height="60" colspan="2">&nbsp;</td>
        </tr>
        <tr>
          <td height="25" colspan="2">
		  科研之友客户服务小组<br />
		  Email: support@scholarmate.com <br />
		 
        </tr>
      </table></td>
  </tr>
</table>
<table width="700" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td height="60" style="color:#666; font-size:11px;">此邮件为“科研之友”系统发出，请勿回复。<br />
如果您不想收到此类邮件，请点击<#if (screenMailUrl?exists)><a style="text-decoration:none;" href="${screenMailUrl}" target="_blank">此处</a><#else><a href="#">此处</a></#if>进行设置。</td>
  </tr>
</table>
<#if (proLoad?exists)>
<#if (isView?exists)>
	<#if isView != "view">
	<img src="${proLoad}" style="display:none;"/>
	</#if>
	<#else>
	<img src="${proLoad}" style="display:none;"/>
</#if>
</#if>
</body>
</html>
