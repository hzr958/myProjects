package com.smate.center.mail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.mail.dao.MailBlacklistDao;
import com.smate.center.mail.model.MailBlacklist;

/**
 * 黑名单服务
 * 
 * @author tsz
 *
 */
@Service("mailBlacklistService")
@Transactional(rollbackFor = Exception.class)
public class MailBlacklistServiceImpl implements MailBlacklistService {

  @Autowired
  private MailBlacklistDao mailBlacklistDao;

  @Override
  public boolean isExistsBlacklist(String email) {
    MailBlacklist mailBlacklist = mailBlacklistDao.getByEmail(email);
    if (mailBlacklist != null) {
      return true;
    }
    return false;
  }

  @Override
  public boolean isExistsBlacklistHost(String host) {
    MailBlacklist mailBlacklist = mailBlacklistDao.getByEmail(host);
    if (mailBlacklist != null) {
      return true;
    }
    return false;
  }

}
