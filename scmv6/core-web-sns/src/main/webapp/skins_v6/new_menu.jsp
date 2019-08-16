<%@ page language="java" pageEncoding="UTF-8"%>
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
}); 

//显示应用菜单
function showAppMenu( obj){
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
    });
}
//主页
function toMyHomepage(){
    BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function(){
        document.location.href = "/psnweb/homepage/show";
    });
}
//联系人菜单
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



function toApplication(event){
  /* $.ajax({
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
  });  */
  event.stopPropagation();
  event.preventDefault();
  if(document.getElementsByClassName("application_bomb-background").length>0){
      document.getElementsByClassName("application_bomb-background")[0].style.display = "block";
  }
  setTimeout(function(){
      if(document.getElementsByClassName("application_bomb-container").length>0){
         document.getElementsByClassName("application_bomb-container")[0].style.right = "0px";
      }
      if(document.getElementsByClassName(" header-main__dropdown_psn").length>0){
          document.getElementsByClassName(" header-main__dropdown_psn")[0].style.display = "none";
      }
  },150);
}

document.onclick = function(){
    if(document.getElementsByClassName("application_bomb-container").length>0){
        document.getElementsByClassName("application_bomb-container")[0].style.right = "-350px";
    }
    setTimeout(function(){
        if(document.getElementsByClassName("application_bomb-background").length>0){
            document.getElementsByClassName("application_bomb-background")[0].style.display = "none";
        }
    },100);
}

function snsToCXC(){
    BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function(){
        window.open("/psnweb/login/tocxc");
    });
}

</script>
<%@ include file="/common/taglibs.jsp"%>
<div class="header__nav" style="width: 400px !important">
  <!-- /*width:358px;*/ -->
  <nav class="header__nav_box">
    <ul class="header-nav__list" style="height: 124%; justify-content: center;">
      <!--  添加居中 -->
      <li onclick="toDynMain();"
        class="header-nav__item <c:if test='${userData.menuId==1}'> header-nav__item-selected</c:if>"
        data-class="first-page"><span class="header-nav__item-content"> <s:text
            name='sns.menu.homepage.index' />
      </span></li>
      <li onclick="toMyHomepage();"
        class="header-nav__item <c:if test='${userData.menuId==2}'> header-nav__item-selected</c:if>"
        data-class="mine-item"><span class="header-nav__item-content"> <s:text name='sns.menu.homepage.name' />
      </span></li>
      <li onclick="toFriends();"
        class="header-nav__item <c:if test='${userData.menuId==3}'> header-nav__item-selected</c:if>"
        data-class="mine-friend"><span class="header-nav__item-content"> <s:text name='sns.menu.friend.name' />
      </span></li>
      <li onclick="toGroups();"
        class="header-nav__item <c:if test='${userData.menuId==4}'> header-nav__item-selected</c:if>"
        data-class="mine-group"><span class="header-nav__item-content"> <s:text name='sns.menu.group.name' />
      </span></li>
      <li onclick="toApplication(event);"
        class="header-nav__item <c:if test='${userData.menuId==5}'> header-nav__item-selected</c:if>"
        data-class="mine-application"><span><s:text name='sns.menu.application.name' /><span></li>
    </ul>
    <div class="header-nav__item-bottom setting-list_page-item_hidden"></div>
  </nav>
</div>
