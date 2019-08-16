package com.smate.web.psn.service.keyword;

import com.smate.web.psn.exception.ServiceException;

/**
 * 关键词基础服务类
 *
 * @author wsn
 * @createTime 2017年6月21日 下午2:33:18
 *
 */
public interface BaseDiscKeyService {

  /**
   * 删除相同关键词投票记录
   * 
   * @param psnId
   * @param friendPsnId
   * @throws ServiceException
   */
  public void deleteSameVoteRecord(Long psnId, Long friendPsnId) throws ServiceException;
}
