package com.smate.web.group.dao.group.homepage;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.group.homepage.GroupHomePageConfig;

/**
 * 
 * @author zjh
 * 
 */

@Repository
public class GroupHomePageDao extends SnsHibernateDao<GroupHomePageConfig, Long> {

  /**
   * 根据具体的模板ID与groupId，获取群组的配置信息.
   * 
   * @param tmpId
   * @param groupId
   * @return
   * @throws DaoException
   */
  public GroupHomePageConfig getConfig(Long tmpId, Long groupId) throws Exception {
    return super.findUnique("from GroupHomePageConfig where groupId = ? and tmpId = ? ", groupId, tmpId);

  }

}
