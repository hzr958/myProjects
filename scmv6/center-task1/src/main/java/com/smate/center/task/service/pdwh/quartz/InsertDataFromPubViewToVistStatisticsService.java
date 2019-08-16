package com.smate.center.task.service.pdwh.quartz;

import java.util.List;

import com.smate.center.task.v8pub.sns.po.PubViewPO;

public interface InsertDataFromPubViewToVistStatisticsService {
  /**
   * 执行数据插入
   */
  public void doInsertData(PubViewPO pubViewPO);

  /**
   * 获取所有V_PubView表中数据的数量
   * 
   * @return
   */
  public Long getCountNum();

  /**
   * 从表中分段获取对应数据
   * 
   * @param start
   * @param size
   * @return
   */
  public List<PubViewPO> queryNeedInsertData(int start, int size);
}
