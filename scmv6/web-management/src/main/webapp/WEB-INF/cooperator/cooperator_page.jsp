<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s:if test="page.result.size()>0">
  <div class="table_header">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="t_css">
      <tbody>
        <tr class="t_tr">
          <td align="left" width="60%">&nbsp;<s:text name="cooperator.label.table.head.cooperator" /></td>
          <td align="center" width="10%">&nbsp;<s:text name="cooperator.label.pubCount" /></td>
          <td align="center" width="10%">&nbsp;<s:text name="cooperator.label.readCount" /></td>
          <td align="center" width="10%">&nbsp;<s:text name="cooperator.label.citeCount" /></td>
          <td align="center" width="10%">&nbsp;<s:text name="cooperator.label.table.head.hindex" /></td>
        </tr>
      </tbody>
    </table>
  </div>
  <div class="main-column">
    <table width="100%" border="0" cellspacing="0" cellpadding="1" class="t_css hanggao">
      <s:iterator value="page.result">
        <tr class="line_1">
          <td width="60" align="center">
            <p>
              <a href="${ctx}/resume/psnView?des3PsnId=${des3PsnId}" target="_blank"><img src="${avatars }"
                width="50" height="50" border="0" /></a>
            </p>
          </td>
          <td align="left">
            <p>
              <a style="margin-left: 15px" href="${ctx}/resume/psnView?des3PsnId=${des3PsnId}" class="Blue"
                target="_blank">${viewName }</a>
            </p>
            <p>
              <span style="margin-left: 15px; color: #666;">${titolo }</span>
            </p>
          </td>
          <td align="center" width="10%">&nbsp;${pubCount }</td>
          <td align="center" width="10%">&nbsp;${readCount }</td>
          <td align="center" width="10%">&nbsp;${citeCount}</td>
          <td align="center" width="10%">&nbsp;30</td>
        </tr>
      </s:iterator>
      <%-- <%@ include file="/common_v5/page/page-ajax.jsp"%> --%>
    </table>
  </div>
  <s:if test="page.pageNo<page.totalPages">
    <div class="clear_h20"></div>
    <div id="next_page_link" style="width: 938px; cursor: pointer;" class="morenews2">
      <img id="nextHtmlTip" src="${res}/images_v5/loading_img.gif" style="vertical-align: middle; display: none;" /> <a
        onclick="CooperatorCmd.loadCooperator(${page.pageNo+1}, '${page.pageSize}');" class="Blue"
        style="cursor: pointer;"><s:text name="sns.click.load.more" /></a>
    </div>
  </s:if>
</s:if>
<s:else>
  <div class="confirm_words " style="width: 694px;">
    <i class="icon_prop"></i><span class="cuti"><s:text name="cooperator.tip.noRecord" /></span>
  </div>
</s:else>