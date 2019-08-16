package com.smate.center.batch.service.prj;

import com.smate.core.base.utils.model.security.Person;

public interface ProjectCommentService {

  /**
   * 更新项目评论信息人员信息.
   * 
   * @param message
   * @throws ServiceException
   */
  public void updatePsnInf(Person person);

}
