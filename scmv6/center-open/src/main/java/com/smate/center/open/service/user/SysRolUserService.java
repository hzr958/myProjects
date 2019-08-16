package com.smate.center.open.service.user;


import com.smate.core.base.utils.model.cas.security.SysRolUser;

/**
 * open系统 通过juid取psnid服务 cas数据原
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public interface SysRolUserService {

  public SysRolUser getSysRolUserByGuid(String guid);
}
