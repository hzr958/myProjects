package com.smate.center.task.service.rol.quartz;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.rol.quartz.PsnPmCoemailDao;
import com.smate.center.task.dao.rol.quartz.PubAssignAuthorDao;
import com.smate.center.task.dao.rol.quartz.PubAssignCniprAuthorDao;
import com.smate.center.task.dao.rol.quartz.PubAssignCnkiAuthorDao;
import com.smate.center.task.dao.rol.quartz.PubAssignCnkiConferenceDao;
import com.smate.center.task.dao.rol.quartz.PubAssignCnkiJournalDao;
import com.smate.center.task.dao.rol.quartz.PubAssignCnkiKeywordsDao;
import com.smate.center.task.dao.rol.quartz.PubAssignCnkiPatAuthorDao;
import com.smate.center.task.dao.rol.quartz.PubAssignConferenceDao;
import com.smate.center.task.dao.rol.quartz.PubAssignEmailDao;
import com.smate.center.task.dao.rol.quartz.PubAssignKeywordsDao;
import com.smate.center.task.dao.rol.quartz.PubAssignPubMedAuthorDao;
import com.smate.center.task.dao.rol.quartz.PubAssignPubMedConferenceDao;
import com.smate.center.task.dao.rol.quartz.PubAssignPubMedEmailDao;
import com.smate.center.task.dao.rol.quartz.PubAssignPubMedJournalDao;
import com.smate.center.task.dao.rol.quartz.PubAssignPubMedKeywordsDao;
import com.smate.center.task.dao.rol.quartz.PubAssignSpsAuthorDao;
import com.smate.center.task.dao.rol.quartz.PubAssignSpsConferenceDao;
import com.smate.center.task.dao.rol.quartz.PubAssignSpsEmailDao;
import com.smate.center.task.dao.rol.quartz.PubAssignSpsJournalDao;
import com.smate.center.task.dao.rol.quartz.PubAssignSpsKeywordsDao;
import com.smate.center.task.dao.rol.quartz.PubRolMemberDao;
import com.smate.center.task.dao.rol.quartz.PublicationRolDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rol.quartz.PsnPmCoemail;
import com.smate.center.task.model.rol.quartz.PubAssignAuthor;
import com.smate.center.task.model.rol.quartz.PubAssignCniprAuthor;
import com.smate.center.task.model.rol.quartz.PubAssignCnkiAuthor;
import com.smate.center.task.model.rol.quartz.PubAssignCnkiConference;
import com.smate.center.task.model.rol.quartz.PubAssignCnkiJournal;
import com.smate.center.task.model.rol.quartz.PubAssignCnkiKeywords;
import com.smate.center.task.model.rol.quartz.PubAssignCnkiPatAuthor;
import com.smate.center.task.model.rol.quartz.PubAssignConference;
import com.smate.center.task.model.rol.quartz.PubAssignKeywords;
import com.smate.center.task.model.rol.quartz.PubAssignPubMedAuthor;
import com.smate.center.task.model.rol.quartz.PubAssignPubMedConference;
import com.smate.center.task.model.rol.quartz.PubAssignPubMedJournal;
import com.smate.center.task.model.rol.quartz.PubAssignPubMedKeywords;
import com.smate.center.task.model.rol.quartz.PubAssignSpsAuthor;
import com.smate.center.task.model.rol.quartz.PubAssignSpsConference;
import com.smate.center.task.model.rol.quartz.PubAssignSpsJournal;
import com.smate.center.task.model.rol.quartz.PubAssignSpsKeywords;
import com.smate.center.task.model.rol.quartz.PublicationRol;
import com.smate.center.task.single.util.pub.ConstPublicationType;
import com.smate.center.task.single.util.pub.PubXmlDbUtils;

/**
 * 用户确认成果，完善用户信息service.
 * 
 * @author liqinghua
 * 
 */
