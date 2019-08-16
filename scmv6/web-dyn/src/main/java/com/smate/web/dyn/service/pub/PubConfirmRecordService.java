package com.smate.web.dyn.service.pub;

import com.smate.core.base.utils.exception.DynException;
import com.smate.web.dyn.model.pub.PubConfirmRecord;

public interface PubConfirmRecordService {

  /**
   * 获取成果认领记录
   * 
   * @param snsPubId
   * @return
   * @throws ServiceException
   */
  public PubConfirmRecord getRecordBySnsPubId(Long snsPubId) throws DynException;

}
