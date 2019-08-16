package com.smate.center.batch.service.application;

import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;

/**
 * 推荐算法-必要条件
 * 
 * @author lichangwen
 * 
 */
public interface RequirService {

  /**
   * 匹配方式
   * 
   * @param id {psnId或pubId}
   * @param kwList 初始传入的关键词集
   * @return
   * @throws ServiceException
   */
  List<?> matching(Long id, List<?> kwList) throws ServiceException;
}
