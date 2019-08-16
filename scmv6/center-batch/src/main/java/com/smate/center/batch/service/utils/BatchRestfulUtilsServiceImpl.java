package com.smate.center.batch.service.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 
 * BatchTask-restful工具类
 * 
 *
 *
 * @since 6.0.1
 */
@Service("inspgRestfulUtils")
public class BatchRestfulUtilsServiceImpl implements BatchRestfulUtilsService {

  @Value("${initEmail.restful.url}")
  private String initEmailUrl;

  @Value("${initDyn.restful.url}")
  private String initDynUrl;

  @Value("${realTimeDyn.restful.url}")
  private String initNewDynUrl;

  @Value("${initWeChatMsgSend.restful.url}")
  private String initWeChatMsgSendUrl;

  /**
   * 获取保存邮件数据restful链接
   * 
   * @return
   */
  @Override
  public String getInitEmailRestfulUrl() throws BatchTaskException {
    return this.initEmailUrl;
  }

  public String getInitEmailUrl() {
    return initEmailUrl;
  }

  public void setInitEmailUrl(String initEmailUrl) {
    this.initEmailUrl = initEmailUrl;
  }

  @Override
  public String getInitDynRestfulUrl() throws BatchTaskException {
    return this.initDynUrl;
  }

  public String getInitDynUrl() {
    return initDynUrl;
  }

  public void setInitDynUrl(String initDynUrl) {
    this.initDynUrl = initDynUrl;
  }

  /**
   * 获取WeChat消息发送restful链接
   * 
   * @return String
   */
  @Override
  public String getInitWeChatMsgSendRestfulUrl() throws BatchTaskException {
    return this.initWeChatMsgSendUrl;
  }

  public String getInitWeChatMsgSendUrl() {
    return initWeChatMsgSendUrl;
  }

  public void setInitWeChatMsgSendUrl(String initWeChatMsgSendUrl) {
    this.initWeChatMsgSendUrl = initWeChatMsgSendUrl;
  }

  /**
   * 构造新动态链接
   */
  @Override
  public String getInitNewDynRestfulUrl() throws BatchTaskException {
    return this.initNewDynUrl;
  }

  public String getInitNewDynUrl() {
    return initNewDynUrl;
  }

  public void setInitNewDynUrl(String initNewDynUrl) {
    this.initNewDynUrl = initNewDynUrl;
  }

}

