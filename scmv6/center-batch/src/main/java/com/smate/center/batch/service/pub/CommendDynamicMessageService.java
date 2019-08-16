package com.smate.center.batch.service.pub;

import net.sf.json.JSONObject;

/**
 * 生成动态 共用方法 抽取类2015.1.12_scm-6454
 * 
 * @author tsz
 * 
 */
public interface CommendDynamicMessageService {

  /**
   * 生成动态公用入口.
   */
  void produceDynamic(JSONObject jsonObject, String dynJson, boolean extFlag);

}
