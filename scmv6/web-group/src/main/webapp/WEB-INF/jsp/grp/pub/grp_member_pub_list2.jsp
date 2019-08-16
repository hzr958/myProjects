<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count='${page.totalCount}'></div>
<s:iterator value="page.result" var="pub" status="st">
  <div class="group-achive_body-content_item main-list__item dev_pub_grp_${pub.pubId}" id="dev_pub_grp_${pub.des3PubId}"
    style="padding: 12px 16px !important; height: 112px;" hasFulltext="${pub.hasFulltext }" isOwn="${pub.isOwn }"
    des3RecvPsnId=${pub.des3RecvPsnId } des3PubId='<iris:des3 code="${pub.pubId}"/>' existGrpPub=${pub.existGrpPub }
    fullTextPermission="${pub.fullTextPermission }">
    <div class="group-achive_body-content_checked-contaioner">
      <div
        class="group-achive_body-content_item-selector <c:if test="${pub.isImport==1}"> item-selected_forbid-tip </c:if>"></div>
      <c:if test="${pub.isImport!=1}">
        <s:if test="#pub.existGrpPub==1">
          <i class="group-achive_body-content_repeat-tip" title="<s:text name='groups.pub.member.imported'/>"></i>
        </s:if>
      </c:if>
    </div>
    <div class="group-achive_body-content_item-avator" style="position: relative; text-align: center;">
      <s:if test="#pub.hasFulltext==1 && (#pub.isOwn==1 || #pub.fullTextPermission!=2)">
        <a href="${pub.fullTextUrl}"> <s:if test="#pub.fullTextImaUrl!=null">
            <img src="${pub.fullTextImaUrl }" onerror="this.src='/resscmwebsns/images_v5/images2016/file_img1.jpg'"
              title='<s:text name="groups.pub.member.download.fulltext"/>' />
          </s:if> <s:else>
            <img src="/resscmwebsns/images_v5/images2016/file_img1.jpg"
              title='<s:text name="groups.pub.member.download.fulltext"/>' />
          </s:else>
        </a>
        <a href="${pub.fullTextUrl}" class="new-tip_container-content"
          title='<s:text name="groups.pub.member.download.fulltext"/>'> <img
          src="/resmod/smate-pc/img/file_ upload1.png" class="new-tip_container-content_tip1"> <img
          src="/resmod/smate-pc/img/file_ upload.png" class="new-tip_container-content_tip2">
        </a>
      </s:if>
      <s:else>
        <s:if test="#pub.isOwn==0">
          <a class="dev_no_fulltext"> <img src="${pub.fullTextImaUrl }"
            onerror="/resscmwebsns/images_v5/images2016/file_img.jpg"
            title='<s:text name="groups.pub.member.request.fulltext"/>'>
          </a>
          <div class="pub_uploadFulltext  pub-idx__full-text__tip pub-idx__full-text__tip1 dev_update_text_event">
            <div class="fileupload__box fileupload__box-border"
              title='<s:text name="groups.pub.member.request.fulltext"/>' style="border: none;">
              <div class="fileupload__core initial_shown">
                <div class="fileupload__initial">
                  <img src="/resmod/smate-pc/img/file_ upload1.png" class="request-add__tip1_avator"> <img
                    src="/resmod/smate-pc/img/file_ upload.png" class="request-add__tip2_avator"> <input
                    type="file" class="fileupload__input" style="display: none;">
                </div>
              </div>
            </div>
          </div>
        </s:if>
        <s:if test="#pub.isOwn==1">
          <a class="dev_no_fulltext"> <img src="${pub.fullTextImaUrl }"
            onerror="/resscmwebsns/images_v5/images2016/file_img.jpg"
            title='<s:text name="groups.pub.member.upload.fulltext"/>'>
          </a>
          <div class="pub_uploadFulltext  pub-idx__full-text__tip pub-idx__full-text__tip1 dev_update_text_event"
            des3Id="${pub.pubId}">
            <div class="fileupload__box fileupload__box-border" onclick="GrpPub.fileuploadBoxOpenInputClick(event)"
              title='<s:text name="groups.pub.member.upload.fulltext"/>' style="border: none;">
              <div class="fileupload__core initial_shown">
                <div class="fileupload__initial">
                  <img src="/resmod/smate-pc/img/file_ upload1.png" class="request-add__tip1_avator"> <img
                    src="/resmod/smate-pc/img/file_ upload.png" class="request-add__tip2_avator"> <input
                    type="file" class="fileupload__input" style="display: none;">
                </div>
                <div class="fileupload__progress">
                  <canvas width="56" height="56"></canvas>
                  <div class="fileupload__progress_text"></div>
                </div>
                <div class="fileupload__saving">
                  <div class="preloader"></div>
                  <div class="fileupload__saving-text"></div>
                </div>
                <div class="fileupload__finish"></div>
                <div class="fileupload__hint-text" style="display: none">添加全文</div>
              </div>
            </div>
          </div>
        </s:if>
      </s:else>
      <c:if test="${pub.labeld==1 }">
        <span class="pub-idx__icon_grant-mark" style="margin: 0px;" title = "<s:text name='groups.pub.member.financialInstitutionMark'/>"></span>
      </c:if>
      <c:if test="${pub.updateMark==1}">
        <i class="demo-tip_onlineimport-icon" style="margin: 0px;"
          title="<s:text name='groups.pub.member.updatemark1' /> "></i>
      </c:if>
      <c:if test="${pub.updateMark==2}">
        <i class="demo-tip_inlineimport-icon" style="margin: 0px;"
          title="<s:text name='groups.pub.member.updatemark2' /> "></i>
      </c:if>
    </div>
    <div class="group-achive_body-content_item-infor">
      <div class="group-achive_body-content_item-title multipleline-ellipsis" style="height: 40px; overflow: hidden;">
        <div class="multipleline-ellipsis__content-box">
          <a href="${pub.pubIndexUrl}" target="_Blank" title="${pub.showTitle }">${pub.showTitle }</a>
        </div>
      </div>
      <div class="group-achive_body-content_item-author" title="${pub.noneHtmlLableAuthorNames }">${pub.authors }</div>
      <div class="group-achive_body-content_item-time" title="${pub.showBrif }">${pub.showBrif }</div>
    </div>
  </div>
</s:iterator>