<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<dl class="s_field">
  <!-- 跳转到人员列表中去 -->
  <c:forEach items="${keywordIdentificationForm}" var="key" varStatus="stat">
    <c:if test="${stat.count <= 3}">
      <dd onclick="toPsnList(${key.keywordId})">
        <span> <c:if test="${key.count >0 }">
				${key.count}	              
			 </c:if> <c:if test="${key.count <1 }">
			     0
			 </c:if>
        </span>
        <div class="test"
          style="display: inline-block; text-overflow: ellipsis; overflow: hidden; white-space: nowrap; width: 40%; vertical-align: middle">
          ${ key.keyword}</div>
        <ul class="dz_lt1">
          <c:forEach items="${key.friendList}" var="person" varStatus="st">
            <c:if test="${st.count <= 3}">
              <li><img src="${person.avatars}" style="width: 2.5rem; height: 2.5rem"
                onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'"></li>
            </c:if>
          </c:forEach>
        </ul>
      </dd>
    </c:if>
    <c:if test="${stat.count == 4}">
      <dt>
        <a href="javascript:void(0)" onclick="moreKeyword()">查看更多</a>
      </dt>
    </c:if>
  </c:forEach>
</dl>
