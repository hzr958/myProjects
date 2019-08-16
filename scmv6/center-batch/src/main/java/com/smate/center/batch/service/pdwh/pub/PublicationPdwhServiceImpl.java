package com.smate.center.batch.service.pdwh.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.connector.dao.exception.CreateBatchJobException;
import com.smate.center.batch.connector.enums.BatchOpenCodeEnum;
import com.smate.center.batch.connector.enums.BatchWeightEnum;
import com.smate.center.batch.connector.factory.BatchJobsFactory;
import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.connector.service.job.BatchJobsService;
import com.smate.center.batch.connector.util.BatchJobUtil;
import com.smate.center.batch.dao.rol.pub.PublicationRolPdwhDao;
import com.smate.center.batch.dao.sns.pub.PubMemberDao;
import com.smate.center.batch.dao.sns.pub.PubPdwhSnsRelationDao;
import com.smate.center.batch.dao.sns.pub.PublicationPdwhDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PublicationRolPdwh;
import com.smate.center.batch.model.sns.pub.PubPdwhSnsRelation;
import com.smate.center.batch.model.sns.pub.PublicationPdwh;
import com.smate.center.batch.model.sns.pub.PublicationXml;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.ConstPdwhPubRefDb;
import com.smate.center.batch.service.pub.PublicationXmlService;
import com.smate.center.batch.service.pub.pubgrouping.PubGroupingService;
import com.smate.center.batch.util.pub.PubXmlDbUtils;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.IrisNumberUtils;

/**
 * 成果基准库id.
 * 
 * @author liqinghua
 * 
 */
@Service("publicationPdwhService")
@Transactional(rollbackFor = Exception.class)
public class PublicationPdwhServiceImpl implements PublicationPdwhService {

  /**
   * 
   */
  private static final long serialVersionUID = -2003867995465796950L;
  private static Logger logger = LoggerFactory.getLogger(PublicationPdwhServiceImpl.class);
  @Autowired
  private PublicationPdwhDao publicationPdwhDao;
  @Autowired
  private PublicationRolPdwhDao publicationRolPdwhDao;
  @Autowired
  private PublicationXmlService publicationXmlService;
  @Autowired
  private PubGroupingService pubGroupingService;
  @Autowired
  private PubMemberDao pubMemberDao;
  @Autowired
  private PubPdwhSnsRelationDao pubPdwhSnsRelationDao;
  @Autowired
  private BatchJobsService batchJobsService;
  @Resource(name = "batchJobsContextFactory")
  private BatchJobsFactory batchJobsFactory;

  @Override
  public PublicationPdwh getPubPdwh(Long pubId) throws ServiceException {

    try {
      return publicationPdwhDao.get(pubId);
    } catch (Exception e) {
      logger.error("获取sns成果基准库ID", e);
      throw new ServiceException("获取sns成果基准库ID", e);
    }
  }

  @Override
  public PublicationRolPdwh getPubRolPdwh(Long pubId) throws ServiceException {
    try {
      return publicationRolPdwhDao.get(pubId);
    } catch (Exception e) {
      logger.error("获取rol成果基准库ID", e);
      throw new ServiceException("获取rol成果基准库ID", e);
    }
  }

  @Override
  public void savePublicationPdwh(PublicationPdwh pdwh) throws ServiceException {
    try {

      publicationPdwhDao.save(pdwh);
    } catch (Exception e) {
      logger.error("保存sns成果基准库ID", e);
      throw new ServiceException("保存sns成果基准库ID", e);
    }
  }

  @Override
  public void savePublicationRolPdwh(PublicationRolPdwh pdwh) throws ServiceException {
    try {

      publicationRolPdwhDao.save(pdwh);
    } catch (Exception e) {
      logger.error("保存rol成果基准库ID", e);
      throw new ServiceException("保存rol成果基准库ID", e);
    }
  }

