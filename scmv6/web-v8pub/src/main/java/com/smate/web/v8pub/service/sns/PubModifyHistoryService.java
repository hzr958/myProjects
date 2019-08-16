package com.smate.web.v8pub.service.sns;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubModifyHistory;

/**
 * 个人库成果修改历史记录 服务接口
 * 
 * @author yhx
 *
 */
public interface PubModifyHistoryService {
  /**
   * 根据pubId、psnId查找最新的历史修改记录
   * 
   * @param pubId
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public PubModifyHistory findListByPubIdAndPsnId(Long pubId, Long psnId) throws ServiceException;

  /**
   * 保存记录
   * 
   * @param pubModifyHistory
   * @throws ServiceException
   */
  public void savePubModifyHistory(PubModifyHistory pubModifyHistory) throws ServiceException;
}
