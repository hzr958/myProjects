package com.smate.center.batch.service.pub;

import java.io.Serializable;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.pub.mq.RcmdSyncFlagMessage;

/**
 * 推荐信息同步服务.
 * 
 * @author lqh
 * 
 */
public interface RcmdSyncDBService extends Serializable {

  /**
   * 保存刷新结果.
   * 
   * @param msg
   * @throws ServiceException
   */
  public void saveSyncInfo(RcmdSyncFlagMessage msg) throws ServiceException;

  /**
   * 群组信息和成员变动，同步到推荐服务
   * 
   * @author zzx
   * @param flag
   * @throws ServiceException
   */
  void saveRcmdSyncInfo(RcmdSyncFlagMessage flag) throws ServiceException;


}
