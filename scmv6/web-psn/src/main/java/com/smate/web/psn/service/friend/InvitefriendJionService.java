package com.smate.web.psn.service.friend;

import java.util.List;
import java.util.Map;

import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.friend.InviteJionForm;

/**
 * 群组-成员-邀请成员加入接口
 * 
 * @author lhd
 *
 */
public interface InvitefriendJionService {

  /**
   * 群组-成员-邀请好友加入
   * 
   * @param form
   * @throws ServiceException
   */
  void getFriendJionList(InviteJionForm form) throws Exception;

  /**
   * 通过检索获取好友名字
   * 
   * @param form
   * @throws Exception
   */
  List<Map<String, Object>> getFriendNames(InviteJionForm form) throws Exception;

  /**
   * 获取好友名字
   * 
   * @param form
   * @throws Exception
   */
  void getMyFriendNames(InviteJionForm form) throws Exception;

}
