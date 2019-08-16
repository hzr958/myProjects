<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
<style type="text/css">
.page-section {
  width: 72px;
  height: 24px;
  line-height: 24px;
  text-align: center;
  border: 1px solid #aaa;
  cursor: pointer;
  background-color: #e1e1e1;
}

.chip_selected {
  background-color: #fff;
  border-bottom: none;
}

.page-section-box {
  top: 64px;
  left: 128px;
  position: absolute;
  margin: 64px auto 0 auto;
  width: 800px;
  height: 500px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.page-section-line {
  width: 12px;
  height: 24px;
  border-bottom: 1px solid #aaa;
}

.page-section-line2 {
  width: 560px;
  height: 24px;
  border-bottom: 1px solid #aaa;
}
</style>
<script type="text/javascript">
	var resmodctx = "${resmod}";
	var ctxpath = "${ctx}";
	var resmodpath = "${resmod}";
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/jquery.scmtips.css" />
<script type="text/javascript" src="${resmod}/js/plugin/jquery.scmtips.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.watermark.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		patentMaintLatedLeftMenu();
		patentMaintSearch(null,0,1);
		addEvent();
	});

	addEvent=function(){
		var attr=[1,2,3];
		var codeset;
		var sum = document.getElementsByClassName("page-section");
		for(var i = 0; i<sum.length; i++){
	        sum[i].addEventListener("click",function(){
	        	var text =  this.innerText;
	            if(text==="机构"){
	            	codeset = 1;
	        	}else if(text==="人员"){
	        		codeset = 2;
	        	}else{
	        		codeset = 3;
	        	}
	            
	            var mId = 0;
	    		if(document.getElementById("leftMenuId")){
	    			mId = document.getElementById("leftMenuId").getAttribute("value");
	    		}

	            if(document.getElementsByClassName("chip_selected")){
	                var tal = document.getElementsByClassName("chip_selected");
	                for(var j = 0; j<tal.length; j++){
	    	            tal[j].setAttribute("class","page-section");
	    	        };
	        	}
	        	if(this.classList.contains("chip_selected")){
	        	 	this.setAttribute("class","page-section");
	        	}else{
	        		document.getElementById("main4").setAttribute("code",codeset);
	                this.className+=" "+"chip_selected";
	        	 }
	    	   //patentMaintSearch(null,"${mId}",codeset);
	        	patentMaintSearch(null,mId,codeset);
	        })
		}

		
	}
	
	
	selectCurrentPage = function(codeset){
		var sum = document.getElementsByClassName("page-section");
		
		if(sum != null&&sum!=undefined){
			for(var j = 0; j<sum.length; j++){
				if(j==codeset-1){
					sum[j].setAttribute("class","page-section chip_selected");
				}else{
					sum[j].setAttribute("class","page-section");
				}
			}	
		}
	}
	
	
	menuSelected = function(obj) {
		$(obj).closest(".ax_default-text").find(".ax_default-section").find("span").removeClass("ax_selected");
		$(obj).find("span").addClass("ax_selected");
	}

	patentMaintSearch = function(obj,mId,codeset) {
		var mId = mId;
		var codeset = codeset;
		$.ajax({
			url : "/scmmanagement/keywordsdistr/ajaxkeyModulesinfo",
			type : "post",
			dataType : "html",
			data : {
				"mId" : mId,
				"modId":codeset
			},
			timeout : 10000,
			success : function(data) {
				$("#main4").html(data);
				menuSelected(obj);
				selectCurrentPage(codeset);
				document.getElementById("leftMenuId").setAttribute("value", mId);
			}
		});
	}

	patentMaintLatedLeftMenu = function() {
		$.ajax({
			url : "/scmmanagement/keywordsdistr/ajaxkwcategoryLeft",
			type : "post",
			data : {
				"id" : 1
			},
			dataType : "html",
			timeout : 10000,
			success : function(data) {
				if (data) {
					$("#base").html(data);
				}
			}
		});
	}
</script>
</head>
<body>
  <div id="content" style="display: flex; justify-content: center; align-items: center; margin_right: 32px;">
    <input type="hidden" value="${mId}" name="leftMenuId" id="leftMenuId">
      <div id="base" class=""></div>
      <div id="u297" class="ax_default line">
        <img id="u297_img" class="img " src="${resmod}/images/u95.png">
      </div>
      <div
        style="display: flex; justify-content: flex-start; align-items: center; position: relative; margin: -638px 0 0 16px;">
        <div class="page-section-line"></div>
        <div class="page-section chip_selected">机构</div>
        <div class="page-section-line"></div>
        <div class="page-section">人员</div>
        <div class="page-section-line"></div>
        <div class="page-section">论文</div>
        <div class="page-section-line2"></div>
        <div id="main4" class="page-section-box" code="1"></div>
      </div>
  </div>
</body>
</html>