package com.smate.center.batch.dao.rol.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.rol.pub.PubFulltextImageRefreshRol;
import com.smate.core.base.utils.data.RolHibernateDao;


/**
 * 成果全文转换图片刷新持久类.
 * 
 * @author pwl
 * 
 */
@Repository
public class PubFulltextImageRefreshRolDao extends RolHibernateDao<PubFulltextImageRefreshRol, Long> {

  /**
   * 批量查询需要刷新的数据.
   * 
   * @param startId
   * @param maxSize
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PubFulltextImageRefreshRol> queryRefreshDataBatch(int maxSize) throws DaoException {
    return super.createQuery("from PubFulltextImageRefreshRol t where t.status=?", new Object[] {0})
        .setMaxResults(maxSize).list();
  }
}
