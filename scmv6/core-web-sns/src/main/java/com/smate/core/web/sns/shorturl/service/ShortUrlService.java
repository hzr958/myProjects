package com.smate.core.web.sns.shorturl.service;

/**
 * 短地址服务
 * 
 * @author tsz
 *
 */
public interface ShortUrlService {

  /**
   * 通过短地址 获取真实地址
   * 
   * @param shortUrl
   * @return
   * @throws Exception
   */
  public String getRealUrlParamet(String shortUrl);

  /**
   * 增加使用次数
   * 
   * @param shourUrl
   */
  public void addUseTimes(String shortUrl);
}
