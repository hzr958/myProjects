package com.smate.center.mail.client.utils;

import org.apache.commons.lang3.StringUtils;

public class MailClientUtils {

  /**
   * 环境变量配置客户端名字
   */
  public static final String CLIENT_NAME = "CLIENT_NAME";

  /**
   * 获取 客户端名字方法
   * 
   * @return
   * @throws Exception
   */
  public static String getClientName() throws Exception {
    String clientName = System.getenv(CLIENT_NAME);
    if (StringUtils.isBlank(clientName)) {
      throw new Exception("请设置客户端名字!!!");
    }
    return clientName;
  }
}
