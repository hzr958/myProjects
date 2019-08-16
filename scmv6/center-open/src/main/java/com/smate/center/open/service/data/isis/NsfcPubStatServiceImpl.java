package com.smate.center.open.service.data.isis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.open.dao.data.isissns.SnsNsfcPrjReportDao;
import com.smate.center.open.isis.model.data.isissns.SnsNsfcPrjRptPub;
import com.smate.center.open.model.consts.ConstRegion;
import com.smate.center.open.model.publication.PubXmlDocument;
import com.smate.center.open.service.consts.ConstRegionService;
import com.smate.center.open.service.pdwh.jnl.JnlLevelService;
import com.smate.center.open.service.publication.MyPublicationQueryService;
import com.smate.center.open.service.publication.ScholarPublicationXmlManager;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.pub.model.Publication;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;

/**
 * 向基金委提供项目结题数据-接口
 * 
 * @author hp
 * @date 2015-10-21
 */
@Service("nsfcPubStatService")
public class NsfcPubStatServiceImpl implements NsfcPubStatService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ScholarPublicationXmlManager scholarPublicationXmlManager;
  @Autowired
  private ConstRegionService constRegionService;
  @Autowired
  private MyPublicationQueryService myPublicationQueryService;
  @Autowired
  private JnlLevelService jnlLevelService;
  @Autowired
  private SnsNsfcPrjReportDao snsNsfcPrjReportDao;

  /**
   * 从SNS取数据
   * 
   * @throws Exception
   */
  @Override
  public Map<String, Object> getPubStatByIns(Map<String, Object> dataMap) throws Exception {
    Map<String, Object> map = snsNsfcPrjReportDao.getPubStatByIns(dataMap);
    Map<String, Object> statRptMap = new HashMap<String, Object>();
    Map<String, Object> mapaward = new HashMap<String, Object>();
    mapaward.put("award_nat_zrkx1", map.get("AWARD_NAT_ZRKX1"));
    mapaward.put("award_nat_zrkx2", map.get("AWARD_NAT_ZRKX2"));
    mapaward.put("award_nat_kjjb1", map.get("AWARD_NAT_KJJB1"));
    mapaward.put("award_nat_kjjb2", map.get("AWARD_NAT_KJJB2"));
    mapaward.put("award_nat_fm1", map.get("AWARD_NAT_FM1"));
    mapaward.put("award_nat_fm2", map.get("AWARD_NAT_FM2"));
    mapaward.put("award_prv_zrkx1", map.get("AWARD_PRV_ZRKX1"));
    mapaward.put("award_prv_zrkx2", map.get("AWARD_PRV_ZRKX2"));
    mapaward.put("award_prv_kjjb1", map.get("AWARD_PRV_KJJB1"));
    mapaward.put("award_prv_kjjb2", map.get("AWARD_PRV_KJJB2"));
    mapaward.put("award_int_xs", map.get("AWARD_INT_XS"));
    mapaward.put("award_other", map.get("AWARD_OTHER"));
    statRptMap.put("pub_award", mapaward);
    Map<String, Object> mapbook = new HashMap<String, Object>();

    mapbook.put("zh_ycb", map.get("ZH_YCB"));
    mapbook.put("zh_dcb", map.get("ZH_DCB"));
    mapbook.put("en_ycb", map.get("EN_YCB"));
    mapbook.put("en_dcb", map.get("EN_DCB"));

    statRptMap.put("pub_book", mapbook);

    // 会议
    Map<String, Object> mapconf_paper = new HashMap<String, Object>();

    mapconf_paper.put("report_int_ty", map.get("REPORT_INT_TY"));
    mapconf_paper.put("report_int_fz", map.get("REPORT_INT_FZ"));
    mapconf_paper.put("report_nat_ty", map.get("REPORT_NAT_TY"));
    mapconf_paper.put("report_nat_fz", map.get("REPORT_NAT_FZ"));

    statRptMap.put("pub_conf_paper", mapconf_paper);

    // 期刊
    Map<String, Object> mapjournal = new HashMap<String, Object>();

    mapjournal.put("journal_home_yb", map.get("JOURNAL_HOME_YB"));
    mapjournal.put("journal_home_hx", map.get("JOURNAL_HOME_HX"));
    mapjournal.put("journal_int", map.get("JOURNAL_INT"));
    mapjournal.put("journal_idx_sci", map.get("JOURNAL_IDX_SCI"));
    mapjournal.put("journal_idx_ei", map.get("JOURNAL_IDX_EI"));
    mapjournal.put("journal_idx_istp", map.get("JOURNAL_IDX_ISTP"));
    mapjournal.put("journal_idx_isr", map.get("JOURNAL_IDX_ISR"));
    statRptMap.put("journal", mapjournal);

    // 专利
    Map<String, Object> mappatent = new HashMap<String, Object>();
    mappatent.put("home_app", map.get("HOME_APP"));
    mappatent.put("home_auth", map.get("HOME_AUTH"));
    mappatent.put("abroad_app", map.get("ABROAD_APP"));
    mappatent.put("abroad_auth", map.get("ABROAD_AUTH"));
    statRptMap.put("pub_patent", mappatent);
    return statRptMap;
  }

  void temp(List<SnsNsfcPrjRptPub> pubs) throws Exception {
    Map<String, Object> statRptMap = new HashMap<String, Object>();
    Integer journal_idx_sci = 0;
    Integer journal_idx_ei = 0;
    Integer journal_idx_istp = 0;
    Integer journal_idx_isr = 0;

    List<Long> awardPubIds = null;
    List<Long> bookPubIds = null;
    List<Long> confPubIds = null;
    List<Long> jnlPubIds = null;
    List<Long> patentPubIds = null;
    if (!CollectionUtils.isEmpty(pubs)) {
      awardPubIds = new ArrayList<Long>();
      bookPubIds = new ArrayList<Long>();
      confPubIds = new ArrayList<Long>();
      jnlPubIds = new ArrayList<Long>();
      patentPubIds = new ArrayList<Long>();
      for (SnsNsfcPrjRptPub pub : pubs) {
        switch (pub.getPubType().intValue()) {
          case PublicationTypeEnum.AWARD:
            awardPubIds.add(pub.getNsfcPrjRptPubPK().getPubId());
            break;
          case PublicationTypeEnum.BOOK:
            bookPubIds.add(pub.getNsfcPrjRptPubPK().getPubId());
            break;
          case PublicationTypeEnum.CONFERENCE_PAPER:
            confPubIds.add(pub.getNsfcPrjRptPubPK().getPubId());
            break;
          case PublicationTypeEnum.JOURNAL_ARTICLE:
            jnlPubIds.add(pub.getNsfcPrjRptPubPK().getPubId());
            String listInfo = pub.getListInfo();
            if (!StringUtils.isBlank(listInfo)) {
              listInfo = listInfo.toLowerCase();
              if (listInfo.indexOf("SCI") > -1) {
                journal_idx_sci++;
              } else if (listInfo.indexOf("EI") > -1) {
                journal_idx_ei++;
              } else if (listInfo.indexOf("ISTP") > -1) {
                journal_idx_istp++;
              }
            }
            break;
          case PublicationTypeEnum.PATENT:
            patentPubIds.add(pub.getNsfcPrjRptPubPK().getPubId());
            break;

          default:
            break;
        }
      }
    }
    // 奖励

    statRptMap.put("pub_award", this.getPrjFinalAwardStats(awardPubIds));

    // 专著

    statRptMap.put("pub_book", this.getPrjFinalBookStats(bookPubIds));

    // 会议

    statRptMap.put("pub_conf_paper", this.getPrjFinalConfStats(confPubIds));

    // 期刊
    Map<String, Object> pubStatMap = new HashMap<String, Object>();
    pubStatMap.put("journal_idx_sci", journal_idx_sci);
    pubStatMap.put("journal_idx_ei", journal_idx_ei);
    pubStatMap.put("journal_idx_istp", journal_idx_istp);
    pubStatMap.put("journal_idx_isr", journal_idx_isr);
    statRptMap.put("journal", this.getPrjFinalJnlLevelStats(pubStatMap, jnlPubIds));

    // 专利

    statRptMap.put("pub_patent", this.getPrjFinalPatentStats(patentPubIds));
    // return statRptMap;
  }

  /**
   * 获取进展/结题报告奖励统计.
   * 
   *
   * @param pubIds
   * @return
   * @throws Exception
   * @throws ServiceException
   */
  private Map<String, Object> getPrjFinalAwardStats(List<Long> pubIds) throws Exception {
    Integer award_nat_zrkx1 = 0;
    Integer award_nat_zrkx2 = 0;
    Integer award_nat_kjjb1 = 0;
    Integer award_nat_kjjb2 = 0;
    Integer award_nat_fm1 = 0;
    Integer award_nat_fm2 = 0;
    Integer award_prv_zrkx1 = 0;
    Integer award_prv_zrkx2 = 0;
    Integer award_prv_kjjb1 = 0;
    Integer award_prv_kjjb2 = 0;
    Integer award_int_xs = 0;
    Integer award_other = 0;
    if (!CollectionUtils.isEmpty(pubIds)) {
      for (Long pubId : pubIds) {
        PubXmlDocument pubXmlDoc = null;
        try {
          pubXmlDoc = this.scholarPublicationXmlManager.getPubXml(pubId);
        } catch (Exception e) {
          logger.error("获取成果（奖励）的XML出现异常了喔,pubId={}", pubId, e);
          throw new Exception(e);
        }
        if (pubXmlDoc != null) {
          String awardGrade = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_AWARD_XPATH, "award_grade");
          String awardCategory = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_AWARD_XPATH, "award_category");
          if (awardGrade.indexOf("国际学术奖") > -1) {
            award_int_xs++;
          } else if (awardGrade.indexOf("国家一等奖") > -1) {
            if (awardCategory.indexOf("自然科学") > -1) {
              award_nat_zrkx1++;
            } else if (awardCategory.indexOf("科技进步") > -1) {
              award_nat_kjjb1++;
            } else if (awardCategory.indexOf("发明") > -1) {
              award_nat_fm1++;
            } else {
              award_other++;
            }
          } else if (awardGrade.indexOf("国家二等奖") > -1) {
            if (awardCategory.indexOf("自然科学") > -1) {
              award_nat_zrkx2++;
            } else if (awardCategory.indexOf("科技进步") > -1) {
              award_nat_kjjb2++;
            } else if (awardCategory.indexOf("发明") > -1) {
              award_nat_fm2++;
            } else {
              award_other++;
            }
          } else if (awardGrade.indexOf("省部一等奖") > -1) {
            if (awardCategory.indexOf("自然科学") > -1) {
              award_prv_zrkx1++;
            } else if (awardCategory.indexOf("科技进步") > -1) {
              award_prv_kjjb1++;
            } else {
              award_other++;
            }
          } else if (awardGrade.indexOf("省部二等奖") > -1) {
            if (awardCategory.indexOf("自然科学") > -1) {
              award_prv_zrkx2++;
            } else if (awardCategory.indexOf("科技进步") > -1) {
              award_prv_kjjb2++;
            } else {
              award_other++;
            }
          } else {
            award_other++;
          }
        }
      }
    }

    Map<String, Object> pubStatMap = new HashMap<String, Object>();

    pubStatMap.put("award_nat_zrkx1", award_nat_zrkx1);
    pubStatMap.put("award_nat_zrkx2", award_nat_zrkx2);
    pubStatMap.put("award_nat_kjjb1", award_nat_kjjb1);
    pubStatMap.put("award_nat_kjjb2", award_nat_kjjb2);
    pubStatMap.put("award_nat_fm1", award_nat_fm1);
    pubStatMap.put("award_nat_fm2", award_nat_fm2);
    pubStatMap.put("award_prv_zrkx1", award_prv_zrkx1);
    pubStatMap.put("award_prv_zrkx2", award_prv_zrkx2);
    pubStatMap.put("award_prv_kjjb1", award_prv_kjjb1);
    pubStatMap.put("award_prv_kjjb2", award_prv_kjjb2);
    pubStatMap.put("award_int_xs", award_int_xs);
    pubStatMap.put("award_other", award_other);
    return pubStatMap;
  }

  /**
   * 获取进展/结题报告专著统计.
   * 
   *
   * @param pubIds
   * @return
   * @throws Exception
   * @throws ServiceException
   */
  private Map<String, Object> getPrjFinalBookStats(List<Long> pubIds) throws Exception {
    Integer zh_ycb = 0;
    Integer zh_dcb = 0;
    Integer en_ycb = 0;
    Integer en_dcb = 0;
    if (!CollectionUtils.isEmpty(pubIds)) {
      for (Long pubId : pubIds) {
        PubXmlDocument pubXmlDoc = null;
        try {
          pubXmlDoc = this.scholarPublicationXmlManager.getPubXml(pubId);
        } catch (Exception e) {
          logger.error("获取成果（专著）的XML出现异常了喔,pubId={}", pubId, e);
          throw new Exception(e);
        }
        if (pubXmlDoc != null) {
          String language = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "language");
          String language2 = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_BOOK_XPATH, "language");
          String publishYear = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_year");
          if (StringUtils.isBlank(language) || XmlUtil.isChinese(language) || "1".equals(language2)) {
            if (StringUtils.isBlank(publishYear)) {
              zh_dcb++;
            } else {
              zh_ycb++;
            }
          } else {
            if (StringUtils.isBlank(publishYear)) {
              en_dcb++;
            } else {
              en_ycb++;
            }
          }
        }
      }
    }
    Map<String, Object> pubStatMap = new HashMap<String, Object>();
    pubStatMap.put("zh_ycb", zh_ycb);
    pubStatMap.put("zh_dcb", zh_dcb);
    pubStatMap.put("en_ycb", en_ycb);
    pubStatMap.put("en_dcb", en_dcb);
    return pubStatMap;
  }

  /**
   * 获取进展/结题报告会议统计.
   * 
   * @param pubStatMap
   * @param pubIds
   * @return
   * @throws ServiceException
   */
  private Map<String, Object> getPrjFinalConfStats(List<Long> pubIds) throws Exception {
    Integer report_int_ty = 0;
    Integer report_int_fz = 0;
    Integer report_nat_ty = 0;
    Integer report_nat_fz = 0;
    if (!CollectionUtils.isEmpty(pubIds)) {
      for (Long pubId : pubIds) {
        PubXmlDocument pubXmlDoc = null;
        try {
          pubXmlDoc = this.scholarPublicationXmlManager.getPubXml(pubId);
        } catch (Exception e) {
          logger.error("获取成果（会议）的XML出现异常了喔,pubId={}", pubId, e);
          throw new Exception(e);
        }
        if (pubXmlDoc != null) {
          String confName = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "conf_name");
          String paperType = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "paper_type");
          if (confName.toUpperCase().indexOf("international") > -1 || confName.indexOf("国际") > -1) {
            if ("A".equals(paperType)) {
              report_int_ty++;
            } else if ("E".equals(paperType)) {
              report_int_fz++;
            }
          } else {
            if ("A".equals(paperType)) {
              report_nat_ty++;
            } else if ("E".equals(paperType)) {
              report_nat_fz++;
            }
          }
        }
      }
    }
    Map<String, Object> pubStatMap = new HashMap<String, Object>();
    pubStatMap.put("report_int_ty", report_int_ty);
    pubStatMap.put("report_int_fz", report_int_fz);
    pubStatMap.put("report_nat_ty", report_nat_ty);
    pubStatMap.put("report_nat_fz", report_nat_fz);
    return pubStatMap;
  }

  /**
   * 获取进展/结题报告期刊级别统计.
   * 
   * @param pubStatMap
   * @param pubIds
   * @return
   * @throws ServiceException
   */
  private Map<String, Object> getPrjFinalJnlLevelStats(Map<String, Object> pubStatMap, List<Long> pubIds)
      throws Exception {
    Integer journal_int = 0;
    Integer journal_home_hx = 0;
    Integer journal_home_yb = 0;
    if (!CollectionUtils.isEmpty(pubIds)) {
      try {

        List<Long> jids = new ArrayList<Long>();
        List<Long> pubIdsTemp = new ArrayList<Long>();
        for (int i = 0; i < pubIds.size(); i++) {
          pubIdsTemp.add(pubIds.get(i));
          if (i % 500 == 0) {
            List<Publication> pubs = myPublicationQueryService.getPubsByPubIds(pubIdsTemp);
            for (Publication p : pubs) {
              jids.add(p.getJID());
            }
            pubIdsTemp.clear();
            // break;
          }
        }

        if (!CollectionUtils.isEmpty(jids)) {
          Map<String, Object> jnlLevelStatsMap = jnlLevelService.getJnlLevelStats(jids);
          journal_int = Integer.parseInt(jnlLevelStatsMap.get("intJnlNum").toString());
          journal_home_hx = Integer.parseInt(jnlLevelStatsMap.get("homeCoreJnlNum").toString());
          int otherJnlNum = Integer.parseInt(jnlLevelStatsMap.get("otherJnlNum").toString());
          journal_home_yb = pubIds.size() - jids.size() + otherJnlNum;
        }
      } catch (Exception e) {
        logger.error("获取进展/结题报告期刊级别统计出现异常了喔,pubIds={}", pubIds.toString(), e);
        throw new Exception(e);
      }
    }

    pubStatMap.put("journal_int", journal_int);
    pubStatMap.put("journal_home_hx", journal_home_hx);
    pubStatMap.put("journal_home_yb", journal_home_yb);
    return pubStatMap;
  }

  /**
   * 获取进展/结题报告专利统计.
   * 
   * @param pubStatMap
   * @param pubIds
   * @return
   * @throws ServiceException
   */
  private Map<String, Object> getPrjFinalPatentStats(List<Long> pubIds) throws Exception {
    Integer home_app = 0;
    Integer home_auth = 0;
    Integer abroad_app = 0;
    Integer abroad_auth = 0;
    if (!CollectionUtils.isEmpty(pubIds)) {
      for (Long pubId : pubIds) {
        PubXmlDocument pubXmlDoc = null;
        try {
          pubXmlDoc = this.scholarPublicationXmlManager.getPubXml(pubId);
        } catch (Exception e) {
          logger.error("获取成果（专利）的XML出现异常了喔,pubId={}", pubId, e);
          throw new Exception(e);
        }
        if (pubXmlDoc != null) {
          String countryName = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "country_name");
          String startYear = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "start_year");
          boolean isHome = false;
          if (StringUtils.isBlank(countryName)) {
            isHome = true;
          }
          if (!isHome && (countryName.contains("中国") || countryName.contains("香港") || countryName.contains("澳门")
              || countryName.contains("台湾"))) {
            if (StringUtils.isBlank(startYear)) {
              home_app++;
            } else {
              home_auth++;
            }
            isHome = true;
          } else {
            if (!isHome) {
              List<ConstRegion> allCNRegions = constRegionService.getAllCNRegion();
              for (ConstRegion region : allCNRegions) {
                if (region.getZhName().contains(countryName) || countryName.contains(region.getZhName())) {
                  if (StringUtils.isBlank(startYear)) {
                    home_app++;
                  } else {
                    home_auth++;
                  }
                  isHome = true;
                  break;
                }
              }
            }
            if (!isHome) {
              if (StringUtils.isBlank(startYear)) {
                abroad_app++;
              } else {
                abroad_auth++;
              }
            }
          }
        }
      }
    }
    Map<String, Object> pubStatMap = new HashMap<String, Object>();
    pubStatMap.put("home_app", home_app);
    pubStatMap.put("home_auth", home_auth);
    pubStatMap.put("abroad_app", abroad_app);
    pubStatMap.put("abroad_auth", abroad_auth);
    return pubStatMap;
  }

}
