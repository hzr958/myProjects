<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Title</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>注册成功</title>
<link rel="stylesheet" href="${ressie }/css/reset.css" />
<link rel="stylesheet" href="${ressie }/css/administrator.css" />
<script src="${ressie }/js/jquery-1.9.1.min.js"></script>
</head>
<body>
  <div class="message">
    <div class="message_conter">
      <div class="pass_conter_left fl">
        <ul class="pass_conter_triumph">
          <li><img src="${ressie }/images/pass.png" alt=""></li>
          <li>
            <h1>成功提交加入${userRolData.rolTitleCh}科研之友机构版申请</h1>
            <p>
              请耐心等待单位管理审核，点击 <a href="/psnweb/index">这里</a> 返回${userRolData.rolTitleCh}科研之友机构版首页
            </p>
            <p>
              点击 <a href="${domainscm }/scmwebsns/?locale=${locale}">这里</a> 返回科研之友首页
            </p>
          </li>
        </ul>
      </div>
      <div class="message_conter_right fl">
        <h1 class="seek ft16 pt15">检索与保存项目论文</h1>
        <div class="seek_1 pd15">
          <p class="f666 ftbold">面向校、院系科研管理人员：</p>
          <p>垮库自动收集工具</p>
          <p>管理机构项目成果</p>
        </div>
        <h1 class="seek ft16 pt15">分析与管理科研信息</h1>
        <div class="seek_1 pd15">
          <p class="f666 ftbold">面向校、院系领导：</p>
          <p>垮库自动收集工具</p>
          <p>年度科研报告</p>
        </div>
        <h1 class="seek ft16 pt15">分析与管理科研信息</h1>
        <div class="seek_1 pd15">
          <p class="f666 ftbold">面向企业、公众用户：</p>
          <p>检索科研项目</p>
          <p>检索科研成果</p>
        </div>
      </div>
    </div>
    <div class="clear"></div>
  </div>
</body>
</html>
