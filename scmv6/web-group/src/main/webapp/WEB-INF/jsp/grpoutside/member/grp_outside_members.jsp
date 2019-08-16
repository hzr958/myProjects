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
              <a onclick='openPsnDetail("${pi.des3PsnId}")'><img src="${pi.person.avatars}"
                onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'"></a>
            </div>
          <!-- 头衔  start-->
            <div class="psn-idx__avatar_box-title">
              <!--拥有者 -->
              <c:if test="${pi.role == 1}">
                <s:text name='groups.outside.member.owner.lable' />
              </c:if>
              <!--管理员-->
              <c:if test="${pi.role == 2}">
                <s:text name='groups.outside.member.administrator.lable' />
              </c:if>
              <!--组员 -->
              <c:if test="${pi.role != 1 && pi.role != 2}">
                <s:text name='groups.outside.member.groupMember.lable' />
              </c:if>
            </div>
            <!-- 头衔 end -->
          </div>
          <div class="psn-idx__main_box">
            <div class="psn-idx__main">
              <div class="psn-idx__main_name">
                <a onclick='openPsnDetail("${pi.des3PsnId}")' title="${pi.name}">${pi.name}</a>
              </div>
              <div class="psn-idx__main_title">${pi.person.insName}</div>
              <div class="psn-idx__main_area">
                <div class="kw__box">
                  <s:iterator value="#pi.psnDisciplineKeyList" var="psnD" status="st">
                    <div class="kw-chip_small">${psnD.keyWords}</div>
                  </s:iterator>
                </div>
              </div>
              <div class="psn-idx__main_stats">
                <span class="psn-idx__main_stats-item"><s:text name='groups.outside.member.pro' />:
                  ${pi.psnStatistics.prjSum}</span><span class="psn-idx__main_stats-item"><s:text
                    name='groups.outside.member.pub' />: ${pi.psnStatistics.pubSum}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="main-list__item_actions">
      <div class="action-dropdown dev_member_opt"></div>
    </div>
  </div>
</s:iterator>
<!--列表循环 end  -->
