<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head> 
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
  <title>
	Scholarmate - Retrieve Password
</title> 
 </head> 
 <body> 
  <!-- 页眉内容 (样式内容)--> 
  <!-- 正文内容 (样式内容)--> 
  <table width="620" border="0" align="center" cellpadding="0" cellspacing="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#999999; background:#f7f7f4;"> 
   <tbody> 
    <tr> 
     <td height="50" align="left" valign="middle" style="background:#666666;"><a href="https://mailuat.scholarmate.com/EL/5c17056d0e6ea1c8847cb3de" target="_blank"><img src="http://www.scholarmate.com/resscmwebsns/images_v5/scm_email_logo.png" style="border:none; padding-left:20px;" /></a></td> 
    </tr> 
    <tr> 
     <td style="padding:30px;"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#333333;"> 
       <tbody> 
        <tr> 
         <td align="left" valign="top">
            <!--替换内容start-->  
          <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#333333;"> 
           <tbody> 
            <tr> 
             <td style="font-size:14px; font-weight:bold; line-height:150%;"> <p style="margin:0; padding:0;"> ${userName}, You used Forget Password to recover your ScholarMate account for the email address: ${forgetEmail} ，the request has been verified. From the email address you provided, we found ${users?size} matched result(s): </p> </td> 
            </tr> 
            <tr> 
             <td height="15">&nbsp;</td> 
            </tr> 
            <tr> 
             <td> 
              <table border="0" cellspacing="0" cellpadding="0" style=" width:100%; font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#333333; background:#fff; padding:20px; border:1px solid #eaeaea; line-height:150%;"> 
               <tbody>
                <tr> 
                 <td align="left">
                  <#list users as user>
                  <#list links as link>
                  <#if user_index==link_index>
                  ${"Login account"+(user_index+1)}：<br />
                   Login Name:${user.loginName}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    Name:${user.userName}<br />
                     Reset password link:<br />
                     <a style="word-break: break-all;color:#55b1f5;" href="${link.url}">${link.url}</a>
                     <#if user_has_next><br /><br /></#if>
                     </#if>
                  </#list>
                  </#list>
                   </td> 
                </tr> 
               </tbody>
              </table> </td> 
            </tr> 
           </tbody>
          </table> </td> 
        </tr>
        <!--替换内容end--> 
       </tbody>
      </table> </td> 
    </tr> 
   </tbody>
  </table>       
  <!-- 英文新页脚 --> 
  <table width="620" border="0" align="center" cellpadding="0" cellspacing="0" style="font-family:Arial, Helvetica, '宋体'; font-size:12px; color:#999999; background:#f7f7f4; padding:15px 0; margin-top:15px;"> 
   <tbody>
    <tr> 
     <td align="center" valign="bottom" style="color:#999999; font-size:12px; line-height:150%;"> Like us @ <a href="http://www.facebook.com/scholarmate1" target="_blank" style="color:#55b1f5; text-decoration:none;">facebook.com/scholarmate1</a>; follow us @ <a href="http://twitter.com/ScholarMate1" target="_blank" style="color:#55b1f5; text-decoration:none;">twitter.com/ScholarMate1</a> and <a href="http://plus.google.com/+Scholarmate" target="_blank" style="color:#55b1f5; text-decoration:none;">plus.google.com/+Scholarmate</a>.<br /> This is a system generated email, please do not reply. For enquiry, please write to <a href="mailto:support@scholarmate.com" target="_blank" style="color:#55b1f5; text-decoration:none;">support@scholarmate.com</a>.<br /> If you cannot view this email properly, please<a href="${viewMailPath!''}${viewMailUrl}" target="_blank" style="color:#55b1f5; text-decoration:none;"> click here.</a> </td> 
    </tr> 
   </tbody>
  </table>   
 </body>
</html>
