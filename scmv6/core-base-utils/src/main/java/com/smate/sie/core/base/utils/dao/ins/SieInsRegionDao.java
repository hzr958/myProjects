package com.smate.sie.core.base.utils.dao.ins;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.ins.SieInsRegion;

/**
 * 高校所在地Dao
 * 
 * @author hd
 *
 */
@Repository
public class SieInsRegionDao extends SieHibernateDao<SieInsRegion, Long> {

  @SuppressWarnings("unchecked")
  public List<SieInsRegion> getListByInsId(Long insId) {
    String hql = " from SieInsRegion t where t.insId= ? order by t.insId ";
    return super.createQuery(hql, insId).list();
  }

  public void deleteByInsId(Long mergeid) {
    String hql = "delete from SieInsRegion t where t.insId= ? ";
    super.createQuery(hql, mergeid).executeUpdate();
  }

}
