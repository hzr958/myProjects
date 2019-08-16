package com.smate.web.group.service.group.homepage;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.group.dao.group.homepage.GroupHomePageDao;
import com.smate.web.group.model.group.homepage.GhpConfig;
import com.smate.web.group.model.group.homepage.GhpConfigElement;
import com.smate.web.group.model.group.homepage.GroupHomePageConfig;

/**
 * 
 * @author zjh
 */
@Transactional(rollbackFor = Exception.class)
@Service("groupHomePageService")
public class GroupHomePageServiceImpl implements GroupHomePageService {
  /**
   * 
   */

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GroupHomePageDao groupHomePageDao;
  @Autowired
  private GroupHomePageInitCloseProcess groupHomePageInitCloseProcess;
  @Autowired
  private GroupHomePageInitProcess groupHomePageInitProcess;

  @Override
  public GroupHomePageConfig getHomePageConfig(Long tmpId, Long groupId) throws Exception {

    try {
      GroupHomePageConfig homepageConfig = groupHomePageDao.getConfig(tmpId, groupId);
      GhpConfig config = null;
      // 如果未配置，则根据默认配置初始化
      if (homepageConfig == null) {
        homepageConfig = groupHomePageInitProcess.init(tmpId);
        config = homepageConfig.getConfig();
      } else {// 如果已配置
        // 如果用户关闭，站内用户或机构用户还能够看到基本信息
        if (0 == homepageConfig.getEnabled()) {
          config = groupHomePageInitCloseProcess.init(tmpId);
          // 未关闭
        } else {
          config = this.convertConfig(homepageConfig.getConfData());
        }
        homepageConfig.setConfig(config);
      }
      return homepageConfig;
    } catch (Exception e) {
      logger.error("getHomePageConfig错误:", e);
      throw new Exception(e);
    }
  }

  /**
   * 将配置信息从JSON数据转换成PO.
   * 
   * @param form
   * @param tmpMap
   * @param resumeconfig
   */
  @SuppressWarnings("rawtypes")
  public GhpConfig convertConfig(String gphConfig) {

    GhpConfig jObj = JacksonUtils.jsonObject(gphConfig, GhpConfig.class);
    if (jObj != null) {
      Map<String, Class> m = new HashMap<String, Class>();
      m.put("configElements", GhpConfigElement.class);
      // GhpConfig configObject = (GhpConfig) JSONObject.toBean(jObj,
      // GhpConfig.class, m);
      return jObj;
    }
    return null;
  }

}
