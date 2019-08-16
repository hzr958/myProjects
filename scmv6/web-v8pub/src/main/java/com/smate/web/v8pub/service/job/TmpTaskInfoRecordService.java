package com.smate.web.v8pub.service.job;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.job.TmpTaskInfoRecord;

/**
 * 重复成果分组任务实现类
 * 
 * @author YJ
 *
 *         2018年9月10日
 */
public interface TmpTaskInfoRecordService {

  /**
   * 通过psnId获取任务记录
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  TmpTaskInfoRecord getPsnRepeatPubRecord(Long psnId) throws ServiceException;

  /**
   * 保存任务记录
   * 
   * @param tmpTaskInfoRecord
   * @throws ServiceException
   */
  void saveOrUpdate(TmpTaskInfoRecord tmpTaskInfoRecord) throws ServiceException;

}
