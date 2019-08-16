package com.smate.center.task.v8pub.service;

import java.util.List;

import com.smate.center.task.v8pub.pdwh.po.PdwhDataTaskPO;
import com.smate.center.task.v8pub.sns.po.PubDataTaskPO;

public interface DataTaskService {

  void save(PdwhDataTaskPO pdwhData);

  void save(PubDataTaskPO pubData);

  /**
   * 获取需要处理的数据
   * 
   * @param startId
   * @param endId
   * @param sIZE
   * @return
   */
  List<PdwhDataTaskPO> findPdwhNeedDeal(Long startId, Long endId, Integer sIZE);

  List<PubDataTaskPO> findNeedDeal(Long startId, Long endId, Integer sIZE);
}
