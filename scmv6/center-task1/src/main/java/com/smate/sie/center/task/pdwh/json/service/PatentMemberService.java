package com.smate.sie.center.task.pdwh.json.service;

import com.smate.sie.core.base.utils.pub.exception.ServiceException;
import com.smate.sie.core.base.utils.pub.service.PubJsonDTO;

/**
 * 专利成员服务接口
 * 
 * @author lijianming
 * @date 2019年3月14日
 */
public interface PatentMemberService {

  /**
   * 删除专利成员
   * 
   * @param patJson
   * @throws ServiceException
   */
  public void deletePatMemberByPatId(PubJsonDTO patJson) throws ServiceException;

  /**
   * 保存专利成员
   * 
   * @param patJson
   * @throws ServiceException
   */
  public void savePatMember(PubJsonDTO patJson) throws ServiceException;
}
