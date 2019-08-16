package com.smate.sie.center.task.service;

import java.util.List;

import com.smate.sie.center.task.model.SiePubSyncFulltextRefresh;

/***
 * 同步SNS在SIE库中的成果全文，现在包含有基准库成果和动态成果
 * 
 * @author yxy
 * @Date 201903
 */
public interface SieSynSnsPubFulltextService {

  /**
   * 获取需要更新的条目
   */
  int getMaxDynPubNum();

  /**
   * 根据参数获取刷新表中的数据
   */
  List<SiePubSyncFulltextRefresh> syncSnsPubOnSie(int firstPage, long batchSize);

  /**
   * 比较全文更新时间，同步全文消息至sie中
   */
  void synNewFulltext(SiePubSyncFulltextRefresh siePubSyncFulltextRefresh);

  void syncSnsPubToSie(long batchSize);


}
