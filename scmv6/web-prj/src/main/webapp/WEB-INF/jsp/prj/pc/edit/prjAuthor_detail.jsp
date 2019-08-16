<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<div>
  <label class="title" style="display: none" tab="2" for="_prj_members_prj_member_member_psn_name"> <s:text
      name="projectEdit.label.author_name.all"></s:text>
  </label> <label class="title" style="display: none" tab="2" for="_prj_members_prj_member_email"> <s:text
      name="projectEdit.label.author_name"></s:text>
  </label> <label class="title" style="display: none" tab="2" for="_prj_members_prj_member"> <s:text
      name="projectEdit.label.author"></s:text>
  </label>
</div>
<div class="warn">
  <i class="warn-icon"></i>
  <s:text name="projectEdit.label.tip" />
</div>
<table id="tblPsns" width="100%" border="0" cellspacing="0" cellpadding="0" class="t_table mtop10">
  <tr class="t1_css">
    <td width="6%"><s:text name="projectEdit.label.select" /> <input type="hidden" name="/prj_members/@remove_pms"
      id="_prj_members_remove_pms" value="" /></td>
    <td width="5%"><s:text name="projectEdit.label.seq_no" /></td>
    <td width="15%"><s:text name="projectEdit.label.author_name_zh" /></td>
    <td width="15%"><s:text name="projectEdit.label.author_name_en" /></td>
    <td><s:text name="projectEdit.label.member_ins" /></td>
    <td width="15%"><s:text name="projectEdit.label.member_email" /></td>
    <td width="11%"><s:text name="projectEdit.label.member_notify_author" /></td>
  </tr>
  <tr class="template">
    <td align="center"><input id="selPsns" name="selPsns" type="radio" /> <input type="hidden"
      id="_prj_members_prj_member_0_pm_id" name="/prj_members/prj_member[0]/:pm_id" value="" /> <input type="hidden"
      id="_prj_members_prj_member_0_seq_no" name="/prj_members/prj_member[0]/:seq_no" value="" /> <input type="hidden"
      class="memberOwnerFlag" id="_prj_members_prj_member_0_owner" name="/prj_members/prj_member[0]/:owner" value="0" />
      <input type="hidden" class="member_ins_count" id="_prj_members_prj_member_0_ins_count"
      name="/prj_members/prj_member[0]/:ins_count" value="1" /></td>
    <td align="center"><span id="_prj_members_prj_member_0_sequence_no"
      name="/prj_members/prj_member[0]/:sequence_no"></span></td>
    <td align="center"><input type="hidden" id="_prj_members_prj_member_0_member_psn_id"
      name="/prj_members/prj_member[0]/:member_psn_id" value="" /> <input type="text" class="inp_text"
      id="_prj_members_prj_member_0_member_psn_name" name="/prj_members/prj_member[0]/:member_psn_name" maxlength="50"
      value="" style="width: 130px;" /></td>
    <td align="center"><input type="text" class="inp_text" id="_prj_members_prj_member_0_member_psn_name_en"
      name="/prj_members/prj_member[0]/:member_psn_name_en" maxlength="50" value="" style="width: 130px;" /></td>
    <td align="center"><input type="hidden" class="author_ins_old_input"
      id="_prj_members_prj_member_${flag}${index}_old_ins_name1" name="/prj_members/prj_member[0]/@old_ins_name1"
      value="" /> <input type="hidden" class="author_ins_id_input" style="width: 275px"
      id="_prj_members_prj_member_0_ins_id1" name="/prj_members/prj_member[0]/:ins_id1" value="" /> <input type="text"
      maxlength="100" class="inp_text author_ins_name_input" isselect="true" style="width: 275px"
      id="_prj_members_prj_member_0_ins_name1" name="/prj_members/prj_member[0]/:ins_name1" value="" /></td>
    <td align="center"><input type="text" class="inp_text" id="_prj_members_prj_member_0_email"
      name="/prj_members/prj_member[0]/:email" maxlength="50" value="" style="width: 180px;" /></td>
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
    <c:set var="owner">
      <x:out select="$psn/@owner" />
    </c:set>
    <c:set var="ins_count">
      <x:out select="$psn/@ins_count" />
    </c:set>
    <c:set var="ins_id1">
      <x:out select="$psn/@ins_id1" />
    </c:set>
    <tr>
      <td align="center"><input id="selPsns" name="selPsns" type="radio" /> <input type="hidden"
        id="_prj_members_prj_member_${flag}${index}_pm_id" name="/prj_members/prj_member[${flag}${index}]/@pm_id"
        value="<x:out select="$psn/@pm_id"/>" /> <input type="hidden"
        id="_prj_members_prj_member_${flag}${index}_seq_no" name="/prj_members/prj_member[${flag}${index}]/@seq_no"
        value="${index+1 }" /> <input type="hidden" class="memberOwnerFlag"
        id="_prj_members_prj_member_${flag}${index}_owner" name="/prj_members/prj_member[${flag}${index}]/@owner"
        value="${owner }" /> <input type="hidden" id="_prj_members_prj_member_${flag}${index}_ins_count"
        name="/prj_members/prj_member[${flag}${index}]/@ins_count" value="${ins_count }" /></td>
      <td align="center"><span id="_prj_members_prj_member_${flag}${index}_sequence_no"
        name="/prj_members/prj_member[${flag}${index}]/@sequence_no">${index}</span></td>
      <td align="center"><input type="hidden" id="_prj_members_prj_member_${flag}${index}_member_psn_id"
        name="/prj_members/prj_member[${flag}${index}]/@member_psn_id" value="<x:out select="$psn/@member_psn_id"/>" />
        <input type="text" class="inp_text" id="_prj_members_prj_member_${flag}${index}_member_psn_name"
        name="/prj_members/prj_member[${flag}${index}]/@member_psn_name" maxlength="50"
        value="<x:out select="$psn/@member_psn_name"/>" style="width: 130px;" /></td>
      <td align="center"><input type="text" class="inp_text"
        id="_prj_members_prj_member_${flag}${index}_member_psn_name_en"
        name="/prj_members/prj_member[${flag}${index}]/@member_psn_name_en" maxlength="50"
        value="<x:out select="$psn/@member_psn_name_en"/>" style="width: 130px;" /></td>
      <td align="center"><input type="hidden" id="_prj_members_prj_member_${flag}${index}_old_ins_id1"
        name="/prj_members/prj_member[${flag}${index}]/@old_ins_id1" value="${ins_id1 }" /> <input type="hidden"
        class="author_ins_old_input" id="_prj_members_prj_member_${flag}${index}_old_ins_name1"
        name="/prj_members/prj_member[${flag}${index}]/@old_ins_name1" value="<x:out select="$psn/@ins_name1"/>" /> <input
        type="hidden" class="author_ins_id" id="_prj_members_prj_member_${flag}${index}_ins_id1"
        name="/prj_members/prj_member[${flag}${index}]/@ins_id1" value="${ins_id1 }" /> <input type="text"
        maxlength="100" class="inp_text  author_ins_name_input" isselect="true" style="width: 275px"
        id="_prj_members_prj_member_${flag}${index}_ins_name1"
        name="/prj_members/prj_member[${flag}${index}]/@ins_name1" value="<x:out select="$psn/@ins_name1"/>" /></td>
      <td align="center"><input type="text" class="inp_text" id="_prj_members_prj_member_${flag}${index}_email"
        name="/prj_members/prj_member[${flag}${index}]/@email" maxlength="50" style="width: 180px;"
        value="<x:out select="$psn/@email"/>" /></td>
      <td align="center"><input type="checkbox" id="_prj_members_prj_member_${flag}${index}_notify_author"
        name="/prj_members/prj_member[${flag}${index}]/@notify_author" value="1"
        <c:if test="${notify_author eq '1' }">checked</c:if> /></td>
    </tr>
  </x:forEach>
</table>
<div class="mtop10">
  <a class="uiButton f_normal" style="cursor: pointer;" id="cmdAddbeforePsn"
    onclick="addPrjAuthor('tblPsns','selPsns',1);"> <s:text name="projectEdit.label.add" />
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
       ProjectEnter.AutoComplete.authorInsSetupInit("${ins_auto_init}");
    });
</script>