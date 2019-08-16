package com.smate.center.batch.service.rol.pubassign;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.chain.pubassign.process.IPubAssignXmlProcess;
import com.smate.center.batch.dao.rol.pub.PubAssignSpsAuthorDao;
import com.smate.center.batch.dao.rol.pub.PubAssignSpsConferenceDao;
import com.smate.center.batch.dao.rol.pub.PubAssignSpsDeptDao;
import com.smate.center.batch.dao.rol.pub.PubAssignSpsEmailDao;
import com.smate.center.batch.dao.rol.pub.PubAssignSpsJournalDao;
import com.smate.center.batch.dao.rol.pub.PubAssignSpsKeywordsDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubAssignSpsAuthor;
import com.smate.center.batch.model.rol.pub.PubAssignSpsConference;
import com.smate.center.batch.model.rol.pub.PubAssignSpsDept;
import com.smate.center.batch.model.rol.pub.PubAssignSpsEmail;
import com.smate.center.batch.model.rol.pub.PubAssignSpsJournal;
import com.smate.center.batch.model.rol.pub.PubAssignSpsKeywords;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;

/**
 * scopus成果数据展开，用于指派成果使用.
 * 
 * @author liqinghua
 * 
 */
@Service("pubAssignSpsXmlService")
@Transactional(rollbackFor = Exception.class)
public class PubAssignSpsXmlServiceImpl implements PubAssignSpsXmlService {

  /**
   * 
   */
  private static final long serialVersionUID = 585467784227643142L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubAssignSpsAuthorDao pubAssignSpsAuthorDao;
  @Autowired
  private PubAssignSpsConferenceDao pubAssignSpsConferenceDao;
  @Autowired
  private PubAssignSpsDeptDao pubAssignSpsDeptDao;
  @Autowired
  private PubAssignSpsJournalDao pubAssignSpsJournalDao;
  @Autowired
  private PubAssignSpsKeywordsDao pubAssignSpsKeywordsDao;
  @Autowired
  private PubAssignSpsEmailDao pubAssignSpsEmailDao;
  private IPubAssignXmlProcess spsPubAssignXmlProcess;

  @Override
  public void extractAssignData(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws ServiceException {

    try {
      if (context.isScopusImport() && !this.pubAssignSpsAuthorDao.isExistsPubAuthor(context.getCurrentPubId())) {
        spsPubAssignXmlProcess.start(xmlDocument, context);
      }
    } catch (Exception e) {
      logger.error("导入成果展开指派数据", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean isExtractAssignData(Long pubId) throws ServiceException {

    if (this.pubAssignSpsAuthorDao.isExistsPubAuthor(pubId)) {
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
      name = StringUtils.substring(StringUtils.isBlank(name) ? null : name.trim().toLowerCase(), 0, 100);
      Integer type = 0;
      int blankIndex = name.indexOf(" ");
      if (blankIndex > 0 && (name.length() - blankIndex > 2)) {
        type = 1;
      }
      String prefixName = name;
      if (name.indexOf(" ") > 0) {
        prefixName = name.substring(0, name.indexOf(" ") + 2);
      }
      PubAssignSpsAuthor pubAuth = new PubAssignSpsAuthor(pubId, pubInsId, prefixName, name, insId, seqNo, type);
      this.pubAssignSpsAuthorDao.save(pubAuth);
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
      PubAssignSpsConference pubconf = new PubAssignSpsConference(name, name.hashCode(), pubId, pubInsId);
      pubconf.setName(name);
      pubconf.setNameHash(name.hashCode());
      pubconf.setPubInsId(pubInsId);
      this.pubAssignSpsConferenceDao.save(pubconf);
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

      PubAssignSpsDept pubDept = new PubAssignSpsDept(dept, pubId, pubInsId, seqNo, insId);
      pubAssignSpsDeptDao.save(pubDept);
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
      PubAssignSpsJournal pubJournal = new PubAssignSpsJournal(jid, StringUtils.substring(jname, 0, 200),
          StringUtils.substring(issn, 0, 100), pubId, pubInsId);
      this.pubAssignSpsJournalDao.save(pubJournal);
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
      PubAssignSpsKeywords pubKeywords = new PubAssignSpsKeywords(pubId, keywords, keyHash, pubInsId);
      this.pubAssignSpsKeywordsDao.save(pubKeywords);
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
      PubAssignSpsEmail pubEmail = new PubAssignSpsEmail(pubId, pubInsId, email, insId, seqNo);
      this.pubAssignSpsEmailDao.save(pubEmail);
    } catch (Exception e) {
      logger.error("保存成果作者EMAIL", e);
      throw new ServiceException(e);
    }

  }

  public void setSpsPubAssignXmlProcess(IPubAssignXmlProcess spsPubAssignXmlProcess) {
    this.spsPubAssignXmlProcess = spsPubAssignXmlProcess;
  }

}
