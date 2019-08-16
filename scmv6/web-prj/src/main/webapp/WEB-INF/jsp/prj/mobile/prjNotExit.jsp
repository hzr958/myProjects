<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jstl/xml_rt" prefix="x"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EDGE, chrome=1" />
<meta http-equiv="content-style-type" content="text/css" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link href="/resmod/css/wechat.css" rel="stylesheet" type="text/css">
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod}/js/common/smate.common.js"></script>
</head>
<body style="background-color: white !important;">
  <div  class="new-edit_keword-header" style="overflow-x: initial;">
    <a onclick="SmateCommon.goBack('/dynweb/mobile/dynshow');" class="fl"><i class="material-icons"
        style="font-size: 30px">keyboard_arrow_left</i></a> 
      <li class="new-edit_keword-header_title" id="pub_detail_head_li">详情</li>
    <div class="content" style="padding-top: 15px;">
    </div>
  </div>
  <div class="no_effort" style="margin-top: 100px;">
        <div class="response_no-result" style="margin-top: 0px !important; padding:0px; font-size: 15px; text-align: center;  width: 100%;">
                               对不起，该记录已被删除。
        </div>
  </div>
</body>
</html>