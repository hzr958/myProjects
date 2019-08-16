package com.smate.web.psn.service.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.smate.core.base.utils.model.security.Person;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.mailbox.InviteMailBox;
import com.smate.web.psn.model.message.Message;

/**
 * 消息服务类
 * 
 * @author oyh
 * 
 */
public interface MessageService {

  Map<String, Long> sendInviteMessage(Message message) throws ServiceException;

  /**
   * 同步站内好友邀请
   * 
   * @param inviteMailBox
   * @throws ServiceException
   */

}
