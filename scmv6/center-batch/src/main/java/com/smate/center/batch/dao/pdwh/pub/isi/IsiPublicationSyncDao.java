package com.smate.center.batch.dao.pdwh.pub.isi;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.pdwh.pub.isi.IsiPublicationSync;
import com.smate.core.base.utils.data.PdwhHibernateDao;


/**
 * 
 * @author pwl
 * 
 */
@Repository
public class IsiPublicationSyncDao extends PdwhHibernateDao<IsiPublicationSync, Long> {

  @SuppressWarnings("unchecked")
  public List<Long> queryNeedSyncData(int maxSize) throws DaoException {

    String hql = "select t.pubId from IsiPublicationSync t where t.status = 0";
    return (List<Long>) super.createQuery(hql).setMaxResults(maxSize).list();
  }

  /**
   * 删除同步信息.
   * 
   * @param pubIdList
   * @throws DaoException
   */
  public void deleteIsiPublicationSync(List<Long> pubIdList) throws DaoException {

    super.createQuery("delete from IsiPublicationSync t where t.pubId in(:pubIdList)")
        .setParameterList("pubIdList", pubIdList).executeUpdate();
  }
}
