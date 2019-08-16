package com.smate.core.base.utils.excel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 读取Excel
 * 
 * @author zzx
 *
 */
public class XlsUtils {

  /**
   * @param args
   */
  public static void main(String[] args) {
    ZipFile xlsxFile = null;
    try {
      xlsxFile = new ZipFile(new File("e:\\gongyingshang.xlsx"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    getDate(xlsxFile);
  }

  public static void getDate(ZipFile xlsxFile) {
    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      // 先读取sharedStrings.xml这个文件备用
      ZipEntry sharedStringXML = xlsxFile.getEntry("xl/sharedStrings.xml");
      InputStream sharedStringXMLIS = xlsxFile.getInputStream(sharedStringXML);
      Document sharedString;
      sharedString = dbf.newDocumentBuilder().parse(sharedStringXMLIS);
      NodeList str = sharedString.getElementsByTagName("t");
      String sharedStrings[] = new String[str.getLength()];
      for (int n = 0; n < str.getLength(); n++) {
        Element element = (Element) str.item(n);
        sharedStrings[n] = element.getTextContent();
      }
      // 找到解压文件夹里的workbook.xml,此文件中包含了这张工作表中有几个sheet
      ZipEntry workbookXML = xlsxFile.getEntry("xl/workbook.xml");
      InputStream workbookXMLIS = xlsxFile.getInputStream(workbookXML);
      Document doc = dbf.newDocumentBuilder().parse(workbookXMLIS);
      // 获取一共有几个sheet
      NodeList nl = doc.getElementsByTagName("sheet");
      for (int i = 0; i < nl.getLength(); i++) {
        Element element = (Element) nl.item(i);// 将node转化为element，用来得到每个节点的属性
        System.out.println(element.getAttribute("name"));// 输出sheet节点的name属性的值
        // 接着就要到解压文件夹里找到对应的name值的xml文件，比如在workbook.xml中有<sheet
        // name="Sheet1"
        // sheetId="1" r:id="rId1" /> 节点
        // 那么就可以在解压文件夹里的xl/worksheets下找到sheet1.xml,这个xml文件夹里就是包含的表格的内容
        ZipEntry sheetXML = xlsxFile.getEntry("xl/worksheets/" + element.getAttribute("name").toLowerCase() + ".xml");
        InputStream sheetXMLIS = xlsxFile.getInputStream(sheetXML);
        Document sheetdoc = dbf.newDocumentBuilder().parse(sheetXMLIS);
        NodeList rowdata = sheetdoc.getElementsByTagName("row");
        for (int j = 0; j < rowdata.getLength(); j++) {
          // 得到每个行
          // 行的格式：
          /*
           * <row r="1" spans="1:3">r表示第一行,spans表示有几列 <c r="A1" t="s">//r表示该列的列表
           * ，t="s"个人认为是表示这个单元格的内容可以在sharedStrings.xml这个文件里找到，对应的节点 下标就是v节点的值，即0，若没有t属性，则v的值就是该单元格的内容 <v>0</v>
           * </c> <c r="B1" t="s"> <v>1</v> </c> <c r="C1" t="s"> <v>2</v> </c> </row>
           */
          Element row = (Element) rowdata.item(j);
          // 根据行得到每个行中的列
          NodeList columndata = row.getElementsByTagName("c");
          for (int k = 0; k < 1; k++) {

            Element column = (Element) columndata.item(k);
            NodeList values = column.getElementsByTagName("v");
            Element value = (Element) values.item(0);
            if (column.getAttribute("t") != null & column.getAttribute("t").equals("s")) {
              // 如果是共享字符串则在sharedstring.xml里查找该列的值
              String mobile = sharedStrings[Integer.parseInt(value.getTextContent())];
            } else {
              if (value != null) {

                System.out.print(value.getTextContent() + " ");
              } else {
                System.out.println("j : " + j + "   k : " + k + "  null");
                System.out.println("空");
              }
            }
          }
          System.out.println();
        }

      }
    } catch (ZipException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SAXException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ParserConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
}
