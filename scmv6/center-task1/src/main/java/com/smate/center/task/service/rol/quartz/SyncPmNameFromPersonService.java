package com.smate.center.task.service.rol.quartz;

import java.util.List;

import com.smate.center.task.model.pdwh.quartz.TmpTaskInfoRecord;

public interface SyncPmNameFromPersonService {

  /**
   * 根据类型填充数据
   * 
   * @param psnId
   * @param type
   */

  public void startFillData(TmpTaskInfoRecord job) throws Exception;

  /**
   * 获取需要同步的记录
   * 
   * @param size
   * @return
   * @throws Exception
   */
  public List<TmpTaskInfoRecord> batchGetNeedSyncData(Integer size) throws Exception;

  public void updateTaskStatus(Long jobId, int status, String errmsg);

}
