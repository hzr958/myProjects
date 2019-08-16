<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link rel="stylesheet" type="text/css" href="${resmod}/css_v5/public.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css_v5/common.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css_v5/plugin/jquery.scmtips.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css_v5/pop.css" />
<script type="text/javascript" src="${resmod}/js_v5/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.scmtips.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/scm.page.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript">
var ctxpath="${ctx}";
var chooseFile="<s:text name='group.jsTip.chooseFile'/>";
var oPSuccess="<s:text name='group.succ'/>";
var opFailed="<s:text name='group.fail'/>";
 var msgTitle="<s:text name='publicationEdit.label.save.title'/>";
 var deletePubFolderMsg="<s:text name='maint.search.msg.confirm.delete.pubfolder'/>";
 var required_publication="<s:text name='maint.action.addToFolder.msg.required_publication'/>";
 var unClassicValue="<iris:des3 code='-1'/>";
</script>
</head>
<body>
  <form id="mainForm" action="/prjweb/project/ajaxLinkFile">
    <%-- <input type="hidden" name="suffix" value="${suffix}"/> --%>
    <input type="hidden" value="${des3GrouId}" name="des3GrouId" />
    <%-- <input type="hidden" value="${prjOrpub}" name="prjOrpub"/> --%>
    <div class="dialog_content2" style="padding-top: 10px;">
      <div class="file-label-list">
        <s:if test="page.totalCount > 0">
          <table class="tr_box" border="0" cellSpacing="0" cellPadding="0" width="100%">
            <thead>
              <tr>
                <td width="10%"><s:text name="common.label.selet" /></td>
                <td width="65%"><s:text name="group.grid.head.col1" /></td>
                <td><s:text name="group.grid.head.col3" /></td>
              </tr>
            </thead>
            <tbody>
              <s:iterator value="page.result" id="result" status="itStat">
                <tr>
                  <td align="center">
                    <%-- <input type="radio" name="ckfile" value="<iris:des3 code="${archiveFileId}"></iris:des3>" fname="${fileName}" fdomain="${domain}" fpath="${filePath}" fdesc="${fileDesc}"/> --%>
                    <input type="radio" name="ckfile" value="${archiveFileId}" fname="${fileName}" fdomain="${domain}"
                    fpath="${filePath}" fdesc="${fileDesc}" downloadUrl="${downloadUrl }" /> <label for="checkbox13"></label>
                  </td>
                  <td align="left"><a class="Blue" style="cursor: pointer;"
                    onclick="BaseUtils.fileDown('${downloadUrl }',this,event)">${fileName}</a></td>
                  <%-- <td align="left"><a class="Blue"  herf='${downloadUrl }' style="cursor:pointer;">${fileName}</a></td> --%>
                  <td align="left"><fmt:formatNumber value="${fileSize/1024}" minFractionDigits="2"></fmt:formatNumber>
                    KB</td>
                </tr>
              </s:iterator>
            </tbody>
            <tfoot></tfoot>
          </table>
        </s:if>
        <s:else>
          <div class="norecord" style="width: auto;">
            <div class="wrong_tips">
              <s:text name="maint.notice.label.search_result.no_record"></s:text>
            </div>
          </div>
        </s:else>
      </div>
      <%@ include file="/common/page-thickbox.jsp"%>
    </div>
    <div class="pop_buttom" style="position: absolute; bottom: 0px;">
      <%-- 	 <c:choose>
		<c:when test="${prjOrpub eq 1}">
	 	<a class="uiButton uiButtonConfirm text14" title="<s:text name="common.label.confirm1"/>" href="javascript:void(0)" onclick="updatePubFullTextFile()"><s:text name="common.label.confirm1"/></a>
	</c:when>
	<c:otherwise>
		<a class="uiButton uiButtonConfirm text14" title="<s:text name="common.label.confirm1"/>" href="javascript:void(0)" onclick="updatePrjFullTextFile()"><s:text name="common.label.confirm1"/></a>
	</c:otherwise>
	</c:choose> --%>
      <a class="uiButton uiButtonConfirm text14" title="<s:text name="common.label.confirm1"/>"
        href="javascript:void(0)" onclick="updateFullTextFile()"><s:text name="common.label.confirm1" /></a> <a
        class="uiButton text14 mright10" title="<s:text name="dyn.add.mydyn.cancel.label"/>" href="javascript:void(0)"
        onclick="parent.$.Thickbox.closeWin();"><s:text name="dyn.add.mydyn.cancel.label" /></a>
    </div>
  <form>
  <script>

