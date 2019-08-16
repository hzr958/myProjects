package com.smate.web.psn.service.pub;

import java.io.Serializable;
import java.util.List;

import com.smate.core.base.pub.model.PubMemberPO;
import com.smate.web.psn.exception.ServiceException;

/**
 * 成果作者服务.
 * 
 * 
 */
public interface PubMemberService extends Serializable {

  /**
   * 获取成果作者列表.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  List<PubMemberPO> getPubMemberList(Long pubId) throws Exception;
}
