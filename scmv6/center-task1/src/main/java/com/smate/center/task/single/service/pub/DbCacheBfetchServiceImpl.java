package com.smate.center.task.single.service.pub;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.task.dao.pdwh.quartz.DbCacheBfetchDao;
import com.smate.center.task.dao.pdwh.quartz.OriginalPdwhPubRelationDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.pdwh.pub.DbCacheBfetch;
import com.smate.center.task.model.pdwh.pub.OriginalPdwhPubRelation;
import com.smate.center.task.single.oldXml.pub.ImportPubXmlDocument;
import com.smate.core.base.pub.enums.PublicationTypeEnum;

/**
 * 批量抓取成果XML处理业务实现类.
 * 
 * @author LJ 2017-2-28
 * 
 */
@Service("dbCacheBfetchService")
@Transactional(rollbackFor = Exception.class)
public class DbCacheBfetchServiceImpl implements DbCacheBfetchService {
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String scmDomain;
  private static final long serialVersionUID = 922900327912416415L;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private DbCacheBfetchDao dbCacheBfetchDao;
  @Autowired
  private OriginalPdwhPubRelationDao originalPdwhPubRelationDao;

  @Override
  public void saveError(Long id, String message) throws ServiceException {
    dbCacheBfetchDao.saveError(id, message);

  }

  @Override
  public List<DbCacheBfetch> getTohandleList(int size) throws ServiceException {
    List<DbCacheBfetch> list = null;
    try {
      list = dbCacheBfetchDao.getToHandleXmlList(size);
    } catch (ServiceException e) {
      logger.error("批量获取待处理XML数据错误", e);
    }
    return list;
  }

  @Override
  public void saveSuccess(Long id) throws ServiceException {
    // 处理完更新状态为1
    dbCacheBfetchDao.modifyStatus(id);

  }

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

    // SCM-22626 新增对成果类型的判断逻辑，成果类型为空的成果，不导入基准库中
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
  public void saveXml(Long insId, Integer pubYear, String fileName, String xml) {
    try {
      if (StringUtils.isBlank(xml)) {
        logger.error("xmlStr为空，不保存 fileName:" + fileName);
        return;
      }
      DbCacheBfetch dbcf = new DbCacheBfetch(insId, xml, fileName, pubYear, 5);
      dbCacheBfetchDao.save(dbcf);
    } catch (Exception e) {
      logger.error("保存XML错误", e);
      throw new ServiceException("保存XML错误", e);
    }

  }


  @Override
  public Long saveOriginalPdwhPubRelation(Long xmlId, String seqNo, Long insId, long psnId, int recordFrom) {
    OriginalPdwhPubRelation originalPdwhPub =
        new OriginalPdwhPubRelation(xmlId, seqNo, insId, psnId, recordFrom, 0, new Date());
    originalPdwhPubRelationDao.save(originalPdwhPub);
    return originalPdwhPub.getId();
  }
}
