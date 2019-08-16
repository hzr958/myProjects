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
<div class="module-home__fixed-layer_filter" style="width: 265px;margin-top:14px;">
   <div class="searchbox__container main-list__searchbox" list-search="prjpub" style="width: 90%;">
    <div class="searchbox__main" style="width: 250px;">
      <input placeholder='成果检索'>
      <div class="searchbox__icon material-icons"></div>
    </div>
  </div>
  <div class="filter-list vert-style option_has-stats filter-list__specStyle" list-filter="prjpub">
    <div class="filter-list__section js_filtersection" filter-section="pubType" filter-method="multiple">
      <div class="filter-section__header">
        <div class="filter-section__title">
          成果类型
        </div>
        <i class="material-icons filter-section__toggle">expand_more</i>
      </div>
      <ul class="filter-value__list">
        <li class="filter-value__item js_filtervalue" filter-value="4">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="">期刊论文</div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
        <li class="filter-value__item js_filtervalue" filter-value="3">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="">会议论文</div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
        <li class="filter-value__item js_filtervalue" filter-value="5">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="">专利</div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
        <li class="filter-value__item js_filtervalue" filter-value="1">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="">奖励</div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
        <li class="filter-value__item js_filtervalue" filter-value="2">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="">书/著作</div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
        <li class="filter-value__item js_filtervalue" filter-value="10">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="">
            书籍章节
          </div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
        <li class="filter-value__item js_filtervalue" filter-value="8">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="">
            学位论文
          </div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
        <li class="filter-value__item js_filtervalue " filter-value="12">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption " title="">
            标准
          </div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
        <li class="filter-value__item js_filtervalue " filter-value="13">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="">
            软件著作权
          </div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
        <li class="filter-value__item js_filtervalue" filter-value="7">
          <div class="input-custom-style">
            <input type="checkbox"> <i class="material-icons custom-style"></i>
          </div>
          <div class="filter-value__option js_filteroption" title="">
            其他
          </div>
          <div class="filter-value__stats js_filterstats">(0)</div> <i
          class="material-icons filter-value__cancel js_filtercancel">close</i>
        </li>
      </ul>
    </div>
  </div>
</div>
