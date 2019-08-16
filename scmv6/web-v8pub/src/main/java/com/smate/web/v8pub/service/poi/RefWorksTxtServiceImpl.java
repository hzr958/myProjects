package com.smate.web.v8pub.service.poi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.smate.core.base.exception.ServiceException;
import com.smate.web.v8pub.vo.exportfile.ExportPubTemp;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 到处RefWorks的txt文档.
 * 
 * @author WeiLong Peng
 * 
 */
@Service("refWorksTxtService")
@Transactional(rollbackFor = Exception.class)
public class RefWorksTxtServiceImpl implements RefWorksTxtService {

  /**
   * 
   */
  private static final long serialVersionUID = -1818789757308577079L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private String fileRoot;
  @Autowired
  private Configuration txtFreemarkerConfiguration;

  public void setFileRoot(String fileRoot) {
    this.fileRoot = fileRoot;
  }

  public String getFileRoot() {
    return fileRoot;
  }

  @Override
  public String exportPubRefworksTxt(List<ExportPubTemp> publications)
      throws ServiceException, IOException, TemplateException {
    StringBuffer txtBuffer = new StringBuffer();
    List<Map<String, Object>> dataMapList = exportPubTempToMap(publications);
    if (CollectionUtils.isNotEmpty(dataMapList)) {
      for (Map<String, Object> dataMap : dataMapList) {
        Integer pubType = Integer.valueOf((String) dataMap.get("pubType"));
        if (pubType == 2 || pubType == 3 || pubType == 4 || pubType == 7) {
          Template template = txtFreemarkerConfiguration.getTemplate("txt_pub_refworks.ftl", "utf-8");
          if (txtBuffer.length() == 0) {
            txtBuffer.append(FreeMarkerTemplateUtils.processTemplateIntoString(template, dataMap));
          } else {
            txtBuffer.append("\r\n\r\n\r\n" + FreeMarkerTemplateUtils.processTemplateIntoString(template, dataMap));
          }
        }
      }
    }

    if (StringUtils.isBlank(txtBuffer.toString())) {
      return "";
    }

    return txtBuffer.toString();
  }

  private List<Map<String, Object>> exportPubTempToMap(List<ExportPubTemp> publications) {
    List<Map<String, Object>> dataMapList = new ArrayList<Map<String, Object>>();
    for (ExportPubTemp pub : publications) {
      Map<String, Object> pubMap = new HashMap<>();
      Integer pubType = pub.getPubtype();
      pubMap.put("pubType", pubType.toString());
      pubMap.put("pubTitle", pub.getTitle());
      pubMap.put("authors", StringUtils.isNotBlank(pub.getAuthornames()) ? pub.getAuthornames().split("; |；") : null);
      pubMap.put("sourceUrl", pub.getFulltexturl());
      pubMap.put("abstract", pub.getSummary());
      pubMap.put("publishYear", pub.getPublishyear());
      pubMap.put("volume", pub.getVolume());
      pubMap.put("issue", pub.getIssue());
      if (pub.getStartpage() == null && pub.getEndpage() == null && pub.getPagenumber() != null) {
        String startPage = pub.getPagenumber().replaceAll("-[0-9]*$", "");
        String endPage = pub.getPagenumber().replaceAll("^[0-9]*-?", "");
        pubMap.put("startPage", startPage);
        pubMap.put("endPage", endPage);
      } else {
        pubMap.put("startPage", pub.getStartpage());
        pubMap.put("endPage", pub.getEndpage());
      }
      pubMap.put("proceedingTitle", pub.getConferencename());
      pubMap.put("confVenue", pub.getCountry());
      pubMap.put("pubTypeName", getPubTypeName(pubType));
      pubMap.put("original", pub.getJournalname());
      pubMap.put("keywords", StringUtils.isNotBlank(pub.getKeywords()) ? pub.getKeywords().split("; |；") : null);
      pubMap.put("doi", pub.getDoi());
      pubMap.put("articleNumber", pub.getPagenumber());

      dataMapList.add(pubMap);
    }
    return dataMapList;
  }

  private String getPubTypeName(Integer pubType) {
    String typeName = "";
    switch (pubType) {
      case 1:// 奖励
        break;
      case 2:// 书籍
        typeName = "Book, Edited";
        break;
      case 3:// 会议论文
        typeName = "Conference Paper";
        break;
      case 4:// 期刊论文
        typeName = "Journal Article";
        break;
      case 5:// 专利
        break;
      case 7:// 其他
        typeName = "Others";
        break;
      case 8:// 学位论文
        break;
      case 10:// 书籍章节
        break;
      default:
        break;
    }
    return typeName;
  }

}
