<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><s:text name="commend.fund.name.app" /></title>
<link type="text/css" rel="stylesheet" href="${res}/css_v5/app/app.css" />
</head>
<body>
  <div id="content" style="margin-top: 45px">
    <div class="main-box">
      <ul class="use_list">
        <li>
          <%--参考文献 --%>
          <div class="use_img">
            <a href="${ctx }/reference/maint?menuId=4"><img src="${res}/images_v5/icon/t10.gif" width="50"
              height="50" title="<s:text name="app.item.name.reference" />" /></a>
          </div>
          <div class="use_introduction">
            <h3>
              <a href="${ctx }/reference/maint?menuId=4" class="b"><s:text name="app.item.name.reference" /></a>
            </h3>
            <p>
              <s:text name="app.item.label.reference" />
            </p>
          </div>
        </li>
        <li>
          <%--机构主页 --%>
          <div class="use_img">
            <a href="/inspg/inspgmain"><img src="${res}/images_v5/icon2/icon22.gif" width="50" height="50"
              title="<s:text name="机构主页" />" /></a>
          </div>
          <div class="use_introduction">
            <h3>
              <a href="/inspg/inspgmain" class="b"><s:text name="机构主页" /></a>
            </h3>
            <p>
              <s:text name="创建机构主页，发布最新科研动态，推广机构" />
            </p>
          </div>
        </li>
        <%-- <li>
					文献推荐
					<div class="use_img">
						<a href="${ctx }/reference/refrecommend?reNewAction=need&menuId=4"><img src="${res}/images_v5/icon/p09.gif" width="50"
							height="50" title="<s:text name="app.item.name.refrecommend" />" /></a>
					</div>
					<div class="use_introduction">
						<h3>
							<a href="${ctx }/reference/refrecommend?reNewAction=need&menuId=4" class="b"><s:text name="app.item.name.refrecommend" /></a>
						</h3>
						<p><s:text name="app.item.label.refrecommend" /></p>
					</div>
				</li> --%>
        <li>
          <%--文件 --%>
          <div class="use_img">
            <a href="/psnweb/myfile/filemain?model=myFile"><img src="${res}/images_v5/icon2/icon28.gif" width="50"
              height="50" title="<s:text name="app.item.name.file" />" /></a>
          </div>
          <div class="use_introduction">
            <h3>
              <a href="/psnweb/myfile/filemain?model=myFile" class="b"><s:text name="app.item.name.file" /></a>
            </h3>
            <p>
              <s:text name="app.item.label.file" />
            </p>
          </div>
        </li>
        <!-- 	<li> -->
        <%--找专家 --%>
        <%-- 			<div class="use_img">
						<a href="${ctx }/keywordscmd/index?menuId=4"><img src="${res}/images_v5/icon2/icon31.gif"
							width="50" height="50" title="<s:text name="app.item.name.expertise" />" /></a>
					</div>
					<div class="use_introduction">
						<h3>
							<a href="${ctx }/keywordscmd/index?menuId=4" class="b"><s:text name="app.item.name.expertise" /></a>
						</h3>
						<p><s:text name="app.item.label.expertise" /></p>
					</div>
				</li> --%>
        <li>
          <%--科研基金 --%>
          <div class="use_img">
            <a href="/prjweb/fund/main?menuId=4"><img src="${res}/images_v5/icon/t11.gif" width="50" height="50"
              title="<s:text name="app.item.name.fund" />" /></a>
          </div>
          <div class="use_introduction">
            <h3>
              <a href="/prjweb/fund/main?menuId=4" class="b"><s:text name="app.item.name.fund" /></a>
            </h3>
            <p>
              <s:text name="app.item.label.fund" />
            </p>
          </div>
        </li>
        <li>
          <%--项目申请建议 --%>
          <div class="use_img">
            <a href="${ctx }/fundapp/enterApp?menuId=4"><img src="${res}/images_v5/icon2/icon06.gif" width="50"
              height="50" title="<s:text name="app.item.name.fundRecommend" />" /></a>
          </div>
          <div class="use_introduction">
            <h3>
              <a href="${ctx }/fundapp/enterApp?menuId=4" class="b"><s:text name="app.item.name.fundRecommend" /></a>
            </h3>
            <p>
              <s:text name="app.item.label.fundRecommend" />
            </p>
          </div>
        </li>
        <li>
          <%--学术期刊 --%>
          <div class="use_img">
            <a href="${ctx }/commend/jnl?menuId=4"><img src="${res}/images_v5/icon/p03.gif" width="50" height="50"
              title="<s:text name="app.item.name.journal" />" /></a>
          </div>
          <div class="use_introduction">
            <h3>
              <a href="${ctx }/commend/jnl?menuId=4" class="b"><s:text name="app.item.name.journal" /></a>
            </h3>
            <p>
              <s:text name="app.item.label.journal" />
            </p>
          </div>
        </li>
        <li>
          <%--论文投稿建议 --%>
          <div class="use_img">
            <a href="${ctx }/paper/journal/main?menuId=4"><img src="${res}/images_v5/icon2/icon17.gif" width="50"
              height="50" title="<s:text name="app.item.name.paperRecommend" />" /></a>
          </div>
          <div class="use_introduction">
            <h3>
              <a href="${ctx }/paper/journal/main?menuId=4" class="b"><s:text name="app.item.name.paperRecommend" /></a>
            </h3>
            <p>
              <s:text name="app.item.label.paperRecommend" />
            </p>
          </div>
        </li>
      </ul>
    </div>
  </div>
</body>
</html>