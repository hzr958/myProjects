package com.smate.center.open.service.pdwh.jnl;

import java.util.List;
import java.util.Map;

/**
 * 期刊级别SERVICE.
 * 
 * @author xys
 *
 */
public interface JnlLevelService {

  /**
   * 获取期刊级别统计.
   * 
   * @param jids
   * @return
   * @throws ServiceException
   */
  public Map<String, Object> getJnlLevelStats(List<Long> jids) throws Exception;

  /**
   * 获取期刊级别jids.
   * 
   * @param jids
   * @return
   * @throws ServiceException
   */
  public Map<String, Object> getJnlLevelJids(List<Long> jids) throws Exception;
}
