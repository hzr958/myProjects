<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s:if test="page.result.size() > 0">
  <!--star-->
  <table width="100%" border="0" cellspacing="0" cellpadding="0"  style="word-wrap:break-word;word-break:break-all;white-space:normal;" class="tr_box mtop10">
  <thead>
  <tr>
    <td width="40" align="center"><input onclick="categoryMaint.checkAll(this);" value="" type="checkbox" id="checkAll" name="checkAll" class="checkAll"/></td>
    <td width="150">资助机构</td>
    <td width="150">资助类别</td>
    <td width="150">更新时间</td>
    <td width="150">开始时间&nbsp;-&nbsp;截止时间</td>
  </tr>
  </thead>
  <s:iterator value="page.result" var="fund">
  <tr>
    <td align="center"><input onclick="categoryMaint.check(this);" value="${fund.id}" type="checkbox" name="fundcheck" class="fundcheck"/></td>
    <td align="left">
    <s:if test="#fund.agencyUrl == null || #fund.agencyUrl == ''">
    	${fund.agencyViewName}
    </s:if>
    <s:else>
   	 <a href="${fund.agencyUrl}" target="blank" class="Blue"  >${fund.agencyViewName}</a>
    </s:else>
    </td>
    <td align="left"><a target="blank" onclick="categoryMaint.editCategory(this);" fundId="${fund.id}" class="Blue" >${fund.categoryViewName }</a></td>
     <td align="center"><s:date name="#fund.updateDate" format="yyyy-MM-dd HH:mm:ss"/></td>
    <td align="center"><s:date name="#fund.startDate" format="yyyy/MM/dd"/>-<s:date name="#fund.endDate" format="yyyy/MM/dd"/></td>
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
  