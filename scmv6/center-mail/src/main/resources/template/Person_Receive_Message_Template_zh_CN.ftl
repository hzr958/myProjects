<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>好友发了一条站内信给你</title>
</head>

<body>
<table width="620" border="0" align="center" cellpadding="0" cellspacing="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#999999; background:#f7f7f4;">
  <tr>
    <td height="50" align="left" valign="middle" style="background:#666666;"><a href="${domainUrl!''}"  target="_blank"><img src="http://www.scholarmate.com/resscmwebsns/images_v5/scm_email_logo.png"  style="border:none; padding-left:20px;"></a></td>
  </tr>
  <tr>
    <td style="padding:30px;">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#333333;">
          <tr>
            <td style="font-size:14px; font-weight:bold; line-height:150%;">
                <span style="margin:0; padding:0;">${psnName!''}，<span ><a href="${sendPsnUrl!'#'}"  target="_blank"  style=" color:#55b1f5;text-decoration:none; ">${sendPsnName!''}</a></span>给你发了一条站内信</span>
            </td>
          </tr>
          <tr>
            <td>
                <table border="0" cellspacing="0" cellpadding="0" style=" width:100%; font-family:Arial, Helvetica, 'Microsoft YaHei'; font-size:14px; color:#333333; background:#fff; padding:20px; border:1px solid #eaeaea; line-height:150%;">
                <#if type =="text">  
   					    <tbody>
                    <tr style="" align="left" valign="top" > 
                      <td align="left" valign="top"  style="display: block;  float: left;  width: 100%; margin: 10px 0px;">
                        <a href="${pubDetailUrl!'#'}"  target="_blank" style="border:none; padding:0; margin:0; float:left; margin-right:20px;">
                           <img src="${avatars!'#'}" width="64" height="64" align="left" style="margin-right: 12px; border-radius: 50%;">
                        </a>
                        <span style=" margin:0; padding:0; font-weight:bold; display: block;">
                          <a  href="${sendPsnUrl!'#'}" target="_blank" style="text-decoration:none;color:#333333;">${sendPsnName}</a>
                        </span>
                        <span style="color:#999999; margin:0; padding:0;  display: block;">
                          ${content}
                           <a href="${chatUrl!'#'}"  target="_blank"  style=" color:#55b1f5;text-decoration:none; ">[查看消息]</a>
                        </span>
                      </td>
                    </tr> 
                  </tbody>
				        <#elseif type ==  "file">  
    			        <tbody>
                    <tr height="20"></tr>
                    <tr style="" align="left" valign="top"  style="margin: 0px 0px;"> 
                      <td align="left" valign="top"  style=" display: block;  float: left; width: 100%;margin: 18px 0px; ">
                        <a href="${sendPsnUrl!'#'}"  target="_blank" style="border:none; padding:0; margin:0; float:left; margin-right:20px;">
                           <img src="${avatars!'#'}" width="64" height="64" align="left" style="margin-right: 12px; border-radius: 50%;">
                        </a>
                        <span style=" margin:0; padding:0; font-weight:bold;  display: block;">
                          <a href="${sendPsnUrl!'#'}"  target="_blank" style="text-decoration:none;color:#333333;">${sendPsnName}</a>
                        </span>
                        <span style="color:#999999; margin:0; padding:0;  display: block;">
                           给你发送了一个文件
                           <a href="${chatUrl!'#'}"  target="_blank"  style=" color:#55b1f5;text-decoration:none; ">[查看文件]</a>
                        </span>
                      </td>
                     </tr>
                 </tbody>
                  <#elseif type ==  "prj">  
    			        <tbody>
                    <tr height="20"></tr>
                    <tr style="" align="left" valign="top"  style="margin:00px 0px;"> 
                      <td align="left" valign="top"  style="display: block;  float: left;  width: 100%; margin: 18px 0px;">
                        <a href="${sendPsnUrl!'#'}"  target="_blank" style="border:none; padding:0; margin:0; float:left; margin-right:20px;">
                          <img src="${avatars!'#'}" width="64" height="64" align="left" style="margin-right: 12px; border-radius: 50%;">
                        </a>
                        <span style=" margin:0; padding:0; font-weight:bold;  display: block;">
                          <a  href="${sendPsnUrl!'#'}" target="_blank" style="text-decoration:none;color:#333;">${sendPsnName}</a>
                       </span>
                        <span style="color:#999999; margin:0; padding:0;  display: block;">
                          给你发送了一个项目
                          <a href="${chatUrl!'#'}"  target="_blank"  style=" color:#55b1f5;text-decoration:none; ">[查看项目]</a>
                       </span>
                       </td>
                     </tr>
                 </tbody>
                 <#elseif type ==  "fund">  
    			        <tbody>
                    <tr height="20"></tr>
                    <tr style="" align="left" valign="top"  style="margin: 00px 0px;"> 
                      <td align="left" valign="top"  style="display: block;  float: left;  width: 100%; margin: 18px 0px;">
                        <a href="${sendPsnUrl!'#'}"  target="_blank" style="border:none; padding:0; margin:0; float:left; margin-right:20px;">
                          <img src="${avatars!'#'}" width="64" height="64" align="left" style="margin-right: 12px; border-radius: 50%;">
                        </a>
                        <span style=" margin:0; padding:0; font-weight:bold;  display: block;">
                          <a  href="${sendPsnUrl!'#'}" target="_blank" style="text-decoration:none;color:#333;">${sendPsnName}</a>
                       </span>
                        <span style="color:#999999; margin:0; padding:0;  display: block;">
                         给你发送了一个基金机会
                          <a href="${chatUrl!'#'}"  target="_blank"  style=" color:#55b1f5;text-decoration:none; ">[查看基金机会]</a>
                       </span>
                      </td>
                     </tr>
                 </tbody>
                <#elseif type ==  "news">
                  <tbody>
                  <tr height="20"></tr>
                  <tr style="" align="left" valign="top"  style="margin: 00px 0px;">
                    <td align="left" valign="top"  style="display: block;  float: left;  width: 100%; margin: 18px 0px;">
                      <a href="${sendPsnUrl!'#'}"  target="_blank" style="border:none; padding:0; margin:0; float:left; margin-right:20px;">
                        <img src="${avatars!'#'}" width="64" height="64" align="left" style="margin-right: 12px; border-radius: 50%;">
                      </a>
                      <span style=" margin:0; padding:0; font-weight:bold;  display: block;">
                          <a  href="${sendPsnUrl!'#'}" target="_blank" style="text-decoration:none;color:#333;">${sendPsnName}</a>
                       </span>
                      <span style="color:#999999; margin:0; padding:0;  display: block;">
                         给你发送了一条新闻
                          <a href="${chatUrl!'#'}"  target="_blank"  style=" color:#55b1f5;text-decoration:none; ">[查看新闻]</a>
                       </span>
                    </td>
                  </tr>
                  </tbody>
                 <#elseif type ==  "fulltext">  
    			        <tbody>
                    <tr height="20"></tr>
                    <tr style="" align="left" valign="top"  style="margin: 0px 0px;"> 
                      <td align="left" valign="top"  style="display: block;  float: left;  width: 100%; margin: 18px 0px;">
                        <a href="${sendPsnUrl!'#'}"  target="_blank" style="border:none; padding:0; margin:0; float:left; margin-right:20px;">
                          <img src="${avatars!'#'}" width="64" height="64" align="left" style="margin-right: 12px; border-radius: 50%;">
                        </a>
                        <span style=" margin:0; padding:0; font-weight:bold;  display: block;">
                          <a  href="${sendPsnUrl!'#'}" target="_blank" style="text-decoration:none;color:#333;">${sendPsnName}</a>
                       </span>
                        <span style="color:#999999; margin:0; padding:0;  display: block;">
                         给你发送了一个全文
                          <a href="${chatUrl!'#'}"  target="_blank"  style=" color:#55b1f5;text-decoration:none; ">[查看全文]</a>
                       </span>
                      </td>
                     </tr>
                 </tbody>
                 <#elseif type ==  "agency">  
    			        <tbody>
                    <tr height="20"></tr>
                    <tr style="" align="left" valign="top"  style="margin: 0px 0px;"> 
                      <td align="left" valign="top"  style="display: block;  float: left;  width: 100%; margin: 18px 0px;">
                        <a href="${sendPsnUrl!'#'}"  target="_blank" style="border:none; padding:0; margin:0; float:left; margin-right:20px;">
                          <img src="${avatars!'#'}" width="64" height="64" align="left" style="margin-right: 12px; border-radius: 50%;">
                        </a>
                        <span style=" margin:0; padding:0; font-weight:bold;  display: block;">
                          <a  href="${sendPsnUrl!'#'}" target="_blank" style="text-decoration:none;color:#333;">${sendPsnName}</a>
                       </span>
                        <span style="color:#999999; margin:0; padding:0;  display: block;">
                         给你发送了一个资助机构
                          <a href="${chatUrl!'#'}"  target="_blank"  style=" color:#55b1f5;text-decoration:none; ">[查看资助机构]</a>
                       </span>
                      </td>
                     </tr>
                 </tbody>
				       <#else>  
                  <tbody>
                    <tr height="20"></tr>
                     <tr style="" align="left" valign="top"  style="margin: 0px 0px;"> 
                     <td align="left" valign="top"  style="display: block;  float: left;  width: 100%; margin: 18px 0px;">
                        <a href="${sendPsnUrl!'#'}"  target="_blank" style="border:none; padding:0; margin:0; float:left; margin-right:20px;">
                          <img src="${avatars!'#'}" width="64" height="64" align="left" style="margin-right: 12px; border-radius: 50%;">
                        </a>
                        <span style=" margin:0; padding:0; font-weight:bold;  display: block;">
                          <a  href="${sendPsnUrl!'#'}" target="_blank" style="text-decoration:none;color:#333;">${sendPsnName}</a>
                       </span>
                        <span style="color:#999999; margin:0; padding:0;  display: block;">
                         给你发送了一个论文
                          <a href="${chatUrl!'#'}"  target="_blank"  style=" color:#55b1f5;text-decoration:none; ">[查看论文]</a>
                       </span>
                      </td>
                     </tr> 
                  </tbody>
				     </#if> 
                  <#if listMsg?? && (listMsg?size>0)> 
                  <tbody>
                      <tr style="" align="left"> 
                       <td align="left" style="padding-bottom: 4px; padding-top: 20px; border-bottom: 1px solid #e9e9e9;  display:block; width: 100%;">
                        <span style=" height:26px; padding:0 20px; margin:0; margin-right:10px; line-height:26px; color:#999; text-decoration:none; display:inline-block;">历史对话</span>
                       </td>
                      </tr> 
                 </tbody>
                  <#list listMsg as msgEmail>   
		                 <#if msgEmail.type =="text">  
		   					 <tbody>
                  <tr height="25"></tr>
		                <tr style="" align="left" valign="top"  style="margin: 0px 0px;">                     
                      <td align="left" valign="top"  style=" display: block;  float: left;  width: 100%; margin: 20px 0px;">
                        <a href="${msgEmail.sendPsnUrl!'#'}"  target="_blank" style="border:none; padding:0; margin:0; float:left; margin-right:20px;">
                          <img src="${msgEmail.avatars!'#'}" width="64" height="64" align="left" style="margin-right: 12px; border-radius: 50%;">
                        </a>
                        <span style=" margin:0; padding:0; font-weight:bold;  display: block;">
                          <a href="${msgEmail.sendPsnUrl!'#'}"  target="_blank" style="text-decoration:none;color:#333;">${msgEmail.sendPsnName}</a>
                       </span>
                        <span style="color:#999999; margin:0; padding:0;  display: block;">
                         ${msgEmail.content}
                          <a href="${chatUrl!'#'}"  target="_blank"  style=" color:#55b1f5;text-decoration:none; ">[查看消息]</a>
                       </span>
                      </td>
		                </tr>
		               </tbody>
						<#elseif msgEmail.type =="file">  
		    			   <tbody>
                  <tr height="25"></tr>
		                <tr style="" align="left"  valign="top"  style="margin: 0px 0px;"> 
                      <td align="left" valign="top"  style="display: block;  float: left;  width: 100%; margin: 20px 0px;">
                        <a href="${msgEmail.sendPsnUrl!'#'}"  target="_blank" style="border:none; padding:0; margin:0; float:left; margin-right:20px;">
                          <img src="${msgEmail.avatars!'#'}" width="64" height="64" align="left" style="margin-right: 12px; border-radius: 50%;">
                        </a>
                        <span style=" margin:0; padding:0; font-weight:bold;  display: block;">
                          <a  href="${msgEmail.sendPsnUrl!'#'}" target="_blank" style="text-decoration:none;color:#333;">${msgEmail.sendPsnName}</a>
                       </span>
                        <span style="color:#999999; margin:0; padding:0;  display: block;">
                         给你发送了一个文件
                          <a href="${chatUrl!'#'}"  target="_blank"  style=" color:#55b1f5;text-decoration:none; ">[查看文件]</a>
                       </span>
                      </td>
		                  </tr> 
		                 </tbody>
                         <#elseif msgEmail.type =="news">
                           <tbody>
                           <tr height="25"></tr>
                           <tr style="" align="left"  valign="top"  style="margin: 0px 0px;">
                             <td align="left" valign="top"  style="display: block;  float: left;  width: 100%; margin: 20px 0px;">
                               <a href="${msgEmail.sendPsnUrl!'#'}"  target="_blank" style="border:none; padding:0; margin:0; float:left; margin-right:20px;">
                                 <img src="${msgEmail.avatars!'#'}" width="64" height="64" align="left" style="margin-right: 12px; border-radius: 50%;">
                               </a>
                               <span style=" margin:0; padding:0; font-weight:bold;  display: block;">
                          <a  href="${msgEmail.sendPsnUrl!'#'}" target="_blank" style="text-decoration:none;color:#333;">${msgEmail.sendPsnName}</a>
                       </span>
                               <span style="color:#999999; margin:0; padding:0;  display: block;">
                         给你发送了一条新闻
                          <a href="${chatUrl!'#'}"  target="_blank"  style=" color:#55b1f5;text-decoration:none; ">[查看新闻]</a>
                       </span>
                             </td>
                           </tr>
                           </tbody>
		                 <#elseif msgEmail.type =="prj">  
		    			   <tbody>
                  <tr height="25"></tr>
		                    <tr align="left" valign="top"  style="margin: 0px 0px;"> 
                          <td align="left" valign="top"  style="display: block;  float: left;  width: 100%;margin: 20px 0px;">
                            <a href="${msgEmail.sendPsnUrl!'#'}"  target="_blank" style="border:none; padding:0; margin:0; float:left; margin-right:20px;">
                              <img src="${msgEmail.avatars!'#'}" width="64" height="64" align="left" style="margin-right: 12px; border-radius: 50%;">
                            </a>
                            <span style=" margin:0; padding:0; font-weight:bold;  display: block;">
                              <a  href="${msgEmail.sendPsnUrl!'#'}" target="_blank" style="text-decoration:none;color:#333;">${msgEmail.sendPsnName}</a>
                           </span>
                            <span style="color:#999999; margin:0; padding:0;  display: block;">
                             给你发送了一个项目
                              <a href="${chatUrl!'#'}"  target="_blank"  style=" color:#55b1f5;text-decoration:none; ">[查看项目]</a>
                           </span>
                          </td>
		                     </tr> 
		                 </tbody>
		                  <#elseif msgEmail.type =="fund">  
		    			      <tbody>
                      <tr height="25"></tr>
		                <tr align="left" valign="top"  style="margin: 00px 0px;"> 
                        <td align="left" valign="top"  style="display: block;  float: left;  width: 100%; margin: 20px 0px;">
                          <a href="${msgEmail.sendPsnUrl!'#'}"  target="_blank" style="border:none; padding:0; margin:0; float:left; margin-right:20px;">
                            <img src="${msgEmail.avatars!'#'}" width="64" height="64" align="left" style="margin-right: 12px; border-radius: 50%;">
                          </a>
                          <span style=" margin:0; padding:0; font-weight:bold;  display: block; ">
                            <a  href="${msgEmail.sendPsnUrl!'#'}" target="_blank" style="text-decoration:none;color:#333;">${msgEmail.sendPsnName}</a>
                         </span>
                          <span style="color:#999999; margin:0; padding:0;  display: block;">
                           给你发送了一个基金机会
                            <a href="${chatUrl!'#'}"  target="_blank"  style=" color:#55b1f5;text-decoration:none; ">[查看基金机会]</a>
                         </span>
                        </td>
		                    </tr> 
		                 </tbody>
		                 <#elseif msgEmail.type =="fulltext">  
		    			      <tbody>
                      <tr height="25"></tr>
		                    <tr align="left" valign="top"  style="margin: 00px 0px;"> 
                          <td align="left" valign="top"  style="display: block;  float: left;  width: 100%; margin: 20px 0px;">
                            <a href="${msgEmail.sendPsnUrl!'#'}"  target="_blank" style="border:none; padding:0; margin:0; float:left; margin-right:20px;">
                              <img src="${msgEmail.avatars!'#'}"  width="64" height="64" align="left" style="margin-right: 12px; border-radius: 50%;">
                            </a>
                            <span style=" margin:0; padding:0; font-weight:bold;  display: block;">
                              <a  href="${msgEmail.sendPsnUrl!'#'}"  target="_blank" style="text-decoration:none;color:#333;">${msgEmail.sendPsnName}</a>
                           </span>
                            <span style="color:#999999; margin:0; padding:0;  display: block;">
                            给你发送了一个全文
                              <a href="${chatUrl!'#'}"  target="_blank"  style=" color:#55b1f5;text-decoration:none; ">[查看全文]</a>
                           </span>
                          </td>

		                     </tr> 
		                 </tbody>
		                  <#elseif msgEmail.type =="agency">  
		    			   <tbody>
                  <tr height="25"></tr>
		                    <tr align="left" valign="top"  style="margin: 0px 0px;"> 
                          <td align="left" valign="top"  style="display: block;  float: left;  width: 100%; margin: 20px 0px;">
                            <a href="${msgEmail.sendPsnUrl!'#'}"  target="_blank" style="border:none; padding:0; margin:0; float:left; margin-right:20px;">
                              <img src="${msgEmail.avatars!'#'}" width="64" height="64" align="left" style="margin-right: 12px; border-radius: 50%;">
                            </a>
                            <span style=" margin:0; padding:0; font-weight:bold;  display: block;">
                              <a  href="${msgEmail.sendPsnUrl!'#'}"  target="_blank" style="text-decoration:none;color:#333;">${msgEmail.sendPsnName}</a>
                           </span>
                            <span style="color:#999999; margin:0; padding:0;  display: block;">
                             给你发送了一个资助机构
                              <a href="${chatUrl!'#'}"  target="_blank"  style=" color:#55b1f5;text-decoration:none; ">[查看资助机构]</a>
                           </span>
                          </td>
		                     </tr> 
		                 </tbody>
						<#else>  
		   				  <tbody>
                  <tr height="25"></tr>
		                    <tr align="left" valign="top" style="margin: 0px 0px;"> 
                          <td align="left" valign="top"  style="display: block;  float: left;  width: 100%; margin: 20px 0px;">
                            <a href="${msgEmail.sendPsnUrl!'#'}"  target="_blank" style="border:none; padding:0; margin:0; float:left; margin-right:20px;">
                              <img src="${msgEmail.avatars!'#'}" width="64" height="64" align="left" style="margin-right: 12px; border-radius: 50%;">
                            </a>
                            <span style=" margin:0; padding:0; font-weight:bold;  display: block;">
                              <a href="${msgEmail.sendPsnUrl!'#'}" target="_blank" style="text-decoration:none;color:#333;">${msgEmail.sendPsnName}</a>
                           </span>
                            <span style="color:#999999; margin:0; padding:0;  display: block;">
                             给你发送了一个论文
                              <a href="${chatUrl!'#'}"  target="_blank"  style=" color:#55b1f5;text-decoration:none; ">[查看论文]</a>
                            </span>
                          </td>
		                     </tr>
		                  </tbody>
						</#if> 
		             </#list>
		             </#if>        
                </table>
            </td>
          </tr>


          <tr height="20"></tr>

          <tr style="margin:25px 0 0 0px;">
            <td style="margin:25px 0 0 0px;">
              <span style=" margin:25px 0 0 0px;display: inline;">
                <a href="${chatUrl}"  target="_blank" style=" height:26px; padding:0 20px; margin:0; margin-right:10px; line-height:26px; color:#fff; text-decoration:none; background:#55b1f5; border:1px solid #49a0e0; display:inline-block;  margin-top: 20px;">查看对话</a>
              </span>
              &nbsp;&nbsp;&nbsp;&nbsp;
              <span style=" margin:25px 0 0 0px;display: inline; padding:0 20px;">
                <a href="${chatUrl}"  target="_blank" style=" height:26px; padding:0 20px; margin:0; margin-right:10px; line-height:26px; color:#fff; text-decoration:none; background:#55b1f5; border:1px solid #49a0e0; display:inline-block; margin-top: 20px;">回复消息</a>
              </span>
            </td>
          </tr>
        </table>
	</td>
  </tr>
</table>
 <#include "/scm_base_foot_zh_CN.ftl" encoding= "UTF-8">              	
</body>
</html>
