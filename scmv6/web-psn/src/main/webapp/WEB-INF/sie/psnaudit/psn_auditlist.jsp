<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s:if test="page.totalCount > 0">
  <s:iterator value="page.result" id="obj" status="itStat" var="data">
    <div class="message_ask message_list">
      <div class="message_list_check">
        <span class="check_fx"> <input type="checkbox" class="ipt-hide" name="psnId" value="${data.pk.psnId}">
          <label class="checkbox checklist"> </label>
        </span> <input type="hidden" value="${data.pk.psnId}" name="psnId" />
        <s:if test="avatars==null">
          <a class="message_ask_portrait" target="_blank"
            href="${domainscm }/psnweb/homepage/show?des3PsnId=${data.des3PsnId}"><img class="sie_list_potytait"
            src="${ressie}/images/logo_psndefault.png" alt=""></a>
        </s:if>
        <s:else>
          <a class="message_ask_portrait" target="_blank"
            href="${domainscm }/psnweb/homepage/show?des3PsnId=${data.des3PsnId}"><img class="sie_list_potytait"
            src="${avatars}" alt=""></a>
        </s:else>
      </div>
      <div class="message_big">
        <p class=" fg sie_df">
          <b class="monicker_bt"><span class="monicker_lt" style="display: inline-block;"> <font color="red"
              style="font-size: 15px; font-weight: bold;"><c:if test="${data.status==0}">待审核</c:if> <c:if
                  test="${data.status==1}">已审核</c:if> <c:if test="${data.status==9}">已拒绝</c:if></font>
          </span> </b> <span> <a href="${domainscm }/psnweb/homepage/show?des3PsnId=${data.des3PsnId}" target="_blank"
            title="${data.zhName}" class="message_data">${data.zhName}</a>&nbsp;${data.position}
          </span>
        </p>
        <p class="sie_df">
          <b class="monicker_bt"><span class="monicker_lt" style="display: inline-block;"> <%--                                  <font color="red" style="font-size: 15px;font-weight: bold;"><c:if test="${data.status==0}">待审核</c:if><c:if test="${data.status==1}">已审核</c:if><c:if test="${data.status==9}">已拒绝</c:if></font> --%>
              <!--                                 <br/> --> <s:date name="#data.createDate" format="yyyy-MM-dd" /></span> </b> <b><span
            class="postbox2">${data.email}</span> <c:if test="${!empty data.mobile}">
              <span class="postbox3">${data.mobile}</span>
            </c:if> </b>
        </p>
        <%--                         <p><span class="postbox2">${data.email}</span> <span class="postbox3">${data.mobile}</span></p> --%>
        <!--                         <p class="sie_df">                 -->
        <!--                                 <span class="evaluate"> -->
        <!--                                    <a href="#" class="assist_4"><i></i>编辑</a> -->
        <!--                                 <a href="#" class="assist_5"><i></i>删除</a> -->
        <!--                                 <a href="#" class="assist_10"><i></i>设置</a> -->
        <!--                                 </span> -->
        <!--                         </p> -->
      </div>
    </div>
  </s:iterator>
</s:if>
<s:else>
  <div class="confirm_words_bt">没有符合条件的记录，请修改您的检索条件后重新检索。</div>
</s:else>
<%@include file="/common/page-tags.jsp"%>