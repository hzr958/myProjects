package com.smate.center.batch.service.rol.pubassign;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.chain.pubassign.process.IPubAssignXmlProcess;
import com.smate.center.batch.dao.rol.pub.PubAssignAuthorDao;
import com.smate.center.batch.dao.rol.pub.PubAssignConferenceDao;
import com.smate.center.batch.dao.rol.pub.PubAssignDeptDao;
import com.smate.center.batch.dao.rol.pub.PubAssignEmailDao;
import com.smate.center.batch.dao.rol.pub.PubAssignJournalDao;
import com.smate.center.batch.dao.rol.pub.PubAssignKeywordsDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubAssignAuthor;
import com.smate.center.batch.model.rol.pub.PubAssignConference;
import com.smate.center.batch.model.rol.pub.PubAssignDept;
import com.smate.center.batch.model.rol.pub.PubAssignEmail;
import com.smate.center.batch.model.rol.pub.PubAssignJournal;
import com.smate.center.batch.model.rol.pub.PubAssignKeywords;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;

/**
 * 成果数据展开，用于指派成果使用.
 * 
 * @author liqinghua
 * 
 */
@Service("pubAssignXmlService")
@Transactional(rollbackFor = Exception.class)
public class PubAssignXmlServiceImpl implements PubAssignXmlService {

  /**
   * 
   */
  private static final long serialVersionUID = 1915173422657325936L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubAssignAuthorDao pubAssignAuthorDao;
  @Autowired
  private PubAssignConferenceDao pubAssignConferenceDao;
  @Autowired
  private PubAssignDeptDao pubAssignDeptDao;
  @Autowired
  private PubAssignJournalDao pubAssignJournalDao;
  @Autowired
  private PubAssignKeywordsDao pubAssignKeywordsDao;
  @Autowired
  private PubAssignEmailDao pubAssignEmailDao;
  private IPubAssignXmlProcess isiPubAssignXmlProcess;

  @Override
  public void extractAssignData(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws ServiceException {

    try {
      if (context.isIsiImport() && !this.pubAssignAuthorDao.isExistsPubAuthor(context.getCurrentPubId())) {
        isiPubAssignXmlProcess.start(xmlDocument, context);
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
      PubAssignAuthor pubAuth = new PubAssignAuthor(pubId, pubInsId, prefixName, initName, fullName, insId, seqNo);
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
      PubAssignConference pubconf = new PubAssignConference(name, name.hashCode(), pubId, pubInsId);
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

      PubAssignDept pubDept = new PubAssignDept(dept, pubId, pubInsId, seqNo, insId);
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
      PubAssignJournal pubJournal = new PubAssignJournal(jid, StringUtils.substring(jname, 0, 200),
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
      PubAssignKeywords pubKeywords = new PubAssignKeywords(pubId, keywords, keyHash, pubInsId);
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
      PubAssignEmail pubEmail = new PubAssignEmail(pubId, pubInsId, email, insId, seqNo);
      this.pubAssignEmailDao.save(pubEmail);
    } catch (Exception e) {
      logger.error("保存成果作者EMAIL", e);
      throw new ServiceException(e);
    }

  }

  public void setIsiPubAssignXmlProcess(IPubAssignXmlProcess isiPubAssignXmlProcess) {
    this.isiPubAssignXmlProcess = isiPubAssignXmlProcess;
  }

}
