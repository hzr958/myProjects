<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<div>
  <label class="title" style="display: none" tab="1" for="_prj_members_prj_member_member_psn_name"> <s:text
      name="projectEdit.label.author_name"></s:text>
  </label> <label class="title" style="display: none" tab="1" for="_prj_members_prj_member"> <s:text
      name="projectEdit.label.author"></s:text>
  </label>
</div>
<table id="tblPsns" width="100%" border="0" cellspacing="0" cellpadding="0" class="t_table mtop10">
  <tr class="t1_css">
    <td width="5%"><s:text name="projectEdit.label.select" /> <input type="hidden" name="/prj_members/@remove_pms"
      id="_prj_members_remove_pms" value="" /> <input type="hidden" id="currentInsId" value="${currentInsId }" /> <input
      type="hidden" name="/prj_members/@remove_psns" id="_prj_members_remove_psns" value="" /> <input type="hidden"
      id="currentInsName" value="${currentInsName }" /></td>
    <td width="5%"><s:text name="projectEdit.label.seq_no" /></td>
    <td width="20%"><s:text name="projectEdit.label.author_name"></s:text></td>
    <td width="34%"><s:text name="projectEdit.label.member_ins"></s:text></td>
    <td width="24%"><s:text name="projectEdit.label.member" /></td>
    <td width="21%"><s:text name="projectEdit.label.member_notify_author" /></td>
  </tr>
  <tr class="template">
    <td align="center"><input id="selPsns" name="selPsns" type="radio" /> <input type="hidden"
      id="_prj_members_prj_member_0_pm_id" name="/prj_members/prj_member[0]/:pm_id" value="" /> <input type="hidden"
      id="_prj_members_prj_member_0_seq_no" name="/prj_members/prj_member[0]/:seq_no" value="" /> <input type="hidden"
      id="_prj_members_prj_member_0_email" name="/prj_members/prj_member[0]/:email" value="" /> <input type="hidden"
      class="member_ins_count" id="_prj_members_prj_member_0_ins_count" name="/prj_members/prj_member[0]/:ins_count"
      value="1" /></td>
    <td align="center"><span id="_prj_members_prj_member_0_sequence_no"
      name="/prj_members/prj_member[0]/:sequence_no"></span></td>
    <td align="center"><input type="text" class="inp_text prj_member_input_name"
      id="_prj_members_prj_member_0_member_psn_name" name="/prj_members/prj_member[0]/:member_psn_name" maxlength="50"
      value="" style="width: 130px;" /></td>
    <td align="left">
      <ul class="pub_author_ul">
        <li><input type="hidden" class="author_ins_id" name="/prj_members/prj_member[0]/:ins_id1" value="" /> <input
          type="hidden" class="author_ins_name" name="/prj_members/prj_member[0]/:ins_name1" value="" /> <input
          type="radio" name="/prj_members/prj_member[0]/:select_ins_id"
          onclick="ProjectEnter.AuthorDetails.selectIns(this)" value="${currentInsId }" />${currentInsName }</li>
        <li><input type="radio" name="/prj_members/prj_member[0]/:select_ins_id"
          onclick="ProjectEnter.AuthorDetails.selectIns(this)" value="1" /> <s:text
            name="projectEdit.label.otherOrganization" /></li>
      </ul>
    </td>
    <td align="center"><input type="hidden" class="member_psn_id_hidden"
      id="_prj_members_prj_member_0_member_psn_id" name="/prj_members/prj_member[0]/:member_psn_id" value="" /> <input
      type="hidden" class="member_psn_acname_hidden" id="_prj_members_prj_member_0_member_psn_acname"
      name="/prj_members/prj_member[0]/:member_psn_acname" value="" />
      <div class="wrap_ac_psn_div" style="display: none">
        <label class="member_psn_acname_label"></label> <a class="Blue member_psn_acname_remove" style="display: none"
          onclick="ProjectEnter.AuthorDetails.removeInsPsn(this)"> <s:text name="projectEdit.label.alink.clear" />
        </a> <input type="text" style="width: 220px;" class="inp_text member_psn_acname_input" isselect="true"
          id="_prj_members_prj_member_0_acname_input" name="/prj_members/prj_member[0]/:acname_input" maxlength="100"
          value="" />
      </div></td>
    <td align="center"><input type="checkbox" id="_prj_members_prj_member_0_notify_author"
      name="/prj_members/prj_member[0]/:notify_author" value="1" /></td>
  </tr>
  <c:set value="0" var="index" scope="page" />
  <x:forEach select="$myoutput/data/prj_members/prj_member" var="psn">
    <c:set value="${index+1}" var="index" scope="page" />
    <c:choose>
      <c:when test="${index<10}">
        <c:set value="0" var="flag" scope="page" />
      </c:when>
      <c:otherwise>
        <c:set value="" var="flag" scope="page" />
      </c:otherwise>
    </c:choose>
    <c:set var="notify_author">
      <x:out select="$psn/@notify_author" />
    </c:set>
    <c:set var="ins_count">
      <x:out select="$psn/@ins_count" />
    </c:set>
    <tr>
      <td align="center"><input id="selPsns" name="selPsns" type="radio" /> <input type="hidden"
        id="_prj_members_prj_member_${flag}${index}_pm_id" name="/prj_members/prj_member[${flag}${index}]/@pm_id"
        value="<x:out select="$psn/@pm_id"/>" /> <input type="hidden"
        id="_prj_members_prj_member_${flag}${index}_email" name="/prj_members/prj_member[${flag}${index}]/@email"
        value="<x:out select="$psn/@email"/>" /> <input type="hidden"
        id="_prj_members_prj_member_${flag}${index}_seq_no" name="/prj_members/prj_member[${flag}${index}]/@seq_no"
        value="${index+1 }" /> <input type="hidden" id="_prj_members_prj_member_${flag}${index}_ins_count"
        name="/prj_members/prj_member[${flag}${index}]/@ins_count" value="${ins_count }" /></td>
      <td align="center"><span id="_prj_members_prj_member_${flag}${index}_sequence_no"
        name="/prj_members/prj_member[${flag}${index}]/@sequence_no">${index}</span></td>
      <td align="center"><input type="text" class="inp_text prj_member_input_name"
        id="_prj_members_prj_member_${flag}${index}_member_psn_name"
        name="/prj_members/prj_member[${flag}${index}]/@member_psn_name" maxlength="50"
        value="<x:out select="$psn/@member_psn_name"/>" style="width: 130px;" /></td>
      <td align="left"><c:set var="current_ins_checked" value="" /> <c:set var="other_ins_checked" value="" /> <c:set
          var="ins_id">
          <x:out select="$psn/@ins_id1" />
        </c:set> <c:set var="matched_ins_id">
          <x:out select="$psn/@matched_ins_id" />
        </c:set> <c:set var="other_ins_id" value="1"></c:set> <c:set var="show_ac_psn" value="false"></c:set> <c:if
          test="${!empty matched_ins_id && matched_ins_id ne currentInsId}">
          <c:set var="other_ins_id" value="${matched_ins_id }"></c:set>
        </c:if> <c:choose>
          <c:when test="${ins_id eq currentInsId}">
            <c:set var="member_psn_acname_input_display"></c:set>
            <c:set var="current_ins_checked">checked="checked"</c:set>
            <c:set var="other_ins_checked" value="" />
            <c:set var="show_ac_psn" value="true"></c:set>
          </c:when>
          <c:when test="${(!empty ins_id && ins_id eq matched_ins_id)||ins_id eq 1}">
            <c:set var="current_ins_checked" value="" />
            <c:set var="other_ins_checked">checked="checked"</c:set>
          </c:when>
          <c:otherwise>
            <c:set var="matched_ins_id">
              <x:out select="$psn/@matched_ins_id" />
            </c:set>
            <c:if test="${(matched_ins_id eq currentInsId) || (empty matched_ins_id)}">
              <c:set var="matched_ins_id" value="1"></c:set>
            </c:if>
          </c:otherwise>
        </c:choose> <input type="hidden" id="_prj_members_prj_member_${flag}${index}_old_ins_id1"
        name="/prj_members/prj_member[${flag}${index}]/@old_ins_id1" value="${ins_id }" /> <input type="hidden"
        class="author_ins_old_input" id="_prj_members_prj_member_${flag}${index}_old_ins_name1"
        name="/prj_members/prj_member[${flag}${index}]/@old_ins_name1" value="<x:out select="$psn/@ins_name1"/>" /> <input
        type="hidden" class="author_ins_id" id="_prj_members_prj_member_${flag}${index}_ins_id1"
        name="/prj_members/prj_member[${flag}${index}]/@ins_id1" value="${ins_id }" /> <input type="hidden"
        class="author_ins_name_input" isselect="true" style="width: 280px"
        id="_prj_members_prj_member_${flag}${index}_ins_name1"
        name="/prj_members/prj_member[${flag}${index}]/@ins_name1" value="<x:out select="$psn/@ins_name1"/>" />
        <ul class="pub_author_ul">
          <li><input type="hidden" class="author_ins_id" id="_prj_members_prj_member_${flag}${index}_ins_id1"
            name="/prj_members/prj_member[${flag}${index}]/@ins_id1" value="${ins_id }" /> <input type="hidden"
            class="author_matched_ins_id" id="_prj_members_prj_member_${flag}${index}_matched_ins_id"
            name="/prj_members/prj_member[${flag}${index}]/@matched_ins_id" value="${matched_ins_id }" /> <input
            type="hidden" class="author_ins" id="_prj_members_prj_member_${flag}${index}_old_ins_name"
            name="/prj_members/prj_member[${flag}${index}]/@old_ins_name" value="<x:out select="$psn/@old_ins_name"/>" />
            <input type="hidden" maxlength="200" class="inp_text author_ins_name" style="width: 200px"
            id="_prj_members_prj_member_${flag}${index}_ins_name"
            name="/prj_members/prj_member[${flag}${index}]/@ins_name" onfocus=""
            value="<x:out select="$psn/@ins_name"/>" /> <input type="radio"
            name="/prj_members/prj_member[${flag}${index}]/:select_ins_id" ${current_ins_checked }
            onclick="ProjectEnter.AuthorDetails.selectIns(this)" value="${currentInsId }" />${currentInsName }</li>
          <li><input type="radio" name="/prj_members/prj_member[${flag}${index}]/:select_ins_id"
            ${other_ins_checked } onclick="ProjectEnter.AuthorDetails.selectIns(this)" value="${other_ins_id }" /> <s:text
              name="projectEdit.label.otherOrganization" /></li>
        </ul></td>
      <td align="center"><c:set var="member_psn_id">
          <x:out select="$psn/@member_psn_id" />
        </c:set> <c:set var="member_psn_acname_display"></c:set> <c:if test="${empty member_psn_id }">
          <c:set var="member_psn_acname_display">display:none</c:set>
        </c:if> <c:if test="${!empty member_psn_id }">
          <c:set var="member_psn_acname_input_display">display:none</c:set>
        </c:if>
        <div class="wrap_ac_psn_div" <c:if test="${!show_ac_psn}">style="display:none"</c:if>>
          <input type="hidden" class="member_psn_id_hidden" id="_prj_members_prj_member_${flag}${index}_member_psn_id"
            name="/prj_members/prj_member[${flag}${index}]/@member_psn_id" value="${member_psn_id }" /> <input
            type="hidden" class="member_psn_acname_hidden"
            id="_prj_members_prj_member_${flag}${index}_member_psn_acname"
            name="/prj_members/prj_member[${flag}${index}]/@member_psn_acname"
            value="<x:out select="$psn/@member_psn_acname"/>" /> <label class="member_psn_acname_label"
            style="${member_psn_acname_display }"> <x:out select="$psn/@member_psn_acname" />
          </label> <a class="Blue member_psn_acname_remove" style="${member_psn_acname_display }"
            onclick="ProjectEnter.AuthorDetails.removeInsPsn(this)"> <s:text name="projectEdit.label.alink.clear" />
          </a> <input type="text" style="width:220px;${member_psn_acname_input_display }"
            class="inp_text member_psn_acname_input" isselect="true"
            id="_prj_members_prj_member_${flag}${index}_acname_input"
            name="/prj_members/prj_member[${flag}${index}]/@acname_input" maxlength="100" value="" />
        </div></td>
      <td align="center"><input type="checkbox" id="_prj_members_prj_member_${flag}${index}_notify_author"
        name="/prj_members/prj_member[${flag}${index}]/@notify_author" value="1"
        <c:if test="${notify_author eq '1' }">checked</c:if> /></td>
    </tr>
  </x:forEach>
