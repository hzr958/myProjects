<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
  <title></title>
  <meta charset="utf-8">
  <link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
  <script type="text/javascript" src="${resprj}/sns/institution/institution.base.js"></script>

  <script type="text/javascript">
      $(function(){
          var keylist = document.getElementsByClassName("create-InstitutionalGroups_header-item");
          for(var i = 0;i < keylist.length;i++){
              keylist[i].addEventListener("click",function(){
                  if(!this.classList.contains("create-InstitutionalGroups_header-checked")){
                      document.getElementsByClassName("create-InstitutionalGroups_header-checked")[0].classList.remove("create-InstitutionalGroups_header-checked");
                      this.classList.add("create-InstitutionalGroups_header-checked");
                  }
                  var item = $(this).attr("item");
                  Institution.ajaxInsPageList(item);
                  BaseUtils.changeLocationHref("item",item);
              })
          }
          var item = ${item};
          $(".create-InstitutionalGroups_header-item[item='"+item+"']").click();

      });
  </script>
</head>

<body>

<div class="create-InstitutionalGroups_container" style="min-height: 720px;">
  <div class="create-InstitutionalGroups_header">
    <div class="create-InstitutionalGroups_header-box">
      <span class="create-InstitutionalGroups_header-item create-InstitutionalGroups_header-checked" data-show="mine-focus" item="1">我管理的</span>
      <span class="create-InstitutionalGroups_header-item" data-show="Exten-item" item="2">我关注的</span>
    </div>
    <div class="create-InstitutionalGroups_header-btn">
      <div class="create-InstitutionalGroups_header-icon">+</div>
      <span class="create-InstitutionalGroups_header-detail" onclick="Institution.createInsPage();">创建机构主页</span>
    </div>
  </div>
  <div class="create-InstitutionalGroups_body" id="ins_list_id">

  </div>
</div>
</body>
</html>
