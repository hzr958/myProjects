package com.smate.web.management.service.psn;

import com.smate.core.base.utils.model.cas.security.User;



/**
 * 系统用户服务接口
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public interface UserService {

  String getLoginNameById(Long personId);

  User getpsnIdByEmail(String mergeCount);

}
