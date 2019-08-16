package com.smate.web.group.service.group.homepage;

import com.smate.web.group.model.group.homepage.GhpConfig;

/**
 * 主页信息设置为关闭时，不同的模板也需要公开部分信息.
 * 
 * @author liqinghua
 * 
 */
public interface GroupHomePageInitCloseProcess {

  /**
   * 入口，需传入相应的模板ID.
   * 
   * @param tmpId
   * @return
   */
  GhpConfig init(long tmpId);
}
