<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:forEach items="${psnInfoList}" var="searchResult" varStatus="pStauts">
  <tr class="line02 search_info_box ">
    <td width="60" valign="top"><a href="${snsctx}/resume/view?des3PsnId=${searchResult.des3PsnId}&des3CpsnId="
      target="_blank"> <img src="${searchResult.avatarUrl}" width="50" height="50"
        onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'" />
    </a></td>
    <td align="left">
      <p></p>
      <div class="Fleft">
        <a href="${snsctx}/resume/view?des3PsnId=${searchResult.des3PsnId}&des3CpsnId=" target="_blank"
          class="Blue cu14"> ${searchResult.name} </a>
      </div> <a href="${snsctx}/resume/view?des3PsnId=${searchResult.des3PsnId}&des3CpsnId=" target="_blank"
      class="uiButton Fright" title="<s:text name='user.search.label.viewdetail'/>" style="padding: 3px 15px;"><s:text
          name="user.search.label.viewdetail" /></a>
      <div class="clear"></div>
      <p>
        <span class="f666">${searchResult.insInfo}</span>
      </p>
      <dl>
        <dd>
          <c:if test="${not empty searchResult.profileUrl  }">
            <a href="${domainsns}/profile/${searchResult.profileUrl }" target="_blank" class="Blue">${domainsns}/profile/${searchResult.profileUrl }</a>
          </c:if>
        </dd>
        <dd>
          <label style="color: #999"><s:text name="user.search.label.hCount" /></label>${searchResult.hindex }&nbsp;&nbsp;&nbsp;&nbsp;<label
            style="color: #999"><s:text name="user.search.label.pubCount" /></label>${searchResult.pubSum }&nbsp;&nbsp;&nbsp;&nbsp;<label
            style="color: #999"><s:text name="user.search.label.citeCount" /></label>${searchResult.citedSum }</dd>
      </dl> <input type="hidden" id="scoreNum" name="scoreNum" value="0">
      <div class="clear"></div>
    </td>
  </tr>
</c:forEach>
<s:if test="psnInfoList.size()>=20">
  <input type="hidden" id="showMore" />
</s:if>