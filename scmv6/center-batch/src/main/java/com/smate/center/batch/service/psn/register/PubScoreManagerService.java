package com.smate.center.batch.service.psn.register;

import java.util.Map;

/**
 * 计算人员对应成果的匹配分数的业务逻辑接口.
 * 
 * @author mjg
 * 
 */
public interface PubScoreManagerService {

  /**
   * 处理新注册用户的成果匹配.
   * 
   */
  void dealExactMatchedPubScore(Map<String, Object> params);

}
