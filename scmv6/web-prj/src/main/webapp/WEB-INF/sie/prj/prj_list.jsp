
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s:if test="page.totalCount > 0">
  <s:iterator value="page.result" id="obj" status="itStat" var="data">
    <div class="message_ask message_list">
      <input type="hidden" value="${data.id}" name="id" />
      <div class="message_big">
        <!--                  <p class=" fg"> -->
        <p class="w550">
          <a href="#" class="data2" title="${data.zhTitle}">${data.zhTitle}，${data.externalNo}</a>
        </p>
        <p class="monicker">
          <b class="monicker_bt"> <span class="monicker_lt"><c:if test="${empty data.pubSum}">0</c:if> <c:if
                test="${!empty data.pubSum}">${data.pubSum}</c:if> </span>
          </b> <b class="ofw mw410">${data.psnName}<c:if test="${!empty data.unitName}">，${data.unitName}</c:if></b>
        </p>
        <p class="f999 mw410">
          <c:if test="${!empty data.prjFromName}">${data.prjFromName}，</c:if>${data.startEndDate}<c:if
            test="${!empty data.amount}">，${data.amount}</c:if>
        </p>
        <p class="evaluate">
          <!--                             <a href="#" class="assist"><i></i> 赞(6)</a> -->
          <!--                             <a href="#" class="assist_2"><i></i>评论(2)</a> -->
          <!--                             <a href="#" class="assist_3"><i></i>分享</a> -->
          <a href="#" class="assist_4"><i></i>编辑</a> <a href="#" class="assist_5"><i></i>删除</a>
          <!--                             <a href="#" class="assist_10"><i></i>引用</a> -->
        </p>
      </div>
    </div>
  </s:iterator>
</s:if>
<s:else>
  <div class="confirm_words_bt">没有符合条件的记录，请修改您的检索条件后重新检索。</div>
</s:else>
<%@include file="/common/page-tags.jsp"%>