<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script src="${resmod}/js_v5/scm.maint.js" type="text/javascript"></script>
<script>
 $(document).ready(function(){
ScmMaint.searchSomeOneBind();
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
<c:choose>
  <c:when test="${!empty userData.rolLogoUrl }">
    <div class="header-main__logo"
      onclick="window.location.href='http://${userData.rolDomain}/insweb/main?rolInsId=${userData.rolInsId }&locale=${locale}&_self=0'"
      style="display: flex; align-items: center; max-width: 368px;">
      <img class="header-main__logo-avator" src="${resmod}/smate-pc/img/logo_bt.png" style="width: 165px; height: 22px;">
      <a class="header-main__logo-detail" style="width: 220px;"> <span class="header-main__logo-detail_name">${ userData.rolTitle}</span>
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
    <div class="header-main__logo" onclick="window.location.href='/'" style="display: flex; flex-shrink: 0;">
      <img src="${resmod }/smate-pc/img/smatelogo_transparent.png">
    </div>
  </c:otherwise>
</c:choose>
<div class="header-main__search" style="width: 275px; margin: 0 0px 0 10px; display: flex; flex-shrink: 0;">
  <div class="searchbox__container searchbox__container-limit"
    style="max-width: 65% !important; width: 320px !important;">
    <div class="searchbox__main top-main_search-input" style="">
      <form id="search_some_one_form" action="/pub/search/pdwhmain" method="get" style="width: 90%;">
        <input id="search_some_one" maxlength="150" name="searchString"
          placeholder="<s:text name='page.main.search.tip'/>" title="<s:text name='page.main.search.tip'/>"
          autocomplete="off" value="${searchString }" style="padding: 4px 12px 4px 6px;">
      </form>
      <div class="searchbox__icon" onclick="ScmMaint.searchSomeOne()"></div>
    </div>
  </div>
</div>
