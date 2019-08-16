package com.smate.center.batch.service.pub;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.ConPrjRptPubDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.ConPrjRptPub;
import com.smate.center.batch.model.sns.pub.ConstPubType;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.center.batch.model.sns.pub.PublicationList;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.util.pub.ConstPublicationType;
import com.smate.core.base.utils.constant.PubXmlConstants;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("conPrjRptPubService")
@Transactional(rollbackFor = Exception.class)
public class ConPrjRptPubServiceImpl implements ConPrjRptPubService {

  private static final long serialVersionUID = -2257604385739166582L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConPrjRptPubDao conPrjRptPubDao;
  @Autowired
  private PublicationService publicationService;
  @Autowired
  private PublicationListService publicationListService;
  @Autowired
  private ConstPubTypeService constPubTypeService;

  @Override
  public List<ConPrjRptPub> findRptPubs(Long psnId, Long nsfcRptId) throws ServiceException {
    try {
      List<ConPrjRptPub> list = this.conPrjRptPubDao.findRptPubs(psnId, nsfcRptId);
      if (CollectionUtils.isNotEmpty(list)) {
        for (ConPrjRptPub rptPub : list) {
          ConstPubType pubType = this.constPubTypeService.get(rptPub.getPubType());
          rptPub
              .setPubTypeName(StringUtils.isNotBlank(pubType.getZhName()) ? pubType.getZhName() : pubType.getEnName());
        }
      }
      return list;
    } catch (Exception e) {
      logger.error("查找延续项目报告成果出现错误，psnId=" + psnId + ",nsfcRptId=" + nsfcRptId, e);
      throw new ServiceException("查找延续项目报告成果出现错误，psnId=" + psnId + ",nsfcRptId=" + nsfcRptId, e);
    }
  }

  @Override
  public List<Long> findRptPubIds(Long nsfcRptId) throws ServiceException {
    try {
      return this.conPrjRptPubDao.findPubIds(nsfcRptId);
    } catch (Exception e) {
      logger.error("获取以提交的成果ID出现错误，nsfcRptId=" + nsfcRptId, e);
      throw new ServiceException("获取以提交的成果ID出现错误，nsfcRptId=" + nsfcRptId, e);
    }
  }

  @Override
  public void addPubFromMate(Long nsfcRptId, String pubIds) throws ServiceException {
    try {
      if (StringUtils.isBlank(pubIds)) {
        return;
      }
      List<Long> pubIdList = new ArrayList<Long>();
      String[] aPubIds = pubIds.split(",");
      for (String pubId : aPubIds) {
        pubIdList.add(Long.valueOf(pubId));
      }
      List<Publication> pubList = this.publicationService.findPubsByIds(pubIdList);
      Integer maxSeqNo = this.conPrjRptPubDao.findMaxSeq(nsfcRptId);
      for (Publication pub : pubList) {
        ConPrjRptPub conPrjRptPub = new ConPrjRptPub();
        conPrjRptPub = this.warpRptPubFromSmatePub(pub, conPrjRptPub);
        if (conPrjRptPub == null) {
          continue;
        }
        conPrjRptPub.setNsfcRptId(nsfcRptId);
        conPrjRptPub.setSeqNo(maxSeqNo + 1);
        maxSeqNo++;
        this.conPrjRptPubDao.save(conPrjRptPub);
      }
    } catch (Exception e) {
      logger.error("添加延续报告成果出现错误，nsfcRptId=" + nsfcRptId + ",pubIds=" + pubIds, e);
      throw new ServiceException("添加延续报告成果出现错误，nsfcRptId=" + nsfcRptId + ",pubIds=" + pubIds, e);
    }

  }

  @Override
  public void removePubFromRpt(Long psnId, Long nsfcRptId, String pubIds) throws ServiceException {
    if (StringUtils.isBlank(pubIds)) {
      return;
    }

    List<Long> pubIdList = new ArrayList<Long>();
    String[] aPubIds = pubIds.split(",");
    for (String pubId : aPubIds) {
      pubIdList.add(Long.valueOf(pubId));
    }
    this.conPrjRptPubDao.removePubFromRpt(nsfcRptId, pubIdList);

    List<ConPrjRptPub> rptPubList = this.findRptPubs(psnId, nsfcRptId);
    if (CollectionUtils.isEmpty(rptPubList)) {
      return;
    }

    Integer seqNo = 1;
    for (ConPrjRptPub pub : rptPubList) {
      pub.setSeqNo(seqNo);
      this.conPrjRptPubDao.save(pub);
      seqNo++;
    }

  }

