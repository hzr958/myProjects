<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>邀请认同研究领域</title>
</head>

<body>
<table width="620" border="0" align="center" cellpadding="0" cellspacing="0" style="font-family:Arial, Helvetica, ' Microsoft YaHei'; font-size:14px; color:#999999; background:#f7f7f4;">
  <tr>
    <td height="50" align="left" valign="middle" style="background:#666666;"><a href="${domainUrl!''}"  target="_blank"><img src="http://www.scholarmate.com/resscmwebsns/images_v5/scm_email_logo.png"  style="border:none; padding-left:20px;"></a></td>
  </tr>
  <tr>
    <td style="padding:30px;">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family:Arial, Helvetica, ' Microsoft YaHei'; font-size:14px; color:#333333;">
          <tr>
             <td style="line-height:25px;"><span style="font-weight:bold;">${receverPsnName}</span>，<a href="${psnUrl!'#'}" style="font-weight:bold; color:#426cad;text-decoration:none;">${psnName}</a>最近更新了研究领域。
             </td>
          </tr>
          <tr>
            <td height="15">&nbsp;</td>
          </tr>
          <tr>
            <td>
            
                <table border="0" cellspacing="0" cellpadding="0" style=" width:100%; font-family:Arial, Helvetica,  'Microsoft YaHei'; font-size:14px; color:#333333; background:#fff; padding:20px; border:1px solid #eaeaea; line-height:150%;">
                  <tr>
                    <td rowspan="4" valign="top">
                     <#list raList as ra>
                      
                        <span style=" font-size:14px;  font-weight: bold; margin: 8px 6px;  line-height:18px; text-align:center; text-decoration:none; border: 1px solid #ccc; 
                        border-right-color: #fff; padding: 9px 14px 7px 6px; color:#666666; display: inline-block;  border-width: 1px;">${ra}
                         <a href="${psnUrl!'#'}" style=" font-size: 14px;  margin: 8px 4px;  line-height:19px; font-weight: bold; text-align:center;margin-right:-15px; text-decoration:none; border: 1px solid #ccc; border-width: 1px;  padding: 10px 10px 9px 10px;color:forestgreen; border-bottom: none; display: ruby-base;">+</a>
                        </span>&nbsp;
                    </#list>
                   	</br>
                    <span style="display: block;">
                      <a href="${psnUrl!''}"  target="_blank" style=" height:26px; padding:3px 40px; margin:0;margin-top:50px; margin-right:70px; line-height:26px; color:#fff; text-decoration:none; background:#55b1f5; border:1px solid #49a0e0; display:inline-block; margin-left: 70px;">认同研究领域</a>
                      <a href="${psnUrl!''}"  target="_blank"  style=" height:26px; padding:3px 40px; margin:0; margin-right:10px; line-height:26px; color:#666666; text-decoration:none; background:#ffffff; border:1px solid #c9cdd1; display:inline-block;">查看更多</a>
                    </span>
                    </td>
                  </tr>
                </table>
            </td>
          </tr>
          <tr>
            <td valign="bottom" style=" height:30px; font-size:14px; text-align:center; color:#999999; font-style:italic;">认同研究领域，加强合作交流</td>
          </tr>
        </table>
	</td>
  </tr>
</table>
 <#include "/scm_base_foot_zh_CN.ftl" encoding= "UTF-8">              	
</body>
</html>
