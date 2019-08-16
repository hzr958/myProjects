package com.smate.web.dyn.service.dynamic;

import com.smate.core.base.utils.exception.DynException;
import com.smate.web.dyn.form.dynamic.DynamicForm;

/**
 * 实时动态服务类接口
 * 
 * @author zk
 *
 */
public interface DynamicRealtimeService {

  /**
   * 实时动态
   * 
   * @param form
   * @throws Exception
   */
  public void dynamicRealtime(DynamicForm form) throws DynException;
}
