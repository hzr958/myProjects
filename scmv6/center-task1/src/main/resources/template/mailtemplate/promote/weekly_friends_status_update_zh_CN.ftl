<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>你好友的动态更新</title>
</head>

<body>
<#include "base_header_zh_CN.ftl" encoding="UTF-8">
<table width="620" border="0" align="center" cellpadding="0" cellspacing="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:12px; color:#999999; background:#f7f7f4;">
  <tr>
    <td width="20" align="left" valign="middle" style="background:#666666;">&nbsp;</td>
    <td height="50" align="left" valign="middle" style="background:#666666;"><a href="${domainUrl!''}"  target="_blank"><img src="https://www.scholarmate.com/resscmwebsns/images_v5/scm_email_logo.png" style="border:none;"></a></td>
  </tr>
  <tr>
    <td colspan="2" style="padding:30px;">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" style=" font-size:12px; color:#333333;">
        <#if (pubPsnName0?exists)|| (pubPsnName1?exists)||(pubPsnName2?exists)>
          <tr>
            <td style="font-size:14px; font-weight:bold; line-height:150%;">成果更新：<ahref="${psnUrl!'#'}"  target="_blank" style=" color:#55b1f5; text-decoration:none;">${pubDynCount!'0'}</a> 位好友确认了<a href="${psnUrl!'#'}" target="_blank" style=" color:#55b1f5; text-decoration:none;">${pubExtendsCount!pubDynCount}</a>篇论文成果</td>
          </tr>
          
          <tr>
            <td height="8"></td>
          </tr>
          
          <tr>
            <td>
                <table border="0" cellspacing="0" cellpadding="0" style=" width:100%; font-size:14px; color:#333333; background:#fff; padding:20px; border:1px solid #eaeaea; line-height:150%;">
                <#if (pubPsnName0?exists)>
                  <tr style="margin-bottom:0; padding-bottom:0;">
                    
                    <td width="13%" valign="top"><a href="${pubAuthorUrl0!'#'}"  target="_blank"  style=" border:none; padding:0; margin:0; float:left; margin-right:20px; text-decoration:none;"><img src="${psnAvatar0}" width="50" height="50" style="border:none;"><br><span style="width:50px; color:#55b1f5; display:block; text-align:center;">${pubPsnName0}</span></a>
                    </td>
                    
                    <td valign="top">
                    <span style=" margin:0; padding:0; font-weight:bold;display:block;"><a href="${pubResLink0!'#'}"  target="_blank" style=" color:#333; text-decoration:none;">${resTitle0!''}</a></span>
                    <span style="color:#999999; margin:0; padding:0;">${reAuthor0!''}</span>
                    <p style="color:#999999; margin:0; padding:0;">${resOrigin0!''}</p></td>
                  </tr>
                  
                  <tr style=" padding-top:0; padding-bottom:0; line-height:100%;">
                    <td colspan="2" valign="bottom"><span style=" padding:0; margin:0; width:100%; border-bottom:1px dashed #eaeaea; display:inline-block; height:2px; margin-bottom:7px;"></span></td>
                  </tr>
                    </#if>
                    
                    <#if (pubPsnName1?exists)>
                  <tr style="margin-bottom:0; padding-bottom:0;">
                    
                    <td width="13%" valign="top"><a href="${pubAuthorUrl1!'#'}"  target="_blank"  style=" border:none; padding:0; margin:0; float:left; margin-right:20px; text-decoration:none;"><img src="${psnAvatar1}" width="50" height="50" style="border:none;"><br><span style="width:50px; color:#55b1f5; display:block; text-align:center;">${pubPsnName1}</span></a>
                    </td>
                    
                    <td valign="top">
                    <span style=" margin:0; padding:0; font-weight:bold;display:block;"><a href="${pubResLink1!'#'}"  target="_blank" style=" color:#333; text-decoration:none;">${resTitle1!''}</a></span>
                    <span style="color:#999999; margin:0; padding:0;">${reAuthor1!''}</span>
                    <p style="color:#999999; margin:0; padding:0;">${resOrigin1!''}</p></td>
                  </tr>
                  
                  <tr style=" padding-top:0; padding-bottom:0; line-height:100%;">
                    <td colspan="2" valign="bottom"><span style=" padding:0; margin:0; width:100%; border-bottom:1px dashed #eaeaea; display:inline-block; height:2px; margin-bottom:7px;"></span></td>
                  </tr>
                    </#if>
                  
                    <#if (pubPsnName2?exists)>
                  <tr style="margin-bottom:0; padding-bottom:0;">
                    
                    <td width="13%" valign="top"><a href="${pubAuthorUrl2!'#'}"  target="_blank"  style=" border:none; padding:0; margin:0; float:left; margin-right:20px; text-decoration:none;"><img src="${psnAvatar2}" width="50" height="50" style="border:none;"><br><span style="width:50px; color:#55b1f5; display:block; text-align:center;">${pubPsnName2}</span></a>
                    </td>
                    
                    <td valign="top">
                    <span style=" margin:0; padding:0; font-weight:bold;display:block;"><a href="${pubResLink1!'#'}"  target="_blank" style=" color:#333; text-decoration:none;">${resTitle2!''}</a></span>
                    <span style="color:#999999; margin:0; padding:0;">${reAuthor2!''}</span>
                    <p style="color:#999999; margin:0; padding:0;">${resOrigin2!''}</p></td>
                  </tr>
                  
                  <tr style=" padding-top:0; padding-bottom:0; line-height:100%;">
                    <td colspan="2" valign="bottom"><span style=" padding:0; margin:0; width:100%; border-bottom:1px dashed #eaeaea; display:inline-block; height:2px; margin-bottom:7px;"></span></td>
                  </tr>
                    </#if>
                  
                   <#if (pubExtendsCount?number>3) >
                  <tr style=" padding-top:0; padding-bottom:0; line-height:100%;">
                    <td colspan="2" align="left" valign="top"><a href="${psnUrl!'#'}"   target="_blank" style="color:#999; text-decoration:none; font-size:12px;">>>查看更多</a></td>
                  </tr>
                  </#if>
                </table>
            </td>
          </tr>
          
          <tr>
            <td height="15"></td>
          </tr>
           </#if>
           
           
          
          
          <#if (enterRa?exists)>
          <tr>
            <td style="font-size:14px; font-weight:bold; line-height:150%;">研究领域更新：<a href="${psnUrl!'#'}"  target="_blank" style=" color:#55b1f5; text-decoration:none;">${raDynCount!''}</a>位好友更新了研究领域</td>
          </tr>
          <tr>
            <td height="8"></td>
          </tr>
          <tr>
            <td>
                <table border="0" cellspacing="0" cellpadding="0" style=" width:100%; font-size:14px; color:#333333; background:#fff; padding:20px; border:1px solid #eaeaea; line-height:150%;">
                    <#list rDynList as raDyn >
                   <#if (raDyn.keyword?exists)>
                  <tr style="margin-bottom:0; padding-bottom:0;">
                    <td width="13%"><a  href="${raDyn.frdPsnUrl!'#'}"   target="_blank" style=" border:none; padding:0; margin:0; float:left; margin-right:20px; text-decoration:none;"><img src="${raDyn.psnAvatar}" width="50" height="50"style="border:none;"><br><span style="width:50px; color:#55b1f5; display:block; text-align:center;">${raDyn.psnName!''}</span></a></td>
                    <td><a  href="${raDyn.frdPsnUrl!'#'}%26src%3ddiscipline_box" target="_blank"  style="text-decoration:none"><span style=" color:#999; text-decoration:none;">${raDyn.keyword}</span></a></td>
                  </tr>
                  <tr style=" padding-top:0; padding-bottom:0; line-height:100%;">
                    <td colspan="2" valign="bottom"><span style=" padding:0; margin:0; width:100%; border-bottom:1px dashed #eaeaea; display:inline-block; height:2px; margin-bottom:7px;"></span></td>
                  </tr>
                   </#if>
                  </#list>
                  
                    <#if (raDynCount?number >3)>
                  <tr style=" padding-top:0; padding-bottom:0; line-height:100%;">
                    <td colspan="2" align="left" valign="top"><a href="${psnUrl!'#'}" target="_blank"  style="color:#999; text-decoration:none; font-size:12px;">>>查看更多</a></td>
                  </tr>
                   </#if>
                </table>
            </td>
          </tr>
          <tr>
            <td height="15"></td>
          </tr>
          </#if>          
          
          
          
          <#if (pagePsnName0?exists)|| (pagePsnName1?exists)||(pagePsnName2?exists)>
          <tr>
            <td style="font-size:14px; font-weight:bold; line-height:150%;">个人主页更新：<a href="${psnUrl!'#'}"  target="_blank" style=" color:#55b1f5; text-decoration:none;">${pageDynCount!''}</a>位好友更新了个人主页</td>
          </tr>
          
          <tr>
            <td height="8"></td>
          </tr>
          <tr>
            <td>
                <table border="0" cellspacing="0" cellpadding="0" style=" width:100%; font-size:14px; color:#333333; background:#fff; padding:20px; border:1px solid #eaeaea; line-height:150%;">
                 <#if (pagePsnName0?exists)>
                 <tr style="margin-bottom:0; padding-bottom:0;">
                    <td width="13%"><a href="${oFrdUrl0!'#'}" target="_blank" style=" border:none; padding:0; margin:0; float:left; margin-right:20px; text-decoration:none;"><img src="${psnAvatars0}" width="50" height="50"style="border:none;"><br><span style="width:50px; color:#55b1f5; display:block; text-align:center;">${pagePsnName0}</span></a></td>
                    <td>
                    <span style=" color:#999; text-decoration:none; margin-right:10px;">
                    <#assign resType0=pageResType0?number/>
                <#if (resType0==19)>
              		更新工作经历
                <#else>
              		更新教育经历
                </#if>
                </span>
                <a href="${oFrdUrl0!'#'}" target="_blank"   style=" margin:0; padding:0; color:#666; text-decoration:none; font-weight:bold;">${oName0!'#'}</a>
                    </td>
                  </tr>
                  </#if>
                  
                  <#if (pagePsnName1?exists)>
                  <tr style=" padding-top:0; padding-bottom:0; line-height:100%;">
                    <td colspan="2" valign="bottom"><span style=" padding:0; margin:0; width:100%; border-bottom:1px dashed #eaeaea; display:inline-block; height:2px; margin-bottom:7px;"></span>
                    </td>
                  </tr> 
                 <tr style="margin-bottom:0; padding-bottom:0;">
                    <td width="13%"><a href="${oFrdUrl1!'#'}"  target="_blank" style=" border:none; padding:0; margin:0; float:left; margin-right:20px; text-decoration:none;"><img src="${psnAvatars1}" width="50" height="50"style="border:none;"><br><span style="width:50px; color:#55b1f5; display:block; text-align:center;">${pagePsnName1}</span></a></td>
                    <td>
                    <span style=" color:#999; text-decoration:none; margin-right:10px;">
                    <#assign resType1=pageResType1?number/>
                <#if (resType1==19)>
              		更新工作经历
                <#else>
              		更新教育经历
                </#if>
                </span>
                <a href="${oFrdUrl0!'#'}" target="_blank"   style=" margin:0; padding:0; color:#666; text-decoration:none; font-weight:bold;">${oName1!'#'}</a>
                    </td>
                  </tr>
                  </#if>
                  
                   <#if (pagePsnName2?exists)>
                  <tr style=" padding-top:0; padding-bottom:0; line-height:100%;">
                    <td colspan="2" valign="bottom"><span style=" padding:0; margin:0; width:100%; border-bottom:1px dashed #eaeaea; display:inline-block; height:2px; margin-bottom:7px;"></span>
                    </td>
                  </tr> 
                 <tr style="margin-bottom:0; padding-bottom:0;">
                    <td width="13%"><a href="${oFrdUrl2!'#'}"  target="_blank" style=" border:none; padding:0; margin:0; float:left; margin-right:20px; text-decoration:none;"><img src="${psnAvatars2}" width="50" height="50"style="border:none;"><br><span style="width:50px; color:#55b1f5; display:block; text-align:center;">${pagePsnName2}</span></a></td>
                    <td>
                    <span style=" color:#999; text-decoration:none; margin-right:10px;">
                    <#assign resType2=pageResType2?number/>
                <#if (resType2==19)>
              		更新工作经历
                <#else>
              		更新教育经历
                </#if>
                </span>
                <a href="${oFrdUrl0!'#'}" target="_blank"   style=" margin:0; padding:0; color:#666; text-decoration:none; font-weight:bold;">${oName2!'#'}</a>
                    </td>
                  </tr>
                  </#if>
                   
                   
                   <#if (pageDynCount?number>3)>
                  <tr style=" padding-top:0; padding-bottom:0; line-height:100%;">
                    <td colspan="2" align="left" valign="top"><a href="${psnUrl!'#'}"  target="_blank"  style="color:#999; text-decoration:none; font-size:12px;">>>查看更多</a></td>
                  </tr>
                   </#if>
                </table>
            </td>
          </tr>
          </#if>
          
          
          
          
          
          <tr>
            <td valign="bottom" style=" height:50px; font-size:14px; font-weight:bold;">
            <a href="${psnUrl!'#'}" target="_blank" style=" width:100%; height:30px; text-align:center; margin:0; margin-right:10px; line-height:30px; color:#fff; text-decoration:none; background:#55b1f5; border:1px solid #49a0e0; display:inline-block;">查看全部好友动态</a></td>
          </tr>
          <tr>
            <td valign="bottom" style=" height:30px; font-size:14px; text-align:center; color:#999999; font-style:italic;">确认并分享成果，提高论文引用</td>
          </tr>
        </table>
	</td>
  </tr>
</table>
 <#include "scm_base_foot_zh_CN.ftl" encoding="UTF-8">               	
</body>
</html>
