<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>文件导入</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${resmod}/js_v8/pub/fileimport/fileimport_${lang}.js"></script>
<script type="text/javascript" src="${resmod}/js_v8/pub/fileimport/fileimport.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub.js"></script>
<script type="text/javascript">
$(function(){
	FileImport.select();
	$("#sourceFile").val("");
    if(document.getElementsByClassName("header__nav")){
        document.getElementById("num2").style.display="flex";
        document.getElementsByClassName("header-main__box")[0].removeChild(document.getElementById("num1"));
    }
    if(document.getElementById("search_some_one")){
        document.getElementById("search_some_one").onfocus = function(){
            this.closest(".searchbox__main").style.borderColor = "#2882d8";
        }
        document.getElementById("search_some_one").onblur = function(){
            this.closest(".searchbox__main").style.borderColor = "#ccc";
        }
    }
    var moveele = document.getElementsByClassName("header-nav__item-bottom")[0];
    var obj=document.getElementsByClassName("header-nav__item-selected")[0];
    if(obj){
        moveele.style.width = obj.offsetWidth + "px";
        moveele.style.left = obj.offsetLeft + "px";     
    } 
    var url = window.location.href;
    url = url.replace(/\?.*/g,"");
    history.pushState(null,null,url);
    if($("#resultMsg").length>0){
	    positioncenter({targetele:'new-success_save',closeele:'new-success_save-header_tip'});	
    }
    //2018-12-11 ajb
    //$("#fileCollectImportBackBtn").click();
});

