<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s:if test="grpPubShowInfoList.size>0">
  <div class="module-card__box">
    <div class="module-card__header">
      <div class="module-card-header__title">
        <s:text name='groups.outside.discuss.pub' />
      </div>
      <button class="button_main button_link" onclick="GrpBase.toOutsideGrpPub()">
        <s:text name='groups.outside.more.grpPub' />
      </button>
    </div>
    <div class="main-list__list">
      <s:iterator value="grpPubShowInfoList" var="pub" status="st">
        <div class="main-list__item">
          <div class="main-list__item_content">
            <div class="pub-idx_x-small">
              <div class="pub-idx__base-info">
                <div class="pub-idx__full-text_box">
                  <div class="pub-idx__full-text_img">
                    <s:if test="pub.hasFulltext==1">
                      <s:if test="pub.fullTextImaUrl!=null">
                        <img src="${pub.fullTextImaUrl }" />
                      </s:if>
                      <s:else>
                        <img src="" />
                      </s:else>
                    </s:if>
                    <s:else>
                      <img src="" />
                    </s:else>
                  </div>
                </div>
                <div class="pub-idx__main_box">
                  <div class="pub-idx__main">
                    <div class="pub-idx__main_title">
                      <a onclick="openPubDetail('<iris:des3 code="${pub.pubId}"/>',event)" title='<s:if test="#locale=='zh_CN'">${pub.zhTitle }</s:if><s:else>${pub.enTitle }</s:else>'> <s:if
                          test="#locale=='zh_CN'">
                          <s:if test="#pub.zhTitle==null || #pub.zhTitle==''">${pub.enTitle }</s:if>
                          <s:else>${pub.zhTitle }</s:else>
                        </s:if> <s:else>
                          <s:if test="#pub.enTitle==null || #pub.enTitle==''">${pub.zhTitle }</s:if>
                          <s:else>${pub.enTitle }</s:else>
                        </s:else>
                      </a>
                    </div>
                    <div class="pub-idx__main_author" title="${pub.noneHtmlLableAuthorNames }">${pub.authors }</div>
                    <div class="pub-idx__main_src" title="${pub.zhBrif }" style="display:block;">${pub.zhBrif}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </s:iterator>
    </div>
  </div>
</s:if>