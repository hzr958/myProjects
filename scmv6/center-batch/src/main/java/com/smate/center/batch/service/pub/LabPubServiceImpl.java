package com.smate.center.batch.service.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.center.batch.dao.sns.pub.NsfcLabGroupDao;
import com.smate.center.batch.dao.sns.pub.NsfcLabPubDao;
import com.smate.center.batch.enums.pub.XmlOperationEnum;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.PublicationNotFoundException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.ConstPubType;
import com.smate.center.batch.model.sns.pub.LabPubModel;
import com.smate.center.batch.model.sns.pub.NsfcExpertPub;
import com.smate.center.batch.model.sns.pub.NsfcLabGroup;
import com.smate.center.batch.model.sns.pub.NsfcLabPub;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.center.batch.model.sns.pub.PublicationForm;
import com.smate.center.batch.model.sns.pub.PublicationList;
import com.smate.center.batch.model.sns.pub.PublicationXml;
import com.smate.center.batch.model.sns.pub.StiasSyncGroup;
import com.smate.center.batch.model.sns.pub.SyncProposalModel;
import com.smate.center.batch.oldXml.prj.BriefUtilis;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 实验室成果模块
 * 
 * @author oyh
 * 
 */
@Service("labPubService")
@Transactional(rollbackFor = Exception.class)
public class LabPubServiceImpl implements LabPubService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 字段.
   */
  private static final List<String> FIELDS = new ArrayList<String>();
  static {
    FIELDS.add("/pub_award/@issue_ins_name"); // 颁奖机构
    FIELDS.add("/pub_book/@publisher"); // 出版社
    FIELDS.add("/pub_journal/@jname");
    FIELDS.add("/publication/@publish_year"); // 成果取得时间
    FIELDS.add("/publication/@publish_month");
    FIELDS.add("/publication/@publish_day");
    FIELDS.add("/publication/@volume");
    FIELDS.add("/publication/@issue");
    FIELDS.add("/publication/@start_page");
    FIELDS.add("/publication/@end_page");
    FIELDS.add("/pub_patent/@patent_org");
    FIELDS.add("/pub_patent/@patent_no");
    FIELDS.add("/pub_thesis/@issue_org");

  }

  @Autowired
  private NsfcLabPubDao nsfcLabPubDao;

  @Autowired
  private NsfcLabGroupDao nsfcLabGroupDao;
  @Autowired
  private ConstPubTypeService constPubTypeService;
  @Autowired
  private MyPublicationQueryService myPublicationQueryService;
  @Autowired
  private ScholarPublicationXmlManager scholarPublicationXmlManager;
  @Autowired
  private PublicationListService publicationListService;
  @Autowired
  private PublicationService publicationService;

  @Autowired
  private PubXmlStoreService pubXmlStoreService;

  private ConstPubTypeService getConstPubTypeService() throws ServiceException {

    return this.constPubTypeService;
  }

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

  private PubXmlStoreService getPubXmlStoreService() throws ServiceException {

    return this.pubXmlStoreService;
  }

  @Override
  public PublicationXml getById(Long pubId) throws ServiceException {

    try {
      return getPubXmlStoreService().get(pubId);
    } catch (BatchTaskException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public int addPublicationFromMyMate(LabPubModel form) throws ServiceException {
    try {
      int addTotal = 0;
      Long pId = form.getPId();
      NsfcLabGroup labGroup = this.nsfcLabGroupDao.get(pId);
      Integer year = labGroup.getYear();

      if (StringUtils.isNotBlank(form.getPubIds())) {
        Long psnId = SecurityUtils.getCurrentUserId();
        List<Long> list = new ArrayList<Long>();
        String[] aPubIds = form.getPubIds().split(",");
        for (String pubId : aPubIds) {
          list.add(Long.valueOf(pubId));
        }

        List<Publication> pubList = getMyPublicationQueryService().queryOutput(psnId, list);
        for (Publication pub : pubList) {

          if (!pub.getPublishYear().equals(year)) {
            continue;
          }
          addTotal++;
          NsfcLabPub labPub = new NsfcLabPub();
          labPub.setPubId(pub.getId());
          PublicationForm pubForm = new PublicationForm();
          pubForm.setPubId(pub.getId());
          labPub.setPubOwnerPsnId(pub.getPsnId());
          labPub.setLabPsnId(SecurityUtils.getCurrentUserId());
          labPub.setRefId(form.getPId());

          pubForm = getScholarPublicationXmlManager().loadXml(pubForm);
          this.wrapRptPublication(pub, labPub, pubForm.getPubXml(), true);
          Integer seqNo = this.nsfcLabPubDao.getNsfcLabPubMaxSeqNo(form.getPId());
          labPub.setSeqNo(seqNo.intValue() + 1);
          labPub.setNodeId(SecurityUtils.getCurrentUserNodeId());
          this.nsfcLabPubDao.save(labPub);
        }

      } else if (StringUtils.isNotBlank(form.getJsonParams())) {
        JSONArray array = JSONArray.fromObject(form.getJsonParams());
        if (CollectionUtils.isNotEmpty(array)) {
          for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            Long pubId = obj.getLong("pubId");
            Integer nodeId = obj.getInt("nodeId");
            Long psnId = obj.getLong("psnId");
            Publication pub = null;
            pub = getPublicationService().getPublicationById(pubId);
            if (pub != null) {

              if (!pub.getPublishYear().equals(year)) {
                continue;
              }
              addTotal++;
              NsfcLabPub nsfcLabPub = new NsfcLabPub();
              nsfcLabPub.setPubId(pub.getId());

              PublicationForm pubForm = new PublicationForm();
              pubForm.setPubId(pub.getId());
              nsfcLabPub.setPubOwnerPsnId(pub.getPsnId());
              nsfcLabPub.setLabPsnId(SecurityUtils.getCurrentUserId());
              nsfcLabPub.setRefId(form.getPId());
              pubForm = getScholarPublicationXmlManager().loadXml(pubForm);
              this.wrapRptPublication(pub, nsfcLabPub, pubForm.getPubXml(), true);
              if (nsfcLabPub.getTitle().length() > 1000) {
                throw new ServiceException("000001");

              }
              Integer seqNo = this.nsfcLabPubDao.getNsfcLabPubMaxSeqNo(form.getPId());
              nsfcLabPub.setSeqNo(seqNo.intValue() + 1);
              nsfcLabPub.setNodeId(nodeId);
              this.nsfcLabPubDao.save(nsfcLabPub);
            }
          }
        }
      }
      return addTotal;

    } catch (ServiceException e) {
      logger.error(e.getMessage() + e);
    } catch (PublicationNotFoundException e) {
      logger.error(e.getMessage() + e);
    } catch (DaoException e) {
      logger.error(e.getMessage() + e);
    }

    return 0;
  }

  private void wrapRptPublication(Publication pub, NsfcLabPub labPub, String xml, boolean isReList)
      throws ServiceException {
    PubXmlDocument doc = null;
    try {
      doc = new PubXmlDocument(xml);
    } catch (DocumentException e) {
      logger.error(e.getMessage() + e);
    }
    if (doc != null) {
      labPub.setAuthors(pub.getAuthorNames());
      labPub.setIsOpen(1);
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
        labPub.setListInfo(listInfo);

      }

      labPub.setPubType(pub.getTypeId());
      labPub.setPubYear(pub.getPublishYear() == null ? 0 : pub.getPublishYear());
      labPub.setPubMonth(pub.getPublishMonth() == null ? 0 : pub.getPublishMonth());
      labPub.setPubDay(pub.getPublishDay() == null ? 0 : pub.getPublishDay());
      String title = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title");
      if (StringUtils.isBlank(title)) {
        title = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title");
      }
      labPub.setTitle(XmlUtil.trimAllHtml(title));
      Locale locale = LocaleContextHolder.getLocale();
      String brief = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc");
      if (locale.equals(Locale.US)) {
        String briefEn = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc_en");
        if (StringUtils.isBlank(briefEn)) {
          briefEn = brief;
        }
        labPub.setSource(briefEn);
      } else {
        labPub.setSource(brief);
      }
      labPub.setVersion(pub.getVersionNo());

    }
  }

  @Override
  public List<NsfcLabPub> getMyLabPubs(Long pId) throws ServiceException {

    List<NsfcLabPub> list = new ArrayList<NsfcLabPub>();
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      list = this.nsfcLabPubDao.getMyLabPubs(psnId, pId);
      List<ConstPubType> pubTypeList = this.constPubTypeService.getAll();
      for (NsfcLabPub labPub : list) {
        for (ConstPubType pubType : pubTypeList) {
          if (pubType.getId().intValue() == labPub.getPubType().intValue()) {
            labPub.setPubTypeName(
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
  public void removeLabPubs(LabPubModel form) throws ServiceException {
    JSONArray jsonArray = JSONArray.fromObject(form.getJsonParams());
    Long psnId = SecurityUtils.getCurrentUserId();
    if (CollectionUtils.isNotEmpty(jsonArray)) {
      for (int i = 0; i < jsonArray.size(); i++) {
        JSONObject obj = jsonArray.getJSONObject(i);
        Long pubId = obj.getLong("pubId");
        Long pId = obj.getLong("pId");

        try {

          this.nsfcLabPubDao.deleteLabPubs(psnId, pubId, pId);
        } catch (DaoException e) {
          logger.error("移除实验室成果失败pubId={},psnId={}", new Object[] {pubId, psnId, e});
        }

      }
      try {
        List<Long> lList = this.nsfcLabPubDao.getLabPubOrder(psnId, form.getPId());
        if (CollectionUtils.isNotEmpty(lList)) {
          for (int i = 1; i <= lList.size(); i++) {
            this.nsfcLabPubDao.updateLabPubOrder(i, lList.get(i - 1), psnId);
          }
        }
      } catch (Exception e) {

        logger.error("更新排序失败！", e);
        throw new ServiceException(e);
      }
    }

  }

  @Override
  public void saveLabPubs(LabPubModel form) throws ServiceException {
    if (StringUtils.isNotBlank(form.getJsonParams())) {
      JSONArray jsonArray = JSONArray.fromObject(form.getJsonParams());
      Long psnId = SecurityUtils.getCurrentUserId();
      if (CollectionUtils.isNotEmpty(jsonArray)) {
        for (int i = 0; i < jsonArray.size(); i++) {
          try {
            JSONObject obj = jsonArray.getJSONObject(i);
            Long key = NumberUtils.toLong(ServiceUtil.decodeFromDes3(obj.getString("des3Key")));
            int labPubType = obj.getInt("labPubType");
            int pubRefTotal = obj.getInt("pubRefTotal");
            NsfcLabPub labPub = this.nsfcLabPubDao.get(key);
            labPub.setLabPubType(labPubType);
            labPub.setPubRefTotal(pubRefTotal);
            labPub.setIsTag(null);
            labPub.setPubAttMembers(null);
            labPub.setIsInsAtt(null);
            labPub.setInsOrder(null);
            if (labPubType != 1) {
              int isTag = obj.getInt("isTag");
              String pubAttMembers = obj.getString("pubAttMembers");
              int isInsAtt = obj.getInt("isInsAtt");
              labPub.setIsTag(isTag);
              labPub.setPubAttMembers(pubAttMembers);
              if (isInsAtt == 1) {
                int insOrder = obj.getInt("insOrder");
                labPub.setIsInsAtt(isInsAtt);
                labPub.setInsOrder(insOrder);
              } else {
                labPub.setIsInsAtt(0);
                labPub.setInsOrder(null);

              }

            }
            this.nsfcLabPubDao.save(labPub);

          } catch (Exception e) {
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
  public void saveLabPubTag(Integer isTag, Long key) throws ServiceException {
    try {

      NsfcLabPub labPub = this.nsfcLabPubDao.get(key);
      if (isTag == null || isTag == 0) {
        labPub.setIsTag(0);
      } else if (isTag > 0) {
        labPub.setIsTag(1);
      }
      this.nsfcLabPubDao.save(labPub);
    } catch (Exception e) {
      logger.error("保存key={}的tag={}失败", new Object[] {key, isTag, e});
    }

  }

  @Override
  public void saveLabPubCitation(String citation, Long key) throws ServiceException {
    try {
      NsfcLabPub pubTemp = this.nsfcLabPubDao.get(key);
      pubTemp.setListInfo(citation);
      this.nsfcLabPubDao.save(pubTemp);

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
  public void savePublicationSort(LabPubModel form) throws ServiceException {
    JSONArray jArray = JSONArray.fromObject(form.getJsonParams());
    if (CollectionUtils.isNotEmpty(jArray)) {
      try {
        List saveList = new ArrayList();
        for (int i = 0; i < jArray.size(); i++) {
          JSONObject obj = jArray.getJSONObject(i);
          Integer seqNo = obj.getInt("seqNo");
          Long pubId = obj.getLong("pubId");

          NsfcLabPub labPub = this.nsfcLabPubDao.loadLabPub(SecurityUtils.getCurrentUserId(), pubId, form.getPId());
          labPub.setSeqNo(seqNo);
          saveList.add(labPub);
        }

        this.nsfcLabPubDao.saveLabPubs(saveList);
      } catch (Exception e) {
        logger.error("调整排序功能失败！", e);
      }
    }
  }

  @Override
  public Set<Long> getLabPubIds(Long pId) throws ServiceException {
    try {
      Long psnId = SecurityUtils.getCurrentUserId();

      return this.nsfcLabPubDao.getLabPubIds(psnId, pId);
    } catch (Exception e) {

      logger.error("查询psnId={}的专家成果id出现异常", new Object[] {SecurityUtils.getCurrentUserId(), e});

    }

    return null;
  }

  @Override
  public void syncPublicationToLabPub(PublicationForm loadXml) throws ServiceException {

    try {
      Publication pub = getPublicationService().getPublicationById(loadXml.getPubId());
      Long psnId = SecurityUtils.getCurrentUserId();
      NsfcLabPub labPub = this.nsfcLabPubDao.loadLabPub(psnId, loadXml.getPubId(), loadXml.getPId());
      if (labPub != null) {

        labPub.setNodeId(SecurityUtils.getCurrentUserNodeId());
        PublicationForm pubForm = new PublicationForm();
        pubForm.setPubId(loadXml.getPubId());
        pubForm = getScholarPublicationXmlManager().loadXml(pubForm);
        this.wrapRptPublication(pub, labPub, pubForm.getPubXml(), true);
        labPub.setRefId(loadXml.getPId());
        this.nsfcLabPubDao.save(labPub);

      }

    } catch (Exception e) {

      logger.error("同步专家psnId={},pubId={}的成果数据失败！",
          new Object[] {SecurityUtils.getCurrentUserId(), loadXml.getPubId(), e});
      throw new ServiceException(e);

    }
  }

  @Override
  public List<NsfcExpertPub> loadLabPubsByGuid(SyncProposalModel model) throws ServiceException {
    return null;
  }

  @Override
  public String covertPubTypeTree(List<Map<String, String>> list, boolean needTop) throws ServiceException {

    List<Map> lmap = new ArrayList<Map>();
    Locale locale = LocaleContextHolder.getLocale();
    if (needTop) {
      // 构造选择学科
      Map<String, Object> selnode = new HashMap<String, Object>();
      Map<String, Object> selnodeAttributes = new HashMap<String, Object>();
      Map<String, Object> selnodeData = new HashMap<String, Object>();
      // 顶层属性attributes
      selnodeAttributes.put("id", "");
      // data属性
      if (locale.equals(Locale.US)) {
        selnodeData.put("title", "Select Type");
      } else {
        selnodeData.put("title", "选择类型");
      }
      selnodeData.put("attributes", selnodeAttributes);
      selnode.put("attributes", selnodeAttributes);
      selnode.put("data", selnodeData);
      lmap.add(selnode);
    }

    for (Map<String, String> tempMap : list) {
      Map<String, Object> node = new HashMap<String, Object>();
      Map<String, Object> nodeAttributes = new HashMap<String, Object>();
      Map<String, Object> nodeData = new HashMap<String, Object>();
      // 顶层属性attributes
      nodeAttributes.put("id", tempMap.get("id"));
      // data属性
      nodeData.put("title", tempMap.get("title"));

      nodeData.put("attributes", nodeAttributes);
      node.put("attributes", nodeAttributes);
      node.put("data", nodeData);

      lmap.add(node);
    }
    return JSONArray.fromObject(lmap).toString();

  }

  @Override
  public void syncSaveNsfcLabGroup(NsfcLabGroup labGroup) throws ServiceException {
    try {
      this.nsfcLabGroupDao.save(labGroup);
    } catch (Exception e) {

      logger.error("保存labId={} groupId={}的关系失败！", new Object[] {labGroup.getLabId(), labGroup.getGroupId(), e});
      throw new ServiceException(e);

    }

  }

  @Override
  public List<NsfcLabPub> loadLabPubsByLabId(Long labId, Integer year) throws ServiceException {
    try {
      NsfcLabGroup labGroup = this.nsfcLabGroupDao.find(labId, year);
      Map<Integer, String> map = new HashMap<Integer, String>();

      List<NsfcLabPub> labPubs = this.nsfcLabPubDao.findBy("refId", labGroup.getId());

      if (labPubs != null) {
        List<ConstPubType> ret = getConstPubTypeService().getAll();
        if (ret != null) {
          for (ConstPubType pubType : ret) {
            map.put(pubType.getId(), pubType.getName());

          }

        }

        for (NsfcLabPub labPub : labPubs) {

          labPub.setPubTypeDes(map.get(labPub.getPubType()));
          this.getData(LocaleContextHolder.getLocale(),
              new PubXmlDocument(this.getById(labPub.getPubId()).getXmlData()), labPub);

        }

      }

      return labPubs;

    } catch (Exception e) {

      logger.error("读取labId={}的成果列表失败！", new Object[] {labId, e});
      throw new ServiceException(e);

    }

  }

  private void getData(Locale locale, PubXmlDocument xmlDocument, NsfcLabPub labPub) throws Exception {

    Map<String, String> datas = BriefUtilis.getFieldsData(FIELDS, xmlDocument);
    String year = datas.get(FIELDS.get(3));
    String month = datas.get(FIELDS.get(4));
    String day = datas.get(FIELDS.get(5));
    String awardDate = BriefUtilis.getStandardDateString(year, month, day);
    labPub.setAwardDate(awardDate);// 成果取得时间

    switch (labPub.getPubType()) {

      case PublicationTypeEnum.AWARD: // 奖励
        labPub.setPubDes1(datas.get(FIELDS.get(0)));
        labPub.setPubDes2("");
        break;
      case PublicationTypeEnum.BOOK: // 书
      case PublicationTypeEnum.BOOK_CHAPTER: // 书籍章节
        labPub.setPubDes1(datas.get(FIELDS.get(1)));
        labPub.setPubDes2("");
        break;
      case PublicationTypeEnum.CONFERENCE_PAPER: // 会议论文.
        labPub.setPubDes1("");
        labPub.setPubDes2("");
        break;
      case PublicationTypeEnum.JOURNAL_ARTICLE: // 期刊文章.
        labPub.setPubDes1(datas.get(FIELDS.get(2)));
        String volume = datas.get(FIELDS.get(6));
        String issue = datas.get(FIELDS.get(7));
        String page = datas.get(FIELDS.get(8)) + "-" + datas.get(FIELDS.get(9));
        labPub.setPubDes2(year + "/" + volume + "(" + issue + ")/" + page);
        break;
      case PublicationTypeEnum.PATENT: // 专利
        labPub.setPubDes1(datas.get(FIELDS.get(10)));
        labPub.setPubDes2(datas.get(FIELDS.get(11)));
        break;

      case PublicationTypeEnum.THESIS: // 学位论文.
        labPub.setPubDes1(datas.get(FIELDS.get(12)));
        labPub.setPubDes2("");

        break;

      case PublicationTypeEnum.OTHERS: // 其他
        labPub.setPubDes1("");
        labPub.setPubDes2("");
    }

  }

  @Override
  public List<NsfcLabGroup> findByLabPsnId(Long labPsnId) throws ServiceException {

    try {
      return this.nsfcLabGroupDao.findByLabPsnId(labPsnId);
    } catch (Exception e) {

      logger.error("查询labPsnId={}的nsfc_lab_group 记录出现异常!", new Object[] {labPsnId, e});
      throw new ServiceException(e);
    }

  }

  @Override
  public void updateLabStatus(StiasSyncGroup model) throws ServiceException {

    try {
      NsfcLabGroup nsfcLabGroup = null;
      nsfcLabGroup = this.findLab(NumberUtils.toLong(model.getLabid()), NumberUtils.toInt(model.getYear()));
      if (nsfcLabGroup == null) {
        nsfcLabGroup = this.findLab(NumberUtils.toLong(model.getLabid()), 0);

      }

      if (nsfcLabGroup != null && nsfcLabGroup.getLabId().toString().equals(model.getLabid())) {
        Assert.notNull(model.getStatus(), "修改实验室申报书的status不允许为空！");

        nsfcLabGroup.setStatus(NumberUtils.toInt(model.getStatus()));
        if (StringUtils.isNotEmpty(model.getPrpname())) {
          nsfcLabGroup.setLabPrpName(model.getPrpname());

        }
        if (StringUtils.isNotBlank(model.getYear())) {

          nsfcLabGroup.setYear(NumberUtils.toInt(model.getYear()));
        }

        this.nsfcLabGroupDao.save(nsfcLabGroup);

      } else {

        throw new ServiceException();
      }
    } catch (Exception e) {

      logger.error("修改实验室申请书labId={}的状态失败！", new Object[] {model.getLabid(), e});
      throw new ServiceException(e);
    }
  }

  @Override
  public Long getRepPubsTotal(Long labPsnId, Long pId) throws ServiceException {

    try {
      return this.nsfcLabPubDao.getRepPubTotal(labPsnId, pId);
    } catch (Exception e) {

      logger.error("查询labPsnId={}的代表性成果失败！", new Object[] {labPsnId, e});
      throw new ServiceException(e);
    }

  }

  @Override
  public NsfcLabGroup findLabById(Long pId) throws ServiceException {
    Long psnId = SecurityUtils.getCurrentUserId();
    NsfcLabGroup labGroup = null;

    try {
      labGroup = this.nsfcLabGroupDao.find(pId, psnId);
    } catch (Exception e) {

      logger.error("读取pId={},labPsnId={}的实验室记录失败！", pId, psnId);
      throw new ServiceException(e);

    }

    return labGroup;

  }

  @Override
  public NsfcLabGroup findLab(Long labId, Integer year) throws ServiceException {

    try {
      return this.nsfcLabGroupDao.find(labId, year);
    } catch (Exception e) {

      logger.error("查询labId={},year={}的NsfcLabGroup对象失败！", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public List<NsfcLabGroup> findByLabId(Long labId) throws ServiceException {
    List<NsfcLabGroup> labGroupList = null;
    try {
      labGroupList = this.nsfcLabGroupDao.findByLabId(labId);
    } catch (Exception e) {
      logger.error("findByLabId", e);
      throw new ServiceException("findByLabId", e);
    }

    return labGroupList;

  }

  @Override
  public List<NsfcLabGroup> getNsfcLabGroupList(Long psnId) {
    return nsfcLabGroupDao.getNsfcLabGroupList(psnId);
  }

  @Override
  public void delNsfcLabGroup(NsfcLabGroup labGroup) {
    nsfcLabGroupDao.delete(labGroup);
  }

  @Override
  public void saveNsfcLabGroup(NsfcLabGroup labGroup) {
    nsfcLabGroupDao.save(labGroup);
  }
}
