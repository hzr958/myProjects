package com.smate.web.dyn.service.dynamic;

import com.smate.core.base.utils.exception.DynException;
import com.smate.web.dyn.form.dynamic.DynamicForm;

/**
 * 
 * @author zjh 检查和生成数据的接口
 *
 */

public interface DynamicPrepareService {

  /**
   * 具体服务类各自实现-------进行各自的逻辑处理
   * 
   * @param parameterData
   * @return
   * @throws PsnBuildException
   */

  void checkAndDealData(DynamicForm form) throws DynException;
}
