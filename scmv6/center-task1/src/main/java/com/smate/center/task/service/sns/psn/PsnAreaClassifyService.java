package com.smate.center.task.service.sns.psn;

import java.io.Serializable;
import java.util.List;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.psn.PsnAreaClassify;

/**
 * 人员领域大类，默认使用前两个项目大类，无项目情况下使用成果期刊大类.
 * 
 * @author liqinghua
 * 
 */
public interface PsnAreaClassifyService extends Serializable {

  /**
   * 获取人员大类.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<String> getPsnClassify(Long psnId) throws ServiceException;

}
