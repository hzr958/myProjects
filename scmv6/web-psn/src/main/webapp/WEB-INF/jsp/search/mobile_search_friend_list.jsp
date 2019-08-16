<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s:if test="page.result.size()>0">
  <c:forEach items="${page.result}" var="psn">
    <div id="add_${psn.psnId}" class="list_item_container psn_info" des3PsnId="${psn.des3PsnId }">
      <div class="list_item_section"></div>
      <div class="list_item_section">
        <div class="person_namecard_whole hasBg">
          <div class="avatar" onclick="open_outhome('${psn.des3PsnId}');">
            <img class="avatar" id="avatar_${psn.psnId }" src="${psn.avatars}"
              onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
          </div>
          <div class="person_information" onclick="open_outhome('${psn.des3PsnId}');">
            <div class="name">
              <c:out value="${psn.name}" escapeXml="false" />
            </div>
            <div class="institution">
              <c:choose>
                <c:when test="${not empty psn.insName && not empty psn.position}">
                  <c:out value="${psn.insName}" escapeXml="false" />,&nbsp;<c:out value="${psn.position}"
                    escapeXml="false" />
                </c:when>
                <c:when test="${not empty psn.insName && empty psn.position}">
                  <c:out value="${psn.insName}" escapeXml="false" />
                </c:when>
                <c:when test="${empty psn.insName && not empty psn.position}">
                  <c:out value="${psn.position}" escapeXml="false" />
                </c:when>
              </c:choose>
            </div>
          </div>
          <div class="operations">
            <div onclick="AddFriend.request('${psn.des3PsnId}','${psn.psnId}');"
              class="btn_normal btn_bg_origin fc_blue500" style="margin-right: 0px;">添加</div>
          </div>
        </div>
      </div>
      <div class="list_item_section"></div>
    </div>
  </c:forEach>
</s:if>
<s:else>
  <div class="list_item_container psn_item_notice">
    <div class="list_item_section"></div>
    <div class="list_item_section">
      <div class="person_namecard_whole hasBg">未查询到相关记录</div>
    </div>
    <div class="list_item_section"></div>
  </div>
  <input id="msg_next_page" type="hidden" value="false" />
</s:else>