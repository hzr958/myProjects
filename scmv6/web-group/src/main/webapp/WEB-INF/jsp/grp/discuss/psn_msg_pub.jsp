<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count='${page.totalCount}'></div>
<s:iterator value="page.result" var="pub" status="st">
  <div class="main-list__item" pubId=${pub.pubId } des3PubId="${pub.des3PubId }">
    <div class="main-list__item_checkbox">
      <div class="input-custom-style">
        <s:if test="isRadio">
          <c:if test="${pub.permission!=4}">
            <!--  隐私的不显示-->
            <input name="pub-type" style="background-color: red" type="radio" class=''>
          </c:if>
          <c:if test="${pub.permission==4}">
            <input name="pub-type" style="background-color: red" readonly="true" disabled="disabled" type="radio" class=''
                   title='<c:if test="${pub.isOwn==1 }"><s:text name='groups.base.pubShareContent' /></c:if><c:if test="${pub.isOwn==0 }"><s:text name='groups.base.notOwenerShare' /></c:if>'>
          </c:if>
        </s:if>
        <s:else>
          <input name="pub-type" style="background-color: red" type="checkbox" class='<c:if test="${pub.permission==4}">input-custom-style_nopointer</c:if>'>
        </s:else>
        <i class='material-icons custom-style <c:if test="${pub.permission==4}">input-custom-style_nopointer</c:if>'></i>
      </div>
      
    </div>
    <div class="main-list__item_content">
      <div class="pub-idx_medium no-full-text">
        <div class="pub-idx__base-info">
          <div class="pub-idx__full-text_box">
            <div class="pub-idx__full-text_img">
              <s:if test="pub.hasFulltext==1">
                <s:if test="pub.fullTextImaUrl!=null">
                  <img src="${pub.fullTextImaUrl }" />
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
                <a href="${pub.pubIndexUrl }" target="_Blank"> <s:if test="#locale=='zh_CN'">
                    <s:if test="#pub.zhTitle==null||#pub.zhTitle==''">${pub.enTitle }</s:if>
                    <s:else>${pub.zhTitle }</s:else>
                  </s:if> <s:else>
                    <s:if test="#pub.enTitle==null ||#pub.enTitle==''">${pub.zhTitle }</s:if>
                    <s:else>${pub.enTitle }</s:else>
                  </s:else>
                </a>
                <c:if test="${pub.permission==4}">
                   <i class="selected-func_close-pointernone" title='<c:if test="${pub.isOwn==1 }"><s:text name='groups.base.pubShareContent' /></c:if><c:if test="${pub.isOwn==0 }"><s:text name='groups.base.notOwenerShare' /></c:if>'></i>
                </c:if>
               
              </div>
              <div class="pub-idx__main_author">${pub.authors }</div>
              <div class="pub-idx__main_src">${pub.zhBrif}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</s:iterator>
