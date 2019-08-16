<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<dl id="dl_data_size" datasize=${page.totalCount }>
  <s:if test="page.result.size()>0">
    <c:forEach items="${page.result}" var="psn">
      <dd>
        <a href="/psnweb/mobile/outhome?des3ViewPsnId=${psn.des3PsnId }" des3PsnId="${psn.des3PsnId }"
          class="fr_infro psn_info"><img src="${psn.avatars}" id="avatar_${psn.psnId }"
          onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
          <h3>${psn.name}</h3> <c:choose>
            <c:when test="${not empty psn.insName&&not empty psn.position}">
              <p>${psn.insName};${psn.position}</p>
            </c:when>
            <c:when test="${not empty psn.insName&&empty psn.position}">
              <p>${psn.insName}</p>
            </c:when>
            <c:otherwise>
              <p>${psn.position}</p>
            </c:otherwise>
          </c:choose> </a>
      </dd>
    </c:forEach>
  </s:if>
  <s:else>
    <div id="con_one_2" class=" noRecord" style="margin-top: -10px">
      <div class="content">
        <div class="no_effort">
          <h2 class="tc">很抱歉，未找到与检索条件相关结果</h2>
          <div class="no_effort_tip pl27">
            <span>温馨提示： </span>
            <p>检查输入条件是否正确.</p>
            <p>尝试同义词或更通用关键词.</p>
            <p>更换检索类别或过滤条件.</p>
          </div>
        </div>
      </div>
    </div>
  </s:else>
</dl>
