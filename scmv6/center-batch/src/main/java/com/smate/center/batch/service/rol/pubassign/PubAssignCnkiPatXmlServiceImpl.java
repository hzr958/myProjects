package com.smate.center.batch.service.rol.pubassign;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.chain.pubassign.process.IPubAssignXmlProcess;
import com.smate.center.batch.dao.rol.pub.PubAssignCnkiPatAuthorDao;
import com.smate.center.batch.dao.rol.pub.PubAssignCnkiPatDeptDao;
import com.smate.center.batch.dao.rol.pub.PubAssignCnkiPatKeywordsDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubAssignCnkiPatAuthor;
import com.smate.center.batch.model.rol.pub.PubAssignCnkiPatDept;
import com.smate.center.batch.model.rol.pub.PubAssignCnkiPatKeywords;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;

/**
 * CnkiPat成果数据展开，用于指派成果使用.
 * 
 * @author liqinghua
 * 
 */
@Service("pubAssignCnkiPatXmlService")
@Transactional(rollbackFor = Exception.class)
public class PubAssignCnkiPatXmlServiceImpl implements PubAssignCnkiPatXmlService {

  /**
   * 
   */
  private static final long serialVersionUID = 5535640475515305753L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubAssignCnkiPatAuthorDao pubAssignCnkiPatAuthorDao;
  @Autowired
  private PubAssignCnkiPatDeptDao pubAssignCnkiPatDeptDao;
  @Autowired
  private PubAssignCnkiPatKeywordsDao pubAssignCnkiPatKeywordsDao;
  private IPubAssignXmlProcess cnkiPatPubAssignXmlProcess;

  @Override
  public void extractAssignData(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws ServiceException {

    try {
      if (context.isCnkiPatImport() && !this.pubAssignCnkiPatAuthorDao.isExistsPubAuthor(context.getCurrentPubId())) {
        cnkiPatPubAssignXmlProcess.start(xmlDocument, context);
      }
    } catch (Exception e) {
      logger.error("导入CnkiPat成果展开指派数据", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean isExtractAssignData(Long pubId) throws ServiceException {

    if (this.pubAssignCnkiPatAuthorDao.isExistsPubAuthor(pubId)) {
      return true;
    }
    return false;
  }

  @Override
  public void savePubAuthor(Long pubId, Long pubInsId, String name, Long insId, Integer seqNo) throws ServiceException {

    if (StringUtils.isBlank(name)) {
      return;
    }
    try {
      name = StringUtils.substring(name.trim().toLowerCase(), 0, 100);
      PubAssignCnkiPatAuthor pubAuth = new PubAssignCnkiPatAuthor(pubId, pubInsId, name, insId, seqNo);
      this.pubAssignCnkiPatAuthorDao.save(pubAuth);
    } catch (Exception e) {
      logger.error("保存导入CnkiPat成果展开作者数据", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void savePubDept(String dept, Long pubId, Long pubInsId, Integer seqNo, Long insId) throws ServiceException {
    if (StringUtils.isBlank(dept)) {
      return;
    }
    try {
      dept = StringUtils.substring(dept.trim().toLowerCase(), 0, 200);

      PubAssignCnkiPatDept pubDept = new PubAssignCnkiPatDept(dept, pubId, pubInsId, seqNo, insId);
      pubAssignCnkiPatDeptDao.save(pubDept);
    } catch (Exception e) {
      logger.error("保存CnkiPat成果部门信息", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void savePubKeywords(String keywords, Long pubId, Long pubInsId) throws ServiceException {
    if (StringUtils.isBlank(keywords)) {
      return;
    }
    try {
      keywords = StringUtils.substring(keywords.trim().toLowerCase(), 0, 100);
      Integer keyHash = keywords.hashCode();
      PubAssignCnkiPatKeywords pubKeywords = new PubAssignCnkiPatKeywords(pubId, keywords, keyHash, pubInsId);
      this.pubAssignCnkiPatKeywordsDao.save(pubKeywords);
    } catch (Exception e) {
      logger.error("保存CnkiPat成果关键词", e);
      throw new ServiceException(e);
    }
  }

  public void setCnkiPatPubAssignXmlProcess(IPubAssignXmlProcess cnkiPatPubAssignXmlProcess) {
    this.cnkiPatPubAssignXmlProcess = cnkiPatPubAssignXmlProcess;
  }

}
