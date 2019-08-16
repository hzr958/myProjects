package com.smate.center.batch.service.rol.pub;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.pub.mq.PubAssignMessage;


/**
 * 成果指派服务.
 * 
 * @author yamingd
 * 
 */
public interface PubRolAssignService {

  /**
   * 按成果指派人员.
   * 
   * @param insPubId
   * @param insId
   * @throws ServiceException
   */
  void doAssignPub(PubAssignMessage message) throws ServiceException;
}
