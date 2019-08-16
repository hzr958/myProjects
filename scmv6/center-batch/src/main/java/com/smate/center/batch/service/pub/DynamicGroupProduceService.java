package com.smate.center.batch.service.pub;

import net.sf.json.JSONObject;

import com.smate.center.batch.exception.pub.ServiceException;

/**
 * 群组动态生成服务接口_MJG_SCM-5012.
 * 
 * @author mjg
 * @since 2014-11-12
 */
public interface DynamicGroupProduceService {

  /**
   * 生成群组动态.
   * 
   * @param jsonParam
   * @throws ServiceException
   */
  void produceGroupDynamic(String jsonParam);

  /**
   * 生成群组动态公共入口.
   * 
   * @param jsonObject
   * @param dynJson
   * @param extFlag
   * @throws ServiceException
   */
  void produceGroupDynamic(JSONObject jsonObject, String dynJson, boolean extFlag);
}
