package com.smate.center.task.single.service.pub;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.pdwh.quartz.DbCachePfetchDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubXmlToHandleDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.pdwh.pub.DbCachePfetch;
import com.smate.center.task.model.pdwh.pub.PdwhPubXmlToHandle;
import com.smate.center.task.single.oldXml.pub.ImportPubXmlDocument;
import com.smate.core.base.pub.enums.PublicationTypeEnum;

/**
 * 批量抓取成果XML处理业务实现类.
 * 
 * @author LJ 2017-3-10
 * 
 */
@Service("dbCachePfetchService")
@Transactional(rollbackFor = Exception.class)
public class DbCachePfetchServiceImpl implements DbCachePfetchService {

  private static final long serialVersionUID = 922900327912416415L;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private DbCachePfetchDao dbCachePfetchDao;
  /*
   * @Autowired private BatchJobsDao batchJobsDao;
   */
  @Autowired
  private PdwhPubXmlToHandleDao pdwhPubXmlToHandleDao;

  @Override
  public void saveError(Long id) throws ServiceException {
    dbCachePfetchDao.saveError(id);

  }

  @Override
  public List<DbCachePfetch> getTohandleList(int size) throws ServiceException {
    List<DbCachePfetch> list = null;
    try {
      list = dbCachePfetchDao.getToHandleXmlList(size);
    } catch (ServiceException e) {
      logger.error("批量获取待处理XML数据错误", e);
    }
    return list;
  }

  @Override
  public void saveSuccess(Long id) throws ServiceException {
    // 处理完更新状态为1
    dbCachePfetchDao.modifyStatus(id);

  }

  /*
   * @Override public void constructBatchJobs(Long XmlId) throws ServiceException { try { //
   * 构造存入v-batch_jobs表实体 String jobContext = "{\"msg_id\":" + XmlId + "}\""; BatchJobs jobs = new
   * BatchJobs(); jobs.setJobContext(jobContext); jobs.setWeight("B"); jobs.setStatus(0);
   * jobs.setStrategy(BatchJobDetailConstant.Dbcache_Bfetch_XML); jobs.setCreateTime(new Date());
   * batchJobsDao.save(jobs); } catch (Exception e) { logger.error("构造存入v-batch_jobs表实体出错,xmlId=" +
   * XmlId, e); throw new ServiceException("保存到v-batch_jobs表实体出错", e); } }
   */

  @Override
  public boolean validateXml(ImportPubXmlDocument doc) throws ServiceException {
    String dbCode = doc.getSourceDbCode();
    String enTitle = doc.getEnTitle();
    String zhTitle = doc.getZhTitle();

    if (StringUtils.isBlank(dbCode)) {
      return false;
    }

    if (StringUtils.isBlank(enTitle) && StringUtils.isBlank(zhTitle)) {
      return false;
    }

    // SCM-22626 新增对成果类型的判断逻辑，成果类型为空的成果，不导入值基准库中
    if (doc.getPubType() == null) {
      return false;
    }
    // 如果抓取专利专利号与公开号都为空，则不导入
    if (doc.getPubType() == 5 || dbCode.equalsIgnoreCase("cnkipat")) {
      String patentNo = doc.getPatentNo();
      String patentOpenNo = doc.getPatentOpenNo();
      if (StringUtils.isBlank(patentNo) && StringUtils.isBlank(patentOpenNo)) {
        return false;
      }
    }

    // isi,ei等成果isisource，eisource，doi不能全部为空
    if (dbCode.equalsIgnoreCase("ei") || dbCode.equalsIgnoreCase("sci") || dbCode.equalsIgnoreCase("istp")
        || dbCode.equalsIgnoreCase("ssci")) {
      String doi = doc.getDoi();
      String sourceId = doc.getSourceId();
      if (StringUtils.isBlank(doi) && StringUtils.isBlank(sourceId)) {
        return false;
      }
    }

    // 20190605 基准库增加标准号判断，软件著作权的登记号判断，值为空的情况不进行导入基准库
    if (PublicationTypeEnum.STANDARD == doc.getPubType()) {
      String standardNo = doc.getStandardNo();
      if (StringUtils.isBlank(standardNo)) {
        return false;
      }
    }
    if (PublicationTypeEnum.SOFTWARE_COPYRIGHT == doc.getPubType()) {
      String registerNo = doc.getRegisterNo();
      if (StringUtils.isBlank(registerNo)) {
        return false;
      }
    }

    return true;
  }

  @Override
  public void savePdwhPubXml(PdwhPubXmlToHandle pdwhPubXmlToHandle) throws ServiceException {
    pdwhPubXmlToHandleDao.save(pdwhPubXmlToHandle);
  }

  /*
   * @Override public void constructBatchJobs(Long XmlId, int status) { try { // 构造存入v-batch_jobs表实体
   * String jobContext = "{\"msg_id\":" + XmlId + "}\""; BatchJobs jobs = new BatchJobs();
   * jobs.setJobContext(jobContext); jobs.setWeight("B"); jobs.setStatus(status);
   * jobs.setStrategy(BatchJobDetailConstant.Dbcache_Bfetch_XML); jobs.setCreateTime(new Date());
   * batchJobsDao.save(jobs); } catch (Exception e) { logger.error("构造存入v-batch_jobs表实体出错,xmlId=" +
   * XmlId, e); throw new ServiceException("保存到v-batch_jobs表实体出错", e); } }
   */

}
