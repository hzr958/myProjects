
    <w:p w:rsidR="00280A7F" w:rsidRPr="0081039A" w:rsidRDefault="0046303F" w:rsidP="0081039A">
            <w:pPr>
              <w:spacing w:beforeLines="50" w:before="120"/>
              <w:rPr>
                <w:rFonts w:ascii="楷体" w:eastAsia="楷体" w:hAnsi="楷体" w:cs="宋体" w:hint="eastAsia"/>
                <w:color w:val="0070C0"/>
                <w:sz w:val="28"/>
                <w:szCs w:val="28"/>
              </w:rPr>
            </w:pPr>
            <w:r>
              <w:rPr>
                <w:rFonts w:ascii="楷体" w:eastAsia="楷体" w:hAnsi="楷体" w:cs="宋体" w:hint="eastAsia"/>
                <w:color w:val="0070C0"/>
                <w:sz w:val="28"/>
                <w:szCs w:val="28"/>
              </w:rPr>
              <w:t>${workEduData.title}</w:t>
            </w:r>
          </w:p>
       <#if (workEduData.items?size>0)>
       <#list workEduData.items as workEduItem>
          <w:p w:rsidR="00280A7F" w:rsidRPr="007A3DAB" w:rsidRDefault="0046303F">
            <w:pPr>
              <w:numPr>
                <w:ilvl w:val="0"/>
                <w:numId w:val="1"/>
              </w:numPr>
              <w:spacing w:line="360" w:lineRule="atLeast"/>
              <w:rPr>
                <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cs="宋体"/>
              </w:rPr>
            </w:pPr>
            <w:r w:rsidRPr="007A3DAB">
              <w:rPr>
                <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
              </w:rPr>
              <w:t>${workEduItem}</w:t>
            </w:r>
          </w:p>
          
          
   </#list>
</#if>