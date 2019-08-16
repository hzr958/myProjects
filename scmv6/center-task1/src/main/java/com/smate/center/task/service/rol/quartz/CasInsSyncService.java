package com.smate.center.task.service.rol.quartz;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.bpo.InsInfo;
import com.smate.core.base.utils.model.cas.security.User;

/**
 * 
 * 单位同步 cas 服务
 * 
 * @author hd
 *
 */
public interface CasInsSyncService {
  /**
   * 通过loginName获取user
   * 
   * @param email
   * @return
   */
  public User findUserByLoginName(String email);

  /**
   * 增加人员登陆账号.
   * 
   * @param info
   * @return
   * @throws ServiceException
   */
  Long addSysUser(InsInfo info) throws ServiceException;

  /**
   * 清除cas端脏数据.
   * 
   * @param psnId
   * @throws ServiceException
   */
  void deleteCas(Long psnId);

}
