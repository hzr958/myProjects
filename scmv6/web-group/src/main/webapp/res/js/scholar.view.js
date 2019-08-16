ScholarView = function() {
}
ScholarView.getWebContextPath = function() {
  return snsctx;
}
ScholarView.gotoEdit = function(tab, pubId) {
  if (!tab || tab == null){tab = 0;}
  if (window.opener) {
    var url = ScholarView.getWebContextPath() + "/publication.do?action=preModify&pub_Id=" + pubId + "&tab=" + tab
        + "&pubfly=1";
    window.opener.target = "_parent";
    window.opener.location.href = url;
    window.opener.focus();
    self.close();
  } else {
    var url = ScholarView.getWebContextPath() + "/publication.do?action=preModify&pub_Id=" + pubId + "&tab=" + tab
        + "&pubfly=1";
    window.top.location.href = url;
  }
};

ScholarView.viewPubDetail = function(des3Id, obj, Id, params, count) {
  if (obj != null) {
    var params = "des3Id=" + encodeURIComponent(des3Id);
    if (typeof $(obj).parent().parent().parent().find(".pub_nodeId_class").val() != 'undefined') {
      var nodeId = $(obj).parent().parent().parent().find(".pub_nodeId_class").val();
      if (nodeId != "") {
        params += "&nodeId=" + $(obj).parent().parent().parent().find(".pub_nodeId_class").val();
      }
    }
    if ('undefined' != typeof $(obj).parent().parent().parent().find(".pub_groupId_class").val()) {
      params += "&des3GroupId=" + $(obj).parent().parent().parent().find(".pub_groupId_class").val();
    }
    if ('undefined' != typeof $(obj).parent().parent().parent().find(".pub_ownerId_class").val()) {
      params += "&des3OwnerId=" + $(obj).parent().parent().parent().find(".pub_ownerId_class").val();
    }
    if ('undefined' != typeof ($("#snsNodeId").val()) && $.trim($("#snsNodeId").val()).length > 0) {

      params += "&snsNodeId=" + $("#snsNodeId").val();

    }
    var des3ResRecId = $(obj).parent().parent().parent().find(".des3ResRecId").val();
    if (typeof des3ResRecId != "undefined" && des3ResRecId != "") {
      params += "&des3ResRecId=" + encodeURIComponent(des3ResRecId);
    } else {
      var des3ResSendId = $(obj).parent().parent().parent().find(".des3ResSendId").val();;
      if (typeof des3ResSendId != "undefined" && des3ResSendId != "") {
        params += "&des3ResSendId=" + encodeURIComponent(des3ResSendId);
      }
    }
  } else {
    var params = params;

  }

  // tj修改成果详情出现旋转图片
  var pram = {
    pubId : Id,
    des3Id : des3Id
  };
  var over = "coll_" + Id;
  var newwindow = window.open("about:blank");
  $.ajax({
    url : "/pubweb/publication/ajaxview",
    type : "post",
    data : pram,
    dataType : "json",
    success : function(data) {
      if (data.result == 2) {
        newwindow.location.href = data.shortUrl;
        newwiondow.focus();
      }
      if (data.result == 1 || data.ajaxSessionTimeOut == 'yes') {
        // ScholarView.forceOpen(ScholarView.getWebContextPath()+"/publication/view?"+params);
        newwindow.location.href = "/pubweb/outside/details?" + params + "&currentDomain=" + "/pubweb" + "&pubFlag=1";
        newwindow.focus();
      } else if (data.result == 0) {
        if (count == null) {
          // window.location.href="/groupweb/grouppub/view?"+params+"&gpubId="+Id;
          // var newwindow = window.open("/groupweb/grouppub/view?"+params+"&gpubId="+Id);
          // newwindow.focus();
          newwindow.location.href = "/pubweb/publication/wait?" + params;
          newwindow.focus();
        }
      }

    },
    error : function() {

    }
  });

  // $.Event.stopEvent();
};

