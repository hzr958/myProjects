<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%><html>
<head>
<title>科研之友</title>
<meta charset="utf-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/mobile.css">
<script type="text/javascript" src="/resmod/js/jquery.js"></script>
<script type="text/javascript">
       $(function(){
         selectRecommendFundMenu("fundFind");
       })
      //处理选中效果
        function dealSelectStatus(obj){
          $(".new-financial_body-items_check").removeClass("new-financial_body-items_check");
            $(obj).addClass("new-financial_body-items_check");
            location.href="/prj/mobile/fundfindmain?searchRegionCodes="+$(obj).attr("value");
        }
      
      function goMyhome(){
        location.href="/psnweb/mobile/myhome";
      }
    </script>
<body>
  <div class="message-page__header"
    style="position: fixed; top: 0px; z-index: 55; display: flex; justify-content: space-between;">
    <i class="material-icons" style="margin-left: 15px; width: 10vw;" onclick="window.history.back();">keyboard_arrow_left</i>
    <span style="width: 80vw; display: flex; justify-content: center; align-items: center;">基金发现</span> <i
      style="width: 10vw; margin-right: 15px"></i>
  </div>
  <div class="new-financial_body-container" style="padding: 0px; margin: 60px 0px 60px 0px; justify-content:center;">
  <div class="new-financial_body-items" onclick="dealSelectStatus(this)" id="region_157" value="157">国家</div>
    <c:forEach items="${regionList}" var="region" varStatus="stat">
      <div class="new-financial_body-items" onclick="dealSelectStatus(this)" id="region_${region.id}"
        value="${region.id}">${region.zhName}</div>
    </c:forEach>
  </div>
  <%@include file="/WEB-INF/jsp/mobile/bottom/mobile_fund_bottom.jsp"%>
</body>
</html>