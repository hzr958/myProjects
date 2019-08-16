<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<head>
<meta charset="UTF-8">
<title>Title</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>prj</title>
<link rel="stylesheet" href="${ressie }/css/achievement_lt.css" />
<link rel="stylesheet" href="${ressie }/css/index.css" />
<link rel="stylesheet" href="${ressie }/css/administrator.css" />
<link rel="stylesheet" href="${ressie}/css/scmpcframe.css" />
<link rel="styleSheet" href="${ressie }/css/plugin/jquery.thickbox.css" />
<script type="text/javascript" src="${ressie}/js/plugin/jquery.thickbox.min.js"></script>
<script type="text/javascript" src="${ressie }/js/plugin/scmpc_autofill.js"></script>
<script type="text/javascript" src="${ressie}/js/plugin/restipboxlist.js"></script>
<script type="text/javascript" src="${resprj}/prj/prj.js"></script>
<script type="text/javascript">
var local='${locale}';
     $(document).ready(function(){
//     	 loadLeftData();
    	  scmpcListfilling({
              targetleftUrl:"/prjweb/project/prjlist",
              targetcntUrl:"/prjweb/project/ajaxleftcount"          
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

         if(!document.getElementsByClassName("message_list")){
                 var switchlist = document.getElementsByClassName("left_container-item_title-tip");
                 for(var i = 0; i < switchlist.length; i++ ){
                     switchlist[i].innerHTML="expand_less";
                     switchlist[i].closest(".left_container-item").querySelector(".left_container-item_box").style.display="none";
                 }
             }  
    	 
    	 
           query();
//            tb_init('a.thickbox, area.thickbox, input.thickbox');  
           
   });
    
    function addPrj(){
//     	   var alt = 'http://nankai.bj.scholarmate.com/prjweb/project/ajaxAddChooseTip?TB_iframe=true&width=396&height=272';
//         $('#reserve').attr('alt', alt);
//         $('#reserve').click();
          $("#ball_content_top").show();
    }
    function addPrj(){
        var a=document.getElementById('popup').innerHTML
        scmpcnewtip({
            targettitle: '选择新增方式', 
            targetcllback: '', 
            targettxt: a,
            targetfooter:0
            });
        
    }
 </script>
</head>
<body>
  <!-- <form name="mainForm" id="mainForm" action="" method="post"> -->
  <input type="hidden" alt="" title="项目新增" class="thickbox" id="reserve" />
  <div class="conter">
    <div id="con_five_1" style="display: none"></div>
    <div id="con_five_2" class="hover">
      <!--左边-->
      <div class="achievement_conter sie_achievement_conter1">
        <div class="left-file__fill_container  fl left-wrap">
          <div class="left_container-search_box left-top_search-container">
            <input placeholder="检索" class="left_container-search_input" value=""> <i
              class="material-icons left_container-search_tip">search</i>
          </div>
          <div class="left-filter_item" style="display: flex; flex-direction: column;">
            <div class="achievement_conter_left left-filter_list" filter-id="unitId" filter-title="部门" id="leftProv"
              filter-search="hidden" filter-selector="single">
              <c:forEach var="list" items="${mapUnitList}">
                <div class="left_container-item_list">
                  <div class="left_container-item_list-content">${list['itemName']}</div>
                  <div class="left_container-item_list-num">
                    (<span class="left_container-item_list-detail">0</span>)
                  </div>
                  <input class="left_container-item_list-value" type="hidden" value="${list['itemId']}  " />
                </div>
              </c:forEach>
            </div>
            <div class="achievement_conter_left left-filter_list" filter-id="disCode" filter-title="领域" id="leftNature"
              filter-search="hidden" filter-selector="single" filter-open="close">
              <c:forEach var="list" items="${mapDiscis}">
                <div class="left_container-item_list">
                  <div class="left_container-item_list-content">${list['itemName']}</div>
                  <div class="left_container-item_list-num">
                    (<span class="left_container-item_list-detail">0</span>)
                  </div>
                  <input class="left_container-item_list-value" type="hidden" value="${list['itemId']}  " />
                </div>
              </c:forEach>
            </div>
            <div class="achievement_conter_left left-filter_list" filter-id="year" id="leftPos" filter-title="年份"
              filter-search="hidden" filter-selector="single" filter-open="close">
              <c:forEach var="list" items="${mapYears}">
                <div class="left_container-item_list">
                  <div class="left_container-item_list-content">${list['itemName']}</div>
                  <div class="left_container-item_list-num">
                    (<span class="left_container-item_list-detail">0</span>)
                  </div>
                  <input class="left_container-item_list-value" type="hidden" value="${list['itemId']}  " />
                </div>
              </c:forEach>
            </div>
            <div class="achievement_conter_left left-filter_list" filter-id="prjFromId" filter-title="来源"
              id="leftNature" filter-search="hidden" filter-selector="single" filter-open="close">
              <c:forEach var="list" items="${mapPrjFroms}">
                <div class="left_container-item_list">
                  <div class="left_container-item_list-content">${list['itemName']}</div>
                  <div class="left_container-item_list-num">
                    (<span class="left_container-item_list-detail">0</span>)
                  </div>
                  <input class="left_container-item_list-value" type="hidden" value="${list['itemId']}  " />
                </div>
              </c:forEach>
            </div>
          </div>
        </div>
        <!--右边-->
        <div class="achievement_conter_right fr">
          <div class="left-wrap w940">
            <div class="achievement_achievement">
              <p>
                项目列表 <span>（总数：<span class="examine_totla">${totalNum}</span>）
                </span>
              </p>
              <div id="increased" class="fr">
                <a href="#" class="increased" onclick="addPrj();"><span></span>新增</a> <a href="#" class="derive"
                  onclick="exportExcel()"><span></span>导出</a>
              </div>
            </div>
            <div class="headline ftbold">
              <span class="fr f999">成果</span> 项目名称 / 批准号 / 负责人 / 依托部门 / 项目来源 / 起止日期 / 资助金额(万元)
            </div>
          </div>
          <div id="fresh" class="achievement_conter_right right-content_container list_left_boder"></div>
        </div>
      </div>
    </div>
    <div id="con_five_3" style="display: none"></div>
    <div id="con_five_4" style="display: none"></div>
    <div id="con_five_5" style="display: none"></div>
    <div id="con_five_6" style="display: none"></div>
  </div>
  <p class="clear"></p>
  <!-- </form> -->
  <div class="ball_content_top" id="popup" style="visibility: hidden;">
    <ul class="ball_content" style="padding: 0px;">
      <li class="ball_content_paper"><a href="/prjweb/project/importfile"><i class="icon_file_paper"></i>
          <p>文件导入</p></a>
        <p class="ball_content_paper_pattern">通过文件方式批量导入项目</p></li>
      <li class="ball_content_handwork"><a href="#"><i class="icon_file_handwork"></i>
          <p>手工录入</p></a>
        <p class="ball_content_paper_pattern">通过手工录入方式逐条录入项目</p></li>
    </ul>
  </div>
</body>
</html>