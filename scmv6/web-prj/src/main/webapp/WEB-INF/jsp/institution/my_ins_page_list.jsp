<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>


<c:forEach items="${listInfo}" var="info" >
  <div class="create-InstitutionalGroups_body-item">
    <div class="create-InstitutionalGroups_item-avator" onclick="Institution.openUrl('${info.visitUrl}')">
      <img src="${info.logoUrl}"  onerror="this.src='/resmod/smate-pc/img/logo_instdefault.png'">
    </div>
    <div class="create-InstitutionalGroups_item-body">
      <div>
        <div class="create-InstitutionalGroups_item-title" onclick="Institution.openUrl('${info.visitUrl}')">
            ${info.insName}
        </div>
        <div class="create-InstitutionalGroups_item-count">
          <div class="create-InstitutionalGroups_countbox">
            <span class="create-InstitutionalGroups_countbox-title">${info.stView}</span>
            <span class="create-InstitutionalGroups_countbox-num">阅读</span>
          </div>
          <div class="create-InstitutionalGroups_countbox">
            <span class="create-InstitutionalGroups_countbox-title">${info.stShare}</span>
            <span class="create-InstitutionalGroups_countbox-num">分享</span>
          </div>
          <div class="create-InstitutionalGroups_countbox">
            <span class="create-InstitutionalGroups_countbox-title">${info.stFollow}</span>
            <span class="create-InstitutionalGroups_countbox-num">关注</span>
          </div>
        </div>
      </div>
      <div>
        <div class="create-InstitutionalGroups_item-Admin" onclick="Institution.openUrl('${info.manageUrl}')">进入管理</div>
          <%--<div class="create-InstitutionalGroups_item-Exten">推广</div>--%>
      </div>
    </div>
  </div>
</c:forEach>
<c:if test="${empty  listInfo}">
<div class="response_no-result" style="margin: 0 auto;">
  <div class="no_effort" style="padding: 88px 0 50px 130px !important;">
    <h2 style="height: 40px!important; line-height: 40px!important; font-size: 20px;">你还未创建属于自己的机构主页</h2>
    <div class="no_effort_tip">
      <span>温馨提示：</span>
      <p>利用科研之友社交网络推广机构.</p>
      <p>邀请更多人关注，有效推广产品和成果.</p>
      <p>获得更多帮助机构提高协同创新能力的服务.</p>
    </div>
  </div>
</div>
</c:if>

