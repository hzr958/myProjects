package com.smate.center.batch.service.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.mail.emailsrv.MailPromoteStatusDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.mail.emailsrv.MailPromoteStatus;

/**
 * 
 * 推广邮件状态服务类
 * 
 * @author zk
 * 
 */
@Service("mailPromoteStatusService")
@Transactional(rollbackFor = Exception.class)
public class MailPromoteStatusServiceImpl implements MailPromoteStatusService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private MailPromoteStatusDao mailPromoteStatusDao;

  /**
   * 获取推广邮件状态信息
   */
  @Override
  public MailPromoteStatus getMailStatusByTempCode(Integer tempCode) throws ServiceException {
    try {
      return mailPromoteStatusDao.getMailStatusByTempCode(tempCode);
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveMailPromoteStatus(MailPromoteStatus mailStatus) throws ServiceException {
    mailPromoteStatusDao.save(mailStatus);
  }
}
