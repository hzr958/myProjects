<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<head>
<meta charset="utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="X-UA-Compatible" content="IE=10;IE=9; IE=8; IE=EDGE" />
<title><s:text name="project.enter.project" /></title>
<script type="text/javascript">
	var publicationEdit_upload_tip_content = '<s:text name="publicationEdit.upload.tip_content"/>';
	var publicationEdit_upload_tip_content2 = '<s:text name="publicationEdit.upload.tip_content2"/>';
	publicationEdit_upload_tip_content = publicationEdit_upload_tip_content.replace("anyones","anyone's");
	publicationEdit_upload_tip_content2 = publicationEdit_upload_tip_content2.replace("anyones","anyone's");
	var maint_alart_deleteTipTitle ='<s:text name="maint.alart.deleteTipTitle"/>';
	var watermark_tips = '<s:text name="project.enter.watermark.tips"/>';
	var resctx = "${res}";
	var respath = resctx;
	var fileLink = "<iris:fileLink></iris:fileLink>";
	var lang = "${lang}";
	var tabIndex = "${tabIndex}";
	var locale = "${locale}";
	var saveSuccess = "${saveSuccess}";
	var currentPsnId = "${currentPsnId}";
	var currentNodeId = "${currentNodeId}";
	var jsessionId = "<%=session.getId() %>";
	<c:choose>
	<c:when  test="${webContextType eq 'scmwebrol'}">
		 var webContextType = "scmwebrol";
	</c:when>
	<c:otherwise >
		var webContextType = "scmwebsns";
	</c:otherwise>
</c:choose>
</script>
<style type="text/css">
label.error {
  color: #c00;
  background: transparent url(${res}/images_v5/mainreg_icon_error.gif) no-repeat scroll 0 0;
  padding-left: 20px;
}
</style>
<link rel="stylesheet" type="text/css" href="${res}/css_v5/plugin/jquery.validate.css" />
<link rel="stylesheet" type="text/css" href="${res}/css_v5/plugin/jquery.complete.css" />
<link rel="stylesheet" type="text/css" href="${res}/css_v5/plugin/jquery.scmtips.css" />
<link rel="stylesheet" type="text/css" media="screen" href="${res}/css_v5/plugin/jquery.alerts.css" />
<link rel="stylesheet" type="text/css" href="${res}/css_v5/plugin/jquery.thickbox.css" />
<link rel="stylesheet" type="text/css" href="${res}/css_v5/plugin/jquery.discipline.css" />
<link rel="stylesheet" type="text/css" href="${res}/css_v5/plugin/jquery.autoword.css" />
<link rel="stylesheet" type="text/css" href="${res}/css_v5/plugin/jquery.authorityselector.css" />
<link rel="stylesheet" type="text/css" href="${res}/css_v5/common.css" />
<script type="text/javascript" src="/resmod/js/plugin/jquery.fileupload.js"></script>
<script type="text/javascript" src="/resmod/js/plugin/jquery.fileupload-check.js"></script>
<script type="text/javascript" src="${resprj}/sns/prj/edit/project-fulltext-upload.js"></script>
<script type="text/javascript" src="${resprj}/sns/prj/edit/project-attach-upload.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.filestyle.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.autoword.js"></script>
<script type="text/javascript" src="/resmod/js_v5/plugin/jquery.complete.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.discipline.js"></script>
<script type="text/javascript" src="/resmod/js_v5/plugin/jquery.disccomplete.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.watermark.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.thickbox.resources.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.thickbox.min.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.scmtips.js"></script>
<script type="text/javascript" src="${res }/js_v5/home/enter.utility.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.alerts_${locale}.js"></script>
<%-- <script type="text/javascript" src="${res}/js_v5/plugin/jquery.alerts.js"></script> --%>
<script type="text/javascript" src="${res }/js_v5/home/project/prjenter.base.js"></script>
<script type="text/javascript" src="${resprj}/sns/prj/edit/project.enter.js"></script>
<script type="text/javascript" src="${res }/js_v5/home/project/prjEnterTable_${locale}.js"></script>
<script type="text/javascript" src="${res }/js_v5/home/project/prjEnterTable.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.validate.js"></script>
<script type="text/javascript" src="${res }/js_v5/plugin/jquery.authorityselector.js"></script>
<%@ include file="edit_i18n.jsp"%>
<script type="text/javascript">
	var editors={};
