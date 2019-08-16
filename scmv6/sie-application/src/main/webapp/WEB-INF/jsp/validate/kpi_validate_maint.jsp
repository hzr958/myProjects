<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="UTF-8">
  <title>科研验证</title>
  <link rel="stylesheet" href="${ressie }/css/reset.css" />
  <link rel="stylesheet" href="${ressie }/css/achievement_lt.css" />
  <link rel="stylesheet" href="${ressie }/css/main_list.css" />
  <link rel="stylesheet" href="${ressie }/css/index.css" />
  <link rel="stylesheet" href="${ressie }/css/administrator.css" />
  <link rel="stylesheet" href="${ressie }/css/unit.css" />
  <link rel="stylesheet" href="${ressie }/css/newplugstyle.css" />
  <link rel="stylesheet" href="${ressie }/css/newpagestyle.css" />
  <link rel="stylesheet" href="${ressie }/css/plugin/toast.css" />
  <script type="text/javascript" src="${ressie }/js/jquery-1.9.1.min.js"></script>
  <script type="text/javascript" src="${ressie }/js/jquery.js"></script>
  <!-- 进入功能加载效果 -->
  <script type="text/javascript" src="${resmod }/js/loadStateIco.js"></script>
  <script type="text/javascript" src="${ressie }/js/plugin/scmpc_autofill.js"></script>
  <script type="text/javascript" src="${resapp }/validate/validatelist.js"></script>
  <script type="text/javascript" src="${ressie }/js/plugin/page.tag.js"></script>
  <script type="text/javascript" src="${ressie }/js/plugin/restipboxlist.js"></script>
  <script type="text/javascript" src="${ressie }/js/plugin/smate.toast.js"></script>
  <script type="text/javascript" src="${ressie }/js/plugin/confirm.js"></script>
  <script type="text/javascript" src="${resapp }/validate/validate.form.js"></script>
  <script type="text/javascript" src="/resmod/mobile/js/jquery-qrcode.min.js"></script>
  <script type="text/javascript">
     var local ='${locale}';
     var locale ='${locale}';
     //登录框登录不刷新
     $("#login_box_refresh_currentPage").val("false");
     $(document).ready(function(){
         $("#validatelist").doLoadStateIco({
             style:"height:28px; width:28px; margin:auto;margin-top:100px;",
             status:1
         });
           scmpcListfilling({
               targetleftUrl:"/application/validate/ajaxvalidatelist",
               targetcntUrl:"/application/validate/ajaxvalidatecount"              
           });
           reloadcheck();
           document.getElementsByClassName("left-top_search-container")[0].querySelector("input").onfocus = function(){
               this.closest(".left-top_search-container").style.border="1px solid #288aed";
              this.placeholder="";
           }
           document.getElementsByClassName("left-top_search-container")[0].querySelector("input").onblur = function(){
               this.closest(".left-top_search-container").style.border="1px solid #ccc";
               if(local=="zh_CN"){
               this.placeholder="检索";}else{
                   this.placeholder="Search";
               }
           }
           //左侧条件滚动效果
           document.getElementsByClassName("left-filter_item")[0].style.maxHeight = window.innerHeight - 190 + "px";
           window.onresize = function(){
               document.getElementsByClassName("left-filter_item")[0].style.maxHeight = window.innerHeight - 190 + "px";
           }
           setTimeout(changePositionHeight(),500);
           query();
   });
 
 function goBackCallbackNo(){
	 return;
 }
 function deletePub(pubId){
	 if(pubId != ""){
    	 var option={
                 'screentxt':"您确定删除选定成果吗？" ,
                 'screencallback':confirmDeletePub,
                 'screencallbackData':pubId,
                 'screencancelcallback':goBackCallbackNo
         };
    	 popconfirmbox(option);
     } else {
         scmpublictoast("请先选择成果",1500);
     }
 }
 
 function confirmDeletePub(pubId){
	 if(pubId == ""){
         return;
     }
	 $.ajax({
         url : "/pubweb/publication/ajaxdeletepub",
         type : "post",
         dataType : 'json',
         data : {
             "des3Id" : pubId
         },
         success : function(data) {
        	 if(data.result == "success"){
                 scmpublictoast("删除成功！",1500);
                 setTimeout(function() {
                     window.location.reload();
                 }, 1000);
        	 }else if (data.ajaxSessionTimeOut == "yes") {
        		 InsLogin.timeout.deal();
             }
         },
         error : function(XMLHttpRequest, textStatus, errorThrown) {
             scmpublictoast("操作失败，请联系管理员！",1500);
         }
     });
 }
 </script>
