package com.smate.web.psn.service.friend;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 好友相关的邮件发送业务逻辑实现类.
 * 
 * @author maojianguo
 * 
 */
@Service("friendMailService")
@Transactional(rollbackFor = Exception.class)
public class FriendMailServiceImpl implements FriendMailService {


}
