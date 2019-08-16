<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count='${count}'></div>
<s:iterator value="pubInfo" var="pub" status="st">
  <div class="main-list__item" pubId=${pub.pubId } des3PubId="${pub.des3PubId }">
    <div class="main-list__item_checkbox">
      <div class="input-custom-style">
        <input name="pub-type" style="background-color: red" type="checkbox" class="">
        <i class="material-icons custom-style "></i>
      </div>
      
    </div>
    <div class="main-list__item_content">
      <div class="pub-idx_medium no-full-text">
        <div class="pub-idx__base-info">
          <div class="pub-idx__full-text_box">
            <div class="pub-idx__full-text_img">
              <%--<s:if test="pub.hasFulltext==1">
                <img src="/resscmwebsns/images_v5/images2016/file_img1.jpg" />
              </s:if>
              <s:else>
                <img src="/resscmwebsns/images_v5/images2016/file_img.jpg" />
              </s:else>--%>
            </div>
          </div>
          <div class="pub-idx__main_box">
            <div class="pub-idx__main">
              <div class="pub-idx__main_title">
                <a href="${pub.pubUrl }" target="_Blank">
                   ${pub.title }
                </a>
              </div>
              <div class="pub-idx__main_author">${pub.authorNames }</div>
              <div class="pub-idx__main_src">${pub.briefDesc}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</s:iterator>
