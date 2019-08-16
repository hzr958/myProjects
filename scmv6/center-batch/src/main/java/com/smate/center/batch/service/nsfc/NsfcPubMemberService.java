package com.smate.center.batch.service.nsfc;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.model.sns.nsfc.NsfcPubMember;



/**
 * 成果作者服务.
 * 
 * 
 */
public interface NsfcPubMemberService extends Serializable {


  /**
   * 获取成果作者列表.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  List<NsfcPubMember> getNsfcPubMemberList(Long pubId) throws Exception;
}

