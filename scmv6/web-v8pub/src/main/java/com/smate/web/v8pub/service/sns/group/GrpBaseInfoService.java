package com.smate.web.v8pub.service.sns.group;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.group.GrpBaseinfo;

/**
 * 群组基础信息服务类
 * 
 * @author YJ
 *
 *         2018年8月3日
 */
public interface GrpBaseInfoService {

  /**
   * 获取项目编号
   * 
   * @param grpId
   * @return
   * @throws ServiceException
   */
  public String getProjectNo(Long grpId) throws ServiceException;

  /**
   * 获取群组成果信息对象
   * 
   * @param grpId
   * @return
   * @throws ServiceException
   */
  public GrpBaseinfo getByGrpId(Long grpId) throws ServiceException;
}
