package com.smate.center.task.service.pdwh.quartz;

import java.io.Serializable;

import com.smate.center.task.exception.ServiceException;

/**
 * 成果基准库id.
 * 
 * @author liqinghua
 * 
 */
public interface PublicationPdwhService extends Serializable {


  /**
   * 获取psnId好友的论文数
   * 
   * @param psnId
   * @param pdwhPubId
   * @param dbid
   * @return
   * @throws ServiceException
   */
  int getPubPdwhIdByPsnFriend(Long psnId, Long pdwhPubId, int dbid) throws ServiceException;

}
