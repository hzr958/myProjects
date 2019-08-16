package com.smate.center.batch.service.pub;

import net.sf.json.JSONObject;

import com.smate.center.batch.exception.pub.ServiceException;

/**
 * 个人动态服务接口.
 * 
 * @author mjg
 * 
 */
public interface DynamicProduceService {
  /**
   * 生成动态.
   * 
   * @param jsonParam
   * @throws ServiceException
   */
  void produceDynamic(String jsonParam);

  /**
   * 生成动态公共入口.
   * 
   * @param jsonObject
   * @param dynJson
   * @param extFlag
   * @throws ServiceException
   */
  void produceDynamic(JSONObject jsonObject, String dynJson, boolean extFlag);
}
