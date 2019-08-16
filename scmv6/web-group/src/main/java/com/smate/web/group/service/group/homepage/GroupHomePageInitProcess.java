package com.smate.web.group.service.group.homepage;

import com.smate.web.group.model.group.homepage.GroupHomePageConfig;


/**
 * 当群组未设置公开信息时，根据不同用户，初始化不同的公开信息.
 * 
 * @author liqinghua
 * 
 */
public interface GroupHomePageInitProcess {
  /**
   * 入口，需传入相应的模板ID.
   * 
   * @param tmpId
   * @return
   */
  GroupHomePageConfig init(long tmpId);

}
