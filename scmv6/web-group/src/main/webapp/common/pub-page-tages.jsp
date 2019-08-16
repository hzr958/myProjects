<%@ include file="/common/taglibs.jsp"%>
<s:if test="page.totalPages>1 || page.totalCount>page.pageSize">
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
      <span id="page_tages_size_span"> <s:text name='page.pageShow1' />
      </span> &nbsp;
      <s:select list="#{10:10,20:20,50:50}" name="page.pageSize" id="pageSize" listKey="key" listValue="value"
        value="page.pageSize" onchange="page.submit()" />
      &nbsp;
      <s:text name='page.pageShow2' />
    </div>
    <div class="Jump-page">
      <s:if test="page.isHasPre()">
        <a class="Blue" href="javascript:;" onclick="page.submit('${page.pageNo-1}');"><s:text name='page.prePage' /></a>
      </s:if>
      <s:else>
        <s:text name='page.prePage' />
      </s:else>
      <s:if test="page.pageNo<=page.listNum/2+1">
        <c:forEach begin="1" end="${page.totalPages<=page.listNum?page.totalPages:page.listNum}" var="tmp">
          <a class="${tmp eq page.pageNo?'Blue b':''}" href="javascript:;" onclick="page.submit('${tmp}');">${tmp}</a>
        </c:forEach>
      </s:if>
      <s:elseif test="page.totalPages-page.pageNo>page.listNum/2">
        <c:forEach begin="${page.pageNo-(page.listNum-page.listNum%2)/2}"
          end="${page.pageNo+(page.listNum-page.listNum%2)/2}" var="tmp">
          <a class="${tmp eq page.pageNo?'Blue b':''}" href="javascript:;" onclick="page.submit('${tmp}');">${tmp}</a>
        </c:forEach>
      </s:elseif>
      <s:else>
        <c:forEach begin="${(page.totalPages-page.listNum+1)<=0?1:(page.totalPages-page.listNum+1)}"
          end="${page.totalPages}" var="tmp">
          <a class="${tmp eq page.pageNo?'Blue b':''}" href="javascript:;" onclick="page.submit('${tmp}');">${tmp}</a>
        </c:forEach>
      </s:else>
      <s:if test="page.isHasNext()">
        <a class="Blue" href="javascript:;" onclick="page.submit('${page.pageNo+1}');"><s:text name='page.nextPage' /></a>
      </s:if>
      <s:else>
        <s:text name='page.nextPage' />
      </s:else>
      <s:text name='page.to' />
      &nbsp;
      <s:textfield cssStyle="width:20px" id="toPage" maxlength="5" />
      &nbsp;
      <s:text name='page.pageShow' />
      <a class="uiButton" onclick="page.topage()" title="<s:text name='common.label.confirm'/>" href="javascript:;"><s:text
          name='common.label.confirm' /></a>
    </div>
  </div>
</s:if>
<s:elseif test="page.totalCount>0">
  <input type="hidden" name="page.order" id="order" value="${page.order}" />
  <input type="hidden" name="page.orderBy" id="orderBy" value="${page.orderBy}" />
  <input type="hidden" name="page.pageSize" id="pageSize" value="${page.pageSize}" />
</s:elseif>