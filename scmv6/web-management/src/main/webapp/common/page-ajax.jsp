<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true"%>
<c:if test="${page.totalCount ne -1}">
  <div class="web-Page">
    <input type="hidden" name="page.order" id="order" value="${page.order}" /> <input type="hidden" name="page.orderBy"
      id="orderBy" value="${page.orderBy}" /> <input type="hidden" name="page.pageNo" id="pageNo"
      value="${page.pageNo}" />
    <div class="page-number">
      <s:text name='page.totalPages'>
        <s:param>${page.totalPages}</s:param>
      </s:text>
      /${page.totalCount}
      <s:text name='page.findSize' />
      <span id="page_tages_size_span">每页显示</span> &nbsp;
      <s:select list="#{10:10,20:20,50:50}" name="page.pageSize" id="pageSize" listKey="key" listValue="value"
        value="page.pageSize" onchange="loadOtherPage();" />
      &nbsp;条
    </div>
    <div class="Jump-page">
      <s:if test="page.isHasPre()">
        <a class="Blue" onclick="loadOtherPage('${page.pageNo-1}');"><s:text name='page.prePage' /></a>
      </s:if>
      <s:else>
        <s:text name='page.prePage' />
      </s:else>
      <s:if test="page.pageNo<=page.listNum/2+1">
        <c:forEach begin="1" end="${page.totalPages<=page.listNum?page.totalPages:page.listNum}" var="tmp">
          <a class="${tmp eq page.pageNo?'Blue b':''}" onclick="loadOtherPage('${tmp}');">${tmp}</a>
        </c:forEach>
      </s:if>
      <s:elseif test="page.totalPages-page.pageNo>page.listNum/2">
        <c:forEach begin="${page.pageNo-(page.listNum-page.listNum%2)/2}"
          end="${page.pageNo+(page.listNum-page.listNum%2)/2}" var="tmp">
          <a class="${tmp eq page.pageNo?'Blue b':''}" onclick="loadOtherPage('${tmp}');">${tmp}</a>
        </c:forEach>
      </s:elseif>
      <s:else>
        <c:forEach begin="${(page.totalPages-page.listNum+1)<=0?1:(page.totalPages-page.listNum+1)}"
          end="${page.totalPages}" var="tmp">
          <a class="${tmp eq page.pageNo?'Blue b':''}" onclick="loadOtherPage('${tmp}');">${tmp}</a>
        </c:forEach>
      </s:else>
      <s:if test="page.isHasNext()">
        <a class="Blue" onclick="loadOtherPage('${page.pageNo+1}');">下一页</a>
      </s:if>
      <s:else>
			下一页
		</s:else>
      <s:text name='page.to' />
      &nbsp;
      <s:textfield cssStyle="width:20px" id="toPage" maxlength="5" />
      &nbsp;页 <a class="uiButton uiButtonConfirm" onclick="topage();" title="确定">确定</a>
    </div>
  </div>
</c:if>