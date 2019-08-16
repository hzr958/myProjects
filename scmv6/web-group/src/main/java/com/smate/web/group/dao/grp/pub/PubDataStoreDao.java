package com.smate.web.group.dao.grp.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.group.pub.PubDataStore;


@Repository
public class PubDataStoreDao extends SnsHibernateDao<PubDataStore, Long> {

  /**
   * 删除
   *
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param pubId
   */
  public void deleteData(Long pubId) {
    String hql = "delete from PubDataStore where pubId=?";
    super.createQuery(hql, pubId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<PubDataStore> getRefreshList(Integer size, Long lastPubId, Long startPubId, Long endPubId) {
    Long startId = startPubId > lastPubId ? startPubId : lastPubId;

    String hql = "from PubDataStore t where t.pubId>=? and t.pubId < ?";

    return super.createQuery(hql, startId, endPubId).setMaxResults(size).list();

  }
}
