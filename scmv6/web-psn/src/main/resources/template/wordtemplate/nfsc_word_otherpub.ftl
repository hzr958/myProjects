        <w:p w:rsidR="00280A7F" w:rsidRPr="003B652F" w:rsidRDefault="00164856">
            <w:pPr>
              <w:spacing w:beforeLines="50" w:before="120"/>
              <w:ind w:firstLineChars="100" w:firstLine="241"/>
              <w:rPr>
                <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cstheme="minorEastAsia" w:hint="eastAsia"/>
                <w:b/>
                <w:bCs/>
                <w:color w:val="0070C0"/>
                <w:szCs w:val="28"/>
              </w:rPr>
            </w:pPr>
            <w:r w:rsidRPr="003B652F">
              <w:rPr>
                <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cstheme="minorEastAsia" w:hint="eastAsia"/>
                <w:b/>
                <w:color w:val="auto"/>
                <w:szCs w:val="28"/>
              </w:rPr>
              <w:t xml:space="preserve">二、论著之外的代表性研究成果和学术奖励</w:t>
            </w:r>
          </w:p>
        
        
        
        
        <#if (cvPubData.objectContent?size>0)>
        <#list cvPubData.objectContent as pubItem>
            <w:p>
            <w:pPr>
              <w:numPr>
                <w:ilvl w:val="0"/>
                <w:numId w:val="4"/>
              </w:numPr>
              <w:spacing w:line="360" w:lineRule="atLeast"/>
              <w:ind w:left="828" w:hanging="119"/>
              <w:rPr>
                <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cstheme="minorEastAsia"/>
              </w:rPr>
            </w:pPr>
            <w:r>
              <w:rPr>
                <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cstheme="minorEastAsia" w:hint="eastAsia"/>
              </w:rPr>
              <w:t xml:space="preserve"> </w:t>
            </w:r>
            <#if (pubItem.authorList)?? && (pubItem.authorList?size>0)>
            <#if (pubItem.pubType != 1)>
            <#list pubItem.authorList as member>
            <#if (member.name)??>
            
            <#if member_index != 0>
                <w:r>
              <w:rPr>
                <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cstheme="minorEastAsia" w:hint="eastAsia"/>
              </w:rPr>
              <w:t xml:space="preserve">; </w:t>
            </w:r>
            </#if>
            
            <#if (member.owner == 1)>
            <w:r>
              <w:rPr>
                <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cstheme="minorEastAsia" w:hint="eastAsia"/>
                <w:b/>
              </w:rPr>
              <w:t>${member.name}</w:t>
            </w:r>
            <#else>
            
               <w:r>
              <w:rPr>
                <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cstheme="minorEastAsia" w:hint="eastAsia"/>
              </w:rPr>
              <w:t>${member.name}</w:t>
            </w:r>
            
            </#if>
            <#if (member.firstAuthor)??>
            <#if (member.firstAuthor == 1)>
            <w:r>
              <w:rPr>
                <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cstheme="minorEastAsia" w:hint="eastAsia"/>
                <w:vertAlign w:val="superscript"/>
              </w:rPr>
              <w:t>（#）</w:t>
            </w:r>
            </#if>
            </#if>
            <#if (member.communicable)??>
            <#if (member.communicable == 1)>
            <w:r>
              <w:rPr>
                <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cstheme="minorEastAsia" w:hint="eastAsia"/>
                <w:vertAlign w:val="superscript"/>
              </w:rPr>
              <w:t>（*）</w:t>
            </w:r>
            </#if>
            </#if>
            </#if>
            </#list>
            
            
            
            
            <#else>
                <w:r>
              <w:rPr>
                <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cstheme="minorEastAsia" w:hint="eastAsia"/>
                <w:b/>
              </w:rPr>
              <w:t>${pubItem.currentPsnName}</w:t>
            </w:r>
                
                
                <#if (member.firstAuthor)??>
                <#if (pubItem.isFirstAuthor == "1")>
            <w:r>
              <w:rPr>
                <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cstheme="minorEastAsia" w:hint="eastAsia"/>
                <w:vertAlign w:val="superscript"/>
              </w:rPr>
              <w:t>（#）</w:t>
            </w:r>
            </#if>
            </#if>
            <#if (pubItem.communicable == "1")>
            <w:r>
              <w:rPr>
                <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cstheme="minorEastAsia" w:hint="eastAsia"/>
                <w:vertAlign w:val="superscript"/>
              </w:rPr>
              <w:t>（*）</w:t>
            </w:r>
            </#if>
                
                
                <w:r>
              <w:rPr>
                <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cstheme="minorEastAsia" w:hint="eastAsia"/>
              </w:rPr>
              <w:t>${pubItem.awardSeq}</w:t>
            </w:r>
            
            </#if>
                <w:r>
              <w:rPr>
                <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cstheme="minorEastAsia" w:hint="eastAsia"/>
              </w:rPr>
              <w:t xml:space="preserve">, </w:t>
            </w:r>
            
            </#if>
            
            <#if (pubItem.pubType != 1)>
            
                <w:hyperlink r:id="rId${pubItem.wordHrefSeq}" w:history="1">
              <w:r w:rsidRPr="0009442D">
                <w:rPr>
                  <w:rStyle w:val="a3"/>
                  <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cstheme="minorEastAsia" w:hint="eastAsia"/>
                  <w:color w:val="auto"/>
                  <w:u w:val="none"/>
                </w:rPr>
                <w:t>${pubItem.title}</w:t>
              </w:r>
            </w:hyperlink>
                <w:r>
              <w:rPr>
                <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cstheme="minorEastAsia" w:hint="eastAsia"/>
              </w:rPr>
              <w:t>, ${pubItem.source}</w:t>
            </w:r>
            
                <w:r>
              <w:rPr>
                <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cstheme="minorEastAsia" w:hint="eastAsia"/>
                <w:b/>
              </w:rPr>
              <w:t xml:space="preserve">    （${pubItem.pubTypeName}）</w:t>
            </w:r>

            <#else>

                <w:hyperlink r:id="rId${pubItem.wordHrefSeq}" w:history="1">
              <w:r w:rsidRPr="0009442D">
                <w:rPr>
                  <w:rStyle w:val="a3"/>
                  <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cstheme="minorEastAsia" w:hint="eastAsia"/>
                  <w:color w:val="auto"/>
                  <w:u w:val="none"/>
                </w:rPr>
                <w:t>${pubItem.title}</w:t>
              </w:r>
            </w:hyperlink>

                <w:r>
              <w:rPr>
                <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cstheme="minorEastAsia" w:hint="eastAsia"/>
              </w:rPr>
              <w:t>, ${pubItem.source}</w:t>
            </w:r>
                <#if (pubItem.authorList)??>
                
                <#list pubItem.authorList as member>
                <#if (member.name)??>
                
                <#if member_index != 0 >
                        <w:r>
                <w:rPr>
                    <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cstheme="minorEastAsia" w:hint="eastAsia"/>
                </w:rPr>
                <w:t xml:space="preserve">; </w:t>
                </w:r>
                
                <#else>
                    <w:r>
                <w:rPr>
                    <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cstheme="minorEastAsia" w:hint="eastAsia"/>
                </w:rPr>
                <w:t xml:space="preserve">, </w:t>
                </w:r>
                </#if>
                
                
                <#if (member.owner == 1)>
                    <w:r>
              <w:rPr>
                <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cstheme="minorEastAsia" w:hint="eastAsia"/>
                <w:b/>
              </w:rPr>
              <w:t>${member.name}</w:t>
            </w:r>
                <#else>
                    <w:r>
              <w:rPr>
                <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cstheme="minorEastAsia" w:hint="eastAsia"/>
              </w:rPr>
              <w:t>${member.name}</w:t>
            </w:r>
                 </#if>
                 
                 <#if (member.firstAuthor)??>
                 <#if (member.firstAuthor == 1)>
            <w:r>
              <w:rPr>
                <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cstheme="minorEastAsia" w:hint="eastAsia"/>
                <w:vertAlign w:val="superscript"/>
              </w:rPr>
              <w:t>（#）</w:t>
            </w:r>
            </#if>
            </#if>
            <#if (member.communicable == 1)>
            <w:r>
              <w:rPr>
                <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cstheme="minorEastAsia" w:hint="eastAsia"/>
                <w:vertAlign w:val="superscript"/>
              </w:rPr>
              <w:t>（*）</w:t>
            </w:r>
            </#if>
                 
                 
                 
                 </#if>
                 </#list>
                    <w:r>
              <w:rPr>
                <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cstheme="minorEastAsia" w:hint="eastAsia"/>
                <w:b/>
              </w:rPr>
              <w:t xml:space="preserve">    （${pubItem.pubTypeName}）</w:t>
            </w:r>
                 
                </#if>
            </#if>
            
        </w:p>
        </#list>
        </#if>