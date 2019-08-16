package com.smate.center.open.service.interconnection.pub.extract;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

/**
 * 
 * @author AiJiangBin
 *
 */
@Service("extractFileToXmlService")
public class ExtractFileToXmlServiceImpl implements ExtractFileToXmlService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private String importFileTemplateFolder;
  private ResourceLoader resourceLoader = new DefaultResourceLoader();

  @Autowired
  private ExtractFileDriver extractFileDriver;

  @Override
  public String extract(Map<Integer, Map<String, Object>> resultMap, String fileName) {

    try {
      String xmlData = extractFileDriver.extract(resultMap);
      TransformerFactory factory = TransformerFactory.newInstance();
      String xslPath = resourceLoader.getResource(importFileTemplateFolder).getFile().getPath();
      StreamSource xslt = new StreamSource(xslPath + System.getProperty("file.separator") + fileName + ".xsl");
      Transformer transformer = factory.newTransformer(xslt);
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      ByteArrayInputStream bis = new ByteArrayInputStream(xmlData.getBytes());
      StreamResult result = new StreamResult(os);
      StreamSource source = new StreamSource(bis);
      transformer.transform(source, result);
      os.flush();
      os.close();
      xmlData = os.toString();

      // 记录取成果日志

      return xmlData;
    } catch (Exception e) {
      logger.error("将xml 转换成 xsl文件失败！", e);
      StringBuilder pubIds = new StringBuilder();
      for (Integer in : resultMap.keySet()) {
        // map.keySet()返回的是所有key的值
        Map<String, Object> temp = resultMap.get(in);// 得到每个key多对用value的值
        pubIds.append(temp.get("pub_id") + ",");
      }
      logger.error("可能错误的pubid=" + pubIds);
    }
    return "";

  }

  public String getImportFileTemplateFolder() {
    return importFileTemplateFolder;
  }

  public void setImportFileTemplateFolder(String importFileTemplateFolder) {
    this.importFileTemplateFolder = importFileTemplateFolder;
  }

}
