package com.smate.web.v8pub.service.autocomplete;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.autocomplete.AcAwardIssueIns;
import com.smate.web.v8pub.service.BaseService;

/**
 * 颁奖机构
 * 
 * @author YJ
 *
 *         2018年10月16日
 */
public interface AcAwardIssueInsService extends BaseService<Long, AcAwardIssueIns> {

  /**
   * 通过name匹配颁奖机构
   * 
   * @param name
   * @return
   * @throws ServiceException
   */
  AcAwardIssueIns getByName(String name) throws ServiceException;

}
