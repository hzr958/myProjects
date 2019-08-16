<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:choose>
  <c:when test="${lang=='zh' }">
    <input class="inp_text" style="width: 40px; ime-mode: disabled;" maxlength="4" value="${start_year }"
      id="${dateInputTagId }start_year" name="${dateInputTagName }start_year" />/ <input class="inp_text"
      style="width: 20px; ime-mode: disabled;" maxlength="2" value="${start_month }" id="${dateInputTagId }start_month"
      name="${dateInputTagName}start_month" />/ <input class="inp_text" style="width: 20px; ime-mode: disabled;"
      maxlength="2" value="${start_day }" name="${dateInputTagName}start_day" id="${dateInputTagId }start_day" />
  </c:when>
  <c:otherwise>
    <input class="inp_text" style="width: 20px; ime-mode: disabled;" maxlength="2" value="${start_day }"
      id="${dateInputTagId }start_day" name="${dateInputTagName}start_day" />/ <input class="inp_text"
      style="width: 20px; ime-mode: disabled;" maxlength="2" value="${start_month }" id="${dateInputTagId }start_month"
      name="${dateInputTagName}start_month" />/ <input class="inp_text" style="width: 40px; ime-mode: disabled;"
      maxlength="4" value="${start_year }" id="${dateInputTagId }start_year" name="${dateInputTagName}start_year" />
  </c:otherwise>
</c:choose>
<s:text name="projectEdit.label.period_seperator" />
<c:choose>
  <c:when test="${lang=='zh' }">
    <input class="inp_text" style="width: 40px; ime-mode: disabled;" maxlength="4" value="${end_year }"
      id="${dateInputTagId }end_year" name="${dateInputTagName}end_year" />/ <input class="inp_text"
      style="width: 20px; ime-mode: disabled;" maxlength="2" value="${end_month }" id="${dateInputTagId }end_month"
      name="${dateInputTagName}end_month" />/ <input class="inp_text" style="width: 20px; ime-mode: disabled;"
      maxlength="2" value="${end_day }" id="${dateInputTagId }end_day" name="${dateInputTagName}end_day" />
  </c:when>
  <c:otherwise>
    <input class="inp_text" style="width: 20px; ime-mode: disabled;" maxlength="2" value="${end_day }"
      id="${dateInputTagId }end_day" name="${dateInputTagName}end_day" />/ <input class="inp_text"
      style="width: 20px; ime-mode: disabled;" maxlength="2" value="${end_month }" id="${dateInputTagId }end_month"
      name="${dateInputTagName}end_month" />/ <input class="inp_text" style="width: 40px; ime-mode: disabled;"
      maxlength="4" value="${end_year }" id="${dateInputTagId }end_year" name="${dateInputTagName}end_year" />
  </c:otherwise>
</c:choose>
<c:choose>
  <c:when test="${lang=='zh' }">
	&nbsp;(<s:text name="all.msg.eg" /> 2007/6/21 - 2007/7/14 <s:text name="projectEdit.label.or" /> 2007/6 - 2007/9)
	</c:when>
  <c:otherwise>
	&nbsp;(<s:text name="all.msg.eg" /> 20/12/2007 - 25/12/2007 <s:text name="projectEdit.label.or" /> 12/2007 - 02/2008)
	</c:otherwise>
</c:choose>