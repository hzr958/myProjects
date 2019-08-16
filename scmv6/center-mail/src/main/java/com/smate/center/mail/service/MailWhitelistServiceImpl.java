package com.smate.center.mail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.mail.dao.MailWhitelistDao;
import com.smate.center.mail.model.MailWhitelist;

/**
 * 白名单服务
 * 
 * @author tsz
 *
 */
@Service("mailWhitelistService")
@Transactional(rollbackFor = Exception.class)
public class MailWhitelistServiceImpl implements MailWhitelistService {

  @Autowired
  private MailWhitelistDao mailWhitelistDao;

  @Override
  public boolean isExistsWhitelist(String email) {
    MailWhitelist mailWhitelist = mailWhitelistDao.getByEmail(email);
    if (mailWhitelist != null) {
      return true;
    }
    return false;
  }

}
