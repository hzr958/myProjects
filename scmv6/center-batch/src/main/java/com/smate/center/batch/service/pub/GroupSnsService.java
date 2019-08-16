package com.smate.center.batch.service.pub;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.GroupPsn;

/**
 * 群组同步接口.
 * 
 * @author zhuagnyanming
 * 
 */
public interface GroupSnsService extends Serializable {


  /**
   * 同步群组信息到群组成员.
   * 
   * @param groupPsn
   * @param psnList
   * @throws ServiceException
   */
  public void syncGroupInvitePsn(GroupPsn groupPsn, List<Long> psnList) throws ServiceException;

}
