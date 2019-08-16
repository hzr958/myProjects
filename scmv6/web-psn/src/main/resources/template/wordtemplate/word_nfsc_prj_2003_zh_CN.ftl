
    <w:p w:rsidR="00280A7F" w:rsidRDefault="0046303F">
            <w:pPr>
              <w:spacing w:beforeLines="50" w:before="120"/>
              <w:rPr>
                <w:rFonts w:ascii="楷体" w:eastAsia="楷体" w:hAnsi="楷体" w:cs="宋体"/>
                <w:b/>
                <w:bCs/>
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
              <w:t>主持或参加科研项目（课题）及人才计划项目情况:</w:t>
            </w:r>
          </w:p>
       <#if (pubPrjData.objectContent?size>0)>
       <#list pubPrjData.objectContent as pubPrjItem>
          <w:p>
            <w:pPr>
              <w:numPr>
                <w:ilvl w:val="0"/>
                <w:numId w:val="2"/>
              </w:numPr>
              <w:tabs>
                <w:tab w:val="left" w:pos="420"/>
              </w:tabs>
              <w:spacing w:line="360" w:lineRule="atLeast"/>
              <w:ind w:left="799" w:hanging="119"/>
              <w:rPr>
                <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cs="宋体"/>
              </w:rPr>
            </w:pPr>
            <w:r>
              <w:rPr>
                <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
              </w:rPr>
              <w:t xml:space="preserve"> </w:t>
            </w:r>
            <w:r>
              <w:rPr>
                <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
              </w:rPr>
              <w:t  xml:space="preserve"><#if pubPrjItem.agencyAndscheme??><#if pubPrjItem.agencyAndscheme != "">${pubPrjItem.agencyAndscheme}, </#if></#if><#if pubPrjItem.externalNo??><#if pubPrjItem.externalNo!="">${pubPrjItem.externalNo}, </#if></#if></w:t>
            </w:r>
            
            
            
            <#if pubPrjItem.showTitle??>
            <w:hyperlink r:id="rId${pubPrjItem.wordHrefSeq}" w:history="1">
              <w:r w:rsidRPr="00594477">
                <w:rPr>
                  <w:rStyle w:val="a3"/>
                  <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
                  <w:color w:val="auto"/>
                  <w:u w:val="none"/>
                </w:rPr>
                <w:t><#if pubPrjItem.showTitle!="">${pubPrjItem.showTitle}</#if></w:t>
              </w:r>
            </w:hyperlink>
            </#if>
            
            
            <w:r w:rsidRPr="007A3DAB">
              <w:rPr>
                <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
              </w:rPr>
              <w:t><#if pubPrjItem.showDate??><#if pubPrjItem.showDate!="">, ${pubPrjItem.showDate}</#if></#if><#if pubPrjItem.amountAndUnit??><#if pubPrjItem.amountAndUnit!="">, ${pubPrjItem.amountAndUnit}</#if></#if><#if pubPrjItem.prjState??><#if pubPrjItem.prjState!="">, ${pubPrjItem.prjState}</#if></#if><#if pubPrjItem.prjOwner??><#if pubPrjItem.prjOwner!="">, ${pubPrjItem.prjOwner}</#if></#if></w:t>
            </w:r>
            
          </w:p>
      </#list>
</#if>