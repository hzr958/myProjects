<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Title</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>unit</title>
<link rel="stylesheet" href="${ressie }/css/achievement_lt.css" />
<link rel="stylesheet" href="${ressie }/css/index.css" />
<script type="text/javascript" src="${respsn}/person/siePsn.js"></script>
<script type="text/javascript" src="${ressie }/js/plugin/scmpc_autofilllist.js"></script>
<script type="text/javascript">
     $(document).ready(function(){
    	 scmpcListfilling({
    		 targetID:"leftProv",
    		 targettitle:"部门",
    		 targetmark:"unitType",
    		 targetlistId:"fresh",
    		 targetcnturl:"/insweb/unit/getunittypecount",
    	     targeturl:"/insweb/unit/unitlist"
    	 });
    	   scmpcListfilling({
               targetID:"leftProv2",
               targettitle:"学科",
               targetmark:"disCode",
               targetlistId:"fresh",
//                targetcnturl:"/insweb/unit/getunittypecount",
               targeturl:"/insweb/unit/unitlist"
           });
    	   query();
   });
 </script>
</head>
<body>
  <div class="achievement_conter">
    <div class="achievement_conter_left">
      <div class="achievement_conter_left" id="leftProv">
        <c:forEach var="list" items="${mapList}">
          <div class="left_container-item_list">
            <div class="left_container-item_list-content">${list.value}</div>
            <div class="left_container-item_list-num">
              (<span class="left_container-item_list-detail">0</span>)
            </div>
            <input class="left_container-item_list-value" type="hidden" value="${list.key}  " />
          </div>
        </c:forEach>
      </div>
      <div class="achievement_conter_left" id="leftProv2">
        <c:forEach var="obj" items="${mapDiscis}">
          <div class="left_container-item_list">
            <div class="left_container-item_list-content">${obj.value}</div>
            <div class="left_container-item_list-num">
              (<span class="left_container-item_list-detail">0</span>)
            </div>
            <input class="left_container-item_list-value" type="hidden" value="${obj.key}  " />
          </div>
        </c:forEach>
      </div>
    </div>
    <div class="achievement_conter_right">
      <form name="mainForm" id="mainForm" action="#" method="post">
        <div class="achievement_achievement">
          <div class="fr" id="increased">
            <a href="#" class="increased"><span></span>新增</a> <a href="#" class="derive" onclick="exportExcel();"><span></span>
              导出</a>
          </div>
          <p>
            人员列表 <span>（总数：${totalNum}）</span>
          </p>
        </div>
        <div class="headline ftbold">
          <span class="fr f999">项目&nbsp; /&nbsp; 成果&nbsp; /&nbsp; 专利</span> 姓名&nbsp; / &nbsp;职称 &nbsp; / &nbsp; 部门&nbsp;
          / &nbsp; 学科&nbsp; / &nbsp; 联系方式
        </div>
        <!--大数据列表-->
        <div id="fresh"></div>
        <!--翻页-->
      </form>
    </div>
  </div>
</body>
</html>
