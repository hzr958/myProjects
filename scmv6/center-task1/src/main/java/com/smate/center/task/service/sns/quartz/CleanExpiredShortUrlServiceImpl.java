package com.smate.center.task.service.sns.quartz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.shorturl.OpenShortUrlDao;

@Service("cleanExpiredShortUrlService")
@Transactional(rollbackFor = Exception.class)
public class CleanExpiredShortUrlServiceImpl implements CleanExpiredShortUrlService {
  @Autowired
  private OpenShortUrlDao openShortUrlDao;

  @Override
  public List<Long> getNeedCleanData() throws Exception {
    return openShortUrlDao.getHasExpiredUrl();
  }

  @Override
  public void handleUrldata(Long id) {
    openShortUrlDao.delete(id);
  }

}
