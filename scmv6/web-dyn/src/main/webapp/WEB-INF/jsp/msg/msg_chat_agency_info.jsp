<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="inbox-chat__record_content">
  <div class="inbox-chat__record_text">
    <div class="pub-idx_medium">
      <div class="pub-idx__base-info">
        <div class="pub-idx__full-text_box">
          <div class="pub-idx__full-text_img">
            <a href="/prjweb/fundAgency/detailMain?Des3FundAgencyId=<iris:des3 code="${msil.agencyId}"/>"
              target="_blank"> <img src="${msil.agencyLogoUrl}"
              onerror="this.onerror=null;this.src='${resmod}/smate-pc/img/logo_instdefault.png'">
            </a>
          </div>
        </div>
        <div class="pub-idx__main_box">
          <div class="pub-idx__main">
            <!-- <div class="pub-idx__main_title"> -->
            <a class="pub-idx__main_titlea" href="/prjweb/fundAgency/detailMain?Des3FundAgencyId=<iris:des3 code="${msil.agencyId}"/>"
                target="_blank">${msil.showAgencyTitle }</a>
            <!-- </div> -->
            <div class="pub-idx__main_author">${msil.showDesc }</div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <s:if test="#msil.IAmSender==1">
    <div class="inbox-chat__record_delete material-icons" onclick="MsgBase.delMsgContent('${msil.msgRelationId}',event)">delete</div>
  </s:if>
</div>