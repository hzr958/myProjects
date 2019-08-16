<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 	<!--  备注 -->
		<div class="fill pub">
			<label class="title"><s:text name="projectEdit.label.remark" /></label>
			<input type="text" class="textbox" maxlength="400" style="width:600px" id="_project_remark" name="/project/@remark" value="<x:out  select="$myoutput/data/project/@remark" />" />
		</div> --%>
<li>
  <dl>
    <dd class="info_name">
      <s:text name="projectEdit.label.remark" />
    </dd>
    <dd>
      <input type="text" class="inp_text" maxlength="400" style="width: 600px" id="_project_remark"
        name="/project/@remark" value="<x:out  select="$myoutput/data/project/@remark" />" />
    </dd>
  </dl>
</li>
<%-- <!--  全文 -->
		<div class="fill pub">
			<label class="title"><s:text name="projectEdit.label.fulltext_url" /></label>
			<input type="text" class="textbox" maxlength="400" style="width:600px" id="_prj_fulltext_fulltext_url" name="/prj_fulltext/@fulltext_url" value="<x:out  select="$myoutput/data/prj_fulltext/@fulltext_url" />" />
		</div> --%>
<li>
  <dl>
    <dd class="info_name">
      <s:text name="projectEdit.label.fulltext_url" />
    </dd>
    <dd>
      <input type="text" class="inp_text" maxlength="400" style="width: 600px" id="_prj_fulltext_fulltext_url"
        name="/prj_fulltext/@fulltext_url" value="<x:out  select="$myoutput/data/prj_fulltext/@fulltext_url" />" />
    </dd>
  </dl>
</li>