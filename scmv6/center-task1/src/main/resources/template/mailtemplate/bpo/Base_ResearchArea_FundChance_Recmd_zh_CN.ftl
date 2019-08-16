<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>根据研究领域的基金机会推荐</title>
</head>

<body>
<#include "/base_header_zh_CN.ftl" encoding= "UTF-8">
<table width="600" border="0" align="center" cellpadding="0" bgcolor="#d0daea" cellspacing="10" style="font-family:Arial, Helvetica, '宋体'; font-size:12px; color:#333;">
  <tr>
    <td bgcolor="#FFFFFF"><table width="580" border="0" align="center" cellpadding="0" bgcolor="#426cad" cellspacing="1">
        <tr>
          <td bgcolor="#FFFFFF"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
              <tr>
                <td height="60" valign="top" bgcolor="#426cad"><table width="94%" border="0" align="center" cellpadding="0" cellspacing="0">
                    <tr>
                      <td height="60" align="left" valign="middle" style="font-size:24px; font-family:Helvetica, Arial, '宋体'; font-weight:bold; color:#FFF;">科研之友</td>
                    </tr>
                  </table></td>
              </tr>
              <tr>
                <td align="center" valign="top"><table width="100%" border="0" cellspacing="20" cellpadding="0" style="font-size:14px;">
                    <tr>
                      <td align="left" valign="top"><table width="100%" border="0" cellpadding="6" cellspacing="0" style="font-size:14px;">
                          <tr style="font-size:14px;">
                            <td style="line-height:25px;"><span style="font-weight:bold;">${receivePsnName}</span>，向您推荐适合您研究领域的<a href="${viewUrl}" style=" text-decoration:none;font-weight:bold; color:#426cad;">${counts !'0'}</a>个基金机会：</td>
                          </tr>
                          <tr>
                            <td align="left" valign="top" bgcolor="#f9f9f9"><table width="100%" border="0" cellspacing="0" cellpadding="5" style="font-size:12px; color:#005eac;">
                                <#setting number_format="#.000">
                                <#assign flag=0> 
                                <#list fundList as fund>
                                <tr>
                                  <td width="33%" align="left"><a href="${viewUrl}" style="text-decoration:none; color:#005eac; font-size:12px;">${fund.fundName!''}</a></td>
                                  <td width="33%" align="left">
	                                  <!--<a href="#" style="text-decoration:none; color:#005eac; font-size:12px;">-->
	                                  ${fund.agencyName!''}
	                                  <!--</a>-->
                                  </td>
                                  <td align="left" style="color:#000;">
	                                             相关性： <#assign sufCo=5-fund.recommendation/>
                                     <#list 1..fund.recommendation as  co >★</#list><#if (sufCo?number != 0)><#list 1..sufCo as  co2 >☆</#list></#if>
	                                  <!--<a href="${viewUrl}" style="text-decoration:none; color:#005eac; font-size:12px;">${fund.recommendation!''}</a>-->
	                                  <#if (fund.conPriorFlag==1)>
	                                   <#assign flag=1>
	                                  <span style="color:#F00; font-weight:bold; font-size:14px;">*</span>
	                                  </#if>
                                  </td>
                                </tr>
                                </#list>
                                <#if (counts?number > 3)>
                                <tr>
                                  <td align="left"><a href="${viewUrl}" style="text-decoration:none;color:#005eac;">……</a></td>
                                  <td align="left">&nbsp;</td>
                                  <td align="left">&nbsp;</td>
                                </tr>
                                </#if>
                                <#if (flag==1)>
                                 <tr>
			                          <td colspan="3" align="left"  width="100%" valign="middle" style="color: #666666;">
			                          				*历史数据显示，国家自然科学基金［地区/青年］科学基金项目的资助率高于面上项目的资助率。
			                          </td>
			                       </tr>
			                       </#if>
                              </table></td>
                          </tr>
                          
                        </table></td>
                    </tr>
                    <tr>
                      <td align="left" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                          <tr>
                            <td width="26%" align="left" valign="middle"><table width="130" border="0" cellpadding="0" cellspacing="1" bgcolor="#6a8bbf">
                                <tr>
                                  <td height="30" align="center" bgcolor="#d6e3f6"  style=" border-top:1px solid #ebf1fb;">
                                  	<a href="${viewUrl}" style="font-size:14px; font-weight:bold; color:#3f68a8; text-align:center; line-height:30px; text-align:center; text-decoration:none;">查看详情</a>
                                  </td>
                                </tr>
                              </table></td>
                          </tr>
                        </table></td>
                    </tr>
                  </table>
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td height="40" align="center" bgcolor="#f3f3f3" style="text-align:center; font-size:14px; color:#999999;">完善个人主页，获取更多未知的基金机会推荐。</td>
                    </tr>
                  </table></td>
              </tr>
            </table></td>
        </tr>
      </table></td>
  </tr>
</table>
<#include "/scm_base_foot_zh_CN.ftl" encoding= "UTF-8">
</body>
</html>
