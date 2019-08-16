<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<li>
  <dl>
    <dd class="info_name">
      <s:text name="project.authority.tip" />
      <s:text name="colon.all" />
    </dd>
    <dd>
      <div align="center">
        <span class="uiButton uiSelectorButton selectClass" style="width: 120px;"></span>
      </div>
      <input type="hidden" id="_prj_authority" name="/prj_meta/@authority" value="${empty authority?7:authority}" />
    </dd>
  </dl>
</li>