package com.smate.center.batch.service.utils;

import com.smate.core.base.utils.exception.BatchTaskException;


public interface BatchRestfulUtilsService {

  /**
   * 获取保存邮件数据restful链接
   * 
   * @return
   */
  String getInitEmailRestfulUrl() throws BatchTaskException;

  /**
   * 获取保存保存生成动态restful链接
   * 
   * @return
   */
  String getInitDynRestfulUrl() throws BatchTaskException;

  /**
   * 获取保存保存生成动态restful链接
   * 
   * @return
   */
  String getInitNewDynRestfulUrl() throws BatchTaskException;

  /**
   * 获取WeChat消息发送restful链接
   * 
   * @return String
   */
  String getInitWeChatMsgSendRestfulUrl() throws BatchTaskException;

}
