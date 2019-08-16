<#if (workEduData.items?size>0)>
<w:p wsp:rsidR="002334C1" wsp:rsidRPr="008132DD"
					wsp:rsidRDefault="002334C1" wsp:rsidP="00D35AC0">
					<w:pPr>
						<w:pStyle w:val="a7" />
						<w:ind w:left-chars="81" w:left="170" />
						<w:rPr>
							<w:rFonts w:ascii="Arial" w:h-ansi="Arial" w:cs="Arial" />
							<wx:font wx:val="Arial" />
							<w:b />
							<w:sz w:val="24" />
							<w:sz-cs w:val="24" />
							<w:lang w:fareast="ZH-HK" />
						</w:rPr>
					</w:pPr>
					<w:r wsp:rsidRPr="008132DD">
						<w:rPr>
							<w:rFonts w:ascii="Arial" w:h-ansi="Arial" w:cs="Arial" />
							<wx:font wx:val="Arial" />
							<w:b />
							<w:sz w:val="24" />
							<w:sz-cs w:val="24" />
							<w:lang w:fareast="ZH-HK" />
						</w:rPr>
						<w:t>${workEduData.title}</w:t>
					</w:r>
				</w:p>
				<#list workEduData.items as workEduItem>
				<w:p wsp:rsidR="002334C1" wsp:rsidRPr="008132DD"
					wsp:rsidRDefault="00C12FAC" wsp:rsidP="008132DD">
					<w:pPr>
						<w:pStyle w:val="a7" />
						<w:spacing w:before-lines="50"<#if !workEduItem_has_next> w:after-lines="100"</#if> />
						<w:ind w:left-chars="81" w:left="170" />
						<w:rPr>
							<w:rFonts w:ascii="Arial" w:h-ansi="Arial" w:cs="Arial" />
							<wx:font wx:val="Arial" />
							<w:noProof />
							<w:sz w:val="20" />
							<w:sz-cs w:val="20" />
						</w:rPr>
					</w:pPr>
					<#if workEduItem_index == 0>
					<w:r wsp:rsidRPr="008132DD">
						<w:rPr>
							<w:rFonts w:ascii="Arial" w:h-ansi="Arial" w:cs="Arial" />
							<wx:font wx:val="Arial" />
							<w:noProof />
							<w:sz w:val="20" />
							<w:sz-cs w:val="20" />
						</w:rPr>
						<w:pict>
							<v:shape id="_x0000_s1028" type="#_x0000_t32"
								style="position:absolute;left:0;text-align:left;margin-left:8.1pt;margin-top:2.05pt;width:510.15pt;height:0;z-index:2"
								o:connectortype="straight" strokecolor="#d8d8d8" />
						</w:pict>
					</w:r>
					</#if>
					<w:r wsp:rsidR="002334C1" wsp:rsidRPr="008132DD">
						<w:rPr>
							<w:rFonts w:ascii="Arial" w:h-ansi="Arial" w:cs="Arial" />
							<wx:font wx:val="Arial" />
							<w:noProof />
							<w:sz w:val="20" />
							<w:sz-cs w:val="20" />
						</w:rPr>
						<w:t>${workEduItem}</w:t>
					</w:r>
				</w:p>
				</#list>
				</#if>