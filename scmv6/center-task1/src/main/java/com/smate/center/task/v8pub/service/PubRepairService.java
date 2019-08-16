package com.smate.center.task.v8pub.service;

import java.util.List;

import com.smate.center.task.v8pub.pdwh.po.PdwhDataTaskPO;
import com.smate.center.task.v8pub.sns.po.PubDataTaskPO;

public interface PubRepairService {

  List<PubDataTaskPO> findPubId(Long startId, Long endId, Integer size);

  List<PdwhDataTaskPO> findPdwhId(Integer size, Long startId, Long endId);

  void save(PubDataTaskPO pubData);

  void save(PdwhDataTaskPO pubData);

  /**
   * 完善members中id的数据
   */
  void perferPdwhMemberId(PdwhDataTaskPO pdwhData);

  void perferSnsMemberId(PubDataTaskPO pubData);


  /**
   * 编目信息修复
   *
   * @param pubData
   */
  void rebuildPdwhBerifDesc(PdwhDataTaskPO pubData);

  void rebuildSnsBerifDesc(PubDataTaskPO pubData);


  /**
   * 重构publishDate字段
   *
   * @param pubData
   */
  void rebuildSnsPublishDate(PubDataTaskPO pubData);

  void rebuildPdwhPublishDate(PdwhDataTaskPO pubData);

  /**
   * 修复个人库收录情况数据
   * 
   * @param pubData
   */
  void rebuildPubSituation(PubDataTaskPO pubData);

  /**
   * 修复基准库期刊数据
   * 
   * @param pubData
   */
  void repairPdwhJournal(PdwhDataTaskPO pubData);

  /**
   * 修复个人库作者数据
   * 
   * @param pubData
   */
  void repairSnsPubMember(PubDataTaskPO pubData);

}
