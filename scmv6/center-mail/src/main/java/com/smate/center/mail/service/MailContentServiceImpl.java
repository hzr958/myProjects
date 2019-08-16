package com.smate.center.mail.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.mail.connector.dao.MailContentDao;
import com.smate.center.mail.connector.mongodb.model.MailContent;

/**
 * 邮件内容记录 服务实现
 * 
 * @author yhx
 *
 */
@Service("mailContentService")
@Transactional(rollbackFor = Exception.class)
public class MailContentServiceImpl implements MailContentService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private MailContentDao mailContentDao;

  @Override
  public MailContent getMailContentByMail(Long mailId) {
    return mailContentDao.findById(mailId);
  }


}
