package com.smate.center.batch.service.pdwh.pubmatch;

import java.io.Serializable;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.pub.mq.InsAliasSyncMessage;

/**
 * 单位别名同步服务.
 * 
 * @author liqinghua
 * 
 */
public interface InsNameSyncService extends Serializable {

  /**
   * 同步单位别名.
   * 
   * @param msg
   * @throws ServiceException
   */
  public void syncInsName(InsAliasSyncMessage msg) throws ServiceException;
}
