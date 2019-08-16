<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count="${page.totalCount}"></div>
<!--列表循环 start  -->
<s:iterator value="msgShowInfoList" var="msil" status="st">
  <div style="cursor: pointer;" class="main-list__item <c:if test="${msil.status==0}"> item_selected</c:if>"
    onclick="MsgBase.clickMsgType(${msil.type},'',event,this);MsgBase.setOneReadMsg('${msil.msgRelationId}',0,event,this);">
    <div class="main-list__item_content">
      <div class="notification__box">
        <div class="notification__content">
          <c:if test="${msil.type==1}">
            <div class="notification__text">
              <span class="text_link-style" onclick="MsgBase.hrefUrl('${msil.senderShortUrl}',event)">
                ${msil.senderZhName} </span> <span><s:text name="dyn.msg.center.requestAddFriend" /> </span>
            </div>
          </c:if>
          <c:if test="${msil.type==2}">
            <div class="notification__text">
              <span><s:text name="dyn.msg.center.find" /></span> <span class="text_link-style">${msil.pubTitleZh}</span>
              <span><s:text name="dyn.msg.center.wait" /></span> <span class="text_link-style">${msil.resCount }</span>
              <span><s:text name="dyn.msg.center.findPub" /></span>
            </div>
          </c:if>
          <c:if test="${msil.type==3}">
            <div class="notification__text">
              <span><s:text name="dyn.msg.center.find" /></span> <span class="text_link-style">${msil.pubTitleZh}</span>
              <span><s:text name="dyn.msg.center.wait" /></span> <span class="text_link-style">${msil.resCount }</span>
              <span><s:text name="dyn.msg.center.findfulltext" /></span>
            </div>
          </c:if>
          <c:if test="${msil.type==4}">
            <div class="notification__text">
              <span><s:text name="dyn.msg.center.mayBeYouHaveFunPub" /></span> <a
                onclick="MsgBase.hrefUrl('${msil.pubShortUrl }',event)"> <span class="text_link-style">${msil.pubTitleZh}</span>
              </a>
            </div>
          </c:if>
          <c:if test="${msil.type==5}">
            <div class="notification__text">
              <span><s:text name="dyn.msg.center.mayBeYouKnow" /></span>
              <s:iterator value="#msil.rcmdFriendZhNameList" id="item" status="st">
                <span class="text_link-style"> <a
                  onclick="MsgBase.hrefUrl('${msil.rcmdFriendShortUrlList[st.index] }',event)"> <s:property
                      value="#item" />
                </a> <c:if test="${fn:length(msil.rcmdFriendZhNameList) ne st.index+1}">
            						、
            					 	</c:if>
                </span>
              </s:iterator>
            </div>
          </c:if>
          <c:if test="${msil.type==8}">
            <div class="notification__text" des3GrpId="<iris:des3 code="${msil.requestGrpId}" />">
              <span class="text_link-style" onclick="MsgBase.hrefUrl('${msil.senderShortUrl}',event)"><a>${msil.senderZhName}</a></span>
              <span><s:text name="dyn.msg.center.requestAddGrp" /></span> <a
                onclick="MsgBase.hrefUrl('${msil.grpShortUrl }',event)"> <span class="text_link-style">${msil.grpName}</span>
              </a>
            </div>
          </c:if>
          <c:if test="${msil.type==9}">
            <div class="notification__text">
              <span class="text_link-style" onclick="MsgBase.hrefUrl('${msil.senderShortUrl}',event)"><a>${msil.senderZhName}</a></span>
              <span><s:text name="dyn.msg.center.inviteYouAddGrp" /></span> <a
                onclick="MsgBase.hrefUrl('${msil.grpShortUrl }',event)"> <span class="text_link-style">${msil.grpName}</span>
              </a>
            </div>
          </c:if>
          <div class="notification__actions">
            <div class="notification__time">
              <fmt:formatDate value="${msil.createDate }" pattern="yyyy-MM-dd HH:mm" />
            </div>
            <div class="notification__settings">
              <c:if test="${msil.status==0}">
                <div class="notification__settings_item material-icons dev_one_read"
                  onclick="MsgBase.setOneReadMsg('${msil.msgRelationId}',1,event,this)">fiber_manual_record</div>
              </c:if>
              <div class="notification__settings_item material-icons"
                onclick="MsgBase.delMsg('${msil.msgRelationId}',event,this)">delete</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</s:iterator>