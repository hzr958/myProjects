package com.smate.center.task.v8pub.service;

import java.util.List;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.v8pub.pdwh.po.PdwhDataTaskPO;

public interface PdwhAuthorNamesFormatService {

  /**
   * 获取需要处理的数据
   * 
   * @param startId
   * @param endId
   * @param sIZE
   * @return
   */
  List<PdwhDataTaskPO> findPdwhNeedDeal(Long startId, Long endId, Integer sIZE) throws ServiceException;

  /**
   * 保存处理结果信息
   * 
   * @param pubData
   */
  void save(PdwhDataTaskPO pubData) throws ServiceException;

  /**
   * 格式化成果作者名数据
   * 
   * @param pubData
   * @throws ServiceException
   */
  void formatAuthorNames(PdwhDataTaskPO pubData) throws ServiceException;

}
