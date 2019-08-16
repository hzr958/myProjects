package com.smate.center.task.service.algorithm;

import java.util.List;

import com.smate.center.task.exception.ServiceException;


/**
 * 推荐算法-加分条件
 * 
 * @author lichangwen
 * 
 */
public interface PlusService {

  /**
   * 加分条件
   * 
   * @param id {psnId或pubId}
   * @param list 满足必要条件的结果集
   * @param kwList 初始传入的关键词集
   * @return
   * @throws ServiceException
   */
  List<?> complex(Long id, List<?> list, List<?> kwList) throws ServiceException;

}
