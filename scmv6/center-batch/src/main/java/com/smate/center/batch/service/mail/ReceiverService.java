package com.smate.center.batch.service.mail;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;

/**
 * 接收人获取辅助接口.
 * 
 * @author pwl
 * 
 */
public interface ReceiverService extends Serializable {
  /**
   * 从psnId:psnName,:email组成的字符串获取接收人psnId或者email.
   * 
   * @param receiversStr
   * @return
   * @throws ServiceException
   */
  @SuppressWarnings("rawtypes")
  Map<String, List> getReceivePsnIdAndEmail(String receiversStr) throws ServiceException;
}
