<p><#if (project_no!='')>Project No.：${project_no}</#if></p>
<#if (member_en_full_name)??><#if (member_en_full_name != '')><span style="word-break:break-word;"  class="pub_author_list_span"  alt="${project_no}" >${member_en_full_name}</span><br/></#if></#if>
<span id="title_${project_no}"  class="pubGridTitle"  style="word-break:break-word;cursor:pointer;" >
<span class="notPrintLinkSpan"  style="cursor:pointer"  onclick="javascript:ProjectView.viewInsPrjDetail('${prj_des3_id},${node_id}',this);" ><font color="#005eac">${project_en_title}</font></span>
</span>&#160;<br/>
<span class="maintBriefDesc" ><#if (project_en_brief)??>${project_en_brief}</#if></span>&#160;