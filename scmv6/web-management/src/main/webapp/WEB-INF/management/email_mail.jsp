<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<link rel="stylesheet" type="text/css" href="${resmod}/css/public.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css/common.css" />
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/management/psn.management.js"></script>
<script type="text/javascript">
$("document").ready(function() {
	var data ={
			"typeId":$("#typeId").val(),
			"psnEmail":$("#mailAddress").val(),
			"psnId":$("#psnId").val()
	}
	scmmanagement.ajaxEmailInfoList("/scmmanagement/emailInfo/ajaxEmailInfoList" ,data);  
});
</script>
<div class="pop_box" style="border: none;">
  <div class="rol_search">
    <input type="hidden" value="1" class="typeId" id="typeId" />
    </td> <input type="hidden" value="${psnEmail}" class="mailAddress" id="mailAddress" />
    </td> <input type="hidden" value="${psnId}" class="psnId" id="psnId" />
    </td>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tbody>
        <tr>
        <tr>
          <td width="10%" align="center"><s:text name="发送时间：" /></td>
          <td width="20%" align="center"><select id="email_date" class="input_unit queryUnitId_inp inp_text"
            style="width: 150px;" onchange="switchEmailDate(this);">
              <option value="1" selected="selected">一个月前</option>
              <option value="2">两个月前</option>
              <option value="3">三个月前</option>
          </select> <!-- <input type="text" id="queryUnitId_in" class="input_unit queryUnitId_inp inp_text"   value="一个月前" /> -->
          </td>
          <td width="10%" align="center"><s:text name="至" /></td>
          <td align="30%" align="center"><input type="text" class="inp_text" name="psnName" id="psnName"
            value="${nowDate}" style="width: 150px" disabled="disabled" /></td>
        </tr>
        </tr>
      </tbody>
    </table>
  </div>
  <div id="main-column"></div>
</div>
</body>
