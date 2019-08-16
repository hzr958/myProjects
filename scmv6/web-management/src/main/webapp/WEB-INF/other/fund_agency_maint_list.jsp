<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s:if test="page.result.size() > 0">
  <!--star-->
  <div class="table_header">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="t_css">
      <tr class="t_tr">
        <td width="5%" align="center"><input onclick="agencyMaint.checkAll(this);" value="" type="checkbox" id="checkAll" name="checkAll" class="checkAll"/></td>
        <td width="10%" align="center">&nbsp;</td>
        <td align="left">资助机构</td>
        <td width="25%" align="center">地区信息</td>
        <td width="25%" align="center">URL</td>
      </tr>
    </table>
  </div>
  <div class="main-column">
    <table width="100%" border="0" cellspacing="0" cellpadding="1" class="t_css">
      <s:iterator value="page.result" var="item">
        <tr class="line_1">
           <td width="5%" align="center"><%-- <input onclick="agencyMaint.check(this);" value="${item.id}" type="radio" name="fundcheck" /> --%>
          <input value="${item.id}" type="checkbox" name="fundcheck" class="fundcheck" onclick="agencyMaint.check(this);"/>
          </td>
          <td width="10%" align="center"><c:choose>
              <c:when test="${empty item.logoUrl }">
                <img src="${resmod}/images_v5/fund_logo/default_logo.jpg" width="50" height="50" />
              </c:when>
              <c:when test="${fn:contains(item.logoUrl,'http')}">
                <img src="${item.logoUrl}" width="50" height="50" />
              </c:when>
              <c:otherwise>
                <img src="${resmod}/${item.logoUrl}" width="50" height="50" />
              </c:otherwise>
            </c:choose></td>
          <td align="left">
          <a target="blank" class="Blue" onclick="agencyMaint.editAgency(this);" agencyid="${item.id}">${item.nameView}</a>
          </td>
          <td width="25%" align="center"><p>${item.regionName}</p></td>
          <td width="25%" align="center" style="word-break:break-all"><a href="${item.url}" class="Blue" >${item.url}</a></td>
        </tr>
      </s:iterator>
    </table>
  </div>
  <%@ include file="/common/page-ajax.jsp"%>
  <!--end-->
</s:if>
<s:else>
  <div id="con_one_page">
    <div style="width: 694px;" class="confirm_words ">
      <i class="icon_prop"></i><span class="cuti">没有找到符合条件的数据。</span>
    </div>
  </div>
</s:else>