  @Override
  public void updateTag(Long nsfcRptId, Long pubId, Integer isTag) throws ServiceException {
    try {
      ConPrjRptPub conPrjRptPub = this.conPrjRptPubDao.findPubInfo(nsfcRptId, pubId);
      if (conPrjRptPub != null) {
        conPrjRptPub.setIsTag(isTag);
        this.conPrjRptPubDao.save(conPrjRptPub);
      }
    } catch (Exception e) {
      logger.error("更新延续报告成果标注情况出现错误，nsfcRptId=" + nsfcRptId + ",pubId=" + pubId + ",isTag=" + isTag, e);
      throw new ServiceException("更新延续报告成果标注情况出现错误，nsfcRptId=" + nsfcRptId + ",pubId=" + pubId + ",isTag=" + isTag, e);
    }

  }

  @Override
  public void updateListInfo(Long nsfcRptId, Long pubId, String listInfo) throws ServiceException {
    try {
      ConPrjRptPub conPrjRptPub = this.conPrjRptPubDao.findPubInfo(nsfcRptId, pubId);
      if (conPrjRptPub != null) {
        conPrjRptPub.setListInfo(listInfo);
        this.conPrjRptPubDao.save(conPrjRptPub);
      }

      String[] citations = StringUtils.split(listInfo, ",");
      Integer listIstp = 0;
      Integer listSci = 0;
      Integer listEi = 0;
      if (citations.length > 0) {
        for (String cited : citations) {
          if ("EI".equals(cited)) {
            listEi = 1;
          }
          if ("SCI".equals(cited)) {
            listSci = 1;
          }
          if ("ISTP".equals(cited)) {
            listIstp = 1;
          }
        }
      }
      String xml = publicationService.getPubXmlById(pubId);
      xml = this.reBuildCitedXml(xml, listEi, listSci, listIstp);
      PubXmlProcessContext context = new PubXmlProcessContext();
      context.setCurrentPubId(pubId);
      context.setCurrentUserId(conPrjRptPub.getPubOwnerPsnId());
      publicationService.updatePublication(xml, context);
    } catch (Exception e) {
      logger.error("更新延续报告成果标注情况出现错误，nsfcRptId=" + nsfcRptId + ",pubId=" + pubId + ",listInfo=" + listInfo, e);
      throw new ServiceException(
          "更新延续报告成果标注情况出现错误，nsfcRptId=" + nsfcRptId + ",pubId=" + pubId + ",listInfo=" + listInfo, e);
    }

  }

  private String reBuildCitedXml(String xml, Integer listEi, Integer listSci, Integer listIstp) {
    try {
      PubXmlDocument doc = new PubXmlDocument(xml);
      if (!doc.existsNode(PubXmlConstants.PUB_LIST_XPATH)) {
        doc.createElement(PubXmlConstants.PUB_LIST_XPATH);
      }
      doc.setNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ei", listEi == null || listEi == 0 ? "0" : "1");
      doc.setNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_sci", listSci == null || listSci == 0 ? "0" : "1");
      doc.setNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_istp", listIstp == null || listIstp == 0 ? "0" : "1");
      String listSSCI = doc.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ssci");
      String listSCI = doc.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_sci");
      String listISTP = doc.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_istp");
      if ((listSSCI == null || "0".equals(listSSCI) || "".equals(listSSCI)) && (listSCI == null || "0".equals(listSCI))
          && (listISTP == null || "0".equals(listISTP))) {
        doc.removeAttribute(PubXmlConstants.PUBLICATION_XPATH, "cite_times");
      }
      return doc.getXmlString();
    } catch (DocumentException e) {
      logger.error("xml格式有错" + xml);
    }
    return null;
  }

