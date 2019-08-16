<p><#if (project_no != '')>项目编号：${project_no}</#if></p>
<#if (member_zh_full_name)??><#if (member_zh_full_name != '')><span style="word-break:break-word;"  class="pub_author_list_span"  alt="${project_no}" >${member_zh_full_name}</span><br/></#if></#if>
<span id="title_${project_no}"  class="pubGridTitle"  style="word-break:break-word;cursor:pointer;" >
<span class="notPrintLinkSpan"  style="cursor:pointer"  onclick="javascript:ProjectView.viewInsPrjDetail('${prj_des3_id},${node_id}',this);" ><font color="#005eac">${project_zh_title}</font></span>
</span>&#160;<br/>
<span class="maintBriefDesc" ><#if (project_zh_source)??>${project_zh_source}</#if></span>&#160;