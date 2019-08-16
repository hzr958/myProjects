package com.smate.center.batch.dao.pdwh.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.PubAlldsSync;
import com.smate.core.base.utils.data.PdwhHibernateDao;


/**
 * 
 * @author cwli
 * 
 */
@Repository
public class PubAllIdsSyncDao extends PdwhHibernateDao<PubAlldsSync, Long> {

  public void savePubAlldsSync(Long pubId, Integer dbid) {
    super.save(new PubAlldsSync(pubId, dbid));
  }

  @SuppressWarnings("unchecked")
  public List<PubAlldsSync> batchSyncIds(int maxsize) {
    String hql = "from PubAlldsSync where status=?";
    return super.createQuery(hql, 0).setMaxResults(maxsize).list();
  }

  public void udpatePubAlldsSync(Long id, Integer status) {
    String hql = "update PubAlldsSync set status=? where id=?";
    super.createQuery(hql, status, id).executeUpdate();
  }

}
