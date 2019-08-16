<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Title</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>人员列表</title>
<link rel="stylesheet" href="${ressie }/css/achievement_lt.css" />
<link rel="stylesheet" href="${ressie }/css/index.css" />
<link rel="stylesheet" href="${ressie }/css/administrator.css" />
<script type="text/javascript" src="${ressie}/js/plugin/restipboxlist.js"></script>
<script type="text/javascript" src="${ressie }/js/plugin/scmpc_autofill.js"></script>
<script type="text/javascript" src="${ressie }/js/page.tag.js"></script>
<script type="text/javascript" src="${respsn}/person/psn.js"></script>
<script type="text/javascript">
     var local='${locale}';
     $(document).ready(function(){
         scmpcListfilling({
             targetleftUrl:"/psnweb/person/ajaxpsnlist",
             targetcntUrl:"/psnweb/person/ajaxpsncount"          
         });
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
         query();
   });
 </script>
</head>
<body>
  <div class="conter">
    <div id="con_five_1" style="display: none"></div>
    <div id="con_five_2" style="display: none"></div>
    <div id="con_five_3" style="display: none"></div>
    <div id="con_five_4" style="display: none"></div>
    <div id="con_five_5" style="display: none"></div>
    <div id="con_five_6" class="hover">
      <div class="achievement_conter sie_achievement_conter1">
        <!--左边-->
        <div class="left-file__fill_container fl left-wrap">
          <div class="left_container-search_box left-top_search-container">
            <input placeholder="检索" class="left_container-search_input" value=""> <i
              class="material-icons left_container-search_tip">search</i>
          </div>
          <div class="left-filter_item" style="display: flex; flex-direction: column;">
            <div class="achievement_conter_left left-filter_list" filter-id="unitId" filter-title="部门" id="leftProv"
              filter-search="hidden" filter-selector="single">
              <c:forEach var="list" items="${unitList}">
                <div class="left_container-item_list">
                  <div class="left_container-item_list-content">${list['unitName'] }</div>
                  <div class="left_container-item_list-num">
                    (<span class="left_container-item_list-detail">0</span>)
                  </div>
                  <input class="left_container-item_list-value" type="hidden" value="${list['unitId'] }  " />
                </div>
              </c:forEach>
            </div>
            <div class="achievement_conter_left left-filter_list" filter-id="dcCode" filter-title="学科" id="leftNature"
              filter-search="hidden" filter-selector="single" filter-open="close">
              <c:forEach var="list" items="${discpList}">
                <div class="left_container-item_list">
                  <div class="left_container-item_list-content">${list['dcName'] }</div>
                  <div class="left_container-item_list-num">
                    (<span class="left_container-item_list-detail">0</span>)
                  </div>
                  <input class="left_container-item_list-value" type="hidden" value="${list['dcCode'] }  " />
                </div>
              </c:forEach>
            </div>
            <div class="achievement_conter_left left-filter_list" filter-id="posId" filter-title="职称" id="leftPos"
              filter-search="hidden" filter-selector="single" filter-open="close">
              <c:forEach var="list" items="${posList}">
                <div class="left_container-item_list">
                  <div class="left_container-item_list-content">${list['posName'] }</div>
                  <div class="left_container-item_list-num">
                    (<span class="left_container-item_list-detail">0</span>)
                  </div>
                  <input class="left_container-item_list-value" type="hidden" value="${list['posId'] }  " />
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
                人员列表 <span>（总数：<span id="top_totalcount" class="examine_totla"></span>）
                </span>
              </p>
              <div id="increased">
                <a href="#" onclick="add()" class="increased"><span></span>新增</a> <a href="#" class="derive ttc">发送账号</a>
                <a href="#" class="derive ttc">设置学科</a> <a href="#" class="derive"><span></span> 导出</a>
              </div>
            </div>
            <div class="headline ftbold">
              <span class="check_fx"> <input type="checkbox" class="ipt-hide"> <label class="checkbox"
                id="selectAll"> </label>
              </span> <span class="fr f999">项目 / 成果 / 专利</span> 姓名 / 职称 / 部门 / 学科 / 联系方式
            </div>
          </div>
          <!--大数据列表-->
          <div id="mainlist" class="right-content_container list_left_boder"></div>
        </div>
      </div>
      <p class="clear"></p>
    </div>
    <div class="ball_content_top" id="popup" style="visibility: hidden;">
      <ul class="ball_content" style="padding: 0px;">
        <li class="ball_content_paper"><a href="/psnweb/person/importpage"><i class="icon_file_paper"></i>
            <p>文件导入</p></a>
          <p class="ball_content_paper_pattern">通过文件方式批量导入人员</p></li>
        <li class="ball_content_handwork"><a href="#"><i class="icon_file_handwork"></i>
            <p>手工录入</p></a>
          <p class="ball_content_paper_pattern">通过手工录入方式逐条录入人员</p></li>
      </ul>
    </div>
</body>
</html>
