<script type="text/javascript">
  	function tosubmits(p){
  	  	if(!/\d+/g.test(p))
  	  	  	p = 1;
  		$("#pageNo").attr("value",p);
  		$("#mainForm").submit();				
  	}
  	function topage(){
  	  	var toPage =  $("#toPage").attr("value")
  		if(!/\d+/g.test(toPage))
  			toPage = 1;
  		$("#pageNo").attr("value",toPage);
  		$("#mainForm").submit();
  	}
  	function resetPageNo(){
  		$("#pageNo").attr("value",1);
  	}
  	
</script>
<div class="page_NO">
  <input name="page.pageNo" id="pageNo" type="hidden" value="${page.pageNo }" /> <span class="page_img"> <s:if
      test="page.pageNo>1">
      <a href="#" onclick="tosubmits(1);"><img src="${res}/images/btn_first_page.gif" alt="" /></a>&nbsp;
			</s:if> <s:else>
      <img src="${res}/images/btn_first_pagegray.gif" alt="" />&nbsp;
			</s:else> <s:if test="page.isHasPre()">
      <a href="#" onclick="tosubmits('${page.pageNo-1}');"><img src="${res}/images/btn_prev_page.gif" alt="" /></a>&nbsp;
			</s:if> <s:else>
      <img src="${res}/images/btn_prev_pagegray.gif" alt="" />&nbsp;
			</s:else> <s:if test="page.isHasNext()">
      <a href="#" onclick="tosubmits('${page.pageNo+1}');"><img src="${res}/images/btn_next_page.gif" alt="" /></a>&nbsp;
			</s:if> <s:else>
      <img src="${res}/images/btn_next_pagegray.gif" alt="" />&nbsp;
			</s:else> <s:if test="page.isHasNext()">
      <a href="#" onclick="tosubmits('${page.lastPage}');"><img src="${res}/images/btn_last_page.gif" alt="" /></a> &nbsp; 
			</s:if> <s:else>
      <img src="${res}/images/btn_last_pagegray.gif" alt="" />&nbsp;</s:else> <s:text name='page.toPage' /> <input id="toPage"
    name="toPage" style="width: 20px;" onkeyup="this.value=this.value.replace(/\D/g,'')" maxlength="5" type="text"
    value="${page.pageNo }" /> <input type="button" value="<s:text name='page.jump' />" class="button_go"
    style="cursor: pointer" onclick="topage();" /> &nbsp; <s:text name='page.totalPages'>
      <s:param>${page.totalPages}</s:param>
    </s:text>   &nbsp;${page.totalCount} <s:text name='page.findSize' /> &nbsp;<s:text name='page.pageShow1' /> <s:select
      list="#{10:10,20:20,50:50}" name="page.pageSize" listKey="key" listValue="value" value="page.pageSize"
      onchange="tosubmits('');" /> <%--s:text name='page.pageSize' /> &nbsp;<a href="#" title="<s:text name='page.showTo' />" class="xls" > </a>
			< a href="#" title="<s:text name='page.putPage' />" class="print" > &nbsp; </a>--%>
  </span>
  <%-- span class="info"><s:text name='page.find' />${page.totalCount}<s:text name='page.findSize' />,
		 <s:text name='page.show' /><s:if test="page.pageCount>=1">${page.first}</s:if><s:else>0</s:else><s:text name='page.to' />${page.first+page.pageCount-1}</span>--%>
</div>