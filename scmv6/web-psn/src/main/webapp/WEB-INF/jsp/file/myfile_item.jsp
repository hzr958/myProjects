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
    var setheight = window.innerHeight - 180 - 95;
    var contentlist = document.getElementsByClassName("content-details_container");
    for(var i = 0; i < contentlist.length; i++){
        contentlist[i].style.minHeight = setheight + "px";
        contentlist[i].querySelector(".main-list__list").style.minHeight = setheight + "px";
    }
});
</script>
<div class="module-home__fixed-layer" style="top: 120px;">
  <div class="module-home__fixed-layer_filter" style="border-right: none;">
    <div class="module-home__fixed-layer_search-box">
      <div class="searchbox__container main-list__searchbox" list-search="myfile_list">
        <div class="searchbox__main">
          <input placeholder="<s:text name='apps.files.search' />">
          <div class="searchbox__icon material-icons"></div>
        </div>
      </div>
    </div>
    <div class="module-home__fixed-layer_filter-box">
      <div class="hidden-scrollbar__box">
        <div class="filter-list vert-style option_has-stats" list-filter="myfile_list">
          <div class="filter-list__section js_filtersection" choose-single filter-section="fileTypeNum"
            filter-method="single">
            <div class="filter-section__header" style="width: 240px;">
              <div class="filter-section__title">
                <s:text name='apps.files.type' />
              </div>
              <i class="material-icons filter-section__toggle">expand_less</i>
            </div>
            <ul class="filter-value__list">
              <li class="filter-value__item option_no-data js_filtervalue" filter-value="1">
                <div class="input-custom-style">
                  <input type="checkbox"> <i class="material-icons custom-style"></i>
                </div>
                <div class="filter-value__option">PDF</div>
                <div class="filter-value__stats">(0)</div> <i class="material-icons filter-value__cancel">close</i>
              </li>
              <li class="filter-value__item option_no-data js_filtervalue" filter-value="2">
                <div class="input-custom-style">
                  <input type="checkbox"> <i class="material-icons custom-style"></i>
                </div>
                <div class="filter-value__option">DOC</div>
                <div class="filter-value__stats">(0)</div> <i class="material-icons filter-value__cancel">close</i>
              </li>
              <li class="filter-value__item option_no-data js_filtervalue" filter-value="3">
                <div class="input-custom-style">
                  <input type="checkbox"> <i class="material-icons custom-style"></i>
                </div>
                <div class="filter-value__option">XLS</div>
                <div class="filter-value__stats">(0)</div> <i class="material-icons filter-value__cancel">close</i>
              </li>
              <li class="filter-value__item option_no-data js_filtervalue" filter-value="7">
                <div class="input-custom-style">
                  <input type="checkbox"> <i class="material-icons custom-style"></i>
                </div>
                <div class="filter-value__option">
                  <s:text name='apps.files.type.other' />
                </div>
                <div class="filter-value__stats">(0)</div> <i class="material-icons filter-value__cancel">close</i>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
    <div class="check-file_share"  onclick="VFileMain.showShareRecordsUI(this)"><s:text
              name='apps.files.showShareRecords' /> </div>
  </div>
</div>
<div class="module-home__list-layer">
  <div class="module-home__list-layer_list" style="min-height: 650px; border-left: 1px solid #ddd; padding-left: 12px; margin-top: 15px;">
    <div class="main-list content-details_container">
      <div class="main-list__list" list-main="myfile_list">
        <!-- 我的文件-文件列表 -->
      </div>
      <div class="main-list__footer">
        <div class="pagination__box" list-pagination="myfile_list">
          <!-- 翻页 -->
        </div>
      </div>
    </div>
    <jsp:include page="/common/footer_infor.jsp" />
  </div>