<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<c:set var="fileId">
  <x:out select="$myoutput/data/prj_fulltext/@file_id" />
</c:set>
<c:set var="fileNodeId">
  <x:out select="$myoutput/data/prj_fulltext/@node_id" />
</c:set>
<c:if test="${empty fileNodeId }">
  <c:set var="fileNodeId" value="${currentNodeId }"></c:set>
</c:if>
<c:set var="fileName">
  <x:out escapeXml="false" select="$myoutput/data/prj_fulltext/@file_name" />
</c:set>
<c:set var="fileDesc">
  <x:out escapeXml="false" select="$myoutput/data/prj_fulltext/@file_desc" />
</c:set>
<c:set var="upload_date">
  <x:out select="$myoutput/data/prj_fulltext/@upload_date" />
</c:set>
<c:set var="file_ext">
  <x:out select="$myoutput/data/prj_fulltext/@file_ext" />
</c:set>
<c:set var="fileInsId">
  <x:out select="$myoutput/data/prj_fulltext/@ins_id" />
</c:set>
<c:set var="permission">
  <x:out select="$myoutput/data/prj_fulltext/@permission" />
</c:set>
<c:if test="${empty fileInsId}">
  <c:set var="fileInsId" value="${currentInsId }"></c:set>
</c:if>
<c:set var="style" value="display:none;"></c:set>
<c:set var="uploadStyle" value=""></c:set>
<c:if test="${!empty fileId}">
  <c:set var="style" value=""></c:set>
  <c:set var="uploadStyle" value="display:none;"></c:set>
</c:if>
<input type="hidden" id="upload_attachement_type" value="project" />
<div class="d_text mtop5 mdown10">
  <div class="do_title">
    <span> <s:text name="projectEdit.label.fulltext_file" />
    </span>
  </div>
  <div id="divFullTextTipsMsg" class="warn mtop10" style="${uploadStyle }">
    <i class="warn-icon"></i> <span> <s:text name="projectEdit.label.wf.upload.Claims" />
    </span>
  </div>
  <div class="upload mtop10" id="divUploadUpFullTextFile" style="${uploadStyle }">
    <ul>
      <li>
        <dl>
          <dd class="wz">
            <s:text name="projectEdit.label.wf.uploadFiles" />
            <s:text name="colon.all" />
          </dd>
          <dd>
            <input type="file" id="filedata" name="filedata" />
          </dd>
        </dl>
      </li>
      <li>
        <dl>
          <dd class="wz">
            <s:text name="projectEdit.label.file_desc" />
            <s:text name="colon.all" />
          </dd>
          <dd>
            <textarea id="_fulltext_fileupload_desc" style="width: 452px; height: 40px;" class="inp_text" cols="20"
              rows="2"></textarea>
          </dd>
        </dl>
      </li>
      <li style="margin-left: 85px; display: inline;"><a class="uiButton uiButtonConfirm" style="cursor: pointer;"
        onclick="confirmFullTextUpload()" id="btnUpload2"> <s:text name="projectEdit.label.wf.upload" />
      </a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <!-- <input type="button" id="confirm_fulltext_btn"  class="thickbox" style="display:none" alt="/#TB_inline?height=100&amp;width=450&amp;modal=true&amp;inlineId=confirm_fulltext"></input> -->
        <div style="padding-left: 18%; width: 24px; height: 24px;">
          <img src="${res}/images/loading.gif" id="loading" style="display: none" />
        </div></li>
    </ul>
  </div>
  <table id="fullTextUploadMsg" style="${style }" class="t_table mtop10 t_nr" cellSpacing="0" cellPadding="0">
    <tr class="t1_css">
      <td><s:text name="projectEdit.label.file_name" /> <b class="red">*</b></td>
      <td width="40%"><s:text name="projectEdit.label.file_desc" /></td>
      <td width="12%"><s:text name="projectEdit.label.upload_date" /></td>
      <c:if test="${res ne '/resscmwebrol'}">
        <td width="16%"><s:text name="publication.label.permissions" /></td>
      </c:if>
      <td width="6%"><s:text name="projectEdit.label.action" /></td>
    </tr>
    <tr>
      <td align="center"><input type="hidden" id="_prj_fulltext_current_node_id" value="${currentNodeId }" /> <input
        type="hidden" id="_prj_fulltext_current_ins_id" value="${currentInsId }" /> <input type="hidden"
        id="_prj_fulltext_node_id" name="/prj_fulltext/@node_id" value="${fileNodeId }" /> <input type="hidden"
        id="_prj_fulltext_file_id" name="/prj_fulltext/@file_id" value="${fileId }" /> <input type="hidden"
        id="_prj_fulltext_upload_date" name="/prj_fulltext/@upload_date" value="${upload_date}" /> <input type="hidden"
        id="_prj_fulltext_file_ext" name="/prj_fulltext/@file_ext" value="${file_ext}" /> <input type="hidden"
        id="_prj_fulltext_ins_id" name="/prj_fulltext/@ins_id" value="${fileInsId}" /> <input type="text"
        class="inp_text" maxlength="200" style="width: 244px" id="_prj_fulltext_file_name"
        name="/prj_fulltext/@file_name" value="<c:out value="${fileName}"/>" /></td>
      <td align="center"><input type="text" class="inp_text" maxlength="200" style="width: 330px"
        id="_prj_fulltext_file_desc" name="/prj_fulltext/@file_desc" value="<c:out value="${fileDesc}"/>" /></td>
      <td align="center"><span id="span_prj_fulltext_upload_date">${upload_date}</span></td>
      <c:if test="${res ne '/resscmwebrol'}">
        <td align="center"><select name="/prj_fulltext/@permission" class="inp_text">
            <%-- 									<option value="1" ${empty permission || permission eq 1||permission eq 0?'selected=selected':''} ><s:text name="publication.label.friend.view"/></option>
 --%>
            <option value="2" ${permission eq 2 ||permission eq 1 ?'selected=selected':''}><s:text
                name="publication.label.me.view" /></option>
            <option value="0" ${empty permission ||permission eq 0?'selected=selected':''}><s:text
                name="publication.label.all.view" /></option>
        </select></td>
      </c:if>
      <td align="center">
        <%-- <a class="Blue" href="<iris:fileLink nodeId="${fileNodeId}" insId="${fileInsId }" fileCode="${fileId}"></iris:fileLink>" id="fulltext_downlink" target="new"> --%>
        <a class="Blue" onclick="BaseUtils.fileDown('${downloadUrl }',this,event)" id="fulltext_downlink" target="new">
          <!--  <a class="Blue" onclick=' ' id="fulltext_downlink" target="new"> --> <s:text
            name="projectEdit.label.download" />
      </a> &nbsp;/ <a class="Blue" href="javascript:removeFullTextFile()" id="fulltext_romovelink"> <s:text
            name="projectEdit.label.removelink" />
      </a>
      </td>
    </tr>
  </table>
