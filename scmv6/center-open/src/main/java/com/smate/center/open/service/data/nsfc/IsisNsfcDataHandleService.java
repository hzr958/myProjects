package com.smate.center.open.service.data.nsfc;

import java.util.Map;

/**
 * isis成果在线 具体服务接口
 * 
 * @author tsz
 *
 */
public interface IsisNsfcDataHandleService {


  /**
   * isis成果在线 服务总入口
   * 
   * @param map
   * @return
   */
  public String handleIsisData(Map<String, Object> dataParamet);

}
