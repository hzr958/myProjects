package com.smate.web.v8pub.service.findduplicate;

import java.util.Map;
import com.smate.web.v8pub.exception.ServiceException;

/**
 * 成果查重 处理接口
 * 
 * @author tsz
 *
 * @date 2018年8月16日
 */
public interface DuplicateCheckHandlerService {

  /**
   * 执行查重业务方法
   * 
   * @param dup
   * @return
   * @throws DuplicateCheckParameterException
   */
  Map<String, String> excute(PubDuplicateDTO dup) throws ServiceException;

}
