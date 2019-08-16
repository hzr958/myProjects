package com.smate.web.group.service.group.homepage;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.smate.web.group.model.group.homepage.GhpConfig;


/**
 * 主页信息设置为关闭时，不同的模板也需要公开部分信息.
 * 
 * @author liqinghua
 * 
 */
@Component("groupHomePageInitCloseProcess")
public class GroupHomePageInitCloseProcessImpl implements Serializable, GroupHomePageInitCloseProcess {

  /**
   * 
   */
  private static final long serialVersionUID = -3609698950595188703L;

  /**
   * 入口，需传入相应的模板ID.
   * 
   * @param tmpId
   * @return
   */
  public GhpConfig init(long tmpId) {

    GhpConfig config = null;
    // 1暂时只有1
    if (1 == tmpId) {
      config = this.publicUser();
    }
    return config;
  }

  /**
   * 公众用户：完全不公开.
   * 
   * @return
   */
  private GhpConfig publicUser() {
    return null;
  }
}