ScholarView.viewPdwhPubDetail = function(des3Id, dbid) {
  var params = "des3Id=" + encodeURIComponent(des3Id);
  ScholarView.forceOpen("/pubweb/outside/pdwhdetails?" + params + "&dbid=" + dbid);
};

ScholarView.viewPubSimple = function(des3Id, obj) {
  var params = "des3Id=" + encodeURIComponent(des3Id);
  if (typeof $(obj).parent().parent().parent().find(".pub_nodeId_class").val() != 'undefined') {
    params += "&nodeId=" + $(obj).parent().parent().parent().find(".pub_nodeId_class").val();
  }
  if ('undefined' != typeof $(obj).parent().parent().parent().find(".pub_groupId_class").val()) {
    params += "&groupId=" + $(obj).parent().parent().parent().find(".pub_groupId_class").val();
  }
  if ('undefined' != typeof $(obj).parent().parent().parent().find(".pub_ownerId_class").val()) {
    params += "&ownerId=" + $(obj).parent().parent().parent().find(".pub_ownerId_class").val();
  }
  if ('undefined' != typeof ($("#snsNodeId").val()) && $.trim($("#snsNodeId").val()).length > 0) {

    params += "&snsNodeId=" + $("#snsNodeId").val();
  }
  ScholarView.forceOpen(ScholarView.getWebContextPath() + "/publication/viewSimple?" + params);
};

ScholarView.openCitesUrl = function(pubId, obj)

{
  var params = "des3Id=" + encodeURIComponent(pubId);
  if (typeof $(obj).parent().parent().find(".pub_nodeId_class").val() != 'undefined') {
    params += "&nodeId=" + $(obj).parent().parent().parent().find(".pub_nodeId_class").val();
  }
  if ('undefined' != typeof $(obj).parent().parent().parent().find(".pub_groupId_class").val()) {
    params += "&groupId=" + $(obj).parent().parent().parent().find(".pub_groupId_class").val();
  }
  $.ajax({
    url : ScholarView.getWebContextPath() + "/puburl/ajaxPubCitedUrl",
    type : "POST",
    data : params,
    dataType : "json",
    success : function(data) {
      if (data) {
        var openUrl;
        if (data.result == "success") {
          var win = window.open(data.viewUrl);
        } else {
          alert(scholar_view.error);
        }
      }
    },
    error : function() {

    }
  });

};

ScholarView.openRolCitesUrl = function(pubId, pubNodeId) {
  var url = ScholarView.getWebContextPath() + "/puburl/cites?des3Id=" + pubId + "," + pubNodeId;
  var win = window.open(url);
  if (win.focus) {
    win.focus();
  }
}

ScholarView.openSourceUrl = function(pubId, pubNodeId) {
  var url = ScholarView.getWebContextPath() + "/puburl/source?des3Id=" + pubId + "," + pubNodeId;
  var win = window.open(url);
  if (win.focus) {
    win.focus();
  }
};

ScholarView.openPdwhSourceUrl = function(pubId, dbid) {
  var url = ScholarView.getWebContextPath() + "/puburl/source?des3Id=" + pubId + "&dbid=" + dbid;
  var win = window.open(url);
  if (win.focus) {
    win.focus();
  }
};

ScholarView.openFullTextUrl = function(pubId, obj) {
  var params = "des3Id=" + encodeURIComponent(pubId);
  if (typeof $(obj).parent().find(".pub_nodeId_class").val() != 'undefined') {
    params += "&nodeId=" + $(obj).parent().parent().parent().find(".pub_nodeId_class").val();
  }
  if ('undefined' != typeof $(obj).parent().parent().parent().find(".pub_groupId_class").val()) {
    params += "&groupId=" + $(obj).parent().parent().parent().find(".pub_groupId_class").val();
  }
  $.ajax({
    url : ScholarView.getWebContextPath() + "/puburl/ajaxPubFulltextUrl",
    type : "POST",
    data : params,
    dataType : "json",
    success : function(data) {
      if (data) {
        if (data.result == "success") {
          window.open(data.viewUrl);
        } else {
          jAlert(scholar_view.error, "");
        }
      }
    },
    error : function() {

    }
  });
  // var url = ScholarView.getWebContextPath()+"/puburl/fulltexturl.action?pubId="+pubId;
};

