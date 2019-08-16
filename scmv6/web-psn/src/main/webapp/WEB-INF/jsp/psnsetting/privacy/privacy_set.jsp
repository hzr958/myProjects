<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="set-email__main-title">
  <span class="set-email__main-heading" style="font-size: 20px; font-weight: normal;"><s:text
      name="privacy.setting" /><span class="set-privacy__tip"><s:text name="privacy.setting.part1" /><span
      class="set-privacy__setfunc"><a href="/psnweb/homepage/show?jumpto=showSecuritySetting"><s:text
            name="privacy.setting.part2" /></a></span> <s:text name="privacy.setting.part3" /></span></span>
</div>
<div>
  <input type="hidden" id="initPrivateJson" value="<c:out value="${userSettings.initPrivateJson}"/>">
  <div class="set-privacy__list">
    <span class="set-privacy__list-title"><s:text name="privacy.sendMsg" />:</span>
    <div class="set-privacy__list-content">
      <span class="set-privacy__list-inner" code="" id="sendMsg">联系人</span> <i class="set-privacy__list-flag"></i>
      <div class="set-privacy__selector-container">
        <s:iterator value="userSettings.pravicyList" id="cd">
          <s:if test="#attr.lang eq  'zh'">
            <c:if test="${cd.key.code ne 2 and  cd.key.code ne 4}">
              <div class="set-privacy__list-item" item="${cd.key.code}">${cd.zhCnName}</div>
            </c:if>
          </s:if>
          <s:else>
            <c:if test="${cd.key.code ne 2 and  cd.key.code ne 4}">
              <div class="set-privacy__list-item" item="${cd.key.code}">${cd.enUsName}</div>
            </c:if>
          </s:else>
        </s:iterator>
      </div>
    </div>
  </div>
  <div class="set-privacy__list">
    <span class="set-privacy__list-title"><s:text name="privacy.reqAddFrd" />:</span>
    <div class="set-privacy__list-content">
      <span class="set-privacy__list-inner" code="" id="reqAddFrd">联系人</span> <i class="set-privacy__list-flag"></i>
      <div class="set-privacy__selector-container">
        <s:iterator value="userSettings.pravicyList" id="cd">
          <!-- 谁能加我为联系人修改：个人隐私查看权限值为2时，显示为“没有人”，值设定为2-仅本人 -->
          <s:if test="#attr.lang eq  'zh'">
            <s:if test="#cd.key.code ==0">
              <div class="set-privacy__list-item" item="${cd.key.code}">${cd.zhCnName}</div>
            </s:if>
            <s:elseif test="#cd.key.code==2">
              <div class="set-privacy__list-item" item="${cd.key.code}">没有人</div>
            </s:elseif>
          </s:if>
          <s:else>
            <s:if test="#cd.key.code ==0">
              <div class="set-privacy__list-item" item="${cd.key.code}">${cd.enUsName}</div>
            </s:if>
            <s:elseif test="#cd.key.code==2">
              <div class="set-privacy__list-item" item="${cd.key.code}">No one</div>
            </s:elseif>
          </s:else>
        </s:iterator>
      </div>
    </div>
  </div>
  <!-- SCM-18833 个人设置-隐私设置，建议去掉-“谁能查看我的联系人列表”功能 -->
  <%-- <div class="set-privacy__list">
                    <span class="set-privacy__list-title"><s:text name="privacy.vFrdList"/>:</span>
                    <div class="set-privacy__list-content">
                      <span class="set-privacy__list-inner"  code=""  id="vFrdList">联系人</span>
                      <i class="set-privacy__list-flag"></i> 
                      <div class="set-privacy__selector-container">
                       	<s:iterator value="userSettings.pravicyList" id="cd">
							<s:if test="#attr.lang eq  'zh'">
                                  <s:if test="#cd.key.code !=4">
								  <div class="set-privacy__list-item" item="${cd.key.code}" >${cd.zhCnName}</div>
                                   </s:if>
							</s:if>
							<s:else>
                                 <s:if test="#cd.key.code !=4">
								    <div class="set-privacy__list-item" item="${cd.key.code}" >${cd.enUsName}</div>
                                 </s:if>
							</s:else>
						 </s:iterator>
                      </div>
                    </div>
                  </div> --%>
  <!-- SCM-15408  目前没有以下功能 -->
  <%--  <div class="set-privacy__list">
                    <span class="set-privacy__list-title"><s:text name="privacy.vJoinGroupDyn"/>:</span>
                    <div class="set-privacy__list-content">
                      <span class="set-privacy__list-inner" code=""  id="vJoinGroupDyn" >联系人</span>
                      <i class="set-privacy__list-flag"></i> 
                      <div class="set-privacy__selector-container">
                         <s:iterator value="userSettings.pravicyList" id="cd">
							<s:if test="#attr.lang eq  'zh'">
								<div class="set-privacy__list-item" item="${cd.key.code}" >${cd.zhCnName}</div>
							</s:if>
							<s:else>
								<div class="set-privacy__list-item" item="${cd.key.code}" >${cd.enUsName}</div>
							</s:else>
						 </s:iterator>
                      </div>
                    </div>
                  </div>

                   
                   <div class="set-privacy__list">
                    <span class="set-privacy__list-title" ><s:text name="privacy.vAddFrd"/>:</span>
                    <div class="set-privacy__list-content">
                      <span class="set-privacy__list-inner" code=""  id="vAddFrd">联系人</span>
                      <i class="set-privacy__list-flag"></i> 
                      <div class="set-privacy__selector-container">
                        <s:iterator value="userSettings.pravicyList" id="cd">
							<s:if test="#attr.lang eq  'zh'">
								<div class="set-privacy__list-item" item="${cd.key.code}" >${cd.zhCnName}</div>
							</s:if>
							<s:else>
								<div class="set-privacy__list-item" item="${cd.key.code}" >${cd.enUsName}</div>
							</s:else>
						 </s:iterator>
                      </div>
                    </div>
                  </div> --%>
  <div class="set-privacy__list">
    <span class="set-privacy__list-title"><s:text name="privacy.vMyLiter" />:</span>
    <div class="set-privacy__list-content">
      <span class="set-privacy__list-inner" code="" id="vMyLiter">关注我的人</span> <i class="set-privacy__list-flag"></i>
      <div class="set-privacy__selector-container">
        <s:iterator value="userSettings.pravicyList" id="cd">
          <s:if test="#attr.lang eq  'zh'">
            <c:if test="${cd.key.code ne 0  and  cd.key.code ne 1 }">
              <div class="set-privacy__list-item" item="${cd.key.code}">${cd.zhCnName}</div>
            </c:if>
          </s:if>
          <s:else>
            <c:if test="${cd.key.code ne 0 and  cd.key.code ne 1}">
              <div class="set-privacy__list-item" item="${cd.key.code}">${cd.enUsName}</div>
            </c:if>
          </s:else>
        </s:iterator>
      </div>
    </div>
  </div>
  <div class="set-privacy__btn" onclick="PsnsettingPrivacy.submitModify(this);"
    style="margin-left: 238px; margin-top: 28px;">
    <s:text name="privacy.saveEdit" />
  </div>
</div>