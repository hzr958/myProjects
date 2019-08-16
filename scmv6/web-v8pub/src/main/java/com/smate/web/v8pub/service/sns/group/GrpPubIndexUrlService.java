package com.smate.web.v8pub.service.sns.group;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.group.GrpPubIndexUrlPO;

/**
 * 群组成果短地址服务
 * 
 * @author YJ
 *
 *         2018年9月28日
 */
public interface GrpPubIndexUrlService {

  /**
   * 保存群组成果短地址
   * 
   * @param grpPubIndexUrlPO
   * @throws ServiceException
   */
  void saveOrUpdate(GrpPubIndexUrlPO grpPubIndexUrlPO) throws ServiceException;

  /**
   * 根据pubId和grpId获取群组成果短地址记录
   * 
   * @param pubId
   * @param grpId
   * @return
   */
  GrpPubIndexUrlPO get(Long pubId, Long grpId) throws ServiceException;
}
