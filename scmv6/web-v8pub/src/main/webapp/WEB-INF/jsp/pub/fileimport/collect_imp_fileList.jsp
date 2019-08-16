<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
<title>成果导入</title>
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
  <script type="text/javascript" src="${resmod}/js_v8/pub/fileimport/fileimport_${lang}.js"></script>
  <script type="text/javascript" src="${resmod}/js_v8/pub/fileimport/fileimport.js"></script>
  <script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
  <script type="text/javascript" src="${resmod}/js/psnhome/pub/pub.js"></script>
  <script type="text/javascript">
$(function(){
	initImpList();
	var  fixele = document.getElementsByClassName("new-getdata_tip-container")[0];
	fixele.style.top = (window.innerHeight - fixele.offsetHeight)/2 + "px";
	fixele.style.left = (window.innerWidth - fixele.offsetWidth)/2 + "px";
	window.onresize = function(){
	    fixele.style.top = (window.innerHeight - fixele.offsetHeight)/2 + "px";
	    fixele.style.left = (window.innerWidth - fixele.offsetWidth)/2 + "px";
	}
	
});
function initImpList(){
    $(".selected-author,.selected-author_confirm").click(function(){
        var $obj = $(this);
        if($obj.hasClass("selected-author_confirm")){
            if($obj.attr("name") == "selectAll"){
                 $(".selected-author[name='seqNo']").removeClass("selected-author_confirm");
            }
            $obj.removeClass("selected-author_confirm");  
            if($obj.attr("name") == "seqNo"){
	            $("i[name='selectAll']").removeClass("selected-author_confirm");        	
            }
        }else{
            if($obj.attr("name") == "selectAll"){
                $(".selected-author[name='seqNo']").addClass("selected-author_confirm");
            }
            $obj.addClass("selected-author_confirm");               
            var selAll = true ;
            $(".selected-author[name='seqNo']").each(function(){
                if(  !$(this).hasClass("selected-author_confirm") ){
                    selAll = false ;
                }
            });
            if(selAll === true){
                $(".selected-author[name='selectAll']").addClass("selected-author_confirm");
            }
        }
    });
};
function getJson(){
	var pubList=[];
	$(".new-importresult_container-body_item").each(function(i){
		var $this = $(this);
		var pub = {};
		var $selectSeqNo = $this.find("div[name='seqNo']");
		pub.seqNo = $selectSeqNo.attr("value");
		pub.sitations=[];
		$this.find("i[name='situation']").each(function(){
			var $situation = $(this);
			if($situation.hasClass("selected-author_confirm")){
				pub.sitations.push($situation.attr("value"));
			}
		});
		pub.pubType = $this.find("select[name='pubType']").val();
		pub.repeatSelect = $this.find("[name='repeatSelect']").last().val();
	    pub.pubPermission = $this.find("[name='pubPermission']").val();
	    pub.des3PubFileId = $("#des3PubFileId").val();
	    pub.dupPubId = $this.find("[name='dupPubId']").val();
		pubList.push(pub);
	});
	var importSaveVo = {};
	importSaveVo.cacheKey = $("#cacheKey").val();
	importSaveVo.changPubList = pubList;
	return importSaveVo;
};
function savePub(obj){
  $("#import_status").val("false");
  $("#loadingImage1").attr("src", "/resmod/smate-pc/img/upload.gif");//显示导入时的加载中图片
  BaseUtils.doHitMore(obj,10000);
  setTimeout(function () { 
    $.ajax({
      url:"/pub/file/save",
      type:"post",
      data:{"saveJson":JSON.stringify(getJson())},
      dataType:"html",
      async:false,
      success:function(data){
        $("#loadingImage1").attr("src", "");//显示导入时的加载中图片
        $("#loadingImage1").hide();
        $("#import_status").val("true");
        $("#resultMsg").html(data);
        show();//collect_imp_fileResult.jsp
      },
      error:function(e){
        $("#loadingImage1").attr("src", "");
        $("#loadingImage1").hide()
        $("#import_status").val("true");
        //$("#fileimportpub").hide();
      }
    });
  }, 10);
};
function back(){
  if($("#import_status").val() == "true"){
    window.open("/pub/file/importenter","_self");
  }
};
function viewHistory(){
    location.href="/psnweb/homepage/show?module=pub&sortBy=updateDate";
};
function continueImpFile(){
    window.open("/pub/file/importenter","_self");
};

