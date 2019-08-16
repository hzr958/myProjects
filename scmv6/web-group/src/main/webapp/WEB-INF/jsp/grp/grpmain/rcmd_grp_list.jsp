<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count='${page.totalCount}'></div>
<s:iterator value="grpShowInfoList" var="grpShowInfo">
  <div class="main-list__item">
    <div class="main-list__item_content">
      <div class="grp-idx_medium">
        <div class="grp-idx__base-info">
          <div class="grp-idx__logo_box">
            <div class="grp-idx__logo_img">
              <a href='/groupweb/grpinfo/main?des3GrpId=<iris:des3 code="${grpShowInfo.grpBaseInfo.grpId}"/>'> <img
                src="${grpShowInfo.grpBaseInfo.grpAuatars}"
                onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
              </a>
            </div>
            <div class="grp-idx__grp-type">
              <c:if test="${grpShowInfo.grpBaseInfo.grpCategory ==12}">
                <s:text name='groups.list.interest' />
              </c:if>
              <c:if test="${grpShowInfo.grpBaseInfo.grpCategory ==11}">
                <s:text name='groups.list.pro' />
              </c:if>
              <c:if test="${grpShowInfo.grpBaseInfo.grpCategory ==10}">
                <s:text name='groups.list.cur' />
              </c:if>
            </div>
          </div>
          <div class="grp-idx__main_box">
            <div class="grp-idx__main">
              <div class="grp-idx__main_name">
                <a href='/groupweb/grpinfo/main?des3GrpId=<iris:des3 code="${grpShowInfo.grpBaseInfo.grpId}"/>'>${grpShowInfo.grpBaseInfo.grpName}</a>
                <!-- 隐藏 SCM-15094 -->
              </div>
              <div class="grp-idx__main_kw">
                <div class="kw__box"
                  smate_disciplines="${grpShowInfo.firstDisciplinetName};${grpShowInfo.secondDisciplinetName}"
                  smate_keywords="${grpShowInfo.grpKwDisc.keywords}">
                  <!-- 领域关键词 -->
                </div>
              </div>
              <div class="grp-idx__main_stats">
                <!--  课程群组 成员，文献，课件，作业  -->
                <s:if test="#grpShowInfo.grpBaseInfo.grpCategory ==10">
                  <span class="grp-idx__main_stats-item"><s:text name='groups.list.num.person' />:
                    ${grpShowInfo.grpStatistic.sumMember}</span>
                  <span class="grp-idx__main_stats-item"><s:text name='groups.list.num.pub' />:
                    ${grpShowInfo.grpStatistic.sumPubs}</span>
                  <span class="grp-idx__main_stats-item"><s:text name='groups.file.num.curware' />:
                    ${grpShowInfo.grpCourseFileSum}</span>
                  <span class="grp-idx__main_stats-item"><s:text name='groups.file.num.work' />:
                    ${grpShowInfo.grpWorkFileSum}</span>
                </s:if>
                <!--  项目群组 成员，成果，文献，文件   -->
                <s:elseif test="#grpShowInfo.grpBaseInfo.grpCategory ==11">
                  <span class="grp-idx__main_stats-item"><s:text name='groups.list.num.person' />:
                    ${grpShowInfo.grpStatistic.sumMember}</span>
                  <span class="grp-idx__main_stats-item"><s:text name='groups.project.num.pub' />:
                    ${grpShowInfo.grpProjectPubSum}</span>
                  <span class="grp-idx__main_stats-item"><s:text name='groups.project.num.ref' />:
                    ${grpShowInfo.grpProjectRefSum}</span>
                  <span class="grp-idx__main_stats-item"><s:text name='groups.list.num.file' />:
                    ${grpShowInfo.grpStatistic.sumFile}</span>
                </s:elseif>
                <s:else>
                  <span class="grp-idx__main_stats-item"><s:text name='groups.list.num.person' />:
                    ${grpShowInfo.grpStatistic.sumMember}</span>
                  <span class="grp-idx__main_stats-item"><s:text name='groups.list.num.pub' />:
                    ${grpShowInfo.grpStatistic.sumPubs}</span>
                  <span class="grp-idx__main_stats-item"><s:text name='groups.list.num.file' />:
                    ${grpShowInfo.grpStatistic.sumFile}</span>
                </s:else>
              </div>
            </div>
          </div>
        </div>
        <div class="grp-idx__intro multipleline-ellipsis" style="cursor: default">
          <div style="cursor: default;" class="multipleline-ellipsis__content-box" style="cursor:default">${grpShowInfo.grpBaseInfo.grpDescription }</div>
        </div>
      </div>
    </div>
    <div class="main-list__item_actions">
      <button class="button_main button_grey" onclick="GrpManage.optRcmdGrp(  '${grpShowInfo.des3GrpId }' , 9)">
        <s:text name='groups.list.action.ignore' />
      </button>
      <button class="button_main button_primary-changestyle"
        onclick="GrpManage.optRcmdGrp('${grpShowInfo.des3GrpId }' , 1 ,'${grpShowInfo.grpBaseInfo.openType}');">
        <s:text name='groups.list.action.join' />
      </button>
    </div>
  </div>
</s:iterator>
<s:if test="page.totalPages == page.pageNo && page.totalCount>0">
  <jsp:include page="/skins_v6/footer_infor.jsp" />
</s:if>