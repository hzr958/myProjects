<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
var randomModule = "${randomModule}";
</script>
<s:if test="psnInfoList.size()>0">
  <s:if test="beforehand==0">
    <div class="container__card">
      <div class="module-card__box">
        <div class="module-card__header"
          style="background: #f4f4f4 none repeat scroll 0 0; border-bottom: 1px solid #ddd;">
          <div class="module-card-header__title">
            <s:text name="friend.Req" />
          </div>
          <button onclick="Rm.reqFriendAll();" class="button_main button_link">
            <s:text name="friend.more" />
          </button>
        </div>
        <div class="main-list__list item_vert-style item_no-border"
          style="background: #fafafa none repeat scroll 0 0; padding: 12px;">
          <div class="main-list__list-title">
            <s:text name="friend.tips.agreeReq" />
          </div>
          <div class="main-list__list-item__container">
            <div class="main-list__item_content">
              <div class="main-list__list-item__container-header">
                <div>
                  <a href="${psnInfoList[0].psnShortUrl }" target="_Blank"> <img
                    src="${psnInfoList[0].person.avatars }" class="main-list__list-item__container-avator"
                    onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
                  </a>
                </div>
                <div class="main-list__list-item__container-body">
                  <div class="main-list__list-item__container-name">
                    <a href="${psnInfoList[0].psnShortUrl }" title="${psnInfoList[0].name }" target="_Blank">
                      ${psnInfoList[0].name } </a>
                  </div>
                  <div style="width: 210px" class="main-list__list-item__container-duty"
                    title='${psnInfoList[0].person.insName }<c:if test="${!empty psnInfoList[0].person.position }">,&nbsp;${psnInfoList[0].person.position }</c:if>'>
                    ${psnInfoList[0].person.insName }
                    <c:if test="${!empty psnInfoList[0].person.position }">,&nbsp;${psnInfoList[0].person.position }</c:if>
                  </div>
                </div>
              </div>
              <div class="main-list__list-item__container-target"></div>
            </div>
            <div class="main-list__list-item__container-footer">
              <button class="new-standard_normal-cancle" onclick="Rm.friend_neglet('${psnInfoList[0].des3PsnId }')">
                <s:text name="friend.ignore" />
              </button>
              <button class="button_main button_primary-reverse"
                onclick="Rm.friend_agree('${psnInfoList[0].des3PsnId }')">
                <s:text name="friend.agree" />
              </button>
            </div>
          </div>
        </div>
        <s:if test="psnInfoList.size()>1">
          <div class="main-list__list item_vert-style item_no-border" style="display: none;">
            <div class="main-list__list-title">
              <s:text name="friend.tips.agreeReq" />
            </div>
            <div class="main-list__list-item__container">
              <div class="main-list__item_content">
                <div class="main-list__list-item__container-header">
                  <div>
                    <a href="${psnInfoList[1].psnShortUrl }" target="_Blank"> <img
                      src="${psnInfoList[1].person.avatars }" class="main-list__list-item__container-avator"
                      onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
                    </a>
                  </div>
                  <div class="main-list__list-item__container-body">
                    <div class="main-list__list-item__container-name">
                      <a href="${psnInfoList[1].psnShortUrl }" target="_Blank" title="${psnInfoList[1].name }">
                        ${psnInfoList[1].name } </a>
                    </div>
                    <div style="width: 210px" class="main-list__list-item__container-duty"
                      title='${psnInfoList[1].person.insName }<c:if test="${!empty psnInfoList[1].person.position }">,&nbsp;${psnInfoList[1].person.position }</c:if>'>
                      ${psnInfoList[1].person.insName }
                      <c:if test="${!empty psnInfoList[1].person.position }">,&nbsp;${psnInfoList[1].person.position }</c:if>
                    </div>
                  </div>
                </div>
                <div class="main-list__list-item__container-target"></div>
              </div>
              <div class="main-list__list-item__container-footer">
                <button class="new-standard_normal-cancle" onclick="Rm.friend_neglet('${psnInfoList[1].des3PsnId }')">
                  <s:text name="friend.ignore" />
                </button>
                <button class="button_main button_primary-reverse"
                  onclick="Rm.friend_agree('${psnInfoList[1].des3PsnId }')">
                  <s:text name="friend.agree" />
                </button>
              </div>
            </div>
          </div>
        </s:if>
      </div>
    </div>
  </s:if>
  <s:if test="beforehand==1&&psnInfoList.size()>1">
    <div class="main-list__list item_vert-style item_no-border" style="display: none;">
      <div class="main-list__list-title">
        <s:text name="friend.tips.agreeReq" />
      </div>
      <div class="main-list__list-item__container">
        <div class="main-list__item_content">
          <div class="main-list__list-item__container-header">
            <div>
              <a href="${psnInfoList[1].psnShortUrl }" target="_Blank"> <img src="${psnInfoList[1].person.avatars }"
                class="main-list__list-item__container-avator"
                onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
              </a>
            </div>
            <div class="main-list__list-item__container-body">
              <div class="main-list__list-item__container-name">
                <a href="${psnInfoList[1].psnShortUrl }" target="_Blank" title="${psnInfoList[1].name }">
                  ${psnInfoList[1].name } </a>
              </div>
              <div style="width: 210px" class="main-list__list-item__container-duty"
                title='${psnInfoList[1].person.insName }<c:if test="${!empty psnInfoList[1].person.position }">,&nbsp;${psnInfoList[1].person.position }</c:if>'>
                ${psnInfoList[1].person.insName }
                <c:if test="${!empty psnInfoList[1].person.position }">,&nbsp;${psnInfoList[1].person.position }</c:if>
              </div>
            </div>
          </div>
          <div class="main-list__list-item__container-target"></div>
        </div>
        <div class="main-list__list-item__container-footer">
          <button class="new-standard_normal-cancle" onclick="Rm.friend_neglet('${psnInfoList[1].des3PsnId }')">
            <s:text name="friend.ignore" />
          </button>
          <button class="button_main button_primary-reverse" onclick="Rm.friend_agree('${psnInfoList[1].des3PsnId }')">
            <s:text name="friend.agree" />
          </button>
        </div>
      </div>
    </div>
  </s:if>
</s:if>
