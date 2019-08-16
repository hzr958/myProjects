	<w:tbl>
	<w:tblPr>
		<w:tblW w:w="10352" w:type="dxa" />
		<w:tblBorders>
			<w:top w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0"
				w:color="BFBFBF" />
			<w:left w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0"
				w:color="BFBFBF" />
			<w:bottom w:val="single" w:sz="4" wx:bdrwidth="10"
				w:space="0" w:color="BFBFBF" />
			<w:right w:val="single" w:sz="4" wx:bdrwidth="10"
				w:space="0" w:color="BFBFBF" />
			<w:insideH w:val="single" w:sz="4" wx:bdrwidth="10"
				w:space="0" w:color="BFBFBF" />
			<w:insideV w:val="single" w:sz="4" wx:bdrwidth="10"
				w:space="0" w:color="BFBFBF" />
		</w:tblBorders>
		<w:tblLayout w:type="Fixed" />
		<w:tblCellMar>
			<w:left w:w="28" w:type="dxa" />
			<w:right w:w="85" w:type="dxa" />
		</w:tblCellMar>
		<w:tblLook w:val="04A0" />
	</w:tblPr>
	<w:tblGrid>
		<w:gridCol w:w="10352" />
		<w:gridCol w:w="10352" />
	</w:tblGrid>
	<w:tr wsp:rsidR="00D20A63" wsp:rsidRPr="00D35AC0" wsp:rsidTr="00D35AC0">
		<w:trPr>
			<w:trHeight w:val="2259" />
		</w:trPr>
		<w:tc>
			<w:tcPr>
				<w:tcW w:w="2019" w:type="dxa" />
				<w:tcBorders>
					<w:right w:val="single" w:sz="4" wx:bdrwidth="10"
						w:space="0" w:color="FFFFFF" />
				</w:tcBorders>
			</w:tcPr>
			<w:p wsp:rsidR="00D20A63" wsp:rsidRPr="00D35AC0"
				wsp:rsidRDefault="00D35AC0" wsp:rsidP="00D35AC0">
				<w:pPr>
					<w:jc w:val="center" />
					<w:rPr>
						<w:rFonts w:ascii="Calibri" w:fareast="宋体" w:h-ansi="Calibri"
							w:cs="Times New Roman" />
					</w:rPr>
				</w:pPr>
				<w:r wsp:rsidRPr="00D35AC0">
					<w:rPr>
						<w:rFonts w:ascii="Calibri" w:fareast="宋体" w:h-ansi="Calibri"
							w:cs="Times New Roman" />
						<w:noProof />
					</w:rPr>
					<w:pict>
						<v:shapetype id="_x0000_t75" coordsize="21600,21600"
							o:spt="75" o:preferrelative="t" path="m@4@5l@4@11@9@11@9@5xe"
							filled="f" stroked="f">
							<v:stroke joinstyle="miter" />
							<v:formulas>
								<v:f eqn="if lineDrawn pixelLineWidth 0" />
								<v:f eqn="sum @0 1 0" />
								<v:f eqn="sum 0 0 @1" />
								<v:f eqn="prod @2 1 2" />
								<v:f eqn="prod @3 21600 pixelWidth" />
								<v:f eqn="prod @3 21600 pixelHeight" />
								<v:f eqn="sum @0 0 1" />
								<v:f eqn="prod @6 1 2" />
								<v:f eqn="prod @7 21600 pixelWidth" />
								<v:f eqn="sum @8 21600 0" />
								<v:f eqn="prod @7 21600 pixelHeight" />
								<v:f eqn="sum @10 21600 0" />
							</v:formulas>
							<v:path o:extrusionok="f" gradientshapeok="t"
								o:connecttype="rect" />
							<o:lock v:ext="edit" aspectratio="t" />
						</v:shapetype>
						<w:binData w:name="wordml://${avatarName}" xml:space="preserve">${base64Avatars}</w:binData>
						<v:shape id="圖片 25" o:spid="_x0000_i1028" type="#_x0000_t75"
							style="width:81.65pt;height:95.8pt;visibility:visible;mso-wrap-style:square">
							<v:imagedata src="wordml://${avatarName}" o:title=""
								cropbottom="5933f" />
						</v:shape>
					</w:pict>
				</w:r>
			</w:p>
		</w:tc>
		<w:tc>
			<w:tcPr>
				<w:tcW w:w="4983" w:type="dxa" />
				<w:tcBorders>
					<w:left w:val="single" w:sz="4" wx:bdrwidth="10"
						w:space="0" w:color="FFFFFF" />
				</w:tcBorders>
			</w:tcPr>
			<#if info0?? && info0!=''>
			<w:p wsp:rsidR="00D20A63" wsp:rsidRPr="00D35AC0"
				wsp:rsidRDefault="00D20A63" wsp:rsidP="004D5A78">
				<w:pPr>
				    <w:spacing w:before-lines="100"/>
				    <w:jc w:val="left" />
					<w:rPr>
						<w:rFonts w:ascii="宋体" w:h-ansi="宋体" w:cs="Times New Roman" />
						<wx:font wx:val="宋体" />
						<w:b />
						<w:sz w:val="24" />
						<w:sz-cs w:val="24" />
					</w:rPr>
				</w:pPr>
				<w:r wsp:rsidRPr="00D35AC0">
					<w:rPr>
						<w:rFonts w:ascii="宋体" w:h-ansi="宋体" w:cs="Times New Roman"
							w:hint="fareast" />
						<wx:font wx:val="宋体" />
						<w:b />
						<w:sz w:val="24" />
						<w:sz-cs w:val="24" />
					</w:rPr>
					<w:t>${info0}</w:t>
				</w:r>
			</w:p>
			</#if>
			<#if info1?? && info1!=''>
			<w:p wsp:rsidR="00D20A63" wsp:rsidRPr="00D35AC0"
				wsp:rsidRDefault="00D20A63" wsp:rsidP="00D35AC0">
				<w:pPr>
					<w:spacing w:after-lines="50" />
					<w:jc w:val="left" />
					<w:rPr>
						<w:rFonts w:ascii="宋体" w:h-ansi="宋体" w:cs="Times New Roman" />
						<wx:font wx:val="宋体" />
						<w:sz w:val="24" />
						<w:sz-cs w:val="24" />
					</w:rPr>
				</w:pPr>
				<w:r wsp:rsidRPr="00D35AC0">
					<w:rPr>
						<w:rFonts w:ascii="宋体" w:h-ansi="宋体" w:cs="Times New Roman"
							w:hint="fareast" />
						<wx:font wx:val="宋体" />
						<w:sz w:val="18" />
						<w:sz-cs w:val="18" />
					</w:rPr>
					<w:t>${info1}</w:t>
				</w:r>
			</w:p>
			</#if>
			<#if info2?? && info2!=''>
			<w:p wsp:rsidR="00D20A63" wsp:rsidRPr="00D35AC0"
				wsp:rsidRDefault="00D20A63" wsp:rsidP="004D5A78">
				<w:pPr>
				    <w:jc w:val="left" />
					<w:rPr>
						<w:rFonts w:ascii="Calibri" w:fareast="宋体" w:h-ansi="Calibri"
							w:cs="Times New Roman" />
					</w:rPr>
				</w:pPr>
				<w:r wsp:rsidRPr="00D35AC0">
					<w:rPr>
						<w:rFonts w:ascii="Calibri" w:fareast="宋体" w:h-ansi="Calibri"
							w:cs="Times New Roman" w:hint="fareast" />
					</w:rPr>
					<w:t></w:t>
				</w:r>
				<w:r wsp:rsidRPr="00D35AC0">
					<w:rPr>
						<w:rFonts w:ascii="Calibri" w:fareast="宋体" w:h-ansi="Calibri"
							w:cs="Times New Roman" w:hint="fareast" />
						<wx:font wx:val="宋体" />
						<w:sz w:val="18" />
						<w:sz-cs w:val="18" />
					</w:rPr>
					<w:t>${info2}</w:t>
				</w:r>
			</w:p>
			</#if>
			<#if info3?? && info3!=''>
			<w:p wsp:rsidR="00D20A63" wsp:rsidRPr="00D35AC0"
				wsp:rsidRDefault="00D20A63" wsp:rsidP="004D5A78">
				<w:pPr>
				    <w:jc w:val="left" />  
					<w:rPr>
						<w:rFonts w:ascii="Calibri" w:fareast="宋体" w:h-ansi="Calibri"
							w:cs="Times New Roman" />
					</w:rPr>
				</w:pPr>
				<w:r wsp:rsidRPr="00D35AC0">
					<w:rPr>
						<w:rFonts w:ascii="Calibri" w:fareast="宋体" w:h-ansi="Calibri"
							w:cs="Times New Roman" w:hint="fareast" />
						<wx:font wx:val="宋体" />
						<w:sz w:val="18" />
						<w:sz-cs w:val="18" />
					</w:rPr>
					<w:t>${info3}</w:t>
				</w:r>
			</w:p>
			</#if>
			<#if info4?? && info4!=''>
			<w:p wsp:rsidR="00D20A63" wsp:rsidRPr="00D35AC0"
				wsp:rsidRDefault="00D20A63" wsp:rsidP="004D5A78">
				<w:pPr>
				    <w:jc w:val="left" />
					<w:rPr>
						<w:rFonts w:ascii="Calibri" w:fareast="宋体" w:h-ansi="Calibri"
							w:cs="Times New Roman" />
					</w:rPr>
				</w:pPr>
				<w:r wsp:rsidRPr="00D35AC0">
					<w:rPr>
						<w:rFonts w:ascii="Calibri" w:fareast="宋体" w:h-ansi="Calibri"
							w:cs="Times New Roman" w:hint="fareast" />
						<wx:font wx:val="宋体" />
						<w:sz w:val="18" />
						<w:sz-cs w:val="18" />
					</w:rPr>
					<w:t>${info4}</w:t>
				</w:r>
			</w:p>
			</#if>
			<#if info5?? && info5!=''>
			<w:p wsp:rsidR="00D20A63" wsp:rsidRPr="00D35AC0"
				wsp:rsidRDefault="00D20A63" wsp:rsidP="004D5A78">
				<w:pPr>
				    <w:jc w:val="left" />
					<w:rPr>
						<w:rFonts w:ascii="Calibri" w:fareast="宋体" w:h-ansi="Calibri"
							w:cs="Times New Roman" />
					</w:rPr>
				</w:pPr>
				<w:r wsp:rsidRPr="00D35AC0">
					<w:rPr>
						<w:rFonts w:ascii="Calibri" w:fareast="宋体" w:h-ansi="Calibri"
							w:cs="Times New Roman" w:hint="fareast" />
						<wx:font wx:val="宋体" />
						<w:sz w:val="18" />
						<w:sz-cs w:val="18" />
					</w:rPr>
					<w:t>${info5}</w:t>
				</w:r>
			</w:p>
			</#if>
			<#if info6?? && info6!=''>
			<w:p wsp:rsidR="00D20A63" wsp:rsidRPr="00D35AC0"
				wsp:rsidRDefault="00D20A63" wsp:rsidP="004D5A78">
				<w:pPr>
				    <w:jc w:val="left" />
					<w:rPr>
						<w:rFonts w:ascii="Calibri" w:fareast="宋体" w:h-ansi="Calibri"
							w:cs="Times New Roman" />
					</w:rPr>
				</w:pPr>
				<w:r wsp:rsidRPr="00D35AC0">
					<w:rPr>
						<w:rFonts w:ascii="Calibri" w:fareast="宋体" w:h-ansi="Calibri"
							w:cs="Times New Roman" w:hint="fareast" />
						<wx:font wx:val="宋体" />
						<w:sz w:val="18" />
						<w:sz-cs w:val="18" />
					</w:rPr>
					<w:t>${info6}</w:t>
				</w:r>
			</w:p>
			</#if>
		</w:tc>
	</w:tr>
</w:tbl>