package com.smate.sie.core.base.utils.dao.insguid;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.insguid.SieInsGuid;

/**
 * ins_guid.
 * 
 * @author zhangtinggeng
 * 
 */
@Repository
public class SieInsGuidDao extends SieHibernateDao<SieInsGuid, Long> {

  @SuppressWarnings("unchecked")
  public List<SieInsGuid> queryNeedRefresh(int maxSize) {
    return super.createQuery("from InsGuid t where t.guid is null").setMaxResults(maxSize).list();
  }

  public Long queryInsIdByGuid(String guid) {
    return (Long) super.createQuery("select t.insId from InsGuid t where t.guid = ?", guid).uniqueResult();
  }

  public String queryGuidByInsId(Long insId) {
    return (String) super.createQuery("select t.guid from InsGuid t where t.insId = ?", insId).uniqueResult();
  }

  public void deleteByInsId(long mergeid) {
    String hql = "delete from InsGuid t where t.insId= ? ";
    super.createQuery(hql, mergeid).executeUpdate();
  }
}
