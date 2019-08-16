<w:p>
            <w:pPr>
                <w:spacing w:before-lines="50" />
                <w:rPr>
                    <w:rFonts w:ascii="楷体" w:h-ansi="楷体" w:fareast="楷体" w:cs="宋体" w:hint="default" />
                    <w:color w:val="0070C0" />
                    <w:sz w:val="28" />
                    <w:sz-cs w:val="28" />
                </w:rPr>
            </w:pPr>
            <w:r>
                <w:rPr>
                    <w:rFonts w:ascii="楷体" w:h-ansi="楷体" w:fareast="楷体" w:cs="宋体" w:hint="fareast" />
                    <w:b />
                    <w:b-cs />
                    <w:color w:val="0070C0" />
                    <w:sz w:val="28" />
                    <w:sz-cs w:val="28" />
                </w:rPr>
                <w:t><#if (workEduData.title)??>${workEduData.title}</#if></w:t>
            </w:r>
        </w:p>
        
        <#if (workEduData.items)??>
        <#list workEduData.items as workEduItem>
        <w:p>
            <w:pPr>
                <w:spacing w:line="440" w:line-rule="at-least" />
                <w:ind w:first-line="320" />
                <w:rPr>
                    <w:rFonts w:ascii="宋体" w:h-ansi="宋体" w:cs="宋体" w:hint="default" />
                    <w:sz w:val="24" />
                    <w:sz-cs w:val="24" />
                </w:rPr>
            </w:pPr>           
           
            <w:r>
                <w:rPr>
                    <w:rFonts w:ascii="宋体" w:h-ansi="宋体" w:cs="宋体" w:hint="fareast" />
                    <w:sz w:val="24" />
                    <w:sz-cs w:val="24" />
                </w:rPr>
                <w:t>1. ${workEduItem}</w:t>
            </w:r>
        </w:p>
        </#list>
        </#if>