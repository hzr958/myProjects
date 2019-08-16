package com.smate.center.open.service.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 
 * restful工具类
 * 
 * @author zk
 *
 * @since 6.0.1
 */
@Service("openRestfulUtils")
public class OpenRestfulUtils implements OpenRestfulUtilsService {

  @Value("${initEmail.restful.url}")
  private String initEmailUrl;
  @Value("${initDyn.restful.url}")
  private String initDynUrl;


  /**
   * 获取保存邮件数据restful链接
   * 
   * @return
   */
  @Override
  public String getInitEmailRestfulUrl() throws Exception {
    return this.initEmailUrl;
  }

  public String getInitEmailUrl() {
    return initEmailUrl;
  }

  public void setInitEmailUrl(String initEmailUrl) {
    this.initEmailUrl = initEmailUrl;
  }

  @Override
  public String getInitDynRestfulUrl() throws Exception {
    return this.initDynUrl;
  }

  public String getInitDynUrl() {
    return initDynUrl;
  }

  public void setInitDynUrl(String initDynUrl) {
    this.initDynUrl = initDynUrl;
  }


}

