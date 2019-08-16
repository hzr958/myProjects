<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<head>
<meta charset="utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="X-UA-Compatible" content="IE=10;IE=9; IE=8; IE=EDGE" />
<title><s:text name="project.enter.project" /></title>
<script type="text/javascript">

</script>
<style type="text/css">
label.error {
  color: #c00;
  background: transparent url(${res}/images_v5/mainreg_icon_error.gif) no-repeat scroll 0 0;
  padding-left: 20px;
}
</style>
  <link rel="stylesheet" type="text/css" href="${resmod}/smate-pc/css/scmpcframe.css">
  <script type="text/javascript" src="${res }/js_v5/home/enter.utility.js"></script>
  <script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
  <script type="text/javascript" src="${resprj}/sns/prj/v9edit/project.enter_${locale}.js"></script>
  <script type="text/javascript" src="${resprj}/sns/prj/v9edit/project.enter.js"></script>
  <script type="text/javascript" src="${resmod}/js/smate.filestyle.js"></script>
  <script type="text/javascript" src="${resmod}/js/plugin/jquery.fileupload.js"></script>
  <script type="text/javascript" src="${resprj}/sns/prj/v9edit/project-attach-upload.js"></script>
<%@ include file="edit_i18n.jsp"%>
<script type="text/javascript">
    var currentNodeId = "";
    var saveSuccess = "<%=request.getParameter("saveSuccess") %>";
    window.onload = function() {

        /*var checkstyle = document.getElementsByClassName("arrow-selected_icon")[0];
        var selected_box = document.getElementsByClassName("arrow-selected_box")[0];
        selected_box.onclick = function (e) {
            e.stopPropagation();
            var obj = $(this).find(".arrow-selected_icon")[0];
            if (obj.innerHTML == "arrow_drop_down") {
                obj.innerHTML = "arrow_drop_up";
                obj.parentNode.querySelector(".arrow-selected_container").style.display = "block";
            } else {
                obj.innerHTML = "arrow_drop_down";
                obj.parentNode.querySelector(".arrow-selected_container").style.display = "none";
            }
        };

        document.onclick = function () {
            if (checkstyle.innerHTML == "arrow_drop_up") {
                checkstyle.innerHTML = "arrow_drop_down";
                checkstyle.parentNode.querySelector(".arrow-selected_container").style.display = "none";
            }
        }*/
    }


   $(document).ready(function() {


       //隐私类型
       var authority = $("#_prj_authority").val();
       if(authority == "7"){
           $("#_prj_authority_show").val($("#_prj_authority_1").html());
       }else{
           $("#_prj_authority_show").val($("#_prj_authority_2").html());
       }

    //项目类型
    var prj_type = $("#_project_prj_type").attr("type") ;
    if(prj_type == "1"){
      $("#_project_prj_type1").addClass("selected-oneself_confirm");
      $("#_project_prj_type2").removeClass("selected-oneself_confirm");
    }else{
        $("#_project_prj_type1").removeClass("selected-oneself_confirm");
        $("#_project_prj_type2").addClass("selected-oneself_confirm");
        $("#_project_prj_type_h").val("0");//默认值
    }
    //项目状态
    var prj_state = $("#_project_prj_state").attr("state");
    if(prj_state == "01"){
        $("#_project_prj_state").find(".selected-oneself").removeClass("selected-oneself_confirm");
        $("#_project_prj_state1").addClass("selected-oneself_confirm");
    }else if(prj_state == "02"){
        $("#_project_prj_state").find(".selected-oneself").removeClass("selected-oneself_confirm");
        $("#_project_prj_state2").addClass("selected-oneself_confirm");
    }else{
        $("#_project_prj_state").find(".selected-oneself").removeClass("selected-oneself_confirm");
        $("#_project_prj_state3").addClass("selected-oneself_confirm");
        $("#_project_prj_stat_h").val("03");//默认值
    }

    // 项目摘要
       var abstract = $("#_project_zh_abstract").val();
       abstract = abstract.replace("</span>","");
       abstract = abstract.replace("<span>","");
       abstract = $.trim(abstract);
       $("#_project_zh_abstract").val(abstract);
	//保存成功
	if(saveSuccess && saveSuccess == 'true'){

		if(true){
            scmpublictoast("<s:text name='projectEdit.label.save.ok'/>" , 2000) ;
		}else{
			$.scmtips.show("success","<s:text name='projectEdit.label.save.missparameters'/>");
			setTimeout(function(){
				ProjectEnter.forcusError();
			},1000);
		}

	};

	//项目成员
    ProjectMember.dealMoveArrow();

    //项目附件
    ProjectAttachment.initialization($("#projectAttachmentList")[0]);

   //上传全文
   ProjectFulltext.initialization($("#upload_fulltext_view")[0]);
   if($("#_prj_fulltext_file_id").val() == ""){
       $("#upload_fulltext_view").css("display","");
       $("#upload_fulltext_content").css("display","none");
   }
   if($("#fulltext_permission").val() == 2){
       $("#jsFullTextPic").removeClass("selected-func_close-open");
       $("#jsFullTextPic").attr("title","仅自己可以查看");
   };

     //添加关键词
       ProjectEnter.showKeyItem();
       ProjectEnter.showEnKeyItem();
     $("#addKeyInput").keyup(function(event){
         event.stopPropagation();
         if(event.keyCode == 13){
             ProjectEnter.addkey(event);
         }
     });
     $("#addKeyInput").blur(function(event){
         event.stopPropagation();
         ProjectEnter.addkey(event);
     });
     $("#addKeyInput").on("input",function(event){
         event.stopPropagation();
         if(/[;；]/.test($(this).val())){
             ProjectEnter.addkey(event);
         }
     });
       $("#addKeyInputEn").keyup(function(event){
           event.stopPropagation();
           if(event.keyCode == 13){
               ProjectEnter.addEnkey(event);
           }
       });
       $("#addKeyInputEn").blur(function(event){
           event.stopPropagation();
           ProjectEnter.addEnkey(event);
       });
       $("#addKeyInputEn").on("input",function(event){
           event.stopPropagation();
           if(/[;；]/.test($(this).val())){
               ProjectEnter.addEnkey(event);
           }
       });
      // end

       //项目日期
       ProjectEnter.initProjectDate();
       ProjectEnter.listenerPrjDate();

       //项目成员
       ProjectMember.initListenerPrjMember();
       ProjectMember.checkPrjMember();

       //绑定事件
       window.addFormElementsEvents();
       //window.addFormElementsEvents();
       //金额
       ProjectEnter.listenerAmount();

       ProjectEnter.listenerEmail();


});

</script>

</head>
