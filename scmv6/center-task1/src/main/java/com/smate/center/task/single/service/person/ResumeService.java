package com.smate.center.task.single.service.person;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.smate.core.base.utils.exception.SysServiceException;

/**
 * 设置公开信息接口.
 * 
 * @author liqinghua
 * 
 */
/**
 * @author lichangwen
 * 
 */
public interface ResumeService extends Serializable {

  /**
   * 获取工作经历权限,默认公开.
   * 
   * @param workId
   * @return
   * @throws ServiceException
   */
  String getWorkAuthority(Long workId) throws SysServiceException;

}
