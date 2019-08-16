package com.smate.center.task.v8pub.sorl.update;

import java.util.List;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.v8pub.pdwh.po.PdwhDataTaskPO;

public interface PdwhPubSorlService {

  /**
   * 获取需要更新的基准库任务记录
   * 
   * @param startId
   * @param endId
   * @param sIZE
   * @return
   */
  List<PdwhDataTaskPO> findPdwhId(Long startId, Long endId, Integer sIZE);

  /**
   * 执行更新基准库sorl
   * 
   * @param pubData
   */
  void updatePdwhPubSorl(PdwhDataTaskPO pubData) throws ServiceException;

  /**
   * 保存任务表信息
   * 
   * @param pubData
   */
  void save(PdwhDataTaskPO pubData);

}
