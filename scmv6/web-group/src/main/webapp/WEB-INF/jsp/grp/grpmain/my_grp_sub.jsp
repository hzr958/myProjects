<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
    var grpCount =${grpCount };
    var jumpto="${jumpto}";
    var hasIvite="${hasIvite}";
    var hasReqGrp="${hasReqGrp}";
$(document).ready(function(){
//是否有邀请记录
  if(hasIvite==1 ){
		if(hasReqGrp == 0){//如果不存在请求记录，先将请求按钮隐藏
			$(".dev_grp_module_reg").css("display","none");
		}
		if(jumpto && jumpto == "grpInvite" ){//兼容待任务处理邮件中的群组邀请
			showDialog("join_grp_invite_box");	
			$(".dev_grp_module_invite").click();
		}else if(jumpto && jumpto == "grpReq"){//兼容待任务处理邮件中的群组请求
			if(hasReqGrp == 0){//如果请求记录不存在，则显示邀请
				showDialog("join_grp_invite_box");	
				$(".dev_grp_module_invite").click();
			}else if(hasReqGrp == 1){//如果存在请求记录
				showDialog("join_grp_invite_box");	
				$(".dev_grp_module_reg").click();
			}
		}
		else{
			showDialog("join_grp_invite_box");	
			$(".dev_grp_module_invite").click();
		}
	}else if(hasReqGrp == 1){//请求记录
		$(".dev_grp_module_invite").css("display","none");
		showDialog("join_grp_invite_box");	
		$(".dev_grp_module_reg").click();
	}
	if(grpCount!=null&&grpCount!=""&&grpCount>0){
		$("#grp_list").doLoadStateIco({
			style:"height:28px; width:28px; margin:auto;margin-top:24px;",
			status:1
		});
		GrpBase.showGrpList();
	}else{
        $("#grp_list").html("<div class='response_no-result'>" + groupBase.noRecord + "</div>");
        $("#foot_jsp_div").empty();
		$("#grp_list").closest(".container__horiz_right").find(".dev_grp_page").remove();
	}
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
		var titlelist = document.getElementsByClassName("filter-value__option");
		for(var i = 0; i < titlelist.length ;i++ ){
			titlelist[i].onmouseover =  function(){
				var text = this.innerHTML.trim();
				this.setAttribute("title",text);
			}
		}
});
</script>
<div class="module-home__fixed-layer" style="top: 120px;">
  <div class="module-home__fixed-layer_filter" style="border: none;">
    <div class="searchbox__container main-list__searchbox" list-search="grplist">
      <div class="searchbox__main" style="display: flex;">
        <input placeholder="<s:text name='groups.list.search' />">
        <div class="searchbox__icon material-icons"></div>
      </div>
    </div>
    <div class="filter-list vert-style option_has-stats" list-filter="grplist">
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
      <div class="filter-list__section js_filtersection" filter-section="searchByRole" filter-method="single">
        <div class="filter-section__header">
          <div class="filter-section__title">
            <s:text name='groups.list.access' />
          </div>
          <i class="material-icons filter-section__toggle">expand_less</i>
        </div>
        <ul class="filter-value__list">
          <li class="filter-value__item js_filtervalue" filter-value="2">
            <div class="input-custom-style">
              <input type="checkbox"> <i class="material-icons custom-style"></i>
            </div>
            <div class="filter-value__option js_filteroption" title="">
              <s:text name='groups.list.access.create' />
            </div>
            <div class="filter-value__stats js_filterstats">(0)</div> <i
            class="material-icons filter-value__cancel js_filtercancel">close</i>
          </li>
          <li class="filter-value__item js_filtervalue" filter-value="3">
            <div class="input-custom-style">
              <input type="checkbox"> <i class="material-icons custom-style"></i>
            </div>
            <div class="filter-value__option js_filteroption" title="">
              <s:text name='groups.list.access.join' />
            </div>
            <div class="filter-value__stats js_filterstats">(0)</div> <i
            class="material-icons filter-value__cancel js_filtercancel">close</i>
          </li>
          <li class="filter-value__item js_filtervalue" filter-value="4">
            <div class="input-custom-style">
              <input type="checkbox"> <i class="material-icons custom-style"></i>
            </div>
            <div class="filter-value__option js_filteroption" title="">
              <s:text name='groups.list.access.pend' />
            </div>
            <div class="filter-value__stats js_filterstats">(0)</div> <i
            class="material-icons filter-value__cancel js_filtercancel">close</i>
          </li>
        </ul>
      </div>
    </div>
  </div>
</div>
<div class="module-home__list-layer">
  <div class="module-home__list-layer_list">
    <div class="main-list">
      <div class="main-list__list item_medium-padding" id='grp_list' list-main="grplist"
        style="border-left: 1px solid #ddd;min-height: 700px;">
        <!---------------  群组列表------------------------------------------------------->
      </div>
        <div class="main-list__footer global_no-border dev_grp_page">
          <div class="pagination__box" list-pagination="grplist">
            <!-- 翻页 -->
          </div>
      </div>
  </div>
  <div id="foot_jsp_div"></div><jsp:include page="/skins_v6/footer_infor.jsp" /></div>
</div>
</div>
<%--     <div class="footer">
  <div class="footer_wrap">
    <div class="scholarmate-code">
      <img src="${resmod}/smate-pc/img/footer-code.jpg" width="85" height="85">
      <span>关注科研之友</span>
    </div>
    <div class="footer_cont">
      <p><a href="#">关于我们</a>|<a href="#">隐私政策</a>|<a href="#">服务条款</a>|<a href="#">联系我们</a>|<a href="#">人才招聘</a></p>
      <p>© 2017 爱瑞思 粤ICP备11010329号<img src="${resmod}/smate-pc/img/footer-beian.png" style="width: 14px;" > 粤公网安备 4403052000213</p>
    </div>
  </div>
</div> --%>