<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<div class="project-edit_item-footer_header">
  <div class="project-edit_item-footer_header-left" style="width: 100%;text-align: left;  margin-left: 45px;"><s:text name="projectEdit.label.tabAuthor" /></div>
  <%--<div class="project-edit_item-footer_header-right">
    <div class="handin_import-content_container-right_author-addbtn" onclick="ProjectMember.addPrjMember();"><i class="handin_import-content_container-right_author-add"></i>添加</div>
  </div>--%>
</div>
<div class="project-edit_item-footer_body">
  <div class="project-edit_item-footer_body-left"></div>
  <!-- 成员模板-->
  <div class="handin_import-content_container-right_author-body prj_member_dev prj_member_template"  style="width: 100%;display: none">
    <div class="handin_import-content_container-right_author-body_oneself prj_member_seq_dev">
      <i class="dev_menber_i selected-oneself" onclick="ProjectMember.setOwner(this);"></i>
      <input type="hidden" class="memberOwnerFlag"id="_prj_members_prj_member_00_owner" name="/prj_members/prj_member[00]/@owner"
      value="" />
    </div>
    <input type="hidden" class="json_member_des3PsnId" value="" name="/prj_members/prj_member[00]/@des3_member_psn_id">
    <input type="hidden" id="_prj_members_prj_member_00_pm_id" name="/prj_members/prj_member[00]/@pm_id"
           value="" />
    <input type="hidden" id="_prj_members_prj_member_00_seq_no" name="/prj_members/prj_member[00]/@seq_no"
           value="" />

    <input type="hidden" id="_prj_members_prj_member_00_ins_count"name="/prj_members/prj_member[00]/@ins_count"
           value="" />
    <div class="handin_import-content_container-right_author-body_name">
          <span class="handin_import-content_container-right_author-body_item error_import-tip_border">
              <input type="text"  id="_prj_members_prj_member_00_member_psn_name"  request-url="/psnweb/ac/ajaxpsncooperator"  other-event="ProjectMember.callbackName"
                     name="/prj_members/prj_member[00]/@member_psn_name" maxlength="50"  request-data="ProjectMember.getExitPsnId();"
                     value=""
                     style="width: 85% !important;"  autocomplete="off" class="dev-detailinput_container full_width json_member_name js_autocompletebox" />
          </span>
    </div>
    <div class="handin_import-content_container-right_author-body_work">
          <span class="handin_import-content_container-right_author-body_item">
              <input type="text" id="_prj_members_prj_member_00_ins_name1" name="/prj_members/prj_member[00]/@ins_name1" value=""
                     autocomplete="off" maxlength="100" class="dev-detailinput_container full_width json_member_insNames " />
          </span>
    </div>
    <div class="handin_import-content_container-right_author-body_email ">
            <span class="handin_import-content_container-right_author-body_item error_import-tip_border">
                <input type="text" id="_prj_members_prj_member_00_email"  value=""
                       name="/prj_members/prj_member[00]/@email" maxlength="50" class="dev-detailinput_container full_width json_member_email" />
            </span>
    </div>
    <div class="handin_import-content_container-right_author-body_Communication">
      <i class="selected-author dev_communicale" onclick="ProjectMember.setNotifyAuthor(this);">
        <input type="hidden"   id="_prj_members_prj_member_00_notify_author"
               name="/prj_members/prj_member[00]/@notify_author" value=""  />
      </i>
      <input type="hidden"   class="json_member_communicable" name="communicable" value="false" />
    </div>
    <div class="handin_import-content_container-right_author-body_edit arrow_dev">
      <i class="selected-func_delete" onclick="ProjectMember.delPrjMember(this);"></i>
      <i class="selected-func_down" onclick="ProjectMember.downMember(this);"></i>
      <i class="selected-func_up" onclick="ProjectMember.upMember(this);"></i>
    </div>
  </div>
  <!-- 成员模板 end-->
  <div class="project-edit_item-footer_body-right" id="projectMemberList">
    <div class="handin_import-content_container-right_author-title" style="width: 100%;">
      <div class="handin_import-content_container-right_author-oneself">
        <s:text name="project.edit.myself"/></div>
      <div class="handin_import-content_container-right_author-name">
        <s:text name="projectEdit.label.author_name"/></div>
      <div class="handin_import-content_container-right_author-work">
        <s:text name="projectEdit.label.member_ins" /></div>
      <div class="handin_import-content_container-right_author-email">
        <s:text name="projectEdit.label.member_email" /></div>
      <div class="handin_import-content_container-right_author-Communication">
        <s:text name="projectEdit.label.member_notify_author" /></div>
      <div class="handin_import-content_container-right_author-edit">
        <s:text name="projectEdit.label.operate"/></div>
    </div>
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
      <div class="handin_import-content_container-right_author-body prj_member_dev"  style="width: 100%;">
      <div class="handin_import-content_container-right_author-body_oneself prj_member_seq_dev">
        <c:if test="${owner == 1}">
          <i class="dev_menber_i selected-oneself_confirm" onclick="ProjectMember.setOwner(this);"></i>
        </c:if>
        <c:if test="${owner != 1}">
          <i class="dev_menber_i selected-oneself" onclick="ProjectMember.setOwner(this);"></i>
        </c:if>
        <input type="hidden" class="memberOwnerFlag"id="_prj_members_prj_member_${flag}${index}_owner" name="/prj_members/prj_member[${flag}${index}]/@owner"
               value="${owner }" />
      </div>
        <input type="hidden" class="json_member_des3PsnId" value="<x:out select="$psn/@des3_member_psn_id"/>" name="/prj_members/prj_member[${flag}${index}]/@des3_member_psn_id">
        <input type="hidden" id="_prj_members_prj_member_${flag}${index}_pm_id" name="/prj_members/prj_member[${flag}${index}]/@pm_id"
               value="<x:out select="$psn/@pm_id"/>"  />
        <input type="hidden" id="_prj_members_prj_member_${flag}${index}_seq_no" name="/prj_members/prj_member[${flag}${index}]/@seq_no"
                value="${index+1 }" />
        <input type="hidden" id="_prj_members_prj_member_${flag}${index}_ins_count"name="/prj_members/prj_member[${flag}${index}]/@ins_count"
               value="${ins_count }" />
      <div class="handin_import-content_container-right_author-body_name">
          <span class="handin_import-content_container-right_author-body_item error_import-tip_border">
              <input type="text"  id="_prj_members_prj_member_${flag}${index}_member_psn_name"    request-url="/psnweb/ac/ajaxpsncooperator"  other-event="ProjectMember.callbackName"
                     name="/prj_members/prj_member[${flag}${index}]/@member_psn_name" maxlength="50"  request-data="ProjectMember.getExitPsnId();"
                     value="<x:out select="$psn/@member_psn_name" escapeXml="false"/>"
                     style="width: 85% !important;"  autocomplete="off" class="dev-detailinput_container full_width json_member_name js_autocompletebox" />
          </span>
      </div>
      <div class="handin_import-content_container-right_author-body_work">
          <span class="handin_import-content_container-right_author-body_item">
              <input type="text" id="_prj_members_prj_member_${flag}${index}_ins_name1" name="/prj_members/prj_member[${flag}${index}]/@ins_name1" value="<x:out select="$psn/@ins_name1" escapeXml="false"/>"
                     autocomplete="off" maxlength="100" class="dev-detailinput_container full_width json_member_insNames " />
          </span>
      </div>
      <div class="handin_import-content_container-right_author-body_email ">
            <span class="handin_import-content_container-right_author-body_item error_import-tip_border">
                <input type="text" id="_prj_members_prj_member_${flag}${index}_email"  value="<x:out select="$psn/@email" escapeXml="false"/>"
                       name="/prj_members/prj_member[${flag}${index}]/@email" maxlength="50" class="dev-detailinput_container full_width json_member_email" />
            </span>
      </div>
      <div class="handin_import-content_container-right_author-body_Communication">
        <i class="selected-author dev_communicale <c:if test="${notify_author eq '1' }">selected-author_confirm</c:if>" onclick="ProjectMember.setNotifyAuthor(this);">
          <input type="hidden"   id="_prj_members_prj_member_${flag}${index}_notify_author"
                 name="/prj_members/prj_member[${flag}${index}]/@notify_author" value="${notify_author}"  />
        </i>
        <input type="hidden"   class="json_member_communicable" name="communicable" value="false" />
      </div>
      <div class="handin_import-content_container-right_author-body_edit arrow_dev">
        <i class="selected-func_delete" onclick="ProjectMember.delPrjMember(this);"></i>
        <i class="selected-func_down" onclick="ProjectMember.downMember(this);"></i>
        <i class="selected-func_up" onclick="ProjectMember.upMember(this);"></i>
      </div>
    </div>
    </x:forEach>
  </div>
</div>
