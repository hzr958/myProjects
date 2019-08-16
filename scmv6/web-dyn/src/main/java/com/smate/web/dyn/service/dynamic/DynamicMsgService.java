package com.smate.web.dyn.service.dynamic;

import java.util.Map;

import com.smate.core.base.dyn.model.DynamicMsg;
import com.smate.core.base.utils.exception.DynException;

/**
 * 
 * @author zjh 成果动态service
 *
 */
public interface DynamicMsgService {
  public int getPubDynamicCount(Long psnId, Integer resType, String dynType) throws DynException;

  /**
   * 
   * @param mapData
   * @return
   * @throws DynException
   */
  public Boolean getPubPermit(Map<String, Object> mapData) throws DynException;

  /**
   * 通过动态id获取动态
   * 
   * @param dynId
   * @return
   * @throws Exception
   */
  public DynamicMsg getById(Long dynId) throws Exception;

}
