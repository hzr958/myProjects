<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	
});
</script>
<div class="js_listinfo" smate_count="${page.totalCount}"></div>
<c:forEach items="${grpShowInfoList}" var="grp" varStatus="stat">
  <div class="main-list__item">
    <div class="main-list__item_content">
      <div class="pub-idx_small">
        <div class="pub-idx__base-info">
          <div class="pub-idx__full-text_box">
            <div>
              <a href="${grp.grpIndexUrl }" target="_Blank"> <img class="main-list__list-item__container-avator"
                src="${grp.grpAuatars}">
              </a>
            </div>
          </div>
          <div class="pub-idx__main_box">
            <div class="pub-idx__main">
              <div class="pub-idx__main_title">
                <a href="${grpShowInfoList[0].grpInviterUrl }" target="_Blank"> <b class="blue">${grpShowInfoList[0].grpInviterName }</b>
                </a>
                <s:text name="groups.home.invite.list" />
                <a href="${grp.grpIndexUrl }" target="_Blank"> <b class="blue"> <c:out value="${grp.grpName }"
                      escapeXml="false" />
                </b>
                </a>
              </div>
              <div class="pub-idx__main_author">
                <div class="kw__box">
                  <div class="kw-chip_medium">${grp.firstDisciplinetName }</div>
                </div>
              </div>
              <div class="pub-idx__main_src">
                <span><s:text name="groups.base.pubNum" />:${grp.grpStatistic.sumPubs } &nbsp;&nbsp;</span> <span><s:text
                    name="groups.base.memberNum" />:${grp.grpStatistic.sumMember }</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="main-list__item_actions">
      <button class="button_main button_dense button_grey" onclick="Rm.ivitegrpApplication(0,'${grp.des3GrpId}',this)">
        <s:text name="groups.list.btn.ignore" />
      </button>
      <button class="button_main button_dense button_primary-changestyle"
        onclick="Rm.ivitegrpApplication(1,'${grp.des3GrpId}',this)">
        <s:text name="groups.list.btn.agree" />
      </button>
    </div>
  </div>
</c:forEach>
