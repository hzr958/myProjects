package com.smate.center.open.webservice;

import javax.jws.WebService;

/**
 * 第三方系统获取数据入口(webservice)
 * 
 * @author zk
 *
 */
@WebService(serviceName = "scmopenws", targetNamespace = "http://ws.server.iris.com",
    endpointInterface = "com.smate.center.open.webservice.ScmOpenDataWsService")
public interface ScmOpenDataWsService {

  /**
   * 获取开放数据
   * 
   * @param openData
   * @return
   */
  String getScmOpenData(String openId, String token, String data);

}
