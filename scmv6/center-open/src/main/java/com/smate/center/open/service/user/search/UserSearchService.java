package com.smate.center.open.service.user.search;

import com.smate.core.base.consts.model.Institution;

/**
 * 检索用户服务
 * 
 * @author Administrator
 *
 */
public interface UserSearchService {

  /**
   * 更新保存用户检索表的单位记录.
   * 
   * @param psnId
   * @param ins
   */
  void updateUserIns(Long psnId, Institution ins);
}
