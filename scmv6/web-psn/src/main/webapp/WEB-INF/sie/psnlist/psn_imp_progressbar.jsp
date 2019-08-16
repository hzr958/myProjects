<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <title><s:text name="group.list.title" /></title> <%-- <link rel="stylesheet" type="text/css" href="${res }/css_v5/public.css" />
<link rel="stylesheet" type="text/css" href="${res }/css_v5/common.css" />
<link rel="stylesheet" type="text/css" href="${res }/css_v5/pop.css" />
<link rel="stylesheet" type="text/css" href="${res }/css_v5/plugin/jquery.scmtips.css" />
<link type="text/css" rel="stylesheet" href="${res }/css_v5/plugin/jquery.thickbox.css" />
<link type="text/css" rel="stylesheet" href="${res }/css_v5/plugin/jquery.alerts.css" /> --%>
  <script type="text/javascript">
var locale = "${locale}";var ctxpath="${ctx}";var respath="${res}";
var domainPath = "http://" + document.domain;
var imgUrlPath ="";
var res="${res}";
</script>
  <%-- <script type="text/javascript" src="${res }/js_v5/prototype/prototype.js"></script>  --%>
  <script type="text/javascript" src="${ressie}/js/plugin/prototype.js"></script>
  <script type="text/javascript" src="${respsn}/person/jsProgressBarHandler.js"></script>
  <script type="text/javascript">
window.onload=function(){
	var myProgressBar = null;
	var timerId = null;
	setTimeout(function (){
		myJsProgressBarHandler.setPercentage('element1','0');
		loadProgressBar();
	},1000);
}

function loadProgressBar(){
	timerId = window.setInterval(function() {
		
		var oldrate=myJsProgressBarHandler.getPercentage('element1');
		if (true && oldrate>=100){
			//进度完成后回调
			setTimeout(function (){
				parent.$.Thickbox.closeWin()
				parent.finish();
			},2000);
		}else{
			//获取进度函数
			parent.getProgress(function(data){
				//修改进度回调函数
				if(data.result=='success'){
					myJsProgressBarHandler.setPercentage('element1','+'+(data.rate-oldrate));
				}
			});
			
		} 
	},
	2000);
}
</script>
</head>
<body>
  <div class="dialog_content" style="padding-top: 20px; padding-left: 25px;">
    <div id="demo">
      <span class="progressBar" id="element1">0%</span> <br /> <br />
    </div>
  </div>
</body>
</html>