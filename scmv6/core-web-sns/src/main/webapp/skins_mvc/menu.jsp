<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript" src="/resmod/smate-pc/newconfirmemail/v_confirmemail_${locale }.js"></script>
<script type="text/javascript" src="/resmod/smate-pc/newconfirmemail/v_confirmemail.js"></script>
<script type="text/javascript" src="/resmod/smate-pc/newconfirmemail/v_confirmmobile.js"></script>
<script type="text/javascript" src="/resmod/smate-pc/new-improveimpact/scmpc-increaseimpact.js"></script>
<script type="text/javascript" src="/resmod/smate-pc/new-improveimpact/v_increaseimpact.js"></script>
<script type="text/javascript" src="/resmod/smate-pc/js/scm-pc_filedragbox.js"></script>
<script type="text/javascript" src="/resmod/smate-pc/new-improveimpact/improveimpact_${locale }.js"></script>

<script type="text/javascript">

$(document).ready(function(){
    setTimeout(checkAccountEmail, 2000); //延迟加载，先加着 完善个人信息
    setTimeout(judgeAlertImpact,2000);
});

//显示应用菜单
function showAppMenu(obj){
    $(obj).find(".application_bomb").css("display" ,"block") ;
}
//隐藏app菜单
function hiddenAppMenu(obj){
    $(obj).find(".application_bomb").css("display" ,"none") ;
}

//去首页
function toDynMain(){
    BaseUtils.checkCurrentSysTimeout("/dynweb/ajaxtimeout", function(){
        document.location.href = "/dynweb/main";
    }, 1);
}
//主页
function toMyHomepage(){
    BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function(){
        document.location.href = "/psnweb/homepage/show";
    }, 1);
}
//好友菜单
function toFriends(){
    BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function(){
        document.location.href = "/psnweb/friend/main?module=rec";
    }, 1);
}
//群组
function toGroups(){
    BaseUtils.checkCurrentSysTimeout("/groupweb/ajaxtimeout", function(){
        document.location.href = "/groupweb/mygrp/main";
    }, 1);
}
//项目
function toPrj(){
    BaseUtils.checkCurrentSysTimeout("/prjweb/ajaxtimeout", function(){
        document.location.href = "/prjweb/project/prjmain";
    }, 1);
}
//基金
function toFunds(){
    BaseUtils.checkCurrentSysTimeout("/prjweb/ajaxtimeout", function(){
        document.location.href = "/prjweb/fund/main";
    }, 1);
}
//文献（论文）
function toReferences(){
    BaseUtils.checkCurrentSysTimeout("/pubweb/ajaxtimeout", function(){
        document.location.href = "/pub/applypapermain";
    }, 1);
}
//文件
function toFiles(){
    BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function(){
        document.location.href = "/psnweb/myfile/filemain";
    }, 1);
}
//机构主页
function toInspage(){
    BaseUtils.checkCurrentSysTimeout("/prjweb/ajaxtimeout", function(){
        document.location.href = "/prjweb/ins/mypage";
    }, 1);
}
function snsToCXC(){
    BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function(){
        document.location.href = "/psnweb/login/tocxc";
    }, 1);
}

