package com.smate.center.batch.service.rol.pubassign;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.chain.pubassign.process.IPubAssignXmlProcess;
import com.smate.center.batch.dao.rol.pub.PubAssignPubMedAuthorDao;
import com.smate.center.batch.dao.rol.pub.PubAssignPubMedConferenceDao;
import com.smate.center.batch.dao.rol.pub.PubAssignPubMedDeptDao;
import com.smate.center.batch.dao.rol.pub.PubAssignPubMedEmailDao;
import com.smate.center.batch.dao.rol.pub.PubAssignPubMedJournalDao;
import com.smate.center.batch.dao.rol.pub.PubAssignPubMedKeywordsDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubAssignPubMedAuthor;
import com.smate.center.batch.model.rol.pub.PubAssignPubMedConference;
import com.smate.center.batch.model.rol.pub.PubAssignPubMedDept;
import com.smate.center.batch.model.rol.pub.PubAssignPubMedEmail;
import com.smate.center.batch.model.rol.pub.PubAssignPubMedJournal;
import com.smate.center.batch.model.rol.pub.PubAssignPubMedKeywords;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;

/**
 * pubmed成果数据展开，用于指派成果使用.
 * 
 * @author liqinghua
 * 
 */
@Service("pubAssignPubMedXmlService")
@Transactional(rollbackFor = Exception.class)
public class PubAssignPubMedXmlServiceImpl implements PubAssignPubMedXmlService {

  /**
   * 
   */
  private static final long serialVersionUID = -4320722914779558925L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubAssignPubMedAuthorDao pubAssignPubMedAuthorDao;
  @Autowired
  private PubAssignPubMedConferenceDao pubAssignPubMedConferenceDao;
  @Autowired
  private PubAssignPubMedDeptDao pubAssignPubMedDeptDao;
  @Autowired
  private PubAssignPubMedJournalDao pubAssignPubMedJournalDao;
  @Autowired
  private PubAssignPubMedKeywordsDao pubAssignPubMedKeywordsDao;
  @Autowired
  private PubAssignPubMedEmailDao pubAssignPubMedEmailDao;
  private IPubAssignXmlProcess pubMedPubAssignXmlProcess;

  @Override
  public void extractAssignData(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws ServiceException {

    try {
      if (context.isPubMedImport() && !this.pubAssignPubMedAuthorDao.isExistsPubAuthor(context.getCurrentPubId())) {
        pubMedPubAssignXmlProcess.start(xmlDocument, context);
      }
    } catch (Exception e) {
      logger.error("导入成果展开指派数据", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean isExtractAssignData(Long pubId) throws ServiceException {

    if (this.pubAssignPubMedAuthorDao.isExistsPubAuthor(pubId)) {
      return true;
    }
    return false;
  }

  @Override
  public void savePubAuthor(Long pubId, Long pubInsId, String initName, String fullName, Long insId, Integer seqNo)
      throws ServiceException {

    if (StringUtils.isBlank(initName)) {
      return;
    }
    try {
      initName = StringUtils.substring(initName.trim().toLowerCase(), 0, 100);
      String prefixName = initName;
      if (initName.indexOf(" ") > 0) {
        prefixName = initName.substring(0, initName.indexOf(" ") + 2);
      }
      fullName = StringUtils.substring(StringUtils.isBlank(fullName) ? null : fullName.trim().toLowerCase(), 0, 100);
      PubAssignPubMedAuthor pubAuth =
          new PubAssignPubMedAuthor(pubId, pubInsId, prefixName, initName, fullName, insId, seqNo);
      this.pubAssignPubMedAuthorDao.save(pubAuth);
    } catch (Exception e) {
      logger.error("保存导入成果展开作者数据", e);
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
      // 查重
      PubAssignPubMedConference pubconf = new PubAssignPubMedConference(name, name.hashCode(), pubId, pubInsId);
      pubconf.setName(name);
      pubconf.setNameHash(name.hashCode());
      pubconf.setPubInsId(pubInsId);
      this.pubAssignPubMedConferenceDao.save(pubconf);
    } catch (Exception e) {
      logger.error("保存导入成果会议数据", e);
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

      PubAssignPubMedDept pubDept = new PubAssignPubMedDept(dept, pubId, pubInsId, seqNo, insId);
      pubAssignPubMedDeptDao.save(pubDept);
    } catch (Exception e) {
      logger.error("保存成果部门信息", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void savePubJournal(Long jid, String jname, String issn, Long pubId, Long pubInsId) throws ServiceException {

    if (jid == null) {
      return;
    }
    try {
      PubAssignPubMedJournal pubJournal = new PubAssignPubMedJournal(jid, StringUtils.substring(jname, 0, 200),
          StringUtils.substring(issn, 0, 100), pubId, pubInsId);
      this.pubAssignPubMedJournalDao.save(pubJournal);
    } catch (Exception e) {
      logger.error("保存成果期刊信息", e);
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
      PubAssignPubMedKeywords pubKeywords = new PubAssignPubMedKeywords(pubId, keywords, keyHash, pubInsId);
      this.pubAssignPubMedKeywordsDao.save(pubKeywords);
    } catch (Exception e) {
      logger.error("保存成果关键词", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void savePubEmail(String email, Long pubId, Long pubInsId, Long insId, Integer seqNo) throws ServiceException {

    if (StringUtils.isBlank(email)) {
      return;
    }
    try {
      email = StringUtils.substring(email.trim().toLowerCase(), 0, 100);
      PubAssignPubMedEmail pubEmail = new PubAssignPubMedEmail(pubId, pubInsId, email, insId, seqNo);
      this.pubAssignPubMedEmailDao.save(pubEmail);
    } catch (Exception e) {
      logger.error("保存成果作者EMAIL", e);
      throw new ServiceException(e);
    }

  }

  public void setPubMedPubAssignXmlProcess(IPubAssignXmlProcess pubMedPubAssignXmlProcess) {
    this.pubMedPubAssignXmlProcess = pubMedPubAssignXmlProcess;
  }

}
