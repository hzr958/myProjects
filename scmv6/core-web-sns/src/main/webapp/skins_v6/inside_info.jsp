<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<style type="text/css">
.dev_material-icons {
  direction: ltr;
  display: inline-block;
  font-family: "Material Icons";
  font-feature-settings: "liga";
  font-size: 24px;
  font-style: normal;
  font-weight: normal;
  letter-spacing: normal;
  line-height: 1;
  overflow-wrap: normal;
  text-rendering: optimizelegibility;
  text-transform: none !important;
  white-space: nowrap;
}
</style>
<script type="text/javascript">
//全文请求
function toReqFullTextMsg(){
    BaseUtils.checkCurrentSysTimeout("/dynweb/ajaxtimeout", function(){
        document.location.href = "/dynweb/showmsg/msgmain?model=reqFullTextMsg";
    }, 1);
}
//站内信
function toChatMsg(){
    BaseUtils.checkCurrentSysTimeout("/dynweb/ajaxtimeout", function(){
        document.location.href = "/dynweb/showmsg/msgmain?model=chatMsg";
    }, 1);
}
//消息中心
function toCenterMsg(){
    BaseUtils.checkCurrentSysTimeout("/dynweb/ajaxtimeout", function(){
        document.location.href = "/dynweb/showmsg/msgmain?model=centerMsg";
    	
    }, 1);
}
//个人设置
function toPsnSettings(){
    BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function(){
        document.location.href = "/psnweb/psnsetting/main";
    }, 1);
}
//切换站点
function toChangeSites(){
    BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function(){
        document.location.href = "/psnweb/site/switchsite";
    }, 1);
}
//切换语言
function changeCurrentLang(lang){
    BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function(){
        BaseUtils.change_local_language(lang)
    }, 1);
}

//是否显示切换角色菜单
$(function(){
    var currentInsId = $("#current_user_insid").val();
    var currentPsnId = $("#userDes3PsnId").val();
    var needJudgeRole = !BaseUtils.checkIsNull(currentInsId) && !BaseUtils.checkIsNull(currentPsnId) && currentInsId != "0";
    if(needJudgeRole){
        $.ajax({
            url: "/oauth/sierole/ajaxjudge",
            type: "post",
            data: {
                "des3PsnId" : currentPsnId,
                "des3InsId" : currentInsId
            },
            dataType: "json",
            success: function(data){
                if(data.result == "success" && data.hasMultiRole == "1"){
                    var sys = $("#sys_of_user").val();
                    if("SIE" == sys){
                        $("#change_rol_role_li").show();
                    }else{
                        $("#change_sie_role_li").show();
                    }
                }
            },
            error: function(){}
        });
    }
    var url = window.location.href;
    if(url.indexOf("/dynweb/showmsg")!= -1){
    	$(".header-nav__item-bottom").hide();
    }
});
</script>
<input type="hidden" value="<iris:des3 code='${userData.rolInsId}'/>" id="current_user_insid" />
<input type="hidden" value="${userData.des3PsnId}" id="userDes3PsnId" />
<input type="hidden" value="${userData.sys }" id="sys_of_user" />
<div class="header-main__actions" style="display: flex; flex-shrink: 0;">
  <div class="header-main__actions_item" onclick="toReqFullTextMsg();">
    <div id="full_text_request_msg_count" class=""></div>
    <div class="header-main__actions_icon fulltext-request"></div>
    <div class="header-main__actions_item-tip">
      <s:text name='page.main.full_request' />
    </div>
  </div>
  <div class="header-main__actions_item" onclick="toChatMsg();">
    <div id="chat_msg_count" class=""></div>
    <div class="header-main__actions_icon inbox-messages"></div>
    <div class="header-main__actions_item-tip">
      <s:text name='page.main.inner.message' />
    </div>
  </div>
  <%--<div class="header-main__actions_item" onclick="toCenterMsg();">
    <div id="other_msg_count" class=""></div>
    <div class="header-main__actions_icon system-notifications"></div>
    <div class="header-main__actions_item-tip">
      <s:text name='page.main.msg.center' />
    </div>
  </div>--%>
  <div class="header-main__psn" style="margin-left: 5px; width: 28px; height: 28px;">
    <div class="header-main__psn-avatar" id="skin_psn_auatars">
      <img src="${userData.avatars }?S=<%=Math.random() %>"
        onerror="this.onerror=null;this.src='/resmod/smate-pc/img/logo_psndefault.png'">
    </div>
    <div class="header-main__dropdown_psn" id="skin_psn_setting">
      <div class="chevron-icon_up">
        <div class="chevron-icon__div1"></div>
        <div class="chevron-icon__div2"></div>
      </div>
      <ul class="menu__list">
        <li class="menu__item">
          <div class="menu-item__content">
            <div class="menu-item__content_main" onclick="toPsnSettings();">
              <s:text name='page.main.selfSetting' />
            </div>
            <div class="menu-item__content_icon"></div>
          </div>
        </li>
        <li class="menu__item">
          <div class="menu-item__content">
            <div class="menu-item__content_main" onclick="toChangeSites();">
              <s:text name='page.main.scholmate_online' />
            </div>
            <div class="menu-item__content_icon"></div>
          </div>
        </li>
        <li class="menu__item">
          <div class="header-main__actions_link"
            onclick="javascript:window.open('http://crm2.qq.com/page/portalpage/wpa.php?uin=800018382&amp;cref=${pageContext.request.requestURL}&amp;ref=&amp;pt=scholarmate kefu&amp;f=1&amp;ty=1&amp;ap=&amp;as=&amp;aty=&amp;a=', '_blank', 'height=544, width=644,toolbar=no,scrollbars=no,menubar=no,status=no');">
            <s:text name='page.main.online' />
          </div>
        </li>
        <li class="menu__item" id="change_sie_role_li" style="display: none;">
          <div class="menu-item__content">
            <div class="menu-item__content_main"
              onclick="window.location.href='http://${userData.rolDomain}/insweb/select-user-role?switch=yes'">
              <s:text name="skin.main.switchrole" />
            </div>
            <div class="menu-item__content_icon"></div>
          </div>
        </li>
        <li class="menu__item" id="change_rol_role_li" style="display: none;">
          <div class="menu-item__content">
            <div class="menu-item__content_main"
              onclick="window.location.href='http://${userData.rolDomain}/scmwebrol/select-user-role?switch=yes'">
              <s:text name="skin.main.switchrole" />
            </div>
            <div class="menu-item__content_icon"></div>
          </div>
        </li>
        <li class="menu__item">
          <div class="menu-item__content">
            <c:if test="${locale=='zh_CN'}">
              <div class="menu-item__content_main" onclick="changeCurrentLang('en_US')">English</div>
            </c:if>
            <c:if test="${locale=='en_US'}">
              <div class="menu-item__content_main" onclick="changeCurrentLang('zh_CN')">中文</div>
            </c:if>
            <div class="menu-item__content_icon"></div>
          </div>
        </li>
        <li class="menu__item">
          <div class="menu-item__content">
            <div class="menu-item__content_main"
              onclick="window.location.href='https://'+document.domain+'/oauth/logout?sys=${userData.sys}&sysDomain=${userData.rolDomain}'">
              <s:text name='skins.main.jsp.logout' />
            </div>
            <div class="menu-item__content_icon"></div>
          </div>
        </li>
      </ul> 
    </div>
  </div>
  <i class="header-main__psn-icon" onclick="BaseUtils.expand_more(event)"></i>
</div>
<!-- <div class="new-header_function-container">
  <i class="new-header_function-container_icon"></i>
  <span class="new-header_function-container_detail">升级账号</span>
</div> -->