  @Override
  public PublicationRolPdwh savePublicationRolPdwh(Long pubId, Integer dbId, Long queryId) throws ServiceException {
    try {

      PublicationRolPdwh pubRolpdwh = this.getPubRolPdwh(pubId);
      if (pubRolpdwh == null) {
        pubRolpdwh = new PublicationRolPdwh(pubId);
      }
      // isi文献
      if (PubXmlDbUtils.isIsiDb(dbId)) {
        pubRolpdwh.setIsiId(queryId);
        // scopus文献
      } else if (PubXmlDbUtils.isScopusDb(dbId)) {
        pubRolpdwh.setSpsId(queryId);
        // cnki文献
      } else if (PubXmlDbUtils.isCnkiDb(dbId)) {
        pubRolpdwh.setCnkiId(queryId);
        // ei
      } else if (PubXmlDbUtils.isEiDb(dbId)) {
        pubRolpdwh.setEiId(queryId);
        // wanfang
      } else if (PubXmlDbUtils.isWanFangDb(dbId)) {
        pubRolpdwh.setWfId(queryId);
        // CNIPRDb
      } else if (PubXmlDbUtils.isCNIPRDb(dbId)) {
        pubRolpdwh.setCniprId(queryId);
        // pubmed
      } else if (PubXmlDbUtils.isPubMedDb(dbId)) {
        pubRolpdwh.setPubmedId(queryId);
      } // ieeexp
      else if (PubXmlDbUtils.isIEEEXploreDb(dbId)) {
        pubRolpdwh.setIeeeXpId(queryId);
      } // ScienceDirect
      else if (PubXmlDbUtils.isScienceDirectDb(dbId)) {
        pubRolpdwh.setScdId(queryId);
      } // baidu
      else if (PubXmlDbUtils.isBaiduDb(dbId)) {
        pubRolpdwh.setBaiduId(queryId);
      } // cnkipat
      else if (PubXmlDbUtils.isCnkipatDb(dbId)) {
        pubRolpdwh.setCnkiPatId(queryId);
      }
      this.savePublicationRolPdwh(pubRolpdwh);

      // 对保存的成果进行分组 tsz_2014.11.25_SCM-5983
      // 传对象过去
      pubGroupingService.pubGroupingBuilder(pubRolpdwh);
      return pubRolpdwh;

    } catch (Exception e) {
      logger.error("获取基准库发送的基准库查询ID", e);
      throw new ServiceException("保存成果基准库ID", e);
    }
  }

  @Override
  public void refreshPubPdwhToXml(PublicationRolPdwh pubRolPdwh) throws ServiceException {

    try {
      PublicationXml pubXml = publicationXmlService.rolGetById(pubRolPdwh.getPubId());
      if (pubXml == null || StringUtils.isBlank(pubXml.getXmlData())) {
        return;
      }
      PubXmlDocument doc = new PubXmlDocument(pubXml.getXmlData());
      Element em = (Element) doc.getNode(PubXmlConstants.PUB_PDWH_XPATH);
      if (em == null) {
        em = doc.createElement(PubXmlConstants.PUB_PDWH_XPATH);
      }
      em.addAttribute("cnipr_id", pubRolPdwh.getCniprId() == null ? "" : pubRolPdwh.getCniprId().toString());
      em.addAttribute("cnki_id", pubRolPdwh.getCnkiId() == null ? "" : pubRolPdwh.getCnkiId().toString());
      em.addAttribute("ei_id", pubRolPdwh.getEiId() == null ? "" : pubRolPdwh.getEiId().toString());
      em.addAttribute("isi_id", pubRolPdwh.getIsiId() == null ? "" : pubRolPdwh.getIsiId().toString());
      em.addAttribute("sps_id", pubRolPdwh.getSpsId() == null ? "" : pubRolPdwh.getSpsId().toString());
      em.addAttribute("wf_id", pubRolPdwh.getWfId() == null ? "" : pubRolPdwh.getWfId().toString());
      em.addAttribute("pubmed_id", pubRolPdwh.getPubmedId() == null ? "" : pubRolPdwh.getPubmedId().toString());
      em.addAttribute("ieeexp_id", pubRolPdwh.getIeeeXpId() == null ? "" : pubRolPdwh.getIeeeXpId().toString());
      em.addAttribute("scd_id", pubRolPdwh.getScdId() == null ? "" : pubRolPdwh.getScdId().toString());
      em.addAttribute("baidu_id", pubRolPdwh.getBaiduId() == null ? "" : pubRolPdwh.getBaiduId().toString());
      em.addAttribute("cnkipat_id", pubRolPdwh.getCnkiPatId() == null ? "" : pubRolPdwh.getCnkiPatId().toString());
      // 更新xml
      publicationXmlService.rolSave(pubRolPdwh.getPubId(), doc.getXmlString());
    } catch (Exception e) {
      logger.error("将publicationpdwh表中的数据同步到XML中", e);
      throw new ServiceException("将publicationpdwh表中的数据同步到XML中", e);
    }
  }

