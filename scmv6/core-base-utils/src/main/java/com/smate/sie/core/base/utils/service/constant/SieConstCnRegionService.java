package com.smate.sie.core.base.utils.service.constant;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.model.consts.SieConstRegion;

/**
 * 中国省市常量service.
 * 
 * @author hd
 *
 */
public interface SieConstCnRegionService {
  /**
   * 获取单个数据.
   * 
   * @param name
   * @return
   * @throws SysServiceException
   */
  public SieConstRegion getCityByName(String name) throws SysServiceException;

}
