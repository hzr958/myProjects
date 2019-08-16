<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(function(){
    var targetlist = document.getElementsByClassName("searchbox__main");
    for(var i = 0; i< targetlist.length; i++){
        targetlist[i].querySelector("input").onfocus = function(){
            this.closest(".searchbox__main").style.cssText = "border:1px solid #3faffa;";
        }
        targetlist[i].querySelector("input").onblur = function(){
            this.closest(".searchbox__main").style.cssText = "border:1px solid #ccc;";  
        }
    }
});
</script>
<div class="dialogs__childbox_fixed">
  <div class="dialogs__header">
    <div class="dialogs__header_title">
      <spring:message code='homepage.profile.title.select.featured.publications' />
    </div>
  </div>
</div>
<div class="dialogs__childbox_adapted">
  <div class="dialogs__content global__padding_24">
    <div class="sugg-picker">
      <div class="sugg-picker__header">
        <spring:message code='homepage.profile.note.select.featured.publications' />
      </div>
      <div class="sugg-picker__panel">
        <div class="sugg-panel">
          <div class="sugg-panel__title">
            <div class="searchbox__container main-list__searchbox" list-search="psnOpenPubList">
              <div class="searchbox__main">
                <input placeholder="<spring:message code='homepage.profile.note.search.pub'/>">
                <div class="searchbox__icon material-icons"></div>
              </div>
            </div>
          </div>
          <div class="sugg-panel__content" id="openPubListContent" style="height: 480px;">
            <div class="main-list__list global_no-border" list-main="psnOpenPubList" id="psnOpenPubList"></div>
          </div>
        </div>
        <div class="sugg-panel right-panel">
          <div class="sugg-panel__title">
            <spring:message code='homepage.profile.note.selected.pub' />
          </div>
          <div class="sugg-panel__content">
            <div class="main-list__list global_no-border" id="addedPubList">
              <c:if test="${!empty pubVO.pubInfoList}">
                <c:forEach items="${pubVO.pubInfoList }" var="representPub" varStatus="status">
                  <div class="main-list__item" des3pubid="${representPub.des3PubId }">
                    <div class="main-list__item_content">
                      <div class="pub-idx_x-small">
                        <div class="pub-idx__base-info">
                          <div class="pub-idx__full-text_box">
                            <div class="pub-idx__full-text_img">
                              <img src="">
                            </div>
                          </div>
                          <div class="pub-idx__main_box">
                            <div class="pub-idx__main">
                              <div class="pub-idx__main_title selected_pub_title">${representPub.title}</div>
                              <div class="pub-idx__main_author selected_pub_author">${representPub.authorNames }</div>
                              <div class="pub-idx__main_author selected_pub_BriefDesc">${representPub.briefDesc }</div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="main-list__item_actions" onclick="delRepresentPub(this);">
                      <i class="material-icons">close</i>
                    </div>
                  </div>
                </c:forEach>
              </c:if>
              <div class="main-list__item" id="addedPubItem" style="display: none;" des3pubid="">
                <div class="main-list__item_content">
                  <div class="pub-idx_x-small">
                    <div class="pub-idx__base-info">
                      <div class="pub-idx__full-text_box">
                        <div class="pub-idx__full-text_img">
                          <img src="">
                        </div>
                      </div>
                      <div class="pub-idx__main_box">
                        <div class="pub-idx__main">
                          <div class="pub-idx__main_title selected_pub_title"></div>
                          <div class="pub-idx__main_author selected_pub_author"></div>
                          <div class="pub-idx__main_author selected_pub_BriefDesc"></div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="main-list__item_actions" onclick="delRepresentPub(this);">
                  <i class="material-icons">close</i>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<div class="dialogs__childbox_fixed">
  <div class="dialogs__footer">
    <button class="button_main button_primary-reverse"
      onclick="PsnResume.saveCVRepresentPub(this, '${pubVO.moduleId}');">
      <spring:message code='homepage.profile.btn.save' />
    </button>
    <button class="button_main button_primary-cancle" onclick="PsnResume.hideRepresentPubBox(this);">
      <spring:message code='homepage.profile.btn.cancel' />
    </button>
  </div>
</div>
