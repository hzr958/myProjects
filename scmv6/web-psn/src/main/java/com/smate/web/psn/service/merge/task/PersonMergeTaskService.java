package com.smate.web.psn.service.merge.task;



import java.io.Serializable;

import com.smate.web.psn.exception.ServiceException;

/**
 * 人员合并服务.
 * 
 * @author liangguokeng
 * 
 */
public interface PersonMergeTaskService extends Serializable {


  /**
   * 保存需合并人员记录.
   * 
   * @param savePsnId 需要保留的人员ID.
   * @param delPsnId 需要删除的人员ID.
   */
  void saveMergePsn(Long savePsnId, Long delPsnId) throws ServiceException;
}