</table>
<div class="mtop10">
  <a class="uiButton f_normal" style="cursor: pointer;" id="cmdAddbeforePsn"
    onclick="addRolPrjAuthor('tblPsns','selPsns',1);"> <s:text name="projectEdit.label.add" />
  </a> <a class="uiButton f_normal" style="cursor: pointer;" id="cmdMoveNextPsn"
    onclick="movePrev('tblPsns','selPsns','1');"> <s:text name="projectEdit.label.movePrev" />
  </a> <a class="uiButton f_normal" style="cursor: pointer;" id="cmddelPsn" onclick="moveNext('tblPsns','selPsns','1');">
    <s:text name="projectEdit.label.moveNext" />
  </a> <a class="uiButton f_normal" style="cursor: pointer;" id="cmdMovePrevPsn"
    onclick="removePrjAuthor('tblPsns','selPsns','1');"> <s:text name="projectEdit.label.delete" />
  </a>
</div>
<c:choose>
  <c:when test="${webContextType eq 'scmwebrol'}">
    <c:set var="ins_auto_init" value="authorInstitution" />
  </c:when>
  <c:otherwise>
    <c:set var="ins_auto_init" value="psnIns" />
  </c:otherwise>
</c:choose>
<script type="text/javascript">
			   $(document).ready(function(){
				   ProjectEnter.AutoComplete.rolAuthorPsnSetupInit("authorInstitution");
			   });
			</script>