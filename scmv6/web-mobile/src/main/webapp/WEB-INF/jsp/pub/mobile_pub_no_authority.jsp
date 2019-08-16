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
<link href="${resmod}/mobile/css/common.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css" />
<link href="${resmod}/css/public.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript">
    function login(){
        var service = window.location.href;
        link = "/oauth/index?service="+encodeURIComponent(service);
        window.location.href=link;
    }
    function findOtherPub(){
    	var title = $("#zhtitle").val();
    	/* if(title==""){
    		title = $("#entitle").val();
    	} */
    	link = "/pub/paper/search?searchString="+encodeURIComponent(title);
    	window.location.href=link;
    }
</script>
</head>
<body>
  <input id="des3PubId" name="des3PubId" type="hidden" value="<iris:des3 code='${pubOperateVO.pubId}'/>" />
  <input id="zhtitle" name="zhtitle" type="hidden" value="${pubOperateVO.title}" />
  <%-- <input id="entitle" name="entitle" type="hidden" value="${entitle}"/>
 --%>
  <!-- ===================================================== -->
  <div class="m-top">
    <a onclick="window.history.go(-1);" class='fl'><i class="material-icons navigate_before">keyboard_arrow_left</i></a>
    返回
  </div>
  <!-- ===================================================== -->
  <div style="padding-top: 66px;">
    <div class="tips" style="width: 270px; margin-left: auto; margin-right: auto;">
      <div class="tips_close"></div>
      <div class="tips_info">
        <ul>
          <li class="tips_wrong"><span>该论文由于个人隐私设置, 无法查看. 请</span> <c:if test="${pubOperateVO.psnId > 0 }">
              <a style="cursor: pointer" onclick="findOtherPub()"><span>查看其它论文</span></a>
            </c:if> <c:if test="${pubOperateVO.psnId == null || pubOperateVO.psnId == 0 }">
              <a style="cursor: pointer" onclick="login()"><span class="">立即登录</span></a>查看</c:if></li>
        </ul>
      </div>
    </div>
  </div>
</body>
</html>