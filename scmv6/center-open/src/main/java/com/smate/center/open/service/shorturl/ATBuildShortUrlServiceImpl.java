package com.smate.center.open.service.shorturl;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.model.shorturl.OpenShortUrl;

/**
 * 个人成果类型 A ( 32位 长短地址)
 * 
 * @author tsz
 *
 */
@Transactional(rollbackFor = Exception.class)
public class ATBuildShortUrlServiceImpl extends BuildShortUrlBase implements BuildShortUrlService {

  private static Integer HAS_EXPIRATION_TIME = 0;// 0长期 ， 1临时
  private static Integer HAS_TIMES_LIMIT = 0; // 是否有次数限制 。 0没有 ， 1有
  private static Integer TIMES_USED = 0; // 已经使用的次数
  private static Integer TIMES_LIMIT = 0; // 可使用次数
  // private static Date EXPIRATION_TIME; // 过期时间

  @Override
  public String buildShortUrl(Map<String, Object> paramet) {

    return super.getShortUrl32();
  }

  @Override
  public void buildShortUrlParam(OpenShortUrl shortUrl) {
    shortUrl.setExpirationTime(null);
    shortUrl.setHasExpirationTime(HAS_EXPIRATION_TIME);
    shortUrl.setTimesLimit(TIMES_LIMIT);
    shortUrl.setTimesUsed(TIMES_USED);
    shortUrl.setHasTimesLimit(HAS_TIMES_LIMIT);
  }

}