</script>
<%@ include file="/common/taglibs.jsp"%>
<div class="header__nav">
  <nav class="header__nav_box">
    <ul class="header-nav__list" style="height: 124%;">
      <li onclick="toDynMain();" class="header-nav__item <c:if test='${userData.menuId==1}'> item_selected</c:if>">
        <a> <spring:message code='sns.menu.homepage.index' />
      </a>
      </li>
      <li onclick="toMyHomepage();" class="header-nav__item <c:if test='${userData.menuId==2}'> item_selected</c:if>">
        <a> <spring:message code='sns.menu.homepage.name' />
      </a>
      </li>
      <li onclick="toFriends();" class="header-nav__item <c:if test='${userData.menuId==3}'> item_selected</c:if>">
        <a> <spring:message code='sns.menu.friend.name' />
      </a>
      </li>
      <li onclick="toGroups();" class="header-nav__item <c:if test='${userData.menuId==5}'> item_selected</c:if>">
        <a> <spring:message code='sns.menu.group.name' />
      </a>
      </li>
      <li class="header-nav__item  <c:if test='${userData.menuId==5}'> item_selected</c:if>"
        onmouseover="showAppMenu(this);" onmouseout="hiddenAppMenu(this);"><a><spring:message
            code='sns.menu.application.name' /></a>
        <div class="application_bomb">
          <div class="application_bomb-body">
            <div class="application_bomb-body_content">
              <div class="application_bomb-body_content-title">
                <spring:message code="dyn.main.menu.personal.services" />
              </div>
              <div class="application_bomb-body_content-container">
                <div class="application_bomb-body_content-list">
                  <a onclick="toFunds();">
                    <div class="application_bomb-body_content-list_img application_bomb-body_content-fund"></div>
                    <div class="application_bomb-body_content-list_detail">
                      <spring:message code="dyn.main.menu.fund" />
                    </div>
                  </a>
                </div>
                <div class="application_bomb-body_content-list">
                  <a onclick="toPrj();">
                    <div class="application_bomb-body_content-list_img application_bomb-body_content-project"></div>
                    <div class="application_bomb-body_content-list_detail">
                      <spring:message code="dyn.main.menu.pro" />
                    </div>
                  </a>
                </div>
                <div class="application_bomb-body_content-list">
                  <a onclick="toReferences();">
                    <div class="application_bomb-body_content-list_img application_bomb-body_content-paper"></div>
                    <div class="application_bomb-body_content-list_detail">
                      <spring:message code="dyn.main.menu.paper" />
                    </div>
                  </a>
                </div>
                <div class="application_bomb-body_content-list">
                  <a target="_black" onclick="snsToCXC();">
                    <div class="application_bomb-body_content-list_img application_bomb-body_content-patent"></div>
                    <div class="application_bomb-body_content-list_detail">
                      <spring:message code="dyn.main.menu.patent" />
                    </div>
                  </a>
                </div>
                <div class="application_bomb-body_content-list">
                  <a onclick="toFiles();">
                    <div class="application_bomb-body_content-list_img application_bomb-body_content-file"></div>
                    <div class="application_bomb-body_content-list_detail">
                      <spring:message code="dyn.main.menu.file" />
                    </div>
                  </a>
                </div>
                <div class="application_bomb-body_content-list">
                  <a onclick="toInspage();">
                    <div class="application_bomb-body_content-list_img application_bomb-body_content-file"></div>
                    <div class="application_bomb-body_content-list_detail">
                      <spring:message code="dyn.main.menu.inspage" />
                    </div>
                  </a>
                </div>
              </div>
            </div>
            <div class="application_bomb-body_content">
              <div class="application_bomb-body_content-title">
                <spring:message code="dyn.main.menu.sie.services" />
              </div>
              <div class="application_bomb-body_content-serverse">
                <div class="application_bomb-body_content-serverse_list border-bottom_style-dashed">
                  <a href="http://irisaas.smate.com/egrantweb/" target="_black">
                    <div class="application_bomb-body_content-serverse_list-title" style="margin-top: 12px;">
                      <spring:message code="dyn.main.menu.unit.irisaas" />
                    </div>
                    <div class="application_bomb-body_content-serverse_list-detail" style="margin: 12px 0px;">
                      <spring:message code="dyn.main.menu.unit.intelligent" />
                    </div>
                  </a>
                </div>
                <div class="application_bomb-body_content-serverse_list border-bottom_style-dashed">
                  <a href="${resmod}/cotrun/html/sie_index.html" target="_black">
                    <div class="application_bomb-body_content-serverse_list-title" style="margin: 12px 0px;">
                      <spring:message code="dyn.main.menu.unit.sie" />
                    </div>
                    <div class="application_bomb-body_content-serverse_list-detail" style="margin-bottom: 20px;">
                      <spring:message code="dyn.main.menu.unit.platform" />
                    </div>
                  </a>
                </div>
                <div class="application_bomb-body_content-serverse_list">
                  <a href="/application/validate/maint" target="_black">
                    <div class="application_bomb-body_content-serverse_list-title" style="margin: 12px 0px;">
                      <spring:message code="dyn.main.menu.unit.quickaccess.srv" />
                    </div>
                    <div class="application_bomb-body_content-serverse_list-detail" style="margin-bottom: 20px;">
                      <spring:message code="dyn.main.menu.unit.quickaccess.info" />
                    </div>
                  </a>
              </div>
              </div>
            </div>
          </div>
          <div class="application_bomb-footer">
            <div class="application_bomb-footer_content">
              <i class="application_bomb-footer_tip"></i>
              <div class="application_bomb-footer_content-detail">
                <a href="http://sie.scholarmate.com/" target="_blank"><spring:message
                    code="dyn.main.menu.create.sie" /></a>
              </div>
            </div>
          </div>
        </div></li>
      <%-- <li class="header-nav__item <c:if test='${userData.menuId==5}'> item_selected</c:if>"><a href="/psnweb/application/main">
                <spring:message code='sns.menu.application.name' />
                </a></li> --%>
      <!-- SCM-16398  隐藏应用  start-->
      <%-- <li class="header-nav__item <c:if test='${userData.menuId==5}'> item_selected</c:if>">
                            <!-- <a href="/psnweb/application/main"> -->
                            <spring:message code='sns.menu.application.name' />
                            <!-- </a> -->
                            <div class="header-nav__item-list">
                                <div style="display: flex; flex-direction: column;">
                                    <a href="/prjweb/fund/main">
                                        <div class="header-nav__item-list__grp">
                                            <c:if test="${locale == 'en_US'}">
                                                <p class="header-nav__item-list__grp-fund" style="margin-left: 10px; margin-right: 6px;"></p>
                                                <p style="width: 63px; text-align: left;">
                                                    <spring:message code="sns.menu.fundapply.name"></s:text>
                                                </p>
                                                </c:if>
                                            <c:if test="${locale == 'zh_CN'}">
                                                <p class="header-nav__item-list__grp-fund"></p>
                                                <p style="margin-left: 12px;">
                                                    <spring:message code="sns.menu.fundapply.name"></s:text>
                                                </p>
                                            </c:if>
                                        </div>
                                    </a>
                                    <a href="/groupweb/mygrp/main">
                                        <div class="header-nav__item-list__grp">
                                            <c:if test="${locale == 'en_US'}">
                                                <p class="header-nav__item-list__grp-project" style="margin-left: 10px; margin-right: 6px;"></p>
                                                <p style="width: 63px; text-align: left;">
                                                    <spring:message code="sns.menu.homepage.project"></s:text>
                                                </p>
                                            </c:if>
                                            <c:if test="${locale == 'zh_CN'}">
                                                <p class="header-nav__item-list__grp-project"></p>
                                                <p style="margin-left: 12px;">
                                                    <spring:message code="sns.menu.homepage.project"></s:text>
                                                </p>
                                            </c:if>
                                        </div>
                                    </a>
                                    <a href="/pubweb/pubrecommendmain">
                                        <div class="header-nav__item-list__grp">
                                            <c:if test="${locale == 'en_US'}">
                                                <p class="header-nav__item-list__grp-paper ewreewrewrewr" style="margin-left: 10px; margin-right: 6px;"></p>
                                                <p style="width: 63px; text-align: left;">
                                                    <spring:message code="sns.menu.papersub.name"></s:text>
                                                </p>
                                            </c:if>
                                            <c:if test="${locale == 'zh_CN'}">
                                                <p class="header-nav__item-list__grp-paper"></p>
                                                <p style="margin-left: 12px;">
                                                    <spring:message code="sns.menu.papersub.name"></s:text>
                                                </p>
                                            </c:if>
                                        </div>
                                    </a>
                                    <a href="/psnweb/login/tocxc" target="_black">
                                        <div class="header-nav__item-list__grp">
                                            <c:if test="${locale == 'en_US'}">
                                                <p class="header-nav__item-list__grp-patent" style="margin-left: 10px; margin-right: 6px;"></p>
                                                <p style="width: 63px; text-align: left;">
                                                    <spring:message code="pub.menu.search.patent"></s:text>
                                                </p>
                                            </c:if>
                                            <c:if test="${locale == 'zh_CN'}">
                                                <p class="header-nav__item-list__grp-patent"></p>
                                                <p  style="margin-left: 12px;">
                                                    <spring:message code="pub.menu.search.patent"></s:text>
                                                </p>
                                            </c:if>
                                        </div>
                                    </a>
                                </div>
                            </div>
                        </li> --%>
      <!-- SCM-16398  隐藏应用  end-->
    </ul>
  </nav>
</div>
