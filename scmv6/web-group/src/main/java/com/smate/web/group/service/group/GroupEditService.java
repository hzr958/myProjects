package com.smate.web.group.service.group;

import com.smate.web.group.exception.GroupException;
import com.smate.web.group.model.group.GroupPsn;

public interface GroupEditService {

  /**
   * 编辑群组后保存.
   * 
   * @param groupPsn
   * @throws ServiceException
   */
  void updateGroupPsn(GroupPsn groupPsn) throws GroupException;

  /**
   * 刷新群组头像.
   * 
   * @param groupImgUrl
   * @return
   */
  String refreshRemoteAvatars(String groupImgUrl);

}
