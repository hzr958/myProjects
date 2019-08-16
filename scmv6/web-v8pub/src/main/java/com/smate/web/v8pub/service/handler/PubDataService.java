package com.smate.web.v8pub.service.handler;

import com.smate.web.v8pub.exception.PubHandlerException;
import com.smate.web.v8pub.exception.ServiceException;

/**
 * 成果数据服务
 * 
 * @author tsz
 *
 * @date 2018年6月6日
 */
public interface PubDataService {

  /**
   * 根据类型调用不一样的服务处理
   * 
   * @return String json
   * @throws PubHandlerException
   */
  String pubHandleByType(PubDTO pub) throws ServiceException;
}
