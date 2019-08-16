package com.smate.web.psn.service.brief;

import com.smate.web.psn.exception.PsnException;

/**
 * 个人简介 服务
 * 
 * @author tsz
 *
 */
public interface PsnBriefService {

  /**
   * 根据人员id获取 个人简介
   * 
   * @param psnId
   * @return
   * @throws PsnException
   */
  public String getPsnBrief(Long psnId) throws PsnException;

}
