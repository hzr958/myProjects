
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s:if test="page.totalCount > 0">
  <s:iterator value="page.result" id="obj" status="itStat" var="data">
    <div class="message_ask message_list">
      <span class="check_fx"> <input type="checkbox" class="ipt-hide"> <label class="checkbox checklist">
      </label>
      </span> <a class="message_ask_portrait" target="_blank"
        href="${domainscm }/psnweb/homepage/show?des3PsnId=${data.des3PsnId}"> <c:if test="${!empty data.avatars}">
          <img class="sie_list_potytait" src="${data.avatars}" alt="">
        </c:if> <c:if test="${empty data.avatars}">
          <img class="sie_list_potytait" src="${ressie}/images/head_nan_photo.jpg" alt="">
        </c:if>
      </a>
      <div class="message_big">
        <p class=" fg">
          <a target="_blank" title="${data.zhName}"
            href="${domainscm }/psnweb/homepage/show?des3PsnId=${data.des3PsnId}" class="message_data sie_lsit_span2">${data.zhName}</a>
          <span class="sie_lsit_span2" title="${data.position}">&nbsp;${data.position}</span>
        </p>
        <p class="monicker">
          <b><span>${data.prjSum}</span><span>${data.pubSum}</span><span>${data.ptSum}</span></b> <b> <c:if
              test="${!empty  data.unitName}">
              <span class="sie_lsit_span1" title="${data.unitName}">${data.unitName}</span>
            </c:if> <c:if test="${!empty  data.disciName}">
              <span class="sie_lsit_span1" title="${data.disciName}">${data.disciName}</span>
            </c:if>
          </b>
        </p>
        <p class="f999">
          <c:if test="${!empty  data.email}">
            <span class="postbox4">${data.email}</span>
          </c:if>
          <c:if test="${!empty  data.tel}">
            <span class="postbox5">${data.tel}</span>
          </c:if>
        </p>
        <p class="evaluate">
          <a href="#" class="assist_4"><i></i>编辑</a> <a href="#" class="assist_5"><i></i>删除</a> <a href="#"
            class="assist_10"><i></i>设置</a>
        </p>
      </div>
    </div>
  </s:iterator>
</s:if>
<s:else>
  <div class="confirm_words_bt">没有符合条件的记录，请修改您的检索条件后重新检索。</div>
</s:else>
<%@include file="/common/page-tags.jsp"%>