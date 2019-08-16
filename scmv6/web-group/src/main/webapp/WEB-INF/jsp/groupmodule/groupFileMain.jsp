<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${ressns}/js/group/groupDetailFiles_${locale}.js"></script>
<%-- <script type="text/javascript" src="${resscmsns}/js_v5/plugin/jquery.proceeding.js"></script>--%>
<title>群组文件</title>
<script type="text/javascript">
//文件描述输入框水印字符
var fileDescMaxSize = "<s:text name='group.detailsFiles.fileDescMaxSize'/>";
var currentPsnGroupRoleStatus='${currentPsnGroupRoleStatus}';
	$(document).ready(function() {
		if(currentPsnGroupRoleStatus=='2' || currentPsnGroupRoleStatus=='3' || currentPsnGroupRoleStatus=='4'){
			Group.myFileList();
		}
		Group.groupFileList();
		Group.groupFileListening();
		$("#doUploadFile_btn").thickbox({
			//'closeEvent':targetModule
		});
		$("#showFileEdit").thickbox({
		});
		Group.editGroupFileWatermark();
	  });
	function openFile(fileId, nodeId, type) {
// 		window.open(snsctx + "/file/download?des3Id=" + fileId + "&nodeId="+ nodeId + "&type=" + type);
		window.location.href = snsctx + "/file/download?des3Id=" + fileId + "&nodeId=" + nodeId + "&type=" + type;
	};
	function targetModule(){
		Group.selectTargetModule('file');
	}
	function findDes3GroupId(){
		return $("#des3GroupId").val();
	}
	function findNodeId(){
		return 1;
	}
	function finSumMembers(){
		return $("#sumMember").val();
	}
	function getSelectMenuId(){
		return "-1";	
	}
	function getNavAction(){
		return "groupFiles";
	}
</script>
</head>
<body>
  <div class="group_box">
    <input type="hidden" id="doUploadFile_btn" class="thickbox" />
    <div class="group_l fl">
      <div class="lt_box pa10">
        <div class="mn_wrap">
          <c:if test="${currentPsnGroupRoleStatus==2 || currentPsnGroupRoleStatus==3 || currentPsnGroupRoleStatus==4}">
            <a onclick="Group.groupUploadFile();" id="add_group_file_btn"
              class="waves-effect waves-light w86 fr button01">添加文件</a>
          </c:if>
          <div class="m_s_box fl">
            <input type="text" placeholder="查找文件" class="input_default" id="fileSearch" /> <input type="button"
              class="s_btn_small" />
          </div>
          <div class="clear"></div>
        </div>
      </div>
    </div>
    <div class="group_r"></div>
    <!-- replaceWith替换用 -->
    <div class="clear_h40"></div>
  </div>
  <s:include value="groupFileEdit.jsp" />
</body>
</html>
