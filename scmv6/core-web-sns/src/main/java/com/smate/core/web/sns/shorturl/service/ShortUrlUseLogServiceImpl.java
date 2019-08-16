package com.smate.core.web.sns.shorturl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.shorturl.OpenShortUrlUseLogDao;
import com.smate.core.base.utils.model.shorturl.OpenShortUrlUseLog;

/**
 * 短地址日志服务实现
 * 
 * @author tsz
 *
 */
@Service("shortUrlUseLogService")
@Transactional(rollbackFor = Exception.class)
public class ShortUrlUseLogServiceImpl implements ShortUrlUseLogService {

  @Autowired
  private OpenShortUrlUseLogDao openShortUrlUseLogDao;

  @Override
  public void addUseLog(OpenShortUrlUseLog log) {
    openShortUrlUseLogDao.save(log);
  }


}
