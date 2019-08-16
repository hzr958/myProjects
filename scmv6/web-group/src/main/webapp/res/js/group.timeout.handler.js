var Group = Group ? Group : {};

Group.timeout = Group.timeout ? Group.timeout : {};

Group.timeout.alert = function() {
  // 超时
  jConfirm(i18n_timeout, i18n_tipTitle, function(r) {
    if (r) {
      var url = top.location.href;
      url = url.replace('#', '');
      if (url.indexOf('des3GroupId') >= 0
          && (url.indexOf('nodeId') >= 0 || url.indexOf('des3NodeId') >= 0 || url.indexOf('groupNodeId') >= 0)) {
        if (url.indexOf('menuId') < 0) {
          top.location.href = url + "&menuId=31";
        } else {
          document.location.href = url;
        }
      } else {
        document.location.href = url + "?groupPsn.des3GroupId=" + encodeURIComponent($("#des3GroupId").val())
            + "&groupPsn.groupNodeId=" + encodeURIComponent($("#groupNodeId").val()) + "&menuId=31";
      }
    }
  });
};
Group.timeout.alert2 = function() {
  // 超时
  jConfirm(i18n_timeout, i18n_tipTitle, function(r) {
    if (r) {
      var url = document.location.href;
      url = url.replace('#', '');
      document.location.href = url;
    }
  });
};
/** 检查登录是否超时 */
Group.timeout.check = function(callback) {
  Group.timeout.checkCore(callback, Group.timeout.alert);
};
Group.timeout.check2 = function(callback) {
  Group.timeout.checkCore(callback, Group.timeout.alert2);
};
Group.timeout.checkCore = function(callback1, callback2) {
  $.ajax({
    url : snsctx + '/group/ajaxCheckTimeoutStatus',
    type : 'get',
    dataType : 'json',
    success : function(data) {
      if (data.result == 'success') {
        if (callback1) {
          callback1();
        }
      } else {
        if (callback2) {
          callback2();
        }
      }
    },
    error : function() {
      $.scmtips.show('error', 'error');
    }
  });
};