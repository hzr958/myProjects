<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
var $createGrpChipBox;
$(document).ready(function(){
	//关键字自动完成加载插件.
	addFormElementsEvents(document.getElementById("keyWordsBox"));
	/* dealKeywordSaveBtnStatus(); */
	$createGrpChipBox=window.ChipBox({
        name:"oneKeyWords", 
        maxItem:10,
       callbacks:{
            compose: function(){
                $("#homepage_keywords_save_btn").removeAttr("disabled");
            }
       }
            /* remove: function(){
                var keyCount = $("#oneKeyWords").find(".chip__box").length;
                if(keyCount > 0){
                    $("#homepage_keywords_save_btn").removeAttr("disabled");
                }else{
                    $("#homepage_keywords_save_btn").attr("disabled", "true");
                }
            }
        }*/
    });
});


</script>
<div class="dialogs__box" style="width: 520px;" id="keyWordsBox" dialog-id="keyWordsBox" cover-event="hide">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="dialogs__header_title">
        <s:text name='homepage.profile.box.title.select.expertise' />
      </div>
    </div>
  </div>
  <div class="dialogs__childbox_adapted">
    <div class="dialogs__content global__padding_24">
      <div class="input__box no-input-area" style="margin-bottom: 48px;">
        <label class="input__title"><s:text name='homepage.profile.box.title.select.ten.expertise.recommend' /></label>
        <div class="chip-panel__box" id="oneKeyWords" chipbox-id="oneKeyWords">
          <!-- 关键词 -->
          <c:if test="${!empty keywords }">
            <c:forEach items="${keywords}" var="keyword" varStatus="status">
              <div class="chip__box psnkeyword_show" id="boxkeywords_${status.index }">
                <div class="chip__avatar">
                  <!-- <img src="/img/avatar.jpg"> -->
                </div>
                <div class="chip__text keyWordsBox_text"><c:out value="${keyword.keyWords }"/></div>
                <div class="chip__icon icon_delete" onclick="javascript:removeKeywords('boxkeywords_${status.index }');">
                  <i class="material-icons">close</i>
                </div>
              </div>
            </c:forEach>
          </c:if>
          <div class="chip-panel__manual-input js_autocompletebox" id="autokeywords"
            request-url="/groupweb/mygrp/ajaxautoconstkeydiscs" contenteditable="true"></div>
        </div>
        <div class="global-hint-text"></div>
      </div>
      <div class="input__box no-input-area psn_rcmd_keywords">
        <c:if test="${!empty recommendKeywords }">
          <label class="input__title"><s:text name='homepage.profile.box.title.recommended.expertise' /></label>
          <div class="chip-panel__box inline-style">
            <c:forEach items="${recommendKeywords}" var="recommendKeyword" varStatus="status">
              <div class="chip__box white-style recommendKeywordItem" id="recommendKeywords_${status.index }"
                style="${status.index > 4 ? 'display:none;' : ''} cursor: pointer;"
                onclick="javascript:addkeyWordsBox('recommendKeywords_${status.index }', '${recommendKeyword }',$createGrpChipBox);">
                <div class="chip__avatar">
                  <!-- <img src="/img/avatar.jpg"> -->
                </div>
                <div class="chip__text">${recommendKeyword }</div>
                <div class="chip__icon icon_add">
                  <i class="material-icons">add</i>
                </div>
              </div>
            </c:forEach>
          </div>
          <div class="global-hint-text"></div>
        </c:if>
      </div>
    </div>
  </div>
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__footer">
      <button class="button_main button_primary-reverse" onclick="javascript: saveKeyWordsBox(this);"
        id="homepage_keywords_save_btn">
        <s:text name='homepage.profile.btn.save' />
      </button>
      <button class="button_main button_primary-cancle" onclick="javascript:hideKeyWordsBox(this);">
        <s:text name='homepage.profile.btn.cancel' />
      </button>
    </div>
  </div>
</div>