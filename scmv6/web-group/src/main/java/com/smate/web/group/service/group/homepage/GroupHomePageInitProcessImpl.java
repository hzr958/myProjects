package com.smate.web.group.service.group.homepage;

import org.springframework.stereotype.Component;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.group.model.group.homepage.GhpConfig;
import com.smate.web.group.model.group.homepage.GhpConfigItem;
import com.smate.web.group.model.group.homepage.GroupHomePageConfig;

/**
 * 当用户未设置公开信息时，根据不同用户，初始化不同的公开信息.
 * 
 * @author liqinghua
 * 
 */
@Component("groupHomePageInitProcess")
public class GroupHomePageInitProcessImpl implements GroupHomePageInitProcess {

  /**
   * 入口，需传入相应的模板ID.
   * 
   * @param tmpId
   * @return
   */
  public GroupHomePageConfig init(long tmpId) {

    GroupHomePageConfig ghpConfig = new GroupHomePageConfig();
    ghpConfig.setTmpId(tmpId);
    GhpConfig config = null;
    // 1暂时只有1
    if (1 == tmpId) {
      config = this.defaultUser();
      ghpConfig.setEnabled(1);
    }
    ghpConfig.setConfig(config);
    if (config != null) {
      String tp = JacksonUtils.jsonObjectSerializer(config);
      ghpConfig.setConfData(tp);
    }
    return ghpConfig;
  }

  /**
   * 
   * 
   * 基本信息：不公开， 联系方式：默认不公开
   * 
   * 科研项目：默认不公开
   * 
   * 科研成果：默认不公开，科研文献：默认不公开
   * 
   * 文件：默认不公开.
   * 
   * @return
   */
  private GhpConfig defaultUser() {
    GhpConfig config = new GhpConfig();
    config.setBasic(new GhpConfigItem(1));
    config.setContact(new GhpConfigItem(0, 0, 1));
    config.setGpMember(new GhpConfigItem(0, 0, 2));
    config.setResearchPrj(new GhpConfigItem(0, 0, 3));
    config.setResearchPub(new GhpConfigItem(0, 0, 4));
    config.setResearchRef(new GhpConfigItem(0, 0, 5));
    config.setFile(new GhpConfigItem(0, 0, 6));
    return config;
  }

}
