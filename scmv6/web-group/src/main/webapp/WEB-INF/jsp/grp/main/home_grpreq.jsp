<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
var randomModule = "${randomModule}";
</script>
<s:if test="grpShowInfoList.size()>0">
  <s:if test="beforehand==0">
    <div class="container__card">
      <div class="module-card__box">
        <div class="module-card__header"
          style="background: #f4f4f4 none repeat scroll 0 0; border-bottom: 1px solid #ddd;">
          <div class="module-card-header__title">
            <s:text name="groups.label.joinReq" />
          </div>
          <button onclick="Rm.showGrpInviteRegInfo('reg');" class="button_main button_link">
            <s:text name="groups.more" />
          </button>
        </div>
        <div class="main-list__list item_vert-style item_no-border"
          style="background: #fafafa none repeat scroll 0 0; padding: 12px;">
          <div class="main-list__list-title">
            <s:text name="groups.tips.allowJoinGroup" />
          </div>
          <div class="main-list__list-item__container">
            <div class="main-list__item_content">
              <div class="main-list__list-item__container-header">
                <div>
                  <a href="${grpShowInfoList[0].grpInviterUrl }" target="_Blank"> <img
                    src="${grpShowInfoList[0].grpInviterAvatars }" class="main-list__list-item__container-avator"
                    onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
                  </a>
                </div>
                <div class="main-list__list-item__container-body">
                  <div class="main-list__list-item__container-name">
                    <a href="${grpShowInfoList[0].grpInviterUrl }" title="${grpShowInfoList[0].grpInviterName }"
                      target="_Blank"> ${grpShowInfoList[0].grpInviterName } </a>
                  </div>
                  <div style="width: 210px" class="main-list__list-item__container-duty">${grpShowInfoList[0].grpInviterInsName }<c:if
                      test="${!empty grpShowInfoList[0].grpInviterDepartment }">,&nbsp;${grpShowInfoList[0].grpInviterDepartment }</c:if>
                  </div>
                </div>
              </div>
              <div class="main-list__list-item__container-target" title="${grpShowInfoList[0].grpName }">
                <s:text name="groups.label.reqJoin" />
                <a href="${grpShowInfoList[0].grpIndexUrl }" target="_Blank">
                  <div class="blue main-list__list-item__container-name">${grpShowInfoList[0].grpName }</div>
                </a>
              </div>
            </div>
            <div class="main-list__list-item__container-footer">
              <button class="button_main button_primary-normalstyle"
                onclick="Rm.disposegrpApplication(0,'${grpShowInfoList[0].grpInviterDes3psnId}','${grpShowInfoList[0].des3GrpId}')">
                <s:text name="groups.list.btn.ignore" />
              </button>
              <button class="button_main button_primary-reverse"
                onclick="Rm.disposegrpApplication(1,'${grpShowInfoList[0].grpInviterDes3psnId}','${grpShowInfoList[0].des3GrpId}')">
                <s:text name="groups.list.btn.agree" />
              </button>
            </div>
          </div>
        </div>
        <s:if test="grpShowInfoList.size()>1">
          <div class="main-list__list item_vert-style item_no-border" style="display: none;">
            <div class="main-list__list-title">
              <s:text name="groups.tips.allowJoinGroup" />
            </div>
            <div class="main-list__list-item__container">
              <div class="main-list__item_content">
                <div class="main-list__list-item__container-header">
                  <div>
                    <a href="${grpShowInfoList[1].grpInviterUrl }" target="_Blank"> <img
                      src="${grpShowInfoList[1].grpInviterAvatars }" class="main-list__list-item__container-avator"
                      onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
                    </a>
                  </div>
                  <div class="main-list__list-item__container-body">
                    <div class="main-list__list-item__container-name">
                      <a href="${grpShowInfoList[1].grpInviterUrl }" target="_Blank">
                        ${grpShowInfoList[1].grpInviterName } </a>
                    </div>
                    <div style="width: 210px" class="main-list__list-item__container-duty">${grpShowInfoList[1].grpInviterInsName }<c:if
                        test="${!empty grpShowInfoList[1].grpInviterDepartment }">,&nbsp;${grpShowInfoList[1].grpInviterDepartment }</c:if>
                    </div>
                  </div>
                </div>
                <div class="main-list__list-item__container-target">
                  <s:text name="groups.label.reqJoin" />
                  <a href="${grpShowInfoList[1].grpIndexUrl }" target="_Blank"> <b class="blue">
                      ${grpShowInfoList[1].grpName }</b>
                  </a>
                </div>
              </div>
              <div class="main-list__list-item__container-footer">
                <button class="button_main button_primary-changestyle"
                  onclick="Rm.disposegrpApplication(0,'${grpShowInfoList[1].grpInviterDes3psnId}','${grpShowInfoList[1].des3GrpId}')">
                  <s:text name="groups.list.btn.ignore" />
                </button>
                <button class="button_main button_primary-reverse"
                  onclick="Rm.disposegrpApplication(1,'${grpShowInfoList[1].grpInviterDes3psnId}','${grpShowInfoList[1].des3GrpId}')">
                  <s:text name="groups.list.btn.agree" />
                </button>
              </div>
            </div>
          </div>
        </s:if>
      </div>
    </div>
  </s:if>
  <s:if test="beforehand==1&&grpShowInfoList.size()>1">
    <div class="main-list__list item_vert-style item_no-border" style="display: none;">
      <div class="main-list__list-title">
        <s:text name="groups.tips.allowJoinGroup" />
      </div>
      <div class="main-list__list-item__container">
        <div class="main-list__item_content">
          <div class="main-list__list-item__container-header">
            <div>
              <a href="${grpShowInfoList[1].grpInviterUrl }" target="_Blank"> <img
                src="${grpShowInfoList[1].grpInviterAvatars }" class="main-list__list-item__container-avator"
                onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
              </a>
            </div>
            <div class="main-list__list-item__container-body">
              <div class="main-list__list-item__container-name">
                <a href="${grpShowInfoList[1].grpInviterUrl }" target="_Blank"> ${grpShowInfoList[1].grpInviterName }
                </a>
              </div>
              <div style="width: 210px" class="main-list__list-item__container-duty">${grpShowInfoList[1].grpInviterInsName }<c:if
                  test="${!empty grpShowInfoList[1].grpInviterDepartment }">,&nbsp;${grpShowInfoList[1].grpInviterDepartment }</c:if>
              </div>
            </div>
          </div>
          <div class="main-list__list-item__container-target" title="${grpShowInfoList[1].grpName }">
            <s:text name="groups.label.reqJoin" />
            <a href="${grpShowInfoList[1].grpIndexUrl }" target="_Blank">
              <div class="blue main-list__list-item__container-name">${grpShowInfoList[1].grpName }</div>
            </a>
          </div>
        </div>
        <div class="main-list__list-item__container-footer">
          <button class="button_main button_primary-changestyle"
            onclick="Rm.disposegrpApplication(0,'${grpShowInfoList[1].grpInviterDes3psnId}','${grpShowInfoList[1].des3GrpId}')">
            <s:text name="groups.list.btn.ignore" />
          </button>
          <button class="button_main button_primary-reverse"
            onclick="Rm.disposegrpApplication(1,'${grpShowInfoList[1].grpInviterDes3psnId}','${grpShowInfoList[1].des3GrpId}')">
            <s:text name="groups.list.btn.agree" />
          </button>
        </div>
      </div>
    </div>
  </s:if>
</s:if>