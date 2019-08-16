<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
var randomModule = "${randomModule}";
</script>
<s:if test="grpShowInfoList.size()>0">
  <!-- 第一次访问页面时，请求两条数据，这样的好处在于快速显示数据  beforehand为0表示第一次访问  -->
  <s:if test="beforehand==0">
    <div class="container__card">
      <div class="module-card__box">
        <div class="module-card__header"
          style="background: #f4f4f4 none repeat scroll 0 0; border-bottom: 1px solid #ddd;">
          <div class="module-card-header__title">
            <s:text name="groups.label.joinRecmd" />
          </div>
          <button onclick="Rm.showGrpInviteRegInfo('invite')" class="button_main button_link">
            <s:text name="groups.more" />
          </button>
        </div>
        <div class="main-list__list item_vert-style item_no-border"
          style="background: #fafafa none repeat scroll 0 0; padding: 12px;">
          <div class="main-list__list-title">
            <s:text name="groups.tips.joinGroup" />
          </div>
          <div class="main-list__list-item__container">
            <div class="main-list__item_content">
              <div class="main-list__list-item__container-header">
                <div>
                  <a href="${grpShowInfoList[0].grpIndexUrl }" target="_Blank"> <img
                    src="${grpShowInfoList[0].grpAuatars }" class="main-list__list-item__container-avator"
                    onerror="this.src='/resmod/smate-pc/img/logo_grpdefault.png'">
                  </a>
                </div>
                <div class="main-list__list-item__container-body">
                  <div class="main-list__list-item__container-name" title="${grpShowInfoList[0].grpName }">
                    <a href="${grpShowInfoList[0].grpIndexUrl }" target="_Blank"> <b class="blue">
                        ${grpShowInfoList[0].grpName } </b>
                    </a>
                  </div>
                  <c:if test="${!empty grpShowInfoList[0].firstDisciplinetName }">
                    <div class="main-list__list-item__container-name">
                      <div class="kw__box">
                        <div class="kw-chip_medium">${grpShowInfoList[0].firstDisciplinetName }</div>
                        <c:if test="${!empty grpShowInfoList[0].secondDisciplinetName }">
                        <div class="kw-chip_medium">${grpShowInfoList[0].secondDisciplinetName }</div>
                        </c:if>
                      </div>
                    </div>
                  </c:if>
                  <div class="main-list__list-item__container-name">
                    <span><s:text name="groups.base.memberNum" />:&nbsp;${grpShowInfoList[0].grpStatistic.sumMember }
                      &nbsp;&nbsp;</span> <span><s:text name="groups.base.pubNum" />:&nbsp;${grpShowInfoList[0].grpStatistic.sumPubs }</span>
                  </div>
                </div>
              </div>
              <div class="main-list__list-item__container-target">
                <a href="${grpShowInfoList[0].grpInviterUrl }" target="_Blank"
                  title="${grpShowInfoList[0].grpInviterName }">
                  <div class="blue main-list__list-item__container-name">${grpShowInfoList[0].grpInviterName }</div>
                </a>
                <s:text name="groups.home.invite" />
              </div>
              <div class="main-list__list-item__container-target"></div>
            </div>
            <div class="main-list__list-item__container-footer">
              <button class="button_main button_primary-normalstyle"
                onclick="Rm.ivitegrpApplication(0,'${grpShowInfoList[0].des3GrpId}',null)">
                <s:text name="groups.list.btn.ignore" />
              </button>
              <button class="button_main button_primary-reverse"
                onclick="Rm.ivitegrpApplication(1,'${grpShowInfoList[0].des3GrpId}',null)">
                <s:text name="groups.list.btn.agree" />
              </button>
            </div>
          </div>
        </div>
        <s:if test="grpShowInfoList.size()>1">
          <div class="main-list__list item_vert-style item_no-border" style="display: none;">
            <div class="main-list__list-title">
              <s:text name="groups.tips.joinGroup" />
            </div>
            <div class="main-list__list-item__container">
              <div class="main-list__item_content">
                <div class="main-list__list-item__container-header">
                  <div>
                    <a href="${grpShowInfoList[1].grpIndexUrl }" target="_Blank"> <img
                      src="${grpShowInfoList[1].grpAuatars }" class="main-list__list-item__container-avator"
                      onerror="this.src='/resmod/smate-pc/img/logo_grpdefault.png'">
                    </a>
                  </div>
                  <div class="main-list__list-item__container-body">
                    <div class="main-list__list-item__container-name" title="${grpShowInfoList[1].grpName }">
                      <a href="${grpShowInfoList[1].grpIndexUrl }" target="_Blank"> <b class="blue">
                          ${grpShowInfoList[1].grpName } </b>
                      </a>
                    </div>
                    <c:if test="${!empty grpShowInfoList[1].firstDisciplinetName }">
                      <div class="main-list__list-item__container-name">
                        <div class="kw__box">
                          <div class="kw-chip_medium">${grpShowInfoList[1].firstDisciplinetName }</div>
                        </div>
                      </div>
                    </c:if>
                    <div class="main-list__list-item__container-name">
                      <span><s:text name="groups.base.memberNum" />:&nbsp;${grpShowInfoList[1].grpStatistic.sumMember }
                        &nbsp;&nbsp;</span> <span><s:text name="groups.base.pubNum" />:&nbsp;${grpShowInfoList[1].grpStatistic.sumPubs }</span>
                    </div>
                  </div>
                </div>
                <div class="main-list__list-item__container-target" title="${grpShowInfoList[1].grpInviterName }">
                  <a href="${grpShowInfoList[1].grpInviterUrl }" target="_Blank">
                    <div class="blue main-list__list-item__container-name">${grpShowInfoList[1].grpInviterName }</div>
                  </a>
                  <s:text name="groups.home.invite" />
                </div>
                <div class="main-list__list-item__container-target"></div>
              </div>
              <div class="main-list__list-item__container-footer">
                <button class="button_main button_primary-normalstyle"
                  onclick="Rm.ivitegrpApplication(0,'${grpShowInfoList[1].des3GrpId}',null)">
                  <s:text name="groups.list.btn.ignore" />
                </button>
                <button class="button_main button_primary-reverse"
                  onclick="Rm.ivitegrpApplication(1,'${grpShowInfoList[1].des3GrpId}',null)">
                  <s:text name="groups.list.btn.agree" />
                </button>
              </div>
            </div>
          </div>
        </s:if>
      </div>
    </div>
  </s:if>
  <!-- 下一次操作之后请求下一条数据 -->
  <s:if test="beforehand==1&&grpShowInfoList.size()>1">
    <div class="main-list__list item_vert-style item_no-border"" >
      <div class="main-list__list-title">
        <s:text name="groups.tips.joinGroup" />
      </div>
      <div class="main-list__list-item__container">
        <div class="main-list__item_content">
          <div class="main-list__list-item__container-header">
            <div>
              <a href="${grpShowInfoList[1].grpIndexUrl }" target="_Blank"> <img
                src="${grpShowInfoList[1].grpAuatars }" class="main-list__list-item__container-avator"
                onerror="this.src='/resmod/smate-pc/img/logo_grpdefault.png'">
              </a>
            </div>
            <div class="main-list__list-item__container-body">
              <div class="main-list__list-item__container-name">
                <a href="${grpShowInfoList[1].grpIndexUrl }" target="_Blank"> <b class="blue">
                    ${grpShowInfoList[1].grpName } </b>
                </a>
              </div>
              <c:if test="${!empty grpShowInfoList[1].firstDisciplinetName }">
                <div class="main-list__list-item__container-name">
                  <div class="kw__box">
                    <div class="kw-chip_medium">${grpShowInfoList[1].firstDisciplinetName }</div>
                  </div>
                </div>
              </c:if>
              <div class="main-list__list-item__container-name">
                <span><s:text name="groups.base.memberNum" />:&nbsp;${grpShowInfoList[1].grpStatistic.sumMember }
                  &nbsp;&nbsp;</span> <span><s:text name="groups.base.pubNum" />:&nbsp;${grpShowInfoList[1].grpStatistic.sumPubs }</span>
              </div>
            </div>
          </div>
          <div class="main-list__list-item__container-target">
            <a href="${grpShowInfoList[1].grpInviterUrl }" target="_Blank" title="${grpShowInfoList[1].grpInviterName }">
              <div class="blue main-list__list-item__container-name">${grpShowInfoList[1].grpInviterName }</div>
            </a>
            <s:text name="groups.home.invite" />
          </div>
          <div class="main-list__list-item__container-target"></div>
        </div>
        <div class="main-list__list-item__container-footer">
          <button class="button_main button_primary-changestyle"
            onclick="Rm.ivitegrpApplication(0,'${grpShowInfoList[1].des3GrpId}',null)">
            <s:text name="groups.list.btn.ignore" />
          </button>
          <button class="button_main button_primary-reverse"
            onclick="Rm.ivitegrpApplication(1,'${grpShowInfoList[1].des3GrpId}',null)">
            <s:text name="groups.list.btn.agree" />
          </button>
        </div>
      </div>
    </div>
  </s:if>
</s:if>
