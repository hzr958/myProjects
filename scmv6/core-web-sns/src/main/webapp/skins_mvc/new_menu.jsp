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
	var moveele = document.getElementsByClassName("header-nav__item-bottom")[0];
	var obj=document.getElementsByClassName("header-nav__item-selected")[0];
	if(obj){
		moveele.style.width = obj.offsetWidth + "px";
		moveele.style.left = obj.offsetLeft + "px";		
	}
	
   document.getElementsByClassName("application_bomb-container")[0].addEventListener("click",function(event){
        event=event||window.event;
        event.stopPropagation();
    });
    document.getElementsByClassName("application_bomb-body_content-key_icon")[0].onclick = function(event){
        event.stopPropagation();
        event.preventDefault();
        if(document.getElementsByClassName("application_bomb-container").length>0){
            document.getElementsByClassName("application_bomb-container")[0].style.right = "-350px";
        }
        setTimeout(function(){
            if(document.getElementsByClassName("application_bomb-background").length>0){
               document.getElementsByClassName("application_bomb-background")[0].style.display = "none";
            }
        },100);
    }
    document.getElementsByClassName("application_bomb-background")[0].style.height = window.innerHeight + "px";
    document.getElementsByClassName("application_bomb-container")[0].style.height = window.innerHeight + "px";
    window.onresize = function(){
        document.getElementsByClassName("application_bomb-background")[0].style.height = window.innerHeight + "px";
        document.getElementsByClassName("application_bomb-container")[0].style.height = window.innerHeight + "px";
    };
    document.getElementsByClassName("application_bomb-background")[0].onclick = function(){
        if(document.getElementsByClassName("application_bomb-container").length>0){
            document.getElementsByClassName("application_bomb-container")[0].style.right = "-350px";
        }
        setTimeout(function(){
            if(document.getElementsByClassName("application_bomb-background").length>0){
                document.getElementsByClassName("application_bomb-background")[0].style.display = "none";
            }
        },100);
    };
}); 
document.onclick = function(){
    if(document.getElementsByClassName("application_bomb-container").length>0){
        document.getElementsByClassName("application_bomb-container")[0].style.right = "-350px";
    }
    setTimeout(function(){
        if(document.getElementsByClassName("application_bomb-background").length>0){
            document.getElementsByClassName("application_bomb-background")[0].style.display = "none";
        }
    },100);
};
function toApplication(event){
  $.ajax({
    url : '/dynweb/main/ajaxgetmenuitem',
    type : 'post',
    data : {},
    dataType : 'json',
    success : function(data) {
      BaseUtils.ajaxTimeOut(data, function() {
        if(data.status==1){
          $(".application_bomb-body_content").find(".application_bomb-body_content-serverse_list:eq(-2)").addClass("border-bottom_style-dashed");
          $(".application_bomb-body_content").find(".application_bomb-body_content-serverse_list:last").show();
        }else{
          $(".application_bomb-body_content").find(".application_bomb-body_content-serverse_list:eq(-2)").removeClass("border-bottom_style-dashed");
        }
      });
  }
  });
  event.stopPropagation();
  event.preventDefault();
  if(document.getElementsByClassName("application_bomb-background").length>0){
      document.getElementsByClassName("application_bomb-background")[0].style.display = "block";
  }
  if(document.getElementsByClassName(" header-main__dropdown_psn").length>0){
      document.getElementsByClassName(" header-main__dropdown_psn")[0].style.display = "none";
  }
  setTimeout(function(){
      if(document.getElementsByClassName("application_bomb-container").length>0){
         document.getElementsByClassName("application_bomb-container")[0].style.right = "0px";
      }
  },150);
}

//去首页
function toDynMain(){
    BaseUtils.checkCurrentSysTimeout("/dynweb/ajaxtimeout", function(){
        document.location.href = "/dynweb/main";
    });
}
//主页
function toMyHomepage(){
    BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function(){
        document.location.href = "/psnweb/homepage/show";
    });
}
//好友菜单
function toFriends(){
    BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function(){
        document.location.href = "/psnweb/friend/main?module=rec";
    });
}
//群组
function toGroups(){
    BaseUtils.checkCurrentSysTimeout("/groupweb/ajaxtimeout", function(){
        document.location.href = "/groupweb/mygrp/main";
    });
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
    });
    
}
//文献（论文）
function toReferences(){
    BaseUtils.checkCurrentSysTimeout("/pubweb/ajaxtimeout", function(){
        document.location.href = "/pub/applypapermain";
        
    });
}
//文件
function toFiles(){
    BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function(){
        document.location.href = "/psnweb/myfile/filemain";
    });
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
    });
}

</script>
<%@ include file="/common/taglibs.jsp"%>
<div class="header__nav" style="margin-right: 0px;">
  <nav class="header__nav_box">
    <ul class="header-nav__list" style="height: 124%; justify-content: center;">
      <li onclick="toDynMain();"
        class="header-nav__item <c:if test='${userData.menuId==1}'> header-nav__item-selected</c:if>"
        data-class="first-page"><span class="header-nav__item-content"><spring:message
            code='sns.menu.homepage.index' /></span></li>
      <li onclick="toMyHomepage();"
        class="header-nav__item <c:if test='${userData.menuId==2}'> header-nav__item-selected</c:if>"
        data-class="mine-item"><span class="header-nav__item-content"><spring:message
            code='sns.menu.homepage.name' /></span></li>
      <li onclick="toFriends();"
        class="header-nav__item <c:if test='${userData.menuId==3}'> header-nav__item-selected</c:if>"
        data-class="mine-friend"><span class="header-nav__item-content"><spring:message
            code='sns.menu.friend.name' /></span></li>
      <li onclick="toGroups();"
        class="header-nav__item <c:if test='${userData.menuId==4}'> header-nav__item-selected</c:if>"
        data-class="mine-group"><span class="header-nav__item-content"><spring:message
            code='sns.menu.group.name' /></span></li>
      <li class="header-nav__item <c:if test='${userData.menuId==5}'> header-nav__item-selected</c:if>"
        data-class="mine-application" onclick="toApplication(event)"><span><spring:message
            code='sns.menu.application.name' /></span></li>
    </ul>
    <div class="header-nav__item-bottom setting-list_page-item_hidden"></div>
  </nav>
</div>
<div class="application_bomb-background" style="left: 0px; bottom: 0px;">
  <div class="application_bomb-container">
    <div class="application_bomb">
      <div class="application_bomb-body">
        <div class="application_bomb-body_content">
          <div class="application_bomb-body_content-key">
            <i class="list-results_close application_bomb-body_content-key_icon"></i>
          </div>
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
              <a target="_black" href="/psnweb/login/tocxc">
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
              <a href="${domainrol }/common/index" target="_black">
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
            <a href="/prjweb/ins/create/main" target="_blank"><spring:message code="dyn.main.menu.create.sie" /></a>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
