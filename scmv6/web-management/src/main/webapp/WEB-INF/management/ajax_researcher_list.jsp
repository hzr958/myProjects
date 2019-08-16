<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$("document").ready(function() {
	   $("#checkEmail").thickbox();
});
</script>
<table id="psn_list" width="100%" border="0" cellspacing="0" cellpadding="1" class="t_css">
  <s:iterator value="psnInfoList" var="psnInfo">
    <tr class="line_1">
      <%-- <td width="3%" align="center"><input type="checkbox"
				name="ck_psnId" class="ck_psnId" value="${psnId}" /> 
				<input type="hidden" value="${psnId}" class="psnId" id="psnId" /></td> --%>
      </td>
      <td width="10%" align="center"><a target="_blank" href="${psnUrl}"
        title="<s:property value='#psnInfo.avatarsUrl'/>"> <img src="<s:property value='#psnInfo.avatarsUrl'/>"
          style="width: 60px; height: 60px;" />
      </a></td>
      <td align="left">
        <p>
          <a target="_blank" href="${psnUrl}" class="Blue mright10"><s:property value='#psnInfo.psnName' />&nbsp;&nbsp;<s:property
              value='#psnInfo.firstName' />&nbsp; <s:property value='#psnInfo.lastName' /></a>
        </p>
        <p>
          <s:property value='#psnInfo.posAndTitolo' />
        </p>
        <p>
          <s:property value='#psnInfo.insAndDep' />
        </p>
      </td>
      <td width="12%" align="center">
        <p>
          <s:property value='#psnInfo.psnId' />
        </p>
      </td>
      <td width="12%" align="center">
        <p>
          <s:property value='#psnInfo.loginName' />
        </p>
        <p>
          <s:property value='#psnInfo.email' />
        </p>
      </td>
      <td width="12%" align="center"><s:if test="#psnInfo.prjSum == null">0</s:if> <s:else>
          <s:property value='#psnInfo.prjSum' />
        </s:else>&nbsp;&nbsp;/&nbsp;&nbsp; <s:if test="#psnInfo.pubSum == null">0</s:if> <s:else>
          <s:property value='#psnInfo.pubSum' />
        </s:else>&nbsp;&nbsp;/&nbsp;&nbsp; <s:if test="#psnInfo.patentSum == null">0</s:if> <s:else>
          <s:property value='#psnInfo.patentSum' />
        </s:else></td>
      <td width="12%" align="center"><s:if test="#psnInfo.isCheckEmail == 1">
          <a style="text-decoration: none;" class="blue"
            href="JavaScript:checkEmail('${psnId}','<s:property value='#psnInfo.email'/>')">查看</a></td>
      </s:if>
      <s:else>无</s:else>
      <input type="hidden" id="checkEmail" class="thickbox" title="<s:property value='#psnInfo.psnName'/>的邮件列表" />
      <td width="12%" align="left">
        <p>
          <a href="JavaScript:ajaxVLoginUrl('${psnId}')" class="Blue">生成链接</a>
        </p>
        <p>
          <a href="#" id="vLoginUrl_${psnId}" target="_blank">代登录</a>
        </p>
      </td>
      <td width="12%" align="center">
        <p>
          <s:property value='#psnInfo.regData' />
        </p>
        <p>
          <s:property value='#psnInfo.lastLoginTime' />
        </p>
      </td>
    </tr>
  </s:iterator>
</table>
<s:include value="/common/pub-page-tages.jsp"></s:include>