  @Override
  public void refreshPubPdwhToDB(Long pubId, PubXmlDocument doc) throws ServiceException {

    if (!doc.existsNode(PubXmlConstants.PUB_PDWH_XPATH)) {
      return;
    }

    try {
      PublicationPdwh pdwh = this.getPubPdwh(pubId);
      if (pdwh == null) {
        pdwh = new PublicationPdwh(pubId);
      }

      PubPdwhSnsRelation pubPdwhSnsRelation = new PubPdwhSnsRelation();
      pubPdwhSnsRelation.setSnsPubId(pubId);
      Long cniprId = IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUB_PDWH_XPATH, "cnipr_id"));
      Long cnkiId = IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUB_PDWH_XPATH, "cnki_id"));
      Long eiId = IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUB_PDWH_XPATH, "ei_id"));
      Long isiId = IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUB_PDWH_XPATH, "isi_id"));
      Long spsId = IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUB_PDWH_XPATH, "sps_id"));
      Long wfId = IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUB_PDWH_XPATH, "wf_id"));
      // 添加scd_id导入时的同步，其他需要同步时添加；
      Long scdId = IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUB_PDWH_XPATH, "scd_id"));
      // SCM-15241
      Long cnkipatId =
          IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUB_PDWH_XPATH, "cnkipat_id"));
      Long ieeeId = IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUB_PDWH_XPATH, "ieee_id"));
      Long pubmId = IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUB_PDWH_XPATH, "pubm_id"));
      Long rainpatId =
          IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUB_PDWH_XPATH, "rainpat_id"));
      if (cniprId != null) {
        pdwh.setCniprId(cniprId);
        pubPdwhSnsRelation.setPdwhPubId(cniprId);
      }
      if (cnkiId != null) {
        pdwh.setCnkiId(cnkiId);
        pubPdwhSnsRelation.setPdwhPubId(cnkiId);
      }
      if (eiId != null) {
        pdwh.setEiId(eiId);
        pubPdwhSnsRelation.setPdwhPubId(eiId);
      }
      if (isiId != null) {
        pdwh.setIsiId(isiId);
        pubPdwhSnsRelation.setPdwhPubId(isiId);
      }
      if (spsId != null) {
        pdwh.setSpsId(spsId);
        pubPdwhSnsRelation.setPdwhPubId(spsId);
      }
      if (wfId != null) {
        pdwh.setWfId(wfId);
        pubPdwhSnsRelation.setPdwhPubId(wfId);
      }
      if (scdId != null) {
        pdwh.setScdId(scdId);
        pubPdwhSnsRelation.setPdwhPubId(scdId);
      }
      if (cnkipatId != null) {
        pdwh.setCnkiPatId(cnkipatId);
        pubPdwhSnsRelation.setPdwhPubId(cnkipatId);
      }
      if (ieeeId != null) {
        pdwh.setIeeeXpId(ieeeId);
        pubPdwhSnsRelation.setPdwhPubId(scdId);
      }
      if (pubmId != null) {
        pdwh.setPubmedId(pubmId);
        pubPdwhSnsRelation.setPdwhPubId(scdId);
      }

      if (rainpatId != null) {
        pubPdwhSnsRelation.setPdwhPubId(rainpatId);
      }
      // 新添加的库对应关系不在保存到老关系表
      if (rainpatId == null) {
        this.publicationPdwhDao.save(pdwh);
      }
      this.saveToRelation(pubPdwhSnsRelation);
      // 成果分组 tsz 2014.11.27
      // 对保存的成果进行分组 tsz_2014.11.25_SCM-5983
      pubGroupingService.pubGroupingBuilder(pdwh);

    } catch (Exception e) {
      logger.error("将Xml数据同步到表publicationpdwh中", e);
      throw new ServiceException("将Xml数据同步到表publicationpdwh中", e);
    }
  }

  /**
   * 保存到pubPdwhSnsRelationDao
   * 
   * @param pubPdwhSnsRelation
   */
  public void saveToRelation(PubPdwhSnsRelation pubPdwhSnsRelation) {
    if (pubPdwhSnsRelationDao.get(pubPdwhSnsRelation.getSnsPubId()) == null) {
      pubPdwhSnsRelationDao.save(pubPdwhSnsRelation);
    }

  }

  @Override
  public void refreshRolPubPdwhToDB(Long pubId, PubXmlDocument doc) {
    if (!doc.existsNode(PubXmlConstants.PUB_PDWH_XPATH)) {
      return;
    }
    try {
      PublicationRolPdwh pubRolpdwh = this.getPubRolPdwh(pubId);
      if (pubRolpdwh == null) {
        pubRolpdwh = new PublicationRolPdwh(pubId);
      }
      Long cniprId = IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUB_PDWH_XPATH, "cnipr_id"));
      Long cnkiId = IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUB_PDWH_XPATH, "cnki_id"));
      Long eiId = IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUB_PDWH_XPATH, "ei_id"));
      Long isiId = IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUB_PDWH_XPATH, "isi_id"));
      Long spsId = IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUB_PDWH_XPATH, "sps_id"));
      Long wfId = IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUB_PDWH_XPATH, "wf_id"));
      // 添加scd_id导入时的同步，其他需要同步时添加；
      Long scdId = IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PubXmlConstants.PUB_PDWH_XPATH, "scd_id"));
      if (cniprId != null) {
        pubRolpdwh.setCniprId(cniprId);
      }
      if (cnkiId != null) {
        pubRolpdwh.setCnkiId(cnkiId);
      }
      if (eiId != null) {
        pubRolpdwh.setEiId(eiId);
      }
      if (isiId != null) {
        pubRolpdwh.setIsiId(isiId);
      }
      if (spsId != null) {
        pubRolpdwh.setSpsId(spsId);
      }
      if (wfId != null) {
        pubRolpdwh.setWfId(wfId);
      }
      if (scdId != null) {
        pubRolpdwh.setScdId(scdId);
      }
      this.publicationRolPdwhDao.save(pubRolpdwh);

      // 成果分组 tsz 2014.11.27
      // 对保存的成果进行分组 tsz_2014.11.25_SCM-5983
      pubGroupingService.pubGroupingBuilder(pubRolpdwh);

    } catch (Exception e) {
      logger.error("将Xml数据同步到表publicationpdwh中", e);
      throw new ServiceException("将Xml数据同步到表publicationpdwh中", e);
    }

  }

  @Override
  public List<PublicationPdwh> loadPubPdwhBatch(Long lastId) throws ServiceException {

    try {
      return this.publicationPdwhDao.loadPubPdwhBatch(lastId);
    } catch (Exception e) {
      logger.error("批量获取成果基准库ID信息", e);
      throw new ServiceException("批量获取成果基准库ID信息", e);
    }
  }

  @Override
  public List<Long> findSnsIds(Long id, Integer dbId) throws ServiceException {
    String column = "";
    // isi文献
    if (ConstPdwhPubRefDb.ISI.equals(dbId)) {
      column = "isiId";
    } else if (ConstPdwhPubRefDb.SCOPUS.equals(dbId)) { // scopus文献
      column = "spsId";
    } else if (ConstPdwhPubRefDb.EI.equals(dbId)) { // ei
      column = "eiId";
    } else if (ConstPdwhPubRefDb.CNKI.equals(dbId)) { // cnki文献
      column = "cnkiId";
    } else if (ConstPdwhPubRefDb.CNIPR.equals(dbId)) {// CNIPRDb
      column = "cniprId";
    } else if (ConstPdwhPubRefDb.WanFang.equals(dbId)) { // wanfang
      column = "wfId";
    }
    if (StringUtils.isBlank(column)) {
      logger.error("没有找到dbId=" + dbId + "的数据库字段，请检查PUB_PDWH表");
      throw new ServiceException("没有找到dbId=" + dbId + "的数据库字段，请检查PUB_PDWH表");
    }
    try {
      return this.publicationPdwhDao.findSnsPubIds(id, column);
    } catch (DaoException e) {
      logger.error("获取基准库ID对应的snsID出错", e);
      throw new ServiceException("获取基准库ID对应的snsID出错", e);
    }
  }

  @Override
  public List<PublicationPdwh> getPubPdwhListByPubIds(List<Long> pubIds) throws ServiceException {
    return this.publicationPdwhDao.getPubPdwhListByPubIds(pubIds);
  }

  @Override
  public int getPubPdwhIdByPsnFriend(Long psnId, Long pdwhPubId, int dbid) throws ServiceException {
    return publicationPdwhDao.getPubPdwhIdByPsnFriend(psnId, pdwhPubId, dbid);
  }

  @Override
  public int getPubPdwhIdByPsnCoop(Long psnId, Long pdwhPubId, int dbid) throws ServiceException {
    return 0;
  }

  @Override
  public List<Long> findSnsPubOwnerPsnIds(Page page, Long id, Integer dbId) throws ServiceException {
    String column = "";
    // isi文献
    if (ArrayUtils.contains(ConstPdwhPubRefDb.ISI_LIST, dbId)) {
      column = "isiId";
    } else if (ConstPdwhPubRefDb.SCOPUS.equals(dbId)) { // scopus文献
      column = "spsId";
    } else if (ConstPdwhPubRefDb.EI.equals(dbId)) { // ei
      column = "eiId";
    } else if (ConstPdwhPubRefDb.CNKI.equals(dbId)) { // cnki文献
      column = "cnkiId";
    } else if (ConstPdwhPubRefDb.CNIPR.equals(dbId)) {// CNIPRDb
      column = "cniprId";
    } else if (ConstPdwhPubRefDb.WanFang.equals(dbId)) { // wanfang
      column = "wfId";
    }
    if (StringUtils.isBlank(column)) {
      logger.error("没有找到dbId=" + dbId + "的数据库字段，请检查PUB_PDWH表,基准库ID=" + id);
      return null;
    }
    try {
      return this.publicationPdwhDao.findSnsPubOwnerPsnIds(page, id, column);
    } catch (DaoException e) {
      logger.error("获取基准库ID对应的SnsPubOwnerPsnIds出错", e);
      throw new ServiceException("获取基准库ID对应的SnsPubOwnerPsnIds出错", e);
    }
  }

  @Override
  public List<Long> findSnsPubOwnerPsnIdsByPubId(Page page, Long pubId) throws ServiceException {
    try {
      PublicationPdwh pubPdwh = this.publicationPdwhDao.getPublicationPdwhByPubId(pubId);
      if (pubPdwh == null) {
        return null;
      }
      String column = "";
      Long pubPdwhId = null;
      Map<String, Object> map = this.getPubPdwhId(pubPdwh, column, pubPdwhId);
      column = ObjectUtils.toString(map.get("column"));
      if (StringUtils.isBlank(column)) {
        logger.error("没有找到pubId=" + pubId + "对应的基准库ID字段，请检查PUB_PDWH表");
        return null;
      }
      pubPdwhId = NumberUtils.createLong(map.get("pubPdwhId").toString());
      try {
        return this.publicationPdwhDao.findSnsPubOwnerPsnIdsWithKnow(page, pubPdwhId, column);
      } catch (DaoException e) {
        logger.error("获取基准库ID对应的SnsPubOwnerPsnIds出错, column=" + column + ", pubPdwhId=" + pubPdwhId, e);
        throw new ServiceException("获取基准库ID对应的SnsPubOwnerPsnIds出错, column=" + column + ", pubPdwhId=" + pubPdwhId, e);
      }
    } catch (Exception e) {
      logger.error("根据pubId查找基准库ID出错啦, pubId=" + pubId, e);
      throw new ServiceException("根据pubId查找基准库ID出错啦, pubId=" + pubId, e);
    }
  }

  @Override
  public List<Map<String, String>> findSnsPubOwnersByPubId(Page page, Long pubId) throws ServiceException {
    try {
      PublicationPdwh pubPdwh = this.publicationPdwhDao.getPublicationPdwhByPubId(pubId);
      if (pubPdwh == null) {
        return pubMemberDao.findSnsPubMembers(page, pubId);
      }
      String column = "";
      Long pubPdwhId = null;
      Map<String, Object> map = this.getPubPdwhId(pubPdwh, column, pubPdwhId);
      column = ObjectUtils.toString(map.get("column"));
      if (StringUtils.isBlank(column)) {
        logger.error("没有找到pubId=" + pubId + "对应的基准库ID字段，请检查PUB_PDWH表");
        return null;
      } else {
        if ("isiId".equals(column)) {
          column = "PDWH_ISI_ID";
        } else if ("eiId".equals(column)) {
          column = "PDWH_EI_ID";
        } else if ("spsId".equals(column)) {
          column = "PDWH_SPS_ID";
        } else if ("cnkiId".equals(column)) {
          column = "PDWH_CNKI_ID";
        } else if ("wfId".equals(column)) {
          column = "PDWH_WF_ID";
        } else if ("cniprId".equals(column)) {
          column = "PDWH_CNIPR_ID";
        } else if ("pubmedId".equals(column)) {
          column = "PDWH_PUBMED_ID";
        } else if ("ieeeXpId".equals(column)) {
          column = "PDWH_IEEEXP_ID";
        } else if ("scdId".equals(column)) {
          column = "PDWH_SCD_ID";
        } else if ("baiduId".equals(column)) {
          column = "PDWH_BAIDU_ID";
        } else if ("cnkiPatId".equals(column)) {
          column = "PDWH_CNKIPAT_ID";
        }
      }
      pubPdwhId = NumberUtils.createLong(map.get("pubPdwhId").toString());
      try {
        return this.publicationPdwhDao.findSnsPubauthors(page, pubPdwhId, column, pubId);
      } catch (DaoException e) {
        logger.error("获取基准库ID对应的SnsPubOwnerPsnIds出错, column=" + column + ", pubPdwhId=" + pubPdwhId, e);
        throw new ServiceException("获取基准库ID对应的SnsPubOwnerPsnIds出错, column=" + column + ", pubPdwhId=" + pubPdwhId, e);
      }
    } catch (Exception e) {
      logger.error("根据pubId查找基准库ID出错啦, pubId=" + pubId, e);
      throw new ServiceException("根据pubId查找基准库ID出错啦, pubId=" + pubId, e);
    }
  }

  @Override
  public List<Long> findRelatedPubIdsByPubId(Page page, Long pubId) throws ServiceException {
    try {
      PublicationPdwh pubPdwh = this.publicationPdwhDao.getPublicationPdwhByPubId(pubId);
      if (pubPdwh == null) {
        return null;
      }
      String column = "";
      Long pubPdwhId = null;
      Map<String, Object> map = this.getPubPdwhId(pubPdwh, column, pubPdwhId);
      column = ObjectUtils.toString(map.get("column"));
      pubPdwhId = NumberUtils.createLong(map.get("pubPdwhId").toString());
      if (StringUtils.isBlank(column)) {
        logger.error("没有找到pubId=" + pubId + "对应的基准库ID字段，请检查PUB_PDWH表");
        return null;
      }
      try {
        return this.publicationPdwhDao.findSnsPubPubIds(page, pubPdwhId, column, pubId);
      } catch (DaoException e) {
        logger.error("获取基准库ID对应的相关成果pubIds出错, column=" + column + ", pubPdwhId=" + pubPdwhId + ", pubId=" + pubId, e);
        throw new ServiceException(
            "获取基准库ID对应的相关成果pubIds出错, column=" + column + ", pubPdwhId=" + pubPdwhId + ", pubId=" + pubId, e);
      }
    } catch (Exception e) {
      logger.error("根据pubId查找基准库ID出错啦, pubId=" + pubId, e);
      throw new ServiceException("根据pubId查找基准库ID出错啦, pubId=" + pubId, e);
    }
  }

  @Override
  public List<Long> findOtherRelatedPubIdByPubId(Long pubId) throws ServiceException {
    try {
      PublicationPdwh pubPdwh = this.publicationPdwhDao.getPublicationPdwhByPubId(pubId);
      if (pubPdwh == null) {
        return null;
      }
      String column = "";
      Long pubPdwhId = null;
      Map<String, Object> map = this.getPubPdwhId(pubPdwh, column, pubPdwhId);
      column = ObjectUtils.toString(map.get("column"));
      if (StringUtils.isBlank(column)) {
        logger.error("没有找到pubId=" + pubId + "对应的基准库ID字段，请检查PUB_PDWH表");
        return null;
      }
      pubPdwhId = NumberUtils.createLong(map.get("pubPdwhId").toString());
      try {
        Long psnId = SecurityUtils.getCurrentUserId();
        return this.publicationPdwhDao.findSnsOtherRelPubId(pubPdwhId, column, pubId, psnId);
      } catch (DaoException e) {
        logger.error("获取基准库ID对应的其它相关成果pubId出错, column=" + column + ", pubPdwhId=" + pubPdwhId + ", pubId=" + pubId, e);
        throw new ServiceException(
            "获取基准库ID对应的其它相关成果pubId出错, column=" + column + ", pubPdwhId=" + pubPdwhId + ", pubId=" + pubId, e);
      }
    } catch (Exception e) {
      logger.error("根据pubId查找基准库ID出错啦, pubId=" + pubId, e);
      throw new ServiceException("根据pubId查找基准库ID出错啦, pubId=" + pubId, e);
    }
  }

  private Map<String, Object> getPubPdwhId(PublicationPdwh pubPdwh, String column, Long pubPdwhId)
      throws ServiceException {
    Map<String, Object> map = new HashMap<String, Object>();
    if (pubPdwh.getIsiId() != null && pubPdwh.getIsiId().longValue() > 0) {
      column = "isiId";
      pubPdwhId = pubPdwh.getIsiId();
    } else if (pubPdwh.getSpsId() != null && pubPdwh.getSpsId().longValue() > 0) {
      column = "spsId";
      pubPdwhId = pubPdwh.getSpsId();
    } else if (pubPdwh.getEiId() != null && pubPdwh.getEiId().longValue() > 0) {
      column = "eiId";
      pubPdwhId = pubPdwh.getEiId();
    } else if (pubPdwh.getCnkiId() != null && pubPdwh.getCnkiId().longValue() > 0) {
      column = "cnkiId";
      pubPdwhId = pubPdwh.getCnkiId();
    } else if (pubPdwh.getCniprId() != null && pubPdwh.getCniprId().longValue() > 0) {
      column = "cniprId";
      pubPdwhId = pubPdwh.getCniprId();
    } else if (pubPdwh.getWfId() != null && pubPdwh.getWfId().longValue() > 0) {
      column = "wfId";
      pubPdwhId = pubPdwh.getWfId();
    } else if (pubPdwh.getPubmedId() != null && pubPdwh.getPubmedId().longValue() > 0) {
      column = "pubmedId";
      pubPdwhId = pubPdwh.getPubmedId();
    } else if (pubPdwh.getIeeeXpId() != null && pubPdwh.getIeeeXpId().longValue() > 0) {
      column = "ieeeXpId";
      pubPdwhId = pubPdwh.getIeeeXpId();
    } else if (pubPdwh.getScdId() != null && pubPdwh.getScdId().longValue() > 0) {
      column = "scdId";
      pubPdwhId = pubPdwh.getScdId();
    } else if (pubPdwh.getBaiduId() != null && pubPdwh.getBaiduId().longValue() > 0) {
      column = "baiduId";
      pubPdwhId = pubPdwh.getBaiduId();
    } else if (pubPdwh.getCnkiPatId() != null && pubPdwh.getCnkiPatId().longValue() > 0) {
      column = "cnkiPatId";
      pubPdwhId = pubPdwh.getCnkiPatId();
    }
    map.put("column", column);
    map.put("pubPdwhId", pubPdwhId);
    return map;
  }

  @Override
  public List<Map<String, Object>> findSnsPubPsnIdsAndPubIds(Long id, Integer dbId) throws ServiceException {
    String column = "";
    // isi文献
    if (ArrayUtils.contains(ConstPdwhPubRefDb.ISI_LIST, dbId)) {
      column = "isiId";
    } else if (ConstPdwhPubRefDb.SCOPUS.equals(dbId)) { // scopus文献
      column = "spsId";
    } else if (ConstPdwhPubRefDb.EI.equals(dbId)) { // ei
      column = "eiId";
    } else if (ConstPdwhPubRefDb.CNKI.equals(dbId)) { // cnki文献
      column = "cnkiId";
    } else if (ConstPdwhPubRefDb.CNIPR.equals(dbId)) {// CNIPRDb
      column = "cniprId";
    } else if (ConstPdwhPubRefDb.WanFang.equals(dbId)) { // wanfang
      column = "wfId";
    }
    if (StringUtils.isBlank(column)) {
      logger.error("没有找到dbId=" + dbId + "的数据库字段，请检查PUB_PDWH表,基准库ID=" + id);
      return null;
    }
    try {
      int maxSize = 1;
      List<Map<String, Object>> listMap = this.publicationPdwhDao.findSnsPubPsnIdsAndPubIds(true, maxSize, id, column);
      if (CollectionUtils.isEmpty(listMap)) {
        listMap = this.publicationPdwhDao.findSnsPubPsnIdsAndPubIds(false, maxSize, id, column);
      }
      return listMap;
    } catch (DaoException e) {
      logger.error("根据基准库ID和DBID查找出sns的成果的psnIds、pubIds出错", e);
      throw new ServiceException("根据基准库ID和DBID查找出sns的成果的psnIds、pubIds出错", e);
    }
  }

  @Override
  public List<Long> findSnsPubIdsByPubId(Long pubId) throws ServiceException {
    try {
      PublicationPdwh pubPdwh = this.publicationPdwhDao.getPublicationPdwhByPubId(pubId);
      if (pubPdwh == null) {
        return null;
      }
      String column = "";
      Long pubPdwhId = null;
      Map<String, Object> map = this.getPubPdwhId(pubPdwh, column, pubPdwhId);
      column = ObjectUtils.toString(map.get("column"));
      pubPdwhId = NumberUtils.createLong(map.get("pubPdwhId").toString());
      if (StringUtils.isBlank(column)) {
        logger.error("没有找到pubId=" + pubId + "对应的基准库ID字段，请检查PUB_PDWH表");
        return null;
      }
      try {
        return this.publicationPdwhDao.findSnsPubIds(pubPdwhId, column);
      } catch (DaoException e) {
        logger.error("根据基准库ID查找对应的相关成果pubIds出错, column=" + column + ", pubPdwhId=" + pubPdwhId + ", pubId=" + pubId, e);
        throw new ServiceException(
            "根据基准库ID查找对应的相关成果pubIds出错, column=" + column + ", pubPdwhId=" + pubPdwhId + ", pubId=" + pubId, e);
      }
    } catch (Exception e) {
      logger.error("根据pubId查找基准库ID出错啦, pubId=" + pubId, e);
      throw new ServiceException("根据pubId查找基准库ID出错啦, pubId=" + pubId, e);
    }
  }

  @Override
  public Long getPdwhPubId(Long snsPubId, int dbId) throws ServiceException {
    Long pdwhPubId = null;
    try {
      PublicationPdwh pubPdwh = this.publicationPdwhDao.getPublicationPdwhByPubId(snsPubId);
      if (pubPdwh == null) {
        return null;
      }
      if (ArrayUtils.contains(ConstPdwhPubRefDb.ISI_LIST, dbId)) {
        pdwhPubId = pubPdwh.getIsiId();
      } else if (ConstPdwhPubRefDb.SCOPUS.equals(dbId)) { // scopus文献
        pdwhPubId = pubPdwh.getSpsId();
      } else if (ConstPdwhPubRefDb.EI.equals(dbId)) { // ei
        pdwhPubId = pubPdwh.getEiId();
      } else if (ConstPdwhPubRefDb.CNKI.equals(dbId)) { // cnki文献
        pdwhPubId = pubPdwh.getCnkiId();
      } else if (ConstPdwhPubRefDb.CNIPR.equals(dbId)) {// CNIPRDb
        pdwhPubId = pubPdwh.getCniprId();
      } else if (ConstPdwhPubRefDb.WanFang.equals(dbId)) { // wanfang
        pdwhPubId = pubPdwh.getWfId();
      } else if (ConstPdwhPubRefDb.PubMed.equals(dbId)) { // wanfang
        pdwhPubId = pubPdwh.getPubmedId();
      } else if (ConstPdwhPubRefDb.Baidu.equals(dbId)) { // wanfang
        pdwhPubId = pubPdwh.getBaiduId();
      }
    } catch (Exception e) {
      logger.error("根据sns成果Id获取基准成果Id出错，snsPubId:{},dbId:{}", snsPubId, dbId, e);
    }
    return pdwhPubId;
  }

  /**
   * 通过基准库各来源库id得到成果关系
   * 
   * @param pdwhPubMap
   * @return
   */
  @Override
  public List<PublicationPdwh> getPubPdwhByPdwhId(Map<String, Long> pdwhPubMap) throws ServiceException {
    return publicationPdwhDao.getPubPdwhByPdwhId(pdwhPubMap);
  }

  @Override
  public void saveToBatchJobs(Long pubId, Long insId, String dbSource) throws ServiceException {
    try {

      BatchJobs job = batchJobsFactory.createBatchJob(BatchOpenCodeEnum.PUB_ASSIGN_FOR_ROL,
          BatchJobUtil.getDbSourceContext(pubId + "", insId + "", "\"" + dbSource + "\""),
          BatchWeightEnum.A.toString());
      batchJobsService.saveJob(job);
    } catch (CreateBatchJobException e) {
      throw new ServiceException(e);
    }

  }
}
