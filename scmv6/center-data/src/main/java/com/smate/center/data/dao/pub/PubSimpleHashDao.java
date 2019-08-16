package com.smate.center.data.dao.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.data.model.pub.PubSimpleHash;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * V_PUB_SIMPLE_HASH表实体Dao
 * 
 * @author lxz
 * 
 */
@Repository
public class PubSimpleHashDao extends SnsHibernateDao<PubSimpleHash, Long> {

  /**
   * 获取指定数量的成果
   * 
   * @param size
   * @param startPubId
   * @param endPubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getPubSimpleHashList(Integer size, Long startPubId, Long endPubId) {
    String hql = "select t.pubId from PubSimpleHash t where t.pubId > :startId and t.pubId <= :endId order by t.pubId";
    return super.createQuery(hql).setParameter("startId", startPubId).setParameter("endId", endPubId)
        .setMaxResults(size).list();
  }

}
