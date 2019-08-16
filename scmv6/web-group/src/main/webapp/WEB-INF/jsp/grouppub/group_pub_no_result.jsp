<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s:if
  test="groupInvitePsn != null &&(groupPsn.pubViewType==1 || groupInvitePsn.groupRole==1 || groupInvitePsn.groupRole==2)">
  <div class="ach_main_search_${locale}">
    <h2>
      <s:text name="group.tip.noRecord.pub.title" />
    </h2>
    <ul class="ach_search_list_${locale}">
      <li>
        <!-- 成果检索 --> <a id="search_publication_togroup_op33"
        class="icon_ach01<c:if test='${locale=="en_US"}'> mleft40</c:if>"></a>
        <h4>
          <a id="search_publication_togroup_op22" class="Blue"><s:text name="group.noRecord.title.tip.searchFromDB" />
            <c:if test='${locale=="en_US"}'>
              <br />
              <br />
            </c:if> </a>
        </h4>
        <p>
          <s:text name="group.tip.noRecord.pub.item.importOnlineNew"></s:text>
        </p>
      </li>
      <li>
        <!-- 手工录入 --> <a onclick="toPubCollect()" class="icon_ach02<c:if test='${locale=="en_US"}'> mleft40</c:if>"></a>
        <h4>
          <a onclick="toPubCollect()" class="Blue"><s:text name="group.tip.noRecord.pub.enterManually" /></a>
        </h4>
        <p>
          <s:text name="group.tip.noRecord.pub.item.enterManuallyNew"></s:text>
        </p>
      </li>
      <li>
        <!-- 文件导入 --> <a
        href="/pubweb/group/ajaxMyPubList?articleType=1&groupId=${groupPsn.groupId}&TB_iframe=true&height=435&width=800"
        class="icon_ach04 thickbox<c:if test='${locale=="en_US"}'> mleft40</c:if>"
        title="<s:text name='group.tip.noRecord.pub.importPub' />" id="importPicLink"></a>
        <h4>
          <a
            href="/pubweb/group/ajaxMyPubList?articleType=1&groupId=${groupPsn.groupId}&TB_iframe=true&height=435&width=800"
            class="thickbox Blue" title="<s:text name='group.tip.noRecord.pub.importPub' />" id="importLink"> <s:text
              name="group.tip.noRecord.pub.importPub" />
          </a>
        </h4>
        <p>
          <s:text name="group.tip.noRecord.pub.item.importFromFile2"></s:text>
        </p>
      </li>
    </ul>
    <div class="clear"></div>
  </div>
</s:if>
<s:else>
  <div class="confirm_words" style="width: 694px;">
    <div class="norecord_tips">
      <s:text name="group.tip.noRecord3" />
    </div>
  </div>
</s:else>