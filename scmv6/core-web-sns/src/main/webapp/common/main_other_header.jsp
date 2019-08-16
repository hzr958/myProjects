<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div id="header" style="${pageHeaderMargin}">
  <div class="Top-nr">
    ${pageTopLeftContent }
    <div class="top-right">
      ${pageTopRightScmLogo }
      <div class="sm_topnav">
        <s:text name="page.main.welcome"></s:text>
        <b id="psnBox" style="position: relative;"> <span> <a class="Blue" id="link-setting"> <%--修改了名称的显示方式，限制只显示20个字符_MJG_ --%>
              <c:set var="curr_username"
                value="${ 'null null' == userRolData.username? userRolData.enUsername: userRolData.username }" /> <c:if
                test="${locale=='en_US'}">
                <c:set var="curr_username"
                  value="${ 'null null' == userRolData.enUsername? userRolData.username: userRolData.enUsername}" />
              </c:if> <c:choose>
                <c:when test="${fn:length(curr_username) > 25}">
					      		${fn:substring(curr_username, 0, 25)}......
					     		</c:when>
                <c:otherwise>
					      		${curr_username}
					     		</c:otherwise>
              </c:choose>
          </a>
        </span> <%--账户设置 --%>
          <div class="quit-nav2" id="quit-nav" style="display: none">
            <ul>
              <li><a href="/psnweb/homepage/show" title="<s:text name='page.main.editPage' />"><i
                  class="py-icon icon24"></i> <s:text name='page.main.editPage' /></a></li>
              <li><a href="/psnweb/psnsetting/main" title="<s:text name='page.main.selfSetting' />"><i
                  class="usersetting_icon"></i> <s:text name='page.main.selfSetting' /></a></li>
              <c:if test="${userRolData.rolMultiRole && userRolData.rolInsId != 0}">
                <li><a target="_self" href="http://${userRolData.rolDomain }/scmwebrol/select-user-role?switch=yes"><i
                    class="change_icon"></i> <s:text name="skin.main.switchrole" /></a></li>
              </c:if>
              <!-- <li><a href="###" onclick="ScmMaint.logoutSys('${userRolData.rolInsId eq '2566' ? 'NFSC' : '' }')" title="<s:text name='skins.main.jsp.logout' />"> <i class="py-icon icon25"></i> <s:text name="skins.main.jsp.logout" /> </a></li> -->
              <li><a href="${userRolData.rolInsId eq '2566' ? snsctx : '' }/loginout"
                title="<s:text name='skins.main.jsp.logout' />"> <i class="py-icon icon25"></i> <s:text
                    name="skins.main.jsp.logout" />
              </a></li>
            </ul>
          </div> <%--账户设置 --%>
        </b>&nbsp;&nbsp;|&nbsp;&nbsp;
        <c:if
          test="${selRol ne 'no' && (userRolData.portalCount !=0 || !empty userRolData.rolLogoUrl || !empty userRolData.rolTitle)}">
          <select name="selRol" id="selRol" style="width: 100px;">
            <option selected="selected" value="default"><s:text name="page.main.scholmate_online" /></option>
            <c:if test="${!empty userRolData.rolLogoUrl || !empty userRolData.rolTitle}">
              <option id="scm" value="${snsDomain}/scmwebsns/main?rolInsId=0">科研之友</option>
            </c:if>
            <c:if test="${userRolData.portalCount ne null && userRolData.portalCount !=0}">
              <s:iterator value="#request.userRolData.portal">
                <%--屏蔽跳转到基金委成果在线的链接_MJG_SCM-5175 --%>
                <c:if test="${domain ne 'rol.nsfc.gov.cn'}">
                  <option title="${initTitle}"
                    value="http://${domain}/${webCtx}/main?rolInsId=${insId}&locale=${locale}&_self=0">${initTitle }</option>
                </c:if>
              </s:iterator>
            </c:if>
          </select>
        </c:if>
        <c:if test="${locale=='zh_CN'}">
          <a href="/help" target="_blank"> <s:text name="home.index.helpCenter" />
          </a> | 
					</c:if>
        <c:if test="${locale=='en_US'}">
          <a href="${resscmsns}/html/helpCenter/index_en_US.html" target="_blank">&nbsp;Learning Center&nbsp;</a>|
					</c:if>
        &nbsp;&nbsp;<a id="page_language" onclick="ScmMaint.changeLanguage(${locale=='en_US'}?'zh_CN':'en_US')"> <c:if
            test="${locale=='zh_CN'}">English</c:if> <c:if test="${locale=='en_US'}">中文版</c:if>
        </a>&nbsp;&nbsp;
      </div>
      <%-- <div class="top-search-btn">
					<form action="${snsDomain}/psnweb/personsearch/show" method="get" id="search_some_one_form" target="_black"
						onsubmit="return ScmMaint.searchSomeSubmit()">
						<input type="text" name="searchName" maxlength="60" id="search_some_one" class="top-search" value="<s:text name='page.main.search' />" title="<s:text name='page.main.search' />" /> 
						<a href="#" onclick="ScmMaint.searchSomeOne();">
						<input name="" type="button" class="btn-top_${locale }" />
						</a>
					</form>
				</div> --%>
    </div>
  </div>
  <div id="mainmenu_body">
    <%--加载的菜单 --%>
    <irismenu:menu type="menu" app="sns" />
    <c:if test='${userRolData.currentContext eq "inspg"}'>
      <div class="pull_downnav">
        <div class="two_menu">
          <ul>
            <li id="two_nav1"><a href="/inspg/inspgmain" id="two_nav">机构主页</a></li>
            <li id="two_nav2"><a href="/inspg/inspgatt">关注的主页</a></li>
            <li id="two_nav3"><a href="/inspg/searchinspgexplore">发现主页</a></li>
          </ul>
        </div>
      </div>
    </c:if>
    <%--加载的导航栏 --%>
    <irismenu:menu type="nav" app="sns" />
    <div class="Work-reminds">
      <a id="msg_tip_box" href="${snsctx}/msgbox/smsMain"> <span class="rem-ico"></span> <s:text
          name="message.tip.have" /> <span class="Orange" id="msg_tip_totalTop"></span>&nbsp; <s:text
          name="message.tip.workMind" />
      </a> <span id="main_msg_total_label" class="red" style="display: none;"></span>
    </div>
    <div id="msgTip_div"></div>
    <div class="clear"></div>
  </div>
  <div class="clear_h20"></div>
</div>