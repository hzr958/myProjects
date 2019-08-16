<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	
	}); 
</script>
<div class="js_listinfo" smate_count="${page.totalCount}"></div>
<!--列表循环 start  -->
<s:iterator value="grpShowInfoList" var="gsi" status="st">
  <div class="main-list__item" smate_grpCategory="${gsi.grpCategory}" smate_role="${gsi.role}">
    <div class="main-list__item_content">
      <div class="grp-idx_medium">
        <div class="grp-idx__base-info" style="position: relative;">
          <!--  <div class="grp-idx__logo_box grp-idx__logo_position"> -->
          <div class="grp-idx__logo_box">
            <div class="grp-idx__logo_img">
              <a href='/groupweb/grpinfo/main?des3GrpId=<iris:des3 code="${gsi.grpBaseInfo.grpId}"/>'> <img
                src="${gsi.grpBaseInfo.grpAuatars}" onerror="this.src='${resmod}/smate-pc/img/logo_grpdefault.png'">
              </a>
              <div class="grp-idx__logo_box-tip"
                title="<s:text name='groups.unread.count.tips.front'></s:text>${gsi.groupUnreadCount}&nbsp;<s:text name='groups.unread.count.tips.back'></s:text>"
                style='cursor: pointer;display:<s:if test="%{#gsi.groupUnreadCount>0}">block;</s:if>; left: 47px!important; right: 0px;'>
                <s:if test="%{#gsi.groupUnreadCount<=99}">${gsi.groupUnreadCount}</s:if>
                <s:elseif test="%{#gsi.groupUnreadCount>99}">99+</s:elseif>
              </div>
            </div>
            <div class="grp-idx__grp-type">
              <c:if test="${gsi.grpBaseInfo.grpCategory ==12}">
                <s:text name='groups.list.interest' />
              </c:if>
              <c:if test="${gsi.grpBaseInfo.grpCategory ==11}">
                <s:text name='groups.list.pro' />
              </c:if>
              <c:if test="${gsi.grpBaseInfo.grpCategory ==10}">
                <s:text name='groups.list.cur' />
              </c:if>
            </div>
          </div>
          <div class="grp-idx__main_box">
            <div class="grp-idx__main">
              <div class="grp-idx__main_name">
                <a href='/groupweb/grpinfo/main?des3GrpId=<iris:des3 code="${gsi.grpBaseInfo.grpId}"/>'>${gsi.grpBaseInfo.grpName}</a>
                <!-- 隐藏 SCM-15094 -->
                <%--  <c:if test="${gsi.isGrpUnion ==0}">
                    	<i class="material-icons grp-idx__main_icon" title="已关联一个项目">link</i>
                    </c:if> --%>
              </div>
              <div class="grp-idx__main_kw">
                <div class="kw__box" smate_disciplines="${gsi.firstDisciplinetName};${gsi.secondDisciplinetName}"
                  smate_keywords="${gsi.grpKwDisc.keywords}">
                  <div class='kw-chip_small'></div>
                  <!-- 领域关键词 -->
                </div>
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
                <%-- <span class="grp-idx__main_stats-item"><s:text name='groups.list.person' />:${gsi.grpStatistic.sumMember} </span>
                    		<span class="grp-idx__main_stats-item"><s:text name='groups.list.pub' />:${gsi.grpStatistic.sumPubs}</span>
                    		<span class="grp-idx__main_stats-item"><s:text name='groups.list.file' />:${gsi.grpStatistic.sumFile}</span> --%>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="main-list__item_actions">
      <c:if test="${gsi.pendIngCount > 0}">
        <div class="grp-list__action_pending has-pending">
          <a onclick='GrpBase.toPendIngUI(${gsi.pendIngCountType},"${gsi.des3GrpId}")'>${gsi.pendIngCount}&nbsp;<s:text
              name='groups.list.pendIng' /></a>
        </div>
      </c:if>
    </div>
    <div class="main-list__item_actions">
      <div class="action-dropdown">
        <button class="button_main button_icon" onclick='clickMemberOpt(this,event)'>
          <i class="material-icons">more_vert</i>
        </button>
        <ul class="action-dropdown__list">
          <!-- 选择class = item_hovered -->
          <c:if test="${gsi.isApplyGrp ==0 && st.index != 0}">
            <li class="action-dropdown__item " onclick='GrpBase.setGrpTop("${gsi.des3GrpId}",1)'><s:text
                name='groups.list.action.top' /></li>
          </c:if>
          <c:if test="${gsi.isApplyGrp ==1}">
            <li class="action-dropdown__item "
              onclick='GrpMember.applyJoinGrp(0,"${gsi.des3GrpId}",GrpManage.reloadMyGrpList)'><s:text
                name='groups.list.action.cancelApply' /></li>
          </c:if>
          <c:if test="${gsi.isApplyGrp ==1}">
            <li class="action-dropdown__item " onclick="GrpMember.ajaxMemberApplyJoinGrp(null,'${gsi.des3GrpId}')">
              <s:text name='groups.list.action.applyAgain' />
            </li>
          </c:if>
          <c:if test="${gsi.isTop ==1}">
            <li class="action-dropdown__item " onclick='GrpBase.setGrpTop("${gsi.des3GrpId}",0)'><s:text
                name='groups.list.action.cancelTop' /></li>
          </c:if>
          <c:if test="${gsi.role ==1}">
            <li class="action-dropdown__item"
              onclick='GrpBase.showCopyGrpUI("${gsi.des3GrpId}","${gsi.grpBaseInfo.grpCategory}",this)'><s:text
                name='groups.list.action.copy' /></li>
          </c:if>
          <c:if test="${gsi.isApplyGrp ==0}">
            <li class="action-dropdown__item" onclick='GrpBase.jconfirmDelGrpPsn("${gsi.des3GrpId}",this)'><s:text
                name='groups.list.action.quit' /></li>
          </c:if>
        </ul>
      </div>
    </div>
  </div>
</s:iterator>
<!--列表循环 end  -->
