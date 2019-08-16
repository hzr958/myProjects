<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!-- 项目经费 -->
<div class="New-proManage_RecordHeader"  style="align-items: center; justify-content: space-between;">
  <span style="color:#333; font-size: 16px;">单位：万元</span>
  <div class="New-proManage_RecordHeader-write" onclick="NewProject.addExpenditure()">
    <i class="material-icons">add</i><span>记一笔</span>
  </div>
  <!--   <div class="New-proManage_RecordHeader-exportBudget">导出预算</div>
  <div class="New-proManage_RecordHeader-export">导出</div> -->
</div>
<!-- 经费科目的每一项 -->
<div class="New-proManage_RecordBody">
  <c:if test="${not empty prjExpens}">
    <div class="New-proManage_RecordBody-Header">
      <div class="New-proManage_RecordBody-HeaderTitle">
        <span>经费科目</span>
      </div>
      <div class="New-proManage_RecordBody-HeaderSupport">资助金额(A)</div>
      <div class="New-proManage_RecordBody-HeaderAlready">已拨金额(B)</div>
      <div class="New-proManage_RecordBody-HeaderUsed">已用金额(C)</div>
      <div class="New-proManage_RecordBody-HeaderAdvance">预支金额(D)</div>
      <div class="New-proManage_RecordBody-HeaderAvailable">可用金额(E=A-C-D)</div>
    </div>
    <!-- 经费科目的每一项 -->
    <c:if test="${not empty prjExpens}">
      <c:forEach items="${prjExpens}" var="expen" varStatus="stat">
        <div class="New-proManage_RecordBody-Item">
          <div class="New-proManage_RecordBody-ItemTitle">
            <span>${expen.expenItem}</span>
          </div>
          <div class="New-proManage_RecordBody-ItemSupport">${expen.schemeAmount}</div>
          <div class="New-proManage_RecordBody-ItemAlready">${expen.allocatedAmount}</div>
          <div class="New-proManage_RecordBody-ItemUsed" onclick="NewProject.loadExpenRecord('${expen.id}',this);">${expen.usedAmount}</div>
          <div class="New-proManage_RecordBody-ItemAdvance" onclick="NewProject.loadExpenRecord('${expen.id}',this);">${expen.advanceAmount}</div>
          <div class="New-proManage_RecordBody-ItemAvailable">${expen.availableAmount}</div>
        </div>
      </c:forEach>
    </c:if>
    <!-- 合计 -->
    <div class="New-proManage_RecordBody-footer">
      <div class="New-proManage_RecordBody-ItemTitle" style="text-align: center;">
        <span>合计</span>
      </div>
      <div class="New-proManage_RecordBody-ItemSupport">${schemeTotal}</div>
      <div class="New-proManage_RecordBody-ItemAlready">${allocatedTotal}</div>
      <div class="New-proManage_RecordBody-ItemUsed">${usedTotal}</div>
      <div class="New-proManage_RecordBody-ItemAdvance">${advanceTotal}</div>
      <div class="New-proManage_RecordBody-ItemAvailable">${availableTotal}</div>
    </div>
  </c:if>
</div>
<c:if test="${empty prjExpens}">
  <div class="main-list__list">
    <div class="response_no-result">未找到符合条件的记录</div>
  </div>
</c:if>
