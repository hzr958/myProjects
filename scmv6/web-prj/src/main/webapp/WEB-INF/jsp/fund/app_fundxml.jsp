<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jstl/xml_rt" prefix="x"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<title>科研之友</title>
<link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css">
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<link href="/resmod/css/wechat.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
</head>
<body style="background-color: white;">
  <div class="detail mt61">
    <s:if test="ConstFundCategoryInfo == null ">
      <div class="fund-details_infortitle">
        <div>未找到对应基金详情</div>
      </div>
    </s:if>
    <s:else>
      <input type="hidden" name="logoUrl" id="logoUrl" value="${constFundCategoryInfo.logoUrl}" />
      <input type="hidden" name="defaultLogo" id="defaultLogo"
        value="${domainScm }/ressns/images/default/default_fund_logo.jpg" />
      <div class="fund-details_infor-container">
        <div class="fund-details_Main-title" id="shareFundTitle">${constFundCategoryInfo.fundTitle}</div>
        <div class="fund-details_Sub-title">${constFundCategoryInfo.fundAgencyName}</div>
        <div class="fund-details_infor-item">
          <c:if test="${not empty constFundCategoryInfo.description}">
            <div class="fund-details_infor-item_title">类别描述：</div>
            <div class="fund-details_infor-item_content">${constFundCategoryInfo.description}</div>
          </c:if>
        </div>
        <div class="fund-details_infor-item">
          <c:if test="${not empty constFundCategoryInfo.showYear}">
            <div class="fund-details_infor-item_title">年度：</div>
            <div class="fund-details_infor-item_content">${constFundCategoryInfo.showYear}</div>
          </c:if>
        </div>
        <div class="fund-details_infor-item">
          <c:if test="${not empty constFundCategoryInfo.showDate}">
            <div class="fund-details_infor-item_title">申请日期：</div>
            <div class="fund-details_infor-item_content">${constFundCategoryInfo.showDate}</div>
          </c:if>
        </div>
        <div class="fund-details_infor-item">
          <c:if test="${not empty constFundCategoryInfo.regionName}">
            <div class="fund-details_infor-item_title">适合地区：</div>
            <div class="fund-details_infor-item_content">${constFundCategoryInfo.regionName}</div>
          </c:if>
        </div>
        <div class="fund-details_infor-item">
          <c:if test="${not empty constFundCategoryInfo.scienceAreas}">
            <div class="fund-details_infor-item_title">学科：</div>
            <div class="fund-details_infor-item_content">${constFundCategoryInfo.scienceAreas}</div>
          </c:if>
        </div>
        <div class="fund-details_infor-item">
          <c:if test="${not empty constFundCategoryInfo.strength}">
            <div class="fund-details_infor-item_title">预计资助金额：</div>
            <div class="fund-details_infor-item_content">${constFundCategoryInfo.strength}</div>
          </c:if>
        </div>
        <div class="fund-details_infor-item">
          <c:if test="${not empty constFundCategoryInfo.showIsMatch}">
            <div class="fund-details_infor-item_title">是否配套：</div>
            <div class="fund-details_infor-item_content">${constFundCategoryInfo.showIsMatch}</div>
          </c:if>
        </div>
        <div class="fund-details_infor-item">
          <c:if test="${not empty constFundCategoryInfo.percentage}">
            <div class="fund-details_infor-item_title">比例：</div>
            <div class="fund-details_infor-item_content">${constFundCategoryInfo.percentage}</div>
          </c:if>
        </div>
        <div class="fund-details_infor-item">
          <c:if test="constFundCategoryInfo.fundFileList.size() > 0">
            <div class="fund-details_infor-item_title">附件：</div>
            <s:if test="constFundCategoryInfo.fundFileList.size() > 0">
              <s:iterator value="constFundCategoryInfo.fundFileList" var="file">
                <span onclick="location.href='${filePath}'"><c:out value="${fileName}" /></span>&nbsp;&nbsp;&nbsp;
			                </s:iterator>
            </s:if>
          </c:if>
        </div>
      </div>
      <div class="fund-details_infor-item_title">
        <c:if test="${not empty constFundCategoryInfo.guideUrl}">
          <a href="${constFundCategoryInfo.guideUrl}" target="_Blank"
            style="text-decoration: none; color: #288aed !important;">申报指南</a>
        </c:if>
      </div>
      <div class="fund-details_infor-item_title">
        <c:if test="${not empty constFundCategoryInfo.declareUrl}">
          <a href="${constFundCategoryInfo.declareUrl}" target="_Blank"
            style="text-decoration: none; color: #288aed !important;">立即申报</a>
        </c:if>
      </div>
    </s:else>
  </div>
</body>
</html>
