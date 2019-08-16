<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib
  uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ include file="/common/taglibs.jsp"%>
<s:set var="SESSION_MENU_ID" value="#session['##SESSION_MENU_ID']" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=10;IE=9; IE=8; IE=EDGE" />
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta name="keywords" content="科研" />
<meta http-equiv="content-style-type" content="text/css" />
<style>
.select-item {
  cursor: pointer;
  padding: 0 4px 0px 8px;
}

.select-item:hover {
  background-color: #106abc;
  color: #fff;
}

.part {
  background-color: #106abc;
  color: #fff;
}

.item {
  background-color: #09439b;
}

.show_section-item {
  display: flex;
  justify-content: space-around;
  width: 100%;
  height: 36px;
  line-height: 36px;
  background-color: #106abc;
  margin: 4px 0px;
  position: fixed;
}

.show_section-part {
  cursor: pointer;
  color: #fff;
  padding: 0 8px;
}

.show_section-part:hover {
  color: #fff;
  text-decoration: none;
}

.show_section-part_item:hover {
  color: #fff;
  text-decoration: none;
}

.show_section-part_item {
  color: #fff;
  text-decoration: none;
}

.select-container {
  display: flex;
  justify-content: flex-end;
  position: relative;
  padding: 12px;
}

.selectbox {
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  width: 180px;
  height: 108px;
  border: 1px solid #ccc;
  position: absolute;
  top: 43px;
  right: 12px;
  z-index: 10;
  background-color: #fff;
  display: none;
}

div a {
  text-decoration: none;
}

div a:hover {
  text-decoration: none;
}

.materiale-icons {
  background: rgba(0, 0, 0, 0) url(/resmod/images/icon-show.png) no-repeat scroll 0 0;
  border: 1px solid #fff;
  width: 20px;
  height: 20px;
}

.dev_title-icon_up {
  background-position: -78px -26px;
}

