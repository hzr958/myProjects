package com.smate.sie.center.task.dao;



import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.InsGuid;



/**
 * ins_guid.
 * 
 * @author ztt
 * 
 */
@Repository
public class InsGuidDao extends SieHibernateDao<InsGuid, Long> {

  @SuppressWarnings("unchecked")
  public List<InsGuid> queryNeedRefresh(int maxSize) throws DaoException {
    return super.createQuery("from InsGuid t where t.guid is null").setMaxResults(maxSize).list();
  }

  public Long queryInsIdByGuid(String guid) throws DaoException {

    return (Long) super.createQuery("select t.insId from InsGuid t where t.guid = ?", guid).uniqueResult();
  }

  public String queryGuidByInsId(Long insId) throws DaoException {

    return (String) super.createQuery("select t.guid from InsGuid t where t.insId = ?", insId).uniqueResult();
  }
}
