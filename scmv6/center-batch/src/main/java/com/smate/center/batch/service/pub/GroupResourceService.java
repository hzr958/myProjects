package com.smate.center.batch.service.pub;

import java.io.Serializable;

import com.smate.center.batch.exception.pub.ServiceException;

/**
 * 群组资源接口.
 * 
 * @author zhuagnyanming
 * 
 */
public interface GroupResourceService extends Serializable {

  /**
   * 统计群组成果.
   * 
   * @param groupId
   * @throws ServiceException
   */
  public void reCountGroupPubs(Long groupId) throws ServiceException;

  /**
   * 统计群组文献.
   * 
   * @param groupId
   * @throws ServiceException
   */
  public void reCountGroupRefs(Long groupId) throws ServiceException;

}
