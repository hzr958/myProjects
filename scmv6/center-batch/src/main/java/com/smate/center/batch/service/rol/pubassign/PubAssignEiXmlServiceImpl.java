package com.smate.center.batch.service.rol.pubassign;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.chain.pubassign.process.IPubAssignXmlProcess;
import com.smate.center.batch.dao.rol.pub.PubAssignEiAuthorDao;
import com.smate.center.batch.dao.rol.pub.PubAssignEiConferenceDao;
import com.smate.center.batch.dao.rol.pub.PubAssignEiDeptDao;
import com.smate.center.batch.dao.rol.pub.PubAssignEiEmailDao;
import com.smate.center.batch.dao.rol.pub.PubAssignEiJournalDao;
import com.smate.center.batch.dao.rol.pub.PubAssignEiKeywordsDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubAssignAuthor;
import com.smate.center.batch.model.rol.pub.PubAssignEiAuthor;
import com.smate.center.batch.model.rol.pub.PubAssignEiConference;
import com.smate.center.batch.model.rol.pub.PubAssignEiDept;
import com.smate.center.batch.model.rol.pub.PubAssignEiEmail;
import com.smate.center.batch.model.rol.pub.PubAssignEiJournal;
import com.smate.center.batch.model.rol.pub.PubAssignEiKeywords;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;

/**
 * 成果数据展开，用于指派成果使用.
 * 
 * @author liqinghua
 * 
 */
@Service("pubAssignEiXmlService")
@Transactional(rollbackFor = Exception.class)
public class PubAssignEiXmlServiceImpl implements PubAssignEiXmlService {


  /**
   * 
   */
  private static final long serialVersionUID = -7479526876666540780L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubAssignEiAuthorDao pubAssignAuthorDao;
  @Autowired
  private PubAssignEiConferenceDao pubAssignConferenceDao;
  @Autowired
  private PubAssignEiDeptDao pubAssignDeptDao;
  @Autowired
  private PubAssignEiJournalDao pubAssignJournalDao;
  @Autowired
  private PubAssignEiKeywordsDao pubAssignKeywordsDao;
  @Autowired
  private PubAssignEiEmailDao pubAssignEmailDao;
  private IPubAssignXmlProcess eiPubAssignXmlProcess;

  public IPubAssignXmlProcess getEiPubAssignXmlProcess() {
    return eiPubAssignXmlProcess;
  }

  public void setEiPubAssignXmlProcess(IPubAssignXmlProcess eiPubAssignXmlProcess) {
    this.eiPubAssignXmlProcess = eiPubAssignXmlProcess;
  }

  @Override
  public void extractAssignData(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws ServiceException {

    try {
      if (context.isEiImport() && !this.pubAssignAuthorDao.isExistsPubAuthor(context.getCurrentPubId())) {
        eiPubAssignXmlProcess.start(xmlDocument, context);
      }
    } catch (Exception e) {
      logger.error("导入成果展开指派数据", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean isExtractAssignData(Long pubId) throws ServiceException {

    if (this.pubAssignAuthorDao.isExistsPubAuthor(pubId)) {
      return true;
    }
    return false;
  }

  @Override
  public void savePubAuthor(Long pubId, Long pubInsId, String initName, String fullName, Long insId, Integer seqNo)
      throws ServiceException {

    if (StringUtils.isBlank(fullName)) {
      return;
    }
    try {
      fullName = StringUtils.substring(fullName.trim().toLowerCase(), 0, 100);
      String prefixName = fullName;
      if (fullName.indexOf(" ") > 0) {
        prefixName = fullName.substring(0, fullName.indexOf(" ") + 2);
      }
      // initName = StringUtils.substring(StringUtils.isBlank(initName) ? null :
      // initName.trim().toLowerCase(), 0,
      // 100);
      PubAssignEiAuthor pubAuth = new PubAssignEiAuthor(pubId, pubInsId, prefixName, initName, fullName, insId, seqNo);
      this.pubAssignAuthorDao.save(pubAuth);
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
      PubAssignEiConference pubconf = new PubAssignEiConference(name, name.hashCode(), pubId, pubInsId);
      pubconf.setName(name);
      pubconf.setNameHash(name.hashCode());
      pubconf.setPubInsId(pubInsId);
      this.pubAssignConferenceDao.save(pubconf);
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

      PubAssignEiDept pubDept = new PubAssignEiDept(dept, pubId, pubInsId, seqNo, insId);
      pubAssignDeptDao.save(pubDept);
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
      PubAssignEiJournal pubJournal = new PubAssignEiJournal(jid, StringUtils.substring(jname, 0, 200),
          StringUtils.substring(issn, 0, 100), pubId, pubInsId);
      this.pubAssignJournalDao.save(pubJournal);
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
      PubAssignEiKeywords pubKeywords = new PubAssignEiKeywords(pubId, keywords, keyHash, pubInsId);
      this.pubAssignKeywordsDao.save(pubKeywords);
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
      PubAssignEiEmail pubEmail = new PubAssignEiEmail(pubId, pubInsId, email, insId, seqNo);
      this.pubAssignEmailDao.save(pubEmail);
    } catch (Exception e) {
      logger.error("保存成果作者EMAIL", e);
      throw new ServiceException(e);
    }

  }



}
