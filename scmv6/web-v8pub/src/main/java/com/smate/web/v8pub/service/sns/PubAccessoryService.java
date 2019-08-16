package com.smate.web.v8pub.service.sns;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubAccessoryPO;
import com.smate.web.v8pub.service.BaseService;

import java.util.List;

/**
 * 成果附件服务接口
 * 
 * @author aijiangbin
 * @date 2018年6月1日
 */
public interface PubAccessoryService extends BaseService<Long, PubAccessoryPO> {
  public PubAccessoryPO findByPubIdAndFileId(Long pubId, Long fileId) throws ServiceException;

  /**
   * 通过成果id 查找附近列表
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public List<PubAccessoryPO> findByPubId(Long pubId) throws ServiceException;

  /**
   * 通过pubId删除所有附件
   * 
   * @param pubId
   */
  public void deleteAll(Long pubId) throws ServiceException;
}
