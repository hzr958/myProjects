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
<div class="new__resume-list__item">
  <div style="display: flex; justify-content: center; width: 94%; align-items: center;">
    <div class="new__resume-list__item-line"></div>
    <div class="new__resume-list__item-title new__resume-list__item-title_top">
      <s:text name="resume.project.exp" />
    </div>
    <div class="new__resume-list__item-line"></div>
  </div>
  <div class="new__resume-edit__icon-add new__resume-edit__icon-add_item" onclick="PsnResume.showCVPrjBox(this);">
    <i class="material-icons" style="font-size: 26px;">add</i> <span><s:text name="resume.add" /></span>
  </div>
</div>
<s:if test="prjInfo.size() > 0">
  <s:iterator value="prjInfo" var="prj" status="st">
    <div class="new__resume-list__content-title new__resume-list__content-sub"
      style="display: flex; align-items: baseline;">
      <s:if test="#st.index < 9">
        <div style="width: 24px; min-width: 24px; line-height: 22px;">
      </s:if>
      <s:else>
        <div style="width: 24px; min-width: 28px; line-height: 22px;">
      </s:else>
      ${st.count}.
    </div>
    <div>
      <c:if test="${not empty prj.agencyAndScheme}">${prj.agencyAndScheme},&nbsp;</c:if>
      <c:if test="${not empty prj.externalNo}">${prj.externalNo},&nbsp;</c:if>
      <c:if test="${not empty prj.showTitle}">
        <a href="/prjweb/project/detailsshow?des3PrjId=<iris:des3 code="${prj.prjId}"/>" target="_blank">${prj.showTitle}</a>
      </c:if>
      <c:if test="${not empty prj.showDate}">,&nbsp;${prj.showDate}</c:if>
      <c:if test="${not empty prj.amountAndUnit}">,&nbsp;${prj.amountAndUnit}</c:if>
      <c:if test="${not empty prj.prjState}">,&nbsp;${prj.prjState}</c:if>
      <c:if test="${not empty prj.prjOwner}">,&nbsp;${prj.prjOwner}</c:if>
    </div>
    </div>
  </s:iterator>
</s:if>
<div class="dialogs__box" style="width: 720px;" id="CVPrjBox" dialog-id="CVPrjBox">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="dialogs__header_title">
        <s:text name="resume.prj.add" />
      </div>
    </div>
  </div>
  <div class="dialogs__childbox_adapted">
    <div class="dialogs__content global__padding_24">
      <div class="sugg-picker">
        <div class="sugg-picker__header">
          <s:text name="resume.prj.select" />
        </div>
        <div class="sugg-picker__panel">
          <div class="sugg-panel">
            <div class="sugg-panel__title">
              <div class="searchbox__container main-list__searchbox" list-search="psnPrjList">
                <div class="searchbox__main">
                  <input placeholder='<s:text name="resume.prj.search"/>'>
                  <div class="searchbox__icon material-icons"></div>
                </div>
              </div>
            </div>
            <div class="sugg-panel__content" id="openPrjListContent">
              <div class="main-list__list global_no-border" id="psnPrjList" list-main="psnPrjList"></div>
            </div>
          </div>
          <div class="sugg-panel right-panel">
            <div class="sugg-panel__title">
              <s:text name="resume.prj.imported" />
            </div>
            <div class="sugg-panel__content">
              <div class="main-list__list global_no-border" id="addedPrjList"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__footer">
      <button class="button_main button_primary-reverse" onclick="PsnResume.saveCVPrj(this);">
        <s:text name='homepage.profile.btn.save' />
      </button>
      <button class="button_main" onclick="PsnResume.hideCVPrjBox(this);">
        <s:text name='homepage.profile.btn.cancel' />
      </button>
    </div>
  </div>
</div>