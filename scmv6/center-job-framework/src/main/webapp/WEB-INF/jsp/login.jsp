<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="/job"></c:set>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>登录 - 科研之友任务管理系统</title>

    <meta name="description" content="User login page"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>

    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="${ctx}/res/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${ctx}/res/assets/font-awesome/4.5.0/css/font-awesome.min.css"/>

    <!-- text fonts -->
    <link rel="stylesheet" href="${ctx}/res/assets/css/fonts.googleapis.com.css"/>

    <!-- ace styles -->
    <link rel="stylesheet" href="${ctx}/res/assets/css/ace.min.css"/>

</head>

<body class="login-layout blur-login">
<div class="main-container">
    <div class="main-content">
        <div class="row">
            <div class="col-sm-10 col-sm-offset-1">
                <div class="login-container">
                    <div class="center">
                        <h1>
                            <i class="ace-icon fa fa-leaf green"></i>
                            <span class="red">Center Job</span>
                            <span class="white" id="id-text2">任务管理</span>
                        </h1>
                        <h4 class="blue" id="id-company-text">&copy; 科研之友</h4>
                    </div>

                    <div class="space-6"></div>

                    <div class="position-relative">
                        <div id="login-box" class="login-box visible widget-box no-border">
                            <div class="widget-body">
                                <div class="widget-main">
                                    <h4 class="header blue lighter bigger">
                                        <i class="ace-icon fa fa-coffee green"></i>
                                        请输入您的登录凭证
                                    </h4>

                                    <div class="space-6"></div>

                                    <form>
                                        <fieldset>
                                            <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="text" class="form-control"
                                                                   placeholder="邮箱"/>
															<i class="ace-icon fa fa-user"></i>
														</span>
                                            </label>

                                            <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="password" class="form-control"
                                                                   placeholder="密码"/>
															<i class="ace-icon fa fa-lock"></i>
														</span>
                                            </label>

                                            <div class="space"></div>

                                            <div class="clearfix">
                                                <label class="inline">
                                                    <input type="checkbox" class="ace"/>
                                                    <span class="lbl"> 记住我</span>
                                                </label>

                                                <button type="button"
                                                        class="width-35 pull-right btn btn-sm btn-primary">
                                                    <i class="ace-icon fa fa-key"></i>
                                                    <span class="bigger-110">登录</span>
                                                </button>
                                            </div>

                                            <div class="space-4"></div>
                                        </fieldset>
                                    </form>

                                    <div class="social-or-login center">
                                        <span class="bigger-110">第三方登录</span>
                                    </div>

                                    <div class="space-6"></div>

                                    <div class="social-login center">
                                        <a class="btn btn-primary">
                                            <i class="ace-icon fa fa-wechat"></i>
                                        </a>

                                        <a class="btn btn-info">
                                            <i class="ace-icon fa fa-weibo"></i>
                                        </a>

                                        <a class="btn btn-danger">
                                            <i class="ace-icon fa fa-qq"></i>
                                        </a>
                                    </div>
                                </div><!-- /.widget-main -->

                                <div class="toolbar clearfix">
                                    <div>
                                        <a href="#" data-target="#forgot-box" class="forgot-password-link">
                                            <i class="ace-icon fa fa-arrow-left"></i>
                                            忘记密码
                                        </a>
                                    </div>
                                </div>
                            </div><!-- /.widget-body -->
                        </div><!-- /.login-box -->

                        <div id="forgot-box" class="forgot-box widget-box no-border">
                            <div class="widget-body">
                                <div class="widget-main">
                                    <h4 class="header red lighter bigger">
                                        <i class="ace-icon fa fa-key"></i>
                                        找回密码
                                    </h4>

                                    <div class="space-6"></div>
                                    <p>
                                        请输入您的邮箱地址
                                    </p>

                                    <form>
                                        <fieldset>
                                            <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="email" class="form-control"
                                                                   placeholder="Email"/>
															<i class="ace-icon fa fa-envelope"></i>
														</span>
                                            </label>

                                            <div class="clearfix">
                                                <button type="button" class="width-35 pull-right btn btn-sm btn-danger">
                                                    <i class="ace-icon fa fa-lightbulb-o"></i>
                                                    <span class="bigger-110">发给我!</span>
                                                </button>
                                            </div>
                                        </fieldset>
                                    </form>
                                </div><!-- /.widget-main -->

                                <div class="toolbar center">
                                    <a href="#" data-target="#login-box" class="back-to-login-link">
                                        去登录
                                        <i class="ace-icon fa fa-arrow-right"></i>
                                    </a>
                                </div>
                            </div><!-- /.widget-body -->
                        </div><!-- /.forgot-box -->

                    </div><!-- /.position-relative -->

                    <div class="navbar-fixed-top align-right">
                        <br/>
                        &nbsp;
                        <a id="btn-login-dark" href="#">Dark</a>
                        &nbsp;
                        <span class="blue">/</span>
                        &nbsp;
                        <a id="btn-login-blur" href="#">Blur</a>
                        &nbsp;
                        <span class="blue">/</span>
                        &nbsp;
                        <a id="btn-login-light" href="#">Light</a>
                        &nbsp; &nbsp; &nbsp;
                    </div>
                </div>
            </div><!-- /.col -->
        </div><!-- /.row -->
    </div><!-- /.main-content -->
</div><!-- /.main-container -->

<!-- basic scripts -->

<!--[if !IE]> -->
<script src="${ctx}/res/assets/js/jquery-2.1.4.min.js"></script>

<!-- <![endif]-->

<!--[if IE]>
<script src="${ctx}/res/assets/js/jquery-1.11.3.min.js"></script>
<![endif]-->
<script type="text/javascript">
  if ('ontouchstart'
      in document.documentElement) document.write("<script src='${ctx}/res/assets/js/jquery.mobile.custom.min.js'>"
      + "<" + "/script>");
</script>

<!-- inline scripts related to this page -->
<script type="text/javascript">
  jQuery(function ($) {
    $(document).on('click', '.toolbar a[data-target]', function (e) {
      e.preventDefault();
      var target = $(this).data('target');
      $('.widget-box.visible').removeClass('visible');//hide others
      $(target).addClass('visible');//show target
    });
  });

  //you don't need this, just used for changing background
  jQuery(function ($) {
    $('#btn-login-dark').on('click', function (e) {
      $('body').attr('class', 'login-layout');
      $('#id-text2').attr('class', 'white');
      $('#id-company-text').attr('class', 'blue');

      e.preventDefault();
    });
    $('#btn-login-light').on('click', function (e) {
      $('body').attr('class', 'login-layout light-login');
      $('#id-text2').attr('class', 'grey');
      $('#id-company-text').attr('class', 'blue');

      e.preventDefault();
    });
    $('#btn-login-blur').on('click', function (e) {
      $('body').attr('class', 'login-layout blur-login');
      $('#id-text2').attr('class', 'white');
      $('#id-company-text').attr('class', 'light-blue');

      e.preventDefault();
    });

  });
</script>
</body>
</html>

