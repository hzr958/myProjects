package com.smate.center.task.v8pub.service;

import java.util.List;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.v8pub.pdwh.po.PdwhDataTaskPO;
import com.smate.center.task.v8pub.sns.po.PubDataTaskPO;

public interface PubHandleService {
  /**
   * 获取需要指定数量需要处理的pubId
   * 
   * @param sIZE
   * @return
   */
  List<PubDataTaskPO> findPubNeedDeal(Long startId, Long endId, Integer sIZE);

  /**
   * 获取需要指定数量需要处理的pubId
   * 
   * @param sIZE
   * @return
   */
  List<PdwhDataTaskPO> findPdwhNeedDeal(Long startId, Long endId, Integer sIZE);

  /**
   * 成果数据的任务处理
   * 
   * @param pubList
   */
  void handlePub(PubDataTaskPO pubData) throws ServiceException;

  /**
   * 成果数据的任务处理
   * 
   * @param pubList
   */
  void handlePdwh(PdwhDataTaskPO pubData) throws ServiceException;

  /**
   * 处理基准库成果作者表
   * 
   * @param pubData
   */
  void handlePdwhMember(PdwhDataTaskPO pubData);

}
