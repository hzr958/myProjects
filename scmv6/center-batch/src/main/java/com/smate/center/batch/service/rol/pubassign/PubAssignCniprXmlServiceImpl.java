package com.smate.center.batch.service.rol.pubassign;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.chain.pubassign.process.IPubAssignXmlProcess;
import com.smate.center.batch.dao.rol.pub.PubAssignCniprAuthorDao;
import com.smate.center.batch.dao.rol.pub.PubAssignCniprDeptDao;
import com.smate.center.batch.dao.rol.pub.PubAssignCniprKeywordsDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubAssignCniprAuthor;
import com.smate.center.batch.model.rol.pub.PubAssignCniprDept;
import com.smate.center.batch.model.rol.pub.PubAssignCniprKeywords;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;

/**
 * Cnipr成果数据展开，用于指派成果使用.
 * 
 * @author liqinghua
 * 
 */
@Service("pubAssignCniprXmlService")
@Transactional(rollbackFor = Exception.class)
public class PubAssignCniprXmlServiceImpl implements PubAssignCniprXmlService {

  /**
   * 
   */
  private static final long serialVersionUID = 5947078991212006084L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubAssignCniprAuthorDao pubAssignCniprAuthorDao;
  @Autowired
  private PubAssignCniprDeptDao pubAssignCniprDeptDao;
  @Autowired
  private PubAssignCniprKeywordsDao pubAssignCniprKeywordsDao;
  private IPubAssignXmlProcess cnkiPubAssignXmlProcess;

  @Override
  public void extractAssignData(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws ServiceException {

    try {
      if (context.isCniprImport() && !this.pubAssignCniprAuthorDao.isExistsPubAuthor(context.getCurrentPubId())) {
        cnkiPubAssignXmlProcess.start(xmlDocument, context);
      }
    } catch (Exception e) {
      logger.error("导入CNIPR成果展开指派数据", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean isExtractAssignData(Long pubId) throws ServiceException {

    if (this.pubAssignCniprAuthorDao.isExistsPubAuthor(pubId)) {
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
      PubAssignCniprAuthor pubAuth = new PubAssignCniprAuthor(pubId, pubInsId, name, insId, seqNo);
      this.pubAssignCniprAuthorDao.save(pubAuth);
    } catch (Exception e) {
      logger.error("保存导入CNIPR成果展开作者数据", e);
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

      PubAssignCniprDept pubDept = new PubAssignCniprDept(dept, pubId, pubInsId, seqNo, insId);
      pubAssignCniprDeptDao.save(pubDept);
    } catch (Exception e) {
      logger.error("保存CNIPR成果部门信息", e);
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
      PubAssignCniprKeywords pubKeywords = new PubAssignCniprKeywords(pubId, keywords, keyHash, pubInsId);
      this.pubAssignCniprKeywordsDao.save(pubKeywords);
    } catch (Exception e) {
      logger.error("保存CNIPR成果关键词", e);
      throw new ServiceException(e);
    }
  }

  public void setCniprPubAssignXmlProcess(IPubAssignXmlProcess cnkiPubAssignXmlProcess) {
    this.cnkiPubAssignXmlProcess = cnkiPubAssignXmlProcess;
  }

}
