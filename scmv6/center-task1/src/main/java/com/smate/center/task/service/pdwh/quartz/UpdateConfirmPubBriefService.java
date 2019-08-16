package com.smate.center.task.service.pdwh.quartz;

import java.util.List;

import com.smate.center.task.model.rcmd.quartz.PubConfirmRolPub;

public interface UpdateConfirmPubBriefService {
  /**
   * 获取需要更新的pubId
   * 
   * @param size
   * @param startPubId
   * @return
   * @throws Exception
   */
  public List<PubConfirmRolPub> getUpdatePubIdList(Integer size, Long endPubId) throws Exception;

  /**
   * 更新xml和pub表的brief来源字段
   * 
   * @param pubIdList
   * @return
   * @throws Exception
   */
  public void updatePubConfirmBrie(List<PubConfirmRolPub> pubList) throws Exception;

}
