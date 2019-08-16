package com.smate.center.batch.service.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.NsfcExpertPubDao;
import com.smate.center.batch.enums.pub.XmlOperationEnum;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.PublicationNotFoundException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.ConstPubType;
import com.smate.center.batch.model.sns.pub.ExpertPubModel;
import com.smate.center.batch.model.sns.pub.NsfcExpertPub;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.center.batch.model.sns.pub.PublicationForm;
import com.smate.center.batch.model.sns.pub.PublicationList;
import com.smate.center.batch.model.sns.pub.SyncProposalModel;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.IrisNumberUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 评议专家成果模块
 * 
 * @author oyh
 * 
 */
@Service("expertPubService")
@Transactional(rollbackFor = Exception.class)
public class ExpertPubServiceImpl implements ExpertPubService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private NsfcExpertPubDao nsfcExpertPubDao;
  @Autowired
  private MyPublicationQueryService myPublicationQueryService;
  @Autowired
  private ScholarPublicationXmlManager scholarPublicationXmlManager;
  @Autowired
  private PublicationListService publicationListService;
  @Autowired
  private PublicationService publicationService;
  @Autowired
  private ConstPubTypeService constPubTypeService;

  private MyPublicationQueryService getMyPublicationQueryService() throws ServiceException {

    return this.myPublicationQueryService;
  }

  private ScholarPublicationXmlManager getScholarPublicationXmlManager() throws ServiceException {

    return this.scholarPublicationXmlManager;

  }

  private PublicationListService getPublicationListService() throws ServiceException {

    return this.publicationListService;

  }

  private PublicationService getPublicationService() throws ServiceException {

    return this.publicationService;
  }

  private ConstPubTypeService getConstPubTypeService() throws ServiceException {

    return this.constPubTypeService;
  }

  @Override
  public void addPublicationFromMyMate(ExpertPubModel form) throws ServiceException {
    try {
      if (StringUtils.isNotBlank(form.getPubIds())) {
        List<Long> list = new ArrayList<Long>();
        String[] aPubIds = form.getPubIds().split(",");
        for (String pubId : aPubIds) {
          list.add(Long.valueOf(pubId));
        }
        List<Publication> pubList = getMyPublicationQueryService().findPubsForNsfc(list);
        for (Publication pub : pubList) {
          NsfcExpertPub expertPub = new NsfcExpertPub();
          expertPub.setPubId(pub.getId());
          PublicationForm pubForm = new PublicationForm();
          pubForm.setPubId(pub.getId());
          expertPub.setPubOwnerPsnId(pub.getPsnId());
          pubForm = getScholarPublicationXmlManager().loadXml(pubForm);
          this.wrapRptPublication(pub, expertPub, pubForm.getPubXml(), true);
          Integer seqNo = this.nsfcExpertPubDao.getNsfcExpertPubMaxSeqNo();
          expertPub.setSeqNo(seqNo.intValue() + 1);
          expertPub.setNodeId(SecurityUtils.getCurrentUserNodeId());
          this.nsfcExpertPubDao.save(expertPub);
        }

      }
    } catch (ServiceException e) {
      logger.error(e.getMessage() + e);
    } catch (PublicationNotFoundException e) {
      logger.error(e.getMessage() + e);
    } catch (DaoException e) {
      logger.error(e.getMessage() + e);
    }

  }

  private void wrapRptPublication(Publication pub, NsfcExpertPub expertPub, String xml, boolean isReList)
      throws ServiceException {
    PubXmlDocument doc = null;
    try {
      doc = new PubXmlDocument(xml);
    } catch (DocumentException e) {
      logger.error(e.getMessage() + e);
    }
    if (doc != null) {
      expertPub.setAuthors(pub.getAuthorNames());
      expertPub.setIsOpen(1);
      if (pub.getTypeId() == 5) {
        expertPub.setIsTag(0);
      } else {
        expertPub.setIsTag(1);
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
        }
        expertPub.setListInfo(listInfo);

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
        expertPub.setListInfoSource(listInfoSource.toString());

      }

      expertPub.setPubType(pub.getTypeId());
      expertPub.setPubYear(pub.getPublishYear());
      String title = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title");
      if (StringUtils.isBlank(title)) {
        title = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title");
      }
      expertPub.setTitle(XmlUtil.trimAllHtml(title));
      Locale locale = LocaleContextHolder.getLocale();
      String brief = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc");
      if (locale.equals(Locale.US)) {
        String briefEn = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc_en");
        if (StringUtils.isBlank(briefEn)) {
          briefEn = brief;
        }
        expertPub.setSource(briefEn);
      } else {
        expertPub.setSource(brief);
      }
      expertPub.setVersion(pub.getVersionNo());

    }
  }

  @Override
  public List<NsfcExpertPub> getMyExpertPubs() throws ServiceException {

    List<NsfcExpertPub> list = new ArrayList<NsfcExpertPub>();
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      list = this.nsfcExpertPubDao.getMyExpertPubs(psnId);
      List<ConstPubType> pubTypeList = this.constPubTypeService.getAll();
      for (NsfcExpertPub expertPub : list) {

        for (ConstPubType pubType : pubTypeList) {
          if (pubType.getId().intValue() == expertPub.getPubType().intValue()) {
            expertPub.setPubTypeName(
                StringUtils.isNotBlank(pubType.getZhName()) ? pubType.getZhName() : pubType.getEnName());
            break;
          }

        }
      }
    } catch (DaoException e) {
      logger.error(e.getMessage() + e);
    }
    return list;

  }

  @Override
  public void removeExpertPubs(ExpertPubModel form) throws ServiceException {
    JSONArray jsonArray = JSONArray.fromObject(form.getJsonParams());
    Long psnId = SecurityUtils.getCurrentUserId();
    if (CollectionUtils.isNotEmpty(jsonArray)) {
      for (int i = 0; i < jsonArray.size(); i++) {
        JSONObject obj = jsonArray.getJSONObject(i);
        Long pubId = obj.getLong("pubId");

        try {

          this.nsfcExpertPubDao.deleteExpertPubs(psnId, pubId);
        } catch (DaoException e) {
          logger.error("移除专家成果失败pubId={},psnId={}", new Object[] {pubId, psnId, e});

        }

      }
      try {
        List<Long> lList = this.nsfcExpertPubDao.getExpertPubOrder(psnId);
        if (CollectionUtils.isNotEmpty(lList)) {
          for (int i = 1; i <= lList.size(); i++) {
            this.nsfcExpertPubDao.updateExpertPubOrder(i, lList.get(i - 1), psnId);
          }
        }
      } catch (Exception e) {

        logger.error("更新排序失败！", e);
        throw new ServiceException(e);
      }
    }

  }

  @Override
  public void saveExpertPubs(ExpertPubModel form) throws ServiceException {
    if (StringUtils.isNotBlank(form.getJsonParams())) {
      JSONArray jsonArray = JSONArray.fromObject(form.getJsonParams());
      Long psnId = SecurityUtils.getCurrentUserId();
      if (CollectionUtils.isNotEmpty(jsonArray)) {
        for (int i = 0; i < jsonArray.size(); i++) {
          try {
            JSONObject obj = jsonArray.getJSONObject(i);
            Integer pubType = obj.getInt("pubType");
            Long pubId = obj.getLong("pubId");
            Integer nodeId = obj.getInt("nodeId");
            Long ownerPsnId = obj.getLong("ownerPsnId");
            Integer listEi = null;
            Integer listSci = null;
            Integer listIstp = null;
            if (pubType == 4 || pubType == 3) {
              listEi = IrisNumberUtils.createInteger(obj.getString("listEi"));
              listSci = IrisNumberUtils.createInteger(obj.getString("listSci"));
              listIstp = IrisNumberUtils.createInteger(obj.getString("listIstp"));
              // 修改XML和pub_list
              if (ownerPsnId.longValue() == SecurityUtils.getCurrentUserId().longValue()) {

                String xml = getPublicationService().getPubXmlById(pubId);
                xml = this.reBuildCitedXml(xml, listEi, listSci, listIstp);
                PubXmlProcessContext context = new PubXmlProcessContext();
                context.setCurrentPubId(pubId);
                context.setCurrentUserId(psnId);
                getPublicationService().updatePublication(xml, context);
              }
            }

          } catch (ServiceException e) {
            logger.error(e.getMessage() + e);
            throw new ServiceException("保存专家成果失败....");
          }
        }
      }
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

  @Override
  public void saveExpertPubTag(Integer isTag, Long key) throws ServiceException {
    try {

      NsfcExpertPub expertPub = this.nsfcExpertPubDao.get(key);
      if (isTag == null || isTag == 0) {
        expertPub.setIsTag(0);
      } else if (isTag > 0) {
        expertPub.setIsTag(1);
      }
      this.nsfcExpertPubDao.save(expertPub);
    } catch (Exception e) {
      logger.error("保存key={}的tag={}失败", new Object[] {key, isTag, e});
    }

  }

  @Override
  public void saveExpertPubCitation(String citation, Long key) throws ServiceException {
    try {
      NsfcExpertPub pubTemp = this.nsfcExpertPubDao.get(key);
      pubTemp.setListInfo(citation);
      this.nsfcExpertPubDao.save(pubTemp);

      Publication pub = null;
      pub = getPublicationService().getPublicationById(pubTemp.getPubId());
      if (pub != null && pub.getPsnId().longValue() == SecurityUtils.getCurrentUserId().longValue()) {
        PublicationForm pubForm = new PublicationForm();
        pubForm.setPubId(pub.getId());
        pubForm = getScholarPublicationXmlManager().loadXml(pubForm);
        if (pubForm != null) {
          String xml = pubForm.getPubXml();
          String[] citations = StringUtils.split(citation, ",");
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
          // PublicationList pubList =
          // this.publicationListService.getPublicationList(pubId);
          // if (pubList == null) {
          // pubList = new PublicationList();
          // pubList.setId(pub.getId());
          // pubList.setListEi(listEi);
          // pubList.setListSci(listSci);
          // pubList.setListIstp(listIstp);
          // } else {
          // pubList.setListEi(listEi);
          // pubList.setListSci(listSci);
          // pubList.setListIstp(listIstp);
          // }
          // this.publicationListService.savePublictionList(pubList);
          xml = this.reBuildCitedXml(xml, listEi, listSci, listIstp);
          PubXmlProcessContext context = new PubXmlProcessContext();
          context.setCurrentPubId(pub.getId());
          context.setCurrentUserId(SecurityUtils.getCurrentUserId().longValue());
          context.setCurrentAction(XmlOperationEnum.Edit);
          getPublicationService().updatePublication(xml, context);
        }
      }
    } catch (Exception e) {
      logger.error(e.getMessage() + e);
    }

  }

  @SuppressWarnings("unchecked")
  @Override
  @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
  public void savePublicationSort(ExpertPubModel form) throws ServiceException {
    JSONArray jArray = JSONArray.fromObject(form.getJsonParams());
    if (CollectionUtils.isNotEmpty(jArray)) {
      try {
        List saveList = new ArrayList();
        for (int i = 0; i < jArray.size(); i++) {
          JSONObject obj = jArray.getJSONObject(i);
          Integer seqNo = obj.getInt("seqNo");
          Long pubId = obj.getLong("pubId");

          NsfcExpertPub expertPub = this.nsfcExpertPubDao.loadExpertPub(SecurityUtils.getCurrentUserId(), pubId);
          expertPub.setSeqNo(seqNo);
          saveList.add(expertPub);
        }

        this.nsfcExpertPubDao.saveExpertPubs(saveList);
      } catch (Exception e) {
        logger.error("调整排序功能失败！", e);
      }
    }
  }

  @Override
  public Set<Long> getExpertPubIds() throws ServiceException {
    try {
      Long psnId = SecurityUtils.getCurrentUserId();

      return this.nsfcExpertPubDao.getExpertPubIds(psnId);
    } catch (Exception e) {

      logger.error("查询psnId={}的专家成果id出现异常", new Object[] {SecurityUtils.getCurrentUserId(), e});

    }

    return null;
  }

  @Override
  public void syncPublicationToExpertuPub(PublicationForm loadXml) throws ServiceException {

    try {
      Publication pub = getPublicationService().getPublicationById(loadXml.getPubId());
      Long psnId = SecurityUtils.getCurrentUserId();
      NsfcExpertPub expertPub = this.nsfcExpertPubDao.loadExpertPub(psnId, loadXml.getPubId());
      if (expertPub != null) {

        expertPub.setNodeId(SecurityUtils.getCurrentUserNodeId());
        PublicationForm pubForm = new PublicationForm();
        pubForm.setPubId(loadXml.getPubId());
        pubForm = getScholarPublicationXmlManager().loadXml(pubForm);
        this.wrapRptPublication(pub, expertPub, pubForm.getPubXml(), true);
        this.nsfcExpertPubDao.save(expertPub);

      }

    } catch (Exception e) {

      logger.error("同步专家psnId={},pubId={}的成果数据失败！",
          new Object[] {SecurityUtils.getCurrentUserId(), loadXml.getPubId(), e});
      throw new ServiceException(e);

    }
  }

  @Override
  public List<NsfcExpertPub> loadExpertPubsByGuid(SyncProposalModel model) throws ServiceException {
    try {
      List<NsfcExpertPub> expertPubs = this.nsfcExpertPubDao.getMyExpertPubs(model.getPsnId());
      Map<Integer, String> map = new HashMap<Integer, String>();

      if (expertPubs != null) {
        List<ConstPubType> ret = getConstPubTypeService().getAll();
        if (ret != null) {
          for (ConstPubType pubType : ret) {
            map.put(pubType.getId(), pubType.getName());

          }

        }

        for (NsfcExpertPub expertPub : expertPubs) {

          expertPub.setPubTypeDes(map.get(expertPub.getPubType()));

        }

      }

      return expertPubs;

    } catch (Exception e) {

      logger.error("读取评议专家psnId={}的成果列表失败！", new Object[] {e, model.getPsnId()});

    }

    return null;
  }

  @Override
  public List<NsfcExpertPub> findNsfcExpertPubsByPubId(Long pubId) throws ServiceException {
    try {
      return this.nsfcExpertPubDao.findNsfcExpertPubsByPubId(pubId);
    } catch (DaoException e) {
      logger.error("根据PubId读取评议专家成果失败！PubId=" + pubId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveNsfcExpertPubList(List<NsfcExpertPub> nsfcExpertPubList) throws ServiceException {
    for (NsfcExpertPub nep : nsfcExpertPubList) {
      this.nsfcExpertPubDao.save(nep);
    }

  }

}
