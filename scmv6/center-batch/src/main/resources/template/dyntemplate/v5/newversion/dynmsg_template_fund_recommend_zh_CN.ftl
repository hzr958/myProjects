<li>
	<div class="ui_avatar">
		<span><img width="50" height="50" border="0" src="/resscmwebsns/images_v5/sm_logo.gif"></span>
	</div>
	<div class="publish-nr">
		<div class="moving-contat">
			<p>向您推荐以下科研基金机会：</p>
		</div>
		 <#list psnFundReFormList as fundRecom>
			<div class="txt_box"> 
				<p><a href="/scmwebsns/fund/viewcat?id=${fundRecom.catDes3Id}" class="Blue"  target="_blank">${fundRecom.agencyViewName}--${fundRecom.categoryViewName}</a></p>
			</div>
		</#list>
		<div class="appraisa-choose">
			<p>
				<a href="/scmwebsns/fundapp/commend?menuId=4" class="Blue" target="_blank">查看更多</a>
			</p>
		</div>
	</div>
</li>