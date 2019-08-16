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
})
</script>
<input type="hidden" name="searchPrjPageNo" id="searchPrjPageNo" value="${searchPrjPageNo }" />
<input type="hidden" name="prjPageNo" id="prjPageNo" value="${page.pageNo }" />
<input type="hidden" name="representPrjSum" id="representPrjSum" value="${fn:length(representPrjList) }" />
<div class="dialogs__childbox_fixed">
  <div class="dialogs__header">
    <div class="dialogs__header_title">
      <s:text name='homepage.profile.title.select.featured.projects' />
    </div>
  </div>
</div>
<div class="dialogs__childbox_adapted">
  <div class="dialogs__content global__padding_24">
    <div class="sugg-picker">
      <div class="sugg-picker__header">
        <s:text name='homepage.profile.note.select.featured.projects' />
      </div>
      <div class="sugg-picker__panel">
        <div class="sugg-panel">
          <div class="sugg-panel__title">
            <div class="searchbox__container main-list__searchbox" list-search="psnOpenPrjList">
              <div class="searchbox__main">
                <input placeholder="<s:text name='homepage.profile.note.search.projects'/>">
                <div class="searchbox__icon material-icons"></div>
              </div>
            </div>
          </div>
          <div class="sugg-panel__content" id="openPrjListContent">
            <div class="main-list__list global_no-border" id="psnOpenPrjList" list-main="psnOpenPrjList"></div>
          </div>
        </div>
        <div class="sugg-panel right-panel">
          <div class="sugg-panel__title">
            <s:text name='homepage.profile.note.selected.projects' />
          </div>
          <div class="sugg-panel__content">
            <div class="main-list__list global_no-border" id="addedPrjList">
              <c:if test="${!empty representPrjList}">
                <c:forEach items="${representPrjList }" var="representPrj" varStatus="status">
                  <div class="main-list__item" id="representPrj_${representPrj.id }" prj_id="${representPrj.id }" seq_prj="${status.index+1}">
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
                              <div class="pub-idx__main_title" id="selected_prj_title_${representPrj.id }">${representPrj.prjInfo.showTitle}</div>
                              <div class="pub-idx__main_author" id="selected_prj_author_${representPrj.id }">${representPrj.prjInfo.showAuthorNames }</div>
                              <div class="pub-idx__main_src"></div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="main-list__item_actions">
                      <i class="selected-func_up arrow_up" onclick="upMoveRepresentPrj(this);"></i>
                      <i class="selected-func_down arrow_down" onclick="downMoveRepresentPrj(this);"></i>
                      <i class="material-icons arrow_close" onclick="delRepresentPrjObj(this);">close</i>
                    </div>
                  </div>
                </c:forEach>
              </c:if>
              <div class="main-list__item" id="addedPrjItem" style="display: none;">
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
                          <div class="pub-idx__main_title" id="selected_prj_title_$prjId$">replacetitle</div>
                          <div class="pub-idx__main_author" id="selected_prj_author_$prjId$">replaceAuthorName</div>
                          <div class="pub-idx__main_src"></div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="main-list__item_actions">
                  <i class="selected-func_up arrow_up" onclick="upMoveRepresentPrj(this);"></i>
                  <i class="selected-func_down arrow_down" onclick="downMoveRepresentPrj(this);"></i>
                  <i class="material-icons arrow_close" onclick="delRepresentPrjObj(this);">close</i>
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
    <button class="button_main button_primary-reverse" onclick="savePsnRepresentPrj(this);">
      <s:text name='homepage.profile.btn.save' />
    </button>
    <button class="button_main" onclick="hideRepresentPrjBox(this);">
      <s:text name='homepage.profile.btn.cancel' />
    </button>
  </div>
</div>
