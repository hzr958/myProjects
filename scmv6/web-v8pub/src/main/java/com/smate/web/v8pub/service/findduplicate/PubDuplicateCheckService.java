package com.smate.web.v8pub.service.findduplicate;

import java.util.Map;

/**
 * 成果查重服务
 * 
 * @author tsz
 *
 * @date 2018年8月16日
 */
public interface PubDuplicateCheckService {

  /**
   * 处理查重
   * 
   * @param dup
   * @return
   */
  Map<String, String> dupCheck(PubDuplicateDTO dup);

}