ScholarView.viewFullText = function(pubId) {
  var url = ScholarView.getWebContextPath() + "/puburl/fulltext?des3Id=" + pubId;
  var win = window.open(url);
  if (win.focus) {
    win.focus();
  }
};

ScholarView.viewInsPubDetail = function(des3Id) {

  var url = ScholarView.getWebContextPath() + "/publication/view?des3Id=" + des3Id;
  var win = window.open(url);
}

ScholarView.viewInsRefDetail = function(des3Id, scmDoaim) {

  var url = scmDoaim + "/publication/view?des3Id=" + des3Id;
  var win = window.open(url);
}

ScholarView.openInsCitesUrl = function(pubId) {
  var url = ScholarView.getWebContextPath() + "/puburl/cites?des3Id=" + pubId;
  var win = window.open(url);
}
ScholarView.openInsSourceUrl = function(pubId) {
  var url = ScholarView.getWebContextPath() + "/puburl/source?des3Id=" + pubId;
  var win = window.open(url);
}
ScholarView.openInsFullTextUrl = function(pubId, nodeId) {
  var url = ScholarView.getWebContextPath() + "/puburl/fulltexturl?des3Id=" + pubId + "&nodeId=" + nodeId;
  var win = window.open(url);
}
ScholarView.openPdwhFullTextUrl = function(pubId, dbid) {
  var url = ScholarView.getWebContextPath() + "/puburl/fulltexturl?des3Id=" + pubId + "&dbid=" + dbid;
  var win = window.open(url);
}

ScholarView.openInsRefFullTextUrl = function(refId, scmDoaim) {
  var url = scmDoaim + "/puburl/fulltexturl?des3Id=" + refId;
  var win = window.open(url);
}
ScholarView.viewInsFullText = function(pubId) {
  var url = ScholarView.getWebContextPath() + "/puburl/fulltext?des3Id=" + pubId;
  var win = window.open(url);
}

resetALinkOpenNewWin = function(tc) {
  if ($(tc)) {
    $("a", $(tc)).attr('target', "_blank");
  }
};
ScholarView.forceOpen = function(url) {
  var a = document.getElementById("open_linkxx");
  if (a == null || typeof (a) == "undefined") {
    a = document.createElement("a");
    a.setAttribute("target", "_blank");
    a.setAttribute("href", url);
    a.setAttribute("id", "open_linkxx");
    a.setAttribute("name", "open_linkxx");
    document.body.insertBefore(a, document.body.childNodes[0]);
  } else {
    a.setAttribute("href", url);
  }
  if (document.all) {
    a.click();
  } else {
    var evt = document.createEvent("MouseEvents");
    evt.initEvent("click", true, true);
    a.dispatchEvent(evt);
  }
};

ScholarView.viewTmpSrcUrl = function(tmpUrl) {
  var url = ScholarView.getWebContextPath() + "/publication/viewTmpUrl?tmpUrl=" + encodeURIComponent(tmpUrl);
  var win = window.open(url);
  if (win.focus) {
    win.focus();
  }
};

ScholarView.checkPubsStatus = function(postData, fnc) {
  $.ajax({
    url : "/pubweb/publication/ajaxCheckPubsStatus",
    type : "post",
    data : postData,
    dataType : "json",
    success : function(data) {
      if (data.result == 0) {
        $.scmtips.show('warn', data.msg);
      } else if (data.result == 1) {
        fnc();
      }
    },
    error : function() {
      $.scmtips.show('error', 'error');
    }
  });
};