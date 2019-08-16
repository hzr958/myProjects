<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>帐号确认</title>
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
          <tr><td colspan="2" style="line-height:25px;padding-bottom: 20px;"><span style="font-weight:bold;">亲爱的${zh_CN_psnname}：</span></td></tr>
          <tr>
          <td bgcolor="#FFFFFF" colspan="2" >
            <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
              <!--替换内容begin-->
              <tr>
                <td align="center" valign="top" style="border: 1px solid #e4e4e4;">
                <table width="100%" border="0" cellspacing="20" cellpadding="0" style="font-size:14px;">
                  <tr>
                    <td align="left" valign="top">
                    <table width="100%" border="0" cellpadding="6" cellspacing="0" style="font-size:14px;margin-top:-16px;">
                      <tbody><tr style="font-size:14px;">
                        <td colspan="2" style="line-height:32px;font-size:11pt;padding-bottom: 20px;">
                                    您在“${keycode_ins_name}科研之友机构版”的帐号信息如下：<br>
                                    网址：${keycode_app_InsDomain}<br>
                                    帐号：${keycode_psn_login}<br>
                                    请在1天内登录并设置密码，以确保您个人信息的安全。<br>
                                    <!--隐藏变量：当前机构ID-->
                                    <input type="hidden" id="current_ins_id" name="current_ins_id" value="${current_ins_id}">
                        </td></tr>
                    </tbody></table>
                  </td>
              </tr><!--替换内容end-->
            </table>
            </td>
        </tr>
      </table>
      </td>
  </tr>
    <tr>
        <td height="32" style="width:20%; border-top:1px solid #ddd;padding-top: 20px;">
            <a href="${keycode_app_baseUrl}&amp;locale=zh_CN" style="color: #fff;display:block;line-height:42px;background:#2882d8;border-radius:5px;font-size:14px; text-align:center;font-weight:bold; text-decoration:none;text-align: center;">立即设置密码
            </a>
        </td>
        <td style="font-family:Microsoft JhengHei;font-size:14px;padding-left:16px;border-top:1px solid #ddd;padding-top: 20px;">
            <span style="font-weight: bold;font-family: Arial, Helvetica, 'Microsoft YaHei';">确保您的信息安全是我们的首要任务</span>
        </td>
      </tr>
</table>
</td>
  </tr>
</table>
<#include "/sie_contact_base_foot_zh_CN.ftl" encoding= "UTF-8">
</body>
</html>

