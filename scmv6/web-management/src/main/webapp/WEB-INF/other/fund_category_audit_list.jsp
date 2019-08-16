<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s:if test="page.result.size() > 0">
  <!--star-->
  <table width="100%" border="0" cellspacing="0" cellpadding="0" class="tr_box mtop10">
  <thead>
  <tr>
    <td width="40" align="center"></td>
    <td width="150">资助机构</td>
    <td>资助类别</td>
    <td width="150">提交单位/时间</td>
  </tr>
  </thead>
  <s:iterator value="page.result" var="item">
  <tr>
    <td align="center"><input value="${item.id}" agencyId="${item.agencyId}" type="radio" name="fundcheck" /></td>
    <td align="left" class="agency_name">${item.agencyViewName}</td>
    <td align="left">${item.categoryViewName }</td>
    <td align="left">(${item.insName})<br/><s:date name="#item.createDate" format="yyyy/MM/dd" /></td>
  </tr>
  </s:iterator>
  </table>
  <div class="main-column">
    	<%@ include file="/common/page-ajax.jsp" %>
  </div>
  <!--end--> 
  </s:if><s:else>
  <div id="con_one_page">
		<div style="width: 694px;" class="confirm_words ">
			<i class="icon_prop"></i><span class="cuti">没有找到符合条件的数据。</span>
		</div>
	</div>
  </s:else>
  