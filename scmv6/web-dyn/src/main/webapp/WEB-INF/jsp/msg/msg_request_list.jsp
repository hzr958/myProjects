<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count="${page.totalCount}"></div>
<s:iterator value="msgShowInfoList" var="msil">
  <s:if test="language== 'zh'  ">
    <div class="main-list__item" id='<s:property value="pubId"/>' msgRelationId='<s:property value="msgRelationId" />'
      style="padding: 0px 36px; border-bottom: 1px solid #ddd;">
      <div class="main-list__item_content">
        <div class="request__box">
          <div class="request__header">
            <s:text name="dyn.msg.center.toYouRequestFulltextPre" />
            <a class="request__psn-name request__psn-name_link" href='<s:property value="senderShortUrl" />'
              target="_blank"> ${senderZhName} </a>
            <s:text name="dyn.msg.center.toYouRequestFulltextSuff" />
          </div>
          <div class="request__pub" style="width: 910px;">
            <div class="pub-idx_medium">
              <div class="pub-idx__base-info">
                <div class="pub-idx__full-text_box">
                  <s:if test="hasPubFulltext =='false' ">
                    <div class="pub-idx__full-text_img" style="cursor: default;">
                      <img src="/resmod/images_v5/images2016/file_img.jpg"
                        onerror="this.onerror=null;this.src='/resmod/images_v5/images2016/file_img.jpg'">
                    </div>
                  </s:if>
                  <s:elseif test='pubFulltextImagePath =="" ||  pubFulltextImagePath==null '>
                    <div class="pub-idx__full-text_img"
                      onclick="MsgBase.downloadPubFullText('<iris:des3 code='${msil.pubId }' />')">
                      <img src="/resmod/images_v5/images2016/file_img1.jpg"
                        onerror="this.onerror=null;this.src='/resmod/images_v5/images2016/file_img.jpg'">
                    </div>
                  </s:elseif>
                  <s:else>
                    <div class="pub-idx__full-text_img"
                      onclick="MsgBase.downloadPubFullText('<iris:des3 code='${msil.pubId }' />')">
                      <img src='<s:property value="pubFulltextImagePath" />'
                        onerror="this.onerror=null;this.src='/resmod/images_v5/images2016/file_img.jpg'">
                    </div>
                  </s:else>
                </div>
                <div class="pub-idx__main_box">
                  <div class="pub-idx__main">
                    <div class="pub-idx__main_title pub-idx__main_ellipsis" style="height: 40px; overflow: hidden; line-height: 20px;">
                      <a class="multipleline-ellipsis__content-box" href='<s:property value="pubShortUrl" />' target="_blank"  title="${pubTitleZh }"> <s:property value="pubTitleZh"
                          escapeHtml="false" />
                      </a>
                    </div>
                    <div class="pub-idx__main_author" title="${noneHtmlLablepubAuthorName }">
                      <s:property value="pubAuthorName" escapeHtml="false" />
                    </div>
                    <div class="pub-idx__main_src" title="${pubBriefZh }">
                      <s:property value="pubBriefZh" escapeHtml="false" />
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <s:if test="hasPubFulltext == 'true'  ">
        <div class="main-list__item_actions">
          <div class="request__actions">
            <button class="button_main button_dense button_grey"
              onclick="MsgBase.optFulltextRequest('<s:property value="msgRelationId" />',2 ,this )">
              <s:text name="dyn.msg.center.ignore" />
            </button>
            <button class="button_main button_dense button_primary-changestyle"
              onclick="MsgBase.optFulltextRequest('<s:property value="msgRelationId" />',1 ,this)">
              <s:text name="dyn.msg.center.accept" />
            </button>
          </div>
        </div>
      </s:if>
      <s:else>
        <div class="main-list__item_actions">
          <div class="request__actions">
            <button class="button_main button_dense button_grey"
              onclick="MsgBase.optFulltextRequest('<s:property value="msgRelationId" />',2 ,this )">
              <s:text name="dyn.msg.center.ignore" />
            </button>
            <button class="button_main button_dense button_primary-changestyle"
              onclick="MsgBase.SelectFulltextUpload(this);MsgBase.setOneReadMsg('<s:property value="msgRelationId" />',null,null)">
              <s:text name="dyn.msg.center.upload" />
            </button>
          </div>
        </div>
      </s:else>
    </div>
  </s:if>
  <s:else>
    <div class="main-list__item" id='<s:property value="pubId"/>' msgRelationId='<s:property value="msgRelationId" />'
      style="padding: 0px 36px; border-bottom: 1px solid #ddd;">
      <div class="main-list__item_content">
        <div class="request__box">
          <div class="request__header">
            <s:text name="dyn.msg.center.toYouRequestFulltextPre" />
            <a class="request__psn-name" href='<s:property value="senderShortUrl" />' target="_blank">
              ${senderEnName} </a>
            <s:text name="dyn.msg.center.toYouRequestFulltextSuff" />
          </div>
          <div class="request__pub" style="width: 910px;">
            <div class="pub-idx_medium">
              <div class="pub-idx__base-info">
                <div class="pub-idx__full-text_box">
                  <s:if test="hasPubFulltext =='false' ">
                    <div class="pub-idx__full-text_img" style="cursor: default;">
                      <img src="/resmod/images_v5/images2016/file_img.jpg"
                        onerror="this.onerror=null;this.src='/resmod/images_v5/images2016/file_img.jpg'">
                    </div>
                  </s:if>
                  <s:elseif test='pubFulltextImagePath =="" ||  pubFulltextImagePath==null '>
                    <div class="pub-idx__full-text_img"
                      onclick="MsgBase.downloadPubFullText('<iris:des3 code='${msil.pubId }' />')">
                      <img src="/resmod/images_v5/images2016/file_img1.jpg"
                        onerror="this.onerror=null;this.src='/resmod/images_v5/images2016/file_img.jpg'">
                    </div>
                  </s:elseif>
                  <s:else>
                    <div class="pub-idx__full-text_img"
                      onclick="MsgBase.downloadPubFullText('<iris:des3 code='${msil.pubId }' />')">
                      <img src='<s:property value="pubFulltextImagePath" />'
                        onerror="this.onerror=null;this.src='/resmod/images_v5/images2016/file_img.jpg'">
                    </div>
                  </s:else>
                </div>
                <div class="pub-idx__main_box">
                  <div class="pub-idx__main">
                    <div class="pub-idx__main_title pub-idx__main_ellipsis" style="height: 40px; overflow: hidden; line-height: 20px;">
                      <a class="multipleline-ellipsis__content-box" href='<s:property value="pubShortUrl" />' target="_blank" title="${pubTitleEn }"> <s:property value="pubTitleEn"
                          escapeHtml="false" />
                      </a>
                    </div>
                    <div class="pub-idx__main_author" title="${noneHtmlLablepubAuthorName }">
                      <s:property value="pubAuthorName" escapeHtml="false" />
                    </div>
                    <div class="pub-idx__main_src" title="${pubBriefEn }">
                      <s:property value="pubBriefEn" escapeHtml="false" />
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <s:if test="hasPubFulltext == 'true'  ">
        <div class="main-list__item_actions">
          <div class="request__actions">
            <button class="button_main button_dense button_grey"
              onclick="MsgBase.optFulltextRequest('<s:property value="msgRelationId" />',2 ,this)">
              <s:text name="dyn.msg.center.ignore" />
            </button>
            <button class="button_main button_dense button_primary-changestyle"
              onclick="MsgBase.optFulltextRequest('<s:property value="msgRelationId" />',1 ,this)">
              <s:text name="dyn.msg.center.accept" />
            </button>
          </div>
        </div>
      </s:if>
      <s:else>
        <div class="main-list__item_actions">
          <div class="request__actions">
            <button class="button_main button_dense button_grey"
              onclick="MsgBase.optFulltextRequest('<s:property value="msgRelationId" />',2 ,this )">
              <s:text name="dyn.msg.center.ignore" />
            </button>
            <button class="button_main button_dense button_primary-changestyle"
              onclick="MsgBase.SelectFulltextUpload(this);MsgBase.setOneReadMsg('<s:property value="msgRelationId" />',null,null)">
              <s:text name="dyn.msg.center.upload" />
            </button>
          </div>
        </div>
      </s:else>
    </div>
  </s:else>
</s:iterator>
