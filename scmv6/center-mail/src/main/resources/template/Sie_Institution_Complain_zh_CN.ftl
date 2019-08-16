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
          <span>请跟进以下客户对机构主页的申诉：</span>
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
		              		<td valign="top" style="width: 180px;"><span style="color: #333; margin-top: 40px;margin-left:10px ">姓名：</span>${psnName}</td>
			            </tr>
			            <tr style="height:30px">
			                <td valign="top" style="width: 180px;"><span style="color: #333; margin-top: 40px;margin-left:10px ">邮箱：</span>${email}</td>
			            </tr>
			            <#if mobile??>
			            <tr style="height:30px">
			                <td valign="top" style="width: 180px;"><span style="color: #333; margin-top: 40px;margin-left:10px ">手机号：</span>${mobile}</td>
			            </tr>
			            </#if>
			            <tr style="height:30px">
			                <td valign="top" style="width: 180px;"><span style="color: #333; margin-top: 40px;margin-left:10px ">机构名称：</span>${insName}，${insId?c}</td>
			            </tr>
			            <tr style="height:30px">
			                <td valign="top" style="width: 180px;"><span style="color: #333; margin-top: 40px;margin-left:10px ">理由：</span><#if complainType == 1>我也是该主页管理人员，获取管理权限</#if><#if complainType == 2>我是该主页唯一管理人员，获取唯一管理权限</#if><#if complainType == 3>其他</#if></td>
		              	</tr>
		              	<#if complainType == 3>
		              	<tr style="height:30px">
			                <td valign="top" style="width: 180px;"><span style="color: #333; margin-top: 40px;margin-left:10px ">描述：</span><#if remark??>${remark}</#if></td>
		              	</tr>
		              	</#if>
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