.dev_title-icon_down {
  background-position: -78px 0px;
}
</style>
<c:choose>
  <c:when test="${!empty userRolData.rolLogoUrl and userRolData.fromIsis eq 'nsfcscm'}">
    <c:set var="pageTitle">
      <s:text name="skin.main.title_nsfcscm" />
    </c:set>
    <c:set var="pageHeaderMargin">margin-top:0px;</c:set>
    <c:set var="pageTopLeftContent">
      <div class="unit_logo">
        <img src="${resmod}/images/logo_kyzx.gif" height="50" title="<s:text name='skin.main.title_nsfcscm' />"
          alt="<s:text name='skin.main.title_nsfcscm' />" />
      </div>
    </c:set>
    <c:set var="pageTopRightScmLogo">
      <div class="sm_logo2">
        <a href="${snsDomain}/scmwebsns/main?rolInsId=0"><img src="${resmod}/images/rol_home/s_logo.gif" width="261"
          height="27" alt="<s:text name="skin.main.title_scm" />" title="<s:text name="skin.main.title_scm" />" /></a>
      </div>
    </c:set>
    <c:set var="selRol" value="no" />
  </c:when>
  <c:when test="${!empty userRolData.rolLogoUrl and userRolData.fromIsis eq 'nsfcr'}">
    <c:set var="pageTitle">
      <s:text name="skin.main.title_nsfcr" />
    </c:set>
    <c:set var="pageHeaderMargin">margin-top:0px;</c:set>
    <c:set var="pageTopLeftContent">
      <div class="unit_logo">
        <img src="${resmod }/images/logo_cgzx.gif" height="50" title="<s:text name='skin.main.title_nsfcr' />"
          alt="<s:text name='skin.main.title_nsfcr' />" />
      </div>
    </c:set>
    <c:set var="pageTopRightScmLogo">
      <div class="sm_logo2">
        <a href="${snsDomain}/scmwebsns/main?rolInsId=0"> <img src="${resmod }/images/rol_home/s_logo.gif"
          width="261" height="27" alt="<s:text name="skin.main.title_scm" />"
          title="<s:text name="skin.main.title_scm" />" />
        </a>
      </div>
    </c:set>
    <c:set var="selRol" value="no" />
  </c:when>
  <c:when test="${!empty userRolData.rolLogoUrl}">
    <c:set var="pageTitle">${userRolData.rolTitle }</c:set>
    <c:set var="pageHeaderMargin">margin-top:0px;</c:set>
    <input type="hidden" value="${userRolData.rolInsId }" id="orolInsId" />
    <c:set var="pageTopLeftContent">
      <div class="unit_logo">
        <img src="${userRolData.rolLogoUrl }" height="50" title="${userRolData.rolTitle }"
          alt="${userRolData.rolTitle }" />
      </div>
    </c:set>
    <c:set var="pageTopRightScmLogo">
      <div class="sm_logo2">
        <a href="${snsDomain}/scmwebsns/main?rolInsId=0"><img src="${resmod}/images/rol_home/s_logo.gif" width="261"
          height="27" alt="<s:text name="skin.main.title_scm" />" title="<s:text name="skin.main.title_scm" />" /></a>
      </div>
    </c:set>
  </c:when>
  <c:when test="${!empty userRolData.rolTitle}">
    <c:set var="pageTitle">${userRolData.rolTitle }</c:set>
    <c:set var="pageHeaderMargin">margin-top:0px;</c:set>
    <c:set var="pageTopLeftContent">
      <div class="unit_logo">
        <!-- 修改了单位标题显示内容(当前单位标题值为空时显示其他语言的单位名_MaoJianGuo_2013-01-30_ROL-396 -->
        <c:choose>
          <c:when test="${locale eq 'en_US' }">
            <span
              style="font-family: arial, Tahoma, Verdana, simsun, sans-serif; font-size: 26px; line-height: 50px; color: #395d94;">
              <c:choose>
                <c:when test="${!empty userRolData.rolTitleEn}">
							${userRolData.rolTitleEn}
							</c:when>
                <c:otherwise>
							${userRolData.rolTitleCh}
							</c:otherwise>
              </c:choose>
            </span>
          </c:when>
          <c:otherwise>
            <span style="font-size: 26px; color: #395d94; line-height: 50px; font-family: '微软雅黑', '黑体', '楷体';"> <c:choose>
                <c:when test="${!empty userRolData.rolTitleCh}">
							${userRolData.rolTitleCh}
							</c:when>
                <c:otherwise>
							${userRolData.rolTitleEn}
							</c:otherwise>
              </c:choose>
            </span>
          </c:otherwise>
        </c:choose>
      </div>
    </c:set>
    <c:set var="pageTopRightScmLogo">
      <%-- <div class="sm_logo2"><a href="${snsDomain}/scmwebsns/main?rolInsId=0"><img src="${resmod}/images/rol_home/s_logo.gif" width="261" height="27" alt="<s:text name="skin.main.title_scm" />" title="<s:text name="skin.main.title_scm" />"/></a></div> --%>
      <a alt="<s:text name="skin.main.title_scm" />" title="<s:text name="skin.main.title_scm" />"></a>
    </c:set>
  </c:when>
  <c:otherwise>
    <c:set var="pageTitle">
      <s:text name="skin.main.title_scm" />
    </c:set>
    <c:set var="pageHeaderMargin" value=""></c:set>
    <c:set var="pageTopLeftContent">
      <a href="${snsctx}" title="<s:text name="skin.main.title_scm" />" alt="<s:text name="skin.main.title_scm" />">
        <c:choose>
          <c:when test="${locale eq 'en_US' }">
            <img src="${resmod }/images/logo_${locale }.gif" title="科研之友" />
          </c:when>
          <c:otherwise>
            <img src="${resmod }/images/logo_${locale }.gif" width="277" height="20" title="科研之友" />
          </c:otherwise>
        </c:choose>
      </a>
    </c:set>
    <c:set var="pageTopRightScmLogo" value="" />
  </c:otherwise>
</c:choose>
<title>${pageTitle }</title>
<link href="${resmod }/css/common.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/public.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/home.css" rel="stylesheet" type="text/css" />
<%-- <link href="${resscmsns }/css_v5/css2016/public2016.css" rel="stylesheet" type="text/css" /> --%>
<link href="${resscmsns }/css_v5/css2016/header2016.css" rel="stylesheet" type="text/css" />
<link href="${resscmsns }/css_v5/css2016/footer2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/header_sns.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/page.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/plugin/jquery.browser.tips.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/smate.scmtips.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/jquery.validate.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/organization.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/smate.alerts.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/achievement.css" rel="stylesheet" type="text/css" />
<%-- <link href="${resmod }/css/plugin/jquery.thickbox.css" rel="stylesheet" type="text/css" /> --%>
<link href="${res}/css/thickbox.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
//定义环境变量
var lang = "${lang}";
var isSame="${userRolData.des3PsnId}";
var handle;
var refreshCount=1;
//以上变量为v5版本之前用到的环境变量.以下是v5版本增加的环境变量.
var snsctx='${snsctx}';
var resmod ='${resmod}';
var ressns ='${ressns}';
var resscmsns='${resscmsns}';
var respath = '${res}';
var locale='${locale}';
var domCas='${domaincas}';
var domscm='${domainscm}';
var logoutindex='${logoutindex}';
var searchType="输入姓名、所属机构查找人员";
var searchType2="输入关键词、作者、DOI查找论文";
var searchType3="输入关键词、作者、专利号查找专利";
var domainOauth='${domainoauth}';

window.onload=function(){
	//var pareselectid = "selectmd1";
	var pareselectid= '${selectedId}';
	var chilselecid;
	var id1 = '${selectedId}';
	var id2 = '${mmId}';
    var select = document.getElementsByClassName("materiale-icons")[0];
	var sum = document.getElementsByClassName("select-item");
    var total=sum;
    var num = document.getElementsByClassName("show_section-part");
    var targetbox = document.getElementsByClassName("selectbox")[0];
    var contentele = document.getElementsByClassName("select-container_title")[0];
    var sectionitsum = document.getElementsByClassName("show_section-part_item");   
    choicebox(id1,id2);

    select.addEventListener("click",function(){
    	if(this.classList.contains("dev_title-icon_down")==false){
    		targetbox.style.display = "flex";
    		this.setAttribute("class","materiale-icons");
    		this.className += " " + "dev_title-icon_down";
    	}else{
    		targetbox.style.display = "none";
    		this.setAttribute("class","materiale-icons");
    		this.className += " " + "dev_title-icon_up";
    	}
    })

	for(let i=0 ; i<sum.length; i++){
		sum[i].addEventListener("click",function(){
            if(this.classList.contains("part")==false){
                for(var j=0; j<total.length; j++){
                    total[j].setAttribute("class","select-item");
                    document.getElementsByClassName("show_section-item")[j].style.display="none";
                }
            	this.className +=" "+ "part";
            	pareselectid = this.getAttribute("id");
            	contentele.innerHTML="";
            	contentele.innerHTML=this.innerHTML;
            	targetbox.style.display = "none";
            	select.setAttribute("class","materiale-icons");
            	select.className += " " + "dev_title-icon_up";
            	if(document.getElementsByClassName("show_section-item")){
            		document.getElementsByClassName("show_section-item")[i].style.display="flex";
            	}
            }
            
		})
	}
	for(var z = 0; z<num.length; z++){
        num[z].addEventListener("click",function(){
        	if(this.classList.contains("item")==false){
        		var sort = this.parentNode.querySelectorAll(".show_section-part");
                for(let j=0; j<sort.length; j++){  //change
                    sort[j].setAttribute("class","show_section-part");
                }
                this.className +=" "+ "item";
        	}
        })
	}
	
  	for(var i =0 ; i<sectionitsum.length; i++){
        sectionitsum[i].addEventListener("click",function(){
	    var chilselecid  = this.parentNode.getAttribute("id");
	    var childselectcontent = this.getAttribute("data-jump");
	    window.location.href=childselectcontent+"?mmId="+chilselecid+"&selectedId="+pareselectid;
        })
     }
  	function choicebox(id1,id2){
        if(id1&&id2){
        	var eles = document.getElementsByClassName("show_section-part");
        	var selectEles = document.getElementsByClassName("select-item");
        	var selectIdsV = document.getElementsByClassName("show_section-item");
        	if(eles){
        		for(var i=0; i<eles.length; i++){
        			eles[i].setAttribute("class","show_section-part");
        		}
        	}
        	
        	if(selectEles){
        		for(var j=0; j<selectEles.length; j++){
        			selectEles[j].setAttribute("class","select-item");
        		}
        	}
        	
        	if(selectIdsV){
        		for(var k=0; k<selectIdsV.length; k++){
        			selectIdsV[k].style.display="none";
        		}
        	}
        	
        	if(!document.getElementById(id1)){
        		id1 = "selectmd1";
        	}
        	if(!document.getElementById(id2)){
        		id1 = "mm1";
        	}
        	
        	document.getElementById(id1).className +=" "+ "part";
        	//var num =  id1.replace("selectmd","");
        	document.getElementsByClassName("select-container_title")[0].innerHTML = document.getElementById(id1).innerHTML;
        	document.getElementById(id1+"_v").style.display="flex";
            document.getElementById(id2).className +=" "+ "item";
        }
    
    }
}
</script>
<script type="text/javascript" src="${resmod }/js_v5/jquery.js"></script>
<script type="text/javascript" src="${resmod }/js/plugin/jquery.browser.tips.js"></script>
<script type="text/javascript" src="${resmod }/js/json2.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.watermark.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.baidu.tongji.js"></script>
<script type="text/javascript" src="${resmod}/js/common.ui.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="${resmod}/js/msgbox/msgbox.tip_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/msgbox/msgbox.common.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.browser.tips.js"></script>
<script type="text/javascript" src="${resmod }/js/googleAnalytics.js"></script>
<script type="text/javascript" src="${resmod }/js/smate.menu.js"></script>
<script type="text/javascript" src="${resmod }/js/smate.index.maint.js"></script>
<script type="text/javascript" src="${resmod }/js/smate.maint.js"></script>
<script type="text/javascript" src="${resmod }/js/smate.cookie.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.alerts_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.alerts.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.scmtips.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.proceeding.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.proceedingwin.js"></script>
<script type="text/javascript" src="${resmod}/js/jquery.validate.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.page.js"></script>
<script type="text/javascript" src="${ressns}/js/inspg/inspgstatistics.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.module.loaddiv.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.thickbox.min.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.thickbox.resources.js"></script>
<script type="text/javascript" src="${resmod}/js/link.status.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/judge-browser/judge-browser.js"></script>
<script type="text/javascript">

var msg_common_timeout='<s:text name="sns.tip.timeout"/>';
var msg_common_reminder='<s:text name="dialog.manageTag.tip.cfmDelete.title"/>';
var msgBoxIsClose = parseInt(MsgBoxUtil.getCookie("msgBoxIsClose") == null 
		? 0 : MsgBoxUtil.getCookie("msgBoxIsClose")); //消息弹出框是否点击了关闭.
var scm_url='${snsDomain}/scmwebsns/main?rolInsId=0';		
var online_help_url="http://wpa.qq.com/msgrd?v=3&uin=800018382&site=qq&menu=yes";

</script>
<script type="text/javascript">
</script>
<%@ include file="refreshSession.jsp"%>
<%--以下业务块的head内容 --%>
<decorator:head />
</head>
<body>
  <!-- 节点id  skin_main.jsp-->
  <input id="nodeUrlId" type="hidden" value="<irisnode:node />" />
  <input id="des3PsnId" type="hidden" value="${userRolData.des3PsnId }" />
  <input id="rolDomain" type="hidden" value="${userRolData.rolDomain}" />
  <div class="top-t" style="height: 42px;">
    <div class="header-t" style="height: 50px; position: fixed; display: flex;">
      <div class="header_wrap-t" style="height: 50px;">
        <a href="/" class="logo fl"></a>
      </div>
      <div class="select-container">
        <div class="select-container_infor"
          style="width: 180px; height: 30px; line-height: 30px; border: 1px solid #aaa; display: flex; justify-content: space-around;">
          <div class="select-container_title" style="margin-right: 56px;"></div>
          <div class="materiale-icons dev_title-icon_up" style="margin: 8px 0px; cursor: pointer;"></div>
        </div>
        <div class="selectbox">
          <div id="selectmd1" class="select-item part">
            <a href="${snsDomain}/scmmanagement/cooperatorCmd/main?mmId=mm1&selectedId=selectmd1">查看全部</a>
          </div>
          <div id="selectmd2" class="select-item">
            <a href="${snsDomain}/scmmanagement/cooperatorCmd/main?mmId=mm8&selectedId=selectmd2">科技推荐应用</a>
          </div>
          <div id="selectmd3" class="select-item">
            <a href="${snsDomain}/scmmanagement/researchkws/main?mmId=mm9&selectedId=selectmd3">科学知识图谱分析</a>
          </div>
          <div id="selectmd4" class="select-item">
            <a href="${snsDomain}/scmmanagement/patent/compmain?mmId=mm12&selectedId=selectmd4">技术创新决策支持</a>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="show_section" style="margin-bottom: 120px;">
    <div id="selectmd1_v" class="show_section-item" style="display: none;">
      <div id="mm1" class="show_section-part item">
        <a class="show_section-part_item" href="javascript:void(0);"
          data-jump="${snsDomain}/scmmanagement/cooperatorCmd/main">科技推荐应用</a>
      </div>
      <div id="mm2" class="show_section-part">
        <a class="show_section-part_item" href="javascript:void(0);"
          data-jump="${snsDomain}/scmmanagement/researchkws/main">研究主题词分布</a>
      </div>
      <div id="mm3" class="show_section-part">
        <a class="show_section-part_item" href="javascript:void(0);"
          data-jump="${snsDomain}/scmmanagement/keywordsdistr/frontiermain">研究前沿分析</a>
      </div>
      <div id="mm4" class="show_section-part">
        <a class="show_section-part_item" href="javascript:void(0);"
          data-jump="${snsDomain}/scmmanagement/keywordsdistr/keyModulesmain">研究关键分析</a>
      </div>
      <div id="mm5" class="show_section-part">
        <a class="show_section-part_item" href="javascript:void(0);"
          data-jump="${snsDomain}/scmmanagement/patent/compmain">专利统计对比</a>
      </div>
      <div id="mm6" class="show_section-part">
        <a class="show_section-part_item" href="javascript:void(0);"
          data-jump="${snsDomain}/scmmanagement/patent/mapmain">专利地图</a>
      </div>
      <div id="mm7" class="show_section-part">
        <a class="show_section-part_item" href="javascript:void(0);"
          data-jump="${snsDomain}/scmmanagement/patent/matrixmain">专利矩阵</a>
      </div>
    </div>
    <div id="selectmd2_v" class="show_section-item" style="display: none;">
      <div id="mm8" class="show_section-part item">
        <a class="show_section-part_item" href="javascript:void(0);"
          data-jump="${snsDomain}/scmmanagement/cooperatorCmd/main">科技推荐应用</a>
      </div>
    </div>
    <div id="selectmd3_v" class="show_section-item" style="display: none;">
      <div id="mm9" class="show_section-part item">
        <a class="show_section-part_item" href="javascript:void(0);"
          data-jump="${snsDomain}/scmmanagement/researchkws/main">研究主题词分布</a>
      </div>
      <div id="mm10" class="show_section-part">
        <a class="show_section-part_item" href="javascript:void(0);"
          data-jump="${snsDomain}/scmmanagement/keywordsdistr/frontiermain">研究前沿分析</a>
      </div>
      <div id="mm11" class="show_section-part item">
        <a class="show_section-part_item" href="javascript:void(0);"
          data-jump="${snsDomain}/scmmanagement/keywordsdistr/keyModulesmain">研究关键分析</a>
      </div>
    </div>
    <div id="selectmd4_v" class="show_section-item" style="display: none;">
      <div id="mm12" class="show_section-part">
        <a class="show_section-part_item" href="javascript:void(0);"
          data-jump="${snsDomain}/scmmanagement/patent/compmain">专利统计对比</a>
      </div>
      <div id="mm13" class="show_section-part">
        <a class="show_section-part_item" href="javascript:void(0);"
          data-jump="${snsDomain}/scmmanagement/patent/mapmain">专利地图</a>
      </div>
      <div id="mm14" class="show_section-part">
        <a class="show_section-part_item" href="javascript:void(0);"
          data-jump="${snsDomain}/scmmanagement/patent/matrixmain">专利矩阵</a>
      </div>
    </div>
  </div>
  <div class="clear_h20 zzx"></div>
  <%--加载的主体内容 --%>
  <decorator:body />
  <div class="clear_h20"></div>
  <!-- FOOTER START -->
  <jsp:include page="/common/footer_search.jsp"></jsp:include>
  <!-- FOOTER END -->
</body>
</html>