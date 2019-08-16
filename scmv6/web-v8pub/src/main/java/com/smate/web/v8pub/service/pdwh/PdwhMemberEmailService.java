package com.smate.web.v8pub.service.pdwh;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhMemberEmailPO;
import com.smate.web.v8pub.service.BaseService;

import java.util.List;

/**
 * 基准库成果作者邮件服务
 * 
 * @author YJ
 *
 *         2019年1月8日
 */

public interface PdwhMemberEmailService extends BaseService<Long, PdwhMemberEmailPO> {

  /**
   * 删除指定基准库id的作者邮件信息
   * 
   * @param pdwhPubId
   * @throws ServiceException
   */
  void deleteAll(Long pdwhPubId) throws ServiceException;

  List<PdwhMemberEmailPO> findByPubId(Long pdwhPubId) throws ServiceException;

}
