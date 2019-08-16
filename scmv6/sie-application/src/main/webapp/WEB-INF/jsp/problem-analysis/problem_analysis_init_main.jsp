<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="${ressie}/css/reset.css" />
<link rel="stylesheet" href="${ressie}/css/common.css" />
<link rel="stylesheet" href="${ressie}/css/administrator.css" />
<link rel="stylesheet" href="${ressie}/css/plugin/toast.css" />
<link rel="stylesheet" href="${ressie}/css/researcher.effect.css" />
<script type="text/javascript" src="${ressie}/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${ressie}/js/jquery.js"></script>
<script type="text/javascript" src="${ressie}/js/ion.checkRadio.min.js"></script>
<script type="text/javascript" src="${ressie}/js/action.js"></script>
<script type="text/javascript" src="${ressie}/js/plugin/smate.toast.js"></script>
<script type="text/javascript" src="${ressie}/js/plugin/echarts.js"></script>
<script type="text/javascript" src="${resapp}/problem-analysis/proplem.analysis.extract_zh_CN.js"></script>
<script type="text/javascript" src="${resapp}/problem-analysis/proplem.analysis.extract.js"></script>
<script type="text/javascript" src="${resapp}/problem-analysis/researcher_effect.js"></script>
<title>开题分析</title>
<script type="text/javascript">
$(document).ready(function(){
    //登录框登录不刷新
    $("#login_box_refresh_currentPage").val("false");
});
</script>
</head>
<body>
  <form action="/application/problem/tomaint" method="POST" id="regForm">
    <input type="hidden" name="keywordsList" value="${keyWordsList }" id="keywordsList">
    <div class="pop_bg" style="display: none;" id="imp_pop_bg"></div>
    <div class="sie_upload version-tip" style="display: none;" id="start_analysis">
      <img class="sie_upload1" src="${ressie }/images/upload.gif" alt="">
    </div>
    <div class="message">
      <div class="message_conter">
        <div class="sie_kingmaster_conter ">
          <div class="seek sie_management_seek">
            <p class="headline_1">
              科研查新<span class="ml8">通过大数据技术，了解未来趋势，有针对性投入</span>
            </p>
            <div class="clear"></div>
          </div>
          <div class="bfa">
            <div class="inspection_data pt14">
              <div class="SIE_psninfor-item">
                <div class="SIE_psninfor-item_left" style="width: 70px; margin-right: 14px; text-align: right;">
                  <span style="padding: 0px; margin: 0px;">标题：</span>
                </div>
                <div class="SIE_psninfor-item_right" style="margin-left: 9px;">
                  <div class="SIE_psninfor-item_right-content sie_service_top">
                    <input type="text" name="title" id="title" class="SIE_psninfor-item_right-content_input" value="">
                  </div>
                </div>
              </div>
              <div class="SIE_psninfor-item">
                <div class="SIE_psninfor-item_left" style="width: 70px; margin-right: 14px; text-align: right;">
                  <span style="padding: 0px; margin: 0px;">摘要：</span>
                </div>
                <div class="SIE_psninfor-item_right" style="margin-left: 9px;">
                  <div class="SIE_psninfor-item_right-content">
                    <textarea name="summary" id="summary" cols="30" rows="10" class="textarea_original w715"></textarea>
                  </div>
                </div>
              </div>
            </div>
            <div class="SIE_psninfor-item">
              <div class="SIE_psninfor-item_right">
                <span style="color: #fff !important; margin-top: 14px; margin-bottom: 32px;"
                  class="handin_import-content_container-save" onclick="Analysis.extractKeywords();"> 开始分析 </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </form>
</body>
</html>
