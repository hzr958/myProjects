<li>
	<div class="ui_avatar">
		<span><img width="50" height="50" border="0" src="/resscmwebsns/images_v5/sm_logo_en.gif"></span>
	</div>
	<div class="publish-nr">
		<div class="moving-contat">
			<p>Funding opportunities you may be interested in:</p>
		</div>
		 <#list psnFundReFormList as fundRecom>
			<div class="txt_box"> 
				<p><a href="/scmwebsns/fund/viewcat?id=${fundRecom.catDes3Id}" class="Blue"  target="_blank">${fundRecom.agencyViewNameEn}--${fundRecom.categoryViewNameEn}</a></p>
			</div>
		</#list>
		<div class="appraisa-choose">
			<p>
				<a href="/scmwebsns/fundapp/commend?menuId=4" class="Blue" target="_blank">View More</a>
			</p>
		</div>
	</div>
</li>