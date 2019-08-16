<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>待处理消息通知</title>
</head>

<body>
<table width="620" border="0" align="center" cellpadding="0" cellspacing="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#999999; background:#f7f7f4;">
  <tr>
    <td height="50" align="left" valign="middle" style="background:#666666;"><a href="${domainUrl!''}"  target="_blank"><img src="http://www.scholarmate.com/resscmwebsns/images_v5/scm_email_logo.png" style="border:0;padding-left:20px;"></a></td>
  </tr>
  <tr>
    <td style="padding:30px;">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#333333;">
          <tr>
            <td style="font-size:14px; line-height:150%;">
               <p style="margin:0; padding:0;"><span ><a href="${psnUrl!'#'}"   target="_blank" style=" color:#55b1f5; border:none; padding:0; margin:0; float:left; margin-right:0px; text-decoration: none;">${psnName!''}</a></span>，你有以下任务待处理。</p>
            </td>
          </tr>
          <tr>
            <td height="15">&nbsp;</td>
          </tr>
          <#if (pubConfirmCount?exists&&pubConfirmCount?number>0)>
          <tr>
           <td>
                <table border="0" cellspacing="0" cellpadding="0" style=" width:100%; font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#333333; background:#fff; padding:20px; border:1px solid #eaeaea; line-height:150%; border-top:none; border-bottom: none;">
                  <tr>
                    <td valign="top"  rowspan="4" style="width: 180px;"><span style="color: #333; padding-top: 40px;margin-left:10px ">${pubConfirmCount}条成果待认领</span></td>
                    <td valign="top"  align="center">
                    <a href="${cfmPubUrl!'#'}"  target="_blank" style=" height:26px;  padding:0px 15px; margin:0;margin-left:30px; line-height:26px; color:#fff; text-decoration:none; background:#55b1f5; border:1px solid #49a0e0; display: inline-block; ">&nbsp;&nbsp;&nbsp;&nbsp;认领成果&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="${cfmPubUrl!'#'}"  target="_blank" style=" height:26px;  padding:0px 20px; margin:0; line-height:26px; color:#666666; text-decoration:none; background:#ffffff; border:1px solid #c9cdd1;  display: inline-block;">&nbsp;&nbsp;&nbsp;&nbsp;忽略&nbsp;&nbsp;&nbsp;&nbsp;</a>
                    </td>
                  </tr>
                </table>
            </td>
          </tr>
		 </#if>
          <#if (pubFulltextCount?exists&&pubFulltextCount?number>0)>
          <tr>
           <td>
                <table border="0" cellspacing="0" cellpadding="0" style=" width:100%; font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#333333; background:#fff; padding:20px; border:1px solid #eaeaea; line-height:150%; border-top:none; border-bottom: none;">
                  <tr>
                    <td  valign="top"  rowspan="4" style="width: 180px;"><span style="color: #333; padding-top: 40px;margin-left:10px ">${pubFulltextCount}条全文待认领</span></td>
                    <td valign="top"  align="center">
                    <a href="${cfmfulltextUrl!'#'}"  target="_blank" style="text-decoration: none; border: #49a0e0 1px solid; height: 26px; background: #55b1f5; color: #fff; padding: 0px 15px;  margin: 0px 0px 0px 30px;  display:inline-block; line-height:26px;">&nbsp;&nbsp;&nbsp;&nbsp;认领全文&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="${cfmfulltextUrl!'#'}"  target="_blank" style=" height:26px;  padding:0px 20px; margin:0;line-height:26px; color:#666666; text-decoration:none; background:#ffffff; border:1px solid #c9cdd1;  display: inline-block;">&nbsp;&nbsp;&nbsp;&nbsp;忽略&nbsp;&nbsp;&nbsp;&nbsp;</a>
                    </td>
                  </tr>
                </table>
            </td>
          </tr>
		 </#if>
		 <#if (pubFulltextReqCount?exists&&pubFulltextReqCount?number>0)>
		 <tr>
           <td>
                <table border="0" cellspacing="0" cellpadding="0" style=" width:100%; font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#333333; background:#fff; padding:20px; border:1px solid #eaeaea; line-height:150%; border-top:none; border-bottom: none;">
                  <tr>
                    <td valign="top"  rowspan="4" style="width: 180px;"><span style="color: #333; padding-top: 40px;margin-left:10px ">${pubFulltextReqCount}个全文请求</span></td>
                    <td valign="top"  align="center">
                    <a href="${fulltextReqUrl!'#'}"  target="_blank" style="text-decoration: none; border: #49a0e0 1px solid; height: 26px; background: #55b1f5; color: #fff; padding: 0px 15px;  margin: 0px 0px 0px 30px;  display:inline-block; line-height:26px;">&nbsp;&nbsp;&nbsp;&nbsp;处理请求&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="${fulltextReqUrl!'#'}"  target="_blank" style=" height:26px;  padding:0px 20px; margin:0;line-height:26px; color:#666666; text-decoration:none; background:#ffffff; border:1px solid #c9cdd1;  display: inline-block;">&nbsp;&nbsp;&nbsp;&nbsp;忽略&nbsp;&nbsp;&nbsp;&nbsp;</a>
                    </td>
                  </tr>
                </table>
            </td>
          </tr>
		 </#if>	
           <#if (psnFrdReqCount?exists&&psnFrdReqCount?number>0)>
          <tr>
           <td>
                <table border="0" cellspacing="0" cellpadding="0" style=" width:100%; font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#333333; background:#fff; padding:20px; border:1px solid #eaeaea; line-height:150%; border-top:none; border-bottom: none;">
                  <tr>
                    <td valign="top"  rowspan="4" style="width: 180px;"><span style="color: #333; padding-top: 40px;margin-left:10px ">${psnFrdReqCount}个联系人请求</span></td>
                    <td valign="top"  align="center">
                    <a href="${frdReqUrl!'#'}"  target="_blank" style="text-decoration: none; border: #49a0e0 1px solid; height: 26px; background: #55b1f5; color: #fff; padding: 0px 15px;  margin: 0px 0px 0px 30px;  display:inline-block; line-height:26px;">&nbsp;&nbsp;&nbsp;&nbsp;处理请求&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="${frdReqUrl!'#'}"  target="_blank" style=" height:26px;  padding:0px 20px; margin:0;  line-height:26px; color:#666666; text-decoration:none; background:#ffffff; border:1px solid #c9cdd1;  display: inline-block;">&nbsp;&nbsp;&nbsp;&nbsp;忽略&nbsp;&nbsp;&nbsp;&nbsp;</a>
                    </td>
                  </tr>
                </table>
            </td>
          </tr>
          </#if>
		  
 		 <#if (psnGroupReqCount?exists&&psnGroupReqCount?number>0)>
          <tr>
           <td>
                <table border="0" cellspacing="0" cellpadding="0" style=" width:100%; font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#333333; background:#fff; padding:20px; border:1px solid #eaeaea; line-height:150%; border-top:none; border-bottom: none;">
                  <tr>
                    <td valign="top"  rowspan="4" style="width: 180px;"><span style="color: #333; padding-top: 40px;margin-left:10px ">${psnGroupReqCount}个群组请求</span></td>
                    <td valign="top"  align="center">
                    <a href="${grpReqUrl!'#'}"  target="_blank" style="text-decoration: none; border: #49a0e0 1px solid; height: 26px; background: #55b1f5; color: #fff; padding: 0px 15px;  margin: 0px 0px 0px 30px;  display:inline-block; line-height:26px;">&nbsp;&nbsp;&nbsp;&nbsp;处理请求&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="${grpReqUrl!'#'}"  target="_blank" style=" height:26px;  padding:0px 20px; margin:0; line-height:26px; color:#666666; text-decoration:none; background:#ffffff; border:1px solid #c9cdd1;  display: inline-block;">&nbsp;&nbsp;&nbsp;&nbsp;忽略&nbsp;&nbsp;&nbsp;&nbsp;</a>
                    </td>
                  </tr>
                </table>
            </td>
          </tr>
		 </#if>
 		 <#if (psnGroupInviteCount?exists&&psnGroupInviteCount?number>0)>
          <tr>
           <td>
                <table border="0" cellspacing="0" cellpadding="0" style=" width:100%; font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#333333; background:#fff; padding:20px; border:1px solid #eaeaea; line-height:150%; border-top:none; border-bottom: none;">
                  <tr>
                    <td valign="top"  rowspan="4" style="width: 180px;"><span style="color: #333; padding-top: 40px;margin-left:10px ">${psnGroupInviteCount}个群组邀请</span></td>
                    <td valign="top"  align="center">
                    <a href="${grpInviteUrl!'#'}"  target="_blank" style="text-decoration: none; border: #49a0e0 1px solid; height: 26px; background: #55b1f5; color: #fff; padding: 0px 15px;  margin: 0px 0px 0px 30px;  display:inline-block; line-height:26px;">&nbsp;&nbsp;&nbsp;&nbsp;处理请求&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="${grpInviteUrl!'#'}"  target="_blank" style=" height:26px;  padding:0px 20px; margin:0; line-height:26px; color:#666666; text-decoration:none; background:#ffffff; border:1px solid #c9cdd1; display: inline-block; ">&nbsp;&nbsp;&nbsp;&nbsp;忽略&nbsp;&nbsp;&nbsp;&nbsp;</a>
                    </td>
                  </tr>
                </table>
            </td>
          </tr>
		 </#if>
		 <#if (insideLetterCount?exists&&insideLetterCount?number>0)>
          <tr>
           <td>
                <table border="0" cellspacing="0" cellpadding="0" style=" width:100%; font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#333333; background:#fff; padding:20px; border:1px solid #eaeaea; line-height:150%;border-top:none; border-bottom: none;">
                  <tr>
                    <td valign="top"  rowspan="4" style="width: 180px;"><span style="color: #333;padding-top: 40px;margin-left:10px ">${insideLetterCount}条站内信消息</span></td>
                    <td valign="top" align="center">
                    <a href="${insideLetterUrl!'#'}"  target="_blank" style="text-decoration: none; border: #49a0e0 1px solid; height: 26px; background: #55b1f5; color: #fff; padding: 0px 15px;  margin: 0px 0px 0px 30px;  display:inline-block; line-height:26px;">&nbsp;&nbsp;&nbsp;&nbsp;查看消息&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="${insideLetterUrl!'#'}"  target="_blank" style=" height:26px;  padding:0px 20px; margin:0; line-height:26px; color:#666666; text-decoration:none; background:#ffffff; border:1px solid #c9cdd1;display: inline-block;">&nbsp;&nbsp;&nbsp;&nbsp;忽略&nbsp;&nbsp;&nbsp;&nbsp;</a>
                    </td>
                  </tr>
                </table>
            </td>
          </tr>
	 	 </#if>
		 
		 
		 <#if (otherCount?exists&&otherCount?number>0)>
		 <tr>
           <td>
                <table border="0" cellspacing="0" cellpadding="0" style=" width:100%; font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#333333; background:#fff; padding:20px; border:1px solid #eaeaea; line-height:150%; border-top:none; border-bottom: none;">
                  <tr>
                    <td valign="top"  rowspan="4" style="width: 180px;"><span style="color: #333; padding-top: 40px;margin-left:10px ">${otherCount}条其他消息</span></td>
                    <td valign="top"  align="center">
                    <a href="${centerMsgUrl!'#'}"  target="_blank" style="text-decoration: none; border: #49a0e0 1px solid; height: 26px; background: #55b1f5; color: #fff; padding: 0px 15px;  margin: 0px 0px 0px 30px;  display:inline-block; line-height:26px;">&nbsp;&nbsp;&nbsp;&nbsp;处理请求&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="${centerMsgUrl!'#'}"  target="_blank" style=" height:26px;  padding:0px 20px; margin:0; line-height:26px; color:#666666; text-decoration:none; background:#ffffff; border:1px solid #c9cdd1; display: inline-block;">&nbsp;&nbsp;&nbsp;&nbsp;忽略&nbsp;&nbsp;&nbsp;&nbsp;</a>
                    </td>
                  </tr>
                </table>
            </td>
          </tr>
		 </#if>	
          <tr>
            <td height="15">&nbsp;</td>
          </tr>
         <tr align="center">
            <td valign="bottom" align="center" style=" height:50px; font-size:14px;"><a href="${insideLetterUrl!'#'}"   target="_blank" style=" width:100%; height:26px; text-align:center; margin:0; margin-right:10px; line-height:26px; color:#fff; text-decoration:none; background:#55b1f5; border:1px solid #49a0e0; display:inline-block;">&nbsp;&nbsp;&nbsp;&nbsp;查看更多消息&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
          </tr>
          <tr>
            <td valign="bottom" style=" height:30px; font-size:14px; text-align:center; color:#999999; font-style:italic;">处理任务请求，提高工作效率</td>
          </tr>
        </table>
	</td>
  </tr>
  </table>
  <#include "/scm_base_foot_zh_CN.ftl" encoding= "UTF-8">
</body>
</html>
