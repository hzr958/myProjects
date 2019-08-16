<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>基金机会推荐</title>
</head>
<body>
<table width="780px" border="0" align="center" cellpadding="0" cellspacing="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei' ; font-size:14px; color:#999999; background:#f7f7f4;">
  <tr>
    <td height="50" align="left" valign="middle" style="background:#666666;"><a href="${domainUrl!''}"  target="_blank"><img src="http://www.scholarmate.com/resscmwebsns/images_v5/scm_email_logo.png"  style="border:none; padding-left:20px;"></a></td>
  </tr>
  <tr>
    <td style="padding:30px;">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei'  ; font-size:14px; color:#333333;">
         <tr>
            <td style="font-size:14px; font-weight:bold; line-height:150%;">
                  <p style="margin:0; padding:0;"><a href="${psnUrl}"  target="_blank"  style=" color:#2882d8;text-decoration:none; ">${receivePsnName}</a>，<span >有<a href="${viewUrl}"  target="_blank"  style=" color:#2882d8;text-decoration:none; ">${counts !'0'}</a></span>个关注的基金机会。</p>
            </td>
          </tr>
          <tr>
            <td height="15">&nbsp;</td>
          </tr>
          <tr>
            <td>
                <table border="0" cellspacing="1" cellpadding="5" style=" width:100%; font-family:Arial, Helvetica, 'Microsoft YaHei' ; font-size:14px; color:#333; background:#fff; padding:20px; border:1px solid #eaeaea;">
                    <#list fundList as fund> 
                      <tbody>
                      <tr style="width: auto;">
                        <td style="color:#333333; font-size:14px; padding-right: 5px;"  width="55%">
                          <p style="margin: 0; line-height: 22px;">
                           <#if (fund_index == 0)>
                          <a href="${fundDetail_0!'#'}" target="_blank" style="text-decoration:none; color:#1265cf; font-size:12px; padding-right: 5px; font-size: 14px; ">${fund.fundName!''}</a>
                          </#if>
                           <#if (fund_index == 1)>
                          <a href="${fundDetail_1!'#'}" target="_blank" style="text-decoration:none; color:#1265cf; font-size:12px; padding-right: 5px; font-size: 14px; ">${fund.fundName!''}</a>
                          </#if>
                           <#if (fund_index == 2)>
                          <a href="${fundDetail_2!'#'}" target="_blank" style="text-decoration:none; color:#1265cf; font-size:12px; padding-right: 5px; font-size: 14px; ">${fund.fundName!''}</a>
                          </#if>
                          </p>
                          <p style="color: #333;  margin: 0; line-height: 22px; ">${fund.agencyName!''}</p>
                          <p style="margin: 0; width: 240px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; line-height: 22px;">
                            <a style="color:#999; text-decoration: none;"></a>${fund.disName!''}</p>  
                        </td>
                        <td width="30%" style="padding-right: 20px; line-height: 20px;">${fund.startDate!''}<#if fund.startDate??> — </#if>${fund.endDate!''}</td>
                        <td align="center" valign="middle">
                          <p style=" color: #fff; height: 29px; border-radius: 3px; line-height: 29px; text-align: center; cursor: pointer;">
                         <#if (fund_index == 0)>
                          <a href="${fundDetail_0!'#'}" target="_blank" style="border: 1px solid #218aed; background-color: #218aed; width: 82px; text-align: center;  text-decoration:none; color:#fff; font-size:12px; padding-right: 5px; height: 29px;line-height: 29px;  display: block; width: 100%;">&nbsp;&nbsp;&nbsp;&nbsp;立即查看&nbsp;&nbsp;&nbsp;&nbsp;</a></p>
                         </#if>
                          <#if (fund_index == 1)>
                          <a href="${fundDetail_1!'#'}" target="_blank" style="border: 1px solid #218aed; background-color: #218aed; width: 82px; text-align: center;text-decoration:none; color:#fff; font-size:12px; padding-right: 5px;  height: 29px;line-height: 29px; display: block; width: 100%;">&nbsp;&nbsp;&nbsp;&nbsp;立即查看&nbsp;&nbsp;&nbsp;&nbsp;</a></p>
                         </#if>
                          <#if (fund_index == 2)>
                          <a href="${fundDetail_2!'#'}" target="_blank" style="border: 1px solid #218aed; background-color: #218aed; width: 82px; text-align: center;text-decoration:none; color:#fff; font-size:12px; padding-right: 5px;  height: 29px;line-height: 29px; display: block; width: 100%;">&nbsp;&nbsp;&nbsp;&nbsp;立即查看&nbsp;&nbsp;&nbsp;&nbsp;</a></p>
                         </#if>
                        </td>
                    </tr>
                    </tbody>
                </#list> 
                </table>
            </td>
          </tr>
          <tr>
            <td height="15">&nbsp;</td>
          </tr>
          <tr>
             <td style="font-size:14px; font-weight:bold; line-height:150%;  text-align: left;">
                <p style="margin:0; padding:0; text-align: left;"><a href="${viewUrl}"
                  target="_blank"  style=" color:#2882d8;text-decoration:none; ">修改推荐设置</a></p>
             </td>
           </tr> 
           <tr>
            <td height="40">&nbsp;</td>
          </tr>
            <tr>
         <td align="center" valign="middle">
              <p style=" color: #fff;width: 407px; height: 40px; line-height: 40px; cursor: pointer;">
                <a href="${viewUrl}" target="_blank" style="border: 1px solid #2882d8; width: 407px; background-color: #2882d8; text-decoration:none; color:#fff; font-size:16px; padding-right: 5px; height: 40px; line-height: 40px; display:inline-block;">
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;查看所有关注的基金机会&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>
              </p>
          </td>
         </tr>
          <tr height="20"></tr>
         <tr>
          <td align="center" style="margin: 16px; padding-bottom: 48px; font-size: 16px; color: #999;">点击修改推荐设置，获取更多基金</td>
         </tr>

        </table>
	</td>
  </tr>
</table>
   <#include "/scm_base_foot_zh_CN.ftl" encoding= "UTF-8">
</body>
</html>
