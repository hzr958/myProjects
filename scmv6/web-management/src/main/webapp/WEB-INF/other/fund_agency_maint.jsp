<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctx" value="/scmmanagement" />
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript">
  var resmodctx = "${resmod}";
  var ctxpath = "${ctx}";
  var resmodpath = "${resmod}";
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>基金管理</title>
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/jquery.scmtips.css" />
<script type="text/javascript" src="${resmod}/js/plugin/jquery.scmtips.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.watermark.js"></script>
<link rel="stylesheet" type="text/css" href="${resmod}/css_v5/plugin/jquery.thickbox.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css_v5/plugin/jquery.complete.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css_v5/pop.css" />
<link href="${resmod}/css_v5/common.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/all.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/public.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/page.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/menu/navMenu.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/footer.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.proceedingwin.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.thickbox.min.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.scmtips.js"></script>
<script type='text/javascript' src='${resmod}/js_v5/common.order.js'></script>
<script type="text/javascript" src="${resmod}/js_v5/rol_common.js"></script>
<script type='text/javascript' src='${resmod}/js_v5/plugin/jquery.alerts_${locale}.js'></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.alerts.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/table.float.rol.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.watermark.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/bpo_fund/agency.maint.js"></script>
<script type="text/javascript">
  var isCategoryByAddAgency = false;
  var search_tipcon = '请输入：机构名称';
  var lable_check_input = "请输入检索条件";
  var check_input_date = "开始日期必须在截止日期之前";
  $(document).ready(function() {
    $("a.thickbox,input.thickbox").thickbox({
      ctxpath : "${ctx}",
      respath : "${resmod}"
    });
    agencyMaint.search(0);
  });
  function loadOtherPage(pageNo) {
    if (pageNo)
      $("#pageNo").val(pageNo);
    var searchKey = $.trim($("#searchKey").val());
    searchKey = search_tipcon == searchKey ? "" : searchKey;
    if (searchKey != "") {
      agencyMaint.search(2);
    } else {
      $(".sidebar-nav ul li a").each(function() {
        if ($(this).hasClass("leftnav-hover")) {
          agencyMaint.searchByLeft(this);
          return;
        }
      });
    }
  }

  function topage(pageNo) {
    var toPage = $.trim($("#toPage").val());
    if (!/^\d+$/g.test(toPage))
      toPage = 1;
    $("#pageNo").attr("value", toPage);
    var searchKey = $.trim($("#searchKey").val());
    searchKey = search_tipcon == searchKey ? "" : searchKey;
    if (searchKey != "") {
      agencyMaint.search(2);
    } else {
      $(".sidebar-nav ul li a").each(function() {
        if ($(this).hasClass("leftnav-hover")) {
          agencyMaint.searchByLeft(this);
          return;
        }
      });
    }
  }
  function setTabUrl(url) {
    location.href = url;
  }
</script>
</head>
<body>
  <div class="box_nav3tab">
    <div class="nav4_div_hover">
      <ul>
        <li><a href="${ctx}/fund/agency/main"> <b class="left"></b><b class="middle">资助机构</b><b class="right"></b></a></li>
      </ul>
    </div>
    <div class="nav4_div_on">
      <ul>
        <li><a href="${ctx}/fund/category/main"><b class="left"></b><b class="middle">资助类别</b><b class="right"></b></a></li>
      </ul>
    </div>
    <div class="nav4_div_on">
      <ul>
        <li><a href="${ctx}/fund/third/main"><b class="left"></b><b
            class="middle">待审核基金机会</b><b class="right"></b></a></li>
      </ul>
    </div>
  </div>
  <input type="hidden" class="thickbox" id="thickbox_agency" />
  <div id="rol_main">
    <div id="Tab">
      <div class="Menubox">
        <ul>
          <li id="one1" onClick="setTabUrl('${ctx}/fund/agency/main')" class="hover">资助机构列表</li>
        </ul>
      </div>
      <div class="Contentbox">
        <div id="con_one_1" class="hover">
          <div id="left-wrap" class="left-wrap">
            <img src="${resmod}/images_v5/loading_img.gif" style="vertical-align: middle;" />
          </div>
          <div class="right-wrap">
            <div class="mdown10">
              <span class="Fleft"><span class="red">*</span>&nbsp;显示所有资助机构数据</span> <span class="Fright"> <a
                onclick="agencyMaint.add();" class="uiButton"><i class="add_text"></i>新增</a> <a
                onclick="agencyMaint.edit();" class="uiButton">编辑</a> <a onclick="agencyMaint.remove();"
                class="uiButton">删除</a>
              </span>
              <div class="clear"></div>
            </div>
            <div id="loading_tip">
              <img src="${resmod}/images_v5/loading_img.gif" style="vertical-align: middle;" />&nbsp;
              <s:text name="数据加载中..." />
            </div>
            <div id="searchResult"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</body>
</html>