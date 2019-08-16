package com.smate.core.web.sns.shorturl.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.shorturl.OpenShortUrlDao;
import com.smate.core.base.utils.model.shorturl.OpenShortUrl;

/**
 * 短地址服务实现
 * 
 * @author tsz
 *
 */
@Service("shortUrlService")
@Transactional(rollbackFor = Exception.class)
public class ShortUrlServiceImpl implements ShortUrlService {

  @Autowired
  private OpenShortUrlDao openShortUrlDao;

  @Override
  public String getRealUrlParamet(String shortUrl) {
    OpenShortUrl openShortUrl = null;
    if (StringUtils.isNotBlank(shortUrl)) {
      openShortUrl = openShortUrlDao.getOpenShortUrlByShortUrl(shortUrl);
    }
    if (isUsable(openShortUrl)) {
      // 增加使用次数
      openShortUrl.setTimesUsed(openShortUrl.getTimesUsed() + 1);
      openShortUrlDao.save(openShortUrl);
      return openShortUrl.getRealUrlParamet();
    } else {
      return null;
    }
  }

  /**
   * 效验短地址是否可以使用
   * 
   * @param o
   * @return
   */
  private boolean isUsable(OpenShortUrl o) {
    if (o != null) {
      if (o.getHasExpirationTime() == 0) {
        return true;
      }
      boolean outTime = o.getExpirationTime() == null ? true : o.getExpirationTime().getTime() > new Date().getTime();
      if (o.getHasTimesLimit() == 0 && outTime) {
        return true;
      }
      if (o.getHasTimesLimit() == 1 && o.getTimesLimit() > o.getTimesUsed() && outTime) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void addUseTimes(String shortUrl) {

  }

}
