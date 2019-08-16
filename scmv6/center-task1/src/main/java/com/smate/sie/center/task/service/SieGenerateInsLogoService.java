package com.smate.sie.center.task.service;

import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.rol.Sie6InsPortal;

/**
 * 批量生成单位默认LOGO接口
 * 
 * @author xr
 *
 */
public interface SieGenerateInsLogoService {

  /**
   * 找到单位域名信息中LOGO字段为空的单位域名
   * 
   * @param portalPage
   * @return
   */
  Page<Sie6InsPortal> findInsProtalByWithoutLogo(Page<Sie6InsPortal> portalPage);

  /**
   * 为单位生成默认LOGO
   * 
   * @param insId
   */
  public void GenerateInsLogo(Long insId);

}