</div>
<c:if test="${res ne '/resscmwebrol'}">
  <div style="clear: both; padding-top: 10px;">
    <a title="<s:text name="projectEdit.label.linkFile"/>" class="Blue" onclick="replaceFulltextWithMyFile();"> <s:text
        name="projectEdit.label.linkFile" />
    </a>
  </div>
  <!-- prjweb/project/ajaxLinkFile   ${ctx}/group/ajaxLinkFile-->
  <a
    href="/prjweb/project/ajaxLinkFile?suffix=jpegtxtjpggifpdfdocdocxpnghtmlxhtmlhtmrarzip&prjOrpub=2&TB_iframe=true&height=450&width=550"
    id="replace_fulltext_with_myfile_link" class="thickbox Blue" style="display: none;"></a>
</c:if>
<label class="title" style="display: none" tab="3" for="_prj_fulltext_file_name"> <s:text
    name="projectEdit.label.wf.fullTextFile.filename"></s:text>
</label>
<div class="attachment mtop5">
  <div class="do_title">
    <span> <s:text name="projectEdit.label.otherFileUploadTitle" />
    </span>
  </div>
  <div class="warn mtop10">
    <i class="warn-icon"></i> <span> <s:text name="projectEdit.label.otherFileUpload.Claims" />
    </span>
  </div>
  <label class="title" style="display: none" tab="3" for="_prj_attachments_prj_attachment"> <s:text
      name="projectEdit.label.otherFile.filename"></s:text>
  </label> <label class="title" style="display: none" tab="3" for="_prj_attachments_prj_attachment_file_name"> <s:text
      name="projectEdit.label.file_name"></s:text>
  </label>
  <table id="tblAttachs" class="t_table mtop10 t_nr" cellSpacing="0" cellPadding="0">
    <tr class="t1_css">
      <td width="5%"><s:text name="projectEdit.label.select" /></td>
      <td><s:text name="projectEdit.label.file_name" /> <span class="red">*</span></td>
      <td width="40%"><s:text name="projectEdit.label.file_desc" /></td>
      <td width="12%"><s:text name="projectEdit.label.upload_date" /></td>
      <c:if test="${res ne '/resscmwebrol'}">
        <td width="16%"><s:text name="publication.label.permissions" /></td>
      </c:if>
      <td width="6%"><s:text name="projectEdit.label.action" /></td>
    </tr>
    <tr class="template">
      <td align="center"><input type="radio" class="radiobutton" name="selAttach" /> <input type="hidden"
        id="_prj_attachments_prj_attachment_0_seq_no" name="/prj_attachments/prj_attachment[0]/:seq_no" value="" /> <input
        type="hidden" id="_prj_attachments_prj_attachment_0_node_id" name="/prj_attachments/prj_attachment[0]/:node_id"
        value="${currentNodeId }" /> <input type="hidden" id="_prj_attachments_prj_attachment_0_ins_id"
        name="/prj_attachments/prj_attachment[0]/:ins_id" value="${currentInsId }" /> <input type="hidden"
        id="_prj_attachments_prj_attachment_0_file_id" name="/prj_attachments/prj_attachment[0]/:file_id" value="" /> <input
        type="hidden" id="_prj_attachments_prj_attachment_0_file_ext"
        name="/prj_attachments/prj_attachment[0]/:file_ext" value="" /> <input type="hidden"
        id="_prj_attachments_prj_attachment_0_upload_date" name="/prj_attachments/prj_attachment[0]/:upload_date"
        value="" /> <input type="hidden" id="_prj_attachments_prj_attachment_0_domain_context"
        name="/prj_attachments/prj_attachment[0]/:domain_context" value="${currentDomain}" /></td>
      <!--<td align="center"><span  id="_prj_attachments_prj_attachment_0_sequence_no"  name="/prj_attachments/prj_attachment[0]/:sequence_no" ></span></td>-->
      <td align="center"><input style="width: 200px;" id="_prj_attachments_prj_attachment_0_file_name"
        name="/prj_attachments/prj_attachment[0]/:file_name" type="text" value="" class="inp_text" /></td>
      <td align="center"><input style="width: 330px;" name="/prj_attachments/prj_attachment[0]/:file_desc"
        id="_prj_attachments_prj_attachment_0_file_desc" type="text" value="" class="inp_text" /></td>
      <td align="center" id="_prj_attachments_prj_attachment_0_upload_date_label"></td>
      <c:if test="${res ne '/resscmwebrol'}">
        <td align="center"><c:set var="permission" value="1"></c:set> <select
          id="_prj_attachments_prj_attachment_0_permission" style="width: 120px;"
          name="/prj_attachments/prj_attachment[0]/:permission" class="inp_text">
            <%-- <option value="1" ${empty permission || permission eq 1?'selected=selected':''} ><s:text name="publication.label.friend.view"/></option> --%>
            <option value="2" ${permission eq 2||permission eq 1?'selected=selected':''}><s:text
                name="publication.label.me.view" /></option>
            <option value="0" ${permission eq 0?'selected=selected':''}><s:text
                name="publication.label.all.view" /></option>
        </select></td>
      </c:if>
      <td align="center"><a class="Blue" id="_prj_attachments_prj_attachment_0_download_link"
        style="cursor: pointer;" target="_blank"> <s:text name="projectEdit.label.download" />
      </a></td>
    </tr>
    <c:set value="0" var="index" scope="page" />
    <x:forEach select="$myoutput/data/prj_attachments/prj_attachment" var="attach">
      <c:set value="${index+1}" var="index" scope="page" />
      <c:choose>
        <c:when test="${index<10}">
          <c:set value="0" var="flag" scope="page" />
        </c:when>
        <c:otherwise>
          <c:set value="" var="flag" scope="page" />
        </c:otherwise>
      </c:choose>
      <c:set var="upload_date">
        <x:out select="$attach/@upload_date" />
      </c:set>
      <c:set var="fileCode">
        <x:out select="$attach/@file_id" />
      </c:set>
      <c:set var="fileNodeId">
        <x:out select="$attach/@node_id" />
      </c:set>
      <c:set var="fileInsId">
        <x:out select="$attach/@ins_id" />
      </c:set>
      <c:set var="file_desc">
        <x:out select="$attach/@file_desc" escapeXml="false" />
      </c:set>
      <c:set var="file_name">
        <x:out select="$attach/@file_name" escapeXml="false" />
      </c:set>
      <c:set var="fileExt">
        <x:out select="$attach/@file_ext" escapeXml="false" />
      </c:set>
      <c:set var="fileInsId">
        <x:out select="$attach/@ins_id" />
      </c:set>
      <c:set var="permission">
        <x:out select="$attach/@permission" />
      </c:set>
      <c:if test="${empty fileInsId}">
        <c:set var="fileInsId" value="${currentInsId }"></c:set>
      </c:if>
      <tr>
        <td align="center"><input type="radio" class="radiobutton" name="selAttach" /> <input type="hidden"
          id="_prj_attachments_prj_attachment_${flag }${index }_node_id"
          name="/prj_attachments/prj_attachment[${flag }${index }]/@node_id" value="${fileNodeId }" /> <input
          type="hidden" id="_prj_attachments_prj_attachment_${flag }${index }_ins_id"
          name="/prj_attachments/prj_attachment[${flag }${index }]/@ins_id" value="${fileInsId }" /> <input
          type="hidden" id="_prj_attachments_prj_attachment_${flag }${index }_file_id"
          name="/prj_attachments/prj_attachment[${flag }${index }]/@file_id" value="${fileCode }" /> <input
          type="hidden" id="_prj_attachments_prj_attachment_${flag }${index }_seq_no"
          name="/prj_attachments/prj_attachment[${flag }${index }]/@seq_no" " value="${index }" /> <input type="hidden"
          id="_prj_attachments_prj_attachment_${flag }${index }_upload_date"
          name="/prj_attachments/prj_attachment[${flag }${index }]/@upload_date" value="${upload_date }" /> <input
          type="hidden" id="_prj_attachments_prj_attachment_${flag }${index }_file_ext"
          name="/prj_attachments/prj_attachment[${flag }${index }]/@file_ext" value="${fileExt }" /></td>
        <!--<td align="center"><span  id="_prj_attachments_prj_attachment_${flag }${index }_sequence_no" name="/prj_attachments/prj_attachment[${flag }${index }]/@sequence_no" >${index }</span></td>
						-->
        <td align="center"><input style="width: 200px;"
          name="/prj_attachments/prj_attachment[${flag }${index }]/@file_name"
          id="_prj_attachments_prj_attachment_${flag }${index }_file_name" type="text"
          value="<c:out value="${file_name }"/>" class="inp_text" /></td>
        <td align="center"><input style="width: 330px;"
          name="/prj_attachments/prj_attachment[${flag }${index }]/@file_desc"
          id="_prj_attachments_prj_attachment_${flag }${index }_file_desc" type="text"
          value="<c:out value="${file_desc }"/>" class="inp_text" /></td>
        <td align="center" id="_prj_attachments_prj_attachment_${flag }${index }_upload_date_label">${upload_date }</td>
        <c:if test="${res ne '/resscmwebrol'}">
          <td align="center"><select name="/prj_attachments/prj_attachment[${flag }${index }]/@permission"
            id="_prj_attachments_prj_attachment_${flag }${index }_permission" class="inp_text">
              <option value="2" ${permission eq 2?'selected=selected':''}><s:text
                  name="publication.label.me.view" /></option>
              <option value="0" ${permission eq 0?'selected=selected':''}><s:text
                  name="publication.label.all.view" /></option>
          </select></td>
        </c:if>
        <td align="center"><a class="Blue" id="_prj_attachments_prj_attachment_${flag }${index }_download_link"
          href='<iris:fileLink nodeId="${fileNodeId}" insId="${fileInsId}" fileCode="${fileCode}"/>' target="_blank">
            <s:text name="projectEdit.label.download" />
        </a></td>
      </tr>
    </x:forEach>
  </table>
  <div class="but_div mtop10">
    <input id="filedata1" type="file" name="filedata">
    <!-- <a class="uiButton f_normal" href="javascrip:void(0)" onclick="javascript:document.getElementById('filedata1').click()">上传</a>  -->
    <a class="uiButton f_normal" style="margin-left: 68px; cursor: pointer;" id="cmdMovePrevAttach"
      onclick="movePrev('tblAttachs','selAttach','1')"> <s:text name="projectEdit.label.movePrev" />
    </a> <a class="uiButton f_normal" style="cursor: pointer;" id="cmdMoveNextAttach"
      onclick="moveNext('tblAttachs','selAttach','1')"> <s:text name="projectEdit.label.moveNext" />
    </a> <a class="uiButton f_normal" style="cursor: pointer;" id="cmdDelAttach"
      onclick="deleteSelectedRow('tblAttachs','selAttach','1')"> <s:text name="projectEdit.label.delete" />
    </a> <img src="${res}/images/loading.gif" id="loading1" style="display: none; width: 24px; height: 24px;" />
  </div>
</div>
