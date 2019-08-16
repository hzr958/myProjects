package com.smate.sie.core.base.utils.service.ins;

import com.smate.core.base.utils.exception.SmateException;

/**
 * 机构类型service
 * 
 * @author xr
 *
 */
public interface SieConstInsTypeService {

  /*
   * 根据机构类型名称获取nature
   */
  Long getNatureByName(String zhName) throws SmateException;

}