  private ConPrjRptPub warpRptPubFromSmatePub(Publication pub, ConPrjRptPub conPrjRptPub) throws ServiceException {
    if (pub == null) {
      return null;
    }
    if (conPrjRptPub == null) {
      conPrjRptPub = new ConPrjRptPub();
    }
    String title = pub.getZhTitle();
    if (StringUtils.isBlank(title)) {
      title = pub.getEnTitle();
    }
    String source = pub.getBriefDesc();
    if (StringUtils.isBlank(source)) {
      source = pub.getBriefDescEn();
    }
    String listSource = null;
    String listInfo = null;
    if (ConstPublicationType.PUB_JOURNAL_TYPE.equals(pub.getTypeId())
        || ConstPublicationType.PUB_CONFERECE_TYPE.equals(pub.getTypeId())
        || ConstPublicationType.PUB_OTHERS_TYPE.equals(pub.getTypeId())) {
      PublicationList pubListInfo = this.publicationListService.getPublicationList(pub.getId());
      StringBuilder listInfoSource = new StringBuilder();
      StringBuilder listInfoSb = new StringBuilder();
      if (pubListInfo != null) {
        if (pubListInfo.getListEi() != null && pubListInfo.getListEi().intValue() == 1) {
          listInfoSb.append(",EI");
        }
        if (pubListInfo.getListSci() != null && pubListInfo.getListSci().intValue() == 1) {
          listInfoSb.append(",SCI");
        }
        if (pubListInfo.getListIstp() != null && pubListInfo.getListIstp().intValue() == 1) {
          listInfoSb.append(",ISTP");
        }
        if (StringUtils.startsWith(listInfoSb, ",")) {
          listInfoSb = listInfoSb.deleteCharAt(0);
        }

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
        listSource = listInfoSource.toString();
        listInfo = listInfoSb.toString();
      }
    }
    conPrjRptPub.setPubId(pub.getId());
    conPrjRptPub.setAuthors(pub.getAuthorNames());
    conPrjRptPub.setTitle(title);
    conPrjRptPub.setPubType(pub.getTypeId());
    conPrjRptPub.setPubYear(pub.getPublishYear());
    conPrjRptPub.setPubMoth(pub.getPublishMonth());
    conPrjRptPub.setPubDay(pub.getPublishDay());
    conPrjRptPub.setListInfo(listInfo);
    conPrjRptPub.setListInfoSource(listSource);
    conPrjRptPub.setSource(source);
    conPrjRptPub.setPubOwnerPsnId(pub.getPsnId());
    conPrjRptPub.setImpactFactors(pub.getImpactFactors());
    conPrjRptPub.setCitedTimes(pub.getCitedTimes());
    conPrjRptPub.setIsTag(0);
    return conPrjRptPub;
  }

  @Override
  public void updatePubSeq(Long nsfcRptId, String jsonParams) throws ServiceException {
    try {
      if (StringUtils.isBlank(jsonParams)) {
        return;
      }
      JSONArray jArray = JSONArray.fromObject(jsonParams);
      if (CollectionUtils.isNotEmpty(jArray)) {
        for (int i = 0; i < jArray.size(); i++) {
          JSONObject obj = jArray.getJSONObject(i);
          Integer seqNo = obj.getInt("seqNo");
          Long pubId = obj.getLong("pubId");
          ConPrjRptPub conPrjRptPub = this.conPrjRptPubDao.findPubInfo(nsfcRptId, pubId);
          conPrjRptPub.setSeqNo(seqNo);
          this.conPrjRptPubDao.save(conPrjRptPub);
        }
      }
    } catch (Exception e) {
      logger.error("保存成果排序时发生错误！", e);
      throw new ServiceException("保存成果排序时发生错误！", e);
    }
  }

  @Override
  public void updateMatePubToConPub(Long pubId) throws ServiceException {
    try {
      Publication pub = this.publicationService.getPublicationById(pubId);
      List<ConPrjRptPub> rptPubList = this.conPrjRptPubDao.findPubList(pubId);
      if (pub != null && CollectionUtils.isNotEmpty(rptPubList)) {
        for (ConPrjRptPub rptPub : rptPubList) {
          rptPub = this.warpRptPubFromSmatePub(pub, rptPub);
          this.conPrjRptPubDao.save(rptPub);
        }
      }
    } catch (Exception e) {
      logger.error("SCM同步成果信息到延续报告成果时候出现错误！pubId=" + pubId, e);
      throw new ServiceException("SCM同步成果信息到延续报告成果时候出现错误！pubId=" + pubId, e);
    }
  }

}
