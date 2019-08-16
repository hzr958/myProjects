package com.smate.web.dyn.service.dynamic;

import com.smate.core.base.utils.exception.DynException;
import com.smate.web.dyn.form.dynamic.DynamicForm;

public interface DynamicQuickShareService {

  /**
   * 快速分享
   * 
   * @throws Exception
   * @return
   * 
   */
  public void quickShare(DynamicForm form) throws DynException;



}
