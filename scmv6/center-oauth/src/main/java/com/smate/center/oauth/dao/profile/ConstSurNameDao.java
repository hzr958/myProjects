package com.smate.center.oauth.dao.profile;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.oauth.exception.DaoException;
import com.smate.center.oauth.model.profile.ConstSurName;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 复姓DAO.
 * 
 * @author zjh
 * 
 */
@Repository
public class ConstSurNameDao extends SnsHibernateDao<ConstSurName, Long> {
  /**
   * 判断常量类别是否已经存在.
   * 
   * @param id
   * @return
   * @throws DaoException
   */

  @SuppressWarnings("unchecked")
  public List<ConstSurName> findAllSurName() {
    String hql = "from ConstSurName t order by t.name";

    return super.createQuery(hql).list();
  }

}