@Service("psnConfirmPmService")
@Transactional(rollbackFor = Exception.class)
public class psnConfirmPmServiceImpl implements PsnConfirmPmService {
  @Autowired
  private PublicationRolDao publicationRolDao;
  @Autowired
  private PubRolMemberDao pubRolMemberDao;
  @Autowired
  private PubAssignAuthorDao pubAssignAuthorDao;
  @Autowired
  private PsnPmService psnPmService;
  @Autowired
  private PubAssignCnkiAuthorDao pubAssignCnkiAuthorDao;
  @Autowired
  private PubAssignCnkiJournalDao pubAssignCnkiJournalDao;
  @Autowired
  private PubAssignCnkiConferenceDao pubAssignCnkiConferenceDao;
  @Autowired
  private PubAssignCnkiKeywordsDao pubAssignCnkiKeywordsDao;
  @Autowired
  private PubAssignEmailDao pubAssignEmailDao;
  @Autowired
  private PsnPmCoemailDao psnPmCoemailDao;
  @Autowired
  private PubAssignPubMedJournalDao pubAssignPubMedJournalDao;
  @Autowired
  private PubAssignConferenceDao pubAssignConferenceDao;
  @Autowired
  private PubAssignKeywordsDao pubAssignKeywordsDao;
  @Autowired
  private PubAssignSpsAuthorDao pubAssignSpsAuthorDao;
  @Autowired
  private PubAssignSpsJournalDao pubAssignSpsJournalDao;
  @Autowired
  private PubAssignSpsEmailDao pubAssignSpsEmailDao;
  @Autowired
  private PubAssignSpsConferenceDao pubAssignSpsConferenceDao;
  @Autowired
  private PubAssignSpsKeywordsDao pubAssignSpsKeywordsDao;
  @Autowired
  private PubAssignCniprAuthorDao pubAssignCniprAuthorDao;
  @Autowired
  private PubAssignCnkiPatAuthorDao pubAssignCnkiPatAuthorDao;
  @Autowired
  private PubAssignPubMedAuthorDao pubAssignPubMedAuthorDao;
  @Autowired
  private PubAssignPubMedEmailDao pubAssignPubMedEmailDao;
  @Autowired
  private PubAssignPubMedConferenceDao pubAssignPubMedConferenceDao;
  @Autowired
  private PubAssignPubMedKeywordsDao pubAssignPubMedKeywordsDao;

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void psnConfirmPm(Long pubId, Long psnId, Long pmId) throws ServiceException {
    PublicationRol pub = publicationRolDao.get(pubId);
    if (pub == null || pub.getSourceDbId() == null) {
      return;
    }
    Integer seqNo = pubRolMemberDao.getPubMemberSeqNo(pubId, pmId);
    // ISI库导入.
    Long dbId = Long.valueOf(pub.getSourceDbId());
    if (PubXmlDbUtils.isIsiDb(dbId)) {
      psnConfirmIsiPm(psnId, pub, seqNo);
    } else if (PubXmlDbUtils.isCnkiDb(dbId)) {// cnki
      psnConfirmCnkiPm(psnId, pub, seqNo);
    } else if (PubXmlDbUtils.isScopusDb(dbId)) {// scopus
      psnConfirmSpsPm(psnId, pub, seqNo);
    } else if (PubXmlDbUtils.isCNIPRDb(dbId)) {// cnipr
      psnConfirmCniprPm(psnId, pub, seqNo);
    } else if (PubXmlDbUtils.isCnkipatDb(dbId)) {// cnkipat
      psnConfirmCnkiPatPm(psnId, pub, seqNo);
    } else if (PubXmlDbUtils.isPubMedDb(dbId)) {// pubmed
      this.psnConfirmPubMedPm(psnId, pub, seqNo);
    }

  }

