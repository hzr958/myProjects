
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	GrpBase.showDisciplinesAndKeywordsForIviteGrpList();
	}); 
</script>
<s:iterator value="grpShowInfoList" var="gsi" status="st">
  <div class="main-list__item" des3GrpId="<iris:des3 code='${gsi.grpId}'/>">
    <div class="main-list__item_content">
      <div class="grp-idx_medium">
        <div class="grp__invite-confirm" style="display: flex;align-items: center;">
          <a href="${gsi.grpInviterUrl }" title="${gsi.grpInviterName }" target="_Blank" class="pub-idx__main_title-huge_limit"><b class="blue">${gsi.grpInviterName }</b></a>&nbsp;
          <s:text name='groups.list.invite' />
        </div>
        <div class="grp-idx__base-info">
          <div class="grp-idx__logo_box">
            <div class="grp-idx__logo_img">
              <s:if test="#gsi.grpIndexUrl!=null">
                <a class="pub_info_title" href="${gsi.grpIndexUrl}" target="_Blank">
              </s:if>
              <s:else>
                <a onclick="GrpBase.openGrpDetail('<iris:des3 code="${gsi.grpId}"/>',event)">
              </s:else>
              <img src="${gsi.grpBaseInfo.grpAuatars}" onerror="this.src='${resmod}/smate-pc/img/logo_grpdefault.png'">
              </a>
            </div>
            <div class="grp-idx__grp-type">
              <c:if test="${gsi.grpBaseInfo.grpCategory ==10}">
                <s:text name='groups.list.cur' />
              </c:if>
              <c:if test="${gsi.grpBaseInfo.grpCategory ==11}">
                <s:text name='groups.list.pro' />
              </c:if>
              <c:if test="${gsi.grpBaseInfo.grpCategory ==12}">
                <s:text name='groups.list.interest' />
              </c:if>
            </div>
          </div>
          <div class="grp-idx__main_box">
            <div class="grp-idx__main">
              <div class="grp-idx__main_name">
                <s:if test="#gsi.grpIndexUrl!=null">
                  <a class="pub_info_title" href="${gsi.grpIndexUrl}" target="_Blank">
                </s:if>
                <s:else>
                  <a onclick="GrpBase.openGrpDetail('<iris:des3 code="${gsi.grpId}"/>',event)">
                </s:else>
                ${gsi.grpBaseInfo.grpName}</a>
                <!-- 隐藏 SCM-15094 -->
                <%-- <c:if test="${gsi.isGrpUnion ==0}">
                    	<i class="material-icons grp-idx__main_icon" title="<s:text name='groups.list.link' />">link</i>
                    </c:if> --%>
              </div>
              <div class="grp-idx__main_kw">
                <div class="kw__box" smate_disciplines="${gsi.firstDisciplinetName};${gsi.secondDisciplinetName}"
                  smate_keywords="${gsi.grpKwDisc.keywords}"></div>
              </div>
              <div class="grp-idx__main_stats">
                <!--  课程群组 成员，文献，课件，作业  -->
                <s:if test="#gsi.grpBaseInfo.grpCategory ==10">
                  <span class="grp-idx__main_stats-item"><s:text name='groups.list.person' />:
                    ${gsi.grpStatistic.sumMember}</span>
                  <span class="grp-idx__main_stats-item"><s:text name='groups.list.pub' />:
                    ${gsi.grpStatistic.sumPubs}</span>
                  <span class="grp-idx__main_stats-item"><s:text name='groups.file.curware' />:
                    ${gsi.grpCourseFileSum}</span>
                  <span class="grp-idx__main_stats-item"><s:text name='groups.file.work' />:
                    ${gsi.grpWorkFileSum}</span>
                </s:if>
                <!--  项目群组 成员，成果，文献，文件   -->
                <s:elseif test="#gsi.grpBaseInfo.grpCategory ==11">
                  <span class="grp-idx__main_stats-item"><s:text name='groups.list.person' />:
                    ${gsi.grpStatistic.sumMember}</span>
                  <span class="grp-idx__main_stats-item"><s:text name='groups.project.pub' />:
                    ${gsi.grpProjectPubSum}</span>
                  <span class="grp-idx__main_stats-item"><s:text name='groups.project.ref' />:
                    ${gsi.grpProjectRefSum}</span>
                  <span class="grp-idx__main_stats-item"><s:text name='groups.list.file' />:
                    ${gsi.grpStatistic.sumFile}</span>
                </s:elseif>
                <s:else>
                  <span class="grp-idx__main_stats-item"><s:text name='groups.list.person' />:
                    ${gsi.grpStatistic.sumMember}</span>
                  <span class="grp-idx__main_stats-item"><s:text name='groups.list.pub' />:
                    ${gsi.grpStatistic.sumPubs}</span>
                  <span class="grp-idx__main_stats-item"><s:text name='groups.list.file' />:
                    ${gsi.grpStatistic.sumFile}</span>
                </s:else>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="main-list__item_actions">
      <button class="button_main button_dense button_grey"
        onclick="GrpBase.ivitegrpApplication(0,'<iris:des3 code="${gsi.grpId}"/>',GrpBase.getIviteGrpList,this)">
        <s:text name='groups.list.btn.ignore' />
      </button>
      <button class="button_main button_dense button_primary-changestyle"
        onclick="GrpBase.ivitegrpApplication(1,'<iris:des3 code="${gsi.grpId}"/>',GrpBase.getIviteGrpList,this)">
        <s:text name='groups.list.btn.accept' />
      </button>
    </div>
  </div>
</s:iterator>