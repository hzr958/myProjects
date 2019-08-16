package com.smate.center.open.service.publication;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
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
import com.smate.core.base.pub.model.Publication;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.service.consts.SysDomainConst;
import com.smate.core.base.utils.string.IrisStringUtils;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 书籍章节.
 * 
 * @author pwl
 * 
 */
public class IrisChapterPubXmlBuildServiceImpl implements IrisPubXmlBuildService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  // @Resource(name = "snsSrvWsServiceLocator")
  // private SnsSrvWsServiceLocator snsSrvWsServiceLocator;
  @Autowired
  private SysDomainConst sysDomainConst;
  @Autowired
  private ConstPubTypeService publicationTypeService;

  @SuppressWarnings("rawtypes")
  @Override
  public String buildPubXml(Publication pub, String xmlData) {
    String pubXml = "";
    try {
      PubXmlDocument doc = new PubXmlDocument(xmlData);
      IrisPubXmlDocument irisDoc = new IrisPubXmlDocument(pub.getPubId());
      Integer pubTypeId = NumberUtils.toInt(doc.getXmlNodeAttribute(PubXmlConstants.PUB_TYPE_XPATH, "id"), 4);
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.PUB_TYPE_ATTR,
          publicationTypeService.queryResultTypeName(pubTypeId));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.PUB_TYPE_ATTR,
          publicationTypeService.queryResultTypeName(pubTypeId));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.ZH_TITLE_ATTR,
          IrisStringUtils.filterXmlStr(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title")));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.EN_TITLE_ATTR,
          IrisStringUtils.filterXmlStr(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title")));

      String zhAbstract =
          IrisStringUtils.filterXmlStr(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_abstract"));
      String enAbstract =
          IrisStringUtils.filterXmlStr(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_abstract"));
      String zhKeywords =
          IrisStringUtils.filterXmlStr(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_keywords"));
      String enKeywords =
          IrisStringUtils.filterXmlStr(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_keywords"));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.ZH_ABSTRACT_ATTR, zhAbstract);
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.EN_ABSTRACT_ATTR, enAbstract);
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.ZH_KEYWORDS_ATTR, zhKeywords);
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.EN_KEYWORDS_ATTR, enKeywords);

      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.SOURCE_DB_ID,
          ObjectUtils.toString(pub.getSourceDbId()));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.SERIES_NAME_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "series_name"));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.CHAPTER_NO_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "chapter_no"));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.EDITORS_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "editors"));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.PUBLISHER_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "publisher"));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.COUNTRY_NAME_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "country_name"));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.CITY_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "city"));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.ISBN_NAME_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "isbn"));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.PUB_YEAR_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_year"));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.PUB_MONTH_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_month"));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.PUB_DAY_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_day"));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.NOT_NUMBER_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "not_number"));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.START_PAGE_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "start_page"));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.END_PAGE_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "end_page"));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.DOI_ATTR,
          IrisStringUtils.filterXmlStr(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "doi")));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.BRIEF_DESC_ATTR,
          IrisStringUtils.filterXmlStr(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc")));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.BRIEF_DESC_EN_ATTR,
          IrisStringUtils.filterXmlStr(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc_en")));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.ARTICLE_NUMBER_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "article_number"));
      // String http = snsSrvWsServiceLocator.getSnsWebUrl()
      String http =
          "https://" + sysDomainConst.getSnsDomain() + sysDomainConst.getSnsContext() + "/publication/view?des3Id="
              + ServiceUtil.encodeToDes3(pub.getPubId().toString()) + "," + ServiceConstants.SCHOLAR_NODE_ID_1;
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.HTTP, http);

      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.FUNDINFO,
          StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "fundinfo")));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.DOC_TYPE,
          StringUtils.trimToEmpty(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "doc_type")));

      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.FULLTEXT_URL_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "fulltext_url"));

      // 2014-08-07
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.ISI_ID_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "isi_id"));
      irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH, IrisPubXmlConstants.EI_ID_ATTR,
          doc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "ei_id"));
      // Long rolPubId =
      // publicationConfirmService.getPsnConfirmPubByRolPubId(pub.getPsnId(),
      // pub.getId());
      // irisDoc.setNodeAttribute(IrisPubXmlConstants.PUBLICATION_XPATH,
      // IrisPubXmlConstants.ROL_PUB_ID,ObjectUtils.toString(rolPubId));

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
      logger.error(String.format("IRIS业务系统导出成果获取成果pubId=%s详情XML出现异常：", pub), e);
    }
    return pubXml;
  }

}
