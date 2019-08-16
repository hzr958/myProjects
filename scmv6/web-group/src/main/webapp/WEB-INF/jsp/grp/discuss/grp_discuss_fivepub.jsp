<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	var buttonlist = document.getElementsByClassName("button_primary");
	clickDynamicreact(buttonlist);
});
</script>
<s:if test="grpPubShowInfoList.size>0">
  <div class="module-card__box">
    <div class="module-card__header">
      <div class="module-card-header__title">
        <s:text name='groups.base.grpPub' />
      </div>
      <button class="button_main button_link" onclick="GrpBase.toGrpPub(this)">
        <s:text name='groups.base.more.grpPub' />
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
                    <div class="pub-idx__main_title  pub-idx__main_title-multipleline pub-idx__main_title-multipleline-box" style="height: 40px; overflow: hidden;"> 
                     
                      <s:if test="#locale=='zh_CN'">
                           <a class="multipleline-ellipsis__content-box"  onclick="DiscussOpenDetail.openPubDetail('<iris:des3 code="${pub.pubId}"/>',event)"  title="${pub.zhTitle }"  style="white-space: inherit;"> 
                          <s:if test="#pub.zhTitle==null || #pub.zhTitle==''">${pub.enTitle }</s:if>
                          <s:else>${pub.zhTitle }</s:else>
                        </s:if> 
                        <s:else>
                          <a class="multipleline-ellipsis__content-box"  onclick="DiscussOpenDetail.openPubDetail('<iris:des3 code="${pub.pubId}"/>',event)"  title="${pub.enTitle }"   style="white-space: inherit;"> 
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
          <div class="main-list__item_actions" style="display: none; ">
            <button class="button_main button_dense button_primary" style="opacity: 0;" disabled="disabled"
              onclick="GrpDiscussList.dynCollectPub('<iris:des3 code="${pub.pubId}"/>','pub',event)">
              <s:text name='groups.base.savePub' />
            </button>
          </div>
        </div>
      </s:iterator>
    </div>
  </div>
</s:if>