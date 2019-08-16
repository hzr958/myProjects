<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div id="sm_header" style="margin-bottom: 10px; height: 50px; position: relative;">
  <%--系统logo --%>
  <div class="sm_logo" style="max-width: 300px;">${pageTopLeftContent}</div>
  <%--切换站点 --%>
  <div class="${'sm_toplogo_en'}">${pageTopRightScmLogo }</div>
  <div class="sm_top_menu" style="padding-top: 20px;">
    <s:text name="page.main.welcome"></s:text>
    <span id="psnBox" style="position: relative;"> <%--用户名 --%> <a class="sm_top_name Blue" id="link-setting">
        <%--修改了名称的显示方式，限制只显示20个字符_MJG_ --%> <c:set var="curr_username"
          value="${ 'null null' == userRolData.username? userRolData.enUsername: userRolData.username }" /> <c:if
          test="${locale=='en_US'}">
          <c:set var="curr_username"
            value="${ 'null null' == userRolData.enUsername? userRolData.username: userRolData.enUsername}" />
        </c:if> <c:choose>
          <c:when test="${fn:length(curr_username) > 10}">
		      		${fn:substring(curr_username, 0, 10)}......
		     		</c:when>
          <c:otherwise>
		      		${curr_username}
		     		</c:otherwise>
        </c:choose>
    </a> <%--账户设置 --%>
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
          <li><a href="#" onclick="ScmMaint.logoutSys()" title="<s:text name='skins.main.jsp.logout' />"> <i
              class="py-icon icon25"></i> <s:text name="skins.main.jsp.logout" />
          </a></li>
        </ul>
      </div> <i class="top_useimg"></i>
    </span> <span>|</span>
    <%--消息中心 --%>
    <b id="msg_tip_zone" style="position: relative;"> <a id="msg_tip_box" href="${snsctx}/msgbox/smsMain"> <s:text
          name="shins.main.jsp.label.msgCenter" /> <span id="main_msg_total_label" class="red"></span>
    </a> <span class="Orange" id="msg_tip_totalTop" style="display: none;">${unReadTotal }</span>
      <div id="msgTip_div" style="position: absolute; top: 18px; top: 8px\0; right: -145px; opacity: 1; display: none;"></div>
    </b> <span>|</span>
    <%--帮助中心 --%>
    <c:if test="${locale=='en_US'}">
      <a href="${res}/html/helpCenter/index_en_US.html" target="_blank">&nbsp;Learning Center&nbsp;</a>
    </c:if>
    <span>|</span>
    <%--在线客服 --%>
    <!-- <a href="#" class="Online-Service"><s:text name='page.main.online' /></a> -->
    <%-- <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=800018382&site=qq&menu=yes"><s:text name='page.main.online' /></a> --%>
    <!-- 		<script charset="utf-8" type="text/javascript" src="http://wpa.b.qq.com/cgi/wpa.php?key=XzgwMDAxODM4Ml8yNDEzNTFfODAwMDE4MzgyXw&id=iframeqq"></script>
 -->
    <%--切换语言 --%>
  </div>
  <div class="clear_h20"></div>
</div>
<%--菜单主体内容 --%>
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
  <%-- <iris:menu type="nav" app="sns" /> --%>
  <div class="clear_h20"></div>
  <!--over-->
  <%--检索人员隐藏标签 --%>
  <span id="top-search_hidden" style="display: none;">
    <form action="${snsctx}/userSearch/main" method="get" id="search_some_one_form" target="_black"
      onsubmit="return ScmMaint.searchSomeSubmit()">
      <input name="userInfo" id="search_some_one" title="<s:text name='page.main.search' />" type="text" /> <a href="#"
        onclick="ScmMaint.searchSomeOne();" class="top_search_btn"></a>
    </form>
  </span>
</div>
<script type="text/javascript">
	   $(function(){//更换QQ的图片，去掉中文
		    $(".sm_toplogo_en img").remove(); 
		   if('${userRolData.sieVersion}'==1){
			  var replaceqqimg =  setInterval(function(){  
				   var div = $("iframe:eq(1)").contents().find("#launchBtn");
				   if(div.length==1){
					   clearInterval(replaceqqimg);
				   }
				   $(div).css({"background-image":"url(/resscmwebsns/images_v5/a01.png)"});
			  },10);
		   }
	      
		});
</script>