function continueImpFile(){
    /**
     * 失败后重置导入类型与文件选择框
     */
    $("#sourceFile").val("");
    $("#dbType").val("");
    setTimeout(function(){
        $("#resultMsg").hide();
    },300);
};
//返回成果页面
function goBackPub(){
    location.href = "/psnweb/homepage/show?module=pub";
};
</script>
</head>
<body>
  <form id="importForm" action="/pub/file/import" enctype="multipart/form-data" method="post">
    <input type="hidden" id="des3PubFileId" name = "des3PubFileId" value="">
    <div class="Federal-retrieval_container">
      <div class="Federal-retrieval_header">
        <div class="Federal-retrieval_header-item">
          <div class="Federal-retrieval_header-avator Federal-retrieval_header-item_first">
            <span class="Federal-retrieval_header-avator_infor">1</span>
          </div>
          <span class="Federal-retrieval_header-line"></span>
          <div class="Federal-retrieval_header-avator Federal-retrieval_header-item_second">
            <span class="Federal-retrieval_header-avator_infor">2</span>
          </div>
          <span class="Federal-retrieval_header-line2"></span>
          <div class="Federal-retrieval_header-avator Federal-retrieval_header-item_third">
            <span class="Federal-retrieval_header-avator_infor">3</span>
          </div>
        </div>
        <div class="Federal-retrieval_header-tip_box">
          <span class="Federal-retrieval_header-item_detail" style="color: #2882d8;"><spring:message
              code="pub.fileimport.stupselectfile" /></span> <span class="Federal-retrieval_header-item_detail"><spring:message
              code="pub.fileimport.stupfiletype" /></span> <span class="Federal-retrieval_header-item_detail"><spring:message
              code="pub.fileimport.stupfilesave" /></span>
        </div>
      </div>
      <div class="Federal-retrieval_searchtip">
        <span class="Federal-retrieval_searchtip-detail"> <spring:message code="pub.fileimport.filemsg1" /> (<span
          class="Federal-retrieval_searchtip-detail_color"><spring:message code="pub.fileimport.filemsg2" /></span>) <spring:message
            code="pub.fileimport.filemsg3" /> (<span class="Federal-retrieval_searchtip-detail_color"><spring:message
              code="pub.fileimport.filemsg4" /></span>) <spring:message code="pub.fileimport.filemsg5" />
        </span>
      </div>
      <div class="Federal-retrieval_content-search">
        <div class="Federal-retrieval_psninfor-left" style="margin-top: 8px;">
          <spring:message code="pub.fileimport.filesource" />
        </div>
        <div class="Federal-retrieval_psninfor-right">
          <div class="Federal-retrieval_importfile-item">
            <i class="selected-oneself" value="SCMEXCEL"></i> <span class="Federal-retrieval_importfile-item_name"><spring:message
                code="pub.fileimport.filexls1" /><a class="Federal-retrieval_importfile-item_about"
              href="/pub/one/openfile?type=1&flag=9" target="_self"><spring:message code="pub.fileimport.filexls2" /></a>
              <spring:message code="pub.fileimport.filexls3" /></span>
          </div>
          <div class="Federal-retrieval_importfile-item">
            <i class="selected-oneself" value="ENDNOTE"></i> <span class="Federal-retrieval_importfile-item_name">EndNote--</span>
            <a href="${resmod}/help/EndNote_${lang}.htm" target="_blank" class="Federal-retrieval_importfile-item_about">Text
              File (*.txt) </a> <i class="find-nothing_tip"></i>
          </div>
          <div class="Federal-retrieval_importfile-item">
            <i class="selected-oneself" value="BIBTEX"></i> <span class="Federal-retrieval_importfile-item_name">BibTex
              --</span> <a href="${resmod}/help/BibTex_${lang}.htm" target="_blank"
              class="Federal-retrieval_importfile-item_about">Text File (*.txt,*.bib)</a> <i class="find-nothing_tip"></i>
          </div>
          <div class="Federal-retrieval_importfile-item">
            <i class="selected-oneself" value="REFWORK"></i> <span class="Federal-retrieval_importfile-item_name">Refworks
              --</span> <a href="${resmod}/help/RefWorks_${lang}.htm" target="_blank"
              class="Federal-retrieval_importfile-item_about">Tagged Format</a> <i class="find-nothing_tip"></i>
          </div>
          <div class="Federal-retrieval_importfile-item">
            <i class="selected-oneself" value="CSA"></i> <span class="Federal-retrieval_importfile-item_name">Cambridge
              Scientific Abstracts (CSA) --</span> <a href="${resmod}/help/CSA_${lang}.htm" target="_blank"
              class="Federal-retrieval_importfile-item_about">PC Format</a> <i class="find-nothing_tip"></i>
          </div>
          <div class="Federal-retrieval_importfile-item">
            <i class="selected-oneself" value="ISI"></i> <span class="Federal-retrieval_importfile-item_name">Web
              of Science --</span> <a href="${resmod}/help/ISI_${lang}.htm" target="_blank"
              class="Federal-retrieval_importfile-item_about">Plain Text</a> <i class="find-nothing_tip"></i>
          </div>
        </div>
      </div>
      <div class="Federal-retrieval_infor-item">
        <div class="Federal-retrieval_infor-left">
          <spring:message code="pub.fileimport.filetext" />
        </div>
        <div class="Federal-retrieval_infor-right" style="border: none;">
          <div class="Federal-retrieval_file-container">
            <i class="Federal-retrieval_file-container—tip"></i> <span
              class="Federal-retrieval_file-container_tip-content"> <input class="sourceFile" readonly="readonly"
              style="display: inline; width: 300px;">
            </span>
          </div>
          <div class="Federal-retrieval_file-container_btn">
            <spring:message code="pub.fileimport.filelook" />
            <input type="File" id="sourceFile" onchange="FileImport.selectFile(this)" name="sourceFile"
              class="Federal-retrieval_file-container_select">
          </div>
        </div>
      </div>
      <div class="Federal-retrieval_infor-item" style="margin-top: 32px;">
        <div class="Federal-retrieval_infor-left"></div>
        <div class="Federal-retrieval_infor-right" style="border: none;">
          <div class="Federal-retrieval_file-container_input" onclick="FileImport.importFile(this);">
            <spring:message code="pub.fileimport.fileimport" />
          </div>
          <div class="Federal-retrieval_infor-footer_clear" id="fileCollectImportBackBtn"
            onclick="goBackPub();">
            <spring:message code="pub.fileimport.back" />
          </div>
        </div>
      </div>
    </div>
    <input type="hidden" name="dbType" id="dbType" value="${importfileVo.dbType}" /> <input type="hidden"
      name="publicationArticleType" id="publicationArticleType" value="${importfileVo.publicationArticleType}" />
  </form>
  <c:if test="${not empty totalResult and totalResult==-1}">
    <div class="background-cover" id="resultMsg" style="display: none;">
      <div class="new-success_save" id="new-success_save">
        <div class="new-success_save-body">
          <div class="new-success_save-body_avator">
            <img src="${resmod}/smate-pc/img/fail.png">
          </div>
          <div class="new-success_save-body_tip">
            <span><spring:message code="referencesearch.lable.contentFormat"/></span>
          </div>
          <div class="new-success_save-body_footer">
            <div class="new-success_save-body_footer-continue" onclick="continueImpFile();"><spring:message code="dialog.manageTag.btn.close"/></div>
          </div>
        </div>
      </div>
    </div>
  </c:if>
</body>
</html>
