package com.smate.center.open.service.interconnection.pub;

import java.util.Map;

import com.smate.core.base.pub.vo.PubDetailVO;

/**
 * u83nu2n0 服务调用的成果接口
 * 
 * @author Ai Jiangbin
 * 
 * @creation 2017年10月30日
 */
public interface OldIsisPubService {

  /**
   * xml 数据 解析成 map对象
   * 
   * @param pubXml
   * @return
   */
  public Map<String, Object> parseXmlToMap(PubDetailVO pubDetailVO) throws Exception;

}
