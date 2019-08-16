<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div id="fileList">
  <script type="text/javascript">
	$(document).ready(function() {
		//分页主入口
		TagesMain.main(loadFile);
	});
	function loadFile(){
		Group.groupFileList(Group.getGroupFileParam());
	}
</script>
  <s:if test="page.pageCount >0">
    <table id="groupFileListTable" border="0" cellpadding="0" cellspacing="0" class="tab_lt">
      <s:iterator value="form.page.result" var="file" status="st">
        <tr id="fileIndex_${st.index}">
          <td valign="top" width="79"><a onclick="openFile('${file.des3FileId}','1','1');" class="file_bg1 ml10">
              <img src="/upfile${fn:substringBefore(filePath, '.')}_c.${fn:substringAfter(filePath, '.')}"
              onerror="this.onerror=null;this.src='${resscmsns}/images_v5/file-image-default.jpg'"> <%--        <s:if test="#file.fileType=='imgIc'">
	       	  <img src="/upfile${fn:substringBefore(filePath, '.')}_c.${fn:substringAfter(filePath, '.')}" onerror="this.src='${resscmsns}/images_v5/file-image-default.jpg'">
	        </s:if>
	         <s:else>
	       	 	<div class="img_${file.fileType}_group img_box_group" title="<c:out value='${file.fileName}'/>"></div>
	       	 </s:else> --%>
          </a></td>
          <td>
            <div>
              <p class="f333 ft16">
                <c:out value="${file.fileName}" />
              </p>
              <p class="f999 mt5" id="viewFileDesc_${st.index}" title="<c:out value='${file.fileDesc}'/>"
                style="text-overflow: ellipsis; white-space: nowrap; overflow: hidden; width: 600px;">
                <c:out value="${file.fileDesc}" escapeXml="false" />
              </p>
              <p class="f999 mt5">
                <s:date name="uploadTime" format="yyyy/MM/dd" />
                &nbsp;&nbsp;|&nbsp;&nbsp;上传于 <span class="f333"><c:out value="${file.psnName}" /></span>
              </p>
              <div class="screen_mn_l">
                <c:if
                  test="${currentPsnGroupRoleStatus == 2||currentPsnGroupRoleStatus == 3||currentPsnGroupRoleStatus == 4}">
                  <%--  <s:if test="#file.psnId==form.psnId"> --%>
                  <a onclick="Group.editGroupFile('<iris:des3 code="${file.groupFileId}"></iris:des3>',${st.index});"
                    class="paper_mn_btn"><i class="material-icons edit"></i></a>
                  <%-- </s:if> --%>
                  <%-- <s:if test="%{form.isAdmin==1 || psnId==form.psnId }"> --%>
                  <a onclick="Group.deleteGroupFile('<iris:des3 code="${file.groupFileId}"></iris:des3>',${st.index});"
                    class="paper_mn_btn"><i class="material-icons delete"></i></a>
                  <%-- </s:if> --%>
                </c:if>
              </div>
            </div>
          </td>
        </tr>
      </s:iterator>
    </table>
    <s:include value="/common/group-page-tages.jsp"></s:include>
  </s:if>
  <s:else>
    <div class="both-center" style="width: 100%; height: 240px;">未找到相关记录</div>
  </s:else>
</div>