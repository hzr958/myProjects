<!-- 检索条件 -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
  $(function() {
    var targetlist = document.getElementsByClassName("searchbox__main");
    for (var i = 0; i < targetlist.length; i++) {
      targetlist[i].querySelector("input").onfocus = function() {
        this.closest(".searchbox__main").style.cssText = "border:1px solid #3faffa;";
      }
      targetlist[i].querySelector("input").onblur = function() {
        this.closest(".searchbox__main").style.cssText = "border:1px solid #ccc;";
      }
    }
  });

</script>
<div class="module-home__fixed-layer_filter" style="width: 265px;">
  <div class="filter-list vert-style option_has-stats filter-list__specStyle" list-filter="prjreport">
    <div class="filter-list__section js_filtersection" filter-section="reportType" filter-method="multiple">
      <div class="filter-section__header">
        <div class="filter-section__title">
          报告类型
        </div>
        <i class="material-icons filter-section__toggle">expand_more</i>
      </div>
      <ul class="filter-value__list">
        <li class="filter-value__item js_filtervalue" filter-value="1">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="">进展报告</div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
        <li class="filter-value__item js_filtervalue" filter-value="2">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="">中期报告</div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
        <li class="filter-value__item js_filtervalue" filter-value="3">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="">审计报告</div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
        <li class="filter-value__item js_filtervalue" filter-value="5">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="">结题报告</div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
        <li class="filter-value__item js_filtervalue" filter-value="6">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="">验收报告</div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
      </ul>
    </div>
  </div>
</div>
