<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>科研之友</title>
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta charset="UTF-8">
<link href="${resmod }/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<link href="${resmod}/mobile/css/style.css" rel="stylesheet" type="text/css">
<link href="${resmod}/css_v5/css2016/public2016.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod }/js/common/smate.common.js"></script>

<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css" />
<script type="text/javascript">
    $(function(){
        dealAreaClickEvent(); //科技领域点击事件
    });
    
    //返回
    function doNoting(){
        window.history.back();
    };
    
    //科技领域点击事件
    function dealAreaClickEvent(){
        $(".dev_area").click(function(){//绑定点击事件
          if($(".new-edit_technology-items_subselect").length>=5 && !$(this).hasClass("new-edit_technology-items_subselect")){
            scmpublictoast("最多设置5个", 1000,3);
            return;
          }else{
            $(this).toggleClass("new-edit_technology-items_subselect"); 
          }
        });
    }
    function savePsnScienceArea(){
      if($(".new-edit_technology-items_subselect").length>5){
        scmpublictoast("最多设置5个", 1000,3);
        return;
      }
      if($(".new-edit_technology-items_subselect").length==0){
        scmpublictoast("最少设置一个", 1000,3);
        return;
      }
      var url = window.location.href;
      var reqUrl = null;
      if(url.indexOf("reqUrl=")>0){
          reqUrl = url.substring(url.indexOf("reqUrl=")+7);
      }
      var selectedAreaArr = [];
      $(".new-edit_technology-items_subselect").each(function(){
        selectedAreaArr.push($(this).attr("value"));
      })
      
      $.ajax({
        url : "/psnweb/sciencearea/ajaxsave",
        type : "post",
        data: {
            "scienceAreaIds": selectedAreaArr.join(",")
        },
        dataType: "json",
        success: function(data){
            if(data.result == "success"){
                 if($("#isHomepageEdit").val() == 0){
                   if(reqUrl){
                     document.location.href = "/psnweb/mobile/improvekeywords?reqUrl="+reqUrl;
                   }else{
                       document.location.href = "/psnweb/mobile/improvekeywords";
                   }
                 }else{
                  document.location.href = "/psnweb/mobile/homepage";
                 }
            }else{
              newMobileTip("网络错误，请稍后重试");
            }
        },
        error: function(){
          newMobileTip("网络错误，请稍后重试");
        }
       });
    }
    </script>
</head>
<body>
  <input type="hidden" id="isHomepageEdit" value="${psnOperateVO.isHomepageEdit }">

  <div class="new_subject-field">
    <div class="provision_container-title">
      <i onclick="javascript:SmateCommon.goBack('/psnweb/mobile/homepage');" class="material-icons">keyboard_arrow_left</i><span>设置科技领域</span> <i></i>
    </div>
    <div class="new_subject-field_checked-container_body">
      <div class="new_subject-field_checked-container_detail" id="scroller">
        <c:forEach items="${allAreas }" var="itemMap" varStatus="status">
          <div class="new_subject-field_item-title" value="${itemMap.categoryId}">
            <span>${itemMap.categoryZh}</span>
          </div>
          <div class="new_subject-field_item-container">
            <c:forEach items="${itemMap.secondLevel }" var="secondArea" varStatus="secondStatus">
              <c:if test="${secondArea.added=='true'}">
                  <div class="new_subject-field_item-detail new-edit_technology-items_subselect dev_area" value="${secondArea.categoryId}" parentid="${itemMap.categoryId}">${secondArea.categoryZh}</div>              
              </c:if>
              <c:if test="${secondArea.added!='true'}">
                  <div class="new_subject-field_item-detail dev_area" value="${secondArea.categoryId}" parentid="${itemMap.categoryId}">${secondArea.categoryZh}</div>
              </c:if>          
            </c:forEach>
          </div>
        </c:forEach>
      </div>
      <div class="save">
        <c:if test="${psnOperateVO.isHomepageEdit == 1}">
          <input type="button" id="save_area_btn" class="save_btn" value="保存" onclick="savePsnScienceArea();" />
          <a onclick="doNoting('cancel');"><input type="button" class="skip_btn" value="取消"  style="float: right;"/></a>
        </c:if>
        <c:if test="${psnOperateVO.isHomepageEdit != 1}">
          <input type="button" id="save_area_btn" class="save_btn new-add_keword-footer_item" style="width: 100%;" value="保存"
            onclick="savePsnScienceArea();" />
        </c:if>
      </div>
    </div>
  </div>
</body>
</html>