  private void psnConfirmPubMedPm(Long psnId, PublicationRol pub, Integer seqNo) {

    try {
      // author
      this.psnConfirmPubMedAuth(psnId, pub.getId(), seqNo);
      // coEmail
      this.psnConfirmPubMedEmail(psnId, pub.getId());
      // journal
      this.psnConfirmJournal(psnId, pub);
      // conference
      this.psnConfirmPubMedConference(psnId, pub);
      // keywords
      this.psnConfirmPubMedKeyWord(psnId, pub.getId());
    } catch (ServiceException e) {
      logger.error("用户确认pubmed成果，完善PM数据", e);
      throw e;
    }
  }

  /**
   * 用户确认成果，完善用户关键词.
   * 
   * @param psnId
   * @param pubId
   * @throws ServiceException
   */
  private void psnConfirmPubMedKeyWord(Long psnId, Long pubId) {
    try {
      List<PubAssignPubMedKeywords> list = pubAssignPubMedKeywordsDao.getKwByPubId(pubId);
      if (list != null && list.size() > 0) {
        for (PubAssignPubMedKeywords kw : list) {
          this.psnPmService.savePsnPmKeyWord(kw.getKeywords(), psnId);
        }
      }
    } catch (Exception e) {
      logger.error("用户确认成果，完善用户关键词", e);
      throw new ServiceException(e);
    }
  }

  private void psnConfirmPubMedConference(Long psnId, PublicationRol pub) {
    if (!ConstPublicationType.PUB_CONFERECE_TYPE.equals(pub.getTypeId())) {
      return;
    }
    try {
      PubAssignPubMedConference pc = pubAssignPubMedConferenceDao.getPubAssignPubMedConferenceByPubId(pub.getId());
      if (pc == null) {
        return;
      }
      this.psnPmService.savePsnPmConference(pc.getName(), psnId);
    } catch (Exception e) {
      logger.error("用户确认成果，完善用户会议论文", e);
      throw new ServiceException(e);
    }
  }

  private void psnConfirmPubMedEmail(Long psnId, Long pubId) {
    try {

      // 查找不重复的合作则邮件
      List<String> coEmails = pubAssignPubMedEmailDao.getNotExistsCoEmail(psnId, pubId);
      if (coEmails == null || coEmails.size() == 0) {
        return;
      }
      // 保存和作者邮件
      for (String coEmail : coEmails) {
        PsnPmCoemail pmCoemail = new PsnPmCoemail(coEmail, psnId);
        this.psnPmCoemailDao.save(pmCoemail);
      }
    } catch (Exception e) {
      logger.error("用户确认Cnki成果，完善用户名与合作者", e);
      throw new ServiceException(e);
    }
  }

