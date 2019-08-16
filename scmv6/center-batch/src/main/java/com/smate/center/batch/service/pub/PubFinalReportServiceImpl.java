package com.smate.center.batch.service.pub;

import java.io.Serializable;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.ProjectReportPubDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.NsfcPrjRptPub;
import com.smate.center.batch.model.sns.pub.NsfcPrjRptPubId;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.center.batch.model.sns.pub.PublicationForm;
import com.smate.center.batch.model.sns.pub.PublicationList;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 成果解题报告模块.
 * 
 * @author LY
 * 
 */
@Service("pubFinalReportService")
@Transactional(rollbackFor = Exception.class)
public class PubFinalReportServiceImpl implements PubFinalReportService, Serializable {

  @Autowired
  private ProjectReportPubDao projectReportPubDao;
  @Autowired
  private PublicationService publicationService;
  @Autowired
  private PublicationListService publicationListService;

  @Override
  public void syncPublicationToFinalReport(PublicationForm loadXml) throws ServiceException {
    NsfcPrjRptPubId rptPubId = new NsfcPrjRptPubId();
    rptPubId.setRptId(loadXml.getRptId());
    rptPubId.setPubId(loadXml.getPubId());
    NsfcPrjRptPub rptPub = this.projectReportPubDao.getProjectReportPub(rptPubId);
    if (rptPub == null) {
      return;
    }
    Publication pub = getPublicationService().getPublicationById(loadXml.getPubId());
    this.wrapRptPublication(pub, rptPub, false);
    if (rptPub.getTitle().length() > 1000) {
      throw new ServiceException("000001");
    }
    rptPub.setNodeId(SecurityUtils.getCurrentUserNodeId());
    this.projectReportPubDao.saveProjectReportPub(rptPub);
  }

  private PublicationService getPublicationService() throws ServiceException {
    try {

      return this.publicationService;
    } catch (Exception e) {

      e.printStackTrace();
      return null;
    }

  }


  private void wrapRptPublication(Publication pub, NsfcPrjRptPub rptPub, boolean isReList) throws ServiceException {

    if (pub != null) {
      if (StringUtils.isNotBlank(pub.getAuthorNames())) {
        rptPub.setAuthors(pub.getAuthorNames().replaceAll("，|,", "、"));
      } else {
        rptPub.setAuthors(pub.getAuthorNames());
      }
      rptPub.setIsOpen(1);
      rptPub.setImpactFactors(pub.getImpactFactors());
      rptPub.setCitedTimes(pub.getCitedTimes());
      if (pub.getTypeId() == 5) {
        rptPub.setIsTag(0);
      } else {
        rptPub.setIsTag(1);
      }
      // 期刊、会议、其他
      if (pub.getTypeId() == 4 || pub.getTypeId() == 3 || pub.getTypeId() == 7) {
        PublicationList pubListInfo = getPublicationListService().getPublicationList(pub.getId());
        String listInfo = "";
        if (pubListInfo != null) {
          if (pubListInfo.getListEi() != null && pubListInfo.getListEi().intValue() == 1) {
            listInfo += "," + "EI";
          }
          if (pubListInfo.getListSci() != null && pubListInfo.getListSci().intValue() == 1) {
            listInfo += "," + "SCI";
          }
          if (pubListInfo.getListIstp() != null && pubListInfo.getListIstp().intValue() == 1) {
            listInfo += "," + "ISTP";
          }
          if (StringUtils.startsWith(listInfo, ",")) {
            listInfo = listInfo.substring(1);
          }

          StringBuilder listInfoSource = new StringBuilder();
          if (pubListInfo != null) {
            if (pubListInfo.getListEiSource() != null && pubListInfo.getListEiSource().intValue() == 1) {
              listInfoSource.append(",EI");
            }
            if (pubListInfo.getListSciSource() != null && pubListInfo.getListSciSource().intValue() == 1) {
              listInfoSource.append(",SCI");
            }
            if (pubListInfo.getListIstpSource() != null && pubListInfo.getListIstpSource().intValue() == 1) {
              listInfoSource.append(",ISTP");
            }
            if (StringUtils.startsWith(listInfoSource.toString(), ",")) {
              listInfoSource = listInfoSource.deleteCharAt(0);
            }
          }
          rptPub.setListInfoSource(listInfoSource.toString());
        }
        rptPub.setListInfo(listInfo);
        // if (isReList && StringUtils.isBlank(listInfo) &&
        // pub.getSourceDbId() != null) {
        // ConstRefDb constRefDb =
        // this.constRefDbService.getConstRefDbById(pub.getSourceDbId().longValue());
        // if (constRefDb != null) {
        // listInfo = constRefDb.getCode();
        // if ("EI".equalsIgnoreCase(listInfo) ||
        // "SCI".equalsIgnoreCase(listInfo)
        // || "ISTP".equalsIgnoreCase(listInfo)) {
        // rptPub.setListInfo(listInfo);
        // }
        // if (StringUtils.isNotBlank(listInfo)) {
        // if (pubListInfo == null) {
        // pubListInfo = new PublicationList();
        // pubListInfo.setId(pub.getId());
        //
        // } else {
        // if ("EI".equalsIgnoreCase(listInfo)) {
        // pubListInfo.setListEi(1);
        // }
        // if ("SCI".equalsIgnoreCase(listInfo)) {
        // pubListInfo.setListSci(1);
        // }
        // if ("ISTP".equalsIgnoreCase(listInfo)) {
        // pubListInfo.setListIstp(1);
        // }
        // }
        // this.publicationListService.savePublictionList(pubListInfo);
        // }
        // }
        //
        // }
      }

      rptPub.setPubType(pub.getTypeId());
      rptPub.setPubYear(pub.getPublishYear());
      String title = pub.getZhTitle();
      if (StringUtils.isBlank(title)) {
        title = pub.getEnTitle();
      }
      rptPub.setTitle(XmlUtil.trimAllHtml(title));
      rptPub.setVersion(pub.getVersionNo());

      Locale locale = LocaleContextHolder.getLocale();
      String brief = pub.getBriefDesc();
      if (locale.equals(Locale.US)) {
        String briefEn = pub.getBriefDescEn();
        rptPub.setSource(StringUtils.isBlank(briefEn) ? brief : briefEn);
      } else {
        rptPub.setSource(brief);
      }
    }
  }

  private PublicationListService getPublicationListService() throws ServiceException {

    return this.publicationListService;
  }

}
