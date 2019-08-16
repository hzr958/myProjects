<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script>
   // 添加二级菜单
   $(function(){
	   var twoMune = ' <div class="nav_erji"  id="nav_erji_4" style="display:none; z-index:1999;">'
		  +'  <dl>'
		  +'       <dd><a href="/inspg/inspgmain" >机构主页</a></dd>'
		  +'      <dd><a href="/inspg/inspgatt" >关注的主页</a></dd>'
		  +'      <dd><a href="/inspg/searchinspgexplore">发现主页</a></dd>'
		  +'  </dl>'
		  +' </div>'  ;
	   $(".nav_wrap-t > ul > .cur").append( twoMune );
   });
</script>
<div id="top-t" class="top-t">
  <div class="header-t">
    <div class="header_wrap-t">
      <!--   <a href="/scmwebsns" class="logo fl"></a> -->
      <input id="rolDomain" type="hidden" value="${userRolData.rolDomain}" />
      <c:choose>
        <c:when test="${!empty userRolData.rolLogoUrl }">
          <div onclick=" window.location.href='/scmwebsns' " class="logo fl "
            style="background: #ffffff; position: relative;">
            <div
              style="width:300px; height:50px; position:absolute; top:0; left:0; background: url(${ userRolData.rolLogoUrl}) no-repeat; transform: scale(0.6, 0.6); transform-origin: top;"></div>
          </div>
        </c:when>
        <c:when test="${!empty userRolData.rolTitle}">
          <div onclick=" window.location.href='/scmwebsns' " class="logo fl "
            style="background: #ffffff; position: relative;">
            <div
              style="width:300px; height:50px;  text-overflow: ellipsis;   white-space: nowrap;
                                     overflow: hidden;   font-weight: bold; font-size:26px; color:#395d94; line-height:50px; font-family:'微软雅黑', '黑体', '楷体';      position:absolute; top:0; left:0; background: url(${ userRolData.rolLogoUrl}) no-repeat; transform: scale(0.6, 0.6); transform-origin: top;">
              ${ userRolData.rolTitle}</div>
          </div>
        </c:when>
        <c:otherwise>
          <div onclick=" window.location.href='/scmwebsns' " class="logo fl"></div>
        </c:otherwise>
      </c:choose>
      <%--切换站点 --%>
      <c:if
        test="${selRol ne 'no' && (userRolData.portalCount !=0 || !empty userRolData.rolLogoUrl || !empty userRolData.rolTitle)}">
        <div class="switching_station" style="margin-top: 5px; margin-left: 0px; width: 88px; */">
          &nbsp;[ <a id="switch_site_label" href="#"><s:text name="page.main.scholmate_online" /><i
            class="top_useimg"></i> </a>]
          <div id="switching_box" class="switching_box" style="opacity: 1; display: none">
            <ul>
              <%-- <li><s:text name="page.main.scholmate_online"/></li> --%>
              <c:if test="${!empty userRolData.rolLogoUrl || !empty userRolData.rolTitle}">
                <li><a id="scm" href="${snsDomain}/scmwebsns/main?rolInsId=0">科研之友</a></li>
              </c:if>
              <c:if test="${userRolData.portalCount ne null && userRolData.portalCount !=0}">
                <s:iterator value="#request.userRolData.portal">
                  <%--屏蔽跳转到基金委成果在线的链接_MJG_SCM-5175 --%>
                  <c:if test="${domain ne 'rol.nsfc.gov.cn'}">
                    <li><a href="http://${domain}/${webCtx}/main?rolInsId=${insId}&locale=${locale}&_self=0">${initTitle }</a></li>
                  </c:if>
                </s:iterator>
              </c:if>
            </ul>
          </div>
        </div>
      </c:if>
      <div class="search fl">
        <div id="divselect1" class="fl">
          <a href="#" id="show_select" class="tp_box"><i id="search_type_img" class="lt_icon"></i></a>
          <!--  <span class="tp_box" id="show_select"><i id="search_type_img" class="lt_icon"></i></span> -->
          <ul id="home_search_items" style="display: none">
            <li onclick="ScmMaint.select_search_paper()"><a href="javascript:void(0);" selectid="1"><i
                class="lw_icon"></i>论文</a></li>
            <li onclick="ScmMaint.select_search_patent()"><a href="javascript:void(0)" selectid="2"><i
                class="zl_icon"></i>专利</a></li>
            <li onclick="ScmMaint.select_search_psn()"><a href="javascript:void(0)" selectid="3"><i
                class="ry_icon"></i>人员</a></li>
          </ul>
        </div>
        <form id="search_some_one_form" action="/pub/search/pdwhmain" method="get">
          <input id="search_some_one" name="searchString" value="" title="输入关键词检索论文、专利、专家、机构... " type="text"
            style="font-family: arial, Tahoma, Verdana, simsun">
        </form>
        <a href="javascript:void(0)" class="s_btn" onclick="ScmMaint.searchSomeOne()"></a>
      </div>
      <!--<div class="login fr">
		        <a href="#" class="ky_top"></a>
		        <p><a href="#">请登录</a>
		          <a href="#">免费注册</a></p>
		      </div>-->
      <div class="logining">
        <div class="mana_r fl" id="psnBox">
          <a href="javascript:;" class="mana_t"> <img src="${userRolData.firstName }?random=<%=Math.random()%>"
            width="30" height="30" onerror="this.onerror=null;this.src='/resmod/smate-pc/img/logo_psndefault.png'"></a>
          <div class="quit-nav2" id="quit-nav" style="display: none; top: 28px;">
            <ul>
              <li><a href="/psnweb/homepage/show" title="<s:text name='page.main.editPage' />"><i
                  class="py-icon icon24"></i> <s:text name='page.main.editPage' /></a></li>
              <li><a href="/psnweb/psnsetting/main" title="<s:text name='page.main.selfSetting' />"><i
                  class="usersetting_icon"></i> <s:text name='page.main.selfSetting' /></a></li>
              <c:if test="${userRolData.rolMultiRole && userRolData.rolInsId != 0}">
                <li><a target="_self" href="http://${userRolData.rolDomain }/scmwebrol/select-user-role?switch=yes"><i
                    class="change_icon"></i> <s:text name="skin.main.switchrole" /></a></li>
              </c:if>
              <li><a href="#" onclick="ScmMaint.logoutSys()" title="<s:text name='skins.main.jsp.logout' />"> <i
                  class="py-icon icon25"></i> <s:text name="skins.main.jsp.logout" />
              </a></li>
            </ul>
          </div>
          <!-- <div class="mana_p">
							<a href=""><i class="mana_icon pson_icon"></i>个人信息</a> <a href=""><i
								class="mana_icon use_icon"></i>账号管理</a> <a href=""><i
								class="mana_icon out_icon"></i>退出系统</a>
						</div> -->
        </div>
        <!-- 消息 -->
        <div class="logining_icon" id="msg_tip_box">
          <a id="msg_tip_count" href="${ctx }/msgbox/smsMain"><i class="notice_icon"></i> <!-- 消息数量 --> </a>
          <div id="msgTip_div" style="position: absolute; top: 18px; right: -145px; opacity: 1; display: none;"></div>
        </div>
        <div class="logining_icon">
          <a href="javascript:void(0)"
            onclick="javascript:window.open('http://crm2.qq.com/page/portalpage/wpa.php?uin=800018382&amp;cref=${pageContext.request.requestURL}&amp;ref=&amp;pt=scholarmate kefu&amp;f=1&amp;ty=1&amp;ap=&amp;as=&amp;aty=&amp;a=', '_blank', 'height=544, width=644,toolbar=no,scrollbars=no,menubar=no,status=no');"><i
            class="question_icon" style="cursor: pointer;"></i></a>
        </div>
        <div class="version_en" onclick="ScmMaint.changeLanguage(${locale=='en_US'}?'zh_CN':'en_US')">
          <c:if test="${locale=='zh_CN'}">
            <a href="javascript:;" style="" h>English </a>
          </c:if>
          <c:if test="${locale=='en_US'}">
            <a href="javascript:;">中文版 </a>
          </c:if>
        </div>
      </div>
    </div>
  </div>
  <%--菜单主体内容 --%>
  <%--菜单主体内容 --%>
  <div id="mainmenu_body">
    <%--加载的菜单 --%>
    <irismenu:menu type="menu" app="sns" />
    <!-- <div class="pull_downnav">
		<div class="two_menu">
			<ul>
				<li id="two_nav1"><a href="/inspg/inspgmain" id="two_nav">机构主页</a></li>
				<li id="two_nav2"><a href="/inspg/inspgatt" >关注的主页</a></li>
				<li id="two_nav3"><a href="/inspg/searchinspgexplore">发现主页</a></li>
			</ul>
		</div>
	</div> -->
    <div class="clear_h20"></div>
    <div class="clear_h20"></div>
    <div class="clear_h20"></div>
    <div class="clear_h20"></div>
    <!--over-->
  </div>
</div>
