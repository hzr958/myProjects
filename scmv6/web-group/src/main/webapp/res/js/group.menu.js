/**
 * 我的群组-群组菜单页面的JS事件.
 * 
 * @author MJG
 * @since 2012-08-15
 */
var Group = Group ? Group : {};
Group.menu = Group.menu ? Group.menu : {};
/**
 * 群组菜单跳转.
 */
Group.menu.jump = function(des3GroupId, groupNodeId, type) {
  $("#des3GroupId").val(des3GroupId);
  $("#groupNodeId").val(groupNodeId);
  $("#searchName").val("");// 清除菜单定位参数
  $("#searchId").val("");// 清除菜单定位参数
  $("#searchKey").val("");// 清除搜索条件
  $("#leftMenuId").val("");// 清除文件夹定位
  $("#orderBy").val("");// 清除排序
  $("#pageNo").val("");// 清除页码
  // 群组成果页面，左边菜单栏中的邀请人员链接
  var isInvite = $("#isInvite").val();
  var des3GroupNodeId = $("#des3GroupNodeId").val();
  var action;
  if (type == 'main') {// 返回群组列表.
    action = snsctx + "/group/main?menuId=31";
  } else if (type == 'pub') {// 群组成果.
    var rolInsId = $("#orolInsId").val();
    if (typeof (rolInsId) != "undefined") {
      action = "/groupweb/grouppub/show?rolInsId=" + rolInsId + "&menuId=31&des3GroupId="
          + encodeURIComponent(des3GroupId) + "&groupNodeId=" + groupNodeId;
    } else {
      action = "/groupweb/grouppub/show?menuId=31&des3GroupId=" + encodeURIComponent(des3GroupId) + "&groupNodeId="
          + groupNodeId;
    }
    window.location.href = action;
    return;
  } else if (type == 'ref') {// 群组文献.
    action = snsctx + "/group/ref?menuId=31";
  } else if (type == 'prj') {// 群组项目.
    action = snsctx + "/group/prj?menuId=31";
  } else if (type == 'file') {// 群组文件.
    action = snsctx + "/group/file?menuId=31";
  } else if (type == 'work') {// 群组作业.
    action = snsctx + "/group/work?menuId=31";
  } else if (type == 'course') {// 群组教学课件.
    action = snsctx + "/group/course?menuId=31";
  } else if (type == 'dyn') {// 群组动态.
    action = snsctx + "/group/dyn?menuId=31";
  } else if (type == 'member') {// 群组成员.
    action = snsctx + "/group/member?menuId=31&isInvite=" + isInvite;
  } else if (type == 'memberNotYet') {// 群组待批准成员.
    action = snsctx + "/group/memberNotYet?menuId=31";
  } else if (type == 'edit') {// 群组设置.
    action = snsctx + "/group/edit?menuId=31";
  } else if (type == 'invite') {// 群组成员邀请.
    action = snsctx + "/group/memberInvite?menuId=31";
  } else if (type == 'intro') {// 群组简介.
    action = snsctx + "/group/groupBriefIntro?menuId=31&des3GroupId=" + encodeURIComponent(des3GroupId)
        + "&groupNodeId=" + groupNodeId;
  } else if (type == 'page') {// 群组主页
    action = snsctx + "/group/homepage/edit?menuId=31";
  } else {
    return;
  }
  var form = $('#groupMenuForm').length > 0 ? $('#groupMenuForm') : $('#mainForm');
  form.attr("method", "post");
  form.attr("action", action);
  Group.timeout.check(function() {
    form.submit();
  });
};
// 删除群组
Group.menu.del = function(des3GroupId, groupNodeId) {

  jConfirm(delGroupCfm, reminder, function(sure) {
    if (sure) {
      $.proceeding.show();
      $.ajax({
        url : snsctx + '/group/ajaxGroupDelete',
        type : 'post',
        dataType : 'json',
        data : {
          "groupPsn.des3GroupId" : des3GroupId,
          "groupPsn.groupNodeId" : groupNodeId
        },
        success : function(data) {
          $.proceeding.hide();
          $.scmtips.show(data.result, data.msg);
          setTimeout(function() {
            window.location.href = snsctx + "/group/main";
          }, 2000);
        },
        error : function() {
          $.proceeding.hide();
          $.scmtips.show("error", Group.myGroup.opFaild);
        }
      });
    }
  });
};
/**
 * 退出群组
 */
Group.menu.leave = function(des3GroupId, groupNodeId) {
  jConfirm(leaveGroupCfm, reminder, function(sure) {
    if (sure) {
      $.proceeding.show();
      $.ajax({
        url : snsctx + '/group/ajaxGroupMemberExit',
        type : 'post',
        dataType : 'json',
        data : {
          "groupPsn.des3GroupId" : des3GroupId,
          "groupPsn.groupNodeId" : groupNodeId
        },
        success : function(data) {
          $.proceeding.hide();
          $.scmtips.show(data.result, data.msg);
          setTimeout(function() {
            window.location.href = snsctx + "/group/main";
          }, 2000);
        },
        error : function() {
          $.proceeding.hide();
          $.scmtips.show("error", Group.myGroup.opFaild);
        }
      });
    }
  });
};