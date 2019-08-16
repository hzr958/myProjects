<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "https://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>会话设置页面</title>
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/mui/mui.min.css">
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/plugin/mui/mui.min.js"></script>
</head>
<body>
  <input type="hidden" id="deleteCache" name="deleteCache" value="0">
  <input type="hidden" id="sessionInvalidate" name="sessionInvalidate" value="0">
  <input type="hidden" id="clearContext" name="clearContext" value="0">
  <input type="hidden" id="clearThreadInfo" name="clearThreadInfo" value="0">
  <input type="hidden" id="deleteCookieAID" name="deleteCookieAID" value="0">
  <input type="hidden" id="deleteCookieOauthLogin" name="deleteCookieOauthLogin" value="0">
  <input type="hidden" id="deleteFundCache" name="deleteFundCache" value="0">
  <header class="mui-bar mui-bar-nav"> <!-- <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a> -->
  <h1 class="mui-title">选择要进行的操作</h1>
  </header>
  <div class="mui-content">
    <div class="mui-content-padded" style="margin: auto; text-align: center; width: 60vw">
      <ul class="mui-table-view">
        <li class="mui-table-view-cell"><span>删除缓存中的权限信息</span>
          <div class="mui-switch" paramId="deleteCache">
            <div class="mui-switch-handle"></div>
          </div></li>
        <li class="mui-table-view-cell"><span>让Session会话失效</span>
          <div class="mui-switch" paramId="sessionInvalidate">
            <div class="mui-switch-handle"></div>
          </div></li>
        <li class="mui-table-view-cell"><span>清空Spring Security中权限信息</span>
          <div class="mui-switch" paramId="clearContext">
            <div class="mui-switch-handle"></div>
          </div></li>
        <li class="mui-table-view-cell"><span>清空线程变量中的信息</span>
          <div class="mui-switch" paramId="clearThreadInfo">
            <div class="mui-switch-handle"></div>
          </div></li>
        <li class="mui-table-view-cell"><span>删除cookie中的AID</span>
          <div class="mui-switch" paramId="deleteCookieAID">
            <div class="mui-switch-handle"></div>
          </div></li>
        <li class="mui-table-view-cell"><span>删除cookie中的oauth_login参数</span>
          <div class="mui-switch" paramId="deleteCookieOauthLogin">
            <div class="mui-switch-handle"></div>
          </div></li>
        <li class="mui-table-view-cell"><span>删除资助机构左侧地区基金统计数缓存</span>
          <div class="mui-switch" paramId="deleteFundCache">
            <div class="mui-switch-handle"></div>
          </div></li>
      </ul>
      <button id='' type="button" class="mui-btn mui-btn-blue" onclick="doSetting();" style="margin-top: 20px;">执行操作</button>
    </div>
  </div>
  <script>
            mui.init({
                swipeBack:true //启用右滑关闭功能
            });
            mui('.mui-content .mui-switch').each(function() { //循环所有toggle
                /**
                 * toggle 事件监听
                 */
                this.addEventListener('toggle', function(event) {
                    //event.detail.isActive 可直接获取当前状态
                    var selectVal = event.detail.isActive ? 1 : 0;
                    var inptId = $(this).attr("paramId");
                    $("#"+inptId).val(selectVal);
                });
            });
            
            function doSetting(){
            	$.ajax({
            		url: "/oauth/session/ajaxoperate",
            		type: "post",
            		dataType: "json",
            		data:{
            			"deleteCache": $("#deleteCache").val(),
            			"sessionInvalidate": $("#sessionInvalidate").val(),
            			"clearContext": $("#clearContext").val(),
            			"clearThreadInfo": $("#clearThreadInfo").val(),
            			"deleteCookieAID": $("#deleteCookieAID").val(),
            			"deleteCookieOauthLogin": $("#deleteCookieOauthLogin").val(),
                        "deleteFundCache": $("#deleteCookieOauthLogin").val(),
                        "deleteFundCache":$("#deleteFundCache").val()
            		},
            		success: function(data){
            			if(data != null){
            				mui.toast("设置操作：" + data.result);
            			}else{
            				mui.toast("设置操作可能是出问题了");
            			}
            		},
            		error: function(){
            			mui.toast("设置操作出错了");
            		}
            	});
            		
            }
        </script>
</body>
</html>