<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="/job"></c:set>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="UTF-8"/>
    <title>科研之友任务管理系统</title>

    <meta name="description" content="Dynamic tables and grids using jqGrid plugin"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>

    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="${ctx}/res/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${ctx}/res/assets/font-awesome/4.5.0/css/font-awesome.min.css"/>

    <!-- page specific plugin styles -->
    <link rel="stylesheet" href="${ctx}/res/assets/css/chosen.min.css"/>
    <link rel="stylesheet" href="${ctx}/res/assets/css/ui.jqgrid.min.css"/>
    <!--<link rel="stylesheet" href="${ctx}/res/jqGrid-5.3.0/css/ui.jqgrid.css" />-->
    <link rel="stylesheet" href="${ctx}/res/assets/css/jquery-ui.min.css"/>
    <!-- ace styles -->
    <link rel="stylesheet" href="${ctx}/res/assets/css/ace.min.css" class="ace-main-stylesheet"
          id="main-ace-style"/>

    <link rel="stylesheet" href="${ctx}/res/assets/css/ace-skins.min.css"/>
    <style>
        .ui-jqdialog-content input.FormElement {
            padding: 5px 4px 6px;
        }

        .ui-jqdialog-content .CaptionTD{
            width: 20%;
        }

        .chosen-container-single .chosen-single {
            line-height: 34px;
            height: 34px;
        }

        .chosen-container {
            font-size: 14px;
            margin-bottom: 3px;
        }

        .btn-group-sm > .btn, .btn-sm, .btn-mini, .btn-lg, .btn-xs {
            border-width: 1px;
        }

        .ui-spinner-input {
            max-width: 120px;
        }

        .progress {
            margin-bottom: 0;
        }

        .ui-jqgrid .ui-jqgrid-labels th {
            text-align: center !important;
        }

        .ui-jqgrid .ui-jqgrid-labels th:first-child {
            border-left: 1px solid #E1E1E1 !important;
        }

        .ui-jqgrid tr.jqgrow td {
            white-space: normal;
        }

        .ui-jqdialog-content .form-view-data {
            white-space: normal;
        }

        .FormGrid .EditTable tr:first-child {
            display: table-row;
        }
    </style>
</head>

<body class="no-skin">
<div id="navbar" class="navbar navbar-default          ace-save-state">
    <div class="navbar-container ace-save-state" id="navbar-container">
        <div class="navbar-header pull-left">
            <a href="index.html" class="navbar-brand">
                <small>
                    <i class="fa fa-leaf"></i>&nbsp; &nbsp;科研之友任务管理系统
                </small>
            </a>
        </div>

        <div class="navbar-buttons navbar-header pull-right" role="navigation">
            <ul class="nav ace-nav">
                <li class="light-blue dropdown-modal">
                    <a data-toggle="dropdown" href="#" class="dropdown-toggle">
                        <img class="nav-user-photo" src="${ctx}/res/assets/images/avatars/user.jpg"
                             alt="Jason's Photo"/>
                        <span class="user-info">
                                    <small>欢迎，</small>管理员
                                </span>

                        <i class="ace-icon fa fa-caret-down"></i>
                    </a>

                    <ul class="user-menu dropdown-menu-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
                        <li>
                            <a href="/oauth/logout">
                                <i class="ace-icon fa fa-power-off"></i>退出
                            </a>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
    </div><!-- /.navbar-container -->
</div>

<div class="main-container ace-save-state" id="main-container">
    <script type="text/javascript">
      try {
        ace.settings.loadState('main-container')
      } catch (e) {
      }
    </script>

    <div id="sidebar" class="sidebar responsive ace-save-state">
        <script type="text/javascript">
          try {
            ace.settings.loadState('sidebar')
          } catch (e) {
          }
        </script>

        <ul class="nav nav-list">
            <li class="active">
                <a href="/job/admin/index">
                    <i class="menu-icon fa fa-tachometer"></i>
                    <span class="menu-text"> 仪表板 </span>
                </a>

                <b class="arrow"></b>
            </li>
            <li class="">
                <a href="#" class="dropdown-toggle">
                    <i class="menu-icon fa fa-list"></i>
                    <span class="menu-text"> 任务列表 </span>

                    <b class="arrow fa fa-angle-down"></b>
                </a>

                <b class="arrow"></b>

                <ul class="submenu">
                    <li class="">
                        <a href="javascript:loadOfflineIndex();">
                            <i class="menu-icon fa fa-caret-right"></i>
                            离线任务
                        </a>

                        <b class="arrow"></b>
                    </li>

                    <li class="">
                        <a href="javascript:loadOnlineIndex();">
                            <i class="menu-icon fa fa-caret-right"></i>
                            在线任务
                        </a>

                        <b class="arrow"></b>
                    </li>
                </ul>
            </li>
        </ul><!-- /.nav-list -->

        <div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
            <i id="sidebar-toggle-icon" class="ace-icon fa fa-angle-double-left ace-save-state"
               data-icon1="ace-icon fa fa-angle-double-left"
               data-icon2="ace-icon fa fa-angle-double-right"></i>
        </div>
    </div>

    <div class="main-content">
        <div class="main-content-inner">
            <div id="page-content" class="page-content">
                <div class="page-header">
                    <h1> 仪表板
                        <small>
                            <i class="ace-icon fa fa-angle-double-right"></i> 概述 & 状态
                        </small>
                    </h1>
                </div>
                <div class="alert alert-info">
                    <button class="close" data-dismiss="alert">
                        <i class="ace-icon fa fa-times"></i>
                    </button>

                    <i class="ace-icon fa fa-hand-o-right"></i>
                    欢迎使用科研之友任务调度中心服务！
                </div>
            </div><!-- /.page-content -->
        </div>
    </div><!-- /.main-content -->

    <a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
        <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
    </a>
</div><!-- /.main-container -->

<!-- basic scripts -->

<!--[if !IE]> -->
<script src="${ctx}/res/assets/js/jquery-2.1.4.min.js"></script>

<!-- <![endif]-->

<script src="${ctx}/res/assets/js/bootstrap.min.js"></script>

<!-- ace scripts -->
<script src="${ctx}/res/assets/js/ace-elements.min.js"></script>
<script src="${ctx}/res/assets/js/ace.min.js"></script>

<%--layer v3.1.1--%>
<script src="${ctx}/res/plugin/layer/layer.js"></script>

<%--jqGrid--%>
<script src="${ctx}/res/assets/js/jquery.jqGrid.min.js"></script>
<script src="${ctx}/res/assets/js/grid.locale-cn.js"></script>
<script src="${ctx}/res/assets/js/jquery-ui.min.js"></script>

<script src="${ctx}/res/js/index.js"></script>
<script src="${ctx}/res/js/custom.jqGrid.js"></script>
<script src="${ctx}/res/assets/js/chosen.jquery.min.js"></script>

<!-- inline scripts related to this page -->
<script type="text/javascript">
  function loadOfflineIndex() {
    var index = layer.load(2);
    $("#page-content").load("/job/offline/index", function () {
      layer.close(index);
    });
    var $this = $(this);
    $("#sidebar .nav").find("li").each(function () {
      if ($(this) == $this) {
        $this.addClass("active");
      } else {
        $(this).removeClass("active");
      }
    });
  }

  function loadOnlineIndex() {
    var index = layer.load(2);
    $("#page-content").load("/job/online/index", function () {
      layer.close(index);
    });
    var $this = $(this);
    $("#sidebar .nav").find("li").each(function () {
      if ($(this) == $this) {
        $this.addClass("active");
      } else {
        $(this).removeClass("active");
      }
    });
  }
</script>
</body>
</html>

