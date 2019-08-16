package com.smate.core.base.psn.service.vip;

import com.smate.core.base.exception.ServiceException;

/**
 * 验证人员是否VIP的服务类
 * 
 * @author YJ
 *
 *         2019年8月7日
 */
public interface PersonVipService {

  /**
   * app_type = 3，项目助理功能验证人员是否是VIP
   * 
   * @param psnId 人员的psnId
   * @return
   * @throws ServiceException
   */
  boolean checkVIPByPsnId(Long psnId) throws ServiceException;

}
