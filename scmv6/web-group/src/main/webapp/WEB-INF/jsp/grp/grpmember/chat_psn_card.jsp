<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="inbox-chat__namecard">
  <div class="inbox-chat__psn-info">
    <div class="psn-idx_medium-new">
      <div class="psn-idx__base-info">
        <div class="psn-idx__avatar_box">
          <div class="psn-idx__avatar_img">
            <a href="${psnInfoList[0].psnShortUrl }" target="_Blank"> <img src="${psnInfoList[0].person.avatars }">
            </a>
          </div>
        </div>
        <div class="psn-idx__main_box">
          <div class="psn-idx__main">
            <div class="psn-idx__main_title-info">
              <a class="psn-idx__main_name" href="${psnInfoList[0].psnShortUrl }" target="_Blank">
                ${psnInfoList[0].name }</a> <span class="psn-idx__main_title">${psnInfoList[0].person.position}</span>
            </div>
            <div class="psn-idx__main_ins-info">
              <span class="psn-idx__main_ins">${psnInfoList[0].person.insName }</span>
              <c:if test="${!empty psnInfoList[0].person.department }">
                  	, <span class="psn-idx__main_dept">${psnInfoList[0].person.department}</span>
              </c:if>
            </div>
            <c:if test="${!empty psnInfoList[0].psnScienceAreaList}">
              <div class="psn-idx__main_research-area">
                <s:text name='groups.chat.psncard.area' />
                <span class="psn-idx__main_area-list"> <s:iterator value="psnInfoList[0].psnScienceAreaList"
                    var="psnA" status="st">
                    <span class="psn-idx__main_area-item">${psnA.showScienceArea}</span>
                    <c:if test="${fn:length(psnInfoList[0].psnScienceAreaList) ne st.index+1}">
                                         ;
                               </c:if>
                  </s:iterator>
                </span>
              </div>
            </c:if>
            <c:if test="${!empty psnInfoList[0].psnDisciplineKeyList}">
              <div class="psn-idx__main_research-area">
                <s:text name='groups.chat.psncard.keywords' />
                <span class="psn-idx__main_area-list"> <s:iterator value="psnInfoList[0].psnDisciplineKeyList"
                    var="psnD" status="st">
                    <span class="psn-idx__main_area-item">${psnD.keyWords}</span>
                    <c:if test="${fn:length(psnInfoList[0].psnDisciplineKeyList) ne st.index+1}">
                                         ;
                                </c:if>
                  </s:iterator>
                </span>
              </div>
            </c:if>
            <div class="psn-idx__main_stats">
              <span class="psn-idx__main_stats-item"><s:text name='groups.member.pro' />: <span
                class="psn-idx__main_stats-num">${psnInfoList[0].psnStatistics.prjSum}</span></span> <span
                class="psn-idx__main_stats-item"><s:text name='groups.member.pub' />: <span
                class="psn-idx__main_stats-num">${psnInfoList[0].psnStatistics.pubSum}</span></span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="inbox-chat__relation">
    <c:if test="${psnInfoList[0].isFriend == 0}">
      <%-- <div class="inbox-chat__relation_text" title="${psnInfoList[0].name }" ><s:text name='groups.chat.psncard.notFriend' /></div> --%>
      <button class="button_main button_dense button_primary-changestyle"
        onclick="MsgBase.addFriend('<iris:des3 code="${psnInfoList[0].person.personId}"/>')">
        <s:text name='groups.chat.psncard.addFriend' />
      </button>
    </c:if>
  </div>
</div>