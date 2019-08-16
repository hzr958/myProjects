<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<input value="  " type="hidden" class="hidden_data" count=${page.totalCount } page=${page.pageNo }
  totalpages=${page.totalPages } currentCount="${page.pageNo }">
<s:if test="thirdSourcesFundlist.size() > 0">
  <!--star-->
  <table width="100%" border="0" cellspacing="0" cellpadding="0" class="tr_box mtop10">
  <thead>
  <tr>
    <td width="40" align="center"><input onclick="fundThirdMaint.checkAll(this);" value="" type="checkbox" id="checkAll" name="checkAll" class="checkAll"/></td>
    <td width="150">基金机会名称</td>
    <td>资助机构</td>
    <td>更新时间</td>
    <td width="150">开始时间&nbsp;-&nbsp;结束时间</td>
  </tr>
  </thead>
  <s:iterator value="thirdSourcesFundlist" var="fund">
  <tr>
    <td align="center"><input onclick="fundThirdMaint.check(this);" value="${fund.id}" type="checkbox" name="fundcheck" class="fundcheck"/></td>
    <td align="left">
   	 <a onclick="fundThirdMaint.enterCheck('${fund.id }')" target="blank" class="Blue"  >${fund.fundTitleCn}</a>
    </td>
    <td align="left"><c:if test="${empty fund.agencyViewName }">${fund.fundingAgency }</c:if><c:if test="${not empty fund.agencyViewName }">${fund.agencyViewName }</c:if></td>
     <td align="center"><s:date name="#fund.updateTime" format="yyyy-MM-dd HH:mm:ss"/></td>
    <td align="center"><s:date name="#fund.applyDateStart" format="yyyy/MM/dd"/>-<s:date name="#fund.applyDateEnd" format="yyyy/MM/dd"/></td>
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
  