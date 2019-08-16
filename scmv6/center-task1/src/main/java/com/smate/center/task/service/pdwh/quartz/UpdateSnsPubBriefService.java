package com.smate.center.task.service.pdwh.quartz;

import java.util.List;

import com.smate.center.task.model.sns.quartz.TemTaskSnsBrief;

public interface UpdateSnsPubBriefService {
  /**
   * 获取需要更新的pubId
   * 
   * @param size
   * @param startPubId
   * @return
   * @throws Exception
   */
  public List<TemTaskSnsBrief> getUpdatePubIdList(Integer size) throws Exception;

  /**
   * 更新xml和pub表的brief来源字段
   * 
   * @param pubIdList
   * @return
   * @throws Exception
   */
  public void updateSnsPubBrie(List<TemTaskSnsBrief> pubList) throws Exception;

}