  private void psnConfirmPubMedAuth(Long psnId, Long pubId, Integer seqNo) {
    try {
      List<PubAssignPubMedAuthor> list = pubAssignPubMedAuthorDao.getPubAuthor(pubId);
      if (list == null || list.size() == 0) {
        return;
      }
      for (PubAssignPubMedAuthor auth : list) {
        // 如果是确认的用户，则完善用户名.
        if (auth.getSeqNo() != null && auth.getSeqNo().equals(seqNo)) {
          String fullName = auth.getFullName();
          String initName = auth.getInitName();
          psnPmService.saveAddtPsnPmIsiName(initName, fullName, psnId);
        } else {// 如果不是确认的用户，则完善合作者.
          psnPmService.savePsnPmIsiConame(auth.getInitName(), auth.getFullName(), psnId);
          // isi的合作者，放入scopus的合作者中
          psnPmService.savePsnPmSpsConame(auth.getInitName(), psnId);
          psnPmService.savePsnPmSpsConame(auth.getFullName(), psnId);
          psnPmService.savePsnPmSpsCoPreName(auth.getPrefixName(), psnId);
        }
      }
    } catch (Exception e) {
      logger.error("用户确认ISI成果，完善用户名与合作者", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 用户确认CnkiPat成果，完善PM数据.
   * 
   * @param pubId
   * @param seqNo
   * @throws ServiceException
   */
  private void psnConfirmCnkiPatPm(Long psnId, PublicationRol pub, Integer seqNo) {
    try {
      // author
      this.psnConfirmCnkiPatAuth(psnId, pub.getId(), seqNo);

    } catch (ServiceException e) {
      logger.error("用户确认CnkiPat成果，完善PM数据", e);
      throw e;
    }
  }

  private void psnConfirmCnkiPatAuth(Long psnId, Long pubId, Integer seqNo) {
    try {
      List<PubAssignCnkiPatAuthor> list = pubAssignCnkiPatAuthorDao.getPubAuthor(pubId);
      if (list == null || list.size() == 0) {
        return;
      }
      for (PubAssignCnkiPatAuthor auth : list) {
        // 如果是确认的用户，则完善用户名.
        if (auth.getSeqNo() != null && auth.getSeqNo().equals(seqNo)) {
          psnPmService.saveAddtPsnPmCnkiName(auth.getName(), psnId);
        } else {// 如果不是确认的用户，则完善合作者.
          psnPmService.savePsnPmCnkiConame(auth.getName(), psnId);
        }
      }
    } catch (Exception e) {
      logger.error("用户确认CnkiPat成果，完善用户名与合作者", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 用户确认Cnipr成果，完善PM数据.
   * 
   * @param pubId
   * @param seqNo
   * @throws ServiceException
   */
  private void psnConfirmCniprPm(Long psnId, PublicationRol pub, Integer seqNo) {
    try {
      // author
      this.psnConfirmCniprAuth(psnId, pub.getId(), seqNo);

    } catch (ServiceException e) {
      logger.error("用户确认Cnipr成果，完善PM数据", e);
      throw e;
    }
  }

  private void psnConfirmCniprAuth(Long psnId, Long pubId, Integer seqNo) {
    try {
      List<PubAssignCniprAuthor> list = pubAssignCniprAuthorDao.getPubAuthor(pubId);
      if (list == null || list.size() == 0) {
        return;
      }
      for (PubAssignCniprAuthor auth : list) {
        // 如果是确认的用户，则完善用户名.
        if (auth.getSeqNo() != null && auth.getSeqNo().equals(seqNo)) {
          psnPmService.saveAddtPsnPmCnkiName(auth.getName(), psnId);
        } else {// 如果不是确认的用户，则完善合作者.
          psnPmService.savePsnPmCnkiConame(auth.getName(), psnId);
        }
      }
    } catch (Exception e) {
      logger.error("用户确认cnipr成果，完善用户名与合作者", e);
      throw new ServiceException(e);
    }
  }

  private void psnConfirmSpsPm(Long psnId, PublicationRol pub, Integer seqNo) {
    try {
      // author
      this.psnConfirmSpsAuth(psnId, pub.getId(), seqNo);
      // coEmail
      this.psnConfirmScopusEmail(psnId, pub.getId());
      // journal
      this.psnConfirmSpsJournal(psnId, pub);
      // conference
      this.psnConfirmSpsConference(psnId, pub);
      // keywords
      this.psnConfirmSpsKeyWord(psnId, pub.getId());
    } catch (ServiceException e) {
      logger.error("用户确认scopus成果，完善PM数据", e);
      throw e;
    }
  }

  private void psnConfirmSpsKeyWord(Long psnId, Long pubId) {
    try {
      List<PubAssignSpsKeywords> list = pubAssignSpsKeywordsDao.getKwByPubId(pubId);
      if (list != null && list.size() > 0) {
        for (PubAssignSpsKeywords kw : list) {
          this.psnPmService.savePsnPmKeyWord(kw.getKeywords(), psnId);
        }
      }
    } catch (Exception e) {
      logger.error("用户确认成果，完善用户关键词", e);
      throw new ServiceException(e);
    }
  }

  private void psnConfirmSpsConference(Long psnId, PublicationRol pub) {
    if (!ConstPublicationType.PUB_CONFERECE_TYPE.equals(pub.getTypeId())) {
      return;
    }
    try {
      PubAssignSpsConference pc = pubAssignSpsConferenceDao.getPubAssignConferenceByPubId(pub.getId());
      if (pc == null) {
        return;
      }
      this.psnPmService.savePsnPmConference(pc.getName(), psnId);
    } catch (Exception e) {
      logger.error("用户确认成果，完善用户会议论文", e);
      throw new ServiceException(e);
    }
  }

  private void psnConfirmSpsJournal(Long psnId, PublicationRol pub) {
    if (!ConstPublicationType.PUB_JOURNAL_TYPE.equals(pub.getTypeId())) {
      return;
    }
    try {
      PubAssignSpsJournal pj = pubAssignSpsJournalDao.getPubAssignJournalByPubId(pub.getId());
      if (pj == null) {
        return;
      }
      this.psnPmService.savePsnPmJournal(pj.getJid(), pj.getJname(), pj.getIssn(), psnId);
    } catch (Exception e) {
      logger.error("用户确认scopus成果，完善用户名期刊", e);
      throw new ServiceException(e);
    }

  }

  /**
   * 用户确认Scopus成果，完善用户名与合作者.
   * 
   * @param psnId
   * @param pubId
   * @param seqNo
   * @throws ServiceException
   */
  private void psnConfirmScopusEmail(Long psnId, Long pubId) {
    try {

      // 查找不重复的合作则邮件
      List<String> coEmails = pubAssignSpsEmailDao.getNotExistsCoEmail(psnId, pubId);
      if (coEmails == null || coEmails.size() == 0) {
        return;
      }
      // 保存和作者邮件
      for (String coEmail : coEmails) {
        PsnPmCoemail pmCoemail = new PsnPmCoemail(coEmail, psnId);
        this.psnPmCoemailDao.save(pmCoemail);
      }
    } catch (Exception e) {
      logger.error("用户确认Scopus成果，完善用户名与合作者", e);
      throw new ServiceException(e);
    }
  }

  private void psnConfirmSpsAuth(Long psnId, Long pubId, Integer seqNo) {
    try {
      List<PubAssignSpsAuthor> list = pubAssignSpsAuthorDao.getPubAuthor(pubId);
      if (list == null || list.size() == 0) {
        return;
      }
      for (PubAssignSpsAuthor auth : list) {
        // 如果是确认的用户，则完善用户名.
        if (auth.getSeqNo() != null && !auth.getSeqNo().equals(seqNo)) {
          psnPmService.savePsnPmSpsConame(auth.getName(), psnId);
          psnPmService.savePsnPmSpsCoPreName(auth.getPrefixName(), psnId);
        }
      }

    } catch (Exception e) {
      logger.error("用户确认SCOPUS成果，完善用户名与合作者", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 用户确认CNKI成果，完善PM数据.
   * 
   * @param pubId
   * @param seqNo
   * @throws ServiceException
   */
  private void psnConfirmCnkiPm(Long psnId, PublicationRol pub, Integer seqNo) {
    try {
      // author
      psnConfirmCnkiAuth(psnId, pub.getId(), seqNo);
      // journal
      psnConfirmCnkiJournal(psnId, pub);
      // conference
      psnConfirmCnkiConference(psnId, pub);
      // keywords
      psnConfirmCnkiKeyWord(psnId, pub.getId());

    } catch (Exception e) {
      logger.error("用户确认ISI成果，完善PM数据", e);
      throw e;

    }

  }

  /**
   * 用户确认Cnki成果，完善用户关键词.
   * 
   * @param psnId
   * @param pubId
   * @throws ServiceException
   */
  private void psnConfirmCnkiKeyWord(Long psnId, Long pubId) {
    try {
      List<PubAssignCnkiKeywords> list = pubAssignCnkiKeywordsDao.getKwByPubId(pubId);
      if (CollectionUtils.isNotEmpty(list)) {
        for (PubAssignCnkiKeywords kw : list) {
          psnPmService.savePsnPmKeyWord(kw.getKeywords(), psnId);
        }
      }
    } catch (Exception e) {
      logger.error("用户确认Cnki成果，完善用户关键词", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 用户确认Cnki成果，完善用户会议论文.
   * 
   * @param psnId
   * @param pub
   * @throws ServiceException
   */
  private void psnConfirmCnkiConference(Long psnId, PublicationRol pub) {
    if (!ConstPublicationType.PUB_CONFERECE_TYPE.equals(pub.getTypeId())) {
      return;
    }
    try {
      PubAssignCnkiConference pc = pubAssignCnkiConferenceDao.getPubAssignConferenceByPubId(pub.getId());
      if (pc == null) {
        return;
      }
      psnPmService.savePsnPmConference(pc.getName(), psnId);
    } catch (Exception e) {
      logger.error("用户确认成果，完善用户会议论文", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 用户确认Cnki成果，完善用户名期刊.
   * 
   * @param psnId
   * @param pub
   * @throws ServiceException
   */
  private void psnConfirmCnkiJournal(Long psnId, PublicationRol pub) {
    if (!ConstPublicationType.PUB_JOURNAL_TYPE.equals(pub.getTypeId())) {
      return;
    }
    try {
      PubAssignCnkiJournal pj = pubAssignCnkiJournalDao.getPubAssignJournalByPubId(pub.getId());
      if (pj == null) {
        return;
      }
      psnPmService.savePsnPmJournal(pj.getJid(), pj.getJname(), pj.getIssn(), psnId);
    } catch (Exception e) {
      logger.error("用户确认Cnki成果，完善用户名期刊", e);
      throw new ServiceException(e);
    }

  }

  /**
   * 用户确认Cnki成果，完善用户名与合作者.
   * 
   * @param psnId
   * @param pubId
   * @param seqNo
   * @throws ServiceException
   */
  private void psnConfirmCnkiAuth(Long psnId, Long pubId, Integer seqNo) throws ServiceException {
    try {
      List<PubAssignCnkiAuthor> list = pubAssignCnkiAuthorDao.getPubAuthor(pubId);
      if (list == null || list.size() == 0) {
        return;
      }
      for (PubAssignCnkiAuthor auth : list) {
        // 如果是确认的用户，则完善用户名.
        if (auth.getSeqNo() != null && auth.getSeqNo().equals(seqNo)) {
          psnPmService.saveAddtPsnPmCnkiName(auth.getName(), psnId);

        } else {// 如果不是确认的用户，则完善合作者.
          psnPmService.savePsnPmCnkiConame(auth.getName(), psnId);
        }
      }
    } catch (Exception e) {
      logger.error("用户确认Cnki成果，完善用户名与合作者", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 用户确认ISI成果，完善PM数据.
   * 
   * @param pubId
   * @param seqNo
   * @throws ServiceException
   */
  private void psnConfirmIsiPm(Long psnId, PublicationRol pub, Integer seqNo) throws ServiceException {

    try {
      // author
      this.psnConfirmIsiAuth(psnId, pub.getId(), seqNo);
      // coEmail
      this.psnConfirmIsiEmail(psnId, pub.getId());
      // journal
      this.psnConfirmJournal(psnId, pub);
      // conference
      this.psnConfirmConference(psnId, pub);
      // keywords
      this.psnConfirmKeyWord(psnId, pub.getId());
    } catch (ServiceException e) {
      logger.error("用户确认ISI成果，完善PM数据", e);
      throw e;
    }
  }

  private void psnConfirmKeyWord(Long psnId, Long pubId) {
    try {
      List<PubAssignKeywords> list = pubAssignKeywordsDao.getKwByPubId(pubId);
      if (list != null && list.size() > 0) {
        for (PubAssignKeywords kw : list) {
          psnPmService.savePsnPmKeyWord(kw.getKeywords(), psnId);
        }
      }
    } catch (Exception e) {
      logger.error("用户确认成果，完善用户关键词", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 用户确认成果，完善用户会议论文.
   * 
   * @param psnId
   * @param pub
   * @throws ServiceException
   */
  private void psnConfirmConference(Long psnId, PublicationRol pub) {
    if (!ConstPublicationType.PUB_CONFERECE_TYPE.equals(pub.getTypeId())) {
      return;
    }
    try {
      PubAssignConference pc = pubAssignConferenceDao.getPubAssignConferenceByPubId(pub.getId());
      if (pc == null) {
        return;
      }
      psnPmService.savePsnPmConference(pc.getName(), psnId);
    } catch (Exception e) {
      logger.error("用户确认成果，完善用户会议论文", e);
      throw new ServiceException(e);
    }

  }

  /**
   * 用户确认成果，完善用户名期刊.
   * 
   * @param psnId
   * @param pub
   * @throws ServiceException
   */
  private void psnConfirmJournal(Long psnId, PublicationRol pub) {

    if (!ConstPublicationType.PUB_JOURNAL_TYPE.equals(pub.getTypeId())) {
      return;
    }
    try {
      PubAssignPubMedJournal pj = pubAssignPubMedJournalDao.getPubAssignPubMedJournalByPubId(pub.getId());
      if (pj == null) {
        return;
      }
      psnPmService.savePsnPmJournal(pj.getJid(), pj.getJname(), pj.getIssn(), psnId);
    } catch (Exception e) {

    }
  }

  /**
   * 用户确认ISI成果，完善用户名与合作者.
   * 
   * @param psnId
   * @param pubId
   * @param seqNo
   * @throws ServiceException
   */
  private void psnConfirmIsiEmail(Long psnId, Long pubId) throws ServiceException {

    try {
      // 查找不重复的合作则邮件
      List<String> coEmails = pubAssignEmailDao.getNotExistsCoEmail(psnId, pubId);
      if (CollectionUtils.isEmpty(coEmails)) {
        return;
      }
      for (String coEmail : coEmails) {
        PsnPmCoemail pmCoemail = new PsnPmCoemail(coEmail, psnId);
        psnPmCoemailDao.save(pmCoemail);
      }

    } catch (Exception e) {
      logger.error("用户确认Cnki成果，完善用户名与合作者", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 用户确认ISI成果，完善用户名与合作者.
   * 
   * @param psnId
   * @param pubId
   * @param seqNo
   * @throws ServiceException
   */

  private void psnConfirmIsiAuth(Long psnId, Long pubId, Integer seqNo) throws ServiceException {
    List<PubAssignAuthor> pubAssignAuthorList = pubAssignAuthorDao.getPubAuthor(pubId);
    if (pubAssignAuthorList == null || pubAssignAuthorList.size() == 0) {
      return;
    }
    for (PubAssignAuthor auth : pubAssignAuthorList) {
      // 如果是确认的用户，则完善用户名.
      if (auth.getSeqNo() != null && auth.getSeqNo().equals(seqNo)) {
        String fullName = auth.getFullName();
        String initName = auth.getInitName();
        psnPmService.saveAddtPsnPmIsiName(initName, fullName, psnId);
      } else {// 如果不是确认的用户，则完善合作者.
        psnPmService.savePsnPmIsiConame(auth.getInitName(), auth.getFullName(), psnId);
        // isi的合作者，放入scopus的合作者中
        psnPmService.savePsnPmSpsConame(auth.getInitName(), psnId);
        psnPmService.savePsnPmSpsConame(auth.getFullName(), psnId);
        psnPmService.savePsnPmSpsCoPreName(auth.getPrefixName(), psnId);

      }

    }

  }

}
