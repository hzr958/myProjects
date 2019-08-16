<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<li>
  <dl>
    <label class="title" for="_project_zh_abstract" style="display: none"> <s:text
        name="projectEdit.label.zh_abstract" />
    </label>
    <dd class="info_name">
      <s:text name="projectEdit.label.zh_abstract" />
      <s:text name="colon.all" />
    </dd>
    <dd>
      <p class="f666">
        <s:text name="projectEdit.label.chinese" />
        
      </p>
      <div>
        <textarea class="inp_textarea" name="/project/@zh_abstract" id="_project_zh_abstract" boxHeight="90" 
            style="width: 682px; height: 60px; margin-top: -1px; border: 1px solid #CCCCCC;"><x:out select="$myoutput/data/project/@zh_abstract" escapeXml="false" />
        </textarea>
      </div>
      <p class="f666">
        <s:text name="projectEdit.label.english" />
        <label class="title" for="_project_en_abstract" style="display: none"> <s:text
            name="projectEdit.label.en_abstract" />
        </label>
      </p>
      <p>
      <div>
        <textarea class="inp_textarea" name="/project/@en_abstract" id="_project_en_abstract" boxHeight="90"
            style="width: 682px; height: 60px; margin-top: -1px; border: 1px solid #CCCCCC;"><x:out select="$myoutput/data/project/@en_abstract" escapeXml="false" />
        </textarea>
      </div>
      </p>
    </dd>
  </dl>
</li>