<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>待处理消息通知</title>
</head>

<body>

<!-- 正文内容 (样式内容)-->
<table width="728" border="0" align="center" cellpadding="0" cellspacing="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei' ; font-size:14px; color:#999999; background:#f7f7f4;">
<tr>
    <td height="50" align="left" valign="middle" style="background:#666666;"><a href="${sie_ins_domain!''}"  target="_blank"><img src="${sie_ins_domain!""}/ressie/images/sie_email_logo2.png"  style="border:none; padding-left:20px;"></a></td>
</tr>
<tr>
    <td style="padding:24px;">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei'  ; font-size:14px; color:#333333;">
          <tr><td style="padding-bottom: 20px;">
          <span>请处理以下全文请求，上传全文有利于提高成果影响力，增加引用</span>
          </td></tr>
          <tr>
          <tr><td bgcolor="#FFFFFF"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
              <!--替换内容begin-->
				<tr><td align="center" valign="top">
                <table width="100%" border="0" cellspacing="20" cellpadding="0" style="font-size:14px;">
                  <tr>
                    <td align="left" valign="top">
                    <table width="100%" border="0" cellpadding="6" cellspacing="0" style="font-size:14px;">
                      	<tr style="height:30px">
		              		<td valign="top" style="width: 180px;"><span style="color: #333; margin-top: 40px;margin-left:10px ">标题：</span><#if title??>${title}</#if></td>
			            </tr>
			            <tr style="height:30px">
			                <td valign="top" style="width: 180px;"><span style="color: #333; margin-top: 40px;margin-left:10px ">理由：</span><#if reason??>${reason}</#if></td>
			            </tr>
			            <tr style="height:30px">
			                <td valign="top" style="width: 180px;"><span style="color: #333; margin-top: 40px;margin-left:10px ">全文所属机构：</span><#if insName??>${insName}</#if></td>
			            </tr>
			            <tr style="height:30px">
			                <td valign="top" style="width: 180px;"><span style="color: #333; margin-top: 40px;margin-left:10px ">请求人：</span><#if psnName??>${psnName}</#if></td>
			            </tr>
			            <tr style="height:30px">
			                <td valign="top" style="width: 180px;"><span style="color: #333; margin-top: 40px;margin-left:10px ">请求人邮箱：</span><#if email??>${email}</#if></td>
		              	</tr>
                  	</table>
                  	</td>
				  </tr><!--替换内容end-->
            						</table>
            					</td>
        					</tr>
      					</table>
      				</td>
  				</tr>
			</table>
		</td>
	</tr>
</table>

<#include "/sie_contact_base_foot_zh_CN.ftl" encoding= "UTF-8">
</body>
</html>
