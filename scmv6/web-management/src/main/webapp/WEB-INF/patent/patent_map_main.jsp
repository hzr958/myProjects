<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
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
<script src="${resmod}/js/d3.v4.min.js"></script>
<script src="${resmod}/js/d3-contour.v1.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		patentMaintLatedLeftMenu();
		patentMaintSearch("#01",0);

	});

	menuSelected = function(obj) {
		$(obj).closest(".ax_default-text").find(".ax_default-section ").find(
				"span").removeClass("ax_selected");
		$(obj).find("span").addClass("ax_selected");
	}
	patentMaintSearch = function(obj,mId) {
		$.ajax({
			url : "/scmmanagement/patent/ajaxmapinfo",
			type : "post",
			data : {
				"mId" : mId
			},
			dataType : "html",
			timeout : 10000,
			success : function(data) {
				$("#main4").html(data);
				menuSelected(obj);

			}
		});

	}

	patentMaintLatedLeftMenu = function() {
		$.ajax({
			url : "/scmmanagement/patent/ajaxcategoryLeft",
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
	};
</script>
</head>
<body>
  <div id="content" style="display: flex; justify-content: center; align-items: center; margin_right: 32px;">
    <div id="base" class=""></div>
    <div id="u297" class="ax_default line">
      <img id="u297_img" class="img " src="${resmod}/images/u95.png">
        <div id="u298" class="text" style="display: none; visibility: hidden">
          <p>
            <span></span>
          </p>
        </div>
    </div>
    <div id="main4" style="width: 800px; height: 600px; margin-top: 110px;"></div>
  </div>
</body>
</html>