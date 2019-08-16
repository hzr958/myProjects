package com.smate.center.task.v8pub.service;

import java.util.List;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.v8pub.pdwh.po.PdwhDataTaskPO;

public interface PdwhPubRepeatService {

  /**
   * 获取处理数据
   * 
   * @param size
   * @param startId
   * @param endId
   * @return
   */
  List<PdwhDataTaskPO> findPdwhId(Integer size, Long startId, Long endId) throws ServiceException;

  /**
   * 保存数据
   * 
   * @param pubData
   */
  void save(PdwhDataTaskPO pubData) throws ServiceException;

  /**
   * 处理重复成果分组
   * 
   * @param pubData
   */
  void dealWithRepeatPub(PdwhDataTaskPO pubData) throws ServiceException;

}
