 <#if (msgTitle?exists)  >  
<a href="#" onclick="showShareList('${sid}','${rid}',this)">${psnName}分享了${total}条
<#switch type>
  <#case '1'>
     成果<input type="hidden" id="shareType" name="shareType" value="${type}"/>
     <#break>
  <#case '2'>
     文献<input type="hidden" id="shareType" name="shareType" value="${type}"/>
     <#break>
  <#case '3'>
     文件<input type="hidden" id="shareType" name="shareType" value="${type}"/>
      <#break>
  <#case '4'>
     文件<input type="hidden" id="shareType" name="shareType" value="${type}"/>
      <#break>    

  <#default>
     其他
</#switch>  
</a>
</#if>
  
  