package com.smate.center.open.service.interconnection.pub.extract;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.smate.center.open.consts.OpenConsts;


@Service("extractFileDriver")
public class ExtractFilePubDriver implements ExtractFileDriver {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public String extract(Map<Integer, Map<String, Object>> resultMap) {

    XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
    String outXml = "";
    ByteArrayOutputStream is1 = new ByteArrayOutputStream();
    XMLStreamWriter xmlWriter = null;
    try {
      xmlWriter = outputFactory.createXMLStreamWriter(is1, "UTF-8");
      xmlWriter.writeStartDocument("Utf-8", "1.0");
      xmlWriter.writeStartElement("publications");

      Map<String, Object> extraData = resultMap.get(-1);
      Set<String> extraKey = extraData.keySet();
      for (String key : extraKey) {
        xmlWriter.writeStartElement(key);
        xmlWriter.writeCData(objToString(extraData.get(key)));
        xmlWriter.writeEndElement();
      }
      // 正常获取的状态
      xmlWriter.writeStartElement(OpenConsts.RESULT_GET_PUB_STATUS);
      xmlWriter.writeCData("0");
      xmlWriter.writeEndElement();

      for (int i = 0; i < resultMap.size() - 1; i++) {
        Map<String, Object> dataMap = resultMap.get(i);
        xmlWriter.writeStartElement("publication");
        Set<String> keySet = dataMap.keySet();
        for (String key : keySet) {
          xmlWriter.writeStartElement(key);
          if ("authors".equals(key)) {
            buildAuthors(xmlWriter, dataMap);
          } else {
            xmlWriter.writeCData(objToString(dataMap.get(key)));
          }
          xmlWriter.writeEndElement();
        }
        xmlWriter.writeEndElement();
      }
      xmlWriter.writeEndElement();
      xmlWriter.writeEndDocument();
      outXml = is1.toString();
    } catch (Exception e) {
      logger.error("将文件拆分成xml格式字符串时出错", e);
    } finally {
      if (is1 != null) {
        try {
          is1.flush();
          is1.close();
        } catch (IOException e) {
          logger.error("关闭流失败", e);
        }
      }
      if (xmlWriter != null) {
        try {
          xmlWriter.flush();
          xmlWriter.close();
        } catch (XMLStreamException e) {
          logger.error("关闭流失败", e);
        }

      }

    }
    return outXml;
  }


  // 构建作者
  private void buildAuthors(XMLStreamWriter xmlWriter, Map<String, Object> dataMap) throws XMLStreamException {
    Object obj = dataMap.get("authors");
    if (obj != null) {
      List<Map<String, String>> authorsList = (List<Map<String, String>>) obj;
      if (authorsList != null && authorsList.size() > 0) {
        for (int j = 0; j < authorsList.size(); j++) {
          Map<String, String> authorMap = authorsList.get(j);
          xmlWriter.writeStartElement("author");
          Set<String> keySet = authorMap.keySet();
          for (String key : keySet) {
            xmlWriter.writeStartElement(key);
            xmlWriter.writeCData(objToString(authorMap.get(key)));
            xmlWriter.writeEndElement();
          }
          xmlWriter.writeEndElement();
        }
      }
    }
  }


  @Override
  public String getDbType() {
    return EXTRACT_FILE_ISIS_PUB;
  }

  public String objToString(Object obj) {
    if (obj != null) {
      return obj.toString();
    }
    return "";
  }


}
