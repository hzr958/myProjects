package com.smate.center.batch.service.pub;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.center.batch.dao.sns.pub.ReschPrjRptDao;
import com.smate.center.batch.dao.sns.pub.ReschPrjRptPubDao;
import com.smate.center.batch.dao.sns.pub.ReschProjectDao;
import com.smate.center.batch.enums.pub.XmlOperationEnum;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.CiteWriteDataBuilder;
import com.smate.center.batch.model.sns.pub.ConstPubType;
import com.smate.center.batch.model.sns.pub.NsfcPrjRptPub;
import com.smate.center.batch.model.sns.pub.NsfcReschPrjRptPub;
import com.smate.center.batch.model.sns.pub.NsfcReschPrjRptPubId;
import com.smate.center.batch.model.sns.pub.NsfcReschPrjRptPubSync;
import com.smate.center.batch.model.sns.pub.NsfcReschProject;
import com.smate.center.batch.model.sns.pub.NsfcReschProjectReport;
import com.smate.center.batch.model.sns.pub.ProjectReportPubModel;
import com.smate.center.batch.model.sns.pub.PubFulltextExtend;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.center.batch.model.sns.pub.PublicationCw;
import com.smate.center.batch.model.sns.pub.PublicationForm;
import com.smate.center.batch.model.sns.pub.PublicationList;
import com.smate.center.batch.model.sns.pub.ReschProjectReportModel;
import com.smate.center.batch.model.sns.pub.ReschProjectReportPubModel;
import com.smate.center.batch.model.sns.pub.SyncNsfcReschProjectReportTemp;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.psn.PersonManager;
import com.smate.center.batch.util.pub.ConstDefTypeName;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.IrisNumberUtils;
import com.smate.core.base.utils.string.ServiceUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 成果研究报告模块.
 * 
 * @author oyh
 * 
 */
