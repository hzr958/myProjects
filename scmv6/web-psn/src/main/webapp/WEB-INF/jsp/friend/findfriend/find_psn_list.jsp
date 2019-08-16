<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<input type="hidden" name="totalpage" id="totalPageCount" value="${page.totalPages }" />
<c:if test="${!empty page.result && page.result.size() > 0}">
  <c:forEach items="${page.result}" var="psn">
    <div class="main-list__item">
      <div class="main-list__item_content">
        <div class="psn-idx_medium-new">
          <div class="psn-idx__base-info">
            <div class="psn-idx__avatar_box">
              <div class="psn-idx__avatar_img">
                <a href="${psn.psnShortUrl}" target="_blank"><img src="${psn.avatars }"
                  onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'"></a>
              </div>
            </div>
            <div class="psn-idx__main_box">
              <div class="psn-idx__main">
                <div class="psn-idx__main_title-info">
                  <a href="${psn.psnShortUrl}" target="_blank" class="psn-idx__main_name">${psn.name }</a>
                </div>
                <div class="psn-idx__main_ins-info">
                  <span class="psn-idx__main_ins">${psn.insName }</span>
                  <c:if test="${!empty psn.insName && !empty psn.department}">, </c:if>
                  <span class="psn-idx__main_dept">${psn.department }</span>
                  <c:if test="${(!empty psn.insName || !empty psn.department) && !empty psn.position}">, </c:if>
                  <span class="psn-idx__main_title">${psn.position }</span>
                  <c:if test="${(!empty psn.insName || !empty psn.department || !empty psn.position) && !empty psn.titolo}">, </c:if>
                  <span class="psn-idx__main_title">${psn.titolo }</span>
                </div>
                <c:if
                  test="${!empty psn.discList && psn.discList.size() > 0 || !empty psn.scienceList && psn.scienceList.size() > 0}">
                  <div class="psn-idx__main_research-area">
                    <s:text name="psn.find.friend.item.science.area" />
                    <span class="psn-idx__main_area-list"> <c:forEach items="${psn.scienceList}" var="keyword"
                        varStatus="st">
                        <span class="psn-idx__main_area-item">${keyword.scienceArea}</span>
                        <c:if test="${!st.last}">; </c:if>
                      </c:forEach> 
                       </span>
                  </div>
                      <c:if test="${!empty psn.discList&&!empty psn.scienceList}">
                       <div class="psn-idx__main_research-area">
                        <s:text name="psn.find.friend.item.keyword" />
                            <span class="psn-idx__main_area-list">
                           <c:forEach
                            items="${psn.discList}" var="keyword" varStatus="st">
                            <span class="psn-idx__main_area-item">${keyword.keyWords }</span>
                            <c:if test="${!st.last}">; </c:if>
                          </c:forEach>
                       </span>
                  </div>
                      </c:if>
                </c:if>
                <div class="psn-idx__main_stats">
                  <span class="psn-idx__main_stats-item"><s:text name="psn.find.friend.item.prj.sum" /> <span
                    class="psn-idx__main_stats-num">${psn.prjSum }</span></span> <span class="psn-idx__main_stats-item"><s:text
                      name="psn.find.friend.item.pub.sum" /> <span class="psn-idx__main_stats-num">${psn.pubSum }</span></span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="main-list__item_actions">
        <c:if test="${psn.needButton }">
          <button class="button_main button_primary" id="addFriendBtn"
            onclick="findPsn.addOneFriend('','${psn.des3PsnId}',this)">
            <s:text name="psn.find.friend.item.addfriend" />
          </button>
        </c:if>
      </div>
    </div>
  </c:forEach>
  <div id="findNoPerson"></div>
</c:if>