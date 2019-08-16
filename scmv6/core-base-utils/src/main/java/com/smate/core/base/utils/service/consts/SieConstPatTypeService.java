package com.smate.core.base.utils.service.consts;

import java.util.List;

import com.smate.core.base.utils.constant.SieConstPatType;
import com.smate.core.base.utils.exception.SysServiceException;

/**
 * 专利类型服务
 * 
 * @author hd
 *
 */
public interface SieConstPatTypeService {

  /***
   * 获取所有类型
   * 
   * @return
   */
  public List<SieConstPatType> getAllTypes() throws SysServiceException;

}
