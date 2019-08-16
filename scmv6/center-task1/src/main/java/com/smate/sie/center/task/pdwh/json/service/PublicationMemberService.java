package com.smate.sie.center.task.pdwh.json.service;

import com.smate.center.task.exception.ServiceException;
import com.smate.sie.core.base.utils.pub.service.PubJsonDTO;

/**
 * 成果成员服务接口
 * 
 * @author ZSJ
 *
 * @date 2019年2月20日
 */
public interface PublicationMemberService {

  public void savePubMember(PubJsonDTO pubJson) throws ServiceException;

  public void deletePubMemberByPubId(PubJsonDTO pubJson) throws ServiceException;
}
