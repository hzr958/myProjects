package com.smate.center.batch.service.pub;

import com.smate.core.base.utils.model.security.Person;

public interface PublicationCommentService {

  /**
   * 更新项目评论信息人员信息.
   * 
   * @param message
   * @throws ServiceException
   */
  void syncPubCommentPsn(Person person);

}
