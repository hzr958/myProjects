package com.smate.center.oauth.dao.profile;

import org.springframework.stereotype.Repository;

import com.smate.center.oauth.exception.OauthException;
import com.smate.center.oauth.model.profile.Personal;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 人员专长数据层接口 PersonalDao
 * 
 * @author Administrator
 *
 */
@Repository
public class PsnDisciplineDao extends SnsHibernateDao<Personal, Long> {

  /**
   * 判断用户的课题专长是否存在.
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  public boolean isPsnDiscExit(Long psnId) throws OauthException {

    Long count = super.findUnique("select count(t.id.psnId) from PsnDiscipline t where t.id.psnId = ? ", psnId);
    if (count > 0) {
      return true;
    }
    return false;
  }
}
