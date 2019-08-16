<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="page border-top">
  <!-------------------------------- 隐藏数据start------------------------------------------------------- -->
  <input type="hidden" name="page.order" id="order" value="${page.order}" /> <input type="hidden" name="page.orderBy"
    id="orderBy" value="${page.orderBy}" /> <input type="hidden" name="page.pageNo" id="pageNo" value="${page.pageNo}" />
  <input type="hidden" name="page.pageSize" id="pageSize" value="${page.pageSize}" /> <input type="hidden"
    name="page.pageCount" id="pageCount" value="${page.pageCount}" /> <input type="hidden" name="page.totalPages"
    id="totalPages" value="${page.totalPages}" /> <input type="hidden" name="page.totalCount" id="totalCount"
    value="${page.totalCount}" />
  <!-------------------------------- 隐藏数据end------------------------------------------------------- -->
  <div class="fr ml20">
    <span class="f333 fl">到第</span>
    <div id="divselect01" class="divselect fl">
      <cite>${page.pageNo}</cite>
      <ul id="divselect01ul">
        <!-------------------------------- 下拉框选择页数 ------------------------------->
      </ul>
    </div>
    <span class="f333 fl">页</span>
  </div>
  <div class="page_cont fr" id="pageModule">
    <a href="javascript:;" class="page_grey" id="lastPage">上一页</a>
    <!-------------------------------- 列表选择页数 ---hover---------------------------->
    <a href="javascript:;" id="nextPage">下一页</a>
  </div>
  <div class="fl">
    <span class="f333 fl">每页显示</span>
    <div id="divselect" class="divselect fl">
      <cite>${page.pageSize}</cite>
      <ul id="divselectul">
        <!-------------------------------- 选择每页数量 ------------------------------->
      </ul>
    </div>
    <span class="f333 fl">条</span>
  </div>
</div>