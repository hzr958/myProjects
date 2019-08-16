           <w:p>
            <w:pPr>
              <w:rPr>
                <w:rFonts w:ascii="楷体" w:eastAsia="楷体" w:hAnsi="楷体" w:cs="宋体"/>
                <w:color w:val="0070C0"/>
                <w:sz w:val="36"/>
                <w:szCs w:val="36"/>
              </w:rPr>
            </w:pPr>
            <w:r w:rsidRPr="0043100B">
              <w:rPr>
                <w:rFonts w:ascii="楷体" w:eastAsia="楷体" w:hAnsi="楷体" w:cs="宋体"/>
                <w:color w:val="0070C0"/>
                <w:sz w:val="36"/>
                <w:szCs w:val="36"/>
              </w:rPr>
              <w:t><#if (cVPsnInfo.name)??>${cVPsnInfo.name}</#if></w:t>
            </w:r>
            <w:r w:rsidRPr="0043100B">
              <w:rPr>
                <w:rFonts w:ascii="楷体" w:eastAsia="楷体" w:hAnsi="楷体" w:cs="宋体"/>
                <w:b/>
                <w:bCs/>
                <w:color w:val="0070C0"/>
                <w:sz w:val="36"/>
                <w:szCs w:val="36"/>
              </w:rPr>
              <w:t xml:space="preserve"> </w:t>
            </w:r>
          </w:p>
          <w:p>
            <w:pPr>
              <w:spacing w:line="360" w:lineRule="atLeast"/>
              <w:rPr>
                <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cs="宋体"/>
                <w:sz w:val="28"/>
                <w:szCs w:val="28"/>
              </w:rPr>
            </w:pPr>
            <w:r>
              <w:rPr>
                <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
              </w:rPr>
              <w:t><#if (cVPsnInfo.insInfo)??>${cVPsnInfo.insInfo}</#if><#if (cVPsnInfo.department)??>, ${cVPsnInfo.department}</#if><#if (cVPsnInfo.degree)??>, ${cVPsnInfo.degree}</#if></w:t>
            </w:r>
          </w:p>