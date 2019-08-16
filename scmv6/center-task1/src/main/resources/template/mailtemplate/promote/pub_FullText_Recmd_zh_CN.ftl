<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>成果全文认领</title>
</head>

<body>
<#include "/base_header_zh_CN.ftl" encoding= "UTF-8">
<table width="620" border="0" align="center" cellpadding="0" cellspacing="0" style="font-family:Arial, Helvetica,  'Microsoft YaHei';font-size:14px; color:#999999; background:#f7f7f4;">
  <tr>
    <td height="50" align="left" valign="middle" style="background:#666666;"><a href="https://www.scholarmate.com"  target="_blank"><img src="https://www.scholarmate.com/resscmwebsns/images_v5/scm_email_logo.png"  style="border:0;padding-left:20px;"></a></td>
  </tr>
  <tr>
    <td style="padding:30px;">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family:Arial, Helvetica,  'Microsoft YaHei'; font-size:14px; color:#333333;">
          <tr>
          <#if (fullTextNum?exists&&fullTextNum?number<2)>
            <td style="font-size:14px; font-weight:bold; line-height:150%;">
                <p style="margin:0; padding:0;"><span >${psnName!''} </span>， 你可能有以下论文全文。</p>
            </td>
            </#if>
             <#if (fullTextNum?exists&&fullTextNum?number>1)>
            <td style="font-size:14px; font-weight:bold; line-height:150%;">
                <p style="margin:0; padding:0;"><span >${psnName!''} </span>，有${fullTextNum!''}篇可能是你的论文全文。</p>
            </td>
           </#if>
          </tr>
          <#list pubDatas as pubdata>
             <tr>
            	<td height="15">&nbsp;</td>
        	 </tr>
          <tr>
            <td>
                <table border="0" cellspacing="0" cellpadding="0" style=" width:100%; font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#333333; background:#fff; padding:20px; border:1px solid #eaeaea; line-height:150%;">
                  <tr>
                    <td rowspan="4" valign="top"><a href="${pubdata.pubDetailUrl!'#'}${locale!''}"  target="_blank"  style="border:none; padding:0; margin:0; float:left; margin-right:20px;"><img src="https://www.scholarmate.com/resscmwebsns/images_v5/fulltextImg/scm_pdf.jpg" width="66" height="78" style="border:1px solid #e2e2e2;"></a>
                    <p style=" margin:0; padding:0; margin-left:84px; font-weight:bold;word-break:break-all;"><a  href="${pubdata.pubDetailUrl!'#'}${locale!''}"   target="_blank" style=" color:#333333; text-decoration:none;">${pubdata.pubTitle!''}</a></p>
                    <p style="color:#999999; margin:0; padding:0; margin-left:84px;">${pubdata.pubAuthors!''} <#if (pubdata.authorNum?exists&&pubdata.authorNum?number>3)><#if (dbId = 0)>等<#else>et al.</#if></#if></p>
                    <p style="color:#999999; margin:0; padding:0; margin-left:84px;">${pubdata.pubBrief!''}</p>
                    <p style="  margin:10px 0 0 84px; font-weight:bold;">
                    <a href="${puburl!''}${locale!''}"  target="_blank" style=" height:26px; padding:0 20px; margin:0; margin-right:10px; line-height:26px; color:#fff; text-decoration:none; background:#55b1f5; border:1px solid #49a0e0; display:inline-block;">确认全文</a>
                    <a href="${puburl!''}${locale!''}" target="_blank"  style=" height:26px; padding:0 20px; margin:0; margin-right:10px; line-height:26px; color:#666666; text-decoration:none; background:#ffffff; border:1px solid #c9cdd1; display:inline-block;">不是我的</a>
                    <a href="${puburl!''}${locale!''}"  target="_blank"  style=" height:26px; padding:0 20px; margin:0; margin-right:10px; line-height:26px; color:#666666; text-decoration:none; background:#ffffff; border:1px solid #c9cdd1; display:inline-block;">查看</a>
                    </p>
                    </td>
                  </tr>
                </table>
            </td>
          </tr>
          </#list>
         <#if (fullTextNum?exists&&fullTextNum?number>1)>
         <tr>
            <td valign="bottom" style=" height:50px; font-size:14px; font-weight:bold;"><a href="${puburl!''}${locale!''}"   target="_blank" style=" width:100%; height:26px; text-align:center; margin:0; margin-right:10px; line-height:26px; color:#fff; text-decoration:none; background:#55b1f5; border:1px solid #49a0e0; display:inline-block;">查看全部</a></td>
          </tr>
          </#if>
          <tr>
            <td valign="bottom" style=" height:30px; font-size:14px; text-align:center; color:#999999; font-style:italic;">确认并分享成果，提高论文引用</td>
          </tr>
        </table>
	</td>
  </tr>
</table>
 <#include "/scm_base_foot_zh_CN.ftl"  encoding= "UTF-8">
</body>
</html>
