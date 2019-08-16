<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count='${page.totalCount}'></div>
<s:iterator value="page.result" var="pub" status="st">
  <div class="main-list__item" des3PubId='<iris:des3 code="${pub.pubId}"/>' existGrpPub=${pub.existGrpPub }>
    <div class="main-list__item_checkbox">
      <div class="input-custom-style">
        <input type="checkbox" name="pub-type"
          <c:if test="${pub.isImport==1}">
                  		disabled="true"    checked
                  	</c:if>>
        <i class="material-icons custom-style"></i>
      </div>
    </div>
    <div class="main-list__item_content">
      <div class="pub-idx_medium">
        <div class="pub-idx__base-info">
          <div class="pub-idx__full-text_box">
            <div class="pub-idx__full-text_img" style="position: relative;">
              <s:if test="#pub.hasFulltext==1">
                <s:if test="#pub.fullTextImaUrl!=null">
                  <img src="${pub.fullTextImaUrl }" />
                </s:if>
                <s:else>
                  <img src="/resscmwebsns/images_v5/images2016/file_img1.jpg" />
                  <!-- <div class="pub-idx__full-text_img-tip">该成果已导入群组</div> -->
                </s:else>
              </s:if>
              <s:else>
                <img src="/resscmwebsns/images_v5/images2016/file_img.jpg" />
                <!-- <div class="pub-idx__full-text_img-tip">该成果已导入群组</div> -->
              </s:else>
              <c:if test="${pub.isImport!=1}">
                <s:if test="#pub.existGrpPub==1">
                  <div class="pub-idx__full-text_img-tip">
                    <s:text name="groups.pub.member.imported" />
                  </div>
                </s:if>
              </c:if>
            </div>
          </div>
          <div class="pub-idx__main_box">
            <div class="pub-idx__main">
              <div class="pub-idx__main_title">
                <s:if test="#pub.pubIndexUrl!=null">
                  <a class="pub_info_title" href="${pub.pubIndexUrl}" target="_Blank">
                </s:if>
                <s:else>
                  <a onclick="DiscussOpenDetail.openPubDetail('<iris:des3 code="${pub.pubId}"/>',event)">
                </s:else>
                <s:if test="#locale=='zh_CN'">
                  <s:if test="#pub.zhTitle==null || #pub.zhTitle==''">${pub.enTitle }</s:if>
                  <s:else>${pub.zhTitle }</s:else>
                </s:if>
                <s:else>
                  <s:if test="#pub.enTitle==null || #pub.enTitle==''">${pub.zhTitle }</s:if>
                  <s:else>${pub.enTitle }</s:else>
                </s:else>
                </a>
              </div>
              <div class="pub-idx__main_author">${pub.authors }</div>
              <div class="pub-idx__main_src">${pub.zhBrif }</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</s:iterator>