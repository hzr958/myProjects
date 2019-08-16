<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="cont_l" style="width:200px;">
	<script type="text/javascript">
		$(function() {
			//左菜单的点击下拉和收起
			if(document.getElementsByClassName("top-main_search-input").length > 0){
			    document.getElementsByClassName("top-main_search-input")[0].style.width = "400px!important"
			}
			var menuParent = $('.menu > .ListTitlePanel > div');//获取menu下的父层的DIV
			var menuList = $('.menuList');
			var name = document.getElementById("iconQh");
			$('.menu > .menuParent > .ListTitlePanel > .ListTitle').each(
					function(i) {//获取列表的大标题并遍历
						$(this).click(function() {
							if ($(menuList[i]).css('display') == 'none') {
								$(menuList[i]).slideDown(300);
								$(this).find("i").html("&#xE5CE;");
								if($(this).attr("id") == "insRegionTitle"){
									$("#insRegionName").show();
								}
							} else {
								$(menuList[i]).slideUp(300);
								$(this).find("i").html("&#xE5CF;");
								if($(this).attr("id") == "insRegionTitle"){
									$("#insRegionName").hide();
								}
							}
						});
					});

			addFormElementsEvents($("#auto_input_div")[0]);
           if(document.getElementsByClassName("header__nav")){
             if(document.getElementById("num1")){
               document.getElementById("num1").style.display="flex";              
             }
             if(document.getElementsByClassName("header-main__box") && document.getElementById("num2")){
               document.getElementsByClassName("header-main__box")[0].removeChild(document.getElementById("num2"));
             }
           }
           if(document.getElementById("search_some_one")){
               document.getElementById("search_some_one").onfocus = function(){
                   this.closest(".searchbox__main").style.borderColor = "#2882d8";
               }
               document.getElementById("search_some_one").onblur = function(){
                   this.closest(".searchbox__main").style.borderColor = "#ccc";
               }
           }
		});
		
	</script>
	<div class="l_menu_tit" style="display: flex; align-items: center;border-bottom: 1px solid #ddd; width: 126%;">
		<i class="mechanism_icon"></i>
		<span style="margin: 10px 0px 0px 12px;"><s:text name="ins.serach.title"/></span>
	</div>
	<div class="menu">
		<!-- 机构类别检索条件 begin -->
		<div class="menuParent">
			<div class="ListTitlePanel">
				<div class="ListTitle" style="width: 220px; padding-left: 0px; margin-left: 32px; border-bottom: 1px solid #ddd;  border-top: none; ">
					<span><s:text name="ins.search.type"/></span>
					<div class="leftbgbt">
						<i class="material-icons" id="iconQh">expand_less</i>
					</div>
				</div>
			</div>
			<div class="menuList menuList-item" id="insCharacters" style="width: 251px;"></div>
			<div id="character_load"></div>
		</div>
		<!-- 机构类别检索条件 end -->
		<!-- 机构地区检索条件 begin -->
		<div class="menuParent" style="width: 260px; padding: 0px;">
			<div class="ListTitlePanel">
				<div class="ListTitle" style="width: 220px;  padding-left: 0px; margin-left: 32px; border-bottom: 1px solid #ddd; border-top: none; padding-top: 0px; " id="insRegionTitle">
					<span><s:text name="ins.search.location"/></span>
					<div class="leftbgbt">
						<i class="material-icons" id="iconQh"></i>
					</div>
				</div>
			</div>
			<div class="menuList menuList-item" id="insRegions" style="width: 251px;"></div>
			<div id="region_load"></div>
			<div class="input__area" id="auto_input_div" style="width: 85%; padding-left: 32px; margin-top: 12px;align-items:center; ">
            
				<input type="text" name="insRegionName" id="insRegionName" value=""
					maxlength="200" class="js_autocompletebox"
					request-url="/psnweb/outside/ajaxautoregion" code=""
					manual-input="no" item-event="SearchIns.inputRegionNameBySelf()"
					placeholder="<s:text name='ins.search.region.tips'/>"  title="<s:text name='ins.search.region.tips'/>"  style="border: 1px solid #ddd; border-radius: 3px; padding-left: 4px;"/>
			</div>
		</div>
		<!-- 机构地区检索条件  end -->

	</div>
</div>