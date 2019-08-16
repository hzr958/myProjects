package com.smate.core.base.psn.service.profile;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.psn.dto.profile.Personal;

/**
 * 个人专长、研究领域服务接口
 * 
 * @author houchuanjie
 * @date 2018年3月20日 上午9:41:42
 */
public interface PersonalService {
  /**
   * 获取个人专长，研究领域等个人信息
   *
   * @author houchuanjie
   * @date 2018年3月20日 上午9:42:43
   * @param psnId
   * @return
   * @throws ServiceException
   */
  Personal getPersonal(Long psnId) throws ServiceException;

  Long getInsIdByPsnId(Long psnId);
}
