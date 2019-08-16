package com.smate.web.dyn.service.statistics;

import java.util.Map;

import com.smate.core.base.utils.exception.DynException;
import com.smate.web.dyn.form.dynamic.DynamicForm;

/**
 * 初始化动态统计service接口
 * 
 * @author lhd
 *
 */
public interface DynStatisticsService {

  public Map<String, Map<String, Object>> getDynStatistics(String des3ids, Long psnId) throws DynException;

  /**
   * 获取动态资源是否收藏
   * 
   * @param des3ids
   * @param psnId
   * @return
   * @throws DynException
   */
  public Map<String, Object> getDynCollect(String des3ids, Long psnId) throws DynException;

  public void updateShareCount(DynamicForm form) throws DynException;

  public String getPubOwner(Long pubId) throws DynException;
}
