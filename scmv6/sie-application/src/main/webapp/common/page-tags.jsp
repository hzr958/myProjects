<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
    // 定位到mainForm中
    $(document).ready(function(){
        $(".page_cont a").attr("href","#");
    });
</script>
<s:if test="page.totalPages>1 || page.totalCount>page.pageSize">
  <div class="page" style="align-items: center;">
    <input type="hidden" name="page.order" id="order" value="${page.order}" /> <input type="hidden" name="page.orderBy"
      id="orderBy" value="${page.orderBy}" /> <input type="hidden" name="page.pageNo" id="pageNo"
      value="${page.pageNo}" />
    <div class="page_left" style="align-items: center;">
      <a href="###" onclick="page.submit('${page.pageNo}');" class="page_1" style="margin-top: 0px;"><div>刷新</div></a>
      <%--                      <c:if test="${!empty page.showExportFlag}"> --%>
      <!--                          <a href="###" id="hide_derive" class="page_derive" onclick="exportExcel();" style="margin-top: 0px;"><div>导出</div></a> -->
      <%--                      </c:if> --%>
      <span class="mr10">共${page.totalPages}页/${page.totalCount} 条 记录</span>
      <div class="f333" style="display: flex; align-items: center;">
        每页显示
        <s:select list="#{10:10,20:20,50:50,100:100}" class="page_lt" name="page.pageSize" id="pageSize" listKey="key"
          listValue="value" value="page.pageSize" onchange="page.submit()" />
        条
      </div>
    </div>
    <div id="page" class="page_cont" style="align-items: center;">
      <a href="#mainForm" style="cursor: pointer;" onclick="page.submit('1');">首页</a>
      <s:if test="page.isHasPre()">
        <a href="#mainForm" class="sie_page" style="cursor: pointer;" onclick="page.submit('${page.pageNo-1}');">上一页</a>
      </s:if>
      <s:else>
                                                 上一页&nbsp;
                    </s:else>
      <s:if test="page.pageNo<=page.listNum/2+1">
        <c:forEach begin="1" end="${page.totalPages<=page.listNum?page.totalPages:page.listNum}" var="tmp">
          <a class="${tmp eq page.pageNo?'sie_page':''}" style="cursor: pointer;" onclick="page.submit('${tmp}');">${tmp}</a>
        </c:forEach>
      </s:if>
      <s:elseif test="page.totalPages-page.pageNo>page.listNum/2">
        <c:forEach begin="${page.pageNo-(page.listNum-page.listNum%2)/2}"
          end="${page.pageNo+(page.listNum-page.listNum%2)/2}" var="tmp">
          <a class="${tmp eq page.pageNo?'sie_page':''}" style="cursor: pointer;" onclick="page.submit('${tmp}');">${tmp}</a>
        </c:forEach>
      </s:elseif>
      <s:else>
        <c:forEach begin="${(page.totalPages-page.listNum+1)<=0?1:(page.totalPages-page.listNum+1)}"
          end="${page.totalPages}" var="tmp">
          <a class="${tmp eq page.pageNo?'sie_page':''}" style="cursor: pointer;" onclick="page.submit('${tmp}');">${tmp}</a>
        </c:forEach>
      </s:else>
      <s:if test="page.isHasNext()">
        <a href="#mainForm" class="sie_page" style="cursor: pointer;" onclick="page.submit('${page.pageNo+1}');">下一页</a>
      </s:if>
      <s:else>
                                                                下一页&nbsp;
                    </s:else>
      <a href="#mainForm" style="cursor: pointer;" onclick="page.submit('${page.totalPages}');">尾页</a>
      <div class="page_lr  f333 " style="display: flex; align-items: center;">
        <span class="ml20 ">到第</span> &nbsp;
        <s:textfield cssStyle="width:20px;border: none;" id="toPage" maxlength="5" />
        &nbsp;页 <a class="skip sie_page" onclick="page.topage()" style="cursor: pointer;" title="跳转">跳转</a>
      </div>
    </div>
  </div>
</s:if>
<s:elseif test="page.totalCount>0">
  <input type="hidden" name="page.order" id="order" value="${page.order}" />
  <input type="hidden" name="page.orderBy" id="orderBy" value="${page.orderBy}" />
  <input type="hidden" name="page.pageSize" id="pageSize" value="${page.pageSize}" />
  <input type="hidden" name="page.pageNo" id="pageNo" value="${page.pageNo}" />
</s:elseif>
<input type="hidden" name="page.totalCount" id="totalCount" value="${page.totalCount}" />