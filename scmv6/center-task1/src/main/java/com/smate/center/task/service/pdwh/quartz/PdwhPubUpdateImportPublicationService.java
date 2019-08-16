package com.smate.center.task.service.pdwh.quartz;

import java.util.List;

import com.smate.center.task.v8pub.pdwh.po.PubPdwhPO;

public interface PdwhPubUpdateImportPublicationService {


  /**
   * 删除主表中状态为1的成果
   */
  void deleteNotExist();

  List<PubPdwhPO> getUpdatePdwhMonth(Integer startIndex, Integer size);

  void savePdwhIndexPublication(PubPdwhPO pubPdwhPO);

}
