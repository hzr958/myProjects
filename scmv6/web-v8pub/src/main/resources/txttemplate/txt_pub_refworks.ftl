RT ${pubTypeName}
T1 ${pubTitle}
<#if (authors)??>
<#list authors as authorName>
A1 ${authorName}
</#list>
</#if>
<#if (sourceUrl)?? && sourceUrl != "">
UL ${sourceUrl}
</#if>
<#if (abstract)?? && abstract != "">
AB ${abstract}
</#if>
<#if (publishYear)?? && publishYear != "">
YR ${publishYear}
</#if>
<#if (volume)?? && volume != "">
VO ${volume}
</#if>
<#if (issue)?? && issue != "">
IS ${issue}
</#if>
<#if (startPage)?? && startPage != "">
SP ${startPage}
</#if>
<#if (endPage)?? && endPage != "">
OP ${endPage}
</#if>
<#if (proceedingTitle)?? && proceedingTitle != "">
T2 ${proceedingTitle}
</#if>
<#if (confVenue)?? && confVenue != "">
PP ${confVenue}
</#if>
<#if (original)?? && original != "">
JF ${original}
</#if>
<#if (keywords)??>
<#list keywords as key>
K1 ${key}
</#list>
</#if>
<#if (doi)?? && doi != "">
DO ${doi}
</#if>
<#if (articleNumber)?? && articleNumber != "">
AN ${articleNumber}
</#if>