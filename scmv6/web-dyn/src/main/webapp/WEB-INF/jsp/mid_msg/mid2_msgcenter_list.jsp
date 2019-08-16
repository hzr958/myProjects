<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!--0=系统消息、1=请求添加联系人消息、2=成果认领、3=全文认领、4=成果推荐、5=联系人推荐、6=基金推荐、7=站内信、8=请求加入群组消息、9=邀请加入群组消息、10=群组动向 , 11=请求全文消息  -->
<div class="js_listinfo" smate_count="${page.totalCount}" scm_id="chat_content_page"
  scm_totalcount="${page.totalCount }" scm_pageno=${page.pageNo } scm_pagetotalPages=${page.totalPages }>
  <s:iterator value="msgShowInfoList" var="msil" status="st">
    <c:if test="${msil.type==1}">
      <div class="message-page__body-container main-list__item">
        <div class="message-page__body-container__infor">
          <img src="1.JPG" class="message-page__body-container__avator">
          <div class="message-page__body-container__psn">
            <div class="message-page__body-name__box">
              <span class="message-page__body-container__name">陈文</span> <span
                class="message-page__body-container__state">与你成为了联系人</span>
            </div>
            <span class="message-page__body-container__work">爱瑞思软件(深圳)有限公司 副总经理</span> <span
              class="message-page__body-container__time">2017-11-20 15:30</span>
          </div>
        </div>
      </div>
    </c:if>
    <c:if test="${msil.type==2}">
      <div class="message-page__content-item main-list__item">
        <div class="message-page__content-box">
          <span>你有</span> <span class="message-page__content-num">5</span> <span>条成果需要认领</span>
        </div>
      </div>
    </c:if>
    <c:if test="${msil.type==3}">
      <div class="message-page__content-item main-list__item">
        <div class="message-page__content-box">
          <span>你有</span> <span class="message-page__content-num">7</span> <span>条全文需要认领</span>
        </div>
        <i class="material-icons">keyboard_arrow_right</i>
      </div>
    </c:if>
    <c:if test="${msil.type==4}">
    </c:if>
    <c:if test="${msil.type==5}">
    </c:if>
    <c:if test="${msil.type==8}">
    </c:if>
    <c:if test="${msil.type==9}">
    </c:if>
  </s:iterator>