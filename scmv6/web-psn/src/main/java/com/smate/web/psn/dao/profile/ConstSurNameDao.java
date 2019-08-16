package com.smate.web.psn.dao.profile;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.profile.ConstSurName;

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
