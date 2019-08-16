
<#if (representPubList)??>
<#list representPubList as pubItem>
<Relationship Id="rId${pubItem.wordHrefSeq}" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/hyperlink" Target="${pubItem.pubIndexUrl}" TargetMode="External"/>
</#list>
</#if>

<#if (otherPubList)??>
<#list otherPubList as pubItem>
<Relationship Id="rId${pubItem.wordHrefSeq}" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/hyperlink" Target="${pubItem.pubIndexUrl}" TargetMode="External"/>
</#list>
</#if>

<#if (prjList)??>
<#list prjList as prjItem>
<Relationship Id="rId${prjItem.wordHrefSeq}" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/hyperlink" Target="${prjItem.prjUrl}" TargetMode="External"/>
</#list>
</#if>
