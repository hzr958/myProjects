package com.smate.center.batch.dao.rol.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.rol.pub.PubFulltextRol;
import com.smate.core.base.utils.data.RolHibernateDao;


/**
 * 成果全文Dao.
 * 
 * @author pwl
 * 
 */
@Repository
public class PubFulltextRolDao extends RolHibernateDao<PubFulltextRol, Long> {

  /**
   * 获取成果全文图片.
   * 
   * @param pubId
   * @return
   * @throws DaoException
   */
  public String queryFulltextImagePath(Long pubId) throws DaoException {
    String fulltextImagePath =
        (String) super.createQuery("select t.fulltextImagePath from PubFulltext t where t.pubId=?", pubId)
            .uniqueResult();
    return fulltextImagePath;
  }
}
