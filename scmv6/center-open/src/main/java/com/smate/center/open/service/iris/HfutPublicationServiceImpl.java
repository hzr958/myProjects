package com.smate.center.open.service.iris;

import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.open.dao.iris.HfutPublicationDao;
import com.smate.center.open.dao.pdwh.jnl.JournalDao;
import com.smate.center.open.dao.sie.publication.RolPubXmlDao;
import com.smate.center.open.dao.sie.publication.SiePublicationListDao;
import com.smate.center.open.model.pdwh.jnl.Journal;
import com.smate.center.open.model.publication.PubXmlDocument;
import com.smate.center.open.model.sie.publication.PublicationRol;
import com.smate.center.open.model.sie.publication.RolPubXml;
import com.smate.center.open.model.sie.publication.SiePublicationList;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.model.Page;

/**
 * 合肥工业大学成果Service实现.
 * 
 * @author xys
 *
 */
@Service("hfutPublicationService")
public class HfutPublicationServiceImpl implements HfutPublicationService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private HfutPublicationDao hfutPublicationDao;
  @Autowired
  private JournalDao journalDao;
  @Autowired
  private RolPubXmlDao rolPubXmlDao;
  @Autowired
  private SiePublicationListDao publicationListDao;

  @Override
  public Page<PublicationRol> getPubs(Map<String, Object> paramsMap, Page<PublicationRol> page) {
    page = this.hfutPublicationDao.getPubs(paramsMap, page);
    if (!CollectionUtils.isEmpty(page.getResult())) {
      Integer typeId = Integer.valueOf(paramsMap.get("typeId").toString());
      if (typeId == PublicationTypeEnum.JOURNAL_ARTICLE) {
        for (PublicationRol pub : page.getResult()) {
          fillJnlName(pub);
          fillPaperInsAndFirstAuthorIns(pub);
          fillPubList(pub);
        }
      } else {
        for (PublicationRol pub : page.getResult()) {
          fillPaperInsAndFirstAuthorIns(pub);
          fillPubList(pub);
        }
      }
    }
    return page;
  }

  /**
   * 填充期刊名称.
   * 
   * @param pub
   */
  private void fillJnlName(PublicationRol pub) {
    Long jid = pub.getJid();
    if (jid != null && jid > 0) {
      Journal jnl = journalDao.get(jid);
      if (jnl != null) {
        pub.setJnlZhName(jnl.getZhName());
        pub.setJnlEnName(jnl.getEnName());
      }
    }
  }

  /**
   * 填充收录情况.
   * 
   * @param pub
   */
  private void fillPubList(PublicationRol pub) {
    SiePublicationList pubList = publicationListDao.get(pub.getId());
    if (pubList != null) {
      pub.setListEi(pubList.getListEi());
      pub.setListSci(pubList.getListSci());
      pub.setListIstp(pubList.getListIstp());
      pub.setListSsci(pubList.getListSsci());
    }
  }

  /**
   * 填充论文单位和第一作者单位.
   * 
   * @param pub
   */
  private void fillPaperInsAndFirstAuthorIns(PublicationRol pub) {
    PubXmlDocument pubXmlDoc = this.getSiePubXML(pub.getId());
    if (pubXmlDoc != null) {
      String org = null;
      boolean isOnlyAuthor = false;
      String organizationsstr = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "organization");
      pub.setPaperIns(organizationsstr);
      String[] authorNames = StringUtils.isBlank(pub.getAuthorNames()) ? null : pub.getAuthorNames().split(",|;");
      if (authorNames != null && authorNames.length == 1) {// 只有一个作者
        org = organizationsstr;
        isOnlyAuthor = true;
      } else {
        String[] orgs = organizationsstr.split("\\[");
        if (orgs != null && orgs.length > 1) {
          org = StringUtils.replace(orgs[1], " ", "").toLowerCase();
        }
      }
      if (!StringUtils.isBlank(org)) {
        if (org.contains("hefeiunivtechnol") || org.contains("hefeitechnoluniv") || org.contains("hefeiunivtechnol")
            || org.contains("hefeiunivtech") || org.contains("hefeiunivtechmol") || org.contains("hefeiunivtechnol")
            || org.contains("hefeiunivtechnool") || org.contains("hefeiunivtechol") || org.contains("hefeiunivtechonl")
            || org.contains("hefeiunivtechonol") || org.contains("heifeiunivtechnol") || org.contains("合肥工业大学")
            || org.contains("hefeiuniversityoftechnology")) {
          pub.setFirstAuthorIns(true);
        }
      }
      if (!isOnlyAuthor && !pub.isFirstAuthorIns()) {
        String[] orgs = organizationsstr.split(";|；");
        if (orgs != null && orgs.length > 0) {
          org = StringUtils.replace(orgs[0], " ", "").toLowerCase();
        }
        if (!StringUtils.isBlank(org)) {
          if (org.contains("hefeiunivtechnol") || org.contains("hefeitechnoluniv") || org.contains("hefeiunivtechnol")
              || org.contains("hefeiunivtech") || org.contains("hefeiunivtechmol") || org.contains("hefeiunivtechnol")
              || org.contains("hefeiunivtechnool") || org.contains("hefeiunivtechol")
              || org.contains("hefeiunivtechonl") || org.contains("hefeiunivtechonol")
              || org.contains("heifeiunivtechnol") || org.contains("合肥工业大学")
              || org.contains("hefeiuniversityoftechnology")) {
            pub.setFirstAuthorIns(true);
          }
        }
      }
    }
  }

  /**
   * 获取单位成果XML.
   * 
   * @param pubId
   * @return
   */
  private PubXmlDocument getSiePubXML(Long pubId) {
    try {
      RolPubXml rolPubXml = rolPubXmlDao.get(pubId);
      if (rolPubXml != null) {
        return new PubXmlDocument(rolPubXml.getPubXml());
      }
    } catch (Exception e) {
      logger.error("获取单位成果XML出现异常了喔,pubId={}", pubId, e);
    }
    return null;
  }

}
