package com.smate.center.batch.service.rol.pubassign;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.chain.pubassign.process.IPubAssignXmlProcess;
import com.smate.center.batch.dao.rol.pub.PubAssignCnkiAuthorDao;
import com.smate.center.batch.dao.rol.pub.PubAssignCnkiConferenceDao;
import com.smate.center.batch.dao.rol.pub.PubAssignCnkiDeptDao;
import com.smate.center.batch.dao.rol.pub.PubAssignCnkiJournalDao;
import com.smate.center.batch.dao.rol.pub.PubAssignCnkiKeywordsDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubAssignCnkiAuthor;
import com.smate.center.batch.model.rol.pub.PubAssignCnkiConference;
import com.smate.center.batch.model.rol.pub.PubAssignCnkiDept;
import com.smate.center.batch.model.rol.pub.PubAssignCnkiJournal;
import com.smate.center.batch.model.rol.pub.PubAssignCnkiKeywords;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;



/**
 * Cnki成果数据展开，用于指派成果使用.
 * 
 * @author liqinghua
 * 
 */
@Service("pubAssignCnkiXmlService")
@Transactional(rollbackFor = Exception.class)
public class PubAssignCnkiXmlServiceImpl implements PubAssignCnkiXmlService {

  /**
   * 
   */
  private static final long serialVersionUID = 1915173422657325936L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubAssignCnkiAuthorDao pubAssignCnkiAuthorDao;
  @Autowired
  private PubAssignCnkiConferenceDao pubAssignCnkiConferenceDao;
  @Autowired
  private PubAssignCnkiDeptDao pubAssignCnkiDeptDao;
  @Autowired
  private PubAssignCnkiJournalDao pubAssignCnkiJournalDao;
  @Autowired
  private PubAssignCnkiKeywordsDao pubAssignCnkiKeywordsDao;
  private IPubAssignXmlProcess cnkiPubAssignXmlProcess;

  @Override
  public void extractAssignData(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws ServiceException {

    try {
      if (context.isCnkiImport() && !this.pubAssignCnkiAuthorDao.isExistsPubAuthor(context.getCurrentPubId())) {
        cnkiPubAssignXmlProcess.start(xmlDocument, context);
      }
    } catch (Exception e) {
      logger.error("导入CNKI成果展开指派数据", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean isExtractAssignData(Long pubId) throws ServiceException {

    if (this.pubAssignCnkiAuthorDao.isExistsPubAuthor(pubId)) {
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
      PubAssignCnkiAuthor pubAuth = new PubAssignCnkiAuthor(pubId, pubInsId, name, insId, seqNo);
      this.pubAssignCnkiAuthorDao.save(pubAuth);
    } catch (Exception e) {
      logger.error("保存导入CNKI成果展开作者数据", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void savePubConference(String name, Long pubId, Long pubInsId) throws ServiceException {

    if (StringUtils.isBlank(name)) {
      return;
    }
    try {
      name = StringUtils.substring(name.trim().toLowerCase(), 0, 200);
      PubAssignCnkiConference pubconf = new PubAssignCnkiConference(name, name.hashCode(), pubId, pubInsId);
      pubconf.setName(name);
      pubconf.setNameHash(name.hashCode());
      pubconf.setPubInsId(pubInsId);
      this.pubAssignCnkiConferenceDao.save(pubconf);
    } catch (Exception e) {
      logger.error("保存导入CNKI成果会议数据", e);
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

      PubAssignCnkiDept pubDept = new PubAssignCnkiDept(dept, pubId, pubInsId, seqNo, insId);
      pubAssignCnkiDeptDao.save(pubDept);
    } catch (Exception e) {
      logger.error("保存CNKI成果部门信息", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void savePubJournal(Long jid, String jname, String issn, Long pubId, Long pubInsId) throws ServiceException {

    if (jid == null) {
      return;
    }
    try {
      PubAssignCnkiJournal pubJournal = new PubAssignCnkiJournal(jid, StringUtils.substring(jname, 0, 200),
          StringUtils.substring(issn, 0, 100), pubId, pubInsId);
      this.pubAssignCnkiJournalDao.save(pubJournal);
    } catch (Exception e) {
      logger.error("保存CNKI成果期刊信息", e);
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
      PubAssignCnkiKeywords pubKeywords = new PubAssignCnkiKeywords(pubId, keywords, keyHash, pubInsId);
      this.pubAssignCnkiKeywordsDao.save(pubKeywords);
    } catch (Exception e) {
      logger.error("保存CNKI成果关键词", e);
      throw new ServiceException(e);
    }
  }

  public void setCnkiPubAssignXmlProcess(IPubAssignXmlProcess cnkiPubAssignXmlProcess) {
    this.cnkiPubAssignXmlProcess = cnkiPubAssignXmlProcess;
  }

}
