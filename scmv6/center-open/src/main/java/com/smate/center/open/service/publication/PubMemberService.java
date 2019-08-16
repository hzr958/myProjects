package com.smate.center.open.service.publication;

import java.io.Serializable;
import java.util.List;

import com.smate.center.open.model.publication.PubMember;

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
  List<PubMember> getPubMemberList(Long pubId) throws Exception;
}
