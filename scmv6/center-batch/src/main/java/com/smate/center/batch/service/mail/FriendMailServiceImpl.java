package com.smate.center.batch.service.mail;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.prj.FriendDao;
import com.smate.center.batch.dao.sns.psn.PsnKnowCopartnerDao;
import com.smate.center.batch.dao.sns.psn.PsnKnowWorkEduDao;
import com.smate.center.batch.dao.sns.pub.ConstDisciplineDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.psn.PsnKnowCopartner;
import com.smate.center.batch.model.psn.PsnKnowWorkEdu;
import com.smate.center.batch.service.emailsimplify.EmailSimplify;
import com.smate.center.batch.service.psn.PersonManager;
import com.smate.core.base.psn.dao.PersonProfileDao;

/**
 * 好友相关的邮件发送业务逻辑实现类.
 * 
 * @author maojianguo
 * 
 */
@Service("friendMailService")
@Transactional(rollbackFor = Exception.class)
public class FriendMailServiceImpl implements FriendMailService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private ConstDisciplineDao constDisciplineDao;
  @Autowired
  private PsnKnowWorkEduDao psnKnowWorkEduDao;
  @Autowired
  private FriendDao friendDao;
  @Autowired
  private PsnKnowCopartnerDao psnKnowCopartnerDao;

  @Autowired
  private PersonManager personManager;

  @Autowired
  private EmailSimplify addFriendEmailService;

  @Override
  public List<PsnKnowWorkEdu> getPsnKnowWorkEdu(Long psnId) throws ServiceException {
    try {
      return psnKnowWorkEduDao.getPsnKnowWorkEdu(psnId);
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public void deletePsnKnowWorkEdu(PsnKnowWorkEdu psnKnowWorkEdu) throws ServiceException {
    psnKnowWorkEduDao.delete(psnKnowWorkEdu);
  }

  @Override
  public List<PsnKnowCopartner> getPsnKnowCopartner(Long psnId) throws ServiceException {
    try {
      return psnKnowCopartnerDao.getPsnKnowCopartner(psnId);
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public void deletePsnKnowCopartner(PsnKnowCopartner psnKnowCopartner) throws ServiceException {
    psnKnowCopartnerDao.delete(psnKnowCopartner);
  }

  /**
   * 好友邀请邮件
   */
  @Override
  public void sendReqAddFrdMail(Map<String, ?> ctxMap, Long mailType) throws ServiceException {

    addFriendEmailService.syncEmailInfo(ctxMap);
  }

}
