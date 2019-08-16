<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%--SCM00010570-MJG-20120416-修改textarea标签的显示内容 --%>
<%-- <div class="fill pub" style="width:920px; float:left; margin-top:0px; margin-bottom:0px;">
		<label class="title"><s:text name="projectEdit.label.ckeywords" /><s:text name="colon.all" /></label>
		<div style="width:550px;min-height:30px; overflow:auto;float:left;" id="zh_auto_disckey_outer_div" class="auto_disckey_outer_div">
		<c:set var="zh_keywords"><x:out select="$myoutput/data/project/@zh_keywords" /></c:set>
		<c:if test="${zh_keywords !=''}">
			<c:set var="zh_keywordArray" value="${fn:split(zh_keywords,',|;')}"/>
						<c:forEach items="${zh_keywordArray}" varStatus="loop">
							<div class="auto_disckey_div">
								<label class="auto_disckey_keywords">${zh_keywordArray[loop.index]}</label>
								<a class="auto_disckey_div_del" onclick="ScholarAutoDisc.del_disckey_div(event)" style="cursor: pointer;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>
							</div>
						</c:forEach>
				</c:if>
			<input name="input" type="text" class="auto_disckey_input" size="1" maxlength="100" style="background:none"/>
		</div>
		<input name="/project/@zh_keywords" id="_project_zh_keywords" type="text" value="<x:out select="$myoutput/data/project/@zh_keywords" />" class="textbox" maxlength="400" style="display:none;width:500px" /> 
		(<s:text name="projectEdit.label.ekeywords_tips"/>)
	</div>
	<div class="fill pub" style="width:920px; float:left; margin-top:0px; margin-bottom:0px;">
		<label class="title"><s:text name="projectEdit.label.ekeywords" /><s:text name="colon.all" /></label>
		<div style="width:550px;min-height:30px; overflow:auto;float:left;" id="eh_auto_disckey_outer_div" class="auto_disckey_outer_div">
		<c:set var="en_keywords"><x:out select="$myoutput/data/project/@en_keywords" /></c:set>
		<c:if test="${en_keywords !=''}">
			<c:set var="en_keywordArray" value="${fn:split(en_keywords,',|;')}"/>
						<c:forEach items="${en_keywordArray}" varStatus="loop">
							<div class="auto_disckey_div">
								<label class="auto_disckey_keywords">${en_keywordArray[loop.index]}</label>
								<a class="auto_disckey_div_del" onclick="ScholarAutoDisc.del_disckey_div(event)" style="cursor: pointer;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>
							</div>
						</c:forEach>
				</c:if>
			<input name="input" type="text" class="auto_disckey_input" size="1" maxlength="100" style="background:none"/>
		</div>
		<input name="/project/@en_keywords" id="_project_en_keywords" type="text" size="100" value="<x:out select="$myoutput/data/project/@en_keywords" />" class="textbox" maxlength="400"  style="display:none;width:500px" /> 
		(<s:text name="projectEdit.label.ekeywords_tips"/>)
	</div> --%>
<li>
  <dl>
    <dd class="info_name">
      <s:text name="projectEdit.label.ckeywords" />
      <s:text name="colon.all" />
    </dd>
    <dd>
      <div id="_project_zh_keywords_div" class="py_list" style="width: 690px;"></div>
      &nbsp;&nbsp;
      <s:text name="projectEdit.label.ekeywords_tips" />
      <input name="/project/@zh_keywords" id="_project_zh_keywords" type="text"
        value="<x:out select="$myoutput/data/project/@zh_keywords" />" class="textbox" maxlength="400"
        style="display: none; width: 600px" />
    </dd>
  </dl>
</li>
<li>
  <dl>
    <dd class="info_name">
      <s:text name="projectEdit.label.ekeywords" />
      <s:text name="colon.all" />
    </dd>
    <dd>
      <div id="_project_en_keywords_div" class="py_list" style="width: 690px;"></div>
      &nbsp;&nbsp;
      <s:text name="projectEdit.label.ekeywords_tips" />
      <input name="/project/@en_keywords" id="_project_en_keywords" type="text" size="100"
        value="<x:out select="$myoutput/data/project/@en_keywords" />" class="textbox" maxlength="400"
        style="display: none; width: 600px" />
  </dl>
</li>