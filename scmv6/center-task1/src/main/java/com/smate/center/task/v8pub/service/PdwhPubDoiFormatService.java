package com.smate.center.task.v8pub.service;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.v8pub.pdwh.po.PdwhDataTaskPO;

public interface PdwhPubDoiFormatService {

  /**
   * 格式化成果doi数据
   * 
   * @param pubData
   * @throws ServiceException
   */
  void formatPubDoi(PdwhDataTaskPO pubData) throws ServiceException;

}
