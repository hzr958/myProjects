<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<li>
  <dl>
    <dd class="info_name">
      <label class="title" tab="0" for="_project_zh_title" style="display: none"> <s:text
          name="projectType.lable.ProjectChineseName" />
      </label> <label class="title" for="_project_title"> <span class="red">*</span> <s:text
          name="projectEdit.label.ctitle" />
      </label>
    </dd>
    <dd>
      <p class="f666">
        <s:text name="projectType.lable.ProjectChineseName" />
        <!-- <a onclick="isShowToolbar('_project_zh_title');" class="mleft10 Blue">
                    <i class="py-icon icon67" style="margin-right: 3px;"></i>
                    <s:text name="projectEdit.label.textformat" />
                </a> -->
      </p>
      <div>
        <c:set var="zh_title">
          <x:out select="$myoutput/data/project/@zh_title" escapeXml="false" />
        </c:set>
        <textarea class="inp_textarea" name="/project/@zh_title" id="_project_zh_title" boxHeight="90"
          style="width: 682px; height: 60px; margin-top: -1px; border: 1px solid #CCCCCC;">${zh_title } </textarea>
      </div>
      <p class="f666">
        <s:text name="projectType.lable.ProjectForeignName" />
        <!-- <a onclick="isShowToolbar('_project_en_title');" class="mleft10 Blue">
                    <i class="py-icon icon67" style="margin-right: 3px;"></i>
                    <s:text name="projectEdit.label.textformat" />
                </a> -->
        <label class="title" for="_project_en_title" style="display: none"> <s:text
            name="projectType.lable.ProjectForeignName" />
        </label>
      </p>
      <p>
      <div>
        <textarea class="inp_textarea" name="/project/@en_title" id="_project_en_title" boxHeight="90"
          style="width: 682px; height: 60px; margin-top: -1px; border: 1px solid #CCCCCC;"><x:out
            select="$myoutput/data/project/@en_title" escapeXml="false" /></textarea>
      </div>
      <p></p>
    </dd>
  </dl>
</li>
