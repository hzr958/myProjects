<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
$(function(){
    var setheight = window.innerHeight - 180 - 95;
    var contentlist = document.getElementsByClassName("content-details_container");
    for(var i = 0; i < contentlist.length; i++){
        contentlist[i].style.minHeight = setheight + "px";
        contentlist[i].querySelector(".main-list__list").style.minHeight = setheight + "px";
        
    } 
    var targetelem = document.getElementsByClassName("filter-section__toggle");
	for(var i = 0 ; i< targetelem.length; i++){
		targetelem[i].onclick = function(){
			if(this.innerHTML==="expand_less"){
				 this.innerHTML = "expand_more";
				 this.closest(".filter-list__section").querySelector(".filter-value__list").style.display="none";
			}else{
				 this.closest(".filter-list__section").querySelector(".filter-value__list").style.display="block";
				 if(!($(this).is($("[scm_friend_module='rec']").find(".filter-section__toggle").eq(0)))){
    				 var box=$(".hidden-scrollbar__box").eq(1);
    				 var fontBoxHeight=$(this).parents(".filter-list__section").prev().height();
    				 var oldHeight=box.scrollTop(); 
    				 box.animate({"scrollTop":oldHeight+fontBoxHeight},500);
				 }
				 this.innerHTML = "expand_less"; 
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
  <div class="module-home__fixed-layer_filter" style="overflow-x: hidden;">
    <div class="hidden-scrollbar__box" style="width: 117%;">
      <!-- =============================过滤条件_start -->
      <div class="module-home__fixed-layer_search-box" style="max-width: 270px; margin-left: 3px;">
        <div class="searchbox__container main-list__searchbox" list-search="myfriend">
          <div class="searchbox__main">
            <input placeholder='<s:text name="friend.search"/>'>
            <div class="searchbox__icon material-icons"></div>
          </div>
        </div>
      </div>
      <!-- =============================过滤条件_end -->
      <div class="module-home__fixed-layer_filter-box">
        <div class="filter-list vert-style option_has-stats" list-filter="myfriend">
          <!-- =============================所在机构_start -->
          <div class="filter-list__section" filter-method="multiple" filter-section="insId" style="width: 240px;">
            <div class="filter-section__header" style="width: 220px;">
              <div class="filter-section__title">
                <s:text name="friend.ins" />
              </div>
              <i class="material-icons filter-section__toggle">expand_less</i>
            </div>
            <ul class="filter-value__list">
            </ul>
          </div>
          <!-- =============================所在机构_end -->
        </div>
      </div>
    </div>
  </div>
</div>
<!-- =============================联系人列表_start -->
<div class="module-home__list-layer">
  <div class="module-home__list-layer_list" style="padding-left: 20px;"> 
    <div class="main-list content-details_container">
      <div class="main-list__list dev_myfriend_list" list-main="myfriend"></div>
      <div class="main-list__footer">
        <div class="pagination__box" list-pagination="myfriend"></div>
      </div>
    </div>
    <jsp:include page="/skins_v6/footer_infor.jsp" />
  </div>
</div>
<!-- =============================联系人列表_end -->
