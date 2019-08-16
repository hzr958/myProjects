<#if (pubPrjData.objectContent?size>0)>
<w:p wsp:rsidR="00D92970" wsp:rsidRPr="00D35AC0"
	wsp:rsidRDefault="002334C1" wsp:rsidP="00D35AC0">
	<w:pPr>
		<w:pStyle w:val="a7" />
		<w:ind w:left-chars="81" w:left="170" />
		<w:rPr>
			<w:rFonts w:ascii="宋体" w:h-ansi="宋体" />
			<wx:font wx:val="宋体" />
			<w:b />
			<w:sz w:val="24" />
			<w:sz-cs w:val="24" />
			<w:lang w:fareast="ZH-CN" />
		</w:rPr>
	</w:pPr>
	<w:r wsp:rsidRPr="00D35AC0">
		<w:rPr>
			<w:rFonts w:ascii="宋体" w:h-ansi="宋体" w:hint="fareast" />
			<wx:font wx:val="宋体" />
			<w:b />
			<w:sz w:val="24" />
			<w:sz-cs w:val="24" />
			<w:lang w:fareast="ZH-HK" />
		</w:rPr>
		<w:t>${pubPrjData.title}</w:t>
	</w:r>
</w:p>
<#list pubPrjData.objectContent as pubPrjItem>
<w:p wsp:rsidR="002334C1" wsp:rsidRPr="00D35AC0"
	wsp:rsidRDefault="008D3E21" wsp:rsidP="00D35AC0">
	<w:pPr>
		<w:pStyle w:val="a7" />
		<w:spacing w:before-lines="50"<#if !pubPrjItem_has_next> w:after-lines="100"</#if>/>
		<w:ind w:left-chars="81" w:left="170" />
		<w:rPr>
			<w:rFonts w:ascii="宋体" w:h-ansi="宋体" />
			<wx:font wx:val="宋体" />
			<w:noProof />
			<w:sz w:val="18" />
			<w:sz-cs w:val="18" />
		</w:rPr>
	</w:pPr>
	<#if pubPrjItem_index == 0>
	<w:r wsp:rsidRPr="00D35AC0">
		<w:rPr>
			<w:rFonts w:ascii="宋体" w:h-ansi="宋体" />
			<wx:font wx:val="宋体" />
			<w:noProof />
            <w:sz w:val="18" />
			<w:sz-cs w:val="18" />
		</w:rPr>
		<w:pict>
			<v:shape id="_x0000_s1029" type="#_x0000_t32"
				style="position:absolute;left:0;text-align:left;margin-left:8.1pt;margin-top:1.6pt;width:510.15pt;height:0;z-index:4"
				o:connectortype="straight" strokecolor="#d8d8d8" />
		</w:pict>
	</w:r>
	</#if>
	<#if pubPrjItem.author0!="">
	<w:r wsp:rsidR="002334C1" wsp:rsidRPr="00D35AC0">
		<w:rPr>
			<w:rFonts w:ascii="宋体" w:h-ansi="宋体" />
			<wx:font wx:val="宋体" />
			<w:noProof />
            <w:sz w:val="18" />
			<w:sz-cs w:val="18" />
		</w:rPr>
		<w:t>${pubPrjItem.author0}</w:t>
	</w:r>
	</#if>
	<#if pubPrjItem.author1!="">
	<w:r wsp:rsidR="002334C1" wsp:rsidRPr="00D35AC0">
		<w:rPr>
			<w:rFonts w:ascii="宋体" w:h-ansi="宋体" />
			<wx:font wx:val="宋体" />
			<w:noProof />
			<w:b />
            <w:sz w:val="18" />
			<w:sz-cs w:val="18" />
		</w:rPr>
		<w:t>${pubPrjItem.author1}</w:t>
	</w:r>
	</#if>
	<#if pubPrjItem.author2!="">
	<w:r wsp:rsidR="002334C1" wsp:rsidRPr="00D35AC0">
		<w:rPr>
			<w:rFonts w:ascii="宋体" w:h-ansi="宋体" />
			<wx:font wx:val="宋体" />
			<w:noProof />
            <w:sz w:val="18" />
			<w:sz-cs w:val="18" />
		</w:rPr>
		<w:t>${pubPrjItem.author2}</w:t>
	</w:r>
	</#if>
	<w:hlink
		w:dest="${pubPrjItem.linkUrl}"
		w:target="_blank">
		<w:r wsp:rsidR="002334C1" wsp:rsidRPr="00D35AC0">
			<w:rPr>
				<w:rFonts w:ascii="宋体" w:h-ansi="宋体" />
				<wx:font wx:val="宋体" />
				<w:noProof />
				<w:color w:val="4F81BD" />
                <w:sz w:val="18" />
			    <w:sz-cs w:val="18" />
			</w:rPr>
			<w:t>${pubPrjItem.title}</w:t>
		</w:r>
	</w:hlink>
	<w:r wsp:rsidR="002334C1" wsp:rsidRPr="00D35AC0">
		<w:rPr>
			<w:rFonts w:ascii="宋体" w:h-ansi="宋体" />
			<wx:font wx:val="宋体" />
			<w:noProof />
            <w:sz w:val="18" />
			<w:sz-cs w:val="18" />
		</w:rPr>
		<w:t>${pubPrjItem.briefDesc}</w:t>
	</w:r>
</w:p>
</#list>
</#if>