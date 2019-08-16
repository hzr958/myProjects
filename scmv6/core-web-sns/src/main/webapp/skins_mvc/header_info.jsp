<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script src="${resmod}/js_v5/scm.maint.js" type="text/javascript"></script>
<script>
 $(document).ready(function(){
     ScmMaint.searchSomeOneBind();
     var targetlist = document.getElementsByClassName("searchbox__main");
     for(var i = 0; i< targetlist.length; i++){
     	targetlist[i].querySelector("input").onfocus = function(){
     		if(!this.closest(".searchbox__main").classList.contains("searchbox__main-focus")){
     		   this.closest(".searchbox__main").classList.add("searchbox__main-focus");
     		}
     	}
     	targetlist[i].querySelector("input").onblur = function(){
     	   if(this.closest(".searchbox__main").classList.contains("searchbox__main-focus")){
     		  this.closest(".searchbox__main").classList.remove("searchbox__main-focus");
     	   }
     	}
     }

 })
 </script>
<c:choose>
  <c:when test="${!empty userData.rolLogoUrl }">
    <div class="header-main__logo"
      onclick="window.location.href='http://${userData.rolDomain}/insweb/main?rolInsId=${userData.rolInsId }&locale=${locale}&_self=0'"
      style="display: flex; align-items: center; max-width: 387px;">
      <img class="header-main__logo-avator" src="${resmod}/smate-pc/img/logo_bt.png" style="width: 165px; height: 22px;">
      <a class="header-main__logo-detail"> <span class="header-main__logo-detail_name">${ userData.rolTitle}</span>
      </a>
    </div>
  </c:when>
  <c:when test="${!empty userData.rolTitle }">
    <div
      onclick="window.location.href='http://${userData.rolDomain}/insweb/main?rolInsId=${userData.rolInsId }&locale=${locale}&_self=0'"
      style="display: flex; align-items: center; max-width: 387px;">
      <img class="header-main__logo-avator" src="${resmod}/smate-pc/img/logo_bt.png" style="width: 165px; height: 22px;">
      <a class="header-main__logo-detail"> <span class="header-main__logo-detail_name">${ userData.rolTitle}</span>
      </a>
    </div>
  </c:when>
  <c:otherwise>
    <div class="header-main__logo" onclick="window.location.href='/'">
      <img src="${resmod }/smate-pc/img/smatelogo_transparent.png">
    </div>
  </c:otherwise>
</c:choose>
<div id="num1" class="header-main__search" style="width: 275px; margin: 0 0px 0 6px; display: none; flex-shrink: 0;">
  <div class="searchbox__container searchbox__container-limit"
    style="max-width: 65% !important; width: 320px !important;">
    <div class="searchbox__main" style="width: 400px !important;">
      <form id="search_some_one_form" action="/pub/search/pdwhmain" method="get" style="width: 90%;">
        <input id="search_suggest_str_type" type="hidden" name ="suggestStrType" value="" />
        <input id="search_suggest_str_psn" type="hidden" name ="suggestStrPsn" value="" />
        <input id="search_suggest_str_pid" type="hidden" name ="suggestStrPid" value="" />
        <input id="search_suggest_str_ins" type="hidden" name ="suggestStrIns" value="" />
        <input id="search_suggest_str_insId" type="hidden" name ="suggestStrInsId" value="" />
        <input id="search_suggest_str_kw" type="hidden" name ="suggestStrKw" value="" />
        <input id="search_some_one" maxlength="150" name="searchString"
          placeholder="<spring:message code='page.main.search.tip'/>"
          title="<spring:message code='page.main.search.tip'/>" autocomplete="off" value="${searchString }"
          style="padding: 4px 12px 4px 6px;">
      <!-- 检索提示列表 -->
      </form>
      <div class="searchbox__icon" onclick="ScmMaint.searchSomeOne()"></div>
      <div style="position: absolute;border: 1px solid #f5f5f5;background: #fff;left: -1px;top: 31px;width: 400px;">
          <div class="preloader_ind-linear" style="width: 100%;margin: 0px 0px 0px 6px;display: flex;flex-shrink: 0;"></div>
          <div class="ac__list"></div>
         </div>
    </div>
    </div>
</div>
<div id="num2" class="header-main__search" style="width: 275px; margin: 0 0px 0 6px; display: none; flex-shrink: 0;">
  <div class="searchbox__container searchbox__container-limit"
    style="max-width: 65% !important; width: 320px !important;">
    <div class="searchbox__main">
      <form id="search_some_one_form" action="/pub/search/pdwhmain" method="get" style="width: 90%;">
        <input id="search_some_one" maxlength="150" name="searchString"
          placeholder="<spring:message code='page.main.search.tip'/>"
          title="<spring:message code='page.main.search.tip'/>" autocomplete="off" value="${searchString }"
          style="padding: 4px 12px 4px 6px;">
      </form>
      <div class="searchbox__icon" onclick="ScmMaint.searchSomeOne()"></div>
    </div>
  </div>
</div>
