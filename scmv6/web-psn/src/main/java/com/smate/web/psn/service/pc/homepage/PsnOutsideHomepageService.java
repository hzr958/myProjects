package com.smate.web.psn.service.pc.homepage;

import com.smate.web.psn.model.homepage.PersonProfileForm;

/**
 * @description 查看是否有合并账号服务类
 * @author xiexing
 * @date 2019年3月12日
 */
public interface PsnOutsideHomepageService {

  /**
   * 校验是否有合并账号
   * 
   * @param form
   * @return
   */
  public boolean checkHasMerge(PersonProfileForm form);
}
