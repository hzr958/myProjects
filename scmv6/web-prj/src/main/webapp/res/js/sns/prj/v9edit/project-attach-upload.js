function ajaxFileUploadAttach() {
  $.ajaxFileUpload({
    url : "/fileweb/fileupload",
    secureuri : false,
    timeout : 10000,
    fileElementId : 'filedata1',
    data : {
      /*
       * 'jsessionId' : jsessionId,
       */
      'nodeId' : currentNodeId,
      "fileDealType" : "generalfile"
    },
    dataType : 'json',
    success : function(data, status) {
      $('#loading1').hide();
      if (data.result == 'error') {
        $.scmtips.show("warn", data.msg);
        /*
         * $("#filedata1").change(function() { showAttachFileName(); });
         */
        $('#file_attach').attr('value', '');
      } else {
        $('#file_attach').attr('value', '');
        pubAttachAddNew(data);
      }
    },
    error : function(data, status, e) {
      $('#loading1').hide();
      $('#file_attach').attr('value', '');
    }
  });
}

function showAttachFileName() {
  var filedata = $('#filedata1').val();
  if (filedata == '') {
    return;
  } else {
    $('#file_attach').attr('value', $('#filedata1').val());
    confirmAttachUpload();
  }
}

function confirmAttachUpload() {
  BaseUtils.checkCurrentSysTimeout("/prjweb/ajaxtimeout", function() {
    if ($.trim($("#filedata1").val()) == "") {
      var msg = $.fn.formateMsg(i18n_prj["publicationEdit.label.fulltextfile.nofile"]);
      $.scmtips.show("warn", msg);
      return false;
    }
    $('#loading1').show();
    ajaxFileUploadAttach();
  }, 0);
}

function pubAttachAddNew(result) {
  ProjectAttachment.addNewLine();
  var length =   $("#projectAttachmentList").find(".prj_attachment_dev").length;
  var num = $.fn.setNumberLen(length, 2);
  var fileName = common.unescapeHTML(result.fileName);
  var fileExt = fileName.substring(fileName.lastIndexOf("."));

  $("#_prj_attachments_prj_attachment_" + num + "_file_name").attr("value", fileName);
  $("#_prj_attachments_prj_attachment_" + num + "_file_id").attr("value", result.fileId);
  $("#_prj_attachments_prj_attachment_" + num + "_upload_date").attr("value", result.createTime);
  $("#_prj_attachments_prj_attachment_" + num + "_file_ext").attr("value", fileExt);
  $("#_prj_attachments_prj_attachment_" + num + "_upload_date_label").attr("innerHTML", result.createTime);
    var des3fid = result.successDes3FileIds.split(",")[0];
    var  downloadUrl = "/scmwebsns/archiveFiles/downLoadNoVer.action?fdesId=";
    downloadUrl = downloadUrl + des3fid + "&nodeId=" + currentNodeId ;
    $("#_prj_attachments_prj_attachment_" + num + "_download_link").attr("onclick", "BaseUtils.fileDown('" + downloadUrl + "',this,event)");
}