@Transactional(rollbackFor = Exception.class)
@Service("pubResearchRptService")
public class PubResearchRptServiceImpl implements PubResearchRptService, Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4943074317619766520L;
  private static Logger logger = LoggerFactory.getLogger(PubResearchRptServiceImpl.class);
  @Autowired
  private ReschProjectDao reschProjectDao;
  @Autowired
  private ReschPrjRptPubDao reschPrjRptPubDao;
  @Autowired
  private ReschPrjRptDao reschPrjRptDao;
  @Autowired
  private PublicationService publicationService;
  @Autowired
  private ConstPubTypeService constPubTypeService;
  @Autowired
  private PublicationXmlService publicationXmlService;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private CiteWriteDataBuilder citeWriteDataBuilder;
  @Autowired
  private MyPublicationQueryService myPublicationQueryService;
  @Autowired
  private ScholarPublicationXmlManager scholarPublicationXmlManager;
  @Autowired
  private PublicationListService publicationListService;
  // @Resource(name = "snsSrvServiceLocator")
  // private SnsSrvServiceLocator snsSrvServiceLocator;
  @Value("${domainscm}")
  private String domainscm;

  private PersonManager getPersonManager() throws ServiceException {

    return this.personManager;
  }

  private CiteWriteDataBuilder getCiteWriteDataBuilderService() throws ServiceException {

    return this.citeWriteDataBuilder;

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
    try {

      return this.publicationService;
    } catch (Exception e) {

      e.printStackTrace();
      return null;
    }

  }

  private ConstPubTypeService getConstPubTypeService() throws ServiceException {

    return this.constPubTypeService;
  }

  @Override
  public List<NsfcReschProjectReport> getProjectReportByPrjId(Long prjId) throws ServiceException {
    List<NsfcReschProjectReport> prjRptList = null;
    try {
      prjRptList = this.reschPrjRptPubDao.getProjectReportPubsByPrjId(prjId);
    } catch (DaoException e) {
      logger.error(e.getMessage() + e);
    }
    return prjRptList;
  }

  @Override
  public Page getProjectReports() throws ServiceException {
    return null;
  }

  @Override
  public List<NsfcReschProjectReport> getProjectReportsByPsnId(Long psnId) throws ServiceException {
    if (psnId == null || psnId.longValue() == 0) {
      psnId = SecurityUtils.getCurrentUserId();
    }
    try {
      return this.reschPrjRptDao.getProjectReportsByPsnId(psnId);
    } catch (DaoException e) {
      logger.error(e.getMessage() + e);
    }
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @seecom.iris.scm.scmweb.service.report.ProjectReportService# getProjectReportsByPsnId()
   */
  @SuppressWarnings("unchecked")
  @Override
  public Page getProjectReportsByPsnId() throws ServiceException {
    Long psnId = SecurityUtils.getCurrentUserId();
    List<NsfcReschProjectReport> list = new ArrayList<NsfcReschProjectReport>();
    try {
      list = this.reschPrjRptDao.getProjectReportsByPsnId(psnId);
    } catch (DaoException e) {
      logger.error(e.getMessage() + e);
    }
    Person person = getPersonManager().getPerson(psnId);
    List<ReschProjectReportModel> retList = new ArrayList<ReschProjectReportModel>();
    for (NsfcReschProjectReport obj : list) {
      ReschProjectReportModel vo = new ReschProjectReportModel();
      NsfcReschProject project = obj.getNsfcProject();
      // Institution ins = this.institutionDao.get(project.getInsId());
      vo.setRptId(obj.getRptId());
      vo.setCtitle(project.getCtitle());
      vo.setDeliverDate(obj.getDeliverDate());
      vo.setDisNo1(project.getDisNo1());
      vo.setInsId(project.getInsId());
      vo.setNsfcPrjId(obj.getNsfcRptId());
      vo.setPiPsnId(project.getPiPsnId());
      vo.setPno(project.getPno());
      vo.setPrjId(project.getPrjId());
      vo.setRptType(obj.getRptType());
      vo.setRptYear(obj.getRptYear());
      vo.setStatus(obj.getStatus());
      vo.setStatYear(project.getStatYear());
      vo.setInsCname(project.getInsName());
      vo.setCname(StringUtils.isNotBlank(person.getName()) ? person.getName() : person.getEname());
      retList.add(vo);
    }
    Page<ReschProjectReportModel> page = new Page<ReschProjectReportModel>();
    page.setTotalCount(retList.size());
    page.setResult(retList);
    return page;
  }

  /*
   * (non-Javadoc)
   * 
   * @seecom.iris.scm.scmweb.service.report.ProjectReportService#
   * getProjectReportSubmits(java.lang.Long, int, int)
   */
  @SuppressWarnings("unchecked")
  @Override
  public Page getProjectReportSubmits(Long rptId, int objectsPerPage, int pageNumber) throws ServiceException {
    List<Object> retList = new ArrayList<Object>();
    Long count = 0L;
    try {
      count = this.reschPrjRptPubDao.getCountProjectReportPubsByRptId(rptId);
      // if (PaginatedListHelper.getPageCount(count, objectsPerPage) <
      // pageNumber) {
      // if (pageNumber > 1) {
      // pageNumber--;
      // }
      // }
      List list = this.reschPrjRptPubDao.getProjectReportPubsByRptId(rptId, objectsPerPage, pageNumber);
      for (Iterator iterator = list.iterator(); iterator.hasNext();) {
        NsfcPrjRptPub prjPub = (NsfcPrjRptPub) iterator.next();
        ProjectReportPubModel vo = new ProjectReportPubModel();
        vo.setAuthors(prjPub.getAuthors());
        vo.setIsOpen(prjPub.getIsOpen());
        vo.setIsTag(prjPub.getIsTag());
        vo.setPubYear(prjPub.getPubYear());
        vo.setTitle(prjPub.getTitle());
        vo.setSource(prjPub.getSource());
        vo.setVersion(prjPub.getVersion());
        ConstPubType pubType;
        if (prjPub.getPubType() != null) {
          pubType = this.getConstPubTypeService().get(prjPub.getPubType().intValue());
          if (pubType != null) {
            if ("zh".equals(LocaleContextHolder.getLocale().getLanguage())) {
              // vo.setPubTypeName(ObjectUtils.toString(pubType.getCname()));
            }
            if ("en".equals(LocaleContextHolder.getLocale().getLanguage())) {
              // vo.setPubTypeName(ObjectUtils.toString(pubType.getEname()));
            }
          }
        }
        vo.setPubId(prjPub.getId().getPubId());
        vo.setRptId(prjPub.getId().getRptId());
        retList.add(vo);
      }

    } catch (DaoException e) {
      logger.error(e.getMessage() + e);
    }
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @seecom.iris.scm.scmweb.service.report.ProjectReportService#
   * getProjectReportPubIds(java.lang.Long)
   */
  @Override
  public String getProjectReportPubIds(Long rptId) throws ServiceException {
    String pubId = "";
    try {
      List<NsfcReschPrjRptPub> list = this.reschPrjRptPubDao.getProjectReportPubsByRptId(rptId);
      for (NsfcReschPrjRptPub prjPub : list) {
        if ("".equals(pubId)) {
          pubId = ObjectUtils.toString(prjPub.getId().getPubId());
        } else {
          pubId += "," + prjPub.getId().getPubId();
        }
      }
    } catch (DaoException e) {
      logger.error(e.getMessage() + e);
    }
    return pubId;
  }

  /*
   * (non-Javadoc)
   * 
   * @seecom.iris.scm.scmweb.service.report.ProjectReportService#
   * saveAddProjectReportPub(java.util.List, java.lang.Long, java.lang.Boolean)
   */
  @Override
  @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
  public void saveAddProjectReportPub(List<Map> list, Long rptId, Boolean isAdd) throws ServiceException {
    if (isAdd) {
      // this.addProjecteReportPub(list, rptId);
    } else {
      this.updateProjecteReportPub(list, rptId);
    }
  }

  /**
   * @param list
   * @param rptId
   * @throws ServiceException
   */
  @SuppressWarnings("unchecked")
  @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
  private void updateProjecteReportPub(List<Map> list, Long rptId) throws ServiceException {
    try {
      if (list != null && !list.isEmpty()) {
        boolean flag = false;
        Map map = list.get(0);
        NsfcReschPrjRptPubId pubId = new NsfcReschPrjRptPubId();
        pubId.setPubId(NumberUtils.toLong(ObjectUtils.toString(map.get("pub_id"))));
        pubId.setRptId(rptId);
        NsfcReschPrjRptPub pub = this.reschPrjRptPubDao.getProjectReportPub(pubId);
        pub.setId(pubId);
        pub.setPubYear(
            map.get("publish_year") == null || "".equals(ObjectUtils.toString(map.get("publish_year"))) ? null
                : NumberUtils.toInt(ObjectUtils.toString(map.get("publish_year"))));
        pub.setAuthors(
            map.get("author_names") == null || "".equals(ObjectUtils.toString(map.get("author_names"))) ? null
                : ObjectUtils.toString(map.get("author_names")));
        String title = map.get("ctitle") == null || "".equals(ObjectUtils.toString(map.get("ctitle"))) ? null
            : ObjectUtils.toString(map.get("ctitle"));
        pub.setTitle(XmlUtil.trimAllHtml(title));
        pub.setPubType(map.get("type_id") == null || "".equals(ObjectUtils.toString(map.get("type_id"))) ? null
            : NumberUtils.toInt(ObjectUtils.toString(map.get("type_id"))));
        pub.setSource(map.get("source") == null || "".equals(ObjectUtils.toString(map.get("source"))) ? null
            : ObjectUtils.toString(map.get("source")));
        Integer updateVersion =
            map.get("version_no") == null || "".equals(ObjectUtils.toString(map.get("version_no"))) ? null
                : NumberUtils.toInt(ObjectUtils.toString(map.get("version_no")));
        if (pub.getVersion() != null && pub.getVersion().longValue() == updateVersion) {
          flag = true;
        }
        pub.setVersion(updateVersion);
        pub.setIsTag(pub.getIsTag());
        pub.setIsOpen(pub.getIsOpen());
        if (StringUtils.isNotEmpty(ObjectUtils.toString(map.get("list_info")))) {
          String listInfo = ObjectUtils.toString(map.get("list_info"));
          String[] listInfos = StringUtils.split(listInfo, ",");
          listInfo = "";
          for (int i = 0; i < listInfos.length; i++) {
            if (StringUtils.isEmpty(listInfo)) {
              listInfo = listInfos[i];
            } else {
              listInfo += "," + listInfos[i];
            }
          }
          pub.setListInfo(listInfo);
        }
        pub.setNeedSyc(!flag ? "1" : pub.getNeedSyc());

        this.reschPrjRptPubDao.saveProjectReportPub(pub);
      }
    } catch (DaoException e) {
      logger.error(e.getMessage() + e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @seecom.iris.scm.scmweb.service.report.ProjectReportService#
   * removeProjectReportPub(java.lang.Long, java.lang.Long)
   */
  @Override
  @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
  public void removeProjectReportPub(Long rptId, Long pubId) throws ServiceException {
    try {
      this.reschPrjRptPubDao.removeProjectReportPublication(rptId, pubId);
    } catch (DaoException e) {
      logger.error(e.getMessage() + e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @seecom.iris.scm.scmweb.service.report.ProjectReportService#
   * saveProjectReportPubList(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
   * java.lang.Long)
   */
  @Override
  @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
  public void saveProjectReportPubList(String selectedIsOpens, String selectedIsTags, String cancelIsOpens,
      String cancelIsTags, Long rptId) {
    // if
    // (!com.iris.scm.scmweb.utils.function.StringUtils.IsNullOrEmpty(selectedIsOpens)
    // ||
    // !com.iris.scm.scmweb.utils.function.StringUtils.IsNullOrEmpty(selectedIsTags))
    // {
    // this.saveProjectReportPubList(selectedIsOpens, selectedIsTags, rptId,
    // 1);
    // }
    //
    // if
    // (!com.iris.scm.scmweb.utils.function.StringUtils.IsNullOrEmpty(cancelIsOpens)
    // ||
    // !com.iris.scm.scmweb.utils.function.StringUtils.IsNullOrEmpty(cancelIsTags))
    // {
    // this.saveProjectReportPubList(cancelIsOpens, cancelIsTags, rptId, 0);
    // }
  }

  /**
   * 
   * @param isOpens
   * @param isTags
   * @param rptId
   * @param isValue 1：表示是；0：表示否
   */
  @SuppressWarnings("unchecked")
  @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
  private void saveProjectReportPubList(String isOpens, String isTags, Long rptId, Integer isValue) {
    List saveList = new ArrayList();
    String[] opens = StringUtils.split(isOpens, ",");
    String[] tags = StringUtils.split(isTags, ",");
    Map<String, String> map = new HashMap<String, String>();
    try {
      for (int i = 0; i < tags.length; i++) {
        map.put(tags[i], tags[i]);
      }
      for (int i = 0; i < opens.length; i++) {
        NsfcReschPrjRptPubId pubId = new NsfcReschPrjRptPubId();
        pubId.setRptId(rptId);
        pubId.setPubId(NumberUtils.toLong(opens[i]));
        NsfcReschPrjRptPub pubTemp = this.reschPrjRptPubDao.getProjectReportPub(pubId);
        pubTemp.setIsOpen(isValue);
        if (map != null && map.containsKey(opens[i])) {
          map.remove(opens[i]);
          pubTemp.setIsTag(isValue);
        }
        pubTemp.setId(pubId);
        saveList.add(pubTemp);
      }
      for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
        String key = (String) iterator.next();
        NsfcReschPrjRptPubId pubId = new NsfcReschPrjRptPubId();
        pubId.setRptId(rptId);
        pubId.setPubId(NumberUtils.toLong(map.get(key)));
        NsfcReschPrjRptPub pubTemp = this.reschPrjRptPubDao.getProjectReportPub(pubId);
        pubTemp.setIsTag(isValue);
        pubTemp.setId(pubId);
        saveList.add(pubTemp);
      }
      this.reschPrjRptPubDao.saveProjectReportPub(saveList);
    } catch (DaoException e) {
      logger.error(e.getMessage() + e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @seecom.iris.scm.scmweb.service.report.ProjectReportService#
   * getProjectReportByNsfcPrjId(java.lang.Long)
   */
  @Override
  public NsfcReschProject getProjectByNsfcPrjId(Long nsfcPrjId) throws ServiceException {

    try {

      NsfcReschProject prjRpt = this.reschProjectDao.getProjectByNsfcPrjId(nsfcPrjId);
      Long psnId = SecurityUtils.getCurrentUserId();
      if (prjRpt == null) {
        return null;
      }
      if (prjRpt != null && prjRpt.getPiPsnId().longValue() != psnId.longValue()) {
        return null;
      }
      return prjRpt;
    } catch (DaoException e) {
      logger.error(e.getMessage() + e);
    }
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @seecom.iris.scm.scmweb.service.report.ProjectReportService#
   * removeProjectReportPub(java.lang.Long, java.lang.String)
   */
  @Override
  @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
  public void removeProjectReportPub(Long rptId, String selectedDelValues) throws ServiceException {
    String[] delPubIds = StringUtils.split(selectedDelValues, ",");
    if (delPubIds.length > 0) {
      Long[] pubIds = new Long[delPubIds.length];
      for (int i = 0; i < delPubIds.length; i++) {
        pubIds[i] = NumberUtils.toLong(delPubIds[i]);
      }
      try {
        this.reschPrjRptPubDao.removeProjectReportPublication(rptId, pubIds);
      } catch (DaoException e) {
        logger.error(e.getMessage() + e);
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @seecom.iris.scm.scmweb.service.report.ProjectReportService#
   * getProjectReportSubmitsAll(java.lang.Long, int, java.lang.Integer)
   */
  @Override
  public List<NsfcReschPrjRptPub> getProjectReportSubmitsAll(Long rptId) throws ServiceException {
    List<NsfcReschPrjRptPub> list = new ArrayList<NsfcReschPrjRptPub>();
    try {
      list = this.reschPrjRptPubDao.getProjectReportPubsByRptId(rptId, 0, 0);
      for (NsfcReschPrjRptPub rptPub : list) {
        ConstPubType pubType = this.getConstPubTypeService().get(rptPub.getPubType());
        rptPub.setPubTypeName(StringUtils.isNotBlank(pubType.getZhName()) ? pubType.getZhName() : pubType.getEnName());
        rptPub.setDefTypeName(ConstDefTypeName.defTypeNameMap.get(rptPub.getDefType()));
      }
    } catch (DaoException e) {
      logger.error(e.getMessage() + e);
    }
    return list;
  }

  /*
   * (non-Javadoc)
   * 
   * @seecom.iris.scm.scmweb.service.report.ProjectReportService#
   * saveProjectReportCitation(java.lang.String, java.lang.Long, java.lang.Long)
   */
  @Override
  @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
  public void saveProjectReportCitation(String citation, Long pubId, Long rptId) {
    try {
      NsfcReschPrjRptPubId id = new NsfcReschPrjRptPubId();
      id.setRptId(rptId);
      id.setPubId(pubId);
      NsfcReschPrjRptPub pubTemp = this.reschPrjRptPubDao.getProjectReportPub(id);
      pubTemp.setId(id);
      pubTemp.setListInfo(citation);
      this.reschPrjRptPubDao.saveProjectReportPub(pubTemp);

      Publication pub = null;
      pub = getPublicationService().getPublicationById(pubId);
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
          this.getPublicationService().updatePublication(xml, context);
        }
      }
    } catch (Exception e) {
      logger.error(e.getMessage() + e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.scmweb.service.report.ProjectReportService#isProjectOwner
   * (com.iris.scm.scmweb.web.struts2.report .model.ProjectReportModel)
   */
  @Override
  public Boolean isProjectOwner(Long proPsnId) {
    try {
      if (proPsnId.longValue() != SecurityUtils.getCurrentUserId().longValue()) {
        return false;
      }
    } catch (Exception e) {
      logger.error(e.getMessage() + e);
      return false;
    }
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.scmweb.service.report.ProjectReportService#savePublicationSort (java.lang.Long,
   * java.lang.Long, java.lang.String)
   */
  @SuppressWarnings("unchecked")
  @Override
  @Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
  public void savePublicationSort(ReschProjectReportPubModel form) {
    JSONArray jArray = JSONArray.fromObject(form.getJsonParams());
    if (CollectionUtils.isNotEmpty(jArray)) {
      try {
        List saveList = new ArrayList();
        for (int i = 0; i < jArray.size(); i++) {
          JSONObject obj = jArray.getJSONObject(i);
          Integer seqNo = obj.getInt("seqNo");
          Long pubId = obj.getLong("pubId");
          NsfcReschPrjRptPubId rptPubId = new NsfcReschPrjRptPubId();
          rptPubId.setPubId(pubId);
          rptPubId.setRptId(form.getRptId());
          NsfcReschPrjRptPub pub = this.reschPrjRptPubDao.getProjectReportPub(rptPubId);
          pub.setSeqNo(seqNo);
          saveList.add(pub);
        }
        this.reschPrjRptPubDao.saveProjectReportPub(saveList);
      } catch (DaoException e) {
        logger.error(e.getMessage() + e);
      }
    }
  }

  @Override
  public boolean updateDefType(ReschProjectReportPubModel form) throws ServiceException {
    try {
      if (!this.validateAction(form)) {
        return false;
      }
      // 要更改的ID放在pubIds里面，方便上一步的验证
      Long pubId = NumberUtils.createLong(form.getPubIds());
      NsfcReschPrjRptPubId rptPubId = new NsfcReschPrjRptPubId(form.getRptId(), pubId);
      NsfcReschPrjRptPub pub = this.reschPrjRptPubDao.getById(rptPubId);
      if (pub == null) {
        return false;
      }
      Integer orgDefType = pub.getDefType();
      Long seqNo = this.reschPrjRptPubDao.getNsfcPrjRptPubMaxSeqNo(form.getRptId(), form.getDefType());
      pub.setSeqNo(seqNo.intValue() + 1);
      pub.setDefType(form.getDefType());
      this.reschPrjRptPubDao.saveProjectReportPub(pub);

      // 把原来类型的重新排序
      List<NsfcReschPrjRptPub> result = this.reschPrjRptPubDao.findPubsByType(form.getRptId(), orgDefType);
      if (CollectionUtils.isNotEmpty(result)) {
        Integer seq = 1;
        for (NsfcReschPrjRptPub rptPub : result) {
          rptPub.setSeqNo(seq);
          seq++;
        }
        this.reschPrjRptPubDao.saveProjectReportPub(result);
      }

    } catch (DaoException e) {
      logger.error("更改成果类型出现问题！", e);
      throw new ServiceException(e);
    }
    return true;
  }

  @Override
  public ReschProjectReportModel getProjectReportByPrjIdAndYear(Long prjId, String year) throws ServiceException {
    if (StringUtils.isBlank(year)) {
      throw new ServiceException("年度不能为空。");
    }
    ReschProjectReportModel model = new ReschProjectReportModel();
    try {
      NsfcReschProjectReport prjRpt =
          this.reschPrjRptPubDao.getProjectReportPubsByPrjIdAndYear(prjId, NumberUtils.toInt(year));

      // com.iris.scm.scmweb.utils.function.BeanUtils.copyPropertiesForObject(model,
      // prjRpt);
    } catch (DaoException e) {
      logger.error(e.getMessage() + e);
    }
    return model;
  }

  @SuppressWarnings("unchecked")
  @Override
  public ReschProjectReportModel getProjectReportLastYear(Long prjId, String year) throws ServiceException {
    List list = this.getProjectReportByPrjId(prjId);
    NsfcReschProjectReport prjRpt = new NsfcReschProjectReport();
    if (CollectionUtils.isNotEmpty(list) && list.size() == 1) {
      prjRpt = (NsfcReschProjectReport) list.get(0);
    } else if (CollectionUtils.isNotEmpty(list) && list.size() > 1) {
      int tmpYear = -1;
      for (Iterator iterator = list.iterator(); iterator.hasNext();) {
        NsfcReschProjectReport object = (NsfcReschProjectReport) iterator.next();
        if (StringUtils.isBlank(year)) {
          if (tmpYear == -1) {
            tmpYear = object.getRptYear();
            prjRpt = object;
          } else {
            if (tmpYear < object.getRptYear().intValue()) {
              prjRpt = object;
            }
          }
        } else if (NumberUtils.toInt(year) == object.getRptYear().intValue()) {
          prjRpt = object;
        }
      }
    }
    ReschProjectReportModel model = new ReschProjectReportModel();
    // try {
    // //
    // com.iris.scm.scmweb.utils.function.BeanUtils.copyPropertiesForObject(model,
    // prjRpt);
    // } catch (IllegalAccessException e) {
    // logger.error(e.getMessage() + e);
    // } catch (InvocationTargetException e) {
    // logger.error(e.getMessage() + e);
    // }
    return model;
  }

  @Override
  public Set<Long> getPubIdsPrjFinalPubByRptId(Long rptId) throws ServiceException {
    try {
      Set<Long> pubIds = this.reschPrjRptPubDao.getProjectReportPubsIdByRptId(rptId);
      return pubIds;
    } catch (DaoException e) {
      logger.error(e.getMessage() + e);
    }
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @seecom.iris.scm.scmweb.service.pubfinalreport.PubFinalReportService#
   * addPublicationFromMyMate(com.iris.scm.scmweb .model.pubfinalreport.ProjectReportPubModel)
   */
  @Override
  public boolean addPublicationFromMyMate(ReschProjectReportPubModel form) throws ServiceException {
    try {
      // 默认添加的全部论著
      if (form.getDefType() == null) {
        form.setDefType(2);
      }
      if (!this.validateAction(form)) {
        return false;
      }
      if (StringUtils.isNotBlank(form.getPubIds())) {
        List<Long> list = new ArrayList<Long>();
        String[] aPubIds = form.getPubIds().split(",");
        for (String pubId : aPubIds) {
          list.add(Long.valueOf(pubId));
        }
        List<Publication> pubList = this.getMyPublicationQueryService().findPubsForNsfc(list);
        for (Publication pub : pubList) {
          NsfcReschPrjRptPubId rptPubId = new NsfcReschPrjRptPubId();
          NsfcReschPrjRptPub rptPub = new NsfcReschPrjRptPub();
          rptPubId.setPubId(pub.getId());
          rptPubId.setRptId(form.getRptId());
          if (this.reschPrjRptPubDao.getById(rptPubId) != null) {
            continue;
          }
          rptPub.setId(rptPubId);
          rptPub.setPubOwnerPsnId(pub.getPsnId());
          this.wrapRptPublication(pub, rptPub);
          if (rptPub.getTitle().length() > 1000) {
            throw new ServiceException("000001");
          }
          rptPub.setDefType(form.getDefType());
          Long seqNo = this.reschPrjRptPubDao.getNsfcPrjRptPubMaxSeqNo(form.getRptId(), form.getDefType());
          rptPub.setSeqNo(seqNo.intValue() + 1);
          rptPub.setNodeId(SecurityUtils.getCurrentUserNodeId());
          this.reschPrjRptPubDao.saveProjectReportPub(rptPub);
        }

      } else if (StringUtils.isNotBlank(form.getJsonParams())) {
        JSONArray array = JSONArray.fromObject(form.getJsonParams());
        if (CollectionUtils.isNotEmpty(array)) {
          for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            Long pubId = obj.getLong("pubId");
            Integer nodeId = obj.getInt("nodeId");
            Long psnId = obj.getLong("psnId");
            Publication pub = getPublicationService().getPublicationById(pubId);
            if (pub != null) {
              NsfcReschPrjRptPubId rptPubId = new NsfcReschPrjRptPubId();
              NsfcReschPrjRptPub rptPub = new NsfcReschPrjRptPub();
              rptPubId.setPubId(pub.getId());
              rptPubId.setRptId(form.getRptId());
              if (this.reschPrjRptPubDao.getById(rptPubId) != null) {
                continue;
              }
              rptPub.setId(rptPubId);
              rptPub.setPubOwnerPsnId(pub.getPsnId());
              this.wrapRptPublication(pub, rptPub);
              if (rptPub.getTitle().length() > 1000) {
                throw new ServiceException("000001");
              }
              rptPub.setDefType(form.getDefType());
              Long seqNo = this.reschPrjRptPubDao.getNsfcPrjRptPubMaxSeqNo(form.getRptId(), form.getDefType());
              rptPub.setSeqNo(seqNo.intValue() + 1);
              rptPub.setNodeId(SecurityUtils.getCurrentUserNodeId());
              this.reschPrjRptPubDao.saveProjectReportPub(rptPub);
            }
          }
        }
      }
    } catch (ServiceException e) {
      logger.error(e.getMessage() + e);
      throw new ServiceException("保存成果至结题报告失败！");
    } catch (DaoException e) {
      logger.error(e.getMessage() + e);
      throw new ServiceException("保存成果至结题报告失败！数据库操作失败");
    }
    return true;
  }

  /**
   * 检查研究报告提交的成果类型是否错误
   * 
   * @param form
   * @return
   * @throws ServiceException
   */
  private boolean validateAction(ReschProjectReportPubModel form) throws ServiceException {
    if (form.getDefType() == 2) {
      return true;
    }
    try {
      Long count = this.reschPrjRptPubDao.countDefPubType(form.getRptId(), 1);
      Integer size = 0;
      if (StringUtils.isNotBlank(form.getPubIds())) {
        size = form.getPubIds().split(",").length;
      }
      if (StringUtils.isNotBlank(form.getJsonParams())) {
        JSONArray array = JSONArray.fromObject(form.getJsonParams());
        size = array.size();
      }
      if (count + size > 5) {
        return false;
      }
    } catch (DaoException e) {
      logger.error("检查研究报告类型时发生错误！", e);
      throw new ServiceException("检查研究报告类型时发生错误！", e);
    }
    return true;
  }

  private void wrapRptPublication(Publication pub, NsfcReschPrjRptPub rptPub) throws ServiceException {
    rptPub.setAuthors(pub.getAuthorNames());
    rptPub.setIsOpen(1);
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

      }
      rptPub.setListInfo(listInfo);

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
    rptPub.setCitedTimes(pub.getCitedTimes());
    rptPub.setImpactFactors(pub.getImpactFactors());

    Locale locale = LocaleContextHolder.getLocale();
    String brief = pub.getBriefDesc();
    if (locale.equals(Locale.US)) {
      String briefEn = pub.getBriefDescEn();
      rptPub.setSource(StringUtils.isBlank(briefEn) ? brief : briefEn);
    } else {
      rptPub.setSource(brief);
    }

  }

  @Override
  public void saveProjectReportOpen(Integer isOpen, Long pubId, Long rptId) throws ServiceException {
    try {
      NsfcReschPrjRptPubId id = new NsfcReschPrjRptPubId();
      id.setRptId(rptId);
      id.setPubId(pubId);
      NsfcReschPrjRptPub pubTemp = this.reschPrjRptPubDao.getProjectReportPub(id);
      if (isOpen == null || isOpen == 0) {
        pubTemp.setIsOpen(0);
      } else if (isOpen > 0) {
        pubTemp.setIsOpen(1);
      }
      pubTemp.setId(id);
      this.reschPrjRptPubDao.saveProjectReportPub(pubTemp);
    } catch (DaoException e) {
      logger.error(e.getMessage() + e);
    }
  }

  @Override
  public void saveProjectReportTag(Integer isTag, Long pubId, Long rptId) throws ServiceException {
    try {
      NsfcReschPrjRptPubId id = new NsfcReschPrjRptPubId();
      id.setRptId(rptId);
      id.setPubId(pubId);
      NsfcReschPrjRptPub pubTemp = this.reschPrjRptPubDao.getProjectReportPub(id);
      if (isTag == null || isTag == 0) {
        pubTemp.setIsTag(0);
      } else if (isTag > 0) {
        pubTemp.setIsTag(1);
      }
      pubTemp.setId(id);
      this.reschPrjRptPubDao.saveProjectReportPub(pubTemp);
    } catch (DaoException e) {
      logger.error(e.getMessage() + e);
    }

  }

  @Override
  public void removeProjectReportPub(ReschProjectReportPubModel form) throws ServiceException {
    try {
      JSONArray jsonArray = JSONArray.fromObject(form.getJsonParams());
      if (CollectionUtils.isNotEmpty(jsonArray)) {
        for (int i = 0; i < jsonArray.size(); i++) {
          JSONObject obj = jsonArray.getJSONObject(i);
          Long pubId = obj.getLong("pubId");
          Integer nodeId = null;
          if (!obj.containsKey("nodeId") || (obj.containsKey("nodeId") && obj.get("nodeId") == null)) {
            nodeId = SecurityUtils.getCurrentUserNodeId();
          } else {
            nodeId = obj.getInt("nodeId");
          }
          try {
            this.reschProjectDao.deletePubFromRpt(pubId, nodeId, form.getRptId());
          } catch (DaoException e) {
            logger.error(e.getMessage() + e);
          }

        }

        // 代表性论著重新排序
        List<Long> dbList = this.reschPrjRptPubDao.findPubIdsByType(form.getRptId(), 1);
        if (CollectionUtils.isNotEmpty(dbList)) {
          for (int i = 1; i <= dbList.size(); i++) {
            this.reschProjectDao.updateRptPubOrder(i, dbList.get(i - 1), form.getRptId());
          }
        }
        // 全部论著重新排序
        List<Long> qbList = this.reschPrjRptPubDao.findPubIdsByType(form.getRptId(), 2);
        if (CollectionUtils.isNotEmpty(qbList)) {
          for (int i = 1; i <= qbList.size(); i++) {
            this.reschProjectDao.updateRptPubOrder(i, qbList.get(i - 1), form.getRptId());
          }
        }

        // List<Long> lList =
        // this.reschProjectDao.getRptPubOrder(form.getRptId());
        // if (CollectionUtils.isEmpty(lList)) {
        // this.syncReschRptPubsToISIS(form.getRptId(), null);
        // }
      }
    } catch (DaoException e) {
      logger.error("删除成果出现问题！", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public NsfcReschProject getProjectByPrjIdAndPsnId(Long prjId) throws ServiceException {
    try {
      NsfcReschProject prj = this.reschProjectDao.getProjectByPrjId(prjId, SecurityUtils.getCurrentUserId());
      return prj;
    } catch (DaoException e) {
      logger.error(e.getMessage() + e);
    }

    return null;
  }

  /**
   * 这里暂时还没有考虑成果不在同一个节点的问题.<br/>
   * (non-Javadoc)
   * 
   * @see com.iris.scm.scmweb.service.pubfinalreport.PubFinalReportService#saveReportPublication(com.iris.scm.scmweb.model.pubfinalreport.ProjectReportPubModel)
   */
  @Override
  public void saveReportPublication(ReschProjectReportPubModel form) throws ServiceException {
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
                // PublicationList pubList =
                // this.publicationListService.getPublicationList(pubId);
                // if (pubList != null) {
                // pubList.setListEi(listEi);
                // pubList.setListSci(listSci);
                // pubList.setListIstp(listIstp);
                // this.publicationListService.savePublictionList(pubList);
                // } else {
                // PublicationList pubCited = new
                // PublicationList();
                // pubCited.setId(pubId);
                // pubCited.setListEi(listEi);
                // pubCited.setListSci(listSci);
                // pubCited.setListIstp(listIstp);
                // this.publicationListService.savePublictionList(pubCited);
                // }
                PublicationService publicationService = getPublicationService();

                String xml = publicationService.getPubXmlById(pubId);
                xml = this.reBuildCitedXml(xml, listEi, listSci, listIstp);
                PubXmlProcessContext context = new PubXmlProcessContext();
                context.setCurrentPubId(pubId);
                context.setCurrentUserId(psnId);
                publicationService.updatePublication(xml, context);
              }
            }

          } catch (ServiceException e) {
            logger.error(e.getMessage() + e);
            throw new ServiceException("保存结题报告成果失败....");
          }
        }

        // 修改版本号
        // updateReschRptVersionId(form);
        // 同步ws
        // this.syncReschRptPubsToISIS(form.getRptId(), null);
      }
    }

    // 发送JMS消息到nsfcROL
    // List<NsfcPrjRptPub> pubList =
    // this.getProjectReportSubmitsAll(form.getRptId());
    // ProjectReportFinalMessage message = new ProjectReportFinalMessage();
    // message.setRptId(form.getRptId());
    // List<NsfcPrjRptPubSync> syncs = new ArrayList<NsfcPrjRptPubSync>();
    // if (CollectionUtils.isNotEmpty(pubList)) {
    // for (NsfcPrjRptPub pub : pubList) {
    // NsfcPrjRptPubSync sync = new NsfcPrjRptPubSync();
    // PublicationXml xmlData = null;
    // if (pub.getNodeId() == SecurityUtils.getCurrentAllNodeId().get(0)) {
    // xmlData = this.publicationXmlService.getById(pub.getId().getPubId());
    // } else {
    // PublicationXmlService remoteService =
    // this.remotingServiceFactory.getServiceByNodeId(
    // pub.getNodeId(), PublicationXmlService.class);
    // xmlData = remoteService.getById(pub.getId().getPubId());
    // }
    // if (xmlData != null) {
    // wrapNsfcPrjRptSyncBean(sync, pub, xmlData.getXmlData());
    // }
    // syncs.add(sync);
    // }
    // }
    // message.setFromNodeId(SecurityUtils.getCurrentAllNodeId().get(0));
    // message.setNsfcPrjRptPubSyncs(syncs);
    // message.setNodeId(10002);
    // this.projectReportFinalPubSyncProducer.publicationToNsfcRolSync(message);
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

  private void wrapNsfcPrjRptSyncBean(NsfcReschPrjRptPubSync sync, NsfcReschPrjRptPub pub, String xmlData)
      throws ServiceException {
    try {
      sync.setIsOpen(pub.getIsOpen());
      sync.setIsTag(pub.getIsTag());
      sync.setListInfo(pub.getListInfo());
      sync.setNeedSyc(pub.getNeedSyc());
      sync.setNodeId(pub.getNodeId());
      sync.setPubId(pub.getId().getPubId());
      sync.setPubType(pub.getPubType());
      sync.setPubYear(pub.getPubYear());
      sync.setSeqNo(pub.getSeqNo());
      sync.setVersion(pub.getVersion());
      // FIXME 2015-10-29 远程调用取消 -done
      // String visitUrl = snsSrvServiceLocator.getWebUrl(BasicRmtSrvModuleConstants.SNS_MODULE_ID);
      String visitUrl = domainscm;
      visitUrl += "/publication/view?des3Id=" + ServiceUtil.encodeToDes3(pub.getId().getPubId().toString()) + "&nodeId="
          + pub.getNodeId();
      sync.setVisitUrl(visitUrl);
      PubXmlDocument doc = null;
      try {
        doc = new PubXmlDocument(xmlData);
      } catch (DocumentException e) {
        logger.error(e.getMessage() + e);
      }
      if (doc == null) {
        sync = null;
        return;
      }
      sync.setAuthors(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names"));
      String zhTitle = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title_text");
      String enTitle = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title_text");
      String title = XmlUtil.getLanguageSpecificText(LocaleContextHolder.getLocale().getLanguage(), zhTitle, enTitle);
      sync.setTitle(XmlUtil.trimAllHtml(title));

      Locale locale = LocaleContextHolder.getLocale();
      String brief = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc");
      if (locale.equals(Locale.US)) {
        String briefEn = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc_en");
        sync.setSource(StringUtils.isBlank(briefEn) ? brief : briefEn);
      } else {
        sync.setSource(brief);
      }
    } catch (Exception e) {
      logger.error("wrapNsfcPrjRptSyncBean", e);
      throw new ServiceException("wrapNsfcPrjRptSyncBean", e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @seecom.iris.scm.scmweb.service.pubfinalreport.PubFinalReportService#
   * getSimilarProjectReport(java.lang.Long, java.lang.Long)
   */
  @Override
  public NsfcReschProjectReport getSimilarProjectReport(Long nsfcPrjId, Long rptYear) throws ServiceException {
    try {
      NsfcReschProjectReport selectRpt = null;
      if (nsfcPrjId != null && nsfcPrjId > 0L) {

        List<NsfcReschProjectReport> unsubmitedList =
            this.getProjectReportByNsfcPrjId(nsfcPrjId, SecurityUtils.getCurrentUserId());
        if (CollectionUtils.isEmpty(unsubmitedList)) {
          unsubmitedList = this.reschPrjRptDao.getProjectReportByPrjId(nsfcPrjId, SecurityUtils.getCurrentUserId());
        }
        if (CollectionUtils.isNotEmpty(unsubmitedList)) {
          if (rptYear != null) {
            for (NsfcReschProjectReport nsfcRpt : unsubmitedList) {
              if (rptYear != null && (nsfcRpt.getStatus() == null || nsfcRpt.getStatus() == 0)
                  && nsfcRpt.getRptYear() == rptYear.longValue()) {
                selectRpt = nsfcRpt;
                break;
              }
            }

          }
          if (selectRpt == null) {
            for (NsfcReschProjectReport nsfcRpt : unsubmitedList) {
              if (nsfcRpt.getStatus() == null || nsfcRpt.getStatus() == 0) {
                selectRpt = nsfcRpt;
                break;
              }
            }
          }
        }
      } else {
        // 找该人最近的一份结题报告
        List<NsfcReschProjectReport> unsubmitedList = new ArrayList<NsfcReschProjectReport>();
        try {
          unsubmitedList = this.getProjectReportsByPsnId(SecurityUtils.getCurrentUserId());
        } catch (ServiceException e) {
          logger.error(e.getMessage() + e);
        }
        if (CollectionUtils.isNotEmpty(unsubmitedList)) {
          if (rptYear != null) {
            for (NsfcReschProjectReport nsfcRpt : unsubmitedList) {
              if (rptYear != null && (nsfcRpt.getStatus() == null || nsfcRpt.getStatus() == 0)
                  && nsfcRpt.getRptYear() == rptYear.longValue()) {
                selectRpt = nsfcRpt;
                break;
              }
            }
          }
          if (selectRpt == null) {
            for (NsfcReschProjectReport nsfcRpt : unsubmitedList) {
              if (nsfcRpt.getStatus() == null || nsfcRpt.getStatus() == 0) {
                selectRpt = nsfcRpt;
                break;
              }
            }
          }
        }
      }
      return selectRpt;
    } catch (DaoException e) {
      logger.error(e.getMessage(), e);
    }
    return null;
  }

  private List<NsfcReschProjectReport> getProjectReportByNsfcPrjId(Long nsfcPrjId, Long psnId) {
    if (psnId == null || psnId.longValue() == 0) {
      psnId = SecurityUtils.getCurrentUserId();
    }
    try {
      return this.reschPrjRptDao.getProjectReportByNsfcPrjId(nsfcPrjId, psnId);
    } catch (DaoException e) {
      logger.error(e.getMessage() + e);
    }
    return null;
  }

  @Override
  public List<NsfcReschPrjRptPub> getProjectReportSubmitsAllForCw(Long rptId) throws ServiceException {
    List<NsfcReschPrjRptPub> list = new ArrayList<NsfcReschPrjRptPub>();
    try {
      list = this.reschPrjRptPubDao.getProjectReportPubsByRptId(rptId, 0, 0);
      for (NsfcReschPrjRptPub rptPub : list) {
        ConstPubType pubType = this.getConstPubTypeService().get(rptPub.getPubType());
        rptPub.setPubTypeName(StringUtils.isNotBlank(pubType.getZhName()) ? pubType.getZhName() : pubType.getEnName());
        PublicationCw cw = new PublicationCw();
        Publication publication = new Publication();
        publication.setId(rptPub.getId().getPubId());
        publication.setTypeId(rptPub.getPubType());
        publication.setPublishYear(rptPub.getPubYear());
        getMyPublicationQueryService().wrapQueryResultTypeName(publication);
        cw = getCiteWriteDataBuilderService().populateDataForCw(publication, false);
        rptPub.setPubCw(cw);
      }
    } catch (DaoException e) {
      logger.error(e.getMessage() + e);
    }
    return list;
  }

  @Override
  public void syncOldNsfcPrjRptPub(Map<String, Object> oldData) throws ServiceException {

    try {

      NsfcReschPrjRptPub obj = new NsfcReschPrjRptPub();
      NsfcReschPrjRptPubId id = new NsfcReschPrjRptPubId();
      Long rptId = Long.valueOf(oldData.get("RPT_ID").toString());
      Long oldPubId = Long.valueOf(oldData.get("PUB_ID").toString());
      Integer version = oldData.get("VERSION") == null ? null : Integer.valueOf(oldData.get("VERSION").toString());
      Integer pubType = oldData.get("PUB_TYPE") == null ? null : Integer.valueOf(oldData.get("PUB_TYPE").toString());
      Integer pubYear = oldData.get("PUB_YEAR") == null ? null : Integer.valueOf(oldData.get("PUB_YEAR").toString());
      String authors = oldData.get("AUTHORS") == null ? null : oldData.get("AUTHORS").toString();
      String title = oldData.get("TITLE") == null ? null : oldData.get("TITLE").toString();
      String source = oldData.get("SOURCE") == null ? null : oldData.get("SOURCE").toString();
      Integer isTag = oldData.get("IS_TAG") == null ? null : Integer.valueOf(oldData.get("IS_TAG").toString());
      Integer isOpen = oldData.get("IS_OPEN") == null ? null : Integer.valueOf(oldData.get("IS_OPEN").toString());
      String needSyc = oldData.get("NEED_SYC") == null ? null : oldData.get("NEED_SYC").toString();
      String listInfo = oldData.get("LIST_INFO") == null ? null : oldData.get("LIST_INFO").toString();
      Integer seqNo = oldData.get("SEQ_NO") == null ? null : Integer.valueOf(oldData.get("SEQ_NO").toString());
      Long piPsnId = oldData.get("PI_PSN_ID") == null ? null : Long.valueOf(oldData.get("PI_PSN_ID").toString());

      // 查找新的成果ID.
      Long pubId = null;
      Long pubowner = null;
      // 标记是否匹配到新的成果ID.
      Integer matched = 1;
      if (piPsnId != null) {
        Publication pub = getPublicationService().getPubByOldPub(oldPubId, piPsnId);
        if (pub != null) {
          pubId = pub.getId();
        }
      }
      if (pubId == null) {
        pubId = oldPubId;
        matched = 0;
      } else {
        pubowner = piPsnId;
      }

      id.setRptId(rptId);
      id.setPubId(pubId);
      obj.setId(id);
      obj.setMatched(matched);
      obj.setVersion(version);
      obj.setPubType(pubType);
      obj.setPubYear(pubYear);
      obj.setAuthors(authors);
      obj.setTitle(title);
      obj.setSource(source);
      obj.setIsTag(isTag);
      obj.setIsOpen(isOpen);
      obj.setNeedSyc(needSyc);
      obj.setListInfo(listInfo);
      obj.setSeqNo(seqNo);
      obj.setNodeId(1);
      obj.setPubOwnerPsnId(pubowner);

      this.reschPrjRptPubDao.saveProjectReportPub(obj);
    } catch (Exception e) {
      logger.error("同步v2.6数据", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void syncOldNsfcProjectReport(Map<String, Object> oldData) throws ServiceException {

    try {
      Long rptId = Long.valueOf(oldData.get("RPT_ID").toString());
      Long prjId = oldData.get("PRJ_ID") == null ? null : Long.valueOf(oldData.get("PRJ_ID").toString());
      Long nsfcRptId = oldData.get("NSFC_RPT_ID") == null ? null : Long.valueOf(oldData.get("NSFC_RPT_ID").toString());
      Integer rptYear = oldData.get("RPT_YEAR") == null ? null : Integer.valueOf(oldData.get("RPT_YEAR").toString());
      Integer rptType = oldData.get("RPT_TYPE") == null ? null : Integer.valueOf(oldData.get("RPT_TYPE").toString());
      Integer status = oldData.get("STATUS") == null ? null : Integer.valueOf(oldData.get("STATUS").toString());
      Date deliverDate = oldData.get("DELIVER_DATE") == null ? null : (Date) oldData.get("DELIVER_DATE");

      SyncNsfcReschProjectReportTemp obj = new SyncNsfcReschProjectReportTemp();
      obj.setRptId(rptId);
      obj.setPrjId(prjId);
      obj.setNsfcRptId(nsfcRptId);
      obj.setRptYear(rptYear);
      obj.setRptType(rptType);
      obj.setStatus(status);
      obj.setDeliverDate(deliverDate);
      this.reschPrjRptDao.saveSyncNsfcProjectReportTemp(obj);
    } catch (Exception e) {
      logger.error("同步v2.6数据", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void syncPublicationToFinalReport(PublicationForm loadXml) throws ServiceException {
    try {
      NsfcReschPrjRptPubId rptPubId = new NsfcReschPrjRptPubId();
      rptPubId.setRptId(loadXml.getRptId());
      rptPubId.setPubId(loadXml.getPubId());
      NsfcReschPrjRptPub rptPub = this.reschPrjRptPubDao.getProjectReportPub(rptPubId);
      if (rptPub == null) {
        return;
      }
      Publication pub = getPublicationService().getPublicationById(loadXml.getPubId());
      this.wrapRptPublication(pub, rptPub);
      if (rptPub.getTitle().length() > 1000) {
        throw new ServiceException("000001");
      }
      rptPub.setNodeId(SecurityUtils.getCurrentUserNodeId());
      this.reschPrjRptPubDao.saveProjectReportPub(rptPub);
      ReschProjectReportPubModel form = new ReschProjectReportPubModel();
      form.setRptId(loadXml.getRptId());
      this.updateReschRptVersionId(form);
    } catch (DaoException e) {
      throw new ServiceException("成果编辑--同步到结题报告出错!!");
    }
  }

  @Override
  public List<NsfcReschPrjRptPub> getProjectFinalPubs(Long nsfcPrjId, Integer rptYear) throws ServiceException {
    try {
      if (nsfcPrjId == null || rptYear == null) {
        throw new ServiceException("项目编号和项目年度不能为空");
      }

      List<NsfcReschProjectReport> prjRpt = this.reschPrjRptDao.getPrjRptByNsfcPrjIdYear(nsfcPrjId, rptYear);
      if (CollectionUtils.isEmpty(prjRpt) || prjRpt.size() > 1) {
        throw new ServiceException("没有找到对应的结题报告nsfcPrjId:" + nsfcPrjId + " rptYear:" + rptYear);
      }
      Long rptId = prjRpt.get(0).getRptId();
      if (isOriginalVersion(rptId)) {

        ReschProjectReportPubModel form = new ReschProjectReportPubModel();
        form.setIgnore(true);
        form.setRptId(rptId);
        this.updateReschRptVersionId(form);
      }

      List<NsfcReschPrjRptPub> pubs = this.getProjectReportSubmitsAll(prjRpt.get(0).getRptId());
      return pubs;
    } catch (DaoException e) {
      logger.error(e.getMessage(), e);
      throw new ServiceException("查询结题报告成果失败");
    }
  }

  private boolean isOriginalVersion(Long rptId) throws ServiceException {

    try {
      NsfcReschProjectReport rpt = this.reschPrjRptDao.getProjectReport(rptId);

      return rpt.getVersionId().longValue() == 0;
    } catch (DaoException e) {
      logger.error("调用isOriginalVersion异常！", e);
      throw new ServiceException(e);

    }

  }

  @Override
  public boolean validateTop5PubAttachment(Long nsfcPrjId, Integer rptYear) throws ServiceException {

    try {
      if (nsfcPrjId == null || rptYear == null) {
        throw new ServiceException("项目编号和项目年度不能为空");
      }

      List<NsfcReschProjectReport> prjRpt = this.reschPrjRptDao.getPrjRptByNsfcPrjIdYear(nsfcPrjId, rptYear);
      if (CollectionUtils.isEmpty(prjRpt) || prjRpt.size() > 1) {
        throw new ServiceException("没有找到对应的结题报告nsfcPrjId:" + nsfcPrjId + " rptYear:" + rptYear);
      }

      List<Long> list = this.reschPrjRptPubDao.getTop5ProjectReportPubsByRptId(prjRpt.get(0).getRptId());

      if (list != null) {

        boolean flag = true;
        for (Long pubId : list) {
          try {

            String xmlData = this.publicationXmlService.getById(pubId).getXmlData();

            PubXmlDocument doc = new PubXmlDocument(xmlData);
            PubFulltextExtend fulltext = doc.getFulltext();
            Long fileId = fulltext.getFullTextFileId();

            if (!doc.existsNode(PubXmlConstants.PUB_ATTACHMENTS_ATTACHMENT_XPATH)
                && (fileId == null || fileId.longValue() == 0)) {

              flag = false;
              break;

            }

          } catch (Exception e) {

            logger.error("解析pub xml异常！", e);
            return false;
          }

        }

        return flag;

      }

    } catch (DaoException e) {
      logger.error(e.getMessage(), e);
      throw new ServiceException("查询结题报告成果失败");
    }

    return false;
  }

  @Override
  public void updateReschRptVersionId(ReschProjectReportPubModel form) throws ServiceException {

    Assert.notNull(form.getRptId(), "报告rptId不允许为空！");

    try {

      if (!form.isIgnore() && this.isOriginalVersion(form.getRptId())) {

        return;
      }

      NsfcReschProjectReport nsfcReschPrjRpt = this.reschPrjRptDao.getProjectReport(form.getRptId());
      nsfcReschPrjRpt.setVersionId(nsfcReschPrjRpt.getVersionId() + 1);
      this.reschPrjRptDao.saveProjectReport(nsfcReschPrjRpt);
    } catch (Exception e) {

      logger.error("修改版本号异常！", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public Long getReschRptVersionId(Long nsfcPrjId, Integer rptYear) throws ServiceException {
    try {
      if (nsfcPrjId == null || rptYear == null) {
        throw new ServiceException("项目编号和项目年度不能为空");
      }

      List<NsfcReschProjectReport> prjRpt = this.reschPrjRptDao.getPrjRptByNsfcPrjIdYear(nsfcPrjId, rptYear);
      if (CollectionUtils.isEmpty(prjRpt) || prjRpt.size() > 1) {
        throw new ServiceException("没有找到对应的结题报告nsfcPrjId:" + nsfcPrjId + " rptYear:" + rptYear);
      }

      return prjRpt.get(0).getVersionId();

    } catch (Exception e) {

      logger.error("获取成果研究报告versionId异常!", e);
      throw new ServiceException(e);
    }
  }


  private String genResultXml(List<NsfcReschPrjRptPub> pubs, String resultCode, long nsfcPrjId, int rptYear,
      Long userId) {
    String result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><data><final_meta prjcode=\"" + nsfcPrjId
        + "\" rptyear=\"" + rptYear + "\"  resultcode=\"" + resultCode + "\" /></data>";

    try {
      Document doc = DocumentHelper.parseText(result);
      Element root = doc.getRootElement();
      Element publications = root.addElement("publications");
      Long psnId = SecurityUtils.getCurrentUserId();
      if (psnId == null || psnId == 0L) {

        psnId = userId;
      }
      Long versionId = getReschRptVersionId(nsfcPrjId, rptYear);
      publications.addAttribute("versionid", versionId.toString());
      if (pubs != null) {

        publications.addAttribute("hastop5files", "" + validateTop5PubAttachment(nsfcPrjId, rptYear) + "");

      } else {

        publications.addAttribute("hastop5files", "true");
      }
      if (pubs != null) {
        for (NsfcReschPrjRptPub pub : pubs) {
          Element publication = publications.addElement("publication");
          createPublication(publication, pub);
        }
      }
      logger.info(doc.asXML());

      return doc.asXML();
    } catch (DocumentException e) {
      result = result.replace("resultcode=\"" + resultCode + "\"", "resultcode=\"isis_9\"");
      return result;
    } catch (ServiceException e) {
      result = result.replace("resultcode=\"" + resultCode + "\"", "resultcode=\"isis_9\"");
      return result;
    }

  }

  @SuppressWarnings("rawtypes")
  private void createPublication(Element publication, NsfcReschPrjRptPub pub) throws ServiceException {

    try {
      Map pros = BeanUtils.describe(pub);
      for (Iterator iterator = pros.keySet().iterator(); iterator.hasNext();) {
        String key = ObjectUtils.toString(iterator.next());
        if ("id".equals(key)) {
          publication.addAttribute("rptId", String.valueOf(pub.getId().getRptId()));
          publication.addAttribute("pubId", String.valueOf(pub.getId().getPubId()));
        } else if (!"class".equalsIgnoreCase(key) && !"pubCw".equalsIgnoreCase(key)
            && !"serialVersionUID".equals(key)) {
          publication.addAttribute(key, ObjectUtils.toString(pros.get(key)));
        }
        // 添加成果域名
        Integer nodeId = pub.getNodeId();
        // FIXME 2015-10-29 远程调用取消 -done
        // String pubWebUrl = this.snsSrvServiceLocator.getWebUrl(BasicRmtSrvModuleConstants.SNS_MODULE_ID);
        String pubWebUrl = domainscm;
        publication.addAttribute("pubWebUrl", pubWebUrl);

      }
    } catch (IllegalAccessException e) {
      throw new ServiceException(e.getMessage());
    } catch (InvocationTargetException e) {
      throw new ServiceException(e.getMessage());
    } catch (Exception e) {
      throw new ServiceException(e.getMessage());
    }

  }

  @Override
  public List<NsfcReschPrjRptPub> findNsfcReschPrjRptPubByPubId(Long pubId) throws ServiceException {
    try {
      return this.reschPrjRptPubDao.findNsfcReschPrjRptPubByPubId(pubId);
    } catch (DaoException e) {
      logger.error("根据PubId查询研究报告成果出现异常，pubId=" + pubId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveNsfcReschPrjRptPubList(List<NsfcReschPrjRptPub> nrpppList) throws ServiceException {
    try {
      this.reschPrjRptPubDao.saveProjectReportPub(nrpppList);
    } catch (DaoException e) {
      logger.error("保存研究报告成果出现异常", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Integer findStatusByRptId(Long rptId) throws ServiceException {
    try {
      return this.reschPrjRptDao.findStatusByRptId(rptId);
    } catch (DaoException e) {
      logger.error("查询研究成果报告状态出现异常，rptId=" + rptId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public NsfcReschProjectReport findById(Long rptId) throws ServiceException {
    try {
      return this.reschPrjRptDao.getProjectReport(rptId);
    } catch (DaoException e) {
      logger.error("查询研究成果报告信息出现异常，rptId=" + rptId, e);
      throw new ServiceException(e);
    }
  }

}