function checkPubTypeDup(pubType,index){
  $.ajax({
    url: "/pub/dup/checkpubtype2",
    type: "post",
    data: {"cacheKey":$("#cacheKey").val(),"pubType":pubType,"index":index},
    dataType:"json",
    timeout: 10000,
    success: function(data){
      if(data.result == "error"){
        scmpublictoast("查重异常", 1000);
      } else if(data.result == "no_dup"){
        $("#dupPubId").val("");
        $(".dup_pub_div1").eq(index - 1).css("display", "none");
        $(".dup_pub_div2").eq(index - 1).css("display", "flex");
        $(".dup_pub_div2").eq(index - 1).after('<input type="hidden" name="repeatSelect" id="repeatSelect" value="2" />');
      } else {
        debugger;
        $("#dupPubId").val(data.result);
        $(".dup_pub_div1").eq(index - 1).css("display", "flex");
        $(".dup_pub_div2").eq(index - 1).css("display", "none");
        $("#repeatSelect").remove();
      }
    },
    error: function(data){}
  });
};


</script>
</head>
<body>
  <form id="listForm" action="" method="post">
    <input type="hidden" id="import_status" name="import_status" value="true" />
    <input type="hidden" id="publicationArticleType" name="publicationArticleType" value="${publicationArticleType}" />
    <input type="hidden" id="des3PubFileId" name="des3PubFileId" value="${importfileVo.des3PubFileId}" />
    <div id="content" style="width: 1200px; margin: 56px auto;">
      <div class="Federal-retrieval_header">
        <div class="Federal-retrieval_header-item">
          <div class="Federal-retrieval_header-avator Federal-retrieval_header-item_fifth">
            <span class="Federal-retrieval_header-avator_infor">1</span>
          </div>
          <span class="Federal-retrieval_header-line3"></span>
          <div class="Federal-retrieval_header-avator Federal-retrieval_header-item_first">
            <span class="Federal-retrieval_header-avator_infor">2</span>
          </div>
          <span class="Federal-retrieval_header-line"></span>
          <div class="Federal-retrieval_header-avator Federal-retrieval_header-item_third">
            <span class="Federal-retrieval_header-avator_infor">3</span>
          </div>
        </div>
        <div class="Federal-retrieval_header-tip_box">
          <span class="Federal-retrieval_header-item_detail" style="color: #7eb4e8;"><spring:message
              code="pub.fileimport.stupselectfile" /></span> <span class="Federal-retrieval_header-item_detail"
            style="color: #2882d8;"><spring:message code="pub.fileimport.stupfiletype" /></span> <span
            class="Federal-retrieval_header-item_detail"><spring:message code="pub.fileimport.stupfilesave" /></span>
        </div>
      </div>
      <div class="new-importresult_container-header">
        <span class="new-importresult_container-header_title"><spring:message code="pub.file.publist" /></span>
      </div>
      <div class="new-importresult_container-neck">
        <spring:message code="pub.file.msgselect" />
        <span class="new-importresult_container-neck_tip"></span>
        <spring:message code="pub.file.notyour" />
      </div>
       <div style="display: flex; align-items: center; margin: 16px 0px;">
      </div> 
      <div class="new-importresult_container-body" style="margin-top: 0px; width: 1200px;">
        <div class="new-importresult_container-body_header">
          <div class="new-importresult_container-body_header-chioce">
            <spring:message code="pub.file.number" />
          </div>
          <div class="new-importresult_container-body_header-infor">
            <spring:message code="pub.file.author" />
          </div>
          <div class="new-importresult_container-body_header-Collection">
            <spring:message code="pub.file.situation" />
          </div>
          <div class="new-importresult_container-body_header-style">
            <spring:message code="pub.file.type" />
          </div>
           <c:if test="${empty importVo.des3GroupId }">
              <div class="new-importresult_container-body_header-record 2">
                   <spring:message code="pub.file.repeat" />
              </div>
           </c:if>
        </div>
        <div class="new-importresult_container-body_item-box">
          <c:forEach items="${importfileVo.pubList }" var="pub">
            <div
              class="new-importresult_container-body_item <c:if test='${pub.authorMatch ==1 }'>new-importresult_container-body_item-background</c:if>">
              <div class="new-importresult_container-body_item-chioce" style="width: 5%;">
                <div name="seqNo" value="${pub.seqNo }">${pub.seqNo }</div>
              </div>
              <div class="new-importresult_container-body_item-infor" style="width: 43%;">
                <div class="new-importresult_container-body_item-infor_title"
                  style="max-height: 40px; overflow: hidden;" title="${pub.title}">
                  <div class="multipleline-ellipsis__content-box" style="pointer-events: none;">${pub.title}</div>
                </div>
                <div class="new-importresult_container-body_item-infor_author" title="${pub.authorNames}">
                  ${pub.authorNames}</div>
                <div class="new-importresult_container-body_item-infor_time" title="${pub.briefDesc}">
                  ${pub.briefDesc}</div>
              </div>
              <div class="new-importresult_container-body_item-style" style=" width: 14%;">
                <div class="new-importresult_container-body_item-style_item">
                  <i name="situation"
                    class='selected-author <c:if test="${pub.EIIncluded}"> oldSituation selected-author_confirm</c:if>'
                    value="EI"></i><span>EI</span>
                </div>
                <div class="new-importresult_container-body_item-style_item">
                  <i name="situation"
                    class='selected-author <c:if test="${pub.SCIEIncluded}"> oldSituation selected-author_confirm</c:if>'
                    value="SCIE"></i><span>SCIE</span>
                </div>
                <div class="new-importresult_container-body_item-style_item">
                  <i name="situation"
                    class='selected-author <c:if test="${pub.ISTPIncluded}"> oldSituation selected-author_confirm</c:if>' 
                    value="ISTP"></i><span>ISTP</span>
                </div>
                <div class="new-importresult_container-body_item-style_item">
                  <i name="situation"
                    class='selected-author <c:if test="${pub.SSCIIncluded}"> oldSituation selected-author_confirm</c:if>'
                    value="SSCI"></i><span>SSCI</span>
                </div>
              </div>
              <div class="new-importresult_container-body_item-Collection" style=" width: 16%;">
                <select name="pubType" onchange="checkPubTypeDup(this.value,'${pub.index}');" >
                  <c:forEach items="${importfileVo.pubTypeList }" var="pubType">
                    <option value="${pubType.id}" <c:if test="${pubType.id == pub.pubType}">selected</c:if>>${pubType.name }</option>
                  </c:forEach>
                </select>
              </div>
              <input type="hidden" name="dupPubId" id="dupPubId" value='<iris:des3 code="${pub.dupPubId}"/>' />
              <div class="new-importresult_container-body_item-record 4  dup_pub_div1" id="dupPubDiv1" 
                style='display:
                    <c:if test="${not empty pub.dupPubId}">flex</c:if>
                    <c:if test="${empty pub.dupPubId}">none</c:if>; width: 21%;'>
                  <div class="new-importresult_container-body_item-record_tip">
                    <spring:message code="pub.file.repeat1" />
                  </div>
                  <div class="new-importresult_container-body_item-record_chioce">
                    <select name="repeatSelect" onchange="FileImport.changeRepeatSelect(this)">
                      <option selected="selected" value="1"><spring:message code="pub.file.update" /></option>
                      <option value="2"><spring:message code="pub.file.add" /></option>
                      <option value="0"><spring:message code="pub.file.skinp" /></option>
                    </select>
                  </div>
                  <div class="new-importresult_container-body_item-record_detail repeatSelectMsg">
                    <spring:message code="pub.file.repeat2" />
                  </div>
                </div>
              <div class="new-importresult_container-body_item-record 4 dup_pub_div2" id="dupPubDiv2" style='display:
                    <c:if test="${not empty pub.dupPubId}">none</c:if>
                    <c:if test="${empty pub.dupPubId}">flex</c:if>; width: 21%;' ></div>
              <c:if test="${empty pub.dupPubId}">
                <input type="hidden" name="repeatSelect" id="repeatSelect" value="2" />
              </c:if>
            </div>
          </c:forEach>
        </div>
      </div>
      <div class="new-importresult_container-footer">
        <div class="import_Achieve-footer_loading" style="margin-right: 72px;">
            <img id="loadingImage1">
        </div>
        <div class="new-importresult_container-footer_close" onclick="back();">
          <spring:message code="pub.file.back" />
        </div>
        <div class="new-importresult_container-footer_save" onclick="savePub(this);">
          <spring:message code="pub.file.save" />
        </div>
      </div>
    </div>
    <input type="hidden" name="cacheKey" id="cacheKey" value="${importfileVo.cacheKey }" /> <input type="hidden"
      name="saveJson" id="saveJson" value="" />
  </form>
  <div class="background-cover" id="resultMsg" style="display: none;"></div>
    <!-- 导入成果时正在加载数据的弹框提示 begin-->
  <div class="background-cover" style="display: none;" id="fileimportpub">
    <div class="new-getdata_tip-container">
      <img src="${resmod }/smate-pc/img/upload.gif" class="new-getdata_tip-container_avator">
      <div class="new-getdata_tip-container_content" id="get_data_from_content" style="text-align: center;color:red;"><spring:message code="pub.file.importing" /></div>
    </div>
  </div>
  
</body>
</html>
