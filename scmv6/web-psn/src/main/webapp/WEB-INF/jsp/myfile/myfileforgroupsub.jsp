<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s:if test="sfList.size>0">
  <s:iterator value="sfList" var="sf" status="st">
    <tr id="myfile_${pageNo}_${st.index}_${randomStr}">
      <s:if test="#st.index==0">
        <input type="hidden" name="filePageNo" value="${pageNo}" />
      </s:if>
      <td><a
        style="text-overflow: ellipsis; white-space: nowrap; overflow: hidden; width: 300px; text-decoration: none; cursor: default; color: #000000;"
        class="file_l" title="<c:out value='${sf.fileName}'/>"><c:out value="${sf.fileName}"></c:out></a></td>
      <td width="50" align="right" valign="middle"><a
        onclick="Group.addGroupFileFromMyFile('${sf.des3Id}','${pageNo}_${st.index}_${randomStr}')" class="blue">添加</a></td>
    </tr>
  </s:iterator>
</s:if>
