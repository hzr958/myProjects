<li class="comfirePub_${dtid?c}">
	<div class="ui_avatar">
		<span><img width="50" height="50" border="0" src="/resscmwebsns/images_v5/sm_logo.gif"></span>
	</div>
	<div class="publish-nr">
		<div class="moving-contat">
			<p>您可能是以下论文的作者：</p>
		</div>
		<div class="txt_box">
			<p>${authors}，<a href="###" class="Blue" onclick="viewPubDetail('${des3Id}',${insId?c})">${pubTitle}</a>
			<#if (source?exists)>
			，${source}
			</#if>
			</p>
		</div>
		<div class="appraisa-choose">
			<p>
				<a href="###" class="Blue" onclick="comfirePub('${dtid?c}')">确认成果</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="/scmwebsns/pubconfirm/publist" class="Blue" target="_blank">查看更多</a>
			</p>
		</div>
	</div>
</li>