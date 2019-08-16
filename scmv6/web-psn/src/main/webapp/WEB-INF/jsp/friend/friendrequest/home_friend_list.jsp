<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count="${page.totalCount}"></div>
<c:forEach items="${psnInfoList}" var="psn" varStatus="stat">
  <div class="main-list__item">
    <div class="main-list__item_content">
      <div class="pub-idx_small">
        <div class="pub-idx__base-info">
          <div class="pub-idx__full-text_box">
            <div>
              <a href="${psn.psnShortUrl }" target="_Blank"> <img class="main-list__list-item__container-avator"
                src="${psn.person.avatars}">
              </a>
            </div>
          </div>
          <div class="pub-idx__main_box">
            <div class="pub-idx__main">
              <div class="pub-idx__main_title">
                <a href="${psn.psnShortUrl }" title='<c:out value="${psn.name }" escapeXml="false" />' target="_Blank">
                  <c:out value="${psn.name }" escapeXml="false" />
                </a>
              </div>
              <div class="pub-idx__main_author">
                <c:out value=" ${psn.person.insName }" escapeXml="false" />
              </div>
              <div class="pub-idx__main_src">
                <c:out value=" ${psn.person.position }" escapeXml="false" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="main-list__item_actions">
      <button class="button_main button_dense button_grey" onclick="Rm.friend_neglet('${psn.des3PsnId }',this)">
        <s:text name="friend.ignore" />
      </button>
      <button class="button_main button_dense button_primary-changestyle"
        onclick="Rm.friend_agree('${psn.des3PsnId }',this)">
        <s:text name="friend.agree" />
      </button>
    </div>
  </div>
</c:forEach>
