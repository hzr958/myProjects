<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<title>科研之友</title>
<link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/mobile.css">
<link href="${resmod}/mobile/css/style.css" rel="stylesheet" type="text/css">
<link href="${resmod}/css_v5/css2016/public2016.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/plugin/new-mobile_Hintframe.js"></script>
<script type="text/javascript">
	function ajaxScmScienceArea() {
        $.ajax({
            url : "/psnweb/mobile/ajaxareas",
            type : "post",
            data : {
                "isHomepageEdit": $("#isHomepageEdit").val()
            },
            dataType : "html",
            success : function(data) {
                $("#area_content").html(data);
            },
            error : function(data) {
              newMobileTip("网络错误，请稍后重试");
            }
        });
    }
	
	//保存
	function savePsnScienceArea(){
		BaseUtils.doHitMore($("#save_area_btn"), 1000);
		var totalSelected = parseInt($("#totalSelected").val());
		var url = window.location.href;
		var reqUrl = null;
		if(url.indexOf("reqUrl=")>0){
			reqUrl = url.substring(url.indexOf("reqUrl=")+7);
		}
		if(totalSelected == 0){
            newMobileTip("至少选择1个二级科技领域");
        }else if(totalSelected > 0){
            var selectedAreaArr = [];
            $(".new-edit_technology-items_subselect").each(function(){
                selectedAreaArr.push($(this).attr("id"));
            });
            $.ajax({
                url : "/psnweb/sciencearea/ajaxsave",
                type : "post",
                data: {
                    "scienceAreaIds": selectedAreaArr.join(",")
                },
                dataType: "json",
                success: function(data){
                    if(data.result == "success"){
                        //不是个人主页的编辑关键词，跳转首页
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
		
	    
	}
	
	$(function(){
		ajaxScmScienceArea();
	})
	
	function doNoting(type){
	    if("skip" == type){
	        document.location.href = "/psnweb/mobile/improvekeywords";
	    }else{
	    	window.history.go(-1);
	    }
    }
</script>
</head>
<body>
  <input type="hidden" id="isHomepageEdit" value="${isHomepageEdit }">
  <div id="area_content"></div>
  <div class="save">
    <s:if test="isHomepageEdit == 1">
      <input type="button" id="save_area_btn" class="save_btn" value="保存" onclick="savePsnScienceArea();" />
      <a onclick="doNoting('cancel');"><input type="button" class="skip_btn" value="取消"  style="float: right;"/></a>
    </s:if>
    <s:else>
      <input type="button" id="save_area_btn" class="save_btn" style="width: 100%;" value="保存"
        onclick="savePsnScienceArea();" />
    </s:else>
  </div>
</body>
</html>
