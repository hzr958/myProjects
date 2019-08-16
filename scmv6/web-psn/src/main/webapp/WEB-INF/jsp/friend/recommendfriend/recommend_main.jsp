<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<script type="text/css" src="${resmod}/smate-pc/css/scmpcframe.css"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_dyreaction.js"></script>
<script type="text/javascript" src="${resmod}/js/friend/friend.main_${locale }.js"></script>
<script type="text/javascript" src="${resmod}/js/friend/myfriend/friend.js"></script>
<script type="text/javascript" src="${resmod}/js/friend/findpsn/friend.findpsn.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/common.ui.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod}/js_v8/pub/plugin/smate.plugin.alerts.js"></script>
<script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<script type="text/javascript" src="${resmod}/js/weixin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="${resmod}/js/sharebutton.js"></script>
<script type='text/javascript' src='${resmod}/js/dialog.js'></script>
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/dialog.css" />
<title>科研之友</title>
<script type="text/javascript">
var module = "${module}";
$(function(){
    var setheight = window.innerHeight - 140 - 95;
    var contentlist = document.getElementsByClassName("content-details_container");
    for(var i = 0; i < contentlist.length; i++){
        contentlist[i].style.minHeight = setheight + "px";
    }
    Friend.moduleClick(module);
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
	if(document.getElementsByClassName("hidden-scrollbar__box")){
	    var scrolllist  = document.getElementsByClassName("hidden-scrollbar__box");
	    for(var i = 0 ; i < scrolllist.length;i++){
	        scrolllist[i].style.height= window.innerHeight - 140 + "px";
	    }
	}
    var headerlist = document.getElementsByClassName("nav_horiz-container");
    var total =document.getElementsByClassName("header__box")[0].offsetWidth;
    var parentleft = document.getElementsByClassName("header__nav")[0].offsetLeft;
    var subleft  = document.getElementsByClassName("header-nav__item-bottom")[0].offsetWidth;
    for(var i = 0 ; i < headerlist.length; i++){
        if(!!window.ActiveXObject || "ActiveXObject" in window){
           /*  headerlist[i].style.right = total - 160 - 20 - parentleft - subleft + "px"; */
            headerlist[i].style.right = 312 + "px";
        }else{
            /* headerlist[i].style.right = total - 150 - 20 - parentleft - subleft + "px"; */
            headerlist[i].style.right = 312 + "px";
        }
    }
});
</script>
</head>
<body>
  <header>
    <div class="header__2nd-nav">
      <div class="header__2nd-nav_box" style="justify-content: flex-end;  position: relative;">
        <c:if test="${locale=='en_US'}">
          <nav class="nav_horiz nav_horiz-container" style="position: absolute; top: 0px;">
            <ul class="nav__list" scm_friend_module="module__list">
              <li class="nav__item item_selected" onclick="Friend.moduleClick('myf');" style="min-width: 81px;"><s:text
                  name="friend.myfriend" /></li>
              <li class="nav__item " onclick="Friend.moduleClick('rec');" style="min-width: 160px;"><s:text
                  name="friend.recommend" /></li>
            </ul>
            <div class="nav__underline nav__underline-en_us"></div>
          </nav>
        </c:if>
        <c:if test="${locale=='zh_CN'}">
          <nav class="nav_horiz nav_horiz-container" style="position: absolute; top: 0px;">
            <ul class="nav__list" scm_friend_module="module__list">
              <li class="nav__item item_selected" onclick="Friend.moduleClick('myf');" style="min-width: 81px;"><s:text
                  name="friend.myfriend" /></li>
              <li class="nav__item " onclick="Friend.moduleClick('rec');" style="min-width: 81px;"><s:text
                  name="friend.recommend" /></li>
            </ul>
            <div class="nav__underline nav__underline-zh_cn"></div>
          </nav>
        </c:if>
        <button class="button_main button_primary-reverse" onclick="Friend.findFriend();">
          <s:text name="friend.findfriend.invite" />
        </button>
      </div>
    </div>
  </header>
  <!-- ==============================================================我的联系人页面_start -->
  <div class="module-home__box" scm_friend_module="myf" style="display: none; margin-top: 40px;">
    <%@ include file="/WEB-INF/jsp/friend/myfriend/friend_main.jsp"%>
  </div>
  <!-- ==============================================================我的联系人页面_end -->
  <div class="module-home__box" scm_friend_module="rec" style="display: none; margin-top: 40px;">
    <div class="module-home__fixed-layer" style="top: 90px; width: auto;">
      <div class="module-home__fixed-layer_filter"  style="border: none;">
        <div class="module-home__fixed-layer_filter-box" style="position: fixed;">
          <div class="hidden-scrollbar__box scrollbar__set-box" style="width: 240px;">
            <div class="filter-list vert-style option_has-stats" list-filter="recommendfriend">
              <!-- =============================所在机构_start -->
              <div class="filter-list__section" filter-method="multiple" filter-section="insId"
                style="width: 240px; min-width: 240px;">
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
              <!-- =============================所在地区_start -->
              <div class="filter-list__section" filter-method="multiple" filter-section="regionId" style="width: 240px;">
                <div class="filter-section__header" style="width: 220px;">
                  <div class="filter-section__title">
                    <s:text name="friend.region" />
                  </div>
                  <i class="material-icons filter-section__toggle">expand_less</i>
                </div>
                <ul class="filter-value__list">
                </ul>
              </div>
              <!-- =============================所在地区_end -->
              <!-- =============================研究领域_start -->
              <div class="filter-list__section" filter-method="multiple" filter-section="scienceAreaId"
                style="width: 240px;">
                <div class="filter-section__header" style="width: 220px;">
                  <div class="filter-section__title">
                    <s:text name="friend.scienceArea" />
                  </div>
                  <i class="material-icons filter-section__toggle">expand_less</i>
                </div>
                <ul class="filter-value__list">
                </ul>
              </div>
              <!-- =============================研究领域_end -->
              <div class="blank_zone"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="module-home__list-layer" style="overflow: hidden; width: auto;">
      <div style="width: 920px;">
        <div class="module-home__list-layer_list module-home__list-set__scroll" style="height: 100%; padding-left: 20px; width: 910px; border-left: 1px solid #ddd;">
          <!-- =============================请求或发送列表_start -->
          <div class="main-list dev_reqorsend_list" style="background-color: #f8f8f8"></div>
          <!-- =============================请求或发送列表_end -->
          <!-- =============================推荐列表_start -->
          <div class="main-list content-details_container">
            <div class="main-list__list" list-main="recommendfriend"></div>
            <jsp:include page="/skins_v6/footer_infor.jsp" />
          </div>
          <!-- =============================推荐列表_end -->
        </div>
      </div>
    </div>
  </div>
  <!-- ==============================================================联系人推荐页面_end -->
  <%@ include file="../findFriend.jsp"%>
  <!-- 分享插件 -->
  <jsp:include page="/common/smate.share.jsp" />
</body>
</html>