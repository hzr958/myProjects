<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function viewDetail(des3PubId){
  var pram = {
      "des3PubId" : des3PubId
    };
    var newwindow = window.open("about:blank");
    $.ajax({
      url : "/pub/details/ajaxpdwhview",
      type : "post",
      data : pram,
      dataType : "json",
      success : function(data) {
        if (data.result == 2) {
          newwindow.location.href = data.shortUrl;
        } else {
          newwindow.location.href = "/pub/details/pdwh?des3PubId=" + encodeURIComponent(des3PubId);
        }
      },
      error : function() {
      }
    });
}
</script>
<table id="psn_list" width="100%" border="0" cellspacing="0" cellpadding="1" class="t_css">
  <s:iterator value="pubInfoList" var="pubInfo">
    <tr class="line_1">
      <%-- <td width="3%" align="center"><input type="checkbox"
				name="ck_psnId" class="ck_psnId" value="${psnId}" /> 
				<input type="hidden" value="${psnId}" class="psnId" id="psnId" /></td> --%>
      <td width="8%" align="center">
      <input type="checkbox" name="ck_psnId" class="ck_psnId" value="<iris:des3 code='${pubInfo.pubId}'/>" /> 
      <input type="hidden" value="${pubInfo.pubId}" class="pubId" id="pubId" /></td>
      <td width="10%" align="left">
          <s:property value='#pubInfo.pubId' />
      </td>
      <td width="32%" align="left">
         <span style="cursor:pointer;" onclick="viewDetail('<iris:des3 code='${pubInfo.pubId}'/>')"> <s:property value='#pubInfo.title' /></span>
      </td>
      <td width="20%" align="left">
        <s:property value='#pubInfo.briefDesc' />
      </td>
      <td width="30%" align="left">
          <s:property value='#pubInfo.authorNames' />
      </td>
    </tr>
  </s:iterator>
</table>
<s:include value="/common/pub-page-tages.jsp"></s:include>
