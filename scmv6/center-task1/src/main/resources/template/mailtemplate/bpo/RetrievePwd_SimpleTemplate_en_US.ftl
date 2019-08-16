<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>[Name] downloaded your publication [Pub Name]</title>
</head>

<body>
<body>
<!-- 正文内容 (样式内容)-->
<table width="620" border="0" align="center" cellpadding="0" cellspacing="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei' ; font-size:14px; color:#999999; background:#f7f7f4;">
<tr>
    <td height="50" align="left" valign="middle" style="background:#666666;"><a href="${domainUrl!''}"  target="_blank"><img src="http://www.scholarmate.com/resscmwebsns/images_v5/scm_email_logo.png"  style="border:none; padding-left:20px;"></a></td>
  </tr>
  
<tr>
    <td style="padding:30px;">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei'  ; font-size:14px; color:#333333;">
          <tr>
          <td bgcolor="#FFFFFF">
          	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
              <!--替换内容begin-->
              <tr>
	            <td align="center" valign="top"><table width="100%" border="0" cellspacing="20" cellpadding="0" style="font-size:14px;">
	              <tr>
	                <td align="left" valign="top"><table width="100%" border="0" cellpadding="6" cellspacing="0" style="font-size:14px;">
	                  <tr style="font-size:14px;">
	                    <td colspan="2" style="line-height:25px;">
	                    	<span style="font-weight:bold;">
		                    	${userName}, your request has been verified. Please click the button to reset your password within 24 hours. 
		                    </span>
	                    </td>
	                  </tr>
	                  <tr>
	                  	<td style="height:6px" colspan="2">
						</td>
	                </tr>
                      
	                 <tr>
	                    <td height="60" colspan="2"><table width="100%" border="0" cellspacing="0" cellpadding="0">
	                      <tr>
	                        <td width="145" align="left" valign="middle">
	                        <table width="190" border="0" cellpadding="0" cellspacing="1" bgcolor="#6a8bbf">
	                          <tr>
								<td height="37" align="center" bgcolor="#55b1f5"  style=" border-top:1px solid #55b1f5;">
									<a href="${resetUrl}" target="_blank" style=" height:26px; padding:0 20px; margin:0; margin-right:10px; line-height:26px; color:#fff; text-decoration:none; background:#55b1f5; display:inline-block;">Reset Password </a>
								</td>
	                          </tr>
	                        </table>
	                        </td>
	                      </tr>
	                    </table>
	                    </td>
	                  </tr>
	                  
	                  <tr>
	                  <td><p>Thanks for your support, and wish you had great experience in ScholarMate.<p></td>
	                  </tr>
	                  
	                  
	                  <tr>
	                  <td><span>ScholarMate：<span><a href="http://www.scholarmate.com" target="_blank" style="text-decoration:none;color:#55b1f5;"> http://www.scholarmate.com</a></td>
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
<!-- 页脚内容(样式内容) -->
<!-- 英文新页脚 -->
<table width="620" border="0" align="center" cellpadding="0" cellspacing="0" style="font-family:Arial, Helvetica, '宋体'; font-size:12px; color:#999999; background:#f7f7f4; padding:15px 0; margin-top:15px;">
  <tr >
	<td align="center" valign="bottom" style="color:#999999; font-size:12px; line-height:150%;">
	      Like us @ <a href="http://www.facebook.com/scholarmate1"  target="_blank"  style="color:#55b1f5; text-decoration:none;">facebook.com/scholarmate</a>; follow us @ <a href="http://www.twitter.com/ScholarMate1"  target="_blank"  style="color:#55b1f5; text-decoration:none;">twitter.com/ScholarMate1</a> and <a href="http://plus.google.com/+Scholarmate"  target="_blank"  style="color:#55b1f5; text-decoration:none;">plus.google.com/+Scholarmate</a>.<br />
			This is a system generated email, please do not reply. For enquiry, please write to <a href="mailto:support@scholarmate.com"   target="_blank" style="color:#55b1f5; text-decoration:none;">support@scholarmate.com</a>.
	</td>
  </tr>
  
   <tr>
    <td align="center" style="padding-top:10px; line-height:140%; color:#666">
   	If you can not read this email, please click the link below to read the details<br /><#if (viewMailUrl?exists)><a href="${viewMailPath!''}${viewMailUrl}"  target="_blank" style="color:#999; width:420px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap; display:inline-block;">${viewMailPath!''}${viewMailUrl}</a><#else>
   	<a href="http://www.scholarmate.com" style="color:#999; width:420px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap; display:inline-block;" >http://www.scholarmate.com</a></#if>
    </td>
  </tr>
</table>        	
</body>
</html>
