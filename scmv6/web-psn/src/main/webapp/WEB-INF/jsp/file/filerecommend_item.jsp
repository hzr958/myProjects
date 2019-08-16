<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
$(function(){
	var targetelem = document.getElementsByClassName("filter-section__toggle");
	for(var i = 0 ; i< targetelem.length; i++){
		targetelem[i].onclick = function(){
			if(this.innerHTML==="expand_less"){
				 this.innerHTML = "expand_more";
				 this.closest(".filter-list__section").querySelector(".filter-value__list").style.display="none";
			}else{
				 this.innerHTML = "expand_less";
				 this.closest(".filter-list__section").querySelector(".filter-value__list").style.display="block";
			}
		}
	}
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
<div class="module-home__fixed-layer">
  <div class="module-home__fixed-layer_filter">
    <div class="module-home__fixed-layer_search-box">
      <div class="searchbox__container main-list__searchbox">
        <div class="searchbox__main">
          <input placeholder="查找文件">
          <div class="searchbox__icon material-icons"></div>
        </div>
      </div>
    </div>
    <div class="module-home__fixed-layer_filter-box">
      <div class="hidden-scrollbar__box">
        <div class="filter-list vert-style option_has-stats">
          <div class="filter-list__section" choose-single>
            <div class="filter-section__header" style="width: 220px;">
              <div class="filter-section__title">文件类型</div>
              <i class="material-icons filter-section__toggle">expand_less</i>
            </div>
            <ul class="filter-value__list">
              <li class="filter-value__item option_no-data">
                <div class="input-custom-style">
                  <input type="checkbox"> <i class="material-icons custom-style"></i>
                </div>
                <div class="filter-value__option">PDF</div>
                <div class="filter-value__stats">(0)</div> <i class="material-icons filter-value__cancel">close</i>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<div class="module-home__list-layer">
  <div class="module-home__list-layer_list">
    <div class="main-list">
      <div class="main-list__list" list-main="filerecommend_list">
        <!-- 文件推荐-文件列表 -->
      </div>
      <div class="main-list__footer">
        <div class="pagination__box"></div>
      </div>
    </div>
  </div>
</div>
