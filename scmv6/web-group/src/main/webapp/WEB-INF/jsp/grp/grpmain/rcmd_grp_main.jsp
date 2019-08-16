<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	$(document).ready(function(){
	    //推荐群组列表
	    //转圈圈
	    $("#grp_rcmd_list").doLoadStateIco({
			style:"height:28px; width:28px; margin:auto;margin-top:24px;",
			status:1
		});
	    
		var titlelist = document.getElementsByClassName("filter-value__option");
		for(var i = 0; i < titlelist.length ;i++ ){
			titlelist[i].onmouseover =  function(){
				var text = this.innerHTML.trim();
				this.setAttribute("title",text);
			}
		}
	    
		
		GrpManage.getRcmdGrpList();
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
		     if(document.getElementsByClassName("hidden-scrollbar__box")){
                 document.getElementsByClassName("hidden-scrollbar__box")[0].style.height= window.innerHeight - 180 + "px";
           }
	});
 </script>
<div class="module-home__fixed-layer" style="top: 120px;">
  <div class="module-home__fixed-layer_filter" style="overflow-x: hidden; border-right: none;">
    <div class="hidden-scrollbar__box" style="width: 117%;">
      <div class="searchbox__container main-list__searchbox" list-search="grprcmdlist"
        style="max-width: 270px; margin-left: 12px;">
        <div class="searchbox__main" style="display: flex;">
          <input placeholder="<s:text name='groups.list.search' />">
          <div class="searchbox__icon material-icons"></div>
        </div>
      </div>
      <div class="filter-list vert-style option_has-stats" list-filter="grprcmdlist">
        <div class="filter-list__section js_filtersection" filter-section="grpCategory" filter-method="single">
          <div class="filter-section__header">
            <div class="filter-section__title">
              <s:text name='groups.list.ctg' />
            </div>
            <i class="material-icons filter-section__toggle">expand_less</i>
          </div>
          <ul class="filter-value__list">
            <li class="filter-value__item js_filtervalue" filter-value="12">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption" title="">
                <s:text name='groups.list.interest' />
              </div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel">close</i>
            </li>
            <li class="filter-value__item js_filtervalue" filter-value="11">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption" title="">
                <s:text name='groups.list.pro' />
              </div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel">close</i>
            </li>
            <li class="filter-value__item js_filtervalue" filter-value="10">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption" title="">
                <s:text name='groups.list.cur' />
              </div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel">close</i>
            </li>
          </ul>
        </div>
        <div class="filter-list__section js_filtersection" filter-section="disciplineCategory" filter-method="single">
          <div class="filter-section__header">
            <div class="filter-section__title">
              <s:text name='groups.list.area' />
            </div>
            <i class="material-icons filter-section__toggle">expand_less</i>
          </div>
          <ul class="filter-value__list">
            <li class="filter-value__item js_filtervalue" filter-value="1">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption" title="">
                <s:text name='groups.list.area.agri' />
              </div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel">close</i>
            </li>
            <li class="filter-value__item js_filtervalue" filter-value="2">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption" title="">
                <s:text name='groups.list.area.sci' />
              </div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel">close</i>
            </li>
            <li class="filter-value__item js_filtervalue" filter-value="3">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption" title="">
                <s:text name='groups.list.area.humanSci' />
              </div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel">close</i>
            </li>
            <li class="filter-value__item js_filtervalue" filter-value="4">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption" title="">
                <s:text name='groups.list.area.econManage' />
              </div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel">close</i>
            </li>
            <li class="filter-value__item js_filtervalue" filter-value="5">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption" title="">
                <s:text name='groups.list.area.engi' />
              </div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel">close</i>
            </li>
            <li class="filter-value__item js_filtervalue" filter-value="6">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption" title="">
                <s:text name='groups.list.area.infoSci' />
              </div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel">close</i>
            </li>
            <li class="filter-value__item js_filtervalue" filter-value="7">
              <div class="input-custom-style">
                <input type="checkbox"> <i class="material-icons custom-style"></i>
              </div>
              <div class="filter-value__option js_filteroption" title="">
                <s:text name='groups.list.area.pharSci' />
              </div>
              <div class="filter-value__stats js_filterstats">(0)</div> <i
              class="material-icons filter-value__cancel js_filtercancel">close</i>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</div>
<div class="module-home__list-layer">
  <div class="module-home__list-layer_list" style="border-left: 1px solid #ddd; min-height: 650px;">
    <div class="main-list">
      <div class="main-list__list item_medium-padding" id='grp_rcmd_list' list-main="grprcmdlist">
        <!---------------  群组列表------------------------------------------------------->
      </div>
      <div class="main-list__footer global_no-border">
        <div class="pagination__box" list-pagination="grprcmdlist"></div>
      </div>
    </div>
  </div>
</div>
