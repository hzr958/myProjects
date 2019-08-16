package com.smate.center.task.service.pdwh.quartz;

import java.util.List;

import com.smate.center.task.model.pdwh.quartz.TemTaskPdwhBrief;

public interface UpdatePdwhPubBrieService {
  /**
   * 获取需要更新的pubId
   * 
   * @param size
   * @param startPubId
   * @return
   * @throws Exception
   */
  public List<TemTaskPdwhBrief> getUpdatePubIdList(Integer sizes) throws Exception;

  /**
   * 更新xml和pub表的brief来源字段
   * 
   * @param pubIdList
   * @return
   * @throws Exception
   */
  public void updatePdwhPubBrie(List<TemTaskPdwhBrief> pubList) throws Exception;
}
