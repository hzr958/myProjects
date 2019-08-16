package com.smate.web.v8pub.service.sns;

import com.smate.web.v8pub.exception.ServiceException;

/**
 * 成果修改 接口
 * 
 * @author aijiangbin
 * @date 2018年6月1日
 */
public interface PubModifyService {
  /**
   * 添加一条修改记录
   * 
   * @param pubId
   * @param psnId
   * @param desc
   * @throws ServiceException
   */
  public void addPubModifyRecord(Long pubId, Long psnId, String desc) throws ServiceException;

}
