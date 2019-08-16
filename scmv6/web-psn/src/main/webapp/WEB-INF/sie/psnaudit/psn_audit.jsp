<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<head>
<meta charset="UTF-8">
<title>Title</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>unit</title>
<link rel="stylesheet" href="${ressie }/css/achievement_lt.css" />
<link rel="stylesheet" href="${ressie }/css/index.css" />
<script type="text/javascript" src="${respsn}/person/siePsnAudit.js"></script>
<%-- <script type="text/javascript"src="${ressie }/js/plugin/scmpc_autofilllist.js"></script> --%>
<script type="text/javascript" src="${ressie }/js/plugin/scmpc_autofill.js"></script>
<!-- <style type="text/css"> -->
<!-- /*    .footer{ */ -->
<!-- /*       margin-top: 415px; */ -->
<!-- /*    }  */ -->
<!-- </style> -->
<script type="text/javascript">
var local='${locale}';
     $(document).ready(function(){
//          loadData();
         scmpcListfilling({
             targetleftUrl:"/psnweb/person/auditlist",
             targetcntUrl:"/psnweb/audit/ajaxleftcount"          
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
   });
    
 </script>
</head>
<body>
  <!--部门-->
  <div class="conter">
    <!--     <div id="con_five_1" style="display:none"></div> -->
    <!--     <div id="con_five_2" style="display:none"></div> -->
    <div id="con_five_3" class="hover">
      <!--左边-->
      <div class="achievement_conter">
        <div class="left-file__fill_container">
          <div class="left_container-search_box left-top_search-container">
            <input placeholder="检索" class="left_container-search_input" value=""> <i
              class="material-icons left_container-search_tip">search</i>
          </div>
          <div class="left-filter_item" style="display: flex; flex-direction: column;">
            <div class="achievement_conter_left left-filter_list" filter-id="unitId" filter-title="部门" id="leftProv"
              filter-search="hidden" filter-selector="single">
              <c:forEach var="list" items="${ConstUnits}">
                <div class="left_container-item_list">
                  <div class="left_container-item_list-content">${list['itemName']}</div>
                  <div class="left_container-item_list-num">
                    (<span class="left_container-item_list-detail">0</span>)
                  </div>
                  <input class="left_container-item_list-value" type="hidden" value="${list['itemId']}  " />
                </div>
              </c:forEach>
            </div>
            <div class="achievement_conter_left left-filter_list" filter-id="status" filter-title="审核状态" id="leftNature"
              filter-search="hidden" filter-selector="single" filter-open="close">
              <c:forEach var="list" items="${ConsAuditStatus}">
                <div class="left_container-item_list">
                  <div class="left_container-item_list-content">${list['itemName']}</div>
                  <div class="left_container-item_list-num">
                    (<span class="left_container-item_list-detail">0</span>)
                  </div>
                  <input class="left_container-item_list-value" type="hidden" value="${list['itemId']}  " />
                </div>
              </c:forEach>
            </div>
            <div class="achievement_conter_left left-filter_list" filter-id="posId" id="leftPos" filter-title="职称"
              filter-search="hidden" filter-selector="single" filter-open="close">
              <c:forEach var="list" items="${posList}">
                <div class="left_container-item_list">
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
        <div class="achievement_conter_right">
          <div class="achievement_achievement" style="text-align: center;">
            <p>
              人员审核 <span>（总数：<span class="examine_totla">${totalNum}</span>）
              </span>
            </p>
            <div id="increased">
              <a href="#" class="derive" onclick="aprove()">批准</a> <a href="#" class="derive" onclick="reject()"> 拒绝</a>
            </div>
          </div>
          <div class="headline ftbold">
            <span class="check_fx"> <input type="checkbox" class="ipt-hide" alt="all"> <label
              class="checkbox" id="selectAll"> </label>
            </span> <span class="fr f999">状态 / 申请日期&nbsp;&nbsp;</span> 姓名 / 职称 / 联系方式
          </div>
          <div id="fresh" class="achievement_conter_right right-content_container list_left_boder"></div>
        </div>
        <div class="clear"></div>
      </div>
    </div>
    <!--     <div id="con_five_4" style="display:none"></div> -->
  </div>
</body>
</html>