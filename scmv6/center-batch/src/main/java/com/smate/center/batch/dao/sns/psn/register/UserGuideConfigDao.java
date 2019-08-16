package com.smate.center.batch.dao.sns.psn.register;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.psn.register.UserGuideConfig;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 新手指南用户配置信息DAO.
 * 
 * @author liangguokeng
 * 
 */
@Repository
public class UserGuideConfigDao extends SnsHibernateDao<UserGuideConfig, Long> {

  public UserGuideConfig findUserGuideConfig(Long psnId) throws DaoException {
    String hql = "from UserGuideConfig t where t.psnId=? ";
    return findUnique(hql, psnId);
  }
}
