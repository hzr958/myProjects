package com.smate.web.dyn.service.dynamic;

import com.smate.core.base.utils.exception.DynException;
import com.smate.web.dyn.form.dynamic.DynamicForm;

/**
 * 
 * @author zjh 动态模板数据的检查和生成
 *
 */

public interface DynamicDataDealService {


  /**
   * 具体服务类各自实现-------校验必要的参数
   * 
   * @param parameterData
   * @return
   */
  public String checkData(DynamicForm form);

  /**
   * 具体服务类各自实现-------进行各自的逻辑处理
   * 
   * @param parameterData
   * @return
   * @throws DynException
   * @throws PsnBuildException
   */

  public void dealData(DynamicForm form) throws DynException;
}
