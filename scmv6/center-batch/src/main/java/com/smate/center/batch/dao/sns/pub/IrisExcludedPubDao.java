package com.smate.center.batch.dao.sns.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.IrisExcludedPub;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 
 * @author pwl
 * 
 */
@Repository
public class IrisExcludedPubDao extends SnsHibernateDao<IrisExcludedPub, Long> {

  public void deleteIrisExcludedPub(String uuid) throws DaoException {
    super.createQuery("delete from IrisExcludedPub t where t.uuid=?", uuid).executeUpdate();
  }
}
