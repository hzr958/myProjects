<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count='${page.totalCount}'></div>
<s:iterator value="page.result" var="pubInfo" status="st">
  <div class="main-list__item grp_pub_rcmd_info" pubId="${pubInfo.pubId }">
    <div class="main-list__item_content">
      <div class="pub-idx_small">
        <div class="pub-idx__base-info">
          <div class="pub-idx__full-text_box">
            <div class="pub-idx__full-text_img">
              <s:if test="#pub.hasFulltext==1">
                <s:if test="#pub.fullTextImaUrl!=null">
                  <img src="${pubInfo.fullTextImaUrl }" />
                </s:if>
                <s:else>
                  <img src="/resscmwebsns/images_v5/images2016/file_img1.jpg" />
                </s:else>
              </s:if>
              <s:else>
                <img src="/resscmwebsns/images_v5/images2016/file_img.jpg" />
              </s:else>
            </div>
          </div>
          <div class="pub-idx__main_box">
            <div class="pub-idx__main">
              <div class="pub-idx__main_title">
                <a onclick="DiscussOpenDetail.openPubDetail('<iris:des3 code="${pubInfo.pubId}"/>',event)"> <s:if
                    test="#locale=='zh_CN'">
                    <s:if test="#pubInfo.zhTitle==null || #pubInfo.zhTitle==''">${pubInfo.enTitle }</s:if>
                    <s:else>${pubInfo.zhTitle }</s:else>
                  </s:if> <s:else>
                    <s:if test="#pubInfo.enTitle==null || #pubInfo.enTitle==''">${pubInfo.zhTitle }</s:if>
                    <s:else>${pubInfo.enTitle }</s:else>
                  </s:else>
                </a>
              </div>
              <div class="pub-idx__main_author">${pubInfo.authors }</div>
              <div class="pub-idx__main_src">${pubInfo.zhBrif }</div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="main-list__item_actions">
      <button class="button_main button_dense" onclick="GrpPub.optionRcmdGrpPub(${pubInfo.pubId},2)">忽略</button>
      <button class="button_main button_dense button_primary-reverse"
        onclick="GrpPub.optionRcmdGrpPub(${pubInfo.pubId},1)">同意</button>
    </div>
  </div>
</s:iterator>