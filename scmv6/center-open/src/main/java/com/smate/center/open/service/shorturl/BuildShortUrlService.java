package com.smate.center.open.service.shorturl;

import java.util.Map;

import com.smate.core.base.utils.model.shorturl.OpenShortUrl;

/**
 * 生成短地址的接口，具体业务具体实现。
 * 
 * @author AiJiangBin
 * 
 */
public interface BuildShortUrlService {

  /**
   * 构建具体的短地址
   * 
   * @return
   */
  public String buildShortUrl(Map<String, Object> paramet);

  /**
   * 构建短地址需要的参数
   * 
   * @param shortUrl
   */
  public void buildShortUrlParam(OpenShortUrl shortUrl);


}