</head>
<body>
  <header>
  <input type="hidden" id="orderNum" value=""/>
  <input type="hidden" id="pay_grade" value=""/>
  <div class="header__2nd-nav">
    <div class="header__2nd-nav_box" style="justify-content: flex-end;">
      <nav class="nav_horiz nav_horiz-container" style="margin-left: 944px; top: 0px;">
      <ul class="nav__list" scm_file_id="menu__list">
        <li class="nav__item item_selected" onclick="Validate.backList();">科研验证</li>
      </ul>
      <div class="nav__underline" style="width: 75px; left: 9px;"></div>
      </nav>
      <div class="header__2nd-nav_action-list">
        <a href="###" style="margin-right: 0px;"></a>
      </div>
    </div>
  </div>
  </header>
  <div class="conter mt70" style="">
    <div id="con_five_1" style="display: none"></div>
    <div id="con_five_2" style="display: none"></div>
    <div id="con_five_3" class="hover">
      <div class="achievement_conter sie_achievement_conter1">
        <div class="left-file__fill_container fl" style="overflow-x: hidden;">
          <div class="left_container-search_box left-top_search-container">
            <input placeholder="检索" class="left_container-search_input" value="" maxlength="20"> <i
              class="left_container-search_icon left_container-search_tip" style="z-index: 10;"></i>
          </div>
          <div class="left-filter_item"
            style="display: flex; flex-direction: column; overflow-y: scroll; width: 260px; overflow-x: hidden;">
            <c:if test="${!empty yearList }">
              <div class="achievement_conter_left left-filter_list" filter-id="prpYear" filter-title="年度"
                id="leftNature" filter-search="hidden" filter-selector="single">
                <c:forEach var="list" items="${yearList}">
                  <div class="left_container-item_list" title="${list['itemName'] }">
                    <div class="left_container-item_list-content">${list['itemName'] }</div>
                    <div class="left_container-item_list-num">
                      (<span class="left_container-item_list-detail">0</span>)
                    </div>
                    <input class="left_container-item_list-value" type="hidden" value="${list['itemId'] }  " />
                  </div>
                </c:forEach>
              </div>
             </c:if>
<!--             <div class="achievement_conter_left left-filter_list" filter-id="typeId" filter-title="验证类型" id="leftProv" -->
<!--               filter-search="hidden" filter-selector="single" filter-open="close"> -->
<%--               <c:forEach var="list" items="${typeList}"> --%>
<%--                 <div class="left_container-item_list" title="${list['itemName'] }"> --%>
<%--                   <div class="left_container-item_list-content">${list['itemName'] }</div> --%>
<!--                   <div class="left_container-item_list-num"> -->
<!--                     (<span class="left_container-item_list-detail">0</span>) -->
<!--                   </div> -->
<%--                   <input class="left_container-item_list-value" type="hidden" value="${list['itemId'] }  " /> --%>
<!--                 </div> -->
<%--               </c:forEach> --%>
<!--             </div> -->
            <div class="achievement_conter_left left-filter_list" filter-id="typeId" filter-title="类型" id="leftProv"
              filter-search="hidden" filter-selector="single" filter-open="close">
              <c:forEach var="list" items="${typeList}">
                <div class="left_container-item_list" title="${list['itemName'] }">
                  <div class="left_container-item_list-content">${list['itemName'] }</div>
                  <div class="left_container-item_list-num">
                    (<span class="left_container-item_list-detail">0</span>)
                  </div>
                  <input class="left_container-item_list-value" type="hidden" value="${list['itemId'] }  " />
                </div>
              </c:forEach>
            </div>
          </div>
        </div>
        <!--右边-->
        <div class="achievement_conter_right fr" style="min-height: 620px;">
          <div class="w940">
            <div class="achievement_achievement">
              <p>
                验证列表 <span>（总数：<span id="top_totalcount" class="examine_totla"></span>）
                </span>
              </p>
              <div id="increased" class="fr">
                  <a href="###" onclick="ValidateList.toAdd('${isPay }');" class="increased"
                    style="display: flex; align-items: center;"><i class="material-icons"
                    style="font-size: 16px; margin: 4px 4px 0px 0px;">add</i></>新的待验证文档</a>
              </div>
            </div>
            <div class="headline ftbold">
              <span class="fr f999" style="margin-right: 30px;">验证结果</span> 标题 / 编号 / 提交时间
            </div>
          </div>
          <!--大数据列表-->
          <div id="validatelist"
            class="achievement_conter_right right-content_container list_left_boder main-list__list" list-main="psnpub"
            style="border: none;"></div>
        </div>
      </div>
    </div>
    <div id="con_five_4" style="display: none"></div>
    <div id="con_five_5" style="display: none"></div>
    <div id="con_five_6" style="display: none"></div>
    <p class="clear"></p>
</body>
</html>
