function ajaxFileUploadFullText() {
  var file_desc = $("#_fulltext_fileupload_desc").val() == i18n_prj["application.file.floatDiv.fileDescSize"] ? "" : $(
      "#_fulltext_fileupload_desc").val();
  $.ajaxFileUpload({
    url : "/fileweb/fileupload",
    secureuri : false,
    timeout : 10000,
    fileElementId : 'filedata',
    data : {
      'jsessionId' : jsessionId,
      'nodeId' : currentNodeId,
      "fileDesc" : file_desc,
      "fileDealType" : "generalfile"
    },
    dataType : 'json',
    success : function(data, status) {
      $('.filedata').val("");
      $("#btnUpload2").attr("disabled", false);
      $('#loading').hide();
      if (data.result == 'error') {
        $.scmtips.show("warn", data.msg);
        $("input[id=filedata]").change(function() {
          if ($(this).val().length > 0) {
            $('.filedata').val($(this).val().substring($(this).val().replace(/\\/g, '/').lastIndexOf('/') + 1));
            $('.filedata').css("color", "#000");
            $("#btnUpload2").attr("disabled", false);
          }
        });
      } else {
        $("#_prj_fulltext_file_id").attr("value", data.fileId);
        var fileName = common.unescapeHTML(data.fileName);
        var fileExt = fileName.substring(fileName.lastIndexOf("."));
        var fileDesc = unescape(data.fileDesc);
        var extendFileInfo = data.extendFileInfo[0];
        var downloadUrl = extendFileInfo.downloadUrl;
        // fileName = fileName.substring(0,fileName.lastIndexOf("."));
        $("#_prj_fulltext_file_name").attr("value", fileName);
        $("#_prj_fulltext_file_desc").attr("value", common.unescapeHTML($.trim(fileDesc)));
        $("#_prj_fulltext_file_ext").attr("value", fileExt);
        $("#_prj_fulltext_upload_date").attr("value", data.createTime);
        var des3fid = data.successDes3FileIds.split(",")[0];
        fileLink = fileLink.replace("&pubId=", "");
        // $("#fulltext_downlink").attr("href", fileLink + des3fid + "&nodeId=" + currentNodeId);
        $("#fulltext_downlink").removeAttr("onclick");
        $("#fulltext_downlink").attr("onclick", "BaseUtils.fileDown('" + downloadUrl + "',this,event)");
        // $("#span_prj_fulltext_upload_date").attr("innerHTML",data.createTime);//???
        $("#span_pub_fulltext_upload_date").text(data.createTime);
        $("#_prj_fulltext_node_id").val($("#_prj_fulltext_current_node_id").val());
        $("#_prj_fulltext_ins_id").val($("#_prj_fulltext_current_ins_id").val());
        fullTextUploadView(false);
        $("#divProgressUpFullTextFile").css("display", "none");
        $("#fullTextUploadMsg").css("display", "");
        $("#_fulltext_fileupload_desc").attr("value", "");
        $("#filedata,.filedata").change(function() {
          if ($(this).val().length > 0) {
            $('.filedata').val($(this).val().substring($(this).val().replace(/\\/g, '/').lastIndexOf('/') + 1));
            $("#btnUpload2").attr("disabled", false);
          }
        });
      }
    },
    error : function(data, status, e) {
      $("#btnUpload2").attr("disabled", false);
      $('#loading').hide();
      $('#file_fulltext').attr('value', '');
      $.scmtips.show("error", status);
    }
  });
}

function fullTextUploadView(flag) {
  if (flag) {
    $("#divDescFullTextFile").css("display", "");
    $("#divUploadUpFullTextFile").css("display", "");
    $("#divFullTextTipsMsg").css("display", "");
    $('.filedata').watermark({
      tipCon : i18n_prj["application.file.upload.maxSize"]
    });
    $('#_fulltext_fileupload_desc').watermark({
      tipCon : i18n_prj["application.file.floatDiv.fileDescSize"]
    });
  } else {
    $("#divDescFullTextFile").css("display", "none");
    $("#divUploadUpFullTextFile").css("display", "none");
    $("#divFullTextTipsMsg").css("display", "none");
  }
}

// 移除全文附件
function removeFullTextFile() {
  $('#file_fulltext').attr('value', '');
  $("#_prj_fulltext_file_id").attr("value", "");
  $("#_prj_fulltext_file_name").attr("value", "");
  $("#_prj_fulltext_file_desc").attr("value", "");
  $("#_prj_fulltext_file_ext").attr("value", "");
  $("#_prj_fulltext_node_id").val($("#_prj_fulltext_current_node_id").val());
  $("#_prj_fulltext_ins_id").val($("#_prj_fulltext_current_ins_id").val());

  $("#_prj_fulltext_upload_date").attr("value", "");
  $("#fulltext_downlink").attr("href", "#");
  // $("#span_prj_fulltext_upload_date").attr("innerHTML","");
  $("#span_prj_fulltext_upload_date").text("");
  fullTextUploadView(true);
  $("#divProgressUpFullTextFile").css("display", "");
  $("#fullTextUploadMsg").css("display", "none");
}

function confirmFullTextUpload() {
  BaseUtils.checkCurrentSysTimeout("/prjweb/ajaxtimeout", function() {
    if ($.trim($("#filedata").val()) == "") {
      var msg = $.fn.formateMsg(i18n_prj["publicationEdit.label.fulltextfile.nofile"]);
      $.scmtips.show("warn", msg);
      $("#btnUpload2").attr("disabled", false);
      return false;
    }
    $('#loading').show();
    ajaxFileUploadFullText();
  }, 0);
}

function replaceFulltextWithMyFile() {
  BaseUtils.checkCurrentSysTimeout("/prjweb/ajaxtimeout", function() {
    $("#replace_fulltext_with_myfile_link").click();
  }, 0);
}