function updateFullTextFile(){
	var fileDesc = $("input[type='radio']:checked").attr("fdesc");
	var filePath = $("input[type='radio']:checked").attr("fpath");
	var fileName=$("input[type='radio']:checked").attr("fname");
	var fileId = $("input[type='radio']:checked").attr("value");
	var funValue = $("input[type='radio']:checked").attr("downloadUrl");
	var date = new Date();
	var createTime = date.getFullYear()+"/"+date.getMonth()+"/"+date.getDate();
	if(fileId==undefined){
		if("${page.totalCount}">0){
			parent.$.scmtips.show("warn",parent.i18n_prj["publicationEdit.label.fulltextfile.nofile"]);
		}else{
			parent.$.Thickbox.closeWin();
		}
		return;
	}
	fileDesc=fileDesc.length>199?fileDesc.substr(0,199):fileDesc;
	var item = "pub";
	if (parent.$('#mainform').prop("action").indexOf("prjweb") > 0) {
		item = "prj";
	}
	parent.$("#_"+item+"_fulltext_file_id").attr("value",fileId);
	var fileExt = fileName.substring(fileName.lastIndexOf("."));
	parent.$("#_"+item+"_fulltext_file_name").attr("value",fileName);
	parent.$("#_"+item+"_fulltext_file_desc").attr("value",fileDesc);
	parent.$("#_"+item+"_fulltext_file_ext").attr("value",fileExt);
	parent.$("#_"+item+"_fulltext_upload_date").attr("value",createTime);
	//parent.$("#fulltext_downlink").attr("href","/archiveFiles/fileDownload.action?fdesId="+data.des3FileId+"&nodeId="+data.nodeId);
	parent.$("#fulltext_downlink").removeAttr("onclick");
	parent.$("#fulltext_downlink").attr("onclick","BaseUtils.fileDown('"+funValue+"',this,event)");
	parent.$("#span_"+item+"_fulltext_upload_date").attr("innerHTML",createTime);
	parent.$("#_"+item+"_fulltext_node_id").val(parent.$("#_"+item+"_fulltext_current_node_id").val());
	parent.$("#_"+item+"_fulltext_ins_id").val(parent.$("#_"+item+"_fulltext_current_ins_id").val());
	if(parent.$("#_"+item+"_fulltext_file_name")){
		parent.fullTextUploadView(false);
		parent.$("#fullTextUploadMsg").css("display","");
		parent.$("#_fulltext_fileupload_desc").attr("value","");
	}	
	parent.$.Thickbox.closeWin();
}

function openFile(fileUrl, obj, e) {
	if(obj){
		preventMultiClick(obj,1000);
	}
	if(e){
		stopNextEvent(e);
	}
	$.ajax({
		url: "/dynweb/ajaxtimeout",
		dataType:"html",
		type:"post",
		data: {
		},
		success: function(data){
				if(fileUrl!=null&&fileUrl!=""){
					window.location.href = fileUrl;
				}
		}
	});
}

function preventMultiClick(obj, time){

	if(time==null){
		time=1000;
	}
	$(obj).css('pointer-events','none');
	//$(obj).attr("disabled",true);
	var click = $(obj).attr("onclick");
	if(click!=null&&click!=""){
		$(obj).attr("onclick","");
	}
	setTimeout(function(){
		$(obj).css('pointer-events','auto');
		//$(obj).removeAttr("disabled");
		if(click!=null&&click!=""){
			$(obj).attr("onclick",click);
		}
	},time);
}

function stopNextEvent(evt){
	if(evt.currentTarget){
		if(evt.stopPropagation){
			evt.stopPropagation();
		}else{
			evt.cancelBubble=true;
		}
	}
};

</script>
</body>
</html>
