
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
var locale='${locale}';
$(document).ready(function() {
  
})
</script>


<div class="home-quickaccess__label">
  <s:text name="dyn.main.quickaccess.shortcuts" />
</div>
<div class="main-list__list">
  <!-- 常用功能 -->
  <ul class="home-quickaccess__list">
    <li class="home-quickaccess__item css-spirit__main"><a href="/psnweb/homepage/show">
        <div class="home-quickaccess-item__main">
          <div class="home-quickaccess-item__logo">
            <div class="css-spirit__large-icon spirit-icon__me-profile"></div>
          </div>
          <div class="home-quickaccess-item__text">
            <s:text name="dyn.main.quickaccess.myProfile" />
          </div>
        </div>
    </a></li>
    <li class="home-quickaccess__item css-spirit__main"><a href="/psnweb/homepage/show?module=pub">
        <div class="home-quickaccess-item__main">
          <div class="home-quickaccess-item__logo">
            <div class="css-spirit__large-icon spirit-icon__me-project"></div>
          </div>
          <div class="home-quickaccess-item__text">
            <s:text name="dyn.main.quickaccess.myPub" />
          </div>
        </div>
    </a></li>
    <li class="home-quickaccess__item css-spirit__main"><a href="/psnweb/homepage/show?module=prj">
        <div class="home-quickaccess-item__main">
          <div class="home-quickaccess-item__logo">
            <div class="css-spirit__large-icon spirit-icon__me-publication"></div>
          </div>
          <div class="home-quickaccess-item__text">
            <s:text name="dyn.main.quickaccess.myPro" />
          </div>
        </div>
    </a></li>
    <li class="home-quickaccess__item css-spirit__main"><a href="/psnweb/friend/main">
        <div class="home-quickaccess-item__main">
          <div class="home-quickaccess-item__logo">
            <div class="css-spirit__large-icon spirit-icon__friend-rcmd"></div>
          </div>
          <div class="home-quickaccess-item__text">
            <s:text name="dyn.main.quickaccess.sugFriends" />
          </div>
        </div>
    </a></li>
    <li class="home-quickaccess__item css-spirit__main"><a href="/psnweb/myfile/filemain?model=myFile">
        <div class="home-quickaccess-item__main">
          <div class="home-quickaccess-item__logo">
            <div class="css-spirit__large-icon spirit-icon__myfile"></div>
          </div>
          <div class="home-quickaccess-item__text">
            <s:text name="dyn.main.quickaccess.myFiles" />
          </div>
        </div>
    </a></li>
    <li class="home-quickaccess__item css-spirit__main"><a href="/groupweb/mygrp/main?model=myGrp">
        <div class="home-quickaccess-item__main">
          <div class="home-quickaccess-item__logo">
            <div class="css-spirit__large-icon spirit-icon__mygroup"></div>
          </div>
          <div class="home-quickaccess-item__text">
            <s:text name="dyn.main.quickaccess.myGroups" />
          </div>
        </div>
    </a></li>
    <%-- <c:if test="${status ==1}"> --%>
     <%-- <c:if test="${locale=='zh_CN'}">
      <li class="home-quickaccess__item css-spirit__main">
         <a href="/application/validate/maint">
            <div class="home-quickaccess-item__main">
                <div class="home-quickaccess-item__logo">
                  <div class="css-spirit__large-icon spirit-icon__researchverification"></div>
                </div>
                <div class="home-quickaccess-item__text"  style="color:#1265cf;">
                   <s:text name="dyn.main.quickaccess.srv" />
                   <span class="home-quickaccess-item__text-tip">NEW</span> 
                </div>
            </div>
          </a>
      </li>
      </c:if>
      <c:if test="${locale=='en_US'}">
          <li class="home-quickaccess__item css-spirit__main"><a href="/application/validate/maint">
            <div class="home-quickaccess-item__main">
              <div class="home-quickaccess-item__logo">
                <div class="css-spirit__large-icon spirit-icon__researchverification-US"></div>
              </div>
              <div class="home-quickaccess-item__text" style="color:#1265cf;">
                <s:text name="dyn.main.quickaccess.srv" />
                <span class="home-quickaccess-item__text-tip">NEW</span>
              </div>
            </div>
            </a>
          </li>
      </c:if>--%>
    <%-- </c:if> --%>
    <div class="main-list home-quickaccess__sublist" id="grp_list"></div>
  </ul>
</div>
