<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count='${psnCount}'></div>
<!--列表循环 start  -->
<s:iterator value="psnInfoList" var="pi" status="st">
  <div class="main-list__item" smate_role='${pi.role}'>
    <div class="main-list__item_content">
      <div class="psn-idx_medium">
        <div class="psn-idx__base-info">
          <div class="psn-idx__avatar_box">
            <div class="psn-idx__avatar_img">
              <a href="/psnweb/homepage/show?des3PsnId=${pi.des3PsnId}" target="_Blank"> <img
                src="${pi.person.avatars}" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
              </a>
            </div>
            <!-- 头衔  start-->
            <div class="psn-idx__avatar_box-title">
              <!--拥有者 -->
              <c:if test="${pi.role == 1}">
                <s:text name='groups.member.owner.lable' />
              </c:if>
              <!--管理员-->
              <c:if test="${pi.role == 2}">
                <s:text name='groups.member.administrator.lable' />
              </c:if>
              <!--组员 -->
              <c:if test="${pi.role != 1 && pi.role != 2}">
                <s:text name='groups.member.groupMember.lable' />
              </c:if>
            </div>
            <!-- 头衔 end -->
          </div>
          <div class="psn-idx__main_box">
            <div class="psn-idx__main">
              <div class="psn-idx__main_name">
                <a href="/psnweb/homepage/show?des3PsnId=${pi.des3PsnId}" title="${pi.name}" target="_Blank">${pi.name}</a>
              </div>
              <div class="psn-idx__main_title1">${pi.insName}</div>
              <div class="psn-idx__main_area">
                <div class="kw__box">
                  <s:iterator value="#pi.psnDisciplineKeyList" var="psnD" status="st">
                    <div class="kw-chip_small">${psnD.keyWords}</div>
                  </s:iterator>
                </div>
              </div>
              <div class="psn-idx__main_stats">
                <span class="psn-idx__main_stats-item"><s:text name='groups.member.pro' />:
                  ${pi.psnStatistics.prjSum}</span><span class="psn-idx__main_stats-item"><s:text
                    name='groups.member.pub' />: ${pi.psnStatistics.pubSum}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="main-list__item_actions">
      <div class="action-dropdown dev_member_opt">
        <button class="button_main button_icon " onclick='GrpMember.clickMemberOpt(this,event)'>
          <i class="material-icons">more_vert</i>
        </button>
        <ul class="action-dropdown__list ">
          <!-- class = list_shown  -->
          <c:if test="${pi.isSetAdmin==1}">
            <li class="action-dropdown__item item_hovered" onclick='GrpMember.updateGrpPsnRole(2,"${pi.des3PsnId}")'><s:text
                name='groups.member.updRole.setAdmin' /></li>
          </c:if>
          <c:if test="${pi.isSetAdmin==2}">
            <li class="action-dropdown__item item_hovered" onclick='GrpMember.updateGrpPsnRole(3,"${pi.des3PsnId}")'><s:text
                name='groups.member.updRole.setMember' /></li>
          </c:if>
          <c:if test="${pi.isGrpOwner==1}">
            <li class="action-dropdown__item"
              onclick='GrpMember.jconfirmNew(GrpMember.updateGrpPsnRoleNew,"<s:text name='groups.member.updRole.setOwnerPre' />&nbsp;${pi.name} <s:text name='groups.member.updRole.setOwnerSuf' />",1,"${pi.des3PsnId}");'><s:text
                name='groups.member.updRole.setOwner' /></li>
          </c:if>
          <c:if test="${pi.isFriend == 0}">
            <li class="action-dropdown__item" des3Id='${pi.des3PsnId}'
              title='<s:text name='groups.member.addFriendPre' />&nbsp;${pi.name}&nbsp;<s:text name='groups.member.addFriendSuf' />'
              onclick="GrpMember.addFriend('${pi.des3PsnId}')"><s:text name='groups.member.addFriend' /></li>
          </c:if>
          <c:if test="${pi.isRemove==1}">
            <li class="action-dropdown__item"
              onclick='GrpMember.jconfirmNew(GrpMember.delGrpPsnNew,"<s:text name='groups.member.updRole.rmvMemberPre' />&nbsp;${pi.name}<s:text name='groups.member.updRole.rmvMemberSuf' />","${pi.des3PsnId}",this)'><s:text
                name='groups.member.updRole.rmvMember' /></li>
          </c:if>
        </ul>
      </div>
    </div>
  </div>
</s:iterator>
<!--列表循环 end  -->
