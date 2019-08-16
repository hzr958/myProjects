<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
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
           var searchRegion = $("#selected_ins_region").val();
           if(searchRegion!=""){
               $("#insRegions").find("em").each(function(){
                    if($(this).text()=="(0)"){
                        $(this).parent().parent().hide();
                    }
                });
                $("#insRegions").find("i[name='insRegion']").css("visibility","visible").html("&#xe5cd;");
                $("#insRegions").find("i[name='insRegion']").css("display","block").html("&#xe5cd;");
                //添加点击类型返回事件
                $("#insRegions").find("a").addClass("selectedRegionItem");
                $("#insRegions").find("a").attr("onclick","page.backSearch('region')");
                //隐藏数据量
                $("#insRegions").find("em").html("");
            }
           
           var searchCharacter = $("#selected_ins_Character").val();
           if(searchCharacter!=""){
               $("#insCharacters").find("em").each(function(){
                    if($(this).text()=="(0)"){
                        $(this).parent().parent().hide();
                    }
                });
                $("#insCharacters").find("i[name='insCharacter']").css("visibility","visible").html("&#xe5cd;");
                $("#insCharacters").find("i[name='insCharacter']").css("display","block").html("&#xe5cd;");
                //添加点击类型返回事件
                $("#insCharacters").find("a").addClass("selectedCharacterItem");
                $("#insCharacters").find("a").attr("onclick","page.backSearch('character')");
                //隐藏数据量
                $("#insCharacters").find("em").html("");
            }
           
           var $regionId = $("#ins_region_id_by_self").val();
           if ($regionId != "") {
             var showName = $("#ins_region_name_by_self").val();
             var createHtml = '<div onclick="page.backSearch(\'region\')"><a class="menuList-item_selected" href="javascript:;"><span>'
               + showName
               + '</span> <em class="fr"></em><i name="insRegion" class="material-icons close" style="visibility: visible;">&#xe5cd;</i></a></div>';
             $("#insRegions").html(createHtml);
             $("#insRegions").attr("title", showName);
             $("#ins_region_id_by_self").val("");
             $("#ins_region_name_by_self").val("");
           }
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
<!-- 左边检索 -->
<div class="module-home__fixed-layer_filter" id="left_filter" style="width: 265px;">
  <div class="cont_l" style="width:200px;">
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
                <div class="menuList menuList-item" id="insCharacters" style="width: 251px;">
                    <%@ include file="ins_character_menu.jsp" %> 
                </div>
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
                <div class="menuList menuList-item" id="insRegions" style="width: 251px;">
                    <%@ include file="ins_region_menu.jsp" %> 
                </div>
                <div id="region_load"></div>
                <%-- <div class="input__area" id="auto_input_div" style="width: 85%; padding-left: 32px; margin-top: 12px; align-items:center;">
                
                    <input type="text" name="insRegionName" id="insRegionName" value=""
                        maxlength="200" class="js_autocompletebox"
                        request-url="/psnweb/outside/ajaxautoprovinces" code=""
                        manual-input="no" item-event="page.inputRegionNameBySelf()"
                        placeholder="<s:text name='ins.search.region.tips'/>"  title="<s:text name='ins.search.region.tips'/>"  style="border: 1px solid #ddd; border-radius: 3px; padding-left: 4px;"/>
                </div> --%>
            </div>
            <!-- 机构地区检索条件  end -->
             </div>
        </div>
    </div>
    <!-- 右边列表 -->
    <div class="mechanism-right" style="margin-left: 0px; width: 880px; " id="right_list">
        <%@ include file="ins_list.jsp" %> 
    </div>