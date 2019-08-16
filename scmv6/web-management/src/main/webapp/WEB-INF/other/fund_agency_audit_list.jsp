<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctx" value="/scmmanagement" />

<s:if test="page.result.size() > 0">
  <!--star-->
  <div class="table_header">
     <table  width="100%" border="0" cellspacing="0" cellpadding="0" class="t_css">
       <tr class="t_tr">
         <td width="5%" align="center">&nbsp;</td>
         <td width="10%" align="center">&nbsp;</td>
         <td align="left">资助机构</td>
         <td width="25%" align="center">提交单位/时间</td>
       </tr>
     </table>
   </div>
  <div class="main-column">
    <table width="100%" border="0" cellspacing="0" cellpadding="1" class="t_css">
    	<s:iterator value="page.result" var="item">
    	<tr class="line_1">
          <td width="5%" align="center"><input value="${item.id}" type="radio" name="fundcheck" /></td>
          <td width="10%" align="center"><img src="${item.logoUrl}" width="50" height="50" /></td>
          <td align="left"><p class="f666">${item.nameView}</p></td>
          <td width="25%" align="center">${item.insName}(<s:date name="#item.createDate" format="yyyy/MM/dd" />)</td>
        </tr>
        </s:iterator>
    </table>
  </div>
    <%@ include file="/common/page-ajax.jsp" %>
  <!--end--> 
  </s:if><s:else>
  <div id="con_one_page">
		<div style="width: 694px;" class="confirm_words ">
			<i class="icon_prop"></i><span class="cuti">没有符合条件的记录。</span>
		</div>
	</div>
  </s:else>
  