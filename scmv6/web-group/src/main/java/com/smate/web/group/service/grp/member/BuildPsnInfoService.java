package com.smate.web.group.service.grp.member;

import com.smate.web.group.model.group.psn.PsnInfo;

/**
 * 构造人员信息service
 * 
 * @author zzx
 */

public interface BuildPsnInfoService {
  /**
   * 构造人员信息
   * 
   * @param psnId
   * @param type
   * @return
   */
  PsnInfo buildPersonInfo(Long psnId, Integer type);
}
