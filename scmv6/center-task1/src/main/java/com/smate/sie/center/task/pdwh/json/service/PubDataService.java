package com.smate.sie.center.task.pdwh.json.service;

import java.util.Map;

import com.smate.center.task.exception.ServiceException;
import com.smate.sie.core.base.utils.pub.service.PubJsonDTO;

/**
 * 成果数据服务
 * 
 * @author ZSJ
 *
 * @date 2019年2月11日
 */
public interface PubDataService {

  /**
   * 根据类型调用不一样的服务处理
   * 
   * @param pub
   * @return
   * @throws ServiceException
   */
  Map<String, Object> pubHandleByType(PubJsonDTO pub) throws ServiceException;
}
