<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<input type="hidden" name="page.order" id="order" value="${page.order}" />
<input type="hidden" name="page.orderBy" id="orderBy" value="${page.orderBy}" />
<c:if test="${page.totalPages gt 1 || page.totalCount gt 10}">
  <div class="pop_page mtop10">
    <input type="hidden" name="page.pageNo" id="pageNo" value="${page.pageNo}" />
    <s:if test="page.isHasPre()">
      <a class="Blue" onclick="page.submit('${page.pageNo-1}');"><s:text name='page.prePage' /></a>
    </s:if>
    <s:else>
      <s:text name='page.prePage' />
    </s:else>
    <s:if test="page.pageNo<=page.listNum/2+1">
      <c:forEach begin="1" end="${page.totalPages<=page.listNum?page.totalPages:page.listNum}" var="tmp">
        <a class="${tmp eq page.pageNo?'Blue b':''}" onclick="page.submit('${tmp}');">${tmp}</a>
      </c:forEach>
    </s:if>
    <s:elseif test="page.totalPages-page.pageNo>page.listNum/2">
      <c:forEach begin="${page.pageNo-(page.listNum-page.listNum%2)/2}"
        end="${page.pageNo+(page.listNum-page.listNum%2)/2}" var="tmp">
        <a class="${tmp eq page.pageNo?'Blue b':''}" onclick="page.submit('${tmp}');">${tmp}</a>
      </c:forEach>
    </s:elseif>
    <s:else>
      <c:forEach begin="${(page.totalPages-page.listNum+1)<=0?1:(page.totalPages-page.listNum+1)}"
        end="${page.totalPages}" var="tmp">
        <a class="${tmp eq page.pageNo?'Blue b':''}" onclick="page.submit('${tmp}');">${tmp}</a>
      </c:forEach>
    </s:else>
    <s:if test="page.isHasNext()">
      <a class="Blue" onclick="page.submit('${page.pageNo+1}');"><s:text name='page.nextPage' /></a>
    </s:if>
    <s:else>
      <s:text name='page.nextPage' />
    </s:else>
  </div>
</c:if>