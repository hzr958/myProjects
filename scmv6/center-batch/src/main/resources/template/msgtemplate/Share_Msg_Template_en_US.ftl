<#if (msgTitle?exists)  >  
<a href="javascript:void(0);" onclick="showShareList('${sid}','${rid}',this)">${psnName} share  ${total}&nbsp;
<#switch type>
  <#case 2>
     Reference(s)<input type="hidden" id="shareType" name="shareType" value="${type}"/>
     <#break>
  <#case 3>
     File(s)<input type="hidden" id="shareType" name="shareType" value="${type}"/>
      <#break>
  <#case 4>
     Project(s)<input type="hidden" id="shareType" name="shareType" value="${type}"/>
     <#break>    
  <#case 1>
     Publication(s)<input type="hidden" id="shareType" name="shareType" value="${type}"/>
     <#break>
  <#default>
     Others
</#switch>  
&nbsp;to you
</a>
</#if>
  
  