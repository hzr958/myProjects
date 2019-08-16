<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>成果影响力的邮件</title>
</head>

<body>
<#include "/base_header_en_US.ftl" encoding= "UTF-8">
<table width="620" border="0" align="center" cellpadding="0" cellspacing="0" style="font-family:Arial, Helvetica,  'Microsoft YaHei'; font-size:12px; color:#999999; background:#f7f7f4;">
  <tr>
    <td height="50" align="left" valign="middle" style="background:#666666;"><a href="https://www.scholarmate.com"  target="_blank"><img src="https://www.scholarmate.com/resscmwebsns/images_v5/scm_email_logo.png" style="border:none; padding-left:20px;"></a></td>
  </tr>
  <tr>
    <td style="padding:30px;">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:12px; color:#333333;">
          <tr>
            <td height="42" colspan="2" align="center" valign="top" style="font-size:14px; font-weight:bold;">
            <a href="${viewDetail!''}"  target="_blank"  style=" width:300px; height:30px; text-align:center; margin:0; line-height:30px; color:#fff; text-decoration:none; background:#55b1f5; border:1px solid #49a0e0; display:inline-block;">View My Research Impact</a>	
            </td>
          </tr>
          <tr>
            <td colspan="2">
                <table border="0" cellspacing="0" cellpadding="0" style=" width:100%; font-family:Arial, Helvetica,  'Microsoft YaHei'; font-size:12px; color:#333333; background:#fff; padding:20px; border:1px solid #eaeaea; line-height:150%;">
                  <tr>
                    <td>
                    	<a href="${viewDetail!''}"  target="_blank"  style=" border:none; padding:0; margin:0; margin-right:20px; float:left;"><img src="https://www.scholarmate.com/resscmwebsns/images_v5/fulltextImg/scm_pdf.jpg" width="66" height="78" style="border:1px solid #e2e2e2;" ></a>
                        <font style=" font-size:30px; color:#55b1f5; height:40px; line-height:40px; font-family:Arial, Helvetica, 'Microsoft YaHei'; padding:0; margin:20px 10px 0 0; float:left;">${titleCount!''}</font>
                        <font style=" height:30px; line-height:30px; font-size:18px; color:#333333; font-family:Arial, Helvetica,  'Microsoft YaHei'; padding:0; margin:0; margin-top:25px; float:left;">${titlesName!''}</font>
                        <a href="${viewDetail!''}"   target="_blank" style=" width:75px; height:26px; padding:0; display:inline-block; text-align:center; margin:0; margin-top:26px; line-height:26px; color:#666666; text-decoration:none; background:#ffffff; border:1px solid #c9cdd1; float:right;">View</a>
                    </td>
                  </tr>
                </table>
            </td>
          </tr>
          <tr>
          	<td height="10" colspan="2"></td>
          </tr>
          <tr>
          	<td colspan="2">
            	<table border="0" cellspacing="0" cellpadding="0" style=" width:100%; font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:12px; color:#333333; background:#fff; padding:10px 20px; border:1px solid #eaeaea; line-height:150%;">
                	<tr>
                    	<td>
                        <table border="0" cellspacing="0" cellpadding="0" style=" width:100%; font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:12px; color:#333333; background:#fff; line-height:150%;">
                    	<tr>
                        	<td width="40%" height="40" align="left" style=" font-size:12px; color:#999999; border-bottom:1px solid #eaeaea;">&nbsp;</td>
                            <td width="30%" height="40" align="center" style="  font-size:14px; color:#999999; border-bottom:1px solid #eaeaea;">Total</td>
                            <td width="30%" height="40" align="center" style=" font-size:14px; color:#999999; border-bottom:1px solid #eaeaea;">This week</td>
                        </tr>
                        <tr>
                        	<th height="40" align="left" style=" font-size:14px; color:#999999; border-bottom:1px solid #eaeaea; text-indent:1em;">Publication</th>
                            <td height="40" align="center" style=" font-size:18px; color:#55b1f5; border-bottom:1px solid #eaeaea;">${pubCount?default(0)}</td>
                            <td height="40" align="center" style=" font-size:18px; color:#55b1f5; border-bottom:1px solid #eaeaea;">${pubMonth?default(0)}</td>
                        </tr>
                        <tr>
                        	<th height="40" align="left" style=" font-size:14px; color:#999999; border-bottom:1px solid #eaeaea; text-indent:1em;">Citation</th>
                            <td height="40" align="center" style=" font-size:18px; color:#55b1f5; border-bottom:1px solid #eaeaea;">${citedCount?default(0)}</td>
                            <td height="40" align="center" style=" font-size:18px; color:#55b1f5; border-bottom:1px solid #eaeaea;">--</td>
                        </tr>
                        <tr>
                        	<th height="40" align="left" style=" font-size:14px; color:#999999; border-bottom:1px solid #eaeaea; text-indent:1em;">Social Factor<span style="color:#999999; font-size:12px; font-weight:normal; font-style:italic;"> (Like, Share）</span></th>
                            <td height="40" align="center" style=" font-size:18px; color:#55b1f5; border-bottom:1px solid #eaeaea;">${socialCount?default(0)}</td>
                            <td height="40" align="center" style=" font-size:18px; color:#55b1f5; border-bottom:1px solid #eaeaea;">${socialMonth?default(0)}</td>
                        </tr>
                        <tr>
                        	<th height="40" align="left" style=" font-size:14px; color:#999999; border-bottom:1px solid #eaeaea; text-indent:1em;">Download</th>
                            <td height="40" align="center" style=" font-size:18px; color:#55b1f5; border-bottom:1px solid #eaeaea;">${downloadCount?default(0)}</td>
                            <td height="40" align="center" style=" font-size:18px; color:#55b1f5; border-bottom:1px solid #eaeaea;">${downloadMonth?default(0)}</td>
                        </tr>
                        <tr>
                        	<th height="40" align="left" style=" font-size:14px; color:#999999; border-bottom:1px solid #eaeaea; text-indent:1em;">Read</th>
                            <td height="40" align="center" style=" font-size:18px; color:#55b1f5; border-bottom:1px solid #eaeaea;">${readCount?default(0)}</td>
                            <td height="40" align="center" style=" font-size:18px; color:#55b1f5; border-bottom:1px solid #eaeaea;">${readMonth?default(0)}</td>
                        </tr>
                        <tr>
                        	<th height="40" align="left" style=" font-size:14px; color:#999999; text-indent:1em;">H-Index<span style="color:#999999; font-size:12px; font-weight:normal; font-style:italic;">  (Personal impact)</span></th>
                            <td height="40" align="center" style=" font-size:18px; color:#55b1f5;">${hindex?default(0)}</td>
                            <td height="40" align="center" style=" font-size:18px; color:#55b1f5;">--</td>
                        </tr>
                        </table>
                    </td>
                    </tr>
                </table>
            </td>
          </tr>
          <tr>
          	<td height="20" colspan="2"></td>
          </tr>
          <tr>
            <td width="370" align="left" valign="middle"><img src="https://www.scholarmate.com/resscmwebsns/images_v5/process_en.png"></td>
            <td width="190" align="center" valign="middle" style=" font-size:14px; text-align:center; color:#999999; line-height:200%;"><a href="${addPub!''}"   target="_blank" style=" width:188px; height:26px; text-align:center; margin:0; line-height:26px; color:#fff; font-weight:bold; text-decoration:none; background:#55b1f5; border:1px solid #49a0e0; display:inline-block;">Upload My Publication</a>
            <p style="font-size:10px; word-wrap: break-word; word-break: normal; font-style:italic;">Upload publication to increase citation</p></td>
          </tr>
        </table>
	</td>
  </tr>
</table>
<#include "/scm_base_foot_en_US.ftl" encoding= "UTF-8">
</body>
</html>
