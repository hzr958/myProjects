package com.smate.center.open.service.google;


import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.open.consts.IrisPubXmlConstants;
import com.smate.center.open.model.publication.IrisPubXmlDocument;
import com.smate.center.open.model.publication.PubXmlDocument;
import com.smate.center.open.service.consts.ConstPubTypeService;
import com.smate.center.open.service.nsfc.pub.NsfcwsPubService;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.string.IrisStringUtils;



/**
 * 其他.
 * 
 * @author pwl
 * 
 */
public class GoogleOtherPubXmlComponent implements GooglePubXmlBuildComponent {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConstPubTypeService publicationTypeService;
  @Autowired
  private NsfcwsPubService googlePubService;

  @SuppressWarnings("rawtypes")
  @Override
  public String buildPubXml(Long pubId, String xmlData) {
    String pubXml = "";
    try {
      PubXmlDocument doc = new PubXmlDocument(xmlData);
      IrisPubXmlDocument irisDoc = new IrisPubXmlDocument(pubId);
      Integer pubTypeId = NumberUtils.toInt(doc.getXmlNodeAttribute(PubXmlConstants.PUB_TYPE_XPATH, "id"), 4);
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.PUB_TYPE_ATTR,
          publicationTypeService.queryResultTypeName(pubTypeId));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.PUB_TYPE_ATTR,
          publicationTypeService.queryResultTypeName(pubTypeId));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.ZH_TITLE_ATTR,
          IrisStringUtils.filterXmlStr(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title")));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.EN_TITLE_ATTR,
          IrisStringUtils.filterXmlStr(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title")));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.COUNTRY_NAME_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "country_name"));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.CITY_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "city"));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.DI_MODE_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "di_mode"));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.PUB_YEAR_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_year"));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.PUB_DATE_DESC_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "pub_date_desc"));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.PUB_MONTH_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_month"));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.PUB_DAY_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_day"));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.START_YEAR_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "start_year"));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.START_MONTH_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "start_month"));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.START_DAY_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "start_day"));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.END_YEAR_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "end_year"));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.END_MONTH_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "end_month"));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.END_DAY_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUB_OTHER_XPATH, "end_day"));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.LIST_INFO_ATTR,
          googlePubService.getPubCitedListByPubId(pubId));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.ISI_CITE_TIMES_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cite_times"));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.DOI_ATTR,
          IrisStringUtils.filterXmlStr(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "doi")));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.BRIEF_DESC_ATTR,
          IrisStringUtils.filterXmlStr(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc")));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.BRIEF_DESC_EN_ATTR,
          IrisStringUtils.filterXmlStr(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc_en")));

      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.FUNDINFO,
          StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "fundinfo")));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.DOC_TYPE,
          StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "doc_type")));

      List authors = doc.getNodes(PubXmlConstants.PUB_MEMBERS_MEMBER_XPATH);
      if (CollectionUtils.isNotEmpty(authors)) {
        for (int i = 0; i < authors.size(); i++) {
          Element ele = (Element) authors.get(i);
          String memNodePath =
              IrisPubXmlConstants.PUB_MEMBERS_MEMBER_INDEX_XPATH.replace("*", ele.attributeValue("seq_no"));
          irisDoc.setNodeAttribute(memNodePath, IrisPubXmlConstants.SEQ__NO_ATTR, ele.attributeValue("seq_no"));
          irisDoc.setNodeAttribute(memNodePath, IrisPubXmlConstants.MEMBER_PSN_NAME_ATTR,
              ele.attributeValue("member_psn_name"));
          irisDoc.setNodeAttribute(memNodePath, IrisPubXmlConstants.AUTHOR_POS_ATTR, ele.attributeValue("author_pos"));
        }
      }
      pubXml = irisDoc.getRoolElementXmlString();
    } catch (Exception e) {
      logger.error(String.format("IRIS业务系统导出成果获取成果pubId=%s详情XML出现异常：", pubId), e);
    }
    return pubXml;
  }

}
