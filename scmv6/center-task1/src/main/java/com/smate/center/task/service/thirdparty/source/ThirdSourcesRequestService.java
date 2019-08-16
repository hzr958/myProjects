package com.smate.center.task.service.thirdparty.source;

import java.util.List;
import java.util.Map;

import com.smate.center.task.model.thirdparty.ThirdSources;
import com.smate.center.task.model.thirdparty.ThirdSourcesType;

/**
 * 第三方接口调用服务.
 * 
 * @author tsz
 *
 */
public interface ThirdSourcesRequestService {

  /**
   * 请求地址. 参数有 url 请求地址 type 请求类型 from_sys 系统标记 time_node 时间节点 key 校验码
   * 
   * @param ts
   * @return
   * @throws Exception
   */
  public List<Map<String, Object>> postUrl(ThirdSources ts, ThirdSourcesType tst);
}
