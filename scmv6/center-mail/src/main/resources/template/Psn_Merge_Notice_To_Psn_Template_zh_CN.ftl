<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>
   人员合并通知邮件
</title>
</head>

<body>
<table width="620" border="0" align="center" cellpadding="0" cellspacing="0"
        style="font-family: Arial, Helvetica, ' Microsoft YaHei'; font-size: 14px; color: #999999; background: #f7f7f4;">
        <tr>
            <td height="50" align="left" valign="middle" style="background: #666666;">
                <a href="https://maildev.scholarmate.com/EL/5c21d317240a9a21fb1f641c" target="_blank">
                    <img src="http://www.scholarmate.com/resscmwebsns/images_v5/scm_email_logo.png" style="border: none; padding-left: 20px;">
                </a>
            </td>
        </tr>
        <tr>
            <td align="center" valign="top">
                <table width="100%" border="0" cellspacing="20" cellpadding="0" style="font-size: 14px;color: #333333;">
                    <tr>
                        <td align="left" valign="top">
                            <table width="100%" border="0" cellpadding="6" cellspacing="0" style="font-size: 14px;">
                                <tr style="font-size: 14px;">
                                    <td colspan="2" style="line-height: 25px;">
                                        <#if (emailList?exists)>
                                        <span style="font-weight:bold;">${psnName!''}</span>，您已经使用帐号合并功能将
                                            <#list emailList?eval as email>
                                                <#if (email_index = 0)><a href="#" style="text-decoration:none; color:#005eac; font-size:12px;">${email}</a>
                                                <#elseif (email_index = emailList?eval?size-1)>和<a href="#" style="text-decoration:none; color:#005eac; font-size:12px;">${email}</a>
                                                <#else>、<a href="#" style="text-decoration:none; color:#005eac; font-size:12px;">${email}</a>
                                                </#if> 
                                            </#list>统一合并到<a href="#" style="text-decoration:none; color:#005eac; font-size:12px;">${email}</a>中。现在您可以使用新帐号<a href="#" style="text-decoration:none; color:#005eac; font-size:12px;">${email}</a>登录，被合并的帐号将不能再使用，但原帐号中的成果、群组等信息将被转移至保留帐号中。</td>
                                        </#if>
                                    </td>
                                </tr>
                                <tr>
                                    <td valign="top" align="left"><table width="100%" cellspacing="0" cellpadding="0" border="0">
                        <tr>
                          <td valign="middle" align="left"><table>
                            <tr>
                              <td><a href="${domainUrl!''}" style=" height:26px; padding:0 20px; margin:0; margin-right:10px; line-height:26px; color:#fff; text-decoration:none; background:#55b1f5; border:1px solid #49a0e0; display:inline-block;">立即登录</a></td>
                            </tr>
                          </table></td>
                        </tr>
                      </table></td>
                                </tr>
                                <tr>
                                    <td style="height: 6px" colspan="2"></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <!--替换内容end-->
                </table>
            </td>
        </tr>
    </table>
	<!-- 中文没有退订新页脚 -->
  <#include "/base_foot_no_unsubscribe_zh_CN.ftl" encoding= "UTF-8">
</body>
</html>  

  