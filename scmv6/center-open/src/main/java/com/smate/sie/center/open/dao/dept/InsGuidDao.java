package com.smate.sie.center.open.dao.dept;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.sie.center.open.model.dept.InsGuid;

/**
 * ins_guid.
 * 
 * 
 */
@Repository
public class InsGuidDao extends RolHibernateDao<InsGuid, Long> {

  @SuppressWarnings("unchecked")
  public List<InsGuid> queryNeedRefresh(int maxSize) throws Exception {
    return super.createQuery("from InsGuid t where t.guid is null").setMaxResults(maxSize).list();
  }

  public Long queryInsIdByGuid(String guid) throws Exception {

    return (Long) super.createQuery("select t.insId from InsGuid t where t.guid = ?", guid).uniqueResult();
  }

  public String queryGuidByInsId(Long insId) throws Exception {

    return (String) super.createQuery("select t.guid from InsGuid t where t.insId = ?", insId).uniqueResult();
  }
}
