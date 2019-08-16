<td width="10%" align="center">
<a target="_blank" href="${psnUrlOutside!'#'}">
    <img src="${avatars!'#'}" width="50" height="50" />
    </a>
</td>
<td align="left">
    <p class="f666">
        <a target="_blank"  href="${psnUrlOutside!'#'}" class="Blue mright10">${zhName!''}</a>
    </p>
     <#if (positionEn?exists || affiliatedIns?exists)>
	    <p>
	        ${affiliatedIns!''}<#if (affiliatedIns?exists) && (positionEn?exists) && (positionEn!="")>; </#if>  ${positionEn!''}
	    </p>
    </#if>
    <a title="赞" style="cursor: pointer;display: none;" onclick="PubPsnAward.award(this,'${desPsnId }')" psnId="${psnId?c }" class="f888 mright10" id="award_${psnId?c }">赞
		<b class="award_num_span_${psnId?c }" style="display: none;">(<b class="award_num_${psnId?c }"></b>)</b>
	</a>
	<a title="取消赞" style="cursor: pointer;display: none;" onclick="PubPsnAward.cancelAward(this,'${desPsnId }')" psnId="${psnId?c }" class="f888 mright10" id="cancelaward_${psnId?c }">赞
		<b class="award_num_span_${psnId?c }" style="display: none;">(<b class="award_num_${psnId?c }"></b>)</b>
	</a>
	<input type="hidden" class="psn_award_p_d" psnId="${psnId?c }" />
	<span class="mright10 fe6e6">|</span>
	<b class="public_pulldown">
	    <a class="f888 mright10 share_sites_show">分享<i class="publication-up"></i></a>
	    <a class="share_pull" style="display:none" resId="${psnId?c }" resTitle="${zhName!''}<#if (affiliatedIns?exists) || (positionEn?exists)>&nbsp;</#if>${affiliatedIns!''}<#if (positionEn?exists) && (affiliatedIns?exists)>; </#if>${positionEn!''}" resPic="${avatars!'#'}" resUrl="${psnUrlOutside!'#'}"></a>
    </b>
</td>