$(document).ready(function() {
  dealAbstractVal();
	$("a.thickbox,input.thickbox").thickbox();
	
	 if(tabIndex && tabIndex.length>0){
		 $("#"+tabIndex).click();		 
	 }
	 
	//隐私设置
	function callback_authority(params,value){
		$("#_prj_authority").val(value);
	}
	$('.selectClass').authoritydiv({"authorityValue":"${authority}","locale":"${locale}"},{},callback_authority);
	 
	//全文
	 $("input[id=filedata]").filestyle({ 
    	 image: "${res}/images_v5/browse_icon_${locale}.gif",
   	     imageheight : 22,
   	     imagewidth : 60,
   	     width : 385
	 }).checkFile({
	     'allowSize': '30MB',
	     'pass': function(){
             if($(this).val().length>0){
                 $('.filedata').val($(this).val().substring($(this).val() .replace(/\\/g, '/').lastIndexOf('/') + 1));
                 $('.filedata').css("color","#000");
                 $("#btnUpload2").attr("disabled",false);    
             }
	     },
	     'fail': function(error){
	         if(error == 'allowSize'){
	             scmpublictoast('<s:text name="projectEdit.label.fulltextfile.upload.msg" />', 2000);
	         }
	     }
   });
	 $('.filedata').watermark({
			tipCon : '<s:text name="application.file.upload.maxSize"/>'
	 });
	 var maxFileDescLength=200;
  	 $('#_fulltext_fileupload_desc').watermark({
		tipCon : '<s:text name="application.file.floatDiv.fileDescSize"/>'
	 }).keyup(function(){ 
		 var area=$(this).val(); 
		 if(area.length>maxFileDescLength){ 
			 $(this).val(area.substring(0,maxFileDescLength)); 
		 } 
	 }).blur(function(){ 
		 var area=$(this).val(); 
		 if(area.length>maxFileDescLength){ 
			 $(this).val(area.substring(0,maxFileDescLength)); 
		 } 
	 });
	 //附件(修改宽度为61px_MJG_ROL-1062).
	 $("input[id=filedata1]").filestyle({ 
		 image: "${res}/images_v5/home/file_upload_${locale}.png",
   	     imageheight : 22,
   	     imagewidth : 61,
   	 	 display:"none"
	 }).checkFile({
         'allowSize': '30MB',
         'pass': function(){                      
             if($(this).val().length > 0){
                 $('.filedata1').val($(this).val().substring($(this).val() .replace(/\\/g, '/').lastIndexOf('/') + 1));
                 confirmAttachUpload();
             }
         },
         'fail': function(error){
             if(error == 'allowSize'){
                 scmpublictoast('<s:text name="projectEdit.label.fulltextfile.upload.msg" />', 2000);
             }
         }
     });


	//学科
	$(".disc_input_class").disccomplete({
	    "ctx": "/prjweb",
	    "url": '/const/ajax-disciplines',
	    "respath": "${res}",
		"supportExt":false,//是否支持推荐插件
	    "allowUserInput":false,//是否允许用户输入，不采用nsfc学科
		"discLevel" : 3,
		"input_width" : 305,
	    "discBind":function(result, type, _close){
			if(type=="save"){
				var _input = $(result["obj"]).prev(".disc_input");
	            if (_input != null) {
	                var _val = result["val"]();//disc结果集
	                _input.attr("code", _val == null ? "" : _val["disc_code"]);
	                _input.val(_val == null ? "" : _val["name"]);
					_input.attr("disc_id", _val == null ? "" : _val["id"]);
	                _input.attr("disc_code", _val == null ? "" : _val["disc_code"]);
	                $("#_project_discipline_id").val(_val == null ? "" : _val["id"]);
				}
        	}
		},
		"bind" : function(data){
			if(data){
				$("#_project_discipline_id").val(data.id);
			}
		}
	});
	$(".disc_input").click(function(){
	    BaseUtils.checkCurrentSysTimeout("/prjweb/ajaxtimeout", function(){
			$(this).next("span").focus().click();
	    }, 0);
	});
	
	if($("#_project_discipline_id").attr("disc_name")!=""){
		$("#discipline_input_0").val($("#_project_discipline_id").attr("disc_name"));
		$("#discipline_span_0").attr("disc_code",$("#_project_discipline_id").attr("disc_code"));
	}
	//关键词自动提示
	$("#_project_zh_keywords_div").autoword({
        "watermark_flag":true,
        "watermark_tip":watermark_tips
    });
	$("#_project_en_keywords_div").autoword({
        "watermark_flag":true,
        "watermark_tip":watermark_tips
    });
	
	$.each($("#_project_zh_keywords").val().split(";"), function(){
		if(this.length>0){
			if(this.indexOf(",")>=0 || this.indexOf("；")>=0 || this.indexOf("，")>=0){
				$.each(this.split(","), function(){
					if(this.length>0){
						if(this.indexOf("；")>=0 || this.indexOf("，")>=0){
							$.each(this.split("；"), function(){
								if(this.length>0){
									if(this.indexOf("，")>=0){
										$.each(this.split("，"), function(){
											if(this.length>0){
												$.autoword["_project_zh_keywords_div"].put(this,this);
											}
										});
									}else{
										$.autoword["_project_zh_keywords_div"].put(this,this);
									}
								}
							});
						}else{
							$.autoword["_project_zh_keywords_div"].put(this,this);
						}
					}
				});
			}else{
	    		$.autoword["_project_zh_keywords_div"].put(this,this);
			}
		}
	});
	
	$.each($("#_project_en_keywords").val().split(";"), function(){
		if(this.length>0){
			if(this.indexOf(",")>=0 || this.indexOf("；")>=0 || this.indexOf("，")>=0){
				$.each(this.split(","), function(){
					if(this.length>0){
						if(this.indexOf("；")>=0 || this.indexOf("，")>=0){
							$.each(this.split("；"), function(){
								if(this.length>0){
									if(this.indexOf("，")>=0){
										$.each(this.split("，"), function(){
											if(this.length>0){
												$.autoword["_project_en_keywords_div"].put(this,this);
											}
										});
									}else{
										$.autoword["_project_en_keywords_div"].put(this,this);
									}
								}
							});
						}else{
							$.autoword["_project_en_keywords_div"].put(this,this);
						}
					}
				});
			}else{
	    		$.autoword["_project_en_keywords_div"].put(this,this);
			}
		}
	});

	//检查项目状态
	//checkPrjState();
	
	/*  $("#_project_ins_name").complete({"key":"ins_name","bind":{"code":"_project_ins_id","enName":"_project_ins_name","callback":function(data){
		  $("#callback_result").html(data.code);
	}}}); */
	//保存成功
	if(saveSuccess && saveSuccess == 'true'){
		var flag = ProjectEnter.getPrjInstance().validate();
		if(flag){
			$.scmtips.show("success","<s:text name='projectEdit.label.save.ok'/>");
		}else{
			$.scmtips.show("success","<s:text name='projectEdit.label.save.missparameters'/>");
			setTimeout(function(){
				ProjectEnter.forcusError();
			},1000);
		}

	};
	
	//登录框登录不刷新
    $("#login_box_refresh_currentPage").val("false");
 
});

function isShowToolbar(id){
	 var editor=editors[id];
	 if ($(editor.toolbar.div).is(':visible')) {
		 editor.toolbar.hide();
	 } else {
		 editor.toolbar.show();
	 }
}

//去掉格式标签
function dealAbstractVal(){
  var enAbs = $("#_project_en_abstract").val();
  var zhAbs = $("#_project_zh_abstract").val();
  if(!BaseUtils.checkIsNull(zhAbs)){
    $("#_project_zh_abstract").val(zhAbs.replace(/<[^>]+>/g, ""));
  }
  if(!BaseUtils.checkIsNull(enAbs)){
    $("#_project_en_abstract").val(enAbs.replace(/<[^>]+>/g, ""));
  }
}
</script>
<style type="text/css">
.pub_author_ul li {
  display: block;
}

.authority_div ul li {
  width: 130px;
}

.authority_div ul li a {
  width: 115px;
}

.authority_div ul li a:hover {
  width: 115px;
}
</style>
</head>