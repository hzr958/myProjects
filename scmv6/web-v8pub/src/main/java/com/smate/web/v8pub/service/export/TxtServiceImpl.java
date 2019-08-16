package com.smate.web.v8pub.service.export;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.smate.core.base.psn.service.psnpub.PsnPubService;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.common.HtmlUtils;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.v8pub.dao.sns.PubSnsDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.PubPO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.vo.exportfile.ExportPubTemp;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 导出txt文档服务实现类
 * 
 * @author lhd
 *
 */
@Service("txtService")
@Transactional(rollbackFor = Exception.class)
public class TxtServiceImpl implements TxtService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private final String encoding = "utf-8";
  @Resource(name = "txtFreemarkerConfiguration")
  private Configuration txtFreemarkerConfiguration;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private PsnPubService psnPubService;

  @Override
  public String exportPubTxt(List<PubInfo> pubInfoList) throws Exception {
    StringBuilder txtBuilder = new StringBuilder();
    if (CollectionUtils.isNotEmpty(pubInfoList)) {
      for (PubInfo pub : pubInfoList) {
        Map<String, Object> dataMap = buildPubCommonFreeMarkerData(pub);
        Template template = txtFreemarkerConfiguration.getTemplate("txt_pub_common.ftl", encoding);
        if (txtBuilder.length() == 0) {
          txtBuilder.append(FreeMarkerTemplateUtils.processTemplateIntoString(template, dataMap));
        } else {
          txtBuilder.append("\r\n\r\n\r\n" + FreeMarkerTemplateUtils.processTemplateIntoString(template, dataMap));
        }
      }
    }
    if (StringUtils.isBlank(txtBuilder)) {
      return "null";
    }
    return txtBuilder.toString();
  }

  @Override
  public List<PubInfo> getInfoList(List<Long> pubIds) throws ServiceException {
    List<PubInfo> pubInfoList = new ArrayList<PubInfo>();
    List<PubPO> pubList = pubSnsDAO.queryPubPOByPubIds(pubIds);
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (CollectionUtils.isNotEmpty(pubList)) {
      for (PubPO pubPo : pubList) {
        PubSnsPO pubSnsPO = (PubSnsPO) pubPo;
        PubInfo pubInfo = new PubInfo();
        // Long pubOwnerPsnId = psnPubService.getPubOwnerId(pubSnsPO.getPubId());
        // if (pubOwnerPsnId.equals(currentUserId)) {
        pubInfo.setPubId(pubSnsPO.getPubId());
        pubInfo.setPubType(pubSnsPO.getPubType());
        pubInfo.setTitle(pubSnsPO.getTitle());
        pubInfo.setBriefDesc(pubSnsPO.getBriefDesc());
        pubInfo.setAuthorNames(pubSnsPO.getAuthorNames());
        pubInfoList.add(pubInfo);
        // }
      }
    }
    return pubInfoList;
  }

  /**
   * 构造freeMarker需要的成果常用字段数据
   * 
   * @param pubXml
   * @return
   */
  private Map<String, Object> buildPubCommonFreeMarkerData(PubInfo pub) {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    String pubTitle = HtmlUtils.Html2Text(pub.getTitle()) + "\r\n";
    // 标题
    dataMap.put("pubTitle", pubTitle);
    String authors = pub.getAuthorNames();
    authors = XmlUtil.formateSymbolAuthors(pubTitle, authors);// 为了与成果列表上显示一致
    authors = HtmlUtils.Html2Text(authors) + "\r\n";// SCM-14191
    // 作者
    dataMap.put("authors", authors);
    String briefDesc = HtmlUtils.Html2Text(pub.getBriefDesc());
    // 来源
    dataMap.put("briefDesc", briefDesc);
    return dataMap;
  }

  @Override
  public String exportPubEndnoteTxt(List<ExportPubTemp> publications)
      throws ServiceException, IOException, TemplateException {
    StringBuffer txtBuffer = new StringBuffer();
    int pubType;
    List<Map<String, String>> dataMapList = exportPubTempToMap(publications);
    Template template = null;
    for (Map<String, String> dataMap : dataMapList) {
      String templateFile = null;
      pubType = Integer.valueOf(dataMap.get("pubType"));
      switch (pubType) {
        case 1:// 奖励
          templateFile = "";
          break;
        case 2:// 书籍
          templateFile = "txt_pub_endnote2.ftl";
          break;
        case 3:// 会议论文
          templateFile = "txt_pub_endnote3.ftl";
          break;
        case 4:// 期刊论文
          templateFile = "txt_pub_endnote4.ftl";
          break;
        case 5:// 专利
          templateFile = "txt_pub_endnote5.ftl";
          break;
        case 7:// 其他
          templateFile = "txt_pub_endnote7.ftl";
          break;
        case 8:// 学位论文
          templateFile = "txt_pub_endnote8.ftl";
          break;
        case 10:// 书籍章节
          templateFile = "txt_pub_endnote10.ftl";
          break;
        default:
          break;
      }

      if (StringUtils.isNotBlank(templateFile)) {
        template = txtFreemarkerConfiguration.getTemplate(templateFile, "utf-8");
        if (txtBuffer.length() == 0) {
          txtBuffer.append(FreeMarkerTemplateUtils.processTemplateIntoString(template, dataMap));
        } else {
          txtBuffer.append("\r\n\r\n\r\n" + FreeMarkerTemplateUtils.processTemplateIntoString(template, dataMap));
        }
      }
    }
    if (StringUtils.isBlank(txtBuffer.toString())) {
      return "";
    }
    return txtBuffer.toString();
  }

  private List<Map<String, String>> exportPubTempToMap(List<ExportPubTemp> publications) {
    List<Map<String, String>> dataMapList = new ArrayList<Map<String, String>>();
    for (ExportPubTemp pub : publications) {
      Map<String, String> pubMap = new HashMap<>();
      Integer pubType = pub.getPubtype();
      pubMap.put("pubType", getValue(pubType.toString()));
      pubMap.put("pubAuthors", getValue(pub.getAuthornames()));
      pubMap.put("year", getValue(pub.getPublishyear()));
      pubMap.put("pubTitle", getValue(pub.getTitle()));
      pubMap.put("publishDate", getValue(pub.getPublishdate()));
      pubMap.put("doi", getValue(pub.getDoi()));
      pubMap.put("keywords", getValue(pub.getKeywords()));
      pubMap.put("abstract", getValue(pub.getSummary()));// 摘要
      pubMap.put("fullTextUrl", getValue(pub.getFulltexturl()));
      pubMap.put("remark", "");// 备注
      switch (pubType) {
        case 1:// 奖励
          break;
        case 2:// 书籍
          pubMap.put("country", getValue(pub.getCountry()));
          pubMap.put("publisher", getValue(pub.getPublisher()));
          pubMap.put("pages", getValue(pub.getTotalpages()));
          pubMap.put("isbn", getValue(pub.getIsbn()));
          pubMap.put("language", getValue(pub.getLanguage()));
          pubMap.put("publicationStatus", getValue(pub.getPublishstatus()));
          break;
        case 3:// 会议论文
          pubMap.put("confrenceName", getValue(pub.getConferencename()));
          pubMap.put("city", getValue(pub.getCountry()));
          pubMap.put("page", getValue(pub.getPagenumber()));
          pubMap.put("confrenceType", getValue(pub.getPapertype()));
          break;
        case 4:// 期刊论文
          pubMap.put("journalName", getValue(pub.getJournalname()));
          pubMap.put("volume", getValue(pub.getVolume()));
          pubMap.put("issue", getValue(pub.getIssue()));
          pubMap.put("page", getValue(pub.getPagenumber()));
          pubMap.put("startPage", getValue(pub.getPagenumber()));
          pubMap.put("issn", getValue(pub.getIssn()));
          break;
        case 5:// 专利
          pubMap.put("country", getValue(pub.getCountry()));
          pubMap.put("patentType", getPatentType(pub.getPatenttype()));
          pubMap.put("patentNumber", getValue(pub.getApplicationno()));
          pubMap.put("patentApplier", getValue(pub.getPatentapplier()));
          pubMap.put("patentStatus", getPatentStatus(pub.getPatentstatus()));
          break;
        case 7:// 其他
          break;
        case 8:// 学位论文
          pubMap.put("academicDept", getValue(pub.getDepartment()));
          pubMap.put("issueOrg", getValue(pub.getThesisinsname()));
          pubMap.put("degree", getValue(pub.getDegree()));
          break;
        case 10:// 书籍章节
          pubMap.put("editors", getValue(pub.getEditors()));
          pubMap.put("country", getValue(pub.getCountry()));
          pubMap.put("bookName", getValue(pub.getBookname()));
          pubMap.put("publisher", getValue(pub.getPublisher()));
          pubMap.put("chapterNumber", getValue(pub.getChapterno()));
          pubMap.put("page", getValue(pub.getPagenumber()));
          pubMap.put("seriesName", getValue(pub.getSeriesname()));
          pubMap.put("isbn", getValue(pub.getIsbn()));
          break;
        default:
          break;
      }
      dataMapList.add(pubMap);
    }
    return dataMapList;
  }

  /**
   * 获取专利类别 51-发明专利 52-实用专利 53-外观专利 54-植物专利
   * 
   * @return
   */
  private String getPatentType(String type) {
    type = getValue(type);
    return type.replaceAll("(51|52|53|54)-", "");
  }

  /**
   * 处理专利状态，0:申请/1:授权
   * 
   * @return
   */
  private String getPatentStatus(String status) {
    status = getValue(status);
    return status.replaceAll("0-|1-", "");
  }


  private String getValue(String val) {
    val = Objects.toString(val, "");
    return val.trim();
  }
}
