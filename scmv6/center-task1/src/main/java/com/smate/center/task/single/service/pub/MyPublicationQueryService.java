package com.smate.center.task.single.service.pub;

import com.smate.center.task.exception.ServiceException;
import com.smate.core.base.psn.model.PsnStatistics;

/**
 * 我的成果管理查询专用服务.
 * 
 * @author yamingd
 */
public interface MyPublicationQueryService {

  PsnStatistics getPsnStatistics(Long psnId);

  /**
   * 统计某个人员成果引用总数.
   * 
   * @return
   * @throws ServiceException
   */
  Integer getTotalCiteTimes(Long psnId) throws ServiceException;

  /**
   * 得到某个人的h指数.
   * 
   * @return
   * @throws ServiceException
   */
  Integer getHindex(Long psnId) throws ServiceException;

  /**
   * 获取某个人的公开成果总数
   */
  Long getOpenPub(Long psnId) throws ServiceException;

}
